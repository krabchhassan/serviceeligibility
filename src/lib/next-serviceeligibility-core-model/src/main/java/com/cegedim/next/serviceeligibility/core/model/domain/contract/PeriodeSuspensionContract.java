package com.cegedim.next.serviceeligibility.core.model.domain.contract;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class PeriodeSuspensionContract implements GenericDomain<PeriodeSuspensionContract> {

  private String dateDebutSuspension;
  private String dateFinSuspension;

  public PeriodeSuspensionContract() {
    /* empty constructor */ }

  public PeriodeSuspensionContract(PeriodeSuspensionContract source) {
    this.dateDebutSuspension = source.getDateDebutSuspension();
    this.dateFinSuspension = source.getDateFinSuspension();
  }

  @Override
  public int compareTo(PeriodeSuspensionContract periodeDroitContract) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.dateDebutSuspension, periodeDroitContract.dateDebutSuspension);
    compareToBuilder.append(this.dateFinSuspension, periodeDroitContract.dateFinSuspension);
    return compareToBuilder.toComparison();
  }
}
