package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.model.entity.ServiceDroitsDCLBEN;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import io.micrometer.tracing.annotation.ContinueSpan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class ServiceDroitsDCLBENService {

  @Autowired MongoTemplate template;

  @ContinueSpan(log = "getServiceDroitsDCLBEN")
  public ServiceDroitsDCLBEN getServiceDroitsDCLBEN() {
    Criteria criteria = Criteria.where("code").is(Constants.AIGUILLAGE);

    Query query = new Query(criteria);

    return template.findOne(query, ServiceDroitsDCLBEN.class, Constants.SERVICEDROITS);
  }
}
