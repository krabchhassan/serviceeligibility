package com.cegedim.next.serviceeligibility.core.services.cartedemat.consolidation;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.*;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.dao.HistoriqueExecutionsDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecutions620;
import com.cegedim.next.serviceeligibility.core.model.job.DataForJob620;
import com.cegedim.next.serviceeligibility.core.model.job.DataForReprise620;
import com.cegedim.next.serviceeligibility.core.services.CartesService;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarantService;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarationService;
import com.cegedim.next.serviceeligibility.core.services.cartedemat.invalidation.InvalidationCarteService;
import com.cegedim.next.serviceeligibility.core.services.common.batch.ARLService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.s3.S3Service;
import com.cegedim.next.serviceeligibility.core.services.trace.TraceExtractionConsoService;
import com.cegedim.next.serviceeligibility.core.task.GenerationCartesByDeclarantTask;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionTechnique;
import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.client.MongoClient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProcessorConsolidationService {
  private final DeclarationConsolideService declarationConsolideService;

  private final DeclarationService declarationService;
  private final DeclarantService declarantService;
  private final HistoriqueExecutionsDao historiqueExecutionsDao;
  private final ARLService arlService;
  private final InvalidationCarteService invalidationCarteService;

  private final EventService eventService;

  private final S3Service s3Service;

  private final CartesService cartesService;

  private final TraceExtractionConsoService traceExtractionConsoService;

  private final BeyondPropertiesService beyondPropertiesService;

  private final Integer taskBatchSize;

  private final Integer contratBatchSize;

  private final MongoClient client;

  public ProcessorConsolidationService(
      @Autowired DeclarantService declarantService,
      @Autowired DeclarationService declarationService,
      @Autowired HistoriqueExecutionsDao historiqueExecutionsDao,
      @Autowired(required = false) EventService eventService,
      @Autowired DeclarationConsolideService declarationConsolideService,
      @Autowired ARLService arlService,
      @Autowired InvalidationCarteService invalidationCarteService,
      @Autowired S3Service s3Service,
      @Autowired CartesService cartesService,
      @Autowired TraceExtractionConsoService traceExtractionConsoService,
      @Autowired BeyondPropertiesService beyondPropertiesService,
      MongoClient client) {
    this.declarationConsolideService = declarationConsolideService;
    this.declarantService = declarantService;
    this.declarationService = declarationService;
    this.historiqueExecutionsDao = historiqueExecutionsDao;
    this.eventService = eventService;
    this.arlService = arlService;
    this.invalidationCarteService = invalidationCarteService;
    this.s3Service = s3Service;
    this.cartesService = cartesService;
    this.traceExtractionConsoService = traceExtractionConsoService;
    this.beyondPropertiesService = beyondPropertiesService;
    this.client = client;
    this.taskBatchSize = beyondPropertiesService.getIntegerProperty(TASK_BATCH_SIZE).orElse(5);
    this.contratBatchSize =
        beyondPropertiesService.getIntegerProperty(CONTRAT_BATCH_SIZE).orElse(200);
  }

  /**
   * Par {@link Declarant} itere avec un stream sur les {@link Declaration} correspondates afin de
   * les regrouper par contrat-personne. Le regroupement se fait au fil de l eau, les declarations
   * sont ordonnees par contrat-personne ce qui permet de les regrouper les unes apres les autres
   * jusqu a trouver une cle contrat-personne differente.
   */
  public void processConsolidation(
      DataForJob620 dataForJob620, HistoriqueExecutions620 newHistoriqueExecutions620)
      throws Exception {
    newHistoriqueExecutions620.setIdentifiant(dataForJob620.getIdentifiant());
    Date dateSynchro = null;
    if (dataForJob620.getDateSynchroStr() == null && dataForJob620.getLastExecution() != null) {
      dateSynchro = dataForJob620.getLastExecution().getDateExecution();
    }

    List<Declarant> declarants = getDeclarants(dataForJob620);

    checkS3File(declarants, dataForJob620.getClientType());
    if (CollectionUtils.isNotEmpty(declarants)) {

      ExecutorService pool = Executors.newFixedThreadPool(declarants.size());
      List<GenerationCartesByDeclarantTask> consolidationDeclarantProcessors = new ArrayList<>();
      for (Declarant declarant : declarants) {
        HistoriqueExecutions620 historiqueExecutions620AMC =
            historiqueExecutionsDao.getLastExecutionForReprise620(
                Constants.NUMERO_BATCH_620, declarant.get_id(), HistoriqueExecutions620.class);
        if (historiqueExecutions620AMC != null
            && historiqueExecutions620AMC.getDateFinCartes() == null) {
          log.info(
              "declarant {}, reprise depuis le contrat {} / adherent {} ",
              declarant.get_id(),
              historiqueExecutions620AMC.getDernierNumeroContrat(),
              historiqueExecutions620AMC.getDernierNumeroAdherent());
          DataForReprise620 dataForReprise620 = new DataForReprise620();
          dataForReprise620.setFromContrat(historiqueExecutions620AMC.getDernierNumeroContrat());
          dataForReprise620.setFromAdherent(historiqueExecutions620AMC.getDernierNumeroAdherent());
          dataForReprise620.setIdDeclarant(declarant.get_id());
          dataForJob620.setDataForReprise620(dataForReprise620);
        } else {
          // initialisation dans GenerationCartesByDeclarantTask.
          historiqueExecutions620AMC = null;
        }

        consolidationDeclarantProcessors.add(
            new GenerationCartesByDeclarantTask(
                dataForJob620,
                declarant,
                dateSynchro,
                historiqueExecutionsDao,
                declarationConsolideService,
                declarationService,
                arlService,
                invalidationCarteService,
                taskBatchSize,
                contratBatchSize,
                eventService,
                cartesService,
                traceExtractionConsoService,
                historiqueExecutions620AMC,
                client));
      }
      List<Future<HistoriqueExecutions620>> results =
          pool.invokeAll(consolidationDeclarantProcessors);
      results.forEach(
          historiqueExecutionsFuture -> {
            try {
              newHistoriqueExecutions620.setIdDerniereDeclarationTraitee(
                  historiqueExecutionsFuture.get().getIdDerniereDeclarationTraitee());
              newHistoriqueExecutions620.incNbDeclarationLue(
                  historiqueExecutionsFuture.get().getNbDeclarationLue());
              newHistoriqueExecutions620.incNbConsolidationCree(
                  historiqueExecutionsFuture.get().getNbConsolidationCree());
              newHistoriqueExecutions620.incNbDeclarationErreur(
                  historiqueExecutionsFuture.get().getNbDeclarationErreur());
              newHistoriqueExecutions620.setDateFinConsolidations(DateUtils.nowUTC());
              newHistoriqueExecutions620.incNbCartesInvalidees(
                  historiqueExecutionsFuture.get().getNbCartesInvalidees());
              newHistoriqueExecutions620.incNbDeclarationIgnoree(
                  historiqueExecutionsFuture.get().getNbDeclarationIgnoree());
              newHistoriqueExecutions620.incNbCartesOk(
                  historiqueExecutionsFuture.get().getNbCartesOk());
              newHistoriqueExecutions620.incNbCartesKo(
                  historiqueExecutionsFuture.get().getNbCartesKo());
              newHistoriqueExecutions620.incNbCartesPapierEdit(
                  historiqueExecutionsFuture.get().getNbCartesPapierEdit());
              newHistoriqueExecutions620.setNbDeclarationTraitee(
                  newHistoriqueExecutions620.getNbDeclarationTraitee());
              newHistoriqueExecutions620.setDateFinCartes(DateUtils.nowUTC());
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
              log.error(e.getMessage(), e);
              throw new ExceptionTechnique(e.getMessage(), e);
            } catch (ExecutionException e) {
              log.error(e.getMessage(), e);
              throw new ExceptionTechnique(e.getMessage(), e);
            }
          });
    }
  }

  private List<Declarant> getDeclarants(DataForJob620 dataForJob620) {
    if (dataForJob620.getDataForReprise620() == null) {
      return declarantService.getDeclarantsByCodeService(
          List.of(Constants.CARTE_DEMATERIALISEE, Constants.CARTE_TP),
          dataForJob620.getCouloirClient());
    }

    Declarant declarantReprise =
        declarantService.getDeclarantByCodeService(
            dataForJob620.getDataForReprise620().getIdDeclarant(),
            List.of(Constants.CARTE_DEMATERIALISEE, Constants.CARTE_TP));
    log.info(
        "Reprise sur le déclarant {} du contrat {} au contrat {}",
        dataForJob620.getDataForReprise620().getIdDeclarant(),
        dataForJob620.getDataForReprise620().getFromContrat(),
        dataForJob620.getDataForReprise620().getToContrat());
    return List.of(declarantReprise);
  }

  public void checkS3File(List<Declarant> declarants, String clientType) throws Exception {
    if (Constants.CLIENT_TYPE_INSURER.equals(clientType)) {
      boolean isDeclarantWithServiceCarteTP =
          declarants.stream()
              .flatMap(declarant -> declarant.getPilotages().stream())
              .anyMatch(
                  pilotage ->
                      Constants.CARTE_TP.equals(pilotage.getCodeService())
                          && pilotage.getServiceOuvert());
      if (isDeclarantWithServiceCarteTP) {
        tryToReadS3File();
      }
    }
  }

  private void tryToReadS3File() throws Exception {
    JsonNode insurerSettings;
    insurerSettings =
        s3Service.readS3File(
            beyondPropertiesService.getPropertyOrThrowError(INSURER_SETTINGS_FILE_PATH));
    if (insurerSettings == null || insurerSettings.isEmpty()) {
      throw new Exception(
          "Problème lors de la récupération du fichier S3 "
              + beyondPropertiesService.getPropertyOrThrowError(INSURER_SETTINGS_FILE_PATH));
    } else {
      JsonNode defaultSetting = null;
      for (JsonNode jsonNode : insurerSettings) {
        if (Constants.DEFAUT.equals(jsonNode.get(Constants.SCOPE).asText())
            && jsonNode.has(Constants.COMMON)
            && Constants.CODE_CARTES_PAPIER.equals(
                jsonNode.get(Constants.COMMON).get(Constants.DOCUMENT_TYPE).asText())) {
          defaultSetting = jsonNode;
          break;
        }
      }
      if (defaultSetting == null) {
        throw new Exception(
            "Le fichier "
                + beyondPropertiesService.getPropertyOrThrowError(INSURER_SETTINGS_FILE_PATH)
                + " ne respecte pas le format attendu.");
      } else if (!defaultSetting.has(Constants.EDITING)) {
        throw new Exception(
            "Le fichier "
                + beyondPropertiesService.getPropertyOrThrowError(INSURER_SETTINGS_FILE_PATH)
                + " ne respecte pas le format attendu : clé 'editing' manquante.");
      }
    }
  }
}
