package com.cegedim.next.serviceeligibility.core.model.crex;

import com.cegedim.common.omu.helper.parameters.ParameterValue;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CompteRenduFacturationHTP implements CompteRenduGeneric {

  private int nbFichierCree = 0;

  private int nbFichierKO = 0;

  private final List<String> compteRendu = new ArrayList<>();

  @Setter private LocalDate dateCalcul;

  @Override
  public Map<String, ParameterValue> asMap() {
    Map<String, ParameterValue> map = new HashMap<>();

    map.put("dateCalcul", ParameterValue.valueOf(dateCalcul.toString()));
    map.put("nbFichierCree", ParameterValue.valueOf(nbFichierCree));
    map.put("nbFichierKO", ParameterValue.valueOf(nbFichierKO));

    // Transform List<String> to String[]
    String[] mappedCompteRendu = compteRendu.toArray(new String[0]);
    map.put("compteRendu", ParameterValue.valueOf(mappedCompteRendu));

    return map;
  }

  public void addCompteRenduValue(String variable, String value) {
    final StringJoiner stringJoiner = new StringJoiner(":", "{", "}");
    stringJoiner.add(variable).add(value);
    compteRendu.add(stringJoiner.toString());
  }

  public void incExportedFilesCount() {
    nbFichierCree++;
  }

  public void incErrorFilesCount() {
    nbFichierKO++;
  }
}
