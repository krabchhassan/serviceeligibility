package com.cegedim.next.serviceeligibility.core.features.importexport.dto;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/** This class defines the parameters for the import data. */
public class ImportParameter {

  @Valid @Getter @Setter private MultipartFile uploadedFile;
}
