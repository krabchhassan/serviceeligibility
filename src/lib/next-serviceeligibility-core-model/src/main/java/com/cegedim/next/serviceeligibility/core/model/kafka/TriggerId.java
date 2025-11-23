package com.cegedim.next.serviceeligibility.core.model.kafka;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TriggerId {

  public TriggerId(String triggerId) {
    this.triggerId = triggerId;
  }

  private String triggerId;
}
