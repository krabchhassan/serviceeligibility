package com.cegedim.next.core.configuration;

import com.cegedim.next.common.kafkaconnect.readiness.model.ConnectorDefinition;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.sourcelab.kafka.connect.apiclient.request.dto.NewConnectorDefinition;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

/** Static methods to read and interpret connectors configuration. */
@Slf4j
public class ConfigParser {
  private static final ObjectMapper mapper = new ObjectMapper();

  /**
   * Transforms our custom json configuration file to a valid {@link NewConnectorDefinition}
   *
   * @param targetEnv
   * @param cloudProvider
   * @param kafkaConnectorsConfiguration
   * @return
   * @throws IOException
   */
  public static Map<String, NewConnectorDefinition> initializeConnectorsMap(
      String targetEnv,
      String cloudProvider,
      KafkaConnectorsConfiguration kafkaConnectorsConfiguration,
      String isEphemeral) {
    return kafkaConnectorsConfiguration.getConnectorConfigurations().stream()
        .map(
            connectorConfiguration ->
                getJsonConfig(connectorConfiguration.getConnectorSourceFile())
                    .map(
                        rawJsonConfig ->
                            jsonTemplating(
                                rawJsonConfig,
                                connectorConfiguration,
                                kafkaConnectorsConfiguration.getElasticURL(),
                                kafkaConnectorsConfiguration.getElasticLogin(),
                                kafkaConnectorsConfiguration.getElasticPassword(),
                                targetEnv,
                                cloudProvider,
                                isEphemeral))
                    .map(ConfigParser::parseConfig)
                    .orElse(null))
        .filter(Objects::nonNull)
        .collect(Collectors.toMap(NewConnectorDefinition::getName, Function.identity()));
  }

  /**
   * Interprets some strings in the json connector configuration
   *
   * @param jsonInput
   * @param elasticsearch
   * @param configuration
   * @param env
   * @param cloudProvider
   * @return
   */
  private static String jsonTemplating(
      String jsonInput,
      KafkaConnectorsConfiguration.ConnectorsConfiguration configuration,
      String elasticsearch,
      String elasticsearchLogin,
      String elasticsearchPassword,
      String env,
      String cloudProvider,
      String isEphemeral) {

    // Topic name management
    String topicName = "";
    if (configuration.getTopicName() != null) {
      topicName = configuration.getTopicName();
      if (!configuration.getDisableTopicNameLowerCase()) {
        topicName = topicName.toLowerCase();
      }
    }
    String indexName;
    if ("true".equals(isEphemeral)) {
      indexName = "bdd-benef-" + env.toLowerCase();
    } else {
      indexName = (configuration.getIndexName() != null) ? configuration.getIndexName() : "";
    }
    String topicPrefix =
        (configuration.getTopicPrefix() != null)
            ? configuration.getTopicPrefix().toLowerCase()
            : "";
    cloudProvider = (cloudProvider != null) ? cloudProvider.toLowerCase() : "";
    env = (env != null) ? env.toLowerCase() : "";

    return jsonInput
        .replaceAll("\\$\\{CONNECTOR_NAME\\}", configuration.getConnectorName())
        .replaceAll("\\$\\{TOPIC_NAME\\}", topicName)
        .replaceAll("\\$\\{INDEX_NAME\\}", indexName)
        .replaceAll("\\$\\{TOPIC_PREFIX\\}", topicPrefix)
        .replaceAll("\\$\\{ELASTICSEARCH_URL\\}", elasticsearch)
        .replaceAll("\\$\\{ELASTICSEARCH_LOGIN\\}", elasticsearchLogin)
        .replaceAll("\\$\\{ELASTICSEARCH_PASSWORD\\}", elasticsearchPassword)
        .replaceAll("\\$\\{TARGET_ENV\\}", env)
        .replaceAll("\\$\\{CLOUD_PROVIDER\\}", cloudProvider)
        .replaceAll("\\$\\{DATABASE_URI_CONNECTION\\}", configuration.getDatabaseUriConnection())
        .replaceAll("\\$\\{DATABASE\\}", configuration.getDatabase());
  }

  /**
   * Parse json connector config to a valid {@link NewConnectorDefinition}
   *
   * @param jsonConfig
   * @return
   */
  private static NewConnectorDefinition parseConfig(String jsonConfig) {
    try {
      ConnectorDefinition connectorDefinition =
          mapper.readValue(jsonConfig, ConnectorDefinition.class);
      return NewConnectorDefinition.newBuilder()
          .withName(connectorDefinition.getName())
          .withConfig(connectorDefinition.getConfig())
          .build();
    } catch (JsonProcessingException exp) {
      log.warn("Cannot parse kafka-connect connector configuration", exp);
    }
    return null;
  }

  /**
   * Read Json from file
   *
   * @param indexFileName
   * @return
   */
  private static Optional<String> getJsonConfig(final String indexFileName) {
    final Resource resource = new ClassPathResource(indexFileName);
    try (final InputStream inputStream = resource.getInputStream()) {
      return Optional.of(StreamUtils.copyToString(inputStream, Charset.defaultCharset()));
    } catch (IOException e) {
      log.warn("Cannot read kafka-connect connector configuration", e);
    }
    return Optional.empty();
  }
}
