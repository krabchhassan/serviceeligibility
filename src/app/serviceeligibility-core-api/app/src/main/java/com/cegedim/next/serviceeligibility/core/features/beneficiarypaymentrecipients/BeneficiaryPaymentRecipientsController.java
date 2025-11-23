package com.cegedim.next.serviceeligibility.core.features.beneficiarypaymentrecipients;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_CONTRACT_PERMISSION;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarypaymentrecipients.ContractWithPaymentRecipientsDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarypaymentrecipients.RequestBeneficiaryPaymentRecipientsDto;
import com.cegedim.next.serviceeligibility.core.business.serviceprestation.service.RestServicePrestationService;
import com.cegedim.next.serviceeligibility.core.restexceptions.v1.model.RestError;
import com.cegedim.next.serviceeligibility.core.restexceptions.v1.model.RestException;
import com.cegedim.next.serviceeligibility.core.restexceptions.v1.model.enums.ExceptionLevel;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RestErrorConstants;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BeneficiaryPaymentRecipientsController {

  private static final String NOT_FOUND_MESSAGE = "No contract found for this beneficiary";

  @Autowired RestServicePrestationService service;

  @NewSpan
  @GetMapping(
      path = "/v1/beneficiaryPaymentRecipients",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_CONTRACT_PERMISSION)
  public ResponseEntity<List<ContractWithPaymentRecipientsDto>> getBeneficiaryPaymentRecipients(
      @RequestParam String insurerId,
      @RequestParam String personNumber,
      @RequestParam(required = false) String subscriberId,
      @RequestParam String date) {

    checkDate(date);

    RequestBeneficiaryPaymentRecipientsDto request =
        new RequestBeneficiaryPaymentRecipientsDto(insurerId, personNumber, subscriberId, date);
    List<ContractWithPaymentRecipientsDto> contractWithPaymentRecipientsDtoList =
        service.getBeneficiaryPaymentRecipients(request);
    if (CollectionUtils.isEmpty(contractWithPaymentRecipientsDtoList)) {
      final RestError restError =
          new RestError(
              RestErrorConstants.ERROR_DIGITAL_CONTRACT_NOT_FOUND,
              NOT_FOUND_MESSAGE,
              ExceptionLevel.ERROR);
      throw new RestException(NOT_FOUND_MESSAGE, restError, HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(contractWithPaymentRecipientsDtoList, HttpStatus.OK);
  }

  private void checkDate(String date) {
    if (!DateUtils.checkDateValidity(date)) {
      final RestError restError =
          new RestError(
              RestErrorConstants.ERROR_BENEF_PAYMENT_RECIPIENT_DATE_BAD_FORMAT,
              String.format("The date %s is not valid", date),
              ExceptionLevel.ERROR);
      throw new RestException(
          String.format("The date %s is not valid", date), restError, HttpStatus.BAD_REQUEST);
    }
  }
}
