package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.droitstp;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import lombok.Data;

@Data
public class PrioriteDroitDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String code;

  private String libelle;
}
