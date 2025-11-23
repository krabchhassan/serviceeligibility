package com.cegedim.next.serviceeligibility.core.model.kafka;

import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DataAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.SocieteEmettrice;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class PersonAI {
  @Id private String id;
  private String idClientBO;
  private Amc amc;
  private String numeroAdherent;
  private List<Contrat> contrats;
  private List<SocieteEmettrice> societesEmettrices;
  private IdentiteContrat identite;
  private DataAssure data;
  private Audit audit;
  private String traceId;
}
