package com.cegedim.next.serviceeligibility.purgeserviceprestationrdo.job.services;

import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduServicePrestationRdoPurge;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import com.cegedim.next.serviceeligibility.purgeserviceprestationrdo.job.constants.ConstantsOmuCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class Processor {

  private final MongoTemplate template;

  private final CrexProducer crexProducer;

  public int processPurge() {
    CompteRenduServicePrestationRdoPurge compteRendu = new CompteRenduServicePrestationRdoPurge();

    int processResult = ConstantsOmuCode.PROCESSED_WITHOUT_ERRORS;
    try {
      long benefKeyCount =
          template.getCollection(Constants.RDO_SERVICE_PRESTATION_COLLECTION).countDocuments();
      dropCollection();
      createCollection();
      compteRendu.addBenefKeyCount(benefKeyCount);
    } catch (Exception e) {
      log.error("Unexpected error while processing batch");
      log.error(
          String.format("Error type : %s | Error message : %S", e.getClass(), e.getMessage()), e);
      processResult = ConstantsOmuCode.PROCESSED_WITH_ERRORS;
    } finally {
      crexProducer.generateCrex(compteRendu);
    }

    return processResult;
  }

  public void dropCollection() {
    template.dropCollection(Constants.RDO_SERVICE_PRESTATION_COLLECTION);
  }

  public void createCollection() {
    template.createCollection(Constants.RDO_SERVICE_PRESTATION_COLLECTION);
  }
}
