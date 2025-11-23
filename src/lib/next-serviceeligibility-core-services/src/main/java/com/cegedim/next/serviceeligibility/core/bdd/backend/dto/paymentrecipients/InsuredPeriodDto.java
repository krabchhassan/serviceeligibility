package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.paymentrecipients;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import lombok.Data;

@Data
public class InsuredPeriodDto implements GenericDto {
  private String start;
  private String end;
}
