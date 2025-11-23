package com.cegedim.next.serviceeligibility.core.task;

import com.cegedim.beyond.schemas.Event;
import com.cegedim.next.serviceeligibility.core.dao.HistoriqueExecutionsDao;
import com.cegedim.next.serviceeligibility.core.job.batch.BulkActions;
import com.cegedim.next.serviceeligibility.core.job.batch.Rejection;
import com.cegedim.next.serviceeligibility.core.job.batch.TraceConsolidation;
import com.cegedim.next.serviceeligibility.core.job.batch.TraceExtractionConso;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.DeclarationConsolide;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecutions620;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.model.entity.card.cartepapiereditique.CartePapierEditique;
import com.cegedim.next.serviceeligibility.core.model.job.DataForJob620;
import com.cegedim.next.serviceeligibility.core.services.CartesService;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarationService;
import com.cegedim.next.serviceeligibility.core.services.cartedemat.consolidation.DeclarationConsolideService;
import com.cegedim.next.serviceeligibility.core.services.cartedemat.invalidation.InvalidationCarteService;
import com.cegedim.next.serviceeligibility.core.services.common.batch.ARLService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.trace.TraceExtractionConsoService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionTechnique;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Tache qui génére les cartes demat / carte papier par déclarant A ce niveau là, ce n'est que de la
 * création de sous tache par bloc de contrat + mise à jour historique execution par declarant
 */
@Slf4j
public class GenerationCartesByDeclarantTask implements Callable<HistoriqueExecutions620> {

  private final DataForJob620 dataForJob620;

  private final Declarant declarant;

  private final Date dateSynchro;

  private final HistoriqueExecutionsDao historiqueExecutionsDao;

  private final DeclarationConsolideService declarationConsolideService;

  private final DeclarationService declarationService;

  private final ARLService arlService;

  private final InvalidationCarteService invalidationCarteService;

  private final EventService eventService;

  private final Integer taskBatchSize;

  private final Integer contratBatchSize;

  private final CartesService cartesService;

  private final TraceExtractionConsoService traceExtractionConsoService;

  private final HistoriqueExecutions620 historiqueExecutions620AMC;

  private final MongoClient client;

  public GenerationCartesByDeclarantTask(
      DataForJob620 dataForJob620,
      Declarant declarant,
      Date dateSynchro,
      HistoriqueExecutionsDao historiqueExecutionsDao,
      DeclarationConsolideService declarationConsolideService,
      DeclarationService declarationService,
      ARLService arlService,
      InvalidationCarteService invalidationCarteService,
      Integer taskBatchSize,
      Integer contratBatchSize,
      EventService eventService,
      CartesService cartesService,
      TraceExtractionConsoService traceExtractionConsoService,
      HistoriqueExecutions620 historiqueExecutions620AMC,
      MongoClient client) {
    this.dataForJob620 = dataForJob620;
    this.declarant = declarant;
    this.dateSynchro = dateSynchro;
    this.historiqueExecutionsDao = historiqueExecutionsDao;
    this.declarationConsolideService = declarationConsolideService;
    this.declarationService = declarationService;
    this.arlService = arlService;
    this.invalidationCarteService = invalidationCarteService;
    this.taskBatchSize = taskBatchSize;
    this.contratBatchSize = contratBatchSize;
    this.eventService = eventService;
    this.cartesService = cartesService;
    this.traceExtractionConsoService = traceExtractionConsoService;
    this.historiqueExecutions620AMC = historiqueExecutions620AMC;
    this.client = client;
  }

  @Override
  public HistoriqueExecutions620 call() throws Exception {
    long startTime = System.currentTimeMillis();
    List<Rejection> rejections = new ArrayList<>();
    List<TraceConsolidation> rejectedTraces = new ArrayList<>();
    List<TraceExtractionConso> rejectedTracesExtraction = new ArrayList<>();
    HistoriqueExecutions620 historiqueAMCInProgress;
    if (historiqueExecutions620AMC == null) {
      historiqueAMCInProgress = new HistoriqueExecutions620();
      historiqueAMCInProgress.setBatch(Constants.NUMERO_BATCH_620);
      historiqueAMCInProgress.setDateExecution(DateUtils.nowUTC());
      historiqueAMCInProgress.setNbDeclarationLue(0);
      historiqueAMCInProgress.setNbCartesOk(0);
      historiqueAMCInProgress.setIdentifiant(dataForJob620.getIdentifiant());
      historiqueAMCInProgress.setIdDeclarant(declarant.get_id());
    } else {
      historiqueAMCInProgress = historiqueExecutions620AMC;
    }

    // On recupere n importe quelle date de synchro car ce sont toujours les memes
    Date dateSynchroToUse = declarant.getPilotages().getFirst().getDateSynchronisation();
    if (dateSynchroToUse == null) {
      dateSynchroToUse = dateSynchro;
    }

    int compteurContrat = 0;
    Map<String, List<Declaration>> mapDeclarations = new HashMap<>();
    List<DeclarationsContratTask> declarationsContratTasks = new ArrayList<>();
    String previousNumeroContrat = "";
    String previousNumeroAdherent = "";
    Stream<Declaration> sortedDeclarations =
        getDeclarations(declarant, dataForJob620, dateSynchroToUse);
    List<Declaration> groupedDeclarations = new ArrayList<>();
    BulkActions bulkActions = null;
    Iterator<Declaration> declarationIterator = sortedDeclarations.iterator();
    while (declarationIterator.hasNext()) {
      Declaration declaration = declarationIterator.next();
      log.debug(
          "declarant {}, contrat {}",
          declaration.getIdDeclarant(),
          declaration.getContrat().getNumero());
      String currentNumeroContrat = declaration.getContrat().getNumero();
      if (compteurContrat == 0) {
        bulkActions = new BulkActions();
        bulkActions.setFirstIdDeclaration(declaration.get_id());
        bulkActions.setFirstNumContrat(currentNumeroContrat);
        compteurContrat++;
      }

      // changement de contrat
      if (!previousNumeroContrat.equals(currentNumeroContrat) && !groupedDeclarations.isEmpty()) {
        mapDeclarations.put(
            declarant.get_id() + "-" + previousNumeroContrat, new ArrayList<>(groupedDeclarations));
        bulkActions.setLastIdDeclaration(groupedDeclarations.getLast().get_id());
        bulkActions.setLastNumContrat(previousNumeroContrat);
        bulkActions.setLastNumAdherent(previousNumeroAdherent);
        groupedDeclarations.clear();
        compteurContrat++;
        if (compteurContrat >= contratBatchSize) {
          // crée une sous tache de création pour X contrat
          declarationsContratTasks.add(createCardsOnContracts(bulkActions, mapDeclarations));
          compteurContrat = 0;
          groupedDeclarations = new ArrayList<>();
          mapDeclarations = new HashMap<>();
          bulkActions = new BulkActions();
          bulkActions.setFirstIdDeclaration(declaration.get_id());
          bulkActions.setFirstNumContrat(currentNumeroContrat);

          if (declarationsContratTasks.size() >= taskBatchSize) {
            updateHistoriqueExecAndClearTasks(
                declarationsContratTasks, historiqueAMCInProgress, rejections, false);
          }
        }
      }
      groupedDeclarations.add(declaration);
      historiqueAMCInProgress.setIdDerniereDeclarationTraitee(declaration.get_id());
      previousNumeroContrat = currentNumeroContrat;
      previousNumeroAdherent = declaration.getContrat().getNumeroAdherent();
    }

    if (!groupedDeclarations.isEmpty()) {
      mapDeclarations.put(declarant.get_id() + "-" + previousNumeroContrat, groupedDeclarations);
      bulkActions.setLastIdDeclaration(groupedDeclarations.getLast().get_id());
      bulkActions.setLastNumContrat(previousNumeroContrat);
      bulkActions.setLastNumAdherent(previousNumeroAdherent);
      declarationsContratTasks.add(createCardsOnContracts(bulkActions, mapDeclarations));
      historiqueAMCInProgress.setIdDerniereDeclarationTraitee(
          groupedDeclarations.getLast().get_id());
    }
    updateHistoriqueExecAndClearTasks(
        declarationsContratTasks, historiqueAMCInProgress, rejections, true);

    if (CollectionUtils.isNotEmpty(rejections)) {
      arlService.buildARL(
          rejections,
          declarant,
          historiqueAMCInProgress.getDateExecution(),
          rejectedTraces,
          rejectedTracesExtraction,
          null,
          null);
    }
    // Save pour les amc qui n ont pas de declarations
    historiqueAMCInProgress.setDateFinCartes(DateUtils.nowUTC());
    historiqueAMCInProgress.setDateFinConsolidations(DateUtils.nowUTC());
    historiqueExecutionsDao.save(historiqueAMCInProgress);
    historiqueAMCInProgress.log();
    long endTime = System.currentTimeMillis();
    log.info(
        "Consolidation carte, Fin Declarant  {}, temps d execution {}",
        declarant.get_id(),
        (endTime - startTime));

    if (declarant.getPilotages().stream()
        .anyMatch(
            pilotage ->
                Constants.CARTE_DEMATERIALISEE.equals(pilotage.getCodeService())
                    && pilotage.getServiceOuvert())) {
      handleEventNbCartesActives(dataForJob620, declarant);
    }

    return historiqueAMCInProgress;
  }

  private DeclarationsContratTask createCardsOnContracts(
      BulkActions bulkActions, Map<String, List<Declaration>> mapDeclarations) {
    DeclarationsContratTask declarationsContratTask =
        new DeclarationsContratTask(
            dataForJob620,
            declarant,
            declarationConsolideService,
            invalidationCarteService,
            mapDeclarations,
            bulkActions);
    ForkJoinPool.commonPool().execute(declarationsContratTask);
    return declarationsContratTask;
  }

  /**
   * Met à jour l'historique execution de l'AMC et supprime les taches courantes en mémoire (pour
   * faire bosser le GC)
   */
  private void updateHistoriqueExecAndClearTasks(
      List<DeclarationsContratTask> declarationsContratTasks,
      HistoriqueExecutions620 newHistoriqueExecutions620AMC,
      List<Rejection> rejections,
      boolean lastTask) {
    declarationsContratTasks.forEach(
        task -> {
          ClientSession session = client.startSession();
          session.startTransaction(
              TransactionOptions.builder().writeConcern(WriteConcern.MAJORITY).build());
          try {
            BulkActions bulk = task.get();
            processBulks(session, bulk, newHistoriqueExecutions620AMC, lastTask);
            session.commitTransaction();
            session.close();
            rejections.addAll(bulk.getRejections());
          } catch (InterruptedException e) {
            session.abortTransaction();
            session.close();
            Thread.currentThread().interrupt();
            log.error(e.getMessage(), e);
            throw new ExceptionTechnique(e.getMessage(), e);
          } catch (ExecutionException e) {
            session.abortTransaction();
            session.close();
            log.error(e.getMessage(), e);
            throw new ExceptionTechnique(e.getMessage(), e);
          }
        });
    declarationsContratTasks.clear();
  }

  /** Méthode qui s'occupe de sauvegarder toutes les collections et envoie les events liés */
  private void processBulks(
      ClientSession session,
      BulkActions bulkActions,
      HistoriqueExecutions620 newHistoriqueExecutions620AMC,
      boolean lastTask) {
    long debut = System.currentTimeMillis();
    log.info(
        "processBulks debut declarant {} du contrat {} au contrat {}",
        declarant.get_id(),
        bulkActions.getFirstNumContrat(),
        bulkActions.getLastNumContrat());
    // Partie sauvegarde, suppression et modification des declarations consolidees

    Collection<DeclarationConsolide> saved =
        declarationConsolideService.bulkSaveUpdateDelete(
            bulkActions, declarant.getCodePartenaire(), session);

    // Send events
    for (Event event : bulkActions.getEvents()) {
      eventService.sendObservabilityEvent(event);
    }

    // Liste les declarations consolidees par amc_contratPeriodeDebut
    Map<String, List<DeclarationConsolide>> consoByContratAmc =
        Stream.concat(saved.stream(), bulkActions.getToUpdate().stream())
            .collect(
                Collectors.groupingBy(conso -> conso.getAMC_contrat() + conso.getPeriodeDebut()));

    // Complete les idDeclarationConso dans carte demat après qu'elles soient
    // sauvegarder ou update pour avoir l'id mongo
    for (CarteDemat demat : bulkActions.getDemats()) {
      String key = demat.getAMC_contrat() + demat.getPeriodeDebut();
      List<DeclarationConsolide> consos =
          consoByContratAmc.getOrDefault(key, Collections.emptyList());
      consos.forEach(conso -> demat.getIdDeclarationsConsolides().add(conso.get_id()));
    }

    // Cartes demat
    int nombreCarteDematInsere = 0;
    if (CollectionUtils.isNotEmpty(bulkActions.getDemats())) {
      nombreCarteDematInsere = cartesService.saveCartesDemat(bulkActions.getDemats(), session);
    }

    // Prepare les traceExtractions des carteDemat sauvegarde en cartePapier. Apres
    // la sauvegarde des declaConsos pour avoir les id mongo
    List<TraceExtractionConso> traceExtractions = new ArrayList<>();
    for (Pair<CarteDemat, String> traceExtractionConso : bulkActions.getTraceExtractionConsos()) {
      CarteDemat carteDemat = traceExtractionConso.getKey();
      String key = carteDemat.getAMC_contrat() + carteDemat.getPeriodeDebut();
      List<DeclarationConsolide> consos =
          consoByContratAmc.getOrDefault(key, Collections.emptyList());
      List<TraceExtractionConso> traceContrat =
          traceExtractionConsoService.generateExtractionTrace(
              carteDemat, traceExtractionConso.getValue(), consos);
      traceExtractions.addAll(traceContrat);
    }

    // met à jour les tracesExtractionConso dans internal pour mise à jour par la
    // suite de l'omu extractcard (dernier step)
    Map<String, List<CarteDemat>> cartesByContratAmc =
        bulkActions.getDemats().stream()
            .collect(
                Collectors.groupingBy(carte -> carte.getAMC_contrat() + carte.getPeriodeDebut()));

    for (CartePapierEditique cartesPapier : bulkActions.getPapiers()) {
      String key =
          cartesPapier.getCartePapier().getNumeroAMC()
              + "-"
              + cartesPapier.getCartePapier().getContrat().getNumero()
              + cartesPapier.getCartePapier().getPeriodeDebut();
      List<CarteDemat> cartes = cartesByContratAmc.getOrDefault(key, Collections.emptyList());
      if (CollectionUtils.isNotEmpty(cartes)) {
        cartesPapier
            .getInternal()
            .setTraceExtractionConso(
                traceExtractionConsoService.generateExtractionTrace(cartes.getFirst(), null, null));
      }
    }

    // Cartes papier
    Collection<CartePapierEditique> cartesPapiers =
        cartesService.saveCartesPapiers(bulkActions.getPapiers(), session);

    // Sauvegarde les invalidations des cartes
    if (CollectionUtils.isNotEmpty(bulkActions.getToInvalid())) {
      invalidationCarteService.saveInvalid(bulkActions.getToInvalid(), session);

      // Envoi d'event pour les invalidations des cartes
      invalidationCarteService.sendEventInvalidCards(bulkActions.getToInvalid());
    }

    HistoriqueExecutions620 histo = bulkActions.getHistoriqueExecution();
    // Rejections
    if (CollectionUtils.isNotEmpty(bulkActions.getRejections())) {
      long carteDematKo =
          bulkActions.getRejections().stream()
              .filter(
                  rejection -> Constants.CARTE_DEMATERIALISEE.equals(rejection.getCodeService()))
              .count();
      histo.incNbCartesKo((int) carteDematKo);
    }

    // Historisation
    histo.incNbCartesOk(nombreCarteDematInsere);
    if (CollectionUtils.isNotEmpty(cartesPapiers)) {
      histo.incNbCartesPapierEdit(bulkActions.getPapiers().size());
    }

    if (CollectionUtils.isNotEmpty(traceExtractions)) {
      traceExtractionConsoService.saveTraceList(traceExtractions, session);
    }

    histo.incNbCartesInvalidees(bulkActions.getToInvalid().size());
    histo.setDernierNumeroContrat(bulkActions.getLastNumContrat());
    histo.setDernierNumeroAdherent(bulkActions.getLastNumAdherent());

    HistoriqueExecutions620 histoBatch = bulkActions.getHistoriqueExecution();
    newHistoriqueExecutions620AMC.incAllFromOther(histoBatch);
    newHistoriqueExecutions620AMC.setDernierNumeroContrat(bulkActions.getLastNumContrat());
    newHistoriqueExecutions620AMC.setDernierNumeroAdherent(bulkActions.getLastNumAdherent());
    newHistoriqueExecutions620AMC.setNbDeclarationTraitee(
        newHistoriqueExecutions620AMC.getNbDeclarationTraitee());
    if (lastTask) {
      newHistoriqueExecutions620AMC.setDateFinCartes(DateUtils.nowUTC());
      newHistoriqueExecutions620AMC.setDateFinConsolidations(DateUtils.nowUTC());
    }
    historiqueExecutionsDao.saveWithSession(newHistoriqueExecutions620AMC, session);

    long fin = System.currentTimeMillis();
    log.info(
        "temps {}, processBulks fin declarant {} du contrat {} au contrat {}",
        (fin - debut),
        declarant.get_id(),
        bulkActions.getFirstNumContrat(),
        bulkActions.getLastNumContrat());
  }

  /** récupère les déclarations à traiter selon si l'on est en mode reprise ou pas. */
  private Stream<Declaration> getDeclarations(
      Declarant declarant, DataForJob620 dataForJob620, Date dateSynchroToUse) {
    if (dataForJob620.getDataForReprise620() == null) {
      return declarationService.findDeclarationsByAMCandCarteEditable(
          declarant.get_id(), dateSynchroToUse);
    }
    return declarationService.findDeclarationsByAMCandCarteEditableAndContracts(
        declarant.get_id(),
        dataForJob620.getDataForReprise620().getFromContrat(),
        dataForJob620.getDataForReprise620().getToContrat(),
        dataForJob620.getDataForReprise620().getFromAdherent());
  }

  /** envoi d'un event avec le nombre de cartes actives par déclarant. */
  private void handleEventNbCartesActives(DataForJob620 dataForJob620, Declarant declarant) {
    String dateExec = DateUtils.formatDate(dataForJob620.getToday());
    String amc = declarant.get_id();
    // TODO voir si index à créer
    eventService.sendObservabilityEventCartesDematActivesCount(
        amc,
        dateExec,
        cartesService.findCartesDematByDeclarantAndDateExec(declarant.get_id(), dateExec));
  }
}
