package com.cegedim.next.serviceeligibility.core.services.pojo;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ResponseTriggerProcessing {

  private List<TriggerContract> triggerContracts = new ArrayList<>();
  boolean isWarning = false;
}
