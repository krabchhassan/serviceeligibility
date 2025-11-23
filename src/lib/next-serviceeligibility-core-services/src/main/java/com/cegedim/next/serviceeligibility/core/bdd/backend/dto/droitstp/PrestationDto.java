package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.droitstp;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import lombok.Data;

@Data
public class PrestationDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String code;

  private String codeRegroupement;

  private String libelle;

  private Boolean isEditionRisqueCarte;

  private String dateEffet;

  private FormuleDto formule;

  private FormuleMetierDto formuleMetier;
}
