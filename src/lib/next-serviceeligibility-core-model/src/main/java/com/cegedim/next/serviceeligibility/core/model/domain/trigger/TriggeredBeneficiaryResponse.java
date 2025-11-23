package com.cegedim.next.serviceeligibility.core.model.domain.trigger;

import com.cegedim.next.serviceeligibility.core.model.entity.PagingResponseModel;
import java.util.List;
import lombok.Data;

@Data
public class TriggeredBeneficiaryResponse {
  private PagingResponseModel paging;
  private List<TriggeredBeneficiary> triggeredBeneficiaries;
}
