package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.cegedim.next.serviceeligibility.core.model.entity.ControleContextuel;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Data;

/** Classe DTO de l'entite {@code ServiceDroits}. */
@JsonInclude(Include.NON_NULL)
@Data
public class ServiceDroitsDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String nom;
  private String id;
  private List<String> listTransco;
  private ControleContextuel controleContextuel;
}
