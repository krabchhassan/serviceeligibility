package com.cegedim.next.serviceeligibility.core.webservices.clc;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.ConventionnementContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PrestationContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PrioriteDroitContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.RemboursementContrat;
import java.util.List;
import lombok.Data;

@Data
public class ReferenceCouvertureCLC {
  private String referenceCouverture;
  private List<PeriodeDroitContratCLC> periodesDroit;
  private List<PrioriteDroitContrat> prioritesDroit;
  private List<ConventionnementContrat> conventionnements;
  private List<PrestationContrat> prestations;
  private List<RemboursementContrat> remboursements;
}
