package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Slf4j
@NoArgsConstructor
@Document(collection = "historiqueExecutions")
@EqualsAndHashCode(callSuper = false)
public abstract class HistoriqueExecutions<T> extends DocumentEntity implements GenericDomain<T> {

  @JsonProperty(value = "Batch")
  protected String Batch;

  protected Date dateExecution;
  protected long nbDeclarationATraiter;

  public abstract void clear();

  public abstract void log();

  public abstract int getNbDeclarationLue();
}
