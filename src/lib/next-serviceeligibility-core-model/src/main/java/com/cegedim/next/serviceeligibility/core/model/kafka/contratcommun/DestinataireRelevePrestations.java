package com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.model.kafka.AdresseAssure;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class DestinataireRelevePrestations implements GenericDomain<DestinataireRelevePrestations> {
  private String idDestinataireRelevePrestations;
  private String idBeyondDestinataireRelevePrestations;
  private Dematerialisation dematerialisation;
  private NomDestinataire nom;
  private AdresseAssure adresse;
  private PeriodeDestinataire periode;

  public void setIdDestinataireRelevePrestations(String newIdDestinataireRelevePrestations) {
    if (newIdDestinataireRelevePrestations != null) {
      this.idDestinataireRelevePrestations = newIdDestinataireRelevePrestations;
    }
  }

  public void setIdBeyondDestinataireRelevePrestations(
      String newIdBeyondDestinataireRelevePrestations) {
    if (newIdBeyondDestinataireRelevePrestations != null) {
      this.idBeyondDestinataireRelevePrestations = newIdBeyondDestinataireRelevePrestations;
    }
  }

  public void setDematerialisation(Dematerialisation newDematerialisation) {
    if (newDematerialisation != null) {
      this.dematerialisation = newDematerialisation;
    }
  }

  public void setNom(NomDestinataire newNom) {
    if (newNom != null) {
      this.nom = newNom;
    }
  }

  public void setAdresse(AdresseAssure newAdresse) {
    if (newAdresse != null) {
      this.adresse = newAdresse;
    }
  }

  public void setPeriode(PeriodeDestinataire newPeriode) {
    if (newPeriode != null) {
      this.periode = newPeriode;
    }
  }

  @Override
  public int compareTo(final DestinataireRelevePrestations destinataireRelevePrestations) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(
        this.idDestinataireRelevePrestations,
        destinataireRelevePrestations.idDestinataireRelevePrestations);
    compareToBuilder.append(
        this.idBeyondDestinataireRelevePrestations,
        destinataireRelevePrestations.idBeyondDestinataireRelevePrestations);
    compareToBuilder.append(
        this.dematerialisation, destinataireRelevePrestations.dematerialisation);
    compareToBuilder.append(this.nom, destinataireRelevePrestations.nom);
    compareToBuilder.append(this.adresse, destinataireRelevePrestations.adresse);
    compareToBuilder.append(this.periode, destinataireRelevePrestations.periode);
    return compareToBuilder.toComparison();
  }
}
