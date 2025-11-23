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
public class Beneficiaire extends BeneficiaireCommon {

  private List<Adresse> adresses;

  public Beneficiaire() {
    /* empty constructor */ }

  public Beneficiaire(BeneficiaireCommon source) {
    super(source);
    this.adresses = new ArrayList<>();
  }

  public Beneficiaire(Beneficiaire source) {
    super(source);
    if (!CollectionUtils.isEmpty(source.getAdresses())) {
      this.adresses = new ArrayList<>();
      for (Adresse add : source.getAdresses()) {
        this.adresses.add(new Adresse(add));
      }
    }
  }

  public int compareTo(Beneficiaire beneficiaire) {
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
