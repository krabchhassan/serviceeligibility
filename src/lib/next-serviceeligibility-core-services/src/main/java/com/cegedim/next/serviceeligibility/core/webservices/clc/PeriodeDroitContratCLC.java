package com.cegedim.next.serviceeligibility.core.webservices.clc;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.TypePeriode;
import lombok.Data;

@Data
public class PeriodeDroitContratCLC {
  private TypePeriode typePeriode;

  private String periodeDebut;
  private String periodeFin;
  private String periodeFinFermeture;

  private String libelleEvenement;
  private String modeObtention;
  private String motifEvenement;
}
