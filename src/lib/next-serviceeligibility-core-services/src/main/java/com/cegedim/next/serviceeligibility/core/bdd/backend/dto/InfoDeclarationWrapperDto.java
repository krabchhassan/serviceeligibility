package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfoDeclarationWrapperDto implements GenericDto {

  /** */
  private static final long serialVersionUID = 1L;

  private List<InfoDeclarationDto> resultatsList;

  long nbResultats = 0;

  public InfoDeclarationWrapperDto() {
    super();
  }

  public InfoDeclarationWrapperDto(List<InfoDeclarationDto> resultatsList, long nbResultats) {
    super();
    this.resultatsList = resultatsList;
    this.nbResultats = nbResultats;
  }
}
