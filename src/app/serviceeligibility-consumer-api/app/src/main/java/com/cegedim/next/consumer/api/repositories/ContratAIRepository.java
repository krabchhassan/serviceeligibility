package com.cegedim.next.consumer.api.repositories;

import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@RequiredArgsConstructor
public class ContratAIRepository {

  private final MongoTemplate mongoTemplate;

  // Find contract based on criteria : return it
  public ContratAIV6 findBy(
      String idDeclarant, String numeroContrat, String dateNaissance, String rangNaissance) {
    // CRITERIA
    Criteria criteria = new Criteria();
    criteria.andOperator(
        Criteria.where("idDeclarant").is(idDeclarant),
        Criteria.where("numero").is(numeroContrat),
        Criteria.where("assures.identite.dateNaissance").is(dateNaissance),
        Criteria.where("assures.identite.rangNaissance").is(rangNaissance));

    Query query = new Query(criteria);

    return mongoTemplate.findOne(query, ContratAIV6.class);
  }

  public ContratAIV6 findBy(String idDeclarant, String numeroContrat, String numeroPersonne) {
    // CRITERIA
    Criteria criteria = new Criteria();
    criteria.andOperator(
        Criteria.where("idDeclarant").is(idDeclarant),
        Criteria.where("numero").is(numeroContrat),
        Criteria.where("assures.identite.numeroPersonne").is(numeroPersonne));

    Query query = new Query(criteria);

    return mongoTemplate.findOne(query, ContratAIV6.class);
  }
}
