package com.cegedim.next.serviceeligibility.core.model.domain.contract.maillerefcouverture;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.GarantieCommun;
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
public class GarantieMailleRefCouv extends GarantieCommun {
  private List<ProduitMailleRefCouv> produits = new ArrayList<>();

  public int compareTo(GarantieMailleRefCouv o) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.appendSuper(super.compareTo(o));
    if (!CollectionUtils.isEmpty(produits)) {
      compareToBuilder.append(produits, o.produits);
    }
    return compareToBuilder.toComparison();
  }

  public GarantieMailleRefCouv(GarantieMailleRefCouv source) {
    super(source);
    if (!CollectionUtils.isEmpty(source.getProduits())) {
      this.produits = new ArrayList<>();
      for (ProduitMailleRefCouv produit : source.getProduits()) {
        this.produits.add(new ProduitMailleRefCouv(produit));
      }
    }
  }
}
