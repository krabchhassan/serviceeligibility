package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails.*;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.*;
import com.cegedim.next.serviceeligibility.core.mapper.MapperBenefDetails;
import com.cegedim.next.serviceeligibility.core.model.domain.Affiliation;
import com.cegedim.next.serviceeligibility.core.model.domain.BeneficiaireV2;
import com.cegedim.next.serviceeligibility.core.model.domain.Contrat;
import com.cegedim.next.serviceeligibility.core.model.domain.benef.*;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.PeriodeComparable;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionServiceFormatDate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MapperIdentificationAssure {

  private static final Logger LOGGER = LoggerFactory.getLogger(MapperIdentificationAssure.class);

  public IdentificationAssureDto entityToDto(Declaration declaration) {
    SimpleDateFormat sdfPeriodeAffiliation = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat sdfDefault = new SimpleDateFormat("dd/MM/yyyy");

    IdentificationAssureDto identification = new IdentificationAssureDto();
    AssureDto assure = new AssureDto();
    AffiliationInfoDto affiliationDto = new AffiliationInfoDto();

    if (declaration.getBeneficiaire() != null) {
      try {
        BeneficiaireV2 beneficiaire = declaration.getBeneficiaire();

        // la date de naissance peut être lunaire, on se contente de la
        // formatter en jj/mm/aaaa
        // sachant qu'elle est stockée en aaaammjj (parse calcule une
        // date si format invalide)
        identification.setDateNaissance(beneficiaire.getDateNaissance());
        if (StringUtils.isNotEmpty(beneficiaire.getDateNaissance())) {
          String dateNaissance =
              beneficiaire.getDateNaissance().substring(6, 8)
                  + "/"
                  + beneficiaire.getDateNaissance().substring(4, 6)
                  + "/"
                  + beneficiaire.getDateNaissance().substring(0, 4);
          identification.setDateNaissance(dateNaissance);
        }

        identification.setRangNaissance(beneficiaire.getRangNaissance());

        assure.setRefExternePersonne(beneficiaire.getRefExternePersonne());
        assure.setNumeroPersonne(beneficiaire.getNumeroPersonne());

        affiliationDto.setNirBeneficiaire(beneficiaire.getNirBeneficiaire());
        affiliationDto.setCleNirBeneficiaire(beneficiaire.getCleNirBeneficiaire());
        affiliationDto.setNirOd1(beneficiaire.getNirOd1());
        affiliationDto.setCleNirOd1(beneficiaire.getCleNirOd1());
        affiliationDto.setNirOd2(beneficiaire.getNirOd2());
        affiliationDto.setCleNirOd2(beneficiaire.getCleNirOd2());

        if (declaration.getContrat() != null) {
          Contrat contrat = declaration.getContrat();
          assure.setNumeroAdherent(contrat.getNumeroAdherent());
          assure.setNumeroAdherentComplet(contrat.getNumeroAdherentComplet());
        }

        getBeneficiaireAffiliation(
            sdfPeriodeAffiliation, sdfDefault, assure, affiliationDto, beneficiaire);

        identification.setAssure(assure);
        identification.setAffiliation(affiliationDto);

        identification.setRegimesParticuliers(
            getCodePeriodeList(beneficiaire.getRegimesParticuliers()));
        identification.setPeriodesMedecinTraitant(
            mapPeriodesMedecinTraitant(beneficiaire.getPeriodesMedecinTraitant()));
        identification.setSituationsParticulieres(
            getCodePeriodeList(beneficiaire.getSituationsParticulieres()));
        identification.setAffiliationsRO(mapAffiliationsRO(beneficiaire.getAffiliationsRO()));
        identification.setTeletransmissions(
            mapTeletransmissions(beneficiaire.getTeletransmissions()));

      } catch (ParseException e) {
        LOGGER.error("MapperIdentificationAssureImpl#Erreur lors de la conversion de date", e);
        throw new ExceptionServiceFormatDate();
      }
    }
    return identification;
  }

  private void getBeneficiaireAffiliation(
      SimpleDateFormat sdfPeriodeAffiliation,
      SimpleDateFormat sdfDefault,
      AssureDto assure,
      AffiliationInfoDto affiliationDto,
      BeneficiaireV2 beneficiaire)
      throws ParseException {
    if (beneficiaire.getAffiliation() != null) {
      Affiliation affiliation = beneficiaire.getAffiliation();
      assure.setNomMarital(affiliation.getNomMarital());
      assure.setNomPatronymique(affiliation.getNomPatronymique());

      Date priodeDebutAffiliation =
          affiliation.getPeriodeDebut() == null
              ? null
              : sdfPeriodeAffiliation.parse(affiliation.getPeriodeDebut());
      affiliationDto.setPeriodeDebut(
          priodeDebutAffiliation == null ? null : sdfDefault.format(priodeDebutAffiliation));

      Date priodeFinAffiliation =
          affiliation.getPeriodeFin() == null
              ? null
              : sdfPeriodeAffiliation.parse(affiliation.getPeriodeFin());
      affiliationDto.setPeriodeFin(
          priodeFinAffiliation == null ? null : sdfDefault.format(priodeFinAffiliation));

      affiliationDto.setRegimeOD1(affiliation.getRegimeOD1());
      affiliationDto.setCaisseOD1(affiliation.getCaisseOD1());
      affiliationDto.setCentreOD1(affiliation.getCentreOD1());

      affiliationDto.setRegimeOD2(affiliation.getRegimeOD2());
      affiliationDto.setCaisseOD2(affiliation.getCaisseOD2());
      affiliationDto.setCentreOD2(affiliation.getCentreOD2());

      affiliationDto.setQualite(affiliation.getQualite());
      affiliationDto.setRegimeParticulier(affiliation.getRegimeParticulier());
    }
  }

  public static List<PeriodeContractDto> mapPeriodesMedecinTraitant(
      List<PeriodeComparable> periodesMedecinTraitant) {
    List<PeriodeContractDto> periodeContractDtos = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(periodesMedecinTraitant)) {
      for (PeriodeComparable periodeMedecinTraitant : periodesMedecinTraitant) {
        PeriodeContractDto periodeContractDto = mapPeriodeContract(periodeMedecinTraitant);
        periodeContractDtos.add(periodeContractDto);
      }
    }
    return periodeContractDtos;
  }

  public static List<NirRattachementRODto> mapAffiliationsRO(
      List<NirRattachementRODeclaration> affiliationsRO) {
    List<NirRattachementRODto> nirRattachementRODtos = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(affiliationsRO)) {
      MapperBenefDetails.mapNirRattachementRO(affiliationsRO, nirRattachementRODtos);
    }
    return nirRattachementRODtos;
  }

  public static List<TeletransmissionDto> mapTeletransmissions(
      List<TeletransmissionDeclaration> teletransmissions) {
    List<TeletransmissionDto> teletransmissionDtos = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(teletransmissions)) {
      for (TeletransmissionDeclaration teletransmission : teletransmissions) {
        TeletransmissionDto teletransmissionDto = new TeletransmissionDto();
        teletransmissionDto.setIsTeletransmission(teletransmission.getIsTeletransmission());
        teletransmissionDto.setPeriode(mapPeriodeContract(teletransmission.getPeriode()));
        teletransmissionDtos.add(teletransmissionDto);
      }
    }
    return teletransmissionDtos;
  }

  private static List<CodePeriodeContractDto> getCodePeriodeList(
      List<CodePeriodeDeclaration> casParticuliers) {
    List<CodePeriodeContractDto> periodeContractDtos = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(casParticuliers)) {
      for (CodePeriodeDeclaration codePeriode : casParticuliers) {
        CodePeriodeContractDto codePeriodeContractDto = new CodePeriodeContractDto();
        codePeriodeContractDto.setCode(codePeriode.getCode());
        PeriodeContractDto periodeContractDto = getPeriodeContractDto(codePeriode);
        codePeriodeContractDto.setPeriode(periodeContractDto);
        periodeContractDtos.add(codePeriodeContractDto);
      }
    }
    return periodeContractDtos;
  }

  @NotNull
  private static PeriodeContractDto getPeriodeContractDto(CodePeriodeDeclaration codePeriode) {
    PeriodeContractDto periodeContractDto = new PeriodeContractDto();
    periodeContractDto.setDebut(codePeriode.getPeriode().getDebut());
    periodeContractDto.setFin(codePeriode.getPeriode().getFin());
    return periodeContractDto;
  }

  @NotNull
  private static PeriodeContractDto mapPeriodeContract(PeriodeComparable periode) {
    PeriodeContractDto periodeContractDto = new PeriodeContractDto();
    periodeContractDto.setDebut(periode.getDebut());
    periodeContractDto.setFin(periode.getFin());
    return periodeContractDto;
  }

  @NotNull
  private static NirDto mapNir(NirDeclaration nir) {
    NirDto nirDto = new NirDto();
    nirDto.setCode(nir.getCode());
    nirDto.setCle(nir.getCle());
    return nirDto;
  }

  @NotNull
  private static RattachementRODto mapRattachementRo(RattachementRODeclaration rattachementRO) {
    RattachementRODto rattachementRODto = new RattachementRODto();
    rattachementRODto.setCodeCaisse(rattachementRO.getCodeCaisse());
    rattachementRODto.setCodeCentre(rattachementRO.getCodeCentre());
    rattachementRODto.setCodeRegime(rattachementRO.getCodeRegime());
    return rattachementRODto;
  }
}
