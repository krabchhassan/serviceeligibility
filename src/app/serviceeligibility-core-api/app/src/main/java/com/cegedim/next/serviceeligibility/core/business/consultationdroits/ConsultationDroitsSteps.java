package com.cegedim.next.serviceeligibility.core.business.consultationdroits;

import static java.util.stream.Collectors.toList;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.BeneficiaireDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ContractDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarantDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.contract.DomaineDroitContratDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.contract.PeriodeDroitContratDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.data.DemandeInfoBeneficiaire;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.exception.*;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeRechercheDeclarantService;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.VisiodroitUtils;
import com.cegedim.next.serviceeligibility.core.business.contrat.dao.ContratDao;
import com.cegedim.next.serviceeligibility.core.features.consultationdroits.MapperContratToContractDto;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service("ConsultationDroitsSteps")
@RequiredArgsConstructor
public class ConsultationDroitsSteps {

  private final ContratDao contratDao;
  private final MapperContratToContractDto mapperContractDto;

  /**
   * Récupère les contrats répondant aux critères de la demande effectuée (infoBenef)
   *
   * @return les contrats valides avec les domaines droits filtrés selon la demande
   */
  @ContinueSpan(log = "getContratsValides")
  public List<ContractDto> getContratsValides(
      DemandeInfoBeneficiaire infoBenef, DeclarantDto declarant, boolean limitWaranties) {
    TypeRechercheDeclarantService typeDeclarant =
        getTypeRechercheDeclarant(declarant, infoBenef.getNumeroPrefectoral());

    List<ContractTP> contrats = getListeContrat(infoBenef, false, typeDeclarant);

    // Si la recherche beneficiaire par nir n'aboutit pas, on recherche par numero
    // adherent
    if (CollectionUtils.isEmpty(contrats)
        && StringUtils.isNotBlank(infoBenef.getNumeroAdherent())) {
      contrats = getListeContrat(infoBenef, true, typeDeclarant);
    }

    if (CollectionUtils.isEmpty(contrats)) {
      if (StringUtils.isEmpty(infoBenef.getNumeroAdherent())) {
        throw new ExceptionNumeroAdherentAbsent();
      }
      if (declarant != null
          && (Constants.NUMERO_CCMO_ECHANGE.equals(declarant.getNumeroRNM())
              || Constants.NUMERO_CCMO.equals(declarant.getNumeroRNM()))) {
        throw new ExceptionServiceCartePapier();
      } else {
        throw new ExceptionServiceBeneficiaireInconnu();
      }
    }

    List<ContractTP> updatedContracts =
        getContractsWithFilteredDomains(contrats, limitWaranties, infoBenef);

    return new ArrayList<>(mapperListeContractToContractDto(updatedContracts, infoBenef));
  }

  /**
   * Récupère les contrats répondant aux critères de la demande effectuée (infoBenef)
   *
   * @return les contrats valides
   */
  @ContinueSpan(log = "getContratsValidesCarteTiersPayant")
  public List<ContractDto> getContratsValidesCarteTiersPayant(
      DemandeInfoBeneficiaire infoBenef, DeclarantDto declarant) {
    TypeRechercheDeclarantService typeDeclarant =
        getTypeRechercheDeclarant(declarant, infoBenef.getNumeroPrefectoral());

    List<ContractTP> contrats = getListeContratCarteTiersPayant(infoBenef, false, typeDeclarant);

    // Si la recherche beneficiaire par nir n'aboutit pas, on recherche par numero
    // adherent
    if (CollectionUtils.isEmpty(contrats)
        && StringUtils.isNotBlank(infoBenef.getNumeroAdherent())) {
      contrats = getListeContratCarteTiersPayant(infoBenef, true, typeDeclarant);
    }

    if (CollectionUtils.isEmpty(contrats)) {
      if (StringUtils.isEmpty(infoBenef.getNumeroAdherent())) {
        throw new ExceptionNumeroAdherentAbsent();
      }
      if (declarant != null
          && (Constants.NUMERO_CCMO_ECHANGE.equals(declarant.getNumeroRNM())
              || Constants.NUMERO_CCMO.equals(declarant.getNumeroRNM()))) {
        throw new ExceptionServiceCartePapier();
      } else {
        throw new ExceptionServiceBeneficiaireInconnu();
      }
    }

    return new ArrayList<>(mapperListeContractToContractDto(contrats, infoBenef));
  }

  /**
   * Récupère la liste des contrats pour une recherche par bénéficiaire
   *
   * @param infoBenef Informations bénéficiaire
   * @param rechercheParAdherent boolean true si on effectue une rechercher par adhérent
   * @param typeDeclarant Type du déclarant
   * @return La liste des contrats pour une recherche par bénéficiaire
   */
  private List<ContractTP> getListeContrat(
      DemandeInfoBeneficiaire infoBenef,
      boolean rechercheParAdherent,
      TypeRechercheDeclarantService typeDeclarant) {

    List<ContractTP> contrats = new ArrayList<>();
    switch (typeDeclarant) {
      case NUMERO_ECHANGE:
        contrats =
            contratDao.findContractsTPByBeneficiary(
                infoBenef.getDateNaissance(),
                infoBenef.getRangNaissance(),
                infoBenef.getNirBeneficiaire(),
                infoBenef.getCleNirBneficiare(),
                infoBenef.getNumeroPrefectoral(),
                false,
                false,
                rechercheParAdherent,
                infoBenef.getNumeroAdherent());
        break;
      case NUMERO_PREFECTORAL:
        contrats =
            contratDao.findContractsTPByBeneficiary(
                infoBenef.getDateNaissance(),
                infoBenef.getRangNaissance(),
                infoBenef.getNirBeneficiaire(),
                infoBenef.getCleNirBneficiare(),
                infoBenef.getNumeroPrefectoral(),
                false,
                true,
                rechercheParAdherent,
                infoBenef.getNumeroAdherent());
        break;
      default:
        break;
    }
    return contrats;
  }

  /**
   * Récupère la liste des contrats pour une recherche multi-bénéficiaires
   *
   * @param infoBenef Informations bénéficiaire
   * @param rechercheParAdherent boolean true si on effectue une rechercher par adhérent
   * @param typeDeclarant Type du déclarant
   * @return La liste des contrats pour une recherche multi-bénéficiaires
   */
  private List<ContractTP> getListeContratCarteTiersPayant(
      DemandeInfoBeneficiaire infoBenef,
      boolean rechercheParAdherent,
      TypeRechercheDeclarantService typeDeclarant) {
    List<ContractTP> contrats = new ArrayList<>();

    switch (typeDeclarant) {
      case NUMERO_ECHANGE:
        contrats =
            contratDao.findContractsForCarteFamille(
                infoBenef.getDateNaissance(),
                infoBenef.getRangNaissance(),
                infoBenef.getNirBeneficiaire(),
                infoBenef.getCleNirBneficiare(),
                infoBenef.getNumeroPrefectoral(),
                true,
                false,
                rechercheParAdherent,
                infoBenef.getNumeroAdherent());
        break;
      case NUMERO_PREFECTORAL:
        contrats =
            contratDao.findContractsForCarteFamille(
                infoBenef.getDateNaissance(),
                infoBenef.getRangNaissance(),
                infoBenef.getNirBeneficiaire(),
                infoBenef.getCleNirBneficiare(),
                infoBenef.getNumeroPrefectoral(),
                true,
                true,
                rechercheParAdherent,
                infoBenef.getNumeroAdherent());
        break;
      default:
        break;
    }
    return contrats;
  }

  /**
   * Vérifie que la periode de couverture du beneficiaire est en accord avec la date de reference
   * passée dans la demande.
   *
   * @param contractList liste de contrats a verifier
   * @param dateReference date de la demande de prise en charge
   * @return true si la liste des contrats est valide sinon false
   */
  @ContinueSpan(log = "checkValiditePeriodeDroit")
  public boolean checkValiditePeriodeDroit(
      List<ContractDto> contractList,
      Date dateReference,
      Date dateFin,
      boolean isRechercheFamille) {
    Date dateReferenceNoTime = DateUtils.formatDateWithHourMinuteSecond(dateReference);
    List<ContractDto> contratsEligibles = new ArrayList<>();

    for (ContractDto contract : contractList) {
      BeneficiaireDto beneficiaire = contract.getBeneficiaire();
      contract.setBeneficiaire(null);
      boolean isValid =
          isDomaineDroitPeriodeDroitValide(
              contract.getDomaineDroits(), dateReferenceNoTime, dateFin, isRechercheFamille);

      if (isValid) {
        contract.setBeneficiaire(beneficiaire);
        contratsEligibles.add(contract);
      }
    }
    contractList.clear();
    if (!contratsEligibles.isEmpty()) {
      contractList.addAll(contratsEligibles);
    }
    return !contractList.isEmpty();
  }

  /**
   * Verifie que'il existe une periode de droit valide dans une liste de domaineDroit, les domaines
   * de droit non valides sont supprimés de la liste.
   *
   * @param domaineDroitList liste de DomaineDroitDto à verifier
   * @param dateReferenceNoTime date à laquelle les droit doivent être ouvert
   * @return vrai si au moins une periode valide est trouvée, faux sinon
   */
  private boolean isDomaineDroitPeriodeDroitValide(
      final List<DomaineDroitContratDto> domaineDroitList,
      final Date dateReferenceNoTime,
      final Date dateFin,
      final boolean isRechercheFamille) {

    final Iterator<DomaineDroitContratDto> domaineDroitIterator = domaineDroitList.iterator();
    DomaineDroitContratDto domaineDroit;
    while (domaineDroitIterator.hasNext()) {
      domaineDroit = domaineDroitIterator.next();
      List<PeriodeDroitContratDto> periodesDroitValides =
          domaineDroit.getGaranties().stream()
              .flatMap(garantie -> garantie.getProduits().stream())
              .flatMap(produit -> produit.getReferencesCouverture().stream())
              .flatMap(referenceCouverture -> referenceCouverture.getNaturesPrestation().stream())
              .flatMap(naturePrestation -> naturePrestation.getPeriodesDroit().stream())
              .filter(
                  periode ->
                      isPeriodeDroitValide(
                          periode, dateReferenceNoTime, dateFin, isRechercheFamille))
              .toList();
      // si toutes les periodes droit d'un domaine sont invalides, on supprime ce
      // domaine
      if (CollectionUtils.isEmpty(periodesDroitValides)) {
        domaineDroitIterator.remove();
      }
    }
    return !domaineDroitList.isEmpty();
  }

  /**
   * Verifie que la periode de droit passee en parametre est valide.<br>
   *
   * @param periodeDroit la periodeDroitContractDto a verifier.
   * @param dateReferenceNoTime date a laquelle les droits doivent etre ouverts.
   * @param dateFin date jusqu a laquelle les droits doivent etre ouverts.
   * @return {@code true} si la periode de droit est valide, {@code false} sinon.
   */
  private boolean isPeriodeDroitValide(
      final PeriodeDroitContratDto periodeDroit,
      final Date dateReferenceNoTime,
      final Date dateFin,
      final boolean isRechercheFamille) {
    final Date periodeDebut =
        DateUtils.parseDate(periodeDroit.getPeriodeDebut(), DateUtils.FORMATTERSLASHED);
    Date periodeFin = null;
    if (periodeDroit.getPeriodeFin() != null) {
      periodeFin = DateUtils.parseDate(periodeDroit.getPeriodeFin(), DateUtils.FORMATTERSLASHED);
    }
    if (dateFin == null) {
      return periodeDebut.compareTo(dateReferenceNoTime) <= 0
          && (periodeFin == null || periodeFin.compareTo(dateReferenceNoTime) >= 0);
    } else {
      return this.setPeriods(periodeDroit, dateReferenceNoTime, dateFin, isRechercheFamille);
    }
  }

  private boolean setPeriods(
      final PeriodeDroitContratDto periodeDroit,
      final Date dateReference,
      final Date dateFin,
      final boolean isRechercheFamille) {
    final Date periodeDebut =
        DateUtils.parseDate(periodeDroit.getPeriodeDebut(), DateUtils.FORMATTERSLASHED);
    Date periodeFin = null;
    if (periodeDroit.getPeriodeFin() != null) {
      periodeFin = DateUtils.parseDate(periodeDroit.getPeriodeFin(), DateUtils.FORMATTERSLASHED);
    }
    if (periodeDebut.compareTo(dateFin) <= 0
        && (periodeFin == null || periodeFin.compareTo(dateReference) >= 0)) {
      if (!isRechercheFamille) {
        if (periodeDebut.compareTo(dateReference) < 0) {
          periodeDroit.setPeriodeDebut(
              DateUtils.formatDate(dateReference, DateUtils.FORMATTERSLASHED));
        }
        if (periodeFin == null || periodeFin.compareTo(dateFin) > 0) {
          periodeDroit.setPeriodeFin(DateUtils.formatDate(dateFin));
        }
      }
      return true;
    }
    return false;
  }

  private TypeRechercheDeclarantService getTypeRechercheDeclarant(
      DeclarantDto declarant, String numAmcRecherche) {
    if (declarant != null) {
      // HACK - Le numero d'amc prefectoral étant celui fourni dans la
      // requete, on passe par le RNM qui est l'id reel de l'amc
      if (numAmcRecherche.equals(declarant.getNumeroRNM())) {
        return TypeRechercheDeclarantService.NUMERO_PREFECTORAL;
      } else {
        return TypeRechercheDeclarantService.NUMERO_ECHANGE;
      }
    }
    log.info("Aucune AMC n'existe avec l'id {}", numAmcRecherche);
    return TypeRechercheDeclarantService.UNDEFINED;
  }

  /**
   * Retourne les contrats filtrées Les domaines de droits et les garanties sont filtrés selon les
   * paramètres donnés en entrée Lance une exception si une non priorisation des garanties est
   * détectée ou si le bénéficiaire n'a pas de droits ouverts
   *
   * @return les contrats filtrées
   */
  public static List<ContractTP> getContractsWithFilteredDomains(
      List<ContractTP> contrats, boolean limitWaranties, DemandeInfoBeneficiaire infoBenef) {
    // 1 = ExceptionPriorisationGaranties
    // 2 = ExceptionServiceDroitNonOuvert - Detecter 6002 : Droits du bénéficiaire
    // non ouverts
    int exceptionDetected = -1;
    Set<String> segmentsRecherche = VisiodroitUtils.getSegmentRecherche(infoBenef);
    List<ContractTP> contractList = new ArrayList<>();

    for (ContractTP contrat : contrats) {
      ContractTP contratUpdated = new ContractTP(contrat);
      List<BeneficiaireContractTP> beneficiairesContract = contratUpdated.getBeneficiaires();
      contratUpdated.setBeneficiaires(null);
      List<BeneficiaireContractTP> beneficiaireContractUpdated = new ArrayList<>();
      for (BeneficiaireContractTP beneficiaireContrat : beneficiairesContract) {
        List<DomaineDroitContractTP> domainesDroits = beneficiaireContrat.getDomaineDroits();
        beneficiaireContrat.setDomaineDroits(new ArrayList<>());
        for (DomaineDroitContractTP domaine : domainesDroits) {
          if (segmentsRecherche.contains(domaine.getCode())) {
            if (limitWaranties) {
              List<PeriodeDroitContractTP> periodeDroitContractList =
                  domaine.getGaranties().stream()
                      .flatMap(garantie -> garantie.getProduits().stream())
                      .flatMap(produit -> produit.getReferencesCouverture().stream())
                      .flatMap(
                          referenceCouverture ->
                              referenceCouverture.getNaturesPrestation().stream())
                      .flatMap(naturePrestation -> naturePrestation.getPeriodesDroit().stream())
                      .collect(toList());

              if (!detectExceptionServiceDroitNonOuvert(infoBenef, periodeDroitContractList)) {
                // Clé : code de la garantie, Valeur : Liste de Paires < code de la priorité,
                // liste des périodes >
                Map<String, Map<String, List<Periode>>> prioritiesPeriodesByGarantie =
                    getPrioritiesPeriodeByGarantie(infoBenef, domaine);

                if (!detectExceptionPriorisationGarantie(prioritiesPeriodesByGarantie)) {
                  domaine.setGaranties(limitGaranties(prioritiesPeriodesByGarantie, domaine));
                  beneficiaireContrat.getDomaineDroits().add(domaine);
                } else {
                  exceptionDetected = 1;
                }
              } else {
                exceptionDetected = 2;
              }
            } else {
              beneficiaireContrat.getDomaineDroits().add(domaine);
            }
          }
        }
        if (!CollectionUtils.isEmpty(beneficiaireContrat.getDomaineDroits())) {
          beneficiaireContractUpdated.add(beneficiaireContrat);
        }
      }
      if (!CollectionUtils.isEmpty(beneficiaireContractUpdated)) {
        contratUpdated.setBeneficiaires(beneficiaireContractUpdated);
        contractList.add(contratUpdated);
      }
    }
    if (CollectionUtils.isEmpty(contractList)) {
      if (exceptionDetected == 1) {
        throw new ExceptionPriorisationGaranties();
      } else if (exceptionDetected == 2) {
        throw new ExceptionServiceDroitNonOuvert();
      }
    }
    return contractList;
  }

  /**
   * A partir des periodes de droit d'un contrat, détecte l'erreur 6002 "Droits du bénéficiaire non
   * ouverts"
   *
   * @param infoBenef : info du benef
   * @param periodeDroitContractList : liste de periode
   */
  private static boolean detectExceptionServiceDroitNonOuvert(
      DemandeInfoBeneficiaire infoBenef, List<PeriodeDroitContractTP> periodeDroitContractList) {
    List<PeriodeDroitContractTP> validPeriodes =
        periodeDroitContractList.stream()
            .filter(
                periodeDroitContract ->
                    !TypePeriode.ONLINE.equals(periodeDroitContract.getTypePeriode())
                        && isPeriodeDroitValide(
                            periodeDroitContract,
                            infoBenef.getDateReference(),
                            infoBenef.getDateFin()))
            .toList();
    return CollectionUtils.isEmpty(validPeriodes);
  }

  /**
   * Détecte une exception de prioristation de garanties
   *
   * @param prioritiesPeriodesByGarantie Map de codeGarantie/ Liste de codePriorité/Periodes
   */
  private static boolean detectExceptionPriorisationGarantie(
      Map<String, Map<String, List<Periode>>> prioritiesPeriodesByGarantie) {
    Map.Entry<String, Map<String, List<Periode>>> previousGarantie = null;
    for (Map.Entry<String, Map<String, List<Periode>>> currentGarantie :
        prioritiesPeriodesByGarantie.entrySet()) {
      if (previousGarantie != null) {
        for (Map.Entry<String, List<Periode>> currentPriorite :
            currentGarantie.getValue().entrySet()) {
          for (Map.Entry<String, List<Periode>> previousPriorite :
              previousGarantie.getValue().entrySet()) {
            if (currentPriorite.getKey().equals(previousPriorite.getKey())) {
              List<Periode> overlappingPeriodes =
                  currentPriorite.getValue().stream()
                      .filter(
                          currentPeriode ->
                              previousPriorite.getValue().stream()
                                  .anyMatch(
                                      previousPeriode ->
                                          DateUtils.isOverlapping(
                                              currentPeriode.getDebut(),
                                              currentPeriode.getFin(),
                                              previousPeriode.getDebut(),
                                              previousPeriode.getFin())))
                      .toList();
              if (!CollectionUtils.isEmpty(overlappingPeriodes)) {
                return true;
              }
            }
          }
        }
      }
      previousGarantie = currentGarantie;
    }
    return false;
  }

  /**
   * Méthode permettant d'obtenir, par garantie, les priorités et leurs périodes associées
   *
   * @param infoBenef : info du benef
   * @param domaine : domaine du contrat
   * @return une Map avec pour clé le code de la garantie et pour valeur la liste des priorités avec
   *     leurs périodes
   */
  private static Map<String, Map<String, List<Periode>>> getPrioritiesPeriodeByGarantie(
      DemandeInfoBeneficiaire infoBenef, DomaineDroitContractTP domaine) {
    Map<String, Map<String, List<Periode>>> prioritiesPeriodesByGarantie = new HashMap<>();
    for (Garantie garantie : domaine.getGaranties()) {
      Map<String, List<Periode>> periodesGarantie =
          garantie.getProduits().stream()
              .flatMap(produit -> produit.getReferencesCouverture().stream())
              .flatMap(referenceCouverture -> referenceCouverture.getNaturesPrestation().stream())
              .flatMap(naturePrestation -> naturePrestation.getPrioritesDroit().stream())
              .filter(
                  prioriteDroitContrat ->
                      isPeriodeDroitValide(
                          prioriteDroitContrat,
                          infoBenef.getDateReference(),
                          infoBenef.getDateFin()))
              .sorted(Comparator.comparing(PrioriteDroitContrat::getCode))
              .collect(
                  Collectors.toMap(
                      PrioriteDroitContrat::getCode,
                      PrioriteDroitContrat::getPeriodes,
                      (first, second) -> {
                        List<Periode> periodes = new ArrayList<>();
                        periodes.addAll(first);
                        periodes.addAll(second);
                        return periodes;
                      }));
      prioritiesPeriodesByGarantie.computeIfPresent(
          garantie.getCodeGarantie(),
          (key, value) -> {
            value.putAll(periodesGarantie);
            return value;
          });
      if (!CollectionUtils.isEmpty(periodesGarantie)) {
        prioritiesPeriodesByGarantie.putIfAbsent(garantie.getCodeGarantie(), periodesGarantie);
      }
    }
    return prioritiesPeriodesByGarantie;
  }

  /**
   * Méthode permettant de trouver la priorité la plus basse et de retourner la ou les garanties
   * ayant cette priorité
   *
   * @param prioritiesPeriodesByGarantie : priorité de la periode
   * @param domaineDroitContract : domaine du contrat
   */
  private static List<Garantie> limitGaranties(
      Map<String, Map<String, List<Periode>>> prioritiesPeriodesByGarantie,
      DomaineDroitContractTP domaineDroitContract) {
    String minPriority = null;
    for (Map.Entry<String, Map<String, List<Periode>>> garantie :
        prioritiesPeriodesByGarantie.entrySet()) {
      Optional<Map.Entry<String, List<Periode>>> optGarantieMinPrio =
          garantie.getValue().entrySet().stream().min(Map.Entry.comparingByKey());
      if (optGarantieMinPrio.isPresent()
          && (minPriority == null
              || minPriority.compareTo(optGarantieMinPrio.get().getKey()) > 0)) {
        minPriority = optGarantieMinPrio.get().getKey();
      }
    }
    String finalMinPriority = minPriority;
    return domaineDroitContract.getGaranties().stream()
        .filter(
            garantie ->
                garantie.getProduits().stream()
                    .flatMap(produit -> produit.getReferencesCouverture().stream())
                    .flatMap(
                        referenceCouverture -> referenceCouverture.getNaturesPrestation().stream())
                    .flatMap(naturePrestation -> naturePrestation.getPrioritesDroit().stream())
                    .anyMatch(priority -> priority.getCode().equals(finalMinPriority)))
        .collect(toList());
  }

  @ContinueSpan(log = "mapperListeContractToContractDto")
  private List<ContractDto> mapperListeContractToContractDto(
      List<ContractTP> contractList, DemandeInfoBeneficiaire infoBenef) {
    mapperContractDto.setNumAmcRecherche(infoBenef.getNumeroPrefectoral());
    return mapperContractDto.entityListToDtoList(
        contractList, DateUtils.formatDate(infoBenef.getDateReference()));
  }

  private static boolean isPeriodeDroitValide(
      PeriodeDroitContractTP periodeDroit, Date dateReference, Date dateFin) {
    Date periodeDebut =
        DateUtils.parseDate(periodeDroit.getPeriodeDebut(), DateUtils.FORMATTERSLASHED);
    Date periodeFin =
        periodeDroit.getPeriodeFin() != null
            ? DateUtils.parseDate(periodeDroit.getPeriodeFin(), DateUtils.FORMATTERSLASHED)
            : null;
    if (dateFin == null) {
      return periodeDebut.compareTo(dateReference) <= 0
          && (periodeFin == null || periodeFin.compareTo(dateReference) >= 0);
    } else {
      return periodeDebut.compareTo(dateFin) <= 0
          && (periodeFin == null || periodeFin.compareTo(dateReference) >= 0);
    }
  }

  private static boolean isPeriodeDroitValide(
      PrioriteDroitContrat prioriteDroitContrat, Date dateReference, Date dateFin) {
    List<Periode> periodes = prioriteDroitContrat.getPeriodes();
    List<String> datesFin = periodes.stream().map(Periode::getFin).toList();
    String dateFinPriorite = DateUtils.getMaxDateOrNull(datesFin);
    Date periodeFin = null;

    List<String> datesDebut = periodes.stream().map(Periode::getDebut).toList();
    String dateDebut =
        datesDebut.stream().min(String::compareTo).orElse(periodes.get(0).getDebut());
    Date periodeDebut = DateUtils.parseDate(dateDebut, DateUtils.FORMATTERSLASHED);
    if (dateFinPriorite != null) {
      periodeFin = DateUtils.parseDate(dateFinPriorite, DateUtils.FORMATTERSLASHED);
    }

    if (dateFin == null) {
      return periodeDebut.compareTo(dateReference) <= 0
          && (periodeFin == null || periodeFin.compareTo(dateReference) >= 0);
    } else {
      return periodeDebut.compareTo(dateFin) <= 0
          && (periodeFin == null || periodeFin.compareTo(dateReference) >= 0);
    }
  }

  /**
   * Dans le cas d'une recherche de type bénéficiaire (segment non <code>null</code>), cette méthode
   * vérifie que le beneficiaire est eligible au service demandé :<br>
   * la liste de contrat doit au moins contenir un contrat dont le code d'un de ses domaines de
   * droit correspond au segment de recherche en paramètre.
   *
   * @param contractList list de contrats à verifier
   * @param segmentsRecherche segments de recherche à chercher
   */
  @ContinueSpan(log = "checkDomaineDroit")
  public void checkDomaineDroit(
      final List<ContractDto> contractList, Set<String> segmentsRecherche) {
    if (!segmentsRecherche.isEmpty()) {
      contractList.removeIf(
          contract -> !hasValidDomaineDroits(contract.getDomaineDroits(), segmentsRecherche));
      if (contractList.isEmpty()) {
        throw new ExceptionServiceBeneficiaireNonEligible();
      }
    }
  }

  /**
   * Verifie qu'il existe un domaineDroit dans une liste de domaineDroit dont le code correspond à
   * la liste des segments de recherche en paramètre. Tous les domaines de droit invalides sont
   * supprimes de la liste.
   *
   * @param domaineDroitList liste de domaine droit à verifier
   * @param segmentsRecherche liste des segments de recherche à chercher
   * @return vrai si au moins un code ou codeExterne correspond aux segmentsRecherche
   */
  private boolean hasValidDomaineDroits(
      final List<DomaineDroitContratDto> domaineDroitList, Set<String> segmentsRecherche) {
    domaineDroitList.removeIf(
        domaineDroit ->
            !segmentsRecherche.contains(domaineDroit.getCode())
                && !segmentsRecherche.contains(domaineDroit.getCodeExterne()));
    return !domaineDroitList.isEmpty();
  }
}
