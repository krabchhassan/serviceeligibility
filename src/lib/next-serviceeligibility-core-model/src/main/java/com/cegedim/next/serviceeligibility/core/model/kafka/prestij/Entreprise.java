package com.cegedim.next.serviceeligibility.core.model.kafka.prestij;

import lombok.Data;

@Data
public class Entreprise {
  private String identifiantSiret;
  private String identifiantSiren;
  private String identifiantNic;
  private String raisonSociale;
}
