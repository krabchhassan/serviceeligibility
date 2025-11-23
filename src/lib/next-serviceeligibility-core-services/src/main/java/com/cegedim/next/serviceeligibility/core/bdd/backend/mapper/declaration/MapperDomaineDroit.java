package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.ConventionDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.DomaineDroitDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.PrestationDto;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.PeriodeDroit;
import com.cegedim.next.serviceeligibility.core.utils.Util;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionServiceFormatDate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MapperDomaineDroit {

  @Autowired private MapperCouverture mapperCouverture;

  @Autowired private MapperConvention mapperConvention;

  @Autowired private MapperPriorites mapperPriorites;

  @Autowired private MapperPrestation mapperPrestation;

  private static final Logger LOGGER = LoggerFactory.getLogger(MapperDomaineDroit.class);

  public DomaineDroitDto entityToDto(
      DomaineDroit domaine, String dateRestitution, Boolean isExclusiviteCarteDematerialise) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat sdfDefault = new SimpleDateFormat("dd/MM/yyyy");

    DomaineDroitDto domaineDroitDto = new DomaineDroitDto();
    domaineDroitDto.setCode(domaine.getCode());
    domaineDroitDto.setFormulaMask(domaine.getFormulaMask());
    domaineDroitDto.setGarantie(domaine.getCodeGarantie());
    domaineDroitDto.setGarantieLibelle(domaine.getLibelleGarantie());
    domaineDroitDto.setTauxRemboursement(domaine.getTauxRemboursement());
    domaineDroitDto.setUniteTaux(domaine.getUniteTauxRemboursement());
    if (domaine.getPeriodeDroit() != null) {
      extractDates(
          domaine,
          sdf,
          sdfDefault,
          domaineDroitDto,
          dateRestitution,
          isExclusiviteCarteDematerialise);
    }
    domaineDroitDto.setPriorite(domaine.getPrioriteDroit().getCode());
    domaineDroitDto.setCouverture(mapperCouverture.entityToDto(domaine));
    domaineDroitDto.setPriorites(mapperPriorites.entityToDto(domaine.getPrioriteDroit()));
    if (domaine.getConventionnements() != null) {
      List<ConventionDto> listeConvention =
          mapperConvention.entityListToDtoList(domaine.getConventionnements());
      listeConvention.sort(Comparator.comparing(ConventionDto::getPriorite));
      domaineDroitDto.setConventions(listeConvention);
    }
    if (domaine.getPrestations() != null) {
      List<PrestationDto> listePrestations =
          mapperPrestation.entityListToDtoList(domaine.getPrestations());
      listePrestations.sort(Comparator.comparing(PrestationDto::getCode));
      PrestationDto codeDef = null;
      for (int i = 0; i < listePrestations.size(); i++) {
        if ("DEF".equals(listePrestations.get(i).getCode())) {
          codeDef = listePrestations.get(i);
          listePrestations.remove(i);
          break;
        }
      }
      if (codeDef != null) {
        listePrestations.addFirst(codeDef);
      }
      domaineDroitDto.setPrestations(listePrestations);
      domaineDroitDto.setNbPrestations(domaine.getPrestations().size());
    }
    domaineDroitDto.setNaturePrestation(domaine.getNaturePrestation());
    domaineDroitDto.setNaturePrestationOnline(domaine.getNaturePrestationOnline());
    return domaineDroitDto;
  }

  public void extractDates(
      DomaineDroit domaine,
      SimpleDateFormat sdf,
      SimpleDateFormat sdfDefault,
      DomaineDroitDto domaineDroitDto,
      String dateRestitution,
      Boolean isExclusiviteCarteDematerialise) {
    try {
      PeriodeDroit periodeDroit = domaine.getPeriodeDroit();
      setPeriodeOnline(domaine, sdf, sdfDefault, domaineDroitDto, periodeDroit);
      if (StringUtils.isNotBlank(periodeDroit.getPeriodeDebut())) {
        Date dateDebut = sdf.parse(periodeDroit.getPeriodeDebut());
        domaineDroitDto.setPeriodeDebutDate(dateDebut);
        domaineDroitDto.setPeriodeDebut(sdfDefault.format(dateDebut));
      }
      if (StringUtils.isNotBlank(periodeDroit.getPeriodeFin())) {
        Date dateFin = sdf.parse(periodeDroit.getPeriodeFin());
        domaineDroitDto.setPeriodeFinDate(dateFin);
        domaineDroitDto.setPeriodeFin(sdfDefault.format(dateFin));
      }
      if (StringUtils.isNotBlank(periodeDroit.getPeriodeFermetureDebut())) {
        Date dateFermetureDebut = sdf.parse(periodeDroit.getPeriodeFermetureDebut());
        domaineDroitDto.setPeriodeFermetureDebut(sdfDefault.format(dateFermetureDebut));
      }
      if (StringUtils.isNotBlank(periodeDroit.getPeriodeFermetureFin())) {
        Date dateFermetureFin = sdf.parse(periodeDroit.getPeriodeFermetureFin());
        domaineDroitDto.setPeriodeFermetureFin(sdfDefault.format(dateFermetureFin));
      }
      String dateFinOffline =
          Util.getDateFinOffline(
              periodeDroit.getPeriodeFin(),
              periodeDroit.getPeriodeFermetureFin(),
              dateRestitution,
              isExclusiviteCarteDematerialise);
      if (StringUtils.isNotBlank(dateFinOffline)) {
        domaineDroitDto.setPeriodeOfflineFin(sdfDefault.format(sdf.parse(dateFinOffline)));
      }
    } catch (ParseException e) {
      LOGGER.error("MapperDomaineDroitImpl#Erreur lors de la conversion de date", e);
      throw new ExceptionServiceFormatDate();
    }
  }

  private static void setPeriodeOnline(
      DomaineDroit domaine,
      SimpleDateFormat sdf,
      SimpleDateFormat sdfDefault,
      DomaineDroitDto domaineDroitDto,
      PeriodeDroit periodeDroit)
      throws ParseException {
    if (domaine.getPeriodeOnline() != null) {
      if (StringUtils.isNotBlank(domaine.getPeriodeOnline().getPeriodeDebut())) {
        Date dateDebut = sdf.parse(domaine.getPeriodeOnline().getPeriodeDebut());
        domaineDroitDto.setPeriodeOnlineDebut(sdfDefault.format(dateDebut));
      }
      if (StringUtils.isNotBlank(domaine.getPeriodeOnline().getPeriodeFin())) {
        Date dateFin = sdf.parse(domaine.getPeriodeOnline().getPeriodeFin());
        domaineDroitDto.setPeriodeOnlineFin(sdfDefault.format(dateFin));
      }
      if (StringUtils.isNotBlank(domaine.getPeriodeOnline().getPeriodeDebut())
          && StringUtils.isBlank(domaine.getPeriodeOnline().getPeriodeFin())) {
        domaineDroitDto.setPeriodeOnlineFin("--/--/----");
      }
    } else {
      Date dateDebut = sdf.parse(periodeDroit.getPeriodeDebut());
      domaineDroitDto.setPeriodeOnlineDebut(sdfDefault.format(dateDebut));
      if (StringUtils.isNotBlank(periodeDroit.getPeriodeFin())) {
        Date dateFin = sdf.parse(periodeDroit.getPeriodeFin());
        domaineDroitDto.setPeriodeOnlineFin(sdfDefault.format(dateFin));
      }
      if (StringUtils.isNotBlank(periodeDroit.getPeriodeDebut())
          && StringUtils.isBlank(periodeDroit.getPeriodeFin())) {
        domaineDroitDto.setPeriodeOnlineFin("--/--/----");
      }
    }
  }

  public List<DomaineDroitDto> entityListToDtoList(
      final List<DomaineDroit> list,
      String dateRestitution,
      Boolean isExclusiviteCarteDematerialise) {
    List<DomaineDroitDto> dtoList = new ArrayList<>();
    for (DomaineDroit domain : list) {
      dtoList.add(entityToDto(domain, dateRestitution, isExclusiviteCarteDematerialise));
    }
    return dtoList;
  }
}
