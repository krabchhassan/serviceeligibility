package com.cegedim.next.serviceeligibility.core.model.domain.contract;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.util.CollectionUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class DomaineDroitContractTP extends DomaineDroitContractTPCommun {
  @Field(order = 6)
  private List<Garantie> garanties;

  public DomaineDroitContractTP() {
    /* empty constructor */ }

  public DomaineDroitContractTP(DomaineDroitContractTP source) {
    super(source);

    if (!CollectionUtils.isEmpty(source.getGaranties())) {
      this.garanties = new ArrayList<>();
      for (Garantie garantie : source.getGaranties()) {
        this.garanties.add(new Garantie(garantie));
      }
    }
  }

  public int compareTo(DomaineDroitContractTP domaineDroitContract) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.appendSuper(super.compareTo(domaineDroitContract));
    compareToBuilder.append(this.garanties, domaineDroitContract.garanties);
    return compareToBuilder.toComparison();
  }
}
