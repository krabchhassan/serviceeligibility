package com.cegedim.next.serviceeligibility.core.model.domain.contract.maillerefcouverture;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.DomaineDroitContractTPCommun;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.util.CollectionUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class DomaineDroitMailleRefCouv extends DomaineDroitContractTPCommun {
  private List<GarantieMailleRefCouv> garanties = new ArrayList<>();

  public DomaineDroitMailleRefCouv() {
    /* empty constructor */ }

  public DomaineDroitMailleRefCouv(DomaineDroitMailleRefCouv source) {
    super(source);

    if (!CollectionUtils.isEmpty(source.getGaranties())) {
      this.garanties = new ArrayList<>();
      for (GarantieMailleRefCouv garantie : source.getGaranties()) {
        this.garanties.add(new GarantieMailleRefCouv(garantie));
      }
    }
  }

  public int compareTo(DomaineDroitMailleRefCouv domaineDroitContract) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.appendSuper(super.compareTo(domaineDroitContract));
    compareToBuilder.append(this.garanties, domaineDroitContract.garanties);
    return compareToBuilder.toComparison();
  }
}
