package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails;

import java.util.List;
import lombok.Data;

@Data
public class ConsolidatedContractDto {
  private String idDeclarant;
  private String numeroContrat;
  private String numeroAdherent;
  private String numeroAdherentComplet;
  private String dateSouscription;
  private String dateResiliation;
  private String type;
  private String nomPorteur;
  private String prenomPorteur;
  private String civilitePorteur;
  private String qualification;
  private String numeroContratCollectif;
  private Boolean isContratResponsable;
  private Boolean isContratCMU;
  private String destinataire;
  private String individuelOuCollectif;
  private String typeConvention;
  private String critereSecondaire;
  private String critereSecondaireDetaille;
  private String numeroExterneContratIndividuel;
  private String numeroExterneContratCollectif;
  private String gestionnaire;
  private String groupeAssures;
  private String numAMCEchange;
  private String numOperateur;
  private String ordrePriorisation;
  private String contratCMUC2S;
  private String dateRestitution;
  private String dateConsolidation;
  private String identifiantCollectivite;
  private String raisonSociale;
  private String siret;
  private String groupePopulation;
  private SuspensionContractDto suspension;
  private List<PeriodeContractDto> periodesContratResponsable;
  private List<CodePeriodeContractDto> periodesContratCMU;
  private List<BeneficiaireContractDto> beneficiaires;
}
