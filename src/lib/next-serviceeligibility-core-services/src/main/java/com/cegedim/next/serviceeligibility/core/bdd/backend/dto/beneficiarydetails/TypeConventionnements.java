package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.cegedim.next.serviceeligibility.core.model.domain.TypeConventionnement;
import lombok.Data;

@Data
public class TypeConventionnements implements GenericDto {
  private Integer priorite;
  private TypeConventionnement typeConventionnement;
}
