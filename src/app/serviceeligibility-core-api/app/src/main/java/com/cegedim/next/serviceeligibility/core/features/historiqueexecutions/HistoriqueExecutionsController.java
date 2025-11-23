package com.cegedim.next.serviceeligibility.core.features.historiqueexecutions;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.CREATE_CONTRACT_PERMISSION;

import com.cegedim.next.serviceeligibility.core.business.historiqueexecution.service.RestHistoriqueExecutionService;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecution;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RequestValidationException;
import io.micrometer.tracing.annotation.NewSpan;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/historiqueExecutions")
@RequiredArgsConstructor
public class HistoriqueExecutionsController {

  private final RestHistoriqueExecutionService service;

  @NewSpan
  @DeleteMapping
  @PreAuthorize(CREATE_CONTRACT_PERMISSION)
  public ResponseEntity<Void> deleteHistoriqueExecutionBatchIdDeclarant(
      @RequestParam(value = "batch") String batch,
      @RequestParam(value = "idDeclarant", required = false) String idDeclarant,
      @RequestParam(value = "codeService", required = false) String codeService,
      @RequestParam(value = "dateExecution", required = false) String dateExecutionStr) {
    if (!("613".equals(batch) || "614".equals(batch))) {
      HistoriqueExecution histoExec =
          service.findLastByBatchIdDeclarant(batch, idDeclarant, codeService);
      if (histoExec == null) {
        String message;
        if (StringUtils.isBlank(idDeclarant)) {
          message = "Historique execution with batch " + batch + " doesn't exist.";
        } else {
          message =
              "Historique execution with batch "
                  + batch
                  + " and declarant id "
                  + idDeclarant
                  + " doesn't exist.";
        }
        throw new RequestValidationException(message, HttpStatus.NOT_FOUND);
      }
      service.deleteHisto(histoExec);
    } else {
      if (StringUtils.isBlank(dateExecutionStr)) {
        throw new RequestValidationException(
            "dateExecution doit être renseignée pour le batch " + batch, HttpStatus.BAD_REQUEST);
      }
      try {
        LocalDateTime dateExecution =
            LocalDateTime.parse(dateExecutionStr, DateTimeFormatter.ISO_DATE_TIME);
        service.deleteByBatchDateExecution(batch, dateExecution, codeService);
      } catch (Exception e) {
        throw new RequestValidationException(
            "dateExecution "
                + dateExecutionStr
                + "n'a pas pu être parsée au format 'yyyy-MM-ddTHH:mm:ss'",
            HttpStatus.BAD_REQUEST);
      }
    }

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
