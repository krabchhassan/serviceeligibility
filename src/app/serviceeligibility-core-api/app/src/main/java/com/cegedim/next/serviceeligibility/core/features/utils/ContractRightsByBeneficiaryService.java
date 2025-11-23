package com.cegedim.next.serviceeligibility.core.features.utils;

import com.cegedim.next.serviceeligibility.core.dao.ContractBackendDao;
import com.cegedim.next.serviceeligibility.core.dto.ContractRightsByBeneficiaryRequestDto;
import com.cegedim.next.serviceeligibility.core.mapper.pau.MapperUniqueAccessPointServiceTP;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.pau.UniqueAccessPointServiceHTPImpl;
import com.cegedim.next.serviceeligibility.core.services.pau.UniqueAccessPointServiceV5HTPImpl;
import com.cegedim.next.serviceeligibility.core.services.pojo.ContractHTPForPau;
import com.cegedim.next.serviceeligibility.core.utils.ContextConstants;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.PwException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RestErrorConstants;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.UAPFunctionalException;
import com.cegedim.next.serviceeligibility.core.webservices.*;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractRightsByBeneficiaryService {
  private final ContractBackendDao contractBackendDao;
  private final MapperUniqueAccessPointServiceTP mapperUniqueAccessPointServiceTP;
  private final UniqueAccessPointTpOnlineSortRightsV5Impl uniqueAccessPointTpSortRights;
  private final ContractRightsByBeneficiaryTriHTPImpl contractRightsByBeneficiaryTriHTP;
  private final ContractRightsByBeneficiaryTriTPImpl contractRightsByBeneficiaryTriTP;
  private final UniqueAccessPointServiceV5HTPImpl uniqueAccessPointServiceHTP;

  public UniqueAccessPointResponse getContractRightsByBeneficiaryResponse(
      ContractRightsByBeneficiaryRequestDto crbRequest) {
    UniqueAccessPointResponse response = new UniqueAccessPointResponse();

    List<ContratAIV6> listContrats = new ArrayList<>();
    List<ContractTP> listContratsTP = new ArrayList<>();
    UniqueAccessPointRequestV5 requestV5 =
        new UniqueAccessPointRequestV5(
            crbRequest.getNir(),
            null,
            null,
            "0001-01-01",
            null,
            crbRequest.getInsurerId(),
            null,
            null,
            crbRequest.getContext(),
            null,
            null,
            null,
            null,
            true);

    processContracts(crbRequest, requestV5, listContrats, listContratsTP);

    requestV5.setContractNumber(null);
    List<GenericRightDto> resultsTP = new ArrayList<>();

    // sort
    if (CollectionUtils.isNotEmpty(listContratsTP)) {
      sortTPContracts(crbRequest, listContratsTP, requestV5, resultsTP);
    } else if (CollectionUtils.isNotEmpty(listContrats)) {
      // pareil pour HTP
      sortHTPContracts(crbRequest, listContrats, requestV5, resultsTP);
    }

    // On ne retourne pas la societeEmettrice
    resultsTP.forEach(right -> right.setIssuingCompanyCode(null));
    response.setContracts(resultsTP);
    return response;
  }

  private void sortHTPContracts(
      ContractRightsByBeneficiaryRequestDto crbRequest,
      List<ContratAIV6> listContrats,
      UniqueAccessPointRequestV5 requestV5,
      List<GenericRightDto> resultsTP) {
    List<ContractHTPForPau> contractHTPForPaus = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(listContrats)) {
      for (ContratAIV6 contract : listContrats) {
        ContractHTPForPau contractHTPForPau = new ContractHTPForPau();
        contractHTPForPau.setContratAIV6(contract);
        contractHTPForPau.setRetour(0);
        contractHTPForPaus.add(contractHTPForPau);
      }
    }
    List<ContratAIV6> contratAIV6s =
        UniqueAccessPointServiceHTPImpl.getContratAIV6s(contractHTPForPaus);

    contractRightsByBeneficiaryTriHTP.triHTP(contratAIV6s, requestV5);
    for (final ContratAIV6 contrat : contratAIV6s) {
      uniqueAccessPointServiceHTP.addMappedResult(
          requestV5, resultsTP, contrat, List.of(crbRequest.getPersonNumber()), false);
    }
  }

  private void sortTPContracts(
      ContractRightsByBeneficiaryRequestDto crbRequest,
      List<ContractTP> listContratsTP,
      UniqueAccessPointRequestV5 requestV5,
      List<GenericRightDto> resultsTP) {
    for (ContractTP contrat : listContratsTP) {
      try {
        mapperUniqueAccessPointServiceTP.addMappedResult(
            requestV5,
            resultsTP,
            contrat,
            ContextConstants.TP_OFFLINE.equals(crbRequest.getContext()),
            contrat.getIdDeclarant(),
            List.of(crbRequest.getPersonNumber()),
            false);
      } catch (PwException e) {
        throw new UAPFunctionalException(
            e.getMessage(),
            HttpStatus.NOT_FOUND,
            RestErrorConstants.ERROR_CODE_PRODUCT_WORKSHOP_ERROR);
      }
    }
    if (ContextConstants.TP_ONLINE.equals(crbRequest.getContext())
        || ContextConstants.TP_OFFLINE.equals(crbRequest.getContext())) {
      contractRightsByBeneficiaryTriTP.triTP(resultsTP, requestV5);
      for (GenericRightDto contract : resultsTP) {
        uniqueAccessPointTpSortRights.sort(contract.getInsured().getRights());
      }
    }
  }

  private void processContracts(
      ContractRightsByBeneficiaryRequestDto crbRequest,
      UniqueAccessPointRequestV5 requestV5,
      List<ContratAIV6> listContrats,
      List<ContractTP> listContratsTP) {
    List<ContractRightsByBeneficiaryRequestDto.ContractSubscriber> distinctContracts =
        crbRequest.getContractList().stream()
            .filter(Objects::nonNull)
            .filter(
                distinctByKey(
                    ContractRightsByBeneficiaryRequestDto.ContractSubscriber::contractNumber))
            .toList();

    for (ContractRightsByBeneficiaryRequestDto.ContractSubscriber contractSubscriber :
        distinctContracts) {
      requestV5.setContractNumber(contractSubscriber.contractNumber());
      requestV5.setSubscriberId(contractSubscriber.subscriberId());

      UniqueAccessPointTPRequestV5 uniqueAccessPointTPRequestV5 =
          new UniqueAccessPointTPRequestV5();
      uniqueAccessPointTPRequestV5.setNumeroPersonnes(List.of(crbRequest.getPersonNumber()));
      uniqueAccessPointTPRequestV5.setRequest(requestV5);

      List<BenefAIV5> benefs = new ArrayList<>();
      BenefAIV5 benef = new BenefAIV5();
      IdentiteContrat idContrat = new IdentiteContrat();
      idContrat.setNumeroPersonne(crbRequest.getPersonNumber());
      benef.setIdentite(idContrat);
      benefs.add(benef);

      if (ContextConstants.HTP.equals(crbRequest.getContext())) {
        listContrats.addAll(contractBackendDao.findContractsForHTP(requestV5, benefs));
      } else if (ContextConstants.TP_ONLINE.equals(crbRequest.getContext())) {
        contractBackendDao.findContractsForTPOnline(uniqueAccessPointTPRequestV5).stream()
            .filter(
                contrat ->
                    Objects.equals(contractSubscriber.contractNumber(), contrat.getNumeroContrat())
                        && Objects.equals(
                            contractSubscriber.subscriberId(), contrat.getNumeroAdherent()))
            .forEach(listContratsTP::add);
      } else if (ContextConstants.TP_OFFLINE.equals(crbRequest.getContext())) {
        contractBackendDao.findContractsForTPOffline(uniqueAccessPointTPRequestV5).stream()
            .filter(
                contrat ->
                    Objects.equals(contractSubscriber.contractNumber(), contrat.getNumeroContrat())
                        && Objects.equals(
                            contractSubscriber.subscriberId(), contrat.getNumeroAdherent()))
            .forEach(listContratsTP::add);
      }
    }
  }

  public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
    Set<Object> seen = ConcurrentHashMap.newKeySet();
    return t -> seen.add(keyExtractor.apply(t));
  }
}
