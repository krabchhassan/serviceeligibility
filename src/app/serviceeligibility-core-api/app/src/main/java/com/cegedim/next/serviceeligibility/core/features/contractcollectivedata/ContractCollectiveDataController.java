package com.cegedim.next.serviceeligibility.core.features.contractcollectivedata;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_CONTRACT_PERMISSION;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ContractCollectiveDataDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ContractCollectiveDataService;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utility.UriConstants;
import com.cegedim.next.serviceeligibility.core.features.utils.ControllerUtils;
import com.cegedim.next.serviceeligibility.core.utils.ContextConstants;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RequestValidationException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RestErrorConstants;
import io.micrometer.tracing.annotation.NewSpan;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1")
public class ContractCollectiveDataController {

  @Autowired private ContractCollectiveDataService service;

  @NewSpan
  @GetMapping(
      value = UriConstants.CONTRACT_COLLECTIVE_DATA,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_CONTRACT_PERMISSION)
  public ResponseEntity<Object> getContractCollectiveData(
      @RequestParam("contractNumber") String contractNumber,
      @RequestParam("context") String context,
      @RequestParam("insurerId") String insurerId,
      @RequestParam("subscriberId") String subscriberId) {
    log.info("Recherche contrat collectif v1");
    if (StringUtils.isBlank(contractNumber)
        || StringUtils.isBlank(context)
        || StringUtils.isBlank(insurerId)
        || StringUtils.isBlank(subscriberId)) {
      throw new RequestValidationException(
          "Les parametres contractNumber, context, insuredId et subscriberId sont obligatoires !",
          HttpStatus.BAD_REQUEST);
    }
    if (!ContextConstants.TP_ONLINE.equals(context)
        && !ContextConstants.TP_OFFLINE.equals(context)
        && !ContextConstants.HTP.equals(context)) {
      throw new RequestValidationException(
          "Le contexte " + context + " n'est pas valide !", HttpStatus.BAD_REQUEST);
    }
    ContractCollectiveDataDto contractCollectiveData =
        service.findContractCollectiveData(contractNumber, context, insurerId, subscriberId);
    if (contractCollectiveData == null) {
      throw new RequestValidationException(
          "Contract not found for the parameters provided",
          HttpStatus.NOT_FOUND,
          RestErrorConstants.ERROR_CODE_CONTRACT_COLLECTIVE_DATA_NOT_FOUND);
    }
    String a = ControllerUtils.prepareOutData(contractCollectiveData);
    if (StringUtils.isNotBlank(a)) {
      return new ResponseEntity<>(a, HttpStatus.OK);
    } else {
      throw new RequestValidationException(
          "Contract not found for the parameters provided",
          HttpStatus.NOT_FOUND,
          RestErrorConstants.ERROR_CODE_CONTRACT_COLLECTIVE_DATA_NOT_FOUND);
    }
  }
}
