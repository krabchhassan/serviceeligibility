package com.cegedim.next.serviceeligibility.consolidationcontract.services;

import static com.cegedim.next.serviceeligibility.consolidationcontract.constants.Constants.BATCH_MODE_NO_RDO;
import static com.cegedim.next.serviceeligibility.consolidationcontract.constants.Constants.BATCH_MODE_RDO;

import com.cegedim.next.serviceeligibility.consolidationcontract.bean.DeclarationKey;
import com.cegedim.next.serviceeligibility.consolidationcontract.bean.ProcessorPartition;
import com.cegedim.next.serviceeligibility.consolidationcontract.config.DeclarationKeyReaderConverter;
import com.cegedim.next.serviceeligibility.consolidationcontract.constants.Constants;
import com.cegedim.next.serviceeligibility.consolidationcontract.util.ConsolidationJobUtil;
import com.cegedim.next.serviceeligibility.core.dao.ContractDao;
import com.cegedim.next.serviceeligibility.core.dao.ContratsAMCexcluesDao;
import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduConsolidationContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.AmcExclues;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContratsAMCexclues;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecutions634;
import com.cegedim.next.serviceeligibility.core.model.job.DataForJob634;
import com.cegedim.next.serviceeligibility.core.services.common.batch.TalendJob;
import com.cegedim.next.serviceeligibility.core.services.contracttp.BulkContratTP;
import com.cegedim.next.serviceeligibility.core.services.contracttp.ContractTPService;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionTechnique;
import com.mongodb.ReadPreference;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Engine extends TalendJob<HistoriqueExecutions634, DataForJob634> {
  public static final String CONTRATS_AMCEXCLUES = "contrats-AMCexclues";
  public static final String UPSERT_CONTRATS_AM_CEXCLUES_DAO = "Upsert contratsAMCexcluesDao";

  @Autowired ContratsAMCexcluesDao contratsAMCexcluesDao;

  @Autowired ContractDao contractDao;

  @Autowired ContractTPService contractTPService;

  @Autowired MongoTemplate template;

  private int nbContratModifie = 0;

  @Autowired CrexProducer crexProducer;

  @Autowired ContractHistoService contractHistoService;

  private static final int HISTORIZATION_BATCH_SIZE = 500;

  private static final int HISTOEXEC_BATCH_SIZE = 50;

  /**
   * @param batchMode RDO ou NO_RDO (par défaut NO_RDO)
   * @param batchId id du batch in RDO mode
   * @param collectionContractName used to force specific contract name in case of RDO
   * @param dateSynchroStr date to restart aggregation from
   * @param listeAMCtoStop list of AMCs to stop de aggregate
   * @param listeAMCforReprise list of AMCs to start back from where they were stopped
   * @param parallelisme niveau de parralélisme pour le mode RDO
   * @param saveBufferSize nombre de contrat inséré par requete pour le mode RDO
   * @param fromDeclaration plage initiale de déclaration à traiter pour le mode RDO
   * @param toDeclaration plage finale de déclaration à traiter pour le mode RDO
   * @param declarationFetchSize taille du cursor pour la requête sur les déclarations (mode RDO)
   * @param contratFetchSize taille du cursor pour la requête sur les contrats (mode RDO)
   * @param contratPartitionSize partition (limite) des contrats à traiter
   * @param partitionSize partition des déclarations à traiter (mode RDO)
   * @param compteRendu compteRendu du crex
   * @param shouldHistorize historize en mode RDO
   * @param declarantsList liste des déclarants en mode RDO
   * @return 0 si ok, -1 si nok
   */
  public int processStart(
      String batchMode,
      String batchId,
      String collectionContractName,
      String dateSynchroStr,
      List<String> listeAMCtoStop,
      List<String> listeAMCforReprise,
      int parallelisme,
      int saveBufferSize,
      long fromDeclaration,
      long toDeclaration,
      int declarationFetchSize,
      int contratFetchSize,
      int contratPartitionSize,
      int partitionSize,
      CompteRenduConsolidationContrat compteRendu,
      boolean shouldHistorize,
      List<String> declarantsList) {
    int processResult = -1;
    newHistoriqueExecutions = new HistoriqueExecutions634();

    try {
      if (BATCH_MODE_RDO.equals(batchMode)) {
        processResult =
            processRDO(
                batchId,
                collectionContractName,
                parallelisme,
                saveBufferSize,
                fromDeclaration,
                toDeclaration,
                declarationFetchSize,
                contratFetchSize,
                contratPartitionSize,
                partitionSize,
                shouldHistorize,
                declarantsList);
      } else if (BATCH_MODE_NO_RDO.equals(batchMode)) {
        DataForJob634 dataForJob634 = new DataForJob634();
        dataForJob634.setCollectionName(collectionContractName);
        dataForJob634.setJddSize(toDeclaration - fromDeclaration);
        dataForJob634.setListeAMCstop(listeAMCtoStop);
        dataForJob634.setListeAMCreprise(listeAMCforReprise);
        dataForJob634.setDateSynchroStr(dateSynchroStr);
        processResult = process(dataForJob634);
      } else {
        throw new IllegalArgumentException(
            String.format(
                "Le paramètre BATCH_MODE doit avoir la valeur %s ou %s, valeur reçue: %s",
                BATCH_MODE_RDO, BATCH_MODE_NO_RDO, batchMode));
      }
    } catch (ExceptionTechnique e) {
      log.error(e.getMessage(), e);
    } catch (Exception e) {
      log.error("Unexpected error while processing batch");
      log.error(
          String.format("Error type : %s | Error message : %S", e.getClass(), e.getMessage()), e);
    } finally {
      compteRendu.addNombreDeclarationTraitee(newHistoriqueExecutions.getNbDeclarationTraitee());
      compteRendu.addNombreDeclarationIgnoree(newHistoriqueExecutions.getNbDeclarationIgnoree());
      compteRendu.addNbContratCree(newHistoriqueExecutions.getNbContratCree());
      compteRendu.addNbContratModifie(nbContratModifie);
      crexProducer.generateCrex(compteRendu);
    }

    return processResult;
  }

  protected void processRecords(DataForJob634 dataForJob634) {
    Declaration declaration = dataForJob634.getDeclaration();
    if (declaration == null) {
      return;
    }

    Integer nombreTotal = declarationDao.countDeclaration(null);
    Integer nombreATraiter = declarationDao.countDeclaration(declaration.get_id());
    String lastDecl = declarationDao.getLastDeclarationId();
    log.info("nombre de déclarations à traiter {} sur {} ", nombreATraiter, nombreTotal);
    newHistoriqueExecutions.setNbDeclarationATraiter(nombreATraiter);
    saveHisto();

    AtomicReference<ContractHistoService.Buffer> previousContract =
        new AtomicReference<>(new ContractHistoService.Buffer(null, null));
    List<ContractTP> listHisto = new ArrayList<>();
    Set<String> alreadyHistorized = new HashSet<>();
    AtomicInteger nbDeclarationTraitee = new AtomicInteger();
    Stream<Declaration> declarationSorted =
        declarationDao.getNextSortedDeclarationsById(declaration.get_id());
    BulkContratTP bulkContratTP = new BulkContratTP();
    declarationSorted.forEach(
        declaration1 -> {
          ContractHistoService.Buffer currentContract =
              contractHistoService.getCurrentContract(
                  previousContract.get(), declaration1, dataForJob634.getCollection());
          currentContract =
              processDeclaration(
                  dataForJob634,
                  declaration1,
                  currentContract,
                  previousContract.get(),
                  bulkContratTP);

          nbDeclarationTraitee.getAndIncrement();
          if (nbDeclarationTraitee.get() > HISTOEXEC_BATCH_SIZE) {
            saveHistoFinished(declaration.get_id());
            nbDeclarationTraitee.set(0);
          }

          if (newHistoriqueExecutions.getNbDeclarationLue() % 10000 == 0) {
            log.info(
                "Nombre de déclarations lues  {} / {}",
                newHistoriqueExecutions.getNbDeclarationLue(),
                newHistoriqueExecutions.getNbDeclarationATraiter());
          }

          previousContract.set(
              handleHistoContract(
                  previousContract.get(),
                  declaration,
                  listHisto,
                  currentContract,
                  alreadyHistorized));
        });
    contractDao.bulkOp(bulkContratTP, dataForJob634.getCollection());
    saveHistoFinished(lastDecl);

    if (!previousContract.get().isCreated() && previousContract.get().isUpdated()) {
      stopWatch.start();
      listHisto.add(previousContract.get().getContract());
      contractHistoService.saveContractsToElastic(listHisto);
      registerTime("saveToElastic");
    }
  }

  private void saveHisto() {
    stopWatch.start();
    historiqueExecutionsDao.save(newHistoriqueExecutions);
    registerTime("Upsert historiqueExecutionsDao");
  }

  private void saveHistoFinished(String id) {
    newHistoriqueExecutions.setIdDerniereDeclarationTraitee(id);
    saveHisto();
  }

  private ContractHistoService.Buffer handleHistoContract(
      ContractHistoService.Buffer previousContract,
      Declaration declaration,
      List<ContractTP> listHisto,
      ContractHistoService.Buffer currentContract,
      Set<String> alreadyHistorized) {
    if (!contractHistoService.isSameContractFlux(previousContract, declaration)) {
      String contractKey = contractHistoService.getHistoKey(declaration);
      if (!previousContract.isCreated()
          && previousContract.isUpdated()
          && !alreadyHistorized.contains(contractKey)) {
        listHisto.add(previousContract.getContract());
        alreadyHistorized.add(contractKey);
        if (listHisto.size() > HISTORIZATION_BATCH_SIZE) {
          stopWatch.start();
          contractHistoService.saveContractsToElastic(listHisto);
          listHisto.clear();
          registerTime("saveToElastic");
        }
      }
      previousContract = currentContract;
    }
    return previousContract;
  }

  private ContractHistoService.Buffer processDeclaration(
      DataForJob634 dataForJob634,
      Declaration declaration,
      ContractHistoService.Buffer currentContract,
      ContractHistoService.Buffer previousContract,
      BulkContratTP bulkContratTP) {
    if (!dataForJob634.getListeAMCstop().contains(declaration.getIdDeclarant())) {
      stopWatch.start();
      if (bulkContratTP.contractIds.contains(
          declaration.getIdDeclarant()
              + declaration.getContrat().getNumero()
              + declaration.getContrat().getNumeroAdherent())) {
        contractDao.bulkOp(bulkContratTP, dataForJob634.getCollection());
        bulkContratTP.reinit();
      }
      int result =
          contractTPService.processDeclarationJob(
              declaration, dataForJob634.getCollection(), bulkContratTP);
      registerTime("processDeclaration");
      if (result != -2) {
        newHistoriqueExecutions.incNbDeclarationTraitee(1);
        if (result == 1) {
          newHistoriqueExecutions.incNbContratCree(1);
          currentContract =
              contractHistoService.getCurrentContract(
                  previousContract, declaration, dataForJob634.getCollection());
          currentContract.setCreated(true);
        } else if (result == 0) {
          nbContratModifie++;
          currentContract.setUpdated(true);
        } else if (result == -1) {
          newHistoriqueExecutions.incNbContratSupprime(1);
        }
        if (bulkContratTP.compteur > 100) {
          contractDao.bulkOp(bulkContratTP, dataForJob634.getCollection());
          bulkContratTP.reinit();
        }
      } else {
        log.error("Erreur lors de la consolidation de la déclaration {}", declaration.get_id());
        // BLUE-5078
        // non bloquant sur erreur de consolidation
        newHistoriqueExecutions.incNbDeclarationErreur(1);
      }
    } else {
      newHistoriqueExecutions.setNbDeclarationIgnoree(
          newHistoriqueExecutions.getNbDeclarationIgnoree() + 1);
    }
    return currentContract;
  }

  protected void fillLastIdProcessedAndDeclaration(DataForJob634 dataForJob634) {
    String lastIdProcessed;
    Declaration declaration;
    registerTime("Init");
    if (dataForJob634.getLastExecution() != null
        && StringUtils.isBlank(dataForJob634.getDateSynchroStr())) {
      // il y a une précédente exécution, on trouve le prochaine déclaration a traiter
      // en fonction du dernier id traité
      lastIdProcessed = dataForJob634.getLastExecution().getIdDerniereDeclarationTraitee();
      log.info("Last ID : {}", lastIdProcessed);
      declaration = declarationDao.getNextDeclarationById(lastIdProcessed);
    } else if (dataForJob634.getLastExecution() == null
        && StringUtils.isNotBlank(dataForJob634.getDateSynchroStr())) {
      // C'est la 1ere exécution, on trouve la 1ere déclaration a traiter et calcul un
      // id "précédent"
      DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
      LocalDate dateSynchroLD;
      try {
        dateSynchroLD = LocalDate.parse(dataForJob634.getDateSynchroStr(), dateFormat);
      } catch (DateTimeParseException e) {
        throw new ExceptionTechnique(
            String.format(
                "Error while parsing dateSynchro : %s", dataForJob634.getDateSynchroStr()),
            e);
      }
      log.info("From date : {}", dataForJob634.getDateSynchroStr());
      stopWatch.start();
      declaration = declarationDao.getNextDeclarationByDateEffet(dateSynchroLD);
      registerTime("Read declarations");
      // get first ID to process (or last id available otherwise -> lastIdProcessed)
      if (declaration != null && StringUtils.isNotBlank(declaration.get_id())) {
        String firstId = declaration.get_id();
        BigInteger bi = new BigInteger(firstId, HEXADECIMAL);
        lastIdProcessed = (bi.subtract(BigInteger.valueOf(1))).toString(HEXADECIMAL);
      } else {
        stopWatch.start();
        lastIdProcessed = declarationDao.getLastDeclarationId();
        registerTime("Read declarations");
      }
    } else {
      String message;
      if (dataForJob634.getLastExecution() != null) {
        message = "dateSynchro must be used at first start only";
      } else {
        message = "dateSynchro must be used at first start";
      }
      throw new ExceptionTechnique(message);
    }
    dataForJob634.setDeclaration(declaration);
    dataForJob634.setLastIdProcessed(lastIdProcessed);
    // on sauvegarde un nouvel histo exec avec le dernier id traité (ou calcul
    // équivalent)
    stopWatch.start();
    newHistoriqueExecutions.setIdDerniereDeclarationTraitee(dataForJob634.getLastIdProcessed());
    historiqueExecutionsDao.save(newHistoriqueExecutions);
  }

  protected void manageAMCReprise(DataForJob634 dataForJob634) {
    stopWatch.start();
    ContratsAMCexclues contratsAMCexclues =
        contratsAMCexcluesDao.getContratsAmcExclues(
            CONTRATS_AMCEXCLUES + dataForJob634.getCollectionName());
    contratsAMCexclues =
        completeListeAMC(
            contratsAMCexclues,
            dataForJob634.getListeAMCstop(),
            dataForJob634.getListeAMCreprise(),
            dataForJob634.getLastIdProcessed());
    dataForJob634.setContratsAMCexclues(contratsAMCexclues);
    dataForJob634.setListeAMCstop(extractAMC(contratsAMCexclues, false));
    dataForJob634.setListeAMCreprise(extractAMC(contratsAMCexclues, true));
    contratsAMCexcluesDao.upsert(
        contratsAMCexclues, CONTRATS_AMCEXCLUES + dataForJob634.getCollectionName());
    registerTime(UPSERT_CONTRATS_AM_CEXCLUES_DAO);
    if (CollectionUtils.isNotEmpty(dataForJob634.getListeAMCreprise())) {
      for (String amcReprise : dataForJob634.getListeAMCreprise()) {
        String lastIdProcessedReprise =
            getLastIdForAmc(dataForJob634.getContratsAMCexclues(), amcReprise);
        BigInteger lastIdProcessedRepriseBI = new BigInteger(lastIdProcessedReprise, HEXADECIMAL);
        BigInteger lastIdProcessedBI =
            new BigInteger(dataForJob634.getLastIdProcessed(), HEXADECIMAL);
        while (lastIdProcessedRepriseBI.compareTo(lastIdProcessedBI) < 0) {
          Declaration declarationReprise =
              declarationDao.getNextDeclarationByIdAndAmc(lastIdProcessedReprise, amcReprise);
          if (declarationReprise == null) {
            break;
          }

          int result =
              contractTPService.processDeclaration(
                  declarationReprise, dataForJob634.getCollection());
          if (result != -2) {
            newHistoriqueExecutions.setNbDeclarationTraitee(
                newHistoriqueExecutions.getNbDeclarationTraitee() + 1);
            if (result == 1) {
              newHistoriqueExecutions.setNbContratCree(
                  newHistoriqueExecutions.getNbContratCree() + 1);
            } else if (result == 0) {
              nbContratModifie++;
            }
            updateAMCinListe(
                dataForJob634.getContratsAMCexclues(), amcReprise, declarationReprise.get_id());
            stopWatch.start();
            contratsAMCexcluesDao.upsert(
                dataForJob634.getContratsAMCexclues(),
                CONTRATS_AMCEXCLUES + dataForJob634.getCollectionName());
            registerTime(UPSERT_CONTRATS_AM_CEXCLUES_DAO);
            stopWatch.start();
            historiqueExecutionsDao.save(newHistoriqueExecutions);
            registerTime("Upsert historiqueExecutionsDao");
            lastIdProcessedReprise = declarationReprise.get_id();
            lastIdProcessedRepriseBI = new BigInteger(declarationReprise.get_id(), HEXADECIMAL);
          } else {
            log.error(
                "Erreur lors de la reprise de l'amc {}, déclaration {}",
                amcReprise,
                declarationReprise.get_id());
            // BLUE-5078
            // non bloquant sur erreur de consolidation
            newHistoriqueExecutions.incNbDeclarationErreur(1);
          }
        }

        // fin de reprise pour l'amc, on peut la supprimer de la collection
        // contrats-AMCexclues
        removeAmcFromList(dataForJob634.getContratsAMCexclues(), amcReprise);
        stopWatch.start();
        contratsAMCexcluesDao.upsert(
            dataForJob634.getContratsAMCexclues(),
            CONTRATS_AMCEXCLUES + dataForJob634.getCollectionName());
        registerTime(UPSERT_CONTRATS_AM_CEXCLUES_DAO);
      }
    }
  }

  private List<String> extractAMC(ContratsAMCexclues contratsAMCexclues, boolean reprise) {
    List<String> listeAmc = new ArrayList<>();
    if (contratsAMCexclues != null) {
      List<AmcExclues> amcExcluesList = contratsAMCexclues.getListeAMC();
      for (AmcExclues amcExclues : amcExcluesList) {
        if (reprise == amcExclues.isReprise()) {
          listeAmc.add(amcExclues.getIdDeclarant());
          if (reprise) {
            log.info("AMC {} will restart", amcExclues.getIdDeclarant());
          } else {
            log.info("AMC {} stopped", amcExclues.getIdDeclarant());
          }
        }
      }
    }
    return listeAmc;
  }

  private void removeAmcFromList(ContratsAMCexclues contratsAMCexclues, String amc) {
    if (contratsAMCexclues != null) {
      List<AmcExclues> amcExcluesList = contratsAMCexclues.getListeAMC();
      for (AmcExclues amcExclues : amcExcluesList) {
        if (amc.equals(amcExclues.getIdDeclarant())) {
          amcExcluesList.remove(amcExclues);
          break;
        }
      }
    }
  }

  private ContratsAMCexclues completeListeAMC(
      ContratsAMCexclues contratsAMCexclues,
      List<String> listeAMCtoStop,
      List<String> listeAMCforReprise,
      String idDerniereDeclaration) {
    List<AmcExclues> amcExcluesList = new ArrayList<>();
    if (contratsAMCexclues != null
        && CollectionUtils.isNotEmpty(contratsAMCexclues.getListeAMC())) {
      amcExcluesList = contratsAMCexclues.getListeAMC();
      for (AmcExclues amcExclues : amcExcluesList) {
        if (StringUtils.isBlank(amcExclues.getIdDerniereDeclaration())) {
          amcExclues.setIdDerniereDeclaration(idDerniereDeclaration);
        }
      }
    } else {
      contratsAMCexclues = new ContratsAMCexclues();
      contratsAMCexclues.setListeAMC(amcExcluesList);
    }
    for (String amcReprise : listeAMCforReprise) {
      if (StringUtils.isNotBlank(amcReprise)) {
        amcReprise = amcReprise.trim();
        for (AmcExclues amcExclues : amcExcluesList) {
          if (amcReprise.equals(amcExclues.getIdDeclarant())) {
            amcExclues.setReprise(true);
            break;
          }
        }
      }
    }
    for (String amcStop : listeAMCtoStop) {
      if (StringUtils.isNotBlank(amcStop)) {
        boolean found = false;
        amcStop = amcStop.trim();
        for (AmcExclues amcExclues : amcExcluesList) {
          if (amcStop.equals(amcExclues.getIdDeclarant())) {
            found = true;
            amcExclues.setReprise(false);
            break;
          }
        }
        if (!found) {
          AmcExclues newAMC = new AmcExclues();
          newAMC.setIdDeclarant(amcStop);
          newAMC.setIdDerniereDeclaration(idDerniereDeclaration);
          newAMC.setReprise(false);
          amcExcluesList.add(newAMC);
        }
      }
    }
    return contratsAMCexclues;
  }

  private void updateAMCinListe(
      ContratsAMCexclues contratsAMCexclues, String amc, String idDeclaration) {
    for (AmcExclues amcExclues : contratsAMCexclues.getListeAMC()) {
      if (amc.equals(amcExclues.getIdDeclarant())) {
        amcExclues.setIdDerniereDeclaration(idDeclaration);
        break;
      }
    }
  }

  private String getLastIdForAmc(ContratsAMCexclues contratsAMCexclues, String amc) {
    for (AmcExclues amcExclues : contratsAMCexclues.getListeAMC()) {
      if (amc.equals(amcExclues.getIdDeclarant())) {
        return amcExclues.getIdDerniereDeclaration();
      }
    }
    return "0";
  }

  /**
   * @param batchId ID du batch pour gérer les exécutions parallèles
   * @param collectionContractName used to force specific contract name in case of RDO
   * @return errorCode
   */
  private int processRDO(
      String batchId,
      String collectionContractName,
      int parallelisme,
      int saveBufferSize,
      long fromDeclaration,
      long toDeclaration,
      int declarationFetchSize,
      int contratFetchSize,
      int contratPartitionSize,
      int partitionSize,
      boolean shouldHistorize,
      List<String> declarantsList)
      throws Exception {
    template.setReadPreference(ReadPreference.secondaryPreferred());
    MappingMongoConverter conv = (MappingMongoConverter) template.getConverter();
    // tell mongodb to use the custom converters
    MongoCustomConversions customConversions =
        new MongoCustomConversions(
            List.of(
                // writing converter, reader converter
                new DeclarationKeyReaderConverter()));
    conv.setCustomConversions(customConversions);
    conv.afterPropertiesSet();
    newHistoriqueExecutions.clear();
    log.info("Début du batch consolidation contract");
    mainWatch.start();
    computeRange(
        batchId,
        parallelisme,
        saveBufferSize,
        fromDeclaration,
        toDeclaration,
        declarationFetchSize,
        contratFetchSize,
        contratPartitionSize,
        collectionContractName,
        partitionSize,
        shouldHistorize,
        declarantsList);
    mainWatch.stop();
    newHistoriqueExecutions.log();
    this.log();
    return 0;
  }

  /**
   * @param parallelisme int
   * @param saveBufferSize int
   * @param contratPartitionSize int
   * @param contractCollection String
   * @param partitionSize int
   */
  private void computeRange(
      String batchId,
      int parallelisme,
      int saveBufferSize,
      long fromDeclaration,
      long toDeclaration,
      int declarationFetchSize,
      int contratFetchSize,
      int contratPartitionSize,
      String contractCollection,
      int partitionSize,
      boolean shouldHistorize,
      List<String> declarantsList)
      throws Exception {
    SimpleDateFormat readerConverterFormat = DateUtils.getReaderConverterFormat();
    newHistoriqueExecutions.setBatch(getBatchNumber() + contractCollection + "-" + batchId);
    newHistoriqueExecutions.setDateExecution(Date.from(Instant.now()));
    HistoriqueExecutions634 lastExecution =
        historiqueExecutionsDao.getLastExecution(
            newHistoriqueExecutions.getBatch(), HistoriqueExecutions634.class);
    if (lastExecution != null) {
      if (log.isInfoEnabled() && lastExecution.getDateCheckpoint() != null) {
        log.info(
            "Le traitement a été relancé, on reprend à partir de l'ancien checkpoint daté du {} à la position {}",
            readerConverterFormat.format(lastExecution.getDateCheckpoint()),
            lastExecution.getPositionCheckpoint());
      }
      newHistoriqueExecutions = new HistoriqueExecutions634(lastExecution);
    } else {
      if (CollectionUtils.isEmpty(declarantsList)) {
        newHistoriqueExecutions.setNbDeclarationsInitial(
            template
                .getDb()
                .runCommand(
                    new Document(
                        "count",
                        com.cegedim.next.serviceeligibility.core.utils.Constants
                            .DECLARATION_COLLECTION))
                .getInteger("n"));
      } else {
        Criteria criteria =
            Criteria.where(com.cegedim.next.serviceeligibility.core.utils.Constants.ID_DECLARANT)
                .in(declarantsList);
        newHistoriqueExecutions.setNbDeclarationsInitial(
            template.count(
                new Query().addCriteria(criteria),
                Declaration.class,
                com.cegedim.next.serviceeligibility.core.utils.Constants.DECLARATION_COLLECTION));
      }

      borneTravail(fromDeclaration, toDeclaration);
      log.info(
          "Borne de travail: {} à {}",
          newHistoriqueExecutions.getFromDeclaration(),
          newHistoriqueExecutions.getToDeclaration());
    }
    historiqueExecutionsDao.save(newHistoriqueExecutions);
    List<ProcessorPartition> processedPartitions = Collections.synchronizedList(new ArrayList<>());

    ProcessorDeclaration processor =
        new ProcessorDeclaration(
            processedPartitions,
            newHistoriqueExecutions,
            contractDao,
            template,
            contractTPService,
            stepsDurations,
            saveBufferSize,
            declarationFetchSize,
            contratFetchSize,
            contratPartitionSize,
            contractCollection,
            parallelisme,
            partitionSize,
            shouldHistorize,
            contractHistoService,
            declarantsList,
            historiqueExecutionsDao);

    log.info("Fin du traitement des partitions, extinction du pool");
    // Write the number of results generated by each task to the console.
    processor.process();
  }

  private void borneTravail(long fromDeclaration, long toDeclaration) {
    if (fromDeclaration > 0) {
      log.info(
          "Le traitement ne démarre pas au début de la collection, on se place donc au début du premier contrat après l'index fourni");
      Query rangeQuery =
          ConsolidationJobUtil.includeDeclarationIndexFields(
              new Query()
                  .with(DeclarationCallable.buildDeclarationIndexSort())
                  .skip(fromDeclaration)
                  .limit(ConsolidationJobUtil.OFFSET_LIMIT),
              null);
      List<DeclarationKey> keys =
          template.find(
              rangeQuery,
              DeclarationKey.class,
              com.cegedim.next.serviceeligibility.core.utils.Constants.DECLARATION_COLLECTION);
      int offset = 1;
      DeclarationKey key = keys.get(0);
      while (offset < keys.size() && key.isSameContract(keys.get(offset))) {
        offset++;
      }
      if (offset >= ConsolidationJobUtil.OFFSET_LIMIT) {
        throw new IndexOutOfBoundsException(
            "Impossible de générer des partitions consistantes. Chaque partition doit débuter par une première déclaration d'un contrat");
      }
      fromDeclaration = fromDeclaration + offset;
    }
    newHistoriqueExecutions.setFromDeclaration(fromDeclaration);
    if (toDeclaration > 0) {
      log.info(
          "Le traitement ne se termine pas à la fin de la collection, on se place donc au à la fin du dernier contrat après l'index fourni");
      Query rangeQuery =
          ConsolidationJobUtil.includeDeclarationIndexFields(
              new Query()
                  .with(DeclarationCallable.buildDeclarationIndexSort())
                  .skip(toDeclaration)
                  .limit(ConsolidationJobUtil.OFFSET_LIMIT),
              null);
      List<DeclarationKey> keys =
          template.find(
              rangeQuery,
              DeclarationKey.class,
              com.cegedim.next.serviceeligibility.core.utils.Constants.DECLARATION_COLLECTION);
      int offset = 1;
      DeclarationKey key = keys.get(0);
      while (offset < keys.size() && key.isSameContract(keys.get(offset))) {
        offset++;
      }
      if (offset >= ConsolidationJobUtil.OFFSET_LIMIT) {
        throw new IndexOutOfBoundsException(
            "Impossible de générer des partitions consistantes. Chaque partition doit se terminer par une dernière déclaration d'un contrat");
      }
      toDeclaration = toDeclaration + offset;
      newHistoriqueExecutions.setToDeclaration(toDeclaration);
    } else {
      log.info("Le traitement se fait jusqu'à la fin de la collection");
      newHistoriqueExecutions.setToDeclaration(newHistoriqueExecutions.getNbDeclarationsInitial());
    }
  }

  @Override
  protected String getBatchNumber() {
    return "634";
  }

  @Override
  protected String getCollection() {
    return Constants.DEFAULT_CONTRATS_COLLECTION;
  }

  @Override
  protected boolean isAmcReprise() {
    return true;
  }
}
