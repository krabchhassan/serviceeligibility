package com.cegedim.next.serviceeligibility.core.model.domain.contract;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ExtractedContract {

  // -- Contract --
  private String idDeclarant;
  private String numeroContrat;
  private String numeroAdherent;
  private String dateSouscription;
  private String dateResiliation;
  private String dateRestitution;

  // -- BeneficiaireContract --
  // private String dateRadiation;
  private LocalDateTime dateCreation;
  private LocalDateTime dateModification;
  private String dateNaissance;
  private String rangNaissance;
  private String nirBeneficiaire;
  private String cleNirBeneficiaire;
  private String nirOd1;
  private String cleNirOd1;
  private String nirOd2;
  private String cleNirOd2;
  // private String numeroPersonne;

  private List<ExtractedDomain> domains;
}
