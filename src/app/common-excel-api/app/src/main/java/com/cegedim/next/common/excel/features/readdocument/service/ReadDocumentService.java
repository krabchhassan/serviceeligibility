package com.cegedim.next.common.excel.features.readdocument.service;

import com.cegedim.common.base.rest.exceptions.model.RestError;
import com.cegedim.common.base.rest.exceptions.model.RestException;
import com.cegedim.common.base.rest.exceptions.model.enums.ExceptionLevel;
import com.cegedim.next.common.excel.businesscomponent.document.model.Document;
import com.cegedim.next.common.excel.businesscomponent.document.model.ExcelSheet;
import com.cegedim.next.common.excel.businesscomponent.document.model.Metadata;
import com.cegedim.next.common.excel.businesscomponent.document.model.SheetContent;
import com.cegedim.next.common.excel.constants.CommonConstants;
import com.cegedim.next.common.excel.features.readdocument.command.FileParameter;
import com.cegedim.next.common.excel.util.error.RequestValidationException;
import com.cegedim.next.common.excel.util.error.RestErrorConstants;
import com.cegedim.next.common.excel.util.excel.ExcelUtility;
import com.monitorjbl.xlsx.StreamingReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

/** This class defines the feature service for reading document. */
@Slf4j
public class ReadDocumentService {

  private static final String MESSAGE_ERROR_ACCESS_FILE = "The access of the file failed.";

  /**
   * Allows to read an Excel file and build an instance of {@link Document}.
   *
   * @param fileParameter an instance of {@link FileParameter} [REQUIRED NOT NULL].
   * @return an instance of {@link Document}.
   * @throws RequestValidationException
   */
  public Document readDocument(@NonNull final FileParameter fileParameter) {
    ZipSecureFile.setMinInflateRatio(-1.0d);
    checkParametersValidity(fileParameter);

    final Document document = new Document();

    final Metadata metadata = new Metadata();
    metadata.setName(FilenameUtils.getName(fileParameter.getUploadedFile().getOriginalFilename()));

    document.setMetadata(metadata);

    try {
      final Workbook workbook =
          StreamingReader.builder()
              .sstCacheSize(5000)
              .open(fileParameter.getUploadedFile().getInputStream());
      for (int sheetNumber = 0; sheetNumber < workbook.getNumberOfSheets(); sheetNumber++) {
        final Sheet sheet = workbook.getSheetAt(sheetNumber);

        if (sheet != null) {
          final ExcelSheet excelSheet = new ExcelSheet();
          excelSheet.setName(sheet.getSheetName());
          excelSheet.setNumber(sheetNumber);
          excelSheet.setColumns(ExcelUtility.getAllColumns(sheet));

          metadata.getSheets().add(excelSheet);

          final SheetContent sheetContent = new SheetContent();
          sheetContent.setSheet(excelSheet);
          Iterator<Row> iterator = sheet.iterator();
          sheetContent.setRows(iterator);
          document.getSheetContents().add(sheetContent);
        }
      }
    } catch (NoSuchElementException ex) {
      throw new RequestValidationException(
          "The file contains an empty sheet.", HttpStatus.BAD_REQUEST);
    } catch (final EncryptedDocumentException | IOException ex) {
      final String developerMessage = "An error is occured when accessing to the MS Excel file";
      log.error(developerMessage, ex);
      final RestError restError =
          new RestError(
              RestErrorConstants.ERROR_LOADING_EXCEL_FILE, developerMessage, ExceptionLevel.ERROR);

      throw new RestException(ex.getMessage(), restError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return document;
  }

  /**
   * Allows to check parameters validity. When a parameter is not valid then we throw an instance of
   * {@link RequestValidationException}.
   *
   * @param fileParameter an instance of {@link FileParameter} [REQUIRED NOT NULL].
   * @throws RequestValidationException
   */
  private void checkParametersValidity(@NonNull final FileParameter fileParameter) {
    if (fileParameter.getUploadedFile() == null) {
      throw new RequestValidationException("The file must exist.", HttpStatus.BAD_REQUEST);
    }

    if (fileParameter.getUploadedFile().isEmpty()) {
      throw new RequestValidationException("The file must be not empty.", HttpStatus.BAD_REQUEST);
    }

    if (fileParameter.getUploadedFile().getContentType() == null) {
      throw new RequestValidationException(
          "The content type of the file must be known.", HttpStatus.BAD_REQUEST);
    }

    if (!checkExcelContentType(fileParameter.getUploadedFile().getContentType())) {
      throw new RequestValidationException(
          "The content type of the file must match with Excel file.", HttpStatus.BAD_REQUEST);
    }

    if (!isFileWeightValid(fileParameter.getUploadedFile())) {
      throw new RequestValidationException(
          String.format(
              "The maximum weight for the file %s must be %s MB.",
              fileParameter.getUploadedFile().getOriginalFilename(),
              CommonConstants.MAX_FILE_SIZE_IN_MEGA_BYTES),
          HttpStatus.BAD_REQUEST);
    }

    try {
      if (fileParameter.getUploadedFile().getInputStream() == null) {
        throw new RequestValidationException(
            "The input stream of the file must exist.", HttpStatus.BAD_REQUEST);
      }
    } catch (final IOException ex) {
      throw new RequestValidationException(MESSAGE_ERROR_ACCESS_FILE, ex, HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Allows to check if the content type defines an Excel file.
   *
   * @param contentType a content type [OPTIONAL].
   * @return <code>true</code> if the content type defines an Excel file else <code>false</code>.
   */
  private boolean checkExcelContentType(final String contentType) {
    return CommonConstants.XLS_CONTENT_TYPE.equals(contentType)
        || CommonConstants.XLSX_CONTENT_TYPE.equals(contentType);
  }

  /**
   * Allows to know if the file weight is valid.
   *
   * @param multipartFile an instance of {@link MultipartFile} [OPTIONAL].
   * @return <tt>true</tt> if the file weight is valid else <tt>false</tt>.
   */
  private boolean isFileWeightValid(final MultipartFile multipartFile) {
    return (multipartFile != null)
        && (multipartFile.getSize() <= CommonConstants.MAX_FILE_SIZE_IN_BYTES);
  }
}
