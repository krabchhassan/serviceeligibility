package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class InfoFichierRecu implements GenericDomain<InfoFichierRecu> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String statut;
  private String codeRejet;
  private String messageRejet;
  private String nomFichier;
  private Integer mouvementRecus;
  private Integer mouvementRejetes;
  private Integer mouvementOk;
  private String nomFichierARL;
  private String versionFichier;
  private Long numeroFichier;

  @Override
  public int compareTo(InfoFichierRecu infoFichierRecu) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.statut, infoFichierRecu.statut);
    compareToBuilder.append(this.codeRejet, infoFichierRecu.codeRejet);
    compareToBuilder.append(this.nomFichier, infoFichierRecu.nomFichier);
    compareToBuilder.append(this.mouvementRecus, infoFichierRecu.mouvementRecus);
    compareToBuilder.append(this.mouvementRejetes, infoFichierRecu.mouvementRejetes);
    compareToBuilder.append(this.mouvementOk, infoFichierRecu.mouvementOk);
    compareToBuilder.append(this.nomFichierARL, infoFichierRecu.nomFichierARL);
    compareToBuilder.append(this.versionFichier, infoFichierRecu.versionFichier);
    compareToBuilder.append(this.numeroFichier, infoFichierRecu.numeroFichier);
    return compareToBuilder.toComparison();
  }
}
