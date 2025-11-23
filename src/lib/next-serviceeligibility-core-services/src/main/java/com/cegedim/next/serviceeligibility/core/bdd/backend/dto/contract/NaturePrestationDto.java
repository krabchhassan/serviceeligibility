package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.contract;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.ConventionnementContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PrestationContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PrioriteDroitContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.RemboursementContrat;
import java.util.List;
import lombok.Data;

@Data
public class NaturePrestationDto {
  private String naturePrestation;
  private List<PeriodeDroitContratDto> periodesDroit;
  private List<PrioriteDroitContrat> prioritesDroit;
  private List<ConventionnementContrat> conventionnements;
  private List<PrestationContrat> prestations;
  private List<RemboursementContrat> remboursements;
}
