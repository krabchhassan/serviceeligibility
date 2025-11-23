package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.paymentrecipients;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.List;
import lombok.Data;

@Data
public class ResponsePaymentRecipientsByContract implements GenericDto {
  private List<PaymentRecipientsByContractDto> contracts;
}
