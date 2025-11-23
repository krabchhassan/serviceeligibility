package com.cegedim.next.serviceeligibility.core.model.job;

import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecutions620;
import java.util.Date;
import lombok.Data;

@Data
public class DataForJob620 extends DataForJob<HistoriqueExecutions620> {

  private Date today;
  private String identifiant;
  private String clientType;
  private DataForReprise620 dataForReprise620;
  private String couloirClient;

  @Override
  public Class<HistoriqueExecutions620> getHistoClass() {
    return HistoriqueExecutions620.class;
  }
}
