package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.ContratAICommun;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssureCommun;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.SocieteEmettrice;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

@Service
public class BenefInfosService {
  public static final String DATE_FIN_INFINI = "9999-12-31";

  public List<Periode> handlePeriodesContratForBenef(ContratAICommun contrat, Assure assure) {
    List<Periode> resultPeriodes = new ArrayList<>();
    Periode periodeContrat = new Periode();
    periodeContrat.setDebut(contrat.getDateSouscription());
    periodeContrat.setFin(contrat.getDateResiliation());

    List<Periode> periodesAssure = new ArrayList<>(assure.getPeriodes());
    for (Periode periode : assure.getPeriodes()) {
      periodesAssure.add(new Periode(periode));
    }
    List<Periode> periodesDroits = new ArrayList<>();
    for (DroitAssureCommun droitAssureCommun : assure.getDroits()) {
      periodesDroits.add(new Periode(droitAssureCommun.getPeriode()));
    }
    // Fusionner les périodes qui se chevauchent ou qui sont adjacentes
    periodesAssure = DateUtils.getPeriodesFusionnees(periodesAssure);
    periodesDroits = DateUtils.getPeriodesFusionnees(periodesDroits);

    List<Periode> finalPeriodesDroits = periodesDroits;
    periodesAssure.stream()
        .filter(
            periodeA -> {
              String dateFinAssure =
                  (assure.getDateRadiation() != null
                          && (periodeA.getFin() == null
                              || DateUtils.before(assure.getDateRadiation(), periodeA.getFin())))
                      ? assure.getDateRadiation()
                      : periodeA.getFin();
              return DateUtils.isOverlapping(
                  periodeA.getDebut(),
                  dateFinAssure,
                  periodeContrat.getDebut(),
                  periodeContrat.getFin());
            })
        .forEach(
            periodeA -> {
              String dateDebut =
                  DateUtils.getMaxDate(
                      periodeA.getDebut(), periodeContrat.getDebut(), DateUtils.FORMATTER);
              String dateFinAssure =
                  (assure.getDateRadiation() != null
                          && (periodeA.getFin() == null
                              || DateUtils.before(assure.getDateRadiation(), periodeA.getFin())))
                      ? assure.getDateRadiation()
                      : periodeA.getFin();
              String dateFin =
                  DateUtils.getMinDate(dateFinAssure, periodeContrat.getFin(), DateUtils.FORMATTER);
              addResultPeriod(resultPeriodes, finalPeriodesDroits, dateDebut, dateFin);
            });

    return resultPeriodes;
  }

  private void addResultPeriod(
      List<Periode> resultPeriodes,
      List<Periode> finalPeriodesDroits,
      String dateDebut,
      String dateFin) {
    finalPeriodesDroits.stream()
        .filter(
            periodeD ->
                DateUtils.isOverlapping(periodeD.getDebut(), periodeD.getFin(), dateDebut, dateFin))
        .forEach(
            periodeD -> {
              Periode resultPeriode = new Periode();
              resultPeriode.setDebut(
                  DateUtils.getMaxDate(periodeD.getDebut(), dateDebut, DateUtils.FORMATTER));
              String minDate =
                  DateUtils.getMinDate(periodeD.getFin(), dateFin, DateUtils.FORMATTER);
              resultPeriode.setFin(minDate != null ? minDate : DATE_FIN_INFINI);
              resultPeriodes.add(resultPeriode);
            });
  }

  public List<SocieteEmettrice> handlePeriodesSocieteEmettriceForBenef(
      List<ContratV5> benefContrats) {
    return benefContrats.stream()
        .filter(contract -> contract.getPeriodes() != null)
        .collect(
            Collectors.groupingBy(
                ContratV5::getSocieteEmettrice,
                Collectors.flatMapping(
                    contract ->
                        contract.getPeriodes() != null
                            ? contract.getPeriodes().stream()
                            : Stream.empty(),
                    Collectors.toList())))
        .entrySet()
        .stream()
        .filter(entry -> !entry.getValue().isEmpty())
        .map(
            entry -> {
              SocieteEmettrice societeEmettrice = new SocieteEmettrice();
              societeEmettrice.setCode(entry.getKey());
              List<Periode> periodesContrats = new ArrayList<>();
              for (Periode periode : (entry.getValue())) {
                periodesContrats.add(new Periode(periode));
              }
              List<Periode> mergedPeriodes = DateUtils.getPeriodesFusionnees(periodesContrats);
              societeEmettrice.setPeriodes(mergedPeriodes);
              return societeEmettrice;
            })
        .toList();
  }
}
