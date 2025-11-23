package com.cegedim.next.serviceeligibility.core.model.domain.insured.prestation;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ModePaiementPrestationAI {
  @NotEmpty(message = "Le code du mode de paiement de la prestation est obligatoire")
  private String code;

  @NotEmpty(message = "Le libellé du mode de paiement de la prestation est obligatoire")
  private String libelle;

  @NotEmpty(message = "Le code monnaie du mode de paiement de la prestation est obligatoire")
  private String codeMonnaie;

  public ModePaiementPrestationAI() {
    /* empty constructor */ }

  public ModePaiementPrestationAI(ModePaiementPrestationAI source) {
    this.code = source.getCode();
    this.libelle = source.getLibelle();
    this.codeMonnaie = source.getCodeMonnaie();
  }
}
