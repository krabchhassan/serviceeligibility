package com.cegedim.next.serviceeligibility.core.cucumber.services;

import com.cegedim.next.serviceeligibility.core.model.kafka.Trace;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestServicePrestationService {
  private final MongoTemplate template;

  public void create(Object servicePrestation) {
    template.insert(servicePrestation, Constants.SERVICE_PRESTATION_COLLECTION);
  }

  public Trace getContractTrace(String id) {
    return template.findById(id, Trace.class, Constants.CONTRACT_TRACE);
  }

  public ContratAIV6 getContractByContractNumber(String numero) {
    final Query qryContrat = new Query();
    qryContrat.addCriteria(Criteria.where(Constants.NUMERO).is(numero));

    return template.findOne(qryContrat, ContratAIV6.class, Constants.SERVICE_PRESTATION_COLLECTION);
  }

  public List<ContratAIV6> getAllServicePrestation() {
    return template.findAll(ContratAIV6.class, Constants.SERVICE_PRESTATION_COLLECTION);
  }

  public void clearCollection() {
    template.remove(new Query(), Constants.SERVICE_PRESTATION_COLLECTION);
  }
}
