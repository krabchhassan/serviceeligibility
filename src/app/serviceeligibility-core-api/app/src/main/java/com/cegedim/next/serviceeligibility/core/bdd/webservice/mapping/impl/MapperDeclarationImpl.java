package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarantDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.PeriodeCMUOuvertDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.PeriodeDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.PeriodeSuspensionDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapperImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperDeclaration;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.business.declarant.service.DeclarantService;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.PeriodeSuspensionDeclaration;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Classe de mapping de l'entite {@code Declaration}. */
@Component
public class MapperDeclarationImpl extends GenericMapperImpl<Declaration, DeclarationDto>
    implements MapperDeclaration {

  @Autowired MapperBeneficiaireImpl mapperBeneficiaire;

  @Autowired MapperContratImpl mapperContrat;

  @Autowired MapperDomaineDroitImpl mapperDomaineDroit;

  @Autowired MapperDeclarantImpl mapperDeclarant;

  @Autowired private DeclarantService declarantService;

  @Override
  public Declaration dtoToEntity(DeclarationDto declarationDto) {
    Declaration declaration = null;
    if (declarationDto != null) {
      declaration = new Declaration();
      declaration.setBeneficiaire(mapperBeneficiaire.dtoToEntity(declarationDto.getBeneficiaire()));
      declaration.setCodeEtat(declarationDto.getCodeEtat());
      declaration.setContrat(mapperContrat.dtoToEntity(declarationDto.getContrat()));
      declaration.setIdDeclarant(getDeclarantDto(declarationDto).getNumeroPrefectoral());
      declaration.setDomaineDroits(
          mapperDomaineDroit.dtoListToEntityList(declarationDto.getDomaineDroits()));
      declaration.setEffetDebut(declarationDto.getEffetDebut());
      declaration.setReferenceExterne(declarationDto.getReferenceExterne());
      declaration.setIsCarteTPaEditer(declarationDto.getIsCarteTPaEditer());
      declaration.setNomFichierOrigine(declarationDto.getNomFichierOrigine());
      declaration.setCarteTPaEditerOuDigitale(declarationDto.getCarteTPaEditerOuDigitale());
    }
    return declaration;
  }

  @Override
  public DeclarationDto entityToDto(
      Declaration declaration,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche) {
    DeclarationDto declarationDto = null;
    if (declaration != null) {
      declarationDto = new DeclarationDto();
      declarationDto.setIdentifiantTechnique(declaration.get_id());
      declarationDto.setBeneficiaire(
          mapperBeneficiaire.entityToDto(
              declaration.getBeneficiaire(),
              profondeurRecherche,
              isFormatV2,
              isFormatV3,
              numAmcRecherche));
      declarationDto.setCodeEtat(declaration.getCodeEtat());
      declarationDto.setContrat(
          mapperContrat.entityToDto(
              declaration.getContrat(),
              profondeurRecherche,
              isFormatV2,
              isFormatV3,
              numAmcRecherche));

      Declarant declarant = declarantService.findById(declaration.getIdDeclarant());

      if (declarant != null) {
        declarationDto.setDeclarantAmc(
            mapperDeclarant.entityToDto(
                declarant, profondeurRecherche, isFormatV2, isFormatV3, numAmcRecherche));
      }

      if (checkMapDomaineDroit(profondeurRecherche)) {
        declarationDto.setDomaineDroits(
            mapperDomaineDroit.entityListToDtoList(
                declaration.getDomaineDroits(),
                profondeurRecherche,
                isFormatV2,
                isFormatV3,
                numAmcRecherche));
      }
      declarationDto.setEffetDebut(declaration.getEffetDebut());
      declarationDto.setReferenceExterne(declaration.getReferenceExterne());
      declarationDto.setIsCarteTPaEditer(declaration.getIsCarteTPaEditer());
      declarationDto.setNomFichierOrigine(declaration.getNomFichierOrigine());
      declarationDto.setCarteTPaEditerOuDigitale(declaration.getCarteTPaEditerOuDigitale());
      declarationDto.setDateRestitution(declaration.getDateRestitution());

      declarationDto.setEtatSuspension(declaration.getEtatSuspension());
      declarationDto.getContrat().setPeriodeSuspensions(new ArrayList<>());
      for (PeriodeSuspensionDeclaration periodeSuspension :
          ListUtils.emptyIfNull(declaration.getContrat().getPeriodeSuspensions())) {
        declarationDto
            .getContrat()
            .getPeriodeSuspensions()
            .add(new PeriodeSuspensionDto(periodeSuspension));
      }

      if (CollectionUtils.isNotEmpty(declaration.getContrat().getPeriodeResponsableOuverts())) {
        List<PeriodeDto> newPers =
            declaration.getContrat().getPeriodeResponsableOuverts().stream()
                .map(PeriodeDto::new)
                .collect(Collectors.toCollection(ArrayList::new));
        declarationDto.getContrat().setPeriodeResponsableOuverts(newPers);
      }

      if (CollectionUtils.isNotEmpty(declaration.getContrat().getPeriodeCMUOuverts())) {
        List<PeriodeCMUOuvertDto> newPers =
            declaration.getContrat().getPeriodeCMUOuverts().stream()
                .map(PeriodeCMUOuvertDto::new)
                .collect(Collectors.toCollection(ArrayList::new));
        declarationDto.getContrat().setPeriodeCMUOuverts(newPers);
      }
    }
    return declarationDto;
  }

  /**
   * Retrouve le bon declarant.
   *
   * @param declarationDto
   * @return
   */
  private DeclarantDto getDeclarantDto(DeclarationDto declarationDto) {
    return declarationDto.getDeclarantAmc();
  }

  /**
   * Methode verifiant la valeur de profondeur de recherche.
   *
   * @return faux si la profondeur de recherche est SANS_DOMAINES ou non renseigne vrai sinon
   */
  private boolean checkMapDomaineDroit(TypeProfondeurRechercheService profondeurRecherche) {
    return profondeurRecherche.equals(TypeProfondeurRechercheService.AVEC_FORMULES)
        || profondeurRecherche.equals(TypeProfondeurRechercheService.SANS_FORMULES);
  }
}
