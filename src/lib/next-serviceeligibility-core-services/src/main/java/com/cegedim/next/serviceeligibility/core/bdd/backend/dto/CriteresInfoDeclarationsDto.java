package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import java.util.List;
import lombok.Data;

/** Critères de recherches des déclarations pour l'IHM de consultation de la BDD */
@Data
public class CriteresInfoDeclarationsDto {

  private List<String> codesAMC;
  private String dateDebPeriode;
  private String dateFinPeriode;
  private String nirBeneficiaire;
  private String numeroAdherent;
  private String numeroContrat;
  private String codeDomaineDroit;
  private int indexRecherche;
  private int nbResultats;
  private int nbParPage;

  @Override
  public String toString() {
    return "CriteresInfoDeclarations [codesAMC="
        + codesAMC
        + ", dateDebPeriode="
        + dateDebPeriode
        + ", dateFinPeriode="
        + dateFinPeriode
        + ", nirBeneficiaire="
        + nirBeneficiaire
        + ", numeroAdherent="
        + numeroAdherent
        + ", numeroContrat="
        + numeroContrat
        + ", codeDomaineDroit="
        + codeDomaineDroit
        + ", indexRecherche="
        + indexRecherche
        + ", nbResultats="
        + nbResultats
        + ", nbParPage="
        + nbParPage
        + "]";
  }
}
