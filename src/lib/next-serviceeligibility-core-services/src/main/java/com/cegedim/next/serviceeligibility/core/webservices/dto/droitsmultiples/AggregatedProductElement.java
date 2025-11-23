package com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class AggregatedProductElement {
  private String codeProduct;
  private String codeOffer;
  private List<String> codeBenefitsNature;
  private String codeAmc;
  private LocalDate from;
  private LocalDate to;
}
