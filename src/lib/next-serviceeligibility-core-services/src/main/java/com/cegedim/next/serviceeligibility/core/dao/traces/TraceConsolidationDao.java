package com.cegedim.next.serviceeligibility.core.dao.traces;

import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.job.batch.TraceConsolidation;
import java.util.List;

public interface TraceConsolidationDao extends IMongoGenericDao<TraceConsolidation> {
  void insertAll(List<TraceConsolidation> traceConsos);

  void saveAll(List<TraceConsolidation> traceConsos);

  List<TraceConsolidation> getAll();

  TraceConsolidation getByServiceAndIdDeclaration(String idDeclaration, String codeService);

  void deleteAll();
}
