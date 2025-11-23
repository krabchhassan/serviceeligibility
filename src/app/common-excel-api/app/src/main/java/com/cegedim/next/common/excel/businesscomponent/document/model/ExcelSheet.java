package com.cegedim.next.common.excel.businesscomponent.document.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** This class defines a sheet which contains the data. */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExcelSheet {

  /** The Excel sheet name. */
  @Getter @Setter private String name;

  /** The Excel sheet number. It starts at 0. */
  @Getter @Setter private Integer number;

  /** The columns definition. */
  @Getter @Setter private List<Column> columns;

  /** Default constructor. */
  public ExcelSheet() {
    this.columns = new ArrayList<>();
  }

  /**
   * Return the columns number. If {@link #getColumns()} is <code>NULL</code> then it returns <code>
   * 0</code> else the size of {@link #getColumns()}.
   *
   * @return an instance of {@link Integer}.
   */
  public Integer getColumnsNumber() {
    return this.columns != null ? this.columns.size() : 0;
  }
}
