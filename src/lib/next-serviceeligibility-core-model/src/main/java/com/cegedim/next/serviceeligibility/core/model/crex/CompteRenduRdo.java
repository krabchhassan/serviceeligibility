package com.cegedim.next.serviceeligibility.core.model.crex;

import com.cegedim.common.omu.helper.parameters.ParameterValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class CompteRenduRdo implements CompteRenduGeneric {
  private long lignesLues;
  private long contratsIntegres;
  private long contratsRejetes;
  private List<String> listeFichiers;

  public CompteRenduRdo() {
    this.listeFichiers = new ArrayList<>();
  }

  public Map<String, ParameterValue> asMap() {
    Map<String, ParameterValue> map = new HashMap<>();

    map.put("lignesLues", ParameterValue.valueOf((int) this.lignesLues));
    map.put("contratsIntegres", ParameterValue.valueOf((int) this.contratsIntegres));
    map.put("contratsRejetes", ParameterValue.valueOf((int) this.contratsRejetes));
    map.put(
        "listeFichiers", ParameterValue.valueOf(CompteRenduUtils.listToArray(this.listeFichiers)));

    return map;
  }

  public void addLignesLues(long lignesLues) {
    this.lignesLues += lignesLues;
  }

  public void addContratsIntegres(long contratsIntegres) {
    this.contratsIntegres += contratsIntegres;
  }

  public void addContratsRejetes(long contratsRejetes) {
    this.contratsRejetes += contratsRejetes;
  }

  public void addListeFichiersValue(String entry) {
    this.listeFichiers.add(entry);
  }
}
