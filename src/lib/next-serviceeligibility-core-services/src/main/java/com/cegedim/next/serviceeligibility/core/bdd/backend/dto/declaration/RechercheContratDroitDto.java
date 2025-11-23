package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration;

import lombok.Data;

@Data
public class RechercheContratDroitDto implements Comparable<RechercheContratDroitDto> {

  private String numero;
  private String numeroAdherent;
  private String qualification;
  private String numeroContratCollectif;
  private boolean droitsOuverts = false;
  private String qualite;
  private String idTechniqueDeclaration;

  private HistoriqueDeclarationsDto historique;

  public int compareTo(RechercheContratDroitDto contratDroitDto) {
    int resultat = 0;
    if (this.numero == null) {
      if (contratDroitDto.getNumero() != null) {
        resultat = -1;
      }
    } else {
      if (contratDroitDto.getNumero() == null) {
        resultat = 1;
      } else {
        resultat = this.numero.compareTo(contratDroitDto.getNumero());
      }
    }
    if (resultat == 0) {
      resultat = compareQualite(contratDroitDto);
    }

    return resultat;
  }

  private int compareQualite(RechercheContratDroitDto contratDroitDto) {
    if (this.qualite == null) {
      if (contratDroitDto.getQualite() == null) {
        return 0;
      } else {
        return -1;
      }
    } else {
      if (contratDroitDto.getQualite() == null) {
        return 1;
      } else {
        return this.qualite.compareTo(contratDroitDto.qualite);
      }
    }
  }
}
