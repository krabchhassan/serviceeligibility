package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo.RDOGroup;
import java.util.List;

public interface RDOServicePrestationDAO {
  void upsert(RDOGroup rdoGroup);

  void upsertMulti(List<RDOGroup> rdoGroups);

  RDOGroup getRDOGroupById(String id);

  void createRDOGroup(RDOGroup rdoGroup);

  void deleteAll();

  long deleteByAMC(String idAMC);
}
