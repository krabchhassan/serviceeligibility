package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.transco;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.List;

/** ServicesTranscoDto contient la data des services transcodage. */
public class ServicesTranscoDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String codeService;

  /* DOCUMENTS EMBEDDED */

  private List<TranscoParamDto> transco;

  /* GETTERS SETTERS */

  public String getCodeService() {
    return codeService;
  }

  public void setCodeService(String codeService) {
    this.codeService = codeService;
  }

  public List<TranscoParamDto> getTransco() {
    return transco;
  }

  public void setTransco(List<TranscoParamDto> transco) {
    this.transco = transco;
  }
}
