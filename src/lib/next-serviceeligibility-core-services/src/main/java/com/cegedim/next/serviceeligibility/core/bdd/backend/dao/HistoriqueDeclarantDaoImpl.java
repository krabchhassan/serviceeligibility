package com.cegedim.next.serviceeligibility.core.bdd.backend.dao;

import com.cegedim.next.serviceeligibility.core.dao.MongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueDeclarant;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/** Classe d'accès aux {@code HistoriqueDeclarant} de la base de donnees. */
@Repository("historiqueDeclarantDao")
public class HistoriqueDeclarantDaoImpl extends MongoGenericDao<HistoriqueDeclarant>
    implements HistoriqueDeclarantDao {

  public HistoriqueDeclarantDaoImpl(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }
}
