package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.RegroupementDomainesTPDto;
import com.cegedim.next.serviceeligibility.core.model.domain.RegroupementDomainesTP;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MapperRegroupementDomainesTP {
  public RegroupementDomainesTP dtoToEntity(RegroupementDomainesTPDto regroupementDomainesTPDto) {
    RegroupementDomainesTP regroupementDomainesTP = null;
    if (regroupementDomainesTPDto != null) {
      regroupementDomainesTP = new RegroupementDomainesTP();
      regroupementDomainesTP.setDomaineRegroupementTP(
          regroupementDomainesTPDto.getDomaineRegroupementTP());
      regroupementDomainesTP.setCodesDomainesTP(regroupementDomainesTPDto.getCodesDomainesTP());
      regroupementDomainesTP.setNiveauRemboursementIdentique(
          regroupementDomainesTPDto.isNiveauRemboursementIdentique());
      regroupementDomainesTP.setDateDebut(regroupementDomainesTPDto.getDateDebut());
      regroupementDomainesTP.setDateFin(regroupementDomainesTPDto.getDateFin());
    }
    return regroupementDomainesTP;
  }

  public RegroupementDomainesTPDto entityToDto(RegroupementDomainesTP regroupementDomainesTP) {
    RegroupementDomainesTPDto regroupementDomainesTPDto = null;
    if (regroupementDomainesTP != null) {
      regroupementDomainesTPDto = new RegroupementDomainesTPDto();
      regroupementDomainesTPDto.setDomaineRegroupementTP(
          regroupementDomainesTP.getDomaineRegroupementTP());
      regroupementDomainesTPDto.setCodesDomainesTP(regroupementDomainesTP.getCodesDomainesTP());
      regroupementDomainesTPDto.setNiveauRemboursementIdentique(
          regroupementDomainesTP.isNiveauRemboursementIdentique());
      regroupementDomainesTPDto.setDateDebut(regroupementDomainesTP.getDateDebut());
      regroupementDomainesTPDto.setDateFin(regroupementDomainesTP.getDateFin());
    }
    return regroupementDomainesTPDto;
  }

  public List<RegroupementDomainesTP> dtoListToEntityList(
      final List<RegroupementDomainesTPDto> dtoList) {
    List<RegroupementDomainesTP> regroupementDomainesTPList = new ArrayList<>();
    for (RegroupementDomainesTPDto dto : dtoList) {
      regroupementDomainesTPList.add(dtoToEntity(dto));
    }
    return regroupementDomainesTPList;
  }

  public List<RegroupementDomainesTPDto> entityListToDtoList(
      final List<RegroupementDomainesTP> regroupementDomainesTPList) {
    List<RegroupementDomainesTPDto> dtoList = new ArrayList<>();
    for (RegroupementDomainesTP regroupementDomainesTP : regroupementDomainesTPList) {
      dtoList.add(entityToDto(regroupementDomainesTP));
    }
    return dtoList;
  }
}
