package com.cegedim.next.serviceeligibility.core.model.crex;

import com.cegedim.common.omu.helper.parameters.ParameterValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class CompteRenduExtractionPopulation implements CompteRenduGeneric {

  private final List<String> nomFichiers = new ArrayList<>();

  @Override
  public Map<String, ParameterValue> asMap() {
    Map<String, ParameterValue> map = new HashMap<>();

    // Transform List<String> to String[]
    String[] mappedCompteRendu = nomFichiers.toArray(new String[0]);
    map.put("FICHIER", ParameterValue.valueOf(mappedCompteRendu));

    return map;
  }

  public void addNomFichiers(String nom) {
    nomFichiers.add(nom);
  }
}
