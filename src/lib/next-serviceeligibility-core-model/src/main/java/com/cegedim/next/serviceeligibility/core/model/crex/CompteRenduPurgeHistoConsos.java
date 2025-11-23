package com.cegedim.next.serviceeligibility.core.model.crex;

import com.cegedim.common.omu.helper.parameters.ParameterValue;
import java.util.Map;
import lombok.Setter;

@Setter
public class CompteRenduPurgeHistoConsos implements CompteRenduGeneric {
  private int contratPurge = 0;
  private String pathToS3;

  @Override
  public Map<String, ParameterValue> asMap() {
    return Map.of(
        "nombreHistoContratPurge",
        ParameterValue.valueOf(contratPurge),
        "cheminFichierPurgeS3",
        ParameterValue.valueOf(pathToS3));
  }
}
