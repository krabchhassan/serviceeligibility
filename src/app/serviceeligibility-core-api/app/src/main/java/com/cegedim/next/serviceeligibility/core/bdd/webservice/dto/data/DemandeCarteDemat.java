package com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DemandeCarteDemat {

  private String numeroAMC;

  private String numeroContrat;

  private String dateReference;

  @Override
  public String toString() {
    return "DemandeCarteDemat [numeroAMC="
        + numeroAMC
        + ", numeroContrat="
        + numeroContrat
        + ", dateReference="
        + dateReference
        + "]";
  }
}
