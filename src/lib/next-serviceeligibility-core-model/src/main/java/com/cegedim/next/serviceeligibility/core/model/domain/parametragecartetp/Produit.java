package com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp;

import lombok.Data;

@Data
public class Produit {
  private String id;
  private String code;
  private String libelle;
  private String ordrePriorisationServicePrestation;
  private String codeGarantieServicePrestation;
  private String libelleGarantieServicePrestation;
  private String periodeDebutDroit;
  private String periodeFinDroit;
  private String codeAmc;
}
