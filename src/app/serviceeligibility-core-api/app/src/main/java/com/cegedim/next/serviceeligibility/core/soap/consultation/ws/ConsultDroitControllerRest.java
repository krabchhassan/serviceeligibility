package com.cegedim.next.serviceeligibility.core.soap.consultation.ws;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_PERMISSION;

import com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.consultation_droit.GetInfoBddRequestDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.consultation_droit.GetInfoBddResponseDto;
import com.cegedim.next.serviceeligibility.core.soap.consultation.mapper.MapperConsultationDroit;
import com.cegedim.next.serviceeligibility.core.utils.RequestValidator;
import io.micrometer.tracing.annotation.NewSpan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ConsultDroitControllerRest {

  @Autowired private ConsultDroitController consultDroitController;

  @Autowired RequestValidator requestValidator;

  @NewSpan
  @GetMapping(
      value = "/v4/consultationDroits",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<GetInfoBddResponseDto> getDroits(
      @RequestBody final GetInfoBddRequestDto requete) {
    log.info("Recherche de droits v4");
    if (requete.getDates() != null) {
      requete.getDates().setDateReference(requete.getDates().getDateDebut());
    }
    requestValidator.validateRequestV4(requete);
    return MapperConsultationDroit.getResponse(
        consultDroitController.getInfoBdd(MapperConsultationDroit.getRequest(requete)));
  }
}
