package com.cegedim.next.serviceeligibility.core.model.kafka;

import lombok.Getter;

@Getter
public enum EventType {
  CONTRACT_EVENT_CREATION("contract-creation-event"),
  CONTRACT_EVENT_MODIFICATION("contract-modification-event"),
  CONTRACT_EVENT_RECEPTION("contract-reception-event"),
  CONTRACT_EVENT_DELETE("contract-suppression-event"),
  CONTRACT_EVENT_INVALID("contract-invalid-reception-event"),
  INSURED_EVENT_RECEPTION("insured-reception-event"),
  INSURED_EVENT_INVALID("insured-invalid-reception-event"),
  INSURED_EVENT_TERMINATION("insured-termination-event"),
  PRESTIJ_EVENT_RECEPTION("prestij-reception-event"),
  PRESTIJ_EVENT_CREATION("prestij-creation-event"),
  PRESTIJ_EVENT_MODIFICATION("prestij-modification-event"),
  PRESTIJ_EVENT_INVALID("prestij-invalid-reception-event"),
  CONTRACT_EVENT_SUSPENSION("contract-suspension-event"),
  CONTRACT_EVENT_SUSPENSION_LIFTED("contract-suspension-lifted-event"),
  BENEFICIARY_EVENT_CREATION("beneficiary-creation-event"),
  BENEFICIARY_EVENT_MODIFICATION("beneficiary-modification-event"),
  DECLARATION_EVENT_CREATION("declaration-creation-event"),
  INSURED_PAYMENT_RECIPIENT_EVENT_MODIFICATION("insured-payment-recipient-modification-event"),
  TRIGGER_FINISHED_EVENT("trigger-finished-event"),
  TRIGGER_BENEFICIARY_FINISHED_EVENT("triggered-beneficiary-finished-event"),
  BENEFICIARY_SENDING_BLB_EROROR_EVENT("beneficiary-sending-blb-error-event"),
  DECLARATION_EVENT_CONSOLIDATION("declaration-conso-event"),
  DECLARATION_EVENT_FAIL_CONSOLIDATION("declaration-conso-failed-event"),
  CARTES_DEMAT_ACTIVES_EVENT_COUNTING("carteTP-demat-active-event"),
  CARTE_DEMAT_EVENT_CREATION("carteTP-demat-creation-event"),
  CARTE_DEMAT_EVENT_DESACTIVATION("carteTP-demat-desactivation-event"),
  CARTE_PAPIER_EVENT_CREATION("carteTP-papier-generate-event"),
  CARTE_PAPIER_EVENT_CREATION_FAILED("carteTP-papier-generate-failed-event"),
  CONTRACT_TP_EVENT_CONSOLIDATION_FAILED("contractTP-consolidation-failed-event"),
  DELAI_RETENTION_EVENT_MODIFICATION("delai-retention-modification-event"),
  DELAI_RETENTION_EVENT_FINISHED("delai-retention-finished-event"),
  TRIGGER_CREATED_IN_ERROR_EVENT("trigger-created-in-error-event"),
  ALMERYS_PRODUCT_EVENT_MODIFICATION("almerys-product-modification-event"),
  ALMERYS_PRODUCT_EVENT_CREATION("almerys-product-creation-event");

  private final String eventType;

  EventType(String eventType) {
    this.eventType = eventType;
  }

  public String getErrorMessage() {
    return ("Could not send " + this.getEventType() + " to Observability");
  }
}
