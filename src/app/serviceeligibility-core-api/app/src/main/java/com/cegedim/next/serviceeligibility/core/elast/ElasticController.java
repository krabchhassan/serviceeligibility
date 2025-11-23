package com.cegedim.next.serviceeligibility.core.elast;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_PERMISSION;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails.BeneficiaryDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.DeclarationBackendService;
import com.cegedim.next.serviceeligibility.core.elast.contract.ElasticHistorisationContractService;
import com.cegedim.next.serviceeligibility.core.mapper.MapperBenefDetails;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.Version;
import com.fasterxml.jackson.annotation.JsonView;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@Slf4j
public class ElasticController {

  private final BenefElasticService benefElasticService;

  private final ElasticHistorisationContractService elasticHistorisationContractService;

  private final DeclarationBackendService declarationBackendService;

  public ElasticController(
      BenefElasticService benefElasticService,
      ElasticHistorisationContractService elasticHistorisationContractService,
      DeclarationBackendService declarationBackendService) {
    this.benefElasticService = benefElasticService;
    this.elasticHistorisationContractService = elasticHistorisationContractService;
    this.declarationBackendService = declarationBackendService;
  }

  @NewSpan
  @PreAuthorize(READ_PERMISSION)
  @GetMapping(value = "/autocompletion")
  public ResponseEntity<List<String>> searchElastic(
      @RequestParam String field, @RequestParam String value) {
    return new ResponseEntity<>(benefElasticService.getAutocomplete(field, value), HttpStatus.OK);
  }

  @NewSpan
  @PreAuthorize(READ_PERMISSION)
  @PostMapping(value = "/searchBeneficiaries")
  @JsonView(Version.Normal.class)
  public ResponseEntity<BenefElasticPageResult> search(@RequestBody BenefSearchRequest request) {
    return new ResponseEntity<>(benefElasticService.search(request), HttpStatus.OK);
  }

  @NewSpan
  @PreAuthorize(READ_PERMISSION)
  @GetMapping(path = "/beneficiaries/{benefId}")
  public ResponseEntity<BeneficiaryDto> getById(@PathVariable("benefId") @NonNull final String id) {
    BenefAIV5 benefAIV5 = benefElasticService.getBenefById(id);
    BeneficiaryDto beneficiaryDto = MapperBenefDetails.mapBeneficiaryDto(benefAIV5);
    List<ContratV5> filteredContracts = new ArrayList<>();
    benefAIV5
        .getContrats()
        .forEach(
            contratV5 -> {
              if (CollectionUtils.isNotEmpty(
                  declarationBackendService
                      .getDeclarationDao()
                      .findDeclarationsByBenefContrat(
                          benefAIV5.getAmc().getIdDeclarant(),
                          benefAIV5.getIdentite().getNumeroPersonne(),
                          contratV5.getNumeroContrat(),
                          null))) {
                filteredContracts.add(contratV5);
              }
            });
    // Tri par numeroAdherent décroissant, puis par numeroContrat décroissant
    filteredContracts.sort(
        Comparator.comparing(ContratV5::getNumeroAdherent, Comparator.reverseOrder())
            .thenComparing(ContratV5::getNumeroContrat, Comparator.reverseOrder()));

    // Extraction des numéros après tri
    List<String> numeroContratTP =
        filteredContracts.stream().map(ContratV5::getNumeroContrat).toList();

    beneficiaryDto.setNumerosContratTP(numeroContratTP);
    return new ResponseEntity<>(beneficiaryDto, HttpStatus.OK);
  }

  @NewSpan
  @PreAuthorize(READ_PERMISSION)
  @GetMapping(value = "/beneficiaries")
  public ResponseEntity<List<BenefAIV5>> getById(
      @RequestParam("ids") @NonNull final List<String> ids) {
    return new ResponseEntity<>(benefElasticService.getByListOfIds(ids), HttpStatus.OK);
  }
}
