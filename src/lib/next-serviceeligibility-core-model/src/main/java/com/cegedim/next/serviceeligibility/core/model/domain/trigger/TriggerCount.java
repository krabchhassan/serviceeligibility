package com.cegedim.next.serviceeligibility.core.model.domain.trigger;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "triggerCount")
@Data
public class TriggerCount {

  @Id private String id;

  private int total;

  private int count;

  private int status;
}
