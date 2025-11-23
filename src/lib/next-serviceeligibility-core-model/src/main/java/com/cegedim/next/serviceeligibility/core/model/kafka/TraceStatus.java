package com.cegedim.next.serviceeligibility.core.model.kafka;

public enum TraceStatus {
  ReceivedFromAi,
  ErrorDeserializing,
  Deserialized,
  SentToKafka,
  ReceivedFromKafka,
  MappingSucceeded,
  SuccesfullyProcessed,
  ValidationFailed,
  IdClientBOInvalid,
  InsuredNotFound,
  UnexpectedBddsException,
  ContractNotFound,
  ContractDeleted,
  UnknownBeneficiary
}
