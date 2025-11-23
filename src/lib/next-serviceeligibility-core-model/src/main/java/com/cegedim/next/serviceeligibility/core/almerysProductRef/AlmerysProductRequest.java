package com.cegedim.next.serviceeligibility.core.almerysProductRef;

import java.util.List;
import lombok.Data;

@Data
public class AlmerysProductRequest {
  private int perPage = 10;
  private int page = 1;
  private String sortBy;
  private String direction;
  private String code;
  private String description;
  private List<String> gts;
  private List<String> lots;
  private boolean showDeletedGts = true;
  private boolean showDeletedLots = true;
}
