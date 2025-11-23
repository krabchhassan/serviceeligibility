package com.cegedim.next.common.kafkaconnect.readiness;

import com.cegedim.next.common.kafkaconnect.readiness.model.ConnectorHealth;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.sourcelab.kafka.connect.apiclient.request.dto.ConnectorStatus;

/** Generates details for the actuator/health page. */
@Slf4j
public class DetailsGenerator {

  private static final String CONNECTOR = "connector.";

  public static ConnectorHealth generateErrorDetailsWhenMissing(final String connectorName) {
    final ConnectorHealth health = new ConnectorHealth(connectorName);
    Map<String, String> errors = health.getErrors();
    String prefixKey = CONNECTOR + connectorName;
    errors.put(prefixKey, String.format("The connector %s is missing", connectorName));
    return health;
  }

  public static ConnectorHealth generateErrorDetails(
      final String connectorName, String errorMessage) {
    final ConnectorHealth health = new ConnectorHealth(connectorName);
    Map<String, String> errors = health.getErrors();
    String prefixKey = CONNECTOR + connectorName;
    errors.put(prefixKey, String.format("Unexpected error: %s", errorMessage));
    return health;
  }

  public static ConnectorHealth generateConnectorDetails(final ConnectorStatus connectorStatus) {
    final ConnectorHealth health = new ConnectorHealth(connectorStatus.getName());
    Map<String, String> details = health.getDetails();
    Map<String, String> errors = health.getErrors();
    String prefixKey = CONNECTOR + connectorStatus.getName();
    details.put(prefixKey + ".name", connectorStatus.getName());
    details.put(prefixKey + ".connector", connectorStatus.getConnector().toString());
    details.put(prefixKey + ".tasks", connectorStatus.getTasks().toString());
    details.put(prefixKey + ".type", connectorStatus.getType());
    // check case where connector is not okay
    if (!ConnectorUtils.isConnectorStatusOk(connectorStatus)) {
      log.warn("Connector state not ok, check connector.state and tasks.state {}", connectorStatus);
      errors.put(
          prefixKey + ".status",
          "Please check connector state and tasks state, should be in 'RUNNING' state");
    } else if (connectorStatus.getTasks().isEmpty()) {
      log.warn("Connector without tasks {}", connectorStatus);
      errors.put(
          prefixKey + ".tasks",
          String.format("No tasks are running for the connector %s", connectorStatus.getName()));
    }
    return health;
  }
}
