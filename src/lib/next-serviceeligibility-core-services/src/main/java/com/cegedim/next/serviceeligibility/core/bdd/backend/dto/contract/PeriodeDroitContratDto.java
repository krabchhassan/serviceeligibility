package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.contract;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.TypePeriode;
import lombok.Data;

@Data
public class PeriodeDroitContratDto {

  private TypePeriode typePeriode;

  private String periodeDebut;
  private String periodeFin;
  private String periodeFinFermeture;
  private String periodeFinOffline;

  private String libelleEvenement;
  private String modeObtention;
  private String motifEvenement;
}
