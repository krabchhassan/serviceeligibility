package com.cegedim.next.serviceeligibility.core.bdd.backend.service;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.COLLECTIVE_CONTRACT_DOUBLE_SEARCH_ENABLED;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ContractCollectiveDataDto;
import com.cegedim.next.serviceeligibility.core.dao.ContractDao;
import com.cegedim.next.serviceeligibility.core.dao.ServicePrestationDao;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratCollectifV6;
import com.cegedim.next.serviceeligibility.core.utils.ContextConstants;
import io.micrometer.tracing.annotation.ContinueSpan;
import org.springframework.stereotype.Service;

/** Classe d'accès aux services lies aux {@code ContractCollectiveData}. */
@Service("contractCollectiveDataService")
public class ContractCollectiveDataServiceImpl implements ContractCollectiveDataService {

  private final ServicePrestationDao servicePrestationDao;

  private final ContractDao contratDao;

  private final boolean isCollectiveContractDoubleSearchEnabled;

  public ContractCollectiveDataServiceImpl(
      ServicePrestationDao servicePrestationDao,
      ContractDao contratDao,
      BeyondPropertiesService beyondPropertiesService) {
    this.servicePrestationDao = servicePrestationDao;
    this.contratDao = contratDao;
    isCollectiveContractDoubleSearchEnabled =
        beyondPropertiesService
            .getBooleanProperty(COLLECTIVE_CONTRACT_DOUBLE_SEARCH_ENABLED)
            .orElse(Boolean.FALSE);
  }

  @Override
  @ContinueSpan(log = "findContractCollectiveData")
  public ContractCollectiveDataDto findContractCollectiveData(
      final String contractNumber,
      final String context,
      final String insurerId,
      final String subscriberId) {
    final ContractCollectiveDataDto collectiveDataDto;
    if (ContextConstants.HTP.equals(context)) {
      collectiveDataDto = getHtpContractCollectiveDataDto(contractNumber, insurerId, subscriberId);
    } else {
      final ContractTP contrat =
          this.contratDao.getContract(insurerId, contractNumber, subscriberId);
      if (contrat == null && isCollectiveContractDoubleSearchEnabled) {
        collectiveDataDto =
            getHtpContractCollectiveDataDto(contractNumber, insurerId, subscriberId);
      } else {
        collectiveDataDto = contrat != null ? this.mapContrat(contrat) : null;
      }
    }

    return collectiveDataDto;
  }

  private ContractCollectiveDataDto getHtpContractCollectiveDataDto(
      String contractNumber, String insurerId, String subscriberId) {
    final ContratAIV6 servicePrestation =
        this.servicePrestationDao.findServicePrestationByContractNumber(
            contractNumber, insurerId, subscriberId);
    return servicePrestation != null ? this.mapServicePrestation(servicePrestation) : null;
  }

  private ContractCollectiveDataDto mapServicePrestation(final ContratAIV6 servicePrestation) {
    final ContractCollectiveDataDto collectiveDataDto = new ContractCollectiveDataDto();
    final Boolean isContratIndividuel = servicePrestation.getIsContratIndividuel();
    collectiveDataDto.setIsIndividualContract(isContratIndividuel);
    if (Boolean.TRUE.equals(isContratIndividuel)
        || servicePrestation.getContratCollectif() == null) {
      collectiveDataDto.setCollectiveContractNumber(null);
    } else {
      ContratCollectifV6 contratCollectifV6 = servicePrestation.getContratCollectif();
      collectiveDataDto.setCollectiveContractNumber(contratCollectifV6.getNumero());
      collectiveDataDto.setSiret(contratCollectifV6.getSiret());
      collectiveDataDto.setPopulationGroup(contratCollectifV6.getGroupePopulation());
      collectiveDataDto.setCompanyName(contratCollectifV6.getRaisonSociale());
      collectiveDataDto.setCompanyId(contratCollectifV6.getIdentifiantCollectivite());
    }
    return collectiveDataDto;
  }

  private ContractCollectiveDataDto mapContrat(final ContractTP contrat) {
    final ContractCollectiveDataDto collectiveDataDto = new ContractCollectiveDataDto();
    final boolean isContratIndividuel = "1".equals(contrat.getIndividuelOuCollectif());
    collectiveDataDto.setIsIndividualContract(isContratIndividuel);
    if (isContratIndividuel) {
      collectiveDataDto.setCollectiveContractNumber(null);
    } else {
      collectiveDataDto.setCollectiveContractNumber(contrat.getNumeroContratCollectif());
      collectiveDataDto.setSiret(contrat.getSiret());
      collectiveDataDto.setPopulationGroup(contrat.getGroupePopulation());
      collectiveDataDto.setCompanyName(contrat.getRaisonSociale());
      collectiveDataDto.setCompanyId(contrat.getIdentifiantCollectivite());
    }
    return collectiveDataDto;
  }
}
