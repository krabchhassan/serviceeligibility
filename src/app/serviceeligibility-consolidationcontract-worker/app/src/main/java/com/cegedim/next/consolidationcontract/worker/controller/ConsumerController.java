package com.cegedim.next.consolidationcontract.worker.controller;

import com.cegedim.next.consolidationcontract.worker.kafka.KafkaConsumersLifecycle;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("consumer")
@RequiredArgsConstructor
public class ConsumerController {

  private final KafkaConsumersLifecycle kafkaConsumersLifecycle;

  @PutMapping("/{consumerId}/pause")
  @NewSpan
  public ResponseEntity<Void> pauseConsumer(@PathVariable String consumerId) {

    kafkaConsumersLifecycle.pauseConsummer(consumerId);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PutMapping("/{consumerId}/resume")
  @NewSpan
  public ResponseEntity<Void> resumeConsumer(@PathVariable String consumerId) {

    kafkaConsumersLifecycle.resumeConsumer(consumerId);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/status")
  @NewSpan
  public ResponseEntity<Map<String, String>> status() {
    return new ResponseEntity<>(kafkaConsumersLifecycle.consumerStatuses(), HttpStatus.OK);
  }
}
