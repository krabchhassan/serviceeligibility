package com.cegedim.next.serviceeligibility.core.business.declarant.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarantDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperDeclarant;
import com.cegedim.next.serviceeligibility.core.dao.DeclarantDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/** Classe d'accès aux services lies aux {@code Declarant}. */
@Service("declarantServiceApi")
public class DeclarantServiceImpl implements DeclarantService {

  /** Bean permettant le mapping du declarant. */
  @Autowired private MapperDeclarant mapperDeclarant;

  @Autowired private DeclarantDao dao;

  @Override
  @Cacheable(value = "declarantCache", key = "#id")
  @ContinueSpan(log = "findById Declarant")
  public Declarant findById(final String id) {
    return dao.findById(id);
  }

  @Override
  @ContinueSpan(log = "findAll Declarant")
  public List<Declarant> findAll() {
    return dao.findAll();
  }

  @Override
  @ContinueSpan(log = "findAllDto DeclarantDto")
  public List<DeclarantDto> findAllDto() {
    return this.mapperDeclarant.entityListToDtoList(this.findAll(), null, false, false, null);
  }

  @Override
  @ContinueSpan(log = "getAmcRecherche DeclarantDto")
  public DeclarantDto getAmcRecherche(final String numAMC) {
    return mapDeclarantForAmcRecherche(numAMC, getDeclarantAmcRecherche(numAMC));
  }

  @Override
  @Cacheable(value = "declarantCache", key = "#numAMC")
  @ContinueSpan(log = "getDeclarantAmcRecherche Declarant")
  public Declarant getDeclarantAmcRecherche(String numAMC) {
    Declarant decl = dao.findById(numAMC);
    if (decl == null) {
      decl = dao.findByAmcEchange(numAMC);
    }
    return decl;
  }

  @Override
  @ContinueSpan(log = "mapDeclarantForAmcRecherche DeclarantDto")
  public DeclarantDto mapDeclarantForAmcRecherche(String numAMC, Declarant declarant) {
    DeclarantDto dto = null;
    if (declarant != null) {
      dto = this.mapperDeclarant.entityToDto(declarant, null, false, false, numAMC);
      if (StringUtils.isBlank(numAMC)) {
        dto.setNumeroPrefectoral(declarant.get_id());
      } else {
        dto.setNumeroPrefectoral(numAMC);
      }
    }

    return dto;
  }
}
