package com.cegedim.next.serviceeligibility.core.business.consultationdroits;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ContractDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarantDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.data.DemandeInfoBeneficiaire;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.exception.ExceptionServiceDroitNonOuvert;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.VisiodroitUtils;
import com.cegedim.next.serviceeligibility.core.business.declarant.service.DeclarantService;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsultationDroitsService {

  private final DeclarantService declarantService;
  private final ConsultationDroitsSteps consultationDroitsSteps;

  @ContinueSpan(log = "getContractsBenefIDB")
  public List<ContractDto> getContractsBenefIDB(
      DemandeInfoBeneficiaire infoBenef, boolean limitWaranties, Declarant declarant) {
    DeclarantDto declarantDto =
        declarantService.mapDeclarantForAmcRecherche(infoBenef.getNumeroPrefectoral(), declarant);
    return this.getContractsBenef(infoBenef, limitWaranties, declarantDto);
  }

  @ContinueSpan(log = "getContractsBenefCLC")
  public List<ContractDto> getContractsBenefCLC(
      DemandeInfoBeneficiaire infoBenef, boolean limitWaranties) {
    DeclarantDto declarant = declarantService.getAmcRecherche(infoBenef.getNumeroPrefectoral());
    return this.getContractsBenef(infoBenef, limitWaranties, declarant);
  }

  private List<ContractDto> getContractsBenef(
      DemandeInfoBeneficiaire infoBenef, boolean limitWaranties, DeclarantDto declarant) {
    List<ContractDto> contractList =
        consultationDroitsSteps.getContratsValides(infoBenef, declarant, limitWaranties);

    Set<String> segmentsRecherche = VisiodroitUtils.getSegmentRecherche(infoBenef);

    // Détecter 6003 ... ?
    consultationDroitsSteps.checkDomaineDroit(contractList, segmentsRecherche);

    // Detecter 6002 : Droits du bénéficiaire non ouverts
    if (!consultationDroitsSteps.checkValiditePeriodeDroit(
        contractList, infoBenef.getDateReference(), infoBenef.getDateFin(), false)) {
      throw new ExceptionServiceDroitNonOuvert();
    }

    return contractList;
  }

  @ContinueSpan(log = "getContractsCarteTiersPayantCLC")
  public List<ContractDto> getContractsCarteTiersPayantCLC(DemandeInfoBeneficiaire infoBenef) {
    DeclarantDto declarant = declarantService.getAmcRecherche(infoBenef.getNumeroPrefectoral());
    return getContractsCarteTiersPayant(infoBenef, declarant);
  }

  private List<ContractDto> getContractsCarteTiersPayant(
      DemandeInfoBeneficiaire infoBenef, DeclarantDto declarant) {
    List<ContractDto> contractList =
        consultationDroitsSteps.getContratsValidesCarteTiersPayant(infoBenef, declarant);

    // Detecter 6002 : Droits du bénéficiaire non ouverts
    if (!consultationDroitsSteps.checkValiditePeriodeDroit(
        contractList, infoBenef.getDateReference(), infoBenef.getDateFin(), true)) {
      throw new ExceptionServiceDroitNonOuvert();
    }

    return contractList;
  }

  @ContinueSpan(log = "getContractsCarteTiersPayantIDB")
  public List<ContractDto> getContractsCarteTiersPayantIDB(
      DemandeInfoBeneficiaire infoBenef, Declarant declarant) {
    DeclarantDto declarantDto =
        declarantService.mapDeclarantForAmcRecherche(infoBenef.getNumeroPrefectoral(), declarant);
    return getContractsCarteTiersPayant(infoBenef, declarantDto);
  }
}
