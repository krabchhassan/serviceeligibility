package com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun;

import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CodePeriode {
  private String code;
  private Periode periode;

  public CodePeriode() {}

  public void setCode(String code) {
    if (code != null) {
      this.code = code;
    }
  }

  public void setCodeToNull() {
    this.code = null;
  }

  public void setPeriode(Periode periode) {
    if (periode != null) {
      this.periode = periode;
    }
  }

  public void setPeriodeToNull() {
    this.periode = null;
  }
}
