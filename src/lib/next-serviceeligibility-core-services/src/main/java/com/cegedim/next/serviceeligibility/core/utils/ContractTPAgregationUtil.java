package com.cegedim.next.serviceeligibility.core.utils;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContractTPAgregationUtil {

  private static List<Periode> copyPeriodesList(List<Periode> periodes) {
    List<Periode> periodesCopy = new ArrayList<>();
    for (Periode periode : periodes) {
      periodesCopy.add(new Periode(periode));
    }
    return periodesCopy;
  }

  public static <T extends Mergeable, U> List<T> mergeSubObjectsList(
      List<U> rootObject, Function<U, List<T>> getToMerge, Function<T, List<Periode>> getPeriodes) {
    List<T> merged = new ArrayList<>();
    Map<String, List<T>> groupedByMergeKey =
        rootObject.stream()
            .flatMap(nature -> getToMerge.apply(nature).stream())
            .collect(Collectors.groupingBy(T::mergeKey));

    for (List<T> value : groupedByMergeKey.values()) {
      List<Periode> mainPeriodes = null;
      for (T nature : value) {
        if (mainPeriodes == null) {
          mainPeriodes = getPeriodes.apply(nature);
          merged.add(nature);
        } else {
          mainPeriodes.addAll(getPeriodes.apply(nature));
        }
      }
    }
    return merged;
  }

  public static List<PeriodeDroitContractTP> mergePeriodeDroitContractTP(
      List<PeriodeDroitContractTP> periodeDroitContractTPS) {
    Map<String, List<PeriodeDroitContractTP>> groupedByMergeKey =
        periodeDroitContractTPS.stream()
            .collect(Collectors.groupingBy(PeriodeDroitContractTP::mergeKey));

    List<PeriodeDroitContractTP> allPeriodesMerged = new ArrayList<>();
    for (List<PeriodeDroitContractTP> periodes : groupedByMergeKey.values()) {
      LinkedList<PeriodeDroitContractTP> merged = new LinkedList<>();
      List<PeriodeDroitContractTP> periodeDroitContractTPS1 = new ArrayList<>(periodes);
      periodeDroitContractTPS1.sort(
          (periode1, periode2) ->
              DateUtils.compareDate(
                  periode1.getPeriodeDebut(),
                  periode2.getPeriodeDebut(),
                  DateUtils.SLASHED_FORMATTER));

      for (PeriodeDroitContractTP periode : periodeDroitContractTPS1) {
        if (merged.isEmpty()) {
          merged.add(periode);
        } else {
          String datePlusOneDay = null;
          String mergedDateFin = merged.getLast().getPeriodeFin();
          if (TypePeriode.OFFLINE.equals(periode.getTypePeriode())
              && merged.getLast().getPeriodeFinFermeture() != null) {
            datePlusOneDay =
                DateUtils.datePlusOneDay(
                    merged.getLast().getPeriodeFinFermeture(), DateUtils.SLASHED_FORMATTER);
            mergedDateFin = merged.getLast().getPeriodeFinFermeture();
          } else if (merged.getLast().getPeriodeFin() != null) {
            datePlusOneDay =
                DateUtils.datePlusOneDay(
                    merged.getLast().getPeriodeFin(), DateUtils.SLASHED_FORMATTER);
          }
          if (StringUtils.isNotBlank(datePlusOneDay)
              && datePlusOneDay.equals(periode.getPeriodeDebut())) {
            merged.getLast().setPeriodeFin(periode.getPeriodeFin());
            merged.getLast().setPeriodeFinFermeture(periode.getPeriodeFinFermeture());
          } else if (DateUtils.before(
              mergedDateFin, periode.getPeriodeDebut(), DateUtils.SLASHED_FORMATTER)) {
            merged.add(periode);
          } else {
            getMaxFinAndSetPeriodesFin(periode, merged);
          }
        }
      }
      allPeriodesMerged.addAll(merged);
    }

    return allPeriodesMerged;
  }

  private static void getMaxFinAndSetPeriodesFin(
      PeriodeDroitContractTP periode, LinkedList<PeriodeDroitContractTP> merged) {
    List<String> periodeFinList =
        Arrays.asList(periode.getPeriodeFin(), merged.getLast().getPeriodeFin());
    String maxFin = DateUtils.getMaxDateOrNull(periodeFinList);
    merged.getLast().setPeriodeFin(maxFin);
    // Si la plus grande periodeFin est celle de la période en cours, on reporte
    // aussi sa finFermeture
    if (periodeFinList.indexOf(maxFin) == 0) {
      merged.getLast().setPeriodeFinFermeture(periode.getPeriodeFinFermeture());
    }
  }

  public static void aggregatePeriodesLists(
      List<ConventionnementContrat> conventionnementContrats,
      List<PrestationContrat> prestationContrats,
      List<RemboursementContrat> remboursementContrats,
      List<PrioriteDroitContrat> prioriteDroitContratList) {
    conventionnementContrats.forEach(
        conventionnementContrat ->
            conventionnementContrat.setPeriodes(
                DateUtils.getPeriodesFusionnees(
                    copyPeriodesList(conventionnementContrat.getPeriodes()),
                    DateUtils.SLASHED_FORMATTER)));
    prestationContrats.forEach(
        prestationContrat ->
            prestationContrat.setPeriodes(
                DateUtils.getPeriodesFusionnees(
                    copyPeriodesList(prestationContrat.getPeriodes()),
                    DateUtils.SLASHED_FORMATTER)));
    remboursementContrats.forEach(
        remboursementContrat ->
            remboursementContrat.setPeriodes(
                DateUtils.getPeriodesFusionnees(
                    copyPeriodesList(remboursementContrat.getPeriodes()),
                    DateUtils.SLASHED_FORMATTER)));
    prioriteDroitContratList.forEach(
        prioriteDroitContrat ->
            prioriteDroitContrat.setPeriodes(
                DateUtils.getPeriodesFusionnees(
                    copyPeriodesList(prioriteDroitContrat.getPeriodes()),
                    DateUtils.SLASHED_FORMATTER)));
  }
}
