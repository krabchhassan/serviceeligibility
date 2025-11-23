package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.digitalcontractinformations;

import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.NameCorporate;
import lombok.Data;

@Data
public class PaymentRecipientDto {
  private String paymentRecipientId;
  private String beyondPaymentRecipientId;
  private NameCorporate name;
}
