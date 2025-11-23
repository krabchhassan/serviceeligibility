package com.cegedim.next.serviceeligibility.core.model.crex;

import com.cegedim.common.omu.helper.parameters.ParameterValue;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecution608;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Data;

public class CompteRenduAlmerys608 implements CompteRenduGeneric {

  private final List<Metrique> metriquePilotages = new ArrayList<>();
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

  public void addMetriquePilotage(List<HistoriqueExecution608> histos) {
    histos.stream().map(Metrique::new).forEach(metriquePilotages::add);
  }

  public void addTotal(HistoriqueExecution608 total) {
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
    private int nbDeclarationsConsolideesAlmerysLues;
    private int nbContrats;
    private int nbMembresContrats;
    private int nbContratsRejetes;
    private int nbRejetsNonBloquants;
    private int nbRejetsBloquants;
    private int nbFichiersGeneres;

    public Metrique(HistoriqueExecution608 historique) {
      this.idDeclarant = historique.getIdDeclarant();
      this.typeConventionnement = historique.getTypeConventionnement();
      this.critereSecondaire = historique.getCritereSecondaire();
      this.critereSecondaireDetaille = historique.getCritereSecondaireDetaille();
      this.nbDeclarationsConsolideesAlmerysLues =
          historique.getNbDeclarationsConsolideesAlmerysLues();
      this.nbContrats = historique.getNbContrats();
      this.nbMembresContrats = historique.getNbMembresContrats();
      this.nbContratsRejetes = historique.getNbContratsRejetes();
      this.nbRejetsNonBloquants = historique.getNbRejetsNonBloquants();
      this.nbRejetsBloquants = historique.getNbRejetsBloquants();
      this.nbFichiersGeneres = historique.getNbFichiersGeneres();
    }

    @Override
    public String toString() {
      return CompteRenduUtils.objToString(this);
    }
  }
}
