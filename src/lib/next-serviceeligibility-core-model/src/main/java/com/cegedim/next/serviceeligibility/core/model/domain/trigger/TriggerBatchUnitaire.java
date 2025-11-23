package com.cegedim.next.serviceeligibility.core.model.domain.trigger;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class TriggerBatchUnitaire {
  private String triggerId;
  private String servicePrestationId;
  private List<TriggeredBeneficiary> triggeredBeneficiaries = new ArrayList<>();
}
