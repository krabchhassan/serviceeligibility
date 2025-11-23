package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.DomaineDroitDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.DroitsDto;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.utils.Util;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MapperDroits {

  @Autowired private MapperDomaineDroit mapperDomaineDroit;

  private static final Logger LOGGER = LoggerFactory.getLogger(MapperDroits.class);
  private static final String EMPTY_DATE = "--/--/----";

  public DroitsDto entityToDto(Declaration declaration) {
    DroitsDto droitsDto = new DroitsDto();
    List<DomaineDroitDto> listeDomainesDroitsDto =
        mapperDomaineDroit.entityListToDtoList(
            declaration.getDomaineDroits(),
            declaration.getDateRestitution(),
            Util.isExcluDemat(declaration.getCarteTPaEditerOuDigitale()));

    droitsDto.setIsDroitOuvert("V".equals(declaration.getCodeEtat()));
    droitsDto.setNbDomaines(listeDomainesDroitsDto.size());
    droitsDto.setNbGaranties(calculGarantieUnique(listeDomainesDroitsDto));

    try {
      if (StringUtils.isNotBlank(declaration.getDateRestitution())) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdfDefault = new SimpleDateFormat("dd/MM/yyyy");
        Date dateRestitution = sdf.parse(declaration.getDateRestitution());
        droitsDto.setDateRestitutionCarte(sdfDefault.format(dateRestitution));
      }
    } catch (ParseException e) {
      LOGGER.error("MapperDomaineDroitImpl#Erreur lors de la conversion de date", e);
      droitsDto.setDateRestitutionCarte(null);
    }

    String periodeDroitDebut =
        listeDomainesDroitsDto.stream()
            .flatMap(
                domaineDroitDto ->
                    Stream.of(
                        domaineDroitDto.getPeriodeOnlineDebut(), domaineDroitDto.getPeriodeDebut()))
            .filter(Objects::nonNull)
            .min(Comparator.comparing(MapperDroits::reverse))
            .orElse(null);
    droitsDto.setPeriodeDroitDebut(periodeDroitDebut);

    boolean containsEmptyDate =
        listeDomainesDroitsDto.stream()
            .map(DomaineDroitDto::getPeriodeOnlineFin)
            .anyMatch(EMPTY_DATE::equals);

    String periodeDroitFin =
        containsEmptyDate
            ? EMPTY_DATE
            : listeDomainesDroitsDto.stream()
                .map(DomaineDroitDto::getPeriodeOnlineFin)
                .filter(Objects::nonNull)
                .max(Comparator.comparing(MapperDroits::reverse))
                .orElse(null);
    droitsDto.setPeriodeDroitFin(periodeDroitFin);

    String periodeDroitOfflineFin =
        listeDomainesDroitsDto.stream()
            .map(DomaineDroitDto::getPeriodeOfflineFin)
            .filter(Objects::nonNull)
            .max(Comparator.comparing(MapperDroits::reverse))
            .orElse(null);
    droitsDto.setPeriodeDroitOfflineFin(periodeDroitOfflineFin);

    Comparator<DomaineDroitDto> comparator = Comparator.comparing(DomaineDroitDto::getCode);
    comparator = comparator.thenComparing(DomaineDroitDto::getPriorite);
    comparator = comparator.thenComparing(d -> MapperDroits.reverse(d.getPeriodeDebut()));
    Stream<DomaineDroitDto> domaineStream = listeDomainesDroitsDto.stream().sorted(comparator);
    droitsDto.setDomaines(domaineStream.toList());
    return droitsDto;
  }

  /** reverse string order, ex: dd/MM/yyyy to yyyy/MM/dd */
  private static String reverse(String s) {
    if (s == null) {
      return null;
    }
    return new StringBuilder(s).reverse().toString();
  }

  private int calculGarantieUnique(List<DomaineDroitDto> listeDomaine) {
    Set<String> setGarantie = new HashSet<>();
    for (DomaineDroitDto domaine : listeDomaine) {
      setGarantie.add(domaine.getGarantie());
    }
    return setGarantie.size();
  }
}
