package com.cegedim.next.serviceeligibility.core.model.job;

import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecution607;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DataForJob607 extends DataForJob<HistoriqueExecution607> {
  private String couloirClient;

  @Override
  public Class<HistoriqueExecution607> getHistoClass() {
    return HistoriqueExecution607.class;
  }
}
