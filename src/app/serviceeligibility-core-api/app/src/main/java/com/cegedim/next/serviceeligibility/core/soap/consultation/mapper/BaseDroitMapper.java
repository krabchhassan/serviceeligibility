package com.cegedim.next.serviceeligibility.core.soap.consultation.mapper;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.*;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.TypeHistoriquePeriodeDroitV4;
import com.cegedimassurances.norme.base_de_droit.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BaseDroitMapper {
  @Mapping(target = "domaineDroits", ignore = true)
  TypeDeclaration declarationDtoToTypeDeclaration(DeclarationDto declarationDto);

  TypeHistoriqueAffiliation affiliationDtoToTypeHistoriqueAffiliation(
      AffiliationDto affiliationDto);

  TypeBeneficiaire beneficiaireDtoToTypeBeneficiaire(BeneficiaireDto beneficiaireDto);

  TypeHistoriquePeriodeDroitV4 periodeDroitDtoToTypeHistoriquePeriodeDroitV4(
      PeriodeDroitDto periodeDroitDto);

  @Mapping(target = "conventionnements", ignore = true)
  @Mapping(target = "prestations", ignore = true)
  TypeDomaineDroit domaineDroitDtoToTypeDomaineDroit(DomaineDroitDto domaineDroitDto);

  TypeHistoriquePeriodeDroit periodeDroitDtoToTypeHistoriquePeriodeDroit(
      PeriodeDroitDto periodeDroitDto);

  TypeConventionnement conventionnementDtoToTypeConventionnement(
      ConventionnementDto conventionnementDto);

  TypePrestation prestationDtoToTypePrestation(PrestationDto prestationDto);

  @Mapping(target = "parametres", ignore = true)
  TypeFormule prestationDtoToTypeFormule(FormuleDto formuleDto);

  TypeParametre parametreDtoToTypeParametre(ParametreDto parametreDto);

  TypePrioriteDroit prioriteDroitDtoToTypePrioriteDroit(PrioriteDroitDto prioriteDroitDto);
}
