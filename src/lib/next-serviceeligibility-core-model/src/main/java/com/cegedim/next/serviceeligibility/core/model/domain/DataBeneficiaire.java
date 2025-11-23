package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

/** Classe qui mappe le document Data */
@Data
public class DataBeneficiaire implements GenericDomain<DataBeneficiaire> {

  private static final long serialVersionUID = 1L;

  /* DOCUMENTS EMBEDDED */
  private Adresse adresse;
  private NomBeneficiaire nom;
  private ContactBeneficiaire contact;
  private RibBeneficiaire rib;

  public DataBeneficiaire() {
    /* empty constructor */ }

  public DataBeneficiaire(DataBeneficiaire source) {
    if (source.getAdresse() != null) {
      this.adresse = new Adresse(source.getAdresse());
    }
    if (source.getNom() != null) {
      this.nom = new NomBeneficiaire(source.getNom());
    }
    if (source.getContact() != null) {
      this.contact = new ContactBeneficiaire(source.getContact());
    }
    if (source.getRib() != null) {
      this.rib = new RibBeneficiaire(source.getRib());
    }
  }

  @Override
  public int compareTo(final DataBeneficiaire data) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.adresse, data.adresse);
    compareToBuilder.append(this.nom, data.nom);
    compareToBuilder.append(this.contact, data.contact);
    compareToBuilder.append(this.rib, data.rib);
    return compareToBuilder.toComparison();
  }

  public void setAdresse(Adresse newAdresse) {
    if (newAdresse != null) {
      this.adresse = newAdresse;
    }
  }

  public void setNom(NomBeneficiaire newNom) {
    if (newNom != null) {
      this.nom = newNom;
    }
  }

  public void setContact(ContactBeneficiaire newContact) {
    if (newContact != null) {
      this.contact = newContact;
    }
  }

  public void setRib(RibBeneficiaire newRib) {
    if (newRib != null) {
      this.rib = newRib;
    }
  }
}
