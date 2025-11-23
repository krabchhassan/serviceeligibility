package com.cegedim.next.serviceeligibility.core.services.contracttp;

import static com.cegedim.next.serviceeligibility.core.utils.DateUtils.SLASHED_FORMATTER;

import com.cegedim.next.serviceeligibility.core.model.domain.Conventionnement;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.PeriodeDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.Prestation;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ConventionnementContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.Mergeable;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.NaturePrestation;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PrestationContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PrioriteDroitContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.RemboursementContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.TypePeriode;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.services.pojo.CasDeclaration;
import com.cegedim.next.serviceeligibility.core.services.pojo.DomaineDroitForConsolidation;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PeriodeDroitTPStep4 {

  public void handleSubPeriods(
      NaturePrestation naturePrestation,
      DomaineDroitForConsolidation domaineDroitForConsolidation) {
    DomaineDroit domaineDroit = domaineDroitForConsolidation.getDomaineDroit();
    PeriodeDroit periodeDroit = getPeriodeDroit(domaineDroitForConsolidation);

    CasDeclaration casDeclaration = domaineDroitForConsolidation.getCasDeclaration();
    if (CasDeclaration.FERMETURE.equals(casDeclaration)
        || CasDeclaration.TFD.equals(casDeclaration)) {
      Periode periodeToRemove;
      if (periodeDroit.getPeriodeFermetureDebut() != null
          && !DateUtils.isReverseDate(
              periodeDroit.getPeriodeFermetureDebut(), periodeDroit.getPeriodeFermetureFin())) {
        periodeToRemove =
            new Periode(
                periodeDroit.getPeriodeFermetureDebut(), periodeDroit.getPeriodeFermetureFin());
        handleSubsPeriods(naturePrestation, domaineDroit, periodeToRemove, false);
      }
    }

    if (!DateUtils.isReverseDate(periodeDroit.getPeriodeDebut(), periodeDroit.getPeriodeFin())) {
      Periode periodeToAdd =
          new Periode(periodeDroit.getPeriodeDebut(), periodeDroit.getPeriodeFin());
      handleSubsPeriods(naturePrestation, domaineDroit, periodeToAdd, true);
    }
  }

  private void handleSubsPeriods(
      NaturePrestation naturePrestation,
      DomaineDroit domaineDroit,
      Periode periodeDecla,
      boolean adding) {
    // Priorite
    PrioriteDroitContrat prioDecla = new PrioriteDroitContrat(domaineDroit.getPrioriteDroit());
    consolidatePeriods(
        naturePrestation.getPrioritesDroit(),
        List.of(prioDecla),
        periodeDecla,
        PrioriteDroitContrat::getPeriodes,
        adding);

    // Remboursement
    RemboursementContrat remboursementContrat =
        new RemboursementContrat(
            domaineDroit.getTauxRemboursement(), domaineDroit.getUniteTauxRemboursement());
    consolidatePeriods(
        naturePrestation.getRemboursements(),
        List.of(remboursementContrat),
        periodeDecla,
        RemboursementContrat::getPeriodes,
        adding);

    // Conventionnement
    List<ConventionnementContrat> newConvs = new ArrayList<>();
    for (Conventionnement convDecla : domaineDroit.getConventionnements()) {
      ConventionnementContrat newConv = new ConventionnementContrat(convDecla);
      newConvs.add(newConv);
    }
    consolidatePeriods(
        naturePrestation.getConventionnements(),
        newConvs,
        periodeDecla,
        ConventionnementContrat::getPeriodes,
        adding);

    // Prestation
    List<PrestationContrat> newPrests = new ArrayList<>();
    for (Prestation prestation : domaineDroit.getPrestations()) {
      PrestationContrat newPrest = new PrestationContrat(prestation);
      newPrests.add(newPrest);
    }
    consolidatePeriods(
        naturePrestation.getPrestations(),
        newPrests,
        periodeDecla,
        PrestationContrat::getPeriodes,
        adding);
  }

  private List<Periode> extractPeriods(
      List<Periode> periodesContrat,
      Periode perDecla,
      boolean mergeable,
      boolean conflicted,
      boolean adding) {
    Periode merged = null;
    List<Periode> newPeriodes = new ArrayList<>();
    for (Periode perContrat : periodesContrat) {
      if (DateUtils.isOverlappingSlashPeriod(
          DateUtils.dateMinusOneDay(perDecla.getDebut(), SLASHED_FORMATTER),
          perDecla.getFin(),
          DateUtils.dateMinusOneDay(perContrat.getDebut(), SLASHED_FORMATTER),
          perContrat.getFin())) {
        if (adding) {
          merged =
              extractAddingPeriod(perDecla, mergeable, conflicted, perContrat, merged, newPeriodes);
        } else if (mergeable) {
          // Cas de fermeture de periode
          newPeriodes.addAll(splitPeriod(perContrat, perDecla));
        }
      } else {
        newPeriodes.add(perContrat);
      }
    }

    if (adding && mergeable && merged == null) {
      newPeriodes.add(new Periode(perDecla));
    }
    return newPeriodes;
  }

  private Periode extractAddingPeriod(
      Periode perDecla,
      boolean mergeable,
      boolean conflicted,
      Periode perContrat,
      Periode merged,
      List<Periode> newPeriodes) {
    if (mergeable) {
      if (merged == null) {
        Periode mergedPeriode = addPeriod(perContrat, perDecla);
        newPeriodes.add(mergedPeriode);
        merged = mergedPeriode;
      } else {
        addPeriod(merged, perContrat);
      }
    } else if (conflicted) {
      newPeriodes.addAll(splitPeriod(perContrat, perDecla));
    } else {
      newPeriodes.add(perContrat);
    }
    return merged;
  }

  private List<Periode> splitPeriod(Periode periodeContrat, Periode periodeDecla) {
    List<Periode> splittedPeriodes = new ArrayList<>();
    String declaDebutMoins1 =
        SLASHED_FORMATTER.format(
            LocalDate.parse(periodeDecla.getDebut(), SLASHED_FORMATTER).minusDays(1));
    String declaFinPlus1 =
        periodeDecla.getFin() != null
            ? SLASHED_FORMATTER.format(
                LocalDate.parse(periodeDecla.getFin(), SLASHED_FORMATTER).plusDays(1))
            : null;

    if (DateUtils.beforeAnyEnglishFormat(periodeContrat.getDebut(), declaDebutMoins1)) {
      Periode firstPart = new Periode(periodeContrat.getDebut(), declaDebutMoins1);
      splittedPeriodes.add(firstPart);
    }

    if (DateUtils.beforeAnyEnglishFormat(declaFinPlus1, periodeContrat.getFin())) {
      Periode lastPart = new Periode(declaFinPlus1, periodeContrat.getFin());
      splittedPeriodes.add(lastPart);
    }

    return splittedPeriodes;
  }

  <T extends Mergeable> void consolidatePeriods(
      List<T> fromContrat,
      List<T> fromDecla,
      Periode perDecla,
      Function<T, List<Periode>> getPeriodes,
      boolean adding) {
    Set<T> elemMerged = new HashSet<>();
    for (T elemDecla : fromDecla) {
      boolean merged = false;
      List<T> toDelete = new ArrayList<>();
      for (T elemContrat : fromContrat) {
        if (!elemMerged.contains(elemContrat)) {
          boolean mergeable = Objects.equals(elemDecla.mergeKey(), elemContrat.mergeKey());
          boolean conflicted = Objects.equals(elemDecla.conflictKey(), elemContrat.conflictKey());
          List<Periode> oldPeriodes = getPeriodes.apply(elemContrat);
          List<Periode> newPeriodes =
              extractPeriods(oldPeriodes, perDecla, mergeable, conflicted, adding);
          oldPeriodes.clear();
          oldPeriodes.addAll(newPeriodes);

          if (mergeable) {
            merged = true;
            elemMerged.add(elemContrat);
          }

          if (oldPeriodes.isEmpty()) {
            toDelete.add(elemContrat);
          }
        }
      }
      if (adding && !merged) {
        getPeriodes.apply(elemDecla).add(new Periode(perDecla));
        fromContrat.add(elemDecla);
        elemMerged.add(elemDecla);
      }

      fromContrat.removeAll(toDelete);
    }
  }

  private Periode addPeriod(Periode periodeContrat, Periode periodeDecla) {
    String ouvertureDeb = DateUtils.getMinDate(periodeContrat.getDebut(), periodeDecla.getDebut());
    String ouvertureFin =
        DateUtils.getMaxDateOrNull(periodeContrat.getFin(), periodeDecla.getFin());
    periodeContrat.setDebut(ouvertureDeb);
    if (ouvertureFin != null) {
      periodeContrat.setFin(ouvertureFin);
    } else {
      periodeContrat.setFinToNull();
    }
    return periodeContrat;
  }

  private PeriodeDroit getPeriodeDroit(DomaineDroitForConsolidation domaineDroitForConsolidation) {
    DomaineDroit domaineDroit = domaineDroitForConsolidation.getDomaineDroit();

    if (TypePeriode.ONLINE.equals(
            domaineDroitForConsolidation.getPeriodeDroitContractTP().getTypePeriode())
        && domaineDroit.getPeriodeOnline() != null) {
      return domaineDroit.getPeriodeOnline();
    }

    return domaineDroit.getPeriodeDroit();
  }
}
