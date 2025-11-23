package com.cegedim.next.serviceeligibility.core.model.domain.contract.mailledomaine;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTPCommun;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.util.CollectionUtils;

/** Contrat TP à la maille DomaineDroit */
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractTPMailleDomaine extends ContractTPCommun {

  private List<BeneficiaireMailleDomaine> beneficiaires;

  public ContractTPMailleDomaine() {
    /* empty constructor */ }

  public ContractTPMailleDomaine(ContractTPMailleDomaine source) {
    super(source);
    if (!CollectionUtils.isEmpty(source.getBeneficiaires())) {
      this.beneficiaires = new ArrayList<>();
      for (BeneficiaireMailleDomaine benef : source.getBeneficiaires()) {
        this.beneficiaires.add(new BeneficiaireMailleDomaine(benef));
      }
    }
  }

  public int compareTo(final ContractTPMailleDomaine contrat) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.appendSuper(super.compareTo(contrat));
    compareToBuilder.append(this.beneficiaires, contrat.beneficiaires);
    return compareToBuilder.toComparison();
  }
}
