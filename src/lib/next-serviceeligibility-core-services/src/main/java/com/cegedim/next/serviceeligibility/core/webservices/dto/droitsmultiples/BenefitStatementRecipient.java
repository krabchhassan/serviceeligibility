package com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples;

import lombok.Data;

@Data
public class BenefitStatementRecipient {
  private String benefitStatementRecipientId;
  private String beyondBenefitStatementRecipientId;
  private NameCorporate name;
  private Address address;
  private Period period;
}
