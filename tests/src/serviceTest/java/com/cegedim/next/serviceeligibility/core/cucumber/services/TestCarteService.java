package com.cegedim.next.serviceeligibility.core.cucumber.services;

import com.cegedim.next.serviceeligibility.core.dao.CarteDematDao;
import com.cegedim.next.serviceeligibility.core.dao.CartePapierDao;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.model.entity.card.cartepapiereditique.CartePapierEditique;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestCarteService {

  private final CarteDematDao carteDematDao;
  private final CartePapierDao cartePapierDao;

  public List<CarteDemat> findAll() {
    return carteDematDao.findAll(CarteDemat.class);
  }

  public List<CartePapierEditique> findAllCartesPapierEditiques() {
    // une cartePapierEditique contient une cartePapier
    return cartePapierDao.findAll(CartePapierEditique.class);
  }

  public CarteDemat findOneById(String id) {
    return carteDematDao.findById(id, CarteDemat.class);
  }
}
