package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import lombok.Data;

@Data
public class Remboursement implements GenericDto {
  private String tauxRemboursement;
  private String uniteTauxRemboursement;
}
