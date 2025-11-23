package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecutions;
import com.mongodb.client.ClientSession;
import org.springframework.data.mongodb.core.query.Criteria;

public interface HistoriqueExecutionsDao {

  <T extends HistoriqueExecutions<T>> T getLastExecution(String batch, Class<T> tClass);

  <T extends HistoriqueExecutions<T>> T getLastExecution(
      String batch, String idDeclarant, Class<T> tClass);

  <T extends HistoriqueExecutions<T>> T getLastExecutionForReprise620(
      String batch, String idDeclarant, Class<T> tClass);

  <T extends HistoriqueExecutions<T>> T save(T h);

  <T extends HistoriqueExecutions<T>> T saveWithSession(T h, ClientSession session);

  void deleteAll();

  long deleteByAMC(String amc);

  <T extends HistoriqueExecutions<T>> T getLastExecution(Criteria criteria, Class<T> tClass);
}
