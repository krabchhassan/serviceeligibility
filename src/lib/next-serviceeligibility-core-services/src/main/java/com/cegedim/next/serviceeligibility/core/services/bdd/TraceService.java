package com.cegedim.next.serviceeligibility.core.services.bdd;

import static com.cegedim.next.serviceeligibility.core.utils.Constants.*;

import com.cegedim.next.serviceeligibility.core.dao.traces.TraceDao;
import com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo.ErrorData;
import com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo.TraceServicePrestation;
import com.cegedim.next.serviceeligibility.core.model.enumeration.TraceSource;
import com.cegedim.next.serviceeligibility.core.model.kafka.Trace;
import com.cegedim.next.serviceeligibility.core.model.kafka.TraceStatus;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.mongodb.BasicDBObject;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TraceService {

  private final TraceDao traceDao;

  @ContinueSpan(log = "createTrace (3 params)")
  public String createTraceForDeclaration(String service, String declarationId, String collection) {
    return createTrace(service, declarationId, collection, null);
  }

  @ContinueSpan(log = "createTrace (4 params)")
  public String createTrace(
      String service, String declarationId, String collection, String idDeclarant) {
    Trace trace = new Trace();
    trace.setOriginId(declarationId);
    trace.setMessage(service);
    trace.setDateCreation(LocalDateTime.now(ZoneOffset.UTC));
    trace.setStatus(TraceStatus.ReceivedFromKafka);
    trace.setIdDeclarant(idDeclarant);
    trace = traceDao.save(trace, collection);
    log.debug("trace benef is {} for declaration id: {}", trace.getId(), declarationId);
    return trace.getId();
  }

  public String createTrace(
      String service,
      String originId,
      TraceSource source,
      TraceStatus status,
      String fileName,
      String collection) {
    return createTrace(service, originId, source, status, fileName, collection, null, null, null);
  }

  public String createTrace(
      String service,
      String originId,
      TraceSource source,
      TraceStatus status,
      String fileName,
      String collection,
      String idDeclarant,
      String numeroContrat,
      String numeroAdherent) {
    Trace trace = new Trace();
    trace.setOriginId(originId);
    trace.setMessage(service);
    trace.setDateCreation(LocalDateTime.now(ZoneOffset.UTC));
    trace.setStatus(status);
    trace.setSource(source);
    trace.setFileName(fileName);
    trace.setIdDeclarant(idDeclarant);
    trace.setNumeroContrat(numeroContrat);
    trace.setNumeroAdherent(numeroAdherent);
    trace = traceDao.save(trace, collection);
    log.debug("trace benef is {} for originId id: {}", trace.getId(), originId);
    return trace.getId();
  }

  public String createTraceWithError(
      TraceSource source,
      TraceStatus status,
      String fileName,
      String collection,
      Long lineNumber,
      List<String> errorMessages) {
    Trace trace = new Trace();
    trace.setDateCreation(LocalDateTime.now(ZoneOffset.UTC));
    trace.setStatus(status);
    trace.setSource(source);
    trace.setFileName(fileName);
    trace.setLineNumber(lineNumber);
    trace.setErrorMessages(errorMessages);
    trace = traceDao.save(trace, collection);
    log.debug("trace benef is {}", trace.getId());
    return trace.getId();
  }

  public void completeTrace(String id, TraceStatus status, String benefId, String collection) {
    Criteria c = Criteria.where(Constants.ID).is(id);
    UpdateDefinition u =
        new Update()
            .set(Constants.UPDATE_DATE, LocalDateTime.now(ZoneOffset.UTC))
            .set(Constants.STATUS, status.toString())
            .set(Constants.BENEF_ID, benefId);
    Query q = new Query(c);
    traceDao.updateFirst(q, u, collection);
  }

  public void updateStatusError(
      String id, TraceStatus status, String errorMessage, String collection) {
    Criteria c = Criteria.where(Constants.ID).is(id);
    UpdateDefinition u =
        new Update()
            .set(Constants.ERROR_MESSAGE, errorMessage)
            .set(Constants.STATUS, status.toString());
    Query q = new Query(c);
    traceDao.updateFirst(q, u, collection);
  }

  public void updateStatusError(
      String id,
      TraceStatus status,
      List<String> errorMessages,
      Long lineNumber,
      String collection) {
    Criteria c = Criteria.where(Constants.ID).is(id);
    UpdateDefinition u =
        new Update()
            .set(Constants.ERROR_MESSAGES, errorMessages)
            .set(Constants.LINE_NUMBER, lineNumber)
            .set(Constants.STATUS, status.toString());
    Query q = new Query(c);
    traceDao.updateFirst(q, u, collection);
  }

  public void updateStatusErrorServicePresta(
      String id,
      TraceStatus status,
      List<ErrorData> errorMessages,
      Long lineNumber,
      String collection) {
    Criteria c = Criteria.where(Constants.ID).is(id);
    UpdateDefinition u =
        new Update()
            .set(Constants.ERROR_MESSAGES, errorMessages)
            .set(Constants.LINE_NUMBER, lineNumber)
            .set(Constants.STATUS, status.toString());
    Query q = new Query(c);
    traceDao.updateFirst(q, u, collection);
  }

  /**
   * get a trace by his id
   *
   * @param id trace id
   * @param collection Collection name
   * @return trace giving his id
   */
  public Trace getTrace(final String id, final String collection) {
    return traceDao.findOneBy(id, collection);
  }

  public long deleteServicePrestationTracesByFileName(final String fileName) {
    return traceDao.deleteServicePrestationTracesByFileName(fileName);
  }

  public long deleteServicePrestationTracesByAmc(final String idDeclarant) {
    return traceDao.deleteServicePrestationTracesByAmc(idDeclarant);
  }

  public long deleteBeneficiaryTracesByOriginId(final String originId) {
    return traceDao.deleteBeneficiaryTracesByOriginId(originId);
  }

  public long deleteBeneficiaryTracesByAmc(final String idDeclarant) {
    return traceDao.deleteBeneficiaryTracesByAmc(idDeclarant);
  }

  public long deleteDeclarationTracesByAmc(final String idDeclarant) {
    return traceDao.deleteDeclarationTracesByAmc(idDeclarant);
  }

  public long deleteDeclarationTracesByOriginId(final String originId) {
    return traceDao.deleteDeclarationTracesByOriginId(originId);
  }

  public long deleteInsuredDataTracesByAmc(final String idDeclarant) {
    return traceDao.deleteInsuredDataTracesByAmc(idDeclarant);
  }

  public String createTrace(String body, String collectionName, String idDeclarant) {
    Trace trace = new Trace();
    trace.setMessage(body);
    trace.setDateCreation(LocalDateTime.now(ZoneOffset.UTC));
    trace.setStatus(TraceStatus.ReceivedFromAi);
    trace.setIdDeclarant(idDeclarant);
    trace = traceDao.save(trace, collectionName);
    return trace.getId();
  }

  public void updateStatus(String id, TraceStatus status, String collectionName) {
    Criteria c = Criteria.where(Constants.ID).is(id);
    UpdateDefinition u =
        new Update()
            .set(UPDATE_DATE, LocalDateTime.now(ZoneOffset.UTC))
            .set(Constants.STATUS, status.toString());
    Query q = new Query(c);
    traceDao.updateFirst(q, u, collectionName);
  }

  public void completeTrace(String id, TraceStatus status, String contractAIId) {
    BasicDBObject updateValues = new BasicDBObject(STATUS, status.toString());
    updateValues.append(UPDATE_DATE, LocalDateTime.now(ZoneOffset.UTC));
    updateValues.append("contratAiId", contractAIId);

    traceDao.updateById(id, updateValues, CONTRACT_TRACE);
  }

  public void updateStatusError(
      String id, TraceStatus status, List<String> errorMessages, String collectionName) {
    BasicDBObject updateValues = new BasicDBObject(STATUS, status.toString());
    updateValues.append(ERROR_MESSAGES, errorMessages);

    traceDao.updateById(id, updateValues, collectionName);
  }

  public void updateStatusError(
      String id, TraceStatus status, JSONArray errorMessages, String collectionName) {
    BasicDBObject updateValues = new BasicDBObject();
    updateValues.append(STATUS, status.toString());
    updateValues.append(ERROR_MESSAGES, errorMessages.toList());

    traceDao.updateById(id, updateValues, collectionName);
  }

  public void updateNumeroContrat(
      String id, String numeroContrat, String numeroAdherant, String collectionName) {
    BasicDBObject updateValues = new BasicDBObject(NUMERO_CONTRAT, numeroContrat);
    updateValues.append(UPDATE_DATE, LocalDateTime.now(ZoneOffset.UTC));
    updateValues.append(NUMERO_ADHERENT, numeroAdherant);

    traceDao.updateById(id, updateValues, collectionName);
  }

  public Page<TraceServicePrestation> getTraceForArl(
      String fileName, String firstTraceId, Pageable pageable) {
    return traceDao.getTraceForArl(fileName, firstTraceId, pageable);
  }

  public long deleteTracesByAMC(String amc) {
    return traceDao.deleteTracesByAMC(amc);
  }

  public long deleteTracesByFileName(String file) {
    return traceDao.deleteTracesByFileName(file);
  }
}
