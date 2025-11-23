package com.cegedim.next.common.excel.businesscomponent.document.model;

import static com.cegedim.next.common.excel.util.excel.ExcelUtility.determineCellValue;

import com.cegedim.next.common.excel.businesscomponent.document.configuration.SheetContentSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Iterator;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

/** This class defines the content of an instance of {@link ExcelSheet}. */
@JsonSerialize(using = SheetContentSerializer.class)
public class SheetContent {

  @Getter @Setter private ExcelSheet sheet;

  @Setter @Getter @JsonIgnore private Iterator<Row> rows;

  public Row getNextNoneEmptyRow() {

    while (rows.hasNext()) {
      Row currentRow = rows.next();
      if (isNotEmptyRow(currentRow)) {
        return currentRow;
      }
    }
    return null;
  }

  private boolean isNotEmptyRow(Row currentRow) {
    return getSheet().getColumns().stream()
            .map(col -> determineCellValue(new DataFormatter(), currentRow, col.getPosition()))
            .filter(Objects::nonNull)
            .count()
        != 0;
  }
}
