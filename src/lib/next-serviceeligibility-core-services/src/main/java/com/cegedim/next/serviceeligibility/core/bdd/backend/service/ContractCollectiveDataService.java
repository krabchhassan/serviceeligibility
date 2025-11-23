package com.cegedim.next.serviceeligibility.core.bdd.backend.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ContractCollectiveDataDto;

public interface ContractCollectiveDataService {
  /**
   * Find contract collective data with the given contractNumber, context and insurerId
   *
   * @param contractNumber contract number
   * @param context context (TP ONLINE/TP OFFLINE/HTP)
   * @param insurerId id declarant
   * @param subscriberId numero adherent
   * @return contract collective data matching the given contractNumber, context and insurerId
   */
  ContractCollectiveDataDto findContractCollectiveData(
      String contractNumber, String context, String insurerId, String subscriberId);
}
