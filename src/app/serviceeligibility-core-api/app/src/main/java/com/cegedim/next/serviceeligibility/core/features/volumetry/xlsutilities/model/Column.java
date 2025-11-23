package com.cegedim.next.serviceeligibility.core.features.volumetry.xlsutilities.model;

import com.cegedim.next.serviceeligibility.core.features.volumetry.xlsutilities.enums.ColumnFormat;
import lombok.Data;

/** This class defines a document column. */
@Data
public class Column {

  /** The column position which starts at 0. */
  private int position;

  /** The column name. */
  private String name;

  /** The column format. */
  private ColumnFormat columnFormat;
}
