package com.cegedim.next.serviceeligibility.core.kafka.observability;

public enum MessageType {
  BLB_LOGICAL_DELETION("job-beneficiary-location-deletion"),
  BLB_BENEFICIARY_FEEDING("beneficiary-location-feeding"),
  RECIPIENT_REQUEST_MESSAGE_TYPE("recipient-request-message"),
  UNDUE_RETENTION_MESSAGE_TYPE("undue-retention-message");

  private final String messageType;

  MessageType(final String messageType) {
    this.messageType = messageType;
  }

  public String getMessageType() {
    return this.messageType;
  }
}
