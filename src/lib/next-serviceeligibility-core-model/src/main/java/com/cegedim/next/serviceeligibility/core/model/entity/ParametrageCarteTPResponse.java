package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTP;
import java.util.List;
import lombok.Data;

@Data
public class ParametrageCarteTPResponse {

  PagingResponseModel paging;
  List<ParametrageCarteTP> parametragesCarteTP;
}
