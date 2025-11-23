package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** Classe qui mappe le document DomaineConvention */
public class DomaineConventionDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  @Getter @Setter private String codeDomaine;

  @Getter @Setter private Integer rang;

  /* DOCUMENTS EMBEDDED */
  @Setter private List<ConventionDto> conventionDtos;

  /**
   * @return La liste des conventions.
   */
  public List<ConventionDto> getConventionDtos() {
    if (this.conventionDtos == null) {
      this.conventionDtos = new ArrayList<>();
    }
    return this.conventionDtos;
  }
}
