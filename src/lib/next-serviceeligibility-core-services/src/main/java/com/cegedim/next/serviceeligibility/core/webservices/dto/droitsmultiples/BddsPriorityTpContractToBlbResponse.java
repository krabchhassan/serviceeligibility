package com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class BddsPriorityTpContractToBlbResponse {
  private String issuingCompanyCode;
  private String insurerId;
  private String beneficiaryId;
}
