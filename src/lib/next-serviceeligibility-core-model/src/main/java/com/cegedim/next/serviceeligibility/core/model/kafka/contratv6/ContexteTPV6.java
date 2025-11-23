package com.cegedim.next.serviceeligibility.core.model.kafka.contratv6;

import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodesDroitsCarte;
import lombok.Data;

@Data
public class ContexteTPV6 {
  private Boolean isCartePapierAEditer;
  private PeriodesDroitsCarte periodesDroitsCarte;
  private String dateRestitutionCarte;
  private Boolean isCarteDematerialisee;
  private Boolean isCartePapier;
}
