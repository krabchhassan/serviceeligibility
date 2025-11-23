package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class PeriodeDroit implements GenericDomain<PeriodeDroit> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String periodeDebut;
  private String periodeFin;
  private String motifEvenement;
  private String libelleEvenement;
  private String dateEvenement;
  private String modeObtention;
  private String periodeFinInitiale;
  private String periodeFermetureDebut;
  private String periodeFermetureFin;

  public PeriodeDroit() {
    /* empty constructor */ }

  public PeriodeDroit(PeriodeDroit source) {
    this.periodeDebut = source.getPeriodeDebut();
    this.periodeFin = source.getPeriodeFin();
    this.motifEvenement = source.getMotifEvenement();
    this.libelleEvenement = source.getLibelleEvenement();
    this.dateEvenement = source.getDateEvenement();
    this.modeObtention = source.getModeObtention();
    this.periodeFinInitiale = source.getPeriodeFinInitiale();
    this.periodeFermetureDebut = source.getPeriodeFermetureDebut();
    this.periodeFermetureFin = source.getPeriodeFermetureFin();
  }

  @Override
  public int compareTo(final PeriodeDroit historiquePeriodeDroit) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.dateEvenement, historiquePeriodeDroit.dateEvenement);
    compareToBuilder.append(this.libelleEvenement, historiquePeriodeDroit.libelleEvenement);
    compareToBuilder.append(this.modeObtention, historiquePeriodeDroit.modeObtention);
    compareToBuilder.append(this.motifEvenement, historiquePeriodeDroit.motifEvenement);
    compareToBuilder.append(this.periodeDebut, historiquePeriodeDroit.periodeDebut);
    compareToBuilder.append(this.periodeFin, historiquePeriodeDroit.periodeFin);
    compareToBuilder.append(
        this.periodeFermetureDebut, historiquePeriodeDroit.periodeFermetureDebut);
    compareToBuilder.append(this.periodeFermetureFin, historiquePeriodeDroit.periodeFermetureFin);
    return compareToBuilder.toComparison();
  }
}
