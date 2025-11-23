package com.cegedim.next.serviceeligibility.core.model.domain.contract.maillerefcouverture;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.*;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.util.CollectionUtils;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class MailleReferenceCouverture implements GenericDomain<MailleReferenceCouverture> {
  private String formulaMask;
  private String referenceCouverture;
  private List<PrestationContrat> prestations = new ArrayList<>();
  private List<PrioriteDroitContrat> prioritesDroit = new ArrayList<>();
  private List<RemboursementContrat> remboursements = new ArrayList<>();
  private List<PeriodeDroitContractTP> periodesDroit = new ArrayList<>();
  private List<ConventionnementContrat> conventionnements = new ArrayList<>();

  @Override
  public int compareTo(MailleReferenceCouverture o) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(referenceCouverture, o.referenceCouverture);
    compareToBuilder.append(formulaMask, o.formulaMask);
    return compareToBuilder.toComparison();
  }

  public MailleReferenceCouverture(MailleReferenceCouverture source) {
    this.referenceCouverture = source.referenceCouverture;
    this.formulaMask = source.formulaMask;

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
