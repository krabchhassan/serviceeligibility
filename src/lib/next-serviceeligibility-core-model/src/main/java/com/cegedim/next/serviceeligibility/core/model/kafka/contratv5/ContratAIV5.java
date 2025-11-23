package com.cegedim.next.serviceeligibility.core.model.kafka.contratv5;

import com.cegedim.next.serviceeligibility.core.model.kafka.ContratCollectif;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.ContratAICommun;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ContratAIV5 extends ContratAICommun {
  private ContexteTP contexteTiersPayant;
  private List<Assure> assures;
  private String version = "5";
  private List<PeriodeSuspension> periodesSuspension;
  private String codeOc;
  private List<PeriodeContratCMUOuvert> periodesContratCMUOuvert;
  private String nomFichierOrigine;
  private ContratCollectif contratCollectif;
}
