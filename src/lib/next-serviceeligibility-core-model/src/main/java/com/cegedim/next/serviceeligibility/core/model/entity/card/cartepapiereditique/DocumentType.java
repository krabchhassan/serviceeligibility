package com.cegedim.next.serviceeligibility.core.model.entity.card.cartepapiereditique;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
@NoArgsConstructor
public class DocumentType implements GenericDomain<DocumentType> {

  ////////////////
  // Attributes //
  ////////////////

  // Properties
  String code;
  String label;
  String communicationChannel;
  String recipientType;
  Boolean gedIndicator;

  @Override
  public int compareTo(DocumentType documentType) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.code, documentType.code);
    compareToBuilder.append(this.label, documentType.label);
    compareToBuilder.append(this.communicationChannel, documentType.communicationChannel);
    compareToBuilder.append(this.recipientType, documentType.recipientType);
    compareToBuilder.append(this.gedIndicator, documentType.gedIndicator);
    return compareToBuilder.toComparison();
  }
}
