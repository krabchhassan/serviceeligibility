package com.cegedim.next.serviceeligibility.core.model.domain.contract.maillegarantie;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.DomaineDroitContractTPCommun;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.util.CollectionUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class DomaineDroitMailleGarantie extends DomaineDroitContractTPCommun {
  private List<MailleGarantie> garanties = new ArrayList<>();

  public DomaineDroitMailleGarantie() {
    /* empty constructor */ }

  public DomaineDroitMailleGarantie(DomaineDroitMailleGarantie source) {
    super(source);

    if (!CollectionUtils.isEmpty(source.getGaranties())) {
      this.garanties = new ArrayList<>();
      for (MailleGarantie garantie : source.getGaranties()) {
        this.garanties.add(new MailleGarantie(garantie));
      }
    }
  }

  public int compareTo(DomaineDroitMailleGarantie domaineDroitContract) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.appendSuper(super.compareTo(domaineDroitContract));
    compareToBuilder.append(this.garanties, domaineDroitContract.garanties);
    return compareToBuilder.toComparison();
  }
}
