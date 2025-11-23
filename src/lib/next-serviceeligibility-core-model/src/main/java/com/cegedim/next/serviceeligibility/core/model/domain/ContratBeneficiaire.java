package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

/** Classe qui mappe le document Contrat */
@Data
public class ContratBeneficiaire implements GenericDomain<ContratBeneficiaire> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String numeroContrat;
  private String codeEtat;

  /* DOCUMENTS EMBEDDED */
  private DataBeneficiaire data;

  private String numeroAdherent;
  private String societeEmettrice;
  private String numeroAMCEchange;

  public ContratBeneficiaire() {
    /* empty constructor */ }

  public ContratBeneficiaire(ContratBeneficiaire source) {
    this.numeroContrat = source.getNumeroContrat();
    this.codeEtat = source.getCodeEtat();

    /* DOCUMENTS EMBEDDED */
    if (source.getData() != null) {
      this.data = new DataBeneficiaire(source.getData());
    }

    this.numeroAdherent = source.getNumeroAdherent();
    this.societeEmettrice = source.getSocieteEmettrice();
    this.numeroAMCEchange = source.getNumeroAMCEchange();
  }

  @Override
  public int compareTo(final ContratBeneficiaire contrat) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.numeroContrat, contrat.numeroContrat);
    compareToBuilder.append(this.codeEtat, contrat.codeEtat);
    compareToBuilder.append(this.data, contrat.data);
    return compareToBuilder.toComparison();
  }

  public void setNumeroContrat(String newNumeroContrat) {
    if (newNumeroContrat != null) {
      this.numeroContrat = newNumeroContrat;
    }
  }

  public void setCodeEtat(String newCodeEtat) {
    if (newCodeEtat != null) {
      this.codeEtat = newCodeEtat;
    }
  }

  public void setData(DataBeneficiaire newData) {
    if (newData != null) {
      this.data = newData;
    }
  }

  public void setNumeroAdherent(String newNumeroAdherent) {
    if (newNumeroAdherent != null) {
      this.numeroAdherent = newNumeroAdherent;
    }
  }

  public void setSocieteEmettrice(String newSocieteEmettrice) {
    if (newSocieteEmettrice != null) {
      this.societeEmettrice = newSocieteEmettrice;
    }
  }

  public void setNumeroAMCEchange(String newNumeroAMCEchange) {
    if (newNumeroAMCEchange != null) {
      this.numeroAMCEchange = newNumeroAMCEchange;
    }
  }
}
