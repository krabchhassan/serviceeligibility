package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.domain.BeneficiaireV2;
import com.cegedim.next.serviceeligibility.core.model.domain.Contrat;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.audit.Audit;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.CollectionUtils;

/** Classe qui mappe la collection DECLARATIONS dans la base de donnees. */
@Data
@Document(collection = "declarations")
public class Declaration extends DocumentEntity implements GenericDomain<Declaration>, Audit {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  @Getter @Setter private String referenceExterne;

  @Getter @Setter private String codeEtat;

  @Getter @Setter private Date effetDebut;

  @Getter @Setter private String idDeclarant;

  @Getter @Setter private Boolean isCarteTPaEditer = false;

  @Getter @Setter private String carteTPaEditerOuDigitale = "0";

  @Getter @Setter private String nomFichierOrigine;

  @Getter @Setter private String idTrigger;

  @Getter @Setter private String versionDeclaration;

  /* TRACE */

  private Date dateCreation;
  private String userCreation;
  private Date dateModification;
  private String userModification;

  /* DOCUMENTS EMBEDDED */

  @Getter @Setter private BeneficiaireV2 beneficiaire;

  @Getter @Setter private Contrat contrat;

  @Setter private List<DomaineDroit> domaineDroits;

  @Getter @Setter private String idOrigine;

  @Getter @Setter private String dateRestitution;

  @Getter @Setter private String etatSuspension;

  public Declaration() {
    /* empty constructor */ }

  public Declaration(Declaration source) {
    this.referenceExterne = source.getReferenceExterne();
    this.codeEtat = source.getCodeEtat();
    if (source.getEffetDebut() != null) {
      this.effetDebut = new Date(source.getEffetDebut().getTime());
    }
    this.idDeclarant = source.getIdDeclarant();
    this.isCarteTPaEditer = source.getIsCarteTPaEditer();
    this.carteTPaEditerOuDigitale = source.getCarteTPaEditerOuDigitale();
    this.nomFichierOrigine = source.getNomFichierOrigine();
    this.idTrigger = source.getIdTrigger();
    this.versionDeclaration = source.getVersionDeclaration();
    if (source.getDateCreation() != null) {
      this.dateCreation = new Date(source.getDateCreation().getTime());
    }
    this.userCreation = source.getUserCreation();
    if (source.getDateModification() != null) {
      this.dateModification = new Date(source.getDateModification().getTime());
    }
    this.userModification = source.getUserModification();
    if (source.getBeneficiaire() != null) {
      this.beneficiaire = new BeneficiaireV2(source.getBeneficiaire());
    }
    if (source.getContrat() != null) {
      this.contrat = new Contrat(source.getContrat());
    }

    if (!CollectionUtils.isEmpty(source.getDomaineDroits())) {
      this.domaineDroits = new ArrayList<>();
      for (DomaineDroit dom : source.getDomaineDroits()) {
        this.domaineDroits.add(new DomaineDroit(dom));
      }
    }
    this.idOrigine = source.getIdOrigine();
    this.dateRestitution = source.getDateRestitution();
    this.etatSuspension = source.getEtatSuspension();
  }

  /**
   * Renvoi une liste des domaines de droits. Si il n'y a aucun domaine de droits, une liste vide
   * est renvoyee.
   *
   * @return La liste des domaines de droits.
   */
  public List<DomaineDroit> getDomaineDroits() {
    if (this.domaineDroits == null) {
      this.domaineDroits = new ArrayList<>();
    }
    return this.domaineDroits;
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
  public int compareTo(final Declaration declaration) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.beneficiaire, declaration.beneficiaire);
    compareToBuilder.append(this.codeEtat, declaration.codeEtat);
    compareToBuilder.append(this.contrat, declaration.contrat);
    compareToBuilder.append(this.dateCreation, declaration.dateCreation);
    compareToBuilder.append(this.dateModification, declaration.dateModification);
    compareToBuilder.append(this.idDeclarant, declaration.idDeclarant);
    compareToBuilder.append(this.domaineDroits, declaration.domaineDroits);
    compareToBuilder.append(this.effetDebut, declaration.effetDebut);
    compareToBuilder.append(this.referenceExterne, declaration.referenceExterne);
    compareToBuilder.append(this.userCreation, declaration.userCreation);
    compareToBuilder.append(this.userModification, declaration.userModification);
    compareToBuilder.append(this.isCarteTPaEditer, declaration.isCarteTPaEditer);
    compareToBuilder.append(this.carteTPaEditerOuDigitale, declaration.carteTPaEditerOuDigitale);
    compareToBuilder.append(this.nomFichierOrigine, declaration.nomFichierOrigine);
    compareToBuilder.append(this.versionDeclaration, declaration.versionDeclaration);
    compareToBuilder.append(this.idOrigine, declaration.idOrigine);
    compareToBuilder.append(this.idTrigger, declaration.idTrigger);
    compareToBuilder.append(this.etatSuspension, declaration.etatSuspension);
    return compareToBuilder.toComparison();
  }

  /**
   * (non-Javadoc)
   *
   * <p>//@see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.beneficiaire == null) ? 0 : this.beneficiaire.hashCode());
    result = prime * result + ((this.codeEtat == null) ? 0 : this.codeEtat.hashCode());
    result = prime * result + ((this.contrat == null) ? 0 : this.contrat.hashCode());
    result = prime * result + ((this.dateCreation == null) ? 0 : this.dateCreation.hashCode());
    result =
        prime * result + ((this.dateModification == null) ? 0 : this.dateModification.hashCode());
    result = prime * result + ((this.idDeclarant == null) ? 0 : this.idDeclarant.hashCode());
    result = prime * result + ((this.domaineDroits == null) ? 0 : this.domaineDroits.hashCode());
    result = prime * result + ((this.effetDebut == null) ? 0 : this.effetDebut.hashCode());
    result =
        prime * result + ((this.referenceExterne == null) ? 0 : this.referenceExterne.hashCode());
    result = prime * result + ((this.userCreation == null) ? 0 : this.userCreation.hashCode());
    result =
        prime * result + ((this.userModification == null) ? 0 : this.userModification.hashCode());
    result =
        prime * result + ((this.isCarteTPaEditer == null) ? 0 : this.isCarteTPaEditer.hashCode());
    result =
        prime * result
            + ((this.carteTPaEditerOuDigitale == null)
                ? 0
                : this.carteTPaEditerOuDigitale.hashCode());
    result =
        prime * result + ((this.nomFichierOrigine == null) ? 0 : this.nomFichierOrigine.hashCode());
    result =
        prime * result
            + ((this.versionDeclaration == null) ? 0 : this.versionDeclaration.hashCode());
    result = prime * result + ((this.idOrigine == null) ? 0 : this.idOrigine.hashCode());
    result = prime * result + ((this.idTrigger == null) ? 0 : this.idTrigger.hashCode());
    result = prime * result + ((this.etatSuspension == null) ? 0 : this.etatSuspension.hashCode());
    return result;
  }

  /**
   * (non-Javadoc)
   *
   * <p>//@see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Declaration other)) {
      return false;
    }
    if (this.beneficiaire == null) {
      if (other.beneficiaire != null) {
        return false;
      }
    } else if (!this.beneficiaire.equals(other.beneficiaire)) {
      return false;
    }
    if (this.codeEtat == null) {
      if (other.codeEtat != null) {
        return false;
      }
    } else if (!this.codeEtat.equals(other.codeEtat)) {
      return false;
    }
    if (this.contrat == null) {
      if (other.contrat != null) {
        return false;
      }
    } else if (!this.contrat.equals(other.contrat)) {
      return false;
    }
    if (this.dateCreation == null) {
      if (other.dateCreation != null) {
        return false;
      }
    } else if (!this.dateCreation.equals(other.dateCreation)) {
      return false;
    }
    if (this.dateModification == null) {
      if (other.dateModification != null) {
        return false;
      }
    } else if (!this.dateModification.equals(other.dateModification)) {
      return false;
    }
    if (this.idDeclarant == null) {
      if (other.idDeclarant != null) {
        return false;
      }
    } else if (!this.idDeclarant.equals(other.idDeclarant)) {
      return false;
    }
    if (this.domaineDroits == null) {
      if (other.domaineDroits != null) {
        return false;
      }
    } else {
      Collections.sort(this.domaineDroits);
      Collections.sort(other.domaineDroits);
      if (!this.domaineDroits.equals(other.domaineDroits)) {
        return false;
      }
    }
    if (this.effetDebut == null) {
      if (other.effetDebut != null) {
        return false;
      }
    } else if (!this.effetDebut.equals(other.effetDebut)) {
      return false;
    }
    if (this.referenceExterne == null) {
      if (other.referenceExterne != null) {
        return false;
      }
    } else if (!this.referenceExterne.equals(other.referenceExterne)) {
      return false;
    }
    if (this.userCreation == null) {
      if (other.userCreation != null) {
        return false;
      }
    } else if (!this.userCreation.equals(other.userCreation)) {
      return false;
    }
    if (this.userModification == null) {
      if (other.userModification != null) {
        return false;
      }
    } else if (!this.userModification.equals(other.userModification)) {
      return false;
    }
    if (this.isCarteTPaEditer == null) {
      if (other.isCarteTPaEditer != null) {
        return false;
      }
    } else if (!this.isCarteTPaEditer.equals(other.isCarteTPaEditer)) {
      return false;
    }
    if (this.carteTPaEditerOuDigitale == null) {
      if (other.carteTPaEditerOuDigitale != null) {
        return false;
      }
    } else if (!this.carteTPaEditerOuDigitale.equals(other.carteTPaEditerOuDigitale)) {
      return false;
    }
    if (this.nomFichierOrigine == null) {
      if (other.nomFichierOrigine != null) {
        return false;
      }
    } else if (!this.nomFichierOrigine.equals(other.nomFichierOrigine)) {
      return false;
    }
    if (this.versionDeclaration == null) {
      if (other.versionDeclaration != null) {
        return false;
      }
    } else if (!this.versionDeclaration.equals(other.versionDeclaration)) {
      return false;
    }
    if (this.idOrigine == null) {
      if (other.idOrigine != null) {
        return false;
      }
    } else if (!this.idOrigine.equals(other.idOrigine)) {
      return false;
    }
    if (this.etatSuspension == null) {
      if (other.etatSuspension != null) {
        return false;
      }
    } else if (!this.etatSuspension.equals(other.etatSuspension)) {
      return false;
    }
    if (this.idTrigger == null) {
      return other.idTrigger == null;
    } else {
      return this.idTrigger.equals(other.idTrigger);
    }
  }
}
