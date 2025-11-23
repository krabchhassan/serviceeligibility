package com.cegedim.next.serviceeligibility.reprisebeneficiaires.services;

import static com.cegedim.next.serviceeligibility.core.job.utils.Constants.*;

import com.cegedim.next.serviceeligibility.core.kafka.common.KafkaSendingException;
import com.cegedim.next.serviceeligibility.core.kafkabenef.serviceprestation.ProducerBenef;
import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduRepriseBenefs;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.PersonService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.reprisebeneficiaires.utils.Utils;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RepriseServicePrestationService {
  @Value("${MAXIMUM_BATCH_SIZE:5000}")
  private int maximumBatchSize;

  private final MongoTemplate mongoTemplate;

  private final Utils utils;

  private final PersonService personService;

  private final ProducerBenef producerBenef;

  private final Logger logger = LoggerFactory.getLogger(RepriseServicePrestationService.class);

  public int processHtp(CompteRenduRepriseBenefs compteRendu) throws InterruptedException {
    int page = 0;
    List<ContratAIV6> batchContrats;

    try {
      // Read Mongo as stream or paginated
      do {
        batchContrats = getServicePrestations(page);

        if (CollectionUtils.isNotEmpty(batchContrats)) {
          logger.info(
              "Retrieving {} - {} HTP contracts from Mongo",
              page * maximumBatchSize,
              (page + 1) * maximumBatchSize);

          // For each contract...
          for (ContratAIV6 contrat : batchContrats) {
            // Extract beneficiary
            processBatchContrat(compteRendu, contrat);
          }
        }

        page += 1;
      } while (CollectionUtils.isNotEmpty(batchContrats));
    } catch (KafkaSendingException e) {
      logger.error("Erreur lors de l'envoi des bénéficiaires : {}", e.getLocalizedMessage());
      return CODE_RETOUR_KAFKA_ERROR;
    }

    return CODE_RETOUR_OK;
  }

  @NewSpan
  public void processBatchContrat(CompteRenduRepriseBenefs compteRendu, ContratAIV6 contrat)
      throws InterruptedException {
    List<BenefAIV5> benefs =
        personService.extractBenefFromContratCommun(contrat.getAssures(), contrat, null, null);

    // Process as consumer-worker
    for (BenefAIV5 benef : benefs) {
      producerBenef.send(benef, Constants.ORIGINE_SERVICE_PRESTATION);
      compteRendu.addBenefsReemisViaHtp(1);
    }

    compteRendu.addContratsHtpRepris(1);
  }

  /**
   * Get 'maximumBatchSize' contracts in servicePrestation collection, paginated
   *
   * @param page The page you're looking for
   * @return A list of up to 'maximumBatchSize' ContratAIV5
   */
  private List<ContratAIV6> getServicePrestations(int page) {
    return mongoTemplate
        .aggregate(utils.getAggregation(page), SERVICE_PRESTATION_COLLECTION, ContratAIV6.class)
        .getMappedResults();
  }
}
