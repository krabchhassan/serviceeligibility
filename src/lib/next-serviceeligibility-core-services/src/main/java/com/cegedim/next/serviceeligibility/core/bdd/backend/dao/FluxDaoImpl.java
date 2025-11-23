package com.cegedim.next.serviceeligibility.core.bdd.backend.dao;

import com.cegedim.next.serviceeligibility.core.dao.MongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Flux;
import com.cegedim.next.serviceeligibility.core.model.query.ParametresFlux;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.mongodb.client.result.UpdateResult;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationUpdate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/** Classe d'accès aux {@code Flux} de la base de donnees. */
@Repository("fluxDao")
public class FluxDaoImpl extends MongoGenericDao<Flux> implements FluxDao {

  public static final String DATE_EXECUTION = "dateExecution";

  public FluxDaoImpl(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  /*
   * (non-Javadoc)
   *
   * @see com.cegedimactiv.basededroit.office.backend.dao.FluxDao#
   * getTotalFluxByRequest(com.cegedimactiv.basededroit.domain.ParametresFlux)
   */
  @Override
  @ContinueSpan(log = "getTotalFluxByRequest")
  public Long getTotalFluxByRequest(final ParametresFlux parametresFlux) {
    final Criteria criteria = this.createSearchQueryForFlux(parametresFlux);

    final Query query = new Query(criteria);

    return this.getMongoTemplate().count(query, Flux.class);
  }

  /*
   * (non-Javadoc)
   *
   * @see com.cegedimactiv.basededroit.office.backend.dao.FluxDao#
   * findFluxByParameters(com.cegedimactiv.basededroit.domain.ParametresFlux)
   */
  @Override
  @ContinueSpan(log = "findFluxByParameters")
  public List<Flux> findFluxByParameters(final ParametresFlux parametresFlux) {

    final Criteria criteria = this.createSearchQueryForFlux(parametresFlux);

    // Pagination
    final int page = parametresFlux.getPosition();
    final int pageSize = parametresFlux.getNumberByPage();
    final Pageable pageableRequest = PageRequest.of(page, pageSize);

    final Query query =
        new Query(criteria)
            .with(Sort.by(Sort.Direction.DESC, DATE_EXECUTION))
            .with(pageableRequest);

    return this.getMongoTemplate().find(query, Flux.class);
  }

  private void updateCriteriaIs(final Criteria crit, final String name, final String param) {
    if (StringUtils.isNotBlank(param)) {
      crit.and(name).is(param);
    }
  }

  private void updateCriteriaRegex(final Criteria crit, final String name, final String param) {
    if (StringUtils.isNotBlank(param)) {
      crit.and(name)
          .regex(Pattern.compile(param.trim(), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE));
    }
  }

  private Criteria createSearchQueryForFlux(final ParametresFlux parametresFlux) {

    final Criteria criteria = Criteria.where("fichierEmis").is(parametresFlux.isFichierEmis());

    this.updateCriteriaIs(criteria, "processus", parametresFlux.getProcessus());

    this.updateCriteriaIs(criteria, "typeFichier", parametresFlux.getTypeFichier());

    this.updateCriteriaIs(criteria, "infoAMC.codeCircuit", parametresFlux.getCodeCircuit());

    this.updateCriteriaRegex(
        criteria, "infoAMC.codePartenaire", parametresFlux.getCodePartenaire());

    this.updateCriteriaIs(criteria, "infoAMC.emetteurDroits", parametresFlux.getEmetteur());

    if (StringUtils.isNotBlank(parametresFlux.getNumeroFichier())) {
      if (parametresFlux.isFichierEmis()) {
        criteria
            .and("infoFichierEmis.numeroFichier")
            .is(Long.valueOf(parametresFlux.getNumeroFichier()));
      } else {
        criteria
            .and("infoFichierRecu.numeroFichier")
            .is(Long.valueOf(parametresFlux.getNumeroFichier()));
      }
    }

    this.updateCriteriaRegex(criteria, "infoAMC.nomAMC", parametresFlux.getNomAmc());

    if (StringUtils.isNotBlank(parametresFlux.getIdDeclarant())) {
      criteria.orOperator(
          Criteria.where("idDeclarant").regex(parametresFlux.getIdDeclarant()),
          Criteria.where("infoAMC.numAMCEchange").regex(parametresFlux.getIdDeclarant()));
    }

    if (parametresFlux.getDateDebut() != null && parametresFlux.getDateFin() != null) {
      criteria.andOperator(
          Criteria.where(DATE_EXECUTION).lte(parametresFlux.getDateFin()),
          Criteria.where(DATE_EXECUTION).gte(parametresFlux.getDateDebut()));
    } else {
      if (parametresFlux.getDateDebut() != null) {
        criteria.and(DATE_EXECUTION).gte(parametresFlux.getDateDebut());
      }
      if (parametresFlux.getDateFin() != null) {
        criteria.and(DATE_EXECUTION).lte(parametresFlux.getDateFin());
      }
    }

    if (parametresFlux.isFichierEmis()) {
      this.updateCriteriaRegex(
          criteria, "infoFichierEmis.nomFichier", parametresFlux.getNomFichier());
    } else {
      this.updateCriteriaRegex(
          criteria, "infoFichierRecu.nomFichier", parametresFlux.getNomFichier());
    }

    return criteria;
  }

  @Override
  public long deleteByAMC(String amc) {
    Criteria criteria = Criteria.where(Constants.ID_DECLARANT).is(amc);
    return getMongoTemplate().remove(new Query(criteria), Flux.class).getDeletedCount();
  }

  @Override
  public long deleteByFileName(String file) {
    Criteria criteria =
        new Criteria()
            .andOperator(
                Criteria.where("fichierEmis").is(false),
                new Criteria()
                    .orOperator(
                        Criteria.where("infoFichierRecu.nomFichier").is(file),
                        Criteria.where("infoFichierRecu.nomFichierARL").is(file)));
    return getMongoTemplate().remove(new Query(criteria), Flux.class).getDeletedCount();
  }

  @Override
  public long replaceFileName(List<String> oldNames, String newName) {
    Criteria criteria = Criteria.where("infoFichierEmis.nomFichier").in(oldNames);

    AggregationOperation aggregationOperation =
        Aggregation.addFields()
            .addFieldWithValue("infoFichierEmis.nomFichierOrigine", "$infoFichierEmis.nomFichier")
            .addFieldWithValue("infoFichierEmis.nomFichier", newName)
            .build();

    UpdateResult result =
        getMongoTemplate()
            .updateMulti(
                new Query(criteria),
                AggregationUpdate.from(List.of(aggregationOperation)),
                Flux.class);
    return result.getMatchedCount();
  }
}
