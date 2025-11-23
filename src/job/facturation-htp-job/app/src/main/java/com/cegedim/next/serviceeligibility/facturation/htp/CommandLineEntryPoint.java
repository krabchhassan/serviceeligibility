package com.cegedim.next.serviceeligibility.facturation.htp;

import com.cegedim.common.omu.helper.OmuHelper;
import com.cegedim.common.omu.helper.exception.SecuredAnalyzerException;
import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduFacturationHTP;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import com.cegedim.next.serviceeligibility.facturation.htp.constants.Constants;
import com.cegedim.next.serviceeligibility.facturation.htp.services.Processor;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import java.io.File;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommandLineEntryPoint implements ApplicationRunner {
  private final OmuHelper omuHelper;

  private final Processor processor;

  private final CrexProducer crexProducer;

  private final Tracer tracer;

  private final String csvDelemiter;

  private final String csvOutputDirectory;

  private final String spanName;

  @Getter private LocalDate dateCalcul;
  private List<String> amcList;

  private final String clientType;

  public CommandLineEntryPoint(
      OmuHelper omuHelper,
      Processor processor,
      CrexProducer crexProducer,
      Tracer tracer,
      @Value("${CSV_DELIMITER}") String csvDelemiter,
      @Value("${CSV_OUTPUT_DIRECTORY}") String csvOutputDirectory,
      @Value("${JOB_SPAN_NAME:default_span}") String spanName,
      @Value("${CLIENT_TYPE:INSURER}") String clientType) {
    this.omuHelper = omuHelper;
    this.processor = processor;
    this.crexProducer = crexProducer;
    this.tracer = tracer;
    this.csvDelemiter = csvDelemiter;
    this.csvOutputDirectory = csvOutputDirectory;
    this.spanName = spanName;
    this.clientType = clientType;
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
    int processReturnCode = Constants.INVALID_ARGUMENT;

    File directory = new File(csvOutputDirectory);
    if (!directory.exists() && !directory.mkdirs()) {
      log.error("Le dossier cible " + csvOutputDirectory + " ne peut pas être créé");
    } else {
      String exportFolder = directory.getAbsolutePath() + File.separator;
      log.info("Dossier d'export " + exportFolder);
      try {
        processReturnCode = readParameters(args);

        CompteRenduFacturationHTP compteRenduFacturationHTP = new CompteRenduFacturationHTP();
        compteRenduFacturationHTP.setDateCalcul(dateCalcul);
        if (processReturnCode != Constants.INVALID_ARGUMENT) {
          if (com.cegedim.next.serviceeligibility.core.utils.Constants.CLIENT_TYPE_INSURER.equals(
              clientType)) {
            processReturnCode =
                processor.calcul(dateCalcul, csvDelemiter, exportFolder, compteRenduFacturationHTP);
          } else {
            processReturnCode =
                processor.calculOTP(
                    dateCalcul,
                    csvDelemiter,
                    exportFolder,
                    compteRenduFacturationHTP,
                    this.amcList);
          }
        }
        crexProducer.generateCrex(compteRenduFacturationHTP);
      } catch (DateTimeParseException | SecuredAnalyzerException | NullPointerException e) {
        log.error(String.format("Erreur lors du parsing des arguments : %s", e.getMessage()));
      }
    }

    log.info("Batch fini, code de retour : {}", processReturnCode);
    return processReturnCode;
  }

  int readParameters(ApplicationArguments args) throws SecuredAnalyzerException {
    OmuCommand arguments = new OmuCommand();

    log.info("Lecture des paramètres d'entrée");
    omuHelper.parseArgs(arguments, args.getSourceArgs());

    int anneeCalcul =
        Strings.isBlank(arguments.getAnneeCalcul())
            ? 0
            : Integer.parseInt(arguments.getAnneeCalcul());
    int moisCalcul =
        Strings.isBlank(arguments.getMoisCalcul())
            ? 0
            : Integer.parseInt(arguments.getMoisCalcul());

    LocalDate currentDate = LocalDate.now();

    // Si aucun mois selectionne on prend le mois precedent de la date courante
    if (moisCalcul <= 0) {
      LocalDate previousMonth = currentDate.minusMonths(1);
      anneeCalcul = previousMonth.getYear();
      moisCalcul = previousMonth.getMonthValue();
    } else if (anneeCalcul <= 0) {
      anneeCalcul = currentDate.getYear();
      // Si mois selectionne apres mois courant alors on prend annee precedente
      if (moisCalcul >= currentDate.getMonth().getValue()) {
        anneeCalcul--;
      }
    }

    this.dateCalcul = YearMonth.of(anneeCalcul, moisCalcul).atEndOfMonth();

    if (com.cegedim.next.serviceeligibility.core.utils.Constants.CLIENT_TYPE_OTP.equals(
        clientType)) {
      String amcListWithCommas = arguments.getAmcList();
      if (StringUtils.isBlank(amcListWithCommas)) {
        log.error(
            "Le paramètre amcList est obligatoire pour un lancement sur un environnement OTP");
        return Constants.INVALID_ARGUMENT;
      }
      this.amcList = Arrays.stream(amcListWithCommas.split(",")).toList();
      log.info("Calcul demandé pour " + dateCalcul + " pour les AMC " + amcListWithCommas);
    } else {
      log.info("Calcul demandé pour " + dateCalcul);
    }
    return 0;
  }
}
