package com.cegedim.next.serviceeligibility.core.services.pojo;

import static com.cegedim.next.serviceeligibility.core.utils.DateUtils.SLASHED_FORMATTER;

import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.Garantie;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.Mergeable;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.NaturePrestation;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PeriodeDroitContractTP;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.Produit;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ReferenceCouverture;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.TypePeriode;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
@NoArgsConstructor
@Slf4j
public class DomaineDroitForConsolidation
    implements GenericDomain<DomaineDroitForConsolidation>, Mergeable {
  private Garantie garantie;
  private Produit produit;
  private ReferenceCouverture referenceCouverture;
  private NaturePrestation naturePrestation;
  private PeriodeDroitContractTP periodeDroitContractTP;

  private DomaineDroit domaineDroit;

  private CasDeclaration casDeclaration;

  @Override
  public String mergeKey() {
    return garantie.mergeKey()
        + produit.mergeKey()
        + referenceCouverture.mergeKey()
        + naturePrestation.mergeKey()
        + periodeDroitContractTP.mergeKey();
  }

  @Override
  public String conflictKey() {
    return "";
  }

  public DomaineDroitForConsolidation(DomaineDroitForConsolidation source) {
    this.garantie = source.getGarantie();
    this.produit = source.getProduit();
    this.referenceCouverture = source.getReferenceCouverture();
    this.naturePrestation = source.getNaturePrestation();
    this.periodeDroitContractTP = source.getPeriodeDroitContractTP();
    this.casDeclaration = source.casDeclaration;
  }

  @Override
  public int compareTo(DomaineDroitForConsolidation o) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(garantie, o.garantie);
    compareToBuilder.append(produit, o.produit);
    compareToBuilder.append(referenceCouverture, o.referenceCouverture);
    compareToBuilder.append(naturePrestation, o.naturePrestation);
    compareToBuilder.append(periodeDroitContractTP, o.periodeDroitContractTP);
    return compareToBuilder.toComparison();
  }

  private String declPeriodeDebut;
  private String declPeriodeFin;
  private String declPeriodeDebutFermeture;
  private String declPeriodeFinFermeture;
  private String declPeriodeDebutFermeturePlus1;
  private String declPeriodeFinFermetureMinus1;
  private boolean fermetureOnOnline;
  private LocalDate declPeriodeDebutFormatDate;
  private LocalDate declPeriodeDebutFormatDateMinus1;
  private LocalDate declPeriodeFinFormatDate;
  private LocalDate declPeriodeFinFormatDatePlus1;
  private LocalDate declPeriodeDebutFermetureFormatDate;
  private LocalDate declPeriodeFinFermetureFormatDate;

  public void setDomaineDroit(DomaineDroit domaineDroit) {
    this.domaineDroit = domaineDroit;
    updateDates();
    updatePeriodeContratTPDates();
  }

  private void updateDates() {
    // recup date + dateFermeture (offline ou online) de la déclaration
    declPeriodeDebut = domaineDroit.getPeriodeDroit().getPeriodeDebut();
    declPeriodeFin = domaineDroit.getPeriodeDroit().getPeriodeFin();
    declPeriodeDebutFermeture = domaineDroit.getPeriodeDroit().getPeriodeFermetureDebut();
    if (declPeriodeFin != null) {
      declPeriodeDebutFermeturePlus1 =
          DateUtils.getStringDatePlusDays(declPeriodeFin, 1, SLASHED_FORMATTER);
    }
    declPeriodeFinFermetureMinus1 =
        DateUtils.getStringDatePlusDays(declPeriodeDebut, -1, SLASHED_FORMATTER);
    declPeriodeFinFermeture = domaineDroit.getPeriodeDroit().getPeriodeFermetureFin();
    if (isOnline() && domaineDroit.getPeriodeOnline() != null) {
      declPeriodeDebut = domaineDroit.getPeriodeOnline().getPeriodeDebut();
      declPeriodeFin = domaineDroit.getPeriodeOnline().getPeriodeFin();
      if (domaineDroit.getPeriodeOnline().getPeriodeFermetureDebut() != null) {
        fermetureOnOnline = true;
        declPeriodeDebutFermeture = domaineDroit.getPeriodeOnline().getPeriodeFermetureDebut();
        declPeriodeFinFermeture = domaineDroit.getPeriodeOnline().getPeriodeFermetureFin();
      }
    }

    declPeriodeDebutFormatDate = LocalDate.parse(declPeriodeDebut, SLASHED_FORMATTER);
    declPeriodeDebutFormatDateMinus1 =
        LocalDate.parse(declPeriodeDebut, SLASHED_FORMATTER).minusDays(1);
    declPeriodeFinFormatDate =
        StringUtils.isNotBlank(declPeriodeFin)
            ? LocalDate.parse(declPeriodeFin, SLASHED_FORMATTER)
            : null;
    declPeriodeFinFormatDatePlus1 =
        StringUtils.isNotBlank(declPeriodeFin)
            ? LocalDate.parse(declPeriodeFin, SLASHED_FORMATTER).plusDays(1)
            : null;

    declPeriodeDebutFermetureFormatDate =
        StringUtils.isNotBlank(declPeriodeDebutFermeture)
            ? LocalDate.parse(declPeriodeDebutFermeture, SLASHED_FORMATTER)
            : null;
    declPeriodeFinFermetureFormatDate =
        StringUtils.isNotBlank(declPeriodeFinFermeture)
            ? LocalDate.parse(declPeriodeFinFermeture, SLASHED_FORMATTER)
            : null;
  }

  private void updatePeriodeContratTPDates() {
    periodeDroitContractTP.setPeriodeDebut(declPeriodeDebut);
    periodeDroitContractTP.setPeriodeFin(declPeriodeFin);
    periodeDroitContractTP.setPeriodeFinFermeture(declPeriodeFinFermeture);
  }

  public boolean isOnline() {
    return periodeDroitContractTP != null
        && TypePeriode.ONLINE.equals(periodeDroitContractTP.getTypePeriode());
  }
}
