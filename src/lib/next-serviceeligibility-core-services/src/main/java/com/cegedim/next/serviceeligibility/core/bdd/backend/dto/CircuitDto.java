package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CircuitDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String codeCircuit;
  private String libelleCircuit;
  private String emetteur;
}
