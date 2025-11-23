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

@Document(collection = "servicePrestIJTrace")
@Data
@EqualsAndHashCode(callSuper = true)
public class ServicePrestIJTrace extends DocumentEntity
    implements GenericDomain<ServicePrestIJTrace> {
  private static final long serialVersionUID = 1L;

  private LocalDateTime dateCreation;
  private LocalDateTime updateDate;
  private String message;
  private TraceStatus status;

  @Override
  @JsonIgnore
  public String get_id() {
    return null;
  }

  @Override
  public int compareTo(final ServicePrestIJTrace servicePrestIJTrace) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.dateCreation, servicePrestIJTrace.dateCreation);
    compareToBuilder.append(this.updateDate, servicePrestIJTrace.updateDate);
    compareToBuilder.append(this.message, servicePrestIJTrace.message);
    compareToBuilder.append(this.status, servicePrestIJTrace.status);
    return compareToBuilder.toComparison();
  }
}
