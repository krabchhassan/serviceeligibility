package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.paymentrecipients;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.PaymentRecipient;
import java.util.List;
import lombok.Data;

@Data
public class PaymentRecipientsByContractDto implements GenericDto {
  private ContractIdDto contractId;
  private List<InsuredPeriodDto> insuredPeriods;
  private List<PaymentRecipient> paymentRecipients;
}
