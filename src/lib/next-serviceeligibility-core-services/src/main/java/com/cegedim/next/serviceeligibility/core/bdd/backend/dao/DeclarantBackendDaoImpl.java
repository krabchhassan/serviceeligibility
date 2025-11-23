package com.cegedim.next.serviceeligibility.core.bdd.backend.dao;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.cegedim.next.serviceeligibility.core.dao.MongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.domain.DeclarantsEchange;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/** Classe d'accès aux {@code Declarant} de la base de donnees. */
@Repository("declarantBackendDao")
public class DeclarantBackendDaoImpl extends MongoGenericDao<Declarant>
    implements DeclarantBackendDao {

  private static final String NUMEROS_AMC_ECHANGES = "numerosAMCEchanges";
  private static final String NUMERO_PREFECTORAL = "numeroPrefectoral";

  public DeclarantBackendDaoImpl(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  @Override
  @ContinueSpan(log = "findById Declarant")
  public Declarant findById(final String id) {
    Declarant declarant = null;
    if (StringUtils.isNotBlank(id)) {
      final Criteria criteria = Criteria.where(NUMERO_PREFECTORAL).is(id.trim());
      final Query query = Query.query(criteria);
      declarant = this.getMongoTemplate().findOne(query, Declarant.class);
    }
    return declarant;
  }

  @Override
  @ContinueSpan(log = "findByCriteria Declarant")
  public List<Declarant> findByCriteria(
      final String id, final String nom, final String couloir, final String service)
      throws UnsupportedEncodingException {

    // CRITERIA
    final Criteria criteria = new Criteria();
    if (StringUtils.isBlank(id)
        && StringUtils.isBlank(nom)
        && StringUtils.isBlank(couloir)
        && StringUtils.isBlank(service)) {
      return new ArrayList<>();
    } else {
      if (!StringUtils.isBlank(id)) {
        criteria.and(NUMERO_PREFECTORAL).regex(id.trim());
      }
      if (!StringUtils.isBlank(nom)) {
        String decodedNom = URLDecoder.decode(nom.trim(), "UTF-8");
        String[] parts = decodedNom.split(" - ");
        if (parts.length == 2) {
          String numeroPrefectoral = parts[0];
          String libelle = parts[1];
          criteria.and(NUMERO_PREFECTORAL).regex(numeroPrefectoral.trim());
          criteria
              .and("nom")
              .regex(
                  Pattern.compile(
                      Pattern.quote(libelle.trim()),
                      Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE));
        } else {
          criteria
              .and("nom")
              .regex(
                  Pattern.compile(
                      Pattern.quote(decodedNom.trim()),
                      Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE));
        }
      }
    }
    if (!StringUtils.isBlank(couloir)) {
      criteria
          .and("pilotages.couloirClient")
          .regex(
              Pattern.compile(
                  Pattern.quote(couloir.trim()), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE));
    }
    if (!StringUtils.isBlank(service)) {
      criteria.and("pilotages.codeService").is(service);
    }

    // QUERY
    final Query query = Query.query(criteria);

    // RESULT
    final List<Declarant> declarants = this.getMongoTemplate().find(query, Declarant.class);
    Collections.sort(declarants);
    return declarants;
  }

  @Override
  @ContinueSpan(log = "findAllDeclarants")
  public List<Declarant> findAllDeclarants() {

    // CRITERIA
    final Criteria criteria = new Criteria();

    // QUERY
    final Query query = new Query(criteria).with(Sort.by(Sort.Direction.ASC, NUMERO_PREFECTORAL));

    // Recherche dans la base de donnees tous les declarants mais declarants
    // ALLEGES : Seulement 3 proprietes remontent de la base : numero AMC,
    // nom AMC et code partenaire.
    query.fields().include(NUMERO_PREFECTORAL);
    query.fields().include("nom");
    query.fields().include("codePartenaire");

    // RESULT
    return this.getMongoTemplate().find(query, Declarant.class);
  }

  @Override
  @ContinueSpan(log = "findByUser Declarant")
  public List<Declarant> findByUser(final String user, int page, final int pageSize) {
    List<Declarant> declarants = new ArrayList<>();
    // Convertir le chiffre page en vrai page 1,2,3,4,... par page/pageSize
    if (page > 0) {
      page = page / pageSize;
    }
    if (StringUtils.isNotBlank(user)) {
      // CRITERIA
      final Criteria criteria = new Criteria();
      criteria.orOperator(
          Criteria.where("userCreation").is(user),
          Criteria.where("userModification").is(user.trim()));

      // QUERY
      final Pageable pageableRequest = PageRequest.of(page, pageSize);
      final Query query =
          new Query(criteria)
              .with(Sort.by(Sort.Direction.DESC, "dateModification"))
              .with(pageableRequest);

      // RESULT
      declarants = this.getMongoTemplate().find(query, Declarant.class);
    }
    return declarants;
  }

  @Override
  @ContinueSpan(log = "findTotalDeclarantsByUser")
  public long findTotalDeclarantsByUser(final String user) {

    Long totalDeclarants = 0L;

    if (StringUtils.isNotBlank(user)) {
      // CRITERIA
      final Criteria criteria = new Criteria();
      criteria.orOperator(
          Criteria.where("userCreation").is(user),
          Criteria.where("userModification").is(user.trim()));

      final Query query = new Query(criteria);
      totalDeclarants = this.getMongoTemplate().count(query, Declarant.class);
    }

    return totalDeclarants;
  }

  @Override
  @ContinueSpan(log = "findAllDeclarantsEchanges")
  public DeclarantsEchange findAllDeclarantsEchanges() {

    final Aggregation aggregation =
        newAggregation(
            match(Criteria.where(NUMEROS_AMC_ECHANGES).exists(true)),
            unwind("$numerosAMCEchanges"),
            group().addToSet(NUMEROS_AMC_ECHANGES).as(NUMEROS_AMC_ECHANGES),
            project(NUMEROS_AMC_ECHANGES));

    final AggregationResults<DeclarantsEchange> results =
        this.getMongoTemplate().aggregate(aggregation, "declarants", DeclarantsEchange.class);
    return results.getUniqueMappedResult();
  }
}
