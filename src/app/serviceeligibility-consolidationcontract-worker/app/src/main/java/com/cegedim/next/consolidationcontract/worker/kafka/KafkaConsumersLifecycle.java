package com.cegedim.next.consolidationcontract.worker.kafka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Service;

/**
 * This service allows us to manage Kafka consumer lifecycle
 *
 * @see #pauseConsummer(String)
 * @see #resumeConsumer(String)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumersLifecycle {

  private final KafkaListenerEndpointRegistry registry;

  /**
   * Pause a kafka consumer giving his id
   *
   * @param consumerId consumer Id
   */
  public void pauseConsummer(final String consumerId) {
    List<MessageListenerContainer> containers =
        (ArrayList<MessageListenerContainer>) registry.getAllListenerContainers();
    for (MessageListenerContainer container : containers) {
      String listenerId = container.getListenerId();
      if (listenerId != null && listenerId.equals(consumerId)) {
        container.pause();
      }
    }
  }

  /**
   * Resume a kafka consumer giving his id
   *
   * @param consumerId consumer Id
   */
  public void resumeConsumer(final String consumerId) {
    List<MessageListenerContainer> containers =
        (ArrayList<MessageListenerContainer>) registry.getAllListenerContainers();
    for (MessageListenerContainer container : containers) {
      String listenerId = container.getListenerId();
      if (listenerId != null && listenerId.equals(consumerId)) {
        container.resume();
      }
    }
  }

  /**
   * Get consumers statuses
   *
   * @return consumers statuses
   */
  public Map<String, String> consumerStatuses() {
    Map<String, String> statuses = new HashMap<>();
    ArrayList<MessageListenerContainer> containers =
        (ArrayList<MessageListenerContainer>) registry.getAllListenerContainers();

    for (MessageListenerContainer container : containers) {
      List<TopicPartition> topicsAssigned =
          (ArrayList<TopicPartition>) container.getAssignedPartitions();
      log.info("Partitions for {}", container.getListenerId());
      if (topicsAssigned != null) {
        for (TopicPartition topicPartition : topicsAssigned) {
          log.info("Partition {}", topicPartition.partition());
        }
      }

      statuses.put(container.getListenerId(), container.isContainerPaused() ? "Paused" : "Running");
    }

    return statuses;
  }
}
