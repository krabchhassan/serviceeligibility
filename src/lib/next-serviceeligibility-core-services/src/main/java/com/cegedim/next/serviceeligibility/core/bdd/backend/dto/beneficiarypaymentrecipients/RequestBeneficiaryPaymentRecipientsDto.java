package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarypaymentrecipients;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestBeneficiaryPaymentRecipientsDto {

  private String insurerId;
  private String personNumber;
  private String subscriberId;
  private String date;
}
