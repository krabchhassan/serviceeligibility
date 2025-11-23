package com.cegedim.next.serviceeligibility.core.model.domain.contract;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.util.CollectionUtils;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Produit extends ProduitCommun {
  @Field(order = 7)
  private List<ReferenceCouverture> referencesCouverture;

  public int compareTo(Produit o) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.appendSuper(super.compareTo(o));
    if (!CollectionUtils.isEmpty(referencesCouverture)) {
      compareToBuilder.append(referencesCouverture, o.referencesCouverture);
    }
    return compareToBuilder.toComparison();
  }

  public Produit(Produit source) {
    super(source);
    if (!CollectionUtils.isEmpty(source.getReferencesCouverture())) {
      this.referencesCouverture = new ArrayList<>();
      for (ReferenceCouverture referenceCouverture : source.getReferencesCouverture()) {
        this.referencesCouverture.add(new ReferenceCouverture(referenceCouverture));
      }
    }
  }
}
