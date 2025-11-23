package com.cegedim.next.serviceeligibility.core.services.trigger;

import static com.cegedim.next.serviceeligibility.core.utils.Constants.*;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ParametresDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddService;
import com.cegedim.next.serviceeligibility.core.mapper.trigger.TriggerMapper;
import com.cegedim.next.serviceeligibility.core.model.domain.*;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.DetailDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.pw.TpOfflineRightsDetails;
import com.cegedim.next.serviceeligibility.core.model.domain.pw.TpOnlineRightsDetails;
import com.cegedim.next.serviceeligibility.core.model.domain.pw.Variable;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.Anomaly;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.Trigger;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiary;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiaryAnomaly;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.services.CalculDroitsTPGenerationService;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarantService;
import com.cegedim.next.serviceeligibility.core.services.pojo.DatesForDomaine;
import com.cegedim.next.serviceeligibility.core.services.pojo.DroitsTPExtended;
import com.cegedim.next.serviceeligibility.core.services.pojo.GenerationDomaineResult;
import com.cegedim.next.serviceeligibility.core.services.pojo.ParametrageTrigger;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.TriggerUtils;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.*;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.TriggerWarningException;
import io.micrometer.tracing.annotation.ContinueSpan;
import jakarta.annotation.Nullable;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TriggerDomaineServiceImpl implements TriggerDomaineService {

  @Autowired private TriggerMapper triggerMapper;

  @Autowired CalculDroitsTPGenerationService calculDroitsTPGenerationService;

  @Autowired private ParametreBddService parametreBddService;

  @Autowired private DeclarantService declarantService;

  @Override
  @ContinueSpan(log = "generationDomaine")
  public GenerationDomaineResult generationDomaine(
      TriggeredBeneficiary triggeredBeneficiary,
      ParametrageCarteTP parametrageCarteTp,
      Trigger trigger,
      String dateFermeture,
      boolean isContratResiliatedFromTheBeginning,
      List<DroitsTPExtended> droitsTPExtendedList)
      throws TriggerWarningException,
          BobbNotFoundException,
          PwException,
          CarenceException,
          TriggerParametersException,
          BeneficiaryToIgnoreException {
    List<DetailDroit> detailsDroitParametrage =
        parametrageCarteTp.getParametrageDroitsCarteTP().getDetailsDroit();
    List<String> listDomainesParametrage = new ArrayList<>();
    if (detailsDroitParametrage != null) {
      listDomainesParametrage =
          detailsDroitParametrage.stream()
              .sorted(Comparator.comparing(DetailDroit::getOrdreAffichage))
              .map(DetailDroit::getCodeDomaineTP)
              .toList();
    }
    ParametrageTrigger parametrageTrigger = new ParametrageTrigger();
    parametrageTrigger.setTriggeredBeneficiary(triggeredBeneficiary);
    parametrageTrigger.setParametrageCarteTP(parametrageCarteTp);
    parametrageTrigger.setOrigine(trigger.getOrigine());
    parametrageTrigger.setDateEffet(trigger.getDateEffet());
    parametrageTrigger.setListDomainesParametrage(listDomainesParametrage);
    parametrageTrigger.setRdo(trigger.isRdo());
    parametrageTrigger.setEventReprise(trigger.isEventReprise());
    parametrageTrigger.setDateSouscription(
        triggeredBeneficiary.getNewContract().getDateSouscription());

    List<DomaineDroit> domainesDeclaration = new ArrayList<>();

    boolean isBeneficiaryToIgnoreException = false;
    boolean isWarning = true;
    for (DroitAssure droitHTP : triggeredBeneficiary.getNewContract().getDroitsGaranties()) {
      DatesForDomaine datesForDomaine =
          getDatesForDomaine(
              dateFermeture, droitHTP, parametrageTrigger, isContratResiliatedFromTheBeginning);
      isWarning = datesForDomaine.isWarning();
      if (datesForDomaine.getRequeteEndDate() == null
          || !DateUtils.before(
              datesForDomaine.getRequeteEndDate(), datesForDomaine.getRequeteStartDate())) {
        try {
          List<DroitsTPExtended> extendedList =
              calculDroitsTPGenerationService.calculDroitsTP(
                  droitHTP,
                  datesForDomaine.getRequeteStartDate(),
                  datesForDomaine.getRequeteEndDate(),
                  datesForDomaine.getDateFinOnline());
          if (CollectionUtils.isNotEmpty(extendedList)) {
            droitsTPExtendedList.addAll(extendedList);
            List<DomaineDroit> domainesToAdd =
                mapFromExtendedRightsToDomainesDroits(extendedList, parametrageTrigger);
            domainesDeclaration.addAll(domainesToAdd);
          }
        } catch (BeneficiaryToIgnoreException e) {
          isBeneficiaryToIgnoreException = true;
        }
      }
    }

    if (isBeneficiaryToIgnoreException && CollectionUtils.isEmpty(domainesDeclaration)) {
      throw new BeneficiaryToIgnoreException();
    }
    if (detailsDroitParametrage != null) {
      setNoOrdreAndSortDomaines(domainesDeclaration, detailsDroitParametrage);
    }

    return new GenerationDomaineResult(domainesDeclaration, isWarning);
  }

  private void setNoOrdreAndSortDomaines(
      List<DomaineDroit> domainesDeclaration, List<DetailDroit> sortedDetailsDroit) {
    Map<String, List<DomaineDroit>> domainsByCode =
        domainesDeclaration.stream().collect(Collectors.groupingBy(DomaineDroit::getCode));

    // JIRA BLUE-5457
    // L indice doit commencer a 1 et non a 0
    int indice = 1;
    for (DetailDroit detailDroit : sortedDetailsDroit) {
      String code = detailDroit.getCodeDomaineTP();
      // Domaine parametres sur le ParametrageDroitsCarteTP
      if (domainsByCode.containsKey(code)) {
        for (DomaineDroit domaineDroit : domainsByCode.get(code)) {
          domaineDroit.setNoOrdreDroit(indice);
        }
        domainsByCode.remove(code);
        indice++;
      }
    }

    // Domaine restant non parametres sur le ParametrageDroitsCarteTP
    for (List<DomaineDroit> domaineDroits : domainsByCode.values()) {
      for (DomaineDroit domaineDroit : domaineDroits) {
        domaineDroit.setNoOrdreDroit(indice);
      }
      indice++;
    }

    domainesDeclaration.sort(
        Comparator.comparing(DomaineDroit::getNoOrdreDroit)
            .thenComparing(domaineDroit -> domaineDroit.getPrioriteDroit().getCode()));
  }

  static DatesForDomaine getDatesForDomaine(
      String dateFermeture,
      DroitAssure droitHTP,
      ParametrageTrigger parametrageTrigger,
      boolean isContratResiliatedFromTheBeginning) {
    DatesForDomaine datesForDomaine = new DatesForDomaine();
    boolean includeFinDroits =
        StringUtils.isBlank(dateFermeture)
            || StringUtils.isBlank(droitHTP.getPeriode().getFin())
            || DateUtils.before(
                droitHTP.getPeriode().getFin(),
                dateFermeture.replace("/", "-"),
                DateUtils.FORMATTER);
    List<String> dates = TriggerUtils.calculDates(parametrageTrigger, droitHTP, includeFinDroits);

    datesForDomaine.setDateFinOnline(getDateFinOnline(dateFermeture, droitHTP));
    String dateFermetureWithDash = null;
    if (dateFermeture != null) {
      dateFermetureWithDash = dateFermeture.replace("/", "-");
    }
    String maxDateDebut = dates.getFirst();
    String minDateFin = dates.getLast();

    if (isContratResiliatedFromTheBeginning && dateFermetureWithDash != null) {
      minDateFin = DateUtils.getMinDate(minDateFin, dateFermetureWithDash, DateUtils.FORMATTER);
    }
    if (minDateFin != null && !DateUtils.before(minDateFin, maxDateDebut)) {
      datesForDomaine.setWarning(false);
    }
    if (parametrageTrigger.isEventReprise()) {
      Periode periodeReprise =
          TriggerUtils.calculPeriodeReprise(
              parametrageTrigger.getParametrageCarteTP(),
              parametrageTrigger.getTriggeredBeneficiary().getNewContract().getDateSouscription(),
              minDateFin);
      maxDateDebut =
          DateUtils.getMaxDate(dates.get(0), periodeReprise.getDebut(), DateUtils.FORMATTER);
      minDateFin = periodeReprise.getFin();
      if (minDateFin != null && !DateUtils.before(minDateFin, maxDateDebut)) {
        datesForDomaine.setWarning(false);
      }
    }
    datesForDomaine.setRequeteEndDate(minDateFin);
    datesForDomaine.setRequeteStartDate(maxDateDebut);
    return datesForDomaine;
  }

  private static String getDateFinOnline(String dateFermeture, DroitAssure droitHTP) {
    return TriggerUtils.getDateFinOnline(dateFermeture, droitHTP.getPeriode().getFin());
  }

  private static String getMinDateFin(List<String> dates, String dateResilOrRadiation) {
    String minDateFin = CollectionUtils.isNotEmpty(dates) && dates.size() > 1 ? dates.get(1) : null;
    return DateUtils.getMinDate(Arrays.asList(minDateFin, dateResilOrRadiation));
  }

  // todo à tester (puis intégrer dans la génération de domaine avec point
  // d'attention sur les dates pour obtenir extendedList
  private synchronized List<DomaineDroit> mapFromExtendedRightsToDomainesDroits(
      List<DroitsTPExtended> extendedList, ParametrageTrigger parametrageTrigger)
      throws TriggerParametersException {

    ParametrageCarteTP parametrageCarteTP = parametrageTrigger.getParametrageCarteTP();
    TriggeredBeneficiary triggeredBeneficiary = parametrageTrigger.getTriggeredBeneficiary();

    List<DetailDroit> detailsDroit =
        parametrageCarteTP.getParametrageDroitsCarteTP().getDetailsDroit();
    Map<String, DetailDroit> mappedDetailsDroit = new HashMap<>();
    if (CollectionUtils.isNotEmpty(detailsDroit)) {
      detailsDroit.sort(Comparator.comparingInt(DetailDroit::getOrdreAffichage));
      synchronized (this) {
        mappedDetailsDroit =
            detailsDroit.stream()
                .collect(Collectors.toMap(DetailDroit::getCodeDomaineTP, Function.identity()));
      }
    }

    return mapDroitTPExtentedToDomaineDroits(
        extendedList, parametrageCarteTP, triggeredBeneficiary, mappedDetailsDroit);
  }

  private List<DomaineDroit> mapDroitTPExtentedToDomaineDroits(
      List<DroitsTPExtended> extendedList,
      ParametrageCarteTP parametrageCarteTP,
      TriggeredBeneficiary triggeredBeneficiary,
      Map<String, DetailDroit> mappedDetailsDroit)
      throws TriggerParametersException {
    List<DomaineDroit> domaineDroitList = new ArrayList<>();
    Declarant declarant = declarantService.findById(parametrageCarteTP.getAmc());
    for (DroitsTPExtended droitsTPExtended : extendedList) {
      mapDetailByDomainRightToDomaineDroit(
          parametrageCarteTP,
          triggeredBeneficiary,
          mappedDetailsDroit,
          domaineDroitList,
          droitsTPExtended,
          declarant);
    }
    return domaineDroitList;
  }

  private void mapDetailByDomainRightToDomaineDroit(
      ParametrageCarteTP parametrageCarteTP,
      TriggeredBeneficiary triggeredBeneficiary,
      Map<String, DetailDroit> mappedDetailsDroit,
      List<DomaineDroit> domaineDroitList,
      DroitsTPExtended droitsTPExtended,
      Declarant declarant)
      throws TriggerParametersException {
    TpOfflineRightsDetails tpOfflineRightsDetails = droitsTPExtended.getDetailsOffline();
    DomaineDroit domaineDroit = new DomaineDroit();
    domaineDroit.setNaturePrestation(tpOfflineRightsDetails.getNature());
    TpOnlineRightsDetails detailsByDomainRightsOnline = droitsTPExtended.getDetailsOnline();
    if (detailsByDomainRightsOnline != null
        && StringUtils.isNotBlank(detailsByDomainRightsOnline.getNature())) {
      domaineDroit.setNaturePrestationOnline(detailsByDomainRightsOnline.getNature());
    }

    LocalDate dateDebutDroitTP = DateUtils.stringToDate(droitsTPExtended.getDateDebut());
    String codeDomaine = droitsTPExtended.getCodeDomaine();
    DetailDroit detailDroit = mappedDetailsDroit.get(codeDomaine);
    domaineDroit.setCode(codeDomaine);
    List<ConventionTP> conventionTPList = declarant != null ? declarant.getConventionTP() : null;

    populateConventions(
        detailDroit,
        domaineDroit,
        parametrageCarteTP,
        detailsByDomainRightsOnline,
        conventionTPList,
        dateDebutDroitTP);

    if (detailDroit == null
        || (Boolean.FALSE.equals(triggeredBeneficiary.getIsCartePapierAEditer())
            && Boolean.FALSE.equals(triggeredBeneficiary.getIsCarteDematerialisee()))
        || (triggeredBeneficiary.getIsCartePapierAEditer() == null
            && !parametrageCarteTP.getParametrageDroitsCarteTP().getIsCarteDematerialisee()
            && !parametrageCarteTP.getParametrageDroitsCarteTP().getIsCarteEditablePapier())) {
      domaineDroit.setIsEditable(false); // BLUE-4521 && BLUE-7218
    }

    domaineDroit.setPeriodeProductElement(droitsTPExtended.getPeriodeProductElement());
    domaineDroit.setPeriodePW(droitsTPExtended.getPeriodePW());
    domaineDroit.setCodeProduit(droitsTPExtended.getCodeProduit());
    domaineDroit.setCodeGarantie(droitsTPExtended.getCodeGarantie());
    domaineDroit.setLibelleGarantie(droitsTPExtended.getLibelle());

    domaineDroit.setIsSuspension(SUSPENSION.equals(triggeredBeneficiary.getEtatSuspension()));

    if (StringUtils.isNotEmpty(
        triggeredBeneficiary.getNewContract().getDateDebutAdhesionIndividuelle())) {
      domaineDroit.setDateAdhesionCouverture(
          triggeredBeneficiary
              .getNewContract()
              .getDateDebutAdhesionIndividuelle()
              .replace("-", "/"));
    }
    triggerMapper.mapTaux(domaineDroit, tpOfflineRightsDetails);
    if (StringUtils.isNotBlank(tpOfflineRightsDetails.getStsFormulaCode())) {
      if (!CollectionUtils.isEmpty(tpOfflineRightsDetails.getVariables())) {
        domaineDroit.setReferenceCouverture(
            codeDomaine
                + "-"
                + tpOfflineRightsDetails.getStsFormulaCode()
                + "-"
                + tpOfflineRightsDetails.getVariables().get(0).getValue());
      } else {
        domaineDroit.setReferenceCouverture(
            codeDomaine + "-" + tpOfflineRightsDetails.getStsFormulaCode());
      }
    } else {
      domaineDroit.setReferenceCouverture(codeDomaine + "-NOSEL");
    }

    domaineDroit.setCodeOc(droitsTPExtended.getCodeOc());

    domaineDroit.setCategorie(codeDomaine);
    domaineDroit.setFormulaMask(tpOfflineRightsDetails.getFormulaMask());
    domaineDroit.setCodeAssureurGarantie(droitsTPExtended.getInsurerCode());
    domaineDroit.setCodeOffre(droitsTPExtended.getCodeOffre());
    domaineDroit.setVersionOffre(droitsTPExtended.getVersionOffre());
    domaineDroit.setModeAssemblage(droitsTPExtended.getModeAssemblage());

    List<CodeRenvoiTP> codeRenvoiTPList = declarant != null ? declarant.getCodeRenvoiTP() : null;
    populateCodeRenvoi(
        domaineDroit, codeRenvoiTPList, detailDroit, detailsByDomainRightsOnline, dateDebutDroitTP);

    PrioriteDroit prio = new PrioriteDroit();

    String prioTspd =
        droitsTPExtended.getOrdrePriorisation() == null
            ? "01"
            : StringUtils.substring(
                StringUtils.leftPad(droitsTPExtended.getOrdrePriorisation(), 2, "0"), -2);
    prio.setCode(prioTspd);
    prio.setLibelle(prioTspd);
    prio.setTypeDroit(prioTspd);
    prio.setPrioriteBO(prioTspd);
    domaineDroit.setPrioriteDroit(prio);

    Prestation prest = new Prestation();
    prest.setCode("DEF");
    prest.setIsEditionRisqueCarte(false);
    Formule form = new Formule();
    form.setNumero(tpOfflineRightsDetails.getStsFormulaCode());
    List<Parametre> params = new ArrayList<>();
    if (!CollectionUtils.isEmpty(tpOfflineRightsDetails.getVariables())) {
      for (Variable variable : tpOfflineRightsDetails.getVariables()) {
        Parametre param = new Parametre();
        param.setNumero(String.valueOf(variable.getStsVariableNumber()));
        param.setValeur(variable.getValue());
        params.add(param);
      }
    }
    form.setParametres(params);
    prest.setFormule(form);
    domaineDroit.setPrestations(List.of(prest));

    PeriodeDroit period = new PeriodeDroit();
    triggerMapper.setPeriodDetails(
        period, droitsTPExtended.getDateDebut(), droitsTPExtended.getDateFin());
    domaineDroit.setPeriodeDroit(period);
    if (droitsTPExtended.getDetailsOnline() != null) {
      PeriodeDroit periodeOnline = new PeriodeDroit();
      periodeOnline.setPeriodeDebut(period.getPeriodeDebut());
      // BLUE-4639 BLUE-4653
      if (StringUtils.isNotBlank(droitsTPExtended.getDateFinOnline())) {
        periodeOnline.setPeriodeFin(droitsTPExtended.getDateFinOnline().replace("-", "/"));
      }
      // fin BLUE-4639 et BLUE-4653

      domaineDroit.setPeriodeOnline(periodeOnline);
    }

    domaineDroit.setCodeCarence(droitsTPExtended.getCarenceCode());
    domaineDroit.setCodeOrigine(droitsTPExtended.getOriginCode());
    domaineDroit.setCodeAssureurOrigine(droitsTPExtended.getOriginInsurerCode());

    domaineDroit.setPeriodeCarence(droitsTPExtended.getCarencePeriode());

    domaineDroitList.add(domaineDroit);
  }

  public void populateCodeRenvoi(
      DomaineDroit domaineDroit,
      List<CodeRenvoiTP> codeRenvoiTPList,
      DetailDroit detailDroit,
      TpOnlineRightsDetails tpOnlineRightsDetails,
      LocalDate dateDebutDroitTP) {
    String reseauPW = tpOnlineRightsDetails != null ? tpOnlineRightsDetails.getNetwork() : null;
    if (detailDroit != null && detailDroit.getCodeRenvoiAction() != null) {
      switch (detailDroit.getCodeRenvoiAction()) {
        case REMPLACER:
          domaineDroit.setCodeRenvoi(detailDroit.getCodeRenvoi());
          break;
        case COMPLETER:
          if (CollectionUtils.isNotEmpty(codeRenvoiTPList)) {
            setCodeRenvoi(domaineDroit, codeRenvoiTPList, detailDroit, reseauPW, dateDebutDroitTP);
            // Si aucun paramétrage au niveau de l'AMC, on positionne le renvoi du
            // paramétrage de génération droits TP. Sinon, on l'ajoute dans
            // codeRenvoiAdditionnel
            if (StringUtils.isBlank(domaineDroit.getCodeRenvoi())) {
              domaineDroit.setCodeRenvoi(detailDroit.getCodeRenvoi());
            } else {
              domaineDroit.setCodeRenvoiAdditionnel(detailDroit.getCodeRenvoi());
            }
          }
          break;
        case GARDER:
          if (CollectionUtils.isNotEmpty(codeRenvoiTPList)) {
            setCodeRenvoi(domaineDroit, codeRenvoiTPList, detailDroit, reseauPW, dateDebutDroitTP);
          }
          break;
        case INHIBER:
        default:
          break;
      }
    }

    domaineDroit.setLibelleCodeRenvoi(
        parametreBddService.findLibelleCodeRenvoi(domaineDroit.getCodeRenvoi()));
    domaineDroit.setLibelleCodeRenvoiAdditionnel(
        parametreBddService.findLibelleCodeRenvoi(domaineDroit.getCodeRenvoiAdditionnel()));
  }

  private boolean checkCodeRenvoiPeriodeAndDomaineTP(
      DetailDroit detailDroit, LocalDate dateDebutDroitTP, CodeRenvoiTP codeRenvoiTP) {
    return DateUtils.isPeriodeValide(codeRenvoiTP.getDateDebut(), codeRenvoiTP.getDateFin())
        && Objects.equals(codeRenvoiTP.getDomaineTP(), detailDroit.getCodeDomaineTP())
        && !dateDebutDroitTP.isBefore(codeRenvoiTP.getDateDebut().toLocalDate())
        && (codeRenvoiTP.getDateFin() == null
            || !dateDebutDroitTP.isAfter(codeRenvoiTP.getDateFin().toLocalDate()));
  }

  private void setCodeRenvoi(
      DomaineDroit domaineDroit,
      List<CodeRenvoiTP> codeRenvoiTPList,
      DetailDroit detailDroit,
      String reseauPW,
      LocalDate dateDebutDroitTP) {
    if (StringUtils.isNotBlank(reseauPW)) {
      codeRenvoiTPList.stream()
          .filter(
              codeRenvoiTP ->
                  checkCodeRenvoiPeriodeAndDomaineTP(detailDroit, dateDebutDroitTP, codeRenvoiTP)
                      && StringUtils.isNotBlank(codeRenvoiTP.getReseauSoin())
                      && reseauPW.equals(codeRenvoiTP.getReseauSoin()))
          .map(CodeRenvoiTP::getCodeRenvoi)
          .findFirst()
          .ifPresent(domaineDroit::setCodeRenvoi);
      if (StringUtils.isBlank(domaineDroit.getCodeRenvoi())) {
        codeRenvoiTPList.stream()
            .filter(
                codeRenvoiTP ->
                    checkCodeRenvoiPeriodeAndDomaineTP(detailDroit, dateDebutDroitTP, codeRenvoiTP)
                        && StringUtils.isBlank(codeRenvoiTP.getReseauSoin()))
            .map(CodeRenvoiTP::getCodeRenvoi)
            .findFirst()
            .ifPresent(domaineDroit::setCodeRenvoi);
      }
    }
  }

  /**
   * Si la convention est saisie sur le paramétrage de génération des droits TP, au niveau du
   * domaine TP, alors on positionne cette convention. Sinon on va rechercher le réseau de soin issu
   * de l’atelier produit. Si un paramétrage existe dans la gestion des AMCs pour le domaine TP et
   * le réseau de soin, alors on récupère la valeur de la convention paramétré. Sinon on va
   * recherche l’existence d’un paramétrage dans la gestion des AMCs pour le domaine TP (sans
   * précision de réseau de soin). Elle sera concaténé ou non avec la convention d’en-tête en
   * fonction de la valeur de la case à cocher demandant la concaténation. Si aucun paramétrage
   * spécifique (au niveau du paramétrage de génération de droits TP, au niveau de l’atelier
   * produit, au niveau du paramétrage des Gestion des AMCs), alors on positionne la convention de
   * niveau en-tête.
   */
  @ContinueSpan(log = "populateConventions")
  public void populateConventions(
      DetailDroit detailDroit,
      DomaineDroit domaineDroit,
      ParametrageCarteTP parametrageCarteTP,
      TpOnlineRightsDetails tpOnlineRightsDetails,
      List<ConventionTP> conventionTPList,
      LocalDate dateDebutTP)
      throws TriggerParametersException {
    String codeEntete = parametrageCarteTP.getParametrageDroitsCarteTP().getCodeConventionTP();
    String code = "";
    if (detailDroit != null && StringUtils.isNotBlank(detailDroit.getConvention())) {
      code = detailDroit.getConvention();
    } else if (detailDroit == null
        && tpOnlineRightsDetails != null
        && tpOnlineRightsDetails.getNetwork() == null) {
      code = codeEntete;
    } else {
      String reseauPW = tpOnlineRightsDetails != null ? tpOnlineRightsDetails.getNetwork() : null;
      ConventionTP conventionTP =
          getCorrespondingConventionTP(
              dateDebutTP, conventionTPList, domaineDroit.getCode(), reseauPW);
      if (conventionTP != null) {
        code =
            generateCode(
                conventionTP.getConventionCible(),
                conventionTP.isConcatenation() ? codeEntete : "");
      } else {
        if (conventionTPList != null) {
          Optional<ConventionTP> emptyDomainConventionTP =
              getDefaultConventionTPByNetwork(conventionTPList, dateDebutTP, reseauPW);
          if (emptyDomainConventionTP.isPresent()) {
            code =
                generateCode(
                    emptyDomainConventionTP.get().getConventionCible(),
                    emptyDomainConventionTP.get().isConcatenation() ? codeEntete : "");
          }
        }
        if (StringUtils.isBlank(code) && StringUtils.isNotBlank(reseauPW)) {
          code = generateCode(reseauPW, codeEntete);
        }
      }
    }

    if (StringUtils.isBlank(code)) {
      code = codeEntete;
    }

    List<Conventionnement> conventionnements = getListConventionnementsFromCode(code);
    domaineDroit.setConventionnements(conventionnements);
  }

  @NotNull
  public static Optional<ConventionTP> getDefaultConventionTPByNetwork(
      List<ConventionTP> conventionTPList, LocalDate dateDebutTP, String reseauPW) {
    return conventionTPList.stream()
        .filter(
            convention ->
                convention.getDomaineTP() == null
                    && convention.getReseauSoin() != null
                    && convention.getReseauSoin().equals(reseauPW)
                    && (convention.getDateDebut() == null
                        || !convention.getDateDebut().toLocalDate().isAfter(dateDebutTP))
                    && (convention.getDateFin() == null
                        || !dateDebutTP.isAfter(convention.getDateFin().toLocalDate())))
        .findFirst();
  }

  /**
   * Recupere le premier parametrage de convention qui a le bon domaineDroitCode, une intervalle de
   * temps qui englobe la dateDebutTP et si reseauPW n'est pas nul le meme code reseau
   */
  private ConventionTP getCorrespondingConventionTP(
      LocalDate dateDebutTP,
      List<ConventionTP> conventionTPList,
      String domaineDroitCode,
      @Nullable String reseauPW) {
    if (conventionTPList == null) {
      return null;
    }

    Predicate<ConventionTP> filter =
        convtp -> {
          if (!Objects.equals(domaineDroitCode, convtp.getDomaineTP())) {
            return false;
          }

          if (dateDebutTP.isBefore(convtp.getDateDebut().toLocalDate())) {
            return false;
          }

          return convtp.getDateFin() == null
              || !dateDebutTP.isAfter(convtp.getDateFin().toLocalDate());
        };
    if (reseauPW != null) {
      filter = filter.and(convtp -> Objects.equals(reseauPW, convtp.getReseauSoin()));
    }
    return conventionTPList.stream().filter(filter).findFirst().orElse(null);
  }

  /** Merge plusieurs String de type "IS/IT" afin de garder l'unicite des codeConv */
  private String generateCode(String codeNetwork, String codeConventionTP) {
    Set<String> setCodes = new LinkedHashSet<>();
    if (StringUtils.isNotBlank(codeNetwork)) {
      String[] listConvention = codeNetwork.split(SLASH);
      setCodes.addAll(List.of(listConvention));
    }
    if (StringUtils.isNotBlank(codeConventionTP)) {
      String[] listConvention = codeConventionTP.split(SLASH);
      setCodes.addAll(List.of(listConvention));
    }
    return String.join(SLASH, setCodes);
  }

  @ContinueSpan(log = "getListConventionnementsFromCode")
  public List<Conventionnement> getListConventionnementsFromCode(String code)
      throws TriggerParametersException {
    getParametresDtoOrThrow(code);
    String[] listConvention = code.split(SLASH);
    List<Conventionnement> listConvFinal = new ArrayList<>();
    for (int i = 0; i < listConvention.length; i++) {
      String convCode = listConvention[i];
      ParametresDto parametresDto = getParametresDtoOrThrow(convCode);
      TypeConventionnement typeConv = new TypeConventionnement();
      typeConv.setCode(convCode);
      typeConv.setLibelle(parametresDto.getLibelle());

      Conventionnement conv = new Conventionnement();
      conv.setPriorite(i);
      conv.setTypeConventionnement(typeConv);
      listConvFinal.add(conv);
    }
    return listConvFinal;
  }

  private ParametresDto getParametresDtoOrThrow(String code) throws TriggerParametersException {
    ParametresDto parametresDto = parametreBddService.findOneByType(CONVENTIONNEMENT, code);
    if (parametresDto != null) {
      return parametresDto;
    }
    // ainsi que le bon code exception
    throw new TriggerParametersException(
        TriggeredBeneficiaryAnomaly.create(Anomaly.CODE_CONVENTION_NOT_FOUND, code));
  }
}
