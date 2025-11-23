package com.cegedim.next.serviceeligibility.core.model.domain.contract.mailleproduit;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.BeneficiaireContractTPCommun;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.util.CollectionUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class BeneficiaireMailleProduit extends BeneficiaireContractTPCommun {
  private List<DomaineDroitMailleProduit> domaineDroits = new ArrayList<>();

  public BeneficiaireMailleProduit() {
    /* empty constructor */ }

  public BeneficiaireMailleProduit(BeneficiaireMailleProduit source) {
    super(source);
    if (!CollectionUtils.isEmpty(source.getDomaineDroits())) {
      this.domaineDroits = new ArrayList<>();
      for (DomaineDroitMailleProduit domain : source.getDomaineDroits()) {
        this.domaineDroits.add(new DomaineDroitMailleProduit(domain));
      }
    }
  }

  public int compareTo(BeneficiaireMailleProduit beneficiaireContract) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.appendSuper(super.compareTo(beneficiaireContract));
    compareToBuilder.append(this.domaineDroits, beneficiaireContract.domaineDroits);
    return compareToBuilder.toComparison();
  }
}
