package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.domain.Pilotage;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

/** Classe qui mappe la collection historiqueDeclarants dans la base de donnees. */
@Document(collection = "historiqueDeclarants")
@Data
@EqualsAndHashCode(callSuper = false)
public class HistoriqueDeclarant extends DocumentEntity
    implements GenericDomain<HistoriqueDeclarant> {

  private static final long serialVersionUID = 1L;

  /* Les types possibles de declarants */
  public static final String TYPE_AMC = "amc";
  public static final String TYPE_AMO = "amo";

  /* PROPRIETES */

  private String nom;
  private String libelle;
  private String siret;
  private String numeroPrefectoral;
  private String codePartenaire;
  private String type = TYPE_AMC;
  private String codeCircuit;
  private String emetteurDroits;
  private String operateurPrincipal;

  /* TRACE */

  private Date dateCreation;
  private String userCreation;
  private Date dateModification;
  private String userModification;

  /* DOCUMENTS EMBEDDED */

  private List<Pilotage> pilotages;

  /* GETTERS SETTERS */

  public boolean isAMC() {
    return type != null && type.equals(TYPE_AMC);
  }

  public boolean isAMO() {
    return type != null && type.equals(TYPE_AMO);
  }

  @Override
  public int compareTo(HistoriqueDeclarant declarant) {

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
}
