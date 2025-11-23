package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.paymentrecipients;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import lombok.Data;

@Data
public class ContractIdDto implements GenericDto {
  private String insurerId;
  private String subscriberId;
  private String number;
}
