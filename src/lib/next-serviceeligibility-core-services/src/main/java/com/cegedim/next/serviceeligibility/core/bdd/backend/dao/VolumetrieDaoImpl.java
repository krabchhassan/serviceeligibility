package com.cegedim.next.serviceeligibility.core.bdd.backend.dao;

import com.cegedim.next.serviceeligibility.core.dao.MongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Volumetrie;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/** Classe d'accès aux {@code Volumetrie} de la base de donnees. */
@Repository("volumetrieDao")
public class VolumetrieDaoImpl extends MongoGenericDao<Volumetrie> implements VolumetrieDao {

  private static final String ID_DECLARANT = "idDeclarant";
  private static final String DATE_EFFET = "dateEffet";

  public VolumetrieDaoImpl(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  @Override
  @ContinueSpan(log = "findLastVolumetries")
  public List<Volumetrie> findLastVolumetries() {

    final Iterable<String> declarants =
        this.getMongoTemplate().getCollection("declarants").distinct("_id", String.class);
    final List<Volumetrie> lastVolumetries = new ArrayList<>();

    for (final String item : declarants) {
      lastVolumetries.addAll(this.findLastVolumetriesPerAmc(item));
    }

    return lastVolumetries;
  }

  private List<Volumetrie> findLastVolumetriesPerAmc(final String declarant) {

    List<Volumetrie> lastVolumetries = new ArrayList<>();

    // Trouve la derniere dateEffet dans la collection volumetrie

    // CRITERIA dateEffet
    final Criteria criteriaDateEffet = Criteria.where(ID_DECLARANT).is(declarant);
    // QUERY dateEffet
    final Query queryDateEffet =
        new Query(criteriaDateEffet).with(Sort.by(Sort.Direction.DESC, DATE_EFFET));

    queryDateEffet.limit(1);
    queryDateEffet.fields().include(DATE_EFFET);

    final Volumetrie volumetrieAvecDateEffet =
        this.getMongoTemplate().findOne(queryDateEffet, Volumetrie.class);

    if (volumetrieAvecDateEffet != null) {
      // CRITERIA
      final Criteria criteria =
          Criteria.where(DATE_EFFET).is(volumetrieAvecDateEffet.getDateEffet());

      // QUERY
      final Query query = new Query();
      query.addCriteria(new Criteria().andOperator(criteria, criteriaDateEffet));

      lastVolumetries = this.getMongoTemplate().find(query, Volumetrie.class);
    }

    return lastVolumetries;
  }
}
