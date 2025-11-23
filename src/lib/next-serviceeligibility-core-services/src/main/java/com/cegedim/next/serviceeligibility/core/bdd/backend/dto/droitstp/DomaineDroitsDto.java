package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.droitstp;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ModeAssemblage;
import java.util.List;
import lombok.Data;

@Data
public class DomaineDroitsDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String code;

  private String codeExterne;

  private String libelle;

  private String codeExterneProduit;

  private String libelleExterne;

  private String codeOptionMutualiste;

  private String libelleOptionMutualiste;

  private String codeProduit;

  private ModeAssemblage modeAssemblage;

  private String libelleProduit;

  private String codeGarantie;

  private String libelleGarantie;

  private String tauxRemboursement;

  private String libelleCodeRenvoi;

  private PrioriteDroitDto prioriteDroit;

  private List<PeriodeDroitDto> periodeDroits;

  private List<ConventionnementDto> conventionnements;

  private List<PrestationDto> prestations;

  private String referenceCouverture;

  private String libelleCodeRenvoiAdditionnel;
}
