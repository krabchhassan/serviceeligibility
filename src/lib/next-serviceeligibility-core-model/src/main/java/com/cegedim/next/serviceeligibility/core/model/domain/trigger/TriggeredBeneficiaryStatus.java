package com.cegedim.next.serviceeligibility.core.model.domain.trigger;

import java.util.Date;
import lombok.Data;

@Data
public class TriggeredBeneficiaryStatus {
  private TriggeredBeneficiaryStatusEnum statut;
  private Date dateEffet;
  private TriggeredBeneficiaryAnomaly anomaly;
}
