package com.cegedim.next.serviceeligibility.core.business.declarant.service;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import io.micrometer.tracing.annotation.ContinueSpan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class RestContratService {

  @Autowired private MongoTemplate template;

  @ContinueSpan(log = "find contrat by id")
  public ContractTP findById(String id) {
    return template.findById(id, ContractTP.class);
  }
}
