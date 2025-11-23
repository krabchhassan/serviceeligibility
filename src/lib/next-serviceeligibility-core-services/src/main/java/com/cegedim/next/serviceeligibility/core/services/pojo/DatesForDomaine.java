package com.cegedim.next.serviceeligibility.core.services.pojo;

import lombok.Data;

@Data
public class DatesForDomaine {

  private String requeteStartDate;
  private String requeteEndDate;
  private String dateFinOnline;
  private boolean isWarning = true;
}
