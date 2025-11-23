package com.cegedim.next.serviceeligibility.core.webservices;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointRequest;
import java.util.List;

public interface UniqueAccessPointTpSortSubscriber {

  void sort(List<ContractTP> contrats, UniqueAccessPointRequest requete);
}
