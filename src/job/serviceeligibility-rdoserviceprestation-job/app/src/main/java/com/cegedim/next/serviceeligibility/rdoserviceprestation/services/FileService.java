package com.cegedim.next.serviceeligibility.rdoserviceprestation.services;

import com.cegedim.next.serviceeligibility.core.dao.TriggerDao;
import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduRdo;
import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduRdoComplet;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.ContratAICommun;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.*;
import com.cegedim.next.serviceeligibility.core.services.ContratAivService;
import com.cegedim.next.serviceeligibility.core.services.bdd.BeneficiaryService;
import com.cegedim.next.serviceeligibility.core.services.bdd.ServicePrestationService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TraceService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerCreationService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionTechnique;
import com.cegedim.next.serviceeligibility.rdoserviceprestation.pojo.RequestFileTask;
import com.cegedim.next.serviceeligibility.rdoserviceprestation.pojo.ResponseFileTask;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileService implements Closeable {

  public static final String META_EXTENSION = ".meta";

  private Set<String> listRdoSp;

  private final TraceService traceService;

  private final FileFlowMetadataService fileFlowMetadataService;

  private final ValidationRdoService validationService;

  private final ContratAivService contratAivService;

  private final ServicePrestationService servicePrestationService;

  private boolean isUnitTest = false;
  private final ReferentielParametrageCarteTPService referentielParametrageCarteTPService;

  private final ParametrageCarteTPService parametrageCarteTPService;

  private final RDOServicePrestationService rdoServicePrestationService;

  private final ExecutorService pool;

  private final EventService eventService;

  public FileService(
      TraceService traceService,
      FileFlowMetadataService fileFlowMetadataService,
      ValidationRdoService validationService,
      ContratAivService contratAivService,
      ServicePrestationService servicePrestationService,
      ReferentielParametrageCarteTPService referentielParametrageCarteTPService,
      BeneficiaryService bservice,
      BeneficiaryKafkaServiceForBatch benefKafkaService,
      TriggerCreationService triggerCreationService,
      TriggerDao triggerDao,
      ParametrageCarteTPService parametrageCarteTPService,
      RDOServicePrestationService rdoServicePrestationService,
      @Value("${THREAD_POOL_SIZE:5}") int threadPoolSize,
      ObjectMapper objectMapper,
      EventService eventService) {
    this.traceService = traceService;
    this.fileFlowMetadataService = fileFlowMetadataService;
    this.validationService = validationService;
    this.contratAivService = contratAivService;
    this.servicePrestationService = servicePrestationService;
    this.referentielParametrageCarteTPService = referentielParametrageCarteTPService;
    this.bservice = bservice;
    this.benefKafkaService = benefKafkaService;
    this.triggerCreationService = triggerCreationService;
    this.triggerDao = triggerDao;
    this.parametrageCarteTPService = parametrageCarteTPService;
    this.pool = Executors.newFixedThreadPool(threadPoolSize);
    this.rdoServicePrestationService = rdoServicePrestationService;
    this.objectMapper = objectMapper;
    this.eventService = eventService;
  }

  @ContinueSpan(log = "setIsUnitTest")
  public void setIsUnitTest(boolean b) {
    isUnitTest = b;
  }

  private final BeneficiaryService bservice;

  private final BeneficiaryKafkaServiceForBatch benefKafkaService;

  private final Logger logger = LoggerFactory.getLogger(FileService.class);

  @Value("${INPUT_FOLDER:/tmp/RDO_SP/}")
  private String rdoSpFolder;

  @Value("${ARL_FOLDER:/tmp/ARL_RDO_SP/}")
  private String arlFolder;

  @Value("${CONTROL_META:true}")
  private boolean controlMeta;

  private final TriggerCreationService triggerCreationService;

  private final TriggerDao triggerDao;

  private final ObjectMapper objectMapper;

  @ContinueSpan(log = "initFolder")
  public Set<String> initFolder(String folder) {
    Set<String> result = null;

    try (Stream<Path> stream = Files.walk(Paths.get(folder), 1)) {
      result =
          stream
              .filter(file -> !Files.isDirectory(file))
              .map(Path::getFileName)
              .map(Path::toString)
              .collect(Collectors.toSet());
    } catch (IOException e) {
      logger.error("could not list files in folder {} error:{}", folder, e.getMessage());
    }
    if (result == null) {
      logger.error("{} is Empty", folder);
    }

    return result;
  }

  @ContinueSpan(log = "init")
  public boolean init() {
    logger.info("Init folder : {}", rdoSpFolder);
    listRdoSp = initFolder(rdoSpFolder);

    return listRdoSp != null;
  }

  public boolean processFolderV5(String now, CompteRenduRdo compteRendu) {
    return processFolder(now, compteRendu, Constants.CONTRACT_VERSION_V5);
  }

  public boolean processFolder(String now, CompteRenduRdo compteRendu, String version) {
    if (CollectionUtils.isEmpty(listRdoSp)) {
      return false;
    }
    logger.info("Folder {} contains {} files", rdoSpFolder, listRdoSp.size());

    // compte rendu csv
    List<CompteRenduRdoComplet> rdoReportitemList = new ArrayList<>();

    List<ProcessFileTask> processFileTasks = new ArrayList<>();
    Class<? extends ContratAICommun> contratVersion = getContratVersion(version);
    boolean generationDroit = parametrageCarteTPService.existParametrageCarteTPActif();
    for (String shortFileName : listRdoSp) {
      String fileName = rdoSpFolder + File.separator + shortFileName;
      if (!fileName.endsWith(META_EXTENSION)) {
        logger.info("processing file {}", fileName);
        if (listRdoSp.contains(shortFileName + META_EXTENSION) || !controlMeta) {
          RequestFileTask requestFileTask =
              new RequestFileTask(
                  shortFileName,
                  fileName,
                  contratVersion,
                  generationDroit,
                  compteRendu,
                  arlFolder,
                  now,
                  isUnitTest,
                  controlMeta);
          processFileTasks.add(
              new ProcessFileTask(
                  fileFlowMetadataService,
                  traceService,
                  validationService,
                  contratAivService,
                  servicePrestationService,
                  referentielParametrageCarteTPService,
                  bservice,
                  benefKafkaService,
                  triggerCreationService,
                  triggerDao,
                  requestFileTask,
                  rdoReportitemList,
                  rdoServicePrestationService,
                  objectMapper,
                  eventService));
        } else {
          logger.warn("file {} ignored : meta is missing", fileName);
        }
      }
    }
    AtomicBoolean problemWhileProcessing = new AtomicBoolean(false);
    try {
      List<Future<ResponseFileTask>> results = null;
      results = pool.invokeAll(processFileTasks);
      results.forEach(
          booleanFuture -> {
            try {
              ResponseFileTask responseFileTask = booleanFuture.get();
              logger.info(
                  "Fichier {} retour {}",
                  responseFileTask.getFileName(),
                  responseFileTask.isFoundTechnicalIssue());
              if (responseFileTask.isFoundTechnicalIssue()) {
                // au moins des fichiers en erreur
                problemWhileProcessing.set(true);
              }
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
              logger.error(e.getMessage(), e);
              throw new ExceptionTechnique(e.getMessage(), e);
            } catch (ExecutionException e) {
              logger.error(e.getMessage(), e);
              throw new ExceptionTechnique(e.getMessage(), e);
            }
          });

      return problemWhileProcessing.get();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ExceptionTechnique(e.getMessage(), e);
    }
  }

  private Class<? extends ContratAICommun> getContratVersion(String version) {
    if (Constants.CONTRACT_VERSION_V5.equals(version)) {
      return ContratAIV5.class;
    } else {
      return ContratAIV6.class;
    }
  }

  @Override
  public void close() {
    pool.shutdown();
  }
}
