package com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class CreationResponse {
  private String traceid;
  private String status;
  private List<String> errorMessages;
  private String provider;
}
