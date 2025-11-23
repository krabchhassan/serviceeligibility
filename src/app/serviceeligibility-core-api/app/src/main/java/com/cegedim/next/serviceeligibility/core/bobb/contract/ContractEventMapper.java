package com.cegedim.next.serviceeligibility.core.bobb.contract;

import com.cegedim.next.serviceeligibility.core.bobb.ContractElement;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.CarenceDroit;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class ContractEventMapper {

  /**
   * Get all contract elements for a contract event.
   *
   * @param event the contract event
   * @return List of contract elements
   */
  @NotNull
  public List<ContractElement> getContractElements(@NotNull final ContratAIV6 event) {
    List<ContractElement> contractElements = new ArrayList<>();
    for (Assure assure : event.getAssures()) {
      for (DroitAssure droit : assure.getDroits()) {
        final ContractElement contractElement = new ContractElement();
        contractElement.setCodeContractElement(droit.getCode());
        contractElement.setCodeInsurer(droit.getCodeAssureur());
        contractElement.setDeadline(LocalDate.parse(droit.getPeriode().getDebut()));
        contractElements.add(contractElement);
        getCarenceContractElements(contractElements, droit);
      }
    }
    return contractElements;
  }

  private static void getCarenceContractElements(
      List<ContractElement> contractElements, DroitAssure droit) {
    if (droit.getCarences() != null && !droit.getCarences().isEmpty()) {
      for (CarenceDroit carence : droit.getCarences()) {
        if (carence.getDroitRemplacement() != null
            && StringUtils.isNotBlank(carence.getDroitRemplacement().getCode())) {
          final ContractElement carenceContractElement = new ContractElement();
          carenceContractElement.setCodeContractElement(carence.getDroitRemplacement().getCode());
          carenceContractElement.setCodeInsurer(carence.getDroitRemplacement().getCodeAssureur());
          carenceContractElement.setDeadline(LocalDate.parse(carence.getPeriode().getDebut()));
          contractElements.add(carenceContractElement);
        }
      }
    }
  }
}
