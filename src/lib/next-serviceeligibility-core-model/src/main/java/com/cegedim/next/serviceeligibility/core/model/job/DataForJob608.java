package com.cegedim.next.serviceeligibility.core.model.job;

import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecution608;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DataForJob608 extends DataForJob<HistoriqueExecution608> {
  private String couloirClient;

  @Override
  public Class<HistoriqueExecution608> getHistoClass() {
    return HistoriqueExecution608.class;
  }
}
