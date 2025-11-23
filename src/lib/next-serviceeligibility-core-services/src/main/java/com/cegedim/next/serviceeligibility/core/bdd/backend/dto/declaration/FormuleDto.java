package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.List;

public class FormuleDto implements GenericDto {

  /** */
  private static final long serialVersionUID = 6498563766149756637L;

  private String numero;
  private List<ParametreFormuleDto> parametres;

  public String getNumero() {
    return numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  public List<ParametreFormuleDto> getParametres() {
    return parametres;
  }

  public void setParametres(List<ParametreFormuleDto> parametres) {
    this.parametres = parametres;
  }
}
