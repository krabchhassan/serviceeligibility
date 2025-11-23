package com.cegedim.next.common.excel.features.writedocument.controller;

import com.cegedim.next.common.excel.constants.CommonConstants;
import com.cegedim.next.common.excel.features.writedocument.command.ResultBuildingExcelFile;
import com.cegedim.next.common.excel.features.writedocument.service.WriteDocumentService;
import io.micrometer.tracing.annotation.NewSpan;
import java.io.File;
import java.io.Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/** This class defines the feature controller for writing document. */
@RestController
public class WriteDocumentController {

  @Autowired private WriteDocumentService writeDocumentService;

  /**
   * Allows to read an instance of {@link Object}.
   *
   * @param reader Reader for Json defining a Excel document
   * @return an instance of {@link ResponseEntity} which defines the content of the generated Excel
   *     {@link File}.
   */
  @PostMapping(
      value = "/documents/excel",
      produces = CommonConstants.XLSX_CONTENT_TYPE,
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @NewSpan
  public ResponseEntity<byte[]> writeDocument(Reader reader) throws Throwable {
    ResultBuildingExcelFile resultBuildingExcelFile =
        this.writeDocumentService.writeExcelDocument(reader);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType(CommonConstants.XLSX_CONTENT_TYPE));
    headers.add("Access-Control-Allow-Origin", "*");
    headers.add("Access-Control-Allow-Methods", "POST");
    headers.add("Access-Control-Allow-Headers", "Content-Type");
    headers.add("Content-Disposition", "filename=" + resultBuildingExcelFile.getFileName());
    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
    headers.add("Pragma", "no-cache");
    headers.add("Expires", "0");
    return new ResponseEntity<>(resultBuildingExcelFile.getContent(), headers, HttpStatus.CREATED);
  }
}
