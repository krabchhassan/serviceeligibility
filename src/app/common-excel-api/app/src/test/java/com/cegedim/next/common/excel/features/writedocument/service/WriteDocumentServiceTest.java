package com.cegedim.next.common.excel.features.writedocument.service;

import com.cegedim.next.common.excel.businesscomponent.document.model.Document;
import com.cegedim.next.common.excel.businesscomponent.document.model.SheetContent;
import com.cegedim.next.common.excel.configuration.ExcelTestConfiguration;
import com.cegedim.next.common.excel.constants.CommonConstants;
import com.cegedim.next.common.excel.features.readdocument.command.FileParameter;
import com.cegedim.next.common.excel.features.readdocument.service.ReadDocumentService;
import com.cegedim.next.common.excel.features.readdocument.service.ReadDocumentServiceTest;
import com.cegedim.next.common.excel.features.writedocument.command.ResultBuildingExcelFile;
import com.cegedim.next.common.excel.util.error.RequestValidationException;
import com.cegedim.next.common.excel.util.excel.ExcelUtility;
import com.cegedim.next.common.excel.util.file.FileUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.HashMap;
import lombok.NonNull;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

/** This class defines tests for {@link WriteDocumentService}. */
@ActiveProfiles("test")
@JsonTest
@Import(ExcelTestConfiguration.class)
class WriteDocumentServiceTest {

  @Autowired private ReadDocumentService readDocumentService;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private WriteDocumentService writeDocumentService;

  DataFormatter dataFormatter = new DataFormatter();

  @Test
  void should_have_valid_result_when_writing_valid_json() throws Throwable {
    File excelFile = null;

    try (ByteArrayInputStream byteArrayInputStream =
        new ByteArrayInputStream(
            FileUtils.readFileToByteArray(
                new File(this.getClass().getResource("/json/exportSimple.json").toURI())))) {
      InputStreamReader inputStreamReader = new InputStreamReader(byteArrayInputStream);
      ResultBuildingExcelFile resultBuildingExcelFile =
          this.writeDocumentService.writeExcelDocument(inputStreamReader);

      Assertions.assertNotNull(resultBuildingExcelFile);
      Assertions.assertNotNull(resultBuildingExcelFile.getFileName());
      Assertions.assertEquals(
          "Liste_Codes_NAF_pour_import_referentiel.xlsx", resultBuildingExcelFile.getFileName());
      Assertions.assertNotNull(resultBuildingExcelFile.getContent());
      Assertions.assertTrue(resultBuildingExcelFile.getContent().length > 0);

      excelFile =
          FileUtility.createlFileInATemporaryDirectory(resultBuildingExcelFile.getFileName());

      Assertions.assertNotNull(excelFile);

      FileUtils.writeByteArrayToFile(excelFile, resultBuildingExcelFile.getContent());

      Document document = buildDocument(excelFile);
      Assertions.assertNotNull(document);

      String generatedJson = toMinifyJsonString(document);
      Assertions.assertNotNull(generatedJson);
    } finally {
      if (excelFile != null && excelFile.getParentFile() != null) {
        FileUtils.deleteQuietly(excelFile.getParentFile());
      }
    }
  }

  @Test
  void should_have_valid_result_when_writing_valid_json_with_complex_type() throws Throwable {
    File excelFile = null;

    try (ByteArrayInputStream byteArrayInputStream =
        new ByteArrayInputStream(
            FileUtils.readFileToByteArray(
                new File(this.getClass().getResource("/json/exportComplex.json").toURI())))) {

      InputStreamReader inputStreamReader = new InputStreamReader(byteArrayInputStream);
      ResultBuildingExcelFile resultBuildingExcelFile =
          this.writeDocumentService.writeExcelDocument(inputStreamReader);

      Assertions.assertNotNull(resultBuildingExcelFile);
      Assertions.assertNotNull(resultBuildingExcelFile.getFileName());
      Assertions.assertEquals("Referential.xlsx", resultBuildingExcelFile.getFileName());
      Assertions.assertNotNull(resultBuildingExcelFile.getContent());
      Assertions.assertTrue(resultBuildingExcelFile.getContent().length > 0);

      excelFile =
          FileUtility.createlFileInATemporaryDirectory(resultBuildingExcelFile.getFileName());

      Assertions.assertNotNull(excelFile);

      FileUtils.writeByteArrayToFile(excelFile, resultBuildingExcelFile.getContent());

      Document document = buildDocument(excelFile);
      Assertions.assertNotNull(document);

      String generatedJson = toMinifyJsonString(document);
      Assertions.assertNotNull(generatedJson);

      MultipartFile multipartFile =
          new MockMultipartFile(
              "file",
              resultBuildingExcelFile.getFileName(),
              Files.probeContentType(excelFile.toPath()),
              IOUtils.toByteArray(new FileInputStream(excelFile)));

      Assertions.assertNotNull(multipartFile);

      FileParameter documentParameter = new FileParameter();
      documentParameter.setUploadedFile(multipartFile);

      Document documentRead = this.readDocumentService.readDocument(documentParameter);

      SheetContent firstSheetContent = documentRead.getSheetContents().get(0);
      checkSheetContentBriefly(firstSheetContent);

      // set the columns number and value associated to check
      HashMap<Integer, String> columnAndValue = new HashMap<>();
      columnAndValue.put(0, "1");
      columnAndValue.put(1, "Test format 1");
      columnAndValue.put(2, "test");
      columnAndValue.put(3, "123456789");
      columnAndValue.put(4, "2018-02-01T00:00:00Z");
      columnAndValue.put(5, "2018/02/01");
      columnAndValue.put(6, "true");
      columnAndValue.put(7, "oui");
      columnAndValue.put(8, "TRUE");
      columnAndValue.put(9, "1");

      checkDataForDefineColumnsNumber(firstSheetContent.getNextNoneEmptyRow(), columnAndValue, 1);
    } finally {
      if (excelFile != null && excelFile.getParentFile() != null) {
        FileUtils.deleteQuietly(excelFile.getParentFile());
      }
    }
  }

  @Test
  void should_have_valid_result_when_writing_json_with_color() throws Throwable {
    try (var input = new ClassPathResource("/json/exportWithColor.json").getInputStream()) {
      var inputContent = input.readAllBytes();
      try (var reader = new InputStreamReader(new ByteArrayInputStream(inputContent))) {
        var result = writeDocumentService.writeExcelDocument(reader);
        var upload =
            new MockMultipartFile(
                "out", "out.xlsx", CommonConstants.XLSX_CONTENT_TYPE, result.getContent());
        var file = new FileParameter();
        file.setUploadedFile(upload);
        var json = readDocumentService.readDocument(file);
        var mapper = new ObjectMapper();
        var actual = mapper.readTree(toMinifyJsonString(json));
        var expected = mapper.readTree(inputContent);
        Assertions.assertEquals(expected, actual);
      }
    }
  }

  @Test
  void should_trim_position_suffix_if_present() throws Throwable {
    try (var input = new ClassPathResource("/json/exportWithDuplicates.json").getInputStream()) {
      var inputContent = input.readAllBytes();
      try (var reader = new InputStreamReader(new ByteArrayInputStream(inputContent))) {
        var result = writeDocumentService.writeExcelDocument(reader);
        var upload =
            new MockMultipartFile(
                "out", "out.xlsx", CommonConstants.XLSX_CONTENT_TYPE, result.getContent());
        var file = new FileParameter();
        file.setUploadedFile(upload);
        var json = readDocumentService.readDocument(file);
        var actual = objectMapper.readTree(toMinifyJsonString(json));
        var expected = objectMapper.readTree(inputContent);
        Assertions.assertEquals(expected, actual);
      }
    }
  }

  private Document buildDocument(@NonNull final File excelFile)
      throws RequestValidationException, IOException {
    return this.readDocumentService.readDocument(builFileParameter(excelFile));
  }

  private FileParameter builFileParameter(@NonNull final File excelFile) throws IOException {
    final FileParameter fileParameter = new FileParameter();
    fileParameter.setUploadedFile(new ReadDocumentServiceTest().buildMultipartFile(excelFile));

    return fileParameter;
  }

  private String toMinifyJsonString(final Object object) throws JsonProcessingException {
    return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
  }

  /**
   * Allows to check a data line with its two columns.
   *
   * @param row a list of {@link Row} instances.
   * @param columnAndValue a HashMap of int (key) and string which are column position and excepted
   *     value
   * @param expectedDataLineNumber an expected data line number [MUST BE >=0].
   */
  private void checkDataForDefineColumnsNumber(
      @NonNull final Row row,
      HashMap<Integer, String> columnAndValue,
      final long expectedDataLineNumber) {
    Assertions.assertNotNull(row);
    Assertions.assertEquals(expectedDataLineNumber, row.getRowNum());

    columnAndValue
        .keySet()
        .forEach(
            columnPosition ->
                checkColumnValue(row, columnPosition, columnAndValue.get(columnPosition)));
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

  /**
   * Allows to check briefly an instance of {@link SheetContent}.
   *
   * @param sheetContent an instance of {@link SheetContent} [REQUIRED NOT NULL].
   */
  private void checkSheetContentBriefly(@NonNull final SheetContent sheetContent) {
    Assertions.assertNotNull(sheetContent);
    Assertions.assertNotNull(sheetContent.getSheet());
  }
}
