package com.cegedim.next.serviceeligibility.core.features.utils;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ContractDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.contract.NaturePrestationDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.consultation_droit.GetInfoBddRequestDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.exception.ExceptionServiceDroitNonOuvert;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ConventionnementContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PrestationContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PrioriteDroitContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.RemboursementContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConsultationDroitsUtils {
  public static GetInfoBddRequestDto getRequestBody(String request) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(request, GetInfoBddRequestDto.class);
  }

  public static void filterContractsPeriodsNotOverlappingRequest(
      List<ContractDto> contractDtoList, String requestStartDate, String requestEndDate) {
    final Iterator<ContractDto> contractDtoIterator = contractDtoList.iterator();
    ContractDto contractDto;
    while (contractDtoIterator.hasNext()) {
      contractDto = contractDtoIterator.next();
      removePeriodesDroitNotOverlapping(requestStartDate, requestEndDate, contractDto);
      removeNaturesPrestWithoutPeriodesDroit(contractDto);
      removeReferencesCouvWithoutNaturesPrest(contractDto);
      removeProduitsWithoutReferencesCouv(contractDto);
      removeGarantiesWithoutProduits(contractDto);
      // Remove DomaineDroits without garanties
      contractDto
          .getDomaineDroits()
          .removeIf(domaineDroit -> domaineDroit.getGaranties().isEmpty());

      if (CollectionUtils.isEmpty(contractDto.getDomaineDroits())) {
        // Remove Contract without domaineDroits
        contractDtoIterator.remove();
      } else {
        // Remove not overlapping subPeriods (remboursements, prestations,
        // prioritesDroit, conventionnements)
        removeSubPeriodsNotOverlapping(requestStartDate, requestEndDate, contractDto);
      }
    }
    if (contractDtoList.isEmpty()) {
      throw new ExceptionServiceDroitNonOuvert();
    }
  }

  private static void removeGarantiesWithoutProduits(ContractDto contractDto) {
    contractDto
        .getDomaineDroits()
        .forEach(
            domaineDroit ->
                domaineDroit.getGaranties().removeIf(garantie -> garantie.getProduits().isEmpty()));
  }

  private static void removeProduitsWithoutReferencesCouv(ContractDto contractDto) {
    contractDto.getDomaineDroits().stream()
        .flatMap(domaineDroit -> domaineDroit.getGaranties().stream())
        .forEach(
            garantie ->
                garantie
                    .getProduits()
                    .removeIf(produit -> produit.getReferencesCouverture().isEmpty()));
  }

  private static void removeReferencesCouvWithoutNaturesPrest(ContractDto contractDto) {
    contractDto.getDomaineDroits().stream()
        .flatMap(domaineDroit -> domaineDroit.getGaranties().stream())
        .flatMap(garantie -> garantie.getProduits().stream())
        .forEach(
            produit ->
                produit
                    .getReferencesCouverture()
                    .removeIf(
                        referenceCouverture ->
                            referenceCouverture.getNaturesPrestation().isEmpty()));
  }

  private static void removeNaturesPrestWithoutPeriodesDroit(ContractDto contractDto) {
    contractDto.getDomaineDroits().stream()
        .flatMap(domaineDroit -> domaineDroit.getGaranties().stream())
        .flatMap(garantie -> garantie.getProduits().stream())
        .flatMap(produit -> produit.getReferencesCouverture().stream())
        .forEach(
            referenceCouverture ->
                referenceCouverture
                    .getNaturesPrestation()
                    .removeIf(naturePrestation -> naturePrestation.getPeriodesDroit().isEmpty()));
  }

  private static void removePeriodesDroitNotOverlapping(
      String requestStartDate, String requestEndDate, ContractDto contractDto) {
    contractDto.getDomaineDroits().stream()
        .flatMap(domaineDroit -> domaineDroit.getGaranties().stream())
        .flatMap(garantie -> garantie.getProduits().stream())
        .flatMap(produit -> produit.getReferencesCouverture().stream())
        .flatMap(referenceCouverture -> referenceCouverture.getNaturesPrestation().stream())
        .forEach(
            naturePrestation ->
                naturePrestation
                    .getPeriodesDroit()
                    .removeIf(
                        periodeDroitContractTP ->
                            !DateUtils.isOverlapping(
                                periodeDroitContractTP.getPeriodeDebut(),
                                periodeDroitContractTP.getPeriodeFin(),
                                requestStartDate,
                                requestEndDate)));
  }

  private static void removeSubPeriodsNotOverlapping(
      String requestStartDate, String requestEndDate, ContractDto contractDto) {
    contractDto.getDomaineDroits().stream()
        .flatMap(domaineDroit -> domaineDroit.getGaranties().stream())
        .flatMap(garantie -> garantie.getProduits().stream())
        .flatMap(produit -> produit.getReferencesCouverture().stream())
        .forEach(
            referenceCouvertureDto -> {
              filterSubPeriods(
                  referenceCouvertureDto.getNaturesPrestation(),
                  NaturePrestationDto::getRemboursements,
                  RemboursementContrat::getPeriodes,
                  requestStartDate,
                  requestEndDate);
              filterSubPeriods(
                  referenceCouvertureDto.getNaturesPrestation(),
                  NaturePrestationDto::getPrestations,
                  PrestationContrat::getPeriodes,
                  requestStartDate,
                  requestEndDate);
              filterSubPeriods(
                  referenceCouvertureDto.getNaturesPrestation(),
                  NaturePrestationDto::getConventionnements,
                  ConventionnementContrat::getPeriodes,
                  requestStartDate,
                  requestEndDate);
              filterSubPeriods(
                  referenceCouvertureDto.getNaturesPrestation(),
                  NaturePrestationDto::getPrioritesDroit,
                  PrioriteDroitContrat::getPeriodes,
                  requestStartDate,
                  requestEndDate);
            });
  }

  private static <T> void filterSubPeriods(
      List<NaturePrestationDto> natures,
      Function<NaturePrestationDto, List<T>> getToMerge,
      Function<T, List<Periode>> getPeriodes,
      String requestStartDate,
      String requestEndDate) {
    natures.forEach(
        naturePrestation ->
            getToMerge
                .apply(naturePrestation)
                .forEach(
                    t ->
                        getPeriodes
                            .apply(t)
                            .removeIf(
                                periode ->
                                    !DateUtils.isOverlapping(
                                        periode.getDebut(),
                                        periode.getFin(),
                                        requestStartDate,
                                        requestEndDate))));
    natures.forEach(
        naturePrestation ->
            getToMerge.apply(naturePrestation).removeIf(t -> getPeriodes.apply(t).isEmpty()));
  }
}
