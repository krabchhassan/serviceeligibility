package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import lombok.Getter;
import lombok.Setter;

/** Classe DTO de l'entite {@code FormuleMetie}. */
@Getter
@Setter
public class FormuleMetierDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String code;
  private String libelle;

  public FormuleMetierDto() {
    /* empty constructor */ }

  public FormuleMetierDto(FormuleMetierDto source) {
    this.code = source.getCode();
    this.libelle = source.getLibelle();
  }
}
