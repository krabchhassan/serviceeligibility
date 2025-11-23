package com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys;

import com.cegedim.next.serviceeligibility.core.model.domain.Affiliation;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jetbrains.annotations.NotNull;

@Data
public class BeneficiaireAlmerys implements GenericDomain<BeneficiaireAlmerys> {
  String dateNaissance;
  String rangNaissance;
  String nirBeneficiaire;
  String cleNirBeneficiaire;
  String nirOd1;
  String cleNirOd1;
  String nirOd2;
  String cleNirOd2;
  String insc;
  String numeroPersonne;
  String refExternePersonne;
  Affiliation affiliation;
  List<AdresseAlmerys> adresses;
  String dateRadiation;
  boolean isSouscripteur;

  @Override
  public int compareTo(@NotNull BeneficiaireAlmerys beneficiaire) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.dateNaissance, beneficiaire.dateNaissance);
    compareToBuilder.append(this.rangNaissance, beneficiaire.rangNaissance);
    compareToBuilder.append(this.nirBeneficiaire, beneficiaire.nirBeneficiaire);
    compareToBuilder.append(this.cleNirBeneficiaire, beneficiaire.cleNirBeneficiaire);
    compareToBuilder.append(this.nirOd1, beneficiaire.nirOd1);
    compareToBuilder.append(this.cleNirOd1, beneficiaire.cleNirOd1);
    compareToBuilder.append(this.nirOd2, beneficiaire.nirOd2);
    compareToBuilder.append(this.cleNirOd2, beneficiaire.cleNirOd2);
    compareToBuilder.append(this.insc, beneficiaire.insc);
    compareToBuilder.append(this.numeroPersonne, beneficiaire.numeroPersonne);
    compareToBuilder.append(this.refExternePersonne, beneficiaire.refExternePersonne);
    compareToBuilder.append(this.affiliation, beneficiaire.affiliation);
    compareToBuilder.append(this.adresses, beneficiaire.adresses);
    compareToBuilder.append(this.dateRadiation, beneficiaire.dateRadiation);
    compareToBuilder.append(this.isSouscripteur, beneficiaire.isSouscripteur);
    return compareToBuilder.toComparison();
  }
}
