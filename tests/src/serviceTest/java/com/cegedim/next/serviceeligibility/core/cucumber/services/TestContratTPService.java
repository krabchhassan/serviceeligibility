package com.cegedim.next.serviceeligibility.core.cucumber.services;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestContratTPService {
  private final MongoTemplate template;

  public void create(ContractTP contrat) {
    template.insert(contrat, Constants.CONTRATS_COLLECTION_NAME);
  }

  public void dropCollection() {
    template.remove(new Query(), Constants.CONTRATS_COLLECTION_NAME);
  }

  public List<ContractTP> findAll() {
    return template.findAll(ContractTP.class);
  }
}
