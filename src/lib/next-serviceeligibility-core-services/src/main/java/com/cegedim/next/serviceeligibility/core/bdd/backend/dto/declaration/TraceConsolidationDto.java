package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ErreursDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import java.util.List;

/** Classe TracePriorisationDto, gere les traces de priorisation */
public class TraceConsolidationDto implements GenericDto {

  /** */
  private static final long serialVersionUID = 1L;

  private String dateExecution;
  private ErreursDto erreur;
  private String nomFichierARL;
  private List<TraceExtractionDto> extractions;
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

  public List<TraceExtractionDto> getExtractions() {
    return extractions;
  }

  public void setExtractions(List<TraceExtractionDto> extractions) {
    this.extractions = extractions;
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
