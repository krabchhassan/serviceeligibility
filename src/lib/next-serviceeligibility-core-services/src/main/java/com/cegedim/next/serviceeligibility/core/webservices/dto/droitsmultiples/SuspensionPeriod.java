package com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples;

import lombok.Data;

@Data
public class SuspensionPeriod {
  private Period period;
  private String suspensionType;
  private String suspensionReason;
  private String suspensionRemovalReason;
}
