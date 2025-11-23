package com.cegedim.next.serviceeligibility.core.model.domain.contract;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExtractedDomain {
  // -- DomaineDroitContract --
  private String codeDomaine;

  // -- PeriodeDroitContract --
  private TypePeriode typePeriode;
  private String periodeDebut;
  private String periodeFin;
  private String periodeFinFermeture;
  private String codeGarantie;
  private String libelleGarantie;
  private String codeProduit;
  private String libelleProduit;
  private String referenceCouverture;
  // -- Remboursement --
  private String tauxRemboursement;
  private String uniteTauxRemboursement;

  // -- Prestation / Formule --
  private String codeFormule;
  private String libelleFormule;
}
