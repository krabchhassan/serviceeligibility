package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.ParametreFormuleDto;
import com.cegedim.next.serviceeligibility.core.model.domain.Parametre;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MapperParametreFormule {

  public ParametreFormuleDto entityToDto(Parametre parametre) {
    ParametreFormuleDto parametreFormuleDto = new ParametreFormuleDto();
    if (parametre != null) {
      parametreFormuleDto.setCode(parametre.getNumero());
      parametreFormuleDto.setValeur(parametre.getValeur());
    }
    return parametreFormuleDto;
  }

  public List<ParametreFormuleDto> entityListToDtoList(final List<Parametre> list) {
    List<ParametreFormuleDto> dtoList = new ArrayList<>();
    for (Parametre domain : list) {
      dtoList.add(entityToDto(domain));
    }
    return dtoList;
  }
}
