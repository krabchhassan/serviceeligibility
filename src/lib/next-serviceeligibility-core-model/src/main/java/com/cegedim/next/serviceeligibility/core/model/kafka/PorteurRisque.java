package com.cegedim.next.serviceeligibility.core.model.kafka;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PorteurRisque {
  @NotBlank(message = "L'information porteurRisque.code est obligatoire")
  private String code;

  @NotBlank(message = "L'information porteurRisque.libelle est obligatoire")
  private String libelle;
}
