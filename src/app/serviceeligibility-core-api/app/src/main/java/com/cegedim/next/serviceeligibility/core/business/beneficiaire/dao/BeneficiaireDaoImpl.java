package com.cegedim.next.serviceeligibility.core.business.beneficiaire.dao;

import com.cegedim.next.serviceeligibility.core.dao.MongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Beneficiaire;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.mongodb.client.result.UpdateResult;
import io.micrometer.tracing.annotation.ContinueSpan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/** Classe d'accès aux {@code Beneficiaire} de la base de donnees. */
@Repository("beneficiaireDao")
public class BeneficiaireDaoImpl extends MongoGenericDao<Beneficiaire> implements BeneficiaireDao {

  public BeneficiaireDaoImpl(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  @Override
  @ContinueSpan(log = "findByIdDeclarant Beneficiaire")
  public Beneficiaire findByIdDeclarant(final String idDeclarant) {
    // CRITERIA
    final Criteria criteria = Criteria.where("amc.idDeclarant").is(idDeclarant);

    // QUERY
    final Query queryBeneficiaire = Query.query(criteria);

    // RESULT
    return this.getMongoTemplate().findOne(queryBeneficiaire, Beneficiaire.class);
  }

  @Override
  @ContinueSpan(log = "removeService")
  public Long removeService(final String idDeclarant, final String service) {
    // CRITERIA
    final Criteria criteria = Criteria.where("amc.idDeclarant").is(idDeclarant);

    // QUERY
    final Query queryBeneficiaire = Query.query(criteria);

    // UPDATE
    final Update update = new Update();
    update.pull("services", service);

    final UpdateResult ur =
        this.getMongoTemplate()
            .updateMulti(queryBeneficiaire, update, Constants.BENEFICIAIRE_COLLECTION_NAME);
    return ur.getModifiedCount();
  }
}
