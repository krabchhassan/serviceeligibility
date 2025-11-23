package com.cegedim.next.serviceeligibility.core.model.domain.contract;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.util.CollectionUtils;

@Data
@NoArgsConstructor
public class ReferenceCouverture implements GenericDomain<ReferenceCouverture>, Mergeable {
  @Field(order = 1)
  private String referenceCouverture;

  @Field(order = 2)
  private String formulaMask;

  @Field(order = 3)
  private List<NaturePrestation> naturesPrestation;

  @Override
  public int compareTo(ReferenceCouverture o) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(referenceCouverture, o.referenceCouverture);
    compareToBuilder.append(formulaMask, o.formulaMask);
    if (!CollectionUtils.isEmpty(naturesPrestation)) {
      for (NaturePrestation naturePrestation : naturesPrestation) {
        for (NaturePrestation refNaturePrestation : o.naturesPrestation) {
          if (naturePrestation
              .getNaturePrestation()
              .equals(refNaturePrestation.getNaturePrestation())) {
            compareToBuilder.append(naturePrestation, refNaturePrestation);
          }
        }
      }
    }
    return compareToBuilder.toComparison();
  }

  public ReferenceCouverture(ReferenceCouverture source) {
    this.referenceCouverture = source.referenceCouverture;
    this.formulaMask = source.formulaMask;

    if (!CollectionUtils.isEmpty(source.getNaturesPrestation())) {
      this.naturesPrestation = new ArrayList<>();
      for (NaturePrestation naturePrestation : source.getNaturesPrestation()) {
        this.naturesPrestation.add(new NaturePrestation(naturePrestation));
      }
    }
  }

  @Override
  public String mergeKey() {
    return referenceCouverture;
  }

  @Override
  public String conflictKey() {
    return "";
  }
}
