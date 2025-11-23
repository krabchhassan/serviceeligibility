package com.cegedim.next.serviceeligibility.core.utils.exceptions;

import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiaryAnomaly;

public class CarenceException extends TriggeredBeneficiaryAnomalyException {
  public CarenceException(TriggeredBeneficiaryAnomaly triggeredBeneficiaryAnomaly) {
    super(triggeredBeneficiaryAnomaly);
  }
}
