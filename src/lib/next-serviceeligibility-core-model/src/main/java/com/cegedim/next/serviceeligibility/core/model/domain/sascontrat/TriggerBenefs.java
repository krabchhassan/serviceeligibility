package com.cegedim.next.serviceeligibility.core.model.domain.sascontrat;

import java.util.List;
import lombok.Data;

@Data
public class TriggerBenefs {
  private String triggerId;
  private List<BenefInfos> benefsInfos;
}
