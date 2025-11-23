package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

/** Classe qui mappe la collection transcoParametrage dans la base de donnees. */
@Document(collection = "transcoParametrage")
public class TranscoParametrage extends DocumentEntity
    implements GenericDomain<TranscoParametrage> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  @Getter @Setter private String codeObjetTransco;

  @Getter @Setter private String nomObjetTransco;

  @Getter @Setter private List<String> colNames;

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((codeObjetTransco == null) ? 0 : codeObjetTransco.hashCode());
    result = prime * result + ((colNames == null) ? 0 : colNames.hashCode());
    result = prime * result + ((nomObjetTransco == null) ? 0 : nomObjetTransco.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    TranscoParametrage other = (TranscoParametrage) obj;
    if (codeObjetTransco == null) {
      if (other.codeObjetTransco != null) return false;
    } else if (!codeObjetTransco.equals(other.codeObjetTransco)) return false;
    if (colNames == null) {
      if (other.colNames != null) return false;
    } else if (!colNames.equals(other.colNames)) return false;
    if (nomObjetTransco == null) {
      if (other.nomObjetTransco != null) return false;
    } else if (!nomObjetTransco.equals(other.nomObjetTransco)) return false;
    return true;
  }

  @Override
  public int compareTo(final TranscoParametrage transcoParametrage) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.codeObjetTransco, transcoParametrage.codeObjetTransco);
    compareToBuilder.append(this.nomObjetTransco, transcoParametrage.nomObjetTransco);
    compareToBuilder.append(this.colNames, transcoParametrage.colNames);
    return compareToBuilder.toComparison();
  }
}
