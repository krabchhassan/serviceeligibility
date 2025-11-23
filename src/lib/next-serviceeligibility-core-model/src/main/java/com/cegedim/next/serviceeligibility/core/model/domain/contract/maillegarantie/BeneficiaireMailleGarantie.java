package com.cegedim.next.serviceeligibility.core.model.domain.contract.maillegarantie;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.BeneficiaireContractTPCommun;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.util.CollectionUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class BeneficiaireMailleGarantie extends BeneficiaireContractTPCommun {
  private List<DomaineDroitMailleGarantie> domaineDroits = new ArrayList<>();

  public BeneficiaireMailleGarantie() {
    /* empty constructor */ }

  public BeneficiaireMailleGarantie(BeneficiaireMailleGarantie source) {
    super(source);
    if (!CollectionUtils.isEmpty(source.getDomaineDroits())) {
      this.domaineDroits = new ArrayList<>();
      for (DomaineDroitMailleGarantie domain : source.getDomaineDroits()) {
        this.domaineDroits.add(new DomaineDroitMailleGarantie(domain));
      }
    }
  }

  public int compareTo(BeneficiaireMailleGarantie beneficiaireContract) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.appendSuper(super.compareTo(beneficiaireContract));
    compareToBuilder.append(this.domaineDroits, beneficiaireContract.domaineDroits);
    return compareToBuilder.toComparison();
  }
}
