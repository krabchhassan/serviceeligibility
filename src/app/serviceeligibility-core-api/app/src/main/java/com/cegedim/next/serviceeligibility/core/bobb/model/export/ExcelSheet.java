package com.cegedim.next.serviceeligibility.core.bobb.model.export;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/** This class defines a sheet which contains the data. */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExcelSheet {

  /** The Excel sheet name. */
  private String name;

  /** The Excel sheet number. It starts at 0. */
  private Integer number;

  private int columnsNumber;

  /** The columns definition. */
  private List<Column> columns;

  /** Default constructor. */
  public ExcelSheet() {
    this.columns = new ArrayList<>();
  }
}
