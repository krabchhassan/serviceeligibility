package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import lombok.Getter;
import lombok.Setter;

/** Classe DTO de l'entite {@code PrioriteDroit}. */
@Getter
@Setter
public class PrioriteDroitDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String code;
  private String libelle;
  private String typeDroit;
  private String prioriteBO;
  private String nirPrio1;
  private String nirPrio2;
  private String prioDroitNir1;
  private String prioDroitNir2;
  private String prioContratNir1;
  private String prioContratNir2;
}
