package com.cegedim.next.serviceeligibility.core.business.consultationdroits.idb;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ContractDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.contract.DomaineDroitContratDto;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Slf4j
@Service("IDBSteps")
public class IDBSteps {

  protected Map<String, Map<String, List<Periode>>> getPeriodesByContract(
      List<ContractDto> contractList) {
    Map<String, Map<String, List<Periode>>> periodesByContract = new HashMap<>();
    for (ContractDto contrat : contractList) {
      periodesByContract.put(contrat.getContrat().getNumero(), new HashMap<>());
      for (DomaineDroitContratDto domaineDroitContrat : contrat.getDomaineDroits()) {
        List<Periode> periodeDroitContracts =
            domaineDroitContrat.getGaranties().stream()
                .flatMap(garantie -> garantie.getProduits().stream())
                .flatMap(produit -> produit.getReferencesCouverture().stream())
                .flatMap(referenceCouverture -> referenceCouverture.getNaturesPrestation().stream())
                .flatMap(naturePrestation -> naturePrestation.getPeriodesDroit().stream())
                .map(
                    periodeDroitContract ->
                        new Periode(
                            periodeDroitContract.getPeriodeDebut(),
                            periodeDroitContract.getPeriodeFin()))
                .collect(Collectors.toList());
        List<Periode> periodesFusionnees =
            DateUtils.getPeriodesFusionnees(
                periodeDroitContracts, DateTimeFormatter.ofPattern(DateUtils.FORMATTERSLASHED));
        periodesByContract
            .get(contrat.getContrat().getNumero())
            .put(domaineDroitContrat.getCode(), periodesFusionnees);
      }
    }
    return periodesByContract;
  }

  protected void truncatePeriode(Periode periode, Date dateDebut, Date dateFin) {
    if (dateDebut != null) {
      LocalDate debut =
          DateUtils.parseLocalDate(DateUtils.formatDate(dateDebut), DateUtils.FORMATTERSLASHED);
      LocalDate fin = null;
      if (dateFin != null) {
        fin = DateUtils.parseLocalDate(DateUtils.formatDate(dateFin), DateUtils.FORMATTERSLASHED);
      }
      if (fin == null || !debut.isAfter(fin)) {
        LocalDate periodeDebut =
            DateUtils.parseLocalDate(periode.getDebut(), DateUtils.FORMATTERSLASHED);
        LocalDate periodeFin = null;
        if (periode.getFin() != null) {
          periodeFin = DateUtils.parseLocalDate(periode.getFin(), DateUtils.FORMATTERSLASHED);
        }
        if (DateUtils.isOverlapping(periodeDebut, periodeFin, debut, fin)) {
          if (periodeDebut != null && periodeDebut.isBefore(debut)) {
            periode.setDebut(DateUtils.formatDate(debut, DateUtils.SLASHED_FORMATTER));
          }
          if (fin != null && (periodeFin == null || periodeFin.isAfter(fin))) {
            periode.setFin(DateUtils.formatDate(fin, DateUtils.SLASHED_FORMATTER));
          }
        }
      }
    }
  }

  protected Pair<String, Periode> selectContractAndPeriodToReturn(
      List<Pair<String, Periode>> closestContractsPeriods, String startDate, String endDate) {
    // On sélectionne les contrats qui couvrent/chevauchent la période demandée
    List<Pair<String, Periode>> closestContractsPeriodsOverlapping =
        closestContractsPeriods.stream()
            .filter(
                contractPeriodePair ->
                    DateUtils.isOverlapping(
                        startDate,
                        endDate,
                        contractPeriodePair.getSecond().getDebut(),
                        contractPeriodePair.getSecond().getFin()))
            .collect(Collectors.toList());
    if (closestContractsPeriodsOverlapping.isEmpty()) {
      return null;
    }
    if (closestContractsPeriodsOverlapping.size() == 1) {
      return closestContractsPeriodsOverlapping.get(0);
    }

    // Si plusieurs contrats couvrent la période demandée
    // => step 1 : on sélectionne le contrat ayant la date de début la plus proche
    // du début de la période demandée
    Comparator<Pair<String, Periode>> comparatorDebut =
        Comparator.comparing(numContratPeriodePair -> numContratPeriodePair.getSecond().getDebut());
    closestContractsPeriodsOverlapping.sort(comparatorDebut);
    String closestStartDate = closestContractsPeriodsOverlapping.get(0).getSecond().getDebut();
    List<Pair<String, Periode>> closestPairs =
        closestContractsPeriodsOverlapping.stream()
            .filter(
                contractPeriodePair ->
                    contractPeriodePair.getSecond().getDebut().equals(closestStartDate))
            .collect(Collectors.toList());

    // Si plusieurs contrats ont la date de début la plus proche de celle demandée
    // => step 2 : on sélectionne le contrat ayant la plus grande date de fin
    if (closestPairs.size() > 1) {
      Comparator<String> nullFirstComparator = Comparator.nullsFirst(Comparator.reverseOrder());
      Comparator<Pair<String, Periode>> comparatorFin =
          (pairA, pairB) ->
              nullFirstComparator.compare(pairA.getSecond().getFin(), pairB.getSecond().getFin());
      closestPairs.sort(comparatorFin);
      String greaterEndDate = closestPairs.get(0).getSecond().getFin();
      List<Pair<String, Periode>> pairsWithGreaterEndDate =
          closestPairs.stream()
              .filter(
                  contractPeriodePair -> {
                    if (greaterEndDate == null) {
                      return contractPeriodePair.getSecond().getFin() == null;
                    } else {
                      return contractPeriodePair.getSecond().getFin().equals(greaterEndDate);
                    }
                  })
              .toList();

      // Si plusieurs contrats ont la plus grande date de fin
      // => step 3 : on sélectionne le contrat ayant le plus petit numéro contrat
      if (pairsWithGreaterEndDate.size() > 1) {
        Comparator<Pair<String, Periode>> comparatorNumeroContrat =
            Comparator.comparing(Pair::getFirst);
        Optional<Pair<String, Periode>> optional =
            pairsWithGreaterEndDate.stream().min(comparatorNumeroContrat);
        return optional.orElse(null);
      } else {
        return pairsWithGreaterEndDate.get(0);
      }
    } else {
      return closestPairs.get(0);
    }
  }
}
