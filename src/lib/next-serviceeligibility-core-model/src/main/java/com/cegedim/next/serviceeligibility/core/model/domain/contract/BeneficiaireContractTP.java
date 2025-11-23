package com.cegedim.next.serviceeligibility.core.model.domain.contract;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.util.CollectionUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class BeneficiaireContractTP extends BeneficiaireContractTPCommun {
  private List<DomaineDroitContractTP> domaineDroits;

  public BeneficiaireContractTP() {
    /* empty constructor */ }

  public BeneficiaireContractTP(BeneficiaireContractTP source) {
    super(source);
    if (!CollectionUtils.isEmpty(source.getDomaineDroits())) {
      this.domaineDroits = new ArrayList<>();
      for (DomaineDroitContractTP domain : source.getDomaineDroits()) {
        this.domaineDroits.add(new DomaineDroitContractTP(domain));
      }
    }
  }

  public int compareTo(BeneficiaireContractTP beneficiaireContract) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.appendSuper(super.compareTo(beneficiaireContract));
    compareToBuilder.append(this.domaineDroits, beneficiaireContract.domaineDroits);
    return compareToBuilder.toComparison();
  }
}
