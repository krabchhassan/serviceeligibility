package com.cegedim.next.serviceeligibility.core.model.job;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContratsAMCexclues;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecutions634;
import java.util.List;
import lombok.Data;

@Data
public class DataForJob634 extends DataForJob<HistoriqueExecutions634> {
  private Declaration declaration;
  private List<String> listeAMCstop;
  private List<String> listeAMCreprise;
  private ContratsAMCexclues contratsAMCexclues;
  private String lastIdProcessed;
  private boolean shouldHistorize;

  @Override
  public Class<HistoriqueExecutions634> getHistoClass() {
    return HistoriqueExecutions634.class;
  }
}
