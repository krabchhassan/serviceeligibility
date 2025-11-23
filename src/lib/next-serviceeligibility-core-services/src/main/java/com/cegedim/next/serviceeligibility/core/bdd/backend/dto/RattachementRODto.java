package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.cegedim.next.serviceeligibility.core.model.domain.benef.RattachementRODeclaration;
import lombok.Data;

@Data
public class RattachementRODto implements GenericDto {
  private String codeRegime;
  private String codeCaisse;
  private String codeCentre;

  public RattachementRODto(RattachementRODeclaration source) {
    if (source != null) {
      this.codeRegime = source.getCodeRegime();
      this.codeCaisse = source.getCodeCaisse();
      this.codeCentre = source.getCodeCentre();
    }
  }
}
