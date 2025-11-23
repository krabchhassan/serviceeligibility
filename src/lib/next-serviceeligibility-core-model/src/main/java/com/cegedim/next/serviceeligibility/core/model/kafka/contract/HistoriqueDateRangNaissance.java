package com.cegedim.next.serviceeligibility.core.model.kafka.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoriqueDateRangNaissance {
  private String dateNaissance;
  private String rangNaissance;

  public void setDateNaissance(String newDateNaissance) {
    if (newDateNaissance != null) {
      this.dateNaissance = newDateNaissance;
    }
  }

  public void setRangNaissance(String newRangNaissance) {
    if (newRangNaissance != null) {
      this.rangNaissance = newRangNaissance;
    }
  }
}
