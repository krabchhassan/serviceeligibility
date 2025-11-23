package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.PeriodeDroitDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapperImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperPeriodeDroit;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.PeriodeDroit;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.text.SimpleDateFormat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/** Classe de mapping de l'entite {@code PeriodeDroit}. */
@Component
public class MapperPeriodeDroitImpl extends GenericMapperImpl<PeriodeDroit, PeriodeDroitDto>
    implements MapperPeriodeDroit {

  @Override
  public PeriodeDroit dtoToEntity(PeriodeDroitDto periodeDroitdto) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    PeriodeDroit periodeDroit = null;
    if (periodeDroitdto != null) {
      periodeDroit = new PeriodeDroit();
      periodeDroit.setDateEvenement(
          periodeDroitdto.getDateEvenemnt() == null
              ? null
              : sdf.format(periodeDroitdto.getDateEvenemnt()));
      periodeDroit.setLibelleEvenement(periodeDroitdto.getLibelleEvenement());
      periodeDroit.setModeObtention(periodeDroitdto.getModeObtention());
      periodeDroit.setMotifEvenement(periodeDroitdto.getMotifEvenement());
      periodeDroit.setPeriodeDebut(
          periodeDroitdto.getPeriodeDebut() == null
              ? null
              : sdf.format(periodeDroitdto.getPeriodeDebut()));
      periodeDroit.setPeriodeFin(
          periodeDroitdto.getPeriodeFin() == null
              ? null
              : sdf.format(periodeDroitdto.getPeriodeFin()));
      periodeDroit.setPeriodeFinInitiale(
          periodeDroitdto.getPeriodeFinInitiale() == null
              ? null
              : sdf.format(periodeDroitdto.getPeriodeFinInitiale()));
      periodeDroit.setPeriodeFermetureDebut(
          periodeDroitdto.getPeriodeFermetureDebut() == null
              ? null
              : sdf.format(periodeDroitdto.getPeriodeFermetureDebut()));
      periodeDroit.setPeriodeFermetureFin(
          periodeDroitdto.getPeriodeFermetureFin() == null
              ? null
              : sdf.format(periodeDroitdto.getPeriodeFermetureFin()));
    }
    return periodeDroit;
  }

  @Override
  public PeriodeDroitDto entityToDto(
      PeriodeDroit periodeDroit,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche) {
    PeriodeDroitDto periodeDroitDto = null;
    if (periodeDroit != null) {
      periodeDroitDto = new PeriodeDroitDto();
      if (StringUtils.isNotBlank(periodeDroit.getDateEvenement())) {
        periodeDroitDto.setDateEvenemnt(
            DateUtils.parseDate(periodeDroit.getDateEvenement(), DateUtils.FORMATTERSLASHED));
      }
      periodeDroitDto.setLibelleEvenement(periodeDroit.getLibelleEvenement());
      periodeDroitDto.setModeObtention(periodeDroit.getModeObtention());
      periodeDroitDto.setMotifEvenement(periodeDroit.getMotifEvenement());
      if (StringUtils.isNotBlank(periodeDroit.getPeriodeDebut())) {
        periodeDroitDto.setPeriodeDebut(
            DateUtils.parseDate(periodeDroit.getPeriodeDebut(), DateUtils.FORMATTERSLASHED));
      }
      if (StringUtils.isNotBlank(periodeDroit.getPeriodeFin())) {
        periodeDroitDto.setPeriodeFin(
            DateUtils.parseDate(periodeDroit.getPeriodeFin(), DateUtils.FORMATTERSLASHED));
      }
      if (StringUtils.isNotBlank(periodeDroit.getPeriodeFinInitiale())) {
        periodeDroitDto.setPeriodeFinInitiale(
            DateUtils.parseDate(periodeDroit.getPeriodeFinInitiale(), DateUtils.FORMATTERSLASHED));
      }
      if (StringUtils.isNotBlank(periodeDroit.getPeriodeFermetureFin())) {
        periodeDroitDto.setPeriodeFermetureFin(
            DateUtils.parseDate(periodeDroit.getPeriodeFermetureFin(), DateUtils.FORMATTERSLASHED));
      }
      if (StringUtils.isNotBlank(periodeDroit.getPeriodeFermetureDebut())) {
        periodeDroitDto.setPeriodeFermetureDebut(
            DateUtils.parseDate(
                periodeDroit.getPeriodeFermetureDebut(), DateUtils.FORMATTERSLASHED));
      }
    }
    return periodeDroitDto;
  }
}
