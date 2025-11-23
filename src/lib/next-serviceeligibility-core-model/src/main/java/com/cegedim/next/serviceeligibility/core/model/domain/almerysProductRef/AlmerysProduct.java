package com.cegedim.next.serviceeligibility.core.model.domain.almerysProductRef;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

/** Objet de representation d'un produit Almerys dans la collection almerysProduct */
@Document(collection = "almerysProduct")
@Data
public class AlmerysProduct implements GenericDomain<AlmerysProduct> {
  @NotNull private String code;
  @NotNull private String description;
  private List<ProductCombination> productCombinations;

  @Override
  public int compareTo(AlmerysProduct almerysProduct) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.code, almerysProduct.code);
    compareToBuilder.append(this.description, almerysProduct.description);
    compareToBuilder.append(this.productCombinations, almerysProduct.productCombinations);
    return compareToBuilder.toComparison();
  }
}
