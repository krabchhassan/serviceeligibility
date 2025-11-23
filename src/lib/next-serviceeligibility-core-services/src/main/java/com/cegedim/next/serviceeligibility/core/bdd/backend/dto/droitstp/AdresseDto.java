package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.droitstp;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import lombok.Data;

@Data
public class AdresseDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String ligne1;

  private String ligne2;

  private String ligne3;

  private String ligne4;

  private String ligne5;

  private String ligne6;

  private String ligne7;

  private String codePostal;

  private String pays;
}
