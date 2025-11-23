package com.cegedim.next.serviceeligibility.core.bdd.backend.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.CircuitDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.CircuitDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.generic.GenericServiceImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.CircuitMapper;
import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Circuit;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/** Classe d'accès aux services lies aux {@code Circuit}. */
@Service("circuitService")
public class CircuitServiceImpl extends GenericServiceImpl<Circuit> implements CircuitService {

  @Autowired private CircuitMapper mapper;

  /**
   * public constructeur.
   *
   * @param circuitDao bean dao injecte
   */
  @Autowired
  public CircuitServiceImpl(@Qualifier("circuitDao") final IMongoGenericDao<Circuit> circuitDao) {
    super(circuitDao);
  }

  /**
   * Cast le genericDao.
   *
   * @return La dao de {@code Circuit}
   */
  @Override
  @ContinueSpan(log = "getCircuitDao")
  public CircuitDao getCircuitDao() {
    return ((CircuitDao) this.getGenericDao());
  }

  @Override
  @ContinueSpan(log = "findAllCircuits")
  public List<Circuit> findAllCircuits() {
    return this.getCircuitDao().findAllCircuits();
  }

  @Override
  @ContinueSpan(log = "findAllDtoCircuits")
  public List<CircuitDto> findAllDtoCircuits() {

    final List<CircuitDto> circuitsDto = new ArrayList<>();
    final List<Circuit> circuits = this.findAllCircuits();
    for (final Circuit circuit : circuits) {
      circuitsDto.add(this.mapper.entityToDto(circuit));
    }

    return circuitsDto;
  }

  @Override
  @ContinueSpan(log = "create CircuitDto")
  public void create(final CircuitDto dto) {
    final Circuit entity = this.mapper.dtoToEntity(dto);
    this.getCircuitDao().create(entity);
  }

  @ContinueSpan(log = "dropCollection Circuit")
  public void dropCollection() {
    this.getCircuitDao().dropCollection(Circuit.class);
  }
}
