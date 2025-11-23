package com.cegedim.next.serviceeligibility.core.model.kafka;

import lombok.Data;

@Data
public class Audit {
  private String dateEmission;

  public void setDateEmission(String newDateEmission) {
    if (newDateEmission != null) {
      this.dateEmission = newDateEmission;
    }
  }

  public void setDateEmissionToNull() {
    this.dateEmission = null;
  }
}
