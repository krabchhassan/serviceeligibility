package com.cegedim.next.serviceeligibility.core.business.historiqueexecution.service;

import com.cegedim.next.serviceeligibility.core.business.historiqueexecution.dao.HistoriqueExecutionDao;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecution;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("historiqueExecution")
public class RestHistoriqueExecutionService {

  @Autowired private HistoriqueExecutionDao dao;

  @ContinueSpan(log = "findById HistoriqueExecution")
  public HistoriqueExecution findById(String id) {
    return dao.findById(id);
  }

  @ContinueSpan(log = "findLastByBatchIdDeclarant HistoriqueExecution")
  public HistoriqueExecution findLastByBatchIdDeclarant(
      String batch, String idDeclarant, String codeService) {
    return dao.findLastByBatchIdDeclarant(batch, idDeclarant, codeService);
  }

  @ContinueSpan(log = "deleteByBatchDateExecution HistoriqueExecution")
  public void deleteByBatchDateExecution(
      String batch, LocalDateTime dateExecution, String codeService) {
    dao.deleteByBatchDateExecution(batch, codeService, dateExecution);
  }

  @ContinueSpan(log = "deleteHisto")
  public void deleteHisto(HistoriqueExecution historiqueExecution) {
    dao.delete(historiqueExecution);
  }

  @ContinueSpan(log = "addHistoriqueExecution")
  public void addHistoriqueExecution(HistoriqueExecution historiqueExecution) {
    dao.add(historiqueExecution);
  }

  @ContinueSpan(log = "deleteAll HistoriqueExecution")
  public void deleteAll() {
    dao.deleteAll();
  }
}
