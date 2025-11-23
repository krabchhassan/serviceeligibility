package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.FormuleDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.ParametreFormuleDto;
import com.cegedim.next.serviceeligibility.core.model.domain.Formule;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MapperFormule {

  @Autowired private MapperParametreFormule mapperParametreFormule;

  public Formule dtoToEntity() {
    return null;
  }

  public FormuleDto entityToDto(Formule formule) {
    FormuleDto formuleDto = new FormuleDto();
    if (formule != null) {
      formuleDto.setNumero(formule.getNumero());
      if (formule.getParametres() != null && !formule.getParametres().isEmpty()) {
        List<ParametreFormuleDto> listeParamFormule =
            mapperParametreFormule.entityListToDtoList(formule.getParametres());
        listeParamFormule.sort(Comparator.comparing(ParametreFormuleDto::getCode));
        formuleDto.setParametres(listeParamFormule);
      }
    }
    return formuleDto;
  }
}
