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
public class Garantie extends GarantieCommun {
  @Field(order = 8)
  private List<Produit> produits;

  public int compareTo(Garantie o) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.appendSuper(super.compareTo(o));
    if (!CollectionUtils.isEmpty(produits)) {
      compareToBuilder.append(produits, o.produits);
    }
    return compareToBuilder.toComparison();
  }

  public Garantie(Garantie source) {
    super(source);
    if (!CollectionUtils.isEmpty(source.getProduits())) {
      this.produits = new ArrayList<>();
      for (Produit produit : source.getProduits()) {
        this.produits.add(new Produit(produit));
      }
    }
  }
}
