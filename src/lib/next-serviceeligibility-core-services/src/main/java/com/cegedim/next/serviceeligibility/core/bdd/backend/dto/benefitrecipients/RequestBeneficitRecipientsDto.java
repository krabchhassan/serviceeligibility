package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.benefitrecipients;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestBeneficitRecipientsDto {

  private String idPerson;
  private String subscriberId;
  private String contractNumber;
  private String insurerId;
}
