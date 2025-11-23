package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.ConventionTPDto;
import com.cegedim.next.serviceeligibility.core.model.domain.ConventionTP;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MapperConventionTP {
  public ConventionTP dtoToEntity(ConventionTPDto conventionTPDto) {
    ConventionTP conventionTP = null;
    if (conventionTPDto != null) {
      conventionTP = new ConventionTP();
      conventionTP.setReseauSoin(conventionTPDto.getReseauSoin());
      conventionTP.setDomaineTP(conventionTPDto.getDomaineTP());
      conventionTP.setConventionCible(conventionTPDto.getConventionCible());
      conventionTP.setConcatenation(conventionTPDto.isConcatenation());
      conventionTP.setDateDebut(conventionTPDto.getDateDebut());
      conventionTP.setDateFin(conventionTPDto.getDateFin());
    }
    return conventionTP;
  }

  public ConventionTPDto entityToDto(ConventionTP conventionTP) {
    ConventionTPDto conventionTPDto = null;
    if (conventionTP != null) {
      conventionTPDto = new ConventionTPDto();
      conventionTPDto.setReseauSoin(conventionTP.getReseauSoin());
      conventionTPDto.setDomaineTP(conventionTP.getDomaineTP());
      conventionTPDto.setConventionCible(conventionTP.getConventionCible());
      conventionTPDto.setConcatenation(conventionTP.isConcatenation());
      conventionTPDto.setDateDebut(conventionTP.getDateDebut());
      conventionTPDto.setDateFin(conventionTP.getDateFin());
    }
    return conventionTPDto;
  }

  public List<ConventionTP> dtoListToEntityList(final List<ConventionTPDto> dtoList) {
    List<ConventionTP> conventionTPList = new ArrayList<>();
    for (ConventionTPDto dto : dtoList) {
      conventionTPList.add(dtoToEntity(dto));
    }
    return conventionTPList;
  }

  public List<ConventionTPDto> entityListToDtoList(final List<ConventionTP> conventionTPList) {
    List<ConventionTPDto> dtoList = new ArrayList<>();
    for (ConventionTP conventionTP : conventionTPList) {
      dtoList.add(entityToDto(conventionTP));
    }
    return dtoList;
  }
}
