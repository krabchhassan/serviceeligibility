package com.cegedim.next.serviceeligibility.core.utils;

import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public final class Util {

  public static <T> List<T> assignList(List<T> a, List<T> b) {
    return a != null ? a : b;
  }

  public static <T> T assign(T a, T b) {
    return a != null ? a : b;
  }

  /** /** Private constructor. */
  private Util() {}

  public static LocalDate stringToDate(String dateString) {
    String cleanedDate = dateString.replace("-", "").replace("/", "");
    return LocalDate.parse(cleanedDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
  }

  public static List<String> stringToList(String listString) {
    List<String> stringList = new ArrayList<>();
    if (StringUtils.isNotBlank(listString)) {
      String[] splittedString = listString.split(",");
      stringList = Arrays.stream(splittedString).map(String::trim).toList();
    }
    return stringList;
  }

  public static List<Periode> aggregatePeriodes(
      List<Periode> listPeriods, String startDate, String endDate) {
    List<Periode> periodesTemp =
        listPeriods.stream()
            .filter(
                periode ->
                    DateUtils.isOverlapping(
                        startDate, endDate, periode.getDebut(), periode.getFin()))
            .sorted(Comparator.comparing(Periode::getDebut))
            .toList();
    if (periodesTemp.isEmpty()) return Collections.emptyList(); // early exit
    return DateUtils.getPeriodesFusionnees(periodesTemp);
  }

  public static List<Periode> aggregatePeriodes(List<Periode> listPeriods) {
    List<Periode> periodesTemp =
        listPeriods.stream()
            .sorted(Comparator.comparing(Periode::getDebut))
            .collect(Collectors.toList());

    return aggregatePeriodesNoSort(periodesTemp);
  }

  private static List<Periode> aggregatePeriodesNoSort(List<Periode> periodesTemp) {
    List<Periode> periodes = new ArrayList<>();
    Periode periodePrev = null;
    for (Periode periode : periodesTemp) {
      if (periodePrev == null) {
        periodePrev = periode;
        continue;
      }
      if (periodePrev.getFin() != null
          && !DateUtils.before(
              DateUtils.getStringDatePlusDays(periodePrev.getFin(), 1, DateUtils.FORMATTER),
              periode.getDebut(),
              DateUtils.FORMATTER)) {
        String maxDate =
            StringUtils.isNotBlank(periodePrev.getFin()) && StringUtils.isNotBlank(periode.getFin())
                ? DateUtils.getMaxDate(periodePrev.getFin(), periode.getFin(), DateUtils.FORMATTER)
                : null;
        setFin(periodePrev, maxDate);
      } else {
        periodes.add(periodePrev);
        periodePrev = periode;
      }
    }
    if (periodePrev != null) {
      periodes.add(periodePrev);
    }
    return periodes;
  }

  private static void setFin(Periode periodePrev, String maxDate) {
    if (maxDate != null) {
      periodePrev.setFin(maxDate);
    } else {
      periodePrev.setFinToNull();
    }
  }

  public static <T> T getDateFinOffline(
      T dateFinOffline, T dateFinFermetureOffline, T dateRestitution, boolean isExcluDemat) {
    if (dateRestitution == null) {
      if (isExcluDemat) {
        return dateFinOffline;
      } else {
        return (dateFinFermetureOffline != null) ? dateFinFermetureOffline : dateFinOffline;
      }
    } else {
      return dateRestitution;
    }
  }

  public static boolean isExcluDemat(String carteTPaEditerOuDigitale) {
    return Constants.ATTESTATION_DIGITALE_UNIQUEMENT_A_DELIVRER.equals(carteTPaEditerOuDigitale);
  }

  public static String getRandomString() {
    SecureRandom random = new SecureRandom();
    int leftLimit = 97; // letter 'a'
    int rightLimit = 122; // letter 'z'

    int randomSize = random.nextInt(15);

    StringBuilder buffer = new StringBuilder(randomSize);
    random
        .ints(randomSize, leftLimit, (rightLimit + 1))
        .forEach(number -> buffer.append((char) number));

    return buffer.toString();
  }

  public static String getFullLibelle(String exceptionCodeLibelle, String commentaire) {
    String libelle = exceptionCodeLibelle;

    if (StringUtils.isNotBlank(commentaire)) {
      libelle += " - " + commentaire;
    }

    return libelle;
  }
}
