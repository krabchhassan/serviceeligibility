package com.cegedim.next.common.kafkaconnect.readiness;

import org.sourcelab.kafka.connect.apiclient.request.dto.ConnectorStatus;

public class ConnectorUtils {

  // kafka connect property status
  public static final String STATE_PROPERTY = "state";
  public static final String STATE_RUNNING = "RUNNING";

  public static boolean isConnectorStatusOk(ConnectorStatus connectorStatus) {
    boolean isConnectorRunning =
        STATE_RUNNING.equalsIgnoreCase(connectorStatus.getConnector().get(STATE_PROPERTY));
    boolean isTasksRunning =
        connectorStatus.getTasks().stream()
            .map(ConnectorStatus.TaskStatus::getState)
            .allMatch(STATE_RUNNING::equalsIgnoreCase);
    return isConnectorRunning && isTasksRunning;
  }
}
