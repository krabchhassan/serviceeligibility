package com.cegedim.next.serviceeligibility.core.bobbcorrespondance.dto;

import com.cegedim.next.serviceeligibility.core.bobb.ProductElement;
import java.util.List;

public record CorrespondenceResponseDto(
    String gtId,
    String codeInsurer,
    String codeContractElement,
    GroupBy groupBy,
    List<ProductElement> items,
    List<PeriodGroupDto> periods) {
  public static CorrespondenceResponseDto product(
      String gtId, String insurer, String codeContractElement, List<ProductElement> items) {
    return new CorrespondenceResponseDto(
        gtId, insurer, codeContractElement, GroupBy.PRODUCT, items, null);
  }

  public static CorrespondenceResponseDto period(
      String gtId, String insurer, String codeContractElement, List<PeriodGroupDto> periods) {
    return new CorrespondenceResponseDto(
        gtId, insurer, codeContractElement, GroupBy.PERIOD, null, periods);
  }
}
