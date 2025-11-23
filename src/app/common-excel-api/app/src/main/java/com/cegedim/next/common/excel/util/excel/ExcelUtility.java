package com.cegedim.next.common.excel.util.excel;

import static java.util.Comparator.comparingInt;

import com.cegedim.next.common.excel.businesscomponent.document.model.Column;
import com.cegedim.next.common.excel.businesscomponent.document.model.ExcelSheet;
import com.cegedim.next.common.excel.businesscomponent.document.model.enums.ColumnFormat;
import com.cegedim.next.common.excel.constants.DateConstants;
import java.util.*;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/** This class defines utility methods on Excel file. */
public final class ExcelUtility {

  /** Private constructor. */
  private ExcelUtility() {}

  /**
   * Allows to get all columns from a workbook.
   *
   * @param sheet a sheet defining a tab of a Excel file [REQUIRED NOT NULL].
   * @return an instance of {@link List} which contains all columns from a tab of Excel file.
   */
  public static List<Column> getAllColumns(@NonNull final Sheet sheet) {

    final List<Column> columns = new ArrayList<>();

    final Row columnRow = sheet.iterator().next();

    if (columnRow != null) {
      final int firstColumnIndex = columnRow.getFirstCellNum();
      // The last cell num is not the index but the index plus
      // one
      final int lastColumnIndexPlusOne = columnRow.getLastCellNum();

      if ((firstColumnIndex != -1) && (lastColumnIndexPlusOne != -1)) {

        for (int indexColumn = firstColumnIndex;
            indexColumn < lastColumnIndexPlusOne;
            indexColumn++) {
          final Cell cell = columnRow.getCell(indexColumn);

          addNewColumn(columns, indexColumn, cell);
        }
      }
    }

    columns.stream()
        .collect(Collectors.groupingBy(Column::getName))
        .forEach(
            (columnName, columnsForColumnName) -> {
              if (columnsForColumnName.size() > 1) {
                columnsForColumnName.forEach(
                    column ->
                        column.setName(String.format("%s@%d", columnName, column.getPosition())));
              }
            });

    return columns;
  }

  private static void addNewColumn(List<Column> columns, int indexColumn, Cell cell) {
    if ((cell != null) && (CellType.STRING.equals(cell.getCellTypeEnum()))) {
      final String columnName = cell.getStringCellValue();
      final short columnColor = cell.getCellStyle().getFillForegroundColor();

      if (StringUtils.isNotBlank(columnName)) {
        columns.add(buildColumn(indexColumn, columnName, columnColor));
      }
    }
  }

  /**
   * Allows to build an instance of {@link Column}.
   *
   * @param columnIndex column index [MUST BE >= 0].
   * @param columnName column name [REQUIRED NOT NULL].
   * @return an instance of {@link Column}.
   */
  private static Column buildColumn(
      final int columnIndex, @NonNull final String columnName, final short columnColor) {
    if (columnIndex < 0) {
      throw new IllegalArgumentException(
          "[ExcelUtility#buildColumn] => The parameter [columnIndex] must be >= 0");
    }

    final Column column = new Column();
    column.setPosition(columnIndex);
    column.setName(columnName);
    column.setColumnFormat(ColumnFormat.STRING);

    final IndexedColors indexedColumnColor = IndexedColors.fromInt(columnColor);
    if (indexedColumnColor != IndexedColors.AUTOMATIC) {
      column.setColor(indexedColumnColor);
    }

    return column;
  }

  /**
   * Allows to determine the cell value.
   *
   * @param dataFormatter an instance of {@link DataFormatter} [REQUIRED NOT NULL].
   * @param dataRow an instance of Row which defines a row of data [REQUIRED NOT NULL].
   * @param position
   * @return an instance of String if determined else <code>NULL</code>.
   */
  public static String determineCellValue(
      @NonNull final DataFormatter dataFormatter, @NonNull final Row dataRow, int position) {
    final Cell cell = dataRow.getCell(position);

    String cellValue = null;

    if (cell != null) {
      final CellType cellType = cell.getCellTypeEnum();

      if (cellType != null) {
        switch (cellType) {
          case NUMERIC:
            cellValue = getCellValueForNumericType(cell);
            break;

          case BOOLEAN, STRING:
            cellValue = dataFormatter.formatCellValue(cell);
            break;

          default:
            break;
        }
      }
    }

    return cellValue;
  }

  /**
   * Allows to build all sheets for the Excel file.
   *
   * @param workbook an instance of {@link Workbook} which defines the content of the Excel file to
   *     create [REQUIRED NOT NULL].
   * @param excelSheets {@link ExcelSheet} instances [OPTIONAL].
   * @param xssfSheetByNameMap map which contains {@link XSSFSheet} instances organized by sheet
   *     name [REQUIRED NOT NULL].
   */
  public static void buildAllExcelFileSheets(
      @NonNull final SXSSFWorkbook workbook,
      final List<ExcelSheet> excelSheets,
      @NonNull final Map<String, SXSSFSheet> xssfSheetByNameMap) {

    if (CollectionUtils.isNotEmpty(excelSheets)) {

      for (final ExcelSheet excelSheet : excelSheets) {

        if ((excelSheet != null) && StringUtils.isNotBlank(excelSheet.getName())) {

          final SXSSFSheet xssfSheet = workbook.createSheet(excelSheet.getName());

          xssfSheetByNameMap.put(excelSheet.getName(), xssfSheet);
        }
      }
    }
  }

  /**
   * Allows to build all column headers of all sheets for the Excel file.
   *
   * @param excelSheets {@link ExcelSheet} instances [OPTIONAL].
   * @param xssfSheetByNameMap map which contains {@link XSSFSheet} instances organized by sheet
   *     name [REQUIRED NOT NULL].
   * @param columnPositionsBySheetNameMapToUpdate map which contains column positions organized by
   *     sheet name [REQUIRED NOT NULL].
   */
  public static void buildAllExcelFileColumnsOfSheets(
      final List<ExcelSheet> excelSheets,
      @NonNull final Map<String, SXSSFSheet> xssfSheetByNameMap,
      @NonNull final Map<String, Map<String, Integer>> columnPositionsBySheetNameMapToUpdate) {

    if (CollectionUtils.isNotEmpty(excelSheets)) {

      for (final ExcelSheet excelSheet : excelSheets) {

        if ((excelSheet != null) && StringUtils.isNotBlank(excelSheet.getName())) {

          final SXSSFSheet xssfSheet = xssfSheetByNameMap.get(excelSheet.getName());

          if (xssfSheet != null) {
            buildAllExcelFileColumnsOfASheet(
                excelSheet.getName(),
                xssfSheet,
                excelSheet.getColumns(),
                columnPositionsBySheetNameMapToUpdate);
          }
        }
      }
    }
  }

  /**
   * Allows to build all columns of a sheet for the Excel file.
   *
   * @param excelSheetName a Excel sheet name [REQUIRED NOT NULL].
   * @param xssfSheet an instance of {@link XSSFSheet} which defines the Excel sheet [REQUIRED NOT
   *     NULL].
   * @param columnsToCreate {@link Column} instances to create [OPTIONAL].
   * @param columnPositionsBySheetNameMapToUpdate column positions organized by sheet names
   *     [REQUIRED NOT NULL].
   */
  private static void buildAllExcelFileColumnsOfASheet(
      @NonNull final String excelSheetName,
      @NonNull final SXSSFSheet xssfSheet,
      final List<Column> columnsToCreate,
      @NonNull final Map<String, Map<String, Integer>> columnPositionsBySheetNameMapToUpdate) {

    if (CollectionUtils.isNotEmpty(columnsToCreate)) {
      final Map<String, Integer> columnPositionByNameMap = new HashMap<>();
      columnPositionsBySheetNameMapToUpdate.put(excelSheetName, columnPositionByNameMap);

      final List<Column> filteredColumns = filterAndSortColumnsToCreate(columnsToCreate);

      if (CollectionUtils.isNotEmpty(filteredColumns)) {
        final Row rowColumnHeader = xssfSheet.createRow(0);

        for (final Column column : filteredColumns) {
          buildExcelFileColumnHeader(columnPositionByNameMap, rowColumnHeader, column);
        }
      }
    }
  }

  /**
   * Allows to build only one column header in the Excel file.
   *
   * @param columnPositionByNameMap map which contains column position by column name [REQUIRED NOT
   *     NULL].
   * @param rowColumnHeader row column header to update with a new column [REQUIRED NOT NULL].
   * @param column an instance of {@link Column} to write in the row column header [OPTIONAL].
   */
  private static void buildExcelFileColumnHeader(
      @NonNull final Map<String, Integer> columnPositionByNameMap,
      @NonNull final Row rowColumnHeader,
      final Column column) {

    if ((column != null) && (column.getName() != null)) {

      columnPositionByNameMap.put(column.getName(), column.getPosition());

      final Cell headerCell = rowColumnHeader.createCell(column.getPosition(), CellType.STRING);

      headerCell.setCellValue(column.getName().replaceFirst("@\\d+$", ""));

      headerCell.setCellStyle(rowColumnHeader.getSheet().getWorkbook().createCellStyle());

      fillForegroundColor(headerCell, column.getColor());
    }
  }

  public static void fillForegroundColor(Cell cell, IndexedColors foregroundColor) {
    if (foregroundColor != null) {
      CellStyle style = cell.getCellStyle();

      style.setFillForegroundColor(foregroundColor.getIndex());
      style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }
  }

  /**
   * Allows to filter and sort {@link Column} instances to create.
   *
   * @param columns {@link Column} instances [REQUIRED NOT NULL].
   * @return {@link Column} instances which are filtered and sorted.
   */
  private static List<Column> filterAndSortColumnsToCreate(@NonNull final List<Column> columns) {
    return columns.stream()
        .filter(Objects::nonNull)
        .sorted(comparingInt(Column::getPosition))
        .toList();
  }

  /**
   * Allows to get the cell value when type is {@link CellType#NUMERIC}.
   *
   * @param cell an instance of {@link Cell} [REQUIRED NOT NULL].
   * @return an instance of {@link String} which defines the cell value.
   */
  private static String getCellValueForNumericType(@NonNull final Cell cell) {
    return DateUtil.isCellDateFormatted(cell)
        ? getFormattedDateValue(cell.getDateCellValue())
        : getFormattedNumericValue(cell.getNumericCellValue());
  }

  /**
   * Allows to get a formatted date value as a {@link String}.
   *
   * @param date an instance of {@link Date} to format [REQUIRED NOT NULL].
   * @return an instance of {@link String} defining the formatted date value.
   */
  private static String getFormattedDateValue(@NonNull final Date date) {
    return DateConstants.DATETIME_FORMAT.format(date);
  }

  /**
   * Allows to get a formatted numeric value as a {@link String}.
   *
   * @param value a double value to format.
   * @return an instance of {@link String} defining the formatted numeric value.
   */
  private static String getFormattedNumericValue(final double value) {
    return NumberToTextConverter.toText(value);
  }
}
