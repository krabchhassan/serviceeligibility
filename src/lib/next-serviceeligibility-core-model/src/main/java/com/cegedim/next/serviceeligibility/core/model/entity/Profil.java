package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.domain.audit.Audit;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.CollectionUtils;

/** Classe qui mappe la collection profils dans la base de donnees. */
@Document(collection = "profils")
public class Profil extends DocumentEntity implements GenericDomain<Profil>, Audit {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  /**
   * code Profil Dans la base de donnees cette propriete presente le code d'un profil et elle est
   * unique. Dans le Dto: elle sera remplacer par identifiant profil.
   */
  @Getter @Setter private String code;

  /* TRACE */
  private Date dateCreation;
  private String userCreation;
  private Date dateModification;
  private String userModification;

  @Getter @Setter private List<String> roles;

  public Profil() {
    /* empty constructor */ }

  public Profil(Profil source) {
    this.code = source.getCode();
    if (source.getDateCreation() != null) {
      this.dateCreation = new Date(source.getDateCreation().getTime());
    }
    this.userCreation = source.getUserCreation();
    if (source.getDateModification() != null) {
      this.dateModification = new Date(source.getDateModification().getTime());
    }
    this.userModification = source.getUserModification();
    if (!CollectionUtils.isEmpty(source.getRoles())) {
      this.roles = new ArrayList<>();
      this.roles.addAll(source.getRoles());
    }
  }

  @Override
  public Date getDateCreation() {
    return this.dateCreation;
  }

  @Override
  public void setDateCreation(Date dateCreation) {
    this.dateCreation = dateCreation;
  }

  @Override
  public String getUserCreation() {
    return this.userCreation;
  }

  @Override
  public void setUserCreation(String userCreation) {
    this.userCreation = userCreation;
  }

  @Override
  public Date getDateModification() {
    return this.dateModification;
  }

  @Override
  public void setDateModification(Date dateModification) {
    this.dateModification = dateModification;
  }

  @Override
  public String getUserModification() {
    return this.userModification;
  }

  @Override
  public void setUserModification(String userModification) {
    this.userModification = userModification;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((code == null) ? 0 : code.hashCode());
    result = prime * result + ((dateCreation == null) ? 0 : dateCreation.hashCode());
    result = prime * result + ((dateModification == null) ? 0 : dateModification.hashCode());
    result = prime * result + ((roles == null) ? 0 : roles.hashCode());
    result = prime * result + ((userCreation == null) ? 0 : userCreation.hashCode());
    result = prime * result + ((userModification == null) ? 0 : userModification.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Profil other = (Profil) obj;
    if (code == null) {
      if (other.code != null) return false;
    } else if (!code.equals(other.code)) return false;
    if (dateCreation == null) {
      if (other.dateCreation != null) return false;
    } else if (!dateCreation.equals(other.dateCreation)) return false;
    if (dateModification == null) {
      if (other.dateModification != null) return false;
    } else if (!dateModification.equals(other.dateModification)) return false;
    if (roles == null) {
      if (other.roles != null) return false;
    } else if (!roles.equals(other.roles)) return false;
    if (userCreation == null) {
      if (other.userCreation != null) return false;
    } else if (!userCreation.equals(other.userCreation)) return false;
    if (userModification == null) {
      return other.userModification == null;
    } else return userModification.equals(other.userModification);
  }

  @Override
  public int compareTo(Profil profil) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.get_id(), profil.get_id());
    return compareToBuilder.toComparison();
  }
}
