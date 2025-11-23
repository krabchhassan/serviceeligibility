package com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DataAssure extends DataAssureCommun {
  private List<DestinatairePrestations> destinatairesPaiements;
  private List<DestinataireRelevePrestations> destinatairesRelevePrestations;

  public void setDestinatairesPaiements(List<DestinatairePrestations> newDestinatairesPaiements) {
    if (newDestinatairesPaiements != null) {
      this.destinatairesPaiements = newDestinatairesPaiements;
    }
  }

  public void setDestinatairesRelevePrestations(
      List<DestinataireRelevePrestations> newDestinatairesRelevePrestations) {
    if (newDestinatairesRelevePrestations != null) {
      this.destinatairesRelevePrestations = newDestinatairesRelevePrestations;
    }
  }
}
