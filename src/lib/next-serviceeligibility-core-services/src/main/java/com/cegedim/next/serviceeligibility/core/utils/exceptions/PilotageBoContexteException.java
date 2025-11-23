package com.cegedim.next.serviceeligibility.core.utils.exceptions;

import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiaryAnomaly;

public class PilotageBoContexteException extends TriggerWarningException {

  public PilotageBoContexteException(TriggeredBeneficiaryAnomaly triggeredBeneficiaryAnomaly) {
    super(triggeredBeneficiaryAnomaly);
  }
}
