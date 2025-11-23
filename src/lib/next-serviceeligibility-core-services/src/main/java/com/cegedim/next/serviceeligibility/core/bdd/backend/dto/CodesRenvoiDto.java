package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import lombok.Data;

@Data
public class CodesRenvoiDto implements GenericDto, Comparable<CodesRenvoiDto> {

  private static final long serialVersionUID = 1L;

  private String code;

  private String libelle;

  @Override
  public int compareTo(CodesRenvoiDto o) {
    return this.getCode().compareTo(o.getCode());
  }
}
