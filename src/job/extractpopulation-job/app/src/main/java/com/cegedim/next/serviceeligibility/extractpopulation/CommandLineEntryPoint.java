package com.cegedim.next.serviceeligibility.extractpopulation;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.common.omu.helper.OmuHelper;
import com.cegedim.common.omu.helper.exception.SecuredAnalyzerException;
import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduExtractionPopulation;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import com.cegedim.next.serviceeligibility.core.utils.InstanceProperties;
import com.cegedim.next.serviceeligibility.extractpopulation.constants.ConstantsExtractPop;
import com.cegedim.next.serviceeligibility.extractpopulation.services.Processor;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import java.io.File;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommandLineEntryPoint implements ApplicationRunner {

  private final Processor processor;
  private final OmuHelper omuHelper;
  private final CrexProducer crexProducer;
  private final Tracer tracer;
  private final String csvDelemiter;
  private final String outputDirectory;
  private final String spanName;
  private String format;
  private final int maxContractsPerFile;

  public CommandLineEntryPoint(
      Processor processor,
      OmuHelper omuHelper,
      CrexProducer crexProducer,
      BeyondPropertiesService beyondPropertiesService,
      Tracer tracer) {
    this.processor = processor;
    this.omuHelper = omuHelper;
    this.crexProducer = crexProducer;
    this.csvDelemiter =
        beyondPropertiesService.getPropertyOrThrowError(InstanceProperties.CSV_DELIMITER);
    this.outputDirectory =
        beyondPropertiesService.getPropertyOrThrowError(InstanceProperties.OUTPUT_DIRECTORY);
    this.spanName =
        beyondPropertiesService
            .getProperty(InstanceProperties.JOB_SPAN_NAME)
            .orElse("default_span");
    this.tracer = tracer;
    this.maxContractsPerFile =
        beyondPropertiesService
            .getIntegerProperty(InstanceProperties.MAX_CONTRACTS_PER_FILE)
            .orElse(1000000);
  }

  @Override
  public void run(ApplicationArguments args) {
    Span newSpan = tracer.nextSpan().name(spanName).start();
    try (Tracer.SpanInScope spanInScope = tracer.withSpan(newSpan.start())) {
      this.process(args);
    } finally {
      newSpan.end();
    }
  }

  private int process(ApplicationArguments args) {
    int processReturnCode = ConstantsExtractPop.INVALID_ARGUMENT;

    File directory = new File(outputDirectory);
    if (!directory.exists() && !directory.mkdirs()) {
      log.error("Le dossier cible {} ne peut pas être créé", outputDirectory);
    } else {
      String exportFolder = directory.getAbsolutePath() + File.separator;
      log.info("Dossier d'export {}", exportFolder);

      processReturnCode = readParameters(args);
      if (processReturnCode != ConstantsExtractPop.INVALID_ARGUMENT) {
        CompteRenduExtractionPopulation compteRenduExtractionPopulation =
            new CompteRenduExtractionPopulation();

        processReturnCode =
            processor.extract(
                this.format,
                csvDelemiter,
                exportFolder,
                compteRenduExtractionPopulation,
                this.maxContractsPerFile);

        crexProducer.generateCrex(compteRenduExtractionPopulation);
      }
    }

    log.info("Batch fini, code de retour : {}", processReturnCode);
    return processReturnCode;
  }

  private int readParameters(ApplicationArguments args) {
    OmuCommand arguments = new OmuCommand();

    try {
      log.info("Lecture du paramètre d'entrée");
      omuHelper.parseArgs(arguments, args.getSourceArgs());
    } catch (SecuredAnalyzerException e) {
      log.error(String.format("Erreur lors du parsing des arguments : %s", e.getMessage()), e);
      return ConstantsExtractPop.INVALID_ARGUMENT;
    }

    this.format = arguments.getFormat();
    if (!isFormatAuthorized()) {
      log.error(
          "Le format de sortie \"{}\" n'est pas pris en charge. (Formats autorisés : CSV, JSON)",
          this.format);
      return ConstantsExtractPop.INVALID_ARGUMENT;
    }

    log.debug("format = {}", this.format);
    return 0;
  }

  private boolean isFormatAuthorized() {
    if (StringUtils.isEmpty(this.format)) {
      return false;
    }
    List<String> authorizedFormats = List.of(ConstantsExtractPop.JSON, ConstantsExtractPop.CSV);
    return authorizedFormats.contains(this.format.toUpperCase());
  }
}
