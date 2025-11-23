package com.cegedim.next.serviceeligibility.core.elast;

import com.cegedim.next.serviceeligibility.core.model.entity.PagingResponseModel;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import java.util.List;
import lombok.Data;

@Data
public class BenefElasticPageResult {
  private List<BenefAIV5> data;
  private PagingResponseModel paging;
}
