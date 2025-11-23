package com.cegedim.next.serviceeligibility.core.model.domain.cartedemat;

import com.cegedim.next.serviceeligibility.core.model.domain.Beneficiaire;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.model.entity.DomaineCarte;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.util.CollectionUtils;

/** Classe qui mappe le document BenefCarteDemat */
@Data
@NoArgsConstructor
public class BenefCarteDemat implements GenericDomain<BenefCarteDemat> {

  private static final long serialVersionUID = 1L;

  private LienContrat lienContrat;
  private Beneficiaire beneficiaire;
  private List<DomaineDroit> domainesCouverture;
  private List<DomaineCarte> domainesRegroup;

  public BenefCarteDemat(BenefCarteDemat source) {
    if (source.getLienContrat() != null) {
      this.lienContrat = new LienContrat(source.getLienContrat());
    }
    if (source.getBeneficiaire() != null) {
      this.beneficiaire = new Beneficiaire(source.getBeneficiaire());
    }
    if (!CollectionUtils.isEmpty(source.getDomainesCouverture())) {
      this.domainesCouverture = new ArrayList<>();
      for (DomaineDroit domaineDroit : source.getDomainesCouverture()) {
        this.domainesCouverture.add(new DomaineDroit(domaineDroit));
      }
    }
  }

  public List<DomaineDroit> getDomainesCouverture() {
    if (this.domainesCouverture == null) {
      this.domainesCouverture = new ArrayList<>();
    }
    return this.domainesCouverture;
  }

  @Override
  public int compareTo(final BenefCarteDemat benefCarte) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.lienContrat, benefCarte.lienContrat);
    compareToBuilder.append(this.domainesCouverture, benefCarte.domainesCouverture);
    compareToBuilder.append(this.beneficiaire, benefCarte.beneficiaire);
    return compareToBuilder.toComparison();
  }
}
