package com.cegedim.next.serviceeligibility.core.model.crex;

import com.cegedim.common.omu.helper.parameters.ParameterValue;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CompteRenduRdoClaim implements CompteRenduGeneric {

  private int nombreContratsLus = 0;
  private int nbBeneficiairesIntegres = 0;

  @Override
  public Map<String, ParameterValue> asMap() {
    Map<String, ParameterValue> map = new HashMap<>();
    map.put("nombreContratsLus", ParameterValue.valueOf(nombreContratsLus));
    map.put("nbBeneficiairesIntegres", ParameterValue.valueOf(nbBeneficiairesIntegres));
    return map;
  }
}
