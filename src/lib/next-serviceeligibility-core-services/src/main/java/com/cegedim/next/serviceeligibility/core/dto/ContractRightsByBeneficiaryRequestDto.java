package com.cegedim.next.serviceeligibility.core.dto;

import com.cegedim.next.serviceeligibility.core.utils.RequestValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.Data;

@Data
public class ContractRightsByBeneficiaryRequestDto {
  @NotBlank(message = "The beneficiaryId is required in the body")
  @Pattern(regexp = "^[^-]+-.+$", message = "beneficiaryId is not in the correct format")
  private String beneficiaryId;

  @NotBlank(message = "The context is required in the body")
  private String context;

  @NotBlank(message = "The nir is required in the body")
  private String nir;

  @NotEmpty(message = "The contractList must not be empty in the body")
  @Valid
  private List<ContractSubscriber> contractList;

  private String insurerId;
  private String personNumber;

  public record ContractSubscriber(
      @NotBlank String subscriberId, @NotBlank String contractNumber) {}

  public String getInsurerId() {
    if (insurerId == null) {
      insurerId = RequestValidator.validateRequestAndExtractBenefId(beneficiaryId, context)[0];
    }
    return insurerId;
  }

  public String getPersonNumber() {
    if (personNumber == null) {
      personNumber = RequestValidator.validateRequestAndExtractBenefId(beneficiaryId, context)[1];
    }
    return personNumber;
  }
}
