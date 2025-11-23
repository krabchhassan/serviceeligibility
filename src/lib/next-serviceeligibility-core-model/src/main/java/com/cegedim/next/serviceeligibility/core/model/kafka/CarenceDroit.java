package com.cegedim.next.serviceeligibility.core.model.kafka;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CarenceDroit {
  @NotBlank(message = "L'information code de la carence de droit est obligatoire")
  private String code;

  @NotNull(message = "L'information periode de la carence de droit est obligatoire")
  @Valid
  private Periode periode;

  @NotBlank(message = "L'information codeDroitRemplacement de la carence de droit est obligatoire")
  private String codeDroitRemplacement;
}
