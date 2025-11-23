package com.cegedim.next.common.excel.features.readdocument.command;

import com.cegedim.next.common.excel.util.error.RestErrorConstants;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/** This class defines the parameters for Excel file uploading. */
public class FileParameter {

  @NotNull(
      message =
          RestErrorConstants.ERROR_VALIDITY_DOCUMENT_UPLOADED_FILE_NOT_FILLED
              + ";The parameter [uploadedFile] must be filled.")
  @Getter
  @Setter
  private MultipartFile uploadedFile;
}
