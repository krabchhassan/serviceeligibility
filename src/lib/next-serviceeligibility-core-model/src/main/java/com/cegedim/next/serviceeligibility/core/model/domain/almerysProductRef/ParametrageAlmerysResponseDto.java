package com.cegedim.next.serviceeligibility.core.model.domain.almerysProductRef;

import com.cegedim.next.serviceeligibility.core.bobb.Lot;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParametrageAlmerysResponseDto {
  private List<AlmerysProduct> parametragesAlmerys;
  private List<Lot> lots;
}
