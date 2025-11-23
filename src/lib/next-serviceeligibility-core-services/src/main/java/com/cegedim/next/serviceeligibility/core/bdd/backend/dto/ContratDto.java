package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.Date;
import java.util.List;
import lombok.Data;

/** Classe DTO de l'entite {@code Adresse}. */
@Data
public class ContratDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String numero;
  private Date dateSouscription;
  private Date dateResiliation;
  private String type;
  private String nomPorteur;
  private String prenomPorteur;
  private String civilitePorteur;
  private String numeroAdherent;
  private String numeroAdherentComplet;
  private String qualification;
  private String numeroContratCollectif;
  private String rangAdministratif;
  private Boolean isContratResponsable;
  private Boolean isContratCMU;
  private String destinataire;
  private String individuelOuCollectif;
  private Date situationDebut;
  private Date situationfin;
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
  private String numeroOperateur;
  private String numeroAMCEchanges;
  private String editeurCarte;
  private String annexe1Carte;
  private String annexe2Carte;
  private String fondCarte;
  private String identifiantCollectivite;
  private String raisonSociale;
  private String siret;
  private String groupePopulation;
  private String codeRenvoi;
  private String libelleCodeRenvoi;

  private String codeItelis;
  private List<PeriodeSuspensionDto> periodeSuspensions;
  private List<PeriodeCMUOuvertDto> periodeCMUOuverts;
  private List<PeriodeDto> periodeResponsableOuverts;
}
