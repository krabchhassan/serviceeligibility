package com.cegedim.next.serviceeligibility.core.services.pau;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PeriodeDroitContractTP;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.TypePeriode;
import com.cegedim.next.serviceeligibility.core.utils.Util;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class IssuingCompanyCodeService {

  /**
   * BLUE-5911 Retrouve le contrat ayant la date de début de validité la plus petite parmi les
   * contrats ayant une période de validité postérieure à la date des soins
   *
   * @return un Triplet correspondant à la réponse attendue : issuingCompanyCode, insurerId,
   *     beneficiaryId
   */
  public Triple<String, String, String> getFuturContractInfo(
      List<String> numeroPersonnes, List<ContractTP> futurContractTPS, boolean isTpOnline) {
    // Liste de triplets de numeroContrat, periodeDebutContratMin, numeroPersonne du
    // benef triée par periodeDebut
    List<Triple<String, String, String>> contractInfos =
        futurContractTPS.stream()
            .flatMap(
                contract ->
                    contract.getBeneficiaires().stream()
                        .filter(
                            beneficiaireContract ->
                                numeroPersonnes.contains(beneficiaireContract.getNumeroPersonne()))
                        .map(
                            beneficiaireContract -> {
                              String periodeDebutMin =
                                  beneficiaireContract.getDomaineDroits().stream()
                                      .flatMap(
                                          domaineDroitContract ->
                                              domaineDroitContract.getGaranties().stream())
                                      .flatMap(garantie -> garantie.getProduits().stream())
                                      .flatMap(
                                          produit -> produit.getReferencesCouverture().stream())
                                      .flatMap(
                                          referenceCouverture ->
                                              referenceCouverture.getNaturesPrestation().stream())
                                      .flatMap(
                                          naturePrestation ->
                                              naturePrestation.getPeriodesDroit().stream()
                                                  .map(
                                                      periodeDroit ->
                                                          handlePeriodeDebut(
                                                              isTpOnline, periodeDroit)))
                                      .min(String::compareTo)
                                      .orElse("");
                              return new ImmutableTriple<>(
                                  contract.getNumeroContrat(),
                                  periodeDebutMin,
                                  beneficiaireContract.getNumeroPersonne());
                            }))
            .sorted(Comparator.comparing(ImmutableTriple::getMiddle))
            .collect(Collectors.toList());

    String minDateDebut = contractInfos.isEmpty() ? "" : contractInfos.get(0).getMiddle();
    ContractTP contractTPFound = getContract(futurContractTPS, minDateDebut, contractInfos);
    if (contractTPFound != null) {
      return new ImmutableTriple<>(
          contractTPFound.getGestionnaire(),
          contractTPFound.getIdDeclarant(),
          contractTPFound.getIdDeclarant() + "-" + contractInfos.get(0).getRight());
    } else {
      return null;
    }
  }

  /**
   * BLUE-5911 Retrouve le contrat ayant la date de fin de validité la plus grande
   *
   * @return un Triplet correspondant à la réponse attendue : issuingCompanyCode, insurerId,
   *     beneficiaryId
   */
  public Triple<String, String, String> getPastContractInfo(
      List<String> numeroPersonnes, List<ContractTP> pastContractTPS, Boolean isTpOnline) {
    // Liste de triplets de numeroContrat, periodeFinContratMax, numeroPersonne du
    // benef triée par periodeMax
    List<Triple<String, String, String>> contractInfos =
        pastContractTPS.stream()
            .flatMap(
                contract -> {
                  boolean isExclusiviteCarteDematerialise =
                      Util.isExcluDemat(contract.getCarteTPaEditerOuDigitale());
                  return contract.getBeneficiaires().stream()
                      .filter(
                          beneficiaireContract ->
                              numeroPersonnes.contains(beneficiaireContract.getNumeroPersonne()))
                      .map(
                          beneficiaireContract -> {
                            String periodeFinMax =
                                beneficiaireContract.getDomaineDroits().stream()
                                    .flatMap(
                                        domaineDroitContract ->
                                            domaineDroitContract.getGaranties().stream())
                                    .flatMap(garantie -> garantie.getProduits().stream())
                                    .flatMap(produit -> produit.getReferencesCouverture().stream())
                                    .flatMap(
                                        referenceCouverture ->
                                            referenceCouverture.getNaturesPrestation().stream())
                                    .flatMap(
                                        naturePrestation ->
                                            naturePrestation.getPeriodesDroit().stream()
                                                .map(
                                                    periodeDroit ->
                                                        handlePeriodeFin(
                                                            isTpOnline,
                                                            periodeDroit,
                                                            contract.getDateRestitution(),
                                                            isExclusiviteCarteDematerialise)))
                                    .max(String::compareTo)
                                    .orElse("");
                            return new ImmutableTriple<>(
                                contract.getNumeroContrat(),
                                periodeFinMax,
                                beneficiaireContract.getNumeroPersonne());
                          });
                })
            .sorted(Comparator.comparing(ImmutableTriple::getMiddle))
            .collect(Collectors.toList());

    String maxDateFin =
        contractInfos.isEmpty() ? "" : contractInfos.get(contractInfos.size() - 1).getMiddle();
    ContractTP contractTPFound = getContract(pastContractTPS, maxDateFin, contractInfos);
    if (contractTPFound != null) {
      return new ImmutableTriple<>(
          contractTPFound.getGestionnaire(),
          contractTPFound.getIdDeclarant(),
          contractTPFound.getIdDeclarant()
              + "-"
              + contractInfos.get(contractInfos.size() - 1).getRight());
    } else {
      return null;
    }
  }

  private String handlePeriodeDebut(Boolean isTpOnline, PeriodeDroitContractTP periodeDroit) {
    if (Boolean.TRUE.equals(isTpOnline)
        && TypePeriode.ONLINE.equals(periodeDroit.getTypePeriode())) {
      return periodeDroit.getPeriodeDebut();
    } else {
      if (TypePeriode.OFFLINE.equals(periodeDroit.getTypePeriode())) {
        return periodeDroit.getPeriodeDebut();
      }
    }
    return "";
  }

  private String handlePeriodeFin(
      Boolean isTpOnline,
      PeriodeDroitContractTP periodeDroit,
      String dateRestitution,
      boolean isExclusiviteCarteDematerialise) {
    String periodeFin = periodeDroit.getPeriodeFin();
    String periodeFinFermeture = periodeDroit.getPeriodeFinFermeture();
    if (Boolean.TRUE.equals(isTpOnline)
        && TypePeriode.ONLINE.equals(periodeDroit.getTypePeriode())
        && periodeFin != null) {
      return periodeFin;
    } else {
      if (TypePeriode.OFFLINE.equals(periodeDroit.getTypePeriode()) && periodeFin != null) {
        return Util.getDateFinOffline(
            periodeFin, periodeFinFermeture, dateRestitution, isExclusiviteCarteDematerialise);
      }
    }
    return "";
  }

  private ContractTP getContract(
      List<ContractTP> results,
      String dateToCompare,
      List<Triple<String, String, String>> contractInfos) {
    List<String> lastNumContracts = new ArrayList<>();
    for (Triple<String, String, String> contractInfo : contractInfos) {
      if (dateToCompare.compareTo(contractInfo.getMiddle()) == 0) {
        lastNumContracts.add(contractInfo.getLeft());
      }
    }
    if (!CollectionUtils.isEmpty(lastNumContracts)) {
      // Si plusieurs contrat ont la même date de validité, tri par numéroContrat
      if (lastNumContracts.size() > 1) {
        lastNumContracts.sort(String::compareTo);
      }
      Optional<ContractTP> contrat =
          results.stream()
              .filter(contract -> lastNumContracts.get(0).equals(contract.getNumeroContrat()))
              .findFirst();
      return contrat.orElse(null);
    } else {
      return null;
    }
  }
}
