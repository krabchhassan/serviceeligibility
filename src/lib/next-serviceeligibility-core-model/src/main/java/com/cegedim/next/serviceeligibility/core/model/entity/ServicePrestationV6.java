package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodeContratCMUOuvert;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodeSuspension;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContexteTPV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratCollectifV6;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@EqualsAndHashCode(callSuper = true)
public class ServicePrestationV6 extends ServicePrestationCommun
    implements GenericDomain<ServicePrestationV6> {
  private static final long serialVersionUID = 1L;

  private ContexteTPV6 contexteTiersPayant; // NOSONAR
  private List<PeriodeSuspension> periodesSuspension; // NOSONAR
  private List<PeriodeContratCMUOuvert> periodesContratCMUOuvert; // NOSONAR
  private ContratCollectifV6 contratCollectif; // NOSONAR

  @Field("assures")
  private Assure assure;

  @Override
  @JsonIgnore
  public String get_id() {
    return null;
  }

  @Override
  public int compareTo(final ServicePrestationV6 servicePrestation) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.getIdDeclarant(), servicePrestation.getIdDeclarant());
    compareToBuilder.append(this.getSocieteEmettrice(), servicePrestation.getSocieteEmettrice());
    compareToBuilder.append(this.getNumero(), servicePrestation.getNumero());
    compareToBuilder.append(this.getNumeroExterne(), servicePrestation.getNumeroExterne());
    compareToBuilder.append(this.getNumeroAdherent(), servicePrestation.getNumeroAdherent());
    compareToBuilder.append(
        this.getNumeroAdherentComplet(), servicePrestation.getNumeroAdherentComplet());
    compareToBuilder.append(this.getDateSouscription(), servicePrestation.getDateSouscription());
    compareToBuilder.append(this.getDateResiliation(), servicePrestation.getDateResiliation());
    compareToBuilder.append(this.getApporteurAffaire(), servicePrestation.getApporteurAffaire());
    compareToBuilder.append(
        this.getPeriodesContratResponsableOuvert(),
        servicePrestation.getPeriodesContratResponsableOuvert());
    compareToBuilder.append(
        this.getPeriodesContratCMUOuvert(), servicePrestation.getPeriodesContratCMUOuvert());
    compareToBuilder.append(
        this.getCritereSecondaireDetaille(), servicePrestation.getCritereSecondaireDetaille());
    compareToBuilder.append(this.getCritereSecondaire(), servicePrestation.getCritereSecondaire());
    compareToBuilder.append(
        this.getIsContratIndividuel(), servicePrestation.getIsContratIndividuel());
    compareToBuilder.append(this.getGestionnaire(), servicePrestation.getGestionnaire());
    compareToBuilder.append(this.getContratCollectif(), servicePrestation.getContratCollectif());
    compareToBuilder.append(this.getQualification(), servicePrestation.getQualification());
    compareToBuilder.append(this.getOrdrePriorisation(), servicePrestation.getOrdrePriorisation());
    compareToBuilder.append(
        this.getContexteTiersPayant(), servicePrestation.getContexteTiersPayant());
    compareToBuilder.append(
        this.getPeriodesSuspension(), servicePrestation.getPeriodesSuspension());
    compareToBuilder.append(this.assure, servicePrestation.assure);
    return compareToBuilder.toComparison();
  }
}
