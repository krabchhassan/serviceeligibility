package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecutionsRenouvellement;

public interface HistoriqueExecutionsRenouvellementDao {

  HistoriqueExecutionsRenouvellement getLastExecution();

  HistoriqueExecutionsRenouvellement getLastExecutionIgnoringRdo();

  void create(HistoriqueExecutionsRenouvellement h);

  long deleteAll();
}
