package com.cegedim.next.serviceeligibility.core.services.event;

import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

@Service
public class EventInsuredTerminationService {

  public int manageEventsInsuredTermination(
      EventService eventService, ContratAIV6 newContract, ContratAIV6 existingContract) {
    int nbSentEvent = 0;
    final String dateResiliation = newContract.getDateResiliation();
    for (Assure assure : newContract.getAssures()) {
      String dateRadiation = assure.getDateRadiation();
      List<String> datesFermeturePeriodeAssure =
          getAllDatesFermeturePeriodeAssure(assure.getPeriodes());
      String previousEndDate = null;
      if (existingContract != null) {
        previousEndDate = getPreviousEndDate(existingContract, assure);
      }
      int condition = computeCondition(dateResiliation, dateRadiation, datesFermeturePeriodeAssure);

      switch (condition) {
        case 4:
          // cas DateResiliation before today
          nbSentEvent +=
              sendEvent(eventService, newContract, assure, previousEndDate, dateResiliation);
          break;
        case 2:
          // cas DateRadiation before today
          nbSentEvent +=
              sendEvent(eventService, newContract, assure, previousEndDate, dateRadiation);
          break;
        case 1:
          // cas DatePeriodePresenceAssure before today
          List<String> previousEndDates = new ArrayList<>();
          if (existingContract != null) {
            previousEndDates = getAllPreviousEndDates(existingContract, assure);
          }
          nbSentEvent +=
              sendEventWhenFermeturePeriode(eventService, newContract, assure, previousEndDates);
          break;
        case 7, 5, 3:
          // cas DateResiliation && DateRadiation && DatePeriodePresenceAssure before
          // today
          // cas DateResiliation && DatePeriodePresenceAssure before today
          // cas DateRadiation && DatePeriodePresenceAssure before today
          nbSentEvent +=
              sendEventWhenSeveralDates(
                  eventService, newContract, assure, datesFermeturePeriodeAssure, previousEndDate);
          break;
        case 6:
          // cas DateResiliation && DateRadiation before today
          nbSentEvent +=
              sendEventWhenSeveralDates(eventService, newContract, assure, null, previousEndDate);
          break;
        default:
          break;
      }
    }
    return nbSentEvent;
  }

  public void manageEventsInsuredTerminationSansEffet(
      EventService eventService, Assure assure, ContratAIV6 newContract, String dateSouscription) {
    eventService.sendObservabilityEventInsuredTermination(
        newContract, assure, null, dateSouscription);
  }

  /**
   * Calcule la condition utilisée pour envoyer les events 'insured termination'
   *
   * @param dateResiliation
   * @param dateRadiation
   * @param datesFermeturePeriodeAssure
   * @return un entier compris entre 0 et 7
   */
  public static int computeCondition(
      String dateResiliation, String dateRadiation, List<String> datesFermeturePeriodeAssure) {
    final String dateToday = DateUtils.formatDate(new Date(), DateUtils.YYYY_MM_DD);

    return computeCondition(dateResiliation, dateRadiation, datesFermeturePeriodeAssure, dateToday);
  }

  public static int computeCondition(
      String dateResiliation,
      String dateRadiation,
      List<String> datesFermeturePeriodeAssure,
      String retentionDate) {
    // Condition initialisée à 0 (sur 3 bits -> 000 en binaire)
    // Le bit de gauche représente la condition isDateResilBeforeToday
    // Le bit de milieu représente la condition isDateRadiaBeforeToday
    // Le bit de droite représente la condition isDatePeriodeAssureBeforeToday
    // 1) Si isDateResilBeforeToday alors condition = 4 (100 en binaire)
    // 2) Si isDateRadiaBeforeToday alors condition = 2 (010 en binaire)
    // 3) Si isDatePeriodeAssureBeforeToday alors condition = 1 (001 en binaire)
    // Par exemple, si les conditions n°1 et n°3 sont vraies et la condition n°2 est
    // fausse, on obtient
    // condition = 5 (101 en binaire)
    int condition = 0;
    boolean isDateResilBeforeToday =
        dateResiliation != null
            && DateUtils.before(dateResiliation, retentionDate, DateUtils.FORMATTER);
    boolean isDateRadiaBeforeToday =
        dateRadiation != null
            && DateUtils.before(dateRadiation, retentionDate, DateUtils.FORMATTER);
    boolean isDatePeriodeAssureBeforeToday =
        CollectionUtils.isNotEmpty(datesFermeturePeriodeAssure)
            && datesFermeturePeriodeAssure.stream()
                .anyMatch(
                    date ->
                        date != null && DateUtils.before(date, retentionDate, DateUtils.FORMATTER));
    condition = condition | BooleanUtils.toInteger(isDateResilBeforeToday) << 2;
    condition = condition | BooleanUtils.toInteger(isDateRadiaBeforeToday) << 1;
    condition = condition | BooleanUtils.toInteger(isDatePeriodeAssureBeforeToday);
    return condition;
  }

  private int sendEventWhenSeveralDates(
      EventService eventService,
      ContratAIV6 newContract,
      Assure assure,
      List<String> datesFermeturePeriodeAssure,
      String previousEndDate) {
    int nbSentEvent = 0;
    List<String> datesToCompare = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(datesFermeturePeriodeAssure)) {
      String minDateFermeturePeriodeAssure =
          DateUtils.getMinDateOrNull(datesFermeturePeriodeAssure);
      if (minDateFermeturePeriodeAssure != null) {
        datesToCompare.add(minDateFermeturePeriodeAssure);
      }
    }
    if (newContract.getDateResiliation() != null) {
      datesToCompare.add(newContract.getDateResiliation());
    }
    if (assure.getDateRadiation() != null) {
      datesToCompare.add(assure.getDateRadiation());
    }
    String minDate = Collections.min(datesToCompare);
    if (previousEndDate == null
        || DateUtils.before(minDate, previousEndDate, DateUtils.FORMATTER)) {
      eventService.sendObservabilityEventInsuredTermination(
          newContract, assure, previousEndDate, minDate);
      nbSentEvent++;
    }
    return nbSentEvent;
  }

  private int sendEventWhenFermeturePeriode(
      EventService eventService,
      ContratAIV6 newContract,
      Assure assure,
      List<String> previousEndDates) {
    int nbSentEvents = 0;
    Date dateToday = new Date();
    LocalDate localDateToday = dateToday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    List<String> datesFermeture = getAllDatesFermeturePeriodeAssure(assure.getPeriodes());
    List<ImmutablePair<String, String>> newAndOldDatesFin =
        getPairsNewAndOldDate(previousEndDates, datesFermeture);

    for (ImmutablePair<String, String> pair : newAndOldDatesFin) {
      String oldDate = pair.getLeft();
      String newDate = pair.getRight();
      LocalDate newLocalDate =
          newDate != null ? DateUtils.parseLocalDate(newDate, DateUtils.YYYY_MM_DD) : null;
      LocalDate oldLocalDate =
          oldDate != null ? DateUtils.parseLocalDate(oldDate, DateUtils.YYYY_MM_DD) : null;

      if ((newLocalDate != null
          && newLocalDate.isBefore(localDateToday)
          && (oldLocalDate == null || newLocalDate.isBefore(oldLocalDate)))) {
        eventService.sendObservabilityEventInsuredTermination(
            newContract, assure, oldDate, newDate);
        nbSentEvents++;
      }
    }
    return nbSentEvents;
  }

  private int sendEvent(
      EventService eventService,
      ContratAIV6 newContract,
      Assure assure,
      String previousEndDate,
      String dateToCompare) {
    int nbSentEvent = 0;
    if (previousEndDate == null
        || DateUtils.before(dateToCompare, previousEndDate, DateUtils.FORMATTER)) {
      eventService.sendObservabilityEventInsuredTermination(
          newContract, assure, previousEndDate, dateToCompare);
      nbSentEvent++;
    }
    return nbSentEvent;
  }

  public String getPreviousEndDate(ContratAIV6 existingContract, Assure assure) {
    Optional<Assure> existingAssure = getExistingAssure(existingContract, assure);
    return existingAssure
        .map(
            value ->
                getDateFermeture(
                    existingContract.getDateResiliation(),
                    value.getDateRadiation(),
                    value.getPeriodes()))
        .orElse(null);
  }

  public List<String> getAllPreviousEndDates(ContratAIV6 existingContract, Assure assure) {
    Optional<Assure> existingAssure = getExistingAssure(existingContract, assure);
    return existingAssure
        .map(value -> getAllDatesFermeturePeriodeAssure(value.getPeriodes()))
        .orElse(Collections.emptyList());
  }

  private static Optional<Assure> getExistingAssure(ContratAIV6 existingContract, Assure assure) {
    return existingContract.getAssures().stream()
        .filter(
            assure1 ->
                assure
                    .getIdentite()
                    .getNumeroPersonne()
                    .equals(assure1.getIdentite().getNumeroPersonne()))
        .findFirst();
  }

  /**
   * Crée une liste de dates de fermeture du contrat existant et du nouveau contrat Afin de pouvoir
   * détecter de nouvelles fermetures de periodes assures
   */
  public List<ImmutablePair<String, String>> getPairsNewAndOldDate(
      List<String> previousEndDates, List<String> datesFermeture) {
    List<ImmutablePair<String, String>> pairs = new ArrayList<>();
    for (int i = 0; i < datesFermeture.size(); i++) {
      String oldDate = i < previousEndDates.size() ? previousEndDates.get(i) : null;
      String newDate = datesFermeture.get(i);
      pairs.add(ImmutablePair.of(oldDate, newDate));
    }
    return pairs;
  }

  public List<String> getDatesFermeturePeriodeAssure(List<Periode> periodeAssures) {
    List<String> datesFerm = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(periodeAssures)) {
      List<Periode> aggregatePeriodes = DateUtils.getPeriodesFusionnees(periodeAssures);
      Periode periode = aggregatePeriodes.stream().reduce((first, second) -> second).orElse(null);
      if (periode != null && periode.getFin() != null) {
        datesFerm.add(periode.getFin());
      }
    }
    return datesFerm;
  }

  public List<String> getAllDatesFermeturePeriodeAssure(List<Periode> periodeAssures) {
    List<String> datesFerm = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(periodeAssures)) {
      List<Periode> aggregatePeriodes = DateUtils.getPeriodesFusionnees(periodeAssures);
      for (Periode periode : aggregatePeriodes) {
        if (periode != null) {
          // BLUE-6073 : on stocke toutes les fins de périodes y compris les null
          // pour pouvoir gérer les events dans le cas où on a plusieurs periodes assurés
          datesFerm.add(periode.getFin());
        }
      }
    }
    return datesFerm;
  }

  private String getDateFermeture(
      String dateResiliation, String dateRadiation, List<Periode> periodeAssures) {
    List<String> datesFerm = new ArrayList<>();
    if (dateResiliation != null) {
      datesFerm.add(dateResiliation);
    }
    if (dateRadiation != null) {
      datesFerm.add(dateRadiation);
    }
    datesFerm.addAll(getDatesFermeturePeriodeAssure(periodeAssures));
    return datesFerm.stream().min(String::compareTo).orElse(null);
  }
}
