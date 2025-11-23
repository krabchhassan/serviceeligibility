package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AttestationDto implements GenericDto {

  /** */
  private static final long serialVersionUID = 6580071121938630301L;

  private Boolean isCarteTpAEditer;

  private Boolean isCarteDemat;

  private Boolean isCartePapier;

  private String conventionEditeur;

  private String communication;

  private String modeleCarte;

  private String annexe1Carte;

  private String annexe2Carte;

  private String codeItelis;

  private String codeRenvoi;
  private String libelleRenvoi;

  private String dateConsolidation;
  private String periodeDebut;

  private String periodeFin;
  private List<AttestationDetailDto> details;
}
