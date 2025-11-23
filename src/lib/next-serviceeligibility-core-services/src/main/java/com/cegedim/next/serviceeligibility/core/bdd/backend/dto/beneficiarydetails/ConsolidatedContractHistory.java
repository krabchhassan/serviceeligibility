package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails;

import java.util.List;
import lombok.Data;

@Data
public class ConsolidatedContractHistory {
  private List<ConsolidatedContractDto> consolidatedContracts;
  private boolean searchNextConsoContractTp;
}
