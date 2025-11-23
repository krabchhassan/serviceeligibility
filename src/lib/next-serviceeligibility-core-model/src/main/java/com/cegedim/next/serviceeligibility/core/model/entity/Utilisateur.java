package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.domain.audit.Audit;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

/** Classe qui mappe la collection Habilitations dans la base de donnees. */
@Document(collection = "users")
@Data
@EqualsAndHashCode(callSuper = false)
public class Utilisateur extends DocumentEntity implements GenericDomain<Utilisateur>, Audit {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String identifiant;
  private String idProfil;

  /* TRACE */

  private Date dateCreation;
  private String userCreation;
  private Date dateModification;
  private String userModification;

  /* GETTERS SETTERS */

  @Override
  public Date getDateCreation() {
    return dateCreation;
  }

  @Override
  public void setDateCreation(Date dateCreation) {
    this.dateCreation = dateCreation;
  }

  @Override
  public String getUserCreation() {
    return userCreation;
  }

  @Override
  public void setUserCreation(String userCreation) {
    this.userCreation = userCreation;
  }

  @Override
  public Date getDateModification() {
    return dateModification;
  }

  @Override
  public void setDateModification(Date dateModification) {
    this.dateModification = dateModification;
  }

  @Override
  public String getUserModification() {
    return userModification;
  }

  @Override
  public void setUserModification(String userModification) {
    this.userModification = userModification;
  }

  @Override
  public int compareTo(Utilisateur user) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.identifiant, user.getIdentifiant());
    return compareToBuilder.toComparison();
  }
}
