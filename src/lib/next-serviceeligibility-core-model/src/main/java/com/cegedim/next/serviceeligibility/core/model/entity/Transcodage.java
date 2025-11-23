package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.domain.audit.Audit;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.CollectionUtils;

/** Classe qui mappe la collection TRANSCODAGE dans la base de donnees. */
@Document(collection = "transcodage")
@Data
@EqualsAndHashCode(callSuper = false)
public class Transcodage extends DocumentEntity implements GenericDomain<Transcodage>, Audit {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String codeService;
  private String codeObjetTransco;
  private List<String> cle;
  private String codeTransco;

  /* TRACE */

  private Date dateCreation;
  private String userCreation;
  private Date dateModification;
  private String userModification;

  public Transcodage() {
    /* empty constructor */ }

  public Transcodage(Transcodage source) {
    this.codeService = source.getCodeService();
    this.codeObjetTransco = source.getCodeObjetTransco();

    if (!CollectionUtils.isEmpty(source.getCle())) {
      this.cle = new ArrayList<>();
      this.cle.addAll(source.getCle());
    }
    this.codeTransco = source.getCodeTransco();

    /* TRACE */

    if (source.getDateCreation() != null) {
      this.dateCreation = new Date(source.getDateCreation().getTime());
    }
    this.userCreation = source.getUserCreation();
    if (source.getDateModification() != null) {
      this.dateModification = new Date(source.getDateModification().getTime());
    }
    this.userModification = source.getUserModification();
  }

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
  public int compareTo(final Transcodage transcodage) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.codeService, transcodage.codeService);
    compareToBuilder.append(this.cle, transcodage.cle);
    compareToBuilder.append(this.codeTransco, transcodage.codeTransco);
    return compareToBuilder.toComparison();
  }
}
