package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.CodeRenvoiTPDto;
import com.cegedim.next.serviceeligibility.core.model.domain.CodeRenvoiTP;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MapperCodeRenvoiTP {
  public CodeRenvoiTP dtoToEntity(CodeRenvoiTPDto codeRenvoiTPDto) {
    CodeRenvoiTP codeRenvoiTP = null;
    if (codeRenvoiTPDto != null) {
      codeRenvoiTP = new CodeRenvoiTP();
      codeRenvoiTP.setReseauSoin(codeRenvoiTPDto.getReseauSoin());
      codeRenvoiTP.setDomaineTP(codeRenvoiTPDto.getDomaineTP());
      codeRenvoiTP.setCodeRenvoi(codeRenvoiTPDto.getCodeRenvoi());
      codeRenvoiTP.setDateDebut(codeRenvoiTPDto.getDateDebut());
      codeRenvoiTP.setDateFin(codeRenvoiTPDto.getDateFin());
    }
    return codeRenvoiTP;
  }

  public CodeRenvoiTPDto entityToDto(CodeRenvoiTP codeRenvoiTP) {
    CodeRenvoiTPDto codeRenvoiTPDto = null;
    if (codeRenvoiTP != null) {
      codeRenvoiTPDto = new CodeRenvoiTPDto();
      codeRenvoiTPDto.setReseauSoin(codeRenvoiTP.getReseauSoin());
      codeRenvoiTPDto.setDomaineTP(codeRenvoiTP.getDomaineTP());
      codeRenvoiTPDto.setCodeRenvoi(codeRenvoiTP.getCodeRenvoi());
      codeRenvoiTPDto.setDateDebut(codeRenvoiTP.getDateDebut());
      codeRenvoiTPDto.setDateFin(codeRenvoiTP.getDateFin());
    }
    return codeRenvoiTPDto;
  }

  public List<CodeRenvoiTP> dtoListToEntityList(final List<CodeRenvoiTPDto> dtoList) {
    List<CodeRenvoiTP> codeRenvoiTPList = new ArrayList<>();
    for (CodeRenvoiTPDto dto : dtoList) {
      codeRenvoiTPList.add(dtoToEntity(dto));
    }
    return codeRenvoiTPList;
  }

  public List<CodeRenvoiTPDto> entityListToDtoList(final List<CodeRenvoiTP> codeRenvoiTPList) {
    List<CodeRenvoiTPDto> dtoList = new ArrayList<>();
    for (CodeRenvoiTP codeRenvoiTP : codeRenvoiTPList) {
      dtoList.add(entityToDto(codeRenvoiTP));
    }
    return dtoList;
  }
}
