package com.cegedimassurances.norme.tarification.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

public class AIHistoConso {

  @JsonProperty("codeAdeli")
  private String codeAdeli;

  @JsonProperty("codeDomainDroit")
  private String codeDomainDroit;

  @JsonProperty("dateCreation")
  private Date dateCreation;

  @JsonProperty("dateReference")
  private Date dateReference;

  @JsonProperty("idContrat")
  private String idContrat;

  @JsonProperty("ebs")
  private String ebs;

  @JsonProperty("montantConsommee")
  private BigDecimal montantConsommee;

  @JsonProperty("numeroAMC")
  private String numeroAMC;

  @JsonProperty("quantiteConsommee")
  private BigDecimal quantiteConsommee;

  @JsonProperty("sensConso")
  private String sensConso;

  @JsonProperty("idBenef")
  private String idBenef;

  public String getIdBenef() {
    return idBenef;
  }

  public void setIdBenef(String idBenef) {
    this.idBenef = idBenef;
  }

  public String getCodeAdeli() {
    return codeAdeli;
  }

  public void setCodeAdeli(String codeAdeli) {
    this.codeAdeli = codeAdeli;
  }

  public String getCodeDomainDroit() {
    return codeDomainDroit;
  }

  public void setCodeDomainDroit(String codeDomainDroit) {
    this.codeDomainDroit = codeDomainDroit;
  }

  public Date getDateCreation() {
    return dateCreation;
  }

  public void setDateCreation(Date dateCreation) {
    this.dateCreation = dateCreation;
  }

  public Date getDateReference() {
    return dateReference;
  }

  public void setDateReference(Date dateReference) {
    this.dateReference = dateReference;
  }

  public String getIdContrat() {
    return idContrat;
  }

  public void setIdContrat(String idContrat) {
    this.idContrat = idContrat;
  }

  public String getEbs() {
    return ebs;
  }

  public void setEbs(String ebs) {
    this.ebs = ebs;
  }

  public BigDecimal getMontantConsommee() {
    return montantConsommee;
  }

  public void setMontantConsommee(BigDecimal montantConsommee) {
    this.montantConsommee = montantConsommee;
  }

  public String getNumeroAMC() {
    return numeroAMC;
  }

  public void setNumeroAMC(String numeroAMC) {
    this.numeroAMC = numeroAMC;
  }

  public BigDecimal getQuantiteConsommee() {
    return quantiteConsommee;
  }

  public void setQuantiteConsommee(BigDecimal quantiteConsommee) {
    this.quantiteConsommee = quantiteConsommee;
  }

  public String getSensConso() {
    return sensConso;
  }

  public void setSensConso(String sensConso) {
    this.sensConso = sensConso;
  }

  public static AIHistoConso fromString(final String bean) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.readValue(bean, AIHistoConso.class);
    } catch (IOException e) {
      return null;
    }
  }

  public String toJson() {
    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      return null;
    }
  }
}
