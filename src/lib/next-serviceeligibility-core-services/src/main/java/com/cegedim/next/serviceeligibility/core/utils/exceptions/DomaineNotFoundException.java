package com.cegedim.next.serviceeligibility.core.utils.exceptions;

import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiaryAnomaly;

public class DomaineNotFoundException extends TriggeredBeneficiaryAnomalyException {
  public DomaineNotFoundException(TriggeredBeneficiaryAnomaly triggeredBeneficiaryAnomaly) {
    super(triggeredBeneficiaryAnomaly);
  }
}
