package com.cegedim.next.serviceeligibility.almerys608.model;

import com.cegedim.next.serviceeligibility.core.job.batch.BulkObject;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "almv3_MembreContrat")
public class MembreContrat extends BulkObject {

  private String numeroContrat;
  private Integer souscripteur;
  private String position;
  private String typeRegime;
  private String dateEntree;
  private String refInterneOs;
  private String dateNaissance;
  private String rangNaissance;
  private String nomPatronimique;
  private String nomUsage;
  private String prenom;
  private Integer medecinTraitant;
  private Integer autonome;
  private String modePaiement;
  private String nni;
  private String nniRatt;
  private String regimeSpecial;
  private String dateRadiation;
}
