package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import lombok.Data;

/** ParametresDto echange la data avec entite Parametres. */
@Data
public class ParametresDto implements GenericDto, Comparable<ParametresDto> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String code;

  private String libelle;

  @Override
  public int compareTo(ParametresDto o) {
    return this.getCode().compareTo(o.getCode());
  }
}
