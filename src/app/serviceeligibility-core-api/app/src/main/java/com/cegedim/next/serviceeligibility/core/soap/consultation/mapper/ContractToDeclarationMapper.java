package com.cegedim.next.serviceeligibility.core.soap.consultation.mapper;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.*;
import com.cegedim.next.serviceeligibility.core.model.domain.Conventionnement;
import com.cegedim.next.serviceeligibility.core.model.domain.Prestation;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.*;
import org.mapstruct.Mapper;

@Mapper
public interface ContractToDeclarationMapper {

  DomaineDroitDto domaineDroitContractTPToDomaineDroitDto(DomaineDroitContractTP domaine);

  PeriodeDroitDto periodeDroitContractTPToPeriodeDroitDto(
      PeriodeDroitContractTP periodeDroitContract);

  BeneficiaireDto beneficiaireContractTPToBeneficiaireDto(
      BeneficiaireContractTP beneficiaireContract);

  ContratDto contractTPToContratDto(ContractTP contract);

  PrioriteDroitDto prioriteDroitToPrioriteDroitDto(PrioriteDroitContrat prioriteDroit);

  PrestationDto prestationToPrestationDto(Prestation prestation);

  ConventionnementDto conventionnementToConventionnementDto(Conventionnement conventionnement);

  ConventionnementDto conventionnementContratToConventionnementDto(
      ConventionnementContrat conventionnementContrat);

  PrestationDto prestationContratToPrestationDto(PrestationContrat prestationContrat);
}
