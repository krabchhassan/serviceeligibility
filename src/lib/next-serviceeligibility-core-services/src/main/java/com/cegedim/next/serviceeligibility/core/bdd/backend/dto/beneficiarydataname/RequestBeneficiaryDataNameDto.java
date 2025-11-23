package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydataname;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestBeneficiaryDataNameDto {

  private String personNumber;
  private String contractNumber;
}
