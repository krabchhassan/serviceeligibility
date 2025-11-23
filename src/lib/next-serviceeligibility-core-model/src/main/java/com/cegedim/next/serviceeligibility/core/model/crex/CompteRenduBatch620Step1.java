package com.cegedim.next.serviceeligibility.core.model.crex;

import com.cegedim.common.omu.helper.parameters.ParameterValue;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class CompteRenduBatch620Step1 implements CompteRenduGeneric {
  private long nombreConsolidationEnSucces;
  private long nombreConsolidationEnEchec;
  private String identifiant;
  private int nombreCartesDematInvalidees;
  private int nombreCartesDematCrees;
  private int nombreCartesDematRejetees;
  private int nombreCartesPapierAEditer;
  private int nombreCartesPapierRejetees;

  public Map<String, ParameterValue> asMap() {
    Map<String, ParameterValue> map = new HashMap<>();

    map.put(
        "nombreConsolidationEnSucces",
        ParameterValue.valueOf((int) this.nombreConsolidationEnSucces));
    map.put(
        "nombreConsolidationEnEchec",
        ParameterValue.valueOf((int) this.nombreConsolidationEnEchec));
    map.put(
        "nombreCartesDematInvalidees", ParameterValue.valueOf(this.nombreCartesDematInvalidees));
    map.put("identifiant", ParameterValue.valueOf(identifiant));
    map.put("nombreCartesDematCrees", ParameterValue.valueOf(this.nombreCartesDematCrees));
    map.put("nombreCartesDematRejetees", ParameterValue.valueOf(this.nombreCartesDematRejetees));
    map.put("nombreCartesPapierAEditer", ParameterValue.valueOf(this.nombreCartesPapierAEditer));
    map.put("nombreCartesPapierRejetees", ParameterValue.valueOf(this.nombreCartesPapierRejetees));

    return map;
  }

  public void addNombreConsolidationEnSucces(long nombreConsolidationEnSucces) {
    this.nombreConsolidationEnSucces += nombreConsolidationEnSucces;
  }

  public void addNombreConsolidationEnEchec(long nombreConsolidationKo) {
    this.nombreConsolidationEnEchec += nombreConsolidationKo;
  }

  public void addNombreCartesDematInvalidees(int nombreCartesDematInvalidees) {
    this.nombreCartesDematInvalidees += nombreCartesDematInvalidees;
  }

  public void addNombreCartesDematCrees(int nombreCartesDematCrees) {
    this.nombreCartesDematCrees += nombreCartesDematCrees;
  }

  public void addNombreCartesDematRejetees(int nombreCartesDematRejetees) {
    this.nombreCartesDematRejetees += nombreCartesDematRejetees;
  }

  public void addNombreCartesPapierAEditer(int nombreCartesPapierAEditer) {
    this.nombreCartesPapierAEditer += nombreCartesPapierAEditer;
  }

  public void addNombreCartesPapierRejetees(int nombreCartesPapierRejetees) {
    this.nombreCartesPapierRejetees += nombreCartesPapierRejetees;
  }
}
