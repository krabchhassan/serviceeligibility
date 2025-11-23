package com.cegedim.next.serviceeligibility.core.bdd.backend.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.CircuitDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.CircuitDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.generic.GenericService;
import com.cegedim.next.serviceeligibility.core.model.entity.Circuit;
import java.util.List;

/** Interface de la classe d'accès aux services lies aux {@code Circuit}. */
public interface CircuitService extends GenericService<Circuit> {

  /**
   * @return La DAO des circuits.
   */
  CircuitDao getCircuitDao();

  /**
   * Recherche dans la base de donnees tous les circuits.
   *
   * @return liste des circuits trouves.
   */
  List<Circuit> findAllCircuits();

  /**
   * Recherche dans la base de donnees tous les circuits.
   *
   * @return liste des circuits trouves.
   */
  List<CircuitDto> findAllDtoCircuits();

  void create(CircuitDto circuit);

  void dropCollection();
}
