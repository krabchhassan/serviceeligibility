package com.cegedim.next.serviceeligibility.core.bobbcorrespondance.model;

import java.time.LocalDate;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class SearchCriteria {
  private String codeInsurer;
  private String codeGarantee;
  private String societeEmettrice;
  private String codeOffer;
  private String codeProduct;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate startDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate endDate;
}
