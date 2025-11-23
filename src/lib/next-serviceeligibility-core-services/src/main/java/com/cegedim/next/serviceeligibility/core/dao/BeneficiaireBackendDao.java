package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointRequestV5;
import java.util.List;

/** Interface de la classe d'accès aux {@code Contract} de la base de donnees. */
public interface BeneficiaireBackendDao {

  List<BenefAIV5> findBenefFromRequest(UniqueAccessPointRequestV5 requete);
}
