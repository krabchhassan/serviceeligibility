package com.cegedim.next.serviceeligibility.core.model.domain.contract.maillerefcouverture;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTPCommun;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.util.CollectionUtils;

/** Contrat TP à la maille Reference Couverture */
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractTPMailleRefCouv extends ContractTPCommun {

  private List<BeneficiaireMailleRefCouv> beneficiaires;

  public ContractTPMailleRefCouv() {
    /* empty constructor */ }

  public ContractTPMailleRefCouv(ContractTPMailleRefCouv source) {
    super(source);
    if (!CollectionUtils.isEmpty(source.getBeneficiaires())) {
      this.beneficiaires = new ArrayList<>();
      for (BeneficiaireMailleRefCouv benef : source.getBeneficiaires()) {
        this.beneficiaires.add(new BeneficiaireMailleRefCouv(benef));
      }
    }
  }

  public int compareTo(final ContractTPMailleRefCouv contrat) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.appendSuper(super.compareTo(contrat));
    compareToBuilder.append(this.beneficiaires, contrat.beneficiaires);
    return compareToBuilder.toComparison();
  }
}
