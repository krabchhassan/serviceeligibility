package com.cegedim.next.core.configuration;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/** This allow to retrieve data from application.yml */
@ConfigurationProperties(prefix = "next.common.kafkaconnect.connectors")
@Data
public class KafkaConnectorsConfiguration {

  // added default values to namespaces shortnames just in case
  private String connectorURL = "http://kafka-connect-service:8083/";
  private String elasticURL = "http://elasticsearch:9200";
  private String elasticLogin;
  private String elasticPassword;

  private List<ConnectorsConfiguration> connectorConfigurations = new ArrayList<>();

  @Data
  public static class ConnectorsConfiguration {
    private String connectorName;
    private String connectorSourceFile;
    private String topicName;
    private Boolean disableTopicNameLowerCase = false;
    private String indexName;
    private String topicPrefix;
    private Boolean mongoConnector = false;
    private String databaseUriConnection;
    private String database;
  }
}
