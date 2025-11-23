package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.droitstp;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import lombok.Data;

@Data
public class DeclarantAmcDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String numeroPrefectoral;

  private String libelle;

  private String nom;

  private String siret;
}
