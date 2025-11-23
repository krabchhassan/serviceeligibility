package com.cegedim.next.serviceeligibility.core.mapper.pau;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.services.OcService;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.CollectiveContractV5;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.GenericRightDto;
import org.springframework.stereotype.Component;

@Component
public class MapperUniqueAccessPointServiceTPV5 extends MapperUniqueAccessPointServiceTP {

  public MapperUniqueAccessPointServiceTPV5(
      OcService ocService,
      MapperUAPRightTDB mapperUAPRightTDB,
      MapperUAPRightEvent mapperUAPRightEvent,
      BeyondPropertiesService beyondPropertiesService) {
    super(ocService, mapperUAPRightTDB, mapperUAPRightEvent, beyondPropertiesService);
  }

  @Override
  void manageCollectiveContract(final ContractTP contractTP, final GenericRightDto genericRights) {
    CollectiveContractV5 collContract = new CollectiveContractV5();
    collContract.setCompanyName(contractTP.getRaisonSociale());
    collContract.setNumber(contractTP.getNumeroContratCollectif());
    collContract.setExternalNumber(contractTP.getNumeroExterneContratCollectif());
    genericRights.setCollectiveContract(collContract);
  }
}
