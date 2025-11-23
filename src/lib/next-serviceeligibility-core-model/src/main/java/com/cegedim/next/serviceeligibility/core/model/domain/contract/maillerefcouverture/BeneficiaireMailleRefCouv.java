package com.cegedim.next.serviceeligibility.core.model.domain.contract.maillerefcouverture;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.BeneficiaireContractTPCommun;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.util.CollectionUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class BeneficiaireMailleRefCouv extends BeneficiaireContractTPCommun {
  private List<DomaineDroitMailleRefCouv> domaineDroits = new ArrayList<>();

  public BeneficiaireMailleRefCouv() {
    /* empty constructor */ }

  public BeneficiaireMailleRefCouv(BeneficiaireMailleRefCouv source) {
    super(source);
    if (!CollectionUtils.isEmpty(source.getDomaineDroits())) {
      this.domaineDroits = new ArrayList<>();
      for (DomaineDroitMailleRefCouv domain : source.getDomaineDroits()) {
        this.domaineDroits.add(new DomaineDroitMailleRefCouv(domain));
      }
    }
  }

  public int compareTo(BeneficiaireMailleRefCouv beneficiaireContract) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.appendSuper(super.compareTo(beneficiaireContract));
    compareToBuilder.append(this.domaineDroits, beneficiaireContract.domaineDroits);
    return compareToBuilder.toComparison();
  }
}
