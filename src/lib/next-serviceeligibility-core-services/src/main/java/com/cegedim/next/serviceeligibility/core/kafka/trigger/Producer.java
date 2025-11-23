package com.cegedim.next.serviceeligibility.core.kafka.trigger;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.*;

import com.cegedim.beyond.messaging.api.MessageProducerWithApiKey;
import com.cegedim.beyond.messaging.api.SendResult;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.beyond.tracing.WithNewTrace;
import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.SasContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.TriggerId;
import com.cegedim.next.serviceeligibility.core.services.bdd.SasContratService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import com.cegedim.next.serviceeligibility.core.services.pojo.ConsolidationDeclarationsContratTrigger;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.TriggerUtils;
import jakarta.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Producer {
  private static final String SENDING_TO_THE_TOPIC_WITH_KEY = "sending to the topic {} with key {}";

  private final TriggerService triggerService;
  private final SasContratService sasContratService;
  private final MessageProducerWithApiKey messageProducer;
  private final BeyondPropertiesService beyondPropertiesService;

  private String topicDemandeDeclaration;
  private String topicDemandeRenouvellementDeclaration;
  private String topicConsolidationContrat;
  private String topicUpdateRenouvellementDeclaration;
  private String topicDemandeRenouvellementDeclarationUnitaire;
  private String topicDemandeAbandonTrigger;

  @PostConstruct
  private void initTopics() {
    topicDemandeDeclaration = buildTopic(KAFKA_TOPIC_BDD_DEMANDE_DECLARATION);
    topicDemandeRenouvellementDeclaration =
        buildTopic(KAFKA_TOPIC_BDD_DEMANDE_RENOUVELLEMENT_DECLARATION);
    topicConsolidationContrat = buildTopic(KAFKA_TOPIC_BDD_CONTRATTP);
    topicUpdateRenouvellementDeclaration =
        buildTopic(KAFKA_TOPIC_BDD_UPDATE_RENOUVELLEMENT_DECLARATION);
    topicDemandeRenouvellementDeclarationUnitaire =
        buildTopic(KAFKA_TOPIC_BDD_DEMANDE_RENOUVELLEMENT_DECLARATION_UNITAIRE);
    topicDemandeAbandonTrigger = buildTopic(KAFKA_TOPIC_BDD_DEMANDE_ABANDON_TRIGGER);
  }

  private String buildTopic(String propKey) {
    return beyondPropertiesService.getProperty(propKey).orElse(null);
  }

  @WithNewTrace
  public void sendContract(TriggerId triggerId, String recyclage, String key) {
    log.debug(SENDING_TO_THE_TOPIC_WITH_KEY, topicDemandeDeclaration, triggerId.getTriggerId());
    messageProducer.send(
        topicDemandeDeclaration,
        null,
        key,
        triggerId,
        null,
        Map.of(Constants.KAFKA_DEMANDE_DECLARATION_HEADER_RECYCLAGE, recyclage));
  }

  /**
   * envoi du contrat lors de la mise à jour du statut -> appel depuis le endpoint
   * DeclencheurController (ui)
   *
   * @param triggerId : id du trigger
   * @param recyclage : recyclage ou pas !
   */
  @WithNewTrace
  public void sendContract(TriggerId triggerId, String recyclage) {
    sendContract(triggerId, recyclage, null);
  }

  @WithNewTrace
  public void sendRenouvellement(String triggerId, String recyclage) {
    log.debug(SENDING_TO_THE_TOPIC_WITH_KEY, topicDemandeRenouvellementDeclaration, triggerId);
    messageProducer.send(
        topicDemandeRenouvellementDeclaration,
        null,
        null,
        new TriggerId(triggerId),
        null,
        Map.of(Constants.KAFKA_DEMANDE_DECLARATION_HEADER_RECYCLAGE, recyclage));
  }

  @WithNewTrace
  public void sendUpdateRenouvellement(
      String triggerId, String updateTrigger, Long randomRecyclingId) {
    log.debug(SENDING_TO_THE_TOPIC_WITH_KEY, topicUpdateRenouvellementDeclaration, triggerId);
    Map<String, Object> header =
        Map.of(
            Constants.KAFKA_DEMANDE_DECLARATION_HEADER_RECYCLAGE,
            Long.toString(randomRecyclingId),
            Constants.KAFKA_DEMANDE_DECLARATION_HEADER_UPDATE_TRIGGER,
            updateTrigger);
    messageProducer.send(
        topicUpdateRenouvellementDeclaration, null, null, new TriggerId(triggerId), null, header);
  }

  @WithNewTrace
  //  @Transactional
  public void sendRenouvellementUnitaire(
      TriggerBatchUnitaire triggerBatchUnitaire, Long randomRecyclingId, String updateTrigger) {
    log.debug(" sendRenouvellementUnitaire updateTrigger {}", updateTrigger);
    log.debug(" sendRenouvellementUnitaire randomRecyclingId {}", randomRecyclingId);
    log.debug(
        "sending to the topic {} for servicePrestationId {}",
        topicDemandeRenouvellementDeclarationUnitaire,
        triggerBatchUnitaire.getServicePrestationId());
    Map<String, Object> header =
        Map.of(
            Constants.KAFKA_DEMANDE_DECLARATION_HEADER_RECYCLAGE,
            Long.toString(randomRecyclingId),
            Constants.KAFKA_DEMANDE_DECLARATION_HEADER_UPDATE_TRIGGER,
            updateTrigger);

    CompletableFuture<SendResult<TriggerBatchUnitaire>> listenableFuture =
        messageProducer.send(
            topicDemandeRenouvellementDeclarationUnitaire,
            null,
            triggerBatchUnitaire.getServicePrestationId(),
            triggerBatchUnitaire,
            null,
            header);

    whenCompleteSendRenouvellementUnitaire(triggerBatchUnitaire, listenableFuture);
  }

  private void whenCompleteSendRenouvellementUnitaire(
      TriggerBatchUnitaire triggerBatchUnitaire,
      CompletableFuture<SendResult<TriggerBatchUnitaire>> listenableFuture) {
    listenableFuture.whenComplete(
        (result, error) -> {
          if (error != null) {
            if (error instanceof KafkaProducerException kafkaProducerException) {
              int compteur = 0;
              if (CollectionUtils.isNotEmpty(triggerBatchUnitaire.getTriggeredBeneficiaries())) {
                SasContrat sas = null;
                for (TriggeredBeneficiary triggeredBeneficiary :
                    triggerBatchUnitaire.getTriggeredBeneficiaries()) {
                  Anomaly anomaly =
                      compteur > 0 ? Anomaly.SAS_FOUND_FOR_THIS_CONTRACT : Anomaly.TECHNICAL_ERROR;
                  TriggeredBeneficiaryAnomaly triggeredBeneficiaryAnomaly =
                      TriggeredBeneficiaryAnomaly.create(anomaly);
                  TriggerUtils.addStatus(
                      triggeredBeneficiary,
                      TriggeredBeneficiaryStatusEnum.Error,
                      triggeredBeneficiaryAnomaly,
                      true);
                  sas =
                      sasContratService.manageSasContrat(
                          sas, triggeredBeneficiary, triggeredBeneficiaryAnomaly.getDescription());
                  compteur++;
                }
                sasContratService.save(sas);
                // nbBenefError : 1 -> on ne compte pas les sas en tant que benef KO sur le
                // trigger.
                triggerService.updateTriggeredBeneficiaries(
                    triggerBatchUnitaire.getTriggeredBeneficiaries());
              }
              log.error(
                  String.format(
                      "Send problem for message %s",
                      kafkaProducerException.getFailedProducerRecord().value()),
                  error);
            }
          } else {
            log.debug("topicDemandeRenouvellementDeclarationUnitaire result : {}", result);
          }
        });
  }

  public void sendAbandonTrigger(String triggerId) {
    log.debug(SENDING_TO_THE_TOPIC_WITH_KEY, topicDemandeAbandonTrigger, triggerId);
    messageProducer.send(topicDemandeAbandonTrigger, new TriggerId(triggerId));
  }

  public void sendDeclarationsByContratAndTrigger(
      ConsolidationDeclarationsContratTrigger consolidationDeclarationsContratTrigger, String key) {
    messageProducer.send(topicConsolidationContrat, key, consolidationDeclarationsContratTrigger);
  }
}
