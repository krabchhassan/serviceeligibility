package com.cegedim.next.beneficiary.worker.service;

import com.cegedim.next.beneficiary.worker.dao.BeneficiaryDao;
import com.cegedim.next.serviceeligibility.core.model.kafka.BenefAI;
import com.cegedim.next.serviceeligibility.core.model.kafka.Trace;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class RestBeneficiaryService {

  @Autowired private MongoTemplate template;

  @Autowired private BeneficiaryDao dao;

  public Trace getBenefTrace(String id) {
    return template.findById(id, Trace.class, Constants.BENEF_TRACE);
  }

  public Trace getDeclarationTrace(String id) {
    return template.findById(id, Trace.class, Constants.DECLARATION_TRACE);
  }

  public BenefAI getBeneficiary(
      String idDeclarant,
      String numeroAdherent,
      String numeroPersonne,
      String dateNaissance,
      String rangNaissance) {
    return dao.getBeneficiary(
        idDeclarant, numeroAdherent, numeroPersonne, dateNaissance, rangNaissance);
  }
}
