package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.bobb.ContractElement;
import com.cegedim.next.serviceeligibility.core.bobb.TriggerBenefContractElement;
import com.cegedim.next.serviceeligibility.core.bobb.services.ContractElementService;
import com.cegedim.next.serviceeligibility.core.model.domain.carence.ParametrageCarence;
import com.cegedim.next.serviceeligibility.core.model.domain.pw.DomainRights;
import com.cegedim.next.serviceeligibility.core.model.domain.pw.DroitsTPOfflineAndOnlinePW;
import com.cegedim.next.serviceeligibility.core.model.domain.pw.TpOfflineRightsDetails;
import com.cegedim.next.serviceeligibility.core.model.domain.pw.TpOnlineRightsDetails;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.Anomaly;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiaryAnomaly;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.PeriodeCarence;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.CarenceDroit;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.services.pojo.*;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.GestionDroits;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.*;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.Period;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CalculDroitsTPGenerationService extends CalculDroitsTPCommonService {

  public CalculDroitsTPGenerationService(
      IPwService pwService,
      ContractElementService contractElementService,
      CarenceService carenceService) {
    super(pwService, contractElementService, carenceService);
  }

  // entrant trigger
  @ContinueSpan(log = "calculDroitsTP (4 params)")
  public List<DroitsTPExtended> calculDroitsTP(
      DroitAssure droitHTP, String requeteStartDate, String requeteEndDate, String dateFinOnline)
      throws BobbNotFoundException,
          TriggerWarningException,
          PwException,
          CarenceException,
          BeneficiaryToIgnoreException {
    return calculDroitsTP(droitHTP, dateFinOnline, new Periode(requeteStartDate, requeteEndDate));
  }

  // ********************
  // hypothèse de départ : date début carence == date début droits
  //
  // from contratHTP :
  // - get liste parametragesBobb pour le droit
  // - forEach
  // --- get parametrageAtelierProduitOfflineList
  // --- initialise listeDroits avec parametrageAtelierProduitOfflineList
  // --- get naturePrestationList from parametrageAtelierProduitOfflineList
  // --- if carence(s), forEachOrdered (dateFin decroissante)
  // ----- get parametrageCarenceList (si vide : erreur carence non paramétrée)
  // ----- controle couverture parametrageCarenceList
  // ----- update listeDroits : pour chaque, si naturePrestation présente dans
  // parametrageCarenceList si dateDebutCarence > dateDebut, alors duplication du
  // droit avec dateFin = dateDebutCarence-1 puis update droit déjà présent avec
  // dateDebut = dateFinCarence+1
  // ----- if droitRemplacement : get parametrageAtelierProduitOfflineList pour
  // remplacement
  // ------- ajoute à listeDroits droits Remplacement si naturePrestation dans
  // naturePrestationList avec : dateDebut = dateDebutCarence, dateFin =
  // dateFinCarence
  // ********************
  @ContinueSpan(log = "calculDroitsTP (3 params)")
  private List<DroitsTPExtended> calculDroitsTP(
      DroitAssure droitHTP, String dateFinOnline, Periode periodeDroitCalcule)
      throws BobbNotFoundException,
          TriggerWarningException,
          PwException,
          CarenceException,
          BeneficiaryToIgnoreException {
    boolean erreurParametrage = false;
    List<DroitsTPExtended> listeDroits = new ArrayList<>();
    log.debug("start calculating");
    LocalDate dateDebutDroit = DateUtils.parse(periodeDroitCalcule.getDebut(), DateUtils.FORMATTER);
    LocalDate dateFinDroit = DateUtils.parse(periodeDroitCalcule.getFin(), DateUtils.FORMATTER);

    ParametrageBobb parametrageBobb = callBobbIncludingIgnored(droitHTP, periodeDroitCalcule);

    if (parametrageBobb == null
        || CollectionUtils.isEmpty(parametrageBobb.getParametrageBobbProductElements())) {
      TriggeredBeneficiaryAnomaly triggeredBeneficiaryAnomaly =
          TriggeredBeneficiaryAnomaly.create(
              Anomaly.BOBB_NO_PRODUCT_FOUND, droitHTP.getCode(), periodeDroitCalcule.getDebut());
      throw new BobbNotFoundException(triggeredBeneficiaryAnomaly);
    }

    String dateDebut = periodeDroitCalcule.getDebut();
    String dateFin = periodeDroitCalcule.getFin();
    List<ParametrageBobbProductElement> parametrageBobbProductElementList =
        parametrageBobb.getParametrageBobbProductElements();
    List<ParametrageBobbNaturePrestation> parametrageBobbNaturePrestationList =
        parametrageBobbProductElementList.stream()
            .flatMap(
                parametrageBobbProductElement ->
                    parametrageBobbProductElement.getNaturePrestation().stream())
            .sorted(
                Comparator.comparing(ParametrageBobbNaturePrestation::getDateDebut)
                    .thenComparing(ParametrageBobbNaturePrestation::getDateFin))
            .toList();
    TriggeredBeneficiaryAnomaly triggeredBeneficiaryAnomaly =
        TriggeredBeneficiaryAnomaly.create(
            Anomaly.BOBB_NO_PRODUCT_FOUND, droitHTP.getCode(), droitHTP.getPeriode().getDebut());
    boolean covered =
        isCoveredByParametrageBobb(
            parametrageBobbNaturePrestationList, dateDebut, triggeredBeneficiaryAnomaly, dateFin);
    if (!covered) {
      throw new BobbNotFoundException(triggeredBeneficiaryAnomaly);
    }

    // Si au moins un paramétrage Bobb a été trouvé (pas de NPE car au pire il
    // s'agit d'une liste vide)
    // Pour chaque paramétrage trouvé et pour chaque ProductElement les
    // composants...
    GestionDroits gestionDroits =
        new GestionDroits(listeDroits, dateDebutDroit, dateFinDroit, periodeDroitCalcule);
    erreurParametrage =
        calculDroitsTpIfParametrageBobbFound(
            droitHTP, dateFinOnline, gestionDroits, erreurParametrage, parametrageBobb);

    throwOnOverlappingDatesDomaines(listeDroits);

    if (erreurParametrage && CollectionUtils.isEmpty(listeDroits)) {
      triggeredBeneficiaryAnomaly =
          TriggeredBeneficiaryAnomaly.create(Anomaly.NO_TP_RIGHTS_CAUSED_BY_WAITINGS_PERIODS);
      log.debug(triggeredBeneficiaryAnomaly.getDescription());
      throw new TriggerWarningException(triggeredBeneficiaryAnomaly);
    }
    log.debug("end calculating, number of rights returned : {}", listeDroits.size());
    return listeDroits;
  }

  private static boolean isCoveredByParametrageBobb(
      List<ParametrageBobbNaturePrestation> parametrageBobbNaturePrestationList,
      String dateDebut,
      TriggeredBeneficiaryAnomaly triggeredBeneficiaryAnomaly,
      String dateFin)
      throws BobbNotFoundException {
    boolean covered = false;
    for (ParametrageBobbNaturePrestation parametrageBobbNaturePrestation :
        parametrageBobbNaturePrestationList) {
      if (!covered) {
        if (DateUtils.before(dateDebut, parametrageBobbNaturePrestation.getDateDebut())) {
          throw new BobbNotFoundException(triggeredBeneficiaryAnomaly);
        }
        if (!DateUtils.after(
            dateDebut, parametrageBobbNaturePrestation.getDateFin(), DateUtils.FORMATTER)) {
          if (!DateUtils.before(
              parametrageBobbNaturePrestation.getDateFin(), dateFin, DateUtils.FORMATTER)) {
            covered = true;
          } else {
            dateDebut =
                DateUtils.datePlusOneDay(
                    parametrageBobbNaturePrestation.getDateFin(), DateUtils.FORMATTER);
          }
        }
      }
    }
    return covered;
  }

  private boolean calculDroitsTpIfParametrageBobbFound(
      DroitAssure droitHTP,
      String dateFinOnline,
      GestionDroits gestionDroits,
      boolean erreurParametrage,
      ParametrageBobb parametrageBobb)
      throws CarenceException, BobbNotFoundException, PwException, BeneficiaryToIgnoreException {
    Periode periodeDroitCalcule = gestionDroits.getPeriodeDroitCalcule();
    List<DroitsTPExtended> listeDroits = gestionDroits.getListeDroits();
    List<ParametrageAtelierProduit> parametrageAtelierProduitFull = new ArrayList<>();
    for (ParametrageBobbProductElement parametrageBobbProductElement :
        parametrageBobb.getParametrageBobbProductElements()) {
      List<DroitsTPExtended> standardExtendedList = new ArrayList<>();
      List<ParametrageAtelierProduit> parametrageAtelierProduitList =
          callPW(droitHTP, parametrageBobbProductElement);

      // Si on a une réponse de l'Atelier Produit...
      if (CollectionUtils.isNotEmpty(parametrageAtelierProduitList)) {

        parametrageAtelierProduitFull.addAll(parametrageAtelierProduitList);

        // GroupeBy nature, si celle-ci est nulle alors on la prend comme
        // Constants.NATURE_PRESTATION_VIDE_BOBB
        Collector<
                ParametrageBobbNaturePrestation,
                ?,
                Map<String, List<ParametrageBobbNaturePrestation>>>
            collector =
                Collectors.groupingBy(
                    (ParametrageBobbNaturePrestation param) ->
                        Objects.requireNonNullElse(
                            param.getNaturePrestation(), Constants.NATURE_PRESTATION_VIDE_BOBB));
        Map<String, List<ParametrageBobbNaturePrestation>> natureFilterListFromBobb =
            parametrageBobbProductElement.getNaturePrestation().stream().collect(collector);

        addDroits(
            standardExtendedList,
            parametrageAtelierProduitList,
            droitHTP,
            natureFilterListFromBobb,
            null,
            null,
            dateFinOnline,
            periodeDroitCalcule);

        List<CarenceDroit> carences =
            Objects.requireNonNullElse(droitHTP.getCarences(), new ArrayList<>());
        carences.sort(
            Comparator.comparing((CarenceDroit carence) -> carence.getPeriode().getFin())
                .reversed());

        List<String> naturePrestationList = getNaturesPrestation(parametrageAtelierProduitList);
        List<DroitsTPExtended> workingExtendedList = new ArrayList<>(standardExtendedList);
        int countSettingsParameterInError = 0;
        // si au moins parametrage manquant, on ne renvoit pas les natures pour ce produit.

        countSettingsParameterInError =
            calcuDroitsTpCarences(
                droitHTP,
                dateFinOnline,
                gestionDroits,
                parametrageAtelierProduitList,
                collector,
                carences,
                naturePrestationList,
                workingExtendedList,
                countSettingsParameterInError);
        if (CollectionUtils.isEmpty(carences)) {
          listeDroits.addAll(standardExtendedList);
        } else {

          erreurParametrage =
              calculDroitsTpWorkingExtendedList(
                  erreurParametrage,
                  listeDroits,
                  standardExtendedList,
                  workingExtendedList,
                  countSettingsParameterInError);
        }
      }
    }

    if (!parametrageAtelierProduitFull.isEmpty()
        && requestPeriodNotIncludedInPeriodPw(
            parametrageAtelierProduitFull,
            gestionDroits.getDateDebutDroit(),
            gestionDroits.getDateFinDroit())) {
      throw new PwException(
          TriggeredBeneficiaryAnomaly.create(Anomaly.PRODUCT_WORKSHOP_NO_COVERAGE));
    }

    return erreurParametrage;
  }

  private int calcuDroitsTpCarences(
      DroitAssure droitHTP,
      String dateFinOnline,
      GestionDroits gestionDroits,
      List<ParametrageAtelierProduit> parametrageAtelierProduitList,
      Collector<
              ParametrageBobbNaturePrestation,
              ?,
              Map<String, List<ParametrageBobbNaturePrestation>>>
          collector,
      List<CarenceDroit> carences,
      List<String> naturePrestationList,
      List<DroitsTPExtended> workingExtendedList,
      int countSettingsParameterInError)
      throws CarenceException, BobbNotFoundException, PwException, BeneficiaryToIgnoreException {
    for (CarenceDroit carence : carences) {
      if (countSettingsParameterInError == 0) {
        LocalDate dateDebut = DateUtils.parse(carence.getPeriode().getDebut(), DateUtils.FORMATTER);
        LocalDate dateFin = DateUtils.parse(carence.getPeriode().getFin(), DateUtils.FORMATTER);

        if (DateUtils.isOverlapping(
            gestionDroits.getDateDebutDroit(),
            gestionDroits.getDateFinDroit(),
            dateDebut,
            dateFin)) {
          // TODO à modifier lors du PI13 pour la récupération de tous les produits issu
          // de Bobb
          List<ParametrageCarence> parametrageCarenceList =
              getParametrageCarenceList(
                  parametrageAtelierProduitList.get(0).getCodeOc(),
                  parametrageAtelierProduitList.get(0).getCodeOffre(),
                  parametrageAtelierProduitList.get(0).getCodeProduit(),
                  carence.getCode(),
                  carence.getPeriode().getDebut(),
                  carence.getPeriode().getFin(),
                  false);
          List<String> naturePrestationCarence =
              getNaturesPrestationInSettings(parametrageCarenceList);
          countSettingsParameterInError =
              calculDroitsTpParametrageCarence(
                  droitHTP,
                  dateFinOnline,
                  gestionDroits.getPeriodeDroitCalcule(),
                  collector,
                  naturePrestationList,
                  workingExtendedList,
                  countSettingsParameterInError,
                  carence,
                  parametrageCarenceList,
                  naturePrestationCarence);
        }
      }
    }
    return countSettingsParameterInError;
  }

  private int calculDroitsTpParametrageCarence(
      DroitAssure droitHTP,
      String dateFinOnline,
      Periode periodeDroitCalcule,
      Collector<
              ParametrageBobbNaturePrestation,
              ?,
              Map<String, List<ParametrageBobbNaturePrestation>>>
          collector,
      List<String> naturePrestationList,
      List<DroitsTPExtended> workingExtendedList,
      int countSettingsParameterInError,
      CarenceDroit carence,
      List<ParametrageCarence> parametrageCarenceList,
      List<String> naturePrestationCarence)
      throws BobbNotFoundException, PwException, BeneficiaryToIgnoreException {
    if (CollectionUtils.isEmpty(parametrageCarenceList)) {
      // cas où les paramètrages de carence ne sont pas bonnes, on renvoit le droit
      // sans nature de prestation
      // TODO changer la liste de nature dans droitTPExtended
      countSettingsParameterInError++;
    } else {
      carencing(workingExtendedList, parametrageCarenceList, carence.getPeriode().getFin());

      if (carence.getDroitRemplacement() != null) {
        ParametrageBobb parametrageBobbRemplacement = callBobbIncludingIgnored(carence);

        if (parametrageBobbRemplacement == null
            || CollectionUtils.isEmpty(
                parametrageBobbRemplacement.getParametrageBobbProductElements())) {

          TriggeredBeneficiaryAnomaly triggeredBeneficiaryAnomaly =
              TriggeredBeneficiaryAnomaly.create(
                  Anomaly.BOBB_NO_PRODUCT_FOUND,
                  droitHTP.getCode(),
                  periodeDroitCalcule.getDebut());
          throw new BobbNotFoundException(triggeredBeneficiaryAnomaly);
        } else {

          calculDroitsTpParametrageBobbRemplacement(
              droitHTP,
              dateFinOnline,
              periodeDroitCalcule,
              collector,
              naturePrestationList,
              workingExtendedList,
              carence,
              parametrageCarenceList,
              naturePrestationCarence,
              parametrageBobbRemplacement);
        }
      }
    }
    return countSettingsParameterInError;
  }

  private void calculDroitsTpParametrageBobbRemplacement(
      DroitAssure droitHTP,
      String dateFinOnline,
      Periode periodeDroitCalcule,
      Collector<
              ParametrageBobbNaturePrestation,
              ?,
              Map<String, List<ParametrageBobbNaturePrestation>>>
          collector,
      List<String> naturePrestationList,
      List<DroitsTPExtended> workingExtendedList,
      CarenceDroit carence,
      List<ParametrageCarence> parametrageCarenceList,
      List<String> naturePrestationCarence,
      ParametrageBobb parametrageBobbRemplacement)
      throws PwException {
    for (ParametrageBobbProductElement parametrageBobbProductElementRemplacement :
        parametrageBobbRemplacement.getParametrageBobbProductElements()) {
      Map<String, List<ParametrageBobbNaturePrestation>> natureCarenceFilterListFromBobb =
          parametrageBobbProductElementRemplacement.getNaturePrestation().stream()
              .collect(collector);

      List<ParametrageAtelierProduit> parametrageAtelierProduitRemplacementList =
          callPW(droitHTP, parametrageBobbProductElementRemplacement);

      if (CollectionUtils.isNotEmpty(parametrageAtelierProduitRemplacementList)) {
        parametrageAtelierProduitRemplacementList.forEach(
            parametrageAtelierProduit ->
                parametrageAtelierProduit
                    .getNaturesTags()
                    .removeIf(
                        papNatureTags ->
                            !naturePrestationList.contains(papNatureTags.getNature())
                                || !naturePrestationCarence.contains(papNatureTags.getNature())));

        addDroits(
            workingExtendedList,
            parametrageAtelierProduitRemplacementList,
            droitHTP,
            natureCarenceFilterListFromBobb,
            carence,
            parametrageCarenceList,
            dateFinOnline,
            periodeDroitCalcule);
      }
    }
  }

  /**
   * Vérifie parmis les {@link DroitsTPExtended} crééent si au moins 2 ont le même domaine et des
   * dates de début et de fin qui se chevauchent. Dans ce cas il throw un {@link TriggerException}
   *
   * <p>cf task/BLUE-4753 : Paramétrage d’une GT pointant sur 2 produits distinct et ayant le
   * domaine PHAR tous les 2 : La génération des droits TP est effectuée au lieu d'être rejetée
   */
  private void throwOnOverlappingDatesDomaines(List<DroitsTPExtended> droitsTPExtendeds) {
    for (DroitsTPExtended droitsToCheck : droitsTPExtendeds) {
      String domaineToCheck = droitsToCheck.getCodeDomaine();

      if (domaineToCheck == null) {
        continue;
      }

      LocalDate debutToCheckGt = DateUtils.stringToDate(droitsToCheck.getDateDebut());
      LocalDate finToCheckGt = DateUtils.stringToDate(droitsToCheck.getDateFin());

      for (DroitsTPExtended droitsTPExtended : droitsTPExtendeds) {
        String domaine = droitsTPExtended.getCodeDomaine();

        if (sameDomains(droitsToCheck, droitsTPExtended, domaineToCheck, domaine)) {
          LocalDate debutGt = DateUtils.stringToDate(droitsTPExtended.getDateDebut());
          LocalDate finGt = DateUtils.stringToDate(droitsTPExtended.getDateFin());
          if (DateUtils.isOverlapping(debutToCheckGt, finToCheckGt, debutGt, finGt)) {
            throw new TriggerException(
                "Les produits "
                    + droitsToCheck.getCodeProduit()
                    + " et "
                    + droitsTPExtended.getCodeProduit()
                    + " ayant la même nature "
                    + domaine
                    + " ont des dates qui se chevauchent");
          }
        }
      }
    }
  }

  private static boolean sameDomains(
      DroitsTPExtended droitsToCheck,
      DroitsTPExtended droitsTPExtended,
      String domaineToCheck,
      String domaine) {
    if (domaine == null) {
      return false;
    }
    return !droitsToCheck.equals(droitsTPExtended) && domaineToCheck.equals(domaine);
  }

  public List<ParametrageAtelierProduit> callPW(
      DroitAssure droitAssure, ParametrageBobbProductElement parametrageBobbProductElement)
      throws PwException {
    List<ParametrageAtelierProduit> parametrageAtelierProduitList = new ArrayList<>();
    for (ParametrageBobbNaturePrestation paramBobb :
        parametrageBobbProductElement.getNaturePrestation()) {
      Period periodBobb = new Period(paramBobb.getDateDebutBobb(), paramBobb.getDateFinBobb());
      List<ParametrageAtelierProduit> pwRes =
          new ArrayList<>(
              getParametrageAtelierProduitsFromTpRights(
                  droitAssure,
                  parametrageBobbProductElement,
                  paramBobb.getDateDebut(),
                  paramBobb.getDateFin(),
                  periodBobb));

      filterPWResponseNature(paramBobb.getNaturePrestation(), pwRes);
      parametrageAtelierProduitList.addAll(pwRes);
    }
    return parametrageAtelierProduitList;
  }

  private List<ParametrageAtelierProduit> getParametrageAtelierProduitsFromTpRights(
      DroitAssure droitAssure,
      ParametrageBobbProductElement parametrageBobbProductElement,
      String dateDebut,
      String dateFin,
      Period periodBobb)
      throws PwException {
    List<ParametrageAtelierProduit> parametrageAtelierProduitList = new ArrayList<>();
    List<String> errorToFill = new ArrayList<>();
    List<DroitsTPOfflineAndOnlinePW> droitsTPOfflineAndOnlinePWList =
        pwService.getDroitsOfflineAndOnlineProductsWorkshop(
            errorToFill,
            parametrageBobbProductElement.getCodeProduit(),
            parametrageBobbProductElement.getCodeOc(),
            dateDebut,
            dateFin);

    if (CollectionUtils.isNotEmpty(errorToFill)) {
      for (String error : errorToFill) {
        log.error("Error pw tpOfflineRight : {}", error);
      }
      throw new PwException(
          TriggeredBeneficiaryAnomaly.create(
              Anomaly.PRODUCT_WORKSHOP_ERROR, String.join(System.lineSeparator(), errorToFill)));
    }

    if (CollectionUtils.isNotEmpty(droitsTPOfflineAndOnlinePWList)) {
      droitAssure
          .getTriggerBenefContractElement()
          .setDroitsTPOfflineAndOnlinePWS(droitsTPOfflineAndOnlinePWList);
      for (DroitsTPOfflineAndOnlinePW droitsPW : droitsTPOfflineAndOnlinePWList) {
        errorInProductSetting(
            droitsPW,
            parametrageBobbProductElement.getCodeProduit(),
            parametrageBobbProductElement.getCodeOc());
        if (!parametrageBobbProductElement.getCodeOffre().equals(droitsPW.getOfferCode())) {
          throw new PwException(
              TriggeredBeneficiaryAnomaly.create(
                  Anomaly.PRODUCT_WORKSHOP_SETTINGS_NOT_FOUND,
                  droitsPW.getOfferCode(),
                  parametrageBobbProductElement.getCodeOffre()));
        }
        calculDroitsTpDomainRights(
            parametrageBobbProductElement, periodBobb, parametrageAtelierProduitList, droitsPW);
      }
    }
    return parametrageAtelierProduitList;
  }

  private void calculDroitsTpDomainRights(
      ParametrageBobbProductElement parametrageBobbProductElement,
      Period periodBobb,
      List<ParametrageAtelierProduit> parametrageAtelierProduitList,
      DroitsTPOfflineAndOnlinePW droitsPW) {
    for (DomainRights domain : droitsPW.getDomains()) {
      TpOfflineRightsDetails domainTpOffline = domain.getTpOffline();
      if (domainTpOffline != null && domainTpOffline.getNature() != null) {
        ParametrageAtelierProduit parametrageAtelierProduit = new ParametrageAtelierProduit();
        parametrageAtelierProduit.setDomaine(domain.getDomainCode());
        TpOnlineRightsDetails domainTpOnline = domain.getTpOnline();
        parametrageAtelierProduit.setCodeProduit(parametrageBobbProductElement.getCodeProduit());
        parametrageAtelierProduit.setCodeOffre(droitsPW.getOfferCode());
        parametrageAtelierProduit.setCodeOc(parametrageBobbProductElement.getCodeOc());
        parametrageAtelierProduit.setVersion(String.valueOf(droitsPW.getOfferVersionCode()));
        parametrageAtelierProduit.setModeAssemblage(droitsPW.getAssemblyMode());
        List<PAPNatureTags> naturesTags = new ArrayList<>();
        PAPNatureTags papNatureTags = new PAPNatureTags();
        parametrageAtelierProduit.setDetailsOffline(domainTpOffline);
        papNatureTags.setNature(domainTpOffline.getNature());
        addNatureTags(naturesTags, papNatureTags);
        if (domainTpOnline != null) {
          parametrageAtelierProduit.setDetailsOnline(domainTpOnline);
          papNatureTags.setNature(domainTpOnline.getNature());
          addNatureTags(naturesTags, papNatureTags);
        }
        parametrageAtelierProduit.setNaturesTags(naturesTags);
        String validityDateStart =
            DateUtils.getMaxDate(
                periodBobb.getStart(), droitsPW.getValidityDate(), DateUtils.FORMATTER);
        String validityDateEnd =
            DateUtils.getMinDate(
                periodBobb.getEnd(), droitsPW.getEndValidityDate(), DateUtils.FORMATTER);
        parametrageAtelierProduit.setValidityDate(validityDateStart);
        parametrageAtelierProduit.setEndValidityDate(validityDateEnd);
        parametrageAtelierProduit.setPwValidityDate(droitsPW.getValidityDate());
        parametrageAtelierProduit.setPwEndValidityDate(droitsPW.getEndValidityDate());
        parametrageAtelierProduit.setBobbEndValidityDate(periodBobb.getEnd());
        parametrageAtelierProduitList.add(parametrageAtelierProduit);
      }
    }
  }

  void errorInProductSetting(
      DroitsTPOfflineAndOnlinePW droitsTPOfflineAndOnlinePW, String productCode, String ocCode)
      throws PwException {
    for (DomainRights domain : droitsTPOfflineAndOnlinePW.getDomains()) {
      TpOfflineRightsDetails domainTpOffline = domain.getTpOffline();
      if (domainTpOffline.getVariables() != null && domainTpOffline.getVariables().size() > 1) {
        throw new PwException(
            TriggeredBeneficiaryAnomaly.create(
                Anomaly.DOMAINE_SEVERAL_PARAMETERS, productCode, ocCode, domain.getDomainCode()));
      }
    }
  }

  private void addNatureTags(List<PAPNatureTags> naturesTags, PAPNatureTags papNatureTags) {
    if (naturesTags.stream().noneMatch(tag -> tag.getNature().equals(papNatureTags.getNature()))) {
      naturesTags.add(papNatureTags);
    }
  }

  public ParametrageBobb callBobbIncludingIgnored(DroitAssure droitHTP, Periode periodeDroit)
      throws BeneficiaryToIgnoreException {
    ContractElement contractElement =
        getContractElementIncludingIgnored(droitHTP.getCode(), droitHTP.getCodeAssureur());
    if (contractElement != null) {
      if (contractElement.isIgnored()) {
        throw new BeneficiaryToIgnoreException();
      }
      droitHTP.setTriggerBenefContractElement(new TriggerBenefContractElement(contractElement));
      return fillBobbParameters(periodeDroit, contractElement);
    }
    return null;
  }

  private ParametrageBobb callBobbIncludingIgnored(CarenceDroit carence)
      throws BobbNotFoundException, BeneficiaryToIgnoreException {
    ParametrageBobb parametrageBobb;
    ContractElement contractElement =
        getContractElementIncludingIgnored(
            carence.getDroitRemplacement().getCode(),
            carence.getDroitRemplacement().getCodeAssureur());
    if (contractElement != null) {
      if (contractElement.isIgnored()) {
        throw new BeneficiaryToIgnoreException();
      }
      carence
          .getDroitRemplacement()
          .setTriggerBenefContractElement(new TriggerBenefContractElement(contractElement));
      parametrageBobb =
          fillBobbParameters(
              new Periode(carence.getPeriode().getDebut(), carence.getPeriode().getFin()),
              contractElement);
    } else {
      TriggeredBeneficiaryAnomaly triggeredBeneficiaryAnomaly =
          TriggeredBeneficiaryAnomaly.create(
              Anomaly.BOBB_SETTINGS_NOT_FOUND,
              carence.getDroitRemplacement().getCode(),
              carence.getDroitRemplacement().getCodeAssureur());
      log.debug(triggeredBeneficiaryAnomaly.getDescription());
      throw new BobbNotFoundException(triggeredBeneficiaryAnomaly);
    }

    return parametrageBobb;
  }

  private void addDroits(
      List<DroitsTPExtended> listeDroits,
      List<ParametrageAtelierProduit> parametrageAtelierProduitList,
      DroitAssure droitHTP,
      Map<String, List<ParametrageBobbNaturePrestation>> bobbNatureFilter,
      @Nullable CarenceDroit carence,
      @Nullable List<ParametrageCarence> parametrageCarenceList,
      String dateFinOnline,
      Periode periodeDroitCalcule) {

    parametrageCarenceList =
        Objects.requireNonNullElse(parametrageCarenceList, Collections.emptyList());

    DateBuffer datesBuffer = new DateBuffer();
    datesBuffer.dateDebut = periodeDroitCalcule.getDebut();
    datesBuffer.dateFin = periodeDroitCalcule.getFin();
    datesBuffer.dateFinOnline = dateFinOnline;

    if (carence != null) {
      datesBuffer.dateDebut =
          DateUtils.getMaxDate(
              datesBuffer.dateDebut, carence.getPeriode().getDebut(), DateUtils.FORMATTER);
      datesBuffer.dateFin =
          DateUtils.getMinDate(
              datesBuffer.dateFin, carence.getPeriode().getFin(), DateUtils.FORMATTER);
    }
    for (ParametrageAtelierProduit parametrageAtelierProduit : parametrageAtelierProduitList) {
      DateBuffer datesBufferPerAtelierProd = datesBuffer.copy();

      completeDateBufferWithAtelierProd(datesBufferPerAtelierProd, parametrageAtelierProduit);

      PeriodeCarence periodeCarence =
          completeDateBufferWithCarences(
              datesBufferPerAtelierProd, parametrageAtelierProduit, parametrageCarenceList);

      for (PAPNatureTags papNatureTags : parametrageAtelierProduit.getNaturesTags()) {
        // Filter nature based on bobb
        List<ParametrageBobbNaturePrestation> paramsBobbNaturePresta =
            getParametrageBobbNaturePrestations(papNatureTags.getNature(), bobbNatureFilter);

        for (ParametrageBobbNaturePrestation parametrageBobbNaturePrestation :
            paramsBobbNaturePresta) {
          DateBuffer dateBufferPerBobbParam = datesBufferPerAtelierProd.copy();

          completeDateBufferWithBobbParams(dateBufferPerBobbParam, parametrageBobbNaturePrestation);

          // Si la date fin est avant la date debut on passe au parametrage bobb suivant
          if (bufferDatesInverse(dateBufferPerBobbParam)) {
            continue;
          }

          DroitsTPExtended droitsTPExtended =
              createDroitsTPExtendedBase(droitHTP, periodeDroitCalcule);
          completeDroitsTPExtendedAtelierProd(droitsTPExtended, parametrageAtelierProduit);

          droitsTPExtended.setDateDebut(dateBufferPerBobbParam.dateDebutForWorking);
          droitsTPExtended.setDateFin(dateBufferPerBobbParam.dateFinForWorking);
          droitsTPExtended.setDateFinOnline(dateBufferPerBobbParam.dateFinOnlineForWorking);

          droitsTPExtended.setPapNatureTags(papNatureTags);

          completeFinOnlineAndDroitsWithCarence(
              carence, droitsTPExtended, dateBufferPerBobbParam.dateFinParametrage, periodeCarence);

          Periode periodeBobb = new Periode();
          periodeBobb.setDebut(parametrageBobbNaturePrestation.getDateDebutBobb());
          periodeBobb.setFin(parametrageBobbNaturePrestation.getDateFinBobb());
          droitsTPExtended.setPeriodeProductElement(periodeBobb);

          Periode periodePW = new Periode();
          periodePW.setDebut(parametrageAtelierProduit.getValidityDate());
          periodePW.setFin(parametrageAtelierProduit.getEndValidityDate());
          droitsTPExtended.setPeriodePW(periodePW);

          listeDroits.add(droitsTPExtended);
        }
      }
    }
  }
}
