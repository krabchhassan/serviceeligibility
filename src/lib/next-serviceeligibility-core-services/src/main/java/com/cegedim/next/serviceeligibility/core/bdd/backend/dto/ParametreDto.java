package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import lombok.Getter;
import lombok.Setter;

/** Classe DTO de l'entite {@code Parametre}. */
@Getter
@Setter
public class ParametreDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String numero;
  private String libelle;
  private String valeur;

  public ParametreDto() {
    /* empty constructor */ }

  public ParametreDto(ParametreDto source) {
    this.numero = source.getNumero();
    this.libelle = source.getLibelle();
    this.valeur = source.getValeur();
  }
}
