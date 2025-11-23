package com.cegedim.next.serviceeligibility.core.cucumber.services;

import com.cegedim.next.serviceeligibility.core.dao.DeclarationDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TestDeclarationService {

  private final DeclarationDao declarationDao;

  public void removeAll() {
    declarationDao.removeAll();
  }

  public Declaration createDeclaration(Declaration declaration) {
    return declarationDao.createDeclaration(declaration, null);
  }

  public List<Declaration> findAll() {
    return declarationDao.findAll();
  }
}
