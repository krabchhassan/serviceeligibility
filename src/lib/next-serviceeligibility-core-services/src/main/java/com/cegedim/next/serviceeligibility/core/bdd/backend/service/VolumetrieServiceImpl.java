package com.cegedim.next.serviceeligibility.core.bdd.backend.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.VolumetrieDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.volumetrie.VolumetrieDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.generic.GenericServiceImpl;
import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Volumetrie;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/** Classe d'accès aux services lies aux {@code Volumetrie}. */
@Service("volumetrieService")
public class VolumetrieServiceImpl extends GenericServiceImpl<Volumetrie>
    implements VolumetrieService {

  /**
   * public constructeur.
   *
   * @param volumetrieDao bean dao injecte
   */
  @Autowired
  public VolumetrieServiceImpl(
      @Qualifier("volumetrieDao") final IMongoGenericDao<Volumetrie> volumetrieDao) {
    super(volumetrieDao);
  }

  /**
   * Cast le genericDao.
   *
   * @return La dao de {@code Volumetrie}
   */
  @Override
  @ContinueSpan(log = "getVolumetrieDao")
  public VolumetrieDao getVolumetrieDao() {
    return ((VolumetrieDao) this.getGenericDao());
  }

  @Override
  @ContinueSpan(log = "findLastVolumetries")
  public List<VolumetrieDto> findLastVolumetries() {
    final List<Volumetrie> volumetries = this.getVolumetrieDao().findLastVolumetries();
    final List<VolumetrieDto> volumetriesDtos = new ArrayList<>();

    if (volumetries != null) {
      for (final Volumetrie volumetrie : volumetries) {
        final VolumetrieDto volumetrieDto = new VolumetrieDto();

        // Mapper les valeurs disponibles dans les valeurs DTO :
        // amc, declarations, personnes, personnesDroitsOuverts
        // Il faut remplir le Code Partenaire et Libelle AMC dans
        // VolumetrieDto plus tard.
        volumetrieDto.setAmc(volumetrie.getIdDeclarant());
        volumetrieDto.setDeclarations(volumetrie.getDeclarations());
        volumetrieDto.setPersonnes(volumetrie.getPersonnes());
        volumetrieDto.setPersonnesDroitsOuverts(volumetrie.getPersonnesDroitsOuverts());

        // personnesDroitsFermes = personnes - personnesDroitsOuverts
        if (volumetrie.getPersonnes() != null && volumetrie.getPersonnesDroitsOuverts() != null) {
          volumetrieDto.setPersonnesDroitsFermes(
              volumetrie.getPersonnes() - volumetrie.getPersonnesDroitsOuverts());
        }
        volumetriesDtos.add(volumetrieDto);
      }
    }
    return volumetriesDtos;
  }
}
