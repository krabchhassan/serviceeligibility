package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ModeAssemblage;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** Classe DTO de l'entite {@code DomaineDroit}. */
@Getter
@Setter
public class DomaineDroitDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String code;
  private String libelle;
  private String codeExterne;
  private String codeExterneProduit;
  private String libelleExterne;
  private String codeOptionMutualiste;
  private String libelleOptionMutualiste;
  private String codeProduit;
  private ModeAssemblage modeAssemblage;
  private String libelleProduit;
  private String codeGarantie;
  private String codeRenvoi;
  private String libelleGarantie;
  private String libelleCodeRenvoi;
  private String tauxRemboursement;
  private String referenceCouverture;
  private String codeProfil;
  private Boolean isEditable;
  private Boolean isSuspension;
  private String uniteTauxRemboursement;
  private String dateAdhesionCouverture;
  private String origineDroits;
  private String categorie;
  private String naturePrestation;

  /* CLES ETRANGERES */
  private PrioriteDroitDto prioriteDroit;
  private List<PrestationDto> prestations;
  private PeriodeDroitDto periodeDroit;
  private PeriodeDroitDto periodeOnline;
  private List<ConventionnementDto> conventionnements;
  private Integer noOrdreDroit;

  private String codeRenvoiAdditionnel;
  private String libelleCodeRenvoiAdditionnel;
}
