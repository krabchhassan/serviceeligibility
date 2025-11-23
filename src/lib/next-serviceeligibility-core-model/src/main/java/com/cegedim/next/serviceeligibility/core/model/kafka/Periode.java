package com.cegedim.next.serviceeligibility.core.model.kafka;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

@Data
public class Periode {
  @NotBlank(message = "L'information début de période est obligatoire")
  private String debut;

  @EqualsAndHashCode.Exclude private String fin;

  public Periode(String debut, String fin) {
    this.debut = debut;
    this.fin = fin;
  }

  public Periode() {}

  public Periode(Periode source) {
    this.debut = source.getDebut();
    this.fin = source.getFin();
  }

  public void setDebut(String newDebut) {
    if (newDebut != null) {
      this.debut = newDebut;
    }
  }

  public void setDebutToNull() {
    this.debut = null;
  }

  public void setFin(String newFin) {
    if (newFin != null) {
      this.fin = newFin;
    }
  }

  public void setFinToNull() {
    this.fin = null;
  }

  public boolean isEquals(Periode periode) {
    return StringUtils.equals(this.debut, periode.getDebut())
        && StringUtils.equals(this.fin, periode.getFin());
  }

  /**
   * Retourne True si les périodes se chevauchent (fin==null veut dire infini)<br>
   * Algo: (p1.debut <= p2.fin) && (p2.debut <= p1.fin)<br>
   * [========== p1 ==========] <br>
   * * * * * * * [========== p2 ==========]
   */
  public boolean overlaps(Periode period) {
    return ObjectUtils.compare(this.debut, period.fin, true) <= 0 // (p1.debut <= p2.fin)
        && ObjectUtils.compare(period.debut, this.fin, true) <= 0; // (p2.debut <= p1.fin)
  }

  /** {@link #overlaps(Periode)} */
  public boolean overlaps(String start, String end) {
    return this.overlaps(new Periode(start, end));
  }
}
