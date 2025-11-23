package com.cegedim.next.serviceeligibility.core.model.kafka.contratv6;

import com.cegedim.next.serviceeligibility.core.model.kafka.ContratCollectif;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ContratCollectifV6 extends ContratCollectif {
  private String identifiantCollectivite;
  private String raisonSociale;
  private String siret;
  private String groupePopulation;
}
