package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import lombok.Data;

/**
 * ErreursDto contient les erreurs générés par les batchs, récupérable dans la collection
 * parametres.
 */
@Data
public class ErreursDto implements GenericDto, Comparable<ErreursDto> {

  private static final long serialVersionUID = 1L;

  private String code;

  private String libelle;

  private String motif;

  private String niveauErreur;

  private String typeErreur;

  @Override
  public int compareTo(ErreursDto o) {
    return this.getCode().compareTo(o.getCode());
  }
}
