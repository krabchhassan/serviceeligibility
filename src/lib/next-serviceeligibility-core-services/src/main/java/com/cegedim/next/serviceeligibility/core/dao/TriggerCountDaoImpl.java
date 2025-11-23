package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggerCount;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@RequiredArgsConstructor
public class TriggerCountDaoImpl implements TriggerCountDao {

  private final MongoTemplate template;

  @Override
  public void saveTriggerCount(String triggerId, int nbTriggerUnitaire) {
    TriggerCount triggerCount = new TriggerCount();
    triggerCount.setId(triggerId);
    triggerCount.setTotal(nbTriggerUnitaire);
    triggerCount.setCount(0);
    template.save(triggerCount);
  }

  @Override
  public void incCountTriggerUnitaire(String triggerId) {
    Query q = new Query(new Criteria().and(Constants.ID).is(triggerId));
    Update update = new Update().inc(Constants.COUNT_TRIGGER_UNITAIRE, 1);
    this.template.upsert(q, update, TriggerCount.class);
  }

  @Override
  public void updateLockTriggerUnitaire(String triggerId) {
    Query q = new Query(new Criteria().and(Constants.ID).is(triggerId));
    Update update = new Update().set(Constants.STATUS, 0);
    template.upsert(q, update, TriggerCount.class);
  }

  @Override
  public long findAndLock(String triggerId) {
    Query q = new Query();
    q.addCriteria(Criteria.where(Constants.ID).is(triggerId).and(Constants.STATUS).is(0));
    Update updateDefinition = new Update().set(Constants.STATUS, 1);
    UpdateResult updateResult = this.template.updateMulti(q, updateDefinition, TriggerCount.class);
    return updateResult.getModifiedCount();
  }

  @Override
  public TriggerCount getTriggerCount(String triggerId) {
    Query q = new Query();
    q.addCriteria(Criteria.where(Constants.ID).is(triggerId));
    return template.findOne(q, TriggerCount.class);
  }
}
