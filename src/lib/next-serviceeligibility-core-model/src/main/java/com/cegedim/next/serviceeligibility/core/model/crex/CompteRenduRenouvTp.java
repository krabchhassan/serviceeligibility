package com.cegedim.next.serviceeligibility.core.model.crex;

import com.cegedim.common.omu.helper.parameters.ParameterValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class CompteRenduRenouvTp implements CompteRenduGeneric {
  private int triggerCount;
  private int triggeredBenefCount;
  private List<String> compteRendu;

  public CompteRenduRenouvTp() {
    this.compteRendu = new ArrayList<>();
  }

  public Map<String, ParameterValue> asMap() {
    Map<String, ParameterValue> map = new HashMap<>();

    map.put("triggerCount", ParameterValue.valueOf(this.triggerCount));
    map.put("triggeredBenefCount", ParameterValue.valueOf(this.triggeredBenefCount));
    map.put("compteRendu", ParameterValue.valueOf(CompteRenduUtils.listToArray(this.compteRendu)));
    return map;
  }

  public void addTriggerCount(int triggerCount) {
    this.triggerCount += triggerCount;
  }

  public void addTriggeredBenefCount(int triggeredBenefCount) {
    this.triggeredBenefCount += triggeredBenefCount;
  }

  public void addCompteRenduValue(String entry) {
    this.compteRendu.add(entry);
  }
}
