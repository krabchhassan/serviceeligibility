package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

/** Classe qui mappe la collection SERVICES droits DCLBEN dans la base de donnees. */
@Document(collection = "serviceDCLBEN")
@Data
@EqualsAndHashCode(callSuper = false)
public class ServiceDroitsDCLBEN extends DocumentEntity
    implements GenericDomain<ServiceDroitsDCLBEN> {
  private static final long serialVersionUID = 1L;
  private String code;
  private int triRestitution;
  private List<String> transco;
  private boolean serviceFictif;
  private String typeService;
  private ControleContextuelDCLBEN controleContextuel;

  @Override
  public int compareTo(ServiceDroitsDCLBEN serviceDroitsDCLBEN) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.code, serviceDroitsDCLBEN.code);
    compareToBuilder.append(this.transco, serviceDroitsDCLBEN.transco);
    return compareToBuilder.toComparison();
  }
}
