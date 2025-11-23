package com.cegedim.next.serviceeligibility.core.model.query;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import java.util.Date;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class ParametresFlux implements GenericDomain<ParametresFlux> {

  private static final long serialVersionUID = 5997427566168521285L;

  private String nomAmc;
  private String idDeclarant;
  private String codePartenaire;
  private String codeCircuit;
  private Date dateDebut;
  private Date dateFin;
  private String emetteur;
  private String numeroFichier;
  private String processus;
  private String typeFichier;
  private boolean isFichierEmis;
  private String nomFichier;
  private boolean isNewSearch;
  private Integer position;
  private Integer numberByPage;

  @Override
  public int compareTo(ParametresFlux parametreFlux) {
    CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.nomAmc, parametreFlux.nomAmc);
    compareToBuilder.append(this.codePartenaire, parametreFlux.codePartenaire);
    compareToBuilder.append(this.codeCircuit, parametreFlux.codeCircuit);
    compareToBuilder.append(this.dateDebut, parametreFlux.dateDebut);
    compareToBuilder.append(this.dateFin, parametreFlux.dateFin);
    compareToBuilder.append(this.emetteur, parametreFlux.emetteur);
    compareToBuilder.append(this.numeroFichier, parametreFlux.numeroFichier);
    compareToBuilder.append(this.processus, parametreFlux.processus);
    compareToBuilder.append(this.typeFichier, parametreFlux.typeFichier);
    compareToBuilder.append(this.isFichierEmis, parametreFlux.isFichierEmis);
    compareToBuilder.append(this.isNewSearch, parametreFlux.isNewSearch);
    compareToBuilder.append(this.position, parametreFlux.position);
    compareToBuilder.append(this.numberByPage, parametreFlux.numberByPage);
    return compareToBuilder.toComparison();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((codeCircuit == null) ? 0 : codeCircuit.hashCode());
    result = prime * result + ((codePartenaire == null) ? 0 : codePartenaire.hashCode());
    result = prime * result + ((dateDebut == null) ? 0 : dateDebut.hashCode());
    result = prime * result + ((dateFin == null) ? 0 : dateFin.hashCode());
    result = prime * result + ((emetteur == null) ? 0 : emetteur.hashCode());
    result = prime * result + ((idDeclarant == null) ? 0 : idDeclarant.hashCode());
    result = prime * result + (isFichierEmis ? 1231 : 1237);
    result = prime * result + (isNewSearch ? 1231 : 1237);
    result = prime * result + ((nomAmc == null) ? 0 : nomAmc.hashCode());
    result = prime * result + ((numberByPage == null) ? 0 : numberByPage.hashCode());
    result = prime * result + ((numeroFichier == null) ? 0 : numeroFichier.hashCode());
    result = prime * result + ((position == null) ? 0 : position.hashCode());
    result = prime * result + ((processus == null) ? 0 : processus.hashCode());
    result = prime * result + ((typeFichier == null) ? 0 : typeFichier.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    ParametresFlux other = (ParametresFlux) obj;
    if (codeCircuit == null) {
      if (other.codeCircuit != null) return false;
    } else if (!codeCircuit.equals(other.codeCircuit)) return false;
    if (codePartenaire == null) {
      if (other.codePartenaire != null) return false;
    } else if (!codePartenaire.equals(other.codePartenaire)) return false;
    if (dateDebut == null) {
      if (other.dateDebut != null) return false;
    } else if (!dateDebut.equals(other.dateDebut)) return false;
    if (dateFin == null) {
      if (other.dateFin != null) return false;
    } else if (!dateFin.equals(other.dateFin)) return false;
    if (emetteur == null) {
      if (other.emetteur != null) return false;
    } else if (!emetteur.equals(other.emetteur)) return false;
    if (idDeclarant == null) {
      if (other.idDeclarant != null) return false;
    } else if (!idDeclarant.equals(other.idDeclarant)) return false;
    if (isFichierEmis != other.isFichierEmis) return false;
    if (isNewSearch != other.isNewSearch) return false;
    if (nomAmc == null) {
      if (other.nomAmc != null) return false;
    } else if (!nomAmc.equals(other.nomAmc)) return false;
    if (numberByPage == null) {
      if (other.numberByPage != null) return false;
    } else if (!numberByPage.equals(other.numberByPage)) return false;
    if (numeroFichier == null) {
      if (other.numeroFichier != null) return false;
    } else if (!numeroFichier.equals(other.numeroFichier)) return false;
    if (position == null) {
      if (other.position != null) return false;
    } else if (!position.equals(other.position)) return false;
    if (processus == null) {
      if (other.processus != null) return false;
    } else if (!processus.equals(other.processus)) return false;
    if (typeFichier == null) {
      if (other.typeFichier != null) return false;
    } else if (!typeFichier.equals(other.typeFichier)) return false;
    return true;
  }
}
