package com.cegedim.next.serviceeligibility.core.model.kafka.contratv5;

import lombok.Data;

@Data
public class ContexteTP {
  private Boolean isCartePapierAEditer;
  private PeriodesDroitsCarte periodesDroitsCarte;
  private String dateRestitutionCarte;
  private String college;
  private String collectivite;
  private Boolean isCarteDematerialisee;
  private Boolean isCartePapier;
}
