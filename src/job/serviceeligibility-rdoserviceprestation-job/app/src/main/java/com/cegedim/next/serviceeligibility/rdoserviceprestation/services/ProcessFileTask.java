package com.cegedim.next.serviceeligibility.rdoserviceprestation.services;

import com.cegedim.common.base.pefb.services.MetaService;
import com.cegedim.next.serviceeligibility.core.dao.TriggerDao;
import com.cegedim.next.serviceeligibility.core.kafka.common.KafkaSendingException;
import com.cegedim.next.serviceeligibility.core.mapper.MapperContrat;
import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduRdo;
import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduRdoComplet;
import com.cegedim.next.serviceeligibility.core.model.entity.FileFlowMetadata;
import com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo.ErrorData;
import com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo.TraceServicePrestation;
import com.cegedim.next.serviceeligibility.core.model.enumeration.StatutFileFlowMetaData;
import com.cegedim.next.serviceeligibility.core.model.enumeration.TraceSource;
import com.cegedim.next.serviceeligibility.core.model.kafka.TraceStatus;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.ContratAICommun;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.*;
import com.cegedim.next.serviceeligibility.core.services.bdd.BeneficiaryService;
import com.cegedim.next.serviceeligibility.core.services.bdd.ServicePrestationService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TraceService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.pojo.ContractValidationBean;
import com.cegedim.next.serviceeligibility.core.services.pojo.ErrorValidationBean;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerCreationService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.ContractReceptionEventUtils;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ValidationContractException;
import com.cegedim.next.serviceeligibility.rdoserviceprestation.pojo.FileTaskBean;
import com.cegedim.next.serviceeligibility.rdoserviceprestation.pojo.RequestFileTask;
import com.cegedim.next.serviceeligibility.rdoserviceprestation.pojo.ResponseFileTask;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.Callable;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Slf4j
public class ProcessFileTask implements Callable<ResponseFileTask> {

  private static final String CAN_T_OPEN_OR_WRITE_THE_FILE = "Can't open or write the file {} : {}";

  private final FileFlowMetadataService fileFlowMetadataService;

  private final TraceService traceService;

  private final ValidationRdoService validationService;

  private final ContratAivService contratAivService;

  private final ServicePrestationService servicePrestationService;

  private final TriggerCreationService triggerCreationService;

  private final TriggerDao triggerDao;

  private final BeneficiaryService bservice;

  private final BeneficiaryKafkaServiceForBatch benefKafkaService;

  private final ReferentielParametrageCarteTPService referentielParametrageCarteTPService;

  private final RequestFileTask requestFileTask;

  private final List<CompteRenduRdoComplet> compteRenduRdoCompletList;

  private final RDOServicePrestationService rdoServicePrestationService;

  private final ObjectMapper objectMapper;

  private final EventService eventService;

  public ProcessFileTask(
      FileFlowMetadataService fileFlowMetadataService,
      TraceService traceService,
      ValidationRdoService validationService,
      ContratAivService contratAivService,
      ServicePrestationService servicePrestationService,
      ReferentielParametrageCarteTPService referentielParametrageCarteTPService,
      BeneficiaryService bservice,
      BeneficiaryKafkaServiceForBatch benefKafkaService,
      TriggerCreationService triggerCreationService,
      TriggerDao triggerDao,
      RequestFileTask requestFileTask,
      List<CompteRenduRdoComplet> compteRenduRdoCompletList,
      RDOServicePrestationService rdoServicePrestationService,
      ObjectMapper objectMapper,
      EventService eventService) {
    this.fileFlowMetadataService = fileFlowMetadataService;
    this.traceService = traceService;
    this.requestFileTask = requestFileTask;
    this.validationService = validationService;
    this.contratAivService = contratAivService;
    this.servicePrestationService = servicePrestationService;
    this.triggerCreationService = triggerCreationService;
    this.triggerDao = triggerDao;
    this.bservice = bservice;
    this.benefKafkaService = benefKafkaService;
    this.referentielParametrageCarteTPService = referentielParametrageCarteTPService;
    this.compteRenduRdoCompletList = compteRenduRdoCompletList;
    this.rdoServicePrestationService = rdoServicePrestationService;
    this.objectMapper = objectMapper;
    this.eventService = eventService;
  }

  @Override
  public ResponseFileTask call() {
    boolean foundTechnicalIssue = false;
    int[] compteurs = {0, 0};
    FileTaskBean fileTaskBean = new FileTaskBean();

    // Création du metadata du flux
    FileFlowMetadata metaData =
        fileFlowMetadataService.createFileFlowMetadata(
            requestFileTask.getShortFileName(), "RDOBatch", "V1");
    // Permet la désérialisation d'une localDateTime
    try (FileInputStream input = new FileInputStream(requestFileTask.getFileName())) {
      JSONParser jsonParser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
      JSONArray jsonArray = (JSONArray) jsonParser.parse(input);
      for (int compteur = 0; compteur < jsonArray.size(); compteur++) {
        try {
          manageContrat((JSONObject) jsonArray.get(compteur), fileTaskBean);
        } catch (JsonProcessingException e) {
          log.error(e.getLocalizedMessage(), e);
          fileTaskBean.setNbContratKo(fileTaskBean.getNbContratKo() + 1);
        }
      }

      // if we have some data ( some boolean) we need to move all temp
      // data to the
      // correct collections and delete the file and temp collections
      // update metadata with correct numbers
      log.info(
          "requestFileTask.getShortFileName() {} nbContratKo {} nbContratOk {}",
          requestFileTask.getShortFileName(),
          fileTaskBean.getNbContratKo(),
          fileTaskBean.getNbContratOk());
      metaData.setNbLignesLues(fileTaskBean.getNumeroContrat());
      metaData.setNbLignesIntegrees(fileTaskBean.getNbContratOk());
      metaData.setNbLignesRejetees(fileTaskBean.getNbContratKo());
      metaData.setStatut(StatutFileFlowMetaData.Completed);

      if (fileTaskBean.getFirstTriggerId() != null) {
        compteurs =
            getCounters(requestFileTask.getShortFileName(), fileTaskBean.getFirstTriggerId());
      }

      String arlFileName =
          processArl(
              requestFileTask.getShortFileName(),
              fileTaskBean.getClientBo(),
              requestFileTask.getBatchExecutionTime(),
              null,
              fileTaskBean.getFirstTraceId());
      metaData.setNomFichierARL(arlFileName);

      // compte rendu complet
      CompteRenduRdoComplet compteRenduRdoComplet =
          new CompteRenduRdoComplet(
              requestFileTask.getShortFileName(),
              arlFileName,
              fileTaskBean.getClientBo(),
              requestFileTask.getBatchExecutionTime(),
              String.valueOf(fileTaskBean.getNumeroContrat()),
              String.valueOf(fileTaskBean.getNbContratOk()),
              String.valueOf(fileTaskBean.getNbContratKo()),
              String.valueOf(compteurs[0]),
              String.valueOf(compteurs[1]));
      compteRenduRdoCompletList.add(compteRenduRdoComplet);

    } catch (IOException e) {
      foundTechnicalIssue = true;
      log.error(
          String.format(
              "error processing/deleting file %s Error: %s",
              requestFileTask.getFileName(), e.getLocalizedMessage()),
          e);
      ContractReceptionEventUtils.sendReceptionEventForRdo(
          requestFileTask.getShortFileName(),
          fileTaskBean.getNumeroContrat(),
          e.getLocalizedMessage(),
          eventService,
          traceService);

    } catch (InterruptedException e) {
      log.warn("Interrupted!", e);
      ContractReceptionEventUtils.sendReceptionEventForRdo(
          requestFileTask.getShortFileName(),
          fileTaskBean.getNumeroContrat(),
          e.getLocalizedMessage(),
          eventService,
          traceService);

      // Restore interrupted state...
      Thread.currentThread().interrupt();
    } catch (Exception e) {
      String message =
          "Erreur lors de la déserialisation du contrat n°"
              + fileTaskBean.getNumeroContrat()
              + " : "
              + e.getLocalizedMessage();
      log.error(message, e);

      String arlFileName =
          processArl(
              requestFileTask.getShortFileName(),
              fileTaskBean.getClientBo(),
              requestFileTask.getBatchExecutionTime(),
              message,
              fileTaskBean.getFirstTraceId());
      metaData.setStatut(StatutFileFlowMetaData.Error);
      metaData.setNomFichierARL(arlFileName);
      fileFlowMetadataService.updateFileFlowMetadata(metaData);
      ContractReceptionEventUtils.sendReceptionEventForRdo(
          requestFileTask.getShortFileName(),
          fileTaskBean.getNumeroContrat(),
          message,
          eventService,
          traceService);
    } finally {
      foundTechnicalIssue =
          foundTechnicalIssue
              || finishFileProcess(
                  requestFileTask.getFileName(),
                  requestFileTask.getCompteRenduRdo(),
                  fileTaskBean.getNumeroContrat(),
                  fileTaskBean.getNbContratOk(),
                  fileTaskBean.getNbContratKo(),
                  metaData);
      writeCompleteReportFile(compteRenduRdoCompletList, requestFileTask.getBatchExecutionTime());
    }
    ResponseFileTask responseFileTask = new ResponseFileTask();
    responseFileTask.setFoundTechnicalIssue(foundTechnicalIssue);
    responseFileTask.setFileName(requestFileTask.getShortFileName());
    return responseFileTask;
  }

  private void manageContrat(JSONObject jsonObject, FileTaskBean fileTaskBean)
      throws JsonProcessingException {
    fileTaskBean.incNumeroContrat();
    ContratAICommun contrat =
        objectMapper.readValue(jsonObject.toString(), requestFileTask.getContratVersion());
    if (contrat.getSocieteEmettrice() != null) {
      contrat.setSocieteEmettrice(StringUtils.leftPad(contrat.getSocieteEmettrice(), 10, "0"));
    }
    // create temp trace for prestation and benef
    String traceId =
        traceService.createTrace(
            objectMapper.writeValueAsString(contrat),
            null,
            TraceSource.File,
            TraceStatus.MappingSucceeded,
            requestFileTask.getShortFileName(),
            Constants.CONTRACT_TRACE,
            contrat.getIdDeclarant(),
            contrat.getNumero(),
            contrat.getNumeroAdherent());

    fileTaskBean.setFirstTraceId(
        Optional.ofNullable(fileTaskBean.getFirstTraceId()).orElse(traceId));
    contrat.setTraceId(traceId);

    try {
      fileTaskBean.setClientBo(contrat.getIdDeclarant());
      fileTaskBean.setFirstTriggerId(
          Optional.ofNullable(fileTaskBean.getFirstTriggerId())
              .orElse(validateAndExtractData(contrat, traceId)));
      // counters
      fileTaskBean.incNbContratOk();
      ContractReceptionEventUtils.sendReceptionEvent(contrat, traceId, eventService);
    } catch (InterruptedException e) {
      log.warn("Interrupted!", e);
      ErrorData errorData = new ErrorData();
      errorData.setNumeroPersonne(null);
      errorData.setMessages(List.of(e.getLocalizedMessage()));
      traceService.updateStatusErrorServicePresta(
          traceId,
          TraceStatus.ValidationFailed,
          List.of(errorData),
          fileTaskBean.getNumeroContrat(),
          Constants.CONTRACT_TRACE);
      ContractReceptionEventUtils.sendReceptionEvent(contrat, traceId, eventService);

      // Restore interrupted state...
      Thread.currentThread().interrupt();
    } catch (ValidationContractException e) {
      log.debug(String.format("Contrat invalide : %s", e.getLocalizedMessage()), e);

      Map<String, List<String>> errors = formatErrors(e);
      traceService.updateStatusErrorServicePresta(
          traceId,
          TraceStatus.ValidationFailed,
          extractListFromMap(errors),
          fileTaskBean.getNumeroContrat(),
          Constants.CONTRACT_TRACE);
      fileTaskBean.incNbContratKo();
      ContractReceptionEventUtils.sendReceptionEvent(contrat, traceId, eventService);
    } catch (Exception e) {
      log.error(String.format("Contrat invalide : %s", e.getLocalizedMessage()), e);
      List<ErrorData> errorDataList = new ArrayList<>();
      ErrorData errorData = new ErrorData();
      errorData.setNumeroPersonne(null);
      errorData.setMessages(List.of(e.getLocalizedMessage()));
      errorDataList.add(errorData);
      traceService.updateStatusErrorServicePresta(
          traceId,
          TraceStatus.ValidationFailed,
          errorDataList,
          fileTaskBean.getNumeroContrat(),
          Constants.CONTRACT_TRACE);
      fileTaskBean.incNbContratKo();
      ContractReceptionEventUtils.sendReceptionEvent(contrat, traceId, eventService);
    }
  }

  private boolean finishFileProcess(
      String file,
      CompteRenduRdo compteRendu,
      Long numeroContrat,
      Long nbContratOk,
      Long nbContratKo,
      FileFlowMetadata metaData) {
    boolean foundTechnicalIssue = false;
    // Affichage du compte-rendu batch par fichier
    log.info("Fin de traitement du Fichier {}", file);
    log.info("Nombre de lignes lues : {}", numeroContrat);
    log.info("Nombre de contrats intégrés : {}", nbContratOk);
    log.info("Nombre de contrats rejetés : {}", nbContratKo);

    compteRendu.addListeFichiersValue(file);
    compteRendu.addLignesLues(numeroContrat);
    compteRendu.addContratsIntegres(nbContratOk);
    compteRendu.addContratsRejetes(nbContratKo);

    Path fileToDeletePath = Paths.get(file);
    try {
      log.info("Deleting file {}", file);
      if (!requestFileTask.isUnitTest()) {
        Files.delete(fileToDeletePath);
      }
    } catch (IOException e) {
      log.error("could not delete {}", fileToDeletePath);
      foundTechnicalIssue = true;
    }
    Path metaToDeletePath = Paths.get(file + FileService.META_EXTENSION);
    try {
      log.info("Deleting file {}{}", file, FileService.META_EXTENSION);
      if (!requestFileTask.isUnitTest()) {
        Files.delete(metaToDeletePath);
      }
    } catch (IOException e) {
      if (requestFileTask.isControlMeta()) {
        log.error("could not delete {}", metaToDeletePath);
        foundTechnicalIssue = true;
      }
    }
    metaData.setFinTraitement(LocalDateTime.now(ZoneOffset.UTC));
    fileFlowMetadataService.updateFileFlowMetadata(metaData);

    return foundTechnicalIssue;
  }

  public String validateAndExtractData(ContratAICommun contratAICommun, String traceId)
      throws KafkaSendingException, InterruptedException {
    validationService.validateContrat(contratAICommun, true, new ContractValidationBean());
    validationService.transformeContrat(contratAICommun);
    ContratAIV6 contrat = convertCommunToV6(contratAICommun);

    contrat.setTraceId(traceId);
    contrat.setNomFichierOrigine(requestFileTask.getShortFileName());
    servicePrestationService.generateBeyondId(contrat);
    referentielParametrageCarteTPService.saveReferentielParametrageCarteTP(contrat);

    List<ContratAIV6> generatedContracts = contratAivService.process(contrat);

    if (CollectionUtils.isNotEmpty(generatedContracts)) {
      ContratAIV6 newContrat = generatedContracts.get(0);
      rdoServicePrestationService.upsertRdo(newContrat);
    }

    List<BenefAIV5> benefs =
        bservice.extractBenefFromContrat(contrat.getAssures(), contrat, null, traceId);
    for (BenefAIV5 benef : benefs) {
      benefKafkaService.send(benef);
    }

    traceService.updateStatus(traceId, TraceStatus.SuccesfullyProcessed, Constants.CONTRACT_TRACE);
    String triggerId = null;
    if (requestFileTask.isGenerationDroit()
        && generatedContracts != null
        && generatedContracts.size() == 2) {
      ContratAIV6 newContract = generatedContracts.get(0);
      ContratAIV6 oldContract = generatedContracts.get(1);

      triggerId =
          triggerCreationService.generateTriggersRdoServicePrestation(
              newContract, oldContract, requestFileTask.getShortFileName());
    }
    return triggerId;
  }

  private int[] getCounters(String nomFichier, String firstTriggerId) throws InterruptedException {
    int tryNumber = 150;
    while (triggerDao.isTriggerByFilenameNotProcessed(nomFichier, firstTriggerId)
        && tryNumber > 0) {
      Thread.sleep(2000);
      tryNumber--;
    }

    return triggerDao.getNombreDeclarationForTriggerByFilename(nomFichier);
  }

  private String processArl(
      String fileName,
      String idDeclarant,
      String batchExecutionTime,
      String deserializationError,
      String firstTraceId) {
    log.info("Génération du fichier CSV");
    String fileNameCSV = "ARL_" + fileName + "_" + batchExecutionTime + ".csv";

    File directory = new File(requestFileTask.getArlFolder());
    if (!directory.exists()) {
      directory.mkdirs();
    }
    String filePath = directory.getAbsolutePath() + File.separator + fileNameCSV;
    File fichierCSV = new File(filePath);
    if (!requestFileTask.isUnitTest()) {
      try (FileWriter outputfileCSV = new FileWriter(fichierCSV, StandardCharsets.UTF_8)) {
        // create CSVWriter with ';' as separator
        CSVWriter writer =
            new CSVWriter(
                outputfileCSV,
                ';',
                ICSVWriter.NO_QUOTE_CHARACTER,
                ICSVWriter.DEFAULT_ESCAPE_CHARACTER,
                ICSVWriter.DEFAULT_LINE_END);

        writer.writeAll(getArlContent(fileName, idDeclarant, deserializationError, firstTraceId));
        // closing writer connection
        writer.close();

        File file = new File(filePath);
        String meta =
            MetaService.createMeta(file, "UTF-8", LocalDateTime.now(), "rdoserviceprestation");
        log.debug("meta: {}", meta);
        meta = meta.replace("\"fileName\":\".", "\"fileName\":\"");
        Files.writeString(
            Paths.get(filePath + FileService.META_EXTENSION), meta, StandardOpenOption.CREATE);
      } catch (IOException e) {
        log.error(CAN_T_OPEN_OR_WRITE_THE_FILE, fileNameCSV, e.getMessage());
      }
    }
    return fileNameCSV;
  }

  protected List<String[]> getArlContent(
      String fileName, String idDeclarant, String deserializationError, String firstTraceId) {
    // create a List which contains String array
    List<String[]> data = new ArrayList<>();

    // create header
    data.add(
        new String[] {"idDeclarant", "numeroAdherent", "numeroContrat", "numeroPersonne", "rejet"});

    if (deserializationError != null) {
      data.add(new String[] {idDeclarant, "", "", "", deserializationError});
    } else {
      int page = 0;
      int pageSize = 1;
      Pageable pageable = PageRequest.of(page++, pageSize);
      Page<TraceServicePrestation> traces =
          traceService.getTraceForArl(fileName, firstTraceId, pageable);
      boolean hasNext = true;

      while (hasNext) {
        for (TraceServicePrestation trace : traces) {
          List<ErrorData> errorMessages = trace.getErrorMessages();
          if (!errorMessages.isEmpty()) {
            for (ErrorData errorData : errorMessages) {
              for (String message : errorData.getMessages()) {
                data.add(
                    new String[] {
                      idDeclarant,
                      trace.getNumeroAdherent(),
                      trace.getNumeroContrat(),
                      errorData.getNumeroPersonne() == null ? "" : errorData.getNumeroPersonne(),
                      message
                    });
              }
            }
          }
        }
        pageable = PageRequest.of(page++, pageSize);
        traces = traceService.getTraceForArl(fileName, firstTraceId, pageable);
        if (traces.isEmpty()) {
          hasNext = false;
        }
      }
    }
    return data;
  }

  private String writeCompleteReportFile(
      List<CompteRenduRdoComplet> compteRenduRdoCompletList, String batchExecutionTime) {
    log.info("Génération du fichier de compte-rendu complet");
    String fileNameCSV = "CR_RDO_CONTRATS_HTP_" + batchExecutionTime + ".csv";
    if (CollectionUtils.isNotEmpty(compteRenduRdoCompletList)) {
      File directory = new File(requestFileTask.getArlFolder());
      if (!directory.exists()) {
        directory.mkdirs();
      }
      String filePath = directory.getAbsolutePath() + File.separator + fileNameCSV;
      File fichierCSV = new File(filePath);
      if (!requestFileTask.isUnitTest()) {
        try (FileWriter outputfileCSV = new FileWriter(fichierCSV)) {
          // create CSVWriter with ';' as separator
          CSVWriter writer =
              new CSVWriter(
                  outputfileCSV,
                  ';',
                  ICSVWriter.NO_QUOTE_CHARACTER,
                  ICSVWriter.DEFAULT_ESCAPE_CHARACTER,
                  ICSVWriter.DEFAULT_LINE_END);

          // create a List which contains String array
          List<String[]> data = new ArrayList<>();
          data.add(
              new String[] {
                "Nom du fichier",
                "Nom du fichier d’ARL",
                "idDeclarant",
                "Date du traitement",
                "Nombre de contrats lus",
                "Nombre de contrats intégrés",
                "Nombres de contrats rejetés",
                "Nombre de déclarations générées en ouverture",
                "Nombre de déclarations générées en fermeture"
              });
          buildReportLine(compteRenduRdoCompletList, data);

          writer.writeAll(data);
          // closing writer connection
          writer.close();

          File file = new File(filePath);
          String meta =
              MetaService.createMeta(file, "UTF-8", LocalDateTime.now(), "rdoserviceprestation");
          log.debug("meta: {}", meta);
          meta = meta.replace("\"fileName\":\".", "\"fileName\":\"");
          Files.writeString(
              Paths.get(filePath + FileService.META_EXTENSION), meta, StandardOpenOption.CREATE);
        } catch (IOException e) {
          log.error(CAN_T_OPEN_OR_WRITE_THE_FILE, fileNameCSV, e.getMessage());
        }
      }
    }
    return fileNameCSV;
  }

  protected static void buildReportLine(
      List<CompteRenduRdoComplet> compteRenduRdoCompletList, List<String[]> data) {
    for (CompteRenduRdoComplet compteRenduRdoComplet : compteRenduRdoCompletList) {
      data.add(
          new String[] {
            compteRenduRdoComplet.getNomFichier(),
            compteRenduRdoComplet.getNomFichierARL(),
            compteRenduRdoComplet.getIdDeclarant(),
            compteRenduRdoComplet.getDateTraitement(),
            compteRenduRdoComplet.getNbContratsLus(),
            compteRenduRdoComplet.getNbContratsIntegres(),
            compteRenduRdoComplet.getNbContratsRejetes(),
            compteRenduRdoComplet.getNbDeclarationsOuverture(),
            compteRenduRdoComplet.getNbDeclarationFermeture()
          });
    }
  }

  private ContratAIV6 convertCommunToV6(ContratAICommun contrat) {
    if (contrat instanceof ContratAIV6) {
      return (ContratAIV6) contrat;
    }
    return MapperContrat.mapV5toV6((ContratAIV5) contrat);
  }

  protected List<ErrorData> extractListFromMap(Map<String, List<String>> errors) {
    List<ErrorData> errorDataList = new ArrayList<>();
    for (Map.Entry<String, List<String>> entry : errors.entrySet()) {
      if (Constants.CONTRAT.equals(entry.getKey())) {
        ErrorData errorData = new ErrorData();
        errorData.setNumeroPersonne(null);
        errorData.setMessages(entry.getValue());
        errorDataList.add(errorData);
      } else {
        ErrorData errorData = new ErrorData();
        errorData.setNumeroPersonne(entry.getKey());
        errorData.setMessages(entry.getValue());
        errorDataList.add(errorData);
      }
    }
    return errorDataList;
  }

  protected Map<String, List<String>> formatErrors(ValidationContractException e) {
    Map<String, List<String>> errorsByNumberPerson = new HashMap<>();
    List<String> contractErrors = new ArrayList<>();
    if (e.getMessage() != null) {
      fillMapWithErrors(e, errorsByNumberPerson, contractErrors);
    }
    if (!CollectionUtils.isEmpty(contractErrors)) {
      errorsByNumberPerson.put(Constants.CONTRAT, contractErrors);
    }
    return errorsByNumberPerson;
  }

  private void fillMapWithErrors(
      ValidationContractException e,
      Map<String, List<String>> errorsByNumberPerson,
      List<String> contractErrors) {
    for (ErrorValidationBean errorValidationBean : e.getErrorValidationBeans()) {
      // S'il s'agit d'une erreur assure
      if (Constants.ASSURE.equals(errorValidationBean.getLevel())) {
        errorsByNumberPerson
            .computeIfAbsent(errorValidationBean.getPersonNumber(), key -> new ArrayList<>())
            .add(errorValidationBean.getError());
      } else {
        contractErrors.add(errorValidationBean.getError());
      }
    }
  }
}
