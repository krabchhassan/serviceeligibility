package com.cegedim.beyond.undue.retention.services;

import static com.cegedim.next.serviceeligibility.core.kafka.observability.MessageType.UNDUE_RETENTION_MESSAGE_TYPE;
import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.*;

import com.cegedim.beyond.messaging.api.MessageProducerWithApiKey;
import com.cegedim.beyond.messaging.api.exception.MessageSendException;
import com.cegedim.beyond.schemas.Message;
import com.cegedim.beyond.schemas.UndueRetentionMessageDto;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.common.organisation.dto.OrganizationDto;
import com.cegedim.common.organisation.exception.OrganizationIndexInitException;
import com.cegedim.common.organisation.exception.OrganizationNotFoundException;
import com.cegedim.common.organisation.service.OrganizationService;
import com.cegedim.next.serviceeligibility.core.kafka.observability.MessageType;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RetentionMessageService {
  private final MessageProducerWithApiKey messageProducer;

  private final String topicNameUndueRetention;
  private final boolean sendUndueRetentionMessages;
  private final OrganizationService organizationService;

  public RetentionMessageService(
      MessageProducerWithApiKey messageProducer,
      BeyondPropertiesService beyondPropertiesService,
      OrganizationService organizationService) {
    this.messageProducer = messageProducer;
    topicNameUndueRetention =
        beyondPropertiesService.getProperty(KAFKA_TOPIC_BDD_UNDUE_RETENTION).orElse(null);

    sendUndueRetentionMessages =
        beyondPropertiesService
            .getBooleanProperty(SEND_UNDUE_RETENTION_MESSAGE)
            .orElse(Boolean.TRUE);
    this.organizationService = organizationService;
  }

  @ContinueSpan(log = "sendUndueRetentionMessage")
  private <T extends Message> void sendMessageUndueRetention(
      String key, T message, Map<String, Object> header) {
    try {
      if (message == null) {
        throw new MessageSendException("Message is null");
      }
      // Try to send event

      messageProducer.send(topicNameUndueRetention, null, key, message, null, header);
    } catch (MessageSendException e) {
      log.error(e.getMessage(), e);
    }
  }

  @ContinueSpan(log = "sendUndueRetentionMessage")
  public void sendUndueRetentionMessage(
      String key, UndueRetentionMessageDto undueRetentionMessageDto) {
    if (shouldStop(UNDUE_RETENTION_MESSAGE_TYPE) || undueRetentionMessageDto == null) {
      return;
    }
    Map<String, Object> header = new HashMap<>();
    try {
      OrganizationDto mainOrganization =
          organizationService.getOrganizationByAmcNumber(undueRetentionMessageDto.getInsurerId());

      OrganizationDto secondaryOrganization =
          organizationService.getOrganizationByAmcNumber(
              undueRetentionMessageDto.getIssuingCompanyCode());
      header =
          Map.of(
              Constants.KAFKA_RETENTION_HEADER_MAINORGANIZATIONCODE,
              mainOrganization.getCode(),
              Constants.KAFKA_RETENTION_HEADER_SECONDARYORGANIZATIONCODE,
              secondaryOrganization.getCode());
    } catch (OrganizationNotFoundException | OrganizationIndexInitException e) {
      log.error(e.getMessage(), e);
    }

    sendMessageUndueRetention(key, undueRetentionMessageDto, header);
  }

  private boolean shouldStop(MessageType messageType) {
    return switch (messageType) {
      case UNDUE_RETENTION_MESSAGE_TYPE -> {
        if (!sendUndueRetentionMessages) {
          log.debug(
              "Envoi de message undue retention désactivé, le message {} ne sera pas envoyé.",
              messageType);
          yield true;
        }
        yield false;
      }
      default -> false;
    };
  }
}
