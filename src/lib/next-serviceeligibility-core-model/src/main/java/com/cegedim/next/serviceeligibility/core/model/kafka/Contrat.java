package com.cegedim.next.serviceeligibility.core.model.kafka;

import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DataAssure;
import java.util.List;
import lombok.Data;

@Data
public class Contrat {
  private String numeroContrat;
  private String codeEtat;
  private DataAssure data;
  private String numeroAdherent;
  private String societeEmettrice;
  private String numeroAMCEchange;
  private List<Periode> periodes;
}
