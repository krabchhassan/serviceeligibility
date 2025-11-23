package com.cegedim.next.serviceeligibility.batch620extractconso.services;

import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduBatch620ExtractionConso;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecutions620;
import com.cegedim.next.serviceeligibility.core.model.job.DataForJob620;
import com.cegedim.next.serviceeligibility.core.services.cartedemat.cartepapier.CartesPapierService;
import com.cegedim.next.serviceeligibility.core.services.common.batch.TalendJob;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Processor extends TalendJob<HistoriqueExecutions620, DataForJob620> {

  @Autowired private CartesPapierService cartesPapierService;

  public int process(DataForJob620 dataForJob620, CompteRenduBatch620ExtractionConso compteRendu) {
    newHistoriqueExecutions = new HistoriqueExecutions620();
    int processResult = -1;
    try {
      processResult = process(dataForJob620);
    } catch (Exception e) {
      log.error("Unexpected error while processing second step of the batch");
      log.error(
          String.format("Error type : %s | Error message : %S", e.getClass(), e.getMessage()), e);
    } finally {
      if (newHistoriqueExecutions != null) {
        compteRendu.addNombreCartesPapierGeneresEnSucces(
            newHistoriqueExecutions.getNbCartesPapierOk());
        compteRendu.addNombreCartesPapierGeneresEnEchec(
            newHistoriqueExecutions.getNbCartesPapierKo());
      }
    }

    return processResult;
  }

  @Override
  protected String getBatchNumber() {
    return Constants.NUMERO_BATCH_620;
  }

  @Override
  protected String getCollection() {
    return "cartesPapier";
  }

  @Override
  protected boolean isAmcReprise() {
    return false;
  }

  @Override
  protected void processRecords(DataForJob620 dataForJob620) {
    newHistoriqueExecutions = dataForJob620.getLastExecution();
    if (newHistoriqueExecutions == null) {
      throw new RuntimeException("Pas d'historique d'exécution trouvé !");
    }
    dataForJob620.setIdentifiant(newHistoriqueExecutions.getIdentifiant());
    cartesPapierService.processCartePapier(dataForJob620);
    historiqueExecutionsDao.save(newHistoriqueExecutions);
  }

  @Override
  protected void manageAMCReprise(DataForJob620 dataForJob620) {
    // not useful in this batch
  }

  @Override
  protected void fillLastIdProcessedAndDeclaration(DataForJob620 dataForJob620) {
    // not useful in this batch yet ?
  }
}
