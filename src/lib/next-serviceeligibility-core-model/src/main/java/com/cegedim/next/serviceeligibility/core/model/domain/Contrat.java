package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.PeriodeCMUOuvert;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.PeriodeComparable;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.PeriodeSuspensionDeclaration;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

/** Classe qui mappe le document Contrat de la déclaration */
@Data
public class Contrat implements GenericDomain<Contrat> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String numero;
  private String dateSouscription;
  private String dateResiliation;
  private String type;
  private String nomPorteur;
  private String prenomPorteur;
  private String civilitePorteur;
  private String numeroAdherent;
  private String numeroAdherentComplet;
  private String qualification;
  private String numeroContratCollectif;
  private String rangAdministratif;
  private Boolean isContratResponsable = false;
  private Boolean isContratCMU = false;
  private String contratCMUC2S = "0";
  private String destinataire;
  private String individuelOuCollectif;
  private String situationDebut;
  private String situationFin;
  private String motifFinSituation;
  private String lienFamilial;
  private String categorieSociale;
  private String situationParticuliere;
  private String typeConvention;
  private String critereSecondaireDetaille;
  private String critereSecondaire;
  private String numeroExterneContratIndividuel;
  private String numeroExterneContratCollectif;
  private String modePaiementPrestations;
  private String gestionnaire;
  private String groupeAssures;
  private String numeroCarte;
  private String editeurCarte;
  private String fondCarte;
  private String annexe1Carte;
  private String annexe2Carte;
  private String numAMCEchange;
  private String numOperateur;
  private String ordrePriorisation;

  private String identifiantCollectivite;
  private String raisonSociale;
  private String siret;
  private String groupePopulation;

  private String codeRenvoi;
  private String libelleCodeRenvoi;

  private List<PeriodeSuspensionDeclaration> periodeSuspensions;

  private String codeItelis;

  private List<PeriodeCMUOuvert> periodeCMUOuverts;
  private List<PeriodeComparable> periodeResponsableOuverts;

  private List<DroitHtp> droitHtps = new ArrayList<>();

  public Contrat() {
    /* empty constructor */ }

  public Contrat(Contrat source) {
    this.numero = source.getNumero();
    this.dateSouscription = source.getDateSouscription();
    this.dateResiliation = source.getDateResiliation();
    this.type = source.getType();
    this.nomPorteur = source.getNomPorteur();
    this.prenomPorteur = source.getPrenomPorteur();
    this.civilitePorteur = source.getCivilitePorteur();
    this.numeroAdherent = source.getNumeroAdherent();
    this.numeroAdherentComplet = source.getNumeroAdherentComplet();
    this.qualification = source.getQualification();
    this.numeroContratCollectif = source.getNumeroContratCollectif();
    this.rangAdministratif = source.getRangAdministratif();
    this.isContratResponsable = source.getIsContratResponsable();
    this.isContratCMU = source.getIsContratCMU();
    this.contratCMUC2S = source.getContratCMUC2S();
    this.destinataire = source.getDestinataire();
    this.individuelOuCollectif = source.getIndividuelOuCollectif();
    this.situationDebut = source.getSituationDebut();
    this.situationFin = source.getSituationFin();
    this.motifFinSituation = source.getMotifFinSituation();
    this.lienFamilial = source.getLienFamilial();
    this.categorieSociale = source.getCategorieSociale();
    this.situationParticuliere = source.getSituationParticuliere();
    this.typeConvention = source.getTypeConvention();
    this.critereSecondaireDetaille = source.getCritereSecondaireDetaille();
    this.critereSecondaire = source.getCritereSecondaire();
    this.numeroExterneContratIndividuel = source.getNumeroExterneContratIndividuel();
    this.numeroExterneContratCollectif = source.getNumeroExterneContratCollectif();
    this.modePaiementPrestations = source.getModePaiementPrestations();
    this.gestionnaire = source.getGestionnaire();
    this.groupeAssures = source.getGroupeAssures();
    this.numeroCarte = source.getNumeroCarte();
    this.editeurCarte = source.getEditeurCarte();
    this.fondCarte = source.getFondCarte();
    this.annexe1Carte = source.getAnnexe1Carte();
    this.annexe2Carte = source.getAnnexe2Carte();
    this.numAMCEchange = source.getNumAMCEchange();
    this.numOperateur = source.getNumOperateur();
    this.ordrePriorisation = source.getOrdrePriorisation();
    this.identifiantCollectivite = source.getIdentifiantCollectivite();
    this.raisonSociale = source.getRaisonSociale();
    this.siret = source.getSiret();
    this.groupePopulation = source.getGroupePopulation();
    this.codeRenvoi = source.getCodeRenvoi();
    this.libelleCodeRenvoi = source.getLibelleCodeRenvoi();
    this.codeItelis = source.getCodeItelis();
    this.periodeSuspensions = source.getPeriodeSuspensions();
    this.periodeCMUOuverts = source.getPeriodeCMUOuverts();
    this.periodeResponsableOuverts = source.getPeriodeResponsableOuverts();
    for (DroitHtp droitHtp : source.droitHtps) {
      this.getDroitHtps().add(droitHtp);
    }
  }

  @Override
  public int compareTo(Contrat contrat) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(categorieSociale, contrat.categorieSociale);
    compareToBuilder.append(civilitePorteur, contrat.civilitePorteur);
    compareToBuilder.append(dateResiliation, contrat.dateResiliation);
    compareToBuilder.append(dateSouscription, contrat.dateSouscription);
    compareToBuilder.append(destinataire, contrat.destinataire);
    compareToBuilder.append(individuelOuCollectif, contrat.individuelOuCollectif);
    compareToBuilder.append(isContratCMU, contrat.isContratCMU);
    compareToBuilder.append(contratCMUC2S, contrat.contratCMUC2S);
    compareToBuilder.append(isContratResponsable, contrat.isContratResponsable);
    compareToBuilder.append(lienFamilial, contrat.lienFamilial);
    compareToBuilder.append(motifFinSituation, contrat.motifFinSituation);
    compareToBuilder.append(nomPorteur, contrat.nomPorteur);
    compareToBuilder.append(numero, contrat.numero);
    compareToBuilder.append(numeroAdherent, contrat.numeroAdherent);
    compareToBuilder.append(numeroAdherentComplet, contrat.numeroAdherentComplet);
    compareToBuilder.append(numeroContratCollectif, contrat.numeroContratCollectif);
    compareToBuilder.append(prenomPorteur, contrat.prenomPorteur);
    compareToBuilder.append(qualification, contrat.qualification);
    compareToBuilder.append(rangAdministratif, contrat.rangAdministratif);
    compareToBuilder.append(situationDebut, contrat.situationDebut);
    compareToBuilder.append(situationFin, contrat.situationFin);
    compareToBuilder.append(situationParticuliere, contrat.situationParticuliere);
    compareToBuilder.append(type, contrat.type);
    compareToBuilder.append(critereSecondaire, contrat.critereSecondaire);
    compareToBuilder.append(critereSecondaireDetaille, contrat.critereSecondaireDetaille);
    compareToBuilder.append(modePaiementPrestations, contrat.modePaiementPrestations);
    compareToBuilder.append(gestionnaire, contrat.gestionnaire);
    compareToBuilder.append(groupeAssures, contrat.groupeAssures);
    compareToBuilder.append(numeroCarte, contrat.numeroCarte);
    compareToBuilder.append(editeurCarte, contrat.editeurCarte);
    compareToBuilder.append(fondCarte, contrat.fondCarte);
    compareToBuilder.append(annexe1Carte, contrat.annexe1Carte);
    compareToBuilder.append(annexe2Carte, contrat.annexe2Carte);
    compareToBuilder.append(numAMCEchange, contrat.numAMCEchange);
    compareToBuilder.append(numOperateur, contrat.numOperateur);
    compareToBuilder.append(identifiantCollectivite, contrat.identifiantCollectivite);
    compareToBuilder.append(raisonSociale, contrat.raisonSociale);
    compareToBuilder.append(siret, contrat.siret);
    compareToBuilder.append(groupePopulation, contrat.groupePopulation);
    compareToBuilder.append(codeRenvoi, contrat.codeRenvoi);
    compareToBuilder.append(libelleCodeRenvoi, contrat.libelleCodeRenvoi);
    compareToBuilder.append(codeItelis, contrat.getCodeItelis());
    compareToBuilder.append(periodeSuspensions, contrat.periodeSuspensions);
    compareToBuilder.append(periodeCMUOuverts, contrat.periodeCMUOuverts);
    compareToBuilder.append(periodeResponsableOuverts, contrat.periodeResponsableOuverts);
    return compareToBuilder.toComparison();
  }
}
