package com.cegedim.next.serviceeligibility.core.bdd.backend.dao;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

import com.cegedim.next.serviceeligibility.core.dao.MongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/** Classe d'accès aux {@code Declaration} de la base de donnees. */
@Repository("declarationBackendDao")
public class DeclarationBackendDaoImpl extends MongoGenericDao<Declaration>
    implements DeclarationBackendDao {

  public static final String ID_DECLARANT = "idDeclarant";
  public static final String CONTRAT_NUMERO = "contrat.numero";
  public static final String NUMEROPERSONNE = "numeroPersonne";
  public static final String BENEFICIAIRE_NUMEROPERSONNE = "beneficiaire.numeroPersonne";
  public static final String ID_ORIGINE = "idOrigine";
  public static final String CONTRAT = "contrat";
  public static final String NUMEROCONTRAT = "numeroContrat";
  public static final String CODE_ETAT = "codeEtat";
  public static final String EFFET_DEBUT = "effetDebut";
  public static final String DECLARATION = "declarations";
  public static final String BENEF_NUMERO_PERSONNE = "beneficiaire.numeroPersonne";

  public DeclarationBackendDaoImpl(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  @Override
  @ContinueSpan(log = "findDeclarationsByCriteria (1 param)")
  public List<Declaration> findDeclarationsByCriteria(
      String idDeclarant, String numeroPersonne, int limit) {

    final Aggregation aggregation =
        this.createSearchQueryForDeclaration(idDeclarant, numeroPersonne, limit);

    final AggregationResults<Declaration> results =
        this.getMongoTemplate().aggregate(aggregation, DECLARATION, Declaration.class);

    return results.getMappedResults();
  }

  @Override
  @ContinueSpan(log = "findDeclarationsByBenefContrat")
  public List<Declaration> findDeclarationsByBenefContrat(
      final String idDeclarant,
      final String numeroPersonne,
      final String numeroContrat,
      Integer limit) {
    final Criteria criteria = Criteria.where(ID_DECLARANT).is(idDeclarant);
    criteria.and(BENEF_NUMERO_PERSONNE).is(numeroPersonne);
    criteria.and(CONTRAT_NUMERO).is(numeroContrat);
    Query query = Query.query(criteria).with(Sort.by(Sort.Direction.DESC, "_id"));
    if (limit != null) {
      query.limit(limit);
    }
    return this.getMongoTemplate().find(query, Declaration.class);
  }

  @Override
  @ContinueSpan(log = "countDeclarationByIdDeclarantAndNumeroPersonne")
  public Long countDeclarationByCriteria(String idDeclarant, String numeroPersonne) {
    final Criteria criteria = this.createCriteria(idDeclarant, numeroPersonne);
    final Query query = Query.query(criteria);
    return this.getMongoTemplate().count(query, Declaration.class);
  }

  /**
   * Creer le query pour rechercher les déclarations en fonction de l'id declarant et le numero de
   * personne
   *
   * @param idDeclarant id du déclarant
   * @param numeroPersonne * numero personne
   * @return query
   */
  private Aggregation createSearchQueryForDeclaration(
      String idDeclarant, String numeroPersonne, int limit) {

    final Criteria criteria = this.createCriteria(idDeclarant, numeroPersonne);

    ////////////
    // MATCH1 //
    ////////////
    final MatchOperation match = Aggregation.match(criteria);

    // PROJECT WE NEED TO BREAK THE INDEX :(
    final AggregationOperation projectOperationBreakIndex;
    projectOperationBreakIndex =
        Aggregation.project()
            .andExpression("_id")
            .as(ID_ORIGINE)
            .andExpression(ID_DECLARANT)
            .as(ID_DECLARANT)
            .andExpression(Constants.BENEF_IN_DECLARATION)
            .as(Constants.BENEF_IN_DECLARATION)
            .andExpression(CONTRAT)
            .as(CONTRAT)
            .andExpression(CODE_ETAT)
            .as(CODE_ETAT)
            .andExpression(EFFET_DEBUT)
            .as(EFFET_DEBUT);
    //////////
    // SORT //
    //////////
    final AggregationOperation sortOperation =
        Aggregation.sort(Sort.Direction.DESC, EFFET_DEBUT).and(Sort.Direction.DESC, ID_ORIGINE);
    ///////////
    // GROUP //
    ///////////
    final AggregationOperation groupOperation;
    groupOperation =
        Aggregation.group(BENEFICIAIRE_NUMEROPERSONNE, CONTRAT_NUMERO, ID_DECLARANT)
            .first(CONTRAT)
            .as(CONTRAT)
            .first(Constants.BENEF_IN_DECLARATION)
            .as(Constants.BENEF_IN_DECLARATION)
            .first(ID_ORIGINE)
            .as(ID_ORIGINE)
            .first(CODE_ETAT)
            .as(CODE_ETAT)
            .first(ID_DECLARANT)
            .as(ID_DECLARANT);
    /////////////
    // PROJECT //
    /////////////
    final AggregationOperation projectOperation;
    projectOperation =
        Aggregation.project()
            .andExpression(ID_ORIGINE)
            .as("_id")
            .andExpression(ID_DECLARANT)
            .as(ID_DECLARANT)
            .andExpression(Constants.BENEF_IN_DECLARATION)
            .as(Constants.BENEF_IN_DECLARATION)
            .andExpression(CONTRAT)
            .as(CONTRAT)
            .andExpression(CODE_ETAT)
            .as(CODE_ETAT);
    /////////////////
    // AGGREGATION //
    /////////////////
    final List<AggregationOperation> aggregationOperations = new ArrayList<>();

    aggregationOperations.add(match);
    aggregationOperations.add(projectOperationBreakIndex);

    aggregationOperations.add(sortOperation);
    aggregationOperations.add(groupOperation);
    aggregationOperations.add(projectOperation);
    aggregationOperations.add(limit(limit));

    return newAggregation(aggregationOperations)
        .withOptions(Aggregation.newAggregationOptions().allowDiskUse(true).build());
  }

  private Criteria createCriteria(String idDeclarant, String numeroPersonne) {
    final Criteria criteria = new Criteria();
    criteria.and(ID_DECLARANT).is(idDeclarant);
    criteria.and(BENEF_NUMERO_PERSONNE).is(numeroPersonne);
    return criteria;
  }
}
