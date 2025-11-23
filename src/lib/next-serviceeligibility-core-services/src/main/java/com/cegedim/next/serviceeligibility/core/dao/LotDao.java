package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.bobb.Lot;
import java.util.List;

public interface LotDao {
  Lot getById(String id);

  List<Lot> getListByIds(List<String> ids);

  List<Lot> getListByIdsForRenewal(List<String> ids);

  Lot getByCode(String codeLot);

  void deleteAllLots();

  List<Lot> findByGT(String guaranteeCode, String insurerCode);
}
