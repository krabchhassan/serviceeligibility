package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.domain.HistoriqueTransco;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

/** Classe qui mappe la collection historiqueTranscodage dans la base de donnees. */
@Document(collection = "historiqueTranscodage")
@Data
@EqualsAndHashCode(callSuper = false)
public class HistoriqueTranscodage extends DocumentEntity
    implements GenericDomain<HistoriqueTranscodage> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String codeService;
  private String codeObjetTransco;

  /* TRACE */

  private Date dateCreation;
  private String userCreation;
  private Date dateModification;
  private String userModification;

  /* DOCUMENTS EMBEDDED */

  private List<HistoriqueTransco> transcoList;

  @Override
  public int compareTo(HistoriqueTranscodage histTransco) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.codeService, histTransco.codeService);
    compareToBuilder.append(this.codeObjetTransco, histTransco.codeObjetTransco);
    compareToBuilder.append(this.transcoList, histTransco.transcoList);
    return compareToBuilder.toComparison();
  }
}
