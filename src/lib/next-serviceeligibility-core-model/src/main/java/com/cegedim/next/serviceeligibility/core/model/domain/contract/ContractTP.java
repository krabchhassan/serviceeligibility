package com.cegedim.next.serviceeligibility.core.model.domain.contract;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.CollectionUtils;

/** Classe qui mappe la collection contrats */
@Document(collection = "contratsTP")
@Data
public class ContractTP extends ContractTPCommun {

  private List<BeneficiaireContractTP> beneficiaires;

  public ContractTP() {
    /* empty constructor */ }

  public ContractTP(ContractTP source) {
    super(source);
    if (!CollectionUtils.isEmpty(source.getBeneficiaires())) {
      this.beneficiaires = new ArrayList<>();
      for (BeneficiaireContractTP benef : source.getBeneficiaires()) {
        this.beneficiaires.add(new BeneficiaireContractTP(benef));
      }
    }
  }

  public int compareTo(final ContractTP contrat) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.appendSuper(super.compareTo(contrat));
    compareToBuilder.append(this.beneficiaires, contrat.beneficiaires);
    return compareToBuilder.toComparison();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ContractTP that = (ContractTP) o;
    return StringUtils.equals(getIdDeclarant(), that.getIdDeclarant())
        && StringUtils.equals(getNumeroContrat(), that.getNumeroContrat())
        && StringUtils.equals(getNumeroAdherent(), that.getNumeroAdherent());
  }
}
