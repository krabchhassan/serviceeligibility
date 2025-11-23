package com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun;

import com.cegedim.next.serviceeligibility.core.model.domain.Adresse;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.model.kafka.ModePaiement;
import com.cegedim.next.serviceeligibility.core.model.kafka.RibAssure;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class DestinatairePrestations implements GenericDomain<DestinatairePrestations> {
  private String idDestinatairePaiements;
  private String idBeyondDestinatairePaiements;
  private NomDestinataire nom;
  private Adresse adresse;
  private RibAssure rib;
  private ModePaiement modePaiementPrestations;
  private PeriodeDestinataire periode;

  public void setIdDestinatairePaiements(String newIdDestinatairePaiements) {
    if (newIdDestinatairePaiements != null) {
      this.idDestinatairePaiements = newIdDestinatairePaiements;
    }
  }

  public void setIdBeyondDestinatairePaiements(String newIdBeyondDestinatairePaiements) {
    if (newIdBeyondDestinatairePaiements != null) {
      this.idBeyondDestinatairePaiements = newIdBeyondDestinatairePaiements;
    }
  }

  public void setNom(NomDestinataire newNom) {
    if (newNom != null) {
      this.nom = newNom;
    }
  }

  public void setAdresse(Adresse newAdresse) {
    if (newAdresse != null) {
      this.adresse = newAdresse;
    }
  }

  public void setRib(RibAssure newRib) {
    if (newRib != null) {
      this.rib = newRib;
    }
  }

  public void setModePaiementPrestations(ModePaiement newModePaiementPrestations) {
    if (newModePaiementPrestations != null) {
      this.modePaiementPrestations = newModePaiementPrestations;
    }
  }

  public void setPeriode(PeriodeDestinataire newPeriode) {
    if (newPeriode != null) {
      this.periode = newPeriode;
    }
  }

  @Override
  public int compareTo(final DestinatairePrestations destinatairePrestations) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(
        this.idDestinatairePaiements, destinatairePrestations.idDestinatairePaiements);
    compareToBuilder.append(
        this.idBeyondDestinatairePaiements, destinatairePrestations.idBeyondDestinatairePaiements);
    compareToBuilder.append(this.nom, destinatairePrestations.nom);
    compareToBuilder.append(this.adresse, destinatairePrestations.adresse);
    compareToBuilder.append(this.rib, destinatairePrestations.rib);
    compareToBuilder.append(
        this.modePaiementPrestations, destinatairePrestations.modePaiementPrestations);
    compareToBuilder.append(this.periode, destinatairePrestations.periode);
    return compareToBuilder.toComparison();
  }
}
