package com.cegedim.next.common.excel.features.writedocument.service;

import static com.cegedim.next.common.excel.util.error.RestErrorConstants.*;
import static com.cegedim.next.common.excel.util.excel.ExcelUtility.buildAllExcelFileColumnsOfSheets;
import static com.cegedim.next.common.excel.util.excel.ExcelUtility.buildAllExcelFileSheets;
import static java.util.Comparator.comparingInt;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.cegedim.common.base.rest.exceptions.model.RestError;
import com.cegedim.common.base.rest.exceptions.model.RestException;
import com.cegedim.common.base.rest.exceptions.model.enums.ExceptionLevel;
import com.cegedim.next.common.excel.businesscomponent.document.model.Column;
import com.cegedim.next.common.excel.businesscomponent.document.model.ExcelSheet;
import com.cegedim.next.common.excel.businesscomponent.document.model.Metadata;
import com.cegedim.next.common.excel.businesscomponent.document.model.enums.ColumnFormat;
import com.cegedim.next.common.excel.constants.CommonConstants;
import com.cegedim.next.common.excel.features.writedocument.command.ResultBuildingExcelFile;
import com.cegedim.next.common.excel.util.error.MalformedBodyException;
import com.cegedim.next.common.excel.util.error.RequestValidationException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.jsfr.json.*;
import org.jsfr.json.provider.JacksonProvider;

/** This class defines the feature service for writing document. */
@Slf4j
public class WriteDocumentService {

  public static final int MAX_PRECISION = 15;

  public ResultBuildingExcelFile writeExcelDocument(@NonNull final Reader reader) throws Throwable {
    return new JsonToExcelConverter(reader).convert();
  }

  private class JsonToExcelConverter {
    public static final String SHEET_NAME = "sheetName";
    public static final String SHEET = "sheet";
    public static final String LINE_NUMBERS = "lineNumbers";
    public static final String METADATA = "metadata";
    public static final String LINE_NUMBER = "lineNumber";
    public static final String UTF_8 = "UTF-8";
    public static final String PRECISION_IS_OUT_OF_RANGE =
        "Precision is out of range (must be between 0 and 14)";

    private Map<String, ColumnFormat> columnFormatMap = new HashMap<>();
    private Map<String, Integer> columnFormatPrecision = new HashMap<>();
    private Map<String, CellStyle> columnCellStyleMap = new HashMap<>();

    private final Reader reader;

    private final JsonSurfer surfer =
        new JsonSurfer(JacksonParser.INSTANCE, JacksonProvider.INSTANCE);
    private final SurfingConfiguration.Builder builder = this.surfer.configBuilder();
    private final SXSSFWorkbook workbook = new SXSSFWorkbook(50);
    private final Map<Short, CellStyle> cellStyleMap = new ConcurrentHashMap<>();
    private final Map<String, SXSSFSheet> sxssfSheetByName = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Integer>> columnPositionsBySheetName =
        new ConcurrentHashMap<>();
    Set<String> excelSheetNames = new HashSet<>();
    private String filename;

    public JsonToExcelConverter(final Reader reader) {
      this.reader = reader;
    }

    public ResultBuildingExcelFile convert() throws Exception {
      try {
        tryToConvert();
      } catch (RuntimeException exception) {

        throw (Exception) exception.getCause();
      }

      return prepareResponse();
    }

    private void tryToConvert() {
      this.builder
          .bind("$.metadata", this::onMetadataFound)
          .bind("$.content[*].sheetName", this::onSheetNameFound)
          .bind("$.content[*].sheetContent[*]", this::onSheetContentFound)
          .withErrorStrategy(wrapCheckExceptionAndRethrowStrategy())
          .buildAndSurf(this.reader);
    }

    private void onMetadataFound(final Object value, final ParsingContext context) {
      Metadata metadata = extractMetadata(value);
      checkMetadataValidity(metadata);
      prepareExcelWorkbookShape(metadata);
      this.filename = metadata.getName();
      context.save(METADATA, metadata);

      metadata
          .getSheets()
          .forEach(
              sheet ->
                  sheet
                      .getColumns()
                      .forEach(
                          column -> {
                            var name = column.getName();
                            var format = column.getColumnFormat();
                            var color = column.getColor();

                            columnFormatMap.put(name, format);
                            columnFormatPrecision.put(name, column.getPrecision());

                            var cellStyle = workbook.createCellStyle();

                            switch (format) {
                              case DATE, NUMERIC:
                                var stringFormat =
                                    format == ColumnFormat.DATE
                                        ? "m/d/yy"
                                        : getPrecisionAsString(name);
                                var dataFormat =
                                    workbook
                                        .getCreationHelper()
                                        .createDataFormat()
                                        .getFormat(stringFormat);
                                cellStyle.setDataFormat(dataFormat);
                                break;
                              default:
                                break;
                            }

                            if (color != null) {
                              cellStyle.setFillForegroundColor(color.getIndex());
                              cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                            }

                            columnCellStyleMap.put(column.getName(), cellStyle);
                          }));
    }

    private void onSheetNameFound(final Object value, final ParsingContext context) {
      TextNode textNode = (TextNode) value;
      String sheetName = textNode.asText();
      checkSheetNameValidity(sheetName);
      SXSSFSheet sxssfSheet = this.sxssfSheetByName.get(sheetName);
      context.save(SHEET_NAME, sheetName);
      context.save(SHEET, sxssfSheet);
      context.save(LINE_NUMBERS, new HashSet<Integer>());
    }

    private void checkSheetNameValidity(final String sheetName) {
      if (StringUtils.isBlank(sheetName)) {
        throw new RequestValidationException(
            String.format(
                "The sheet name of sheet content must be filled for the Excel file [%s].",
                this.filename),
            BAD_REQUEST,
            ERROR_VALIDITY_SHEET_CONTENT_SHEET_NAME_BLANK);
      }

      if (!this.excelSheetNames.add(sheetName)) {
        throw new RequestValidationException(
            String.format(
                "The sheet name [%s] of sheet content must be unique for the Excel file [%s].",
                sheetName, this.filename),
            BAD_REQUEST,
            ERROR_VALIDITY_SHEET_CONTENT_SHEET_NAME_NOT_UNIQUE);
      }

      if (!this.sxssfSheetByName.containsKey(sheetName)) {
        throw new RequestValidationException(
            String.format(
                "The sheet name [%s] of sheet content must match with a Excel sheet defined on the metadata, for the Excel file [%s].",
                sheetName, this.filename),
            BAD_REQUEST,
            ERROR_VALIDITY_SHEET_CONTENT_SHEET_NAME_UNKNOWN);
      }
    }

    private void onSheetContentFound(final Object value, final ParsingContext context) {
      String sheetName = context.load(SHEET_NAME, String.class);
      SXSSFSheet sheet = context.load(SHEET, SXSSFSheet.class);
      HashSet<Integer> lineNumbers = context.load(LINE_NUMBERS, HashSet.class);
      ObjectNode line = (ObjectNode) value;

      Map<String, Integer> columnPositionByColumnName =
          this.columnPositionsBySheetName.get(sheetName);

      checkRowValidity(columnPositionByColumnName.keySet(), line, sheetName, lineNumbers);

      writeToExcelRow(sheet, line, columnPositionByColumnName);
    }

    private void prepareExcelWorkbookShape(final Metadata metadata) {
      List<ExcelSheet> excelSheetsToCreate = filterAndSortExcelSheetsToCreate(metadata.getSheets());
      buildAllExcelFileSheets(this.workbook, excelSheetsToCreate, this.sxssfSheetByName);
      buildAllExcelFileColumnsOfSheets(
          excelSheetsToCreate, this.sxssfSheetByName, this.columnPositionsBySheetName);
    }

    private ErrorHandlingStrategy wrapCheckExceptionAndRethrowStrategy() {
      return new ErrorHandlingStrategy() {
        @Override
        public void handleParsingException(final Exception e) {
          throw new RuntimeException(e);
        }

        @Override
        public void handleExceptionFromListener(final Exception e, final ParsingContext context) {
          throw new RuntimeException(e);
        }
      };
    }

    private Metadata extractMetadata(final Object value) {
      try {
        return new ObjectMapper().readValue(value.toString(), Metadata.class);
      } catch (IOException e) {
        throw new IllegalStateException(e);
      }
    }

    private void checkMetadataValidity(final Metadata metadata) {
      if (metadata == null) {
        throw new RequestValidationException(
            "The instance of Document#metaData can not be built.",
            BAD_REQUEST,
            ERROR_VALIDITY_METADATA_NULL);
      }

      if (StringUtils.isBlank(metadata.getName())) {
        throw new RequestValidationException(
            "The Excel file name must be filled.", BAD_REQUEST, ERROR_VALIDITY_METADATA_NAME_BLANK);
      }

      if (!FilenameUtils.getExtension(metadata.getName())
          .equalsIgnoreCase(CommonConstants.XLSX_FILE_EXTENSION)) {
        throw new RequestValidationException(
            String.format(
                "The Excel file name [%s] must have a XLSX file extension.", metadata.getName()),
            BAD_REQUEST,
            ERROR_VALIDITY_METADATA_NAME_NOT_XLSX);
      }

      if (metadata.getSheetsNumber() <= 0) {
        throw new RequestValidationException(
            String.format(
                "The sheets number must be > 0 for the Excel file [%s].", metadata.getName()),
            BAD_REQUEST,
            ERROR_VALIDITY_METADATA_SHEETS_NUMBER_LESS_THAN_OR_EQUAL_TO_ZERO);
      }

      Set<Integer> excelSheetPositionSet = new HashSet<>();
      for (final ExcelSheet excelSheet : metadata.getSheets()) {
        checkValidityExcelSheet(metadata.getName(), excelSheetPositionSet, excelSheet);
      }
    }

    private void checkValidityExcelSheet(
        @NonNull final String excelFileName,
        @NonNull final Set<Integer> excelSheetPositionSet,
        final ExcelSheet excelSheet) {
      if (excelSheet == null) {
        throw new RequestValidationException(
            String.format(
                "The instance of ExcelSheet must be filled for the Excel file [%s].",
                excelFileName),
            BAD_REQUEST,
            ERROR_VALIDITY_EXCEL_SHEET_NULL);
      }

      if (StringUtils.isBlank(excelSheet.getName())) {
        throw new RequestValidationException(
            String.format(
                "The name of the Excel sheet must be filled for the Excel file [%s].",
                excelFileName),
            BAD_REQUEST,
            ERROR_VALIDITY_EXCEL_SHEET_NAME_BLANK);
      }

      if (excelSheet.getNumber() == null) {
        throw new RequestValidationException(
            String.format(
                "The index position of the Excel sheet [%s] must be filled for the Excel file [%s].",
                excelSheet.getName(), excelFileName),
            BAD_REQUEST,
            ERROR_VALIDITY_EXCEL_SHEET_NUMBER_NULL);
      }

      if (excelSheet.getNumber() < 0) {
        throw new RequestValidationException(
            String.format(
                "The index position of the Excel sheet [%s] must be >= 0 for the Excel file [%s].",
                excelSheet.getName(), excelFileName),
            BAD_REQUEST,
            ERROR_VALIDITY_EXCEL_SHEET_NUMBER_LESS_THAN_ZERO);
      }

      if (!excelSheetPositionSet.add(excelSheet.getNumber())) {
        throw new RequestValidationException(
            String.format(
                "The index position of the Excel sheet [%s] must be unique for the Excel file [%s].",
                excelSheet.getName(), excelFileName),
            BAD_REQUEST,
            ERROR_VALIDITY_EXCEL_SHEET_NUMBER_NOT_UNIQUE);
      }

      if (excelSheet.getColumns() != null && !excelSheet.getColumns().isEmpty()) {
        final Set<Integer> columnPositionSet = new HashSet<>();
        final Set<String> columnNameSet = new HashSet<>();

        for (final Column column : excelSheet.getColumns()) {
          checkValidityColumn(excelSheet.getName(), columnPositionSet, columnNameSet, column);
        }
      }
    }

    private void checkValidityColumn(
        @NonNull final String excelSheetName,
        @NonNull final Set<Integer> columnPositionSet,
        @NonNull final Set<String> columnNameSet,
        final Column column) {
      if (column == null) {
        throw new RequestValidationException(
            String.format("The column must be filled on the Excel sheet [%s].", excelSheetName),
            BAD_REQUEST,
            ERROR_VALIDITY_COLUMN_NULL);
      }

      if (StringUtils.isBlank(column.getName())) {
        throw new RequestValidationException(
            "The column name must be filled on the Excel sheet [%s].",
            BAD_REQUEST, ERROR_VALIDITY_COLUMN_NAME_BLANK);
      }

      if (column.getPosition() < 0) {
        throw new RequestValidationException(
            String.format(
                "The column [%s] position must be >= 0 on the Excel sheet [%s].",
                column.getName(), excelSheetName),
            BAD_REQUEST,
            ERROR_VALIDITY_COLUMN_POSITION_LESS_THAN_ZERO);
      }

      if (!columnPositionSet.add(column.getPosition())) {
        throw new RequestValidationException(
            String.format(
                "The column [%s] position must be unique on the Excel sheet [%s].",
                column.getName(), excelSheetName),
            BAD_REQUEST,
            ERROR_VALIDITY_COLUMN_POSITION_NOT_UNIQUE);
      }

      if (!columnNameSet.add(column.getName())) {
        throw new RequestValidationException(
            String.format(
                "The column name [%s] must be unique on the Excel sheet [%s].",
                column.getName(), excelSheetName),
            BAD_REQUEST,
            ERROR_VALIDITY_COLUMN_NAME_NOT_UNIQUE);
      }

      if (!ColumnFormat.checkValidityColumnFormat(column.getColumnFormat())) {
        throw new RequestValidationException(
            String.format(
                "The column [%s] format must be filled and valid on the Excel sheet [%s].",
                column.getName(), excelSheetName),
            BAD_REQUEST,
            ERROR_VALIDITY_COLUMN_FORMAT_NOT_VALID);
      }
    }

    private List<ExcelSheet> filterAndSortExcelSheetsToCreate(
        @NonNull final List<ExcelSheet> excelSheets) {
      return excelSheets.stream()
          .filter(Objects::nonNull)
          .filter(excelSheet -> StringUtils.isNotBlank(excelSheet.getName()))
          .filter(excelSheet -> Objects.nonNull(excelSheet.getNumber()))
          .sorted(comparingInt(ExcelSheet::getNumber))
          .toList();
    }

    private void checkRowValidity(
        @NonNull final Set<String> definedColumnNames,
        final ObjectNode line,
        final String sheetName,
        final HashSet<Integer> lineNumbers) {

      JsonNode lineNumber = line.get(LINE_NUMBER);

      if (!line.hasNonNull(LINE_NUMBER)) {
        throw new RequestValidationException(
            String.format(
                "The data line number of sheet content [%s] must be filled, for the Excel file [%s].",
                sheetName, this.filename),
            BAD_REQUEST,
            ERROR_VALIDITY_DATA_LINE_NUMBER_NULL);
      }

      if (lineNumber.asInt() < 1) {
        throw new RequestValidationException(
            String.format(
                "The data line number [%s] of sheet content [%s] for the Excel file [%s] must be positive.",
                String.valueOf(lineNumber.asInt()), sheetName, this.filename),
            BAD_REQUEST,
            ERROR_VALIDITY_DATA_LINE_NUMBER_LESS_THAN_ONE);
      }

      if (!lineNumbers.add(lineNumber.asInt())) {
        throw new RequestValidationException(
            String.format(
                "The data line number [%s] of sheet content [%s] must be unique, for the Excel file [%s].",
                String.valueOf(lineNumber.asInt()), sheetName, this.filename),
            BAD_REQUEST,
            ERROR_VALIDITY_DATA_LINE_NUMBER_NOT_UNIQUE);
      }

      if (definedColumnNames.stream().anyMatch(colName -> !line.has(colName))
          || line.has(definedColumnNames.size())) {
        throw new RequestValidationException(
            String.format(
                "The data line of sheet content [%s] must contain the same columns as the metadata, for the Excel file [%s].",
                sheetName, this.filename),
            BAD_REQUEST,
            ERROR_VALIDITY_DATA_LINE_NOT_STANDARD_COLUMN);
      }
    }

    private void writeToExcelRow(
        final SXSSFSheet xssfSheet,
        final ObjectNode line,
        final Map<String, Integer> columnPositionByColumnNameMap) {
      final SXSSFRow rowColumnData = xssfSheet.createRow(line.get(LINE_NUMBER).asInt());

      for (Map.Entry<String, Integer> positionByName : columnPositionByColumnNameMap.entrySet()) {

        SXSSFCell dataCell = rowColumnData.createCell(positionByName.getValue());

        dataCell.setCellStyle(columnCellStyleMap.get(positionByName.getKey()));

        JsonNode jsonNode = line.get(positionByName.getKey());
        String content = jsonNode.isNull() ? null : jsonNode.asText();

        setCellTypeFromColumnFormat(dataCell, positionByName.getKey(), content);
      }
    }

    private ResultBuildingExcelFile prepareResponse() {
      final ResultBuildingExcelFile resultBuildingExcelFile = new ResultBuildingExcelFile();

      try (final Workbook wb = this.workbook) {
        resultBuildingExcelFile.setFileName(this.filename);
        resultBuildingExcelFile.setContent(buildExcelFile(wb));
      } catch (final IOException ex) {
        String errorMsg = "An error is occurred when generating the content of Excel file";
        log.error(errorMsg, ex);
        final RestError restError =
            new RestError(ERROR_GENERATING_EXCEL_FILE, errorMsg, ExceptionLevel.ERROR);

        throw new RestException(ex.getMessage(), restError, INTERNAL_SERVER_ERROR);
      }
      return resultBuildingExcelFile;
    }

    private byte[] buildExcelFile(@NonNull final Workbook workbook) throws IOException {

      ByteArrayOutputStream os = new ByteArrayOutputStream();

      workbook.write(os);

      return os.toByteArray();
    }

    private void setCellTypeFromColumnFormat(
        SXSSFCell dataCell, String columnName, String content) {

      ColumnFormat columnFormat = columnFormatMap.get(columnName);

      switch (columnFormat) {
        case BOOLEAN:
          setCell(dataCell, content, CellType.BOOLEAN);
          break;
        case DATE:
          if (content != null && !content.isEmpty()) {
            ZonedDateTime zdt = ZonedDateTime.parse(content);
            LocalDateTime ldt = zdt.toLocalDateTime();
            Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
            dataCell.setCellValue(out);
          } else {
            dataCell.setCellValue("");
          }
          break;
        case NUMERIC:
          dataCell.setCellType(CellType.NUMERIC);
          if (content != null && !content.isEmpty()) {
            content = content.replace(',', '.');
            dataCell.setCellValue(Double.valueOf(content));
          } else {
            dataCell.setCellValue("");
          }
          break;
        case STRING:
          setCell(dataCell, content, CellType.STRING);
          break;
        case UNKNOWN:
          setCell(dataCell, content, CellType.STRING);
          break;
        default:
          dataCell.setCellType(CellType.STRING);
          if (content != null && !content.isEmpty()) {
            dataCell.setCellValue(content);
          } else {
            dataCell.setCellValue("");
          }
      }
    }

    /**
     * Get precision format of a column
     *
     * @param columnName
     * @return 2 decimals if null
     */
    private String getPrecisionAsString(String columnName) {
      StringBuilder format = new StringBuilder();

      if (columnFormatPrecision.get(columnName) != null) {
        if (columnFormatPrecision.get(columnName) < 0
            || columnFormatPrecision.get(columnName) >= MAX_PRECISION) {
          throw new MalformedBodyException(PRECISION_IS_OUT_OF_RANGE);
        }
        if (columnFormatPrecision.get(columnName) == 0) {
          format.append(0);
        } else {
          format.append("0.");

          for (int i = 0; i < columnFormatPrecision.get(columnName); i++) {
            format.append("0");
          }
        }
      } else {
        format.append("0.00");
      }
      return format.toString();
    }

    private void setCell(SXSSFCell dataCell, String content, CellType cellType) {
      dataCell.setCellType(cellType);
      if (content != null && !content.isEmpty()) {
        dataCell.setCellValue(content);
      } else {
        dataCell.setCellValue("");
      }
    }
  }
}
