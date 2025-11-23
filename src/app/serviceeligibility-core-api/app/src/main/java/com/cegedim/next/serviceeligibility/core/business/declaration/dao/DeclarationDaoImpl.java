package com.cegedim.next.serviceeligibility.core.business.declaration.dao;

import com.cegedim.next.serviceeligibility.core.dao.MongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/** Classe d'accès aux {@code Declaration} de la base de donnees. */
@Repository("declarationDao")
public class DeclarationDaoImpl extends MongoGenericDao<Declaration> implements DeclarationDao {

  private static final String[] QUALIFICATION_CONTRAT_CARTE_FAMILLE = {"B", "C"};

  public DeclarationDaoImpl(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  @Override
  @ContinueSpan(log = "findById Declaration")
  public Declaration findById(final String id) {
    return this.findById(id, Declaration.class);
  }

  @Override
  @ContinueSpan(log = "findAll Declaration")
  public List<Declaration> findAll() {
    return this.findAll(Declaration.class);
  }

  /** PARTIE WS CONSULTATION */
  @Override
  @ContinueSpan(log = "findDeclarationsByBeneficiaire")
  public List<Declaration> findDeclarationsByBeneficiaire(
      final String dateNaissance,
      final String rangNaissance,
      final String nirBeneficiaire,
      final String cleNirBeneficiare,
      final String numAmc,
      final boolean isRechercheCarteFamille,
      final boolean isSearchByIdPrefectoral,
      final boolean isSearchByAdherent,
      final String numeroAdherent) {
    // CRITERIA
    final Criteria criteria =
        Criteria.where("beneficiaire.dateNaissance")
            .is(dateNaissance)
            .and("beneficiaire.rangNaissance")
            .is(rangNaissance);
    if (isSearchByAdherent) {
      criteria.and("contrat.numeroAdherent").is(numeroAdherent);
    } else {
      criteria.orOperator(
          Criteria.where("beneficiaire.nirBeneficiaire")
              .is(nirBeneficiaire)
              .and("beneficiaire.cleNirBeneficiaire")
              .is(cleNirBeneficiare),
          Criteria.where("beneficiaire.nirOd1")
              .is(nirBeneficiaire)
              .and("beneficiaire.cleNirOd1")
              .is(cleNirBeneficiare),
          Criteria.where("beneficiaire.nirOd2")
              .is(nirBeneficiaire)
              .and("beneficiaire.cleNirOd2")
              .is(cleNirBeneficiare));
    }
    if (isSearchByIdPrefectoral) {
      criteria.and("idDeclarant").is(numAmc);
    } else {
      criteria.and("contrat.numAMCEchange").is(numAmc);
    }

    if (isRechercheCarteFamille) {
      criteria.andOperator(
          Criteria.where("contrat.qualification")
              .in(Arrays.asList(QUALIFICATION_CONTRAT_CARTE_FAMILLE)));
    }

    // QUERY
    final Query queryDeclarations = Query.query(criteria);
    queryDeclarations.with(Sort.by(Sort.Direction.DESC, "dateModification"));

    // RESULT
    return this.getMongoTemplate().find(queryDeclarations, Declaration.class);
  }

  @Override
  @ContinueSpan(log = "findDeclarationsByNumeroContrat")
  public List<Declaration> findDeclarationsByNumeroContrat(
      final String numAmc, final String numeroContrat, final boolean isSearchByIdPrefectoral) {

    // CRITERIA
    final Criteria criteria =
        Criteria.where("contrat.numero")
            .is(numeroContrat)
            .and("contrat.qualification")
            .in(Arrays.asList(QUALIFICATION_CONTRAT_CARTE_FAMILLE));

    if (isSearchByIdPrefectoral) {
      criteria.and("idDeclarant").is(numAmc);
    } else {
      criteria.and("contrat.numAMCEchange").is(numAmc);
    }
    // QUERY
    final Query queryDeclarations = Query.query(criteria);
    queryDeclarations.with(Sort.by(Sort.Direction.DESC, "effetDebut"));

    return this.getMongoTemplate().find(queryDeclarations, Declaration.class);
  }

  @Override
  @ContinueSpan(log = "removeAll Declaration")
  public void removeAll() {
    this.getMongoTemplate().findAllAndRemove(new Query(), Constants.DECLARATION_COLLECTION);
  }
}
