package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.InfosAssureDto;
import java.util.List;
import lombok.Data;

@Data
public class DeclarationDetailsDto {
  private List<InfosAssureDto> infosAssureDtos;
  private boolean searchNextDeclarations;
}
