package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

/** Classe qui mappe la collection PARAMETRES dans la base de donnees. */
@Document(collection = "parametres")
@Data
@EqualsAndHashCode(callSuper = false)
public class ParametreBdd extends DocumentEntity implements GenericDomain<ParametreBdd> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String code;
  private List<Object> listeValeurs;

  @Override
  public int compareTo(ParametreBdd parametre) {

    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.code, parametre.code);
    compareToBuilder.append(this.listeValeurs, parametre.listeValeurs);
    return compareToBuilder.toComparison();
  }
}
