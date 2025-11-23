package com.cegedim.next.serviceeligibility.almerys608.model;

import com.cegedim.next.serviceeligibility.core.job.batch.BulkObject;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/** infoBenef */
@Data
@Document(collection = "almv3_Beneficiaire")
public class Beneficiaire extends BulkObject {

  private String numeroContrat;
  private String refInterneOS;
  private String typeBenef;
  private Integer contratResponsable;
  private String codeGrandRegime;
  private String Nni; // NOSONAR
  private String codeCaisseRo;
  private String centreSS;
  private String codeGrandRegime2;
  private String Nni2; // NOSONAR
  private String codeCaisseRo2;
  private String centreSS2;
  private Integer noemise;
  private String codeMouvementCarte;
  private String fondCarte;
  private String annexe1Carte;
  private String annexe2Carte;
}
