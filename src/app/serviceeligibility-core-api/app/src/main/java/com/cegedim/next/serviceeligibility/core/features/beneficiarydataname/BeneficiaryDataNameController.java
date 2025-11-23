package com.cegedim.next.serviceeligibility.core.features.beneficiarydataname;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydataname.BeneficiaryDataNameDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydataname.RequestBeneficiaryDataNameDto;
import com.cegedim.next.serviceeligibility.core.business.beneficiaire.service.RestBeneficaryDataNameService;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.restexceptions.v1.model.RestError;
import com.cegedim.next.serviceeligibility.core.restexceptions.v1.model.RestException;
import com.cegedim.next.serviceeligibility.core.restexceptions.v1.model.enums.ExceptionLevel;
import com.cegedim.next.serviceeligibility.core.utils.PermissionConstants;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RestErrorConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BeneficiaryDataNameController {

  private static final String NOT_FOUND_MESSAGE = "No beneficiary found for this request";

  @Autowired RestBeneficaryDataNameService service;

  @GetMapping(path = "/v1/beneficiaryDataName", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(PermissionConstants.READ_CONTRACT_PERMISSION)
  public ResponseEntity<BeneficiaryDataNameDto> getBeneficiaryDataName(
      @RequestParam String personNumber, @RequestParam String contractNumber) {

    RequestBeneficiaryDataNameDto request =
        new RequestBeneficiaryDataNameDto(personNumber, contractNumber);
    BenefAIV5 benef = service.getBenefForBeneficiaryDataName(request);
    if (benef == null) {
      final RestError restError =
          new RestError(
              RestErrorConstants.ERROR_CODE_PAU_BENEFICIARY_NOT_FOUND,
              NOT_FOUND_MESSAGE,
              ExceptionLevel.ERROR);
      throw new RestException(NOT_FOUND_MESSAGE, restError, HttpStatus.NOT_FOUND);
    }

    BeneficiaryDataNameDto beneficiaryDataNameDto = service.getBeneficiaryDataName(request, benef);

    return new ResponseEntity<>(beneficiaryDataNameDto, HttpStatus.OK);
  }
}
