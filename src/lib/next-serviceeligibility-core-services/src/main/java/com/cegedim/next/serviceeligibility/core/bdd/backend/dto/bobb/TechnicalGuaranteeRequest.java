package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.bobb;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TechnicalGuaranteeRequest {
  @NotBlank(message = "L'information guaranteeCode est obligatoire")
  private String guaranteeCode;

  @NotBlank(message = "L'information insurerCode est obligatoire")
  private String insurerCode;
}
