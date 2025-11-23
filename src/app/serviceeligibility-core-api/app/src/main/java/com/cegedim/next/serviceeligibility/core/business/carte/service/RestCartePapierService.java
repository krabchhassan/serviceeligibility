package com.cegedim.next.serviceeligibility.core.business.carte.service;

import com.cegedim.next.serviceeligibility.core.dao.CartePapierDao;
import com.cegedim.next.serviceeligibility.core.model.entity.card.cartepapiereditique.CartePapierEditique;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestCartePapierService {

  @Autowired private CartePapierDao cartePapierDao;

  @ContinueSpan(log = "findAll CartePapier")
  public List<CartePapierEditique> findAll() {
    return cartePapierDao.findAll(CartePapierEditique.class);
  }

  @ContinueSpan(log = "deleteAll CartePapier")
  public void dropCollection() {
    cartePapierDao.dropCollection(CartePapierEditique.class);
  }

  @ContinueSpan(log = "create CartePapierEditique")
  public void create(CartePapierEditique cartePapierEditique) {
    cartePapierDao.create(cartePapierEditique);
  }

  @ContinueSpan(log = "findById CartePapierEditique")
  public CartePapierEditique findById(String id) {
    return cartePapierDao.findById(id, CartePapierEditique.class);
  }
}
