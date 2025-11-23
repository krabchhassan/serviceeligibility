package com.cegedim.next.serviceeligibility.batch620.job.services;

import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduBatch620Step1;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecutions620;
import com.cegedim.next.serviceeligibility.core.model.job.DataForJob620;
import com.cegedim.next.serviceeligibility.core.services.cartedemat.consolidation.ProcessorConsolidationService;
import com.cegedim.next.serviceeligibility.core.services.common.batch.TalendJob;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Processor extends TalendJob<HistoriqueExecutions620, DataForJob620> {

  @Autowired private ProcessorConsolidationService consolidationService;

  @Override
  protected String getBatchNumber() {
    return Constants.NUMERO_BATCH_620;
  }

  @Override
  protected String getCollection() {
    return "consolidationcontrats";
  }

  @Override
  protected boolean isAmcReprise() {
    return false;
  }

  public int process(DataForJob620 dataForJob620, CompteRenduBatch620Step1 compteRendu) {
    newHistoriqueExecutions = new HistoriqueExecutions620();
    int processResult = -1;
    try {
      processResult = process(dataForJob620);
    } catch (Exception e) {
      log.error("Unexpected error while processing batch");
      log.error(
          String.format("Error type : %s | Error message : %S", e.getClass(), e.getMessage()), e);
    } finally {
      compteRendu.addNombreConsolidationEnSucces(newHistoriqueExecutions.getNbConsolidationCree());
      compteRendu.addNombreConsolidationEnEchec(newHistoriqueExecutions.getNbDeclarationErreur());
      compteRendu.addNombreCartesDematInvalidees(newHistoriqueExecutions.getNbCartesInvalidees());
      compteRendu.addNombreCartesDematCrees(newHistoriqueExecutions.getNbCartesOk());
      compteRendu.addNombreCartesDematRejetees(newHistoriqueExecutions.getNbCartesKo());
      compteRendu.addNombreCartesPapierAEditer(newHistoriqueExecutions.getNbCartesPapierEdit());
      compteRendu.addNombreCartesPapierRejetees(newHistoriqueExecutions.getNbCartesPapierKo());
    }

    return processResult;
  }

  @Override
  protected void processRecords(DataForJob620 dataForJob620) {
    try {
      consolidationService.processConsolidation(dataForJob620, newHistoriqueExecutions);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
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
