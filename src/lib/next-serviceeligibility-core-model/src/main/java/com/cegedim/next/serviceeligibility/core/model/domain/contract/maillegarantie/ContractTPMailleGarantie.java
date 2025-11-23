package com.cegedim.next.serviceeligibility.core.model.domain.contract.maillegarantie;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTPCommun;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.util.CollectionUtils;

/** Contrat TP à la maille Garantie */
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractTPMailleGarantie extends ContractTPCommun {

  private List<BeneficiaireMailleGarantie> beneficiaires;

  public ContractTPMailleGarantie() {
    /* empty constructor */ }

  public ContractTPMailleGarantie(ContractTPMailleGarantie source) {
    super(source);
    if (!CollectionUtils.isEmpty(source.getBeneficiaires())) {
      this.beneficiaires = new ArrayList<>();
      for (BeneficiaireMailleGarantie benef : source.getBeneficiaires()) {
        this.beneficiaires.add(new BeneficiaireMailleGarantie(benef));
      }
    }
  }

  public int compareTo(final ContractTPMailleGarantie contrat) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.appendSuper(super.compareTo(contrat));
    compareToBuilder.append(this.beneficiaires, contrat.beneficiaires);
    return compareToBuilder.toComparison();
  }
}
