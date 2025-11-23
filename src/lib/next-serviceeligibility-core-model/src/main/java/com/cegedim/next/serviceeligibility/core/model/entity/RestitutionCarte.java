package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.domain.audit.Audit;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

/** Classe qui mappe la collection restitutionCarte dans la base de donnees. */
@Document(collection = "restitutionCarte")
@Data
@EqualsAndHashCode(callSuper = false)
public class RestitutionCarte extends DocumentEntity
    implements GenericDomain<RestitutionCarte>, Audit {

  private static final long serialVersionUID = 7405803464318546140L;

  private Date effetDebut;
  private String idDeclarant;
  private String numeroPersonne;
  private String numeroAdherent;
  private String numeroContrat;
  private String dateNaissance;
  private String rangNaissance;
  private String nirOd1;
  private String cleNirOd1;
  private String dateRestitutionCarte;

  /* TRACE */
  private Date dateCreation;
  private String userCreation;
  private Date dateModification;
  private String userModification;

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
  public int compareTo(RestitutionCarte restit) {
    CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.effetDebut, restit.effetDebut);
    compareToBuilder.append(this.idDeclarant, restit.idDeclarant);
    compareToBuilder.append(this.numeroPersonne, restit.numeroPersonne);
    compareToBuilder.append(this.numeroAdherent, restit.numeroAdherent);
    compareToBuilder.append(this.numeroContrat, restit.numeroContrat);
    compareToBuilder.append(this.dateNaissance, restit.dateNaissance);
    compareToBuilder.append(this.rangNaissance, restit.rangNaissance);
    compareToBuilder.append(this.nirOd1, restit.nirOd1);
    compareToBuilder.append(this.cleNirOd1, restit.cleNirOd1);
    compareToBuilder.append(this.dateRestitutionCarte, restit.dateRestitutionCarte);
    return compareToBuilder.toComparison();
  }
}
