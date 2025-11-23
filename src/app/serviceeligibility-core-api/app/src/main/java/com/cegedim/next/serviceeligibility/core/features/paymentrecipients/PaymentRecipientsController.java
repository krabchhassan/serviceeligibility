package com.cegedim.next.serviceeligibility.core.features.paymentrecipients;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_PERMISSION;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.paymentrecipients.PaymentRecipientsByContractDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.paymentrecipients.RequestPaymentRecipientsByContract;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.paymentrecipients.ResponsePaymentRecipientsByContract;
import com.cegedim.next.serviceeligibility.core.business.serviceprestation.service.RestServicePrestationService;
import io.micrometer.tracing.annotation.NewSpan;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class PaymentRecipientsController {

  @Autowired RestServicePrestationService service;

  @NewSpan
  @PostMapping(
      value = "/v1/paymentRecipientsByContract",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<ResponsePaymentRecipientsByContract> getPaymentRecipientsByContract(
      @Valid @RequestBody RequestPaymentRecipientsByContract request) {
    ResponsePaymentRecipientsByContract response = new ResponsePaymentRecipientsByContract();
    List<PaymentRecipientsByContractDto> paymentRecipientsByContractDtoList =
        service.getPaymentRecipientsByContract(request);
    response.setContracts(paymentRecipientsByContractDtoList);
    return ResponseEntity.ok(response);
  }
}
