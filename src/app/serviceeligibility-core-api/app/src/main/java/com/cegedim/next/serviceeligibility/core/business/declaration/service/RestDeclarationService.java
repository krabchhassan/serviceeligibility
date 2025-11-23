package com.cegedim.next.serviceeligibility.core.business.declaration.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarationDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperDeclaration;
import com.cegedim.next.serviceeligibility.core.business.declaration.dao.DeclarationDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestDeclarationService {

  @Autowired private DeclarationDao dao;

  @Autowired private MapperDeclaration mapper;

  @ContinueSpan(log = "create Declaration")
  public void create(Declaration declaration) {
    dao.create(declaration);
  }

  @ContinueSpan(log = "findById Declaration")
  public Declaration findById(String id) {
    return dao.findById(id);
  }

  @ContinueSpan(log = "findAll Declaration")
  public List<Declaration> findAll() {
    return dao.findAll(Declaration.class);
  }

  @ContinueSpan(log = "findAllDto DeclarationDto")
  public List<DeclarationDto> findAllDto() {
    return mapper.entityListToDtoList(dao.findAll(Declaration.class), null, false, false, null);
  }

  @ContinueSpan(log = "removeAll Declaration")
  public void removeAll() {
    this.dao.removeAll();
  }
}
