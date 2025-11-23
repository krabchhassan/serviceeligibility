package com.cegedim.next.serviceeligibility.core.features.volumetry.xlsutilities.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class Content {

  private String sheetName;

  // Each key is dependent of Metadata definition
  private List<Map<String, Object>> sheetContent;

  public Content() {
    this.sheetContent = new ArrayList<>();
  }
}
