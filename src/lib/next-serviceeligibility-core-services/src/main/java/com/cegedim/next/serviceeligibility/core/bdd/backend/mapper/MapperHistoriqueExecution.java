package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.historiqueexecution.HistoriqueExecutionDto;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecution;
import org.springframework.stereotype.Component;

@Component
public class MapperHistoriqueExecution {

  public HistoriqueExecutionDto entityToDto(final HistoriqueExecution entity) {
    final HistoriqueExecutionDto dto = new HistoriqueExecutionDto();
    dto.setBatch(entity.getBatch());
    dto.setCodeService(entity.getCodeService());
    dto.setCritereSecondaire(entity.getCritereSecondaire());
    dto.setCritereSecondaireDetaille(entity.getCritereSecondaireDetaille());
    dto.setDateExecution(entity.getDateExecution());
    dto.setIdDeclarant(entity.getIdDeclarant());
    dto.setTypeConventionnement(entity.getTypeConventionnement());
    return dto;
  }

  public HistoriqueExecution dtoToEntity(final HistoriqueExecutionDto dto) {
    final HistoriqueExecution entity = new HistoriqueExecution();
    entity.setBatch(dto.getBatch());
    entity.setCodeService(dto.getCodeService());
    entity.setCritereSecondaire(dto.getCritereSecondaire());
    entity.setCritereSecondaireDetaille(dto.getCritereSecondaireDetaille());
    entity.setDateExecution(dto.getDateExecution());
    entity.setIdDeclarant(dto.getIdDeclarant());
    entity.setTypeConventionnement(dto.getTypeConventionnement());

    return entity;
  }
}
