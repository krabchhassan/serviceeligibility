package com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun;

import com.cegedim.next.serviceeligibility.core.bobb.TriggerBenefContractElement;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
public class DroitRemplacement {
  private String codeAssureur;
  private String libelle;
  private String code;
  private TriggerBenefContractElement triggerBenefContractElement;

  public boolean isEquals(DroitRemplacement droitRemplacementV3) {
    if (droitRemplacementV3 == null) {
      return false;
    }
    return StringUtils.equals(this.codeAssureur, droitRemplacementV3.getCodeAssureur())
        && StringUtils.equals(this.libelle, droitRemplacementV3.getLibelle())
        && StringUtils.equals(this.code, droitRemplacementV3.getCode());
  }

  public void setCode(String code) {
    if (code != null) {
      this.code = code;
    }
  }

  public void setLibelle(String libelle) {
    if (libelle != null) {
      this.libelle = libelle;
    }
  }

  public void setCodeAssureur(String codeAssureur) {
    if (codeAssureur != null) {
      this.codeAssureur = codeAssureur;
    }
  }

  public DroitRemplacement(DroitRemplacement source) {
    this.setCode(source.getCode());
    this.setCodeAssureur(source.getCodeAssureur());
    this.setLibelle(source.getLibelle());
  }
}
