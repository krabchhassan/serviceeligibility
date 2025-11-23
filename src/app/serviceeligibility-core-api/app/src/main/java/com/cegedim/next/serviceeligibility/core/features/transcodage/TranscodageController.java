package com.cegedim.next.serviceeligibility.core.features.transcodage;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.CREATE_CONTRACT_PERMISSION;
import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_PERMISSION;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.transco.ServicesTranscoDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.transco.TranscodageDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.transco.TranscodageListDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.TranscodageService;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utility.UriConstants;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1" + UriConstants.TRANSCODAGE)
public class TranscodageController {

  @Autowired private TranscodageService service;

  /**
   * Resource REST qui trouve la liste des services transcodage.
   *
   * @return la reponse avec la liste des services transcodage.
   */
  @NewSpan
  @GetMapping(
      value = UriConstants.ALL_SERVICES_TRANSCODAGE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<List<ServicesTranscoDto>> getAllServicesTranscodage() {
    return new ResponseEntity<>(service.getCodesServiceTranscodage(), HttpStatus.OK);
  }

  /**
   * Resource REST qui trouve le code transco pour un service et une cle
   *
   * @return la reponse avec la liste des transcodages.
   */
  @NewSpan
  @GetMapping(value = UriConstants.TRANSCO_PAR_SERVICE, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<TranscodageListDto> getTranscodageByCle(
      @PathVariable("codeService") @NonNull String codeService,
      @PathVariable("codeObjetTransco") @NonNull String codeObjetTransco) {
    TranscodageDto transcoRequestDto = new TranscodageDto();
    transcoRequestDto.setCodeObjetTransco(codeObjetTransco);
    transcoRequestDto.setCodeService(codeService);

    TranscodageListDto transcoList = null;
    if (transcoRequestDto.getCodeService() != null
        && !transcoRequestDto.getCodeService().trim().isEmpty()
        && transcoRequestDto.getCodeObjetTransco() != null
        && !transcoRequestDto.getCodeObjetTransco().trim().isEmpty()) {
      transcoList =
          service.findTranscoByCle(
              transcoRequestDto.getCodeService().trim(), transcoRequestDto.getCodeObjetTransco());
    }
    return new ResponseEntity<>(transcoList, HttpStatus.OK);
  }

  @NewSpan
  @PostMapping(
      value = UriConstants.TRANSCO_PAR_SERVICE,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(CREATE_CONTRACT_PERMISSION)
  public ResponseEntity<Void> updateTranscodage(
      @PathVariable("codeService") @NonNull String codeService,
      @PathVariable("codeObjetTransco") @NonNull String codeObjetTransco,
      @RequestBody TranscodageListDto transcoRequestDto) {

    if (codeService.equals(transcoRequestDto.getCodeService())
        && codeObjetTransco.equals(transcoRequestDto.getCodeObjetTransco())) {

      transcoRequestDto.setCodeService(transcoRequestDto.getCodeService().trim());
      transcoRequestDto.setCodeObjetTransco(transcoRequestDto.getCodeObjetTransco().trim());
      service.updateTranscodage(transcoRequestDto);
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }
}
