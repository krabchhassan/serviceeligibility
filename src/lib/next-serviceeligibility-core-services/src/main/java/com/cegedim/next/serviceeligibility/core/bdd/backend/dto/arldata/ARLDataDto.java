package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.arldata;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.opencsv.bean.CsvBindByName;
import java.io.Serial;
import lombok.Data;

@Data
public class ARLDataDto implements GenericDto {

  @Serial private static final long serialVersionUID = 1L;

  @CsvBindByName(column = "TRAITEMENT_LE")
  private String dateTraitement;

  @CsvBindByName(column = "SERVICE")
  private String codeService;

  @CsvBindByName(column = "NO_AMC")
  private String amcNumber;

  @CsvBindByName(column = "CONVENTION")
  private String convention;

  @CsvBindByName(column = "REGROUPEMENT")
  private String regroupement;

  @CsvBindByName(column = "REGROUPEMENT_DETAILLE")
  private String regroupementDetaille;

  @CsvBindByName(column = "NO_PERSONNE")
  private String numeroPersonne;

  @CsvBindByName(column = "NIR")
  private String nir;

  @CsvBindByName(column = "DATE_NAISSANCE")
  private String dateNaissance;

  @CsvBindByName(column = "RANG_NAISSANCE")
  private String rangNaissance;

  @CsvBindByName(column = "NOM")
  private String nom;

  @CsvBindByName(column = "PRENOM")
  private String prenom;

  @CsvBindByName(column = "NO_CONTRAT")
  private String numeroContrat;

  @CsvBindByName(column = "GESTIONNAIRE_CONTRAT")
  private String gestionnaireContrat;

  @CsvBindByName(column = "GROUPE_ASSURES")
  private String groupeAssures;

  @CsvBindByName(column = "DROITS")
  private String droits;

  @CsvBindByName(column = "MVT")
  private String mouvement;

  @CsvBindByName(column = "DATE_DECLARATION")
  private String dateDeclaration;

  @CsvBindByName(column = "TYPE_REJET")
  private String typeRejet;

  @CsvBindByName(column = "NIVEAU_REJET")
  private String niveauRejet;

  @CsvBindByName(column = "REJET")
  private String rejet;

  @CsvBindByName(column = "MOTIF_REJET")
  private String motifRejet;

  @CsvBindByName(column = "VALEUR_REJET")
  private String valeurRejet;

  // declarationId
  @CsvBindByName(column = "NOM_FICHIER_NO_DECLARATION")
  private String nomFichierNumDeclaration;

  @CsvBindByName(column = "EMETTEUR_DROITS")
  private String emetteurDroits;

  @CsvBindByName(column = "CODE_CIRCUIT")
  private String codeCircuit;

  @CsvBindByName(column = "LIBELLE_CIRCUIT")
  private String libelleCircuit;

  @CsvBindByName(column = "DESTINATAIRE_DROITS")
  private String destinataireDroits;

  @CsvBindByName(column = "BO_EMETTEUR_DROITS")
  private String boEmetteurDroits;

  private String codeClient;
}
