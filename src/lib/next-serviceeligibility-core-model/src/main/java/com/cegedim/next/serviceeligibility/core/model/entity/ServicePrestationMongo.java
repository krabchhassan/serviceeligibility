package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.model.kafka.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.ContratCollectif;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.PorteurRisque;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodeSuspension;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "servicePrestation")
@Data
@EqualsAndHashCode(callSuper = true)
public class ServicePrestationMongo extends DocumentEntity
    implements GenericDomain<ServicePrestationMongo> {
  private static final long serialVersionUID = 1L;

  private String idDeclarant;
  private String societeEmettrice;
  private String numero;
  private String numeroExterne;
  private String numeroAdherent;
  private String numeroAdherentComplet;
  private String dateSouscription;
  private String dateResiliation;
  private String apporteurAffaire;
  private List<Periode> periodesContratResponsableOuvert;
  private List<Periode> periodesContratCMUOuvert;
  private String destinataire;
  private String critereSecondaireDetaille;
  private String critereSecondaire;
  private Boolean isContratIndividuel;
  private String gestionnaire;
  private ContratCollectif contratCollectif;
  private PorteurRisque porteurRisque;
  private String qualification;
  private String ordrePriorisation;
  private List<Periode> periodesDroitsComptablesOuverts;
  private List<PeriodeSuspension> periodesSuspension;
  private List<Assure> assures;

  @Override
  public int compareTo(final ServicePrestationMongo servicePrestation) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.idDeclarant, servicePrestation.idDeclarant);
    compareToBuilder.append(this.societeEmettrice, servicePrestation.societeEmettrice);
    compareToBuilder.append(this.numero, servicePrestation.numero);
    compareToBuilder.append(this.numeroExterne, servicePrestation.numeroExterne);
    compareToBuilder.append(this.numeroAdherent, servicePrestation.numeroAdherent);
    compareToBuilder.append(this.numeroAdherentComplet, servicePrestation.numeroAdherentComplet);
    compareToBuilder.append(this.dateSouscription, servicePrestation.dateSouscription);
    compareToBuilder.append(this.dateResiliation, servicePrestation.dateResiliation);
    compareToBuilder.append(this.apporteurAffaire, servicePrestation.apporteurAffaire);
    compareToBuilder.append(
        this.periodesContratResponsableOuvert, servicePrestation.periodesContratResponsableOuvert);
    compareToBuilder.append(
        this.periodesContratCMUOuvert, servicePrestation.periodesContratCMUOuvert);
    compareToBuilder.append(this.destinataire, servicePrestation.destinataire);
    compareToBuilder.append(
        this.critereSecondaireDetaille, servicePrestation.critereSecondaireDetaille);
    compareToBuilder.append(this.critereSecondaire, servicePrestation.critereSecondaire);
    compareToBuilder.append(this.isContratIndividuel, servicePrestation.isContratIndividuel);
    compareToBuilder.append(this.gestionnaire, servicePrestation.gestionnaire);
    compareToBuilder.append(this.contratCollectif, servicePrestation.contratCollectif);
    compareToBuilder.append(this.porteurRisque, servicePrestation.porteurRisque);
    compareToBuilder.append(this.qualification, servicePrestation.qualification);
    compareToBuilder.append(this.ordrePriorisation, servicePrestation.ordrePriorisation);
    compareToBuilder.append(
        this.periodesDroitsComptablesOuverts, servicePrestation.periodesDroitsComptablesOuverts);
    compareToBuilder.append(this.periodesSuspension, servicePrestation.periodesSuspension);
    compareToBuilder.append(this.assures, servicePrestation.assures);
    return compareToBuilder.toComparison();
  }
}
