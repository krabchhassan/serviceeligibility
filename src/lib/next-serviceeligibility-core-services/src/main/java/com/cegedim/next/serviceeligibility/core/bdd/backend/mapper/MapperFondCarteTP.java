package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.FondCarteTPDto;
import com.cegedim.next.serviceeligibility.core.model.domain.FondCarteTP;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MapperFondCarteTP {
  public FondCarteTP dtoToEntity(FondCarteTPDto fondCarteTPDto) {
    FondCarteTP fondCarteTP = null;
    if (fondCarteTPDto != null) {
      fondCarteTP = new FondCarteTP();
      fondCarteTP.setReseauSoin(fondCarteTPDto.getReseauSoin());
      fondCarteTP.setFondCarte(fondCarteTPDto.getFondCarte());
      fondCarteTP.setDateDebut(fondCarteTPDto.getDateDebut());
      fondCarteTP.setDateFin(fondCarteTPDto.getDateFin());
    }
    return fondCarteTP;
  }

  public FondCarteTPDto entityToDto(FondCarteTP fondCarteTP) {
    FondCarteTPDto fondCarteTPDto = null;
    if (fondCarteTP != null) {
      fondCarteTPDto = new FondCarteTPDto();
      fondCarteTPDto.setReseauSoin(fondCarteTP.getReseauSoin());
      fondCarteTPDto.setFondCarte(fondCarteTP.getFondCarte());
      fondCarteTPDto.setDateDebut(fondCarteTP.getDateDebut());
      fondCarteTPDto.setDateFin(fondCarteTP.getDateFin());
    }
    return fondCarteTPDto;
  }

  public List<FondCarteTP> dtoListToEntityList(final List<FondCarteTPDto> dtoList) {
    List<FondCarteTP> fondCarteTPList = new ArrayList<>();
    for (FondCarteTPDto dto : dtoList) {
      fondCarteTPList.add(dtoToEntity(dto));
    }
    return fondCarteTPList;
  }

  public List<FondCarteTPDto> entityListToDtoList(final List<FondCarteTP> fondCarteTPList) {
    List<FondCarteTPDto> dtoList = new ArrayList<>();
    for (FondCarteTP fondCarteTP : fondCarteTPList) {
      dtoList.add(entityToDto(fondCarteTP));
    }
    return dtoList;
  }
}
