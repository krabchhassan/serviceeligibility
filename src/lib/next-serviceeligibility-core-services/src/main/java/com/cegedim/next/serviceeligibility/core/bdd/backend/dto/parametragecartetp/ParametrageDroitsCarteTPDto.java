package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.parametragecartetp;

import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.DetailDroit;
import java.util.List;
import lombok.Data;

@Data
public class ParametrageDroitsCarteTPDto {
  private String codeConventionTP;
  private String codeOperateurTP;
  private String isCarteDematerialisee;
  private String isCarteEditablePapier;
  private String refFondCarte;
  private String codeAnnexe1;
  private String codeAnnexe2;
  private String codeItelis;
  private String codeRenvoi;
  private List<DetailDroit> detailsDroit;
}
