package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.almerysProductRef;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.bobb.GTDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.List;
import lombok.Data;

@Data
public class ProductCombinationDto implements GenericDto {
  private List<GTDto> garantieTechniqueList;
  private List<LotAlmerysDto> lotAlmerysList;
  private String dateDebut;
  private String dateFin;
}
