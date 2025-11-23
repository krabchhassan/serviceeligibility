package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.HistoriqueInfoDeclarationDto;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MapperHistoriqueInfoDeclaration {

  public HistoriqueInfoDeclarationDto entityToDto(Declaration declaration) {
    HistoriqueInfoDeclarationDto historiqueInfoDeclarationDto = new HistoriqueInfoDeclarationDto();
    SimpleDateFormat sdfEffetDebut = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    Date effetDebut = declaration.getEffetDebut();
    historiqueInfoDeclarationDto.setIdHistorique(declaration.get_id());
    historiqueInfoDeclarationDto.setIsDroitOuvert("V".equals(declaration.getCodeEtat()));
    historiqueInfoDeclarationDto.setEffetDebut(sdfEffetDebut.format(effetDebut));
    historiqueInfoDeclarationDto.setEffetDebutDate(effetDebut);
    historiqueInfoDeclarationDto.setIsCurrent(false);
    return historiqueInfoDeclarationDto;
  }

  public List<HistoriqueInfoDeclarationDto> entityListToDtoList(final List<Declaration> list) {
    List<HistoriqueInfoDeclarationDto> dtoList = new ArrayList<>();
    for (Declaration domain : list) {
      dtoList.add(entityToDto(domain));
    }
    return dtoList;
  }
}
