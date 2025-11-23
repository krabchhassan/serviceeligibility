package com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public class Product {
  String issuingCompanyCode;
  String issuingCompanyName;
  String offerCode;
  String productCode;
  String otpProductCode;
  Set<BenefitType> benefitsType = new HashSet<>();
  Period period;
}
