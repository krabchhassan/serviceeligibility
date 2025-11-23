package com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DigitRelation {
  private Dematerialisation dematerialisation;
  private List<Teletransmission> teletransmissions;

  public DigitRelation() {}

  public void setDematerialisation(Dematerialisation dematerialisation) {
    if (dematerialisation != null) {
      this.dematerialisation = dematerialisation;
    }
  }

  public void setTeletransmissions(List<Teletransmission> teletransmissions) {
    if (teletransmissions != null) {
      this.teletransmissions = teletransmissions;
    }
  }
}
