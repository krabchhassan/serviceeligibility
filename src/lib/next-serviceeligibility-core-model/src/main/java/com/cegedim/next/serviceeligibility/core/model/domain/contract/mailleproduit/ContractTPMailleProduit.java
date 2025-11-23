package com.cegedim.next.serviceeligibility.core.model.domain.contract.mailleproduit;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTPCommun;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.util.CollectionUtils;

/** Contrat TP à la maille Produit */
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractTPMailleProduit extends ContractTPCommun {

  private List<BeneficiaireMailleProduit> beneficiaires;

  public ContractTPMailleProduit() {
    /* empty constructor */ }

  public ContractTPMailleProduit(ContractTPMailleProduit source) {
    super(source);
    if (!CollectionUtils.isEmpty(source.getBeneficiaires())) {
      this.beneficiaires = new ArrayList<>();
      for (BeneficiaireMailleProduit benef : source.getBeneficiaires()) {
        this.beneficiaires.add(new BeneficiaireMailleProduit(benef));
      }
    }
  }

  public int compareTo(final ContractTPMailleProduit contrat) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.appendSuper(super.compareTo(contrat));
    compareToBuilder.append(this.beneficiaires, contrat.beneficiaires);
    return compareToBuilder.toComparison();
  }
}
