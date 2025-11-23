package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration;

import java.util.SortedSet;
import java.util.TreeSet;
import lombok.Data;

@Data
public class RechercheDroitDto {

  int totalContrats = 0;

  private RechercheBeneficiaireDroitDto beneficiaire;

  private SortedSet<RechercheContratDroitDto> contrats = new TreeSet<>();

  public int compareTo(RechercheDroitDto droitDto) {
    if (this.beneficiaire == null) {
      if (droitDto.getBeneficiaire() == null) {
        return 0;
      } else {
        return -1;
      }
    } else {
      if (droitDto.getBeneficiaire() == null) {
        return 1;
      } else {
        return this.beneficiaire.compareTo(droitDto.getBeneficiaire());
      }
    }
  }
}
