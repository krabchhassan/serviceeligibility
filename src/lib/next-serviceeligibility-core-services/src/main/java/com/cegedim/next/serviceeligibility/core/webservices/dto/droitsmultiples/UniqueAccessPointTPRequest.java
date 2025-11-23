package com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples;

import java.util.List;
import lombok.Data;

@Data
public abstract class UniqueAccessPointTPRequest {

  protected UniqueAccessPointRequest request;

  private List<String> numeroPersonnes;

  private boolean isFoundByNumAMCEchange;

  private String numeroAdherent;
}
