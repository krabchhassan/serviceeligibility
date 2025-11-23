package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.digitalcontractinformations;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestDigitalContractInformationsDto {
  private String insurerId;
  private String subscriberId;
  private String contractNumber;
  private String date;
  private List<String> domains;
}
