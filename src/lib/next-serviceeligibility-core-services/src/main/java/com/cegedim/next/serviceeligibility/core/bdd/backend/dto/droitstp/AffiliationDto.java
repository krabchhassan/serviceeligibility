package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.droitstp;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import lombok.Data;

@Data
public class AffiliationDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String nomFamille;

  private String nomUsage;

  private String prenom;

  private String civilite;

  private String periodeDebut;

  private String periodeFin;

  private String qualite;

  private String regimeOD1;

  private String caisseOD1;

  private String centreOD1;

  private String regimeOD2;

  private String caisseOD2;

  private String centreOD2;

  private Boolean hasMedecinTraitant;

  private String regimeParticulier;

  private Boolean isBeneficiaireACS;
}
