package com.cegedim.next.serviceeligibility.core.dao.traces;

import com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo.TraceServicePrestation;
import com.cegedim.next.serviceeligibility.core.model.kafka.Trace;
import com.mongodb.BasicDBObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;

public interface TraceDao {

  /**
   * Method to get a trace by his id
   *
   * @param id trace id
   * @param collection Collection name
   * @return Corresponding trace giving the id
   * @throws IllegalArgumentException if id is empty
   */
  Trace findOneBy(final String id, final String collection);

  long deleteServicePrestationTracesByFileName(final String fileName);

  long deleteServicePrestationTracesByAmc(final String idDeclarant);

  long deleteBeneficiaryTracesByOriginId(final String originId);

  long deleteBeneficiaryTracesByAmc(final String idDeclarant);

  long deleteDeclarationTracesByAmc(final String idDeclarant);

  long deleteDeclarationTracesByOriginId(final String originId);

  long deleteInsuredDataTracesByAmc(final String idDeclarant);

  Page<TraceServicePrestation> getTraceForArl(
      String fileName, String firstTraceId, Pageable pageable);

  long deleteTracesByAMC(String amc);

  long deleteTracesByFileName(String file);

  Trace save(Trace trace, String collection);

  void updateFirst(Query query, UpdateDefinition update, String collection);

  void updateById(String id, BasicDBObject update, String collection);
}
