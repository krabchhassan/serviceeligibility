package com.cegedim.next.serviceeligibility.core.mapper;

import com.cegedim.next.serviceeligibility.core.model.domain.Conventionnement;
import com.cegedim.next.serviceeligibility.core.model.domain.Prestation;
import com.cegedim.next.serviceeligibility.core.model.domain.PrioriteDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ConventionnementContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PrestationContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PrioriteDroitContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import java.util.ArrayList;

public final class MapperContract {

  private MapperContract() {
    // mapper Util.
  }

  public static PrioriteDroitContrat mapPrioriteDroitToPrioriteDroitContrat(
      PrioriteDroit prioriteDroit, Periode periode) {
    PrioriteDroitContrat periodeDroitContract = new PrioriteDroitContrat(prioriteDroit);
    periodeDroitContract.getPeriodes().add(periode);
    return periodeDroitContract;
  }

  public static ConventionnementContrat mapConventionnementToConventionnementContrat(
      Conventionnement conventionnement, Periode periode) {
    ConventionnementContrat conventionnementContrat = new ConventionnementContrat(conventionnement);
    conventionnementContrat.setPeriodes(new ArrayList<>());
    conventionnementContrat.getPeriodes().add(periode);
    return conventionnementContrat;
  }

  public static PrestationContrat mapPrestationToPrestationContrat(
      Prestation prestation, Periode periode, String formulaMask) {
    PrestationContrat prestationContrat = new PrestationContrat(prestation);
    if (prestationContrat.getFormule() != null) {
      prestationContrat.getFormule().setMasqueFormule(formulaMask);
    }
    prestationContrat.getPeriodes().add(periode);
    return prestationContrat;
  }
}
