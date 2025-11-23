package com.cegedim.next.serviceeligibility.core.model.kafka;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

@Data
public class PeriodeCarence {
  private String debut;
  @EqualsAndHashCode.Exclude private String fin;

  public PeriodeCarence(String debut, String fin) {
    this.debut = debut;
    this.fin = fin;
  }

  public PeriodeCarence() {}

  public PeriodeCarence(PeriodeCarence source) {
    this.debut = source.getDebut();
    this.fin = source.getFin();
  }

  public boolean isEquals(PeriodeCarence periodeCarence) {
    return StringUtils.equals(this.debut, periodeCarence.getDebut())
        && StringUtils.equals(this.fin, periodeCarence.getFin());
  }

  public void setDebut(String debut) {
    if (debut != null) {
      this.debut = debut;
    }
  }

  public void setFin(String fin) {
    if (fin != null) {
      this.fin = fin;
    }
  }
}
