package com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun;

import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Teletransmission {
  private Periode periode;
  private Boolean isTeletransmission;

  public Teletransmission() {}

  public Teletransmission(Teletransmission teletransmission) {
    if (teletransmission.getPeriode() != null) {
      this.periode = new Periode(teletransmission.getPeriode());
    }

    if (teletransmission.getIsTeletransmission() != null) {
      this.isTeletransmission = teletransmission.getIsTeletransmission();
    }
  }

  public void setPeriode(Periode periode) {
    if (periode != null) {
      this.periode = periode;
    }
  }

  public void setTeletransmission(Boolean teletransmission) {
    if (teletransmission != null) {
      isTeletransmission = teletransmission;
    }
  }
}
