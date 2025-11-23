package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;

/** Classe TraceRetourDto, gere les traces de retour */
public class TraceRetourDto implements GenericDto {

  /** */
  private static final long serialVersionUID = 1L;

  private String dateExecution;
  private String rejet;
  private String nomFichier;

  @JsonIgnore private Date dateExecutionDate;

  public String getDateExecution() {
    return dateExecution;
  }

  public void setDateExecution(String dateExecution) {
    this.dateExecution = dateExecution;
  }

  public String getRejet() {
    return rejet;
  }

  public void setRejet(String rejet) {
    this.rejet = rejet;
  }

  public String getNomFichier() {
    return nomFichier;
  }

  public void setNomFichier(String nomFichier) {
    this.nomFichier = nomFichier;
  }

  public Date getDateExecutionDate() {
    return dateExecutionDate;
  }

  public void setDateExecutionDate(Date dateExecutionDate) {
    this.dateExecutionDate = dateExecutionDate;
  }
}
