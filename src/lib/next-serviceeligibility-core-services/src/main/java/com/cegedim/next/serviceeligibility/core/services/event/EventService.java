package com.cegedim.next.serviceeligibility.core.services.event;

import static com.cegedim.next.serviceeligibility.core.model.kafka.EventType.*;
import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.SEND_EVENTS;

import com.cegedim.beyond.messaging.api.events.producer.BusinessEventProducer;
import com.cegedim.beyond.messaging.api.exception.MessageSendException;
import com.cegedim.beyond.schemas.*;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.model.crex.JobRecalculBlbEndEventDto;
import com.cegedim.next.serviceeligibility.core.model.domain.BeneficiaireV2;
import com.cegedim.next.serviceeligibility.core.model.domain.Contrat;
import com.cegedim.next.serviceeligibility.core.model.domain.almerysProductRef.AlmerysProduct;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.Trigger;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiary;
import com.cegedim.next.serviceeligibility.core.model.entity.BddsToBlbTracking;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.DeclarationConsolide;
import com.cegedim.next.serviceeligibility.core.model.entity.Retention;
import com.cegedim.next.serviceeligibility.core.model.entity.RetentionHistorique;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CartePapier;
import com.cegedim.next.serviceeligibility.core.model.kafka.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.Nir;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.AssureCommun;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.ContratAICommun;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DataAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DestinatairePrestations;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodeSuspension;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.prestij.PrestIJ;
import com.cegedim.next.serviceeligibility.core.services.pojo.ConsolidationDeclarationsContratTrigger;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.DeclarationConsolideUtils;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiConsumer;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventService {
  private final BusinessEventProducer businessEventProducer;
  private final boolean sendEvents;

  public EventService(
      BusinessEventProducer businessEventProducer,
      BeyondPropertiesService beyondPropertiesService) {
    this.businessEventProducer = businessEventProducer;
    sendEvents = beyondPropertiesService.getBooleanProperty(SEND_EVENTS).orElse(Boolean.TRUE);
  }

  @ContinueSpan(log = "sendObservabilityEvent")
  public <T extends Event> void sendObservabilityEvent(T event) {
    try {
      if (event == null) {
        throw new MessageSendException("Event is null");
      }

      // Try to send event
      businessEventProducer.send(event);
    } catch (MessageSendException e) {
      log.error(e.getMessage(), e);
    }
  }

  /***************
   * Beneficiary *
   ***************/
  @ContinueSpan(log = "sendObservabilityEventBeneficiaryCreation")
  public void sendObservabilityEventBeneficiaryCreation(BenefAIV5 benef) {
    if (shouldStop(benef, BENEFICIARY_EVENT_CREATION)) {
      return;
    }

    DataAssure data = null;
    if (!CollectionUtils.isEmpty(benef.getContrats())) {
      data = benef.getContrats().getFirst().getData();
    }
    IdentiteContrat id = benef.getIdentite();
    String service =
        !CollectionUtils.isEmpty(benef.getServices()) ? benef.getServices().getFirst() : "";
    NomAssure nom = data != null ? data.getNom() : null;
    Nir nir = id != null ? id.getNir() : null;
    Amc amc = benef.getAmc();

    BeneficiaryCreationEventDto beneficiaryCreationEventDto =
        new BeneficiaryCreationEventDto()
            .withService(service)
            .withKey(benef.getKey())
            .withLastName(nom != null ? nom.getNomFamille() : "")
            .withFirstName(nom != null ? nom.getPrenom() : "")
            .withBirthDate(id != null ? id.getDateNaissance() : "")
            .withBirthRank(id != null ? id.getRangNaissance() : "")
            .withNir(nir != null ? nir.getCode() : "")
            .withDeclarantId(amc != null ? amc.getIdDeclarant() : "")
            .withSubscriberNumber(benef.getNumeroAdherent());

    sendObservabilityEvent(beneficiaryCreationEventDto);
  }

  @ContinueSpan(log = "sendObservabilityEventBeneficiaryModification")
  public void sendObservabilityEventBeneficiaryModification(BenefAIV5 benef) {
    if (shouldStop(benef, BENEFICIARY_EVENT_MODIFICATION)) {
      return;
    }

    DataAssure data = benef.getContrats().getFirst().getData();
    IdentiteContrat id = benef.getIdentite();
    String service =
        !CollectionUtils.isEmpty(benef.getServices()) ? benef.getServices().getFirst() : "";
    NomAssure nom = data != null ? data.getNom() : null;
    Nir nir = id != null ? id.getNir() : null;
    Amc amc = benef.getAmc();

    BeneficiaryModificationEventDto beneficiaryCreationEventDto =
        new BeneficiaryModificationEventDto()
            .withService(service)
            .withKey(benef.getKey())
            .withLastName(nom != null ? nom.getNomFamille() : "")
            .withFirstName(nom != null ? nom.getPrenom() : "")
            .withBirthDate(id != null ? id.getDateNaissance() : "")
            .withBirthRank(id != null ? id.getRangNaissance() : "")
            .withNir(nir != null ? nir.getCode() : "")
            .withDeclarantId(amc != null ? amc.getIdDeclarant() : "")
            .withSubscriberNumber(benef.getNumeroAdherent());

    sendObservabilityEvent(beneficiaryCreationEventDto);
  }

  // BENEFICIARY NO SEND TO BLB
  @ContinueSpan(log = "sendObservabilityEventBeneficiaryNoSend")
  public void sendObservabilityEventBeneficiaryNoSend(BddsToBlbTracking bddstoBlb) {
    if (shouldStop(bddstoBlb, BENEFICIARY_SENDING_BLB_EROROR_EVENT)) {
      return;
    }
    BeneficiarySendingBlbErrorEventDto beneficiarySendingBlbErrorEventDto =
        new BeneficiarySendingBlbErrorEventDto()
            .withNir(bddstoBlb.getNir())
            .withDateNaissance(bddstoBlb.getDateNaissance())
            .withRangNaissance(bddstoBlb.getRangNaissance())
            .withCodeErreur(bddstoBlb.getErrorCode())
            .withLabelErreur(bddstoBlb.getErrorLabel());

    sendObservabilityEvent(beneficiarySendingBlbErrorEventDto);
  }

  /************
   * Contract *
   ************/
  @ContinueSpan(log = "sendObservabilityEventContractReception")
  public void sendObservabilityEventContractReception(ContratAICommun contract) {
    if (shouldStop(contract, CONTRACT_EVENT_RECEPTION)) {
      return;
    }

    ContractReceptionEventDto event =
        new ContractReceptionEventDto()
            .withTraceId(contract.getTraceId() != null ? contract.getTraceId() : "")
            .withNumber(contract.getNumero() != null ? contract.getNumero() : "")
            .withDeclarantId(contract.getIdDeclarant() != null ? contract.getIdDeclarant() : "")
            .withSubscriberNumber(
                contract.getNumeroAdherent() != null ? contract.getNumeroAdherent() : "");

    sendObservabilityEvent(event);
  }

  @ContinueSpan(log = "sendObservabilityEventContractReceptionInvalid")
  public void sendObservabilityEventContractReceptionInvalid(
      ContratAICommun contract, String error) {
    if (shouldStop(contract, CONTRACT_EVENT_INVALID)) {
      return;
    }

    ContractInvalidReceptionEventDto event =
        new ContractInvalidReceptionEventDto()
            .withTraceId(contract.getTraceId() != null ? contract.getTraceId() : "")
            .withNumber(contract.getNumero() != null ? contract.getNumero() : "")
            .withDeclarantId(contract.getIdDeclarant() != null ? contract.getIdDeclarant() : "")
            .withSubscriberNumber(
                contract.getNumeroAdherent() != null ? contract.getNumeroAdherent() : "");

    putIfNotNull(event, ContractInvalidReceptionEventDto::withError, error);

    sendObservabilityEvent(event);
  }

  @ContinueSpan(log = "sendObservabilityEventContractSuspension")
  public void sendObservabilityEventContractSuspension(
      ContratAIV6 oldContract, ContratAIV6 newContract) {
    if (shouldStop(newContract, CONTRACT_EVENT_SUSPENSION)) {
      return;
    }

    PeriodeSuspension periode =
        !CollectionUtils.isEmpty(newContract.getPeriodesSuspension())
            ? newContract.getPeriodesSuspension().getFirst()
            : null;

    if (periode == null && oldContract != null) {
      periode =
          CollectionUtils.isNotEmpty(oldContract.getPeriodesSuspension())
              ? oldContract.getPeriodesSuspension().getFirst()
              : null;
    }

    if (periode == null) {
      return;
    }

    ContractSuspensionEventDto event =
        new ContractSuspensionEventDto()
            .withContractId(newContract.getId())
            .withStartDate(periode.getPeriode().getDebut())
            .withTypeOfSuspension(periode.getTypeSuspension())
            .withMotiveSuspension(Objects.requireNonNullElse(periode.getMotifSuspension(), ""));

    sendObservabilityEvent(event);
  }

  @ContinueSpan(log = "sendObservabilityEventContractSuspensionLifted")
  public void sendObservabilityEventContractSuspensionLifted(
      ContratAIV6 oldContract, ContratAIV6 newContract) {
    if (shouldStop(newContract, CONTRACT_EVENT_SUSPENSION_LIFTED)) {
      return;
    }

    PeriodeSuspension periode =
        !CollectionUtils.isEmpty(newContract.getPeriodesSuspension())
            ? newContract.getPeriodesSuspension().getFirst()
            : null;

    if (periode == null && oldContract != null) {
      periode =
          !CollectionUtils.isEmpty(oldContract.getPeriodesSuspension())
              ? oldContract.getPeriodesSuspension().getFirst()
              : null;
    }

    if (periode == null) {
      return;
    }

    ContractSuspensionLiftedEventDto event =
        new ContractSuspensionLiftedEventDto()
            .withContractId(newContract.getId())
            .withStartDate(periode.getPeriode().getDebut())
            .withTypeOfSuspension(periode.getTypeSuspension())
            .withMotiveSuspension(Objects.requireNonNullElse(periode.getMotifSuspension(), ""))
            .withEndDate(
                Objects.requireNonNullElse(
                    periode.getPeriode().getFin(),
                    DateUtils.dateMinusOneDay(periode.getPeriode().getDebut())))
            .withMotiveSuspensionLift(
                Objects.requireNonNullElse(periode.getMotifLeveeSuspension(), ""));

    sendObservabilityEvent(event);
  }

  @ContinueSpan(log = "sendObservabilityEventContractCreation")
  public void sendObservabilityEventContractCreation(ContratAICommun contract) {
    if (shouldStop(contract, CONTRACT_EVENT_CREATION)) {
      return;
    }

    ContractCreationEventDto event =
        new ContractCreationEventDto()
            .withContractId(contract.getId())
            .withNumber(contract.getNumero())
            .withSubscriberNumber(contract.getNumeroAdherent())
            .withDeclarantId(contract.getIdDeclarant())
            .withTraceId(contract.getTraceId());

    sendObservabilityEvent(event);
  }

  @ContinueSpan(log = "sendObservabilityEventContractModification")
  public void sendObservabilityEventContractModification(ContratAICommun contract) {
    if (shouldStop(contract, CONTRACT_EVENT_MODIFICATION)) {
      return;
    }

    ContractModificationEventDto event =
        new ContractModificationEventDto()
            .withContractId(contract.getId())
            .withNumber(contract.getNumero())
            .withSubscriberNumber(contract.getNumeroAdherent())
            .withDeclarantId(contract.getIdDeclarant())
            .withTraceId(contract.getTraceId());

    sendObservabilityEvent(event);
  }

  @ContinueSpan(log = "sendObservabilityEventContractDelete")
  public void sendObservabilityEventContractDelete(ContratAICommun contract) {
    if (shouldStop(contract, CONTRACT_EVENT_DELETE)) {
      return;
    }

    ContractSuppressionEventDto event =
        new ContractSuppressionEventDto()
            .withNumber(contract.getNumero())
            .withDeclarantId(contract.getIdDeclarant())
            .withTraceId(contract.getTraceId());

    sendObservabilityEvent(event);
  }

  /***********
   * Insured *
   ***********/
  @ContinueSpan(log = "sendObservabilityEventInsured")
  public void sendObservabilityEventInsured(String contractId) {
    if (shouldStop(contractId, INSURED_EVENT_RECEPTION)) {
      return;
    }

    InsuredReceptionEventDto event = new InsuredReceptionEventDto().withContractId(contractId);

    sendObservabilityEvent(event);
  }

  @ContinueSpan(log = "sendObservabilityEventInsuredInvalid")
  public void sendObservabilityEventInsuredInvalid(
      String idDeclarant,
      String numeroContrat,
      String dateNaissance,
      String rangNaissance,
      String error) {
    if (shouldStop(numeroContrat, INSURED_EVENT_INVALID)) {
      return;
    }

    InsuredInvalidReceptionEventDto event =
        new InsuredInvalidReceptionEventDto()
            .withDeclarantId(idDeclarant)
            .withContractNumber(numeroContrat)
            .withBirthDateAndRank(dateNaissance + rangNaissance)
            .withError(error);

    sendObservabilityEvent(event);
  }

  @ContinueSpan(log = "sendObservabilityEventInsuredInvalid")
  public void sendObservabilityEventInsuredInvalid(
      String idDeclarant, String numeroContrat, String numeroPersonne, String error) {
    if (shouldStop(numeroContrat, INSURED_EVENT_INVALID)) {
      return;
    }

    InsuredInvalidReceptionEventDto event =
        new InsuredInvalidReceptionEventDto()
            .withDeclarantId(idDeclarant)
            .withContractNumber(numeroContrat)
            .withPersonNumber(numeroPersonne)
            .withError(error);

    sendObservabilityEvent(event);
  }

  @ContinueSpan(log = "sendObservabilityEventInsuredTermination")
  public void sendObservabilityEventInsuredTermination(
      ContratAIV6 contract, AssureCommun assure, String previousEndDate, String newEndDate) {
    if (shouldStop(contract, INSURED_EVENT_TERMINATION)) {
      return;
    }

    InsuredTerminationEventDto event =
        new InsuredTerminationEventDto()
            .withInsurerId(contract.getIdDeclarant())
            .withIssuingCompanyCode(contract.getSocieteEmettrice())
            .withSubscriberNumber(contract.getNumeroAdherent())
            .withContractNumber(contract.getNumero())
            .withPersonNumber(assure.getIdentite().getNumeroPersonne());

    if (assure.getIdentite().getNir() != null) {
      event.withNir(assure.getIdentite().getNir().getCode());
    }

    if (!CollectionUtils.isEmpty(assure.getIdentite().getAffiliationsRO())) {
      List<AffiliationRO> affiliationROList = new ArrayList<>();
      for (NirRattachementRO nirRattachementRO : assure.getIdentite().getAffiliationsRO()) {
        AffiliationRO affiliationRO = new AffiliationRO();
        com.cegedim.beyond.schemas.Nir nir = new com.cegedim.beyond.schemas.Nir();
        nir.withCode(nirRattachementRO.getNir().getCode())
            .withKey(nirRattachementRO.getNir().getCle());
        if (nirRattachementRO.getRattachementRO() != null) {
          AttachementRO attachementRO = new AttachementRO();
          putIfNotNull(
              attachementRO,
              AttachementRO::withCenterCode,
              nirRattachementRO.getRattachementRO().getCodeCentre());
          putIfNotNull(
              attachementRO,
              AttachementRO::withRegimeCode,
              nirRattachementRO.getRattachementRO().getCodeRegime());
          putIfNotNull(
              attachementRO,
              AttachementRO::withHealthInsuranceCompanyCode,
              nirRattachementRO.getRattachementRO().getCodeCaisse());

          affiliationRO.withAttachementRO(attachementRO);
        }
        Period period = new Period().withStart(nirRattachementRO.getPeriode().getDebut());
        putIfNotNull(period, Period::withEnd, nirRattachementRO.getPeriode().getFin());

        affiliationRO.withNir(nir);
        affiliationRO.withPeriod(period);
        affiliationROList.add(affiliationRO);
        event.withAffiliationsRO(affiliationROList);
      }
    }

    event
        .withBirthDate(assure.getIdentite().getDateNaissance())
        .withBirthRank(assure.getIdentite().getRangNaissance());
    putIfNotNull(event, InsuredTerminationEventDto::withPreviousEndDate, previousEndDate)
        .withNewEndDate(newEndDate);

    sendObservabilityEvent(event);
  }

  /***********
   * PrestIJ *
   ***********/
  @ContinueSpan(log = "sendObservabilityEventPrestijReception")
  public void sendObservabilityEventPrestijReception(PrestIJ prestij) {
    if (shouldStop(prestij, PRESTIJ_EVENT_RECEPTION)) {
      return;
    }

    PrestijReceptionEventDto event =
        new PrestijReceptionEventDto().withTraceId(prestij.getTraceId());

    sendObservabilityEvent(event);
  }

  @ContinueSpan(log = "sendObservabilityEventPrestijReceptionInvalid")
  public void sendObservabilityEventPrestijReceptionInvalid(PrestIJ prestij, String error) {
    if (shouldStop(prestij, PRESTIJ_EVENT_INVALID)) {
      return;
    }

    PrestijInvalidReceptionEventDto event =
        new PrestijInvalidReceptionEventDto().withTraceId(prestij.getTraceId()).withError(error);

    sendObservabilityEvent(event);
  }

  @ContinueSpan(log = "sendObservabilityEventPrestijCreation")
  public void sendObservabilityEventPrestijCreation(PrestIJ prestij) {
    if (shouldStop(prestij, PRESTIJ_EVENT_CREATION)) {
      return;
    }

    PrestijCreationEventDto event =
        new PrestijCreationEventDto()
            .withId(prestij.get_id())
            .withNumber(prestij.getContrat().getNumero())
            .withSubscriberNumber(prestij.getContrat().getNumeroAdherent())
            .withDeclarantId(prestij.getOc().getIdClientBO())
            .withTraceId(prestij.getTraceId());

    sendObservabilityEvent(event);
  }

  @ContinueSpan(log = "sendObservabilityEventPrestijModification")
  public void sendObservabilityEventPrestijModification(PrestIJ prestij) {
    if (shouldStop(prestij, PRESTIJ_EVENT_MODIFICATION)) {
      return;
    }

    PrestijModificationEventDto event =
        new PrestijModificationEventDto()
            .withId(prestij.get_id())
            .withNumber(prestij.getContrat().getNumero())
            .withSubscriberNumber(prestij.getContrat().getNumeroAdherent())
            .withDeclarantId(prestij.getOc().getIdClientBO())
            .withTraceId(prestij.getTraceId());

    sendObservabilityEvent(event);
  }

  /***************
   * Declaration *
   ***************/
  @ContinueSpan(log = "sendObservabilityEventDeclarationCreation")
  public void sendObservabilityEventDeclarationCreation(Declaration declaration) {
    if (shouldStop(declaration, DECLARATION_EVENT_CREATION)) {
      return;
    }

    Contrat contrat = declaration.getContrat();
    BeneficiaireV2 beneficiaire = declaration.getBeneficiaire();

    DeclarationCreationEventDto event =
        new DeclarationCreationEventDto().withDeclarantId(declaration.getIdDeclarant());
    if (contrat != null) {
      event.withSubscriberNumber(
          contrat.getNumeroAdherent() != null ? contrat.getNumeroAdherent() : "");
      event.withContractNumber(contrat.getNumero() != null ? contrat.getNumero() : "");
    }
    event.withPersonNumber(beneficiaire != null ? beneficiaire.getNumeroPersonne() : "");

    if (beneficiaire != null) {
      putIfNotNull(event, DeclarationCreationEventDto::withNir, getNir(beneficiaire));
      event.withBirthDate(
          beneficiaire.getDateNaissance() != null ? beneficiaire.getDateNaissance() : "");
      event.withBirthRank(
          beneficiaire.getRangNaissance() != null ? beneficiaire.getRangNaissance() : "");
    }
    event.withCodeEtat(declaration.getCodeEtat() != null ? declaration.getCodeEtat() : "");
    putIfNotNull(
        event, DeclarationCreationEventDto::withFileName, declaration.getNomFichierOrigine());

    sendObservabilityEvent(event);
  }

  private static String getNir(BeneficiaireV2 beneficiaire) {
    if (StringUtils.isNotBlank(beneficiaire.getNirBeneficiaire())) {
      return beneficiaire.getNirBeneficiaire();
    } else if (StringUtils.isNotBlank(beneficiaire.getNirOd1())) {
      return beneficiaire.getNirOd1();
    } else if (StringUtils.isNotBlank(beneficiaire.getNirOd2())) {
      return beneficiaire.getNirOd2();
    }
    return null;
  }

  @ContinueSpan(log = "sendObservabilityEventContractRibModification")
  public void sendObservabilityEventContractRibModification(
      String idDeclarant,
      DestinatairePrestations oldDestinataire,
      DestinatairePrestations newDestinataire,
      String numeroPersonne) {
    if (!sendEvents) {
      return;
    }
    InsuredPaymentRecipientModificationEventDto event =
        new InsuredPaymentRecipientModificationEventDto()
            .withDeclarantId(idDeclarant)
            .withOldPaymentRecipientId(oldDestinataire.getIdDestinatairePaiements())
            .withOldBeyondPaymentRecipientId(oldDestinataire.getIdBeyondDestinatairePaiements())
            .withNewPaymentRecipientId(newDestinataire.getIdDestinatairePaiements())
            .withNewBeyondPaymentRecipientId(newDestinataire.getIdBeyondDestinatairePaiements())
            .withReceptionDate(DateUtils.generateDate())
            .withPersonNumber(idDeclarant + "-" + numeroPersonne);

    sendObservabilityEvent(event);
  }

  @ContinueSpan(log = "sendObservabilityEventTriggerFinished")
  public void sendObservabilityEventTriggerFinished(@NonNull Trigger trigger) {
    if (shouldStop(trigger, TRIGGER_FINISHED_EVENT)) {
      return;
    }

    TriggerFinishedEventDto event =
        new TriggerFinishedEventDto().withIdDeclencheur(trigger.getId());

    String dateDebutTraitementZ =
        DateUtils.dateToLocalDateTime(trigger.getDateDebutTraitement())
            .format(DateUtils.YYYY_MM_DD_T_HH_MM_SS_SSSSSS_Z);
    putIfNotNull(event, TriggerFinishedEventDto::withDateDebutTraitement, dateDebutTraitementZ);
    String dateFinTraitementZ =
        DateUtils.dateToLocalDateTime(trigger.getDateFinTraitement())
            .format(DateUtils.YYYY_MM_DD_T_HH_MM_SS_SSSSSS_Z);
    putIfNotNull(event, TriggerFinishedEventDto::withDateFinTraitement, dateFinTraitementZ);

    event.withType(getEventType(trigger)).withStatut(trigger.getStatus().toString());

    String operationDateZ =
        DateUtils.dateToLocalDateTime(trigger.getDateDebutTraitement())
            .format(DateUtils.YYYY_MM_DD_T_HH_MM_SS_SSSSSS_Z);
    event.withOperationDate(operationDateZ);

    sendObservabilityEvent(event);
  }

  private static String getEventType(Trigger trigger) {
    return switch (trigger.getOrigine()) {
      case Event -> "RECEPTION_IMAGE_CONTRAT";
      case Renewal -> "RENOUVELLEMENT";
      case Request -> "DEMANDE_GESTIONNAIRE";
    };
  }

  @ContinueSpan(log = "sendObservabilityEventTriggerBeneficiaryFinished")
  public void sendObservabilityEventTriggerBeneficiaryFinished(
      @NonNull TriggeredBeneficiary triggeredBenef) {
    if (shouldStop(triggeredBenef, TRIGGER_BENEFICIARY_FINISHED_EVENT)) {
      return;
    }

    TriggeredBeneficiaryFinishedEventDto event =
        new TriggeredBeneficiaryFinishedEventDto().withIdDeclencheur(triggeredBenef.getIdTrigger());

    String idPersonneBeyond = triggeredBenef.getAmc() + "-" + triggeredBenef.getNumeroPersonne();
    String codeAnomaly =
        triggeredBenef.getDerniereAnomalie() != null
            ? triggeredBenef.getDerniereAnomalie().getAnomaly().name()
            : null;

    event
        .withIdPersonneBeyond(idPersonneBeyond)
        .withSocieteEmettrice(triggeredBenef.getGestionnaire());
    putIfNotNull(event, TriggeredBeneficiaryFinishedEventDto::withCodeAnomalie, codeAnomaly);
    event.withNumeroContrat(triggeredBenef.getNumeroContrat());
    putIfNotNull(
        event,
        TriggeredBeneficiaryFinishedEventDto::withNumeroContratCollectif,
        triggeredBenef.getNumeroContratCollectif());
    putIfNotNull(
            event,
            TriggeredBeneficiaryFinishedEventDto::withCritereSecondaireDetaille,
            triggeredBenef.getCritereSecondaireDetaille())
        .withNumeroAdherent(triggeredBenef.getNumeroAdherent())
        .withIdDeclarant(triggeredBenef.getAmc());

    sendObservabilityEvent(event);
  }

  @ContinueSpan(log = "sendObservabilityEventConsolidationDeclaration")
  public void sendObservabilityEventConsolidationDeclaration(
      @NonNull DeclarationConsolide declarationConsolide) {
    if (shouldStop(declarationConsolide, DECLARATION_EVENT_CONSOLIDATION)) {
      return;
    }

    DeclarationConsoEventDto event =
        new DeclarationConsoEventDto()
            .withDeclarantId(declarationConsolide.getIdDeclarant())
            .withDeclarationsId(declarationConsolide.getIdDeclarations())
            .withServiceCodes(declarationConsolide.getCodeServices())
            .withIssuingCompanyCode(declarationConsolide.getContrat().getGestionnaire())
            .withContractNumber(declarationConsolide.getContrat().getNumero())
            .withSubscriberNumber(declarationConsolide.getContrat().getNumeroAdherent())
            .withPersonNumber(declarationConsolide.getBeneficiaire().getNumeroPersonne())
            .withStartDate(DateUtils.formatDate(declarationConsolide.getPeriodeDebut()))
            .withEndDate(DateUtils.formatDate(declarationConsolide.getPeriodeFin()));

    sendObservabilityEvent(event);
  }

  @ContinueSpan(log = "sendObservabilityEventConsolidationFailed")
  public Event prepareObservabilityEventConsolidationFailed(
      @NonNull Declaration declaration, String cause) {
    if (shouldStop(declaration, DECLARATION_EVENT_FAIL_CONSOLIDATION)) {
      return null;
    }
    Set<String> services = new HashSet<>();
    DeclarationConsolideUtils.fillServices(declaration, services);

    return new DeclarationConsoFailedEventDto()
        .withDeclarantId(declaration.getIdDeclarant())
        .withDeclarationsId(declaration.get_id())
        .withServiceCodes(new ArrayList<>(services))
        .withIssuingCompanyCode(declaration.getContrat().getGestionnaire())
        .withContractNumber(declaration.getContrat().getNumero())
        .withSubscriberNumber(declaration.getContrat().getNumeroAdherent())
        .withPersonNumber(declaration.getBeneficiaire().getNumeroPersonne())
        .withStartDate(
            DateUtils.formatDate(
                declaration.getDomaineDroits().getFirst().getPeriodeDroit().getPeriodeDebut()))
        .withEndDate(
            DateUtils.formatDate(
                declaration.getDomaineDroits().getFirst().getPeriodeDroit().getPeriodeFin()))
        .withError(cause);
  }

  @ContinueSpan(log = "sendObservabilityEventCarteDemat")
  public Event prepareObservabilityEventCarteDemat(@NonNull CarteDemat carteDemat) {
    if (shouldStop(carteDemat, CARTE_DEMAT_EVENT_CREATION)) {
      return null;
    }

    return new CartetpDematCreationEventDto()
        .withDeclarantId(carteDemat.getIdDeclarant())
        .withIssuingCompanyCode(carteDemat.getContrat().getGestionnaire())
        .withContractNumber(carteDemat.getContrat().getNumero())
        .withSubscriberNumber(carteDemat.getContrat().getNumeroAdherent())
        .withStartDate(DateUtils.formatDate(carteDemat.getPeriodeDebut()))
        .withEndDate(DateUtils.formatDate(carteDemat.getPeriodeFin()))
        .withNbBeneficiaries(carteDemat.getBeneficiaires().size());
  }

  @ContinueSpan(log = "sendObservabilityEventCarteDematDesactivated")
  public void sendObservabilityEventCarteDematDesactivated(@NonNull CarteDemat carteDemat) {
    if (shouldStop(carteDemat, CARTE_DEMAT_EVENT_DESACTIVATION)) {
      return;
    }

    CartetpDematDesactivationEventDto event =
        new CartetpDematDesactivationEventDto()
            .withDeclarantId(carteDemat.getIdDeclarant())
            .withIssuingCompanyCode(carteDemat.getContrat().getGestionnaire())
            .withContractNumber(carteDemat.getContrat().getNumero())
            .withSubscriberNumber(carteDemat.getContrat().getNumeroAdherent())
            .withStartDate(DateUtils.formatDate(carteDemat.getPeriodeDebut()))
            .withEndDate(DateUtils.formatDate(carteDemat.getPeriodeFin()));

    sendObservabilityEvent(event);
  }

  @ContinueSpan(log = "sendObservabilityEventCartePapier")
  public void sendObservabilityEventCartePapier(@NonNull CartePapier cartePapier) {
    if (shouldStop(cartePapier, CARTE_PAPIER_EVENT_CREATION)) {
      return;
    }

    CartetpPapierGenerateEventDto event =
        new CartetpPapierGenerateEventDto()
            .withDeclarantId(cartePapier.getNumeroAMC())
            .withIssuingCompanyCode(cartePapier.getContrat().getGestionnaire())
            .withContractNumber(cartePapier.getContrat().getNumero())
            .withSubscriberNumber(cartePapier.getContrat().getNumeroAdherent())
            .withStartDate(DateUtils.formatDate(cartePapier.getPeriodeDebut()))
            .withEndDate(DateUtils.formatDate(cartePapier.getPeriodeFin()))
            .withNbBeneficiaries(cartePapier.getBeneficiaires().size());

    sendObservabilityEvent(event);
  }

  @ContinueSpan(log = "sendObservabilityEventCartePapierFailed")
  public void sendObservabilityEventCartePapierFailed(
      @NonNull CartePapier cartePapier, String error) {
    if (shouldStop(cartePapier, CARTE_PAPIER_EVENT_CREATION_FAILED)) {
      return;
    }

    CartetpPapierGenerateFailedEventDto event =
        new CartetpPapierGenerateFailedEventDto()
            .withDeclarantId(cartePapier.getNumeroAMC())
            .withIssuingCompanyCode(cartePapier.getContrat().getGestionnaire())
            .withContractNumber(cartePapier.getContrat().getNumero())
            .withSubscriberNumber(cartePapier.getContrat().getNumeroAdherent())
            .withStartDate(DateUtils.formatDate(cartePapier.getPeriodeDebut()))
            .withEndDate(DateUtils.formatDate(cartePapier.getPeriodeFin()))
            .withNbBeneficiaries(cartePapier.getBeneficiaires().size())
            .withError(error);

    sendObservabilityEvent(event);
  }

  @ContinueSpan(log = "sendObservabilityEventCartesDematActivesCount")
  public void sendObservabilityEventCartesDematActivesCount(
      @NonNull String idDeclarant, @NonNull String dateExec, int nbCartesDematActives) {
    if (shouldStop(idDeclarant, CARTES_DEMAT_ACTIVES_EVENT_COUNTING)) {
      return;
    }
    CartetpDematActiveEventDto event =
        new CartetpDematActiveEventDto()
            .withDeclarantId(idDeclarant)
            .withNbActiveDematerializedCards(nbCartesDematActives)
            .withProcessingDate(DateUtils.formatDate(dateExec));

    sendObservabilityEvent(event);
  }

  @ContinueSpan(log = "sendObservabilityEventStartBlb")
  public void sendObservabilityEventStartRecalculBlb() {

    RecalculBlbStartEventDto event = new RecalculBlbStartEventDto();

    sendObservabilityEvent(event);
  }

  @ContinueSpan(log = "sendObservabilityEventEndBlb")
  public void sendObservabilityEventEndRecalculBlb(JobRecalculBlbEndEventDto dto) {

    RecalculBlbEndEventDto event =
        new RecalculBlbEndEventDto()
            .withDeletionBeforeReinit(dto.getDeletionBeforeReinit())
            .withNbBeneficiaries(dto.getNbBeneficiaries())
            .withNbTrackedBeneficiariesKO(dto.getNbTrackedBeneficiariesKO())
            .withNbTrackedBeneficiariesNoResponse(dto.getNbTrackedBeneficiariesNoResponse())
            .withNbTrackedBeneficiariesOK(dto.getNbTrackedBeneficiariesOK())
            .withNbTrackedBeneficiariesNoSend(dto.getNbTrackedBeneficiariesNoSend());

    sendObservabilityEvent(event);
  }

  // CONTRACT_TP_EVENT_CONSOLIDATION_FAILED
  @ContinueSpan(log = "createObservabilityEventContractTPConsolidationFailed")
  public void sendObservabilityEventContractTPConsolidationFailed(
      ConsolidationDeclarationsContratTrigger consolidationInfos, String errorDate, String cause) {
    if (!sendEvents) {
      return;
    }
    ContracttpConsolidationFailedEventDto event =
        new ContracttpConsolidationFailedEventDto()
            .withContractNumber(consolidationInfos.getNumeroContrat())
            .withDeclarantId(consolidationInfos.getIdDeclarant())
            .withSubscriberNumber(consolidationInfos.getNumeroAdherent())
            .withErrorDate(errorDate)
            .withError(cause);

    sendObservabilityEvent(event);
  }

  @ContinueSpan(log = "sendObservabilityEventDelaiRetentionModification")
  public void sendObservabilityEventDelaiRetentionModification(
      Declarant declarant, String newDetentionPeriod) {
    if (shouldStop(declarant, DELAI_RETENTION_EVENT_MODIFICATION)) {
      return;
    }

    DelaiRetentionModificationEventDto event =
        new DelaiRetentionModificationEventDto()
            .withInsurerId(declarant.get_id())
            .withPreviousDetentionPeriod(declarant.getDelaiRetention())
            .withNewDetentionPeriod(newDetentionPeriod);

    sendObservabilityEvent(event);
  }

  @ContinueSpan(log = "sendObservabilityEventRetentionFinished")
  public void sendObservabilityEventRetentionFinished(
      DelaiRetentionFinishedEventDto.Action action, Retention retention) {
    if (shouldStop(retention, DELAI_RETENTION_EVENT_FINISHED)) {
      return;
    }
    DelaiRetentionFinishedEventDto retentionFinished =
        new DelaiRetentionFinishedEventDto()
            .withAction(action)
            .withIdDeclarant(retention.getInsurerId())
            .withNumeroPersonne(retention.getPersonNumber())
            .withNumeroContrat(retention.getContractNumber())
            .withNumeroAdherent(retention.getSubscriberNumber());
    putIfNotNull(
        retentionFinished,
        DelaiRetentionFinishedEventDto::withOriginalEndDate,
        retention.getOriginalEndDate());
    putIfNotNull(
        retentionFinished,
        DelaiRetentionFinishedEventDto::withCurrentEndDate,
        retention.getCurrentEndDate());

    RetentionHistorique firstHisto = retention.getHistorique().getFirst();
    Historique eventOriginalHisto =
        new Historique()
            .withCreation(firstHisto.creation().format(DateTimeFormatter.ISO_DATE_TIME));
    putIfNotNull(eventOriginalHisto, Historique::setDateRadiation, firstHisto.dateRadiation());
    putIfNotNull(eventOriginalHisto, Historique::setDateResiliation, firstHisto.dateResiliation());
    putIfNotNull(eventOriginalHisto, Historique::setDateSansEffet, firstHisto.dateSansEffet());

    RetentionHistorique lastHisto = retention.getHistorique().getLast();
    Historique eventCurrentHisto =
        new Historique().withCreation(lastHisto.creation().format(DateTimeFormatter.ISO_DATE_TIME));
    putIfNotNull(eventCurrentHisto, Historique::setDateRadiation, lastHisto.dateRadiation());
    putIfNotNull(eventCurrentHisto, Historique::setDateResiliation, lastHisto.dateResiliation());
    putIfNotNull(eventCurrentHisto, Historique::setDateSansEffet, lastHisto.dateSansEffet());

    retentionFinished.getHistorique().add(eventOriginalHisto);
    retentionFinished.getHistorique().add(eventCurrentHisto);

    sendObservabilityEvent(retentionFinished);
  }

  @ContinueSpan(log = "sendObservabilityEventTriggerCreatedInError")
  public void sendObservabilityEventTriggerCreatedInError(
      String amcNumber,
      String issuingCompany,
      String insuredNumber,
      String contractNumber,
      String anomalyDescription) {
    if (shouldStop(amcNumber, TRIGGER_CREATED_IN_ERROR_EVENT)) {
      return;
    }

    TriggerCreatedInErrorEventDto event =
        new TriggerCreatedInErrorEventDto()
            .withAmcNumber(amcNumber)
            .withIssuingCompany(issuingCompany)
            .withInsuredNumber(insuredNumber)
            .withContractNumber(contractNumber)
            .withAnomalyDescription(anomalyDescription);

    sendObservabilityEvent(event);
  }

  @ContinueSpan(log = "sendObservabilityEventAlmerysProductCreation")
  public void sendObservabilityEventAlmerysProductCreation(
      AlmerysProduct almerysProduct, String creationUser) {
    if (shouldStop(almerysProduct, ALMERYS_PRODUCT_EVENT_CREATION)) {
      return;
    }

    AlmerysProductCreationEventDto event =
        new AlmerysProductCreationEventDto()
            .withCode(almerysProduct.getCode())
            .withDescription(almerysProduct.getDescription())
            .withCombinationList(
                EventServiceUtils.formatCombinationsForCreation(
                    almerysProduct.getProductCombinations()))
            .withCreationDate(DateUtils.getNow())
            .withUserCreation(creationUser);

    sendObservabilityEvent(event);
  }

  @ContinueSpan(log = "sendObservabilityEventAlmerysProductModification")
  public void sendObservabilityEventAlmerysProductModification(
      AlmerysProduct existingProduct, AlmerysProduct updatedProduct, String modificationUser) {
    if (shouldStop(updatedProduct, ALMERYS_PRODUCT_EVENT_MODIFICATION)) {
      return;
    }

    List<CombiModif> diffs =
        EventServiceUtils.formatCombinationsForModification(existingProduct, updatedProduct);

    if (CollectionUtils.isNotEmpty(diffs)) {
      AlmerysProductModificationEventDto event =
          new AlmerysProductModificationEventDto()
              .withCode(updatedProduct.getCode())
              .withDescription(updatedProduct.getDescription())
              .withCombinationList(diffs)
              .withModificationDate(DateUtils.getNow())
              .withUserModification(modificationUser);
      sendObservabilityEvent(event);
    }
  }

  private <T> T putIfNotNull(T obj, BiConsumer<T, String> add, String value) {
    if (value != null) {
      add.accept(obj, value);
    }
    return obj;
  }

  private boolean shouldStop(Object value, EventType eventType) {
    if (!sendEvents) {
      log.debug(
          "Envoi d'events vers Observability désactivé, l'event {} ne sera pas envoyé.", eventType);
      return true;
    }
    return value == null;
  }
}
