package com.cegedim.next.serviceeligibility.core.model.domain.contract;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.PeriodeComparable;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.util.CollectionUtils;

@Data
public class ContractTPCommun extends DocumentEntity implements GenericDomain<ContractTPCommun> {
  private String idDeclarant;
  private String numeroContrat;
  private String numeroAdherent;
  private String numeroAdherentComplet;
  private String dateSouscription;
  private String dateResiliation;
  private String type;
  private String nomPorteur;
  private String prenomPorteur;
  private String civilitePorteur;
  private String qualification;
  private String numeroContratCollectif;
  private Boolean isContratResponsable;
  private Boolean isContratCMU;
  private String destinataire;
  private String individuelOuCollectif;
  private String situationDebut;
  private String situationFin;
  private String motifFinSituation;
  private String typeConvention;
  private String critereSecondaire;
  private String critereSecondaireDetaille;
  private String numeroExterneContratIndividuel;
  private String numeroExterneContratCollectif;
  private String gestionnaire;
  private String groupeAssures;
  private String numAMCEchange;
  private String numOperateur;
  private String ordrePriorisation;
  private String contratCMUC2S;
  private String dateRestitution;
  private String origineDeclaration;

  @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
  private LocalDateTime dateCreation;

  @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
  private LocalDateTime dateModification;

  @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
  private LocalDateTime dateConsolidation;

  private SuspensionContract suspension;

  private String identifiantCollectivite;
  private String raisonSociale;
  private String siret;
  private String groupePopulation;

  private List<PeriodeCMUOuvert> periodeCMUOuverts;
  private List<PeriodeComparable> periodeResponsableOuverts;

  private String codeItelis;
  private String carteTPaEditerOuDigitale = "0";

  public ContractTPCommun() {
    /* empty constructor */ }

  public ContractTPCommun(ContractTPCommun source) {
    this.idDeclarant = source.getIdDeclarant();
    this.numeroContrat = source.getNumeroContrat();
    this.numeroAdherent = source.getNumeroAdherent();
    this.numeroAdherentComplet = source.getNumeroAdherentComplet();
    this.dateSouscription = source.getDateSouscription();
    this.dateResiliation = source.getDateResiliation();
    this.type = source.getType();
    this.nomPorteur = source.getNomPorteur();
    this.prenomPorteur = source.getPrenomPorteur();
    this.civilitePorteur = source.getCivilitePorteur();
    this.qualification = source.getQualification();
    this.numeroContratCollectif = source.getNumeroContratCollectif();
    this.isContratResponsable = source.getIsContratResponsable();
    this.isContratCMU = source.getIsContratCMU();
    this.destinataire = source.getDestinataire();
    this.individuelOuCollectif = source.getIndividuelOuCollectif();
    this.situationDebut = source.getSituationDebut();
    this.situationFin = source.getSituationFin();
    this.motifFinSituation = source.getMotifFinSituation();
    this.typeConvention = source.getTypeConvention();
    this.critereSecondaire = source.getCritereSecondaire();
    this.critereSecondaireDetaille = source.getCritereSecondaireDetaille();
    this.numeroExterneContratIndividuel = source.getNumeroExterneContratIndividuel();
    this.numeroExterneContratCollectif = source.getNumeroExterneContratCollectif();
    this.gestionnaire = source.getGestionnaire();
    this.groupeAssures = source.getGroupeAssures();
    this.numAMCEchange = source.getNumAMCEchange();
    this.numOperateur = source.getNumOperateur();
    this.ordrePriorisation = source.getOrdrePriorisation();
    this.contratCMUC2S = source.getContratCMUC2S();
    this.dateRestitution = source.getDateRestitution();
    this.origineDeclaration = source.getOrigineDeclaration();

    this.dateCreation = source.getDateCreation();
    this.dateModification = source.getDateModification();
    this.dateConsolidation = source.getDateConsolidation();

    if (source.getSuspension() != null) {
      this.suspension = new SuspensionContract(source.getSuspension());
    }
    this.codeItelis = source.getCodeItelis();
    this.identifiantCollectivite = source.getIdentifiantCollectivite();
    this.raisonSociale = source.getRaisonSociale();
    this.siret = source.getSiret();
    this.groupePopulation = source.getGroupePopulation();

    if (!CollectionUtils.isEmpty(source.getPeriodeCMUOuverts())) {
      this.periodeCMUOuverts = new ArrayList<>();
      for (PeriodeCMUOuvert periodeCMUOuvert : source.getPeriodeCMUOuverts()) {
        this.periodeCMUOuverts.add(new PeriodeCMUOuvert(periodeCMUOuvert));
      }
    }

    if (!CollectionUtils.isEmpty(source.getPeriodeResponsableOuverts())) {
      this.periodeResponsableOuverts = new ArrayList<>();
      for (PeriodeComparable periodeResponsableOuvert : source.getPeriodeResponsableOuverts()) {
        this.periodeResponsableOuverts.add(new PeriodeComparable(periodeResponsableOuvert));
      }
    }
  }

  @Override
  public int compareTo(final ContractTPCommun contrat) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.idDeclarant, contrat.idDeclarant);
    compareToBuilder.append(this.numeroContrat, contrat.numeroContrat);
    compareToBuilder.append(this.numeroAdherent, contrat.numeroAdherent);
    compareToBuilder.append(this.numeroAdherentComplet, contrat.numeroAdherentComplet);
    compareToBuilder.append(this.dateSouscription, contrat.dateSouscription);
    compareToBuilder.append(this.dateResiliation, contrat.dateResiliation);
    compareToBuilder.append(this.type, contrat.type);
    compareToBuilder.append(this.nomPorteur, contrat.nomPorteur);
    compareToBuilder.append(this.prenomPorteur, contrat.prenomPorteur);
    compareToBuilder.append(this.civilitePorteur, contrat.civilitePorteur);
    compareToBuilder.append(this.qualification, contrat.qualification);
    compareToBuilder.append(this.numeroContratCollectif, contrat.numeroContratCollectif);
    compareToBuilder.append(this.isContratResponsable, contrat.isContratResponsable);
    compareToBuilder.append(this.isContratCMU, contrat.isContratCMU);
    compareToBuilder.append(this.destinataire, contrat.destinataire);
    compareToBuilder.append(this.individuelOuCollectif, contrat.individuelOuCollectif);
    compareToBuilder.append(this.situationDebut, contrat.situationDebut);
    compareToBuilder.append(this.situationFin, contrat.situationFin);
    compareToBuilder.append(this.motifFinSituation, contrat.motifFinSituation);
    compareToBuilder.append(this.typeConvention, contrat.typeConvention);
    compareToBuilder.append(this.critereSecondaire, contrat.critereSecondaire);
    compareToBuilder.append(this.critereSecondaireDetaille, contrat.critereSecondaireDetaille);
    compareToBuilder.append(
        this.numeroExterneContratIndividuel, contrat.numeroExterneContratIndividuel);
    compareToBuilder.append(
        this.numeroExterneContratCollectif, contrat.numeroExterneContratCollectif);
    compareToBuilder.append(this.gestionnaire, contrat.gestionnaire);
    compareToBuilder.append(this.groupeAssures, contrat.groupeAssures);
    compareToBuilder.append(this.numAMCEchange, contrat.numAMCEchange);
    compareToBuilder.append(this.numOperateur, contrat.numOperateur);
    compareToBuilder.append(this.ordrePriorisation, contrat.ordrePriorisation);
    compareToBuilder.append(this.contratCMUC2S, contrat.contratCMUC2S);
    compareToBuilder.append(this.dateRestitution, contrat.dateRestitution);
    compareToBuilder.append(this.dateCreation, contrat.dateCreation);
    compareToBuilder.append(this.dateModification, contrat.dateModification);
    compareToBuilder.append(this.dateConsolidation, contrat.dateConsolidation);
    compareToBuilder.append(this.origineDeclaration, contrat.origineDeclaration);
    compareToBuilder.append(this.suspension, contrat.suspension);
    compareToBuilder.append(this.identifiantCollectivite, contrat.identifiantCollectivite);
    compareToBuilder.append(this.raisonSociale, contrat.raisonSociale);
    compareToBuilder.append(this.siret, contrat.siret);
    compareToBuilder.append(this.groupePopulation, contrat.groupePopulation);
    compareToBuilder.append(this.periodeCMUOuverts, contrat.periodeCMUOuverts);
    compareToBuilder.append(this.periodeResponsableOuverts, contrat.periodeResponsableOuverts);
    compareToBuilder.append(this.codeItelis, contrat.codeItelis);
    return compareToBuilder.toComparison();
  }

  public static boolean isDeclarationOfTheContract(ContractTP contractTP, Declaration declaration) {
    if (contractTP == null || declaration == null) {
      return false;
    }
    return StringUtils.equals(contractTP.getIdDeclarant(), declaration.getIdDeclarant())
        && StringUtils.equals(contractTP.getNumeroContrat(), declaration.getContrat().getNumero())
        && StringUtils.equals(
            contractTP.getNumeroAdherent(), declaration.getContrat().getNumeroAdherent());
  }
}
