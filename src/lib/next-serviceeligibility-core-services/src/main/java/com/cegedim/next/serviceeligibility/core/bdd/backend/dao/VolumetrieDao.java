package com.cegedim.next.serviceeligibility.core.bdd.backend.dao;

import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Volumetrie;
import java.util.List;

/** Interface de la classe d'accès aux {@code Volumetrie} de la base de donnees. */
public interface VolumetrieDao extends IMongoGenericDao<Volumetrie> {

  /**
   * Recherche dans la base de donnees les dernieres volumetries apres la derniere execution du
   * batch.
   *
   * @return liste des dernieres volumetries trouves.
   */
  List<Volumetrie> findLastVolumetries();
}
