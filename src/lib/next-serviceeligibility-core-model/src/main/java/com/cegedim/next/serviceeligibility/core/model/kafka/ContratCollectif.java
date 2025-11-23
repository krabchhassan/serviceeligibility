package com.cegedim.next.serviceeligibility.core.model.kafka;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ContratCollectif {
  @NotBlank(message = "L'information contratCollectif.numero est obligatoire")
  private String numero;

  private String numeroExterne;
}
