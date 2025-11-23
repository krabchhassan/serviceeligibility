package com.cegedim.next.serviceeligibility.core.bdd.backend.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.FluxDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.flux.FluxDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.flux.FluxInfoDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.flux.ParametresFluxDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.generic.GenericServiceImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.MapperFlux;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.MapperParametresFlux;
import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Flux;
import com.cegedim.next.serviceeligibility.core.model.query.ParametresFlux;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Classe d'accès aux services lies aux {@code Flux}.
 *
 * @author cgdm
 */
@Service("fluxService")
public class FluxServiceImpl extends GenericServiceImpl<Flux> implements FluxService {
  @Autowired private MapperFlux mapperFlux;

  @Autowired private MapperParametresFlux mapperParametresFlux;

  /**
   * Constructeur {@link FluxServiceImpl}
   *
   * @param fluxDao dao injection
   */
  @Autowired
  public FluxServiceImpl(@Qualifier("fluxDao") final IMongoGenericDao<Flux> fluxDao) {
    super(fluxDao);
  }

  @Override
  @ContinueSpan(log = "getFluxDao")
  public FluxDao getFluxDao() {
    return ((FluxDao) this.getGenericDao());
  }

  @Override
  @ContinueSpan(log = "getFlux")
  public FluxDto getFlux(final ParametresFluxDto paramFluxDto) {
    final FluxDto fluxDto = new FluxDto();
    final ParametresFlux parametresFlux = this.mapperParametresFlux.dtoToEntity(paramFluxDto);

    Long totauxFlux;
    if (paramFluxDto.isNewSearch()) {
      totauxFlux = this.getFluxDao().getTotalFluxByRequest(parametresFlux);
      fluxDto.setTotalFlux(totauxFlux);
    }

    final List<Flux> listFluxDto = this.getFluxDao().findFluxByParameters(parametresFlux);

    final List<FluxInfoDto> listfluxInfoDto = this.mapperFlux.entityListToDtoList(listFluxDto);

    fluxDto.setFluxInfo(listfluxInfoDto);

    return fluxDto;
  }

  @Override
  @ContinueSpan(log = "create")
  public void create(Flux flux) {
    this.getFluxDao().create(flux);
  }

  @ContinueSpan(log = "deleteAll Flux")
  public void dropCollection() {
    this.getFluxDao().dropCollection(Flux.class);
  }

  @ContinueSpan(log = "update MouvementEmis Flux")
  public boolean updateMouvementEmisFlux(ParametresFluxDto paramFluxDto, int nbErrors) {
    ParametresFlux parametresFlux = this.mapperParametresFlux.dtoToEntity(paramFluxDto);
    final List<Flux> listFluxDto = this.getFluxDao().findFluxByParameters(parametresFlux);
    if (CollectionUtils.isEmpty(listFluxDto)) {
      return false;
    }

    for (Flux flux : listFluxDto) {
      flux.getInfoFichierEmis().setMouvementEmis(nbErrors);
      getFluxDao().update(flux);
    }
    return true;
  }

  @ContinueSpan(log = "update nomFichier Flux")
  public long updateNomFichierEmisFlux(List<String> nomFichierSources, String nomFichierUpdated) {
    return this.getFluxDao().replaceFileName(nomFichierSources, nomFichierUpdated);
  }
}
