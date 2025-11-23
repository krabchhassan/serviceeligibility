package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.flux.ParametresFluxDto;
import com.cegedim.next.serviceeligibility.core.model.query.ParametresFlux;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class MapperParametresFlux {

  private String getNonBlankString(String x) {
    if (StringUtils.isNotBlank(x)) {
      return x.trim();
    }
    return null;
  }

  public ParametresFlux dtoToEntity(ParametresFluxDto parametresFluxDto) {
    ParametresFlux parametresFlux = new ParametresFlux();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

    parametresFlux.setFichierEmis(parametresFluxDto.isFichierEmis());
    parametresFlux.setNewSearch(parametresFluxDto.isNewSearch());

    // Conversion date debut
    Date dateDebut = null;
    if (StringUtils.isNotBlank(parametresFluxDto.getDateDebut())) {

      StringBuilder stringBuffer = new StringBuilder(parametresFluxDto.getDateDebut().trim());
      if (stringBuffer.length() == 10) {
        stringBuffer.append(" 00:00:00");
      }
      try {
        dateDebut = sdf.parse(stringBuffer.toString());
      } catch (ParseException e) {
        dateDebut = null;
      }
    }
    parametresFlux.setDateDebut(dateDebut);

    // Conversion date fin
    Date dateFin = null;
    if (StringUtils.isNotBlank(parametresFluxDto.getDateFin())) {
      StringBuilder stringBuffer = new StringBuilder(parametresFluxDto.getDateFin().trim());
      if (stringBuffer.length() == 10) {
        stringBuffer.append(" 23:59:59");
      }
      try {
        dateFin = sdf.parse(stringBuffer.toString());
      } catch (ParseException e) {
        dateFin = null;
      }
    }
    parametresFlux.setDateFin(dateFin);

    Optional.ofNullable(parametresFluxDto.getAmc())
        .map(this::getNonBlankString)
        .ifPresent(parametresFlux::setIdDeclarant);

    Optional.ofNullable(parametresFluxDto.getNomAMC())
        .map(this::getNonBlankString)
        .ifPresent(parametresFlux::setNomAmc);

    Optional.ofNullable(parametresFluxDto.getCodeCircuit())
        .map(this::getNonBlankString)
        .ifPresent(parametresFlux::setCodeCircuit);

    Optional.ofNullable(parametresFluxDto.getCodePartenaire())
        .map(this::getNonBlankString)
        .ifPresent(parametresFlux::setCodePartenaire);

    Optional.ofNullable(parametresFluxDto.getEmetteur())
        .map(this::getNonBlankString)
        .ifPresent(parametresFlux::setEmetteur);

    Optional.ofNullable(parametresFluxDto.getNumeroFichier())
        .map(this::getNonBlankString)
        .ifPresent(parametresFlux::setNumeroFichier);

    Optional.ofNullable(parametresFluxDto.getProcessus())
        .map(this::getNonBlankString)
        .ifPresent(parametresFlux::setProcessus);

    Optional.ofNullable(parametresFluxDto.getTypeFichier())
        .map(this::getNonBlankString)
        .ifPresent(parametresFlux::setTypeFichier);

    Optional.ofNullable(parametresFluxDto.getNomFichier())
        .map(this::getNonBlankString)
        .ifPresent(parametresFlux::setNomFichier);

    int page = 0;
    int pageSize = 1;

    if (StringUtils.isNotBlank(parametresFluxDto.getPosition())) {
      page = Integer.valueOf(parametresFluxDto.getPosition().trim());
    }

    if (StringUtils.isNotBlank(parametresFluxDto.getNumberByPage())) {
      pageSize = Integer.valueOf(parametresFluxDto.getNumberByPage().trim());
    }

    if (page > 0) {
      page = page / pageSize;
    }

    parametresFlux.setPosition(page);
    parametresFlux.setNumberByPage(pageSize);
    return parametresFlux;
  }
}
