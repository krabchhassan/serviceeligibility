package com.cegedim.next.serviceeligibility.core.services;

import lombok.Data;

@Data
public class TriggerTestPeriode {

  private String motifEvenement;

  private String dateDebut;
  private String dateFin;

  private String dateDebutFermeture;
  private String dateFinFermeture;
  private String dateDebutOnline;
  private String dateFinOnline;
  private String dateFinOffline;
}
