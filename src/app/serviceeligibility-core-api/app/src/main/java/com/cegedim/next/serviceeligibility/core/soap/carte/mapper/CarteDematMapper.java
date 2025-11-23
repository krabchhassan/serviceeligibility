package com.cegedim.next.serviceeligibility.core.soap.carte.mapper;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.AdresseDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ContratDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ParametreDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.PrestationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat.*;
import com.cegedimassurances.norme.cartedemat.beans.*;
import org.mapstruct.Mapper;

@Mapper
public interface CarteDematMapper {

  CarteDematerialiseeResponseType carteDematDtoToCarteDematerialiseeResponseType(
      CarteDematDto carteDematDto);

  CarteDematerialiseeV2ResponseType carteDematDtoToCarteDematerialiseeV2ResponseType(
      CarteDematDto carteDematDto);

  TypeContrat contratDtoToTypeContrat(ContratDto contratDto);

  TypeAdresseContrat adresseDtoToTypeAdresseContrat(AdresseDto adresseDto);

  TypeContratV2 contratDtoToTypeContratV2(ContratDto contratDto);

  TypeBeneficiaire benefCarteDematDtoToTypeBeneficiaire(BenefCarteDematDto benefCarteDematDto);

  TypeBeneficiaireCouverture beneficiaireCouvertureDtoToTypeBeneficiaireCouverture(
      BeneficiaireCouvertureDto beneficiaireCouvertureDto);

  TypePrestation prestationDtoToTypePrestation(PrestationDto prestationDto);

  TypeParametreFormule parametreDtoToTypeParametreFormule(ParametreDto parametreDto);

  TypeDomaineDroits domaineConventionDtoToTypeDomaineDroits(
      DomaineConventionDto domaineConventionDto);

  TypeConventionDomaine conventionDtoToTypeConventionDomaine(ConventionDto conventionDto);
}
