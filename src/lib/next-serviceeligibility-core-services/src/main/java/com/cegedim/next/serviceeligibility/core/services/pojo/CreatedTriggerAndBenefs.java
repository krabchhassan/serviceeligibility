package com.cegedim.next.serviceeligibility.core.services.pojo;

import com.cegedim.next.serviceeligibility.core.model.domain.trigger.Trigger;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiary;
import java.util.List;
import lombok.Data;

@Data
public class CreatedTriggerAndBenefs {

  private Trigger trigger;

  private boolean errorContract;

  private List<TriggeredBeneficiary> benefs;
}
