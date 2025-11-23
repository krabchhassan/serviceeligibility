package com.cegedim.next.serviceeligibility.core.model.domain.contract;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class PeriodeDroitContractTP implements GenericDomain<PeriodeDroitContractTP>, Mergeable {

  private TypePeriode typePeriode;

  private String periodeDebut;
  private String periodeFin;
  private String periodeFinFermeture;

  private String libelleEvenement;
  private String modeObtention;
  private String motifEvenement;

  public PeriodeDroitContractTP() {
    /* empty constructor */ }

  public PeriodeDroitContractTP(PeriodeDroitContractTP source) {
    this.typePeriode = source.getTypePeriode();
    this.periodeDebut = source.getPeriodeDebut();
    this.periodeFin = source.getPeriodeFin();
    this.periodeFinFermeture = source.getPeriodeFinFermeture();
    this.libelleEvenement = source.getLibelleEvenement();
    this.modeObtention = source.getModeObtention();
    this.motifEvenement = source.getMotifEvenement();
  }

  @Override
  public int compareTo(PeriodeDroitContractTP periodeDroitContractTP) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.typePeriode, periodeDroitContractTP.typePeriode);
    compareToBuilder.append(this.periodeDebut, periodeDroitContractTP.periodeDebut);
    compareToBuilder.append(this.periodeFin, periodeDroitContractTP.periodeFin);
    compareToBuilder.append(this.periodeFinFermeture, periodeDroitContractTP.periodeFinFermeture);
    compareToBuilder.append(this.libelleEvenement, periodeDroitContractTP.libelleEvenement);
    compareToBuilder.append(this.modeObtention, periodeDroitContractTP.modeObtention);
    compareToBuilder.append(this.motifEvenement, periodeDroitContractTP.motifEvenement);

    return compareToBuilder.toComparison();
  }

  @Override
  public String mergeKey() {
    return typePeriode.name();
  }

  @Override
  public String conflictKey() {
    return "";
  }
}
