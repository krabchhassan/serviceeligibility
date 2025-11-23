package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/** Classe DTO de l'entite {@code HistoriquePeriodeDroit}. */
@Getter
@Setter
public class PeriodeDroitDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private Date periodeDebut;
  private Date periodeFin;
  private Date periodeFinInitiale;
  private Date dateRenouvellement;
  private String motifEvenement;
  private String libelleEvenement;
  private Date dateEvenemnt;
  private String modeObtention;
  private Date periodeFermetureDebut;
  private Date periodeFermetureFin;
}
