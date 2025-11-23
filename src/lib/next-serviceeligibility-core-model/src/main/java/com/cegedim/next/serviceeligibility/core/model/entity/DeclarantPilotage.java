package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.domain.*;
import com.cegedim.next.serviceeligibility.core.model.domain.audit.Audit;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.beans.Transient;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

/** Classe qui mappe la collection DECLARANTS dans la base de donnees. */
@Document(collection = "declarants")
public class DeclarantPilotage extends DocumentEntity
    implements GenericDomain<DeclarantPilotage>, Audit {

  private static final long serialVersionUID = 1L;

  /* Les types possibles de declarants */
  public static final String TYPE_AMC = "amc";
  public static final String TYPE_AMO = "amo";

  /* PROPRIETES */

  @Getter @Setter private String idClientBO;

  @Getter @Setter private String nom;

  @Getter @Setter private String libelle;

  @Getter @Setter private String siret;

  @Getter @Setter private String numeroPrefectoral;

  @Getter @Setter private String codePartenaire;

  @Getter @Setter private String type = TYPE_AMC;

  @Getter @Setter private String codeCircuit;

  @Getter @Setter private String emetteurDroits;

  @Getter @Setter private String operateurPrincipal;

  /* TRACE */
  private Date dateCreation;
  private String userCreation;
  private Date dateModification;
  private String userModification;

  /* DOCUMENTS EMBEDDED */

  @Getter @Setter private Pilotage pilotages;

  public DeclarantPilotage() {
    /* empty constructor */ }

  public DeclarantPilotage(DeclarantPilotage source) {
    this.idClientBO = source.getIdClientBO();
    this.nom = source.getNom();
    this.libelle = source.getLibelle();
    this.siret = source.getSiret();
    this.numeroPrefectoral = source.getNumeroPrefectoral();
    this.codePartenaire = source.getCodePartenaire();
    this.type = source.getType();
    this.codeCircuit = source.getCodeCircuit();
    this.emetteurDroits = source.getEmetteurDroits();
    this.operateurPrincipal = source.getOperateurPrincipal();
    if (source.getDateCreation() != null) {
      this.dateCreation = new Date(source.getDateCreation().getTime());
    }
    this.userCreation = source.getUserCreation();
    if (source.getDateModification() != null) {
      this.dateModification = new Date(source.getDateModification().getTime());
    }
    this.userModification = source.getUserModification();
    this.pilotages = source.getPilotages();
  }

  /* GETTERS SETTERS */

  @Transient
  public boolean isAMC() {
    return TYPE_AMC.equals(type);
  }

  @Transient
  public boolean isAMO() {
    return TYPE_AMO.equals(type);
  }

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
  public int compareTo(DeclarantPilotage declarant) {

    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.numeroPrefectoral, declarant.numeroPrefectoral);
    compareToBuilder.append(this.siret, declarant.siret);
    compareToBuilder.append(this.type, declarant.type);
    compareToBuilder.append(this.codePartenaire, declarant.codePartenaire);
    compareToBuilder.append(this.codeCircuit, declarant.codeCircuit);
    compareToBuilder.append(this.emetteurDroits, declarant.emetteurDroits);
    compareToBuilder.append(this.operateurPrincipal, declarant.operateurPrincipal);
    return compareToBuilder.toComparison();
  }

  /** (non-Javadoc) //@see java.lang.Object#hashCode() */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result =
        prime * result + ((this.numeroPrefectoral == null) ? 0 : this.numeroPrefectoral.hashCode());
    result = prime * result + ((this.siret == null) ? 0 : this.siret.hashCode());
    result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
    result = prime * result + ((this.codePartenaire == null) ? 0 : this.codePartenaire.hashCode());
    result = prime * result + ((this.codeCircuit == null) ? 0 : this.codeCircuit.hashCode());
    result = prime * result + ((this.emetteurDroits == null) ? 0 : this.emetteurDroits.hashCode());
    result =
        prime * result
            + ((this.operateurPrincipal == null) ? 0 : this.operateurPrincipal.hashCode());
    return result;
  }

  /** (non-Javadoc) //@see java.lang.Object#equals(java.lang.Object) */
  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }

    if (obj == null) {
      return false;
    }

    if (!(obj instanceof DeclarantPilotage)) {
      return false;
    }

    DeclarantPilotage other = (DeclarantPilotage) obj;

    if (this.numeroPrefectoral == null) {
      if (other.numeroPrefectoral != null) {
        return false;
      }
    } else if (!this.numeroPrefectoral.equals(other.numeroPrefectoral)) {
      return false;
    }

    if (this.siret == null) {
      if (other.siret != null) {
        return false;
      }
    } else if (!this.siret.equals(other.siret)) {
      return false;
    }

    if (this.type == null) {
      if (other.type != null) {
        return false;
      }
    } else if (!this.type.equals(other.type)) {
      return false;
    }

    if (this.codePartenaire == null) {
      if (other.codePartenaire != null) {
        return false;
      }
    } else if (!this.codePartenaire.equals(other.codePartenaire)) {
      return false;
    }

    if (this.codeCircuit == null) {
      if (other.codeCircuit != null) {
        return false;
      }
    } else if (!this.codeCircuit.equals(other.codeCircuit)) {
      return false;
    }

    if (this.emetteurDroits == null) {
      if (other.emetteurDroits != null) {
        return false;
      }
    } else if (!this.emetteurDroits.equals(other.emetteurDroits)) {
      return false;
    }

    if (this.operateurPrincipal == null) {
      return other.operateurPrincipal == null;
    } else return this.operateurPrincipal.equals(other.operateurPrincipal);
  }
}
