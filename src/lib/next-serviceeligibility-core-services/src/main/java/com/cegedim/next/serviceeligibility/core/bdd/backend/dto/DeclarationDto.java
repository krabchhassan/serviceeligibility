package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeclarationDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String identifiantTechnique;
  private String referenceExterne;
  private String codeEtat;
  private Date effetDebut;
  private Boolean isCarteTPaEditer;
  private String nomFichierOrigine;
  private String carteTPaEditerOuDigitale;
  private String dateRestitution;

  /* CLES ETRANGERES */
  private DeclarantDto declarantAmc;
  private BeneficiaireDto beneficiaire;
  private ContratDto contrat;
  private List<DomaineDroitDto> domaineDroits;

  private String etatSuspension;
}
