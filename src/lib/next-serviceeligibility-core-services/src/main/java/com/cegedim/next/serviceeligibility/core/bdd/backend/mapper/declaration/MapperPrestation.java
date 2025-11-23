package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.PrestationDto;
import com.cegedim.next.serviceeligibility.core.model.domain.Prestation;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MapperPrestation {

  @Autowired private MapperFormule mapperFormule;

  public PrestationDto entityToDto(Prestation prestation) {
    PrestationDto prestationDto = new PrestationDto();
    if (prestation != null) {
      prestationDto.setCode(prestation.getCode());
      if (prestation.getFormule() != null) {
        prestationDto.setFormule(mapperFormule.entityToDto(prestation.getFormule()));
      }
    }
    return prestationDto;
  }

  public List<PrestationDto> entityListToDtoList(final List<Prestation> list) {
    List<PrestationDto> dtoList = new ArrayList<>();
    for (Prestation domain : list) {
      dtoList.add(entityToDto(domain));
    }
    return dtoList;
  }
}
