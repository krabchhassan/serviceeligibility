package com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun;

import com.cegedim.next.serviceeligibility.core.model.kafka.AdresseAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.Contact;
import com.cegedim.next.serviceeligibility.core.model.kafka.NomAssure;
import lombok.Data;

@Data
public abstract class DataAssureCommun {
  private NomAssure nom;
  private AdresseAssure adresse;
  private Contact contact;

  public void setNom(NomAssure newNom) {
    if (newNom != null) {
      this.nom = newNom;
    }
  }

  public void setAdresse(AdresseAssure newAdresse) {
    if (newAdresse != null) {
      this.adresse = newAdresse;
    }
  }

  public void setContact(Contact newContact) {
    if (newContact != null) {
      this.contact = newContact;
    }
  }
}
