package com.cegedim.next.serviceeligibility.core.model.entity;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

/** Classe qui mappe la collection historiqueExecutions dans la base de donnees. */
@Data
@Slf4j
@NoArgsConstructor
@Document(collection = "historiqueExecutions")
public class HistoriqueExecutions620 extends HistoriqueExecutions<HistoriqueExecutions620> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String idDerniereDeclarationTraitee;
  private String dernierNumeroContrat;
  private String dernierNumeroAdherent;

  private int nbConsolidationCree;
  private int nbDeclarationIgnoree;
  private int nbDeclarationErreur;
  private int nbDeclarationLue;
  private int nbDeclarationTraitee;

  private String idDeclarant;
  private String identifiant;

  private int nbCartesOk;
  private int nbCartesKo;
  private int nbCartesPapierOk;
  private int nbCartesPapierKo;

  private Date dateFinConsolidations;
  private Date dateFinCartes;

  private int nbCartesInvalidees;
  private int nbCartesPapierEdit;

  @Override
  public int compareTo(HistoriqueExecutions620 historiqueExecutions) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.Batch, historiqueExecutions.Batch);
    compareToBuilder.append(this.dateExecution, historiqueExecutions.dateExecution);
    compareToBuilder.append(
        this.idDerniereDeclarationTraitee, historiqueExecutions.idDerniereDeclarationTraitee);
    compareToBuilder.append(this.nbCartesOk, historiqueExecutions.nbCartesOk);
    compareToBuilder.append(this.nbDeclarationLue, historiqueExecutions.nbDeclarationLue);
    compareToBuilder.append(this.nbDeclarationIgnoree, historiqueExecutions.nbDeclarationIgnoree);
    return compareToBuilder.toComparison();
  }

  public void incNbCartesOk(int nb) {
    this.nbCartesOk += nb;
  }

  public void incNbConsolidationCree(int nb) {
    this.nbConsolidationCree += nb;
  }

  public void incNbDeclarationLue(int nb) {
    this.nbDeclarationLue += nb;
  }

  public void incNbDeclarationIgnoree(int nb) {
    this.nbDeclarationIgnoree += nb;
  }

  public void incNbDeclarationErreur(int nb) {
    this.nbDeclarationErreur += nb;
  }

  public void incNbCartesKo(int nb) {
    nbCartesKo += nb;
  }

  public void incNbCartesPapierKo(int nb) {
    nbCartesPapierKo += nb;
  }

  public void incNbCartesPapierOk(int nb) {
    nbCartesPapierOk += nb;
  }

  public void incNbCartesInvalidees(int nb) {
    nbCartesInvalidees += nb;
  }

  public void incNbCartesPapierEdit(int nb) {
    nbCartesPapierEdit += nb;
  }

  public int getNbDeclarationTraitee() {
    return this.nbDeclarationLue - this.nbDeclarationIgnoree - this.nbDeclarationErreur;
  }

  public void incAllFromOther(HistoriqueExecutions620 histOther) {
    incNbDeclarationErreur(histOther.getNbDeclarationErreur());
    incNbConsolidationCree(histOther.getNbConsolidationCree());
    incNbDeclarationIgnoree(histOther.getNbDeclarationIgnoree());
    incNbDeclarationLue(histOther.getNbDeclarationLue());
    incNbCartesInvalidees(histOther.getNbCartesInvalidees());
    incNbCartesOk(histOther.getNbCartesOk());
    incNbCartesKo(histOther.getNbCartesKo());
    incNbCartesPapierEdit(histOther.getNbCartesPapierEdit());
  }

  public void clear() {
    nbCartesOk = 0;
    nbDeclarationLue = 0;
    nbDeclarationIgnoree = 0;
    nbDeclarationErreur = 0;
  }

  public void log() {
    logSeparationStars();
    logPart1();
    logPart2();
    logForExtractionCartesPapier();
    logSeparationStars();
  }

  private void logPart1() {
    log.info("Nombre de déclarations lues : {}", this.getNbDeclarationLue());
    log.info("Nombre de déclarations traitées : {}", this.getNbDeclarationTraitee());
    log.info("Nombre de déclarations ignorées : {}", this.getNbDeclarationIgnoree());
    log.info("Nombre de déclarations en erreur de conso : {}", this.getNbDeclarationErreur());
    log.info("Nombre de consolidations créés : {}", this.getNbConsolidationCree());
    log.info("Nombre de cartes invalidées : {}", this.getNbCartesInvalidees());
  }

  private void logPart2() {
    log.info("Nombre de cartes crées : {}", this.getNbCartesOk());
    log.info("Nombre de cartes ko : {}", this.getNbCartesKo());
    log.info("Nombre de cartes papiers à éditer : {}", this.getNbCartesPapierEdit());
  }

  private void logSeparationStars() {
    log.info("**************************************************");
  }

  public void logExtractionCartesPapier() {
    log.info("Nombre de cartes papier éditées : {}", this.getNbCartesPapierOk());
    log.info("Nombre de cartes papier ko : {}", this.getNbCartesPapierKo());
  }

  public void logForExtractionCartesPapier() {
    logSeparationStars();
    logExtractionCartesPapier();
    logSeparationStars();
  }

  public HistoriqueExecutions620(HistoriqueExecutions620 source) {
    this();
    this.set_id(source.get_id());
    this.setDateExecution(source.getDateExecution());
    this.setDernierNumeroContrat(source.getDernierNumeroContrat());
    this.setBatch(source.getBatch());
    this.setDernierNumeroContrat(source.getDernierNumeroContrat());
    this.setDernierNumeroAdherent(source.getDernierNumeroAdherent());
    this.setDernierNumeroAdherent(source.getDernierNumeroAdherent());
    this.setNbDeclarationLue(source.getNbDeclarationLue());
    this.setNbDeclarationIgnoree(source.getNbDeclarationIgnoree());
    this.setNbDeclarationErreur(source.getNbDeclarationErreur());
    this.setNbDeclarationATraiter(source.getNbDeclarationATraiter());
    this.setIdDeclarant(source.getIdDeclarant());
    this.setNbCartesKo(source.getNbCartesKo());
  }
}
