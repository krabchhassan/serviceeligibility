package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration;

import lombok.Data;

@Data
public class RechercheBeneficiaireDroitDto {

  private String dateNaissance;
  private String rangNaissance;
  private String nirBeneficiaire;
  private String cleNirBeneficiaire;
  private String nirOd1;
  private String cleNirOd1;
  private String nom;
  private String prenom;
  private String civilite;
  private String AMC;
  private String qualite;

  boolean droitsOuverts = false;

  public int compareTo(RechercheBeneficiaireDroitDto beneficiaireDroitDto) {

    int result = 0;

    if (this.AMC == null) {
      if (beneficiaireDroitDto.getAMC() != null) {
        result = -1;
      }
    } else {
      if (beneficiaireDroitDto.getAMC() == null) {
        result = 1;
      } else {
        result = this.AMC.compareTo(beneficiaireDroitDto.getAMC());
      }
    }
    if (result == 0) {
      result = this.compareQualiteBenef(beneficiaireDroitDto);
    }
    return result;
  }

  private int compareQualiteBenef(RechercheBeneficiaireDroitDto beneficiaireDroitDto) {
    int result = 0;

    if (this.qualite == null) {
      if (beneficiaireDroitDto.getQualite() != null) {
        result = -1;
      }
    } else {
      if (beneficiaireDroitDto.getQualite() == null) {
        result = 1;
      } else {
        result = this.qualite.compareTo(beneficiaireDroitDto.getQualite());
      }
    }

    return result;
  }
}
