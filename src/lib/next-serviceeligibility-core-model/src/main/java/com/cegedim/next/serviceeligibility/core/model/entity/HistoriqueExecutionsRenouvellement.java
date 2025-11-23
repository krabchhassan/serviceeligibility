package com.cegedim.next.serviceeligibility.core.model.entity;

import java.util.Date;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/** Classe qui mappe la collection historiqueExecutionsRenouvellement dans la base de donnees. */
@Document(collection = "historiqueExecutionsRenouvellement")
@Data
public class HistoriqueExecutionsRenouvellement {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String dateTraitement;
  private int nombreTriggersCreesRenouvellement;
  private int nombreTriggeredBeneficiariesCreesRenouvellement;
  private int nombreTriggersCreesAnniversaire;
  private int nombreTriggeredBeneficiariesCreesAnniversaire;
  private Date dateExecution;
  private Date dateCreation;
  private boolean isRdo;
}
