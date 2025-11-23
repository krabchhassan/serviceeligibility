package com.cegedim.next.serviceeligibility.core.bdd.backend.generic;

import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.util.List;

/**
 * Interface genererique de la classe {@code genericServiceImpl}.
 *
 * @param <T> Le type de la classe entite manipulee.
 */
public interface GenericService<T extends DocumentEntity> {

  T find(Object id);

  List<T> findAll();

  IMongoGenericDao<T> getGenericDao();

  void setGenericDao(IMongoGenericDao<T> genericDao);
}
