package com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun;

import com.cegedim.next.serviceeligibility.core.bobb.TriggerBenefContractElement;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import lombok.Data;

@Data
public abstract class DroitAssureCommun {
  protected String code;
  protected String codeAssureur;
  protected String libelle;
  protected String ordrePriorisation;
  protected String type;
  protected Periode periode;
  protected String dateAncienneteGarantie;
  protected TriggerBenefContractElement triggerBenefContractElement;

  /**
   * Egalité uniquement sur le code afin de pouvoir faire des merges de droits de la V1 vers la V3
   * via la methode contains
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    DroitAssureCommun other = (DroitAssureCommun) obj;
    if (code == null) {
      return other.code == null;
    } else return code.equals(other.code);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((code == null) ? 0 : code.hashCode());
    return result;
  }

  public void setCodeAssureur(String codeAssureur) {
    if (codeAssureur != null) {
      this.codeAssureur = codeAssureur;
    }
  }

  public void setCodeAssureurToNull() {
    this.codeAssureur = null;
  }

  public void setCode(String code) {
    if (code != null) {
      this.code = code;
    }
  }

  public void setCodeToNull() {
    this.code = null;
  }

  public void setLibelle(String libelle) {
    if (libelle != null) {
      this.libelle = libelle;
    }
  }

  public void setLibelleToNull() {
    this.libelle = null;
  }

  public void setPeriode(Periode periode) {
    if (periode != null) {
      this.periode = periode;
    }
  }

  public void setPeriodeToNull() {
    this.periode = null;
  }

  public void setType(String type) {
    if (type != null) {
      this.type = type;
    }
  }

  public void setTypeToNull() {
    this.type = null;
  }

  public void setOrdrePriorisation(String ordrePriorisation) {
    if (ordrePriorisation != null) {
      this.ordrePriorisation = ordrePriorisation;
    }
  }

  public void setOrdrePriorisationToNull() {
    this.ordrePriorisation = null;
  }

  public void setDateAncienneteGarantie(String dateAncienneteGarantie) {
    if (dateAncienneteGarantie != null) {
      this.dateAncienneteGarantie = dateAncienneteGarantie;
    }
  }

  public void setDateAncienneteGarantieToNull() {
    this.dateAncienneteGarantie = null;
  }
}
