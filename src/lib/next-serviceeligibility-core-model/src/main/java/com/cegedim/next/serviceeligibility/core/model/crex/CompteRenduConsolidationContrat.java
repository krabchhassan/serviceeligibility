package com.cegedim.next.serviceeligibility.core.model.crex;

import com.cegedim.common.omu.helper.parameters.ParameterValue;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class CompteRenduConsolidationContrat implements CompteRenduGeneric {
  private long nombreDeclarationTraitee;
  private long nombreDeclarationIgnoree;
  private long nbContratCree;
  private long nbContratModifie;

  public Map<String, ParameterValue> asMap() {
    Map<String, ParameterValue> map = new HashMap<>();

    map.put(
        "nombreDeclarationTraitee", ParameterValue.valueOf((int) this.nombreDeclarationTraitee));
    map.put(
        "nombreDeclarationIgnoree", ParameterValue.valueOf((int) this.nombreDeclarationIgnoree));
    map.put("nbContratCree", ParameterValue.valueOf((int) this.nbContratCree));
    map.put("nbContratModifie", ParameterValue.valueOf((int) this.nbContratModifie));

    return map;
  }

  public void addNombreDeclarationTraitee(long nombreDeclarationTraitee) {
    this.nombreDeclarationTraitee += nombreDeclarationTraitee;
  }

  public void addNombreDeclarationIgnoree(long nombreDeclarationIgnoree) {
    this.nombreDeclarationIgnoree += nombreDeclarationIgnoree;
  }

  public void addNbContratCree(long nbContratCree) {
    this.nbContratCree += nbContratCree;
  }

  public void addNbContratModifie(long nbContratModifie) {
    this.nbContratModifie += nbContratModifie;
  }
}
