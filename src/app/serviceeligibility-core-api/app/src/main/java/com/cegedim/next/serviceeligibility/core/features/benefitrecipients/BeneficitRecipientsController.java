package com.cegedim.next.serviceeligibility.core.features.benefitrecipients;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_CONTRACT_PERMISSION;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.benefitrecipients.BenefitRecipientsDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.benefitrecipients.RequestBeneficitRecipientsDto;
import com.cegedim.next.serviceeligibility.core.business.beneficiaire.service.RestBeneficitRecipientsService;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RequestValidationException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RestErrorConstants;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BeneficitRecipientsController {

  private static final String NOT_FOUND_MESSAGE = "No beneficiary found for this request";

  @Autowired RestBeneficitRecipientsService service;

  @NewSpan
  @GetMapping(path = "/v1/benefitrecipients", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_CONTRACT_PERMISSION)
  public ResponseEntity<List<BenefitRecipientsDto>> getBeneficitRecipients(
      @RequestParam String idPerson,
      @RequestParam(required = false) String subscriberId,
      @RequestParam String contractNumber,
      @RequestParam String insurerId) {

    RequestBeneficitRecipientsDto request =
        new RequestBeneficitRecipientsDto(idPerson, subscriberId, contractNumber, insurerId);
    BenefAIV5 benef = service.getBenefForBenefitRecipients(request);
    if (benef == null) {
      throw new RequestValidationException(
          NOT_FOUND_MESSAGE,
          HttpStatus.NOT_FOUND,
          RestErrorConstants.ERROR_BENEF_FOR_BENEFIT_RECIPIENT_NOTFOUND);
    }

    List<BenefitRecipientsDto> benefitRecipientsDtoList =
        service.getBenefitRecipients(request, benef);
    return new ResponseEntity<>(benefitRecipientsDtoList, HttpStatus.OK);
  }
}
