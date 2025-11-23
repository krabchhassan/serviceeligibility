package com.cegedim.next.serviceeligibility.core.utils.exceptions;

import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiaryAnomaly;

public class TriggerParametersException extends TriggeredBeneficiaryAnomalyException {
  public TriggerParametersException(TriggeredBeneficiaryAnomaly triggeredBeneficiaryAnomaly) {
    super(triggeredBeneficiaryAnomaly);
  }
}
