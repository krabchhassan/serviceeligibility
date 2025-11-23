package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.bobb.ContractElement;
import com.cegedim.next.serviceeligibility.core.bobb.TriggerBenefContractElement;
import com.cegedim.next.serviceeligibility.core.bobb.services.ContractElementService;
import com.cegedim.next.serviceeligibility.core.model.domain.carence.ParametrageCarence;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.PeriodeCarence;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.CarenceDroit;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.services.pojo.*;
import com.cegedim.next.serviceeligibility.core.utils.*;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.*;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.Period;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointRequestV5;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CalculDroitsTPPAUService extends CalculDroitsTPCommonService {
  private final String offerStructureVersion;

  public CalculDroitsTPPAUService(
      IPwService pwService,
      ContractElementService contractElementService,
      CarenceService carenceService,
      BeyondPropertiesService beyondPropertiesService) {
    super(pwService, contractElementService, carenceService);
    this.offerStructureVersion =
        beyondPropertiesService
            .getProperty(InstanceProperties.PW_OFFERSTRUCTURE_VERSION)
            .orElse("V4");
  }

  // entrant PAU
  @ContinueSpan(log = "calculDroitsTP (6 params)")
  public List<DroitsTPExtended> calculDroitsTP(
      DroitAssure droitHTP,
      Periode periode,
      String requeteStartDate,
      String requeteEndDate,
      String dateFinOnline,
      UniqueAccessPointRequestV5 requestV5)
      throws CarenceException {
    List<DroitsTPExtended> res = new ArrayList<>();

    String minDate =
        DateUtils.getMaxDate(periode.getDebut(), requeteStartDate, DateUtils.FORMATTER);

    String maxDate = DateUtils.getMinDate(periode.getFin(), requeteEndDate, DateUtils.FORMATTER);
    if (maxDate == null || !DateUtils.before(maxDate, minDate)) {
      res.addAll(calculDroitsTP(droitHTP, dateFinOnline, new Periode(minDate, maxDate), requestV5));
    }

    return res;
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
  protected List<DroitsTPExtended> calculDroitsTP(
      DroitAssure droitHTP,
      String dateFinOnline,
      Periode periodeDroitCalcule,
      UniqueAccessPointRequestV5 requestV5)
      throws CarenceException {
    List<DroitsTPExtended> listeDroits = new ArrayList<>();
    log.debug("start calculating");
    LocalDate dateDebutDroit = DateUtils.parse(periodeDroitCalcule.getDebut(), DateUtils.FORMATTER);
    LocalDate dateFinDroit = DateUtils.parse(periodeDroitCalcule.getFin(), DateUtils.FORMATTER);

    boolean forced = false;
    boolean requirePW = true;
    if (requestV5 != null) {
      forced = Boolean.TRUE.equals(requestV5.getIsForced());
      requirePW = Boolean.TRUE.equals(requestV5.getRequirePW());
    }
    Periode periodeCallBobb = periodeDroitCalcule;
    if (forced) {
      periodeCallBobb = new Periode(requestV5.getStartDate(), requestV5.getEndDate());
    }
    ParametrageBobb parametrageBobb = callBobb(droitHTP, periodeCallBobb);

    if (parametrageBobb == null) {
      addDroitsIfBobbNotExist(droitHTP, listeDroits, periodeDroitCalcule);
    }

    // Si au moins un paramétrage Bobb a été trouvé (pas de NPE car au pire il
    // s'agit d'une liste vide)
    // Pour chaque paramétrage trouvé et pour chaque ProductElement les
    // composants...
    GestionDroits gestionDroits =
        new GestionDroits(listeDroits, dateDebutDroit, dateFinDroit, periodeDroitCalcule);
    if (parametrageBobb != null && requirePW) {
      calculDroitsTpIfParametrageBobbFound(
          droitHTP, dateFinOnline, gestionDroits, parametrageBobb, forced);
    }

    log.debug("end calculating, number of rights returned : {}", listeDroits.size());
    return listeDroits;
  }

  private void calculDroitsTpIfParametrageBobbFound(
      DroitAssure droitHTP,
      String dateFinOnline,
      GestionDroits gestionDroits,
      ParametrageBobb parametrageBobb,
      boolean force)
      throws CarenceException {
    boolean erreurParametrage = false;
    Periode periodeDroitCalcule = gestionDroits.getPeriodeDroitCalcule();
    List<DroitsTPExtended> listeDroits = gestionDroits.getListeDroits();

    if (CollectionUtils.isEmpty(parametrageBobb.getParametrageBobbProductElements())) {
      addDroitsIfBobbNotExist(droitHTP, listeDroits, periodeDroitCalcule);
    }

    for (ParametrageBobbProductElement parametrageBobbProductElement :
        parametrageBobb.getParametrageBobbProductElements()) {
      List<DroitsTPExtended> standardExtendedList = new ArrayList<>();
      List<ParametrageAtelierProduit> parametrageAtelierProduitList =
          callPW(parametrageBobbProductElement);

      // Si on a une réponse de l'Atelier Produit...
      if (CollectionUtils.isNotEmpty(parametrageAtelierProduitList)) {

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
                countSettingsParameterInError,
                force);
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
      // Si on n'a pas eu de réponse de l'Atelier Produit
      else {
        DroitsTPExtended droitTPExtended =
            createDroitsTPExtendedBase(droitHTP, periodeDroitCalcule);
        completeDroitsTPExtendedParamBobb(droitTPExtended, parametrageBobbProductElement);
        computePeriodeDroitsTPExtendedWithParamBobb(parametrageBobbProductElement, droitTPExtended);
        listeDroits.add(droitTPExtended);
      }
    }
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
      int countSettingsParameterInError,
      boolean force)
      throws CarenceException {
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
                  true);
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
                  naturePrestationCarence,
                  force);
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
      List<String> naturePrestationCarence,
      boolean force) {
    if (CollectionUtils.isEmpty(parametrageCarenceList)) {
      // cas où les paramètrages de carence ne sont pas bonnes, on renvoit le droit
      // sans nature de prestation
      // TODO changer la liste de nature dans droitTPExtended
      countSettingsParameterInError++;
    } else {
      carencing(workingExtendedList, parametrageCarenceList, carence.getPeriode().getFin());

      if (carence.getDroitRemplacement() != null) {
        ParametrageBobb parametrageBobbRemplacement = callBobb(carence);

        if (parametrageBobbRemplacement == null
            || CollectionUtils.isEmpty(
                parametrageBobbRemplacement.getParametrageBobbProductElements())) {
          DroitsTPExtended droitTPExtended =
              createDroitsTPExtendedBase(droitHTP, periodeDroitCalcule);
          completeDroitsTPExtendedCarence(droitTPExtended, carence);
          workingExtendedList.add(droitTPExtended);

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
              parametrageBobbRemplacement,
              force);
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
      ParametrageBobb parametrageBobbRemplacement,
      boolean force) {
    for (ParametrageBobbProductElement parametrageBobbProductElementRemplacement :
        parametrageBobbRemplacement.getParametrageBobbProductElements()) {
      Map<String, List<ParametrageBobbNaturePrestation>> natureCarenceFilterListFromBobb =
          parametrageBobbProductElementRemplacement.getNaturePrestation().stream()
              .collect(collector);

      List<ParametrageAtelierProduit> parametrageAtelierProduitRemplacementList =
          callPW(parametrageBobbProductElementRemplacement);

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
        DroitsTPExtended droitTPExtended =
            createDroitsTPExtendedBase(droitHTP, periodeDroitCalcule);
        completeDroitsTPExtendedCarence(droitTPExtended, carence);
        completeDroitsTPExtendedParamBobb(
            droitTPExtended, parametrageBobbProductElementRemplacement);
        workingExtendedList.add(droitTPExtended);
      }
    }
  }

  private void addDroitsIfBobbNotExist(
      DroitAssure droitHTP, List<DroitsTPExtended> listeDroits, Periode periodeDroitCalcule) {
    DroitsTPExtended droitTPExtended = createDroitsTPExtendedBase(droitHTP, periodeDroitCalcule);

    listeDroits.add(droitTPExtended);

    List<CarenceDroit> carences =
        Objects.requireNonNullElse(droitHTP.getCarences(), new ArrayList<>());
    carences.sort(
        Comparator.comparing(carence -> ((CarenceDroit) carence).getPeriode().getFin()).reversed());
    for (CarenceDroit carence : carences) {
      if (carence.getDroitRemplacement() != null) {
        ParametrageBobb parametrageBobbRemplacement = callBobb(carence);
        if (parametrageBobbRemplacement == null) {
          DroitsTPExtended droitTPExtended2 =
              createDroitsTPExtendedBase(droitHTP, periodeDroitCalcule);
          completeDroitsTPExtendedCarence(droitTPExtended2, carence);
          listeDroits.add(droitTPExtended2);
        }
      }
    }
  }

  public List<ParametrageAtelierProduit> callPW(
      ParametrageBobbProductElement parametrageBobbProductElement) {
    List<ParametrageAtelierProduit> parametrageAtelierProduitList = new ArrayList<>();
    for (ParametrageBobbNaturePrestation paramBobb :
        parametrageBobbProductElement.getNaturePrestation()) {
      Period periodBobb = new Period(paramBobb.getDateDebutBobb(), paramBobb.getDateFinBobb());
      List<ParametrageAtelierProduit> pwRes =
          new ArrayList<>(
              getParametrageAtelierProduitsFromOffersStructure(
                  parametrageBobbProductElement,
                  paramBobb.getDateDebut(),
                  paramBobb.getDateFin(),
                  periodBobb));

      filterPWResponseNature(paramBobb.getNaturePrestation(), pwRes);
      parametrageAtelierProduitList.addAll(pwRes);
    }
    return parametrageAtelierProduitList;
  }

  private List<ParametrageAtelierProduit> getParametrageAtelierProduitsFromOffersStructure(
      ParametrageBobbProductElement parametrageBobbProductElement,
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
              ContextConstants.HTP,
              "V4");

      if (offerStructures != null) {

        for (int i = 0; i < offerStructures.length(); i++) {
          JSONObject offerStructure = (JSONObject) offerStructures.get(i);

          boolean extractBenefitTypes =
              containsProductCode(offerStructure, parametrageBobbProductElement.getCodeProduit());

          if (extractBenefitTypes) {
            LocalDate offerStart = getDateFromOffer(offerStructure, Constants.PW_VALIDITY_DATE);
            LocalDate offerEnd = getDateFromOffer(offerStructure, Constants.PW_END_VALIDITY_DATE);

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

    } catch (GenericNotFoundException e) {
      log.debug(e.getMessage());
    } catch (Exception e) {
      // Why this thrown exception here ?!?
      log.error(e.getMessage(), e);
    }
    return parametrageAtelierProduitList;
  }

  @org.jetbrains.annotations.Nullable
  private LocalDate getDateFromOffer(JSONObject offerStructure, String pwValidityDate) {
    return offerStructure.has(pwValidityDate)
        ? stringToDate(offerStructure.getString(pwValidityDate))
        : null;
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

  public ParametrageBobb callBobb(DroitAssure droitHTP, Periode periodeDroit) {
    ContractElement contractElement =
        getContractElement(droitHTP.getCode(), droitHTP.getCodeAssureur());
    if (contractElement != null) {
      droitHTP.setTriggerBenefContractElement(new TriggerBenefContractElement(contractElement));
      return fillBobbParameters(periodeDroit, contractElement);
    }
    return null;
  }

  private ParametrageBobb callBobb(CarenceDroit carence) {
    ParametrageBobb parametrageBobb = null;
    ContractElement contractElement =
        getContractElement(
            carence.getDroitRemplacement().getCode(),
            carence.getDroitRemplacement().getCodeAssureur());
    if (contractElement != null) {
      carence
          .getDroitRemplacement()
          .setTriggerBenefContractElement(new TriggerBenefContractElement(contractElement));
      parametrageBobb =
          fillBobbParameters(
              new Periode(carence.getPeriode().getDebut(), carence.getPeriode().getFin()),
              contractElement);
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
          if (!force && bufferDatesInverse(dateBufferPerBobbParam)) {
            continue;
          }

          DroitsTPExtended droitsTPExtended =
              createDroitsTPExtendedBase(droitHTP, periodeDroitCalcule);
          completeDroitsTPExtendedAtelierProd(droitsTPExtended, parametrageAtelierProduit);
          completeDroitsTPExtendedPeriods(
              periodeDroitCalcule, force, droitsTPExtended, dateBufferPerBobbParam);
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

  private static void completeDroitsTPExtendedPeriods(
      Periode periodeDroitCalcule,
      boolean force,
      DroitsTPExtended droitsTPExtended,
      DateBuffer dateBufferPerBobbParam) {
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
}
