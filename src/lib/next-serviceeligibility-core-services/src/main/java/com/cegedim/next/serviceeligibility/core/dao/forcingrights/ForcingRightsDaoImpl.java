package com.cegedim.next.serviceeligibility.core.dao.forcingrights;

import static com.cegedim.next.serviceeligibility.core.utils.Constants.CLIENT_TYPE_INSURER;
import static com.cegedim.next.serviceeligibility.core.utils.Constants.CLIENT_TYPE_OTP;
import static org.springframework.data.mongodb.core.query.Query.query;

import com.cegedim.next.serviceeligibility.core.dao.MongoGenericWithManagementScopeDao;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import com.cegedim.next.serviceeligibility.core.services.scopeManagement.AuthorizationScopeHandler;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

@Repository
public class ForcingRightsDaoImpl<T extends DocumentEntity>
    extends MongoGenericWithManagementScopeDao<T> implements ForcingRightsDao {
  public ForcingRightsDaoImpl(
      MongoTemplate mongoTemplate, AuthorizationScopeHandler authorizationScopeHandler) {
    super(mongoTemplate, authorizationScopeHandler);
  }

  private void addAmcCriteria(String amc, Criteria criteria) {
    applyIssuingCompanyFilter(amc, criteria, Constants.ID_DECLARANT, CLIENT_TYPE_OTP);
    applyIssuingCompanyFilter(amc, criteria, Constants.ID_DECLARANT, CLIENT_TYPE_INSURER, false);
  }

  @Override
  public List<ContractTP> findContratTP(String amc, String personNumber) {
    Criteria criteria = new Criteria();
    addAmcCriteria(amc.trim(), criteria);
    criteria.and(Constants.BENEFICIAIRES_NUMERO_PERSONNE).is(personNumber);
    return this.getMongoTemplate().find(query(criteria), ContractTP.class);
  }

  @Override
  public List<ContratAIV6> findServicePrestationV6(String idDeclarant, String numeroPersonne) {
    Criteria criteria = new Criteria();
    addAmcCriteria(idDeclarant.trim(), criteria);
    criteria.and(Constants.SERVICE_PRESTATION_ASSURES_IDENTITE_NUMERO).is(numeroPersonne);
    return this.getMongoTemplate().find(query(criteria), ContratAIV6.class);
  }
}
