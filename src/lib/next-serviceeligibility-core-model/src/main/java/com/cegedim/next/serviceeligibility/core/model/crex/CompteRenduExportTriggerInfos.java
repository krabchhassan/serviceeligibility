package com.cegedim.next.serviceeligibility.core.model.crex;

import com.cegedim.common.omu.helper.parameters.ParameterValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class CompteRenduExportTriggerInfos implements CompteRenduGeneric {

  private List<String> cheminsFichiersTriggers = new ArrayList<>();

  @Override
  public Map<String, ParameterValue> asMap() {
    Map<String, ParameterValue> map = new HashMap<>();
    map.put(
        "cheminsFichiersTriggers",
        ParameterValue.valueOf(CompteRenduUtils.listToArray(cheminsFichiersTriggers)));
    return map;
  }
}
