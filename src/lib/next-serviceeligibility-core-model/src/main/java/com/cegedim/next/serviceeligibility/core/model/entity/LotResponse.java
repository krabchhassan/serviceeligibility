package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.bobb.Lot;
import java.util.List;
import lombok.Data;

@Data
public class LotResponse {

  PagingResponseModel paging;
  List<Lot> lot;
}
