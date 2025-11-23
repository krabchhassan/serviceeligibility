package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails.CodePeriodeContractDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails.PeriodeContractDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.CommunicationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.ContratDto;
import com.cegedim.next.serviceeligibility.core.model.domain.Affiliation;
import com.cegedim.next.serviceeligibility.core.model.domain.BeneficiaireV2;
import com.cegedim.next.serviceeligibility.core.model.domain.Contrat;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PeriodeCMUOuvert;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.PeriodeComparable;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionServiceFormatDate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MapperContractDto {

  @Autowired private MapperAdresse mapperAdresse;

  private static final Logger LOGGER = LoggerFactory.getLogger(MapperContractDto.class);

  public Declaration dtoToEntity() {
    return null;
  }

  public ContratDto entityToDto(Declaration declaration) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat sdfDefault = new SimpleDateFormat("dd/MM/yyyy");

    ContratDto contratDto = new ContratDto();
    if (declaration.getContrat() != null) {
      Contrat contrat = declaration.getContrat();
      contratDto.setNumero(contrat.getNumero());
      contratDto.setNumeroAdherent(contrat.getNumeroAdherent());
      contratDto.setNumeroContratCollectif(contrat.getNumeroContratCollectif());
      contratDto.setTypeAssure(contrat.getType());

      contratDto.setNumeroExterneContratIndividuel(contrat.getNumeroExterneContratIndividuel());
      contratDto.setNumeroExterneContratCollectif(contrat.getNumeroExterneContratCollectif());
      contratDto.setIsContratCMU(contrat.getIsContratCMU());
      contratDto.setContratCMUC2S(contrat.getContratCMUC2S());

      contratDto.setCritereSecondaire(contrat.getCritereSecondaire());
      contratDto.setCritereSecondaireDetaille(contrat.getCritereSecondaireDetaille());
      contratDto.setRangAdministratif(contrat.getRangAdministratif());
      contratDto.setIsContratResponsable(contrat.getIsContratResponsable());

      contratDto.setGestionnaire(contrat.getGestionnaire());
      contratDto.setTypeConvention(contrat.getTypeConvention());
      contratDto.setQualification(contrat.getQualification());

      contratDto.setCivilitePorteur(contrat.getCivilitePorteur());
      contratDto.setNomPorteur(contrat.getNomPorteur());
      contratDto.setPrenomPorteur(contrat.getPrenomPorteur());
      contratDto.setGroupeAssures(contrat.getGroupeAssures());
      contratDto.setNumOperateur(contrat.getNumOperateur());
      contratDto.setNumAMCEchange(contrat.getNumAMCEchange());
      // Date Souscription
      setDateSouscription(sdf, sdfDefault, contratDto, contrat);

      contratDto.setModePaiementPrestations(contrat.getModePaiementPrestations());
      List<CommunicationDto> communications = new ArrayList<>();

      setBeneficiaire(declaration, contratDto, communications);
      contratDto.setCommunications(communications);

      contratDto.setCodeRenvoi(contrat.getCodeRenvoi());
      contratDto.setLibelleCodeRenvoi(contrat.getLibelleCodeRenvoi());

      contratDto.setPeriodeCMUOuverts(mapContratCMUOuvert(contrat.getPeriodeCMUOuverts()));
      contratDto.setPeriodeResponsableOuverts(
          mapContratResponsableOuvert(contrat.getPeriodeResponsableOuverts()));
    }

    return contratDto;
  }

  private void setBeneficiaire(
      Declaration declaration, ContratDto contratDto, List<CommunicationDto> communications) {
    if (declaration.getBeneficiaire() != null) {
      BeneficiaireV2 beneficiaire = declaration.getBeneficiaire();
      if (beneficiaire.getAdresses() != null && !beneficiaire.getAdresses().isEmpty()) {
        communications.addAll(mapperAdresse.entityListToDtoList(beneficiaire.getAdresses()));
      }
      if (beneficiaire.getAffiliation() != null) {
        Affiliation affiliation = beneficiaire.getAffiliation();
        contratDto.setTypeAssure(affiliation.getTypeAssure());
        contratDto.setIsBeneficiaireACS(affiliation.getIsBeneficiaireACS());
        contratDto.setIsTeleTransmission(affiliation.getIsTeleTransmission());
      }
    }
  }

  private void setDateSouscription(
      SimpleDateFormat sdf, SimpleDateFormat sdfDefault, ContratDto contratDto, Contrat contrat) {
    try {
      Date dateSouscription =
          contrat.getDateSouscription() == null ? null : sdf.parse(contrat.getDateSouscription());
      contratDto.setDateSouscription(
          dateSouscription == null ? null : sdfDefault.format(dateSouscription));
    } catch (ParseException e) {
      LOGGER.error("MapperContrat#Erreur lors de la conversion de date", e);
      throw new ExceptionServiceFormatDate();
    }
  }

  public static List<CodePeriodeContractDto> mapContratCMUOuvert(
      List<PeriodeCMUOuvert> periodesContratCMUOuvert) {

    List<CodePeriodeContractDto> periodeContratCMUOuvertDtos = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(periodesContratCMUOuvert)) {
      for (PeriodeCMUOuvert periodeContratCMUOuvert : periodesContratCMUOuvert) {
        CodePeriodeContractDto periodeContratCMUOuvertDto = new CodePeriodeContractDto();
        periodeContratCMUOuvertDto.setCode(periodeContratCMUOuvert.getCode());
        periodeContratCMUOuvertDto.setPeriode(
            mapPeriodeContract(periodeContratCMUOuvert.getPeriode()));
        periodeContratCMUOuvertDtos.add(periodeContratCMUOuvertDto);
      }
    }
    return periodeContratCMUOuvertDtos;
  }

  public static List<PeriodeContractDto> mapContratResponsableOuvert(
      List<PeriodeComparable> periodesContratResponsableOuvert) {
    List<PeriodeContractDto> periodeContractDtos = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(periodesContratResponsableOuvert)) {
      for (PeriodeComparable periodeContratResponsableOuvert : periodesContratResponsableOuvert) {
        PeriodeContractDto periodeContractDto = mapPeriodeContract(periodeContratResponsableOuvert);
        periodeContractDtos.add(periodeContractDto);
      }
    }
    return periodeContractDtos;
  }

  @NotNull
  private static PeriodeContractDto mapPeriodeContract(PeriodeComparable periode) {
    PeriodeContractDto periodeContractDto = new PeriodeContractDto();
    periodeContractDto.setDebut(periode.getDebut());
    periodeContractDto.setFin(periode.getFin());
    return periodeContractDto;
  }
}
