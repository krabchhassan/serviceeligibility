package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.domain.AmcBeneficiaire;
import com.cegedim.next.serviceeligibility.core.model.domain.AuditBeneficiaire;
import com.cegedim.next.serviceeligibility.core.model.domain.ContratBeneficiaire;
import com.cegedim.next.serviceeligibility.core.model.domain.DataBeneficiaire;
import com.cegedim.next.serviceeligibility.core.model.domain.audit.Audit;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

/** Classe qui mappe la collection BENEFICIAIRE dans la base de donnees. */
@Data
@EqualsAndHashCode(callSuper = false)
@Document(collection = "beneficiaires")
public class Beneficiaire extends DocumentEntity implements GenericDomain<Beneficiaire>, Audit {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String idClientBO;
  private String numeroAdherent;
  private List<String> services;

  /* CLES ETRANGERES */
  private AmcBeneficiaire amc;
  private ContratBeneficiaire contrat;
  private IdentiteContrat identite;
  private DataBeneficiaire data;
  private AuditBeneficiaire audit;

  /* TRACE */

  private Date dateCreation;
  private String userCreation;
  private Date dateModification;
  private String userModification;

  /* METHODS */

  @Override
  public int compareTo(final Beneficiaire beneficiaire) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.dateCreation, beneficiaire.dateCreation);
    compareToBuilder.append(this.dateModification, beneficiaire.dateModification);
    compareToBuilder.append(this.userCreation, beneficiaire.userCreation);
    compareToBuilder.append(this.userModification, beneficiaire.userModification);
    return compareToBuilder.toComparison();
  }

  public void setIdClientBO(String newIdClientBO) {
    if (newIdClientBO != null) {
      this.idClientBO = newIdClientBO;
    }
  }

  public void setNumeroAdherent(String newNumeroAdherent) {
    if (newNumeroAdherent != null) {
      this.numeroAdherent = newNumeroAdherent;
    }
  }

  public void setServices(List<String> newServices) {
    if (newServices != null) {
      this.services = newServices;
    }
  }

  public void setAmc(AmcBeneficiaire newAmc) {
    if (newAmc != null) {
      this.amc = newAmc;
    }
  }

  public void setContrat(ContratBeneficiaire newContrat) {
    if (newContrat != null) {
      this.contrat = newContrat;
    }
  }

  public void setIdentite(IdentiteContrat newIdentite) {
    if (newIdentite != null) {
      this.identite = newIdentite;
    }
  }

  public void setData(DataBeneficiaire newData) {
    if (newData != null) {
      this.data = newData;
    }
  }

  public void setAudit(AuditBeneficiaire newAudit) {
    if (newAudit != null) {
      this.audit = newAudit;
    }
  }

  public void setDateCreation(Date newDateCreation) {
    if (newDateCreation != null) {
      this.dateCreation = newDateCreation;
    }
  }

  public void setUserCreation(String newUserCreation) {
    if (newUserCreation != null) {
      this.userCreation = newUserCreation;
    }
  }

  public void setDateModification(Date newDateModification) {
    if (newDateModification != null) {
      this.dateModification = newDateModification;
    }
  }

  public void setUserModification(String newUserModification) {
    if (newUserModification != null) {
      this.userModification = newUserModification;
    }
  }
}
