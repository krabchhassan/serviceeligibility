package com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys;

import com.cegedim.next.serviceeligibility.core.model.domain.TypeAdresse;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jetbrains.annotations.NotNull;

@Data
public class AdresseAlmerys implements GenericDomain<AdresseAlmerys> {
  /* PROPRIETES */
  private String ligne1;
  private String ligne2;
  private String ligne3;
  private String ligne4;
  private String ligne5;
  private String ligne6;
  private String ligne7;
  private String codePostal;
  private String pays;
  private String telephone;
  private String fixe;
  private String email;

  /* DOCUMENTS EMBEDDED */
  private TypeAdresse typeAdresse;
  private String codeInterne;
  private String nomCommercial;

  @Override
  public int compareTo(@NotNull AdresseAlmerys adresseAlmerys) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.codePostal, adresseAlmerys.codePostal);
    compareToBuilder.append(this.email, adresseAlmerys.email);
    compareToBuilder.append(this.ligne1, adresseAlmerys.ligne1);
    compareToBuilder.append(this.ligne2, adresseAlmerys.ligne2);
    compareToBuilder.append(this.ligne3, adresseAlmerys.ligne3);
    compareToBuilder.append(this.ligne4, adresseAlmerys.ligne4);
    compareToBuilder.append(this.ligne5, adresseAlmerys.ligne5);
    compareToBuilder.append(this.ligne6, adresseAlmerys.ligne6);
    compareToBuilder.append(this.ligne7, adresseAlmerys.ligne7);
    compareToBuilder.append(this.pays, adresseAlmerys.pays);
    compareToBuilder.append(this.telephone, adresseAlmerys.telephone);
    compareToBuilder.append(this.fixe, adresseAlmerys.fixe);
    compareToBuilder.append(this.typeAdresse, adresseAlmerys.typeAdresse);
    compareToBuilder.append(this.codeInterne, adresseAlmerys.codeInterne);
    compareToBuilder.append(this.nomCommercial, adresseAlmerys.nomCommercial);
    return compareToBuilder.toComparison();
  }
}
