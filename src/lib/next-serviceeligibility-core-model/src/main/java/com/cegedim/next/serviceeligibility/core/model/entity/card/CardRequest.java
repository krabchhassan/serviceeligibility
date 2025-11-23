package com.cegedim.next.serviceeligibility.core.model.entity.card;

import lombok.Data;

@Data
public class CardRequest {
  // Mandatory
  private String numeroAmc;
  private String dateReference;
  private String numeroContrat;

  // Optional
  private String numeroAdherent;
}
