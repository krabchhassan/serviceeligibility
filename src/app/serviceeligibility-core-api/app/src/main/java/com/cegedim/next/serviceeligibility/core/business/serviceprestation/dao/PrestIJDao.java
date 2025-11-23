package com.cegedim.next.serviceeligibility.core.business.serviceprestation.dao;

import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.kafka.prestij.PrestIJ;
import java.util.List;

/** Interface de la classe d'accès aux {@code ServicePrestIJ} de la base de donnees. */
public interface PrestIJDao extends IMongoGenericDao<PrestIJ> {

  List<PrestIJ> findServicePrestIJ(String idDeclarant);
}
