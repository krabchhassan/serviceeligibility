package com.cegedim.next.serviceeligibility.core.services.message;

import static com.cegedim.next.serviceeligibility.core.kafka.observability.MessageType.RECIPIENT_REQUEST_MESSAGE_TYPE;
import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.*;

import com.cegedim.beyond.messaging.api.MessageProducerWithApiKey;
import com.cegedim.beyond.messaging.api.exception.MessageSendException;
import com.cegedim.beyond.schemas.Message;
import com.cegedim.beyond.schemas.RecipientsMessageDto;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.kafka.observability.MessageType;
import io.micrometer.tracing.annotation.ContinueSpan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageService {
  private final MessageProducerWithApiKey messageProducer;

  private final String topicName;
  private final boolean sendRecipientMessages;

  public MessageService(
      MessageProducerWithApiKey messageProducer, BeyondPropertiesService beyondPropertiesService) {
    this.messageProducer = messageProducer;
    topicName =
        beyondPropertiesService.getProperty(KAFKA_TOPIC_BDD_RECIPIENTS_REQUEST).orElse(null);

    sendRecipientMessages =
        beyondPropertiesService.getBooleanProperty(SEND_RECIPIENT_MESSAGE).orElse(Boolean.TRUE);
  }

  @ContinueSpan(log = "sendRecipientMessage")
  public <T extends Message> void sendMessage(String key, T message) {
    try {
      if (message == null) {
        throw new MessageSendException("Message is null");
      }
      // Try to send event
      messageProducer.send(topicName, key, message);
    } catch (MessageSendException e) {
      log.error(e.getMessage(), e);
    }
  }

  @ContinueSpan(log = "sendRecipientBenefitsMessage")
  public void sendRecipientBenefitsMessage(String key, RecipientsMessageDto recipientsMessageDto) {
    if (shouldStop(RECIPIENT_REQUEST_MESSAGE_TYPE) || recipientsMessageDto == null) {
      return;
    }
    sendMessage(key, recipientsMessageDto);
  }

  private boolean shouldStop(MessageType messageType) {
    return switch (messageType) {
      case RECIPIENT_REQUEST_MESSAGE_TYPE -> {
        if (!sendRecipientMessages) {
          log.debug(
              "Envoi de message recipient désactivé, le message {} ne sera pas envoyé.",
              messageType);
          yield true;
        }
        yield false;
      }
      default -> false;
    };
  }
}
