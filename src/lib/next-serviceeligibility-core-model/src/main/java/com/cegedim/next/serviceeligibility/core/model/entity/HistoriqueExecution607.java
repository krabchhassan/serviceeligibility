package com.cegedim.next.serviceeligibility.core.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class HistoriqueExecution607 extends HistoriqueExecutions<HistoriqueExecution607> {

  private String codeService;
  private String idDeclarant;
  private String typeConventionnement;
  private String critereSecondaire;
  private String critereSecondaireDetaille;
  private int nbDeclarationSelect;
  private int nbDeclarationConso;
  private int nbRejets;
  private int nbDeclarationLues;

  public void incDeclSelect(int nbSelect) {
    this.nbDeclarationSelect += nbSelect;
  }

  public void incDeclLues(int nbLues) {
    this.nbDeclarationLues += nbLues;
  }

  public void incRejets(int nbRejets) {
    this.nbRejets += nbRejets;
  }

  public void incDeclConso(int nbDeclConso) {
    this.nbDeclarationConso += nbDeclConso;
  }

  @Override
  public void clear() {}

  @Override
  public void log() {
    log.info("*******************************************************");
    if (idDeclarant == null) {
      log.info("********** TOTAL **********");
    } else {
      log.info("Déclarant : {}", idDeclarant);
      log.info("Type conventionnement : {}", typeConventionnement);
      log.info("Critère secondaire : {}", critereSecondaire);
      log.info("Critère secondaire détaillé : {}", critereSecondaireDetaille);
    }
    log.info("Nb declarations lues : {}", nbDeclarationLues);
    log.info("Nb declarations sélectionnées pour conso : {}", nbDeclarationSelect);
    log.info("Nb declarations conso : {}", nbDeclarationConso);
    log.info("Nb rejets : {}", nbRejets);
    log.info("*******************************************************");
  }

  @Override
  public int getNbDeclarationLue() {
    return nbDeclarationLues;
  }

  @Override
  public int compareTo(HistoriqueExecution607 o) {
    return 0;
  }
}
