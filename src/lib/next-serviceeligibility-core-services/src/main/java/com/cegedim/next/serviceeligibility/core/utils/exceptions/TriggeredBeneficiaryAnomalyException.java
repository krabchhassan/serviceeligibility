package com.cegedim.next.serviceeligibility.core.utils.exceptions;

import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiaryAnomaly;
import lombok.Getter;

@Getter
public class TriggeredBeneficiaryAnomalyException extends Exception {
  private final TriggeredBeneficiaryAnomaly triggeredBeneficiaryAnomaly;

  public TriggeredBeneficiaryAnomalyException(
      TriggeredBeneficiaryAnomaly triggeredBeneficiaryAnomaly) {
    super(triggeredBeneficiaryAnomaly.getDescription());
    this.triggeredBeneficiaryAnomaly = triggeredBeneficiaryAnomaly;
  }
}
