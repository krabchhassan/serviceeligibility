package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import lombok.Data;

/** Classe qui mappe le document Convention */
@Data
public class ConventionDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private Integer prioriteConventionnement;
  private String typeConventionnement;
}
