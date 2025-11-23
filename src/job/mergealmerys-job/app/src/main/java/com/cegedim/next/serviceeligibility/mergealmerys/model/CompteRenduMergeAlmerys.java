package com.cegedim.next.serviceeligibility.mergealmerys.model;

import com.cegedim.common.omu.helper.parameters.ParameterValue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class CompteRenduMergeAlmerys {
  private final List<String> fichiersAggreges = new ArrayList<>();
  private final List<String> fichiersCrees = new ArrayList<>();

  public Map<String, ParameterValue> asMap() {
    Map<String, ParameterValue> map = new HashMap<>();
    map.put("nbFichiersCrees", ParameterValue.valueOf(fichiersCrees.size()));
    map.put("nbFichiersAggreges", ParameterValue.valueOf(fichiersAggreges.size()));
    map.put("fichiersCrees", ParameterValue.valueOf(fichiersCrees.toArray(new String[0])));
    map.put("fichiersAggreges", ParameterValue.valueOf(fichiersAggreges.toArray(new String[0])));
    return map;
  }

  public void addMergedFileNames(List<String> mergedFileNames) {
    fichiersAggreges.addAll(mergedFileNames);
  }

  public void addCreatedFileNames(Collection<String> createdFileNames) {
    fichiersCrees.addAll(createdFileNames);
  }
}
