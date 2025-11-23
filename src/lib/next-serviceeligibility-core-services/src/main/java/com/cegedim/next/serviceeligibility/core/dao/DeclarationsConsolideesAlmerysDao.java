package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.domain.Pilotage;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.DeclarationsConsolideesAlmerys;
import com.cegedim.next.serviceeligibility.core.model.entity.almv3.TmpObject2;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.mongodb.core.aggregation.Aggregation;

public interface DeclarationsConsolideesAlmerysDao {

  void save(DeclarationsConsolideesAlmerys declarationsConsolideesAlmerys);

  void createTmpCollection(Aggregation aggregation);

  void dropTemporaryCollection(String tmpCollectionName);

  void createIndexOnTmp2(String collectionName);

  Stream<TmpObject2> getAll(String collectionName);

  Stream<TmpObject2> getAllForSouscripteur(String collectionName);

  Integer countDeclarationConsolideesAlmerys(
      Declarant declarant,
      Pilotage pilotage,
      Date dateDerniereExecution,
      List<String> critSecondaireDetailleToExclude);

  Integer countAll();

  void createIndexContratRefInterneOsOnTmpTable(String collectionName);

  void createIndexContratOnTmpTable(String collectionName);
}
