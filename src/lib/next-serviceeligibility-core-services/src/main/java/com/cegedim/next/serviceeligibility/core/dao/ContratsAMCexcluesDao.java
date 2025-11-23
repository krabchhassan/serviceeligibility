package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContratsAMCexclues;

public interface ContratsAMCexcluesDao {

  ContratsAMCexclues getContratsAmcExclues();

  ContratsAMCexclues getContratsAmcExclues(String collection);

  ContratsAMCexclues upsert(ContratsAMCexclues contratsAMCexclues);

  ContratsAMCexclues upsert(ContratsAMCexclues contratsAMCexclues, String collection);

  void deleteAll();
}
