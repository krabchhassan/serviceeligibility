package com.cegedim.next.serviceeligibility.core.utils.exceptions;

import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiaryAnomaly;

public class PwException extends TriggeredBeneficiaryAnomalyException {

  public PwException(TriggeredBeneficiaryAnomaly triggeredBeneficiaryAnomaly) {
    super(triggeredBeneficiaryAnomaly);
  }
}
