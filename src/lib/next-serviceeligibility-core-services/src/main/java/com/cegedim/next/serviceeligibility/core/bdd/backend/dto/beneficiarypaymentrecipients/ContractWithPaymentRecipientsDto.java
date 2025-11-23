package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarypaymentrecipients;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.digitalcontractinformations.PaymentRecipientDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.List;
import lombok.Data;

@Data
public class ContractWithPaymentRecipientsDto implements GenericDto {

  private String number;
  private String subscriberId;
  private String subscriptionDate;
  private String terminationDate;
  private List<PaymentRecipientDto> paymentRecipients;
}
