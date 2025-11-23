package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

/** Classe DTO Light de l'entite {@code Declarant}. */
@Data
@JsonInclude(Include.NON_EMPTY)
public class DeclarantLightDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  private String numero;

  private String nom;

  private String codePartenaire;
}
