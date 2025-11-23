package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import lombok.Getter;
import lombok.Setter;

/** Classe DTO de l'entite {@code TypeConventionnement}. */
@Getter
@Setter
public class TypeConventionnementDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String code;
  private String libelle;
}
