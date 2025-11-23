package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.almerysProductRef;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.List;
import lombok.Data;

@Data
public class AlmerysProductDto implements GenericDto {
  private String code;
  private String description;
  private List<ProductCombinationDto> productCombinations;
}
