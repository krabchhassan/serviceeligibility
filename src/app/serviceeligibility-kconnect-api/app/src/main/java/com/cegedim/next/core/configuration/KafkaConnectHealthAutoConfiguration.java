package com.cegedim.next.core.configuration;

import com.cegedim.next.common.kafkaconnect.readiness.KafkaConnectorHealthService;
import com.cegedim.next.common.kafkaconnect.readiness.KafkaConnectorScheduler;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({KafkaConnectorsConfiguration.class})
@Slf4j
public class KafkaConnectHealthAutoConfiguration {

  @Bean
  public KafkaConnectorScheduler kafkaConnectorScheduler(
      final KafkaConnectorHealthService kafkaConnectorHealthService) {
    return new KafkaConnectorScheduler(kafkaConnectorHealthService);
  }

  @Bean
  public KafkaConnectorHealthService kafkaConnectorHealthService(
      @Value("${TARGET_ENV:not-configured}") final String targetEnv,
      @Value("${CLOUD_PROVIDER:not-configured}") final String cloudProvider,
      final KafkaConnectorsConfiguration configuration,
      @Value("${IS_EPHEMERAL}") String isEphemeral)
      throws IOException {
    return new KafkaConnectorHealthService(targetEnv, cloudProvider, configuration, isEphemeral);
  }
}
