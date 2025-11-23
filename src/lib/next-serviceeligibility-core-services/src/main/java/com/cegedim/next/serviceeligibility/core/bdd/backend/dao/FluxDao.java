package com.cegedim.next.serviceeligibility.core.bdd.backend.dao;

import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Flux;
import com.cegedim.next.serviceeligibility.core.model.query.ParametresFlux;
import java.util.List;

/** Interface de la classe d'accès aux {@code Flux} de la base de donnees. */
public interface FluxDao extends IMongoGenericDao<Flux> {

  /**
   * Renvoie en fonction des criteres passes en parametre la taille de la liste de Flux.
   *
   * @param requestFluxDto Les criteres
   * @return {@link Long}
   */
  Long getTotalFluxByRequest(ParametresFlux requestFluxDto);

  /**
   * Renvoie en fonction des criteres passes en parametre un objet FluxDto qui contient une liste de
   * Flux.
   *
   * @param parametresFlux Les criteres
   * @return {@link List<Flux>}
   */
  List<Flux> findFluxByParameters(ParametresFlux parametresFlux);

  long deleteByAMC(String amc);

  long deleteByFileName(String file);

  long replaceFileName(List<String> oldNames, String newName);
}
