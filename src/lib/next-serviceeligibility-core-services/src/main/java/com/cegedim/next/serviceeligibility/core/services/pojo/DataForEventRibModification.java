package com.cegedim.next.serviceeligibility.core.services.pojo;

import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DestinatairePrestations;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DataForEventRibModification {

  private String idDeclarant;
  private DestinatairePrestations oldDestinataire;
  private DestinatairePrestations newDestinataire;
  private String numeroPersonne;
}
