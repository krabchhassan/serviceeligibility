package com.cegedim.next.serviceeligibility.core.model.domain.contract.mailledomaine;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.BeneficiaireContractTPCommun;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.util.CollectionUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class BeneficiaireMailleDomaine extends BeneficiaireContractTPCommun {
  private List<MailleDomaineDroit> domaineDroits = new ArrayList<>();

  public BeneficiaireMailleDomaine() {
    /* empty constructor */ }

  public BeneficiaireMailleDomaine(BeneficiaireMailleDomaine source) {
    super(source);
    if (!CollectionUtils.isEmpty(source.getDomaineDroits())) {
      this.domaineDroits = new ArrayList<>();
      for (MailleDomaineDroit domain : source.getDomaineDroits()) {
        this.domaineDroits.add(new MailleDomaineDroit(domain));
      }
    }
  }

  public int compareTo(BeneficiaireMailleDomaine beneficiaireContract) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.appendSuper(super.compareTo(beneficiaireContract));
    compareToBuilder.append(this.domaineDroits, beneficiaireContract.domaineDroits);
    return compareToBuilder.toComparison();
  }
}
