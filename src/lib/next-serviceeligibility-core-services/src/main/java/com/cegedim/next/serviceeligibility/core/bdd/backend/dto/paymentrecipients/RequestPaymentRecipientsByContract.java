package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.paymentrecipients;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.Data;

@Data
public class RequestPaymentRecipientsByContract {
  @NotBlank(message = "The insurerId is required in the body")
  private String insurerId;

  @NotBlank(message = "The beneficiaryId is required in the body")
  @Pattern(regexp = "^[^-]+-.+$", message = "beneficiaryId is not in the correct format")
  private String beneficiaryId;

  @NotBlank(message = "The referenceDate is required in the body")
  @Pattern(
      regexp = "^\\d{4}-\\d{2}-\\d{2}$",
      message = "referenceDate must be in the format YYYY-MM-DD")
  private String referenceDate;

  @NotEmpty(message = "The contracts must not be empty in the body")
  @Valid
  private List<RequestContract> contracts;
}
