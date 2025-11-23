package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class InfoFichierEmis implements GenericDomain<InfoFichierEmis> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String nomFichier;
  private Integer mouvementEmis;
  private Integer mouvementNonEmis;
  private String critereSecondaire;
  private String critereSecondaireDetaille;
  private String nomFichierARL;
  private String versionFichier;
  private Long numeroFichier;

  public InfoFichierEmis() {
    /* empty constructor */ }

  public InfoFichierEmis(InfoFichierEmis source) {
    this.nomFichier = source.getNomFichier();
    this.mouvementEmis = source.getMouvementEmis();
    this.mouvementNonEmis = source.getMouvementNonEmis();
    this.critereSecondaire = source.getCritereSecondaire();
    this.critereSecondaireDetaille = source.getCritereSecondaireDetaille();
    this.nomFichierARL = source.getNomFichierARL();
    this.versionFichier = source.getVersionFichier();
    this.numeroFichier = source.getNumeroFichier();
  }

  @Override
  public int compareTo(InfoFichierEmis infoFichierEmis) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.nomFichier, infoFichierEmis.nomFichier);
    compareToBuilder.append(this.mouvementEmis, infoFichierEmis.mouvementEmis);
    compareToBuilder.append(this.mouvementNonEmis, infoFichierEmis.mouvementNonEmis);
    compareToBuilder.append(this.critereSecondaire, infoFichierEmis.critereSecondaire);
    compareToBuilder.append(
        this.critereSecondaireDetaille, infoFichierEmis.critereSecondaireDetaille);
    compareToBuilder.append(this.nomFichierARL, infoFichierEmis.nomFichierARL);
    compareToBuilder.append(this.versionFichier, infoFichierEmis.versionFichier);
    compareToBuilder.append(this.numeroFichier, infoFichierEmis.numeroFichier);
    return compareToBuilder.toComparison();
  }
}
