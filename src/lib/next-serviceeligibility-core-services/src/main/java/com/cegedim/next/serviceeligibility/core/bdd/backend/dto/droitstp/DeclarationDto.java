package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.droitstp;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class DeclarationDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private DeclarantAmcDto declarantAmc;

  private BeneficiaireDto beneficiaire;

  private ContratDto contrat;

  private List<DomaineDroitsDto> domaineDroits = new ArrayList<>();
}
