package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;
import java.util.List;

/** Classe DTO du pilotage. */
@JsonInclude(Include.NON_NULL)
public class PilotageDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String nom;
  private String id;
  private Boolean serviceOuvert;
  private String typeService;
  private Boolean isCarteEditable;

  /* DOCUMENTS EMBEDDED */

  private List<InfoPilotageDto> regroupements;

  /* GETTERS SETTERS */

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Boolean getServiceOuvert() {
    return serviceOuvert;
  }

  public void setServiceOuvert(Boolean serviceOuvert) {
    this.serviceOuvert = serviceOuvert;
  }

  public List<InfoPilotageDto> getRegroupements() {
    if (this.regroupements == null) {
      this.regroupements = new ArrayList<>();
    }
    return this.regroupements;
  }

  public void setRegroupements(List<InfoPilotageDto> regroupements) {
    this.regroupements = regroupements;
  }

  public String getTypeService() {
    return typeService;
  }

  public void setTypeService(String typeService) {
    this.typeService = typeService;
  }

  public Boolean getIsCarteEditable() {
    return isCarteEditable;
  }

  public void setIsCarteEditable(Boolean isCarteEditable) {
    this.isCarteEditable = isCarteEditable;
  }
}
