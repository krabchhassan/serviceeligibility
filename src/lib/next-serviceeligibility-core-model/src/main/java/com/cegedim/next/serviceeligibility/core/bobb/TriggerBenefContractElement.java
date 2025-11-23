package com.cegedim.next.serviceeligibility.core.bobb;

import com.cegedim.next.serviceeligibility.core.model.domain.pw.DroitsTPOfflineAndOnlinePW;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class TriggerBenefContractElement extends ContractElement {

  private List<DroitsTPOfflineAndOnlinePW> droitsTPOfflineAndOnlinePWS = new ArrayList<>();

  public TriggerBenefContractElement() {
    super();
  }

  public TriggerBenefContractElement(ContractElement contractElement) {
    this.setId(contractElement.getId());
    this.setCodeContractElement(contractElement.getCodeContractElement());
    this.setCodeInsurer(contractElement.getCodeInsurer());
    this.setLabel(contractElement.getLabel());
    this.setAlertId(contractElement.getAlertId());
    this.setDeadline(contractElement.getDeadline());
    this.setIgnored(contractElement.isIgnored());
    this.setCodeAMC(contractElement.getCodeAMC());
    this.setStatus(contractElement.getStatus());
    this.setOrigine(contractElement.getOrigine());
    this.setUser(contractElement.getUser());
    this.setProductElements(contractElement.getProductElements());
    this.droitsTPOfflineAndOnlinePWS = new ArrayList<>();
  }
}
