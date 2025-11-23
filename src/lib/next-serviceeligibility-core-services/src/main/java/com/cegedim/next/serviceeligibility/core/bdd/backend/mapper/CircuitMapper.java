package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.CircuitDto;
import com.cegedim.next.serviceeligibility.core.model.entity.Circuit;
import org.springframework.stereotype.Component;

@Component
public class CircuitMapper {

  public CircuitDto entityToDto(Circuit entity) {
    CircuitDto dto = new CircuitDto();
    dto.setCodeCircuit(entity.getCodeCircuit());
    dto.setEmetteur(entity.getEmetteur());
    dto.setLibelleCircuit(entity.getLibelleCircuit());
    return dto;
  }

  public Circuit dtoToEntity(CircuitDto dto) {
    Circuit entity = new Circuit();
    entity.setCodeCircuit(dto.getCodeCircuit());
    entity.setEmetteur(dto.getEmetteur());
    entity.setLibelleCircuit(dto.getLibelleCircuit());
    return entity;
  }
}
