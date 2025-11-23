package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl.cartedemat;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat.BeneficiaireCouvertureDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapperImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl.MapperPrestationImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.cartedemat.MapperBeneficiaireCouverture;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Classe de mapping de l'entite {@code DomaineDroit}. */
@Component
public class MapperBeneficiaireCouvertureImpl
    extends GenericMapperImpl<DomaineDroit, BeneficiaireCouvertureDto>
    implements MapperBeneficiaireCouverture {

  @Autowired MapperPrestationImpl mapperPrestation;

  @Override
  public DomaineDroit dtoToEntity(BeneficiaireCouvertureDto beneficiaireCouvertureDto) {
    return null;
  }

  @Override
  public BeneficiaireCouvertureDto entityToDto(
      DomaineDroit domaineDroit,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche) {
    BeneficiaireCouvertureDto beneficiaireCouvertureDto = null;
    if (domaineDroit != null) {
      beneficiaireCouvertureDto = new BeneficiaireCouvertureDto();

      beneficiaireCouvertureDto.setCodeDomaine(domaineDroit.getCode());

      beneficiaireCouvertureDto.setTauxRemboursement(domaineDroit.getTauxRemboursement());
      beneficiaireCouvertureDto.setUniteTauxRemboursement(domaineDroit.getUniteTauxRemboursement());

      if (StringUtils.isNotEmpty(domaineDroit.getPeriodeDroit().getPeriodeDebut())) {
        beneficiaireCouvertureDto.setPeriodeDebut(
            DateUtils.stringToXMLGregorianCalendar(
                domaineDroit.getPeriodeDroit().getPeriodeDebut(), DateUtils.FORMATTERSLASHED));
      }

      if (StringUtils.isNotEmpty(domaineDroit.getPeriodeDroit().getPeriodeFin())) {
        beneficiaireCouvertureDto.setPeriodeFin(
            DateUtils.stringToXMLGregorianCalendar(
                domaineDroit.getPeriodeDroit().getPeriodeFin(), DateUtils.FORMATTERSLASHED));
      }

      beneficiaireCouvertureDto.setCodeExterneProduit(domaineDroit.getCodeExterneProduit());
      beneficiaireCouvertureDto.setCodeOptionMutualiste(domaineDroit.getCodeOptionMutualiste());
      beneficiaireCouvertureDto.setLibelleOptionMutualiste(
          domaineDroit.getLibelleOptionMutualiste());
      beneficiaireCouvertureDto.setCodeProduit(domaineDroit.getCodeProduit());
      beneficiaireCouvertureDto.setLibelleProduit(domaineDroit.getLibelleProduit());
      beneficiaireCouvertureDto.setCodeGarantie(domaineDroit.getCodeGarantie());
      beneficiaireCouvertureDto.setLibelleGarantie(domaineDroit.getLibelleGarantie());
      beneficiaireCouvertureDto.setPrioriteDroits(domaineDroit.getPrioriteDroit().getCode());
      beneficiaireCouvertureDto.setOrigineDroits(domaineDroit.getOrigineDroits());

      if (StringUtils.isNotEmpty(domaineDroit.getDateAdhesionCouverture())) {
        beneficiaireCouvertureDto.setDateAdhesionCouverture(
            DateUtils.stringToXMLGregorianCalendar(
                domaineDroit.getDateAdhesionCouverture(), DateUtils.FORMATTERSLASHED));
      }
      beneficiaireCouvertureDto.setLibelleCodeRenvoi(domaineDroit.getLibelleCodeRenvoi());
      beneficiaireCouvertureDto.setCategorieDomaine(domaineDroit.getCategorie());
      beneficiaireCouvertureDto.setPrestationDtos(
          mapperPrestation.entityListToDtoList(
              domaineDroit.getPrestations(),
              TypeProfondeurRechercheService.AVEC_FORMULES,
              isFormatV2,
              isFormatV3,
              numAmcRecherche));
      beneficiaireCouvertureDto.setLibelleCodeRenvoiAdditionnel(
          domaineDroit.getLibelleCodeRenvoiAdditionnel());
    }

    return beneficiaireCouvertureDto;
  }
}
