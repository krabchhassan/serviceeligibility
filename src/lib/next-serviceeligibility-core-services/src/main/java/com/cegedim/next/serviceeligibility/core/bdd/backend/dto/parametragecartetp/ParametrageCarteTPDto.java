package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.parametragecartetp;

import java.util.List;
import lombok.Data;

@Data
public class ParametrageCarteTPDto {
  private String id;
  private String amc;
  private List<String> identifiantCollectivite;
  private List<String> groupePopulation;
  private List<String> critereSecondaireDetaille;
  private ParametrageRenouvellementDto parametrageRenouvellement;
  private ParametrageDroitsCarteTPDto parametrageDroitsCarteTP;
  private List<String> idLots;
  private List<GarantieTechniqueDto> selectedGTs;
}
