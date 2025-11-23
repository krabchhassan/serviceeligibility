package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.paymentrecipients;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestContract implements GenericDto {
  @NotBlank(message = "The subscriberId is required in the body")
  private String subscriberId;

  @NotBlank(message = "The number is required in the body")
  private String number;
}
