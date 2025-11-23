package com.cegedim.next.serviceeligibility.core.model.domain.trigger;

import java.io.Serializable;
import lombok.Data;

@Data
public class TriggeredBeneficiaryAnomaly implements Serializable {

  private Anomaly anomaly;

  private String description;

  public static TriggeredBeneficiaryAnomaly create(Anomaly anomaly, Object... arg) {
    TriggeredBeneficiaryAnomaly triggeredBeneficiaryAnomaly = new TriggeredBeneficiaryAnomaly();
    triggeredBeneficiaryAnomaly.setAnomaly(anomaly);
    triggeredBeneficiaryAnomaly.setDescription(anomaly.getDescription(arg));
    return triggeredBeneficiaryAnomaly;
  }

  public static TriggeredBeneficiaryAnomaly errorRetryRecycling =
      TriggeredBeneficiaryAnomaly.create(Anomaly.ERROR_RETRY_RECYCLING);
}
