package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.List;

/** Classe DTO de l'entite {@code Adresse}. */
public class CommunicationDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  private List<String> lignesAdresse;

  private String telephone;

  private String email;

  private String type;

  private String codePostal;

  public List<String> getLignesAdresse() {
    return lignesAdresse;
  }

  public void setLignesAdresse(List<String> lignesAdresse) {
    this.lignesAdresse = lignesAdresse;
  }

  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getCodePostal() {
    return codePostal;
  }

  public void setCodePostal(String codePostal) {
    this.codePostal = codePostal;
  }
}
