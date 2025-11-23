package com.cegedim.next.serviceeligibility.core.bobb.services;

import com.cegedim.next.serviceeligibility.core.bobb.ContractElement;
import com.cegedim.next.serviceeligibility.core.bobb.ProductElementLight;
import com.cegedim.next.serviceeligibility.core.bobb.util.ContractElementServiceUtil;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.time.LocalDateTime;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductElementService {
  private final ContractElementService contractElementService;

  public List<ProductElementLight> getOfferAndProduct(
      String codeAssureur, String code, String periodeDebut, String periodeFin) {

    if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(codeAssureur)) {
      LocalDateTime dateDebut = DateUtils.parseLocalDateTime(periodeDebut, DateUtils.YYYY_MM_DD);
      LocalDateTime dateFin =
          (periodeFin != null)
              ? DateUtils.parseLocalDateTime(periodeFin, DateUtils.YYYY_MM_DD)
              : null;

      ContractElement contractElement = contractElementService.get(code, codeAssureur, false);
      return ContractElementServiceUtil.getProductElementLights(
          contractElement, dateDebut, dateFin);
    }

    return Collections.emptyList();
  }
}
