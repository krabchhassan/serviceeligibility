package com.cegedim.next.serviceeligibility.core.dao.traces;

import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.job.batch.TraceExtractionConso;
import com.mongodb.client.ClientSession;
import java.util.List;

public interface TraceExtractionConsoDao extends IMongoGenericDao<TraceExtractionConso> {
  TraceExtractionConso save(TraceExtractionConso traceExtractionConso, ClientSession session);

  void saveAll(List<TraceExtractionConso> traceExtractionConsos, ClientSession session);

  List<TraceExtractionConso> getAll();

  void deleteAll();

  void insertAll(List<TraceExtractionConso> traceExtractionConso);

  List<TraceExtractionConso> getByServiceAndIdDeclaration(String idDeclaration, String codeService);

  long replaceFileName(List<String> oldNames, String newName);
}
