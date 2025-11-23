package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class InfosAssureDto implements GenericDto {

  private static final long serialVersionUID = 5739963355606495559L;

  private boolean externalOrigin = false;

  private String id;

  private Date effetDebut;

  private String nom;

  private String prenom;

  private IdentificationAssureDto identification;

  private ContratDto contrat;

  private DroitsDto droits;

  private HistoriqueDeclarationsDto historique;

  private Boolean isRestitutionCarte = false;

  private String dateRestitution;

  private List<AttestationDto> attestations;

  private Boolean isLastDeclaration;

  private TraceDto trace;

  private Boolean isTdb;
}
