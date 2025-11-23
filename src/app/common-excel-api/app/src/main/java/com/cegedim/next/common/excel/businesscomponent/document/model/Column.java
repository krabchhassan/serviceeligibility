package com.cegedim.next.common.excel.businesscomponent.document.model;

import com.cegedim.next.common.excel.businesscomponent.document.model.enums.ColumnFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.IndexedColors;

/** This class defines a document column. */
public class Column {

  /** The column position which starts at 0. */
  @Getter @Setter private int position;

  /** The column name. */
  @Getter @Setter private String name;

  /** The column format. */
  @Getter @Setter private ColumnFormat columnFormat;

  /** In case of numeric Column A decimal precision can be defined */
  @Getter
  @Setter
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Integer precision;

  @Getter
  @Setter
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private IndexedColors color;
}
