package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

/** Classe qui mappe la collection SERVICES droits dans la base de donnees. */
@Document(collection = "services")
@Data
@EqualsAndHashCode(callSuper = false)
public class ServiceDroits extends DocumentEntity implements GenericDomain<ServiceDroits> {

  private static final long serialVersionUID = 1L;
  private String code;
  private int triRestitution;
  private List<String> transco;
  private boolean serviceFictif;
  private String typeService;
  private ControleContextuel controleContextuel;

  @Override
  public int compareTo(final ServiceDroits serviceDroits) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.code, serviceDroits.code);
    compareToBuilder.append(this.transco, serviceDroits.transco);
    return compareToBuilder.toComparison();
  }
}
