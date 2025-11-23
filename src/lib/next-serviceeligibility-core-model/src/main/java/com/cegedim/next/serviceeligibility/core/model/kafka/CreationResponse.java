package com.cegedim.next.serviceeligibility.core.model.kafka;

import lombok.Data;

@Data
public class CreationResponse {
  private String traceid;
  private String status;
  private String errorMessage;
}
