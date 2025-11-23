package com.cegedim.next.serviceeligibility.core.features.digitalcontractinfos;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.digitalcontractinformations.DigitalContractInformationsDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.digitalcontractinformations.RequestDigitalContractInformationsDto;
import com.cegedim.next.serviceeligibility.core.business.digitalcontractinfos.service.DigitalContractInfosService;
import com.cegedim.next.serviceeligibility.core.restexceptions.v1.model.RestError;
import com.cegedim.next.serviceeligibility.core.restexceptions.v1.model.RestException;
import com.cegedim.next.serviceeligibility.core.restexceptions.v1.model.enums.ExceptionLevel;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.PermissionConstants;
import com.cegedim.next.serviceeligibility.core.utils.Util;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RestErrorConstants;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class DigitalContractInfosController {
  private static final String NOT_FOUND_MESSAGE = "No contract found for this beneficiary";
  private final DigitalContractInfosService service;

  @Autowired
  public DigitalContractInfosController(DigitalContractInfosService service) {
    this.service = service;
  }

  @GetMapping(path = "/digitalContractInformations", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(PermissionConstants.READ_CONTRACT_PERMISSION)
  public ResponseEntity<List<DigitalContractInformationsDto>> getDigitalContractInformations(
      @RequestParam String insurerId,
      @RequestParam String subscriberId,
      @RequestParam String contractNumber,
      @RequestParam String date,
      @RequestParam String domains) {
    checkDate(date);

    // Utilisation de HashSet pour garantir l'unicité de la liste
    Set<String> domainSet = new HashSet<>(Util.stringToList(domains));
    List<String> domainList = new ArrayList<>(domainSet);

    RequestDigitalContractInformationsDto request =
        new RequestDigitalContractInformationsDto(
            insurerId, subscriberId, contractNumber, date, domainList);

    List<DigitalContractInformationsDto> digitalContractInformationsDtoList =
        service.getDigitalContractInformations(request);

    if (CollectionUtils.isEmpty(digitalContractInformationsDtoList)) {
      final RestError restError =
          new RestError(
              RestErrorConstants.ERROR_DIGITAL_CONTRACT_NOT_FOUND,
              NOT_FOUND_MESSAGE,
              ExceptionLevel.ERROR);
      throw new RestException(NOT_FOUND_MESSAGE, restError, HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(digitalContractInformationsDtoList, HttpStatus.OK);
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
