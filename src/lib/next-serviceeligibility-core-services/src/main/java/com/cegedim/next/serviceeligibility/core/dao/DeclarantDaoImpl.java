package com.cegedim.next.serviceeligibility.core.dao;

import static org.springframework.data.mongodb.core.aggregation.ArrayOperators.Filter;
import static org.springframework.data.mongodb.core.aggregation.ArrayOperators.In;
import static org.springframework.data.mongodb.core.aggregation.BooleanOperators.And;
import static org.springframework.data.mongodb.core.aggregation.ComparisonOperators.Eq;
import static org.springframework.data.mongodb.core.aggregation.ComparisonOperators.Lte;

import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import io.micrometer.tracing.annotation.ContinueSpan;
import jakarta.validation.ValidationException;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AddFieldsOperation;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository("declarantDaoImpl")
@RequiredArgsConstructor
public class DeclarantDaoImpl implements DeclarantDao {

  private final Logger logger = LoggerFactory.getLogger(DeclarantDaoImpl.class);

  private final MongoTemplate template;

  @Override
  @ContinueSpan(log = "isCompatible")
  public Boolean isCompatible(String idClientBO, String idDeclarant) {
    // CRITERIA
    Criteria criteria = new Criteria();
    criteria.andOperator(
        Criteria.where("idClientBO").is(idClientBO), Criteria.where(Constants.ID).is(idDeclarant));

    Query query = new Query(criteria);

    long nbDeclarant = template.count(query, Declarant.class);
    logger.debug(
        String.format(
            "%s déclarant(s) trouvé avec l'idClientBO : %s et l'AMC : %s",
            nbDeclarant, idClientBO, idDeclarant));
    return (nbDeclarant == 1);
  }

  @Override
  @ContinueSpan(log = "getIdClientBo")
  public String getIdClientBo(String idDeclarant) throws ValidationException {
    Declarant declarant = template.findById(idDeclarant, Declarant.class);
    if (declarant == null) {
      throw new ValidationException("Le déclarant " + idDeclarant + " n'existe pas !");
    } else if (StringUtils.isBlank(declarant.getIdClientBO())) {
      throw new ValidationException(
          "Le déclarant " + idDeclarant + " ne référence aucun idClientBO !");
    }
    return declarant.getIdClientBO();
  }

  @Override
  @ContinueSpan(log = "getAllDeclarantIDs")
  public List<String> getAllDeclarantIDs() {
    return template.findAll(Declarant.class).stream().map(Declarant::get_id).toList();
  }

  @Override
  public Declarant findById(String id) {
    return template.findById(id, Declarant.class);
  }

  @Override
  @ContinueSpan(log = "findAll Declarant")
  public List<Declarant> findAll() {
    return template.findAll(Declarant.class);
  }

  @Override
  @ContinueSpan(log = "findByAmcEchange Declarant")
  public Declarant findByAmcEchange(final String amcEchange) {
    // CRITERIA
    final Criteria criteria = Criteria.where("numerosAMCEchanges").is(amcEchange);

    // QUERY
    final Query queryDeclarant = Query.query(criteria);

    // RESULT
    return template.findOne(queryDeclarant, Declarant.class);
  }

  @Override
  @ContinueSpan(log = "getDeclarantsByCodeService")
  public List<Declarant> getDeclarantsByCodeService(
      List<String> codeServices, String couloirClient) {
    // Filtre la liste de pilotages pour ne garder que les pilotages dans
    // codeServices, actifs et ouverts avant aujourd'hui
    In isInService = In.arrayOf(codeServices).containsValue("$$item.codeService");
    Eq isOpen = Eq.valueOf("item.serviceOuvert").equalToValue(true);
    Lte isOpenBeforeToday = Lte.valueOf("item.dateOuverture").lessThanEqualToValue(new Date());

    And and = And.and(isInService, isOpen, isOpenBeforeToday);
    if (StringUtils.isNotBlank(couloirClient) && !"*".equals(couloirClient)) {
      Eq isCodeCouloir = Eq.valueOf("item.couloirClient").equalToValue(couloirClient);
      and = And.and(isInService, isOpen, isOpenBeforeToday, isCodeCouloir);
    }

    Filter filterPilotages = Filter.filter("pilotages").as("item").by(and);
    AddFieldsOperation addField =
        Aggregation.addFields().addField("pilotages").withValue(filterPilotages).build();

    final Criteria criteria = Criteria.where("pilotages.0").exists(true);

    final Aggregation agg = Aggregation.newAggregation(addField, Aggregation.match(criteria));
    return template
        .aggregate(agg, Constants.DECLARANTS_COLLECTION, Declarant.class)
        .getMappedResults();
  }

  @Override
  @ContinueSpan(log = "getDeclarantByCodeService")
  public Declarant getDeclarantByCodeService(String idDeclarant, List<String> codeServices) {
    // Filtre la liste de pilotages pour ne garder que les pilotages dans
    // codeServices, actifs et ouverts avant aujourd'hui
    In isInService = In.arrayOf(codeServices).containsValue("$$item.codeService");
    Eq isOpen = Eq.valueOf("item.serviceOuvert").equalToValue(true);
    Lte isOpenBeforeToday = Lte.valueOf("item.dateOuverture").lessThanEqualToValue(new Date());
    Filter filterPilotages =
        Filter.filter("pilotages").as("item").by(And.and(isInService, isOpen, isOpenBeforeToday));
    AddFieldsOperation addField =
        Aggregation.addFields().addField("pilotages").withValue(filterPilotages).build();

    final Criteria criteria =
        Criteria.where("pilotages.0").exists(true).and(Constants.ID).is(idDeclarant);

    final Aggregation agg = Aggregation.newAggregation(addField, Aggregation.match(criteria));
    return template
        .aggregate(agg, Constants.DECLARANTS_COLLECTION, Declarant.class)
        .getUniqueMappedResult();
  }

  @Override
  public void create(Declarant declarant) {
    template.save(declarant, Constants.DECLARANTS_COLLECTION);
  }

  @Override
  public void dropCollection() {
    template.remove(new Query(), Declarant.class);
  }
}
