package com.cegedim.next.serviceeligibility.core.services.trigger;

import static com.cegedim.next.serviceeligibility.core.utils.DateUtils.compareDate;
import static java.util.stream.Collectors.groupingBy;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.RestitutionCarteDao;
import com.cegedim.next.serviceeligibility.core.mapper.trigger.TriggerMapper;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.PeriodeDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageDroitsCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageRenouvellement;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.Anomaly;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.ManageBenefsContract;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.PeriodeSuspensionDeclaration;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.Trigger;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggerEmitter;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiary;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiaryAnomaly;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiaryStatusEnum;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.enumeration.DateRenouvellementCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ModeDeclenchementCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.MotifEvenement;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.CarenceDroit;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssureCommun;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodeSuspension;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodesDroitsCarte;
import com.cegedim.next.serviceeligibility.core.services.ParametrageCarteTPService;
import com.cegedim.next.serviceeligibility.core.services.SuspensionService;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarationService;
import com.cegedim.next.serviceeligibility.core.services.bdd.SasContratService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import com.cegedim.next.serviceeligibility.core.services.pojo.DroitsTPExtended;
import com.cegedim.next.serviceeligibility.core.services.pojo.GenerationDomaineResult;
import com.cegedim.next.serviceeligibility.core.services.pojo.ParametrageTrigger;
import com.cegedim.next.serviceeligibility.core.services.pojo.TriggerBeneficiaryToDeclarations;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.TriggerUtils;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.*;
import com.mongodb.client.ClientSession;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Primary
public class TriggerBuildDeclarationNewService extends TriggerBuildDeclaration {

  private static final int CREATION_FERMETURE = 1;
  private static final int PAS_DE_FERMETURE = 2;
  private static final int RIEN_A_INTEGRER_OU_SANS_EFFET = 3;

  protected final DeclarationService declarationService;

  private final TriggerDomaineService triggerDomaineService;

  private final TriggerMapper triggerMapper;
  private final SuspensionService suspensionService;

  public TriggerBuildDeclarationNewService(
      SasContratService sasContratService,
      ParametrageCarteTPService paramCarteTPService,
      TriggerService triggerService,
      SuspensionService suspensionService,
      DeclarationService declarationService,
      TriggerDomaineService triggerDomaineService,
      TriggerMapper triggerMapper,
      RestitutionCarteDao restitutionCarteDao) {
    super(
        sasContratService,
        paramCarteTPService,
        triggerService,
        suspensionService,
        restitutionCarteDao);
    this.triggerMapper = triggerMapper;
    this.triggerDomaineService = triggerDomaineService;
    this.declarationService = declarationService;
    this.suspensionService = suspensionService;
  }

  private MotifEvenement getMotifEvenement(
      TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations, String finPeriodeDroit) {

    if (!CollectionUtils.isEmpty(triggerBeneficiaryToDeclarations.getSuspensionPeriods())) {
      String finSuspensionMax =
          DateUtils.getMaxDateOrNull(
              triggerBeneficiaryToDeclarations.getSuspensionPeriods().stream()
                  .map(periodeSuspension -> periodeSuspension.getPeriode().getFin())
                  .toList());
      finSuspensionMax = finSuspensionMax == null ? null : finSuspensionMax.replace("-", "/");

      if (triggerBeneficiaryToDeclarations.getDateFermeture() != null
          && ((finSuspensionMax != null
                  && compareDate(
                          triggerBeneficiaryToDeclarations.getDateFermeture(),
                          finSuspensionMax,
                          DateUtils.SLASHED_FORMATTER)
                      <= 0)
              || (finSuspensionMax == null
                  && compareDate(
                          triggerBeneficiaryToDeclarations.getDateFermeture(),
                          finPeriodeDroit,
                          DateUtils.SLASHED_FORMATTER)
                      <= 0))) {
        return MotifEvenement.EM;
      }
    }

    return MotifEvenement.FE;
  }

  protected void buildDeclarationsForBenefInDeleteContrat(
      Trigger trigger,
      ManageBenefsContract manageBenefsContract,
      TriggeredBeneficiary benef,
      ClientSession session) {
    TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations =
        new TriggerBeneficiaryToDeclarations();
    triggerBeneficiaryToDeclarations.setTriggeredBeneficiary(benef);
    triggerBeneficiaryToDeclarations.setTrigger(trigger);
    buildDeclarationsForBenefInDeleteContrat(triggerBeneficiaryToDeclarations, session);
    updateBeneficiary(
        manageBenefsContract,
        triggerBeneficiaryToDeclarations.getTriggeredBeneficiary(),
        triggerBeneficiaryToDeclarations);
  }

  protected void buildDeclarationsForBenefInDeleteContrat(
      TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations, ClientSession session) {
    fillExistingDeclarationsForDeleteContrat(triggerBeneficiaryToDeclarations, session);
    fillClosedDeclarations(triggerBeneficiaryToDeclarations, new Date());
    if (triggerBeneficiaryToDeclarations.getClosedDeclaration() != null) {
      String rangAdministratifToClose =
          triggerBeneficiaryToDeclarations
              .getClosedDeclaration()
              .getContrat()
              .getRangAdministratif();
      if (rangAdministratifToClose != null
          && !rangAdministratifToClose.equals(
              triggerBeneficiaryToDeclarations.getTriggeredBeneficiary().getRangAdministratif())) {
        // BLUE-7296
        triggerBeneficiaryToDeclarations.setClosedDeclaration(null);
      }
    }
    for (Declaration declaration : triggerBeneficiaryToDeclarations.getClosedDeclarations()) {
      String rangAdministratifToClose = declaration.getContrat().getRangAdministratif();
      if (rangAdministratifToClose != null
          && rangAdministratifToClose.equals(
              triggerBeneficiaryToDeclarations.getTriggeredBeneficiary().getRangAdministratif())) {
        // BLUE-7296
        declarationService.createDeclaration(declaration, session);
        triggerBeneficiaryToDeclarations.getGeneratedDeclarations().add(declaration);
      }
    }
  }

  @Override
  protected boolean buildDeclarationsForBenef(
      Trigger trigger,
      ManageBenefsContract manageBenefsContract,
      TriggeredBeneficiary benef,
      ParametrageCarteTP param,
      ClientSession session) {

    if (param != null) {
      benef.setParametrageCarteTPId(param.getId());
    } else {
      benef.setParametrageCarteTPId(null);
      benef.setStatut(TriggeredBeneficiaryStatusEnum.Error);
      TriggeredBeneficiaryAnomaly anomalie =
          TriggeredBeneficiaryAnomaly.create(Anomaly.NO_CARD_RIGHT_PARAM);
      benef.setDerniereAnomalie(anomalie);
      handleBuildDeclarationError(
          manageBenefsContract, benef, anomalie, anomalie.getDescription(), false);
      return false;
    }

    if (TriggeredBeneficiaryStatusEnum.Error.equals(benef.getStatut())) {
      TriggeredBeneficiaryAnomaly anomalie = benef.getDerniereAnomalie();
      handleBuildDeclarationError(
          manageBenefsContract, benef, anomalie, anomalie.getDescription(), false);
      return false;
    }

    List<TriggeredBeneficiaryAnomaly> anomalies = new ArrayList<>();
    buildDeclarationsForBenefForPilotageBO(benef, param, anomalies);
    if (!anomalies.isEmpty()) {
      // Cas warning
      handleBuildDeclarationWarning(manageBenefsContract, benef, anomalies);
      return true;
    }

    log.debug("Construction de la déclaration pour le benef {}", benef.getId());
    // créer l'objet TriggerBeneficiaryToDeclarations
    TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations =
        new TriggerBeneficiaryToDeclarations();
    triggerBeneficiaryToDeclarations.setTriggeredBeneficiary(benef);
    // on sauvegarde parce que l'on va les vider de l'objet triggerBenef avant la
    // déclaration d'ouverture
    triggerBeneficiaryToDeclarations.setDateRadiation(benef.getNewContract().getDateRadiation());
    triggerBeneficiaryToDeclarations.setDateResiliation(
        benef.getNewContract().getDateResiliation());
    triggerBeneficiaryToDeclarations.setSuspensionPeriods(
        benef.getNewContract().getPeriodesSuspension());
    triggerBeneficiaryToDeclarations.setWhatToDo(0);
    triggerBeneficiaryToDeclarations.setCloseAll(false);
    triggerBeneficiaryToDeclarations.setParametrageCarteTP(param);
    triggerBeneficiaryToDeclarations.setTrigger(trigger);

    triggerBeneficiaryToDeclarations.setDateFermeture(
        getDateFermeture(
            triggerBeneficiaryToDeclarations.getDateResiliation(),
            triggerBeneficiaryToDeclarations.getDateRadiation(),
            trigger.getDateRestitution()));

    try {
      prepareTriggerBenefForDeclaration(
          trigger, benef, param, session, triggerBeneficiaryToDeclarations);

      updateBeneficiary(manageBenefsContract, benef, triggerBeneficiaryToDeclarations);

    } catch (TriggerWarningException e) {
      log.debug(String.format(TYPE_EXCEPTION, e));
      handleBuildDeclarationWarning(
          manageBenefsContract, benef, List.of(e.getTriggeredBeneficiaryAnomaly()));
      return true;
    } catch (DomaineNotFoundException
        | BobbNotFoundException
        | PwException
        | CarenceException
        | TriggerParametersException e) {
      log.debug(String.format(TYPE_EXCEPTION, e));
      handleBuildDeclarationError(
          manageBenefsContract, benef, e.getTriggeredBeneficiaryAnomaly(), e.getMessage(), true);
      return false;
    } catch (Exception e) {
      // Peu importe l'exception on doit mettre le benef
      // en erreur
      log.error(e.getMessage(), e);
      handleBuildDeclarationError(
          manageBenefsContract,
          benef,
          TriggeredBeneficiaryAnomaly.create(
              Anomaly.UNKNOWN_EXCEPTION, benef.getId(), e.getMessage()),
          e.getMessage(),
          true);
      return false;
    }
    return true;
  }

  private void prepareTriggerBenefForDeclaration(
      Trigger trigger,
      TriggeredBeneficiary benef,
      ParametrageCarteTP param,
      ClientSession session,
      TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations)
      throws CarenceException,
          DomaineNotFoundException,
          BobbNotFoundException,
          PwException,
          TriggerWarningException,
          TriggerParametersException {
    List<Declaration> existingDeclarations =
        getExistingDeclarationsFromMongo(triggerBeneficiaryToDeclarations, session);
    Declaration firstDeclaration =
        fillExistingDeclarationsGetFirst(triggerBeneficiaryToDeclarations, existingDeclarations);

    boolean isManualAnniversaryCase =
        ModeDeclenchementCarteTP.Manuel.equals(
                param.getParametrageRenouvellement().getModeDeclenchement())
            && DateRenouvellementCarteTP.AnniversaireContrat.equals(
                param.getParametrageRenouvellement().getDateRenouvellementCarteTP());

    updateContratResiliatedFromTheBeginning(
        triggerBeneficiaryToDeclarations,
        firstDeclaration,
        triggerBeneficiaryToDeclarations.getDateFermeture());
    try {
      if (isManualAnniversaryCase) {
        checkManualAnniversaryCase(benef, param, triggerBeneficiaryToDeclarations, trigger.isRdo());
      } else {
        createOpenDeclaration(triggerBeneficiaryToDeclarations);
      }
    } catch (BeneficiaryToIgnoreException e) {
      throw new TriggerWarningException(
          TriggeredBeneficiaryAnomaly.create(Anomaly.WARRANTIES_IGNORED));
    }
    Date dateTraitement;
    if (triggerBeneficiaryToDeclarations.getCreatedDeclaration() != null) {
      dateTraitement = triggerBeneficiaryToDeclarations.getCreatedDeclaration().getDateCreation();
    } else {
      dateTraitement = new Date();
    }

    addLatestDeclarationIfDifferences(triggerBeneficiaryToDeclarations, existingDeclarations);

    close(trigger, triggerBeneficiaryToDeclarations, dateTraitement);

    saveDeclarations(triggerBeneficiaryToDeclarations, session);

    if (CollectionUtils.isEmpty(triggerBeneficiaryToDeclarations.getGeneratedDeclarations())) {
      throw new TriggerWarningException(
          TriggeredBeneficiaryAnomaly.create(Anomaly.WARRANTIES_CANCELED_OR_OUT_OF_BOUND));
    }
  }

  private void close(
      Trigger trigger,
      TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations,
      Date dateTraitement) {
    if (triggerBeneficiaryToDeclarations.getCreatedDeclaration() == null
        && triggerBeneficiaryToDeclarations.isWarning()
        && CollectionUtils.isNotEmpty(triggerBeneficiaryToDeclarations.getExistingDeclarations())) {
      // FIN DE GARANTIE antérieur à la date du traitement, c'est comme un sans effet
      triggerBeneficiaryToDeclarations.setWhatToDo(RIEN_A_INTEGRER_OU_SANS_EFFET);
    } else {
      if (CollectionUtils.isNotEmpty(triggerBeneficiaryToDeclarations.getExistingDeclarations())) {
        triggerBeneficiaryToDeclarations.setCloseAll(
            checkIfWeCloseAll(triggerBeneficiaryToDeclarations));
      }
      changementDateFinPeriodeOffline(trigger, triggerBeneficiaryToDeclarations);
      if (triggerBeneficiaryToDeclarations.getDateFermeture() != null
          && triggerBeneficiaryToDeclarations.getCreatedDeclaration() != null) {
        checkWhatToDoNextAndCreateCloseDeclaration(triggerBeneficiaryToDeclarations);
      }

      if (triggerBeneficiaryToDeclarations.isCloseAll()) {
        fillClosedDeclarations(triggerBeneficiaryToDeclarations, dateTraitement);
      } else {
        triggerBeneficiaryToDeclarations.getClosedDeclarations().clear();
      }
    }
  }

  private void buildDeclarationsForBenefForPilotageBO(
      TriggeredBeneficiary benef,
      ParametrageCarteTP param,
      List<TriggeredBeneficiaryAnomaly> anomalies) {
    if (ModeDeclenchementCarteTP.PilotageBO.equals(
        param.getParametrageRenouvellement().getModeDeclenchement())) {
      TriggerUtils.controlePilotageBO(benef, anomalies);
    }
  }

  private void checkManualAnniversaryCase(
      TriggeredBeneficiary benef,
      ParametrageCarteTP param,
      TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations,
      boolean isRdo)
      throws CarenceException,
          DomaineNotFoundException,
          BobbNotFoundException,
          PwException,
          TriggerWarningException,
          TriggerParametersException,
          BeneficiaryToIgnoreException {
    final String dateDebutDroitTP =
        Objects.requireNonNullElse(param.getParametrageRenouvellement().getDateDebutDroitTP(), "");

    if (TriggerUtils.checkIfHasToRenew(
            param, dateDebutDroitTP, benef.getNewContract().getDateSouscription())
        || TriggerUtils.checkIfRenewalForProductChange(
            benef.getNewContract().getDateSouscription(), dateDebutDroitTP, isRdo)) {
      createOpenDeclaration(triggerBeneficiaryToDeclarations);
    } else {
      triggerBeneficiaryToDeclarations.setWhatToDo(RIEN_A_INTEGRER_OU_SANS_EFFET);
    }
  }

  private static void changementDateFinPeriodeOffline(
      Trigger trigger, TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations) {
    if (!TriggerEmitter.Renewal.equals(trigger.getOrigine())
        && !isAdjonction(triggerBeneficiaryToDeclarations.getTriggeredBeneficiary())
        && (triggerBeneficiaryToDeclarations.isChangeRightPeriods()
            || triggerBeneficiaryToDeclarations.getDateFermeture() != null)
        && CollectionUtils.isNotEmpty(triggerBeneficiaryToDeclarations.getExistingDeclarations())) {

      ParametrageTrigger parametrageTrigger = new ParametrageTrigger();
      parametrageTrigger.setTriggeredBeneficiary(
          triggerBeneficiaryToDeclarations.getTriggeredBeneficiary());
      parametrageTrigger.setParametrageCarteTP(
          triggerBeneficiaryToDeclarations.getParametrageCarteTP());
      parametrageTrigger.setOrigine(trigger.getOrigine());
      parametrageTrigger.setDateEffet(trigger.getDateEffet());
      parametrageTrigger.setRdo(trigger.isRdo());
      parametrageTrigger.setEventReprise(trigger.isEventReprise());
      parametrageTrigger.setDateSouscription(
          triggerBeneficiaryToDeclarations
              .getTriggeredBeneficiary()
              .getNewContract()
              .getDateSouscription());

      Periode periode = TriggerUtils.getPeriodeFromParametrage(parametrageTrigger);

      Optional<DomaineDroit> optional =
          triggerBeneficiaryToDeclarations.getExistingDeclarations().stream()
              .flatMap(declaration -> declaration.getDomaineDroits().stream())
              .max(
                  Comparator.comparing(
                      domaineDroit -> domaineDroit.getPeriodeDroit().getPeriodeFin()));
      String dateFin;
      if (optional.isPresent()) {
        dateFin = optional.get().getPeriodeDroit().getPeriodeFin();
        // pour ne pas dépasser l'année courante du paramétrage
        if (periode.getFin() != null
            && DateUtils.before(
                periode.getFin().replace("-", "/"), dateFin, DateUtils.SLASHED_FORMATTER)) {
          dateFin = periode.getFin();
        }
        final String realEndDate = dateFin.replace("-", "/");
        triggerBeneficiaryToDeclarations
            .getCreatedDeclaration()
            .getDomaineDroits()
            .forEach(
                domaineDroit -> {
                  if (domaineDroit.getPeriodeCarence() == null
                      && domaineDroit.getPeriodeProductElement().getFin() == null
                      && domaineDroit.getPeriodePW().getFin() == null
                      && DateUtils.before(
                          domaineDroit.getPeriodeDroit().getPeriodeFin(),
                          realEndDate,
                          DateUtils.SLASHED_FORMATTER)) {
                    domaineDroit.getPeriodeDroit().setPeriodeFin(realEndDate);
                  }
                });
      }
    }
  }

  private void updateBeneficiary(
      ManageBenefsContract manageBenefsContract,
      TriggeredBeneficiary triggeredBeneficiary,
      TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations) {
    manageBenefsContract
        .getDeclarations()
        .addAll(triggerBeneficiaryToDeclarations.getGeneratedDeclarations());
    int nbDeclOuvertes = 0;
    int nbDeclFermees = 0;
    for (Declaration decl : triggerBeneficiaryToDeclarations.getGeneratedDeclarations()) {
      if (Constants.CODE_ETAT_VALIDE.equals(decl.getCodeEtat())) {
        nbDeclOuvertes++;
      } else {
        nbDeclFermees++;
      }
    }
    triggeredBeneficiary.setNbDeclarationsOuverture(nbDeclOuvertes);
    triggeredBeneficiary.setNbDeclarationsFermeture(nbDeclFermees);
    // Maj du statut du bénef
    log.debug("Maj du statut du benef.");
    TriggerUtils.addStatus(
        triggeredBeneficiary, TriggeredBeneficiaryStatusEnum.Processed, null, true);
  }

  protected String getDateFermeture(
      String dateResiliation, String dateRadiation, String dateRestitution) {

    List<String> datesFerm = new ArrayList<>();
    if (dateResiliation != null) {
      datesFerm.add(dateResiliation);
    }
    if (dateRadiation != null) {
      datesFerm.add(dateRadiation);
    }
    if (dateRestitution != null) {
      datesFerm.add(dateRestitution);
    }
    return datesFerm.stream().min(String::compareTo).map(s -> s.replace("-", "/")).orElse(null);
  }

  // Recherche de la déclaration la plus récente en fonction d'une date (à
  // définir) => Probablement declarationService.findDeclarationsOfBenef(...)
  // Mettre dans une sous-liste toutes les déclarations plus récentes que celle
  // trouvée
  // Si cette sous-liste n'est pas vide, on filtre la liste de base en enlevant
  // toutes les déclarations ayant pour id un id correspondant a un idOrigine de
  // la sous-liste
  Declaration fillExistingDeclarationsGetFirst(
      TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarationstriggeredBenef,
      List<Declaration> declList) {
    Declaration firstUsefull = null;
    String dateFermeture = triggerBeneficiaryToDeclarationstriggeredBenef.getDateFermeture();
    LinkedList<Declaration> declSubList = new LinkedList<>();
    // On boucle sur la liste des déclarations à rebours pour déterminer la dernière
    for (int i = declList.size() - 1; i >= 0; i--) {
      Declaration declaration = declList.get(i);

      // On extrait la plus grande date de fin par domaine pour comparer a
      // dateFermeture
      String maxDatesFinDecl = null;

      for (DomaineDroit domaine : declaration.getDomaineDroits()) {
        maxDatesFinDecl =
            DateUtils.getMaxDate(maxDatesFinDecl, domaine.getPeriodeDroit().getPeriodeFin());
      }

      // On considère que la déclaration est trop ancienne si la plus grande des dates
      // de fin droit est inférieure a dateFermeture
      // On considère que la déclaration est trop ancienne si la plus grande des dates
      // de fin droit est inférieure a dateFermeture OU a la date min de la
      // déclaration créée
      if (maxDatesFinDecl != null
          && (dateFermeture != null
              && DateUtils.before(maxDatesFinDecl, dateFermeture, DateUtils.SLASHED_FORMATTER))) {
        // 5098 - On récupère la dernière déclaration précédent la période courante si
        // aucune autre déclaration n'existe pour pouvoir fermer les périodes online si
        // necessaire
        if (declSubList.isEmpty()) {
          declSubList.add(declaration);
        }
        break;
      }
      // Si ce n'est pas la dernière, on l'ajoute a declSubList et on continue de
      // boucler
      else {
        // On la met au début du tableau pour garder les déclarations dans l'ordre
        declSubList.add(declaration);
        if (firstUsefull == null) {
          // On recupere la derniere declaration creee qui impact les nouveaux droits
          firstUsefull = declaration;
        }
      }
    }
    if (CollectionUtils.isNotEmpty(declSubList)) {
      // Pour chacun de ces id, on va enlever les déclarations d'origine de la liste
      // des déclarations
      boolean shouldForceOffline =
          checkForceCloseOffline(triggerBeneficiaryToDeclarationstriggeredBenef);

      declSubList = extractOrigineDeclarations(declSubList);

      declSubList = getValidDeclarations(shouldForceOffline, declSubList);
    }
    triggerBeneficiaryToDeclarationstriggeredBenef.setExistingDeclarations(declSubList);
    return firstUsefull;
  }

  @NotNull
  private static LinkedList<Declaration> extractOrigineDeclarations(
      LinkedList<Declaration> declSubList) {
    // On extrait la liste des id des déclarations à l'origine de la fermeture si
    // applicable
    List<String> declOriginIdList =
        declSubList.stream().map(Declaration::getIdOrigine).filter(Objects::nonNull).toList();

    declSubList =
        declSubList.stream()
            .filter(declaration -> !declOriginIdList.contains(declaration.get_id()))
            .collect(Collectors.toCollection(LinkedList::new));

    return declSubList;
  }

  private static LinkedList<Declaration> getValidDeclarations(
      boolean shouldForceOffline, LinkedList<Declaration> declSubList) {
    if (shouldForceOffline) {
      return declSubList.stream()
          .filter(
              declaration ->
                  declaration.getDomaineDroits().stream()
                      .anyMatch(
                          domaineDroit ->
                              domaineDroit.getPeriodeDroit().getPeriodeFermetureFin() == null
                                  || !DateUtils.before(
                                      domaineDroit.getPeriodeDroit().getPeriodeFermetureFin(),
                                      domaineDroit.getPeriodeDroit().getPeriodeDebut(),
                                      DateUtils.SLASHED_FORMATTER)))
          .collect(Collectors.toCollection(LinkedList::new));
    }
    return declSubList.stream()
        .filter(
            declaration ->
                declaration.getDomaineDroits().stream()
                    .anyMatch(
                        domaineDroit ->
                            !DateUtils.before(
                                domaineDroit.getPeriodeDroit().getPeriodeFin(),
                                domaineDroit.getPeriodeDroit().getPeriodeDebut(),
                                DateUtils.SLASHED_FORMATTER)))
        .collect(Collectors.toCollection(LinkedList::new));
  }

  void fillExistingDeclarationsForDeleteContrat(
      TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarationstriggeredBenef,
      ClientSession session) {
    List<Declaration> declList =
        getExistingDeclarationsFromMongo(triggerBeneficiaryToDeclarationstriggeredBenef, session);
    LinkedList<Declaration> declSubList = new LinkedList<>();
    for (int i = declList.size() - 1; i >= 0; i--) {
      declSubList.add(declList.get(i));
    }
    // On boucle sur la liste des déclarations à rebours pour déterminer la dernière
    if (CollectionUtils.isNotEmpty(declSubList)) {
      declSubList = extractOrigineDeclarations(declSubList);
      // on ne garde pas les déclarations déjà annulées
      declSubList = getValidDeclarations(true, declSubList);

      triggerBeneficiaryToDeclarationstriggeredBenef.setExistingDeclarations(declSubList);
    }
  }

  private List<Declaration> getExistingDeclarationsFromMongo(
      TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations, ClientSession session) {
    TriggeredBeneficiary triggeredBenef =
        triggerBeneficiaryToDeclarations.getTriggeredBeneficiary();
    return declarationService.findDeclarationsOfBenef(
        triggeredBenef.getAmc(),
        triggeredBenef.getNumeroContrat(),
        triggeredBenef.getNumeroPersonne(),
        triggeredBenef.getDateNaissance(),
        triggeredBenef.getRangNaissance(),
        triggeredBenef.getRangAdministratif(),
        session);
  }

  private void addLatestDeclarationIfDifferences(
      TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations,
      List<Declaration> existingDeclarationsFromMongo) {
    if (CollectionUtils.isNotEmpty(existingDeclarationsFromMongo)
        && triggerBeneficiaryToDeclarations.getCreatedDeclaration() != null) {
      Declaration latestDeclaration =
          existingDeclarationsFromMongo.get(existingDeclarationsFromMongo.size() - 1);
      if (latestDeclaration != null
          && (CollectionUtils.isEmpty(triggerBeneficiaryToDeclarations.getExistingDeclarations())
              || !triggerBeneficiaryToDeclarations
                  .getExistingDeclarations()
                  .getFirst()
                  .get_id()
                  .equals(latestDeclaration.get_id()))
          && checkDiffDomaineDroitByGarantie(triggerBeneficiaryToDeclarations, latestDeclaration)) {
        triggerBeneficiaryToDeclarations.getExistingDeclarations().addFirst(latestDeclaration);
      }
    }
  }

  private void updateContratResiliatedFromTheBeginning(
      TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarationstriggeredBenef,
      Declaration firstDeclaration,
      String dateFermeture) {
    if (dateFermeture != null) {
      if (firstDeclaration == null) {
        triggerBeneficiaryToDeclarationstriggeredBenef.setContratResiliatedFromTheBeginning(true);
      } else if (TriggerEmitter.Event.equals(
          triggerBeneficiaryToDeclarationstriggeredBenef.getTrigger().getOrigine())) {
        updateResiliatedFromTheBeginningEventCase(
            triggerBeneficiaryToDeclarationstriggeredBenef, firstDeclaration, dateFermeture);
      } else {

        // cas du renouvellement.
        // si la date de fermeture est égale à la date de fin offline des domaines de la
        // déclaration.
        // et si la dernière déclaration est avec une  date de fermeture
        String dateFermetureOffline =
            getDateFinMaximumOnDomaineDroits(firstDeclaration.getDomaineDroits());
        if (dateFermetureOffline != null && dateFermetureOffline.equals(dateFermeture)) {
          triggerBeneficiaryToDeclarationstriggeredBenef.setContratResiliatedFromTheBeginning(
              Constants.CODE_ETAT_VALIDE.equals(firstDeclaration.getCodeEtat()));
        }
      }
    }
  }

  private void updateResiliatedFromTheBeginningEventCase(
      TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarationstriggeredBenef,
      Declaration firstDeclaration,
      String dateFermeture) {
    // BLUE-7296 : garder ce if oldContract == null pour garantir le recyclage des triggers
    // existants
    if (triggerBeneficiaryToDeclarationstriggeredBenef.getTriggeredBeneficiary().getOldContract()
        == null) {
      triggerBeneficiaryToDeclarationstriggeredBenef.setContratResiliatedFromTheBeginning(true);
    } else {
      // cas de l'event seulement, oldContrat n'est pas renseigné dans le cas d'un
      // renouvellement.
      String oldDateFermeture =
          getDateFermeture(
              triggerBeneficiaryToDeclarationstriggeredBenef
                  .getTriggeredBeneficiary()
                  .getOldContract()
                  .getDateResiliation(),
              triggerBeneficiaryToDeclarationstriggeredBenef
                  .getTriggeredBeneficiary()
                  .getOldContract()
                  .getDateRadiation(),
              triggerBeneficiaryToDeclarationstriggeredBenef
                  .getTriggeredBeneficiary()
                  .getOldContract()
                  .getDateRestitution());

      // si la dernière déclaration est avec unedate de fermeture c'est que l'on a
      // reçu le contrat déjà résilié
      if (oldDateFermeture != null && oldDateFermeture.equals(dateFermeture)) {
        triggerBeneficiaryToDeclarationstriggeredBenef.setContratResiliatedFromTheBeginning(
            Constants.CODE_ETAT_VALIDE.equals(firstDeclaration.getCodeEtat()));
      }
    }
  }

  private static String getDateFinMaximumOnDomaineDroits(List<DomaineDroit> domaineDroits) {
    return domaineDroits.stream()
        .filter(domaineDroit -> domaineDroit.getPeriodeDroit().getPeriodeFermetureDebut() == null)
        .map(domaineDroit -> domaineDroit.getPeriodeDroit().getPeriodeFin())
        .max(String::compareTo)
        .orElse(null);
  }

  /**
   * Cas si isContratResiliatedFromTheBeginning false : Creer une declaration ouverte sans date
   * resil/rad/susp (appelee ALPHA pas mise dans LIST). Se baser sur buildDeclarationOuverte en
   * enlevant les dates du triggerBenef → les mettre dans l’objet TriggerBeneficiaryToDeclarations
   * Sinon : creer une declaration ouverte avec date resil/rad
   */
  void createOpenDeclaration(
      TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarationstriggeredBenef)
      throws DomaineNotFoundException,
          BobbNotFoundException,
          TriggerWarningException,
          PwException,
          CarenceException,
          TriggerParametersException,
          BeneficiaryToIgnoreException {

    TriggeredBeneficiary triggeredBeneficiary =
        triggerBeneficiaryToDeclarationstriggeredBenef.getTriggeredBeneficiary();

    String dateRadiation = triggeredBeneficiary.getNewContract().getDateRadiation();
    String dateResiliation = triggeredBeneficiary.getNewContract().getDateResiliation();

    try {
      Declaration declaration =
          buildDeclarationOuverte(triggerBeneficiaryToDeclarationstriggeredBenef);

      // set createdDeclaration
      triggerBeneficiaryToDeclarationstriggeredBenef.setCreatedDeclaration(declaration);
    } finally {
      triggeredBeneficiary.getNewContract().setDateRadiation(dateRadiation);
      triggeredBeneficiary.getNewContract().setDateResiliation(dateResiliation);
    }
  }

  /**
   * Si Date Fermeture dans au moins une période de droit de alpha. On crée une fermeture. Si Date
   * Fermeture aprés la période de droit. On met la date de fin dans date fin online. Si Date
   * Fermeture avant la période de droit. Ne pas intégré les suspensions. Mettre à jour whatToDo
   * suivant les cas
   *
   * @param triggerBeneficiaryToDeclarationstriggeredBenef : objet transverse de traitement du benef
   * @return accompli ou non
   */
  boolean checkWhatToDoNextAndCreateCloseDeclaration(
      TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarationstriggeredBenef) {
    int nbrDomaineDroit =
        triggerBeneficiaryToDeclarationstriggeredBenef
            .getCreatedDeclaration()
            .getDomaineDroits()
            .size();
    Date dateTraitement =
        triggerBeneficiaryToDeclarationstriggeredBenef.getCreatedDeclaration().getDateCreation();
    if (nbrDomaineDroit == 0) {
      return false;
    }

    LocalDate dateFermeture =
        DateUtils.stringToDate(triggerBeneficiaryToDeclarationstriggeredBenef.getDateFermeture());
    String debutstr =
        DateUtils.getMinDate(
            triggerBeneficiaryToDeclarationstriggeredBenef
                .getCreatedDeclaration()
                .getDomaineDroits()
                .stream()
                .map(domaineDroit -> domaineDroit.getPeriodeDroit().getPeriodeDebut())
                .toList());
    String finstr =
        DateUtils.getMaxDateOrNull(
            triggerBeneficiaryToDeclarationstriggeredBenef
                .getCreatedDeclaration()
                .getDomaineDroits()
                .stream()
                .map(domaineDroit -> domaineDroit.getPeriodeDroit().getPeriodeFin())
                .toList());
    LocalDate debut = DateUtils.stringToDate(debutstr);
    LocalDate fin = DateUtils.stringToDate(finstr);

    ParametrageTrigger parametrageTrigger = new ParametrageTrigger();
    parametrageTrigger.setTriggeredBeneficiary(
        triggerBeneficiaryToDeclarationstriggeredBenef.getTriggeredBeneficiary());
    parametrageTrigger.setParametrageCarteTP(
        triggerBeneficiaryToDeclarationstriggeredBenef.getParametrageCarteTP());
    parametrageTrigger.setOrigine(
        triggerBeneficiaryToDeclarationstriggeredBenef.getTrigger().getOrigine());
    parametrageTrigger.setDateEffet(
        triggerBeneficiaryToDeclarationstriggeredBenef.getTrigger().getDateEffet());
    parametrageTrigger.setRdo(triggerBeneficiaryToDeclarationstriggeredBenef.getTrigger().isRdo());
    parametrageTrigger.setEventReprise(
        triggerBeneficiaryToDeclarationstriggeredBenef.getTrigger().isEventReprise());
    parametrageTrigger.setDateSouscription(
        triggerBeneficiaryToDeclarationstriggeredBenef
            .getTriggeredBeneficiary()
            .getNewContract()
            .getDateSouscription());

    Periode periodeParametrage = TriggerUtils.getPeriodeFromParametrage(parametrageTrigger);
    LocalDate debutParametrage = null;
    LocalDate finParametrage = null;
    if (periodeParametrage != null) {
      debutParametrage = DateUtils.stringToDate(periodeParametrage.getDebut());
      finParametrage = DateUtils.stringToDate(periodeParametrage.getFin());
    }
    if (triggerBeneficiaryToDeclarationstriggeredBenef.isContratResiliatedFromTheBeginning()) {
      // BLUE-5833 -> envoi contrat avec radiation dedans
      triggerBeneficiaryToDeclarationstriggeredBenef.setWhatToDo(PAS_DE_FERMETURE);
      return true;
    } else if (dateFermeture != null
        && dateFermeture.isBefore(debut)
        && periodeParametrage != null
        && !DateUtils.betweenLocalDate(dateFermeture, debutParametrage, finParametrage)) {
      // Date Fermeture est avant la date de debut -> rien à intégré ou fermé sans
      // effet (cas de test JIRA-4937)
      triggerBeneficiaryToDeclarationstriggeredBenef.setWhatToDo(RIEN_A_INTEGRER_OU_SANS_EFFET);
      return true;
    }
    if (fin != null) {
      // !isAfter plutot que isBefore pour inclure le cas d egalite
      if (dateFermeture != null && !dateFermeture.isAfter(fin)) {
        // Date Fermeture dans la période de droit -> création déclaration de fermeture
        triggerBeneficiaryToDeclarationstriggeredBenef.setWhatToDo(CREATION_FERMETURE);

        Declaration closedDeclaration =
            buildClosedDeclaration(triggerBeneficiaryToDeclarationstriggeredBenef, dateTraitement);
        if (closedDeclaration == null) {
          return false;
        }
        triggerBeneficiaryToDeclarationstriggeredBenef.setClosedDeclaration(closedDeclaration);
      } else {
        // Date Fermeture aprés la période de droit -> pas de fermeture
        triggerBeneficiaryToDeclarationstriggeredBenef.setWhatToDo(PAS_DE_FERMETURE);
      }
      return true;
    }
    // Date Fermeture dans la période de droit, qui est sans fin -> création
    // déclaration de fermeture
    triggerBeneficiaryToDeclarationstriggeredBenef.setWhatToDo(CREATION_FERMETURE);
    Declaration closedDeclaration =
        buildClosedDeclaration(triggerBeneficiaryToDeclarationstriggeredBenef, dateTraitement);
    if (closedDeclaration == null) {
      return false;
    }
    triggerBeneficiaryToDeclarationstriggeredBenef.getClosedDeclarations().add(closedDeclaration);
    return true;
  }

  Declaration buildClosedDeclaration(
      TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarationstriggeredBenef,
      Date dateTraitement) {
    Declaration createdDeclaration =
        triggerBeneficiaryToDeclarationstriggeredBenef.getCreatedDeclaration();

    Declaration closedDeclaration = new Declaration(createdDeclaration);
    closedDeclaration.setCodeEtat(Constants.CODE_ETAT_INVALIDE);
    closedDeclaration.setDateCreation(dateTraitement);
    closedDeclaration.setEffetDebut(dateTraitement);
    closedDeclaration.setDateModification(
        Date.from(closedDeclaration.getDateModification().toInstant().plusSeconds(1)));
    closedDeclaration.setIsCarteTPaEditer(false);

    TriggeredBeneficiary triggeredBeneficiary =
        triggerBeneficiaryToDeclarationstriggeredBenef.getTriggeredBeneficiary();
    closedDeclaration.setIdTrigger(triggeredBeneficiary.getIdTrigger());
    closedDeclaration.setEtatSuspension(triggeredBeneficiary.getEtatSuspension());

    Trigger trigger = triggerBeneficiaryToDeclarationstriggeredBenef.getTrigger();
    closedDeclaration.setUserCreation(trigger.getOrigine().toString());
    closedDeclaration.setUserModification(closedDeclaration.getUserCreation());
    closedDeclaration.setNomFichierOrigine(closedDeclaration.getNomFichierOrigine());

    if (StringUtils.isNotBlank(
        triggerBeneficiaryToDeclarationstriggeredBenef.getDateResiliation())) {
      closedDeclaration
          .getContrat()
          .setDateResiliation(
              triggerBeneficiaryToDeclarationstriggeredBenef
                  .getDateResiliation()
                  .replace("-", "/"));
    }
    if (StringUtils.isNotBlank(triggerBeneficiaryToDeclarationstriggeredBenef.getDateRadiation())) {
      closedDeclaration
          .getBeneficiaire()
          .setDateRadiation(
              triggerBeneficiaryToDeclarationstriggeredBenef.getDateRadiation().replace("-", "/"));
    }

    if (!StringUtils.isBlank(trigger.getDateRestitution())) {
      closedDeclaration.setDateRestitution(trigger.getDateRestitution());
    }

    String finPeriodeDroit =
        DateUtils.getMaxDateOrNull(
            closedDeclaration.getDomaineDroits().stream()
                .map(domaineDroit -> domaineDroit.getPeriodeDroit().getPeriodeFin())
                .toList());

    for (DomaineDroit domaineDroit : closedDeclaration.getDomaineDroits()) {
      PeriodeDroit periodeDroit = domaineDroit.getPeriodeDroit();
      if (!Constants.CODE_ETAT_INVALIDE.equals(createdDeclaration.getCodeEtat())) {
        periodeDroit.setPeriodeFermetureFin(periodeDroit.getPeriodeFin());
      }
      String dateFinDroit = triggerBeneficiaryToDeclarationstriggeredBenef.getDateFermeture();

      MotifEvenement motifEvenement =
          getMotifEvenement(triggerBeneficiaryToDeclarationstriggeredBenef, finPeriodeDroit);
      periodeDroit.setMotifEvenement(motifEvenement.name());
      periodeDroit.setLibelleEvenement(motifEvenement.getLibelle());
      // close droit
      if (dateFinDroit != null) {
        DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern(DateUtils.FORMATTERSLASHED);
        if (periodeDroit.getPeriodeFin() != null) {
          periodeDroit.setPeriodeFin(
              Collections.min(List.of(dateFinDroit, periodeDroit.getPeriodeFin())));
        } else {
          periodeDroit.setPeriodeFin(dateFinDroit);
        }
        setFermetureAndFin(periodeDroit, dateTimeFormatter);
        if (domaineDroit.getPeriodeOnline() != null) {
          PeriodeDroit periodeOnline = domaineDroit.getPeriodeOnline();
          periodeOnline.setPeriodeFin(
              TriggerUtils.getDateFinOnlineSameFormat(dateFinDroit, periodeOnline.getPeriodeFin()));
          periodeOnline.setPeriodeDebut(periodeDroit.getPeriodeDebut());
          setFermetureAndFin(periodeOnline, dateTimeFormatter);

          if (!Constants.CODE_ETAT_INVALIDE.equals(createdDeclaration.getCodeEtat())) {
            periodeOnline.setPeriodeFermetureFin(periodeOnline.getPeriodeFin());
          }
        }
      }
    }

    return closedDeclaration;
  }

  private void setFermetureAndFin(PeriodeDroit periodeDroit, DateTimeFormatter dateTimeFormatter) {
    LocalDate fin = DateUtils.stringToDate(periodeDroit.getPeriodeFin());
    if (fin != null) {
      periodeDroit.setPeriodeFermetureDebut(fin.plusDays(1).format(dateTimeFormatter));
    }
  }

  boolean checkIfWeCloseAll(
      TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarationstriggeredBenef) {
    if (triggerBeneficiaryToDeclarationstriggeredBenef.getTriggeredBeneficiary().getOldContract()
            != null
        || CollectionUtils.isNotEmpty(
            triggerBeneficiaryToDeclarationstriggeredBenef.getExistingDeclarations())) {
      if (checkForceCloseOffline(triggerBeneficiaryToDeclarationstriggeredBenef)) {
        return true;
      }
      if (triggerBeneficiaryToDeclarationstriggeredBenef.getTriggeredBeneficiary().getOldContract()
          != null) {
        if (checkDiffDateFermeture(triggerBeneficiaryToDeclarationstriggeredBenef)) {
          return true;
        }
        if (checkDiffDroitsHTP(triggerBeneficiaryToDeclarationstriggeredBenef)) {
          return true;
        }
      }
      if (checkDiffParamCarteTP(triggerBeneficiaryToDeclarationstriggeredBenef)) {
        return true;
      }
      Declaration earliestDeclaration =
          getEarliestExistingDeclaration(triggerBeneficiaryToDeclarationstriggeredBenef);

      return checkDiffDomaineDroitByGarantie(
          triggerBeneficiaryToDeclarationstriggeredBenef, earliestDeclaration);
    }

    return false;
  }

  private boolean checkForceCloseOffline(
      TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarationstriggeredBenef) {
    ParametrageCarteTP param =
        triggerBeneficiaryToDeclarationstriggeredBenef.getParametrageCarteTP();
    if (param != null) {
      ParametrageRenouvellement paramRenouv = param.getParametrageRenouvellement();
      if (paramRenouv != null) {
        return Boolean.TRUE.equals(paramRenouv.getAnnulDroitsOffline());
      }
    }
    return false;
  }

  /** si new/old DF != */
  private boolean checkDiffDateFermeture(
      TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarationstriggeredBenef) {
    String oldDateFermeture =
        getDateFermeture(
            triggerBeneficiaryToDeclarationstriggeredBenef
                .getTriggeredBeneficiary()
                .getOldContract()
                .getDateResiliation(),
            triggerBeneficiaryToDeclarationstriggeredBenef
                .getTriggeredBeneficiary()
                .getOldContract()
                .getDateRadiation(),
            triggerBeneficiaryToDeclarationstriggeredBenef.getTrigger().getDateRestitution());

    return oldDateFermeture != null
        && !oldDateFermeture.equals(
            triggerBeneficiaryToDeclarationstriggeredBenef.getDateFermeture());
  }

  /** si new/old droits (sur droits HTP) != */
  private boolean checkDiffDroitsHTP(
      TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarationstriggeredBenef) {

    List<DroitAssure> oldDroits =
        triggerBeneficiaryToDeclarationstriggeredBenef
            .getTriggeredBeneficiary()
            .getOldContract()
            .getDroitsGaranties();

    List<DroitAssure> newDroits =
        triggerBeneficiaryToDeclarationstriggeredBenef
            .getTriggeredBeneficiary()
            .getNewContract()
            .getDroitsGaranties();

    if (oldDroits.size() == newDroits.size()) {
      return changementDroits(newDroits, oldDroits, triggerBeneficiaryToDeclarationstriggeredBenef);
    } else {
      return true;
    }
  }

  boolean changementDroits(
      List<DroitAssure> newDroits,
      List<DroitAssure> oldDroits,
      TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarationstriggeredBenef) {

    // verifier qu'il ne manque pas de garantie ou si il y en a des nouvelles
    Set<String> listOldCodes =
        oldDroits.stream()
            .map(d -> d.getCode() + "_" + d.getCodeAssureur())
            .collect(Collectors.toSet());
    Set<String> listNewCodes =
        newDroits.stream()
            .map(t -> t.getCode() + "_" + t.getCodeAssureur())
            .collect(Collectors.toSet());

    if (!listNewCodes.containsAll(listOldCodes)) {
      return true;
    }
    // BLUE-4739 verifier si il y a des différences de carences entre ancien et
    // nouveau contrat
    Set<CarenceDroit> listOldCarences =
        oldDroits.stream()
            .filter(droitAssureV3 -> droitAssureV3.getCarences() != null)
            .flatMap(droitAssureV3 -> droitAssureV3.getCarences().stream())
            .collect(Collectors.toSet());
    Set<CarenceDroit> listNewCarences =
        newDroits.stream()
            .filter(droitAssureV3 -> droitAssureV3.getCarences() != null)
            .flatMap(droitAssureV3 -> droitAssureV3.getCarences().stream())
            .collect(Collectors.toSet());
    List<CarenceDroit> listOldCarencesNotInNew =
        listOldCarences.stream()
            .filter(
                oldCarenceDroitV3 ->
                    listNewCarences.stream().noneMatch(oldCarenceDroitV3::isEquals))
            .toList();
    List<CarenceDroit> listNewCarencesNotInold =
        listNewCarences.stream()
            .filter(
                newCarenceDroitV3 ->
                    listOldCarences.stream().noneMatch(newCarenceDroitV3::isEquals))
            .toList();
    if (CollectionUtils.isNotEmpty(listOldCarencesNotInNew)
        || CollectionUtils.isNotEmpty(listNewCarencesNotInold)) {
      return true;
    }
    triggerBeneficiaryToDeclarationstriggeredBenef.setChangeRightPeriods(
        changementPeriodeDroits(newDroits, oldDroits));
    if (triggerBeneficiaryToDeclarationstriggeredBenef.isChangeRightPeriods()) return true;
    // Fin BLUE-4739
    return equalsDroits(newDroits, oldDroits);
  }

  private static boolean changementPeriodeDroits(
      List<DroitAssure> newDroits, List<DroitAssure> oldDroits) {
    Set<Periode> listOldPeriodes =
        oldDroits.stream()
            .map(DroitAssureCommun::getPeriode)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    Set<Periode> listNewPeriodes =
        newDroits.stream()
            .map(DroitAssureCommun::getPeriode)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    List<Periode> listOldPeriodesNotInNew =
        listOldPeriodes.stream()
            .filter(
                oldPeriodeDroitV3 ->
                    listNewPeriodes.stream().noneMatch(oldPeriodeDroitV3::isEquals))
            .toList();
    List<Periode> listNewPeriodesNotInold =
        listNewPeriodes.stream()
            .filter(
                newPeriodeDroitV3 ->
                    listOldPeriodes.stream().noneMatch(newPeriodeDroitV3::isEquals))
            .toList();

    return CollectionUtils.isNotEmpty(listNewPeriodesNotInold)
        || CollectionUtils.isNotEmpty(listOldPeriodesNotInNew);
  }

  boolean equalsDroits(List<DroitAssure> newDroits, List<DroitAssure> oldDroits) {
    TreeMap<Pair<String, String>, List<DroitAssure>> newDroitsByKey =
        newDroits.stream()
            .collect(
                groupingBy(
                    droitAssureV3 ->
                        new ImmutablePair<>(
                            droitAssureV3.getCode(), droitAssureV3.getCodeAssureur()),
                    TreeMap::new,
                    Collectors.toList()));

    TreeMap<Pair<String, String>, List<DroitAssure>> oldDroitsByKey =
        oldDroits.stream()
            .collect(
                groupingBy(
                    droitAssureV3 ->
                        new ImmutablePair<>(
                            droitAssureV3.getCode(), droitAssureV3.getCodeAssureur()),
                    TreeMap::new,
                    Collectors.toList()));

    for (Map.Entry<Pair<String, String>, List<DroitAssure>> newEntry : newDroitsByKey.entrySet()) {
      List<DroitAssure> oldDroit = oldDroitsByKey.get(newEntry.getKey());
      if (oldDroit == null) {
        return true;
      }
      List<DroitAssure> newDroit = newEntry.getValue();
      // on compare chaque droit, la taille doit être identique à ce niveau là,
      // la diférence de taille a déjà ete testé avant
      for (int i = 0; i < newDroit.size(); i++) {
        if (!newDroit.get(i).equalsForDeclaration(oldDroit.get(i))) {
          return true;
        }
      }
    }

    return false;
  }

  /** si new/old parametrage carte TP (sur domaines déclarations)!= */
  private boolean checkDiffParamCarteTP(
      TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarationstriggeredBenef) {
    TriggeredBeneficiary oldTriggeredBeneficiarie =
        triggerService.getLastTriggeredBeneficiariesByServicePrestation(
            triggerBeneficiaryToDeclarationstriggeredBenef
                .getTriggeredBeneficiary()
                .getServicePrestationId(),
            triggerBeneficiaryToDeclarationstriggeredBenef.getTriggeredBeneficiary().getId());
    if (oldTriggeredBeneficiarie != null) {
      String oldParametrageId = oldTriggeredBeneficiarie.getParametrageCarteTPId();

      return oldParametrageId != null
          && !oldParametrageId.equals(
              triggerBeneficiaryToDeclarationstriggeredBenef
                  .getTriggeredBeneficiary()
                  .getParametrageCarteTPId());
    }
    return false;
  }

  private Declaration getEarliestExistingDeclaration(
      TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarationstriggeredBenef) {
    return triggerBeneficiaryToDeclarationstriggeredBenef.getExistingDeclarations().getLast();
  }

  /** TODO: Explications */
  private boolean checkDiffDomaineDroitByGarantie(
      TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarationstriggeredBenef,
      Declaration earliestDeclaration) {
    TreeMap<String, List<DomaineDroit>> oldDomaineDroitByGarantie =
        earliestDeclaration.getDomaineDroits().stream()
            .collect(groupingBy(DomaineDroit::getCodeGarantie, TreeMap::new, Collectors.toList()));

    TreeMap<String, List<DomaineDroit>> newDomaineDroitByGarantie =
        triggerBeneficiaryToDeclarationstriggeredBenef
            .getCreatedDeclaration()
            .getDomaineDroits()
            .stream()
            .collect(groupingBy(DomaineDroit::getCodeGarantie, TreeMap::new, Collectors.toList()));

    if (oldDomaineDroitByGarantie.size() != newDomaineDroitByGarantie.size()) {
      return true;
    }

    for (Map.Entry<String, List<DomaineDroit>> stringListEntry :
        oldDomaineDroitByGarantie.entrySet()) {
      List<DomaineDroit> domaineDroitList = newDomaineDroitByGarantie.get(stringListEntry.getKey());
      if (CollectionUtils.isEmpty(domaineDroitList)) {
        // pas de domaine de droits sur une ancienne garantie
        return true;
      }
      DomaineDroit oldDomain = stringListEntry.getValue().get(0);
      // si on ne trouve pas le triplet Version Offre/ Code Offre / Code Produit ->
      // changement dans PW
      Set<String> oldNaturePWList =
          stringListEntry.getValue().stream()
              .map(
                  domaineDroit ->
                      domaineDroit.getVersionOffre()
                          + domaineDroit.getCodeOffre()
                          + domaineDroit.getCodeProduit())
              .collect(Collectors.toSet());
      Set<String> newPWList =
          domaineDroitList.stream()
              .map(
                  domaineDroit ->
                      domaineDroit.getVersionOffre()
                          + domaineDroit.getCodeOffre()
                          + domaineDroit.getCodeProduit())
              .collect(Collectors.toSet());
      if (!oldNaturePWList.equals(newPWList)) {
        return true;
      }
      // BOBB les natures de prestation ne sont pas les mêmes
      Set<String> oldNaturePrestationList =
          stringListEntry.getValue().stream()
              .map(DomaineDroit::getNaturePrestation)
              .collect(Collectors.toSet());
      Set<String> newNaturePrestationList =
          domaineDroitList.stream()
              .map(DomaineDroit::getNaturePrestation)
              .collect(Collectors.toSet());
      if (!oldNaturePrestationList.equals(newNaturePrestationList)) {
        return true;
      }
      // BOBB les dates ne sont pas les mêmes
      if (oldDomain.getPeriodeProductElement() != null
          && !domaineDroitList.stream()
              .allMatch(
                  domaineDroit ->
                      oldDomain.getCodeProduit().equals(domaineDroit.getCodeProduit())
                          && oldDomain
                              .getPeriodeProductElement()
                              .getDebut()
                              .equals(domaineDroit.getPeriodeProductElement().getDebut())
                          && checkPeriodeFinBobb(oldDomain, domaineDroit))) {
        return true;
      }
      // Parametrage de carence différent
      if (!Constants.ORIGINE_DECLARATIONTDB.equals(
          TriggerUtils.getOrigineDeclaration(earliestDeclaration))) {
        for (DomaineDroit oldDomaineDroit : stringListEntry.getValue()) {
          if (oldDomaineDroit.getPeriodeCarence() != null
              && !domaineDroitList.stream()
                  .allMatch(
                      domaineDroit ->
                          oldDomaineDroit.getNaturePrestation() != null
                              && oldDomaineDroit
                                  .getNaturePrestation()
                                  .equals(domaineDroit.getNaturePrestation())
                              && domaineDroit.getPeriodeCarence() != null
                              && (domaineDroit
                                  .getPeriodeCarence()
                                  .isEquals(oldDomaineDroit.getPeriodeCarence())))) {
            return true;
          }
          if (oldDomaineDroit.getPeriodeCarence() == null
              && domaineDroitList.stream()
                  .noneMatch(
                      domaineDroit ->
                          oldDomaineDroit.getNaturePrestation() != null
                              && oldDomaineDroit
                                  .getNaturePrestation()
                                  .equals(domaineDroit.getNaturePrestation())
                              && domaineDroit.getPeriodeCarence() == null)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  private boolean checkPeriodeFinBobb(DomaineDroit oldDomain, DomaineDroit domaineDroit) {
    return (oldDomain.getPeriodeProductElement().getFin() == null
            && domaineDroit.getPeriodeProductElement().getFin() == null)
        || (oldDomain.getPeriodeProductElement().getFin() != null
            && domaineDroit.getPeriodeProductElement().getFin() != null
            && oldDomain
                .getPeriodeProductElement()
                .getFin()
                .equals(domaineDroit.getPeriodeProductElement().getFin()));
  }

  /**
   * Crée les déclarations de fermeture de droits
   *
   * @param triggerBeneficiaryToDeclarationstriggeredBenef objet dans lequel vont être stockées les
   *     déclarations de fermeture
   * @param dateTraitement date utilisée pour la date de création et d'effet début des déclarations
   */
  void fillClosedDeclarations(
      TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarationstriggeredBenef,
      Date dateTraitement) {
    // si FAF, LIST_FERM=fermeture à chaque déclaration de LIST avec date fermeture
    // = DF (a vérifier/confirmer avec Reynald)
    // sinon LIST_FERM=[]
    LinkedList<Declaration> declarationList =
        triggerBeneficiaryToDeclarationstriggeredBenef.getExistingDeclarations();
    boolean shouldCloseOffline =
        checkForceCloseOffline(triggerBeneficiaryToDeclarationstriggeredBenef);
    if (CollectionUtils.isNotEmpty(declarationList)) {
      LinkedList<Declaration> declarationListFermee = new LinkedList<>();
      String dateFermeture = triggerBeneficiaryToDeclarationstriggeredBenef.getDateFermeture();
      if (triggerBeneficiaryToDeclarationstriggeredBenef.getCreatedDeclaration() != null) {
        for (DomaineDroit domaine :
            triggerBeneficiaryToDeclarationstriggeredBenef
                .getCreatedDeclaration()
                .getDomaineDroits()) {
          dateFermeture =
              DateUtils.getMinDate(
                  dateFermeture,
                  DateUtils.dateMinusOneDay(
                      domaine.getPeriodeDroit().getPeriodeDebut(),
                      DateTimeFormatter.ofPattern(DateUtils.FORMATTERSLASHED)));
        }
      }
      if (shouldCloseOffline) {
        String dateFermeturePlus1 =
            DateUtils.datePlusOneDay(
                dateFermeture, DateTimeFormatter.ofPattern(DateUtils.FORMATTERSLASHED));
        declarationList.removeIf(
            declaration ->
                declaration.getDomaineDroits().stream()
                    .anyMatch(
                        domaineDroit ->
                            (domaineDroit.getPeriodeDroit().getPeriodeFermetureFin() == null
                                    && DateUtils.before(
                                        domaineDroit.getPeriodeDroit().getPeriodeFin(),
                                        dateFermeturePlus1,
                                        DateUtils.SLASHED_FORMATTER))
                                || (domaineDroit.getPeriodeDroit().getPeriodeFermetureFin() != null
                                    && DateUtils.before(
                                        domaineDroit.getPeriodeDroit().getPeriodeFermetureFin(),
                                        dateFermeturePlus1,
                                        DateUtils.SLASHED_FORMATTER))));
      }

      LinkedList<Declaration> declarationLinkedList = new LinkedList<>(declarationList);
      Iterator<Declaration> declarationListIterator = declarationLinkedList.descendingIterator();
      // dans l'autre sens :
      while (declarationListIterator.hasNext()) {
        Declaration declaration = declarationListIterator.next();
        Declaration declarationFermee = new Declaration(declaration);
        declarationFermee.setIdOrigine(declaration.get_id());
        declarationFermee.setIdTrigger(
            triggerBeneficiaryToDeclarationstriggeredBenef.getTrigger().getId());
        List<DomaineDroit> domaineDroitList = declarationFermee.getDomaineDroits();
        if (CollectionUtils.isNotEmpty(domaineDroitList)) {
          setEndDateForDomaineDroit(dateFermeture, domaineDroitList, shouldCloseOffline);
          declarationFermee.setEffetDebut(dateTraitement);
          declarationFermee.setDateCreation(dateTraitement);
        }
        setIsEditableForDomainesDroit(
            domaineDroitList, triggerBeneficiaryToDeclarationstriggeredBenef);

        if (StringUtils.isNotBlank(
            triggerBeneficiaryToDeclarationstriggeredBenef.getDateResiliation())) {
          declarationFermee
              .getContrat()
              .setDateResiliation(
                  triggerBeneficiaryToDeclarationstriggeredBenef
                      .getDateResiliation()
                      .replace("-", "/"));
        }
        if (StringUtils.isNotBlank(
            triggerBeneficiaryToDeclarationstriggeredBenef.getDateRadiation())) {
          declarationFermee
              .getBeneficiaire()
              .setDateRadiation(
                  triggerBeneficiaryToDeclarationstriggeredBenef
                      .getDateRadiation()
                      .replace("-", "/"));
        }
        buildDeclarationFermee(declarationFermee);
        declarationListFermee.add(declarationFermee);
      }

      triggerBeneficiaryToDeclarationstriggeredBenef.setClosedDeclarations(declarationListFermee);
    }
  }

  private static void setEndDateForDomaineDroit(
      String dateFermeture, List<DomaineDroit> domaineDroitList, boolean shouldCloseOffline) {
    List<DomaineDroit> domaineDroitListTemp = new ArrayList<>(domaineDroitList);
    for (DomaineDroit domaineDroit : domaineDroitListTemp) {
      PeriodeDroit periodeDroit = domaineDroit.getPeriodeDroit();
      String dateDebut = periodeDroit.getPeriodeDebut();
      String dateFin = periodeDroit.getPeriodeFin();
      if (dateFermeture == null
          || (dateFin != null
              && compareDate(dateFermeture, dateFin, DateUtils.SLASHED_FORMATTER) <= 0)) {
        checkSansEffet(dateFermeture, periodeDroit, dateDebut, shouldCloseOffline);
      }
      periodeDroit.setPeriodeFermetureDebut(
          DateUtils.datePlusOneDay(periodeDroit.getPeriodeFin(), DateUtils.SLASHED_FORMATTER)
              .replace("-", "/"));
      if (StringUtils.isBlank(periodeDroit.getPeriodeFermetureFin())) {
        periodeDroit.setPeriodeFermetureFin(dateFin);
      }
      if (domaineDroit.getPeriodeOnline() != null) {
        PeriodeDroit periodeOnline = domaineDroit.getPeriodeOnline();
        String dateFinOnline = periodeOnline.getPeriodeFin();
        String minDateFin = DateUtils.getMinDate(dateFinOnline, periodeDroit.getPeriodeFin());
        periodeOnline.setPeriodeFin(minDateFin);
        periodeOnline.setPeriodeFermetureDebut(
            DateUtils.datePlusOneDay(minDateFin, DateUtils.SLASHED_FORMATTER).replace("-", "/"));
        periodeOnline.setPeriodeFermetureFin(dateFinOnline);
      }
    }
  }

  private static void checkSansEffet(
      String dateFermeture,
      PeriodeDroit periodeDroit,
      String dateDebut,
      boolean shouldCloseOffline) {
    if (dateFermeture == null
        || (dateDebut != null
            && compareDate(dateFermeture, dateDebut, DateUtils.SLASHED_FORMATTER) <= 0)) {
      periodeDroit.setPeriodeFin(
          DateUtils.dateMinusOneDay(dateDebut, DateUtils.SLASHED_FORMATTER).replace("-", "/"));
      if (shouldCloseOffline) {
        periodeDroit.setPeriodeFermetureFin(periodeDroit.getPeriodeFin());
      }
    } else {
      periodeDroit.setPeriodeFin(dateFermeture);
    }
  }

  private static void buildDeclarationFermee(Declaration declarationFermee) {
    if (declarationFermee.getDateModification() != null) {
      Date dModif = Date.from(declarationFermee.getDateModification().toInstant().plusSeconds(1));
      declarationFermee.setDateModification(dModif);
    }
    declarationFermee.setUserModification(Constants.GENERATION_DROIT_TP);
    declarationFermee.setCodeEtat(Constants.CODE_ETAT_INVALIDE);
    declarationFermee.setIsCarteTPaEditer(false);
  }

  private static void setIsEditableForDomainesDroit(
      List<DomaineDroit> domaineDroitList,
      TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations) {
    if (periodesDroitsCarteExist(triggerBeneficiaryToDeclarations.getTriggeredBeneficiary())) {
      PeriodesDroitsCarte periodesDroitsCarte =
          triggerBeneficiaryToDeclarations
              .getTriggeredBeneficiary()
              .getNewContract()
              .getPeriodesDroitsCarte();
      LocalDate debutCarte = DateUtils.stringToDate(periodesDroitsCarte.getDebut());
      LocalDate finCarte = DateUtils.stringToDate(periodesDroitsCarte.getFin());
      for (DomaineDroit domaineDroit : domaineDroitList) {
        LocalDate debutDomaine =
            DateUtils.stringToDate(domaineDroit.getPeriodeDroit().getPeriodeDebut());
        LocalDate finDomaine =
            DateUtils.stringToDate(domaineDroit.getPeriodeDroit().getPeriodeFin());
        if ((debutDomaine != null && debutDomaine.isAfter(finDomaine))
            || !DateUtils.isOverlapping(debutCarte, finCarte, debutDomaine, finDomaine)) {
          domaineDroit.setIsEditable(false);
        }
      }
    }
  }

  void saveDeclarations(
      TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations, ClientSession session) {
    // * si ALPHA non-ok :
    // ListDeclarationsGénérées=[LIST_FERM] et fin d'algo

    // Si DF != null
    // ListDeclarationsGénérées=[LIST_FERM, ALPHA]
    // sinon
    // ListDeclarationsGénérées=[LIST_FERM, ALPHA, BETA]
    // sinon
    // ListDeclarationsGénérées=[LIST_FERM, ALPHA]

    if (triggerBeneficiaryToDeclarations.getWhatToDo() != RIEN_A_INTEGRER_OU_SANS_EFFET) {
      // LIST_FERM
      for (Declaration declaration : triggerBeneficiaryToDeclarations.getClosedDeclarations()) {
        declarationService.createDeclaration(declaration, session);
        triggerBeneficiaryToDeclarations.getGeneratedDeclarations().add(declaration);
      }
      // ALPHA
      String idPourFermeture = null;
      if (triggerBeneficiaryToDeclarations.getCreatedDeclaration() != null) {
        log.debug("createDeclaration session {}", session);
        idPourFermeture =
            declarationService
                .createDeclaration(
                    triggerBeneficiaryToDeclarations.getCreatedDeclaration(), session)
                .get_id();
        triggerBeneficiaryToDeclarations
            .getGeneratedDeclarations()
            .add(triggerBeneficiaryToDeclarations.getCreatedDeclaration());
        if (triggerBeneficiaryToDeclarations.getFutureDeclaration() != null) {
          declarationService.createDeclaration(
              triggerBeneficiaryToDeclarations.getFutureDeclaration(), session);
          triggerBeneficiaryToDeclarations
              .getGeneratedDeclarations()
              .add(triggerBeneficiaryToDeclarations.getFutureDeclaration());
        }
      }

      if (triggerBeneficiaryToDeclarations.getDateFermeture() != null) {
        // BETA
        Declaration declaration = triggerBeneficiaryToDeclarations.getClosedDeclaration();
        if (declaration != null) {
          declaration.setIdOrigine(idPourFermeture);
          declarationService.createDeclaration(declaration, session);
          triggerBeneficiaryToDeclarations.getGeneratedDeclarations().add(declaration);
        }
      }
    } else {
      buildDeclarationsForBenefInDeleteContrat(triggerBeneficiaryToDeclarations, session);
    }
  }

  private Declaration buildDeclarationOuverte(
      TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations)
      throws DomaineNotFoundException,
          BobbNotFoundException,
          TriggerWarningException,
          PwException,
          CarenceException,
          TriggerParametersException,
          BeneficiaryToIgnoreException {
    TriggeredBeneficiary triggeredBeneficiary =
        triggerBeneficiaryToDeclarations.getTriggeredBeneficiary();
    ParametrageCarteTP parametrageCarteTp =
        triggerBeneficiaryToDeclarations.getParametrageCarteTP();
    Trigger trigger = triggerBeneficiaryToDeclarations.getTrigger();
    Declaration declaration = new Declaration();
    Date dateTraitement = new Date();
    LocalDate now = LocalDate.now(ZoneOffset.UTC);
    List<DroitsTPExtended> droitsTPExtendedList = new ArrayList<>();
    GenerationDomaineResult generationDomaineResult =
        triggerDomaineService.generationDomaine(
            triggeredBeneficiary,
            parametrageCarteTp,
            trigger,
            triggerBeneficiaryToDeclarations.getDateFermeture(),
            triggerBeneficiaryToDeclarations.isContratResiliatedFromTheBeginning(),
            droitsTPExtendedList);
    triggerBeneficiaryToDeclarations.setWarning(generationDomaineResult.isWarning());
    if (CollectionUtils.isEmpty(generationDomaineResult.getDomaineDroitList())) {
      if (generationDomaineResult.isWarning()) {
        return null;
      } else {
        throw new DomaineNotFoundException(
            TriggeredBeneficiaryAnomaly.create(
                Anomaly.NO_WARRANTY_FOR_THIS_CONTRACT,
                triggeredBeneficiary.getNumeroContrat(),
                triggeredBeneficiary.getNumeroPersonne(),
                triggeredBeneficiary.getAmc()));
      }
    }

    if (TriggerEmitter.Event.equals(trigger.getOrigine())) {
      processDomaineDroitEditable(
          generationDomaineResult.getDomaineDroitList(),
          triggeredBeneficiary,
          trigger.isEventReprise());
    }

    // task/blue-4651 : carences cas n°7
    // On enlève les domaines ayant un date de début supérieure ou égale a la date
    // de fin car ça n'a aucun sens dans le cadre d'une déclaration ouverte
    checkWarning(generationDomaineResult.getDomaineDroitList());

    MotifEvenement motifEvenement =
        TriggerEmitter.Renewal.equals(trigger.getOrigine()) ? MotifEvenement.RE : MotifEvenement.DE;
    generationDomaineResult
        .getDomaineDroitList()
        .forEach(
            domaineDroit -> {
              domaineDroit.getPeriodeDroit().setMotifEvenement(motifEvenement.name());
              domaineDroit.getPeriodeDroit().setLibelleEvenement(motifEvenement.getLibelle());
            });

    declaration.setDomaineDroits(generationDomaineResult.getDomaineDroitList());
    String dateDebut =
        generationDomaineResult.getDomaineDroitList().stream()
            .map(d -> d.getPeriodeDroit().getPeriodeDebut())
            .min(Comparator.naturalOrder())
            .orElse(null);

    // map rest of the declaration
    declaration.setBeneficiaire(
        triggerMapper.mapBeneficiaire(triggeredBeneficiary, dateDebut, DateUtils.formatDate(now)));
    ParametrageDroitsCarteTP parametrage = parametrageCarteTp.getParametrageDroitsCarteTP();
    declaration.setContrat(
        triggerMapper.mapContrat(
            triggeredBeneficiary,
            parametrage,
            now,
            Objects.requireNonNull(dateDebut),
            droitsTPExtendedList));

    declaration.setCodeEtat(Constants.CODE_ETAT_VALIDE);
    declaration.setEtatSuspension(
        suspensionService.suspensionCalculation(
            triggeredBeneficiary.getOldContract(), triggeredBeneficiary.getNewContract()));
    mapPeriodeSuspensionsDecla(
        declaration, triggeredBeneficiary.getNewContract().getPeriodesSuspension());

    declaration.setEffetDebut(dateTraitement);
    declaration.setIdDeclarant(triggeredBeneficiary.getAmc());

    declaration.setIsCarteTPaEditer(triggeredBeneficiary.getIsCartePapierAEditer());
    String resultDigital = TriggerUtils.calculateDigital(triggeredBeneficiary, parametrage);

    declaration.setCarteTPaEditerOuDigitale(resultDigital);

    declaration.setIdTrigger(triggeredBeneficiary.getIdTrigger());
    declaration.setNomFichierOrigine(trigger.getNomFichierOrigine());

    declaration.setDateCreation(dateTraitement);
    declaration.setDateModification(dateTraitement);
    declaration.setUserCreation(trigger.getOrigine().toString());
    declaration.setUserModification(trigger.getOrigine().toString());
    declaration.setVersionDeclaration("3.1");

    if (triggerBeneficiaryToDeclarations.isContratResiliatedFromTheBeginning()) {
      if (StringUtils.isNotBlank(triggerBeneficiaryToDeclarations.getDateResiliation())) {
        declaration
            .getContrat()
            .setDateResiliation(
                triggerBeneficiaryToDeclarations.getDateResiliation().replace("-", "/"));
      }
      if (StringUtils.isNotBlank(triggerBeneficiaryToDeclarations.getDateRadiation())) {
        declaration
            .getBeneficiaire()
            .setDateRadiation(
                triggerBeneficiaryToDeclarations.getDateRadiation().replace("-", "/"));
      }
    }

    return declaration;
  }

  /** Map les periodesSuspension du triggerBenef dans la declaration */
  private void mapPeriodeSuspensionsDecla(
      Declaration newDeclaration, List<PeriodeSuspension> periodeSuspensions) {
    if (CollectionUtils.isNotEmpty(periodeSuspensions)) {
      List<PeriodeSuspensionDeclaration> periodeSuspensionsDeepCopy = new ArrayList<>();
      for (PeriodeSuspension periodeSuspension : periodeSuspensions) {
        periodeSuspensionsDeepCopy.add(getPeriodeSuspensionDeclaration(periodeSuspension));
      }
      newDeclaration.getContrat().setPeriodeSuspensions(periodeSuspensionsDeepCopy);
    }
  }

  private static @NotNull PeriodeSuspensionDeclaration getPeriodeSuspensionDeclaration(
      PeriodeSuspension periodeSuspension) {
    PeriodeSuspensionDeclaration copyPeriode = new PeriodeSuspensionDeclaration();
    copyPeriode.setDebut(periodeSuspension.getPeriode().getDebut().replace("-", "/"));
    if (periodeSuspension.getPeriode().getFin() != null) {
      copyPeriode.setFin(periodeSuspension.getPeriode().getFin().replace("-", "/"));
    }
    copyPeriode.setTypeSuspension(periodeSuspension.getTypeSuspension());
    copyPeriode.setMotifSuspension(periodeSuspension.getMotifSuspension());
    copyPeriode.setMotifLeveeSuspension(periodeSuspension.getMotifLeveeSuspension());
    return copyPeriode;
  }

  /**
   * Si le droit tp est flag a editable alors 3 possibilites : (1) le domaine droit a une periode en
   * dehors de la {@link PeriodesDroitsCarte} alors il n'est pas editable. (2) le domaine droit a
   * une periode a l'interieur de la {@link PeriodesDroitsCarte} alors il est editable. (3) le
   * domaine droit a une periode qui chevauche la {@link PeriodesDroitsCarte} alors on va couper le
   * domaine droit en un domaine droit editable et un ou plusieurs domaine(s) droit non editable(s).
   */
  private void processDomaineDroitEditable(
      List<DomaineDroit> domainesDeclaration,
      TriggeredBeneficiary triggeredBeneficiary,
      boolean eventReprise) {
    if (isNew(triggeredBeneficiary)
        || isAdjonction(triggeredBeneficiary)
        || isNewWithRightsAndOldWithout(triggeredBeneficiary)) {
      PeriodesDroitsCarte periodesDroitsCarte =
          triggeredBeneficiary.getNewContract().getPeriodesDroitsCarte();
      LocalDate debutDroitsCarte = DateUtils.stringToDate(periodesDroitsCarte.getDebut());
      LocalDate finDroitsCarte = DateUtils.stringToDate(periodesDroitsCarte.getFin());
      if (eventReprise) {
        finDroitsCarte = Objects.requireNonNull(finDroitsCarte).plusYears(1);
      }
      List<DomaineDroit> domaineDroitList =
          getDomaineDroitsEditable(domainesDeclaration, debutDroitsCarte, finDroitsCarte);
      domainesDeclaration.clear();
      domainesDeclaration.addAll(domaineDroitList);
    }
  }

  private static boolean isNew(TriggeredBeneficiary triggeredBeneficiary) {
    return periodesDroitsCarteExist(triggeredBeneficiary)
        && (triggeredBeneficiary.getOldContract() == null
            || triggeredBeneficiary.getOldContract().getPeriodesDroitsCarte() != null);
  }

  private static boolean isAdjonction(TriggeredBeneficiary triggeredBeneficiary) {
    return periodesDroitsCarteExist(triggeredBeneficiary)
        && triggeredBeneficiary.getOldContract() != null
        && triggeredBeneficiary.getOldContract().getPeriodesDroitsCarte() != null
        && !triggeredBeneficiary
            .getNewContract()
            .getPeriodesDroitsCarte()
            .getDebut()
            .equals(triggeredBeneficiary.getOldContract().getPeriodesDroitsCarte().getDebut());
  }

  private static boolean isNewWithRightsAndOldWithout(TriggeredBeneficiary triggeredBeneficiary) {
    return periodesDroitsCarteExist(triggeredBeneficiary)
        && triggeredBeneficiary.getOldContract() != null
        && triggeredBeneficiary.getOldContract().getPeriodesDroitsCarte() == null;
  }

  private static boolean periodesDroitsCarteExist(TriggeredBeneficiary triggeredBeneficiary) {
    return triggeredBeneficiary != null
        && triggeredBeneficiary.getNewContract() != null
        && triggeredBeneficiary.getNewContract().getPeriodesDroitsCarte() != null
        && triggeredBeneficiary.getNewContract().getPeriodesDroitsCarte().getDebut() != null
        && triggeredBeneficiary.getNewContract().getPeriodesDroitsCarte().getFin() != null;
  }

  private List<DomaineDroit> getDomaineDroitsEditable(
      List<DomaineDroit> domainesDeclaration,
      LocalDate debutDroitsCarte,
      LocalDate finDroitsCarte) {
    List<DomaineDroit> domaineDroitList = new ArrayList<>();
    for (DomaineDroit domaineDroit : domainesDeclaration) {
      if (Boolean.FALSE.equals(domaineDroit.getIsEditable())) {
        domaineDroitList.add(domaineDroit);
        continue;
      }

      LocalDate debutDomaine =
          DateUtils.stringToDate(domaineDroit.getPeriodeDroit().getPeriodeDebut());
      LocalDate finDomaine = DateUtils.stringToDate(domaineDroit.getPeriodeDroit().getPeriodeFin());

      // Partie avant la periode de droit carte, non editable
      if (debutDroitsCarte.isAfter(debutDomaine)) {
        domaineDroitList.add(
            createFirstPartDomaineEditable(domaineDroit, finDomaine, debutDroitsCarte));
      }

      // Partie pendant la periode de droit carte, editable
      if (DateUtils.isOverlapping(debutDroitsCarte, finDroitsCarte, debutDomaine, finDomaine)) {
        domaineDroitList.add(
            createMiddlePartDomaineEditable(
                domaineDroit, debutDomaine, finDomaine, debutDroitsCarte, finDroitsCarte));
      }

      // Partie apres la periode de droit carte, non editable
      if (finDroitsCarte.isBefore(finDomaine)) {
        domaineDroitList.add(
            createLastPartDomaineEditable(domaineDroit, debutDomaine, finDroitsCarte));
      }

      // Si le domaineDroit avait une fin online infini (null) alors on veut retouver
      // cette information sur la derniere partie creee
      if (domaineDroit.getPeriodeOnline() != null
          && StringUtils.isBlank(domaineDroit.getPeriodeOnline().getPeriodeFin())) {
        DomaineDroit lastCreated = domaineDroitList.get(domaineDroitList.size() - 1);
        if (lastCreated.getPeriodeOnline() != null) {
          lastCreated.getPeriodeOnline().setPeriodeFin(null);
        }
      }
    }
    return domaineDroitList;
  }

  private DomaineDroit createFirstPartDomaineEditable(
      DomaineDroit domaineDroit, LocalDate finDomaine, LocalDate debutDroitsCarte) {
    DomaineDroit domaineDroitSub = new DomaineDroit(domaineDroit);
    LocalDate minDate = DateUtils.getMinDate(finDomaine, debutDroitsCarte.minusDays(1));
    domaineDroitSub.getPeriodeDroit().setPeriodeFin(minDate.format(DateUtils.SLASHED_FORMATTER));
    if (domaineDroit.getPeriodeOnline() != null) {
      LocalDate finOnline = DateUtils.stringToDate(domaineDroit.getPeriodeOnline().getPeriodeFin());
      LocalDate minOnlineDate = DateUtils.getMinDate(finOnline, debutDroitsCarte.minusDays(1));
      domaineDroitSub
          .getPeriodeOnline()
          .setPeriodeFin(minOnlineDate.format(DateUtils.SLASHED_FORMATTER));
    }
    domaineDroitSub.setIsEditable(false);
    return domaineDroitSub;
  }

  private DomaineDroit createMiddlePartDomaineEditable(
      DomaineDroit domaineDroit,
      LocalDate debutDomaine,
      LocalDate finDomaine,
      LocalDate debutDroitsCarte,
      LocalDate finDroitsCarte) {
    DomaineDroit domaineDroitSub = new DomaineDroit(domaineDroit);
    LocalDate debutDate = DateUtils.getMaxDate(debutDomaine, debutDroitsCarte);
    LocalDate finDate = DateUtils.getMinDate(finDomaine, finDroitsCarte);
    domaineDroitSub
        .getPeriodeDroit()
        .setPeriodeDebut(debutDate.format(DateUtils.SLASHED_FORMATTER));
    domaineDroitSub.getPeriodeDroit().setPeriodeFin(finDate.format(DateUtils.SLASHED_FORMATTER));
    if (domaineDroit.getPeriodeOnline() != null) {
      LocalDate finOnline = DateUtils.stringToDate(domaineDroit.getPeriodeOnline().getPeriodeFin());
      LocalDate debutOnline =
          DateUtils.stringToDate(domaineDroit.getPeriodeOnline().getPeriodeDebut());
      LocalDate debutOnlineDate = DateUtils.getMaxDate(debutOnline, debutDroitsCarte);
      LocalDate finOnlineDate = DateUtils.getMinDate(finOnline, finDroitsCarte);
      domaineDroitSub
          .getPeriodeOnline()
          .setPeriodeDebut(debutOnlineDate.format(DateUtils.SLASHED_FORMATTER));
      domaineDroitSub
          .getPeriodeOnline()
          .setPeriodeFin(finOnlineDate.format(DateUtils.SLASHED_FORMATTER));
    }
    domaineDroitSub.setIsEditable(true);
    return domaineDroitSub;
  }

  private DomaineDroit createLastPartDomaineEditable(
      DomaineDroit domaineDroit, LocalDate debutDomaine, LocalDate finDroitsCarte) {
    DomaineDroit domaineDroitSub = new DomaineDroit(domaineDroit);
    LocalDate maxDate = DateUtils.getMaxDate(debutDomaine, finDroitsCarte.plusDays(1));
    domaineDroitSub.getPeriodeDroit().setPeriodeDebut(maxDate.format(DateUtils.SLASHED_FORMATTER));
    if (domaineDroit.getPeriodeOnline() != null) {
      LocalDate debutOnline =
          DateUtils.stringToDate(domaineDroit.getPeriodeOnline().getPeriodeDebut());
      LocalDate maxOnlineDate = DateUtils.getMaxDate(debutOnline, finDroitsCarte.plusDays(1));
      domaineDroitSub
          .getPeriodeOnline()
          .setPeriodeDebut(maxOnlineDate.format(DateUtils.SLASHED_FORMATTER));
    }
    domaineDroitSub.setIsEditable(false);
    return domaineDroitSub;
  }

  private void checkWarning(@NonNull List<DomaineDroit> domainesDeclaration)
      throws TriggerWarningException {
    List<DomaineDroit> domainesDeclarationWithWarning =
        domainesDeclaration.stream()
            .filter(domaineDroit -> domaineDroit.getCodeCarence() != null)
            .toList();
    if (!domainesDeclarationWithWarning.isEmpty()) {
      boolean noCarenceComplete =
          domainesDeclarationWithWarning.stream()
              .noneMatch(
                  domain -> {
                    LocalDate fin =
                        DateUtils.stringToDate(domain.getPeriodeDroit().getPeriodeFin());
                    // Si il n'y a pas de date de fin, pas de risque
                    if (fin == null) {
                      return true;
                    }

                    LocalDate debut =
                        DateUtils.stringToDate(domain.getPeriodeDroit().getPeriodeDebut());
                    // Si il n'y a pas de date de début, c'est anormal
                    if (debut == null) {
                      return false;
                    }

                    // Si il y a les deux dates, on fait la comparaison
                    return fin.isAfter(debut);
                  });

      if (noCarenceComplete) {
        throw new TriggerWarningException(
            TriggeredBeneficiaryAnomaly.create(Anomaly.NO_TP_RIGHTS_CAUSED_BY_WAITINGS_PERIODS));
      }
    }
  }
}
