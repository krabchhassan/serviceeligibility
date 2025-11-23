package com.cegedim.next.serviceeligibility.core.dao.forcingrights;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import java.util.List;

public interface ForcingRightsDao {

  List<ContractTP> findContratTP(String amc, String personNumber);

  List<ContratAIV6> findServicePrestationV6(String idDeclarant, String numeroPersonne);
}
