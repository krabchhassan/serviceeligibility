package com.cegedim.next.serviceeligibility.core.bdd.backend.dao;

import static com.cegedim.next.serviceeligibility.core.bdd.backend.dao.DeclarationBackendDaoImpl.*;

import com.cegedim.next.serviceeligibility.core.dao.MongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.RestitutionCarte;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.mongodb.client.ClientSession;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/** Classe d'accès aux {@code RestitutionCarte} de la base de donnees. */
@Repository("restitutionCarteDao")
public class RestitutionCarteDaoImpl extends MongoGenericDao<RestitutionCarte>
    implements RestitutionCarteDao {

  public RestitutionCarteDaoImpl(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  @Override
  @ContinueSpan(log = "findRestitutionByIdDeclarantBenefContrat")
  public List<RestitutionCarte> findRestitutionByIdDeclarantBenefContrat(
      final String idDeclarant,
      final String numeroPersonne,
      final String numeroContrat,
      final String numeroAdherent,
      final Date dateEffet,
      Integer limit) {
    final Query query =
        Query.query(
            getCriteriaRestitutionCarte(
                idDeclarant, numeroPersonne, numeroContrat, numeroAdherent, dateEffet));
    if (limit != null) {
      query.limit(limit);
    }
    query.with(
        Sort.by(
            List.of(
                new Sort.Order(Sort.Direction.DESC, Constants.EFFET_DEBUT),
                new Sort.Order(Sort.Direction.DESC, Constants.ID))));
    return this.getMongoTemplate().find(query, RestitutionCarte.class);
  }

  @Override
  @ContinueSpan(log = "getRestitutionsIdsByIdDeclarantBenefContrat")
  public List<String> getRestitutionsIdsByIdDeclarantBenefContrat(
      String idDeclarant,
      String numeroPersonne,
      String numeroContrat,
      String numeroAdherent,
      Integer limit) {
    Aggregation aggregation =
        Aggregation.newAggregation(
            Aggregation.match(
                getCriteriaRestitutionCarte(
                    idDeclarant, numeroPersonne, numeroContrat, numeroAdherent, null)),
            Aggregation.limit(limit),
            Aggregation.sort(
                Sort.by(
                    List.of(
                        new Sort.Order(Sort.Direction.DESC, Constants.EFFET_DEBUT),
                        new Sort.Order(Sort.Direction.DESC, Constants.ID)))));
    List<RestitutionCarte> restitutionCartes =
        this.getMongoTemplate()
            .aggregate(aggregation, RestitutionCarte.class, RestitutionCarte.class)
            .getMappedResults();
    return restitutionCartes.stream().map(DocumentEntity::get_id).toList();
  }

  @Override
  @ContinueSpan(log = "findById RestitutionCarte")
  public RestitutionCarte findById(String id) {
    return this.getMongoTemplate().findById(id, RestitutionCarte.class);
  }

  @ContinueSpan(log = "saveRestitutionCarte")
  public RestitutionCarte saveRestitutionCarte(RestitutionCarte restitutionCarte) {
    return this.getMongoTemplate().save(restitutionCarte, Constants.RESTITUTION_CARTE_COLLECTION);
  }

  private static Criteria getCriteriaRestitutionCarte(
      String idDeclarant,
      String numeroPersonne,
      String numeroContrat,
      String numeroAdherent,
      final Date dateEffet) {
    final Criteria criteria = Criteria.where(ID_DECLARANT).is(idDeclarant);
    criteria.and(NUMEROPERSONNE).is(numeroPersonne);
    if (numeroContrat != null) {
      criteria.and(NUMEROCONTRAT).is(numeroContrat);
    }
    if (numeroAdherent != null) {
      criteria.and(Constants.NUMERO_ADHERENT).is(numeroAdherent);
    }
    if (dateEffet != null) {
      criteria.and(EFFET_DEBUT).lte(dateEffet);
    }
    return criteria;
  }

  @ContinueSpan(log = "saveRestitutionCarte")
  public RestitutionCarte saveRestitutionCarte(
      RestitutionCarte restitutionCarte, ClientSession session) {
    if (session != null) {
      return this.getMongoTemplate()
          .withSession(session)
          .save(restitutionCarte, Constants.RESTITUTION_CARTE_COLLECTION);
    } else {
      return this.getMongoTemplate().save(restitutionCarte, Constants.RESTITUTION_CARTE_COLLECTION);
    }
  }
}
