package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.contract.DomaineDroitContratDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractDto implements GenericDto {

  private DeclarantDto declarantAmc;
  private ContratDto contrat;
  private BeneficiaireDto beneficiaire;
  private List<DomaineDroitContratDto> domaineDroits;
}
