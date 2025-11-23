package com.cegedim.next.serviceeligibility.almerys608.dao;

import com.cegedim.next.serviceeligibility.almerys608.model.ProduitsAlmerysExclus;
import com.cegedim.next.serviceeligibility.core.dao.MongoGenericDao;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class ProduitsAlmerysExclusDaoImpl extends MongoGenericDao<ProduitsAlmerysExclus>
    implements ProduitsAlmerysExclusDao {

  private static final String ID_DECLARANT = "idDeclarant";
  private static final String CRITERE_SECONDAIRE_DETAILLE = "critereSecondaireDetaille";

  public ProduitsAlmerysExclusDaoImpl(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  public List<ProduitsAlmerysExclus> findByKey(
      String idDeclarant, String critereSecondaireDetaille) {
    Query q = new Query();
    q.addCriteria(
        Criteria.where(ID_DECLARANT)
            .is(idDeclarant)
            .and(CRITERE_SECONDAIRE_DETAILLE)
            .is(critereSecondaireDetaille));
    return getMongoTemplate().find(q, ProduitsAlmerysExclus.class);
  }
}
