package com.cegedim.next.common.excel.businesscomponent.document.configuration;

import com.cegedim.next.common.excel.businesscomponent.document.model.Column;
import com.cegedim.next.common.excel.businesscomponent.document.model.SheetContent;
import com.cegedim.next.common.excel.util.excel.ExcelUtility;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

/** This class defines a sheet content serializer. */
public class SheetContentSerializer extends JsonSerializer<SheetContent> {

  @Override
  public void serialize(
      final SheetContent sheetContent,
      final JsonGenerator jsonGenerator,
      final SerializerProvider serializers)
      throws IOException {
    jsonGenerator.writeStartObject();

    long countNumberOfLines = 0L;

    final DataFormatter dataFormatter = new DataFormatter();

    jsonGenerator.writeStringField(
        "sheetName", (sheetContent.getSheet() != null) ? sheetContent.getSheet().getName() : null);
    jsonGenerator.writeArrayFieldStart("sheetContent");
    List<Column> columns = sheetContent.getSheet().getColumns();
    Row row;
    while ((row = sheetContent.getNextNoneEmptyRow()) != null) {
      jsonGenerator.writeStartObject();
      countNumberOfLines++;
      jsonGenerator.writeNumberField("lineNumber", row.getRowNum());
      for (final Column column : columns) {
        if ((column != null) && (column.getName() != null)) {
          String cellValue =
              ExcelUtility.determineCellValue(dataFormatter, row, column.getPosition());
          jsonGenerator.writeStringField(column.getName(), StringUtils.trimToNull(cellValue));
        }
      }
      jsonGenerator.writeEndObject();
    }

    jsonGenerator.writeEndArray();
    jsonGenerator.writeNumberField("sheetLinesNumber", countNumberOfLines);
    jsonGenerator.writeEndObject();
  }
}
