package com.cegedim.next.prestij.worker.service;

import com.cegedim.next.prestij.worker.utils.Constants;
import com.cegedim.next.serviceeligibility.core.model.kafka.Trace;
import com.cegedim.next.serviceeligibility.core.model.kafka.TraceStatus;
import com.mongodb.BasicDBObject;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service("traceServiceWorkerPrestIJ")
public class TraceService {
  private static final String ERROR_MESSAGES = "errorMessages";
  private static final String ID = "_id";
  private static final String SOURCE_OBJECT_ID = "sourceObjectId";
  private static final String SET = "$set";
  private static final String UPDATE_DATE = "updateDate";
  private static final String STATUS = "status";

  @Autowired MongoTemplate template;

  public String createTrace(String body, String collectionName) {
    Trace trace = new Trace();
    trace.setMessage(body);
    trace.setDateCreation(LocalDateTime.now(ZoneOffset.UTC));
    trace.setStatus(TraceStatus.ReceivedFromKafka);
    trace = template.save(trace, collectionName);
    return trace.getId();
  }

  public void updateStatus(String id, TraceStatus status, String collectionName) {
    BasicDBObject updateValues = new BasicDBObject(STATUS, status.toString());
    updateValues.append(UPDATE_DATE, LocalDateTime.now(ZoneOffset.UTC));

    template
        .getCollection(collectionName)
        .updateOne(new BasicDBObject(ID, new ObjectId(id)), new BasicDBObject(SET, updateValues));
  }

  public void completeTrace(String id, TraceStatus status, String prestIJId) {
    BasicDBObject updateValues = new BasicDBObject(STATUS, status.toString());
    updateValues.append(UPDATE_DATE, LocalDateTime.now(ZoneOffset.UTC));
    updateValues.append(SOURCE_OBJECT_ID, prestIJId);

    template
        .getCollection(Constants.PRESTIJ_TRACE)
        .updateOne(new BasicDBObject(ID, new ObjectId(id)), new BasicDBObject(SET, updateValues));
  }

  public void updateStatusError(
      String id, TraceStatus status, List<String> errorMessages, String collectionName) {
    BasicDBObject updateValues = new BasicDBObject(STATUS, status.toString());
    updateValues.append(ERROR_MESSAGES, errorMessages);
    template
        .getCollection(collectionName)
        .updateOne(new BasicDBObject(ID, new ObjectId(id)), new BasicDBObject(SET, updateValues));
  }
}
