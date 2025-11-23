package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.model.domain.trigger.PeriodeSuspensionDeclaration;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.ServicePrestationTriggerBenef;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodeSuspension;
import com.cegedim.next.serviceeligibility.core.utils.BusinessSortUtility;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SuspensionService {

  @ContinueSpan(log = "suspensionCalculation ServicePrestation")
  public String suspensionCalculation(
      final ServicePrestationTriggerBenef existing,
      final ServicePrestationTriggerBenef newContract) {
    final List<PeriodeSuspension> suspensions = newContract.getPeriodesSuspension();
    // If the new contract doesn't have any suspension...
    if (CollectionUtils.isEmpty(suspensions)) {
      if (existing != null) {
        return this.detectRemovedPeriodeFromSuspensions(
            suspensions, existing.getPeriodesSuspension());
      }

      return Constants.ERREUR_SUSPENSION;
    }

    BusinessSortUtility.triListePeriodesSupension(suspensions);

    // If the old contract doesn't exist or if it doesn't have any suspension...
    if (existing == null || CollectionUtils.isEmpty(existing.getPeriodesSuspension())) {
      final Periode p = suspensions.get(0).getPeriode();
      if (StringUtils.isNotBlank(p.getDebut())) {
        if (StringUtils.isBlank(p.getFin())) {
          return Constants.SUSPENSION;
        } else {
          return Constants.LEVEE_SUSPENSION;
        }
      }
    }
    // If the old contract exists and have a suspension...
    else {
      final List<PeriodeSuspension> existingSuspensions = existing.getPeriodesSuspension();
      BusinessSortUtility.triListePeriodesSupension(existingSuspensions);
      final Periode newPeriode = suspensions.get(0).getPeriode();
      final Periode existingPeriode = existingSuspensions.get(0).getPeriode();

      // If the old and new start of the suspension are different...
      if (!newPeriode.getDebut().equals(existingPeriode.getDebut())) {
        final String periodWasRemovedResult =
            this.detectRemovedPeriodeFromSuspensions(suspensions, existingSuspensions);
        if (periodWasRemovedResult.equals(Constants.ERREUR_SUSPENSION)) {
          if (StringUtils.isBlank(newPeriode.getFin())
              || newPeriode.getDebut().equals(newPeriode.getFin())) {
            return Constants.SUSPENSION;
          } else {
            return Constants.LEVEE_SUSPENSION;
          }
        }
      }
      // If the old and new start of the suspension are the same...
      else {
        // If the end dates are the same and not null...
        if (!StringUtils.isBlank(newPeriode.getFin())
            && newPeriode.getFin().equals(existingPeriode.getFin())) {
          return Constants.SUSPENSION;
        }
        // If both end dates are empty...
        else if (StringUtils.isBlank(newPeriode.getFin())
            && StringUtils.isBlank(existingPeriode.getFin())) {
          return Constants.SUSPENSION;
        }
        // If only the old contract have an end date...
        else if (!StringUtils.isBlank(existingPeriode.getFin())
            && StringUtils.isBlank(newPeriode.getFin())) {
          return Constants.SUSPENSION;
        }
        // If only the new contract have an end date...
        // OR if both end dates are different...
        else if ((!StringUtils.isBlank(newPeriode.getFin())
                && StringUtils.isBlank(existingPeriode.getFin()))
            || !newPeriode.getFin().equals(existingPeriode.getFin())) {
          return Constants.LEVEE_SUSPENSION;
        }
      }
    }
    return Constants.ERREUR_SUSPENSION;
  }

  private String detectRemovedPeriodeFromSuspensions(
      final List<PeriodeSuspension> suspensions,
      final List<PeriodeSuspension> existingSuspensions) {
    if (!CollectionUtils.isEmpty(existingSuspensions)) {
      final Periode existingPeriode = existingSuspensions.get(0).getPeriode();
      if (CollectionUtils.isEmpty(suspensions)) {
        // the last periode has been removed
        return Constants.LEVEE_SUSPENSION;
        // leve suspension
      } else {
        final Periode newPeriode = suspensions.get(0).getPeriode();
        // If the existingStartDate is bigger than new Start date, this means that the
        // existing Debut periode is later than new Debut Periode ->
        // this means that the periode has been removed
        return existingPeriode.getDebut().compareTo(newPeriode.getDebut()) > 1
            ? Constants.LEVEE_SUSPENSION
            : Constants.ERREUR_SUSPENSION;
      }
    }
    return Constants.ERREUR_SUSPENSION;
  }

  public PeriodeSuspensionDeclaration getLastSuspensionActive(
      List<PeriodeSuspensionDeclaration> periodesSuspension,
      String minPeriodeDebut,
      String maxPeriodeFin) {
    LocalDate droitDebut = DateUtils.parse(minPeriodeDebut, DateUtils.SLASHED_FORMATTER);
    LocalDate droitFin = DateUtils.parse(maxPeriodeFin, DateUtils.SLASHED_FORMATTER);

    // Récupérer les suspensions dans la période de droit
    List<PeriodeSuspensionDeclaration> suspensions =
        periodesSuspension.stream()
            .filter(suspension -> isWithinDroitPeriod(suspension, droitDebut, droitFin))
            .toList();

    // Trouver une suspension active en priorité
    Optional<PeriodeSuspensionDeclaration> lastActive =
        suspensions.stream()
            .filter(this::isActive)
            .max(
                Comparator.comparing(
                    PeriodeSuspensionDeclaration::getDebut,
                    Comparator.nullsLast(Comparator.naturalOrder())));

    // Si aucune suspension active, récupérer la dernière annulée
    return lastActive.orElseGet(
        () ->
            suspensions.stream()
                .max(
                    Comparator.comparing(
                        PeriodeSuspensionDeclaration::getDebut,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .orElse(null));
  }

  private boolean isWithinDroitPeriod(
      PeriodeSuspensionDeclaration suspension, LocalDate droitDebut, LocalDate droitFin) {
    LocalDate dateDebut = DateUtils.parse(suspension.getDebut(), DateUtils.SLASHED_FORMATTER);
    LocalDate dateFin = DateUtils.parse(suspension.getFin(), DateUtils.SLASHED_FORMATTER);

    return DateUtils.isOverlapping(dateDebut, dateFin, droitDebut, droitFin);
  }

  private boolean isActive(PeriodeSuspensionDeclaration suspension) {
    LocalDate dateDebut = DateUtils.parse(suspension.getDebut(), DateUtils.SLASHED_FORMATTER);
    LocalDate dateFin = DateUtils.parse(suspension.getFin(), DateUtils.SLASHED_FORMATTER);

    return DateUtils.isPeriodeValide(dateDebut, dateFin);
  }
}
