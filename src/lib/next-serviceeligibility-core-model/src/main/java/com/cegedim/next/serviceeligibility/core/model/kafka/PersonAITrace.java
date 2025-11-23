package com.cegedim.next.serviceeligibility.core.model.kafka;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "personsTrace")
@Data
public class PersonAITrace {
  @Id private String id;
  private String originId;
  private LocalDateTime dateCreation;
  private LocalDateTime dateModification;
  private String message;
  private String personAiId;
  private TraceStatus status;
  private String errorMessage;
}
