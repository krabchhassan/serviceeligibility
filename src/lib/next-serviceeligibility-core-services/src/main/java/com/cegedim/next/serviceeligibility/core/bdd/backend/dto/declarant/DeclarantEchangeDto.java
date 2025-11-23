package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.List;

/** Classe DTO du {@code DeclarantsEchange}. */
public class DeclarantEchangeDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private List<String> numerosAMCEchanges;

  public List<String> getNumerosAMCEchanges() {
    return numerosAMCEchanges;
  }

  public void setNumerosAMCEchanges(List<String> numerosAMCEchanges) {
    this.numerosAMCEchanges = numerosAMCEchanges;
  }
}
