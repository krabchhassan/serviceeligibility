package com.cegedim.next.serviceeligibility.purgehistoconsos;

import static com.cegedim.next.serviceeligibility.purgehistoconsos.constants.Constants.PROCESSED_WITH_ERRORS;

import com.cegedim.common.omu.helper.OmuHelper;
import com.cegedim.common.omu.helper.exception.SecuredAnalyzerException;
import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduPurgeHistoConsos;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import com.cegedim.next.serviceeligibility.purgehistoconsos.services.Processor;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandLineEntryPoint implements ApplicationRunner {

  private final Processor processor;
  private final CrexProducer crexProducer;
  private final Tracer tracer;
  private final OmuHelper omuHelper;

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

  private int process(ApplicationArguments args) {
    CompteRenduPurgeHistoConsos compteRenduPurgeHistoConsos = new CompteRenduPurgeHistoConsos();

    OmuCommand omuCommand = new OmuCommand();
    try {
      omuHelper.parseArgs(omuCommand, args.getSourceArgs());

      int days =
          StringUtils.isEmpty(omuCommand.getDays()) ? 365 : Integer.parseInt(omuCommand.getDays());

      int processReturnCode = processor.calcul(compteRenduPurgeHistoConsos, days);

      crexProducer.generateCrex(compteRenduPurgeHistoConsos);

      log.info("Batch fini, code de retour : {}", processReturnCode);
      return processReturnCode;
    } catch (SecuredAnalyzerException | NumberFormatException e) {
      log.error(e.getMessage(), e);
      return PROCESSED_WITH_ERRORS;
    }
  }
}
