package com.cegedim.next.serviceeligibility.core.services;

import static com.cegedim.next.serviceeligibility.core.utils.Constants.GENERATION_DROIT_TP;
import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.PW_OFFERSTRUCTURE_VERSION;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.bobb.ContractElement;
import com.cegedim.next.serviceeligibility.core.bobb.ProductElement;
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
import com.cegedim.next.serviceeligibility.core.utils.Util;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.*;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.Period;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointRequestV5;
import io.micrometer.tracing.annotation.ContinueSpan;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CalculDroitsTPService {

  private final String offerStructureVersion;

  public CalculDroitsTPService(
      ContractElementService contractElementService,
      IPwService pwService,
      CarenceService carenceService,
      BeyondPropertiesService beyondPropertiesService) {
    this.contractElementService = contractElementService;
    this.pwService = pwService;
    this.carenceService = carenceService;
    offerStructureVersion =
        beyondPropertiesService.getProperty(PW_OFFERSTRUCTURE_VERSION).orElse("V4");
  }

  // Le Bean est cree dans les job/worker utilisant CalculDroitsTPService
  ContractElementService contractElementService;

  IPwService pwService;

  CarenceService carenceService;

  @ContinueSpan(log = "calculDroitsTP (6 params)")
  public List<DroitsTPExtended> calculDroitsTP(
      DroitAssure droitHTP,
      Periode periode,
      String requeteStartDate,
      String requeteEndDate,
      String dateFinOnline,
      String context,
      UniqueAccessPointRequestV5 requestV5)
      throws BobbNotFoundException, PwException, TriggerWarningException, CarenceException {
    List<DroitsTPExtended> res = new ArrayList<>();

    String minDate =
        DateUtils.getMaxDate(periode.getDebut(), requeteStartDate, DateUtils.FORMATTER);
    String maxDate = DateUtils.getMinDate(periode.getFin(), requeteEndDate, DateUtils.FORMATTER);
    if (maxDate == null || !DateUtils.before(maxDate, minDate)) {
      res.addAll(
          calculDroitsTP(
              droitHTP, context, dateFinOnline, new Periode(minDate, maxDate), requestV5));
    }
    return res;
  }

  @ContinueSpan(log = "calculDroitsTP (5 params)")
  public List<DroitsTPExtended> calculDroitsTP(
      DroitAssure droitHTP,
      String requeteStartDate,
      String requeteEndDate,
      String dateFinOnline,
      String context)
      throws BobbNotFoundException, TriggerWarningException, PwException, CarenceException {
    return calculDroitsTP(
        droitHTP, context, dateFinOnline, new Periode(requeteStartDate, requeteEndDate), null);
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
  @ContinueSpan(log = "calculDroitsTP (4 params)")
  public List<DroitsTPExtended> calculDroitsTP(
      DroitAssure droitHTP,
      String context,
      String dateFinOnline,
      Periode periodeDroitCalcule,
      UniqueAccessPointRequestV5 requestV5)
      throws BobbNotFoundException, TriggerWarningException, PwException, CarenceException {
    boolean erreurParametrage = false;
    List<DroitsTPExtended> listeDroits = new ArrayList<>();
    log.debug("start calculating");
    LocalDate dateDebutDroit = DateUtils.parse(periodeDroitCalcule.getDebut(), DateUtils.FORMATTER);
    LocalDate dateFinDroit = DateUtils.parse(periodeDroitCalcule.getFin(), DateUtils.FORMATTER);

    List<ParametrageBobb> parametrageBobbList;
    boolean forced = false;
    boolean requirePW = true;
    if (requestV5 != null) {
      forced = Boolean.TRUE.equals(requestV5.getIsForced());
      requirePW = Boolean.TRUE.equals(requestV5.getRequirePW());
    }
    if (forced) {
      Periode periodeForced = new Periode(requestV5.getStartDate(), requestV5.getEndDate());
      parametrageBobbList = callBobb(droitHTP, periodeForced);
    } else {
      parametrageBobbList = callBobb(droitHTP, periodeDroitCalcule);
    }

    // Si l'appel n'est pas dans le cadre de la génération des droits TP (appel PAU
    // en HTP par exemple) et...
    // Si aucun paramétrage Bobb n'a été trouvé
    if (!GENERATION_DROIT_TP.equals(context) && CollectionUtils.isEmpty(parametrageBobbList)) {
      addDroitsIfBobbNotExist(droitHTP, context, listeDroits, periodeDroitCalcule);
    }

    // Si au moins un paramétrage Bobb a été trouvé (pas de NPE car au pire il
    // s'agit d'une liste vide)
    // Pour chaque paramétrage trouvé et pour chaque ProductElement les
    // composants...
    GestionDroits gestionDroits =
        new GestionDroits(listeDroits, dateDebutDroit, dateFinDroit, periodeDroitCalcule);
    if (requirePW) {
      erreurParametrage =
          calculDroitsTpIfParametrageBobbFound(
              droitHTP,
              context,
              dateFinOnline,
              gestionDroits,
              erreurParametrage,
              parametrageBobbList,
              forced);
    }

    if (GENERATION_DROIT_TP.equals(context)) {
      throwOnOverlappingDatesDomaines(listeDroits);

      // Si aucun droit n'a été généré et...
      // Si une carence existe et...
      // Si on est dans le cadre de la génération de droits TP
      if (erreurParametrage && CollectionUtils.isEmpty(listeDroits)) {
        TriggeredBeneficiaryAnomaly triggeredBeneficiaryAnomaly =
            TriggeredBeneficiaryAnomaly.create(Anomaly.NO_TP_RIGHTS_CAUSED_BY_WAITINGS_PERIODS);
        log.debug(triggeredBeneficiaryAnomaly.getDescription());
        throw new TriggerWarningException(triggeredBeneficiaryAnomaly);
      }
    }
    log.debug("end calculating, number of rights returned : {}", listeDroits.size());
    return listeDroits;
  }

  private boolean calculDroitsTpIfParametrageBobbFound(
      DroitAssure droitHTP,
      String context,
      String dateFinOnline,
      GestionDroits gestionDroits,
      boolean erreurParametrage,
      List<ParametrageBobb> parametrageBobbList,
      boolean force)
      throws CarenceException, BobbNotFoundException, PwException {
    for (ParametrageBobb parametrageBobb : parametrageBobbList) {
      Periode periodeDroitCalcule = gestionDroits.getPeriodeDroitCalcule();
      List<DroitsTPExtended> listeDroits = gestionDroits.getListeDroits();
      if (!GENERATION_DROIT_TP.equals(context)
          && CollectionUtils.isEmpty(parametrageBobb.getParametrageBobbProductElements())) {
        addDroitsIfBobbNotExist(droitHTP, context, listeDroits, periodeDroitCalcule);
      }
      List<ParametrageAtelierProduit> parametrageAtelierProduitFull = new ArrayList<>();
      for (ParametrageBobbProductElement parametrageBobbProductElement :
          parametrageBobb.getParametrageBobbProductElements()) {
        List<DroitsTPExtended> standardExtendedList = new ArrayList<>();
        List<ParametrageAtelierProduit> parametrageAtelierProduitList =
            callPW(droitHTP, parametrageBobbProductElement, context);

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
              periodeDroitCalcule,
              force);

          List<CarenceDroit> carences =
              Objects.requireNonNullElse(droitHTP.getCarences(), new ArrayList<>());
          carences.sort(
              Comparator.comparing((CarenceDroit carence) -> carence.getPeriode().getFin())
                  .reversed());

          List<String> naturePrestationList = getNaturesPrestation(parametrageAtelierProduitList);
          List<DroitsTPExtended> workingExtendedList = new ArrayList<>(standardExtendedList);
          int countSettingsParameterInError =
              0; // si au moins parametrage manquant, on ne renvoit pas les
          // natures pour ce produit.

          countSettingsParameterInError =
              calcuDroitsTpCarences(
                  droitHTP,
                  context,
                  dateFinOnline,
                  gestionDroits,
                  parametrageAtelierProduitList,
                  collector,
                  carences,
                  naturePrestationList,
                  workingExtendedList,
                  countSettingsParameterInError,
                  force);
          erreurParametrage =
              checkErreurParametrage(
                  erreurParametrage,
                  listeDroits,
                  standardExtendedList,
                  carences,
                  workingExtendedList,
                  countSettingsParameterInError);
        }
        // Si on n'a pas eu de réponse de l'Atelier Produit et...
        // Si on n'est pas dans le cadre de la génération des droits TP (appel PAU en
        // HTP par exemple)
        else if (!GENERATION_DROIT_TP.equals(context)) {
          DroitsTPExtended droitTPExtended =
              createDroitsTPExtendedBase(droitHTP, periodeDroitCalcule);
          completeDroitsTPExtendedParamBobb(droitTPExtended, parametrageBobbProductElement);
          computePeriodeDroitsTPExtendedWithParamBobb(
              parametrageBobbProductElement, droitTPExtended);
          listeDroits.add(droitTPExtended);
        }
      }

      if (!force
          && !parametrageAtelierProduitFull.isEmpty()
          && requestPeriodNotIncludedInPeriodPw(
              parametrageAtelierProduitFull,
              gestionDroits.getDateDebutDroit(),
              gestionDroits.getDateFinDroit())) {
        throw new PwException(
            TriggeredBeneficiaryAnomaly.create(Anomaly.PRODUCT_WORKSHOP_NO_COVERAGE));
      }
    }
    return erreurParametrage;
  }

  private static boolean checkErreurParametrage(
      boolean erreurParametrage,
      List<DroitsTPExtended> listeDroits,
      List<DroitsTPExtended> standardExtendedList,
      List<CarenceDroit> carences,
      List<DroitsTPExtended> workingExtendedList,
      int countSettingsParameterInError) {
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
    return erreurParametrage;
  }

  private static void computePeriodeDroitsTPExtendedWithParamBobb(
      ParametrageBobbProductElement parametrageBobbProductElement,
      DroitsTPExtended droitTPExtended) {
    for (ParametrageBobbNaturePrestation parametrageBobbNaturePrestation :
        ListUtils.emptyIfNull(parametrageBobbProductElement.getNaturePrestation())) {
      String dateDebut =
          DateUtils.getMaxDate(
              droitTPExtended.getDateDebut(),
              parametrageBobbNaturePrestation.getDateDebut(),
              DateUtils.FORMATTER);
      String dateFin =
          DateUtils.getMinDate(
              droitTPExtended.getDateFin(),
              parametrageBobbNaturePrestation.getDateFin(),
              DateUtils.FORMATTER);
      // Si la date fin est avant la date debut on passe au parametrage bobb suivant
      if (dateFin != null && DateUtils.before(dateFin, dateDebut)) {
        continue;
      }
      droitTPExtended.setDateDebut(dateDebut);
      droitTPExtended.setDateFin(dateFin);
    }
  }

  private static boolean calculDroitsTpWorkingExtendedList(
      boolean erreurParametrage,
      List<DroitsTPExtended> listeDroits,
      List<DroitsTPExtended> standardExtendedList,
      List<DroitsTPExtended> workingExtendedList,
      int countSettingsParameterInError) {
    if (CollectionUtils.isEmpty(workingExtendedList)
        && CollectionUtils.isNotEmpty(standardExtendedList)) {
      erreurParametrage = true;
    } else {
      if (countSettingsParameterInError > 0) {
        workingExtendedList.forEach(item -> item.setPapNatureTags(null));
      }
      listeDroits.addAll(workingExtendedList);
    }
    return erreurParametrage;
  }

  private int calcuDroitsTpCarences(
      DroitAssure droitHTP,
      String context,
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
      int countSettingsParameterInError,
      boolean force)
      throws CarenceException, BobbNotFoundException, PwException {
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
                  parametrageAtelierProduitList.getFirst().getCodeOc(),
                  parametrageAtelierProduitList.getFirst().getCodeOffre(),
                  parametrageAtelierProduitList.getFirst().getCodeProduit(),
                  carence.getCode(),
                  carence.getPeriode().getDebut(),
                  carence.getPeriode().getFin(),
                  !GENERATION_DROIT_TP.equals(context));
          List<String> naturePrestationCarence =
              getNaturesPrestationInSettings(parametrageCarenceList);
          countSettingsParameterInError =
              calculDroitsTpParametrageCarence(
                  droitHTP,
                  context,
                  dateFinOnline,
                  gestionDroits.getPeriodeDroitCalcule(),
                  collector,
                  naturePrestationList,
                  workingExtendedList,
                  countSettingsParameterInError,
                  carence,
                  parametrageCarenceList,
                  naturePrestationCarence,
                  force);
        }
      }
    }
    return countSettingsParameterInError;
  }

  private int calculDroitsTpParametrageCarence(
      DroitAssure droitHTP,
      String context,
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
      List<String> naturePrestationCarence,
      boolean force)
      throws BobbNotFoundException, PwException {
    if (CollectionUtils.isEmpty(parametrageCarenceList)) {
      // cas où les paramètrages de carence ne sont pas bonnes, on renvoit le droit
      // sans nature de prestation
      // TODO changer la liste de nature dans droitTPExtended
      countSettingsParameterInError++;
    } else {
      carencing(workingExtendedList, parametrageCarenceList, carence.getPeriode().getFin());

      if (carence.getDroitRemplacement() != null) {
        List<ParametrageBobb> parametrageBobbRemplacementList = callBobb(carence, context);

        if (!GENERATION_DROIT_TP.equals(context)
            && CollectionUtils.isEmpty(parametrageBobbRemplacementList)) {
          DroitsTPExtended droitTPExtended =
              createDroitsTPExtendedBase(droitHTP, periodeDroitCalcule);
          completeDroitsTPExtendedCarence(droitTPExtended, carence);
          workingExtendedList.add(droitTPExtended);
        }

        calculDroitsTpParametrageBobbRemplacement(
            droitHTP,
            context,
            dateFinOnline,
            periodeDroitCalcule,
            collector,
            naturePrestationList,
            workingExtendedList,
            carence,
            parametrageCarenceList,
            naturePrestationCarence,
            parametrageBobbRemplacementList,
            force);
      }
    }
    return countSettingsParameterInError;
  }

  private void calculDroitsTpParametrageBobbRemplacement(
      DroitAssure droitHTP,
      String context,
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
      List<ParametrageBobb> parametrageBobbRemplacementList,
      boolean force)
      throws PwException {
    for (ParametrageBobb parametrageBobbRemplacement : parametrageBobbRemplacementList) {
      for (ParametrageBobbProductElement parametrageBobbProductElementRemplacement :
          parametrageBobbRemplacement.getParametrageBobbProductElements()) {
        Map<String, List<ParametrageBobbNaturePrestation>> natureCarenceFilterListFromBobb =
            parametrageBobbProductElementRemplacement.getNaturePrestation().stream()
                .collect(collector);

        List<ParametrageAtelierProduit> parametrageAtelierProduitRemplacementList =
            callPW(droitHTP, parametrageBobbProductElementRemplacement, context);

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
              periodeDroitCalcule,
              force);
        } else {
          if (!GENERATION_DROIT_TP.equals(context)) {
            DroitsTPExtended droitTPExtended =
                createDroitsTPExtendedBase(droitHTP, periodeDroitCalcule);
            completeDroitsTPExtendedCarence(droitTPExtended, carence);
            completeDroitsTPExtendedParamBobb(
                droitTPExtended, parametrageBobbProductElementRemplacement);
            workingExtendedList.add(droitTPExtended);
          }
        }
      }
    }
  }

  boolean requestPeriodNotIncludedInPeriodPw(
      List<ParametrageAtelierProduit> parametrageAtelierProduitList,
      LocalDate dateDebutDroit,
      LocalDate dateFinDroit) {
    List<String> dateDebutPw =
        parametrageAtelierProduitList.stream()
            .filter(Objects::nonNull)
            .map(ParametrageAtelierProduit::getPwValidityDate)
            .toList();
    Optional<String> optionalDebut =
        dateDebutPw.stream().filter(Objects::nonNull).min(String::compareTo);
    List<String> dateFinPw =
        parametrageAtelierProduitList.stream()
            .filter(Objects::nonNull)
            .map(ParametrageAtelierProduit::getPwEndValidityDate)
            .toList();
    LocalDate dateFinPW = null;
    if (dateFinPw.stream().noneMatch(Objects::isNull)) {
      Optional<String> optionalFin =
          dateFinPw.stream().filter(Objects::nonNull).max(String::compareTo);
      dateFinPW = DateUtils.parse(optionalFin.orElse(null), DateUtils.FORMATTER);
    }
    LocalDate dateDebutPW = DateUtils.parse(optionalDebut.get(), DateUtils.FORMATTER); // NOSONAR

    java.time.Period periodD = java.time.Period.between(dateDebutPW, dateDebutDroit);
    java.time.Period periodF = null;
    if (dateFinPW != null
        && dateFinDroit == null) { // si y a une date de fin sur PW alors que les droits non,
      // c'est que ce n'est pas couvert
      return true;
    }
    if (dateFinPW != null) {
      periodF = java.time.Period.between(dateFinDroit, dateFinPW);
    }
    return periodD.isNegative() || (periodF != null && periodF.isNegative());
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

        if (domaine == null) {
          continue;
        }
        checkOverlapping(
            droitsToCheck, domaineToCheck, debutToCheckGt, finToCheckGt, droitsTPExtended, domaine);
      }
    }
  }

  private static void checkOverlapping(
      DroitsTPExtended droitsToCheck,
      String domaineToCheck,
      LocalDate debutToCheckGt,
      LocalDate finToCheckGt,
      DroitsTPExtended droitsTPExtended,
      String domaine) {
    if (!droitsToCheck.equals(droitsTPExtended) && domaineToCheck.equals(domaine)) {
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

  private void addDroitsIfBobbNotExist(
      DroitAssure droitHTP,
      String context,
      List<DroitsTPExtended> listeDroits,
      Periode periodeDroitCalcule)
      throws BobbNotFoundException {
    DroitsTPExtended droitTPExtended = createDroitsTPExtendedBase(droitHTP, periodeDroitCalcule);

    listeDroits.add(droitTPExtended);

    List<CarenceDroit> carences =
        Objects.requireNonNullElse(droitHTP.getCarences(), new ArrayList<>());
    carences.sort(
        Comparator.comparing(carence -> ((CarenceDroit) carence).getPeriode().getFin()).reversed());
    for (CarenceDroit carence : carences) {
      if (carence.getDroitRemplacement() != null) {
        List<ParametrageBobb> parametrageBobbRemplacementList = callBobb(carence, context);
        if (CollectionUtils.isEmpty(parametrageBobbRemplacementList)) {
          DroitsTPExtended droitTPExtended2 =
              createDroitsTPExtendedBase(droitHTP, periodeDroitCalcule);
          completeDroitsTPExtendedCarence(droitTPExtended2, carence);
          listeDroits.add(droitTPExtended2);
        }
      }
    }
  }

  @ContinueSpan(log = "getParametrageCarenceList")
  public List<ParametrageCarence> getParametrageCarenceList(
      String codeAssureur,
      String codeOffre,
      String codeProduit,
      String codeCarence,
      String dateDebut,
      String dateFin,
      boolean silentException)
      throws CarenceException {
    List<ParametrageCarence> parametrageCarenceList =
        callCarence(codeAssureur, codeOffre, codeProduit, codeCarence, silentException);

    if (CollectionUtils.isEmpty(parametrageCarenceList) && !silentException) {
      TriggeredBeneficiaryAnomaly triggeredBeneficiaryAnomaly =
          TriggeredBeneficiaryAnomaly.create(
              Anomaly.WAITINGS_PERIODES_SETTINGS_NOT_FOUND,
              codeCarence,
              dateDebut,
              dateFin,
              codeAssureur,
              codeOffre,
              codeProduit);
      throw new CarenceException(triggeredBeneficiaryAnomaly);
    }
    if (errorParametrageDatesCarence(dateDebut, dateFin, parametrageCarenceList)) {
      if (!silentException) {
        TriggeredBeneficiaryAnomaly triggeredBeneficiaryAnomaly =
            TriggeredBeneficiaryAnomaly.create(
                Anomaly.WAITINGS_PERIODES_SETTINGS_NOT_FOUND,
                codeCarence,
                dateDebut,
                dateFin,
                codeAssureur,
                codeOffre,
                codeProduit);
        throw new CarenceException(triggeredBeneficiaryAnomaly);
      } else {
        return Collections.emptyList();
      }
    }
    return parametrageCarenceList;
  }

  @ContinueSpan
  public List<ParametrageCarence> getParametrageCarenceListForTrigger(
      String codeAssureur, String codeOffre, String codeProduit, CarenceDroit carence)
      throws CarenceException {
    return getParametrageCarenceList(
        codeAssureur,
        codeOffre,
        codeProduit,
        carence.getCode(),
        carence.getPeriode().getDebut(),
        carence.getPeriode().getFin(),
        false);
  }

  /**
   * Controle que la liste de parametrage couvre entierement la periode en entrée
   *
   * @param dateDebutCarence
   * @param dateFinCarence
   * @param parametrageCarenceList
   * @return
   */
  boolean errorParametrageDatesCarence(
      String dateDebutCarence,
      String dateFinCarence,
      List<ParametrageCarence> parametrageCarenceList) {
    parametrageCarenceList.sort(Comparator.comparing(ParametrageCarence::getDateDebutParametrage));
    boolean isContinuous = checkContinuousPeriod(parametrageCarenceList);
    if (!isContinuous) {
      return false;
    }

    LocalDate dateDebut = DateUtils.stringToDate(dateDebutCarence);
    LocalDate dateFin = DateUtils.stringToDate(dateFinCarence);
    int nbParameterError = parametrageCarenceList.size();
    for (ParametrageCarence parametrageCarence : parametrageCarenceList) {
      LocalDate dateDebutParametrage =
          DateUtils.stringToDate(parametrageCarence.getDateDebutParametrage());
      LocalDate dateFinParametrage =
          DateUtils.stringToDate(parametrageCarence.getDateFinParametrage());
      if (DateUtils.betweenLocalDate(dateDebut, dateDebutParametrage, dateFinParametrage)
          && dateFin != null
          && DateUtils.betweenLocalDate(dateFin, dateDebutParametrage, dateFinParametrage)) {
        nbParameterError--;
      }
    }

    return nbParameterError == parametrageCarenceList.size();
  }

  private boolean checkContinuousPeriod(List<ParametrageCarence> parametrageCarenceList) {
    // check if the period is continuous
    String dateFin = null;
    for (ParametrageCarence parametrageCarence : parametrageCarenceList) {
      if (parametrageCarence.getDateFinParametrage() != null) {
        dateFin = parametrageCarence.getDateFinParametrage();
      } else {
        if (dateFin != null) {
          String dateDebutPlus1 =
              DateUtils.getStringDatePlusDays(
                  parametrageCarence.getDateDebutParametrage(), 1, DateUtils.FORMATTER);
          if (!dateDebutPlus1.equals(dateFin)) {
            return false;
          }
        }
      }
    }
    return true;
  }

  public List<ParametrageAtelierProduit> callPW(
      DroitAssure droitAssure,
      ParametrageBobbProductElement parametrageBobbProductElement,
      String context)
      throws PwException {
    List<ParametrageAtelierProduit> parametrageAtelierProduitList = new ArrayList<>();
    for (ParametrageBobbNaturePrestation paramBobb :
        parametrageBobbProductElement.getNaturePrestation()) {
      List<ParametrageAtelierProduit> pwRes = new ArrayList<>();
      Period periodBobb = new Period(paramBobb.getDateDebutBobb(), paramBobb.getDateFinBobb());
      if (GENERATION_DROIT_TP.equals(context)) {
        pwRes.addAll(
            getParametrageAtelierProduitsFromTpRights(
                droitAssure,
                parametrageBobbProductElement,
                paramBobb.getDateDebut(),
                paramBobb.getDateFin(),
                periodBobb));
      } else {
        pwRes.addAll(
            getParametrageAtelierProduitsFromOffersStructure(
                parametrageBobbProductElement,
                context,
                paramBobb.getDateDebut(),
                paramBobb.getDateFin(),
                periodBobb));
      }

      filterPWResponseNature(paramBobb.getNaturePrestation(), pwRes);
      parametrageAtelierProduitList.addAll(pwRes);
    }
    return parametrageAtelierProduitList;
  }

  /**
   * Filtre la reponse du product workshop pour ne garder que les parametrage en lien avec la
   * naturePrestation demande. Si nature vide ou null alors on accepte toutes les natures
   */
  private void filterPWResponseNature(
      String naturePrestation, List<ParametrageAtelierProduit> pwRes) {
    if (StringUtils.isNotBlank(naturePrestation)) {
      Iterator<ParametrageAtelierProduit> iterator = pwRes.iterator();
      while (iterator.hasNext()) {
        ParametrageAtelierProduit param = iterator.next();
        param.getNaturesTags().removeIf(pap -> !naturePrestation.equals(pap.getNature()));
        if (param.getNaturesTags().isEmpty()) {
          iterator.remove();
        }
      }
    }
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

  private List<ParametrageAtelierProduit> getParametrageAtelierProduitsFromOffersStructure(
      ParametrageBobbProductElement parametrageBobbProductElement,
      String context,
      String dateDebut,
      String dateFin,
      Period periodBobb) {
    List<ParametrageAtelierProduit> parametrageAtelierProduitList = new ArrayList<>();
    try {
      JSONArray offerStructures =
          pwService.getOfferStructure(
              parametrageBobbProductElement.getCodeOc(),
              parametrageBobbProductElement.getCodeOffre(),
              null,
              dateDebut,
              dateFin,
              context,
              "V4");

      if (offerStructures != null) {

        for (int i = 0; i < offerStructures.length(); i++) {
          JSONObject offerStructure = (JSONObject) offerStructures.get(i);

          boolean extractBenefitTypes =
              containsProductCode(offerStructure, parametrageBobbProductElement.getCodeProduit());

          if (extractBenefitTypes) {
            LocalDate offerStart =
                stringToDate(offerStructure.getString(Constants.PW_VALIDITY_DATE));
            LocalDate offerEnd =
                offerStructure.has(Constants.PW_END_VALIDITY_DATE)
                    ? stringToDate(offerStructure.getString(Constants.PW_END_VALIDITY_DATE))
                    : null;

            LocalDate validityDateStart =
                DateUtils.getMaxDate(stringToDate(periodBobb.getStart()), offerStart);
            LocalDate validityDateEnd =
                DateUtils.getMinDate(stringToDate(periodBobb.getEnd()), offerEnd);

            JSONArray natures = offerStructure.getJSONArray("natures");
            List<PAPNatureTags> naturesTags = new ArrayList<>();
            for (int compteur = 0; compteur < natures.length(); compteur++) {
              fillPAPNatureTags(parametrageBobbProductElement, natures, naturesTags, compteur);
            }
            ParametrageAtelierProduit parametrageAtelierProduit = new ParametrageAtelierProduit();
            parametrageAtelierProduit.setCodeProduit(
                parametrageBobbProductElement.getCodeProduit());
            parametrageAtelierProduit.setCodeOffre(parametrageBobbProductElement.getCodeOffre());
            parametrageAtelierProduit.setCodeOc(parametrageBobbProductElement.getCodeOc());
            parametrageAtelierProduit.setNaturesTags(naturesTags);
            parametrageAtelierProduit.setValidityDate(DateUtils.formatDate(validityDateStart));
            parametrageAtelierProduit.setEndValidityDate(DateUtils.formatDate(validityDateEnd));
            parametrageAtelierProduit.setBobbEndValidityDate(periodBobb.getEnd());
            parametrageAtelierProduit.setPwValidityDate(DateUtils.formatDate(offerStart));
            parametrageAtelierProduit.setPwEndValidityDate(DateUtils.formatDate(offerEnd));
            parametrageAtelierProduitList.add(parametrageAtelierProduit);
          }
        }
      }

    } catch (Exception e) {
      // Why this trown exception here ?!?
      log.error(e.getMessage(), e);
      return parametrageAtelierProduitList;
    }
    return parametrageAtelierProduitList;
  }

  private void fillPAPNatureTags(
      ParametrageBobbProductElement parametrageBobbProductElement2,
      JSONArray natures,
      List<PAPNatureTags> naturesTags,
      int compteur) {
    PAPNatureTags natureTags = new PAPNatureTags();
    if ("V5".equals(offerStructureVersion)) {
      JSONObject natureTagsFromJson = natures.getJSONObject(compteur);
      String nature = natureTagsFromJson.getString("natureCode");
      if (natureFromPWInBobb(nature, parametrageBobbProductElement2.getNaturePrestation())) {
        natureTags.setNature(nature);
        List<String> tags = new ArrayList<>();
        if (natureTagsFromJson.has("tags")) {
          for (Object o : natureTagsFromJson.getJSONArray("tags").toList()) {
            tags.add(o.toString());
          }
          natureTags.setTags(tags);
        }
      }
    } else {
      String nature = natures.getString(compteur);
      if (natureFromPWInBobb(nature, parametrageBobbProductElement2.getNaturePrestation())) {
        natureTags.setNature(nature);
      }
    }
    if (StringUtils.isNotBlank(natureTags.getNature())) {
      naturesTags.add(natureTags);
    }
  }

  private static boolean natureFromPWInBobb(
      String natureFromPw, List<ParametrageBobbNaturePrestation> naturePrestationList) {
    ParametrageBobbNaturePrestation parametrageBobbNaturePrestation =
        naturePrestationList.stream()
            .filter(
                aggregatedProductElementBenefitNature1 ->
                    natureFromPw.equals(
                        aggregatedProductElementBenefitNature1.getNaturePrestation()))
            .findFirst()
            .orElse(null);
    return parametrageBobbNaturePrestation != null
        || naturePrestationList.stream()
            .allMatch(
                parametrageBobbNaturePrestation1 ->
                    Constants.NATURE_PRESTATION_VIDE_BOBB.equals(
                        parametrageBobbNaturePrestation1.getNaturePrestation()));
  }

  private LocalDate stringToDate(String dateString) {
    if (StringUtils.isEmpty(dateString)) {
      return null;
    }

    return Util.stringToDate(dateString);
  }

  private boolean containsProductCode(JSONObject offerStructure, String productCode) {
    JSONArray products = offerStructure.getJSONArray("products");

    if (products != null) {
      for (int i = 0; i < products.length(); i++) {
        if (products.getString(i).equals(productCode)) {
          return true;
        }
      }
    }

    return false;
  }

  public List<ParametrageBobb> callBobb(DroitAssure droitHTP, Periode periodeDroit) {
    List<ParametrageBobb> parametrageBobbList = new ArrayList<>();
    ContractElement contractElement =
        getContractElement(droitHTP.getCode(), droitHTP.getCodeAssureur(), false);
    if (contractElement != null) {
      droitHTP.setTriggerBenefContractElement(new TriggerBenefContractElement(contractElement));
      fillBobbParameters(periodeDroit, parametrageBobbList, contractElement);
    }
    return parametrageBobbList;
  }

  private static void fillBobbParameters(
      Periode periode, List<ParametrageBobb> parametrageBobbList, ContractElement contractElement) {
    if (contractElement.getProductElements() != null) {
      ParametrageBobb parametrageBobb = new ParametrageBobb();
      parametrageBobb.setCodeAssureur(contractElement.getCodeInsurer());
      parametrageBobb.setCodeGarantie(contractElement.getCodeAMC());
      parametrageBobb.setParametrageBobbProductElements(new ArrayList<>());
      List<ProductElement> productElements = contractElement.getProductElements();
      for (ProductElement productElement : productElements) {
        if (DateUtils.compareDate(periode.getDebut(), periode.getFin()) <= 0
            && DateUtils.compareDate(
                    periode.getDebut(), DateUtils.formatDateTime(productElement.getTo()))
                <= 0
            && DateUtils.compareDate(
                    periode.getFin(), DateUtils.formatDateTime(productElement.getFrom()))
                >= 0) {
          ParametrageBobbProductElement parametrageBobbProductElement1 =
              parametrageBobb.getParametrageBobbProductElements().stream()
                  .filter(
                      parametrageBobbProductElement ->
                          parametrageBobbProductElement
                                  .getCodeProduit()
                                  .equals(productElement.getCodeProduct())
                              && parametrageBobbProductElement
                                  .getCodeOffre()
                                  .equals(productElement.getCodeOffer())
                              && parametrageBobbProductElement
                                  .getCodeOc()
                                  .equals(productElement.getCodeAmc()))
                  .findFirst()
                  .orElse(null);
          if (parametrageBobbProductElement1 == null) {
            parametrageBobbProductElement1 = new ParametrageBobbProductElement();
            parametrageBobb.getParametrageBobbProductElements().add(parametrageBobbProductElement1);
            parametrageBobbProductElement1.setCodeOc(productElement.getCodeAmc());
            parametrageBobbProductElement1.setCodeOffre(productElement.getCodeOffer());
            parametrageBobbProductElement1.setCodeProduit(productElement.getCodeProduct());
          }
          ParametrageBobbNaturePrestation parametrageBobbNaturePrestation =
              new ParametrageBobbNaturePrestation();
          parametrageBobbNaturePrestation.setNaturePrestation(
              productElement.getCodeBenefitNature());
          parametrageBobbNaturePrestation.setDateDebutBobb(
              DateUtils.formatDateTime(productElement.getFrom()));
          parametrageBobbNaturePrestation.setDateFinBobb(
              DateUtils.formatDateTime(productElement.getTo()));
          parametrageBobbNaturePrestation.setDateDebut(
              DateUtils.getMaxDate(
                  DateUtils.formatDateTime(productElement.getFrom()),
                  periode.getDebut(),
                  DateUtils.FORMATTER));
          parametrageBobbNaturePrestation.setDateFin(
              DateUtils.getMinDate(
                  DateUtils.formatDateTime(productElement.getTo()),
                  periode.getFin(),
                  DateUtils.FORMATTER));
          parametrageBobbProductElement1.getNaturePrestation().add(parametrageBobbNaturePrestation);
        }
      }
      parametrageBobbList.add(parametrageBobb);
    }
  }

  private List<ParametrageBobb> callBobb(CarenceDroit carence, String context)
      throws BobbNotFoundException {
    List<ParametrageBobb> parametrageBobbList = new ArrayList<>();
    ContractElement contractElement =
        getContractElement(
            carence.getDroitRemplacement().getCode(),
            carence.getDroitRemplacement().getCodeAssureur(),
            false);
    if (contractElement != null) {
      carence
          .getDroitRemplacement()
          .setTriggerBenefContractElement(new TriggerBenefContractElement(contractElement));
      fillBobbParameters(
          new Periode(carence.getPeriode().getDebut(), carence.getPeriode().getFin()),
          parametrageBobbList,
          contractElement);
    } else if (GENERATION_DROIT_TP.equals(context)) {
      TriggeredBeneficiaryAnomaly triggeredBeneficiaryAnomaly =
          TriggeredBeneficiaryAnomaly.create(
              Anomaly.BOBB_SETTINGS_NOT_FOUND,
              carence.getDroitRemplacement().getCode(),
              carence.getDroitRemplacement().getCodeAssureur());
      log.debug(triggeredBeneficiaryAnomaly.getDescription());
      throw new BobbNotFoundException(triggeredBeneficiaryAnomaly);
    }

    return parametrageBobbList;
  }

  @ContinueSpan(log = "callCarence")
  public List<ParametrageCarence> callCarence(
      String codeAssureur,
      String codeOffre,
      String codeProduit,
      String codeCarence,
      boolean silentException)
      throws CarenceException {
    try {
      List<ParametrageCarence> parametrageCarenceList =
          carenceService.getParametragesCarence(codeAssureur, codeOffre, codeProduit);
      if (CollectionUtils.isNotEmpty(parametrageCarenceList)) {
        return parametrageCarenceList.stream()
            .filter(parametrageCarence -> parametrageCarence.getCodeCarence().equals(codeCarence))
            .collect(Collectors.toList()); // NOSONAR ImmutableCollections after sort.
      }
    } catch (CarenceException e) {
      if (!silentException) {
        throw e;
      }
    }

    return Collections.emptyList();
  }

  private List<String> getNaturesPrestation(
      List<ParametrageAtelierProduit> parametragesAtelierProduitOffline) {
    return parametragesAtelierProduitOffline.stream()
        .flatMap(
            parametrageAtelierProduit ->
                parametrageAtelierProduit.getNaturesTags().stream().map(PAPNatureTags::getNature))
        .toList();
  }

  private List<String> getNaturesPrestationInSettings(List<ParametrageCarence> parametrageCarence) {
    return parametrageCarence.stream().map(ParametrageCarence::getNaturePrestation).toList();
  }

  private void addDroits(
      List<DroitsTPExtended> listeDroits,
      List<ParametrageAtelierProduit> parametrageAtelierProduitList,
      DroitAssure droitHTP,
      Map<String, List<ParametrageBobbNaturePrestation>> bobbNatureFilter,
      @Nullable CarenceDroit carence,
      @Nullable List<ParametrageCarence> parametrageCarenceList,
      String dateFinOnline,
      Periode periodeDroitCalcule,
      boolean force) {

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
          if (checkCanceledPeriod(force, dateBufferPerBobbParam)) continue;

          DroitsTPExtended droitsTPExtended =
              createDroitsTPExtendedBase(droitHTP, periodeDroitCalcule);
          completeDroitsTPExtendedAtelierProd(droitsTPExtended, parametrageAtelierProduit);
          setDates(periodeDroitCalcule, force, dateBufferPerBobbParam, droitsTPExtended);
          droitsTPExtended.setPapNatureTags(papNatureTags);

          completeFinOnlineAndDroitsWithCarence(
              carence, droitsTPExtended, dateBufferPerBobbParam.dateFinParametrage);
          if (carence != null) {
            droitsTPExtended.setCarencePeriode(periodeCarence);
          }
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

  private static boolean checkCanceledPeriod(boolean force, DateBuffer dateBufferPerBobbParam) {
    return (!force
        && dateBufferPerBobbParam.dateFinForWorking != null
        && DateUtils.before(
            dateBufferPerBobbParam.dateFinForWorking, dateBufferPerBobbParam.dateDebutForWorking));
  }

  private static void setDates(
      Periode periodeDroitCalcule,
      boolean force,
      DateBuffer dateBufferPerBobbParam,
      DroitsTPExtended droitsTPExtended) {
    if (force) {
      droitsTPExtended.setDateDebut(periodeDroitCalcule.getDebut());
      droitsTPExtended.setDateFin(periodeDroitCalcule.getFin());
    } else {
      droitsTPExtended.setDateDebut(dateBufferPerBobbParam.dateDebutForWorking);
      droitsTPExtended.setDateFin(dateBufferPerBobbParam.dateFinForWorking);
      droitsTPExtended.setDateFinOnline(dateBufferPerBobbParam.dateFinOnlineForWorking);
    }
  }

  /**
   * Si la {@link CarenceDroit} est non nulle ecrase la dateFinOnline du {@link DroitsTPExtended}
   * par la plus petite des dates entre dateFinParametrage et carence.getPeriode().getFin().
   * Complete le {@link DroitsTPExtended} avec les donnees presentes dans la {@link CarenceDroit}
   */
  private void completeFinOnlineAndDroitsWithCarence(
      @Nullable CarenceDroit carence,
      DroitsTPExtended droitsTPExtended,
      String dateFinParametrage) {
    if (carence != null) {

      String finOnline =
          DateUtils.getMinDate(
              dateFinParametrage, carence.getPeriode().getFin(), DateUtils.FORMATTER);
      droitsTPExtended.setDateFinOnline(finOnline);

      // BLUE-4652 Lors de la génération des
      // droits TP, les droit liés à la
      // garantie de remplacement ont une
      // date de fin ONLINE au 31/12/2023,
      // alors que la carence se termine le
      // 31/01/2024

      completeDroitsTPExtendedCarence(droitsTPExtended, carence);
    }
  }

  /**
   * Initialise dateDebutForWorking a la plus grande date entre dateDebut et
   * parametrageAtelierProduit.getValidityDate() && Initialise dateFinForWorking a la plus petite
   * date entre dateFin et parametrageAtelierProduit.getEndValidityDate()
   */
  private void completeDateBufferWithAtelierProd(
      DateBuffer dateBuffer, ParametrageAtelierProduit parametrageAtelierProduit) {
    dateBuffer.dateDebutForWorking =
        DateUtils.getMaxDate(
            dateBuffer.dateDebut, parametrageAtelierProduit.getValidityDate(), DateUtils.FORMATTER);
    dateBuffer.dateFinForWorking =
        DateUtils.getMinDate(
            dateBuffer.dateFin,
            parametrageAtelierProduit.getEndValidityDate(),
            DateUtils.FORMATTER);
    dateBuffer.dateFinOnlineForWorking =
        DateUtils.getMinDate(
            dateBuffer.dateFinOnline,
            parametrageAtelierProduit.getEndValidityDate(),
            DateUtils.FORMATTER);
  }

  /**
   * Remplace la dateDebutForWorking par parametrageBobbNaturePrestation.getDateDebut() si celle ci
   * existe et est plus grande && Remplace la dateFinForWorking par
   * parametrageBobbNaturePrestation.getDateFin() si celle ci existe et est plus petite
   */
  private void completeDateBufferWithBobbParams(
      DateBuffer dateBuffer, ParametrageBobbNaturePrestation parametrageBobbNaturePrestation) {
    dateBuffer.dateDebutForWorking =
        DateUtils.getMaxDate(
            dateBuffer.dateDebutForWorking,
            parametrageBobbNaturePrestation.getDateDebut(),
            DateUtils.FORMATTER);
    dateBuffer.dateFinForWorking =
        DateUtils.getMinDate(
            dateBuffer.dateFinForWorking,
            parametrageBobbNaturePrestation.getDateFin(),
            DateUtils.FORMATTER);
  }

  /**
   * Parcourt la liste de carence ayant les meme naturePrestation que l atelierProduit && Remplace
   * dateDebutForWorking par la plus grande date parametrageCarence.getDateDebutParametrage() des
   * carences && Remplace dateFinForWorking par la plus petite date
   * parametrageCarence.getDateFinParametrage() des carences && Initialise dateFinParametrage par la
   * plus petite date parametrageCarence.getDateFinParametrage() des carences
   */
  private PeriodeCarence completeDateBufferWithCarences(
      DateBuffer dateBuffer,
      ParametrageAtelierProduit parametrageAtelierProduit,
      List<ParametrageCarence> parametrageCarenceList) {
    List<ParametrageCarence> parametrageCarenceSubList =
        parametrageCarenceList.stream()
            .filter(
                parametrageCarence ->
                    parametrageAtelierProduit.getNaturesTags().stream()
                        .anyMatch(
                            papNatureTags ->
                                papNatureTags
                                    .getNature()
                                    .equals(parametrageCarence.getNaturePrestation())))
            .toList();
    PeriodeCarence periodeCarence = new PeriodeCarence();
    for (ParametrageCarence parametrageCarence : parametrageCarenceSubList) {
      dateBuffer.dateDebutForWorking =
          DateUtils.getMaxDate(
              dateBuffer.dateDebutForWorking,
              parametrageCarence.getDateDebutParametrage(),
              DateUtils.FORMATTER);
      dateBuffer.dateFinForWorking =
          DateUtils.getMinDate(
              dateBuffer.dateFinForWorking,
              parametrageCarence.getDateFinParametrage(),
              DateUtils.FORMATTER);

      dateBuffer.dateFinParametrage =
          DateUtils.getMinDate(
              dateBuffer.dateFinParametrage,
              parametrageCarence.getDateFinParametrage(),
              DateUtils.FORMATTER);
      periodeCarence.setDebut(parametrageCarence.getDateDebutParametrage());
      periodeCarence.setFin(parametrageCarence.getDateFinParametrage());
    }
    return periodeCarence;
  }

  /**
   * Recupere la liste de paramétrage Bobb ayant soit le meme tag de nature, soit la nature vide ""
   * (qui veut dire toutes les natures) sinon renvoie une liste vide
   */
  private List<ParametrageBobbNaturePrestation> getParametrageBobbNaturePrestations(
      String nature, @NonNull Map<String, List<ParametrageBobbNaturePrestation>> bobbNatureFilter) {
    if (bobbNatureFilter.containsKey(nature)) {
      return bobbNatureFilter.get(nature);
    } else {
      return bobbNatureFilter.getOrDefault(
          Constants.NATURE_PRESTATION_VIDE_BOBB, Collections.emptyList());
    }
  }

  /**
   * Complete un {@link DroitsTPExtended} avec les donnes presentes dans {@link
   * ParametrageAtelierProduit}
   */
  private void completeDroitsTPExtendedAtelierProd(
      DroitsTPExtended droitTPExtended, ParametrageAtelierProduit parametrageAtelierProduit) {
    droitTPExtended.setDetailsOffline(parametrageAtelierProduit.getDetailsOffline());
    droitTPExtended.setDetailsOnline(parametrageAtelierProduit.getDetailsOnline());
    droitTPExtended.setCodeDomaine(parametrageAtelierProduit.getDomaine());
    droitTPExtended.setCodeOffre(parametrageAtelierProduit.getCodeOffre());
    droitTPExtended.setCodeProduit(parametrageAtelierProduit.getCodeProduit());
    droitTPExtended.setVersionOffre(parametrageAtelierProduit.getVersion());
    droitTPExtended.setCodeOc(parametrageAtelierProduit.getCodeOc());
    droitTPExtended.setModeAssemblage(parametrageAtelierProduit.getModeAssemblage());
  }

  /**
   * Complete un {@link DroitsTPExtended} avec les donnees presentes dans le {@link
   * ParametrageBobbProductElement}
   */
  private void completeDroitsTPExtendedParamBobb(
      DroitsTPExtended droitTPExtended,
      ParametrageBobbProductElement parametrageBobbProductElement) {
    droitTPExtended.setCodeOffre(parametrageBobbProductElement.getCodeOffre());
    droitTPExtended.setCodeProduit(parametrageBobbProductElement.getCodeProduit());
    droitTPExtended.setCodeOc(parametrageBobbProductElement.getCodeOc());
  }

  /**
   * Cree un {@link DroitsTPExtended} et initialise ses donnees avec celle du {@link DroitAssure}
   *
   * @return {@link DroitsTPExtended} initialise
   */
  private DroitsTPExtended createDroitsTPExtendedBase(
      DroitAssure droitHTP, Periode periodeDroitCalcule) {
    DroitsTPExtended droitTPExtended = new DroitsTPExtended();
    droitTPExtended.setCodeGarantie(droitHTP.getCode());
    droitTPExtended.setInsurerCode(droitHTP.getCodeAssureur());
    droitTPExtended.setType(droitHTP.getType());
    droitTPExtended.setOrdrePriorisation(droitHTP.getOrdrePriorisation());
    droitTPExtended.setDateAncienneteGarantie(droitHTP.getDateAncienneteGarantie());
    droitTPExtended.setLibelle(droitHTP.getLibelle());
    droitTPExtended.setDateDebut(periodeDroitCalcule.getDebut());
    droitTPExtended.setDateFin(periodeDroitCalcule.getFin());

    return droitTPExtended;
  }

  private void completeDroitsTPExtendedCarence(
      DroitsTPExtended droitTPExtended, CarenceDroit carence) {
    // Settings of original values
    droitTPExtended.setOriginCode(droitTPExtended.getCodeGarantie());
    droitTPExtended.setOriginInsurerCode(droitTPExtended.getInsurerCode());

    // Values from carence
    droitTPExtended.setCodeGarantie(carence.getDroitRemplacement().getCode());
    droitTPExtended.setInsurerCode(carence.getDroitRemplacement().getCodeAssureur());
    droitTPExtended.setCarenceCode(carence.getCode());
  }

  // ----- update listeDroits : pour chaque, si naturePrestation présente dans
  // parametrageCarenceList si dateDebutCarence > dateDebut, alors duplication du
  // droit avec dateFin = dateDebutCarence-1 puis update droit déjà présent avec
  // dateDebut = dateFinCarence+1

  /**
   * Applique les paramétrages d'une carence sur les droits
   *
   * @param listeDroits
   * @param parametrageCarenceList
   * @param dateFinCarence
   */
  void carencing(
      List<DroitsTPExtended> listeDroits,
      List<ParametrageCarence> parametrageCarenceList,
      String dateFinCarence) {
    parametrageCarenceList.sort(
        Comparator.comparing(ParametrageCarence::getDateDebutParametrage).reversed());
    List<DroitsTPExtended> listeDroitsToAdd = new ArrayList<>();
    String dateFinCarenceForWorking;
    for (DroitsTPExtended droitsTPExtended : listeDroits) {
      dateFinCarenceForWorking = dateFinCarence;
      DroitsTPExtended droitsTPExtendedOriginal = droitsTPExtended;
      List<ParametrageCarence> parametrageCarenceSubList =
          parametrageCarenceList.stream()
              .filter(
                  parametrageCarence ->
                      droitsTPExtended
                          .getPapNatureTags()
                          .getNature()
                          .equals(parametrageCarence.getNaturePrestation()))
              .toList();
      for (ParametrageCarence parametrageCarence : parametrageCarenceSubList) {
        dateFinCarenceForWorking =
            DateUtils.getMinDate(
                Objects.requireNonNullElse(
                    parametrageCarence.getDateFinParametrage(), dateFinCarenceForWorking),
                dateFinCarenceForWorking,
                DateUtils.FORMATTER);

        droitsTPExtendedOriginal =
            getDroitsTPExtended(
                listeDroitsToAdd,
                dateFinCarenceForWorking,
                droitsTPExtendedOriginal,
                parametrageCarence);
      }
    }

    listeDroits.removeIf(
        droitsTP ->
            droitsTP.getDateFin() != null
                && DateUtils.before(droitsTP.getDateFin(), droitsTP.getDateDebut()));

    for (DroitsTPExtended droitsToAdd : listeDroitsToAdd) {
      if (droitsToAdd.getDateFin() == null
          || !DateUtils.before(droitsToAdd.getDateFin(), droitsToAdd.getDateDebut())) {
        listeDroits.add(droitsToAdd);
      }
    }
  }

  private DroitsTPExtended getDroitsTPExtended(
      List<DroitsTPExtended> listeDroitsToAdd,
      String dateFinCarenceForWorking,
      DroitsTPExtended droitsTPExtendedOriginal,
      ParametrageCarence parametrageCarence) {
    if (DateUtils.before(
        droitsTPExtendedOriginal.getDateDebut(), parametrageCarence.getDateDebutParametrage())) {
      DroitsTPExtended droitsTPExtendedClone = new DroitsTPExtended(droitsTPExtendedOriginal);
      droitsTPExtendedOriginal.setDateDebut(
          DateUtils.getStringDatePlusDays(dateFinCarenceForWorking, 1, DateUtils.FORMATTER));
      droitsTPExtendedClone.setDateFin(
          DateUtils.getMinDate(
              droitsTPExtendedOriginal.getDateFin(),
              DateUtils.getStringDatePlusDays(
                  parametrageCarence.getDateDebutParametrage(), -1, DateUtils.FORMATTER),
              DateUtils.FORMATTER));
      if (parametrageCarence.getDateDebutParametrage() != null
          && DateUtils.before(
              parametrageCarence.getDateDebutParametrage(),
              droitsTPExtendedOriginal.getDateFin())) {
        droitsTPExtendedClone.setDateFinOnline(droitsTPExtendedClone.getDateFin());
      }
      droitsTPExtendedOriginal = droitsTPExtendedClone;
      droitsTPExtendedOriginal.setCarencePeriode(
          new PeriodeCarence(
              parametrageCarence.getDateDebutParametrage(),
              parametrageCarence.getDateFinParametrage()));
      listeDroitsToAdd.add(droitsTPExtendedOriginal);
    } else {
      String newStartDate =
          DateUtils.getStringDatePlusDays(dateFinCarenceForWorking, 1, DateUtils.FORMATTER);
      // gestion du cas où la date de début requêté par le PAU est supérieur à la fin
      // de la carence.
      if (!DateUtils.before(newStartDate, droitsTPExtendedOriginal.getDateDebut())) {
        droitsTPExtendedOriginal.setDateDebut(newStartDate);
        droitsTPExtendedOriginal.setCarencePeriode(
            new PeriodeCarence(
                parametrageCarence.getDateDebutParametrage(),
                parametrageCarence.getDateFinParametrage()));
      }
    }
    return droitsTPExtendedOriginal;
  }

  private ContractElement getContractElement(
      @NotNull final String codeContractElement,
      @NotNull final String codeInsurer,
      boolean includeIgnored) {
    ContractElement contractElement =
        contractElementService.get(codeContractElement, codeInsurer, includeIgnored);
    if (contractElement == null || !includeIgnored && contractElement.isIgnored()) {
      return null;
    }
    return contractElement;
  }

  /** Classe buffer de multiple dates qui servent au parametrage des droits */
  private static class DateBuffer {
    String dateDebut;
    String dateFin;
    String dateDebutForWorking;
    String dateFinForWorking;
    String dateFinParametrage;
    String dateFinOnlineForWorking;
    String dateFinOnline;

    DateBuffer copy() {
      DateBuffer copyBuffer = new DateBuffer();
      copyBuffer.dateDebut = dateDebut;
      copyBuffer.dateFin = dateFin;
      copyBuffer.dateDebutForWorking = dateDebutForWorking;
      copyBuffer.dateFinForWorking = dateFinForWorking;
      copyBuffer.dateFinParametrage = dateFinParametrage;
      copyBuffer.dateFinOnline = dateFinOnline;
      copyBuffer.dateFinOnlineForWorking = dateFinOnlineForWorking;

      return copyBuffer;
    }
  }
}
