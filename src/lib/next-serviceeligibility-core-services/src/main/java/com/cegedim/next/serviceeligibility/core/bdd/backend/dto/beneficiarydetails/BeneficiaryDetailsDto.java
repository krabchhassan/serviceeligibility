package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.RechercheInfosDroitsDto;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class BeneficiaryDetailsDto {
  private BeneficiaryDto benefDetails;
  private RechercheInfosDroitsDto declarationToOpen;
  private DeclarationDetailsDto declarationDetails;
  private List<ConsolidatedContractDto> consolidatedContractList;
  private Map<String, ConsolidatedContractHistory> historiqueConsolidations;
  private ConsolidatedContractDto contractToOpen;
  private Map<String, AttestationsContractDto> attestations;
  private Map<String, List<BeneficiaireContractDto>> otherBenefs;
  private List<String> sasContractList;
}
