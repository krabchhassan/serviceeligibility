package com.cegedim.next.serviceeligibility.core.model.kafka.contratv5;

import com.cegedim.next.serviceeligibility.core.model.kafka.AdresseAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.ModePaiement;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.RibAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.NomDestinataire;
import lombok.Data;

@Data
public class DestinatairePaiementsV5 {
  private String idDestinatairePaiements;
  private String idBeyondDestinatairePaiements;
  private NomDestinataire nom;
  private AdresseAssure adresse;
  private RibAssure rib;
  private ModePaiement modePaiementPrestations;
  private Periode periode;
}
