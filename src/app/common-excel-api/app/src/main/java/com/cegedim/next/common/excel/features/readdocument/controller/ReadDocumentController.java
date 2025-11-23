package com.cegedim.next.common.excel.features.readdocument.controller;

import com.cegedim.next.common.excel.businesscomponent.document.model.Document;
import com.cegedim.next.common.excel.features.readdocument.command.FileParameter;
import com.cegedim.next.common.excel.features.readdocument.service.ReadDocumentService;
import com.cegedim.next.common.excel.util.error.RequestValidationException;
import io.micrometer.tracing.annotation.NewSpan;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/** This class defines the feature controller for reading document. */
@RestController
public class ReadDocumentController {

  @Autowired private ReadDocumentService readDocumentService;

  /**
   * Allows to read an instance of {@link Object}.
   *
   * @param fileParameter an instance of {@link FileParameter} [REQUIRED NOT NULL].
   * @return an instance of {@link ResponseEntity} which defines the created {@link Object}
   *     instances.
   * @throws RequestValidationException
   */
  @PostMapping(
      value = "/documents",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @NewSpan
  public ResponseEntity<Document> readDocument(@Valid final FileParameter fileParameter) {
    Document document = this.readDocumentService.readDocument(fileParameter);
    return new ResponseEntity<>(document, HttpStatus.OK);
  }
}
