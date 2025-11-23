package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;

/** Classe DTO - un element dans la liste declarants - Mes derniers modifies. */
@JsonInclude(Include.NON_EMPTY)
public class ServicesDeclarantDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String numero;

  private String nom;

  private String libelle;

  private String user;

  private List<ServiceCouloirsDto> services;

  /* GETTERS SETTERS */

  public String getNom() {
    return nom;
  }

  public String getNumero() {
    return numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public String getLibelle() {
    return libelle;
  }

  public void setLibelle(String libelle) {
    this.libelle = libelle;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public List<ServiceCouloirsDto> getServices() {
    return services;
  }

  public void setServices(List<ServiceCouloirsDto> services) {
    this.services = services;
  }
}
