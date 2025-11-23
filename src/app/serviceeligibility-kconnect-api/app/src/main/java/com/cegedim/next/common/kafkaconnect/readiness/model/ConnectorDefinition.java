package com.cegedim.next.common.kafkaconnect.readiness.model;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copy of {@link org.sourcelab.kafka.connect.apiclient.request.dto.ConnectorDefinition} with a
 * private constructor (for serialization)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class ConnectorDefinition {
  private String name;
  private Map<String, String> config;
}
