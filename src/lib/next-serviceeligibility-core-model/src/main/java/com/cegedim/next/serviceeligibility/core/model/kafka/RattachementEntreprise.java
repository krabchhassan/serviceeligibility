package com.cegedim.next.serviceeligibility.core.model.kafka;

import lombok.Data;

@Data
public class RattachementEntreprise {
  private String siretEtabGeographique;
  private String raisonSocialeEtablissement;
  private String groupePopulation;
  private String adresseEtablissement;
}
