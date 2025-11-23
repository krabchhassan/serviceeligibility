package com.cegedim.next.serviceeligibility.core.model.domain.contract.maillerefcouverture;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.ProduitCommun;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.util.CollectionUtils;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProduitMailleRefCouv extends ProduitCommun {
  private List<MailleReferenceCouverture> referencesCouverture = new ArrayList<>();

  public int compareTo(ProduitMailleRefCouv o) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.appendSuper(super.compareTo(o));
    if (!CollectionUtils.isEmpty(referencesCouverture)) {
      compareToBuilder.append(referencesCouverture, o.getReferencesCouverture());
    }
    return compareToBuilder.toComparison();
  }

  public ProduitMailleRefCouv(ProduitMailleRefCouv source) {
    super(source);
    if (!CollectionUtils.isEmpty(source.getReferencesCouverture())) {
      this.referencesCouverture = new ArrayList<>();
      for (MailleReferenceCouverture mailleReferenceCouverture : source.getReferencesCouverture()) {
        this.referencesCouverture.add(new MailleReferenceCouverture(mailleReferenceCouverture));
      }
    }
  }
}
