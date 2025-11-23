package com.cegedim.next.serviceeligibility.core.services.claim;

import static com.cegedim.next.serviceeligibility.core.utils.Constants.CLIENT_TYPE_INSURER;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.claim.ContractByBeneficiaryDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.claim.ContractWithOrdrePrio;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.claim.NirDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.claim.NomDto;
import com.cegedim.next.serviceeligibility.core.dao.forcingrights.ForcingRightsDao;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.BeneficiaireContractTP;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PeriodeDroitContractTP;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.TypePeriode;
import com.cegedim.next.serviceeligibility.core.model.kafka.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.AssureCommun;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.utils.ContextConstants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.Util;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ForcingRightsService {

  private final ForcingRightsDao forcingRightsDaoImpl;

  public List<ContractByBeneficiaryDto> getContractsByBenefForHTP(
      String insurerId, String personNumber) {
    List<ContractWithOrdrePrio> result = new ArrayList<>();
    List<ContratAIV6> servicesPrestations =
        forcingRightsDaoImpl.findServicePrestationV6(insurerId, personNumber);
    for (ContratAIV6 servicePrest : servicesPrestations) {
      NirDto nir = this.getNirHTP(personNumber, servicePrest);
      NomDto nom = this.getSubscriberHTP(servicePrest);

      List<Periode> periodes = extractPeriodes(personNumber, servicePrest);
      List<Periode> periodesFusionnees =
          DateUtils.getPeriodesFusionnees(periodes, DateUtils.FORMATTER);
      result.addAll(
          periodesFusionnees.stream()
              .map(
                  periode ->
                      new ContractWithOrdrePrio(
                          new ContractByBeneficiaryDto(
                              servicePrest.getIdDeclarant(),
                              servicePrest.getNumero(),
                              servicePrest.getNumeroAdherent(),
                              servicePrest.getSocieteEmettrice(),
                              new Period(periode.getDebut(), periode.getFin()),
                              nir,
                              nom,
                              servicePrest.getIsContratIndividuel()),
                          servicePrest.getOrdrePriorisation()))
              .toList());
    }

    triContracts(result);
    return result.stream().map(ContractWithOrdrePrio::contract).toList();
  }

  /**
   * Extrait les periodes de l assure cible. date debut = date debut GT, date fin = la plus petite
   * date de fin entre date resiliation, date radiation, date fin GT. Les periodes inversees sont
   * filtrees
   */
  private List<Periode> extractPeriodes(String personNumber, ContratAIV6 servicePrest) {
    return servicePrest.getAssures().stream()
        .filter(assure -> assure.getIdentite().getNumeroPersonne().equals(personNumber))
        .flatMap(
            assure ->
                assure.getDroits().stream()
                    .map(
                        droit -> {
                          String debut = droit.getPeriode().getDebut();
                          String fin =
                              DateUtils.getMinDate(
                                  droit.getPeriode().getFin(),
                                  servicePrest.getDateResiliation(),
                                  assure.getDateRadiation());
                          return new Periode(debut, fin);
                        })
                    .filter(
                        periode ->
                            !DateUtils.isReverseDateStr(periode.getDebut(), periode.getFin())))
        .toList();
  }

  protected NomDto getSubscriberHTP(ContratAIV6 servicePrest) {
    NomDto subscriber = new NomDto();
    Optional<Assure> assureOptional =
        servicePrest.getAssures().stream().filter(AssureCommun::getIsSouscripteur).findFirst();
    if (assureOptional.isPresent()) {
      NomAssure nom = assureOptional.get().getData().getNom();
      subscriber.setCivility(nom.getCivilite());
      subscriber.setLastname(nom.getNomFamille());
      subscriber.setCommonName(nom.getNomUsage());
      subscriber.setFirstname(nom.getPrenom());
    }
    return subscriber;
  }

  protected NirDto getNirHTP(String personNumber, ContratAIV6 servicePrest) {
    NirDto nir = new NirDto();
    Optional<Assure> assureOptional =
        servicePrest.getAssures().stream()
            .filter(assure -> assure.getIdentite().getNumeroPersonne().equals(personNumber))
            .findFirst();
    if (assureOptional.isPresent()) {
      IdentiteContrat identite = assureOptional.get().getIdentite();
      Nir nirAssure = identite.getNir();
      if (nirAssure != null) {
        nir.setCode(nirAssure.getCode());
        nir.setKey(nirAssure.getCle());
      } else {
        List<NirRattachementRO> rattachementROList = identite.getAffiliationsRO();
        if (CollectionUtils.isNotEmpty(rattachementROList)) {
          Nir nirRO = rattachementROList.get(0).getNir();
          nir.setCode(nirRO.getCode());
          nir.setKey(nirRO.getCle());
        }
      }
    }
    return nir;
  }

  public List<ContractByBeneficiaryDto> getContractsByBenefForTP(
      String insurerId, String personNumber, String context, String clientType) {
    List<ContractWithOrdrePrio> result = new ArrayList<>();
    List<ContractTP> contractTPList = forcingRightsDaoImpl.findContratTP(insurerId, personNumber);
    for (ContractTP contractTP : contractTPList) {
      NirDto nir = this.getNirTP(personNumber, contractTP);
      NomDto nom = this.getSubscriberTP(contractTP);
      boolean isIndividualContract = isIndividualContractTP(contractTP);

      List<PeriodeDroitContractTP> periodeDroitContractTPList =
          getPeriodeDroitContractTPList(context, clientType, contractTP, personNumber);
      List<Periode> periodes;
      if (context.equals(ContextConstants.TP_OFFLINE)) {
        periodes = getPeriodesOfflines(periodeDroitContractTPList, contractTP.getDateRestitution());
      } else {
        periodes = getPeriodesOnlines(periodeDroitContractTPList);
      }
      List<Periode> periodesFusionnees =
          DateUtils.getPeriodesFusionnees(periodes, DateUtils.SLASHED_FORMATTER);
      result.addAll(
          periodesFusionnees.stream()
              .map(
                  periode ->
                      new ContractWithOrdrePrio(
                          new ContractByBeneficiaryDto(
                              contractTP.getIdDeclarant(),
                              contractTP.getNumeroContrat(),
                              contractTP.getNumeroAdherent(),
                              contractTP.getGestionnaire(),
                              new Period(periode.getDebut(), periode.getFin()),
                              nir,
                              nom,
                              isIndividualContract),
                          contractTP.getOrdrePriorisation()))
              .toList());
    }

    triContracts(result);
    return result.stream().map(ContractWithOrdrePrio::contract).toList();
  }

  protected NomDto getSubscriberTP(ContractTP contractTP) {
    NomDto nom = new NomDto();
    nom.setCivility(contractTP.getCivilitePorteur());
    nom.setLastname(contractTP.getNomPorteur());
    nom.setFirstname(contractTP.getPrenomPorteur());
    return nom;
  }

  protected boolean isIndividualContractTP(ContractTP contractTP) {
    if ("2".equals(contractTP.getIndividuelOuCollectif())) {
      return false;
    } else if ("1".equals(contractTP.getIndividuelOuCollectif())) {
      return true;
    }
    return false;
  }

  protected NirDto getNirTP(String personNumber, ContractTP contractTP) {
    NirDto nirDto = new NirDto();
    Optional<BeneficiaireContractTP> beneficiaireContractTPOptional =
        contractTP.getBeneficiaires().stream()
            .filter(
                beneficiaireContractTP ->
                    personNumber.equals(beneficiaireContractTP.getNumeroPersonne()))
            .findFirst();
    if (beneficiaireContractTPOptional.isPresent()) {
      BeneficiaireContractTP beneficiaireContractTP = beneficiaireContractTPOptional.get();
      nirDto.setCode(
          beneficiaireContractTP.getNirBeneficiaire() != null
              ? beneficiaireContractTP.getNirBeneficiaire()
              : beneficiaireContractTP.getNirOd1());
      nirDto.setKey(
          beneficiaireContractTP.getCleNirBeneficiaire() != null
              ? beneficiaireContractTP.getCleNirBeneficiaire()
              : beneficiaireContractTP.getCleNirOd1());
    }
    return nirDto;
  }

  protected List<Periode> getPeriodesOnlines(
      List<PeriodeDroitContractTP> periodeDroitContractTPList) {
    return periodeDroitContractTPList.stream()
        .filter(
            periodeDroitContractTP ->
                !DateUtils.isReverseDateStr(
                    periodeDroitContractTP.getPeriodeDebut(),
                    periodeDroitContractTP.getPeriodeFin()))
        .map(
            periodeDroitContractTP ->
                new Periode(
                    periodeDroitContractTP.getPeriodeDebut(),
                    periodeDroitContractTP.getPeriodeFin()))
        .toList();
  }

  protected List<Periode> getPeriodesOfflines(
      List<PeriodeDroitContractTP> periodeDroitContractTPList, String dateRestitution) {
    return periodeDroitContractTPList.stream()
        .map(
            periodeDroitContractTP -> {
              String dateFinOffline =
                  Util.getDateFinOffline(
                      periodeDroitContractTP.getPeriodeFin(),
                      periodeDroitContractTP.getPeriodeFinFermeture(),
                      dateRestitution,
                      false);
              return new Periode(periodeDroitContractTP.getPeriodeDebut(), dateFinOffline);
            })
        .filter(periode -> !DateUtils.isReverseDateStr(periode.getDebut(), periode.getFin()))
        .toList();
  }

  protected List<PeriodeDroitContractTP> getPeriodeDroitContractTPList(
      String context, String clientType, ContractTP contractTP, String personNumber) {
    return contractTP.getBeneficiaires().stream()
        .filter(
            beneficiaireContractTP ->
                beneficiaireContractTP.getNumeroPersonne().equals(personNumber))
        .flatMap(beneficiaireContractTP -> beneficiaireContractTP.getDomaineDroits().stream())
        .flatMap(domaineDroitContractTP -> domaineDroitContractTP.getGaranties().stream())
        .flatMap(garantie -> garantie.getProduits().stream())
        .flatMap(produit -> produit.getReferencesCouverture().stream())
        .flatMap(referenceCouverture -> referenceCouverture.getNaturesPrestation().stream())
        .flatMap(
            naturePrestation ->
                naturePrestation.getPeriodesDroit().stream()
                    .filter(
                        periodeDroitContractTP ->
                            getPredicateTypePeriode(context, periodeDroitContractTP, clientType)))
        .toList();
  }

  private boolean getPredicateTypePeriode(
      String context, PeriodeDroitContractTP periodeDroitContractTP, String clientType) {
    if (CLIENT_TYPE_INSURER.equals(clientType)) {
      if (context.equals(ContextConstants.TP_ONLINE)) {
        return TypePeriode.ONLINE.equals(periodeDroitContractTP.getTypePeriode());
      } else {
        return TypePeriode.OFFLINE.equals(periodeDroitContractTP.getTypePeriode());
      }
    } else {
      return true;
    }
  }

  protected void triContracts(List<ContractWithOrdrePrio> contracts) {
    Comparator<ContractWithOrdrePrio> comparatorStartPeriod =
        Comparator.comparing(
            contractWithOrdrePrio -> contractWithOrdrePrio.contract().period().getStart(),
            Comparator.reverseOrder());
    Comparator<ContractWithOrdrePrio> comparatorEndPeriod =
        Comparator.comparing(
            contractWithOrdrePrio -> contractWithOrdrePrio.contract().period().getEnd(),
            Comparator.nullsFirst(Comparator.reverseOrder()));
    Comparator<ContractWithOrdrePrio> comparatorOrdrePrio =
        Comparator.comparing(ContractWithOrdrePrio::ordrePriorisation);
    Comparator<ContractWithOrdrePrio> comparatorSubscriberId =
        Comparator.comparing(
            contractWithOrdrePrio -> contractWithOrdrePrio.contract().subscriberId());
    Comparator<ContractWithOrdrePrio> comparatorContractNumber =
        Comparator.comparing(
            contractWithOrdrePrio -> contractWithOrdrePrio.contract().contractNumber());
    Comparator<ContractWithOrdrePrio> comparatorTotal =
        comparatorStartPeriod
            .thenComparing(comparatorEndPeriod)
            .thenComparing(comparatorOrdrePrio)
            .thenComparing(comparatorSubscriberId)
            .thenComparing(comparatorContractNumber);
    contracts.sort(comparatorTotal);
  }
}
