package com.cegedim.next.serviceeligibility.core.model.kafka.contratv5;

import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DataAssure;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.List;
import lombok.Data;

@Data
public class ContratV5 {
  private String numeroContrat;
  private String codeEtat;
  private DataAssure data;
  private String numeroAdherent;
  private String societeEmettrice;
  private String numeroAMCEchange;

  @JsonView(value = Version.Advanced.class)
  private List<Periode> periodes;

  public void setNumeroContrat(String newNumeroContrat) {
    if (newNumeroContrat != null) {
      this.numeroContrat = newNumeroContrat;
    }
  }

  public void setCodeEtat(String newCodeEtat) {
    if (newCodeEtat != null) {
      this.codeEtat = newCodeEtat;
    }
  }

  public void setData(DataAssure newData) {
    if (newData != null) {
      this.data = newData;
    }
  }

  public void setNumeroAdherent(String newNumeroAdherent) {
    if (newNumeroAdherent != null) {
      this.numeroAdherent = newNumeroAdherent;
    }
  }

  public void setSocieteEmettrice(String newSocieteEmettrice) {
    if (newSocieteEmettrice != null) {
      this.societeEmettrice = newSocieteEmettrice;
    }
  }

  public void setNumeroAMCEchange(String newNumeroAMCEchange) {
    if (newNumeroAMCEchange != null) {
      this.numeroAMCEchange = newNumeroAMCEchange;
    }
  }

  public void setPeriodes(List<Periode> newPeriodesContrat) {
    if (newPeriodesContrat != null) {
      this.periodes = newPeriodesContrat;
    }
  }
}
