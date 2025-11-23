package com.cegedim.next.serviceeligibility.consolidationcontract.services;

import com.cegedim.next.serviceeligibility.consolidationcontract.bean.*;
import com.cegedim.next.serviceeligibility.consolidationcontract.util.ConsolidationJobUtil;
import com.cegedim.next.serviceeligibility.core.dao.ContractDao;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTPCommun;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecutions634;
import com.cegedim.next.serviceeligibility.core.services.contracttp.ContractTPService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@Slf4j
public class DeclarationCallable implements Callable<HistoriqueExecutions634> {

  @Transient private final StopWatch stopWatchProducer = new StopWatch();
  private final List<ContractTP> contractsToSave = Collections.synchronizedList(new LinkedList<>());
  private final List<ContractTP> contractsTPHistos =
      Collections.synchronizedList(new LinkedList<>());
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

  private final Map<Integer, Pair<SkipRange, List<ContractKey>>> contractsToSkip = new HashMap<>();

  @Transient private final List<ProcessorPartition> processedPartitions;

  private final HistoriqueExecutions634 lastExecution;

  private final boolean shouldHistorize;
  @Transient private final ContractHistoService contractHistoService;

  private final List<String> declarantList;

  public DeclarationCallable(RequestDeclarationCallable declarationCallable) {
    this.lastExecution = new HistoriqueExecutions634(declarationCallable.lastExecution());
    this.processedPartitions = declarationCallable.processedPartitions();
    this.shouldHistorize = declarationCallable.shouldHistorize();
    this.contractHistoService = declarationCallable.contractHistoService();
    this.declarantList = declarationCallable.declarantList();

    declarationCallable
        .partitions()
        .forEach(
            partition ->
                log.info(
                    "Une partition est fournie, on traite la collection de {} à {}: {} -> {}",
                    partition.getRange().getFromDeclaration(),
                    partition.getRange().getToDeclaration(),
                    partition.getRange().getFromContractKey(),
                    partition.getRange().getToContractKey()));
    logInfoCallable(declarationCallable);
    this.partitions = declarationCallable.partitions();
    this.contratsCollection = declarationCallable.contractCollection();
    this.saveBufferSize = declarationCallable.saveBufferSize();
    this.contratPartitionSize = declarationCallable.contratPartitionSize();
    this.declarationFetchSize = declarationCallable.declarationFetchSize();
    this.contratFetchSize = declarationCallable.contratFetchSize();
    this.stepsDurations = declarationCallable.stepsDurations();
    this.contractTPService = declarationCallable.contractTPService();
    this.template = declarationCallable.template();
    this.contractDao = declarationCallable.contractDao();
  }

  private static void logInfoCallable(RequestDeclarationCallable declarationCallable) {
    final long nbDeclarationsInitial;
    final long nbDeclarationsATraiter;
    final long initialPosition;
    if (declarationCallable.partitions().isEmpty()) {
      initialPosition = 0;
    } else {
      Optional<ProcessorPartition> optionalProcessorPartition =
          declarationCallable.partitions().stream()
              .min(Comparator.comparing(p -> p.getRange().getFromDeclaration()));
      initialPosition =
          optionalProcessorPartition
              .map(processorPartition -> processorPartition.getRange().getFromDeclaration())
              .orElse(0L);
    }
    nbDeclarationsInitial =
        declarationCallable.partitions().stream()
            .mapToLong(p -> p.getRange().getToDeclaration() - p.getRange().getFromDeclaration())
            .sum();
    nbDeclarationsATraiter =
        declarationCallable.partitions().stream()
            .mapToLong(p -> p.getRange().getToDeclaration())
            .max()
            .orElse(0);

    log.info(
        "Dans la tache : Nombre de déclarations à traiter initiale : {}", nbDeclarationsInitial);
    log.info(
        "Dans la tache : Nombre de déclarations à traiter : {}",
        nbDeclarationsATraiter - initialPosition);
  }

  private Query includeContratIndexFields(Query query) {
    query
        .fields()
        .include(Constants.ID_DECLARANT, Constants.NUMERO_CONTRAT, Constants.NUMERO_ADHERENT);
    return query;
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

  private Sort buildContratIndexSort() {
    return Sort.by(
        List.of(
            new Sort.Order(Sort.Direction.ASC, Constants.ID_DECLARANT),
            new Sort.Order(Sort.Direction.ASC, Constants.NUMERO_CONTRAT),
            new Sort.Order(Sort.Direction.ASC, Constants.NUMERO_ADHERENT)));
  }

  private ContractKey fillContractToSkip(ProcessorPartition partition) {
    long toSkip;
    if (contractsToSkip.isEmpty()) {
      toSkip = 0;
    } else {
      toSkip =
          contractsToSkip.get(contractsToSkip.size() - 1).getKey().getFromDeclaration()
              + contractsToSkip.get(contractsToSkip.size() - 1).getKey().getToDeclaration();
    }
    Pair<SkipRange, List<ContractKey>> newScan =
        new ImmutablePair<>(new SkipRange(toSkip, 0, null, null), new ArrayList<>());

    Criteria fromCriteria =
        ConsolidationJobUtil.getGreatherCriteria(partition.getRange().getFromContractKey(), false);
    Criteria toCriteria =
        new Criteria()
            .orOperator(
                Criteria.where(Constants.ID_DECLARANT)
                    .lt(partition.getRange().getToContractKey().getIdDeclarant()),
                new Criteria()
                    .andOperator(
                        Criteria.where(Constants.ID_DECLARANT)
                            .is(partition.getRange().getToContractKey().getIdDeclarant()),
                        Criteria.where(Constants.NUMERO_CONTRAT)
                            .lte(partition.getRange().getToContractKey().getNumeroContrat())),
                new Criteria()
                    .andOperator(
                        Criteria.where(Constants.ID_DECLARANT)
                            .is(partition.getRange().getToContractKey().getIdDeclarant()),
                        Criteria.where(Constants.NUMERO_CONTRAT)
                            .is(partition.getRange().getToContractKey().getNumeroContrat()),
                        Criteria.where(Constants.NUMERO_ADHERENT)
                            .lt(partition.getRange().getToContractKey().getNumeroAdherent())));
    log.info(
        "Recherche des contrat existants pour la partition {} entre {} et {}",
        partition.getPartitionNumber(),
        partition.getRange().getFromContractKey(),
        partition.getRange().getToContractKey());
    Query contratQuery =
        includeContratIndexFields(
                new Query().cursorBatchSize(contratFetchSize).with(buildContratIndexSort()))
            .addCriteria(new Criteria().andOperator(fromCriteria, toCriteria))
            .skip(toSkip)
            .limit(contratPartitionSize);
    Stream<ContractKey> i =
        template.stream(contratQuery, ContractKey.class, this.contratsCollection);
    newScan.getValue().addAll(i.toList());
    log.info(
        "Nombre de contrat existants pour la partition {} (skip={}, limit={}) : {})",
        partition.getPartitionNumber(),
        toSkip,
        contratPartitionSize,
        newScan.getValue().size());
    newScan.getKey().setToDeclaration(newScan.getValue().size());
    contractsToSkip.put(contractsToSkip.size(), newScan);
    if (newScan.getValue().isEmpty()) return null;
    else {
      newScan.getKey().setFromContractKey(newScan.getValue().getFirst());
      newScan.getKey().setToContractKey(newScan.getValue().getLast());
      newScan.getKey().setToDeclaration(newScan.getValue().size());
      return newScan.getValue().getLast();
    }
  }

  @Override
  public HistoriqueExecutions634 call() {
    List<HistoriqueExecutions634> statistics =
        this.partitions.stream()
            .map(
                partition -> {
                  HistoriqueExecutions634 result = new HistoriqueExecutions634();
                  result.setNbDeclarationATraiter(
                      partition.getRange().getToDeclaration()
                          - partition.getRange().getFromDeclaration());
                  log.info("Début du processor de la partition {}", partition.getPartitionNumber());
                  log.info(
                      "Processing de la partition {}: de {} à {}",
                      partition.getPartitionNumber(),
                      partition.getRange().getFromDeclaration(),
                      partition.getRange().getToDeclaration());
                  Query q = new Query();
                  if (CollectionUtils.isNotEmpty(declarantList)) {
                    Criteria criteria = Criteria.where(Constants.ID_DECLARANT).in(declarantList);
                    q.addCriteria(criteria);
                  }
                  q.cursorBatchSize(declarationFetchSize)
                      .with(buildDeclarationIndexSort())
                      .skip(partition.getRange().getFromDeclaration())
                      .limit(
                          Math.toIntExact(
                              partition.getRange().getToDeclaration()
                                  - partition.getRange().getFromDeclaration()));

                  AtomicReference<ContractTP> currentContract = new AtomicReference<>(null);
                  AtomicReference<ContractTP> nextContract = new AtomicReference<>(null);
                  AtomicReference<ContractTP> saveContract = new AtomicReference<>(null);
                  AtomicReference<ContractKey> lastSkippedContract = new AtomicReference<>(null);
                  AtomicBoolean skippingContrat = new AtomicBoolean(false);
                  AtomicReference<ContractHistoService.Buffer> histoBuffer =
                      new AtomicReference<>(new ContractHistoService.Buffer(null, null));
                  // Charge les contrats déjà existants pour ne pas les récréer en cas de reprise
                  contractsToSkip.clear();
                  lastSkippedContract.set(fillContractToSkip(partition));
                  Stream<Declaration> i =
                      template.stream(q, Declaration.class, Constants.DECLARATION_COLLECTION);
                  log.info(
                      "Début de lecture des déclarations pour la partition {}",
                      partition.getPartitionNumber());
                  stopWatchProducer.start();
                  i.forEach(
                      declaration ->
                          processDeclaration(
                              new RequestProcessDeclarationRDO(
                                  partition,
                                  declaration,
                                  skippingContrat,
                                  saveContract,
                                  currentContract,
                                  result,
                                  histoBuffer,
                                  nextContract,
                                  lastSkippedContract)));
                  this.registerTime(stopWatchProducer, "Fetching MongoDB");
                  if (currentContract.get() != null && !skippingContrat.get()) {
                    contractsToSave.add(currentContract.get());
                  }
                  if (histoBuffer.get().getContract() != null) {
                    contractsTPHistos.add(histoBuffer.get().getContract());
                  }
                  saveContracts(result);

                  logDeclaration(partition, result);
                  log.info("Fin du processor de la partition {}", partition.getPartitionNumber());
                  processedPartitions.add(partition);
                  return result;
                })
            .toList();
    HistoriqueExecutions634 historiqueExecutions634 = new HistoriqueExecutions634(lastExecution);
    historiqueExecutions634.incNbDeclarationTraitee(
        statistics.stream().mapToInt(HistoriqueExecutions634::getNbDeclarationTraitee).sum());
    historiqueExecutions634.incNbDeclarationErreur(
        statistics.stream().mapToInt(HistoriqueExecutions634::getNbDeclarationErreur).sum());
    historiqueExecutions634.incNbDeclarationIgnoree(
        statistics.stream().mapToInt(HistoriqueExecutions634::getNbDeclarationIgnoree).sum());
    historiqueExecutions634.incNbContratCree(
        statistics.stream().mapToInt(HistoriqueExecutions634::getNbContratCree).sum());
    return historiqueExecutions634;
  }

  private void processDeclaration(RequestProcessDeclarationRDO requestProcessDeclarationRDO) {
    if (requestProcessDeclarationRDO
        .declaration()
        .getDateCreation()
        .after(lastExecution.getDateExecution())) {
      requestProcessDeclarationRDO.result().incNbDeclarationIgnoree(1);
    } else {
      requestProcessDeclarationRDO.result().incNbDeclarationTraitee(1);
      this.registerTime(stopWatchProducer, "Fetching MongoDB");
      if (ContractTPCommun.isDeclarationOfTheContract(
          requestProcessDeclarationRDO.currentContract().get(),
          requestProcessDeclarationRDO.declaration())) {
        if (requestProcessDeclarationRDO.skippingContrat().get()) {
          // Si le contrat est à ignorer on incrémente juste le compteur
          requestProcessDeclarationRDO.result().incNbDeclarationIgnoree(1);
        } else {
          computeDeclaration(
              requestProcessDeclarationRDO.declaration(),
              requestProcessDeclarationRDO.nextContract(),
              requestProcessDeclarationRDO.currentContract(),
              requestProcessDeclarationRDO.histoBuffer());
        }
      } else {
        saveInfoContract(requestProcessDeclarationRDO);
      }
      if (requestProcessDeclarationRDO.result().getNbDeclarationLue() % 10000 == 0) {
        logDeclaration(
            requestProcessDeclarationRDO.partition(), requestProcessDeclarationRDO.result());
      }
      stopWatchProducer.start();
    }
  }

  private static void logDeclaration(ProcessorPartition partition, HistoriqueExecutions634 result) {
    log.info(
        "Nombre de déclarations lues pour la partition {}: {} / {}",
        partition.getPartitionNumber(),
        result.getNbDeclarationLue(),
        result.getNbDeclarationATraiter());
  }

  private void saveInfoContract(RequestProcessDeclarationRDO requestProcessDeclarationRDO) {
    if (!requestProcessDeclarationRDO.skippingContrat().get()) {
      requestProcessDeclarationRDO
          .saveContract()
          .set(requestProcessDeclarationRDO.currentContract().get());
      if (requestProcessDeclarationRDO.saveContract().get() != null) {
        contractsToSave.add(requestProcessDeclarationRDO.saveContract().get());
        lastExecution.setDernierIdDeclarant(
            requestProcessDeclarationRDO.saveContract().get().getIdDeclarant());
        lastExecution.setDernierNumeroContrat(
            requestProcessDeclarationRDO.saveContract().get().getNumeroContrat());
        lastExecution.setDernierNumeroAdherent(
            requestProcessDeclarationRDO.saveContract().get().getNumeroAdherent());
        requestProcessDeclarationRDO.saveContract().set(null);
        if (contractsToSave.size() >= saveBufferSize) {
          saveContracts(requestProcessDeclarationRDO.result());
        }
      }
    }
    stopWatchProducer.start();
    // Set le current à null pour forcer la création d'un nouvel objet
    // dans la fonction 'contractTPService.processDeclarationPerf'
    requestProcessDeclarationRDO.currentContract().set(null);
    if (shouldHistorize) {
      ContractHistoService.Buffer newBuffer =
          contractHistoService.getCurrentContract(
              requestProcessDeclarationRDO.histoBuffer().get(),
              requestProcessDeclarationRDO.declaration(),
              this.contratsCollection);
      requestProcessDeclarationRDO.histoBuffer().set(newBuffer);
    }
    requestProcessDeclarationRDO
        .nextContract()
        .set(
            contractTPService.processDeclarationRDO(
                requestProcessDeclarationRDO.declaration(),
                requestProcessDeclarationRDO.currentContract().get()));
    // Le prochain contrat à sauvegarder devient le contrat courrant
    requestProcessDeclarationRDO
        .currentContract()
        .set(requestProcessDeclarationRDO.nextContract().get());
    this.registerTime(stopWatchProducer, "Processing declaration");

    // Si l'on arrive après le dernier contrat fetch, on prend les X suivants
    if (isAfter(
        requestProcessDeclarationRDO.declaration(),
        requestProcessDeclarationRDO.lastSkippedContract().get())) {
      stopWatchProducer.start();
      requestProcessDeclarationRDO
          .lastSkippedContract()
          .set(fillContractToSkip(requestProcessDeclarationRDO.partition()));
      this.registerTime(stopWatchProducer, "Lecture des contrats à ignorer");
    }
    // Vérifie si le prochain contrat à sauvegarder est à ignorer
    if (!contractsToSkip.isEmpty()) {
      stopWatchProducer.start();
      requestProcessDeclarationRDO
          .skippingContrat()
          .set(
              this.contractsToSkip
                  .get(contractsToSkip.size() - 1)
                  .getValue()
                  .removeIf(c -> c.isSameContract(requestProcessDeclarationRDO.declaration())));
      this.registerTime(stopWatchProducer, "Vérification des contrats à ignorer");
    } else {
      requestProcessDeclarationRDO.skippingContrat().set(false);
    }
  }

  private void computeDeclaration(
      Declaration d,
      AtomicReference<ContractTP> nextContract,
      AtomicReference<ContractTP> currentContract,
      AtomicReference<ContractHistoService.Buffer> histoBuffer) {
    stopWatchProducer.start();
    nextContract.set(contractTPService.processDeclarationRDO(d, currentContract.get()));
    this.registerTime(stopWatchProducer, "Processing declaration");
    // Le prochain contrat devient le contrat courant
    currentContract.set(nextContract.get());

    if (shouldHistorize && !contractHistoService.isSameContractFlux(histoBuffer.get(), d)) {
      if (histoBuffer.get().getContract() != null) {
        contractsTPHistos.add(histoBuffer.get().getContract());
      }
      ContractHistoService.Buffer newBuffer =
          new ContractHistoService.Buffer(nextContract.get(), contractHistoService.getFluxKey(d));
      histoBuffer.set(newBuffer);
    }
  }

  public boolean isAfter(Declaration declaration, ContractKey contrat) {
    if (contrat == null || declaration == null) return false;
    int cp = StringUtils.compare(declaration.getIdDeclarant(), contrat.getIdDeclarant());
    if (cp == 0) {
      cp = StringUtils.compare(declaration.getContrat().getNumero(), contrat.getNumeroContrat());
      if (cp == 0) {
        return StringUtils.compare(
                declaration.getContrat().getNumeroAdherent(), contrat.getNumeroAdherent())
            > 0;
      } else {
        return cp > 0;
      }
    } else {
      return cp > 0;
    }
  }

  private void saveContracts(HistoriqueExecutions634 historiqueExecutions634) {
    stopWatchProducer.start();
    int nbSaved = contractDao.bulkInsert(contractsToSave, contratsCollection);
    contractsTPHistos.forEach(contractHistoService::saveToElastic);
    historiqueExecutions634.incNbContratCree(nbSaved);
    // Après l'enregistrement on efface les objets
    contractsToSave.clear();
    contractsTPHistos.clear();
    this.registerTime(stopWatchProducer, "Bulk insert contrat");
  }

  private void registerTime(StopWatch sw, String action) {
    sw.stop();
    stepsDurations.put(action, stepsDurations.getOrDefault(action, 0L) + sw.getTime());
    sw.reset();
  }
}
