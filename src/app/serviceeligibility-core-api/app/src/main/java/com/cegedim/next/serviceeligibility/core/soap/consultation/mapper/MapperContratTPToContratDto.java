package com.cegedim.next.serviceeligibility.core.soap.consultation.mapper;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.CLIENT_TYPE;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ContratDto;
import com.cegedim.next.serviceeligibility.core.model.domain.benef.CodePeriodeDeclaration;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.BeneficiaireContractTP;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PeriodeCMUOuvert;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.PeriodeComparable;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionServiceFormatDate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapperContratTPToContratDto {

  private final BeyondPropertiesService beyondPropertiesService;

  private final ContractToDeclarationMapper contractToDeclarationMapper;

  private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");

  public ContratDto mapContrat(
      ContractTP contractTP, BeneficiaireContractTP benef, String dateReference) {
    // Extract dates before mapstruct
    String dateSouscription = contractTP.getDateSouscription();
    String dateResiliation = contractTP.getDateResiliation();
    String situationDebut = contractTP.getSituationDebut();
    String situationFin = contractTP.getSituationFin();

    contractTP.setDateSouscription(null);
    contractTP.setDateResiliation(null);
    contractTP.setSituationDebut(null);
    contractTP.setSituationFin(null);

    ContratDto dto = contractToDeclarationMapper.contractTPToContratDto(contractTP);
    dto.setNumero(contractTP.getNumeroContrat());
    dto.setNumeroOperateur(contractTP.getNumOperateur());
    dto.setNumeroAMCEchanges((contractTP.getNumAMCEchange()));
    if (StringUtils.isAllBlank(contractTP.getNumeroContratCollectif())) {
      dto.setNumeroContratCollectif("");
    }

    dto.setNumeroAdherent(contractTP.getNumeroAdherent());
    dto.setNumeroAdherentComplet(contractTP.getNumeroAdherentComplet());

    if (benef != null) {
      dto.setRangAdministratif(benef.getRangAdministratif());
      dto.setCategorieSociale(benef.getCategorieSociale());
      dto.setSituationParticuliere(benef.getSituationParticuliere());
      dto.setModePaiementPrestations(benef.getModePaiementPrestations());
      if (benef.getAffiliation() != null) {
        dto.setLienFamilial(benef.getAffiliation().getQualite());
      }
    }

    try {
      if (StringUtils.isNotEmpty(dateSouscription)) {
        dto.setDateSouscription(simpleDateFormat.parse(dateSouscription));
      }
      if (StringUtils.isNotEmpty(dateResiliation)) {
        dto.setDateResiliation(simpleDateFormat.parse(dateResiliation));
      }
      if (StringUtils.isNotEmpty(situationDebut)) {
        dto.setSituationDebut(simpleDateFormat.parse(situationDebut));
      }
      if (StringUtils.isNotEmpty(situationFin)) {
        dto.setSituationfin(simpleDateFormat.parse(situationFin));
      }

      if (Constants.CLIENT_TYPE_INSURER.equals(
          beyondPropertiesService.getPropertyOrThrowError(CLIENT_TYPE))) {
        mapCMU(dto, contractTP.getPeriodeCMUOuverts(), dateReference);
        mapContratResponsable(dto, contractTP.getPeriodeResponsableOuverts(), dateReference);
        if (benef != null) {
          mapSituationParticuliere(dto, benef.getSituationsParticulieres(), dateReference);
        }
      }
    } catch (ParseException e) {
      throw new ExceptionServiceFormatDate();
    }

    // remise des dates pour pas n'avoir une date vide : BLUE-7010
    contractTP.setDateSouscription(dateSouscription);
    contractTP.setDateResiliation(dateResiliation);
    contractTP.setSituationDebut(situationDebut);
    contractTP.setSituationFin(situationFin);

    return dto;
  }

  private void mapSituationParticuliere(
      ContratDto dto, List<CodePeriodeDeclaration> situationsParticulieres, String dateReference)
      throws ParseException {
    for (CodePeriodeDeclaration situation : ListUtils.emptyIfNull(situationsParticulieres)) {
      PeriodeComparable periode = situation.getPeriode();
      if (periode != null
          && DateUtils.betweenString(dateReference, periode.getDebut(), periode.getFin())) {
        if (StringUtils.isNotBlank(periode.getDebut())) {
          dto.setSituationDebut(simpleDateFormat.parse(periode.getDebut()));
        }

        if (StringUtils.isNotBlank(periode.getFin())) {
          dto.setSituationfin(simpleDateFormat.parse(periode.getFin()));
        }

        dto.setSituationParticuliere(situation.getCode());
      }
    }
  }

  private void mapContratResponsable(
      ContratDto dto, List<PeriodeComparable> periodeResponsableOuverts, String dateReference) {
    for (PeriodeComparable periodeResponsable : ListUtils.emptyIfNull(periodeResponsableOuverts)) {
      if (DateUtils.betweenString(
          dateReference, periodeResponsable.getDebut(), periodeResponsable.getFin())) {
        dto.setIsContratResponsable(true);
        break;
      }
    }
  }

  private void mapCMU(
      ContratDto dto, List<PeriodeCMUOuvert> periodeCMUOuverts, String dateReference) {
    for (PeriodeCMUOuvert periodeCMUOuvert : ListUtils.emptyIfNull(periodeCMUOuverts)) {
      if (periodeCMUOuvert.getPeriode() != null
          && DateUtils.betweenString(
              dateReference,
              periodeCMUOuvert.getPeriode().getDebut(),
              periodeCMUOuvert.getPeriode().getFin())) {
        dto.setIsContratCMU(true);
        break;
      }
    }
  }
}
