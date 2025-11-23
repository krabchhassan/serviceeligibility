package com.cegedim.next.serviceeligibility.core.features.consultationdroits;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.AffiliationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.BeneficiaireDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ContratDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarantDto;
import com.cegedim.next.serviceeligibility.core.webservices.idb_clc.TypeBeneficiaire;
import com.cegedim.next.serviceeligibility.core.webservices.idb_clc.TypeContrat;
import com.cegedim.next.serviceeligibility.core.webservices.idb_clc.TypeHistoriqueAffiliation;
import com.cegedim.next.serviceeligibility.core.webservices.interrogationdroitsbenefs.DeclarantAmc;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface IDBCLCStructMapper {

  DeclarantAmc declarantDtoToDeclarantAmc(DeclarantDto declarantDto);

  TypeContrat contratDtoToTypeContrat(ContratDto contratDto);

  @Mapping(target = "historiqueAffiliations", ignore = true)
  TypeBeneficiaire beneficiaireDtoToTypeBeneficiaire(BeneficiaireDto beneficiaireDto);

  TypeHistoriqueAffiliation affiliationDtoToTypeHistoriqueAffiliation(
      AffiliationDto affiliationDto);
}
