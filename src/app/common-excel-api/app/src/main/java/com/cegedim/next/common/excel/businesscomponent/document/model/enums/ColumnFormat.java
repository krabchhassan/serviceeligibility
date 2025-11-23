package com.cegedim.next.common.excel.businesscomponent.document.model.enums;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/** This enumerated class defines all column formats. */
public enum ColumnFormat {
  BOOLEAN,
  DATE,
  NUMERIC,
  STRING,
  @JsonEnumDefaultValue
  UNKNOWN;

  /**
   * Allows to check the validity of a column format.
   *
   * @param columnFormat an instance of {@link ColumnFormat} [OPTIONAL].
   * @return <code>true</code> if the column format is valid else <code>false</code>.
   */
  public static boolean checkValidityColumnFormat(final ColumnFormat columnFormat) {
    return (columnFormat != null) && !ColumnFormat.UNKNOWN.equals(columnFormat);
  }
}
