package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.volumetrie;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import lombok.Data;

/** VolumetrieDto echange la data avec entite Volumetrie. */
@Data
public class VolumetrieDto implements GenericDto, Comparable<VolumetrieDto> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String codePartenaire;
  private String amc;
  private Long declarations;
  private Long personnes;
  private Long personnesDroitsOuverts;
  private Long personnesDroitsFermes;

  @Override
  public int compareTo(VolumetrieDto o) {

    int compare = 0;
    String codePartenaire1 = this.getCodePartenaire();
    String codePartenaire2 = o.getCodePartenaire();
    String amc1 = this.getAmc();
    String amc2 = o.getAmc();

    if (codePartenaire1 == null && codePartenaire2 == null) {
      compare = amc1.compareTo(amc2);
    } else if (codePartenaire1 != null && codePartenaire2 != null) {
      compare = codePartenaire1.compareTo(codePartenaire2);
      if (compare == 0) {
        compare = amc1.compareTo(amc2);
      }
    } else if (codePartenaire1 == null) {
      compare = 1;
    } else {
      compare = -1;
    }

    return compare;
  }
}
