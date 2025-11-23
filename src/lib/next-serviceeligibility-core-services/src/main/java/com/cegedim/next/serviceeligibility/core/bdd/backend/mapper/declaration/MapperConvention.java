package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.ConventionDto;
import com.cegedim.next.serviceeligibility.core.model.domain.Conventionnement;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MapperConvention {

  public Conventionnement dtoToEntity() {
    return null;
  }

  public ConventionDto entityToDto(Conventionnement conventionnement) {
    ConventionDto conventionDto = new ConventionDto();
    conventionDto.setCode(conventionnement.getTypeConventionnement().getCode());
    conventionDto.setPriorite(conventionnement.getPriorite().toString());
    return conventionDto;
  }

  public List<ConventionDto> entityListToDtoList(final List<Conventionnement> list) {
    List<ConventionDto> dtoList = new ArrayList<>();
    for (Conventionnement domain : list) {
      dtoList.add(entityToDto(domain));
    }
    return dtoList;
  }
}
