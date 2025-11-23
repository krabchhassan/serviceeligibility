package com.cegedim.next.common.kafkaconnect.readiness;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConnectorScheduler {

  private final KafkaConnectorHealthService kafkaConnectorHealthService;

  public KafkaConnectorScheduler(KafkaConnectorHealthService kafkaConnectorHealthService) {
    this.kafkaConnectorHealthService = kafkaConnectorHealthService;
  }

  @Scheduled(fixedRate = 30000)
  public Health health() {
    try {
      log.debug("Start kafka connector scheduler");
      Health.Builder builder = kafkaConnectorHealthService.doHealth();
      log.debug("End kafka connector scheduler {}", builder.build());
      return builder.build();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return Health.down().withException(e).build();
    }
  }
}
