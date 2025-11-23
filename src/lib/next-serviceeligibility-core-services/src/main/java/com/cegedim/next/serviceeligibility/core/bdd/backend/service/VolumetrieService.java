package com.cegedim.next.serviceeligibility.core.bdd.backend.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.VolumetrieDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.volumetrie.VolumetrieDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.generic.GenericService;
import com.cegedim.next.serviceeligibility.core.model.entity.Volumetrie;
import java.util.List;

/** Interface de la classe d'accès aux services lies aux {@code Volumetrie}. */
public interface VolumetrieService extends GenericService<Volumetrie> {

  /**
   * @return La DAO des volumetries.
   */
  VolumetrieDao getVolumetrieDao();

  /**
   * Recherche dans la base de donnees les dernieres volumetries apres la derniere execution du
   * batch.
   *
   * @return liste des dernieres volumetries Dto trouves.
   */
  List<VolumetrieDto> findLastVolumetries();
}
