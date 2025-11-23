package com.cegedim.next.serviceeligibility.almerys608;

import com.cegedim.common.omu.helper.OmuHelper;
import com.cegedim.common.omu.helper.exception.SecuredAnalyzerException;
import com.cegedim.next.serviceeligibility.almerys608.services.ProcessorAlmerys608;
import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduAlmerys608;
import com.cegedim.next.serviceeligibility.core.model.job.DataForJob608;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandLineEntryPoint implements ApplicationRunner {

  private final ProcessorAlmerys608 processorAlmerys608;
  private final OmuHelper omuHelper;
  private final CrexProducer crexProducer;

  private String couloirClient;

  private static final int INVALID_ARGUMENT = -1;

  @Override
  public void run(ApplicationArguments args) {
    CompteRenduAlmerys608 compteRendu = new CompteRenduAlmerys608();
    int processReturnCode = readParameters(args);
    if (processReturnCode != INVALID_ARGUMENT) {
      DataForJob608 dataForJob608 = new DataForJob608();
      dataForJob608.setJddSize(0);
      dataForJob608.setCouloirClient(couloirClient);
      processReturnCode = processorAlmerys608.process(dataForJob608, compteRendu);
    }
    crexProducer.generateCrex(compteRendu);
    log.info("Batch fini, code de retour : {}", processReturnCode);
    System.exit(processReturnCode);
  }

  public int readParameters(ApplicationArguments args) {
    OmuCommand arguments = new OmuCommand();

    try {
      log.info("Lecture des paramètres d'entrée");
      omuHelper.parseArgs(arguments, args.getSourceArgs());
    } catch (SecuredAnalyzerException e) {
      log.error("Erreur lors du parsing des arguments : {}", e.getMessage(), e);
      return INVALID_ARGUMENT;
    }
    couloirClient = arguments.getCouloirClient();

    return 0;
  }
}
