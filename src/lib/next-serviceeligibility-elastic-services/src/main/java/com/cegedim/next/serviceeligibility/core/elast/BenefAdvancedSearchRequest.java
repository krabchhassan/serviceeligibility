package com.cegedim.next.serviceeligibility.core.elast;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.opensearch.search.sort.SortOrder;

@Data
public class BenefAdvancedSearchRequest {

  private List<Sort> sorts = new ArrayList<>();

  private int perPage = 10;

  private int page = 0;

  private String name;

  private String firstName;

  private String nir;

  private String subscriberId;

  private String contractNumber;

  private String birthDate;

  private String birthRank;

  private String postalCode;

  private String issuingCompany;
  private String insurerId;

  private String dateReference;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Sort {

    private String field;

    private String order = SortOrder.ASC.toString();
  }
}
