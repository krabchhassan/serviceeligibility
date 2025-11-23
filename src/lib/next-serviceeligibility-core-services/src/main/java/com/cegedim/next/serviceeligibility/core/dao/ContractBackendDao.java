package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointRequestV5;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointTPRequest;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointTPRequestV5;
import java.util.List;

/** Interface de la classe d'accès aux {@code Contract} de la base de donnees. */
public interface ContractBackendDao extends IMongoGenericDao<ContractTP> {

  List<ContractTP> findContractsForTPOnline(UniqueAccessPointTPRequestV5 request);

  List<ContractTP> findContractsForTPOffline(UniqueAccessPointTPRequestV5 request);

  List<ContratAIV6> findContractsForHTP(UniqueAccessPointRequestV5 requete, List<BenefAIV5> benefs);

  List<ContractTP> findPastContractsForOS(UniqueAccessPointTPRequest request, boolean isTpOnline);

  List<ContractTP> findFuturContractsForOS(UniqueAccessPointTPRequest request, boolean isTpOnline);
}
