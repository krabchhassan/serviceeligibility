package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails.CodePeriodeContractDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails.PeriodeContractDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** Classe DTO de l'entite {@code Contrat}. */
public class ContratDto implements GenericDto {

  /** */
  private static final long serialVersionUID = 6281115429150619570L;

  private String numero;

  private String dateSouscription;

  private String typeAssure;

  private String nomPorteur;

  private String prenomPorteur;

  private String civilitePorteur;

  private String numeroAdherent;

  private String qualification;

  private String numeroContratCollectif;

  private String rangAdministratif;

  private Boolean isContratResponsable;

  private Boolean isContratCMU;

  private String contratCMUC2S;

  private String destinataire;

  private String individuelOuCollectif;

  private String situationDebut;

  private String situationFin;

  private String motifFinSituation;

  private String lienFamilial;

  private String categorieSociale;

  private String situationParticuliere;

  private String numeroExterneContratIndividuel;

  private String numeroExterneContratCollectif;

  private String typeConvention;

  private String critereSecondaireDetaille;

  private String critereSecondaire;

  private String modePaiementPrestations;

  private String gestionnaire;

  private String groupeAssures;

  private String numOperateur;

  private String numAMCEchange;

  private String numeroAdherentComplet;

  private Boolean isBeneficiaireACS = false;

  private Boolean isTeleTransmission;

  private List<CommunicationDto> communications;

  @Getter @Setter private String codeRenvoi;
  @Getter @Setter private String libelleCodeRenvoi;
  @Getter @Setter private List<CodePeriodeContractDto> periodeCMUOuverts;
  @Getter @Setter private List<PeriodeContractDto> periodeResponsableOuverts;

  public String getNumero() {
    return numero;
  }

  public void setNumero(final String numero) {
    this.numero = numero;
  }

  public String getDateSouscription() {
    return dateSouscription;
  }

  public void setDateSouscription(final String dateSouscription) {
    this.dateSouscription = dateSouscription;
  }

  public String getTypeAssure() {
    return typeAssure;
  }

  public void setTypeAssure(final String typeAssure) {
    this.typeAssure = typeAssure;
  }

  public String getContratCMUC2S() {
    return contratCMUC2S;
  }

  public String getNomPorteur() {
    return nomPorteur;
  }

  public void setNomPorteur(final String nomPorteur) {
    this.nomPorteur = nomPorteur;
  }

  public String getPrenomPorteur() {
    return prenomPorteur;
  }

  public void setPrenomPorteur(final String prenomPorteur) {
    this.prenomPorteur = prenomPorteur;
  }

  public String getCivilitePorteur() {
    return civilitePorteur;
  }

  public void setCivilitePorteur(final String civilitePorteur) {
    this.civilitePorteur = civilitePorteur;
  }

  public String getNumeroAdherent() {
    return numeroAdherent;
  }

  public void setNumeroAdherent(final String numeroAdherent) {
    this.numeroAdherent = numeroAdherent;
  }

  public String getQualification() {
    return qualification;
  }

  public void setQualification(final String qualification) {
    this.qualification = qualification;
  }

  public String getNumeroContratCollectif() {
    return numeroContratCollectif;
  }

  public void setNumeroContratCollectif(final String numeroContratCollectif) {
    this.numeroContratCollectif = numeroContratCollectif;
  }

  public String getRangAdministratif() {
    return rangAdministratif;
  }

  public void setRangAdministratif(final String rangAdministratif) {
    this.rangAdministratif = rangAdministratif;
  }

  public Boolean getIsContratResponsable() {
    return isContratResponsable;
  }

  public void setIsContratResponsable(final Boolean isContratResponsable) {
    this.isContratResponsable = isContratResponsable;
  }

  public void setContratCMUC2S(String contratCMUC2S) {
    this.contratCMUC2S = contratCMUC2S;
  }

  public Boolean getIsContratCMU() {
    return isContratCMU;
  }

  public void setIsContratCMU(final Boolean isContratCMU) {
    this.isContratCMU = isContratCMU;
  }

  public String getDestinataire() {
    return destinataire;
  }

  public void setDestinataire(final String destinataire) {
    this.destinataire = destinataire;
  }

  public String getIndividuelOuCollectif() {
    return individuelOuCollectif;
  }

  public void setIndividuelOuCollectif(final String individuelOuCollectif) {
    this.individuelOuCollectif = individuelOuCollectif;
  }

  public String getSituationDebut() {
    return situationDebut;
  }

  public void setSituationDebut(final String situationDebut) {
    this.situationDebut = situationDebut;
  }

  public String getSituationFin() {
    return situationFin;
  }

  public void setSituationFin(final String situationFin) {
    this.situationFin = situationFin;
  }

  public String getMotifFinSituation() {
    return motifFinSituation;
  }

  public void setMotifFinSituation(final String motifFinSituation) {
    this.motifFinSituation = motifFinSituation;
  }

  public String getLienFamilial() {
    return lienFamilial;
  }

  public void setLienFamilial(final String lienFamilial) {
    this.lienFamilial = lienFamilial;
  }

  public String getCategorieSociale() {
    return categorieSociale;
  }

  public void setCategorieSociale(final String categorieSociale) {
    this.categorieSociale = categorieSociale;
  }

  public String getSituationParticuliere() {
    return situationParticuliere;
  }

  public void setSituationParticuliere(final String situationParticuliere) {
    this.situationParticuliere = situationParticuliere;
  }

  public String getNumeroExterneContratIndividuel() {
    return numeroExterneContratIndividuel;
  }

  public void setNumeroExterneContratIndividuel(String numeroExterneContratIndividuel) {
    this.numeroExterneContratIndividuel = numeroExterneContratIndividuel;
  }

  public String getNumeroExterneContratCollectif() {
    return numeroExterneContratCollectif;
  }

  public void setNumeroExterneContratCollectif(String numeroExterneContratCollectif) {
    this.numeroExterneContratCollectif = numeroExterneContratCollectif;
  }

  public String getTypeConvention() {
    return typeConvention;
  }

  public void setTypeConvention(String typeConvention) {
    this.typeConvention = typeConvention;
  }

  public String getCritereSecondaireDetaille() {
    return critereSecondaireDetaille;
  }

  public void setCritereSecondaireDetaille(String critereSecondaireDetaille) {
    this.critereSecondaireDetaille = critereSecondaireDetaille;
  }

  public String getCritereSecondaire() {
    return critereSecondaire;
  }

  public void setCritereSecondaire(String critereSecondaire) {
    this.critereSecondaire = critereSecondaire;
  }

  public String getModePaiementPrestations() {
    return modePaiementPrestations;
  }

  public void setModePaiementPrestations(String modePaiementPrestations) {
    this.modePaiementPrestations = modePaiementPrestations;
  }

  public String getGestionnaire() {
    return gestionnaire;
  }

  public void setGestionnaire(String gestionnaire) {
    this.gestionnaire = gestionnaire;
  }

  public String getGroupeAssures() {
    return groupeAssures;
  }

  public void setGroupeAssures(String groupeAssures) {
    this.groupeAssures = groupeAssures;
  }

  public String getNumOperateur() {
    return numOperateur;
  }

  public void setNumOperateur(String numOperateur) {
    this.numOperateur = numOperateur;
  }

  public String getNumAMCEchange() {
    return numAMCEchange;
  }

  public void setNumAMCEchange(String numAMCEchange) {
    this.numAMCEchange = numAMCEchange;
  }

  public String getNumeroAdherentComplet() {
    return numeroAdherentComplet;
  }

  public void setNumeroAdherentComplet(String numeroAdherentComplet) {
    this.numeroAdherentComplet = numeroAdherentComplet;
  }

  public Boolean getIsBeneficiaireACS() {
    return isBeneficiaireACS;
  }

  public void setIsBeneficiaireACS(Boolean isBeneficiaireACS) {
    this.isBeneficiaireACS = isBeneficiaireACS;
  }

  public Boolean getIsTeleTransmission() {
    return isTeleTransmission;
  }

  public void setIsTeleTransmission(Boolean isTeleTransmission) {
    this.isTeleTransmission = isTeleTransmission;
  }

  public List<CommunicationDto> getCommunications() {
    return communications;
  }

  public void setCommunications(List<CommunicationDto> adresseDto) {
    this.communications = adresseDto;
  }
}
