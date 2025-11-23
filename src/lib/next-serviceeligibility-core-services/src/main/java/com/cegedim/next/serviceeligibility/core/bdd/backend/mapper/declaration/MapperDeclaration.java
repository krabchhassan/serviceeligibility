package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.AttestationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.InfosAssureDto;
import com.cegedim.next.serviceeligibility.core.model.domain.Affiliation;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.BenefCarteDemat;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.DomaineCarte;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.TriggerUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MapperDeclaration {

  @Autowired private MapperIdentificationAssure mapperIdentificationAssure;

  @Autowired private MapperContractDto mapperContractDto;

  @Autowired private MapperDroits mapperDroits;

  @Autowired private MapperAttestation mapperAttestation;

  @Autowired private MapperTrace mapperTrace;

  // TODO changer l'objet CarteDemat en Carte
  public InfosAssureDto entityToDto(
      Declaration declaration, List<CarteDemat> carteDematList, boolean requestFromBenefTpDetails) {
    InfosAssureDto infosAssureDto = new InfosAssureDto();

    infosAssureDto.setId(declaration.get_id());
    infosAssureDto.setEffetDebut(declaration.getEffetDebut());
    if (declaration.getBeneficiaire() != null
        && declaration.getBeneficiaire().getAffiliation() != null) {
      Affiliation affiliation = declaration.getBeneficiaire().getAffiliation();
      infosAssureDto.setNom(affiliation.getNom());
      infosAssureDto.setPrenom(affiliation.getPrenom());
    }

    infosAssureDto.setIdentification(mapperIdentificationAssure.entityToDto(declaration));
    infosAssureDto.setContrat(mapperContractDto.entityToDto(declaration));
    infosAssureDto.setDroits(mapperDroits.entityToDto(declaration));
    if (!requestFromBenefTpDetails) {
      infosAssureDto.setAttestations(mapperAttestation.entityToDto(declaration));
    } else if (CollectionUtils.isNotEmpty(carteDematList)) {
      for (CarteDemat carte : carteDematList) {
        getAttestations(declaration, infosAssureDto, carte);
      }
      orderCertificationsByStartDate(infosAssureDto);
    }

    infosAssureDto.setTrace(mapperTrace.entityToDto(declaration));
    infosAssureDto.setIsTdb(
        Constants.ORIGINE_DECLARATIONTDB.equals(TriggerUtils.getOrigineDeclaration(declaration)));
    return infosAssureDto;
  }

  private void getAttestations(
      Declaration declaration, InfosAssureDto infosAssureDto, CarteDemat carte) {
    List<BenefCarteDemat> benefCarteDematList =
        carte.getBeneficiaires().stream()
            .filter(
                beneficiaire ->
                    declaration
                        .getBeneficiaire()
                        .getNumeroPersonne()
                        .equals(beneficiaire.getBeneficiaire().getNumeroPersonne()))
            .toList();
    for (BenefCarteDemat benefCarteDemat : benefCarteDematList) {
      List<DomaineCarte> domainesRegroup = benefCarteDemat.getDomainesRegroup();
      if (domainesRegroup != null) {
        List<AttestationDto> attestationDtoList = infosAssureDto.getAttestations();
        if (CollectionUtils.isEmpty(attestationDtoList)) {
          attestationDtoList = new ArrayList<>();
        }
        AttestationDto attestationDto =
            mapperAttestation.entityToDtoFromDomRegroup(carte, domainesRegroup);
        attestationDtoList.add(attestationDto);
        infosAssureDto.setAttestations(attestationDtoList);
      } else if (benefCarteDemat.getDomainesCouverture() != null) {
        List<AttestationDto> attestationDtoList = infosAssureDto.getAttestations();
        if (CollectionUtils.isEmpty(attestationDtoList)) {
          attestationDtoList = new ArrayList<>();
        }
        AttestationDto attestationDto =
            mapperAttestation.entityToDtoFromDomCouv(
                carte, benefCarteDemat.getDomainesCouverture());
        attestationDtoList.add(attestationDto);
        infosAssureDto.setAttestations(attestationDtoList);
      }
    }
  }

  private static void orderCertificationsByStartDate(InfosAssureDto infosAssureDto) {
    List<AttestationDto> attestationDtoList = infosAssureDto.getAttestations();
    if (CollectionUtils.isNotEmpty(attestationDtoList)) {
      attestationDtoList.sort(Comparator.comparing(AttestationDto::getPeriodeDebut));
    }
    infosAssureDto.setAttestations(attestationDtoList);
  }
}
