package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.DeclarationBackendDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.HistoriqueDeclarationsDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.HistoriqueInfoDeclarationDto;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MapperHistoriqueDeclarations {

  @Autowired private MapperHistoriqueInfoDeclaration mapperHistoriqueInfoDeclaration;

  @Autowired private DeclarationBackendDao declarationDao;

  public HistoriqueDeclarationsDto entityToDto(Declaration declaration) {
    HistoriqueDeclarationsDto histoDto = new HistoriqueDeclarationsDto();
    List<Declaration> declarations =
        declarationDao.findDeclarationsByBenefContrat(
            declaration.getIdDeclarant(),
            declaration.getBeneficiaire().getNumeroPersonne(),
            declaration.getContrat().getNumero(),
            Constants.MAX_DECLARATIONS_FOR_UI);

    List<HistoriqueInfoDeclarationDto> listeHisto =
        mapperHistoriqueInfoDeclaration.entityListToDtoList(declarations);
    for (HistoriqueInfoDeclarationDto histo : listeHisto) {
      if (declaration.get_id().equals(histo.getIdHistorique())) {
        histo.setIsCurrent(true);
        break;
      }
    }
    Comparator<HistoriqueInfoDeclarationDto> comparator =
        Comparator.comparing(HistoriqueInfoDeclarationDto::getEffetDebutDate);
    Stream<HistoriqueInfoDeclarationDto> histoStream =
        listeHisto.stream().sorted(comparator.reversed());
    histoDto.setInfosHistorique(histoStream.toList());
    histoDto.setNumeroContrat(declaration.getContrat().getNumero());
    return histoDto;
  }
}
