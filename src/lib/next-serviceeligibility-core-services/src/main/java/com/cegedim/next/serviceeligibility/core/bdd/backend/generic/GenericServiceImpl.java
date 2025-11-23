package com.cegedim.next.serviceeligibility.core.bdd.backend.generic;

import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Classe mere de toutes les classes de la couche service.
 *
 * @param <T> Le type d'entitee manipulee.
 */
@Service("genericService")
public class GenericServiceImpl<T extends DocumentEntity> implements GenericService<T> {

  private IMongoGenericDao<T> genericDao;
  private Class<T> clazz;

  @Autowired
  public GenericServiceImpl(@Qualifier("mongoGenericDao") final IMongoGenericDao<T> genericDao) {
    this.genericDao = genericDao;
  }

  @Override
  @ContinueSpan(log = "find mongo generic")
  public T find(final Object object) {
    final String id = object.toString();
    return this.genericDao.findById(id, this.clazz);
  }

  @Override
  @ContinueSpan(log = "findAll mongo generic")
  public List<T> findAll() {
    return this.genericDao.findAll(this.clazz);
  }

  @Override
  @ContinueSpan(log = "getGenericDao")
  public IMongoGenericDao<T> getGenericDao() {
    return this.genericDao;
  }

  @Override
  @ContinueSpan(log = "setGenericDao")
  public void setGenericDao(final IMongoGenericDao<T> genericDao) {
    this.genericDao = genericDao;
  }
}
