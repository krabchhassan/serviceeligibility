package com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp;

import java.io.Serializable;
import lombok.Data;

@Data
public class DetailDroit implements Serializable {
  private int ordreAffichage;
  private String codeDomaineTP;
  private String libelleDomaineTP;
  private String convention;
  private String codeRenvoi;
  private CodeRenvoiAction codeRenvoiAction;
  @Deprecated private Boolean isDomaineEditable;
}
