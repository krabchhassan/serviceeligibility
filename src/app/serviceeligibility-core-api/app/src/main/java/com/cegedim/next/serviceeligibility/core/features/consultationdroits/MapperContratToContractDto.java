package com.cegedim.next.serviceeligibility.core.features.consultationdroits;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.CLIENT_TYPE;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.AffiliationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.BeneficiaireDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ContractDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.contract.*;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperAdresse;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperAffiliation;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperDeclarant;
import com.cegedim.next.serviceeligibility.core.business.declarant.service.DeclarantService;
import com.cegedim.next.serviceeligibility.core.model.domain.Adresse;
import com.cegedim.next.serviceeligibility.core.model.domain.Affiliation;
import com.cegedim.next.serviceeligibility.core.model.domain.benef.CodePeriodeDeclaration;
import com.cegedim.next.serviceeligibility.core.model.domain.benef.TeletransmissionDeclaration;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.*;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.PeriodeComparable;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.services.ReferentielParametrageCarteTPService;
import com.cegedim.next.serviceeligibility.core.soap.consultation.mapper.ContractToDeclarationMapper;
import com.cegedim.next.serviceeligibility.core.soap.consultation.mapper.MapperContratTPToContratDto;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.Util;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class MapperContratToContractDto {

  private final BeyondPropertiesService beyondPropertiesService;

  private final DeclarantService declarantService;

  private final MapperDeclarant mapperDeclarant;

  private final MapperAdresse mapperAdresse;

  private final MapperAffiliation mapperAffiliation;

  private final ContractToDeclarationMapper contractToDeclarationMapper;
  private final ReferentielParametrageCarteTPService referentielParametrageCarteTPService;

  private final MapperContratTPToContratDto mapperContratTPToContratDto;

  private String numAmcRecherche;

  public List<ContractDto> entityListToDtoList(
      List<ContractTP> contractList, String dateReference) {
    List<ContractDto> contractDtoList = new ArrayList<>();
    for (ContractTP contract : contractList) {
      Declarant declarant = declarantService.findById(contract.getIdDeclarant());
      for (BeneficiaireContractTP beneficiaire : contract.getBeneficiaires()) {
        contractDtoList.add(mapContract(contract, beneficiaire, declarant, dateReference));
      }
    }
    return contractDtoList;
  }

  ContractDto mapContract(
      ContractTP contract,
      BeneficiaireContractTP beneficiaire,
      Declarant declarant,
      String dateReference) {
    ContractDto contractDto = new ContractDto();
    if (declarant != null) {
      contractDto.setDeclarantAmc(
          mapperDeclarant.entityToDto(declarant, null, false, false, numAmcRecherche));
    }

    contractDto.setContrat(
        mapperContratTPToContratDto.mapContrat(contract, beneficiaire, dateReference));

    List<DomaineDroitContratDto> domaineDroitContratDtos = new ArrayList<>();
    for (DomaineDroitContractTP domaineDroitContract : beneficiaire.getDomaineDroits()) {
      domaineDroitContratDtos.add(
          mapDomaine(
              domaineDroitContract,
              contract.getDateRestitution(),
              Util.isExcluDemat(contract.getCarteTPaEditerOuDigitale())));
    }
    contractDto.setDomaineDroits(domaineDroitContratDtos);

    BeneficiaireDto beneficiaireDto = mapBeneficiaire(beneficiaire, dateReference);
    contractDto.setBeneficiaire(beneficiaireDto);

    return contractDto;
  }

  private BeneficiaireDto mapBeneficiaire(
      BeneficiaireContractTP beneficiaireContractTP, String dateReference) {
    Affiliation affiliation = beneficiaireContractTP.getAffiliation();
    beneficiaireContractTP.setAffiliation(null);
    List<Adresse> adresses = beneficiaireContractTP.getAdresses();
    beneficiaireContractTP.setAdresses(null);
    BeneficiaireDto dto =
        contractToDeclarationMapper.beneficiaireContractTPToBeneficiaireDto(beneficiaireContractTP);
    dto.setAdresses(
        mapperAdresse.entityListToDtoList(adresses, null, false, false, numAmcRecherche));
    AffiliationDto affiliationDto =
        mapperAffiliation.entityToDto(affiliation, null, false, false, numAmcRecherche);
    dto.setAffiliation(affiliationDto);
    beneficiaireContractTP.setAffiliation(affiliation);
    beneficiaireContractTP.setAdresses(adresses);

    if (Constants.CLIENT_TYPE_INSURER.equals(
            beyondPropertiesService.getPropertyOrThrowError(CLIENT_TYPE))
        && affiliationDto != null) {
      mapTeletransmissionInsurer(
          affiliationDto, beneficiaireContractTP.getTeletransmissions(), dateReference);
      mapRegimeParticulier(
          affiliationDto, beneficiaireContractTP.getRegimesParticuliers(), dateReference);
      mapMedecinTraitant(
          affiliationDto, beneficiaireContractTP.getPeriodesMedecinTraitant(), dateReference);
    }

    return dto;
  }

  private void mapMedecinTraitant(
      AffiliationDto affiliationDto,
      List<PeriodeComparable> periodesMedecinTraitant,
      String dateRef) {
    for (PeriodeComparable periodeMedecin : ListUtils.emptyIfNull(periodesMedecinTraitant)) {
      if (DateUtils.betweenString(dateRef, periodeMedecin.getDebut(), periodeMedecin.getFin())) {
        affiliationDto.setMedecinTraitant(true);
        break;
      }
    }
  }

  private void mapRegimeParticulier(
      AffiliationDto affiliationDto,
      List<CodePeriodeDeclaration> regimesParticuliers,
      String dateRef) {
    for (CodePeriodeDeclaration regime : ListUtils.emptyIfNull(regimesParticuliers)) {
      if (regime.getPeriode() != null
          && DateUtils.betweenString(
              dateRef, regime.getPeriode().getDebut(), regime.getPeriode().getFin())) {
        affiliationDto.setMedecinTraitant(true);
        break;
      }
    }
  }

  private void mapTeletransmissionInsurer(
      AffiliationDto affiliationDto,
      List<TeletransmissionDeclaration> teletransmissionDeclarations,
      String dateRef) {
    for (TeletransmissionDeclaration teletransmission :
        ListUtils.emptyIfNull(teletransmissionDeclarations)) {
      if (teletransmission.getPeriode() != null
          && DateUtils.betweenString(
              dateRef,
              teletransmission.getPeriode().getDebut(),
              teletransmission.getPeriode().getFin())) {
        affiliationDto.setHasTeleTransmission(true);
        break;
      }
    }
  }

  DomaineDroitContratDto mapDomaine(
      DomaineDroitContractTP domaine,
      String dateRestitution,
      Boolean isExclusiviteCarteDematerialise) {
    DomaineDroitContratDto dto = new DomaineDroitContratDto();
    dto.setCode(domaine.getCode());
    dto.setCodeExterne(domaine.getCodeExterne());
    List<GarantieDto> garantieDtos = new ArrayList<>();
    for (Garantie garantie : ListUtils.emptyIfNull(domaine.getGaranties())) {
      garantieDtos.add(mapGarantie(garantie, dateRestitution, isExclusiviteCarteDematerialise));
    }
    dto.setGaranties(garantieDtos);
    return dto;
  }

  GarantieDto mapGarantie(
      Garantie garantie, String dateRestitution, Boolean isExclusiviteCarteDematerialise) {
    GarantieDto garantieDto = new GarantieDto();
    garantieDto.setCodeGarantie(garantie.getCodeGarantie());
    List<ProduitDto> produitDtos = new ArrayList<>();
    for (Produit produit : ListUtils.emptyIfNull(garantie.getProduits())) {
      produitDtos.add(mapProduit(produit, dateRestitution, isExclusiviteCarteDematerialise));
    }
    garantieDto.setProduits(produitDtos);
    return garantieDto;
  }

  ProduitDto mapProduit(
      Produit produit, String dateRestitution, Boolean isExclusiviteCarteDematerialise) {
    ProduitDto produitDto = new ProduitDto();
    produitDto.setCodeProduit(produit.getCodeProduit());
    List<ReferenceCouvertureDto> referenceCouvertureDtos = new ArrayList<>();
    for (ReferenceCouverture referenceCouverture :
        ListUtils.emptyIfNull(produit.getReferencesCouverture())) {
      referenceCouvertureDtos.add(
          mapReferenceCouverture(
              referenceCouverture, dateRestitution, isExclusiviteCarteDematerialise));
    }
    produitDto.setReferencesCouverture(referenceCouvertureDtos);
    return produitDto;
  }

  ReferenceCouvertureDto mapReferenceCouverture(
      ReferenceCouverture referenceCouverture,
      String dateRestitution,
      Boolean isExclusiviteCarteDematerialise) {
    ReferenceCouvertureDto referenceCouvertureDto = new ReferenceCouvertureDto();
    referenceCouvertureDto.setReferenceCouverture(referenceCouverture.getReferenceCouverture());
    List<NaturePrestationDto> naturePrestationDtos = new ArrayList<>();
    for (NaturePrestation naturePrestation :
        ListUtils.emptyIfNull(referenceCouverture.getNaturesPrestation())) {
      naturePrestationDtos.add(
          mapNaturePrestation(naturePrestation, dateRestitution, isExclusiviteCarteDematerialise));
    }
    referenceCouvertureDto.setNaturesPrestation(naturePrestationDtos);
    return referenceCouvertureDto;
  }

  NaturePrestationDto mapNaturePrestation(
      NaturePrestation naturePrestation,
      String dateRestitution,
      Boolean isExclusiviteCarteDematerialise) {
    NaturePrestationDto naturePrestationDto = new NaturePrestationDto();
    naturePrestationDto.setNaturePrestation(naturePrestation.getNaturePrestation());

    naturePrestationDto.setPeriodesDroit(
        mapPeriodeDroitContrat(
            naturePrestation.getPeriodesDroit(), dateRestitution, isExclusiviteCarteDematerialise));

    naturePrestationDto.setPrioritesDroit(naturePrestation.getPrioritesDroit());
    naturePrestationDto.setPrestations(naturePrestation.getPrestations());
    naturePrestationDto.setConventionnements(naturePrestation.getConventionnements());
    naturePrestationDto.setRemboursements(naturePrestation.getRemboursements());

    return naturePrestationDto;
  }

  List<PeriodeDroitContratDto> mapPeriodeDroitContrat(
      List<PeriodeDroitContractTP> periodesDroitContractTP,
      String dateRestitution,
      Boolean isExclusiviteCarteDematerialise) {
    List<PeriodeDroitContratDto> periodeDroitContratDtos = new ArrayList<>();
    for (PeriodeDroitContractTP periodeDroitContractTP : periodesDroitContractTP) {
      PeriodeDroitContratDto periodeDroitContratDto = new PeriodeDroitContratDto();
      periodeDroitContratDto.setPeriodeDebut(periodeDroitContractTP.getPeriodeDebut());
      periodeDroitContratDto.setPeriodeFin(periodeDroitContractTP.getPeriodeFin());
      periodeDroitContratDto.setPeriodeFinFermeture(
          periodeDroitContractTP.getPeriodeFinFermeture());
      periodeDroitContratDto.setMotifEvenement(periodeDroitContractTP.getMotifEvenement());
      periodeDroitContratDto.setModeObtention(periodeDroitContractTP.getModeObtention());
      periodeDroitContratDto.setTypePeriode(periodeDroitContractTP.getTypePeriode());
      periodeDroitContratDto.setLibelleEvenement(periodeDroitContractTP.getLibelleEvenement());
      String dateFinOffline =
          Util.getDateFinOffline(
              periodeDroitContractTP.getPeriodeFin(),
              periodeDroitContractTP.getPeriodeFinFermeture(),
              dateRestitution,
              isExclusiviteCarteDematerialise);
      if (StringUtils.isNotBlank(dateFinOffline)) {
        periodeDroitContratDto.setPeriodeFinOffline(dateFinOffline);
      }

      periodeDroitContratDtos.add(periodeDroitContratDto);
    }
    return periodeDroitContratDtos;
  }
}
