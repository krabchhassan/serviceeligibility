package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.entity.DeclarationConsolide;
import com.mongodb.client.ClientSession;
import java.util.Collection;
import java.util.List;

public interface DeclarationConsolideDao {

  Collection<DeclarationConsolide> insertAll(List<DeclarationConsolide> declarationConsolides);

  List<DeclarationConsolide> getDeclarationsConsolideesByAmcContrats(String amcContracts);

  void deleteAll();

  void bulkDeleteUpdate(
      List<DeclarationConsolide> toDeletes,
      List<DeclarationConsolide> toUpdates,
      ClientSession session);

  long deleteByAMC(String amc);

  long deleteByDeclaration(String declaration);
}
