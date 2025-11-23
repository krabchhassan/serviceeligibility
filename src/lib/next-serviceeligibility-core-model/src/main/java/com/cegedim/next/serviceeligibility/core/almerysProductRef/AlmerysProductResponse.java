package com.cegedim.next.serviceeligibility.core.almerysProductRef;

import com.cegedim.next.serviceeligibility.core.model.domain.almerysProductRef.AlmerysProduct;
import com.cegedim.next.serviceeligibility.core.model.entity.PagingResponseModel;
import java.util.List;
import lombok.Data;

@Data
public class AlmerysProductResponse {
  PagingResponseModel paging;
  List<AlmerysProduct> almerysProductList;
}
