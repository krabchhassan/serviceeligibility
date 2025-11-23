package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.Date;
import lombok.Data;

/** Classe DTO de l'entite {@code HistoriqueAffiliatio}. */
@Data
public class AffiliationDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String nom;
  private String nomPatronymique;
  private String nomMarital;
  private String prenom;
  private String civilite;
  private Date periodeDebut;
  private Date periodeFin;
  private String qualite;
  private String regimeOD1;
  private String caisseOD1;
  private String centreOD1;
  private String regimeOD2;
  private String caisseOD2;
  private String centreOD2;
  private Boolean medecinTraitant;
  private String regimeParticulier;
  private Boolean isBeneficiaireACS = false;
  private Boolean hasTeleTransmission;
  private String typeAssure;
}
