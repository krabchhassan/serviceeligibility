package com.cegedim.next.serviceeligibility.core.services.pojo;

import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ExtendedOffreProduits;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiaryAnomaly;
import lombok.Data;

@Data
public class WaitingExtendedOffreProduits {

  private TriggeredBeneficiaryAnomaly waitingParameterError;

  private ExtendedOffreProduits offersAndProducts;
}
