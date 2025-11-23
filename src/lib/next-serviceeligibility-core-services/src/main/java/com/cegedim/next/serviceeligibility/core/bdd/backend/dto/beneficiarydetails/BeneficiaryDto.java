package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails;

import com.cegedim.next.serviceeligibility.core.model.kafka.Amc;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.SocieteEmettrice;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.Version;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.List;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BeneficiaryDto {

  private List<String> services;
  private String key;
  private String environnement;

  @JsonView(value = Version.Advanced.class)
  private List<SocieteEmettrice> societesEmettrices;

  private Amc amc;
  private IdentiteContrat identite;
  private List<String> numerosContratTP;

  private List<ContratV5> contrats;
}
