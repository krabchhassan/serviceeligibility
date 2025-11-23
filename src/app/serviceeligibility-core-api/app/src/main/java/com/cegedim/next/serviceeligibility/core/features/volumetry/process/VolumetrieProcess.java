package com.cegedim.next.serviceeligibility.core.features.volumetry.process;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.volumetrie.VolumetrieDto;
import java.util.List;

/** Interface du processus general de dialogue avec la base de droits - services param. */
public interface VolumetrieProcess {

  /**
   * Recherche dans la base de donnees les dernieres volumetries apres la derniere execution du
   * batch.
   *
   * @return liste des dernieres volumetries Dto trouves.
   */
  List<VolumetrieDto> getLastVolumetries();

  byte[] getFilteredVolumetrieAsXLS(String amc, String codePartenaire, String authHeader);

  public String getVolumetrieCriteriaTitle(String amc, String codePartenaire);
}
