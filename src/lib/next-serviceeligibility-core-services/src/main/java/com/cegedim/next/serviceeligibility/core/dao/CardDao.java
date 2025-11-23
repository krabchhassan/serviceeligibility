package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.entity.card.CardRequest;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import java.util.List;

public interface CardDao {
  List<CarteDemat> findCartesDematFromRequest(CardRequest request);
}
