package com.cegedim.next.common.kafkaconnect.readiness;

import com.cegedim.next.common.kafkaconnect.readiness.model.ConnectorHealth;
import com.cegedim.next.core.configuration.ConfigParser;
import com.cegedim.next.core.configuration.KafkaConnectorsConfiguration;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.sourcelab.kafka.connect.apiclient.Configuration;
import org.sourcelab.kafka.connect.apiclient.KafkaConnectClient;
import org.sourcelab.kafka.connect.apiclient.request.dto.ConnectorStatus;
import org.sourcelab.kafka.connect.apiclient.request.dto.NewConnectorDefinition;
import org.sourcelab.kafka.connect.apiclient.rest.exceptions.ResourceNotFoundException;
import org.springframework.boot.actuate.health.Health;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Asserts that connectors are alive and running.
 *
 * <p>It also creates connectors when they are missing and restart tasks when not running.
 */
@Slf4j
public class KafkaConnectorHealthService {

  public static final String PROXY_SCHEME = "http";
  private final Map<String, NewConnectorDefinition> connectorDefinitionMap;
  private final KafkaConnectClient kafkaConnectClient;

  public KafkaConnectorHealthService(
      final String targetEnv,
      final String cloudProvider,
      final KafkaConnectorsConfiguration kafkaConnectorsConfiguration,
      final String isEphemeral)
      throws IOException {
    connectorDefinitionMap =
        ConfigParser.initializeConnectorsMap(
            targetEnv, cloudProvider, kafkaConnectorsConfiguration, isEphemeral);
    kafkaConnectClient =
        initializeKafkaConnectClient(kafkaConnectorsConfiguration.getConnectorURL());
  }

  /**
   * List the health of every connector configured in my microservice. This method was refactored to
   * not only probes connectors but also to try to fix any problem. It's a two step process:
   *
   * <ul>
   *   <li>Lists, all connectors health. Are they present ? Are they correctly running ?
   *   <li>For all connectors in error (missing or not running) an attempt to fix them is done
   * </ul>
   *
   * @return
   */
  public Health.Builder doHealth() {
    final Health.Builder builder = Health.up();

    // 1 List the health of every connector configured in my microservice.
    Map<String, ConnectorHealth> connectorsHealthsResults =
        connectorDefinitionMap.values().stream()
            .map(this::connectorHealthCheck)
            .collect(Collectors.toMap(ConnectorHealth::getConnectorName, Function.identity()));

    // Is there any in "error" ? (missing or not running)
    Map<String, String> errorsDetails = extractErrors(connectorsHealthsResults);

    builder.withDetails(connectorsHealthsResults);
    if (errorsDetails.isEmpty()) {
      // no errors -> all details are reported and work is done
      return builder.up();
    } else {
      // at least one error -> attempting to fix them
      connectorsHealthsResults.values().stream()
          .filter(connectorHealth -> !connectorHealth.getErrors().isEmpty())
          .forEach(
              connectorToFix ->
                  fixConnector(connectorDefinitionMap.get(connectorToFix.getConnectorName())));
      // all errors found are still reported.
      // Next health call, all should be ok.
      return builder.down();
    }
  }

  /**
   * Checks if the given connector is present and running, a detailed report is given in return
   *
   * @param connectorDefinition
   * @return
   */
  private ConnectorHealth connectorHealthCheck(final NewConnectorDefinition connectorDefinition) {
    String connectorName = connectorDefinition.getName();
    ConnectorStatus connectorStatus;
    try {
      connectorStatus = kafkaConnectClient.getConnectorStatus(connectorName);
    } catch (ResourceNotFoundException rnfe) {
      log.error("Connector {} is missing", connectorName);
      return DetailsGenerator.generateErrorDetailsWhenMissing(connectorName);
    } catch (Exception e) {
      log.error("Unexpected error", e);
      return DetailsGenerator.generateErrorDetails(connectorName, e.getMessage());
    }
    // happy path
    return DetailsGenerator.generateConnectorDetails(connectorStatus);
  }

  /**
   * Attempts to fix a broken connector by: Creating a missing connector OR restarting his tasks to
   * set it in a RUNNING state.
   *
   * @param connectorDefinition
   */
  private void fixConnector(final NewConnectorDefinition connectorDefinition) {
    Assert.notNull(connectorDefinition, "Impossible to fix connector, parameter is null");
    String connectorName = connectorDefinition.getName();
    log.info("Try to fix connector {}", connectorDefinition.getName());
    try {
      // 1 find missing connector and create them
      Collection<String> connectors = kafkaConnectClient.getConnectors();
      if (!connectors.contains(connectorName)) {
        // create the missing connector
        log.info(
            "Connector is missing, connector to create name: {} with config: {}",
            connectorName,
            connectorDefinition.getConfig());
        kafkaConnectClient.addConnector(connectorDefinition);
        log.info("Connector {} created", connectorName);
      } else {
        // 2 check tasks status
        ConnectorStatus connectorStatus = kafkaConnectClient.getConnectorStatus(connectorName);
        if (!ConnectorUtils.isConnectorStatusOk(connectorStatus)) {
          log.warn("Connector {} not running, restarting tasks", connectorStatus.getName());
          boolean restartsOk =
              connectorStatus.getTasks().stream()
                  .map(taskStatus -> restartConnectorTask(connectorName, taskStatus.getId()))
                  .map(CompletableFuture::join)
                  .allMatch(b -> Boolean.TRUE == b);
          if (restartsOk) {
            log.info("All restart tasks done for connector {}", connectorName);
          } else {
            log.warn("Restarts are not correctly done from connector {}", connectorName);
          }
        }
      }
    } catch (final Exception e) {
      log.error("Unexpected error during attempt to fix the connector {} ", connectorName, e);
    }
  }

  private CompletableFuture<Boolean> restartConnectorTask(String name, int taskId) {
    log.info("Connector {} taskId {} is beeing restarted", name, taskId);
    return CompletableFuture.supplyAsync(
        () -> kafkaConnectClient.restartConnectorTask(name, taskId));
  }

  /**
   * Strips the "/connectors" part if present and initialize kafka connect client.
   *
   * @param url
   * @return
   */
  private KafkaConnectClient initializeKafkaConnectClient(String url) {
    int i = url.indexOf("/connectors");
    String baseUrl = i == -1 ? url : url.substring(0, i);
    Configuration kafkaConnectConfiguration = new Configuration(baseUrl);
    // proxy configuration
    configureProxy(kafkaConnectConfiguration);
    return new KafkaConnectClient(kafkaConnectConfiguration);
  }

  /**
   * set proxy if necessary.
   *
   * @param configuration
   * @return
   */
  private Configuration configureProxy(Configuration configuration) {
    String proxyHost = System.getProperty("http.proxyHost");
    Integer proxyPort = -1;
    try {
      proxyPort = Integer.parseInt(System.getProperty("http.proxyPort"));
    } catch (NumberFormatException nbfe) {
      // do nothing
    }
    if (!StringUtils.isEmpty(proxyHost) && proxyPort != -1) {
      log.debug("proxy configuration set host: {}, port: {}", proxyHost, proxyPort);
      return configuration.useProxy(proxyHost, proxyPort, PROXY_SCHEME);
    }
    return configuration;
  }

  private Map<String, String> extractErrors(Map<String, ConnectorHealth> connectorsHealthsResults) {
    return connectorsHealthsResults.values().stream()
        .filter(connectorHealth -> !connectorHealth.getErrors().isEmpty())
        .flatMap(connectorHealth -> connectorHealth.getErrors().entrySet().stream())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }
}
