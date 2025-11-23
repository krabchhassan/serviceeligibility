package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;

public class PrestationDto implements GenericDto {

  /** */
  private static final long serialVersionUID = 6022870080042166496L;

  private String code;
  private FormuleDto formule;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public FormuleDto getFormule() {
    return formule;
  }

  public void setFormule(FormuleDto formule) {
    this.formule = formule;
  }
}
