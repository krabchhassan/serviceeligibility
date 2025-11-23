package com.cegedim.next.serviceeligibility.core.model.domain.contract;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

/** Classe qui mappe la collection Contrats-AMCexclues dans la base de donnees. */
@Document(collection = "Contrats-AMCexclues")
@Data
@EqualsAndHashCode(callSuper = false)
public class ContratsAMCexclues extends DocumentEntity
    implements GenericDomain<ContratsAMCexclues> {

  /* PROPRIETES */

  private List<AmcExclues> listeAMC;

  @Override
  public int compareTo(ContratsAMCexclues contratsAMCexclues) {

    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.listeAMC, contratsAMCexclues.listeAMC);
    return compareToBuilder.toComparison();
  }
}
