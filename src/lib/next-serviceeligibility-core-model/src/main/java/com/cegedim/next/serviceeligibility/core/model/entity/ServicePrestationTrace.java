package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.model.kafka.TraceStatus;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "servicePrestationTrace")
@Data
@EqualsAndHashCode(callSuper = true)
public class ServicePrestationTrace extends DocumentEntity
    implements GenericDomain<ServicePrestationTrace> {
  private static final long serialVersionUID = 1L;

  private String originId;
  private LocalDateTime dateCreation;
  private LocalDateTime dateModification;
  private String message;
  private String contratAiId;
  private TraceStatus status;
  private String errorMessage;
  private String sourceObjectId;

  @Override
  @JsonIgnore
  public String get_id() {
    return null;
  }

  @Override
  public int compareTo(final ServicePrestationTrace servicePrestationTrace) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.originId, servicePrestationTrace.originId);
    compareToBuilder.append(this.dateCreation, servicePrestationTrace.dateCreation);
    compareToBuilder.append(this.dateModification, servicePrestationTrace.dateModification);
    compareToBuilder.append(this.message, servicePrestationTrace.message);
    compareToBuilder.append(this.contratAiId, servicePrestationTrace.contratAiId);
    compareToBuilder.append(this.status, servicePrestationTrace.status);
    compareToBuilder.append(this.errorMessage, servicePrestationTrace.errorMessage);
    compareToBuilder.append(this.sourceObjectId, servicePrestationTrace.sourceObjectId);
    return compareToBuilder.toComparison();
  }
}
