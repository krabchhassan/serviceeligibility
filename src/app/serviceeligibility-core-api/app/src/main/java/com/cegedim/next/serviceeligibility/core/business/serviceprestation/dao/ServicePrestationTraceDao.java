package com.cegedim.next.serviceeligibility.core.business.serviceprestation.dao;

import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.ServicePrestationTrace;
import java.util.List;

/** Interface de la classe d'accès aux {@code ServicePrestation} de la base de donnees. */
public interface ServicePrestationTraceDao extends IMongoGenericDao<ServicePrestationTrace> {

  /**
   * Recherche dans la base de donnees les traces du service Prestation en fonction de l'id du
   * service Prestation
   *
   * @param contratAiId l'id du Service Prestation
   * @return les traces du Service Prestation
   */
  List<ServicePrestationTrace> findServicePrestationTrace(String contratAiId);

  /**
   * Recherche dans la base de donnees toutes les traces du service Prestation
   *
   * @return toutes les traces du Service Prestation
   */
  List<ServicePrestationTrace> getAll();
}
