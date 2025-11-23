package com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples;

import java.util.List;
import lombok.Data;

@Data
public class Insured {
  Boolean isSubscriber;
  String administrativeRank;
  Identity identity;
  InsuredData data;
  String healthMutualSubscriptionDate;
  String individualSubscriptionStartDate;
  String individualSubscriptionNumber;
  String cancellationDate;
  DigitRelation digitRelation;
  List<Period> attendingPhysicianOpenedPeriods;
  List<SpecialPlan> specialPlans;
  List<SpecialStatus> specialStatuses;
  Quality quality;
  List<Right> rights;
}
