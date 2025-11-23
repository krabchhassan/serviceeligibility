package com.cegedim.next.serviceeligibility.core.model.kafka;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class ModePaiement implements GenericDomain<ModePaiement> {
  @NotBlank(message = "L'information code du mode de paiement est obligatoire")
  private String code;

  @NotBlank(message = "L'information libelle du mode de paiement est obligatoire")
  private String libelle;

  @NotBlank(message = "L'information codeMonnaie du mode de paiement est obligatoire")
  private String codeMonnaie;

  public ModePaiement(String code, String libelle, String codeMonnaie) {
    this.code = code;
    this.libelle = libelle;
    this.codeMonnaie = codeMonnaie;
  }

  public ModePaiement() {}

  public void setCode(String newCode) {
    if (newCode != null) {
      this.code = newCode;
    }
  }

  public void setLibelle(String newLibelle) {
    if (newLibelle != null) {
      this.libelle = newLibelle;
    }
  }

  public void setCodeMonnaie(String newCodeMonnaie) {
    if (newCodeMonnaie != null) {
      this.codeMonnaie = newCodeMonnaie;
    }
  }

  @Override
  public int compareTo(ModePaiement modePaiement) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.code, modePaiement.code);
    compareToBuilder.append(this.libelle, modePaiement.libelle);
    compareToBuilder.append(this.codeMonnaie, modePaiement.codeMonnaie);
    return compareToBuilder.toComparison();
  }
}
