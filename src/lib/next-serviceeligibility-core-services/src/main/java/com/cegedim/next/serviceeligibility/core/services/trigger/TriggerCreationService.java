package com.cegedim.next.serviceeligibility.core.services.trigger;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.RENEWAL_THRESHOLD;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.bobb.GarantieTechnique;
import com.cegedim.next.serviceeligibility.core.dao.RequestParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.kafka.trigger.Producer;
import com.cegedim.next.serviceeligibility.core.mapper.trigger.TriggerMapper;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageRenouvellement;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.*;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiaryAnomaly;
import com.cegedim.next.serviceeligibility.core.model.enumeration.DateRenouvellementCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ModeDeclenchementCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ParametrageCarteTPStatut;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.TriggerId;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContexteTPV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratCollectifV6;
import com.cegedim.next.serviceeligibility.core.services.ParametrageCarteTPService;
import com.cegedim.next.serviceeligibility.core.services.bdd.SasContratService;
import com.cegedim.next.serviceeligibility.core.services.bdd.ServicePrestationService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import com.cegedim.next.serviceeligibility.core.services.pojo.CreatedTriggerAndBenefs;
import com.cegedim.next.serviceeligibility.core.services.pojo.ParametrageTrigger;
import com.cegedim.next.serviceeligibility.core.services.pojo.WaitingExtendedOffreProduits;
import com.cegedim.next.serviceeligibility.core.utils.*;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.BeneficiaryToIgnoreException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.BobbNotFoundException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.PilotageBoContexteException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.TriggerException;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TriggerCreationService {

  private static final String INVALID_REQUEST =
      "La requête de création du trigger est incomplète !";
  private static final String LA_DATE_S_N_EST_PAS_UNE_DATE_AU_FORMAT_S =
      "La date %s n'est pas une date au format %s";

  private final Logger logger = LoggerFactory.getLogger(TriggerCreationService.class);

  private final SimpleDateFormat formatter = new SimpleDateFormat(DateUtils.YYYY_MM_DD);

  private static final String RECYCLAGE_FALSE = "false";

  protected final ParametrageCarteTPService paramCarteTPService;

  private final SasContratService sasContratService;

  private final Producer producer;

  private final TriggerService triggerService;

  private final MongoClient client;

  private final TriggerMapper triggerMapper;

  private final ServicePrestationService servicePrestationService;

  private final BeyondPropertiesService beyondPropertiesService;

  @ContinueSpan(log = "generateTriggers (1 param)")
  public List<Trigger> generateTriggers(TriggerGenerationRequest request) {
    List<Trigger> triggers = new ArrayList<>();
    if (request.getEmitter() == null || StringUtils.isBlank(request.getEmitter().toString())) {
      throw new TriggerException(INVALID_REQUEST);
    }
    // Request from UI
    else if (TriggerEmitter.Request.equals(request.getEmitter())
        && StringUtils.isNotBlank(request.getIdDeclarant())
        && StringUtils.isNotBlank(request.getIndividualContractNumber())
        && StringUtils.isNotBlank(request.getDate())
        && StringUtils.isNotBlank(request.getNumeroAdherent())) {
      ContratAIV6 contrat =
          servicePrestationService.getContratByUK(
              request.getIdDeclarant(),
              request.getIndividualContractNumber(),
              request.getNumeroAdherent());
      request.setGenerationNextYear(generateNextYear(contrat));
      manageTriggerForContract(request, triggers, false, contrat, null, false, null, true);
    }
    // Renewal
    else if (request.getEmitter().equals(TriggerEmitter.Renewal)) {
      manageTriggersForBatch(request, triggers);
    } else {
      throw new TriggerException(INVALID_REQUEST);
    }
    return triggers;
  }

  public void manageTriggersForBatch(TriggerGenerationRequest request, List<Trigger> triggers) {
    logger.info("Gestion des déclencheurs pour le batch {}", request.getDate());
    String date = request.getDate();
    Date dateTraitement = new Date();
    if (date == null) {
      date = formatter.format(dateTraitement);
    } else {
      if (!DateUtils.isValidDate(date, DateUtils.YYYY_MM_DD, false)) {
        throw new TriggerException(
            String.format(LA_DATE_S_N_EST_PAS_UNE_DATE_AU_FORMAT_S, date, DateUtils.YYYY_MM_DD));
      }
      try {
        dateTraitement = formatter.parse(date);
      } catch (ParseException e) {
        throw new TriggerException(
            String.format(LA_DATE_S_N_EST_PAS_UNE_DATE_AU_FORMAT_S, date, DateUtils.YYYY_MM_DD));
      }
    }

    logger.debug("Date de traitement {}", dateTraitement);
    // Récupération des paramétrage de carte TP à gérer
    List<ParametrageCarteTP> params =
        paramCarteTPService.getParametrageToExecute(date, request.isRdo());

    // Récupération des contrats impactés pour chaque paramétrage
    if (!CollectionUtils.isEmpty(params)) {
      getContractsConcernedBySetting(request, triggers, date, dateTraitement, params);
    } else {
      logger.info("Aucun paramétrage de carte TP à traiter");
    }
  }

  @ContinueSpan(log = "manageTriggerForContract")
  public void manageTriggerForContract(
      TriggerGenerationRequest request,
      List<Trigger> triggers,
      boolean createTriggeredBenefWhenError,
      ContratAIV6 newContract,
      ContratAIV6 oldContract,
      boolean isEndpointDelete,
      String nomFichierOrigine,
      boolean sentToKafka) {
    // si oldContract est renseigné et newContract est null : cas du delete
    // si oldContract est null et newContract est renseigné : cas du event insert
    // si oldContract est renseigné et newContract est renseigné : cas du event
    // update
    String keyTopicDemandeDeclaration;
    if (newContract != null) {
      keyTopicDemandeDeclaration =
          newContract.getIdDeclarant() + newContract.getNumero() + newContract.getNumeroAdherent();
    } else {
      keyTopicDemandeDeclaration =
          oldContract.getIdDeclarant() + oldContract.getNumero() + oldContract.getNumeroAdherent();
    }

    CreatedTriggerAndBenefs createdTriggerAndBenefs;
    if (createTriggeredBenefWhenError) {
      createdTriggerAndBenefs =
          createTriggerNoControl(request, newContract, oldContract, isEndpointDelete);
    } else {
      createdTriggerAndBenefs =
          createTrigger(
              request,
              createTriggeredBenefWhenError,
              newContract,
              oldContract,
              isEndpointDelete,
              nomFichierOrigine);
    }

    saveTriggerAndSendMessagesKafka(
        request,
        triggers,
        newContract,
        createdTriggerAndBenefs.getBenefs(),
        createdTriggerAndBenefs.getTrigger(),
        sentToKafka,
        createdTriggerAndBenefs.isErrorContract(),
        keyTopicDemandeDeclaration);

    if (request.isGenerationNextYear()) {
      if (createTriggeredBenefWhenError) {
        createdTriggerAndBenefs =
            createTriggerNoControl(request, newContract, oldContract, isEndpointDelete);
      } else {
        createdTriggerAndBenefs =
            createTrigger(
                request,
                createTriggeredBenefWhenError,
                newContract,
                oldContract,
                isEndpointDelete,
                nomFichierOrigine);
      }

      createdTriggerAndBenefs.getTrigger().setEventReprise(true);
      saveTriggerAndSendMessagesKafka(
          request,
          triggers,
          newContract,
          createdTriggerAndBenefs.getBenefs(),
          createdTriggerAndBenefs.getTrigger(),
          sentToKafka,
          createdTriggerAndBenefs.isErrorContract(),
          keyTopicDemandeDeclaration);
    }
  }

  // creation du trigger sans contrôle
  private CreatedTriggerAndBenefs createTriggerNoControl(
      TriggerGenerationRequest request,
      ContratAIV6 newContract,
      ContratAIV6 oldContract,
      boolean isEndpointDelete) {
    // Création du déclencheur
    Trigger trigger = new Trigger();

    trigger.setAmc(request.getIdDeclarant());
    if (newContract != null) {
      ContexteTPV6 context = newContract.getContexteTiersPayant();
      if (context != null && context.getDateRestitutionCarte() != null) {
        trigger.setDateRestitution(context.getDateRestitutionCarte().replace("-", "/"));
      }
    }

    trigger.setDateEffet(request.getDate());
    trigger.setOrigine(request.getEmitter());

    // Création des bénéficiaires associés au déclencheur
    List<TriggeredBeneficiary> newBenefs = new ArrayList<>();
    List<TriggeredBeneficiary> oldBenefs = new ArrayList<>();
    if (newContract != null) {
      newBenefs =
          TriggerUtils.extractBenefsFromContracts(List.of(newContract), isEndpointDelete, true);

      if (CollectionUtils.isEmpty(newBenefs)) {
        throw new TriggerException(
            String.format(
                "Pour le contrat %s-%s, aucun bénéficiaire n'est éligible en date du %s",
                request.getIdDeclarant(),
                request.getIndividualContractNumber(),
                request.getDate()));
      }
    }

    if (oldContract != null) {
      oldBenefs =
          TriggerUtils.extractBenefsFromContracts(List.of(oldContract), isEndpointDelete, false);
    }

    // This method also fuses oldBenefs' oldConract into newBenefs' oldContract if
    // the benef is found
    List<TriggeredBeneficiary> benefs =
        TriggerUtils.extractBenefsUniquesFromContracts(newBenefs, oldBenefs);

    extractForceCartePapierDemat(request, benefs);
    CreatedTriggerAndBenefs createdTriggerAndBenefs = new CreatedTriggerAndBenefs();
    createdTriggerAndBenefs.setBenefs(benefs);
    createdTriggerAndBenefs.setTrigger(trigger);
    return createdTriggerAndBenefs;
  }

  private CreatedTriggerAndBenefs createTrigger(
      TriggerGenerationRequest request,
      boolean createTriggeredBenefWhenError,
      ContratAIV6 newContract,
      ContratAIV6 oldContract,
      boolean isEndpointDelete,
      String nomFichierOrigine) {
    // Création du déclencheur
    Trigger trigger = new Trigger();

    trigger.setNomFichierOrigine(nomFichierOrigine);
    trigger.setAmc(request.getIdDeclarant());
    if (newContract != null) {
      ContexteTPV6 context = newContract.getContexteTiersPayant();
      if (context != null && context.getDateRestitutionCarte() != null) {
        trigger.setDateRestitution(context.getDateRestitutionCarte().replace("-", "/"));
      }
    }

    // Checks if the date is valid
    if (!DateUtils.isValidDate(request.getDate(), DateUtils.YYYY_MM_DD, false)) {
      throw new TriggerException(
          String.format(
              LA_DATE_S_N_EST_PAS_UNE_DATE_AU_FORMAT_S, request.getDate(), DateUtils.YYYY_MM_DD));
    }

    trigger.setDateEffet(request.getDate());
    trigger.setOrigine(request.getEmitter());

    // Création des bénéficiaires associés au déclencheur
    List<TriggeredBeneficiary> newBenefs = new ArrayList<>();
    List<TriggeredBeneficiary> oldBenefs = new ArrayList<>();

    boolean errorContract = false;

    List<TriggeredBeneficiary> benefsToIgnore = new ArrayList<>(); // Map contrats
    if (newContract != null) {
      newBenefs =
          TriggerUtils.extractBenefsFromContracts(List.of(newContract), isEndpointDelete, true);

      if (CollectionUtils.isEmpty(newBenefs)) {
        throw new TriggerException(
            String.format(
                "Pour le contrat %s-%s, aucun bénéficiaire n'est éligible en date du %s",
                request.getIdDeclarant(),
                request.getIndividualContractNumber(),
                request.getDate()));
      }
      errorContract =
          mapProduitBenefs(
              newBenefs,
              createTriggeredBenefWhenError,
              newContract,
              !TriggerEmitter.Renewal.equals(trigger.getOrigine()),
              TriggerEmitter.Request.equals(trigger.getOrigine()),
              benefsToIgnore);
    }

    if (oldContract != null) {
      oldBenefs =
          TriggerUtils.extractBenefsFromContracts(List.of(oldContract), isEndpointDelete, false);
      getParametrageForBenefs(oldBenefs, oldContract, request.getEmitter());
    }

    // This method also fuses oldBenefs' oldConract into newBenefs' oldContract if
    // the benef is found
    List<TriggeredBeneficiary> benefs =
        TriggerUtils.extractBenefsUniquesFromContracts(newBenefs, oldBenefs);

    // Removes beneficiaries with only guarantees to be ignored
    benefs.removeAll(benefsToIgnore);
    extractForceCartePapierDemat(request, benefs);
    CreatedTriggerAndBenefs createdTriggerAndBenefs = new CreatedTriggerAndBenefs();
    createdTriggerAndBenefs.setBenefs(benefs);
    createdTriggerAndBenefs.setTrigger(trigger);
    createdTriggerAndBenefs.setErrorContract(errorContract);
    return createdTriggerAndBenefs;
  }

  private void extractForceCartePapierDemat(
      TriggerGenerationRequest request, List<TriggeredBeneficiary> benefs) {
    if (TriggerEmitter.Request.equals(request.getEmitter())) {
      boolean forcePapier = Boolean.TRUE.equals(request.getForcePapier());
      boolean forceDemat = Boolean.TRUE.equals(request.getForceDemat());
      for (TriggeredBeneficiary benef : benefs) {
        benef.setIsCartePapier(forcePapier);
        benef.setIsCartePapierAEditer(forcePapier);
        benef.setIsCarteDematerialisee(forceDemat);
      }
    }
  }

  private void saveTriggerAndSendMessagesKafka(
      TriggerGenerationRequest request,
      List<Trigger> triggers,
      ContratAIV6 newContract,
      List<TriggeredBeneficiary> benefs,
      Trigger trigger,
      boolean sentToKafka,
      boolean errorContract,
      String keyTopicDemandeDeclaration) {
    if (newContract != null) {
      // Sauvegarde des données en base
      Trigger savedTrigger = triggerService.saveToMongoNewTrigger(trigger, benefs, null);

      // If the contract didn't raise any error... && the trigger was saved in
      // Mongo...
      if (!errorContract && savedTrigger != null) {
        // Update 'triggers' for return and send message to Kafka
        triggers.add(savedTrigger);
        if (sentToKafka) {
          producer.sendContract(
              new TriggerId(trigger.getId()), RECYCLAGE_FALSE, keyTopicDemandeDeclaration);
        }
      }
      // If the contract raised an error...
      else if (savedTrigger != null) {
        triggers.add(savedTrigger);
        sasContratService.manageSasContrat(
            savedTrigger,
            benefs,
            request.getIdDeclarant(),
            request.getIndividualContractNumber(),
            newContract.getId(),
            newContract.getNumeroAdherent());
      }
    } else {
      // cas du delete contrat
      logger.debug("Action de Fermeture sur les benefs");

      Trigger savedTrigger = triggerService.saveToMongoForDeleteContrat(trigger, benefs);

      // If the trigger was saved in Mongo...
      if (savedTrigger != null) {
        // Update 'triggers' for return and send message to Kafka
        triggers.add(savedTrigger);
        if (sentToKafka) {
          producer.sendContract(
              new TriggerId(savedTrigger.getId()), RECYCLAGE_FALSE, keyTopicDemandeDeclaration);
        }
      }
    }
  }

  @ContinueSpan(log = "manageTriggerForContract")
  public void manageTriggerForContract(
      TriggerGenerationRequest request,
      List<Trigger> triggers,
      ContratAIV6 newContract,
      ContratAIV6 oldContract,
      boolean isEndpointDelete) {
    manageTriggerForContract(
        request, triggers, true, newContract, oldContract, isEndpointDelete, null, true);
  }

  /**
   * méthode appelé seulement pour une création de droit ce n'est pas appelé pour un sans effet (où
   * newContrat est vide)
   */
  @ContinueSpan(log = "mapProduitBenefsAndIgnoreForRenewal")
  public boolean mapProduitBenefsAndIgnoreForRenewal(
      List<TriggeredBeneficiary> benefs,
      ContratAIV6 contrat,
      List<ParametrageCarteTP> parametrageCarteTPList) {
    List<TriggeredBeneficiary> toIgnore = new ArrayList<>();
    boolean error = mapProduitBenefsForRenewal(benefs, contrat, parametrageCarteTPList, toIgnore);
    benefs.removeAll(toIgnore);
    return error;
  }

  /**
   * méthode appelé seulement pour une création de droit ce n'est pas appelé pour un sans effet (où
   * newContrat est vide)
   */
  @ContinueSpan(log = "mapProduitBenefs")
  private boolean mapProduitBenefs(
      List<TriggeredBeneficiary> benefs,
      boolean createTriggeredBenefWhenError,
      ContratAIV6 contrat,
      boolean notRenewal,
      boolean notBo,
      List<TriggeredBeneficiary> toIgnore) {
    boolean errorContract = false;

    // If there are at least one beneficiary...
    if (contrat != null && CollectionUtils.isNotEmpty(benefs)) {
      // For each beneficiary...
      for (TriggeredBeneficiary benef : benefs) {
        try {
          List<DroitAssure> droits = benef.getNewContract().getDroitsGaranties();

          // If the beneficiary have open rights...
          mapOpenRights(createTriggeredBenefWhenError, contrat, notRenewal, benef, droits, notBo);
        } catch (BobbNotFoundException e) {
          // If there's an error...
          errorContract = true;
          TriggerUtils.manageBenefError(
              createTriggeredBenefWhenError, benef, e.getTriggeredBeneficiaryAnomaly(), true);
        } catch (PilotageBoContexteException e) {
          logger.warn(e.getMessage(), e);
          TriggerUtils.manageBenefWarning(benef, e.getTriggeredBeneficiaryAnomaly(), true);
        } catch (BeneficiaryToIgnoreException e) {
          logger.info("Benef ignored {}-{}", benef.getAmc(), benef.getNumeroPersonne());
          toIgnore.add(benef);
        }
      }
    } else if (contrat == null) {
      errorContract = true;
    }

    // Return a boolean saying if there was at least one error within the
    // contract
    return errorContract;
  }

  @ContinueSpan(log = "mapProduitBenefsForRenewal")
  private boolean mapProduitBenefsForRenewal(
      List<TriggeredBeneficiary> benefs,
      ContratAIV6 contrat,
      List<ParametrageCarteTP> parametrageCarteTPList,
      List<TriggeredBeneficiary> toIgnore) {
    boolean errorContract = false;

    // If there are at least one beneficiary...
    if (contrat != null && CollectionUtils.isNotEmpty(benefs)) {
      // For each beneficiary...
      for (TriggeredBeneficiary benef : benefs) {
        try {
          List<DroitAssure> droits = benef.getNewContract().getDroitsGaranties();

          // If the beneficiary have open rights...
          mapOpenRightsRenewal(contrat, parametrageCarteTPList, benef, droits);
        } catch (BobbNotFoundException e) {
          // If there's an error...
          errorContract = true;
          TriggerUtils.manageBenefError(true, benef, e.getTriggeredBeneficiaryAnomaly(), true);
        } catch (PilotageBoContexteException e) {
          logger.warn(e.getMessage(), e);
          TriggerUtils.manageBenefWarning(benef, e.getTriggeredBeneficiaryAnomaly(), true);
        } catch (BeneficiaryToIgnoreException e) {
          logger.info("Benef ignored {}-{}", benef.getAmc(), benef.getNumeroPersonne());
          toIgnore.add(benef);
        }
      }
    } else if (contrat == null) {
      errorContract = true;
    }

    // Return a boolean saying if there was at least one error within the
    // contract
    return errorContract;
  }

  private void mapOpenRights(
      boolean createTriggeredBenefWhenError,
      ContratAIV6 contrat,
      boolean notRenewal,
      TriggeredBeneficiary benef,
      List<DroitAssure> droits,
      boolean notBo)
      throws BobbNotFoundException, PilotageBoContexteException, BeneficiaryToIgnoreException {
    if (!CollectionUtils.isEmpty(droits)) {
      List<WaitingExtendedOffreProduits> waitingExtendedOffreProduitsList =
          triggerMapper.getOffersAndThrowException(benef.getNewContract());

      // If there are no error yet and the beneficiary doesn't
      // have any parametrageCarteTPId...
      if (StringUtils.isBlank(benef.getParametrageCarteTPId())) {
        updateParametrageCarteTPIdOnTriggerBenef(contrat, notRenewal, benef, notBo);
      }

      // Dans le cadre de la Request par UI, le parametrage de carte TP est obligatoire
      if (!createTriggeredBenefWhenError && StringUtils.isBlank(benef.getParametrageCarteTPId())) {
        TriggeredBeneficiaryAnomaly triggeredBeneficiaryAnomaly =
            TriggeredBeneficiaryAnomaly.create(Anomaly.NO_CARD_RIGHT_PARAM);
        throw new TriggerException(triggeredBeneficiaryAnomaly.getDescription());
      }

      if (notRenewal) {
        checkPilotageBoContexte(benef, contrat, notBo);
      }

      WaitingExtendedOffreProduits waitingExtendedOffreProduits =
          waitingExtendedOffreProduitsList.stream()
              .filter(
                  waitingExtendedOffreProduits1 ->
                      waitingExtendedOffreProduits1.getWaitingParameterError() != null)
              .findFirst()
              .orElse(null);
      if (waitingExtendedOffreProduits
          != null) { // cas où il y a un paramètrage de carence en erreur
        TriggerUtils.manageBenefError(
            createTriggeredBenefWhenError,
            benef,
            waitingExtendedOffreProduits.getWaitingParameterError(),
            true);
      }
    }
  }

  private void mapOpenRightsRenewal(
      ContratAIV6 contrat,
      List<ParametrageCarteTP> parametrageCarteTPList,
      TriggeredBeneficiary benef,
      List<DroitAssure> droits)
      throws BobbNotFoundException, PilotageBoContexteException, BeneficiaryToIgnoreException {
    if (!CollectionUtils.isEmpty(droits)) {
      List<WaitingExtendedOffreProduits> waitingExtendedOffreProduitsList =
          triggerMapper.getOffersAndThrowException(benef.getNewContract());

      // If there are no error yet and the beneficiary doesn't
      // have any parametrageCarteTPId...
      if (StringUtils.isBlank(benef.getParametrageCarteTPId())) {
        updateParametrageCarteTPIdOnTriggerBenefForRenewal(contrat, parametrageCarteTPList, benef);
      }

      waitingExtendedOffreProduitsList.stream()
          .filter(
              waitingExtendedOffreProduits1 ->
                  waitingExtendedOffreProduits1.getWaitingParameterError() != null)
          .findFirst()
          .ifPresent(
              waitingExtendedOffreProduits ->
                  TriggerUtils.manageBenefError(
                      true, benef, waitingExtendedOffreProduits.getWaitingParameterError(), true));
    }
  }

  /**
   * Vérifie si dans le cadre d un declenchement en PilotageBO que le contexte tier payant et sa
   * periode sont bien presents. Throw une exception dans le cas problematique
   */
  private void checkPilotageBoContexte(
      TriggeredBeneficiary beneficiary, ContratAIV6 contrat, boolean notBo)
      throws PilotageBoContexteException {
    try {
      beneficiary.setParametrageCarteTPId(null);
      updateParametrageCarteTPIdOnTriggerBenef(contrat, true, beneficiary, notBo);
      ParametrageCarteTP param = paramCarteTPService.getParametrageCarteTP(beneficiary, true);

      if (param != null
          && ModeDeclenchementCarteTP.PilotageBO.equals(
              param.getParametrageRenouvellement().getModeDeclenchement())) {
        ContexteTPV6 contexte = contrat.getContexteTiersPayant();
        if (contexte == null || contexte.getPeriodesDroitsCarte() == null) {
          beneficiary.setParametrageCarteTPId(null);
          throw new PilotageBoContexteException(
              TriggeredBeneficiaryAnomaly.create(Anomaly.NO_CARD_RIGHT_PERIODS));
        }
      }
    } catch (TriggerException e) {
      logger.info(e.getMessage());
    }
  }

  private void updateParametrageCarteTPIdOnTriggerBenef(
      ContratAIV6 contract,
      boolean notManuel,
      TriggeredBeneficiary triggeredBeneficiary,
      boolean notBo) {
    String identifiantCollectivite = null;
    String groupePopulation = null;

    // If the contract has a contratCollectif...
    ContratCollectifV6 contratCollectif = contract.getContratCollectif();
    if (contratCollectif != null) {
      identifiantCollectivite = contratCollectif.getIdentifiantCollectivite();
      groupePopulation = contratCollectif.getGroupePopulation();
    }

    List<GarantieTechnique> gts =
        paramCarteTPService.extractGTs(triggeredBeneficiary.getNewContract().getDroitsGaranties());

    // Get best parameters
    ParametrageCarteTP bestParam =
        paramCarteTPService.getBestParametrage(
            identifiantCollectivite,
            groupePopulation,
            contract.getCritereSecondaireDetaille(),
            gts,
            new RequestParametrageCarteTP(contract.getIdDeclarant(), true, true, notManuel, notBo));

    // If there are parameters...
    if (bestParam != null) {
      triggeredBeneficiary.setParametrageCarteTPId(bestParam.getId());
    }
  }

  private void updateParametrageCarteTPIdOnTriggerBenefForRenewal(
      ContratAIV6 contract,
      List<ParametrageCarteTP> parametrageCarteTPList,
      TriggeredBeneficiary triggeredBeneficiary) {
    String identifiantCollectivite = null;
    String groupePopulation = null;

    // If the contract has a contratCollectif...
    ContratCollectifV6 contratCollectif = contract.getContratCollectif();
    if (contratCollectif != null) {
      identifiantCollectivite = contratCollectif.getIdentifiantCollectivite();
      groupePopulation = contratCollectif.getGroupePopulation();
    }

    List<GarantieTechnique> gts =
        paramCarteTPService.extractGTs(triggeredBeneficiary.getNewContract().getDroitsGaranties());

    // Get best parameters
    ParametrageCarteTP bestParam =
        paramCarteTPService.getBestParametrageRenouvellement(
            identifiantCollectivite,
            groupePopulation,
            contract.getCritereSecondaireDetaille(),
            gts,
            parametrageCarteTPList);

    // If there are parameters...
    if (bestParam != null) {
      triggeredBeneficiary.setParametrageCarteTPId(bestParam.getId());
    }
  }

  private ParametrageCarteTP getBestParametrageSouscripteur(
      ContratAIV6 contract, boolean notManuel, Assure assurePrincipal) {
    String identifiantCollectivite = null;
    String groupePopulation = null;

    // If the contract has a contratCollectif...
    ContratCollectifV6 contratCollectif = contract.getContratCollectif();
    if (contratCollectif != null) {
      identifiantCollectivite = contratCollectif.getIdentifiantCollectivite();
      groupePopulation = contratCollectif.getGroupePopulation();
    }

    List<GarantieTechnique> gts = paramCarteTPService.extractGTs(assurePrincipal.getDroits());
    // Get best parameters
    return paramCarteTPService.getBestParametrage(
        identifiantCollectivite,
        groupePopulation,
        contract.getCritereSecondaireDetaille(),
        gts,
        new RequestParametrageCarteTP(contract.getIdDeclarant(), true, true, notManuel, false));
  }

  private void getParametrageForBenefs(
      List<TriggeredBeneficiary> benefs, ContratAIV6 contrat, TriggerEmitter triggerEmitter) {
    for (TriggeredBeneficiary benef : benefs) {
      if (benef.getNewContract() != null) {
        List<WaitingExtendedOffreProduits> offersAndProducts =
            triggerMapper.getOffers(benef.getNewContract());

        if (CollectionUtils.isNotEmpty(offersAndProducts)) {
          ParametrageCarteTP parametrageCarteTP =
              paramCarteTPService.extractBestParametrage(contrat, triggerEmitter, benef);

          if (parametrageCarteTP != null) {
            benef.setParametrageCarteTPId(parametrageCarteTP.getId());
          }
        }
      }
    }
  }

  private String getDateValiditeDroit(ParametrageCarteTP param, String requestDate) {
    ParametrageRenouvellement paramRenouvellement = param.getParametrageRenouvellement();
    String dateValidite = null;
    // Mode Automatique
    if (paramRenouvellement.getModeDeclenchement().equals(ModeDeclenchementCarteTP.Automatique)) {
      if (paramRenouvellement
          .getDateRenouvellementCarteTP()
          .equals(DateRenouvellementCarteTP.DebutEcheance)) {
        // Si echéance le droit doit être valide à la date d'échéance de
        // l'année
        // courante
        String dateDebutPlusDelai =
            DateUtils.getStringDatePlusDays(
                requestDate,
                param.getParametrageRenouvellement().getDelaiDeclenchementCarteTP(),
                DateUtils.FORMATTER);
        dateValidite =
            String.format(
                "%s-%s-%s",
                Objects.requireNonNull(DateUtils.stringToDate(dateDebutPlusDelai)).getYear(),
                StringUtils.right(paramRenouvellement.getDebutEcheance(), 2),
                StringUtils.left(paramRenouvellement.getDebutEcheance(), 2));
      } else if (paramRenouvellement
          .getDateRenouvellementCarteTP()
          .equals(DateRenouvellementCarteTP.AnniversaireContrat)) {
        // Si anniversaire, on enlève le délai à la date de la request
        // ou à défaut du
        // jour
        dateValidite = getDateValiditeWhenAnniversaireContrat(requestDate, paramRenouvellement);
      }
    }
    // Mode Manuel - on récupère la date du paramétrage
    else if (paramRenouvellement.getModeDeclenchement().equals(ModeDeclenchementCarteTP.Manuel)) {
      String dateDebutDroitTP = paramRenouvellement.getDateDebutDroitTP();
      if (StringUtils.isNotBlank(dateDebutDroitTP)) {
        dateValidite = StringUtils.substring(dateDebutDroitTP, 0, 10);
      }
    }
    // Mode Pilotage Back Office Contrat
    else {
      dateValidite = requestDate;
      if (StringUtils.isBlank(dateValidite)) {
        dateValidite = new SimpleDateFormat(DateUtils.YYYY_MM_DD).format(new Date());
      }
    }
    return dateValidite;
  }

  private String getDateValiditeWhenAnniversaireContrat(
      String requestDate, ParametrageRenouvellement paramRenouvellement) {
    String dateValidite;
    dateValidite = requestDate;
    if (StringUtils.isBlank(dateValidite)) {
      dateValidite = new SimpleDateFormat(DateUtils.YYYY_MM_DD).format(new Date());
    }

    Date date;
    try {
      date = formatter.parse(dateValidite);
    } catch (ParseException e) {
      throw new TriggerException(
          String.format(
              LA_DATE_S_N_EST_PAS_UNE_DATE_AU_FORMAT_S, dateValidite, DateUtils.YYYY_MM_DD));
    }
    Integer delai = paramRenouvellement.getDelaiDeclenchementCarteTP();
    if (delai != null) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      calendar.add(Calendar.DATE, delai);
      date = calendar.getTime();
    }
    dateValidite = new SimpleDateFormat(DateUtils.YYYY_MM_DD).format(date);
    return dateValidite;
  }

  @ContinueSpan(log = "generateTriggerFromContracts")
  public String generateTriggerFromContracts(
      ContratAIV6 newContract,
      ContratAIV6 oldContract,
      boolean isEndpoint,
      String nomFichierOrigine,
      boolean sendToKafka) {
    boolean createTriggeredBenefWhenError = nomFichierOrigine == null;
    logger.debug("Génération du déclencheur");
    // If both contracts are null, do nothing
    if (newContract == null && oldContract == null) {
      logger.debug("Génération du déclencheur : Aucun contrat, aucun déclencheur à générer");
      return null;
    }

    // Extract parameters from contracts
    String idDeclarant = Objects.requireNonNullElse(newContract, oldContract).getIdDeclarant();
    String numeroContrat = Objects.requireNonNullElse(newContract, oldContract).getNumero();
    boolean generationNextYear =
        generateNextYear(Objects.requireNonNullElse(newContract, oldContract));

    logger.debug("ReGeneration annee suivante {}", generationNextYear);
    // Build request
    TriggerGenerationRequest request = new TriggerGenerationRequest();
    request.setDate(formatter.format(new Date()));
    request.setEmitter(TriggerEmitter.Event);
    request.setIdDeclarant(idDeclarant);
    request.setGenerationNextYear(generationNextYear);
    request.setIndividualContractNumber(numeroContrat);

    // Generate triggers from contract
    List<Trigger> triggers = new ArrayList<>();
    manageTriggerForContract(
        request,
        triggers,
        createTriggeredBenefWhenError,
        newContract,
        oldContract,
        isEndpoint,
        nomFichierOrigine,
        sendToKafka);

    // If we have at least one trigger...
    if (!CollectionUtils.isEmpty(triggers)) {
      logger.debug(
          "Génération du déclencheur : Déclencheur créé sous l'id {}", triggers.get(0).getId());
      return triggers.get(0).getId();
    }
    // If we don't have any trigger...
    logger.debug("Génération du déclencheur : Aucun déclencheur créé");
    return null;
  }

  private boolean generateNextYear(ContratAIV6 newContract) {
    Assure assurePrincipal = TriggerUtils.getAssureSouscripteur(newContract);
    if (assurePrincipal != null) {
      ParametrageCarteTP parametrageCarteTP =
          getBestParametrageSouscripteur(newContract, false, assurePrincipal);
      if (parametrageCarteTP != null) {
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        LocalDate startRenewal;
        long nbDays;
        if (DateUtils.isLeapYear(today.getYear()) && today.getMonthValue() < 2) {
          nbDays =
              parametrageCarteTP.getParametrageRenouvellement().getDelaiDeclenchementCarteTP() + 1L;
        } else {
          nbDays = parametrageCarteTP.getParametrageRenouvellement().getDelaiDeclenchementCarteTP();
        }
        if (nbDays > 0) {
          if (ModeDeclenchementCarteTP.Automatique.equals(
                  parametrageCarteTP.getParametrageRenouvellement().getModeDeclenchement())
              && DateRenouvellementCarteTP.AnniversaireContrat.equals(
                  parametrageCarteTP
                      .getParametrageRenouvellement()
                      .getDateRenouvellementCarteTP())) {
            startRenewal =
                LocalDate.of(
                    year,
                    Integer.parseInt(newContract.getDateSouscription().substring(5, 7)),
                    Integer.parseInt(newContract.getDateSouscription().substring(8, 10)));
            long nbDaysBirthday = ChronoUnit.DAYS.between(today, startRenewal);
            if (nbDaysBirthday < 0) {
              startRenewal = startRenewal.plusYears(1);
            }
          } else {
            year = today.getYear() + 1;
            startRenewal = LocalDate.of(year, 1, 1);
          }
          return ChronoUnit.DAYS.between(today, startRenewal) <= nbDays;
        }
      }
    }
    return false;
  }

  // Called from renouvellementdroitstp-job
  private void getContractsConcernedBySetting(
      TriggerGenerationRequest request,
      List<Trigger> triggers,
      String date,
      Date dateTraitement,
      List<ParametrageCarteTP> params) {
    logger.debug("Au moins un paramétrage de carte TP à traiter");
    ClientSession session = null;
    // transactionnal false sert pour les tests
    if (beyondPropertiesService
        .getBooleanProperty(InstanceProperties.TRANSACTIONNAL)
        .orElse(true)) {
      session = client.startSession();
    }

    // Generate unique id for this batch execution
    String batchExecutionId = UUID.randomUUID().toString();

    for (ParametrageCarteTP param : params) {
      if (logger.isInfoEnabled()) {
        logger.info(
            "Gestion du paramétrage de carte TP {} : {}",
            param.getId(),
            TriggerUtils.getLog(param));
      }
      String dateValiditeDroit = getDateValiditeDroit(param, date);
      Stream<ContratAIV6> contrats =
          servicePrestationService.getContratForParametrageStream(
              param, dateTraitement, dateValiditeDroit, batchExecutionId);
      List<ContratAIV6> contratAIV6s = new ArrayList<>(contrats.toList());
      if (CollectionUtils.isNotEmpty(contratAIV6s)) {
        // Déclencheur pour le paramétrage de carte TP
        buildTriggerForParametrageCarteTP(
            triggers, date, session, param, contratAIV6s, batchExecutionId, request.isRdo());
      }

      updateLastExecAndDesactivateManuel(param);
    }

    if (beyondPropertiesService
        .getBooleanProperty(InstanceProperties.TRANSACTIONNAL)
        .orElse(true)) {
      session.close();
    }
  }

  private void buildTriggerForParametrageCarteTP(
      List<Trigger> triggers,
      String date,
      ClientSession session,
      ParametrageCarteTP param,
      List<ContratAIV6> contrats,
      String batchExecutionId,
      boolean isRdo) {
    Trigger trigger = new Trigger();
    trigger.setAmc(param.getAmc());
    trigger.setOrigine(TriggerEmitter.Renewal);
    trigger.setDateEffet(date);
    trigger.setRdo(isRdo);

    // Ouvrir la transaction
    if (beyondPropertiesService.getBooleanProperty(InstanceProperties.TRANSACTIONNAL).orElse(true)
        && session != null) {
      session.startTransaction(
          TransactionOptions.builder().writeConcern(WriteConcern.MAJORITY).build());
    }

    List<ParametrageCarteTP> parametrageCarteTPList =
        paramCarteTPService.getByAmc(
            new RequestParametrageCarteTP(
                param.getAmc(),
                true,
                true,
                !ModeDeclenchementCarteTP.Manuel.equals(
                    param.getParametrageRenouvellement().getModeDeclenchement()),
                false));

    Trigger savedTrigger = null;
    boolean benefInserted = false;

    List<ObjectId> contractIdList = new ArrayList<>();
    ResultFromContratAiv result =
        processContratAIVList(
            param,
            contrats,
            batchExecutionId,
            trigger,
            contractIdList,
            benefInserted,
            savedTrigger,
            parametrageCarteTPList);
    logger.debug("Compteur de contrat traité {}", result.compteur());
    if (CollectionUtils.isNotEmpty(result.contractIdList())) {
      servicePrestationService.updateContractsExecutionId(
          result.contractIdList(), batchExecutionId);
    }

    if (result.benefInserted()) {
      updateTriggerWithTriggerBenefs(triggers, session, param, result.savedTrigger());
    }
    // Rollback de la transaction
    else {
      if (beyondPropertiesService.getBooleanProperty(InstanceProperties.TRANSACTIONNAL).orElse(true)
          && session != null) {
        session.abortTransaction();
      }
      logger.debug("Aucun trigger à sauvegarder pour le paramétrage {}", param.getId());
    }
  }

  private @NotNull ResultFromContratAiv processContratAIVList(
      ParametrageCarteTP param,
      List<ContratAIV6> contrats,
      String batchExecutionId,
      Trigger trigger,
      List<ObjectId> contractIdList,
      boolean benefInserted,
      Trigger savedTrigger,
      List<ParametrageCarteTP> parametrageCarteTPList) {
    int compteur = 0;
    for (ContratAIV6 contrat : contrats) {
      compteur++;

      // Déclencheurs bénéfs pour chaque assuré des contrats
      List<TriggeredBeneficiary> benefs;
      if (ModeDeclenchementCarteTP.Manuel.equals(
          param.getParametrageRenouvellement().getModeDeclenchement())) {
        benefs = extractBenefsFromContractForRenouvellementManuel(param, contrat, trigger);
      } else {
        benefs =
            extractBenefsFromContractForRenouvellement(
                param, contrat, trigger, parametrageCarteTPList);
      }
      boolean isContractError =
          mapProduitBenefsAndIgnoreForRenewal(benefs, contrat, parametrageCarteTPList);
      if (!isContractError && CollectionUtils.isNotEmpty(benefs)) {
        // Si nous ne sommes pas dans le cadre d'un paramétrage Automatique, on ignore
        // cette étape
        if (ModeDeclenchementCarteTP.Automatique.equals(
            param.getParametrageRenouvellement().getModeDeclenchement())) {
          contractIdList = manageContractUpdateForBatch(contractIdList, contrat, batchExecutionId);
        }

        logger.debug("contrat {}", contrat.getNumero());
        if (!benefInserted) {
          // Création du trigger en base
          // Ce trigger sera modifié au fur et a mesure de l'ajout de triggeredBenefs
          savedTrigger =
              triggerService.saveTriggerToMongo(
                  trigger, 0, param.getParametrageRenouvellement().getSeuilSecurite());
        }
        benefInserted = true;

        logger.debug("contrat {} : Sauvegarde en base du trigger et de ses benef", contrat.getId());
        // Sauvegarde en base des triggeredBenefs et mise à jour du trigger
        Trigger tempTrigger =
            triggerService.saveToMongoTriggerUIandRenouv(
                savedTrigger,
                benefs,
                param.getParametrageRenouvellement().getSeuilSecurite(),
                param.getParametrageRenouvellement().getDateDebutDroitTP(),
                false);
        savedTrigger = Objects.requireNonNullElse(tempTrigger, savedTrigger);
      }
    }
    return new ResultFromContratAiv(savedTrigger, benefInserted, contractIdList, compteur);
  }

  private record ResultFromContratAiv(
      Trigger savedTrigger, boolean benefInserted, List<ObjectId> contractIdList, int compteur) {}

  private List<ObjectId> manageContractUpdateForBatch(
      List<ObjectId> contractIdList, ContratAIV6 contrat, String batchExecutionId) {
    // Add contract id to list
    contractIdList.add(new ObjectId(contrat.getId()));

    // If we reached the threshold...
    if (contractIdList.size()
        >= beyondPropertiesService.getIntegerProperty(RENEWAL_THRESHOLD).orElse(20000)) {
      // Update all seen contracts with unique batch execution id and reset
      // contractIdList
      servicePrestationService.updateContractsExecutionId(contractIdList, batchExecutionId);
      return new ArrayList<>();
    }

    return contractIdList;
  }

  private void updateTriggerWithTriggerBenefs(
      List<Trigger> triggers,
      ClientSession session,
      ParametrageCarteTP param,
      Trigger savedTrigger) {
    long nbTriggerBenefs = triggerService.getNbTriggeredBeneficiaries(savedTrigger.getId());
    savedTrigger.setNbBenef((int) nbTriggerBenefs);
    triggerService.saveTriggerToMongo(
        savedTrigger,
        savedTrigger.getNbBenef(),
        param.getParametrageRenouvellement().getSeuilSecurite());
    // Commit de la transaction et envoi du message Kafka
    if (beyondPropertiesService
        .getBooleanProperty(InstanceProperties.TRANSACTIONNAL)
        .orElse(true)) {
      session.commitTransaction();
    }
    triggers.add(savedTrigger);
    logger.debug("Envoi du message kafka pour le trigger {}", savedTrigger.getId());
    producer.sendRenouvellement(savedTrigger.getId(), RECYCLAGE_FALSE);
  }

  @ContinueSpan(log = "generateTriggersRdoServicePrestation")
  public String generateTriggersRdoServicePrestation(
      ContratAIV6 newContract, ContratAIV6 oldContract, String nomFichierOrigine) {
    try {
      // we won't generate a trigger if there is no TP card parameters or bobbs
      // -> renouvellement manuel post rdo
      return generateTriggerFromContracts(newContract, oldContract, false, nomFichierOrigine, true);
    } catch (TriggerException e) {
      logger.error(
          String.format("Trigger exception while generating triggers : %s", e.getMessage()), e);
    } catch (Exception e) {
      logger.error(String.format("Unexpected error in contract consumer : %s", e.getMessage()), e);
    }
    return null;
  }

  @ContinueSpan(log = "generateTriggersConsumer")
  public String generateTriggersConsumer(
      ContratAIV6 newContract, ContratAIV6 oldContract, boolean sendToKafka) {
    try {
      // In any case, generate a trigger
      return generateTriggerFromContracts(newContract, oldContract, false, null, sendToKafka);
    } catch (TriggerException e) {
      logger.error(
          String.format("Trigger exception while generating triggers : %s", e.getMessage()), e);
    } catch (Exception e) {
      logger.error(String.format("Unexpected error in contract consumer : %s", e.getMessage()), e);
    }
    return null;
  }

  private void updateLastExecAndDesactivateManuel(ParametrageCarteTP parametrageCarteTP) {
    parametrageCarteTP
        .getParametrageRenouvellement()
        .setDerniereExecution(LocalDateTime.now(ZoneOffset.UTC));
    if (ModeDeclenchementCarteTP.Manuel.equals(
        parametrageCarteTP.getParametrageRenouvellement().getModeDeclenchement())) {
      parametrageCarteTP.setStatut(ParametrageCarteTPStatut.Inactif);
    }
    paramCarteTPService.update(parametrageCarteTP);
  }

  public List<TriggeredBeneficiary> extractBenefsFromContractForRenouvellement(
      final ParametrageCarteTP param,
      final ContratAIV6 contrat,
      Trigger trigger,
      List<ParametrageCarteTP> parametrageCarteTPList) {
    List<TriggeredBeneficiary> benefs = new ArrayList<>();
    ParametrageTrigger parametrageTrigger = new ParametrageTrigger();
    parametrageTrigger.setParametrageCarteTP(param);
    parametrageTrigger.setOrigine(TriggerEmitter.Renewal);
    parametrageTrigger.setDateEffet(trigger.getDateEffet());
    parametrageTrigger.setRdo(trigger.isRdo());
    parametrageTrigger.setEventReprise(trigger.isEventReprise());
    parametrageTrigger.setDateSouscription(contrat.getDateSouscription());
    Periode periodeFromParametrage = TriggerUtils.getPeriodeFromParametrage(parametrageTrigger);
    Assure assureSouscripteur =
        TriggerUtils.getAssureForRenouvellement(contrat, periodeFromParametrage);
    // le souscripteur doit avoir le même id de paramétrage du paramétrage à
    // traiter.
    if (assureSouscripteur != null) {
      TriggeredBeneficiary trgBenef = new TriggeredBeneficiary();

      trgBenef.setServicePrestationId(contrat.getId());
      ServicePrestationTriggerBenef contractBenef =
          TriggerUtils.mapContrat(assureSouscripteur, contrat);
      trgBenef.setNewContract(contractBenef);
      ParametrageCarteTP parametrageCarteTPSouscripteur =
          paramCarteTPService.extractBestParametrageForRenouvellement(
              contrat, trgBenef, parametrageCarteTPList);
      if (parametrageCarteTPSouscripteur != null
          && parametrageCarteTPSouscripteur.getId().equals(param.getId())) {
        List<Assure> listeAssure =
            getAssureList(contrat, assureSouscripteur, periodeFromParametrage);

        for (final Assure assure : listeAssure) {
          trgBenef = new TriggeredBeneficiary();

          trgBenef.setServicePrestationId(contrat.getId());

          TriggerUtils.extractInfoAssure(contrat, assure, assureSouscripteur, trgBenef);
          TriggerUtils.addStatus(trgBenef, TriggeredBeneficiaryStatusEnum.ToProcess, null, true);

          contractBenef = TriggerUtils.mapContrat(assure, contrat);

          trgBenef.setNewContract(contractBenef);
          if (assure.equals(assureSouscripteur)) {
            trgBenef.setParametrageCarteTPId(param.getId());
          } else {
            ParametrageCarteTP parametrageCarteTP =
                paramCarteTPService.extractBestParametrageForRenouvellement(
                    contrat, trgBenef, parametrageCarteTPList);
            if (parametrageCarteTP != null) {
              trgBenef.setParametrageCarteTPId(parametrageCarteTP.getId());
            } else {
              logger.error(
                  "Parametrage CarteTP null pour le benef {} / {}",
                  assure.getIdentite().getNumeroPersonne(),
                  contrat.getNumero());
              trgBenef = null;
            }
          }
          if (trgBenef != null) {
            benefs.add(trgBenef);
          }
        }
      }
    }
    return benefs;
  }

  public List<TriggeredBeneficiary> extractBenefsFromContractForRenouvellementManuel(
      final ParametrageCarteTP param, final ContratAIV6 contrat, Trigger trigger) {
    List<TriggeredBeneficiary> benefs = new ArrayList<>();
    ParametrageTrigger parametrageTrigger = new ParametrageTrigger();
    parametrageTrigger.setParametrageCarteTP(param);
    parametrageTrigger.setOrigine(TriggerEmitter.Renewal);
    parametrageTrigger.setDateEffet(trigger.getDateEffet());
    parametrageTrigger.setRdo(trigger.isRdo());
    parametrageTrigger.setEventReprise(trigger.isEventReprise());
    parametrageTrigger.setDateSouscription(contrat.getDateSouscription());
    Periode periodeFromParametrage = TriggerUtils.getPeriodeFromParametrage(parametrageTrigger);
    Assure assureSouscripteur =
        periodeFromParametrage != null
            ? TriggerUtils.getAssureForRenouvellement(contrat, periodeFromParametrage)
            : null;
    // le souscripteur doit avoir le même id de paramétrage du paramétrage à
    // traiter.
    if (assureSouscripteur != null) {
      TriggeredBeneficiary trgBenef;
      ServicePrestationTriggerBenef contractBenef;
      List<Assure> listeAssure = getAssureList(contrat, assureSouscripteur, periodeFromParametrage);
      for (final Assure assure : listeAssure) {
        trgBenef = new TriggeredBeneficiary();
        trgBenef.setServicePrestationId(contrat.getId());
        TriggerUtils.extractInfoAssure(contrat, assure, assureSouscripteur, trgBenef);
        TriggerUtils.addStatus(trgBenef, TriggeredBeneficiaryStatusEnum.ToProcess, null, true);
        contractBenef = TriggerUtils.mapContrat(assure, contrat);
        trgBenef.setNewContract(contractBenef);
        trgBenef.setParametrageCarteTPId(param.getId());
        benefs.add(trgBenef);
      }
    }
    return benefs;
  }

  /** */
  List<Assure> getAssureList(
      ContratAIV6 contrat, Assure assureSouscripteur, Periode periodeFromParametrage) {
    List<Assure> listeAssure = new ArrayList<>();
    listeAssure.add(assureSouscripteur);

    contrat.getAssures().stream()
        .filter(
            assure ->
                assure.getRangAdministratif() != null
                    && !assureSouscripteur
                        .getIdentite()
                        .getNumeroPersonne()
                        .equals(assure.getIdentite().getNumeroPersonne()))
        .sorted(Comparator.comparing(Assure::getRangAdministratif))
        .forEach(
            assure -> {
              for (DroitAssure droitAssure : assure.getDroits()) {
                if (DateUtils.isOverlapping(
                    droitAssure.getPeriode().getDebut(),
                    droitAssure.getPeriode().getFin(),
                    periodeFromParametrage.getDebut(),
                    periodeFromParametrage.getFin())) {
                  listeAssure.add(assure);
                  break;
                }
              }
            });
    return listeAssure;
  }
}
