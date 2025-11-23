package com.cegedim.next.serviceeligibility.core.model.kafka.benef;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BeneficiaireId {
  private String nir;
  private String dateNaissance;
  private String rangNaissance;
  private String trackingId;

  public String getKey() {
    return String.format("%s%s%s", this.nir, this.dateNaissance, this.rangNaissance);
  }
}
