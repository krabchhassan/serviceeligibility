package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.*;
import java.util.List;
import lombok.Data;

@Data
public class PeriodeDroitContractDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private TypePeriode typePeriode;
  private String periodeDebut;
  private String periodeFin;
  private String periodeFinFermeture;
  private String periodeFinOffline;
  private String codeGarantie;
  private String libelleGarantie;
  private String codeProduit;
  private String libelleProduit;
  private String codeExterneProduit;
  private String libelleExterneProduit;
  private String codeAssureurGarantie;
  private String codeOffre;
  private String versionOffre;
  private String libelleEvenement;
  private String masqueFormule;
  private String modeObtention;
  private String motifEvenement;
  private String naturePrestation;
  private String dateAdhesionCouverture;
  private String referenceCouverture;
  private String codeOc;
  private List<PrioriteDroitContract> prioritesDroit;
  private List<ConventionnementContract> conventionnements;
  private List<PrestationContract> prestations;
  private List<RemboursementContract> remboursements;
  private String codeCarence;
  private String codeOrigine;
  private String codeAssureurOrigine;
}
