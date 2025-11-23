package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.TranscoDomainesTPDto;
import com.cegedim.next.serviceeligibility.core.model.domain.TranscoDomainesTP;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MapperTranscodageDomaineTP {
  public TranscoDomainesTP dtoToEntity(TranscoDomainesTPDto transcoDomainesTPDto) {
    TranscoDomainesTP transcoDomainesTP = null;
    if (transcoDomainesTPDto != null) {
      transcoDomainesTP = new TranscoDomainesTP();
      transcoDomainesTP.setDomaineSource(transcoDomainesTPDto.getDomaineSource());
      transcoDomainesTP.setDomainesCible(transcoDomainesTPDto.getDomainesCible());
    }
    return transcoDomainesTP;
  }

  public TranscoDomainesTPDto entityToDto(TranscoDomainesTP transcoDomainesTP) {
    TranscoDomainesTPDto transcoDomainesTPDto = null;
    if (transcoDomainesTP != null) {
      transcoDomainesTPDto = new TranscoDomainesTPDto();
      transcoDomainesTPDto.setDomaineSource(transcoDomainesTP.getDomaineSource());
      transcoDomainesTPDto.setDomainesCible(transcoDomainesTP.getDomainesCible());
    }
    return transcoDomainesTPDto;
  }

  public List<TranscoDomainesTP> dtoListToEntityList(final List<TranscoDomainesTPDto> dtoList) {
    List<TranscoDomainesTP> domainList = new ArrayList<>();
    for (TranscoDomainesTPDto dto : dtoList) {
      domainList.add(dtoToEntity(dto));
    }
    return domainList;
  }

  public List<TranscoDomainesTPDto> entityListToDtoList(final List<TranscoDomainesTP> domainList) {
    List<TranscoDomainesTPDto> dtoList = new ArrayList<>();
    for (TranscoDomainesTP domain : domainList) {
      dtoList.add(entityToDto(domain));
    }
    return dtoList;
  }
}
