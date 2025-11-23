package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

/** Classe qui mappe la collection VOLUMETRIE dans la base de donnees. */
@Document(collection = "volumetrie")
@Data
@EqualsAndHashCode(callSuper = false)
public class Volumetrie extends DocumentEntity implements GenericDomain<Volumetrie> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String idDeclarant;
  private Date dateEffet;
  private Long declarations;
  private Long personnes;
  private Long personnesDroitsOuverts;

  @Override
  public int compareTo(Volumetrie volumetrie) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.idDeclarant, volumetrie.idDeclarant);
    compareToBuilder.append(this.dateEffet, volumetrie.dateEffet);
    compareToBuilder.append(this.declarations, volumetrie.declarations);
    compareToBuilder.append(this.personnes, volumetrie.personnes);
    compareToBuilder.append(this.personnesDroitsOuverts, volumetrie.personnesDroitsOuverts);
    return compareToBuilder.toComparison();
  }
}
