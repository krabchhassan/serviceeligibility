package com.cegedim.next.serviceeligibility.core.model.crex;

import com.cegedim.common.omu.helper.parameters.ParameterValue;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class CompteRenduBatch620ExtractionConso implements CompteRenduGeneric {
  private long nombreCartesPapierGeneresEnSucces;
  private long nombreCartesPapierGeneresEnEchec;

  public Map<String, ParameterValue> asMap() {
    Map<String, ParameterValue> map = new HashMap<>();

    map.put(
        "nombreCartesPapierGeneresEnSucces",
        ParameterValue.valueOf((int) this.nombreCartesPapierGeneresEnSucces));
    map.put(
        "nombreCartesPapierGeneresEnEchec",
        ParameterValue.valueOf((int) this.nombreCartesPapierGeneresEnEchec));

    return map;
  }

  public void addNombreCartesPapierGeneresEnSucces(long nombreCartesPapierGeneresEnSucces) {
    this.nombreCartesPapierGeneresEnSucces += nombreCartesPapierGeneresEnSucces;
  }

  public void addNombreCartesPapierGeneresEnEchec(long nombreCartesPapierGeneresEnEchec) {
    this.nombreCartesPapierGeneresEnEchec += nombreCartesPapierGeneresEnEchec;
  }
}
