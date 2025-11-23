package com.cegedim.next.serviceeligibility.consolidationcontract.services;

import com.cegedim.next.serviceeligibility.consolidationcontract.bean.*;
import com.cegedim.next.serviceeligibility.consolidationcontract.util.ConsolidationJobUtil;
import com.cegedim.next.serviceeligibility.core.dao.ContractDao;
import com.cegedim.next.serviceeligibility.core.dao.HistoriqueExecutionsDao;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecutions634;
import com.cegedim.next.serviceeligibility.core.services.contracttp.ContractTPService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionTechnique;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import joptsimple.internal.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

@Slf4j
public class ProcessorDeclaration {

  public static final int OFFSET_LIMIT = 10000;
  @Transient private final List<ProcessorPartition> partitions;
  @Transient private final MongoTemplate template;
  @Transient private final ContractDao contractDao;
  @Transient private final ContractTPService contractTPService;
  private final Map<String, Long> stepsDurations;
  private final int saveBufferSize;
  private final int contratFetchSize;
  private final int contratPartitionSize;
  private final int declarationFetchSize;
  private final String contratsCollection;
  private final int partitionSize;
  private final int parallelisme;
  private final long nbDeclarationsInitial;
  private final long nbDeclarationsATraiter;
  private final long initialPosition;
  private final List<String> declarantList;

  @Transient private final List<ProcessorPartition> processedPartitions;

  private final HistoriqueExecutions634 lastExecution;

  private final boolean shouldHistorize;
  @Transient private final ContractHistoService contractHistoService;

  private final HistoriqueExecutionsDao historiqueExecutionsDao;

  public ProcessorDeclaration(
      List<ProcessorPartition> processedPartitions,
      HistoriqueExecutions634 lastExecution,
      ContractDao contractDao,
      MongoTemplate template,
      ContractTPService contractTPService,
      Map<String, Long> stepsDurations,
      int saveBufferSize,
      int declarationFetchSize,
      int contratFetchSize,
      int contratPartitionSize,
      String contractCollection,
      int parallelisme,
      int partitionSize,
      boolean shouldHistorize,
      ContractHistoService contractHistoService,
      List<String> declarantList,
      HistoriqueExecutionsDao historiqueExecutionsDao) {
    this.lastExecution = new HistoriqueExecutions634(lastExecution);
    this.processedPartitions = processedPartitions;
    this.partitionSize = partitionSize;
    this.shouldHistorize = shouldHistorize;
    this.contractHistoService = contractHistoService;
    this.declarantList = declarantList;
    this.historiqueExecutionsDao = historiqueExecutionsDao;
    log.info("La partition fournie est null, on traite toute la collection");
    this.nbDeclarationsInitial =
        template
            .getDb()
            .runCommand(new Document("count", Constants.DECLARATION_COLLECTION))
            .getInteger("n");
    long skip = lastExecution.getFromDeclaration();
    nbDeclarationsATraiter = lastExecution.getToDeclaration();
    int limit = 1;
    if (lastExecution.getPositionCheckpoint() > 0) {
      // On doit aller chercher la dernière déclaration du contrat.
      int nbNewDeclarations =
          (int) (this.nbDeclarationsInitial - lastExecution.getNbDeclarationsInitial());
      skip = Math.max(0L, lastExecution.getPositionCheckpoint() - nbNewDeclarations);
      limit = nbNewDeclarations + 1;
    }
    Query rangeQuery =
        ConsolidationJobUtil.includeDeclarationIndexFields(
            new Query().with(buildDeclarationIndexSort()).skip(skip).limit(limit), declarantList);
    List<DeclarationKey> keys =
        template.find(rangeQuery, DeclarationKey.class, Constants.DECLARATION_COLLECTION);

    int delta = 0;
    ContractKey keyFrom;
    if (keys.size() == 1) {
      keyFrom =
          new ContractKey(
              keys.get(0).getIdDeclarant(),
              keys.get(0).getContratNumero(),
              keys.get(0).getNumeroAdherent());
      if (lastExecution.getPositionCheckpoint() == 0) {
        lastExecution.setPremierIdDeclarant(keyFrom.getIdDeclarant());
        lastExecution.setPremierNumeroContrat(keyFrom.getNumeroContrat());
        lastExecution.setPremierNumeroAdherent(keyFrom.getNumeroAdherent());
      }
    } else {
      ContractKey lastKey =
          new ContractKey(
              lastExecution.getDernierIdDeclarant(),
              lastExecution.getDernierNumeroContrat(),
              lastExecution.getDernierNumeroAdherent());
      List<DeclarationKey> filteredKeys = keys.stream().filter(k -> isAfter(k, lastKey)).toList();

      keyFrom =
          filteredKeys.stream()
              .findFirst()
              .map(
                  k ->
                      new ContractKey(
                          k.getIdDeclarant(), k.getContratNumero(), k.getNumeroAdherent()))
              .orElse(lastKey);
      delta = (keys.size() - filteredKeys.size()) - 1;
    }
    rangeQuery =
        ConsolidationJobUtil.includeDeclarationIndexFields(
            new Query().with(buildDeclarationIndexSort()).skip(nbDeclarationsATraiter - 1).limit(1),
            declarantList);
    keys.addAll(template.find(rangeQuery, DeclarationKey.class, Constants.DECLARATION_COLLECTION));
    this.initialPosition = skip + delta;
    this.partitions =
        List.of(
            new ProcessorPartition(
                0,
                new SkipRange(
                    this.initialPosition,
                    nbDeclarationsATraiter,
                    keyFrom,
                    new ContractKey(
                        keys.get(keys.size() - 1).getIdDeclarant(),
                        keys.get(keys.size() - 1).getContratNumero(),
                        keys.get(keys.size() - 1).getNumeroAdherent())),
                PartitionStatus.NOT_PROCESSED));
    log.info("Nombre de déclarations à traiter initiale : {}", nbDeclarationsInitial);
    log.info("Nombre de déclarations à traiter : {}", nbDeclarationsATraiter - initialPosition);
    this.parallelisme = parallelisme;
    this.contratsCollection = contractCollection;
    this.saveBufferSize = saveBufferSize;
    this.contratPartitionSize = contratPartitionSize;
    this.declarationFetchSize = declarationFetchSize;
    this.contratFetchSize = contratFetchSize;
    this.stepsDurations = stepsDurations;
    this.contractTPService = contractTPService;
    this.template = template;
    this.contractDao = contractDao;
  }

  public static Sort buildDeclarationIndexSort() {
    return Sort.by(
        List.of(
            new Sort.Order(Sort.Direction.ASC, Constants.ID_DECLARANT),
            new Sort.Order(Sort.Direction.ASC, Constants.DECLARATION_NUMERO_CONTRAT),
            new Sort.Order(Sort.Direction.ASC, Constants.CONTRAT_NUMERO_ADHERENT),
            new Sort.Order(Sort.Direction.ASC, Constants.EFFET_DEBUT),
            new Sort.Order(Sort.Direction.ASC, Constants.ID)));
  }

  public void process() throws Exception {
    // FolderProcessor tasks to store the subtasks that are going to process the
    // subfolders stored in the folder
    List<DeclarationCallable> tasks = new ArrayList<>();
    List<ProcessorPartition> initialPartitions = new ArrayList<>();
    long currentPosition = this.initialPosition;
    while (currentPosition < nbDeclarationsATraiter) {
      initialPartitions.add(
          new ProcessorPartition(
              initialPartitions.size(),
              new SkipRange(currentPosition, currentPosition + partitionSize, null, null),
              PartitionStatus.NOT_PROCESSED));
      currentPosition += partitionSize;
    }
    log.info("Liste des {} partitions :", initialPartitions.size());
    initialPartitions.forEach(
        p ->
            log.info(
                "Partition {}: de {} à {}",
                p.getPartitionNumber(),
                p.getRange().getFromDeclaration(),
                p.getRange().getToDeclaration()));

    log.info("Décalage des bornes des partitions pour correspondre à des débuts de contrats");
    Sort indexSort = buildDeclarationIndexSort();
    terminalOffset(initialPartitions, indexSort, declarantList);
    if (initialPartitions.isEmpty()) {
      log.warn("Il n'y a pas de déclaration à traiter");
    } else {
      // On ne retient pas les partitions dont le calcul fait apparaitre qu'elle
      // démarre après
      // la fin de la collection
      List<ProcessorPartition> finalPartitions = completeContractKeyInOffset(initialPartitions);

      log.info("Nouvelles bornes des partitions :");
      finalPartitions.forEach(
          p ->
              log.info(
                  "Partition {}: {} à {}",
                  p.getPartitionNumber(),
                  p.getRange().getFromDeclaration(),
                  p.getRange().getToDeclaration()));

      Map<Integer, List<ProcessorPartition>> splittedPartitions =
          finalPartitions.stream()
              .collect(Collectors.groupingBy(p -> p.getPartitionNumber() % parallelisme));

      log.info("Liste des partitions par Task");
      splittedPartitions.forEach(
          (k, v) ->
              log.info(
                  "Partitions : {}",
                  Strings.join(
                      v.stream().map(p -> Integer.toString(p.getPartitionNumber())).toList(),
                      ", ")));

      splittedPartitions.forEach(
          (k, v) -> {
            DeclarationCallable task =
                new DeclarationCallable(
                    new RequestDeclarationCallable(
                        v,
                        processedPartitions,
                        lastExecution,
                        contractDao,
                        template,
                        contractTPService,
                        stepsDurations,
                        saveBufferSize,
                        declarationFetchSize,
                        contratFetchSize,
                        contratPartitionSize,
                        contratsCollection,
                        shouldHistorize,
                        contractHistoService,
                        declarantList));
            tasks.add(task);
          });
    }

    addResultsFromTasks(tasks);
  }

  private List<ProcessorPartition> completeContractKeyInOffset(
      List<ProcessorPartition> initialPartitions) {
    List<ProcessorPartition> finalPartitions =
        initialPartitions.stream()
            .filter(e -> e.getRange().getFromDeclaration() < nbDeclarationsATraiter)
            .toList();

    // La première partition commence à la première déclaration
    finalPartitions
        .get(0)
        .getRange()
        .setFromContractKey(partitions.get(0).getRange().getFromContractKey());

    // La dernière partition se termine au dernier élément de la collection
    finalPartitions
        .get(finalPartitions.size() - 1)
        .getRange()
        .setToDeclaration(nbDeclarationsATraiter);
    finalPartitions
        .get(finalPartitions.size() - 1)
        .getRange()
        .setToContractKey(partitions.get(partitions.size() - 1).getRange().getToContractKey());
    return finalPartitions;
  }

  private void terminalOffset(
      List<ProcessorPartition> initialPartitions, Sort indexSort, List<String> declarantList) {
    for (int i = 1; i < initialPartitions.size(); i++) {
      log.info("Décalage des bornes de la partition {}", i);
      Query rangeQuery =
          ConsolidationJobUtil.includeDeclarationIndexFields(
              new Query()
                  .with(indexSort)
                  .skip(initialPartitions.get(i).getRange().getFromDeclaration())
                  .limit(OFFSET_LIMIT),
              declarantList);
      List<DeclarationKey> keys =
          template.find(rangeQuery, DeclarationKey.class, Constants.DECLARATION_COLLECTION);
      int offset = getOffset(keys);
      initialPartitions
          .get(i)
          .getRange()
          .setFromDeclaration(initialPartitions.get(i).getRange().getFromDeclaration() + offset);
      initialPartitions
          .get(i)
          .getRange()
          .setFromContractKey(
              new ContractKey(
                  keys.get(offset).getIdDeclarant(),
                  keys.get(offset).getContratNumero(),
                  keys.get(offset).getNumeroAdherent()));
      // Fixe la borne supérieure de la partition d'avant
      initialPartitions
          .get(i - 1)
          .getRange()
          .setToDeclaration(initialPartitions.get(i).getRange().getFromDeclaration());
      initialPartitions
          .get(i - 1)
          .getRange()
          .setToContractKey(
              new ContractKey(
                  keys.get(offset - 1).getIdDeclarant(),
                  keys.get(offset - 1).getContratNumero(),
                  keys.get(offset - 1).getNumeroAdherent()));
    }
  }

  private static int getOffset(List<DeclarationKey> keys) {
    int offset = 1;
    if (CollectionUtils.isNotEmpty(keys)) {
      DeclarationKey key = keys.get(0);
      while (offset < keys.size() - 1 && key.isSameContract(keys.get(offset))) {
        offset++;
      }
      if (offset >= OFFSET_LIMIT) {
        throw new IndexOutOfBoundsException(
            "Impossible de générer des partitions consistantes. Chaque partition doit se terminer par une dernière déclaration d'un contrat");
      }
    }
    return offset;
  }

  // For each task stored in the list of tasks, call the join() method that
  // will wait for its finalization and then will return the result of the task.
  // Add that result to the list of strings using the addAll() method.
  private void addResultsFromTasks(List<DeclarationCallable> tasks) throws InterruptedException {
    ExecutorService pool = Executors.newFixedThreadPool(parallelisme);
    List<Future<HistoriqueExecutions634>> results = pool.invokeAll(tasks);
    results.forEach(
        historiqueExecutionsFuture -> {
          try {
            int lastPartitionNumber =
                findLastContiguous(
                    processedPartitions.stream()
                        .map(ProcessorPartition::getPartitionNumber)
                        .sorted()
                        .toList());
            if (lastPartitionNumber != -1) {
              ProcessorPartition lastPartition =
                  processedPartitions.stream()
                      .filter(p -> p.getPartitionNumber() == lastPartitionNumber)
                      .findFirst()
                      .orElse(null);
              long nbPartitionTraitee = processedPartitions.size();
              long nbDeclarationTraitee =
                  processedPartitions.stream()
                      .mapToLong(
                          p -> p.getRange().getToDeclaration() - p.getRange().getFromDeclaration())
                      .sum();
              log.info(
                  "Nombre de partitions traités: {} / Nombre de déclarations traitées: {} / {}",
                  nbPartitionTraitee,
                  nbDeclarationTraitee,
                  historiqueExecutionsFuture.get().getNbDeclarationsInitial());
              if (lastPartition != null) {
                log.info(
                    "Insertion d'un nouveau checkpoint, partition n°{} à la position {}",
                    lastPartition.getPartitionNumber(),
                    lastPartition.getRange().getToDeclaration());
                lastExecution.setDernierIdDeclarant(
                    lastPartition.getRange().getToContractKey().getIdDeclarant());
                lastExecution.setDernierNumeroContrat(
                    lastPartition.getRange().getToContractKey().getNumeroContrat());
                lastExecution.setDernierNumeroAdherent(
                    lastPartition.getRange().getToContractKey().getNumeroAdherent());
                lastExecution.setDateCheckpoint(Date.from(Instant.now()));
                lastExecution.setPositionCheckpoint(lastPartition.getRange().getToDeclaration());

                lastExecution.incNbDeclarationTraitee(
                    historiqueExecutionsFuture.get().getNbDeclarationTraitee());
                lastExecution.incNbDeclarationErreur(
                    historiqueExecutionsFuture.get().getNbDeclarationErreur());
                lastExecution.incNbDeclarationIgnoree(
                    historiqueExecutionsFuture.get().getNbDeclarationIgnoree());
                lastExecution.incNbContratCree(historiqueExecutionsFuture.get().getNbContratCree());
                historiqueExecutionsDao.save(lastExecution);
              }
            }
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

  private Integer findLastContiguous(final List<Integer> array) {
    Integer first = -1;
    for (int i = 0; i < array.size(); i++) {
      if (i == array.get(i)) {
        first = array.get(i);
      }
    }
    return first;
  }

  public boolean isAfter(DeclarationKey declaration, ContractKey contrat) {
    if (contrat == null || declaration == null) return false;
    int cp = StringUtils.compare(declaration.getIdDeclarant(), contrat.getIdDeclarant());
    if (cp == 0) {
      cp = StringUtils.compare(declaration.getContratNumero(), contrat.getNumeroContrat());
      if (cp == 0) {
        return StringUtils.compare(declaration.getNumeroAdherent(), contrat.getNumeroAdherent())
            > 0;
      } else {
        return cp > 0;
      }
    } else {
      return cp > 0;
    }
  }
}
