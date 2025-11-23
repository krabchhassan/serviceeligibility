package com.cegedim.next.serviceeligibility.core.model.domain.trigger;

import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodeContratCMUOuvert;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodeSuspension;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodesDroitsCarte;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

@Data
@NoArgsConstructor
public class ServicePrestationTriggerBenef {
  private String dateAdhesionMutuelle;
  private String dateDebutAdhesionIndividuelle;
  private String numeroAdhesionIndividuelle;

  private PeriodesDroitsCarte periodesDroitsCarte;

  private List<Periode> periodesContratResponsable;
  private List<PeriodeContratCMUOuvert> periodesContratCMU;

  private String dateSouscription;

  private String dateRadiation;
  private String dateResiliation;
  private String dateRestitution;

  private List<DroitAssure> droitsGaranties = new ArrayList<>();
  private List<PeriodeSuspension> periodesSuspension = new ArrayList<>();

  public ServicePrestationTriggerBenef(ServicePrestationTriggerBenef source) {
    this.dateAdhesionMutuelle = source.getDateAdhesionMutuelle();
    this.dateDebutAdhesionIndividuelle = source.getDateDebutAdhesionIndividuelle();
    this.numeroAdhesionIndividuelle = source.getNumeroAdhesionIndividuelle();
    this.dateSouscription = source.getDateSouscription();
    this.dateRadiation = source.getDateRadiation();
    this.dateResiliation = source.getDateResiliation();
    if (source.getPeriodesDroitsCarte() != null) {
      this.periodesDroitsCarte = new PeriodesDroitsCarte(source.getPeriodesDroitsCarte());
    }
    if (!CollectionUtils.isEmpty(source.getPeriodesContratCMU())) {
      this.periodesContratCMU = new ArrayList<>();
      for (PeriodeContratCMUOuvert periodeContratCMUOuvert : source.getPeriodesContratCMU()) {
        this.periodesContratCMU.add(new PeriodeContratCMUOuvert(periodeContratCMUOuvert));
      }
    }
    if (!CollectionUtils.isEmpty(source.getPeriodesContratResponsable())) {
      this.periodesContratResponsable = new ArrayList<>();
      for (Periode periodeResponsable : source.getPeriodesContratResponsable()) {
        this.periodesContratResponsable.add(new Periode(periodeResponsable));
      }
    }

    if (!CollectionUtils.isEmpty(source.getDroitsGaranties())) {
      this.droitsGaranties = new ArrayList<>();
      for (DroitAssure droitAssureV3 : source.getDroitsGaranties()) {
        this.droitsGaranties.add(new DroitAssure(droitAssureV3));
      }
    }

    if (!CollectionUtils.isEmpty(source.getPeriodesSuspension())) {
      this.periodesSuspension = new ArrayList<>();
      for (PeriodeSuspension periode : source.getPeriodesSuspension()) {
        this.periodesSuspension.add(new PeriodeSuspension(periode));
      }
    }
  }
}
