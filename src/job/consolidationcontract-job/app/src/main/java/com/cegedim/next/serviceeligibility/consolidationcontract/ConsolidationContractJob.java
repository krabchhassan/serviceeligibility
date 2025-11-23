package com.cegedim.next.serviceeligibility.consolidationcontract;

import com.cegedim.common.omu.helper.OmuHelperImpl;
import com.cegedim.next.serviceeligibility.consolidationcontract.services.Engine;
import com.cegedim.next.serviceeligibility.core.dao.ContractDaoImpl;
import com.cegedim.next.serviceeligibility.core.dao.ContratsAMCexcluesDaoImpl;
import com.cegedim.next.serviceeligibility.core.dao.DeclarationDaoImpl;
import com.cegedim.next.serviceeligibility.core.dao.HistoriqueExecutionsDaoImpl;
import com.cegedim.next.serviceeligibility.core.elast.contract.ElasticHistorisationContractService;
import com.cegedim.next.serviceeligibility.core.elast.contract.HistoriqueContratRepository;
import com.cegedim.next.serviceeligibility.core.elast.contract.IndexHistoContrat;
import com.cegedim.next.serviceeligibility.core.services.contracttp.ContractTPService;
import com.cegedim.next.serviceeligibility.core.services.contracttp.DomaineTPService;
import com.cegedim.next.serviceeligibility.core.services.contracttp.PeriodeDroitTPService;
import com.cegedim.next.serviceeligibility.core.services.contracttp.PeriodeDroitTPStep1;
import com.cegedim.next.serviceeligibility.core.services.contracttp.PeriodeDroitTPStep2;
import com.cegedim.next.serviceeligibility.core.services.contracttp.PeriodeDroitTPStep3;
import com.cegedim.next.serviceeligibility.core.services.contracttp.PeriodeDroitTPStep4;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * Exemple de ligne de commande java -Dspring.profiles.active=perf
 * -Djava.util.concurrent.ForkJoinPool.common.parallelism=4 -jar
 * job-consolidationcontract-job-13.0.0-dev.4.uncommitted+12be3a4.jar --BATCH_MODE=RDO
 * --COLLECTION_CONTRACT_NAME=contratsPerf --DATE_SYNCHRO=01/01/2023 --SAVE_BUFFER_SIZE=500
 * --PARALLELISME=4 --JDD_SIZE=100000
 *
 * <p>Gradle :job-consolidationcontract-job:clean :job-consolidationcontract-job:bootRun
 * --args='--spring.profiles.active=perf --BATCH_MODE=RDO --BATCH_ID=1
 * --COLLECTION_CONTRACT_NAME=contratsPerf --CONTRAT_FETCH_SIZE=1000 --SAVE_BUFFER_SIZE=300
 * --DECLARATION_FETCH_SIZE=1000 --PARALLELISME=4 --PARTITION_SIZE=10000 --FROM_DECLARATION=0
 * --TO_DECLARATION=100000'
 */
@SpringBootApplication
@Slf4j
@EnableElasticsearchRepositories(basePackageClasses = {HistoriqueContratRepository.class})
@Import(
    value = {
      CommandLineEntryPoint.class,
      ContractTPService.class,
      CrexProducer.class,
      OmuHelperImpl.class,
      HistoriqueExecutionsDaoImpl.class,
      ContratsAMCexcluesDaoImpl.class,
      DeclarationDaoImpl.class,
      ContractDaoImpl.class,
      Engine.class,
      ElasticHistorisationContractService.class,
      DomaineTPService.class,
      PeriodeDroitTPService.class,
      PeriodeDroitTPStep1.class,
      PeriodeDroitTPStep2.class,
      PeriodeDroitTPStep3.class,
      PeriodeDroitTPStep4.class,
      IndexHistoContrat.class
    })
public class ConsolidationContractJob {
  public static void main(final String[] args) throws BeansException {
    log.info("Début de l'exécution");
    try {
      new SpringApplication(ConsolidationContractJob.class).run(args);
    } catch (Exception e) {
      log.error("Erreur lors de l'exécution", e);
    } finally {
      log.info("Fin de l'exécution");
    }
  }
}
