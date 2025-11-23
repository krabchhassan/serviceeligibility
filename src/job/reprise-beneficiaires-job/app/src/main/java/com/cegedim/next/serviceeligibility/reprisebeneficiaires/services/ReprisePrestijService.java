package com.cegedim.next.serviceeligibility.reprisebeneficiaires.services;

import static com.cegedim.next.serviceeligibility.core.job.utils.Constants.*;

import com.cegedim.next.serviceeligibility.core.kafka.common.KafkaSendingException;
import com.cegedim.next.serviceeligibility.core.kafkabenef.serviceprestation.ProducerBenef;
import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduRepriseBenefs;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.prestij.PrestIJ;
import com.cegedim.next.serviceeligibility.core.services.RestPrestIJService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.reprisebeneficiaires.utils.Utils;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReprisePrestijService {
  @Value("${MAXIMUM_BATCH_SIZE:5000}")
  private int maximumBatchSize;

  @Autowired private MongoTemplate mongoTemplate;

  @Autowired private Utils utils;

  @Autowired private RestPrestIJService restPrestIJService;

  @Autowired private ProducerBenef producerBenef;

  private final Logger logger = LoggerFactory.getLogger(ReprisePrestijService.class);

  public int processPrestij(CompteRenduRepriseBenefs compteRendu) throws InterruptedException {
    int page = 0;
    List<PrestIJ> batchContrats;

    try {
      // Read Mongo as stream or paginated
      do {
        batchContrats = getPrestijs(page);

        if (CollectionUtils.isNotEmpty(batchContrats)) {
          logger.debug(
              String.format(
                  "Retrieving %s - %s PrestIj contracts from Mongo",
                  page * maximumBatchSize, (page + 1) * maximumBatchSize));

          // For each contract...
          for (PrestIJ contrat : batchContrats) {
            // Map beneficiary
            processPrestIJcontrat(compteRendu, contrat);
          }
        }

        page += 1;
      } while (CollectionUtils.isNotEmpty(batchContrats));
    } catch (KafkaSendingException e) {
      logger.error(
          String.format("Erreur lors de l'envoi des bénéficiaires : %s", e.getLocalizedMessage()),
          e);
      return CODE_RETOUR_KAFKA_ERROR;
    }

    return CODE_RETOUR_OK;
  }

  @NewSpan
  public void processPrestIJcontrat(CompteRenduRepriseBenefs compteRendu, PrestIJ contrat)
      throws InterruptedException {
    List<BenefAIV5> benefs = restPrestIJService.prestIJMapping(contrat);

    // Process as consumer-worker
    for (BenefAIV5 benef : benefs) {
      producerBenef.send(benef, Constants.ORIGINE_PREST_IJ);
      compteRendu.addBenefsReemisViaPrestij(1);
    }

    compteRendu.addContratsPrestijRepris(1);
  }

  /**
   * Get 'maximumBatchSize' contracts in servicePrestation collection, paginated
   *
   * @param page The page you're looking for
   * @return A list of up to 'maximumBatchSize' ContratAIV5
   */
  private List<PrestIJ> getPrestijs(int page) {
    return mongoTemplate
        .aggregate(utils.getAggregation(page), PRESTIJ_COLLECTION, PrestIJ.class)
        .getMappedResults();
  }
}
