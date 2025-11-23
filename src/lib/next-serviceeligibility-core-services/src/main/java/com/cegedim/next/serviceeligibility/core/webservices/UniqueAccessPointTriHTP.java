package com.cegedim.next.serviceeligibility.core.webservices;

import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointRequest;
import java.util.List;

public interface UniqueAccessPointTriHTP {
  void triHTP(List<ContratAIV6> contrats, UniqueAccessPointRequest requete);
}
