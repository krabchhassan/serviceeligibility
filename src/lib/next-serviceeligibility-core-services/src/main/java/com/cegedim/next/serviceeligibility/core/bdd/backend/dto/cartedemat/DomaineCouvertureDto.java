package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import lombok.Data;

/** Classe qui mappe le document DomainesConventions */
@Data
public class DomaineCouvertureDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String code;
  private String periodeDebut;
  private String periodeFin;
  private String tauxRemboursement;
  private String uniteTauxRemboursement;
  private String codeExterneProduit;
  private String codeOptionMutualiste;
  private String libelleOptionMutualiste;
  private String codeProduit;
  private String libelleProduit;
  private String codeGarantie;
  private String libelleGarantie;
  private String prioriteDroits;
  private String origineDroits;
  private String dateAdhesionCouverture;
  private String libelleCodeRenvoi;
  private String categorieDomaine;
}
