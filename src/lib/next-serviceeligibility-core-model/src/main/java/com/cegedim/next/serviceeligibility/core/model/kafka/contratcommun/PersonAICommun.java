package com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun;

import com.cegedim.next.serviceeligibility.core.model.kafka.Amc;
import com.cegedim.next.serviceeligibility.core.model.kafka.Audit;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
public class PersonAICommun {
  @Id private String id;
  private String idClientBO;
  private Amc amc;
  private String numeroAdherent;
  private IdentiteContrat identite;
  private Audit audit;
  private String traceId;

  public void setId(String newId) {
    if (newId != null) {
      this.id = newId;
    }
  }

  public void setIdToNull() {
    this.id = null;
  }

  public void setIdClientBOToNull() {
    this.idClientBO = null;
  }

  public void setIdClientBO(String newIdClientBo) {
    if (newIdClientBo != null) {
      this.idClientBO = newIdClientBo;
    }
  }

  public void setAmc(Amc newAmc) {
    if (newAmc != null) {
      this.amc = newAmc;
    }
  }

  public void setNumeroAdherent(String newNumeroAdherent) {
    if (newNumeroAdherent != null) {
      this.numeroAdherent = newNumeroAdherent;
    }
  }

  public void setIdentite(IdentiteContrat newIdentite) {
    if (newIdentite != null) {
      this.identite = newIdentite;
    }
  }

  public void setAudit(Audit newAudit) {
    if (newAudit != null) {
      this.audit = newAudit;
    }
  }

  public void setTraceId(String newTraceId) {
    if (newTraceId != null) {
      this.traceId = newTraceId;
    }
  }

  public void setTraceIdToNull() {
    this.traceId = null;
  }
}
