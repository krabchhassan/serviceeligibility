package com.cegedim.next.serviceeligibility.core.model.entity.card.cartepapiereditique;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
@NoArgsConstructor
public class EditingObject implements GenericDomain<EditingObject> {

  ////////////////
  // Attributes //
  ////////////////

  // Properties
  String idEditingObject;
  String stateObject;

  // Embedded documents
  DocumentType documentType;

  @Override
  public int compareTo(EditingObject editingObject) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.idEditingObject, editingObject.idEditingObject);
    return compareToBuilder.toComparison();
  }
}
