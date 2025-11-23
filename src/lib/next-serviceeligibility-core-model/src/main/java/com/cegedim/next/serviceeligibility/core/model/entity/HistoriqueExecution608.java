package com.cegedim.next.serviceeligibility.core.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class HistoriqueExecution608 extends HistoriqueExecutions<HistoriqueExecution608> {

  private String codeService;
  private String idDeclarant;
  private String typeConventionnement;
  private String critereSecondaire;
  private String critereSecondaireDetaille;
  private int numeroFichier;
  private int nbDeclarationsConsolideesAlmerysLues;
  private int nbContrats;
  private int nbMembresContrats;
  private int nbContratsRejetes;
  private int nbRejetsNonBloquants;
  private int nbRejetsBloquants;
  private int nbFichiersGeneres;

  private Datas datas;

  public void incNbFichiersGeneres(int nbFichiersGeneres) {
    this.nbFichiersGeneres += nbFichiersGeneres;
  }

  public void incContrats(int nbContrats) {
    this.nbContrats += nbContrats;
  }

  public void incNbRejetsBloquants(int nbRejetsBloquants) {
    this.nbRejetsBloquants += nbRejetsBloquants;
  }

  public void incNbRejetsNonBloquants(int nbRejetsNonBloquants) {
    this.nbRejetsNonBloquants += nbRejetsNonBloquants;
  }

  public void incMembresContrats(int nbMembresContrats) {
    this.nbMembresContrats += nbMembresContrats;
  }

  public void incDeclarationsConsolideesAlmerysLues(int nbDeclarationsConsolideesAlmerysLues) {
    this.nbDeclarationsConsolideesAlmerysLues += nbDeclarationsConsolideesAlmerysLues;
  }

  public void incNombreContratRejet(int nombreContratRejet) {
    this.nbContratsRejetes += nombreContratRejet;
  }

  public void incNombreBeneficiaire(int nombreBeneficiaire) {
    this.datas.nombreBeneficiaire += nombreBeneficiaire;
  }

  public void incNombreContrat(int nombreContrat) {
    this.datas.nombreContrat += nombreContrat;
  }

  public void incNombreMouvementCC(int nombreMouvementCC) {
    this.datas.nombreMouvementCC += nombreMouvementCC;
  }

  public void incNombreMouvementCS(int nombreMouvementCS) {
    this.datas.nombreMouvementCS += nombreMouvementCS;
  }

  public void incNombreMouvementCarte(int nombreMouvementCarte) {
    this.datas.nombreMouvementCarte += nombreMouvementCarte;
  }

  public void incNombreMouvementMC(int nombreMouvementMC) {
    this.datas.nombreMouvementMC += nombreMouvementMC;
  }

  public void incNombreMouvementMS(int nombreMouvementMS) {
    this.datas.nombreMouvementMS += nombreMouvementMS;
  }

  public void incNombreMouvementRC(int nombreMouvementRC) {
    this.datas.nombreMouvementRC += nombreMouvementRC;
  }

  public void incNombreMouvementRS(int nombreMouvementRS) {
    this.datas.nombreMouvementRS += nombreMouvementRS;
  }

  @Override
  public void clear() {}

  @Override
  public void log() {
    log.info("*******************************************************");
    log.info(
        "********** {} **********",
        idDeclarant == null ? "TOTAL" : idDeclarant + "-" + critereSecondaireDetaille);
    log.info("Nb declarations consolidées Almerys lues :{}", nbDeclarationsConsolideesAlmerysLues);
    log.info("Nb contrats :{}", nbContrats);
    log.info("Nb membres contrats :{}", nbMembresContrats);
    log.info("Nb contrats rejetés :{}", nbContratsRejetes);
    log.info("Nb rejets non bloquants :{}", nbRejetsNonBloquants);
    log.info("Nb rejets bloquants :{}", nbRejetsBloquants);
    log.info("Nb fichiers generes:{}", nbFichiersGeneres);
    log.info("*******************************************************");
  }

  @Override
  public int getNbDeclarationLue() {
    return 0;
  }

  @Override
  public int compareTo(HistoriqueExecution608 o) {
    return 0;
  }
}
