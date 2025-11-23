package com.cegedim.next.serviceeligibility.core.services.pojo;

import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiary;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

@Data
@AllArgsConstructor
public class TriggerContract {

  private String idTrigger;

  private String idDeclarant;

  private String numeroContrat;

  private String numeroAdherent;

  public TriggerContract(List<TriggeredBeneficiary> triggeredBeneficiaryList) {
    if (CollectionUtils.isNotEmpty(triggeredBeneficiaryList)) {
      TriggeredBeneficiary triggeredBeneficiary = triggeredBeneficiaryList.get(0);
      this.idTrigger = triggeredBeneficiary.getIdTrigger();
      this.idDeclarant = triggeredBeneficiary.getAmc();
      this.numeroContrat = triggeredBeneficiary.getNumeroContrat();
      this.numeroAdherent = triggeredBeneficiary.getNumeroAdherent();
    }
  }
}
