package com.cegedim.next.serviceeligibility.core.bdd.backend.dao;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ParametresPrestationDto;
import com.cegedim.next.serviceeligibility.core.dao.MongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.ParametreBdd;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/** Classe d'accès aux {@code ParametreBdd} de la base de donnees. */
@Repository("parametreBddDao")
public class ParametreBddDaoImpl extends MongoGenericDao<ParametreBdd> implements ParametreBddDao {

  private static final String PRESTATIONS = "prestations";
  private static final String CODE = "code";
  private static final String DOMAINE = "listeValeurs.domaines.codeDomaine";
  private static final String LISTE_VALEURS = "listeValeurs";
  private static final String PARAMETRES = "parametres";

  public ParametreBddDaoImpl(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  @Override
  @ContinueSpan(log = "findParametres")
  public ParametreBdd findParametres(final String codeParametre) {
    if (!StringUtils.isBlank(codeParametre)) {
      final Criteria criteria = Criteria.where(CODE).is(codeParametre);
      final Query query = Query.query(criteria);

      return this.getMongoTemplate().findOne(query, ParametreBdd.class);
    }
    return null;
  }

  @Override
  @ContinueSpan(log = "findPrestationsByDomain")
  public List<ParametresPrestationDto> findPrestationsByDomain(final String codeDomaine) {
    if (!StringUtils.isBlank(codeDomaine)) {
      final Aggregation agg =
          Aggregation.newAggregation(
              Aggregation.match(Criteria.where(CODE).is(PRESTATIONS)),
              Aggregation.unwind(LISTE_VALEURS),
              Aggregation.match(Criteria.where(DOMAINE).is(codeDomaine)),
              Aggregation.project(
                  "listeValeurs.code", "listeValeurs.libelle", "listeValeurs.domaines"));
      List<ParametresPrestationDto> parametre = null;
      parametre =
          this.getMongoTemplate()
              .aggregate(agg, PARAMETRES, ParametresPrestationDto.class)
              .getMappedResults();
      return parametre;
    }
    return new ArrayList<>();
  }
}
