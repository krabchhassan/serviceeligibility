package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.entity.card.cartepapiereditique.CartePapierEditique;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.mongodb.client.ClientSession;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Repository("cartePapierDao")
public class CartePapierDaoImpl extends MongoGenericDao<CartePapierEditique>
    implements CartePapierDao {

  public CartePapierDaoImpl(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  @Override
  public Collection<CartePapierEditique> insertAll(
      List<CartePapierEditique> papiers, ClientSession session) {
    return getMongoTemplate().withSession(session).insertAll(papiers);
  }

  @Override
  public Stream<CartePapierEditique> getAllCards(String identifiant) {
    Criteria criteria = Criteria.where("internal." + Constants.IDENTIFIANT).is(identifiant);
    AggregationOperation match = Aggregation.match(criteria);
    final Aggregation aggregation =
        Aggregation.newAggregation(match)
            .withOptions(Aggregation.newAggregationOptions().allowDiskUse(true).build());
    return getMongoTemplate()
        .aggregateStream(
            aggregation, Constants.CARTES_PAPIER_COLLECTION, CartePapierEditique.class);
  }

  @Override
  public long deleteByAMC(String amc) {
    Criteria criteria = Criteria.where("cartePapier." + Constants.NUMERO_AMC).is(amc);
    return getMongoTemplate()
        .remove(new Query(criteria), CartePapierEditique.class)
        .getDeletedCount();
  }

  @Override
  public long deleteByDeclaration(String declaration) {
    Criteria criteria =
        Criteria.where("internal.traceExtractionConso.idDeclaration").is(declaration);
    return getMongoTemplate()
        .remove(new Query(criteria), CartePapierEditique.class)
        .getDeletedCount();
  }
}
