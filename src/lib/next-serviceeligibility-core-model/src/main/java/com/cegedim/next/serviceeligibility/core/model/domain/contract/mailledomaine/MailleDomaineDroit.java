package com.cegedim.next.serviceeligibility.core.model.domain.contract.mailledomaine;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MailleDomaineDroit extends DomaineDroitContractTPCommun {
  private List<PrestationContrat> prestations = new ArrayList<>();
  private List<PrioriteDroitContrat> prioritesDroit = new ArrayList<>();
  private List<RemboursementContrat> remboursements = new ArrayList<>();
  private List<PeriodeDroitContractTP> periodesDroit = new ArrayList<>();
  private List<ConventionnementContrat> conventionnements = new ArrayList<>();

  public MailleDomaineDroit(MailleDomaineDroit source) {
    super(source);
    if (!CollectionUtils.isEmpty(source.getPeriodesDroit())) {
      this.periodesDroit = new ArrayList<>();
      for (PeriodeDroitContractTP periodeDroitContractTP : source.getPeriodesDroit()) {
        this.periodesDroit.add(new PeriodeDroitContractTP(periodeDroitContractTP));
      }
    }
    if (!CollectionUtils.isEmpty(source.getPrioritesDroit())) {
      this.prioritesDroit = new ArrayList<>();
      for (PrioriteDroitContrat prioriteDroit : source.getPrioritesDroit()) {
        this.prioritesDroit.add(new PrioriteDroitContrat(prioriteDroit));
      }
    }
    if (!CollectionUtils.isEmpty(source.getConventionnements())) {
      this.conventionnements = new ArrayList<>();
      for (ConventionnementContrat conv : source.getConventionnements()) {
        this.conventionnements.add(new ConventionnementContrat(conv));
      }
    }
    if (!CollectionUtils.isEmpty(source.getPrestations())) {
      this.prestations = new ArrayList<>();
      for (PrestationContrat prest : source.getPrestations()) {
        this.prestations.add(new PrestationContrat(prest));
      }
    }

    if (!CollectionUtils.isEmpty(source.getRemboursements())) {
      this.remboursements = new ArrayList<>();
      for (RemboursementContrat remboursementContrat : source.getRemboursements()) {
        this.remboursements.add(new RemboursementContrat(remboursementContrat));
      }
    }
  }
}
