package com.cegedim.next.serviceeligibility.core.services.pojo;

import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import lombok.Data;

@Data
public class ContractHTPForPau {

  ContratAIV6 contratAIV6;

  private int retour;

  private Periode periode;
}
