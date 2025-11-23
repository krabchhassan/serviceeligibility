package com.cegedim.next.serviceeligibility.batch635.job.helpers;

import static com.cegedim.next.serviceeligibility.batch635.job.helpers.Helper.*;
import static com.cegedim.next.serviceeligibility.batch635.job.utils.Constants.*;

import com.cegedim.next.serviceeligibility.batch635.job.domain.repository.projection.PeriodeDroitProjection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.function.Predicate;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ExtractionItem {

  private String extractionTime;
  private String referenceDate;
  private String databaseType;
  private String databaseId;
  private String amc;
  private String beneficiaryNir;
  private String beneficiaryNirKey;
  private String beneficiaryBirthDate;
  private String birthDateRank;
  private String periodeDroitStartDate;
  private String periodeDroitEndDate;
  private String adherentNumber;
  private String contratNumber;
  private String personneNumber;
  private String beneficiaryLastName;
  private String beneficiaryFirstName;

  private static ExtractionItem from(
      PeriodeDroitProjection item,
      String amc,
      String referenceDate,
      String extractionTime,
      String identity) {
    return ExtractionItem.builder()
        .extractionTime(extractionTime)
        .referenceDate(referenceDate)
        .databaseType(DATABASE_TYPE)
        .databaseId(identity)
        .amc(amc)
        .beneficiaryNir(item.getNirOd1())
        .beneficiaryNirKey(item.getCleNirOd1())
        .beneficiaryBirthDate(item.getDateNaissance())
        .birthDateRank(item.getRangNaissance())
        .periodeDroitStartDate(COMMON_DATE_FORMATTER.format(item.getPeriodeDebutParsed()))
        .periodeDroitEndDate(COMMON_DATE_FORMATTER.format(item.getPeriodeFinParsed()))
        .adherentNumber(item.getNumeroAdherent())
        .contratNumber(item.getNumeroContrat())
        .personneNumber(item.getNumeroPersonne())
        .beneficiaryFirstName(item.getPrenom())
        .beneficiaryLastName(item.getNom())
        .build();
  }

  @SneakyThrows
  public static List<ExtractionItem> from(
      List<PeriodeDroitProjection> items,
      String amc,
      String referenceDate,
      String identity,
      ExtractionStatusValueHolder extractionStatusValueHolder) {
    LocalDate referenceDateParsed = LocalDate.parse(referenceDate, COMMON_DATE_FORMATTER);
    limitPeriodes(items, referenceDateParsed.getYear());
    handleExtractionStatus(items, referenceDateParsed, extractionStatusValueHolder);
    String extractionTime =
        LocalDateTime.now(ZoneOffset.UTC).format(EXTRACTION_TIME_DATE_FORMATTER);
    return items.stream()
        .map(item -> ExtractionItem.from(item, amc, referenceDate, extractionTime, identity))
        .toList();
  }

  private static void limitPeriodes(List<PeriodeDroitProjection> items, int civilYear) {
    items.stream()
        .forEach(
            item -> {
              limitPeriodeDebut(item, civilYear);
              limitPeriodeFin(item, civilYear);
            });
  }

  private static void limitPeriodeDebut(PeriodeDroitProjection item, int civilYear) {
    LocalDate periodeDebut = item.getPeriodeDebutParsed();
    if (periodeDebut.getYear() < civilYear) {
      LocalDate newPeriodeDebut = LocalDate.of(civilYear, 1, 1);
      item.setPeriodeDebutParsed(newPeriodeDebut);
    }
  }

  private static void limitPeriodeFin(PeriodeDroitProjection item, int civilYear) {
    LocalDate periodeFin = item.getPeriodeFinParsed();
    if (periodeFin.getYear() > civilYear) {
      LocalDate newPeriodeFin = LocalDate.of(civilYear, 12, 31);
      item.setPeriodeFinParsed(newPeriodeFin);
    }
  }

  private static void handleExtractionStatus(
      List<PeriodeDroitProjection> items,
      LocalDate referenceDate,
      ExtractionStatusValueHolder extractionStatusValueHolder) {
    if (extractionStatusValueHolder.getExtractionStatus() == null
        || !EXTRACTION_INDICATOR_V.equals(extractionStatusValueHolder.getExtractionStatus())) {
      String extractionStatus =
          items.stream().anyMatch(isReferenceDateBetweenPeriodeDebutAndPeriodeFin(referenceDate))
              ? EXTRACTION_INDICATOR_V
              : EXTRACTION_INDICATOR_R;
      extractionStatusValueHolder.setExtractionStatus(extractionStatus);
    }
  }

  private static Predicate<PeriodeDroitProjection> isReferenceDateBetweenPeriodeDebutAndPeriodeFin(
      LocalDate referenceDate) {
    return (PeriodeDroitProjection periodeDroitProjection) ->
        isDateBetween(
            periodeDroitProjection.getPeriodeDebutParsed(),
            periodeDroitProjection.getPeriodeFinParsed(),
            referenceDate);
  }
}
