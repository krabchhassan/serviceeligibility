package com.cegedim.next.serviceeligibility.core.utils;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.PeriodeDroitContractTP;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class UniqueAccessPointUtil {

  public static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern(Constants.YYYY_MM_DD);

  public static final DateTimeFormatter FORMATTERSLASHED =
      DateTimeFormatter.ofPattern(Constants.SLASHED_YYYY_MM_DD);

  private UniqueAccessPointUtil() {
    // Utils
  }

  public static void formatDatesForRequest(final UniqueAccessPointRequest requete) {
    final LocalDate startDate = DateUtils.stringToDate(requete.getStartDate());
    final LocalDate endDate = DateUtils.stringToDate(requete.getEndDate());

    if (startDate != null) {
      requete.setStartDate(FORMATTER.format(startDate));
    }
    if (endDate != null) {
      requete.setEndDate(FORMATTER.format(endDate));
    }
  }

  public static void formatDatesContractForRequest(final UniqueAccessPointRequest requete) {
    final LocalDate startDate = DateUtils.stringToDate(requete.getStartDate());
    final LocalDate endDate = DateUtils.stringToDate(requete.getEndDate());

    if (startDate != null) {
      requete.setStartDate(FORMATTERSLASHED.format(startDate));
    }
    if (endDate != null) {
      requete.setEndDate(FORMATTERSLASHED.format(endDate));
    }
  }

  public static Set<String> extractNumPersonnes(Collection<BenefAIV5> benefList) {
    if (CollectionUtils.isEmpty(benefList)) return Collections.emptySet(); // early exit

    return benefList.stream()
        .map(b -> b.getIdentite().getNumeroPersonne())
        .collect(Collectors.toSet());
  }

  public static boolean isOverlappingPeriod(
      UniqueAccessPointRequest request, PeriodeDroitContractTP periodeDroitContract) {
    // TDB : pas de date online
    LocalDate dateFin = null;
    if (!ContextConstants.TP_ONLINE.equals(request.getContext())
        && periodeDroitContract.getPeriodeFinFermeture() != null) {
      dateFin =
          LocalDate.parse(
              periodeDroitContract.getPeriodeFinFermeture(),
              DateTimeFormatter.ofPattern(DateUtils.FORMATTERSLASHED));
    } else if (periodeDroitContract.getPeriodeFin() != null) {
      dateFin =
          LocalDate.parse(
              periodeDroitContract.getPeriodeFin(),
              DateTimeFormatter.ofPattern(DateUtils.FORMATTERSLASHED));
    }
    LocalDate endDate = null;
    if (request.getEndDate() != null) {
      endDate = LocalDate.parse(request.getEndDate());
    }
    return DateUtils.isOverlapping(
        LocalDate.parse(request.getStartDate()),
        endDate,
        LocalDate.parse(
            periodeDroitContract.getPeriodeDebut(),
            DateTimeFormatter.ofPattern(DateUtils.FORMATTERSLASHED)),
        dateFin);
  }

  public static void formatDatesContractForRequestV5(final UniqueAccessPointRequest requete) {
    final LocalDate startDate = DateUtils.stringToDate(requete.getStartDate());
    final LocalDate endDate = DateUtils.stringToDate(requete.getEndDate());

    if (startDate != null) {
      requete.setStartDateForMongoContract(FORMATTERSLASHED.format(startDate));
    }
    if (endDate != null) {
      requete.setEndDateForMongoContract(FORMATTERSLASHED.format(endDate));
    }
  }
}
