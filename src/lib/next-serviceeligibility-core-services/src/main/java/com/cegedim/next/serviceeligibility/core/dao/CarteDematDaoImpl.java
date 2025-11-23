package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.model.query.CriteresRechercheCarteDemat;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.ClientSession;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Repository("carteDematDao")
public class CarteDematDaoImpl extends MongoGenericDao<CarteDemat> implements CarteDematDao {

  public CarteDematDaoImpl(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  @Override
  @ContinueSpan(log = "findCartesDematByDeclarantAndContrat")
  public List<CarteDemat> findCartesDematByDeclarantAndContrat(
      final String idDeclarant, final String numeroContrat) {
    final Criteria criteria =
        Criteria.where(Constants.ID_DECLARANT)
            .is(idDeclarant)
            .and(Constants.CONTRAT_NUMERO)
            .is(numeroContrat)
            .and(Constants.IS_LAST_CARTE_DEMAT)
            .is(true);

    return getMongoTemplate().find(new Query(criteria), CarteDemat.class);
  }

  @Override
  @ContinueSpan(log = "findAllCartesDematByDeclarantAndContrat")
  public List<CarteDemat> findAllCartesDematByDeclarantAndAMCContrat(
      final String idDeclarant, final String numeroContrat, final String numeroPersonne) {
    final String amcContrat = idDeclarant + "-" + numeroContrat;
    final Criteria criteria =
        Criteria.where(Constants.ID_DECLARANT)
            .is(idDeclarant)
            .and(Constants.AMC_CONTRAT)
            .is(amcContrat)
            .and(Constants.CARTE_NUMERO_PERSONNE)
            .is(numeroPersonne);

    Query query = new Query(criteria);
    query.with(Sort.by(Sort.Direction.DESC, Constants.PERIODE_DEBUT));
    query.with(Sort.by(Sort.Direction.DESC, Constants.DATE_CREATION));

    return getMongoTemplate().find(query, CarteDemat.class);
  }

  @Override
  @ContinueSpan(log = "findCartesDematByDeclarantAndDateExec")
  public int findCartesDematByDeclarantAndDateExec(
      final String idDeclarant, final String dateExec) {
    final Criteria criteria =
        Criteria.where(Constants.ID_DECLARANT)
            .is(idDeclarant)
            .and(Constants.PERIODE_DEBUT)
            .lte(dateExec)
            .and(Constants.PERIODE_FIN)
            .gte(dateExec)
            .and(Constants.IS_LAST_CARTE_DEMAT)
            .is(true);

    return (int) getMongoTemplate().count(new Query(criteria), CarteDemat.class);
  }

  @Override
  @ContinueSpan(log = "findCarteByDeclarantAndAmcContrat")
  public List<CarteDemat> findCarteByDeclarantAndAmcContrat(
      final String idDeclarant, final String numeroContrat, String idDeclaration) {
    final String amcContrat = idDeclarant + "-" + numeroContrat;
    final Criteria criteria =
        Criteria.where(Constants.ID_DECLARANT)
            .is(idDeclarant)
            .and(Constants.AMC_CONTRAT)
            .is(amcContrat)
            .and(Constants.ID_DECLARATION)
            .in(idDeclaration);

    Query query = new Query(criteria);
    query.with(Sort.by(Sort.Direction.DESC, Constants.ID));
    return getMongoTemplate().find(query, CarteDemat.class);
  }

  @Override
  @ContinueSpan(log = "findCarteByDeclarantAndNumeroPersonne")
  public CarteDemat findCarteByDeclarantAndNumeroPersonne(
      final String idDeclarant, final String numeroPersonne) {
    final Criteria criteria =
        Criteria.where(Constants.ID_DECLARANT)
            .is(idDeclarant)
            .and(Constants.CARTE_NUMERO_PERSONNE)
            .is(numeroPersonne);

    return getMongoTemplate().findOne(new Query(criteria), CarteDemat.class);
  }

  @Override
  @ContinueSpan(log = "findCartesDematByCriteria")
  public List<CarteDemat> findCartesDematByCriteria(
      final CriteresRechercheCarteDemat criteresRechercheCarteDemat) {

    final String amcContrat =
        criteresRechercheCarteDemat.getNumeroAMC()
            + "-"
            + criteresRechercheCarteDemat.getNumeroContrat();

    // CRITERIA
    final Criteria criteria =
        Criteria.where(Constants.AMC_CONTRAT)
            .is(amcContrat)
            .and("periodeFin")
            .gte(criteresRechercheCarteDemat.getDateReference())
            .and(Constants.IS_LAST_CARTE_DEMAT)
            .is(true);

    // QUERY
    final Query queryCartes = Query.query(criteria);
    return this.getMongoTemplate().find(queryCartes, CarteDemat.class);
  }

  @Override
  @ContinueSpan(log = "findCartesDematByAdherent")
  public List<CarteDemat> findCartesDematByAdherent(
      final CriteresRechercheCarteDemat criteresRechercheCarteDemat) {

    // CRITERIA
    final Criteria criteria =
        Criteria.where("idDeclarant")
            .is(criteresRechercheCarteDemat.getNumeroAMC())
            .and("contrat.numeroAdherent")
            .is(criteresRechercheCarteDemat.getNumeroContrat())
            .and("periodeFin")
            .gte(criteresRechercheCarteDemat.getDateReference())
            .and(Constants.IS_LAST_CARTE_DEMAT)
            .is(true);

    // QUERY
    final Query queryCartes = Query.query(criteria);
    return this.getMongoTemplate().find(queryCartes, CarteDemat.class);
  }

  @Override
  public void updateIsLastCarteAll(List<CarteDemat> carteDemats, ClientSession session) {
    BulkOperations bulkOperations;
    if (session != null) {
      bulkOperations =
          getMongoTemplate()
              .withSession(session)
              .bulkOps(BulkOperations.BulkMode.UNORDERED, CarteDemat.class);
    } else {
      bulkOperations =
          getMongoTemplate().bulkOps(BulkOperations.BulkMode.UNORDERED, CarteDemat.class);
    }
    List<Pair<Query, Update>> upserts = new ArrayList<>();
    for (CarteDemat carteDemat : carteDemats) {
      Query query = Query.query(Criteria.where(Constants.ID).is(carteDemat.get_id()));
      Update update = new Update();
      update.set(Constants.IS_LAST_CARTE_DEMAT, carteDemat.getIsLastCarteDemat());
      update.set(Constants.DATE_MODIFICATION, new Date());
      upserts.add(Pair.of(query, update));
    }

    if (!upserts.isEmpty()) {
      bulkOperations.upsert(upserts);
      bulkOperations.execute();
    }
  }

  @Override
  public int insertAll(List<CarteDemat> demats, ClientSession session) {
    BulkOperations bulkInsertion =
        getMongoTemplate()
            .withSession(session)
            .bulkOps(
                BulkOperations.BulkMode.UNORDERED,
                CarteDemat.class,
                Constants.CARTES_DEMAT_COLLECTION);
    bulkInsertion.insert(demats);
    BulkWriteResult result = bulkInsertion.execute();
    return result.getInsertedCount();
  }

  @Override
  public List<CarteDemat> getLastCartesByAmcContrats(String amcContracts) {
    Criteria criteria =
        Criteria.where(Constants.AMC_CONTRAT)
            .is(amcContracts)
            .and(Constants.IS_LAST_CARTE_DEMAT)
            .is(true);
    return getMongoTemplate().find(new Query(criteria), CarteDemat.class);
  }

  @Override
  public long deleteByAMC(String amc) {
    Criteria criteria = Criteria.where(Constants.ID_DECLARANT).is(amc);
    return getMongoTemplate().remove(new Query(criteria), CarteDemat.class).getDeletedCount();
  }

  @Override
  public long deleteByDeclaration(String declaration) {
    Criteria criteria = Criteria.where(Constants.ID_DECLARATION).is(declaration);
    return getMongoTemplate().remove(new Query(criteria), CarteDemat.class).getDeletedCount();
  }
}
