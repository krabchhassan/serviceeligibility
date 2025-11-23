package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.dao.DeclarationsConsolideesAlmerysDao;
import com.cegedim.next.serviceeligibility.core.model.domain.Pilotage;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.DeclarationsConsolideesAlmerys;
import com.cegedim.next.serviceeligibility.core.model.entity.almv3.TmpObject2;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.aggregation.Aggregation;

@RequiredArgsConstructor
public class DeclarationsConsolideesAlmerysService {
  private final DeclarationsConsolideesAlmerysDao declarationsConsolideesAlmerysDao;

  public void save(DeclarationsConsolideesAlmerys declarationsConsolideesAlmerys) {
    declarationsConsolideesAlmerysDao.save(declarationsConsolideesAlmerys);
  }

  public void createTmpCollection(Aggregation aggregation) {
    declarationsConsolideesAlmerysDao.createTmpCollection(aggregation);
  }

  public void createIndexOnTmp2(String collectionName) {
    declarationsConsolideesAlmerysDao.createIndexOnTmp2(collectionName);
  }

  public void createIndexContratRefInterneOsOnTmpTable(String collectionName) {
    declarationsConsolideesAlmerysDao.createIndexContratRefInterneOsOnTmpTable(collectionName);
  }

  public void createIndexContratOnTmpTable(String collectionName) {
    declarationsConsolideesAlmerysDao.createIndexContratOnTmpTable(collectionName);
  }

  public void deleteTmpCollection(String tmpCollectionName) {
    declarationsConsolideesAlmerysDao.dropTemporaryCollection(tmpCollectionName);
  }

  public Stream<TmpObject2> getAll(String tmpCollectionName) {
    return declarationsConsolideesAlmerysDao.getAll(tmpCollectionName);
  }

  public Stream<TmpObject2> getAllForSouscripteur(String tmpCollectionName) {
    return declarationsConsolideesAlmerysDao.getAllForSouscripteur(tmpCollectionName);
  }

  public Integer countDeclarationConsolideesAlmerys(
      Declarant declarant,
      Pilotage pilotage,
      Date dateDerniereExecution,
      List<String> critSecondaireDetailleToExclude) {
    return declarationsConsolideesAlmerysDao.countDeclarationConsolideesAlmerys(
        declarant, pilotage, dateDerniereExecution, critSecondaireDetailleToExclude);
  }

  public Integer countAllDeclarationConsolideesAlmerys() {
    return declarationsConsolideesAlmerysDao.countAll();
  }
}
