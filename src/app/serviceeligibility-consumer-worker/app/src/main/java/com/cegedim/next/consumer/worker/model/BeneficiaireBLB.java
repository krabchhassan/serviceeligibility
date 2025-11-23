package com.cegedim.next.consumer.worker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.Data;

@Data
public class BeneficiaireBLB {

  private String nir;

  private String dateNaissance;

  private String rangNaissance;

  private String trackingId;

  List<ContratBLB> contrats;

  @JsonIgnore
  public String getKey() {
    return String.format(
        "%s.%s.%s_%s", this.nir, this.dateNaissance, this.rangNaissance, this.trackingId);
  }
}
