package com.cegedim.next.serviceeligibility.core.model.domain.insured.prestation;

import com.cegedim.next.serviceeligibility.core.model.kafka.NomAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.RibAssure;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DestinatairePrestationAI {
  @NotNull(message = "Le noeud nom est obligatoire")
  private NomAssure nom;

  private RibAssure rib;
  private ModePaiementPrestationAI modePaiementPrestations;

  @NotNull(message = "Le noeud période du destinataire de prestation est obligatoire")
  private Periode periode;

  public DestinatairePrestationAI() {
    /* empty constructor */ }

  public DestinatairePrestationAI(DestinatairePrestationAI source) {
    if (source.getNom() != null) {
      this.nom = new NomAssure(source.getNom());
    }
    if (source.getRib() != null) {
      this.rib = new RibAssure(source.getRib());
    }
    if (source.getModePaiementPrestations() != null) {
      this.modePaiementPrestations =
          new ModePaiementPrestationAI(source.getModePaiementPrestations());
    }
    if (source.getPeriode() != null) {
      this.periode = new Periode(source.getPeriode());
    }
  }
}
