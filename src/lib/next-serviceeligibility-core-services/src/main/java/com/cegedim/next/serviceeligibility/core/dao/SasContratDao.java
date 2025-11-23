package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.SasContrat;
import java.util.List;

public interface SasContratDao {

  SasContrat save(SasContrat sas);

  SasContrat getByFunctionalKey(String idDeclarant, String numeroContrat, String numeroAdherent);

  SasContrat getByServicePrestationId(String idServicePrestation);

  List<SasContrat> getByIdTrigger(String idTrigger);

  void delete(String id);

  long deleteByAmc(String amc);

  void deleteAll();

  void updateRecycling(String sasId, boolean recycling);

  void abandonTrigger(String idTrigger);

  void removeEmptySas();

  List<String> getByPersonNumber(String numeroPersonne);

  List<SasContrat> getByContractNumber(String contractNumber);
}
