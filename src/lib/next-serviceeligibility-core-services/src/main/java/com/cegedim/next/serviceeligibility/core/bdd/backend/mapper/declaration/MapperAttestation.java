package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration;

import static com.cegedim.next.serviceeligibility.core.utils.Constants.CARTE_DEMATERIALISEE;
import static com.cegedim.next.serviceeligibility.core.utils.Constants.CARTE_TP;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.AttestationDetailDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.AttestationDto;
import com.cegedim.next.serviceeligibility.core.model.domain.Contrat;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.DomaineCarte;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MapperAttestation {

  @Autowired private MapperAttestationDetail mapperAttestationDetail;

  public Declaration dtoToEntity() {
    return null;
  }

  public List<AttestationDto> entityToDto(Declaration declaration) {
    List<AttestationDto> attestationDtoList = new ArrayList<>();
    AttestationDto attestation = new AttestationDto();
    Contrat contrat = declaration.getContrat();
    attestation.setIsCarteTpAEditer(declaration.getIsCarteTPaEditer());
    attestation.setConventionEditeur(contrat.getEditeurCarte());
    attestation.setCommunication(contrat.getDestinataire());
    attestation.setAnnexe1Carte(contrat.getAnnexe1Carte());
    attestation.setAnnexe2Carte(contrat.getAnnexe2Carte());
    attestation.setCodeItelis(contrat.getCodeItelis());
    attestation.setModeleCarte(contrat.getFondCarte());

    List<AttestationDetailDto> details =
        mapperAttestationDetail.entityListToDtoListFromDomaineDroits(
            declaration.getDomaineDroits());

    // On ordonne par numero ordre edition, sur une liste filtré sur les
    // domaines droits editables
    details.sort(
        (detail1, detail2) -> {
          if (detail1.getNumOrdreEdition() != null) {
            return detail1.getNumOrdreEdition().compareTo(detail2.getNumOrdreEdition());
          } else {
            return detail1.getCodeDomaine().compareTo(detail2.getCodeDomaine());
          }
        });
    Stream<AttestationDetailDto> streamDetails =
        details.stream()
            .filter(detail -> detail.getIsDroitEditable() != null && detail.getIsDroitEditable());
    attestation.setDetails(streamDetails.toList());
    attestationDtoList.add(attestation);
    return attestationDtoList;
  }

  public AttestationDto entityToDtoFromDomRegroup(
      CarteDemat carte, List<DomaineCarte> domaineRegroup) {
    AttestationDto attestation = initializeAttestationDto(carte);
    List<AttestationDetailDto> details =
        mapperAttestationDetail.entityListToDtoListFromDomaineCarte(domaineRegroup);
    sortDetails(attestation, details);
    return attestation;
  }

  public AttestationDto entityToDtoFromDomCouv(CarteDemat carte, List<DomaineDroit> domaineCouv) {
    AttestationDto attestation = initializeAttestationDto(carte);
    List<AttestationDetailDto> details =
        mapperAttestationDetail.entityListToDtoListFromDomaineDroits(domaineCouv);
    sortDetails(attestation, details);
    return attestation;
  }

  private static AttestationDto initializeAttestationDto(CarteDemat carte) {
    AttestationDto attestation = new AttestationDto();
    Contrat contrat = carte.getContrat();
    List<String> codeServices = carte.getCodeServices();
    if (CollectionUtils.isNotEmpty(codeServices)) {
      if (codeServices.contains(CARTE_TP)) {
        attestation.setIsCarteTpAEditer(true);
        attestation.setIsCartePapier(true);
      }

      if (codeServices.contains(CARTE_DEMATERIALISEE)) {
        attestation.setIsCarteDemat(true);
      }
    } else {
      attestation.setIsCarteDemat(true);
    }
    attestation.setAnnexe1Carte(contrat.getAnnexe1Carte());
    attestation.setAnnexe2Carte(contrat.getAnnexe2Carte());
    attestation.setCodeItelis(contrat.getCodeItelis());
    attestation.setModeleCarte(contrat.getFondCarte());
    attestation.setCodeRenvoi(contrat.getCodeRenvoi());
    attestation.setLibelleRenvoi(contrat.getLibelleCodeRenvoi());
    attestation.setPeriodeDebut(carte.getPeriodeDebut());
    attestation.setPeriodeFin(carte.getPeriodeFin());
    return attestation;
  }

  private static void sortDetails(AttestationDto attestation, List<AttestationDetailDto> details) {
    // On ordonne par numero ordre edition, sur une liste filtré sur les
    // domaines droits editables
    details.sort(
        (detail1, detail2) -> {
          if (detail1.getNumOrdreEdition() != null) {
            return detail1.getNumOrdreEdition().compareTo(detail2.getNumOrdreEdition());
          } else {
            return detail1.getCodeDomaine().compareTo(detail2.getCodeDomaine());
          }
        });
    attestation.setDetails(details.stream().toList());
  }
}
