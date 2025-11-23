package com.cegedim.next.serviceeligibility.core.features.almerysProductRef;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.almerysProductRef.AlmerysProductDto;
import com.cegedim.next.serviceeligibility.core.model.entity.PagingResponseModel;
import java.util.List;
import lombok.Data;

@Data
public class AlmerysProductDtoResponse {
  PagingResponseModel paging;
  List<AlmerysProductDto> almerysProductList;
}
