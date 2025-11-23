package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.transco;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;

/** Classe DTO de l'entite {@code ServiceDroits}. */
@JsonInclude(Include.NON_NULL)
public class ServiceDroitsDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String nom;
  private String id;
  private List<String> listTransco;

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

  public List<String> getListTransco() {
    return listTransco;
  }

  public void setListTransco(List<String> listTransco) {
    this.listTransco = listTransco;
  }
}
