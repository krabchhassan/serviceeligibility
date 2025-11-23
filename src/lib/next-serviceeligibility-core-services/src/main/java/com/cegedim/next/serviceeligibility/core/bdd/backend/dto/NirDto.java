package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.cegedim.next.serviceeligibility.core.model.domain.benef.NirDeclaration;
import lombok.Data;

@Data
public class NirDto implements GenericDto {
  private String code;
  private String cle;

  public NirDto(NirDeclaration source) {
    if (source != null) {
      this.code = source.getCode();
      this.cle = source.getCle();
    }
  }
}
