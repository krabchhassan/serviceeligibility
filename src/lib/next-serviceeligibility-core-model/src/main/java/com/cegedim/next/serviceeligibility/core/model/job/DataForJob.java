package com.cegedim.next.serviceeligibility.core.model.job;

import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecutions;
import lombok.Data;

@Data
public abstract class DataForJob<T extends HistoriqueExecutions<T>> {

  private String collection;
  private String collectionName;
  private String dateSynchroStr;
  private T lastExecution;
  private long jddSize;

  public abstract Class<T> getHistoClass();
}
