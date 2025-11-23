package com.cegedim.next.serviceeligibility.core.mapper;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.parametragecartetp.GarantieTechniqueDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.parametragecartetp.ParametrageCarteTPDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.parametragecartetp.ParametrageDroitsCarteTPDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.parametragecartetp.ParametrageRenouvellementDto;
import com.cegedim.next.serviceeligibility.core.bobb.GarantieTechnique;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageDroitsCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageRenouvellement;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.time.LocalDate;
import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class MapperParametrageCarteTP {

  public ParametrageCarteTPDto mapParametrageCarteTPDto(ParametrageCarteTP parametrageCarteTP) {
    ParametrageCarteTPDto dto = new ParametrageCarteTPDto();

    dto.setId(parametrageCarteTP.getId());
    dto.setAmc(parametrageCarteTP.getAmc());
    dto.setIdentifiantCollectivite(
        parametrageCarteTP.getIdentifiantCollectivite() != null
            ? Arrays.asList(parametrageCarteTP.getIdentifiantCollectivite().split(","))
            : Collections.emptyList());
    dto.setGroupePopulation(
        parametrageCarteTP.getGroupePopulation() != null
            ? Arrays.asList(parametrageCarteTP.getGroupePopulation().split(","))
            : Collections.emptyList());
    dto.setCritereSecondaireDetaille(
        parametrageCarteTP.getCritereSecondaireDetaille() != null
            ? Arrays.asList(parametrageCarteTP.getCritereSecondaireDetaille().split(","))
            : Collections.emptyList());
    dto.setParametrageRenouvellement(
        mapParametrageRenouvellementDto(
            parametrageCarteTP.getParametrageRenouvellement(),
            parametrageCarteTP.getDateDebutValidite()));
    dto.setParametrageDroitsCarteTP(
        mapParametrageDroitsCarteTPDto(parametrageCarteTP.getParametrageDroitsCarteTP()));
    dto.setIdLots(parametrageCarteTP.getIdLots());
    dto.setSelectedGTs(mapGarantieTechniques(parametrageCarteTP.getGarantieTechniques()));

    return dto;
  }

  private ParametrageRenouvellementDto mapParametrageRenouvellementDto(
      ParametrageRenouvellement parametrageRenouvellement, String dateDebutValidite) {
    ParametrageRenouvellementDto parametrageRenouvellementDto = new ParametrageRenouvellementDto();
    final String dateToday = DateUtils.formatDate(new Date(), DateUtils.YYYY_MM_DD);

    boolean isDateDebutValiditeBeforeToday =
        DateUtils.before(dateDebutValidite, dateToday, DateUtils.FORMATTER);
    parametrageRenouvellementDto.setDebutValidite(
        isDateDebutValiditeBeforeToday ? null : dateDebutValidite);

    if (StringUtils.isNotBlank(parametrageRenouvellement.getDateExecutionBatch())) {
      boolean isDateExecutionBatchBeforeToday =
          DateUtils.before(
              parametrageRenouvellement.getDateExecutionBatch(), dateToday, DateUtils.FORMATTER);
      parametrageRenouvellementDto.setDateExecutionBatch(
          isDateExecutionBatchBeforeToday
              ? null
              : parametrageRenouvellement.getDateExecutionBatch());
    }
    parametrageRenouvellementDto.setDateRenouvellementCarteTP(
        parametrageRenouvellement.getDateRenouvellementCarteTP());
    if (StringUtils.isNotBlank(parametrageRenouvellement.getDebutEcheance())) {
      int year = LocalDate.parse(dateToday, DateUtils.FORMATTER).getYear();
      String debutEcheanceAvecAnnee = parametrageRenouvellement.getDebutEcheance() + "/" + year;
      LocalDate dateDebutEcheance =
          DateUtils.parseLocalDate(debutEcheanceAvecAnnee, DateUtils.DD_MM_YY);
      parametrageRenouvellementDto.setDebutEcheance(String.valueOf(dateDebutEcheance));
    }
    parametrageRenouvellementDto.setDureeValiditeDroitsCarteTP(
        parametrageRenouvellement.getDureeValiditeDroitsCarteTP());
    parametrageRenouvellementDto.setDelaiDeclenchementCarteTP(
        parametrageRenouvellement.getDelaiDeclenchementCarteTP());
    if (StringUtils.isNotBlank(parametrageRenouvellement.getDateDebutDroitTP())) {
      parametrageRenouvellementDto.setDateDebutDroitTP(
          parametrageRenouvellement.getDateDebutDroitTP());
    }
    if (parametrageRenouvellement.getSeuilSecurite() != null) {
      parametrageRenouvellementDto.setSeuilSecurite(parametrageRenouvellement.getSeuilSecurite());
    }
    parametrageRenouvellementDto.setModeDeclenchement(
        parametrageRenouvellement.getModeDeclenchement());
    if (parametrageRenouvellement.getDelaiRenouvellement() != null) {
      parametrageRenouvellementDto.setDelaiRenouvellement(
          parametrageRenouvellement.getDelaiRenouvellement());
    }
    parametrageRenouvellementDto.setAnnulDroitsOffline(
        String.valueOf(Boolean.TRUE.equals(parametrageRenouvellement.getAnnulDroitsOffline())));

    return parametrageRenouvellementDto;
  }

  private ParametrageDroitsCarteTPDto mapParametrageDroitsCarteTPDto(
      ParametrageDroitsCarteTP parametrageDroitsCarteTP) {
    ParametrageDroitsCarteTPDto parametrageDroitsCarteTPDto = new ParametrageDroitsCarteTPDto();

    parametrageDroitsCarteTPDto.setCodeConventionTP(parametrageDroitsCarteTP.getCodeConventionTP());
    parametrageDroitsCarteTPDto.setCodeOperateurTP(parametrageDroitsCarteTP.getCodeOperateurTP());
    parametrageDroitsCarteTPDto.setIsCarteDematerialisee(
        String.valueOf(parametrageDroitsCarteTP.getIsCarteDematerialisee()));
    parametrageDroitsCarteTPDto.setIsCarteEditablePapier(
        String.valueOf(parametrageDroitsCarteTP.getIsCarteEditablePapier()));
    parametrageDroitsCarteTPDto.setRefFondCarte(parametrageDroitsCarteTP.getRefFondCarte());
    parametrageDroitsCarteTPDto.setCodeAnnexe1(parametrageDroitsCarteTP.getCodeAnnexe1());
    parametrageDroitsCarteTPDto.setCodeAnnexe2(parametrageDroitsCarteTP.getCodeAnnexe2());
    parametrageDroitsCarteTPDto.setCodeItelis(parametrageDroitsCarteTP.getCodeItelis());
    parametrageDroitsCarteTPDto.setCodeRenvoi(parametrageDroitsCarteTP.getCodeRenvoi());
    parametrageDroitsCarteTPDto.setDetailsDroit(parametrageDroitsCarteTP.getDetailsDroit());

    return parametrageDroitsCarteTPDto;
  }

  private List<GarantieTechniqueDto> mapGarantieTechniques(
      List<GarantieTechnique> garantieTechniques) {
    List<GarantieTechniqueDto> garantieTechniquesDto = new ArrayList<>();
    for (GarantieTechnique garantieTechnique : garantieTechniques) {
      GarantieTechniqueDto garantieTechniqueDto = new GarantieTechniqueDto();
      garantieTechniqueDto.setValue(garantieTechnique.getCodeGarantie());
      garantieTechniqueDto.setLabel(
          garantieTechnique.getCodeAssureur() + " - " + garantieTechnique.getCodeGarantie());
      garantieTechniqueDto.setNumber(garantieTechnique.getCodeAssureur());
      garantieTechniquesDto.add(garantieTechniqueDto);
    }
    return garantieTechniquesDto;
  }
}
