package com.cegedim.next.serviceeligibility.core.model.entity.card.cartepapiereditique;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CartePapier;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

/** Classe qui mappe la collection cartesPapier dans la base de données */
@Document(collection = "cartesPapier")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CartePapierEditique extends DocumentEntity
    implements GenericDomain<CartePapierEditique> {

  private static final long serialVersionUID = 1L;

  ////////////////
  // Attributes //
  ////////////////

  // Properties

  // Embedded documents
  CartePapier cartePapier;
  EditingObject editingObject;
  Internal internal;

  @Override
  public int compareTo(CartePapierEditique cartePapierEditique) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.cartePapier, cartePapierEditique.cartePapier);
    compareToBuilder.append(this.editingObject, cartePapierEditique.editingObject);
    compareToBuilder.append(this.internal, cartePapierEditique.internal);
    return compareToBuilder.toComparison();
  }
}
