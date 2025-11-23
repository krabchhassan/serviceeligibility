package com.cegedim.next.serviceeligibility.core.model.crex;

import com.cegedim.common.omu.helper.parameters.ParameterValue;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class CompteRenduServicePrestationRdoPurge implements CompteRenduGeneric {
  private long benefKeyCount;

  public Map<String, ParameterValue> asMap() {
    Map<String, ParameterValue> map = new HashMap<>();

    map.put("benefKeyCount", ParameterValue.valueOf((int) this.benefKeyCount));
    return map;
  }

  public void addBenefKeyCount(long benefKeyCount) {
    this.benefKeyCount += benefKeyCount;
  }
}
