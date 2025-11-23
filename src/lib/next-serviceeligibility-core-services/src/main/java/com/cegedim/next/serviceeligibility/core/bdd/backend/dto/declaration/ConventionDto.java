package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;

public class ConventionDto implements GenericDto {
  /** */
  private static final long serialVersionUID = 7438445175205504149L;

  private String priorite;
  private String code;

  public String getPriorite() {
    return priorite;
  }

  public void setPriorite(String priorite) {
    this.priorite = priorite;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
}
