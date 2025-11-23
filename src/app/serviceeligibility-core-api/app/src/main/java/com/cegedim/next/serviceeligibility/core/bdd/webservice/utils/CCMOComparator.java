package com.cegedim.next.serviceeligibility.core.bdd.webservice.utils;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarationDto;
import java.util.Comparator;

/**
 * Comparateur qui permet d'afficher les déclarations d'une carte CCMO dans l'odre: ASSURE A
 * CONJOINT(E) C ENFANT(S) E Les enfants sont affichés dans l'ordre décroissant des naissances.
 */
public class CCMOComparator implements Comparator<DeclarationDto> {

  @Override
  public int compare(final DeclarationDto o1, final DeclarationDto o2) {
    final CCMODeclarationCodeQualiteOrderEnum codeQualite1 = getCCMOCodeQualiteEnum(o1);
    final CCMODeclarationCodeQualiteOrderEnum codeQualite2 = getCCMOCodeQualiteEnum(o2);
    if (CCMODeclarationCodeQualiteOrderEnum.ENFANT.equals(codeQualite1)
        && CCMODeclarationCodeQualiteOrderEnum.ENFANT.equals(codeQualite2)) {
      // On concatène la date de naissance au rang de naissance et ensuite
      // on compare les valeurs. L'objectif etant de gérer le cas des
      // naissances multiples (jumeaux, triplés,...)
      final String rangNaissance1 =
          o1.getBeneficiaire().getDateNaissance() + o1.getBeneficiaire().getRangNaissance();
      final String rangNaissance2 =
          o2.getBeneficiaire().getDateNaissance() + o2.getBeneficiaire().getRangNaissance();
      return rangNaissance1.compareTo(rangNaissance2);
    }
    return codeQualite1.getRang().compareTo(codeQualite2.getRang());
  }

  private CCMODeclarationCodeQualiteOrderEnum getCCMOCodeQualiteEnum(
      final DeclarationDto declaration) {
    String qualite = declaration.getBeneficiaire().getAffiliation().getQualite();
    if (CCMODeclarationCodeQualiteOrderEnum.ASSURE_PRINCIPAL.getQualite().equals(qualite)) {
      return CCMODeclarationCodeQualiteOrderEnum.ASSURE_PRINCIPAL;
    } else if (CCMODeclarationCodeQualiteOrderEnum.CONJOINT.getQualite().equals(qualite)) {
      return CCMODeclarationCodeQualiteOrderEnum.CONJOINT;
    } else {
      return CCMODeclarationCodeQualiteOrderEnum.ENFANT;
    }
  }

  /** Enumération Code qualité. */
  private enum CCMODeclarationCodeQualiteOrderEnum {
    ASSURE_PRINCIPAL("ASSURE", 1),
    CONJOINT("CONJOI", 2),
    ENFANT("ENFANT", 3);

    private String qualite;
    private Integer rang;

    private CCMODeclarationCodeQualiteOrderEnum(final String qualite, final Integer rang) {
      this.qualite = qualite;
      this.rang = rang;
    }

    public String getQualite() {
      return this.qualite;
    }

    public Integer getRang() {
      return rang;
    }
  }
}
