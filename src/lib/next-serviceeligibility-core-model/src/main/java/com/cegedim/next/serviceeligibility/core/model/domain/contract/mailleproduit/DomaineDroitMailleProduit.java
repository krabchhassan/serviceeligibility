package com.cegedim.next.serviceeligibility.core.model.domain.contract.mailleproduit;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.DomaineDroitContractTPCommun;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.util.CollectionUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class DomaineDroitMailleProduit extends DomaineDroitContractTPCommun {
  private List<GarantieMailleProduit> garanties = new ArrayList<>();

  public DomaineDroitMailleProduit() {
    /* empty constructor */ }

  public DomaineDroitMailleProduit(DomaineDroitMailleProduit source) {
    super(source);

    if (!CollectionUtils.isEmpty(source.getGaranties())) {
      this.garanties = new ArrayList<>();
      for (GarantieMailleProduit garantie : source.getGaranties()) {
        this.garanties.add(new GarantieMailleProduit(garantie));
      }
    }
  }

  public int compareTo(DomaineDroitMailleProduit domaineDroitContract) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.appendSuper(super.compareTo(domaineDroitContract));
    compareToBuilder.append(this.garanties, domaineDroitContract.garanties);
    return compareToBuilder.toComparison();
  }
}
