package com.cegedim.next.common.excel.features.readdocument.service;

import com.cegedim.next.common.excel.businesscomponent.document.model.Column;
import com.cegedim.next.common.excel.businesscomponent.document.model.Document;
import com.cegedim.next.common.excel.businesscomponent.document.model.ExcelSheet;
import com.cegedim.next.common.excel.businesscomponent.document.model.SheetContent;
import com.cegedim.next.common.excel.businesscomponent.document.model.enums.ColumnFormat;
import com.cegedim.next.common.excel.configuration.ExcelTestConfiguration;
import com.cegedim.next.common.excel.error.TestException;
import com.cegedim.next.common.excel.features.readdocument.command.FileParameter;
import com.cegedim.next.common.excel.util.error.RequestValidationException;
import com.cegedim.next.common.excel.util.excel.ExcelUtility;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import lombok.NonNull;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

/** This class defines tests for {@link ReadDocumentService}. */
@ActiveProfiles("test")
@JsonTest
@Import(ExcelTestConfiguration.class)
public class ReadDocumentServiceTest {

  DataFormatter dataFormatter = new DataFormatter();

  private static final String DATE_TIME_2018_01_09T00_00_00Z = "2018-01-09T00:00:00Z";

  private static final String CODE_18 = "Code 18";

  private static final String CODE_17 = "Code 17";

  private static final String CODE_16 = "Code 16";

  private static final String CODE_15 = "Code 15";

  private static final String CODE_14 = "Code 14";

  private static final String CODE_13 = "Code 13";

  private static final String CODE_12 = "Code 12";

  private static final String CODE_11 = "Code 11";

  private static final String CODE_10 = "Code 10";

  private static final String CODE_9 = "Code 9";

  private static final String CODE_8 = "Code 8";

  private static final String CODE_7 = "Code 7";

  private static final String CODE_6 = "Code 6";

  private static final String CODE_5 = "Code 5";

  private static final String CODE_4 = "Code 4";

  private static final String CODE_3 = "Code 3";

  private static final String CODE_2 = "Code 2";

  private static final String CODE_1 = "Code 1";

  private static final String COLUMN_NAME_CODE = "Code";

  private static final String SHEET_NAME_FEUIL1 = "Feuil1";

  private static final String ERROR_MESSAGE_IT_SHOULD_THROW_A_REQUEST_VALIDATION_EXCEPTION =
      "It should throw a RequestValidationException";

  @Autowired private ReadDocumentService readDocumentService;

  /**
   * Allows to build an instance of {@link MultipartFile} from a file path.
   *
   * @param filePath file path ressource [REQUIRED NOT NULL].
   * @return an instance of {@link MultipartFile}.
   * @throws URISyntaxException
   * @throws FileNotFoundException
   * @throws IOException
   */
  private MultipartFile buildMultipartFile(final String filePath)
      throws URISyntaxException, IOException {
    URL documentURL = this.getClass().getResource(filePath);

    File file = new File(documentURL.toURI());

    return buildMultipartFile(file);
  }

  /**
   * Allows to build an instance of {@link MultipartFile} from an instance of {@link File}.
   *
   * @param file an instance of {@link File} [REQUIRED NOT NULL].
   * @return an instance of {@link MultipartFile}.
   * @throws IOException
   * @throws FileNotFoundException
   */
  public MultipartFile buildMultipartFile(final File file) throws IOException {
    MultipartFile multipartFile = null;

    try (FileInputStream input = new FileInputStream(file)) {
      multipartFile =
          new MockMultipartFile(
              "file",
              file.getName(),
              Files.probeContentType(file.toPath()),
              IOUtils.toByteArray(input));
    }

    return multipartFile;
  }

  /**
   * Allows to build an instance of {@link MultipartFile} with no content from a file path.
   *
   * @param filePath file path ressource [REQUIRED NOT NULL].
   * @return an instance of {@link MultipartFile}.
   * @throws URISyntaxException
   * @throws FileNotFoundException
   * @throws IOException
   */
  private MultipartFile buildMultipartFileWithNoContent(final String filePath)
      throws URISyntaxException, IOException {
    URL documentURL = this.getClass().getResource(filePath);

    File file = new File(documentURL.toURI());

    return buildMultipartFileWithNoContent(file);
  }

  /**
   * Allows to build an instance of {@link MultipartFile} from an instance of {@link File} whith no
   * content.
   *
   * @param file an instance of {@link File} [REQUIRED NOT NULL].
   * @return an instance of {@link MultipartFile}.
   * @throws IOException
   * @throws FileNotFoundException
   */
  private MultipartFile buildMultipartFileWithNoContent(final File file) throws IOException {
    MultipartFile multipartFile = null;

    try (FileInputStream input = new FileInputStream(file)) {
      multipartFile =
          new MockMultipartFile(
              "file",
              file.getName(),
              Files.probeContentType(file.toPath()),
              org.apache.commons.io.IOUtils.toByteArray(input)) {

            @Override
            public String getContentType() {
              return null;
            }
          };
    }

    return multipartFile;
  }

  @Test
  void should_have_an_error_when_read_document_with_inexisting_file() {
    try {
      FileParameter fileParameter = new FileParameter();

      this.readDocumentService.readDocument(fileParameter);
      Assertions.fail(ERROR_MESSAGE_IT_SHOULD_THROW_A_REQUEST_VALIDATION_EXCEPTION);
    } catch (final RequestValidationException ex) {
      Assertions.assertNotNull(ex.getStatusCode());
      Assertions.assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
      Assertions.assertEquals("The file must exist.", ex.getMessage());
    }
  }

  @Test
  void should_have_an_error_when_read_document_with_empty_file() throws TestException {
    try {
      MultipartFile multipartFile = buildMultipartFile("/documents/EmptyFile.xlsx");
      Assertions.assertNotNull(multipartFile);

      FileParameter fileParameter = new FileParameter();
      fileParameter.setUploadedFile(multipartFile);

      this.readDocumentService.readDocument(fileParameter);
      Assertions.fail(ERROR_MESSAGE_IT_SHOULD_THROW_A_REQUEST_VALIDATION_EXCEPTION);
    } catch (final RequestValidationException ex) {
      Assertions.assertNotNull(ex.getStatusCode());
      Assertions.assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
      Assertions.assertEquals("The file must be not empty.", ex.getMessage());
    } catch (final URISyntaxException | IOException ex) {
      throw new TestException(ex);
    }
  }

  @Test
  void should_have_an_error_when_read_document_with_no_content_type() throws TestException {
    try {
      MultipartFile multipartFile =
          buildMultipartFileWithNoContent("/documents/2columnsWith3Lines.xlsx");
      Assertions.assertNotNull(multipartFile);

      FileParameter fileParameter = new FileParameter();
      fileParameter.setUploadedFile(multipartFile);

      this.readDocumentService.readDocument(fileParameter);
      Assertions.fail(ERROR_MESSAGE_IT_SHOULD_THROW_A_REQUEST_VALIDATION_EXCEPTION);
    } catch (final RequestValidationException ex) {
      Assertions.assertNotNull(ex.getStatusCode());
      Assertions.assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
      Assertions.assertEquals("The content type of the file must be known.", ex.getMessage());
    } catch (final URISyntaxException | IOException ex) {
      throw new TestException(ex);
    }
  }

  @Test
  void should_have_an_error_when_read_invalid_excel_file() throws TestException {
    try {
      MultipartFile multipartFile = buildMultipartFile("/documents/InvalidFormatFile.doc");
      Assertions.assertNotNull(multipartFile);

      FileParameter documentParameter = new FileParameter();
      documentParameter.setUploadedFile(multipartFile);

      this.readDocumentService.readDocument(documentParameter);
      Assertions.fail(ERROR_MESSAGE_IT_SHOULD_THROW_A_REQUEST_VALIDATION_EXCEPTION);
    } catch (final RequestValidationException ex) {
      Assertions.assertNotNull(ex.getStatusCode());
      Assertions.assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
      Assertions.assertEquals(
          "The content type of the file must match with Excel file.", ex.getMessage());
    } catch (final URISyntaxException | IOException ex) {
      throw new TestException(ex);
    }
  }

  @Test
  void should_have_document_with_2_columns_and_three_lines() throws TestException {
    try {
      MultipartFile multipartFile = buildMultipartFile("/documents/2columnsWith3Lines.xlsx");
      Assertions.assertNotNull(multipartFile);

      FileParameter documentParameter = new FileParameter();
      documentParameter.setUploadedFile(multipartFile);

      Document document = this.readDocumentService.readDocument(documentParameter);
      checkDocumentBriefly(document, "2columnsWith3Lines.xlsx", 1);

      ExcelSheet firstExcelSheet = document.getMetadata().getSheets().get(0);
      checkExcelSheetBriefly(firstExcelSheet, SHEET_NAME_FEUIL1, 2);

      Column column1 = firstExcelSheet.getColumns().get(0);
      checkColumnBriefly(column1, COLUMN_NAME_CODE, 2, ColumnFormat.STRING);

      Column column2 = firstExcelSheet.getColumns().get(1);
      checkColumnBriefly(column2, "Label", 4, ColumnFormat.STRING);

      SheetContent firstSheetContent = document.getSheetContents().get(0);
      checkSheetContentBriefly(firstSheetContent, 3);

      int column1Position = column1.getPosition();
      int column2Position = column2.getPosition();

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Position,
          column2Position,
          1,
          "0",
          "Label of A0");
      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Position,
          column2Position,
          3,
          "1",
          "Label of A1");
      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Position,
          column2Position,
          5,
          "2",
          "Label of A2");
    } catch (final RequestValidationException | URISyntaxException | IOException ex) {
      throw new TestException(ex);
    }
  }

  @Test
  void should_have_document_with_correct_date() throws TestException {
    try {
      final MultipartFile multipartFile = buildMultipartFile("/documents/ExcelDateFile.xlsx");
      Assertions.assertNotNull(multipartFile);

      final FileParameter documentParameter = new FileParameter();
      documentParameter.setUploadedFile(multipartFile);

      final Document document = this.readDocumentService.readDocument(documentParameter);
      checkDocumentBriefly(document, "ExcelDateFile.xlsx", 1);

      final ExcelSheet firstExcelSheet = document.getMetadata().getSheets().get(0);
      checkExcelSheetBriefly(firstExcelSheet, SHEET_NAME_FEUIL1, 2);

      final Column column1 = firstExcelSheet.getColumns().get(0);
      checkColumnBriefly(column1, COLUMN_NAME_CODE, 0, ColumnFormat.STRING);

      final Column column2 = firstExcelSheet.getColumns().get(1);
      checkColumnBriefly(column2, "Date", 1, ColumnFormat.STRING);

      final SheetContent firstSheetContent = document.getSheetContents().get(0);
      checkSheetContentBriefly(firstSheetContent, 22);

      final int column1Name = column1.getPosition();
      final int column2Name = column2.getPosition();

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Name,
          column2Name,
          1,
          CODE_1,
          DATE_TIME_2018_01_09T00_00_00Z);

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Name,
          column2Name,
          2,
          CODE_2,
          DATE_TIME_2018_01_09T00_00_00Z);

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Name,
          column2Name,
          3,
          CODE_3,
          DATE_TIME_2018_01_09T00_00_00Z);

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Name,
          column2Name,
          4,
          CODE_4,
          DATE_TIME_2018_01_09T00_00_00Z);

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Name,
          column2Name,
          5,
          CODE_5,
          DATE_TIME_2018_01_09T00_00_00Z);

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Name,
          column2Name,
          6,
          CODE_6,
          DATE_TIME_2018_01_09T00_00_00Z);

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Name,
          column2Name,
          7,
          CODE_7,
          DATE_TIME_2018_01_09T00_00_00Z);

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Name,
          column2Name,
          8,
          CODE_8,
          DATE_TIME_2018_01_09T00_00_00Z);

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Name,
          column2Name,
          9,
          CODE_9,
          DATE_TIME_2018_01_09T00_00_00Z);

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Name,
          column2Name,
          10,
          CODE_10,
          DATE_TIME_2018_01_09T00_00_00Z);

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Name,
          column2Name,
          11,
          CODE_11,
          DATE_TIME_2018_01_09T00_00_00Z);

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Name,
          column2Name,
          12,
          CODE_12,
          DATE_TIME_2018_01_09T00_00_00Z);

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Name,
          column2Name,
          13,
          CODE_13,
          DATE_TIME_2018_01_09T00_00_00Z);

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Name,
          column2Name,
          14,
          CODE_14,
          DATE_TIME_2018_01_09T00_00_00Z);

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Name,
          column2Name,
          15,
          CODE_15,
          DATE_TIME_2018_01_09T00_00_00Z);

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Name,
          column2Name,
          16,
          CODE_16,
          DATE_TIME_2018_01_09T00_00_00Z);

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Name,
          column2Name,
          17,
          CODE_17,
          DATE_TIME_2018_01_09T00_00_00Z);

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(), column1Name, column2Name, 18, CODE_18, null);

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Name,
          column2Name,
          19,
          "Code 19",
          "qzd/qzdqlqzd");

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Name,
          column2Name,
          20,
          "Code 20",
          "DD/MM/YYYY");

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Name,
          column2Name,
          21,
          "Code 21",
          "2018-01-09T13:00:00Z");

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Name,
          column2Name,
          22,
          "Code 22",
          "2018-01-09T13:10:05Z");
    } catch (final RequestValidationException | URISyntaxException | IOException ex) {
      throw new TestException(ex);
    }
  }

  @Test
  void should_have_document_with_correct_number() throws TestException {
    try {
      final MultipartFile multipartFile = buildMultipartFile("/documents/ExcelNumberFile.xlsx");
      Assertions.assertNotNull(multipartFile);

      final FileParameter documentParameter = new FileParameter();
      documentParameter.setUploadedFile(multipartFile);

      final Document document = this.readDocumentService.readDocument(documentParameter);
      checkDocumentBriefly(document, "ExcelNumberFile.xlsx", 1);

      final ExcelSheet firstExcelSheet = document.getMetadata().getSheets().get(0);
      checkExcelSheetBriefly(firstExcelSheet, SHEET_NAME_FEUIL1, 2);

      final Column column1 = firstExcelSheet.getColumns().get(0);
      checkColumnBriefly(column1, COLUMN_NAME_CODE, 0, ColumnFormat.STRING);

      final Column column2 = firstExcelSheet.getColumns().get(1);
      checkColumnBriefly(column2, "Nombre", 1, ColumnFormat.STRING);

      final SheetContent firstSheetContent = document.getSheetContents().get(0);
      checkSheetContentBriefly(firstSheetContent, 18);

      final int column1Position = column1.getPosition();
      final int column2Name = column2.getPosition();

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(), column1Position, column2Name, 1, CODE_1, "1");

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(), column1Position, column2Name, 2, CODE_2, "1");

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(), column1Position, column2Name, 3, CODE_3, "1.01");

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(), column1Position, column2Name, 4, CODE_4, "1000");

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(), column1Position, column2Name, 5, CODE_5, "2000");

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Position,
          column2Name,
          6,
          CODE_6,
          "999999999999.123");

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(), column1Position, column2Name, 7, CODE_7, "1500");

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Position,
          column2Name,
          8,
          CODE_8,
          "2500.01");

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Position,
          column2Name,
          9,
          CODE_9,
          "3000.1");

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Position,
          column2Name,
          10,
          CODE_10,
          "5000.1");

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Position,
          column2Name,
          11,
          CODE_11,
          "1.01234567890123");

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(), column1Position, column2Name, 12, CODE_12, null);

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Position,
          column2Name,
          13,
          CODE_13,
          "1.1");

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Position,
          column2Name,
          14,
          CODE_14,
          "1.10");

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Position,
          column2Name,
          15,
          CODE_15,
          "1,100.1");

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Position,
          column2Name,
          16,
          CODE_16,
          "1.100,1");

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Position,
          column2Name,
          17,
          CODE_17,
          "1a1");

      checkDataForTwoColumns(
          firstSheetContent.getNextNoneEmptyRow(),
          column1Position,
          column2Name,
          18,
          CODE_18,
          "     ");
    } catch (final RequestValidationException | URISyntaxException | IOException ex) {
      throw new TestException(ex);
    }
  }

  @Test
  void should_tag_column_with_expected_color_when_present_in_xlsx()
      throws URISyntaxException, IOException {
    final MultipartFile multipartFile = buildMultipartFile("/documents/second_column_yellow.xlsx");
    FileParameter fileParameter = new FileParameter();
    fileParameter.setUploadedFile(multipartFile);

    final Document document = readDocumentService.readDocument(fileParameter);

    checkDocumentBriefly(document, "second_column_yellow.xlsx", 1);

    final ExcelSheet sheet = document.getMetadata().getSheets().get(0);

    checkExcelSheetBriefly(sheet, SHEET_NAME_FEUIL1, 2);

    final Column firstColumn = sheet.getColumns().get(0);

    checkColumnBriefly(firstColumn, "Code rejet Claim", 0, ColumnFormat.STRING);

    Assertions.assertNull(firstColumn.getColor());

    final Column secondColumn = sheet.getColumns().get(1);

    checkColumnBriefly(secondColumn, "test_filter", 1, ColumnFormat.STRING);

    Assertions.assertSame(IndexedColors.YELLOW, secondColumn.getColor());
  }

  @Test
  void should_append_position_to_duplicated_columns() throws URISyntaxException, IOException {
    final MultipartFile multipartFile = buildMultipartFile("/documents/duplicate_columns.xlsx");
    FileParameter fileParameter = new FileParameter();
    fileParameter.setUploadedFile(multipartFile);

    final Document document = readDocumentService.readDocument(fileParameter);

    final ExcelSheet sheet = document.getMetadata().getSheets().get(0);

    checkColumnBriefly(sheet.getColumns().get(0), "Code", 0, ColumnFormat.STRING);
    checkColumnBriefly(sheet.getColumns().get(1), "Assitance_filter@1", 1, ColumnFormat.STRING);
    checkColumnBriefly(sheet.getColumns().get(2), "Label", 2, ColumnFormat.STRING);
    checkColumnBriefly(sheet.getColumns().get(3), "Assitance_filter@3", 3, ColumnFormat.STRING);
  }

  /**
   * Allows to check briefly an instance of {@link Document}.
   *
   * @param document an instance of {@link Document} [REQUIRED NOT NULL].
   * @param expectedColumnName an expected column name [REQUIRED NOT NULL].
   * @param sheetsNumber a sheets number [MUST BE > 0].
   */
  private void checkDocumentBriefly(
      final Document document, final String expectedColumnName, final int sheetsNumber) {
    Assertions.assertNotNull(document);
    Assertions.assertNotNull(document.getMetadata());
    Assertions.assertEquals(expectedColumnName, document.getMetadata().getName());
    Assertions.assertEquals(
        Integer.valueOf(sheetsNumber), document.getMetadata().getSheetsNumber());
    Assertions.assertNotNull(document.getMetadata().getSheets());
    Assertions.assertEquals(
        Integer.valueOf(document.getMetadata().getSheets().size()),
        document.getMetadata().getSheetsNumber());
    Assertions.assertNotNull(document.getSheetContents());
    Assertions.assertEquals(sheetsNumber, document.getSheetContents().size());
  }

  /**
   * Allows to check briefly an instance of {@link ExcelSheet}.
   *
   * @param excelSheet an instance of {@link ExcelSheet} [REQUIRED NOT NULL].
   * @param expectedSheetName an expected sheet name [REQUIRED NOT NULL].
   * @param columnsNumber a columns number [MUST BE >=0].
   */
  private void checkExcelSheetBriefly(
      @NonNull final ExcelSheet excelSheet,
      @NonNull final String expectedSheetName,
      final int columnsNumber) {
    Assertions.assertNotNull(excelSheet);
    Assertions.assertEquals(expectedSheetName, excelSheet.getName());

    Assertions.assertNotNull(excelSheet.getColumns());
    Assertions.assertEquals(Integer.valueOf(columnsNumber), excelSheet.getColumnsNumber());
    Assertions.assertEquals(
        Integer.valueOf(excelSheet.getColumns().size()), excelSheet.getColumnsNumber());
  }

  /**
   * Allows to check briefly an instance of {@link SheetContent}.
   *
   * @param sheetContent an instance of {@link SheetContent} [REQUIRED NOT NULL].
   * @param expectedDataLinesNumber an expected data lines number [MUST BE >=0].
   */
  private void checkSheetContentBriefly(
      @NonNull final SheetContent sheetContent, final int expectedDataLinesNumber) {
    Assertions.assertNotNull(sheetContent);
    Assertions.assertNotNull(sheetContent.getSheet());
  }

  /**
   * Allows to check briefly an instance of {@link Column}.
   *
   * @param column an instance of {@link Column} [REQUIRED NOT NULL].
   * @param expectedColumnName an expected name for the column to check [REQUIRED NOT NULL].
   * @param expectedColumnPosition an expected column position [MUST BE >=0].
   * @param expectedColumnFormat an expected column format [REQUIRED NOT NULL].
   */
  private void checkColumnBriefly(
      @NonNull final Column column,
      @NonNull final String expectedColumnName,
      final int expectedColumnPosition,
      @NonNull final ColumnFormat expectedColumnFormat) {
    Assertions.assertNotNull(column);
    Assertions.assertEquals(expectedColumnName, column.getName());
    Assertions.assertEquals(expectedColumnPosition, column.getPosition());
    Assertions.assertEquals(expectedColumnFormat, column.getColumnFormat());
  }

  /** Allows to check a data line with its two columns. */
  private void checkDataForTwoColumns(
      @NonNull final Row row,
      final int column1Position,
      final int column2Position,
      final long expectedDataLineNumber,
      final String expectedColumn1Value,
      final String expectedColumn2Value) {
    Assertions.assertNotNull(row);
    Assertions.assertEquals(expectedDataLineNumber, row.getRowNum());

    checkColumnValue(row, column1Position, expectedColumn1Value);
    checkColumnValue(row, column2Position, expectedColumn2Value);
  }

  /**
   * Allows to check the column value from expected column value.
   *
   * @param row a map defining values by column names [REQUIRED NOT NULL].
   * @param columnPosition a column name [REQUIRED NOT NULL].
   * @param expectedColumnValue an expected column value [OPTIONAL].
   */
  private void checkColumnValue(
      @NonNull final Row row, final int columnPosition, final String expectedColumnValue) {
    Assertions.assertEquals(
        expectedColumnValue,
        ExcelUtility.determineCellValue(this.dataFormatter, row, columnPosition));
  }
}
