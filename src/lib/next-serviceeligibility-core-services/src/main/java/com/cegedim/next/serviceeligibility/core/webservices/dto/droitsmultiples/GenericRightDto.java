package com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Data;

@Data
public class GenericRightDto {
  String context;
  String insurerId;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  String issuingCompanyCode;

  String ocCode;
  String number;
  String externalNumber;
  String subscriberId;
  String subscriberFullId;
  String subscriptionDate;
  String terminationDate;
  String businessContributor;
  List<Period> responsibleContractOpenPeriods;
  List<CmuContractOpenPeriod> cmuContractOpenPeriods;
  String secondaryCriterion;
  String detailedSecondaryCriterion;
  Boolean isIndividualContract;
  String operator;
  CollectiveContractV5 collectiveContract;
  String qualification;
  String prioritizationOrder;
  String itelisCode;
  List<SuspensionPeriod> suspensionPeriods;
  Insured insured;
  Boolean isForced = false;
}
