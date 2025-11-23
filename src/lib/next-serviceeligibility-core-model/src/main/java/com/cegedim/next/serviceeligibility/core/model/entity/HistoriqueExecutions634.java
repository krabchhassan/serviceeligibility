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
public class HistoriqueExecutions634 extends HistoriqueExecutions<HistoriqueExecutions634> {
  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String idDerniereDeclarationTraitee;
  private Date dateCheckpoint;
  private long positionCheckpoint;
  private String premierIdDeclarant;
  private String premierNumeroContrat;
  private String premierNumeroAdherent;
  private String dernierIdDeclarant;
  private String dernierNumeroContrat;
  private String dernierNumeroAdherent;
  private int nbContratCree;
  private int nbContratSupprime;
  private long nbDeclarationsInitial;
  private long fromDeclaration;
  private long toDeclaration;
  private int nbDeclarationTraitee;
  private int nbDeclarationIgnoree;
  private int nbDeclarationErreur;

  @Override
  public int compareTo(HistoriqueExecutions634 historiqueExecutions634) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.Batch, historiqueExecutions634.Batch);
    compareToBuilder.append(this.dateExecution, historiqueExecutions634.dateExecution);
    compareToBuilder.append(
        this.idDerniereDeclarationTraitee, historiqueExecutions634.idDerniereDeclarationTraitee);
    compareToBuilder.append(this.nbContratCree, historiqueExecutions634.nbContratCree);
    compareToBuilder.append(this.nbContratSupprime, historiqueExecutions634.nbContratSupprime);
    compareToBuilder.append(
        this.nbDeclarationTraitee, historiqueExecutions634.nbDeclarationTraitee);
    compareToBuilder.append(
        this.nbDeclarationIgnoree, historiqueExecutions634.nbDeclarationIgnoree);
    return compareToBuilder.toComparison();
  }

  public void incNbContratCree(int nb) {
    this.nbContratCree += nb;
  }

  public void incNbContratSupprime(int nb) {
    this.nbContratSupprime += nb;
  }

  public void incNbDeclarationTraitee(int nb) {
    this.nbDeclarationTraitee += nb;
  }

  public void incNbDeclarationIgnoree(int nb) {
    this.nbDeclarationIgnoree += nb;
  }

  public void incNbDeclarationErreur(int nb) {
    this.nbDeclarationErreur += nb;
  }

  public int getNbDeclarationLue() {
    return this.nbDeclarationTraitee + this.nbDeclarationIgnoree + this.nbDeclarationErreur;
  }

  public void clear() {
    nbContratCree = 0;
    nbContratSupprime = 0;
    nbDeclarationTraitee = 0;
    nbDeclarationIgnoree = 0;
    nbDeclarationErreur = 0;
  }

  public void log() {
    log.info("**************************************************");
    log.info("Nombre de déclarations traitées : {}", this.getNbDeclarationTraitee());
    log.info("Nombre de déclarations ignorées : {}", this.getNbDeclarationIgnoree());
    log.info("Nombre de déclarations en erreur de conso : {}", this.getNbDeclarationErreur());
    log.info("Nombre de contrats créés : {}", this.getNbContratCree());
    log.info("Nombre de contrats supprimés : {}", this.getNbContratSupprime());
    log.info("**************************************************");
  }

  public HistoriqueExecutions634(HistoriqueExecutions634 source) {
    this();
    this.set_id(source.get_id());
    this.setDateExecution(source.getDateExecution());
    this.setDernierNumeroContrat(source.getDernierNumeroContrat());
    this.setBatch(source.getBatch());
    this.setPremierIdDeclarant(source.getPremierIdDeclarant());
    this.setPremierNumeroContrat(source.getPremierNumeroContrat());
    this.setPremierNumeroAdherent(source.getPremierNumeroAdherent());
    this.setDernierIdDeclarant(source.getDernierIdDeclarant());
    this.setDernierNumeroContrat(source.getDernierNumeroContrat());
    this.setDernierNumeroAdherent(source.getDernierNumeroAdherent());
    this.setPositionCheckpoint(source.getPositionCheckpoint());
    this.setDateCheckpoint(source.getDateCheckpoint());
    this.setDernierNumeroAdherent(source.getDernierNumeroAdherent());
    this.setNbDeclarationsInitial(source.getNbDeclarationsInitial());
    this.setFromDeclaration(source.getFromDeclaration());
    this.setToDeclaration(source.getToDeclaration());
    this.setNbDeclarationTraitee(source.getNbDeclarationTraitee());
    this.setNbDeclarationIgnoree(source.getNbDeclarationIgnoree());
    this.setNbDeclarationErreur(source.getNbDeclarationErreur());
    this.setNbDeclarationATraiter(source.getNbDeclarationATraiter());
  }
}
