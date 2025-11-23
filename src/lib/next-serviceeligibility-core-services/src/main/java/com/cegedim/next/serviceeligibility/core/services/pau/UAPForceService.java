package com.cegedim.next.serviceeligibility.core.services.pau;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PeriodeDroitContractTP;
import com.cegedim.next.serviceeligibility.core.utils.ContextConstants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.UniqueAccessPointUtil;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointRequest;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointTPRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class UAPForceService {

  /**
   * Filtre les contrats TP selon les étapes de forçage définies par BLUE-6133
   *
   * @param allContracts Liste des contrats à filtrer
   * @param uniqueAccessPointTPRequest requete du PAU
   * @return le ou les contrats filtrés
   */
  protected List<ContractTP> filterContractsForce(
      List<ContractTP> allContracts, UniqueAccessPointTPRequest uniqueAccessPointTPRequest) {
    List<ContractTP> contractsFiltered = new ArrayList<>();
    if (uniqueAccessPointTPRequest.getRequest().getEndDate() != null) {
      // Step 1 du forçage : si la date de fin est valorisée, on cherche les contrats
      // valides après la demande
      contractsFiltered =
          allContracts.stream()
              .filter(
                  contract ->
                      getPeriodeDroitContractTPStream(uniqueAccessPointTPRequest, contract)
                          .anyMatch(
                              periodeDroitContract ->
                                  LocalDate.parse(
                                          periodeDroitContract.getPeriodeDebut(),
                                          DateTimeFormatter.ofPattern(DateUtils.FORMATTERSLASHED))
                                      .isAfter(
                                          LocalDate.parse(
                                              uniqueAccessPointTPRequest
                                                  .getRequest()
                                                  .getEndDate()))))
              .toList();
      if (!CollectionUtils.isEmpty(contractsFiltered)) {
        // Si résultat lors de la step 1, on retourne les contrats avec la date de début
        // la plus petite
        return getContractsMinStartDate(contractsFiltered, uniqueAccessPointTPRequest);
      }
    }
    if (CollectionUtils.isEmpty(contractsFiltered)) {
      // Step 2 du forçage : on cherche les contrats valides avant la demande
      contractsFiltered =
          allContracts.stream()
              .filter(
                  contract ->
                      getPeriodeDroitContractTPStream(uniqueAccessPointTPRequest, contract)
                          .anyMatch(
                              periodeDroitContract ->
                                  isPeriodeDroitContractBeforeRequest(
                                      uniqueAccessPointTPRequest, periodeDroitContract)))
              .toList();
      if (CollectionUtils.isEmpty(contractsFiltered)) {
        return contractsFiltered;
      }
      // Si résultat lors de la step 2, on retourne les contrats avec la date de fin
      // la plus grande
      return getContractsMaxEndDate(contractsFiltered, uniqueAccessPointTPRequest);
    }
    return contractsFiltered;
  }

  /**
   * Récupére les contrats ayant la date de fin de validité la plus grande
   *
   * @param contracts Liste de contrats
   * @return le ou les contrats ayant la date de fin de validité la plus grande
   */
  private static List<ContractTP> getContractsMaxEndDate(
      List<ContractTP> contracts, UniqueAccessPointTPRequest uniqueAccessPointTPRequest) {
    String dateFinMax =
        getPeriodeDroitContractTPStream(contracts, uniqueAccessPointTPRequest)
            .map(
                periodeDroitContract -> {
                  if (periodeDroitContract.getPeriodeFinFermeture() != null) {
                    return periodeDroitContract.getPeriodeFinFermeture();
                  } else {
                    return periodeDroitContract.getPeriodeFin();
                  }
                })
            .filter(Objects::nonNull)
            .max(String::compareTo)
            .orElse(null);
    return contracts.stream()
        .filter(
            contract ->
                getPeriodeDroitContractTPStream(uniqueAccessPointTPRequest, contract)
                    .anyMatch(
                        periodeDroitContract -> {
                          if (periodeDroitContract.getPeriodeFinFermeture() != null) {
                            return periodeDroitContract.getPeriodeFinFermeture().equals(dateFinMax);
                          } else {
                            return periodeDroitContract.getPeriodeFin().equals(dateFinMax);
                          }
                        }))
        .toList();
  }

  /**
   * Récupère les contrats ayant la date de début de validité la plus petite
   *
   * @param contracts Liste de contrats
   * @return le ou les contrats ayant la date de début de validité la plus petite
   */
  private static List<ContractTP> getContractsMinStartDate(
      List<ContractTP> contracts, UniqueAccessPointTPRequest uniqueAccessPointTPRequest) {
    String dateDebutMin =
        contracts.stream()
            .flatMap(
                contract -> getPeriodeDroitContractTPStream(uniqueAccessPointTPRequest, contract))
            .map(PeriodeDroitContractTP::getPeriodeDebut)
            .min(String::compareTo)
            .orElse(null);
    return contracts.stream()
        .filter(
            contract ->
                getPeriodeDroitContractTPStream(uniqueAccessPointTPRequest, contract)
                    .anyMatch(
                        periodeDroitContract ->
                            periodeDroitContract.getPeriodeDebut().equals(dateDebutMin)))
        .toList();
  }

  private static @NotNull Stream<PeriodeDroitContractTP> getPeriodeDroitContractTPStream(
      UniqueAccessPointTPRequest uniqueAccessPointTPRequest, ContractTP contract) {
    return contract.getBeneficiaires().stream()
        .filter(
            beneficiaireContractTP ->
                uniqueAccessPointTPRequest
                    .getNumeroPersonnes()
                    .contains(beneficiaireContractTP.getNumeroPersonne()))
        .flatMap(beneficiaireContract -> beneficiaireContract.getDomaineDroits().stream())
        .flatMap(domaineDroitContract -> domaineDroitContract.getGaranties().stream())
        .flatMap(garantieContract -> garantieContract.getProduits().stream())
        .flatMap(produitContract -> produitContract.getReferencesCouverture().stream())
        .flatMap(
            referenceCouvertureContract ->
                referenceCouvertureContract.getNaturesPrestation().stream())
        .flatMap(naturePrestationContract -> naturePrestationContract.getPeriodesDroit().stream());
  }

  private static @NotNull Stream<PeriodeDroitContractTP> getPeriodeDroitContractTPStream(
      List<ContractTP> contracts, UniqueAccessPointTPRequest uniqueAccessPointTPRequest) {
    return contracts.stream()
        .flatMap(contract -> getPeriodeDroitContractTPStream(uniqueAccessPointTPRequest, contract));
  }

  /**
   * Vérifie si une periode de droit d'un contrat TP est avant la periode de soin demandée dans le
   * PAU
   *
   * @param uniqueAccessPointTPRequest la requête contenant la période de soin demandée
   * @param periodeDroitContract la periode de droit du contrat TP à tester
   * @return vrai si la période de droit est avant la période demandée, faux sinon
   */
  private boolean isPeriodeDroitContractBeforeRequest(
      UniqueAccessPointTPRequest uniqueAccessPointTPRequest,
      PeriodeDroitContractTP periodeDroitContract) {
    LocalDate dateFin = null;
    if (periodeDroitContract.getPeriodeFinFermeture() != null) {
      dateFin =
          LocalDate.parse(
              periodeDroitContract.getPeriodeFinFermeture(),
              DateTimeFormatter.ofPattern(DateUtils.FORMATTERSLASHED));
    } else if (periodeDroitContract.getPeriodeFin() != null) {
      dateFin =
          LocalDate.parse(
              periodeDroitContract.getPeriodeFin(),
              DateTimeFormatter.ofPattern(DateUtils.FORMATTERSLASHED));
    }
    return dateFin != null
        && dateFin.isBefore(
            LocalDate.parse(uniqueAccessPointTPRequest.getRequest().getStartDate()));
  }

  protected List<ContractTP> filterContracts(
      UniqueAccessPointTPRequest uniqueAccessPointTPRequest, List<ContractTP> contracts) {
    return contracts.stream()
        .filter(
            contract ->
                getPeriodeDroitContractTPStream(uniqueAccessPointTPRequest, contract)
                    .anyMatch(
                        periodeDroitContract ->
                            checkValidityPeriod(
                                uniqueAccessPointTPRequest.getRequest(),
                                contract,
                                periodeDroitContract)))
        .toList();
  }

  private boolean checkValidityPeriod(
      UniqueAccessPointRequest request,
      ContractTP contract,
      PeriodeDroitContractTP periodeDroitContract) {
    boolean checkTpOff = true;

    if (ContextConstants.TP_OFFLINE.equals(request.getContext())
        && contract.getDateRestitution() != null) {
      checkTpOff =
          !LocalDate.parse(
                  contract.getDateRestitution(),
                  DateTimeFormatter.ofPattern(DateUtils.FORMATTERSLASHED))
              .isBefore(LocalDate.parse(request.getStartDate()));
    }

    return checkTpOff && UniqueAccessPointUtil.isOverlappingPeriod(request, periodeDroitContract);
  }
}
