package com.cegedim.next.serviceeligibility.core.business.serviceprestation.dao;

import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.ServicePrestIJTrace;
import java.util.List;

/** Interface de la classe d'accès aux {@code PrestIJ} de la base de donnees. */
public interface ServicePrestIJTraceDao extends IMongoGenericDao<ServicePrestIJTrace> {

  /**
   * Recherche dans la base de donnees les traces du service PrestIJ en fonction de l'id du service
   * PrestIJ
   *
   * @param contratAiId l'id du Service PrestIJ
   * @return les traces du Service PrestIJ
   */
  List<ServicePrestIJTrace> findServicePrestIJTrace(String contratAiId);
}
