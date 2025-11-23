package com.cegedim.next.serviceeligibility.reprisebeneficiaires;

import static com.cegedim.next.serviceeligibility.core.job.utils.Constants.*;

import com.cegedim.common.omu.helper.OmuHelper;
import com.cegedim.common.omu.helper.exception.SecuredAnalyzerException;
import com.cegedim.next.serviceeligibility.core.job.parameters.RepriseBeneficiaireParameters;
import com.cegedim.next.serviceeligibility.core.job.utils.ContractType;
import com.cegedim.next.serviceeligibility.reprisebeneficiaires.services.Processor;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CommandLineEntryPoint implements ApplicationRunner {
  private final OmuHelper omuHelper;

  private final Processor processor;

  private final Tracer tracer;

  private final String spanName;

  public CommandLineEntryPoint(
      OmuHelper omuHelper,
      Processor processor,
      Tracer tracer,
      @Value("${JOB_SPAN_NAME:default_span}") String spanName) {
    this.omuHelper = omuHelper;
    this.processor = processor;
    this.tracer = tracer;
    this.spanName = spanName;
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

  private void process(ApplicationArguments args) {
    RepriseBeneficiaireParameters parameters = new RepriseBeneficiaireParameters();
    readParameters(args, parameters);

    try {
      if (CollectionUtils.isNotEmpty(parameters.getContractTypes())) {
        processor.process(parameters);
      } else {
        log.error(
            "Erreur lors de la lecture des arguments : Aucun argument valide trouvé. Les entrées acceptées sont HTP, TP et PRESTIJ");
        parameters.setCodeRetour(CODE_RETOUR_BAD_REQUEST);
      }
    } catch (Exception e) {
      parameters.setCodeRetour(CODE_RETOUR_UNEXPECTED_EXCEPTION);
      log.error(
          "Erreur innatendue lors du traitement : erreur de type {} - {}",
          e.getClass(),
          e.getMessage());
    }

    log.info("Batch fini, code de retour : {}", parameters.getCodeRetour());
    System.exit(parameters.getCodeRetour());
  }

  public void readParameters(ApplicationArguments args, RepriseBeneficiaireParameters parameters) {
    OmuCommand arguments = new OmuCommand();

    try {
      log.debug("Lecture des paramètres d'entrée");
      omuHelper.parseArgs(arguments, args.getSourceArgs());
    } catch (SecuredAnalyzerException e) {
      log.error("Erreur lors du parsing des arguments : {}", e.getMessage());
      parameters.setCodeRetour(CODE_RETOUR_BAD_REQUEST);
      return;
    }

    String contractTypes = arguments.getContractTypes();
    String dateReprise = arguments.getDateReprise();
    log.debug("contractTypes = {} | dateReprise = {}", contractTypes, dateReprise);

    // Ajout des contractTypes dans les paramètres après validation
    if (StringUtils.isNotEmpty(contractTypes)) {
      String[] array = contractTypes.trim().split(",");

      for (String entry : array) {
        if (EnumUtils.isValidEnum(ContractType.class, entry)) {
          parameters.getContractTypes().add(ContractType.valueOf(entry));
        }
      }
    }

    // Ajout de dateReprise dans les paramètres après validation
    if (StringUtils.isNotEmpty(dateReprise)) {
      try {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        Date parsedDate = formatter.parse(dateReprise.replaceAll("[\r\n;]", ""));
        parameters.setDateReprise(parsedDate);
      } catch (ParseException e) {
        log.error("Erreur lors du parsing de la date : {}", e.getMessage());
        log.error("Le format de date requis est yyyy-MM-dd");
        parameters.setCodeRetour(CODE_RETOUR_BAD_REQUEST);
      }
    }
  }
}
