package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

/** Classe qui mappe la collection CIRCUITS dans la base de donnees. */
@Document(collection = "circuits")
@Data
@EqualsAndHashCode(callSuper = false)
public class Circuit extends DocumentEntity implements GenericDomain<Circuit> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String codeCircuit;
  private String libelleCircuit;
  private String emetteur;

  @Override
  public int compareTo(Circuit circuit) {

    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.codeCircuit, circuit.codeCircuit);
    compareToBuilder.append(this.libelleCircuit, circuit.libelleCircuit);
    compareToBuilder.append(this.emetteur, circuit.emetteur);
    return compareToBuilder.toComparison();
  }
}
