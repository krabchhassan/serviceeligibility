package com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun;

import com.cegedim.next.serviceeligibility.core.model.kafka.PeriodeCarence;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
public class CarenceDroit {
  private String code;
  private DroitRemplacement droitRemplacement;
  private PeriodeCarence periode;

  public boolean isEquals(CarenceDroit carenceDroit) {
    return StringUtils.equals(this.code, carenceDroit.getCode())
        && ((droitRemplacement != null
                && droitRemplacement.isEquals(carenceDroit.getDroitRemplacement()))
            || droitRemplacement == carenceDroit.getDroitRemplacement())
        && ((periode != null && periode.isEquals(carenceDroit.getPeriode()))
            || periode == carenceDroit.getPeriode());
  }

  public void setCode(String code) {
    if (code != null) {
      this.code = code;
    }
  }

  public void setCodeToNull() {
    this.code = null;
  }

  public void setPeriode(PeriodeCarence periode) {
    if (periode != null) {
      this.periode = periode;
    }
  }

  public void setPeriodeToNull() {
    this.periode = null;
  }

  public void setDroitRemplacement(DroitRemplacement droitRemplacement) {
    if (droitRemplacement != null) {
      this.droitRemplacement = droitRemplacement;
    }
  }

  public CarenceDroit(CarenceDroit source) {
    if (source.getDroitRemplacement() != null) {
      this.droitRemplacement = new DroitRemplacement(source.getDroitRemplacement());
    }

    this.setCode(source.getCode());
    if (source.getPeriode() != null) {
      this.setPeriode(new PeriodeCarence(source.getPeriode()));
    }
  }
}
