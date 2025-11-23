package com.cegedim.next.serviceeligibility.core.model.crex;

import com.cegedim.common.omu.helper.parameters.ParameterValue;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecution607;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Data;

public class CompteRenduAlmerys607 implements CompteRenduGeneric {

  private List<Metrique> metriquePilotages = new ArrayList<>();
  private Metrique total;

  @Override
  public Map<String, ParameterValue> asMap() {
    String[] metricString =
        CompteRenduUtils.listToArray(metriquePilotages.stream().map(Metrique::toString).toList());
    return Map.of(
        "metriquesPilotages",
        ParameterValue.valueOf(metricString),
        "total",
        ParameterValue.valueOf(total.toString()));
  }

  public void addMetriquePilotage(List<HistoriqueExecution607> histos) {
    histos.stream().map(Metrique::new).forEach(metriquePilotages::add);
  }

  public void addTotal(HistoriqueExecution607 total) {
    if (total != null) {
      this.total = new Metrique(total);
    }
  }

  @Data
  private static class Metrique {
    private final String idDeclarant;
    private final String typeConventionnement;
    private final String critereSecondaire;
    private final String critereSecondaireDetaille;
    private final int nbDeclSelect;
    private final int nbDeclConso;
    private final int nbRejets;
    private final int nbDeclLues;

    public Metrique(HistoriqueExecution607 historique) {
      this.idDeclarant = historique.getIdDeclarant();
      this.typeConventionnement = historique.getTypeConventionnement();
      this.critereSecondaire = historique.getCritereSecondaire();
      this.critereSecondaireDetaille = historique.getCritereSecondaireDetaille();
      this.nbDeclSelect = historique.getNbDeclarationSelect();
      this.nbDeclConso = historique.getNbDeclarationConso();
      this.nbRejets = historique.getNbRejets();
      this.nbDeclLues = historique.getNbDeclarationLues();
    }

    @Override
    public String toString() {
      return CompteRenduUtils.objToString(this);
    }
  }
}
