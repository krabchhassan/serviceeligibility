package com.cegedim.next.serviceeligibility.core.model.entity.card.cardresponse;

import lombok.Data;

@Data
public class CardResponseContrat {
  private String numero;
  private String numeroAdherent;
  private String numeroAdherentComplet;
  private String nomPorteur;
  private String prenomPorteur;
  private String civilitePorteur;
  private String numeroContratCollectif;
  private String numeroExterneContratIndividuel;
  private String numeroExterneContratCollectif;
  private Boolean isContratResponsable;
  private Boolean isContratCMU;
  private String contratCMUC2S;
  private String individuelOuCollectif;
  private String critereSecondaire;
  private String critereSecondaireDetaille;
  private String gestionnaire;
  private String groupeAssures;
  private String numeroCarte;
  private String editeurCarte;
  private String ordrePriorisation;
  private String identifiantCollectivite;
  private String raisonSociale;
  private String siret;
  private String groupePopulation;
}
