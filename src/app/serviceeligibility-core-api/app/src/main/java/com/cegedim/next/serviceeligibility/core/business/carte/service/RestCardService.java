package com.cegedim.next.serviceeligibility.core.business.carte.service;

import com.cegedim.next.serviceeligibility.core.dao.CarteDematDao;
import com.cegedim.next.serviceeligibility.core.dao.CartePapierDao;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.model.entity.card.cartepapiereditique.CartePapierEditique;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class RestCardService {
  @Qualifier("carteDematDao")
  @Autowired
  private CarteDematDao dao;

  @Autowired private CartePapierDao cartePapierDao;

  @ContinueSpan(log = "create CarteDemat")
  public void create(CarteDemat card) {
    dao.create(card);
  }

  @ContinueSpan(log = "deleteAll CarteDemat")
  public void dropCollection() {
    dao.dropCollection(CarteDemat.class);
    cartePapierDao.dropCollection(CartePapierEditique.class);
  }

  @ContinueSpan(log = "findById CarteDemat")
  public CarteDemat findById(String id) {
    return dao.findById(id, CarteDemat.class);
  }

  @ContinueSpan(log = "findAll CarteDemat")
  public List<CarteDemat> findAll() {
    return dao.findAll(CarteDemat.class);
  }
}
