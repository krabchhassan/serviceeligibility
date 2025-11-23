package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContratsAMCexclues;
import io.micrometer.tracing.annotation.ContinueSpan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository("contrats-AMCexclues")
public class ContratsAMCexcluesDaoImpl implements ContratsAMCexcluesDao {

  @Autowired MongoTemplate template;

  @Override
  @ContinueSpan(log = "getContratsAmcExclues (no param)")
  public ContratsAMCexclues getContratsAmcExclues() {
    return getContratsAmcExclues("contrats-AMCexclues");
  }

  @Override
  @ContinueSpan(log = "getContratsAmcExclues (1 param)")
  public ContratsAMCexclues getContratsAmcExclues(String collection) {
    return template.findOne(new Query(), ContratsAMCexclues.class, collection);
  }

  @Override
  @ContinueSpan(log = "upsert ContratsAMCexclues (1 param)")
  public ContratsAMCexclues upsert(ContratsAMCexclues contratsAMCexclues) {
    return template.save(contratsAMCexclues);
  }

  @Override
  @ContinueSpan(log = "upsert ContratsAMCexclues (2 params)")
  public ContratsAMCexclues upsert(ContratsAMCexclues contratsAMCexclues, String collection) {
    return template.save(contratsAMCexclues, collection);
  }

  @Override
  @ContinueSpan(log = "deleteAll ContratsAMCexclues")
  public void deleteAll() {
    template.remove(ContratsAMCexclues.class);
  }
}
