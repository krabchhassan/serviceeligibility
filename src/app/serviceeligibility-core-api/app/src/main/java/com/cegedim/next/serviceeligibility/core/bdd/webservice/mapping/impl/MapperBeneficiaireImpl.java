package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.BeneficiaireDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.CodePeriodeDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.NirRattachementRODto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.PeriodeDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.TeletransmissionDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapperImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperAdresseAvecFixe;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperAffiliation;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperBeneficiaire;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.BeneficiaireV2;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

/** Classe de mapping de l'entite {@code Beneficiaire}. */
@Component
public class MapperBeneficiaireImpl extends GenericMapperImpl<BeneficiaireV2, BeneficiaireDto>
    implements MapperBeneficiaire {
  public MapperBeneficiaireImpl(
      MapperAdresseAvecFixe mapperAdresseAvecFixe, MapperAffiliation mapperAffiliation) {
    this.mapperAdresseAvecFixe = mapperAdresseAvecFixe;
    this.mapperAffiliation = mapperAffiliation;
  }

  MapperAdresseAvecFixe mapperAdresseAvecFixe;

  MapperAffiliation mapperAffiliation;

  @Override
  public BeneficiaireV2 dtoToEntity(BeneficiaireDto beneficiaireDto) {
    BeneficiaireV2 beneficiaire = null;
    if (beneficiaireDto != null) {
      beneficiaire = new BeneficiaireV2();
      beneficiaire.setAdresses(
          mapperAdresseAvecFixe.dtoListToEntityList(beneficiaireDto.getAdresses()));
      beneficiaire.setCleNirBeneficiaire(beneficiaireDto.getCleNirBeneficiaire());
      beneficiaire.setCleNirOd1(beneficiaireDto.getCleNirOd1());
      beneficiaire.setCleNirOd2(beneficiaireDto.getCleNirOd2());
      beneficiaire.setDateNaissance(beneficiaireDto.getDateNaissance());
      beneficiaire.setAffiliation(mapperAffiliation.dtoToEntity(beneficiaireDto.getAffiliation()));
      beneficiaire.setInsc(beneficiaireDto.getInsc());
      beneficiaire.setNirBeneficiaire(beneficiaireDto.getNirBeneficiaire());
      beneficiaire.setNirOd1(beneficiaireDto.getNirOd1());
      beneficiaire.setNirOd2(beneficiaireDto.getNirOd2());
      beneficiaire.setNumeroPersonne(beneficiaireDto.getNumeroPersonne());
      beneficiaire.setRangNaissance(beneficiaireDto.getRangNaissance());
      beneficiaire.setRefExternePersonne(beneficiaireDto.getRefExternePersonne());
    }
    return beneficiaire;
  }

  @Override
  public BeneficiaireDto entityToDto(
      BeneficiaireV2 beneficiaire,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche) {
    BeneficiaireDto beneficiaireDto = null;
    if (beneficiaire != null) {
      beneficiaireDto = new BeneficiaireDto();
      beneficiaireDto.setAdresses(
          mapperAdresseAvecFixe.entityListToDtoList(
              beneficiaire.getAdresses(),
              profondeurRecherche,
              isFormatV2,
              isFormatV3,
              numAmcRecherche));
      beneficiaireDto.setCleNirBeneficiaire(beneficiaire.getCleNirBeneficiaire());
      beneficiaireDto.setCleNirOd1(beneficiaire.getCleNirOd1());
      beneficiaireDto.setCleNirOd2(beneficiaire.getCleNirOd2());
      beneficiaireDto.setDateNaissance(beneficiaire.getDateNaissance());
      beneficiaireDto.setAffiliation(
          mapperAffiliation.entityToDto(
              beneficiaire.getAffiliation(),
              profondeurRecherche,
              isFormatV2,
              isFormatV3,
              numAmcRecherche));
      beneficiaireDto.setInsc(beneficiaire.getInsc());
      beneficiaireDto.setNirBeneficiaire(beneficiaire.getNirBeneficiaire());
      beneficiaireDto.setNirOd1(beneficiaire.getNirOd1());
      beneficiaireDto.setNirOd2(beneficiaire.getNirOd2());
      beneficiaireDto.setNumeroPersonne(beneficiaire.getNumeroPersonne());
      beneficiaireDto.setRangNaissance(beneficiaire.getRangNaissance());
      beneficiaireDto.setRefExternePersonne(beneficiaire.getRefExternePersonne());
      beneficiaireDto.setDateRadiation(beneficiaire.getDateRadiation());

      if (CollectionUtils.isNotEmpty(beneficiaire.getAffiliationsRO())) {
        List<NirRattachementRODto> newAff =
            beneficiaire.getAffiliationsRO().stream()
                .map(NirRattachementRODto::new)
                .collect(Collectors.toCollection(ArrayList::new));
        beneficiaireDto.setAffiliationsRO(newAff);
      }

      if (CollectionUtils.isNotEmpty(beneficiaire.getPeriodesMedecinTraitant())) {
        List<PeriodeDto> newPers =
            beneficiaire.getPeriodesMedecinTraitant().stream()
                .map(PeriodeDto::new)
                .collect(Collectors.toCollection(ArrayList::new));
        beneficiaireDto.setPeriodesMedecinTraitant(newPers);
      }

      if (CollectionUtils.isNotEmpty(beneficiaire.getTeletransmissions())) {
        List<TeletransmissionDto> newTele =
            beneficiaire.getTeletransmissions().stream()
                .map(TeletransmissionDto::new)
                .collect(Collectors.toCollection(ArrayList::new));
        beneficiaireDto.setTeletransmissions(newTele);
      }

      if (CollectionUtils.isNotEmpty(beneficiaire.getRegimesParticuliers())) {
        List<CodePeriodeDto> newPers =
            beneficiaire.getRegimesParticuliers().stream()
                .map(CodePeriodeDto::new)
                .collect(Collectors.toCollection(ArrayList::new));
        beneficiaireDto.setRegimesParticuliers(newPers);
      }

      if (CollectionUtils.isNotEmpty(beneficiaire.getSituationsParticulieres())) {
        List<CodePeriodeDto> newPers =
            beneficiaire.getSituationsParticulieres().stream()
                .map(CodePeriodeDto::new)
                .collect(Collectors.toCollection(ArrayList::new));
        beneficiaireDto.setSituationsParticulieres(newPers);
      }
    }
    return beneficiaireDto;
  }
}
