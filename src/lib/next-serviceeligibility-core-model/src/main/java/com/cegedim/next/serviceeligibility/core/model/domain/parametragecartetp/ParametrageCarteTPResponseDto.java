package com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp;

import com.cegedim.next.serviceeligibility.core.bobb.Lot;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParametrageCarteTPResponseDto {
  private List<ParametrageCarteTP> parametrageCarteTPS;
  private List<Lot> lots;
}
