package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DomaineDroitDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapperImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperDomaineDroit;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Classe de mapping de l'entite {@code DomaineDroit}. */
@Component
public class MapperDomaineDroitImpl extends GenericMapperImpl<DomaineDroit, DomaineDroitDto>
    implements MapperDomaineDroit {

  @Autowired MapperConventionnementImpl mapperConventionnement;

  @Autowired MapperPeriodeDroitImpl mapperPeriodeDroit;

  @Autowired MapperPrestationImpl mapperPrestation;

  @Autowired MapperPrioriteDroitImpl mapperPrioriteDroit;

  @Override
  public DomaineDroit dtoToEntity(DomaineDroitDto domaineDroitDto) {
    DomaineDroit domaineDroit = null;
    if (domaineDroitDto != null) {
      domaineDroit = new DomaineDroit();
      domaineDroit.setCode(domaineDroitDto.getCode());
      domaineDroit.setCodeExterne(domaineDroitDto.getCodeExterne());
      domaineDroit.setCodeGarantie(domaineDroitDto.getCodeGarantie());
      domaineDroit.setCodeOptionMutualiste(domaineDroitDto.getCodeOptionMutualiste());
      domaineDroit.setCodeProduit(domaineDroitDto.getCodeProduit());
      domaineDroit.setModeAssemblage(domaineDroitDto.getModeAssemblage());
      domaineDroit.setCodeRenvoi(domaineDroitDto.getCodeRenvoi());
      domaineDroit.setConventionnements(
          mapperConventionnement.dtoListToEntityList(domaineDroitDto.getConventionnements()));
      domaineDroit.setPeriodeDroit(
          mapperPeriodeDroit.dtoToEntity(domaineDroitDto.getPeriodeDroit()));
      domaineDroit.setLibelle(domaineDroitDto.getLibelle());
      domaineDroit.setLibelleExterne(domaineDroitDto.getLibelleExterne());
      domaineDroit.setLibelleGarantie(domaineDroitDto.getLibelleGarantie());
      domaineDroit.setLibelleOptionMutualiste(domaineDroitDto.getLibelleOptionMutualiste());
      domaineDroit.setLibelleProduit(domaineDroitDto.getLibelleProduit());
      domaineDroit.setPrestations(
          mapperPrestation.dtoListToEntityList(domaineDroitDto.getPrestations()));
      domaineDroit.setPrioriteDroit(
          mapperPrioriteDroit.dtoToEntity(domaineDroitDto.getPrioriteDroit()));
      domaineDroit.setCodeExterneProduit(domaineDroitDto.getCodeExterneProduit());
      domaineDroit.setLibelleCodeRenvoi(domaineDroitDto.getLibelleCodeRenvoi());
      domaineDroit.setTauxRemboursement(domaineDroitDto.getTauxRemboursement());
      domaineDroit.setReferenceCouverture(domaineDroitDto.getReferenceCouverture());
      domaineDroit.setCodeProfil(domaineDroitDto.getCodeProfil());
      domaineDroit.setIsEditable(domaineDroitDto.getIsEditable());
      domaineDroit.setIsSuspension(domaineDroitDto.getIsSuspension());
      domaineDroit.setNoOrdreDroit(domaineDroitDto.getNoOrdreDroit());
      domaineDroit.setDateAdhesionCouverture(domaineDroitDto.getDateAdhesionCouverture());
      domaineDroit.setUniteTauxRemboursement(domaineDroitDto.getUniteTauxRemboursement());
      domaineDroit.setOrigineDroits(domaineDroitDto.getOrigineDroits());
      domaineDroit.setCategorie(domaineDroitDto.getCategorie());
      domaineDroit.setNaturePrestation(domaineDroitDto.getNaturePrestation());
      if (StringUtils.isNotBlank(domaineDroitDto.getReferenceCouverture())) {
        domaineDroit.setReferenceCouverture(domaineDroitDto.getReferenceCouverture());
      }
      domaineDroit.setPeriodeOnline(
          mapperPeriodeDroit.dtoToEntity(domaineDroitDto.getPeriodeOnline()));
      domaineDroit.setCodeRenvoiAdditionnel(domaineDroitDto.getCodeRenvoiAdditionnel());
      domaineDroit.setLibelleCodeRenvoiAdditionnel(
          domaineDroitDto.getLibelleCodeRenvoiAdditionnel());
    }
    return domaineDroit;
  }

  @Override
  public DomaineDroitDto entityToDto(
      DomaineDroit domaineDroit,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche) {
    DomaineDroitDto domaineDroitDto = null;
    if (domaineDroit != null) {
      domaineDroitDto = new DomaineDroitDto();
      domaineDroitDto.setCode(domaineDroit.getCode());
      domaineDroitDto.setCodeExterne(domaineDroit.getCodeExterne());
      domaineDroitDto.setCodeGarantie(domaineDroit.getCodeGarantie());
      domaineDroitDto.setCodeOptionMutualiste(domaineDroit.getCodeOptionMutualiste());
      domaineDroitDto.setCodeProduit(domaineDroit.getCodeProduit());
      domaineDroit.setModeAssemblage(domaineDroitDto.getModeAssemblage());
      domaineDroitDto.setCodeRenvoi(domaineDroit.getCodeRenvoi());
      domaineDroitDto.setConventionnements(
          mapperConventionnement.entityListToDtoList(
              domaineDroit.getConventionnements(),
              profondeurRecherche,
              isFormatV2,
              isFormatV3,
              numAmcRecherche));
      domaineDroitDto.setPeriodeDroit(
          mapperPeriodeDroit.entityToDto(
              domaineDroit.getPeriodeDroit(),
              profondeurRecherche,
              isFormatV2,
              isFormatV3,
              numAmcRecherche));
      domaineDroitDto.setPeriodeOnline(
          mapperPeriodeDroit.entityToDto(
              domaineDroit.getPeriodeOnline(),
              profondeurRecherche,
              isFormatV2,
              isFormatV3,
              numAmcRecherche));
      domaineDroitDto.setLibelle(domaineDroit.getLibelle());
      domaineDroitDto.setLibelleExterne(domaineDroit.getLibelleExterne());
      domaineDroitDto.setLibelleGarantie(domaineDroit.getLibelleGarantie());
      domaineDroitDto.setLibelleOptionMutualiste(domaineDroit.getLibelleOptionMutualiste());
      domaineDroitDto.setLibelleProduit(domaineDroit.getLibelleProduit());
      domaineDroitDto.setPrestations(
          mapperPrestation.entityListToDtoList(
              domaineDroit.getPrestations(),
              profondeurRecherche,
              isFormatV2,
              isFormatV3,
              numAmcRecherche));
      domaineDroitDto.setPrioriteDroit(
          mapperPrioriteDroit.entityToDto(
              domaineDroit.getPrioriteDroit(),
              profondeurRecherche,
              isFormatV2,
              isFormatV3,
              numAmcRecherche));
      domaineDroitDto.setCodeExterneProduit(domaineDroit.getCodeExterneProduit());
      domaineDroitDto.setLibelleCodeRenvoi(domaineDroit.getLibelleCodeRenvoi());
      domaineDroitDto.setTauxRemboursement(domaineDroit.getTauxRemboursement());
      if (isFormatV2 || isFormatV3) {
        domaineDroitDto.setReferenceCouverture(domaineDroit.getReferenceCouverture());
      }
      domaineDroitDto.setCodeProfil(domaineDroit.getCodeProfil());
      domaineDroitDto.setIsEditable(domaineDroit.getIsEditable());
      domaineDroitDto.setIsSuspension(domaineDroit.getIsSuspension());
      domaineDroitDto.setNoOrdreDroit(domaineDroit.getNoOrdreDroit());
      domaineDroitDto.setDateAdhesionCouverture(domaineDroit.getDateAdhesionCouverture());
      domaineDroitDto.setUniteTauxRemboursement(domaineDroit.getUniteTauxRemboursement());
      domaineDroitDto.setOrigineDroits(domaineDroit.getOrigineDroits());
      domaineDroitDto.setCategorie(domaineDroit.getCategorie());
      domaineDroitDto.setNaturePrestation(domaineDroit.getNaturePrestation());
      domaineDroitDto.setCodeRenvoiAdditionnel(domaineDroit.getCodeRenvoiAdditionnel());
      domaineDroitDto.setLibelleCodeRenvoiAdditionnel(
          domaineDroit.getLibelleCodeRenvoiAdditionnel());
    }
    return domaineDroitDto;
  }
}
