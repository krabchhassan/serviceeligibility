package com.cegedim.next.serviceeligibility.core.model.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.util.CollectionUtils;

/** Classe qui mappe le document Beneficiaire */
@EqualsAndHashCode(callSuper = true)
@Data
public class BeneficiaireV2 extends BeneficiaireCommon {

  private List<AdresseAvecFixe> adresses;

  public BeneficiaireV2() {
    /* empty constructor */ }

  public BeneficiaireV2(BeneficiaireCommon source) {
    super(source);
    this.adresses = new ArrayList<>();
  }

  public BeneficiaireV2(BeneficiaireV2 source) {
    super(source);
    if (!CollectionUtils.isEmpty(source.getAdresses())) {
      this.adresses = new ArrayList<>();
      for (AdresseAvecFixe add : source.getAdresses()) {
        this.adresses.add(new AdresseAvecFixe(add));
      }
    }
  }

  public int compareTo(BeneficiaireV2 beneficiaire) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.getCleNirOd1(), beneficiaire.getCleNirOd1());
    compareToBuilder.append(this.getDateNaissance(), beneficiaire.getDateNaissance());
    compareToBuilder.append(this.getNirOd1(), beneficiaire.getNirOd1());
    compareToBuilder.append(this.getRangNaissance(), beneficiaire.getRangNaissance());
    compareToBuilder.append(this.getRefExternePersonne(), beneficiaire.getRefExternePersonne());
    compareToBuilder.append(this.getAffiliation(), beneficiaire.getAffiliation());
    compareToBuilder.append(this.adresses, beneficiaire.adresses);
    compareToBuilder.append(this.getIdClientBO(), beneficiaire.getIdClientBO());
    compareToBuilder.append(this.getNirBeneficiaire(), beneficiaire.getNirBeneficiaire());
    compareToBuilder.append(this.getCleNirBeneficiaire(), beneficiaire.getCleNirBeneficiaire());
    compareToBuilder.append(this.getDateAdhesionMutuelle(), beneficiaire.getDateAdhesionMutuelle());
    compareToBuilder.append(this.getNirOd2(), beneficiaire.getNirOd2());
    compareToBuilder.append(this.getCleNirOd2(), beneficiaire.getCleNirOd2());
    compareToBuilder.append(this.getInsc(), beneficiaire.getInsc());
    compareToBuilder.append(this.getNumeroPersonne(), beneficiaire.getNumeroPersonne());
    compareToBuilder.append(
        this.getDateDebutAdhesionIndividuelle(), beneficiaire.getDateDebutAdhesionIndividuelle());
    compareToBuilder.append(
        this.getNumeroAdhesionIndividuelle(), beneficiaire.getNumeroAdhesionIndividuelle());
    compareToBuilder.append(this.getDateRadiation(), beneficiaire.getDateRadiation());
    compareToBuilder.append(this.getAffiliationsRO(), beneficiaire.getAffiliationsRO());
    compareToBuilder.append(
        this.getPeriodesMedecinTraitant(), beneficiaire.getPeriodesMedecinTraitant());
    compareToBuilder.append(this.getTeletransmissions(), beneficiaire.getTeletransmissions());
    compareToBuilder.append(this.getRegimesParticuliers(), beneficiaire.getRegimesParticuliers());
    compareToBuilder.append(
        this.getSituationsParticulieres(), beneficiaire.getSituationsParticulieres());
    return compareToBuilder.toComparison();
  }
}
