package com.cegedim.next.serviceeligibility.core.model.kafka.contratv6;

import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.ContratAICommun;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodeContratCMUOuvert;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodeSuspension;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ContratAIV6 extends ContratAICommun {
  private ContexteTPV6 contexteTiersPayant;
  private List<Assure> assures;
  private String version = "6";
  private List<PeriodeSuspension> periodesSuspension;
  private String codeOc;
  private List<PeriodeContratCMUOuvert> periodesContratCMUOuvert;
  private String nomFichierOrigine;
  private ContratCollectifV6 contratCollectif;

  @JsonIgnore private String batchExecutionId;
}
