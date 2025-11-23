package com.cegedim.next.serviceeligibility.core.bdd.backend.dao;

import com.cegedim.next.serviceeligibility.core.dao.MongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueTranscodage;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/** Classe d'accès aux {@code HistoriqueTranscodage} de la base de donnees. */
@Repository("historiqueTranscodageDao")
public class HistoriqueTranscodageDaoImpl extends MongoGenericDao<HistoriqueTranscodage>
    implements HistoriqueTranscodageDao {

  public HistoriqueTranscodageDaoImpl(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }
}
