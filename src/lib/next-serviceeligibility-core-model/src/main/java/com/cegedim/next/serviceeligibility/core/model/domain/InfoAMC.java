package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class InfoAMC implements GenericDomain<InfoAMC> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String nomAMC;
  private String emetteurDroits;
  private String operateurPrincipal;
  private String codePartenaire;
  private String codeCircuit;
  private String numAMCEchange;

  public InfoAMC() {
    /* empty constructor */ }

  public InfoAMC(InfoAMC source) {
    this.codeCircuit = source.getCodeCircuit();
    this.nomAMC = source.getNomAMC();
    this.numAMCEchange = source.getNumAMCEchange();
    this.codePartenaire = source.getCodePartenaire();
    this.emetteurDroits = source.getEmetteurDroits();
    this.operateurPrincipal = source.getOperateurPrincipal();
  }

  /* Getters and Setters */

  @Override
  public int compareTo(InfoAMC infoAMC) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.nomAMC, infoAMC.nomAMC);
    compareToBuilder.append(this.codePartenaire, infoAMC.codePartenaire);
    compareToBuilder.append(this.codeCircuit, infoAMC.codeCircuit);
    compareToBuilder.append(this.emetteurDroits, infoAMC.emetteurDroits);
    compareToBuilder.append(this.operateurPrincipal, infoAMC.operateurPrincipal);
    compareToBuilder.append(this.numAMCEchange, infoAMC.numAMCEchange);
    return compareToBuilder.toComparison();
  }
}
