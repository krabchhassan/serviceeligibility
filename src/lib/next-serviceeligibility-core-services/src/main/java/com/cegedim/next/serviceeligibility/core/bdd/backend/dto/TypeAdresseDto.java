package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import lombok.Getter;
import lombok.Setter;

/** Classe DTO de l'entite {@code TypeAdresse}. */
@Getter
@Setter
public class TypeAdresseDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String libelle;
  private String type;
}
