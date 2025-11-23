package com.cegedim.next.serviceeligibility.core.bobb;

import com.cegedim.next.serviceeligibility.core.bobb.model.export.*;
import com.cegedim.next.serviceeligibility.core.utility.error.UnexpectedFileException;
import com.cegedim.next.serviceeligibility.core.utility.rest.RestConnectorUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public final class XlsxUtils {

  private XlsxUtils() {}

  public static String valueOfCustom(Object object) {
    return String.valueOf(object).equals("null") ? "" : object.toString();
  }

  public static Document checkDocument(
      MultipartFile file,
      String authHeader,
      String[] columns,
      ObjectMapper objectMapper,
      String xlsApiBaseURL) {
    Document document;
    JSONObject jsonObject;
    try {
      final ResponseEntity<String> stringResponseEntity =
          RestConnectorUtils.sendExcelFile(
              xlsApiBaseURL + "/documents", file.getInputStream(), authHeader);

      jsonObject = new JSONObject(new JSONTokener(stringResponseEntity.getBody()));

      document = objectMapper.readValue(jsonObject.toString(), Document.class);
    } catch (final IOException e) {
      throw new UnexpectedFileException("Error input file", e);
    } catch (final JSONException e) {
      throw new UnexpectedFileException("Error parsing JSON file", e);
    }

    if (!validDocument(document, columns)) {
      throw new UnexpectedFileException("Bad file format");
    }
    return document;
  }

  private static boolean validDocument(Document document, String[] columns) {
    boolean result = true;

    if (document != null
        && document.getMetadata() != null
        && document.getMetadata().getSheets() != null
        && document.getMetadata().getSheets().size() == 1) {
      ExcelSheet excelSheet = document.getMetadata().getSheets().get(0);
      // Check the sheet name and the columns number
      if (excelSheet.getColumnsNumber() != 4) {
        result = false;
      }
      // Check the columns name
      if (excelSheet.getColumns() != null) {
        List<String> columnList = List.of(columns);
        result =
            excelSheet.getColumns().stream()
                .filter(column -> !columnList.contains(column.getName()))
                .findAny()
                .isEmpty();
      }
    } else {
      result = false;
    }
    return result;
  }

  public static Document buildXlsxDocument(
      String filename, String sheetName, String[] columnTitles, Content content) {

    // XLS API call
    final Document document = new Document();
    final Metadata metadata = new Metadata();
    final List<Content> contents = new ArrayList<>();

    metadata.setName(filename);
    metadata.addSheet(buildExcelSheet(sheetName, 0, columnTitles));
    metadata.setSheetsNumber(1);

    contents.add(content);
    document.setContent(contents);
    document.setMetadata(metadata);
    return document;
  }

  public static ExcelSheet buildExcelSheet(final String name, int position, String[] columnTitles) {
    final ExcelSheet excelSheet = new ExcelSheet();

    excelSheet.setName(name);
    excelSheet.setNumber(position);

    final List<Column> columns = new ArrayList<>();

    final AtomicInteger counter = new AtomicInteger(0);
    for (final String offerColumn : columnTitles) {
      final Column column = new Column();
      column.setPosition(counter.getAndIncrement());
      column.setColumnFormat(ColumnFormat.STRING);
      column.setName(offerColumn);
      columns.add(column);
    }
    excelSheet.setColumnsNumber(counter.get() - 1);

    excelSheet.setColumns(columns);
    return excelSheet;
  }
}
