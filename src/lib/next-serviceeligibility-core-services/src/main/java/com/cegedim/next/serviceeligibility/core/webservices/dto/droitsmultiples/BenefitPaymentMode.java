package com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples;

import lombok.Data;

@Data
public class BenefitPaymentMode {
  private String code;
  private String label;
  private String currencyCode;
}
