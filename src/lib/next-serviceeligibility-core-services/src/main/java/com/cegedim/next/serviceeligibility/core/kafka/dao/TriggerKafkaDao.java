package com.cegedim.next.serviceeligibility.core.kafka.dao;

import com.cegedim.next.serviceeligibility.core.kafka.trigger.Producer;
import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.SasContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.TriggerId;
import com.cegedim.next.serviceeligibility.core.services.bdd.SasContratService;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.TriggerException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.TriggerNotFoundException;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Iterator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class TriggerKafkaDao {

  @Autowired private MongoTemplate template;

  @Autowired private Producer producer;

  @Autowired private SasContratService sasContratService;

  @Autowired
  @Qualifier("bddAuth")
  private AuthenticationFacade authenticationFacade;

  @ContinueSpan(log = "updateStatus trigger")
  public void updateStatus(String id, TriggerStatus statut) {
    Trigger trigger = template.findById(id, Trigger.class, Constants.TRIGGER);
    if (trigger == null) {
      String message =
          String.format("Aucun déclencheur trouvé avec les informations suivantes : id=%s", id);
      log.debug(message);
      throw new TriggerNotFoundException(message);
    }
    if ((statut.equals(TriggerStatus.Deleted) || statut.equals(TriggerStatus.ToProcess))
        && trigger.getStatus().equals(TriggerStatus.StandBy)) {
      trigger.setStatus(statut);
      trigger.setDateModification(LocalDateTime.now(ZoneOffset.UTC));
      trigger.setUserModification(authenticationFacade.getAuthenticationUserName());
      template.save(trigger, Constants.TRIGGER);
      if (statut.equals(TriggerStatus.ToProcess)) {
        // send to kafka (renouvellement)
        producer.sendRenouvellement(id, "false");
      }
    } else if (statut.equals(TriggerStatus.ToProcess)
        && trigger.getStatus().equals(TriggerStatus.ProcessedWithErrors)) {
      changeSasContratsStatusRecycle(trigger);
      trigger.setStatus(statut);
      trigger.setDateModification(LocalDateTime.now(ZoneOffset.UTC));
      trigger.setUserModification(authenticationFacade.getAuthenticationUserName());
      template.save(trigger, Constants.TRIGGER);
      if (TriggerEmitter.Renewal.equals(trigger.getOrigine())) {
        producer.sendRenouvellement(id, "true");
      } else {
        producer.sendContract(new TriggerId(id), "true");
      }
    } else if (statut.equals(TriggerStatus.Abandoning)
        && trigger.getStatus().equals(TriggerStatus.ProcessedWithErrors)) {
      log.debug("Starting abandon");
      trigger.setStatus(statut);
      trigger.setDateModification(LocalDateTime.now(ZoneOffset.UTC));
      trigger.setUserModification(authenticationFacade.getAuthenticationUserName());
      template.save(trigger, Constants.TRIGGER);

      if (TriggerEmitter.Renewal.equals(trigger.getOrigine())) {
        producer.sendAbandonTrigger(trigger.getId());
      } else {
        // clean sas
        sasContratService.abandonTrigger(trigger.getId());
        // Fin de l'abandon, on change le status a abandonne
        trigger.setStatus(TriggerStatus.Abandonned);
        template.save(trigger, Constants.TRIGGER);
        log.debug("Abandon complete");
      }
    } else {
      String message =
          String.format(
              "La mise à jour du statut du déclencheur ne peut pas se faire de %s vers %s.",
              trigger.getStatus().toString(), statut);
      log.debug(message);
      throw new TriggerException(message);
    }
  }

  /** Les SasContrats liés au trigger demandé en recyclage sont passés en recycling */
  @ContinueSpan(log = "changeCorrelatedTriggerStatusRecycle")
  private void changeSasContratsStatusRecycle(Trigger mainTrigger) {
    List<SasContrat> sasContrats = sasContratService.getByIdTrigger(mainTrigger.getId());

    boolean alreadyRecycling = sasContrats.stream().anyMatch(SasContrat::isRecycling);
    if (alreadyRecycling) {
      throw new TriggerException(
          "Impossible de lancer le recyclage. Un des SasContrats liés au déclencheur "
              + mainTrigger.getId()
              + " est déjà en cours de recyclage");
    }

    sasContrats.forEach(sas -> sasContratService.updateRecycling(sas.getId(), true));
  }

  // send à false pour Junit uniquement (sinon, risque de OOM)
  @ContinueSpan(log = "getTriggersBatchUnitaireAndSend")
  public int getTriggersBatchUnitaireAndSend(
      String idTrigger,
      Long randomRecyclingId,
      List<TriggerBatchUnitaire> triggerBatchUnitaireList) {
    Query q = new Query();
    int nombreTriggerUnitaire = 0;
    Criteria c = new Criteria().and(Constants.ID_TRIGGER).is(idTrigger);
    if (randomRecyclingId != 0L) {
      c.and(Constants.STATUT).is(TriggeredBeneficiaryStatusEnum.Error);
    }
    q.addCriteria(c).with(Sort.by(Sort.Direction.ASC, Constants.SERVICE_PRESTATION_ID));
    int compteur = 0;
    try {
      // bizarre, l'erreur n'est pas remonté sur le try with resources.
      Iterator<TriggeredBeneficiary> benefs =
          template.stream(q, TriggeredBeneficiary.class, Constants.TRIGGERED_BENEFICIARY)
              .iterator();
      String previousServicePrestationId = "-1";
      TriggerBatchUnitaire triggerBatchUnitaire = new TriggerBatchUnitaire();
      while (benefs.hasNext()) {
        TriggeredBeneficiary benef = benefs.next();
        // TriggerUtils.addStatus(benef, TriggeredBeneficiaryStatusEnum.ToProcess, null, true);
        template.save(benef, Constants.TRIGGERED_BENEFICIARY);
        log.debug("id du triggerBenef {}", benef.getId());
        String currentServicePrestationId = benef.getServicePrestationId();
        // Changement de servicePrestation on crée un nouveau
        // TriggerBatchUnitaire
        if (!currentServicePrestationId.equals(previousServicePrestationId)) {
          boolean updateTrigger = compteur % 50 == 0 || !benefs.hasNext();
          log.debug(
              "currentServicePrestationId : {}, est ce qu'il reste encore des benefs : {},  mise à jour du trigger : {}",
              currentServicePrestationId,
              benefs.hasNext(),
              updateTrigger);
          nombreTriggerUnitaire +=
              sendTriggersBatchUnitaire(
                  randomRecyclingId,
                  triggerBatchUnitaireList,
                  previousServicePrestationId,
                  triggerBatchUnitaire,
                  updateTrigger
                      ? Constants.KAFKA_DEMANDE_DECLARATION_HEADER_UPDATE_TRIGGER_INPROGRESS
                      : Constants.KAFKA_DEMANDE_DECLARATION_HEADER_NO_UPDATE);
          triggerBatchUnitaire = new TriggerBatchUnitaire();
          triggerBatchUnitaire.setTriggerId(idTrigger);
          triggerBatchUnitaire.setServicePrestationId(currentServicePrestationId);
          triggerBatchUnitaire.getTriggeredBeneficiaries().add(benef);
        }
        // Même servicePrestation on ajoute le benef à la liste
        else {
          triggerBatchUnitaire.getTriggeredBeneficiaries().add(benef);
        }
        previousServicePrestationId = currentServicePrestationId;
        compteur++;
      }
      log.debug("dernier ServicePrestationId : {}", previousServicePrestationId);
      // Ajout du dernier TriggerBatchUnitaire
      nombreTriggerUnitaire +=
          sendTriggersBatchUnitaire(
              randomRecyclingId,
              triggerBatchUnitaireList,
              previousServicePrestationId,
              triggerBatchUnitaire,
              Constants.KAFKA_DEMANDE_DECLARATION_HEADER_UPDATE_LAST_TRIGGERBENEF);
    } catch (Exception e) { // NOSONAR
      log.error("error while streaming: {}", e.getMessage(), e);
      throw e;
    }
    return nombreTriggerUnitaire;
  }

  private int sendTriggersBatchUnitaire(
      Long randomRecyclingId,
      List<TriggerBatchUnitaire> triggerBatchUnitaireList,
      String previousServicePrestationId,
      TriggerBatchUnitaire triggerBatchUnitaire,
      String updateTrigger) {
    if (!previousServicePrestationId.equals("-1")) {
      if (triggerBatchUnitaireList != null) {
        triggerBatchUnitaireList.add(triggerBatchUnitaire);
      } else {
        producer.sendRenouvellementUnitaire(triggerBatchUnitaire, randomRecyclingId, updateTrigger);
      }
      return 1;
    }
    return 0;
  }
}
