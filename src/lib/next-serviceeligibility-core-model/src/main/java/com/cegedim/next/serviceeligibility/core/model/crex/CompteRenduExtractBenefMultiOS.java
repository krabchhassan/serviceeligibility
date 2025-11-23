package com.cegedim.next.serviceeligibility.core.model.crex;

import com.cegedim.common.omu.helper.parameters.ParameterValue;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CompteRenduExtractBenefMultiOS implements CompteRenduGeneric {

  private String cheminFichierExtraction;
  private int nbBeneficiairesExtraits = 0;

  @Override
  public Map<String, ParameterValue> asMap() {
    Map<String, ParameterValue> map = new HashMap<>();
    map.put("cheminFichierExtraction", ParameterValue.valueOf(cheminFichierExtraction));
    map.put("nbBeneficiairesExtraits", ParameterValue.valueOf(nbBeneficiairesExtraits));
    return map;
  }
}
