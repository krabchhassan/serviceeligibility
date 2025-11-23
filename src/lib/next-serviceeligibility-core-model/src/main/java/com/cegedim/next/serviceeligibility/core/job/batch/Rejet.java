package com.cegedim.next.serviceeligibility.core.job.batch;

import java.util.Date;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "almv3_Rejet")
public class Rejet extends BulkObject {
  private String noPersonne;
  private String nir;
  private String dateNaissance;
  private String rangNaissance;
  private String nom;
  private String prenom;
  private String numContrat;
  private String gestionnaireContrat;
  private String groupeAssures;
  private String droits;
  private String mvt;
  private String dateDeclaration;
  private String noDeclaration;
  private String idDeclarationConsolidee;
  private String critereSecondaire;
  private String critereSecondaireDetaille;
  private String origineDroits;
  private String codeRejetTraces;
  private String error;
  private String refInterneOS; // pour rejet produit exclu
  private Date effetDebut;
}
