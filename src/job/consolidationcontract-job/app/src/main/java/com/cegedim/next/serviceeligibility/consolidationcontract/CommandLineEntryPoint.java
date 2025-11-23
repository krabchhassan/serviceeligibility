package com.cegedim.next.serviceeligibility.consolidationcontract;

import static com.cegedim.next.serviceeligibility.consolidationcontract.constants.Constants.*;

import com.cegedim.common.omu.helper.OmuHelper;
import com.cegedim.common.omu.helper.exception.SecuredAnalyzerException;
import com.cegedim.next.serviceeligibility.consolidationcontract.services.Engine;
import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduConsolidationContrat;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import com.cegedim.next.serviceeligibility.core.utils.Util;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CommandLineEntryPoint implements ApplicationRunner {
  public static final int INVALID_ARGUMENT = -1;

  private final OmuHelper omuHelper;

  private final CrexProducer crexProducer;

  private final Engine engine;

  private final Tracer tracer;

  private String collectionContractName;
  private String dateSynchro;
  private List<String> listeAmcStop;
  private List<String> listeAmcReprise;
  private int parallelisme;
  private long fromDeclaration;
  private long toDeclaration;
  private int declarationFetchSize;
  private int contratFetchSize;
  private int contratPartitionSize;
  private int partitionSize;
  private int saveBufferSize;
  private String batchMode;
  private String batchId;
  private boolean shouldHistorize;
  private List<String> declarantsList;

  private void showLogs(String message) {
    log.debug(message);
  }

  @Value("${JOB_SPAN_NAME:default_span}")
  private String spanName;

  @Override
  public void run(ApplicationArguments args) {
    Span newSpan = tracer.nextSpan().name(spanName).start();
    try (Tracer.SpanInScope spanInScope = tracer.withSpan(newSpan.start())) {
      this.process(args);
    } finally {
      newSpan.end();
    }
  }

  private void process(ApplicationArguments args) {
    CompteRenduConsolidationContrat compteRendu = new CompteRenduConsolidationContrat();
    int processReturnCode = readParameters(args);

    if (-1 != processReturnCode) {
      processReturnCode =
          engine.processStart(
              batchMode,
              batchId,
              collectionContractName,
              dateSynchro,
              listeAmcStop,
              listeAmcReprise,
              parallelisme,
              saveBufferSize,
              fromDeclaration,
              toDeclaration,
              declarationFetchSize,
              contratFetchSize,
              contratPartitionSize,
              partitionSize,
              compteRendu,
              shouldHistorize,
              declarantsList);
    } else {
      crexProducer.generateCrex(compteRendu);
    }

    showLogs(String.format("Batch fini, code de retour : %s", processReturnCode));
    System.exit(processReturnCode);
  }

  public int readParameters(ApplicationArguments args) {
    OmuCommand arguments = new OmuCommand();

    try {
      showLogs("Lecture des paramètres d'entrée");
      omuHelper.parseArgs(arguments, args.getSourceArgs());
    } catch (SecuredAnalyzerException e) {
      log.error(String.format("Erreur lors du parsing des arguments : %s", e.getMessage()), e);
      return INVALID_ARGUMENT;
    }

    String listeAmcRepriseStr = arguments.getListeAmcReprise();
    String listeAmcStopStr = arguments.getListeAmcStop();
    if (StringUtils.isNotBlank(arguments.getBatchMode())) {
      batchMode = arguments.getBatchMode();
    } else {
      batchMode = BATCH_MODE_NO_RDO;
    }
    dateSynchro = arguments.getDateSynchro();
    listeAmcReprise = Arrays.stream(listeAmcRepriseStr.split(",")).toList();
    listeAmcStop = Arrays.stream(listeAmcStopStr.split(",")).toList();
    collectionContractName = arguments.getCollectionContractName();
    shouldHistorize = Boolean.parseBoolean(arguments.getBatchMode());
    if (StringUtils.isNotBlank(arguments.getParallelisme())) {
      parallelisme = Integer.parseInt(arguments.getParallelisme());
    } else {
      parallelisme = 4;
    }
    if (StringUtils.isNotBlank(arguments.getSaveBufferSize())) {
      saveBufferSize = Integer.parseInt(arguments.getSaveBufferSize());
    } else {
      saveBufferSize = 500;
    }
    if (StringUtils.isNotBlank(arguments.getFromDeclaration())) {
      fromDeclaration = Integer.parseInt(arguments.getFromDeclaration());
    } else {
      fromDeclaration = 0;
    }
    if (StringUtils.isNotBlank(arguments.getToDeclaration())) {
      toDeclaration = Integer.parseInt(arguments.getToDeclaration());
    } else {
      toDeclaration = 0;
    }
    if (StringUtils.isNotBlank(arguments.getContratPartitionSize())) {
      contratPartitionSize = Integer.parseInt(arguments.getContratPartitionSize());
    } else {
      contratPartitionSize = 10000;
    }
    if (StringUtils.isNotBlank(arguments.getDeclarationFetchSize())) {
      declarationFetchSize = Integer.parseInt(arguments.getDeclarationFetchSize());
    } else {
      declarationFetchSize = 0;
    }
    if (StringUtils.isNotBlank(arguments.getContratFetchSize())) {
      contratFetchSize = Integer.parseInt(arguments.getContratFetchSize());
    } else {
      contratFetchSize = 0;
    }
    if (StringUtils.isNotBlank(arguments.getPartitionSize())) {
      partitionSize = Integer.parseInt(arguments.getPartitionSize());
    } else {
      partitionSize = 500000;
    }
    batchId = arguments.getBatchId();
    declarantsList = Util.stringToList(arguments.getDeclarantsList());
    showLogs(
        String.format(
            "batchMode = %s | dateSynchro = %s | listeAmcReprise = %s | listeAmcStop = %s | collectionContractName = %s | parallelisme = %d | saveBufferSize = %d | fromDeclaration = %d | toDeclaration = %d | contratFetchSize=%d | partitionSize=%d | batchId=%s | declarantsList=%s",
            batchMode,
            dateSynchro,
            listeAmcRepriseStr,
            listeAmcStopStr,
            collectionContractName,
            parallelisme,
            saveBufferSize,
            fromDeclaration,
            toDeclaration,
            contratPartitionSize,
            partitionSize,
            batchId,
            declarantsList));
    if (BATCH_MODE_RDO.equals(batchMode)) {
      if (StringUtils.isBlank(collectionContractName)
          || DEFAULT_CONTRATS_COLLECTION.equals(collectionContractName)) {
        log.error(
            "Lorsque le mode --BATCH_MODE vaut {}, le paramètre --COLLECTION_CONTRACT_NAME doit être renseigné ou différent de contrats ",
            BATCH_MODE_RDO);
        return -1;
      }
    }

    return 0;
  }
}
