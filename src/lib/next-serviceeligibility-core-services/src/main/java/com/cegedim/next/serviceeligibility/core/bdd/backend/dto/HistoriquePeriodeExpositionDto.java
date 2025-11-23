package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoriquePeriodeExpositionDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String typeExposition;
  private Date persiodeDebut;
  private Date persiodeFin;
  private String motifEvenemrn;
  private String libelleEvenement;
  private Date dateEvenement;
  private String modeObtention;
  private Boolean isCarteRecuperee = false;
  private Date dateRestitutionCarte;
  private Boolean isSuspensionDefinitive = false;
}
