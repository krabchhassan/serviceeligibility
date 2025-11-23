package com.cegedim.next.serviceeligibility.reprisebeneficiaires.services;

import static com.cegedim.next.serviceeligibility.core.job.utils.Constants.*;

import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduRepriseBenefs;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.mongodb.client.result.UpdateResult;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class RepriseDeclarationService {
  @Autowired private MongoTemplate mongoTemplate;

  private final Logger logger = LoggerFactory.getLogger(RepriseDeclarationService.class);

  @NewSpan
  public int processTp(Date dateReprise, CompteRenduRepriseBenefs compteRendu) {
    if (dateReprise != null) {
      logger.info("Updating declarations from Mongo");

      long updatedDeclarations = updateDeclarations(dateReprise);
      compteRendu.addContratsTpRepris(updatedDeclarations);

      return CODE_RETOUR_OK;
    }

    // Si dateReprise n'est pas renseignée on renvoie une erreur
    logger.error(
        "Error - To process declarations you need to enter a dateReprise - stopping process");
    return CODE_RETOUR_BAD_REQUEST;
  }

  private long updateDeclarations(Date dateReprise) {
    Query query = new Query(Criteria.where("effetDebut").gte(dateReprise));
    Update updateDefinition = new Update().inc("updateElastic", 1);
    UpdateResult result = mongoTemplate.updateMulti(query, updateDefinition, Declaration.class);

    return result.getModifiedCount();
  }
}
