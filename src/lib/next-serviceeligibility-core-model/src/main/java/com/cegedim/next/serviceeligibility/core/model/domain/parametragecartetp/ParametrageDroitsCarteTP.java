package com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class ParametrageDroitsCarteTP implements Serializable {
  private String codeConventionTP;
  private String codeOperateurTP;
  private Boolean isCarteDematerialisee;
  private String refFondCarte;
  private String codeAnnexe1;
  private String codeAnnexe2;
  private String codeItelis;
  private String codeRenvoi;
  private List<DetailDroit> detailsDroit;

  private Boolean isCarteEditablePapier;
}
