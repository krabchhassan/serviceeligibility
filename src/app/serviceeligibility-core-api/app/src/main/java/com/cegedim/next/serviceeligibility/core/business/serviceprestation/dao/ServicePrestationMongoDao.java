package com.cegedim.next.serviceeligibility.core.business.serviceprestation.dao;

import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.ServicePrestationMongo;
import java.util.List;

/** Interface de la classe d'accès aux {@code ServicePrestation} de la base de donnees. */
public interface ServicePrestationMongoDao extends IMongoGenericDao<ServicePrestationMongo> {

  /**
   * Recherche dans la base de donnees un service Prestation en fonction de l'AMC
   *
   * @param idDeclarant l'AMC
   * @return le Service Prestation
   */
  List<ServicePrestationMongo> findServicePrestationMongo(String idDeclarant);
}
