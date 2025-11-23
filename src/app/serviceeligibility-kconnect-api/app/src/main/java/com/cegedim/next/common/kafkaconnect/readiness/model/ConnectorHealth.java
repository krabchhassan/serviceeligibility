package com.cegedim.next.common.kafkaconnect.readiness.model;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConnectorHealth {
  private String connectorName;
  private Map<String, String> details = new HashMap<>();
  private Map<String, String> errors = new HashMap<>();

  public ConnectorHealth(String connectorName) {
    this.connectorName = connectorName;
  }
}
