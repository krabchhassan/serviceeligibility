package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ErreursDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import java.util.List;

/** Classe TraceExtractionDto, gere les traces d'extraction */
public class TraceExtractionDto implements GenericDto {

  /** */
  private static final long serialVersionUID = 1L;

  private String dateExecution;
  private ErreursDto erreur;
  private String nomFichierARL;
  private String emetteur;
  private String nomFichier;
  private Integer numeroFichier;
  private String client;
  private List<TraceRetourDto> retours;
  private Boolean isTraceValide;

  @JsonIgnore private Date dateExecutionDate;
  @JsonIgnore private String codeService;

  public String getDateExecution() {
    return dateExecution;
  }

  public void setDateExecution(String dateExecution) {
    this.dateExecution = dateExecution;
  }

  public String getNomFichierARL() {
    return nomFichierARL;
  }

  public void setNomFichierARL(String nomFichierARL) {
    this.nomFichierARL = nomFichierARL;
  }

  public String getEmetteur() {
    return emetteur;
  }

  public void setEmetteur(String emetteur) {
    this.emetteur = emetteur;
  }

  public String getNomFichier() {
    return nomFichier;
  }

  public void setNomFichier(String nomFichier) {
    this.nomFichier = nomFichier;
  }

  public Integer getNumeroFichier() {
    return numeroFichier;
  }

  public void setNumeroFichier(Integer numeroFichier) {
    this.numeroFichier = numeroFichier;
  }

  public String getClient() {
    return client;
  }

  public void setClient(String client) {
    this.client = client;
  }

  public List<TraceRetourDto> getRetours() {
    return retours;
  }

  public void setRetours(List<TraceRetourDto> retours) {
    this.retours = retours;
  }

  public ErreursDto getErreur() {
    return erreur;
  }

  public void setErreur(ErreursDto erreur) {
    this.erreur = erreur;
  }

  public Date getDateExecutionDate() {
    return dateExecutionDate;
  }

  public void setDateExecutionDate(Date dateExecutionDate) {
    this.dateExecutionDate = dateExecutionDate;
  }

  public Boolean getIsTraceValide() {
    return isTraceValide;
  }

  public void setIsTraceValide(Boolean isTraceValide) {
    this.isTraceValide = isTraceValide;
  }

  public String getCodeService() {
    return codeService;
  }

  public void setCodeService(String codeService) {
    this.codeService = codeService;
  }
}
