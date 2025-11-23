package com.cegedim.next.serviceeligibility.core.bdd.backend.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.FluxDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.flux.FluxDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.flux.ParametresFluxDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.generic.GenericService;
import com.cegedim.next.serviceeligibility.core.model.entity.Flux;
import java.util.List;

/**
 * Interface de la classe d'accès aux services lies aux {@code Declarant}.
 *
 * @author cgdm
 */
public interface FluxService extends GenericService<Flux> {

  /**
   * @return La DAO Flux.
   */
  FluxDao getFluxDao();

  /**
   * Renvoie en fonction des criteres passes en parametre un objet FluxDto qui contient une liste de
   * Flux et la taille de la liste.
   *
   * @param parametresFluxDto Les criteres
   * @return {@link FluxDto}
   */
  FluxDto getFlux(ParametresFluxDto parametresFluxDto);

  void create(Flux flux);

  void dropCollection();

  boolean updateMouvementEmisFlux(ParametresFluxDto paramFluxDto, int nbErrors);

  long updateNomFichierEmisFlux(List<String> nomFichierSources, String nomFichierUpdated);
}
