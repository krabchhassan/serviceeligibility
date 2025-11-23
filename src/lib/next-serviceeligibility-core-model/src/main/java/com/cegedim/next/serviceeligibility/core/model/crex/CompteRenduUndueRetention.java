package com.cegedim.next.serviceeligibility.core.model.crex;

import com.cegedim.common.omu.helper.parameters.ParameterValue;
import java.util.*;
import lombok.Getter;

@Getter
public class CompteRenduUndueRetention implements CompteRenduGeneric {

  private int nbOccurrenceModifiee = 0;
  private int nbOccurrenceAnnulee = 0;
  private int nbOccurrenceEnvoyee = 0;

  private final List<String> compteRendu = new ArrayList<>();

  @Override
  public Map<String, ParameterValue> asMap() {
    Map<String, ParameterValue> map = new HashMap<>();

    map.put("nbOccurrenceModifiee", ParameterValue.valueOf(nbOccurrenceModifiee));
    map.put("nbOccurrenceAnnulee", ParameterValue.valueOf(nbOccurrenceAnnulee));
    map.put("nbOccurrenceEnvoyee", ParameterValue.valueOf(nbOccurrenceEnvoyee));

    // Transform List<String> to String[]
    String[] mappedCompteRendu = compteRendu.toArray(new String[0]);
    map.put("compteRendu", ParameterValue.valueOf(mappedCompteRendu));

    return map;
  }

  public void incNbOccurrenceModifiee() {
    nbOccurrenceModifiee++;
  }

  public void incNbOccurrenceAnnulee() {
    nbOccurrenceAnnulee++;
  }

  public void incNbOccurrenceEnvoyee() {
    nbOccurrenceEnvoyee++;
  }
}
