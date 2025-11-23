package com.cegedim.next.consumer.worker.model;

import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import lombok.Data;

@Data
public class ContratBLB implements Cloneable {

  private String societeEmettrice;

  private String debut;

  private String fin;

  private String numeroAdherent;

  public ContratBLB(final ContratAIV6 contrat) {
    this.societeEmettrice = contrat.getSocieteEmettrice();
    this.debut = contrat.getDateSouscription();
    this.fin = contrat.getDateResiliation();
    this.numeroAdherent = contrat.getNumeroAdherent();
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
