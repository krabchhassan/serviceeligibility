package com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples;

import java.util.List;
import lombok.Data;

@Data
public class InsuredData {
  private Name name;
  private Address address;
  private Contact contact;
  private List<PaymentRecipient> paymentRecipients;
  private List<BenefitStatementRecipient> benefitStatementRecipients;
}
