package com.cegedim.next.serviceeligibility.core.model.domain.contract;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.util.CollectionUtils;

@Data
public class SuspensionContract implements GenericDomain<SuspensionContract> {

  List<PeriodeSuspensionContract> periodesSuspension;
  private String etatSuspension;

  public SuspensionContract() {
    /* empty constructor */ }

  public SuspensionContract(SuspensionContract source) {
    this.etatSuspension = source.getEtatSuspension();
    if (!CollectionUtils.isEmpty(source.getPeriodesSuspension())) {
      this.periodesSuspension = new ArrayList<>();
      for (PeriodeSuspensionContract periode : source.getPeriodesSuspension()) {
        this.periodesSuspension.add(new PeriodeSuspensionContract(periode));
      }
    }
  }

  @Override
  public int compareTo(SuspensionContract periodeDroitContract) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.periodesSuspension, periodeDroitContract.periodesSuspension);
    compareToBuilder.append(this.etatSuspension, periodeDroitContract.etatSuspension);
    return compareToBuilder.toComparison();
  }
}
