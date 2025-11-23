package com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples;

import lombok.Data;

@Data
public class PaymentRecipient {
  private String paymentRecipientId;
  private String beyondPaymentRecipientId;
  private NameCorporate name;
  private Address address;
  private Rib rib;
  private BenefitPaymentMode benefitPaymentMode;
  private Period period;
}
