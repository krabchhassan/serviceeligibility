package com.cegedim.next.serviceeligibility.core.model.domain.trigger;

import com.cegedim.next.serviceeligibility.core.model.entity.PagingResponseModel;
import java.util.List;
import lombok.Data;

@Data
public class TriggerResponse {
  private PagingResponseModel paging;
  private List<Trigger> triggers;
}
