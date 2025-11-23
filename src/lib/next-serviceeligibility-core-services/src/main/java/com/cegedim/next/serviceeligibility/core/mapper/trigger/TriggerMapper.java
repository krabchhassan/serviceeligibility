package com.cegedim.next.serviceeligibility.core.mapper.trigger;

import static java.util.stream.Collectors.*;

import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddService;
import com.cegedim.next.serviceeligibility.core.bobb.ContractElement;
import com.cegedim.next.serviceeligibility.core.bobb.ProductElementLight;
import com.cegedim.next.serviceeligibility.core.bobb.services.ContractElementService;
import com.cegedim.next.serviceeligibility.core.bobb.services.ProductElementService;
import com.cegedim.next.serviceeligibility.core.model.domain.*;
import com.cegedim.next.serviceeligibility.core.model.domain.Contrat;
import com.cegedim.next.serviceeligibility.core.model.domain.benef.CodePeriodeDeclaration;
import com.cegedim.next.serviceeligibility.core.model.domain.benef.NirDeclaration;
import com.cegedim.next.serviceeligibility.core.model.domain.benef.NirRattachementRODeclaration;
import com.cegedim.next.serviceeligibility.core.model.domain.benef.RattachementRODeclaration;
import com.cegedim.next.serviceeligibility.core.model.domain.benef.TeletransmissionDeclaration;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PeriodeCMUOuvert;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.PeriodeComparable;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ExtendedOffreProduits;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageDroitsCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.Produit;
import com.cegedim.next.serviceeligibility.core.model.domain.pw.TpOfflineRightsDetails;
import com.cegedim.next.serviceeligibility.core.model.domain.pw.TpOnlineRightsDetails;
import com.cegedim.next.serviceeligibility.core.model.domain.pw.Variable;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.*;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.ServicePrestationTriggerBenef;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiary;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.kafka.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.CarenceDroit;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodeContratCMUOuvert;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarantService;
import com.cegedim.next.serviceeligibility.core.services.pojo.DroitsTPExtended;
import com.cegedim.next.serviceeligibility.core.services.pojo.WaitingExtendedOffreProduits;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.TauxConstants;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.BeneficiaryToIgnoreException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.BobbNotFoundException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.TriggerException;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TriggerMapper {
  private final ContractElementService contractElementService;
  private final ProductElementService productElementService;
  private final ParametreBddService parametreBddService;

  private final DeclarantService declarantsService;

  public static final String SANS_RESEAU_SOIN = "Sans réseau de soin";

  @ContinueSpan(log = "mapContrat")
  public Contrat mapContrat(
      TriggeredBeneficiary triggeredBeneficiary,
      ParametrageDroitsCarteTP parametrageDroitsCarteTP,
      LocalDate dateJour,
      String dateDebutDroit,
      List<DroitsTPExtended> droitsTPExtendedList) {
    Contrat contrat = new Contrat();
    LocalDate localDateDebutDroit =
        LocalDate.parse(dateDebutDroit.replace("/", "-"), DateUtils.FORMATTER);
    ServicePrestationTriggerBenef newContract = triggeredBeneficiary.getNewContract();
    contrat.setNumero(triggeredBeneficiary.getNumeroContrat());
    if (StringUtils.isNotEmpty(newContract.getDateSouscription())) {
      contrat.setDateSouscription(newContract.getDateSouscription().replace("-", "/"));
    }
    if (StringUtils.isNotEmpty(newContract.getDateResiliation())) {
      contrat.setDateResiliation(newContract.getDateResiliation().replace("-", "/"));
    }
    contrat.setType("B");
    contrat.setNomPorteur(triggeredBeneficiary.getNomPorteur());
    contrat.setPrenomPorteur(triggeredBeneficiary.getPrenomPorteur());
    contrat.setCivilitePorteur(triggeredBeneficiary.getCivilitePorteur());
    contrat.setNumeroAdherent(triggeredBeneficiary.getNumeroAdherent());
    contrat.setNumeroAdherentComplet(triggeredBeneficiary.getNumeroAdherentComplet());
    String qualification = triggeredBeneficiary.getQualification();
    if (StringUtils.isNotBlank(qualification)) {
      contrat.setQualification(StringUtils.substring(qualification, 0, 1));
    }
    contrat.setNumeroContratCollectif(triggeredBeneficiary.getNumeroContratCollectif());
    contrat.setRangAdministratif(triggeredBeneficiary.getRangAdministratif());
    contrat.setIdentifiantCollectivite(triggeredBeneficiary.getIdentifiantCollectivite());
    contrat.setRaisonSociale(triggeredBeneficiary.getRaisonSociale());
    contrat.setGroupePopulation(triggeredBeneficiary.getGroupePopulation());
    contrat.setSiret(triggeredBeneficiary.getSiret());

    mapPeriodesContratResponsable(newContract.getPeriodesContratResponsable(), contrat, dateJour);

    mapPeriodesContratCMU(newContract.getPeriodesContratCMU(), contrat, dateJour);

    String subvalue = Boolean.TRUE.equals(triggeredBeneficiary.getIsBeneficiaireACS()) ? "2" : "1";
    contrat.setContratCMUC2S(Boolean.TRUE.equals(contrat.getIsContratCMU()) ? subvalue : "0");

    contrat.setIndividuelOuCollectif(
        Boolean.TRUE.equals(triggeredBeneficiary.getIsContratIndividuel()) ? "1" : "2");
    contrat.setTypeConvention(parametrageDroitsCarteTP.getCodeConventionTP());
    contrat.setCritereSecondaireDetaille(triggeredBeneficiary.getCritereSecondaireDetaille());
    contrat.setCritereSecondaire(triggeredBeneficiary.getCritereSecondaire());
    contrat.setNumeroExterneContratIndividuel(
        triggeredBeneficiary.getNumeroExterneContratIndividuel());
    contrat.setNumeroExterneContratCollectif(
        triggeredBeneficiary.getNumeroExterneContratCollectif());
    String modePaiementPrestations = triggeredBeneficiary.getModePaiementPrestations();
    if (StringUtils.isNotBlank(modePaiementPrestations)) {
      contrat.setModePaiementPrestations(StringUtils.substring(modePaiementPrestations, 0, 1));
    }
    contrat.setGestionnaire(triggeredBeneficiary.getGestionnaire());
    if (parametrageDroitsCarteTP.getRefFondCarte() != null) {
      contrat.setFondCarte(parametrageDroitsCarteTP.getRefFondCarte());
    } else {
      determineFondCarteForContrat(
          triggeredBeneficiary, droitsTPExtendedList, contrat, localDateDebutDroit);
    }
    contrat.setAnnexe1Carte(parametrageDroitsCarteTP.getCodeAnnexe1());
    contrat.setAnnexe2Carte(parametrageDroitsCarteTP.getCodeAnnexe2());
    contrat.setNumOperateur(parametrageDroitsCarteTP.getCodeOperateurTP());
    contrat.setOrdrePriorisation(triggeredBeneficiary.getOrdrePriorisation());
    contrat.setLienFamilial(triggeredBeneficiary.getQualite());
    contrat.setCodeItelis(parametrageDroitsCarteTP.getCodeItelis());

    String codeRenvoi = parametrageDroitsCarteTP.getCodeRenvoi();
    contrat.setCodeRenvoi(codeRenvoi);
    contrat.setLibelleCodeRenvoi(parametreBddService.findLibelleCodeRenvoi(codeRenvoi));
    for (DroitAssure droitAssure : triggeredBeneficiary.getNewContract().getDroitsGaranties()) {
      DroitHtp droitHtp = new DroitHtp();
      droitHtp.setCode(droitAssure.getCode());
      droitHtp.setCodeAssureur(droitAssure.getCodeAssureur());
      // changement de separateur !
      Periode periode = new Periode();
      periode.setDebut(droitAssure.getPeriode().getDebut().replace("-", "/"));
      if (droitAssure.getPeriode().getFin() != null) {
        periode.setFin(droitAssure.getPeriode().getFin().replace("-", "/"));
      }
      droitHtp.setPeriode(periode);
      contrat.getDroitHtps().add(droitHtp);
    }
    return contrat;
  }

  private void determineFondCarteForContrat(
      TriggeredBeneficiary triggeredBeneficiary,
      List<DroitsTPExtended> droitsTPExtendedList,
      Contrat contrat,
      LocalDate localDateDebutDroit) {
    if (aucunChangementReseauEnCoursAnnee(droitsTPExtendedList)) {
      Set<String> reseauxPW =
          droitsTPExtendedList.stream()
              .map(
                  droitsTPExtended -> {
                    TpOnlineRightsDetails tpOnlineRightsDetails =
                        droitsTPExtended.getDetailsOnline();
                    return tpOnlineRightsDetails != null
                        ? tpOnlineRightsDetails.getNetwork()
                        : null;
                  })
              .filter(Objects::nonNull)
              .collect(Collectors.toSet());

      Declarant declarant = declarantsService.findById(triggeredBeneficiary.getAmc());
      List<FondCarteTP> fondsCarteFromParametrageAMC = declarant.getFondCarteTP();

      if (CollectionUtils.isNotEmpty(fondsCarteFromParametrageAMC)) {
        Set<FondCarteTP> fondsCarteFiltres =
            fondsCarteFromParametrageAMC.stream()
                .filter(
                    fondCarte ->
                        reseauxPW.contains(fondCarte.getReseauSoin())
                            && DateUtils.betweenLocalDate(
                                localDateDebutDroit,
                                fondCarte.getDateDebut().toLocalDate(),
                                fondCarte.getDateFin() != null
                                    ? fondCarte.getDateFin().toLocalDate()
                                    : null))
                .collect(Collectors.toSet());

        if (fondsCarteFiltres.isEmpty()) {
          // Aucun fond de carte correspondant, recherche de "Sans réseau de soin"
          contrat.setFondCarte(
              fondsCarteFromParametrageAMC.stream()
                  .filter(
                      fondCarte ->
                          SANS_RESEAU_SOIN.equals(fondCarte.getReseauSoin())
                              && DateUtils.betweenLocalDate(
                                  localDateDebutDroit,
                                  fondCarte.getDateDebut().toLocalDate(),
                                  fondCarte.getDateFin() != null
                                      ? fondCarte.getDateFin().toLocalDate()
                                      : null))
                  .map(FondCarteTP::getFondCarte)
                  .findFirst()
                  .orElse(null));
        } else if (fondsCarteFiltres.size() > 1) {
          // Plusieurs fonds de carte : ne rien afficher s'ils sont différents
          String firstFondCarte = fondsCarteFiltres.iterator().next().getFondCarte();
          boolean allSameFondCarte =
              fondsCarteFiltres.stream()
                  .allMatch(fond -> fond.getFondCarte().equals(firstFondCarte));
          contrat.setFondCarte(allSameFondCarte ? firstFondCarte : null);
        } else {
          // Un seul fond de carte trouvé
          contrat.setFondCarte(fondsCarteFiltres.iterator().next().getFondCarte());
        }
      } else {
        contrat.setFondCarte(null);
      }
    }
  }

  public static boolean aucunChangementReseauEnCoursAnnee(
      List<DroitsTPExtended> droitsTPExtendedList) {
    if (droitsTPExtendedList.isEmpty()) {
      return true;
    }
    String debutActuel = droitsTPExtendedList.get(0).getDateDebut();
    String finActuel = droitsTPExtendedList.get(0).getDateFin();

    return droitsTPExtendedList.stream()
        .allMatch(
            droit ->
                droit.getDateDebut().equals(debutActuel) && droit.getDateFin().equals(finActuel));
  }

  private void mapPeriodesContratResponsable(
      List<Periode> periodesContratResponsable, Contrat contrat, LocalDate dateJour) {
    List<PeriodeComparable> periodes = new ArrayList<>();
    for (Periode periode : ListUtils.emptyIfNull(periodesContratResponsable)) {
      PeriodeComparable periodeComparable =
          new PeriodeComparable(periode.getDebut(), periode.getFin());
      periodes.add(periodeComparable);

      LocalDate debut = DateUtils.parse(periode.getDebut(), DateUtils.FORMATTER);
      LocalDate fin = DateUtils.parse(periode.getFin(), DateUtils.FORMATTER);
      if (DateUtils.betweenLocalDate(dateJour, debut, fin)) {
        contrat.setIsContratResponsable(true);
      }
    }
    contrat.setPeriodeResponsableOuverts(periodes);
  }

  private void mapPeriodesContratCMU(
      List<PeriodeContratCMUOuvert> periodesContratCMU, Contrat contrat, LocalDate dateJour) {
    List<PeriodeCMUOuvert> periodeCMUOuverts = new ArrayList<>();
    for (PeriodeContratCMUOuvert periodeContratCMUOuvert :
        ListUtils.emptyIfNull(periodesContratCMU)) {
      Periode periode = periodeContratCMUOuvert.getPeriode();
      PeriodeCMUOuvert periodeCMUOuvert = new PeriodeCMUOuvert();
      periodeCMUOuvert.setCode(periodeContratCMUOuvert.getCode());
      periodeCMUOuvert.setPeriode(new PeriodeComparable(periode.getDebut(), periode.getFin()));
      periodeCMUOuverts.add(periodeCMUOuvert);

      LocalDate debut = DateUtils.parse(periode.getDebut(), DateUtils.FORMATTER);
      LocalDate fin = DateUtils.parse(periode.getFin(), DateUtils.FORMATTER);
      if (DateUtils.betweenLocalDate(dateJour, debut, fin)) {
        contrat.setIsContratCMU(true);
      }
    }
    contrat.setPeriodeCMUOuverts(periodeCMUOuverts);
  }

  @ContinueSpan(log = "mapBeneficiaire")
  public BeneficiaireV2 mapBeneficiaire(
      TriggeredBeneficiary triggeredBeneficiary, String dateDebut, String dateGenerationDroits) {
    BeneficiaireV2 beneficiaire = new BeneficiaireV2();
    beneficiaire.setNumeroPersonne(triggeredBeneficiary.getNumeroPersonne());
    beneficiaire.setSouscripteurAlmv3(triggeredBeneficiary.isSouscripteurAlmv3());
    beneficiaire.setRefExternePersonne(triggeredBeneficiary.getRefExternePersonne());
    Affiliation affiliation = new Affiliation();
    affiliation.setNom(triggeredBeneficiary.getNom());
    affiliation.setNomPatronymique(triggeredBeneficiary.getNomPatronymique());
    affiliation.setNomMarital(triggeredBeneficiary.getNomMarital());
    affiliation.setPrenom(triggeredBeneficiary.getPrenom());
    affiliation.setCivilite(triggeredBeneficiary.getCivilite());
    if (StringUtils.isNotEmpty(triggeredBeneficiary.getPeriodeDebutAffiliation())) {
      affiliation.setPeriodeDebut(
          triggeredBeneficiary.getPeriodeDebutAffiliation().replace("-", "/"));
    }
    if (StringUtils.isNotEmpty(triggeredBeneficiary.getPeriodeFinAffiliation())) {
      affiliation.setPeriodeFin(triggeredBeneficiary.getPeriodeFinAffiliation().replace("-", "/"));
    }
    affiliation.setQualite(triggeredBeneficiary.getQualite());

    affiliation.setHasMedecinTraitant(triggeredBeneficiary.getHasMedecinTraitant());
    affiliation.setRegimeParticulier(triggeredBeneficiary.getCodeRegimeParticulier());
    affiliation.setIsBeneficiaireACS(triggeredBeneficiary.getIsBeneficiaireACS());
    affiliation.setIsTeleTransmission(triggeredBeneficiary.getIsTeleTransmission());

    affiliation.setTypeAssure(triggeredBeneficiary.getTypeAssure());
    beneficiaire.setDateNaissance(triggeredBeneficiary.getDateNaissance());
    beneficiaire.setRangNaissance(triggeredBeneficiary.getRangNaissance());
    if (StringUtils.isNotEmpty(triggeredBeneficiary.getNewContract().getDateRadiation())) {
      beneficiaire.setDateRadiation(
          triggeredBeneficiary.getNewContract().getDateRadiation().replace("-", "/"));
    }

    if (triggeredBeneficiary.getNir() != null) {
      beneficiaire.setNirBeneficiaire(triggeredBeneficiary.getNir());
      beneficiaire.setCleNirBeneficiaire(triggeredBeneficiary.getCleNir());
    }
    extractInfoNirBenef(
        beneficiaire, affiliation, triggeredBeneficiary.getAffiliationsRO(), dateDebut);

    mapAffiliationsRO(beneficiaire, triggeredBeneficiary.getAffiliationsRO());
    mapPeriodesMedecinTraitant(beneficiaire, triggeredBeneficiary.getPeriodesMedecinTraitant());
    mapTeletransmissions(beneficiaire, triggeredBeneficiary.getTeletransmissions());
    List<CodePeriodeDeclaration> regimes =
        mapCodePeriode(triggeredBeneficiary.getRegimesParticuliers());
    if (CollectionUtils.isNotEmpty(regimes)) {
      beneficiaire.setRegimesParticuliers(regimes);
    }
    List<CodePeriodeDeclaration> situations =
        mapCodePeriode(triggeredBeneficiary.getSituationsParticulieres());
    if (CollectionUtils.isNotEmpty(situations)) {
      beneficiaire.setSituationsParticulieres(situations);
    }

    if (StringUtils.isBlank(beneficiaire.getNirBeneficiaire())
        && StringUtils.isBlank(beneficiaire.getNirOd1())
        && StringUtils.isBlank(beneficiaire.getNirOd2())) {
      String error =
          "Impossible de déterminer un Nir pour le contrat "
              + triggeredBeneficiary.getNumeroContrat()
              + " pour l'assuré numero personne "
              + triggeredBeneficiary.getNumeroPersonne()
              + " pour l'AMC "
              + triggeredBeneficiary.getAmc()
              + ". ";
      throw new TriggerException(error);
    }

    beneficiaire.setAffiliation(affiliation);
    DestinataireRelevePrestations destinataireRelevePrestationsValid =
        getDestinataireRelevePrestationsValide(triggeredBeneficiary, dateGenerationDroits);
    if (destinataireRelevePrestationsValid != null) {
      beneficiaire.setAdresses(
          List.of(
              mapAdresse(
                  destinataireRelevePrestationsValid.getAdresse(),
                  triggeredBeneficiary.getContact())));
    }

    ServicePrestationTriggerBenef newContract = triggeredBeneficiary.getNewContract();

    if (StringUtils.isNotEmpty(newContract.getDateAdhesionMutuelle())) {
      beneficiaire.setDateAdhesionMutuelle(newContract.getDateAdhesionMutuelle().replace("-", "/"));
    }
    if (StringUtils.isNotEmpty(newContract.getDateDebutAdhesionIndividuelle())) {
      beneficiaire.setDateDebutAdhesionIndividuelle(
          newContract.getDateDebutAdhesionIndividuelle().replace("-", "/"));
    }
    beneficiaire.setNumeroAdhesionIndividuelle(newContract.getNumeroAdhesionIndividuelle());

    return beneficiaire;
  }

  private List<CodePeriodeDeclaration> mapCodePeriode(List<CodePeriode> regimesParticuliers) {
    List<CodePeriodeDeclaration> codePeriodeDeclarations = new ArrayList<>();
    for (CodePeriode codePeriode : ListUtils.emptyIfNull(regimesParticuliers)) {
      CodePeriodeDeclaration codePeriodeDeclaration = new CodePeriodeDeclaration();
      codePeriodeDeclaration.setCode(codePeriode.getCode());
      Periode periode = codePeriode.getPeriode();
      if (periode != null) {
        codePeriodeDeclaration.setPeriode(
            new PeriodeComparable(periode.getDebut(), periode.getFin()));
      }
      codePeriodeDeclarations.add(codePeriodeDeclaration);
    }
    return codePeriodeDeclarations;
  }

  private void mapTeletransmissions(
      BeneficiaireV2 beneficiaire, List<Teletransmission> teletransmissions) {
    List<TeletransmissionDeclaration> newTeletransmissions = new ArrayList<>();
    for (Teletransmission teletransmission : ListUtils.emptyIfNull(teletransmissions)) {
      TeletransmissionDeclaration newTele = new TeletransmissionDeclaration();
      Periode periode = teletransmission.getPeriode();
      if (periode != null) {
        newTele.setPeriode(new PeriodeComparable(periode.getDebut(), periode.getFin()));
      }
      newTele.setIsTeletransmission(teletransmission.getIsTeletransmission());

      newTeletransmissions.add(newTele);
    }

    if (!newTeletransmissions.isEmpty()) {
      beneficiaire.setTeletransmissions(newTeletransmissions);
    }
  }

  private void mapPeriodesMedecinTraitant(
      BeneficiaireV2 beneficiaire, List<Periode> periodesMedecinTraitant) {
    List<PeriodeComparable> newPeriodesMedecinTraitant = new ArrayList<>();
    for (Periode periode : ListUtils.emptyIfNull(periodesMedecinTraitant)) {
      PeriodeComparable newPeriodeComparable =
          new PeriodeComparable(periode.getDebut(), periode.getFin());
      newPeriodesMedecinTraitant.add(newPeriodeComparable);
    }

    if (!newPeriodesMedecinTraitant.isEmpty()) {
      beneficiaire.setPeriodesMedecinTraitant(newPeriodesMedecinTraitant);
    }
  }

  private void mapAffiliationsRO(
      BeneficiaireV2 beneficiaire, List<NirRattachementRO> affiliationsRO) {
    List<NirRattachementRODeclaration> newAffiliationsRO = new ArrayList<>();
    for (NirRattachementRO nirRattachementRO : ListUtils.emptyIfNull(affiliationsRO)) {
      NirRattachementRODeclaration newAffiliation = new NirRattachementRODeclaration();

      Nir nir = nirRattachementRO.getNir();
      if (nir != null) {
        newAffiliation.setNir(new NirDeclaration(nir.getCode(), nir.getCle()));
      }

      RattachementRO rattachementRO = nirRattachementRO.getRattachementRO();
      if (rattachementRO != null) {
        newAffiliation.setRattachementRO(
            new RattachementRODeclaration(
                rattachementRO.getCodeRegime(),
                rattachementRO.getCodeCaisse(),
                rattachementRO.getCodeCentre()));
      }

      Periode periode = nirRattachementRO.getPeriode();
      if (periode != null) {
        newAffiliation.setPeriode(new PeriodeComparable(periode.getDebut(), periode.getFin()));
      }

      newAffiliationsRO.add(newAffiliation);
    }

    if (!newAffiliationsRO.isEmpty()) {
      beneficiaire.setAffiliationsRO(newAffiliationsRO);
    }
  }

  private static DestinataireRelevePrestations getDestinataireRelevePrestationsValide(
      TriggeredBeneficiary triggeredBeneficiary, String dateGenerationDroits) {
    DestinataireRelevePrestations newestDestinataire = null;
    for (DestinataireRelevePrestations destinataireRelevePrestations :
        ListUtils.emptyIfNull(triggeredBeneficiary.getDestinatairesRelevePrestation())) {
      PeriodeDestinataire period = destinataireRelevePrestations.getPeriode();
      if (DateUtils.betweenString(dateGenerationDroits, period.getDebut(), period.getFin())) {
        // Recupere la premiere periode qui englobe la date de generation des droits
        return destinataireRelevePrestations;
      } else if (newestDestinataire == null
          || period.getDebut().compareTo(newestDestinataire.getPeriode().getDebut()) > 0) {
        // Soit si il n y a pas de periode valide a la date de generation des droits
        // alors on prend l adresse ayant une date de debut la plus recente
        newestDestinataire = destinataireRelevePrestations;
      }
    }
    return newestDestinataire;
  }

  private void extractInfoNirBenef(
      BeneficiaireV2 beneficiaire,
      Affiliation affiliation,
      List<NirRattachementRO> nirRattachementROList,
      String dateDebut) {
    beneficiaire.setNirOd1(beneficiaire.getNirBeneficiaire());
    beneficiaire.setCleNirOd1(beneficiaire.getCleNirBeneficiaire());
    if (CollectionUtils.isNotEmpty(nirRattachementROList)) {
      int compteur = 0;
      for (final NirRattachementRO affiliationRO : nirRattachementROList) {
        // pour l'instant, on contrôle uniquement que l'affiliation est valide au début
        // des droits générés
        if ((affiliationRO.getPeriode() != null
            && (affiliationRO.getPeriode().getFin() == null
                || !DateUtils.beforeAnyFormat(affiliationRO.getPeriode().getFin(), dateDebut))
            && (!DateUtils.beforeAnyFormat(dateDebut, affiliationRO.getPeriode().getDebut())))) {
          final Nir nir = affiliationRO.getNir();
          final RattachementRO rattRO = affiliationRO.getRattachementRO();
          if (compteur == 0) {
            beneficiaire.setNirOd1(nir.getCode());
            beneficiaire.setCleNirOd1(nir.getCle());
            if (rattRO != null) {
              affiliation.setRegimeOD1(rattRO.getCodeRegime());
              affiliation.setCaisseOD1(rattRO.getCodeCaisse());
              affiliation.setCentreOD1(rattRO.getCodeCentre());
            }
            compteur++;
          } else if (!beneficiaire.getNirOd1().equals(nir.getCode())) {
            beneficiaire.setNirOd2(nir.getCode());
            beneficiaire.setCleNirOd2(nir.getCle());
            if (rattRO != null) {
              affiliation.setRegimeOD2(rattRO.getCodeRegime());
              affiliation.setCaisseOD2(rattRO.getCodeCaisse());
              affiliation.setCentreOD2(rattRO.getCodeCentre());
            }
            break;
          }
        }
      }
    }
  }

  @ContinueSpan(log = "mapAdresse")
  public AdresseAvecFixe mapAdresse(AdresseAssure source, Contact contact) {
    AdresseAvecFixe adresse = new AdresseAvecFixe();
    if (source != null) {
      adresse.setCodePostal(source.getCodePostal());
      adresse.setLigne1(source.getLigne1());
      adresse.setLigne2(source.getLigne2());
      adresse.setLigne3(source.getLigne3());
      adresse.setLigne4(source.getLigne4());
      adresse.setLigne5(source.getLigne5());
      adresse.setLigne6(source.getLigne6());
      adresse.setLigne7(source.getLigne7());
      TypeAdresse typeAdresse = new TypeAdresse();
      typeAdresse.setType("AD");
      adresse.setTypeAdresse(typeAdresse);
    }
    if (contact != null) {
      adresse.setEmail(contact.getEmail());
      adresse.setTelephone(contact.getMobile());
      adresse.setFixe(contact.getFixe());
    }
    return adresse;
  }

  @ContinueSpan(log = "mapTaux")
  public void mapTaux(DomaineDroit d, TpOfflineRightsDetails details) {
    String taux = TauxConstants.T_100;
    String unite = TauxConstants.U_POURCENTAGE;
    if (CollectionUtils.isNotEmpty(details.getVariables())) {
      Variable variable = details.getVariables().get(0);
      taux = variable.getValue();
      switch (variable.getStsVariableNumber()) {
        case 1, 7:
          if (taux.contains(",") || taux.contains(".")) {
            unite = TauxConstants.U_NUMERIQUE;
          } else {
            unite = TauxConstants.U_FORFAIT;
          }
          break;
        default:
          unite = TauxConstants.U_POURCENTAGE;
          break;
      }
    } else if (TauxConstants.F_099.equals(details.getStsFormulaCode())) {
      unite = TauxConstants.U_TEXT;
      taux = TauxConstants.T_PRISE_EN_CHARGE;
    } else if (TauxConstants.F_FGL_NC.equals(details.getFormulaCode())) {
      unite = TauxConstants.U_TEXT;
      taux = TauxConstants.T_NON_COUVERT;
    }
    d.setTauxRemboursement(taux);
    d.setUniteTauxRemboursement(unite);
  }

  @ContinueSpan(log = "setPeriodDetails")
  public void setPeriodDetails(PeriodeDroit period, String dateDebut, String dateFin) {
    if (StringUtils.isNotEmpty(dateDebut)) {
      period.setPeriodeDebut(dateDebut.replace("-", "/"));
    }
    if (StringUtils.isNotEmpty(dateFin)) {
      period.setPeriodeFin(dateFin.replace("-", "/"));
    }

    // BLUE-4921 : Correction cosmétique de la date de fin si dateFin < dateDebut
    if (StringUtils.isNotEmpty(dateDebut)
        && StringUtils.isNotEmpty(dateFin)
        && DateUtils.before(dateFin, dateDebut)) {
      period.setPeriodeFin(DateUtils.dateMinusOneDay(dateDebut));
    }

    period.setModeObtention("M");
  }

  @ContinueSpan(log = "mapRightToOfferAndProduct")
  public List<WaitingExtendedOffreProduits> mapRightToOfferAndProduct(
      DroitAssure droit, Periode periodeForBobb) {
    List<WaitingExtendedOffreProduits> waitingExtendedOffreProduitsList = new ArrayList<>();
    List<ProductElementLight> productElementLightList =
        productElementService.getOfferAndProduct(
            droit.getCodeAssureur(),
            droit.getCode(),
            periodeForBobb.getDebut(),
            periodeForBobb.getFin());

    for (ProductElementLight productElementLight : productElementLightList) {
      if (StringUtils.isNotBlank(productElementLight.getCodeOffer())) {

        WaitingExtendedOffreProduits waitingExtendedOffreProduits =
            new WaitingExtendedOffreProduits();
        waitingExtendedOffreProduits.setWaitingParameterError(null);

        waitingExtendedOffreProduits.setOffersAndProducts(
            mapExtendedOffreProduit(
                droit.getCode(),
                droit.getLibelle(),
                productElementLight,
                periodeForBobb.getDebut(),
                periodeForBobb.getFin()));
        waitingExtendedOffreProduitsList.add(waitingExtendedOffreProduits);
      }
    }

    return waitingExtendedOffreProduitsList;
  }

  private static ExtendedOffreProduits mapExtendedOffreProduit(
      String codeDroitServicePrestation,
      String libelleDroitServicePrestation,
      ProductElementLight productElementLight,
      String periodeDebutDroit,
      String periodeFinDroit) {
    ExtendedOffreProduits extendedOffreProduits = new ExtendedOffreProduits();
    extendedOffreProduits.setCode(productElementLight.getCodeOffer());
    Produit product = new Produit();
    product.setCode(productElementLight.getCodeProduct());
    product.setCodeAmc(productElementLight.getCodeAmc());
    product.setPeriodeDebutDroit(periodeDebutDroit);
    product.setPeriodeFinDroit(periodeFinDroit);
    product.setCodeGarantieServicePrestation(codeDroitServicePrestation);
    product.setLibelleGarantieServicePrestation(libelleDroitServicePrestation);
    List<Produit> products = new ArrayList<>();
    products.add(product);
    extendedOffreProduits.setProduits(products);
    return extendedOffreProduits;
  }

  @ContinueSpan(log = "mapReplacementRightToOfferAndProduct")
  public List<ExtendedOffreProduits> mapReplacementRightToOfferAndProduct(
      DroitRemplacement droit, String periodeDebutDroit, String periodeFinDroit) {
    List<ProductElementLight> productElementLightList =
        productElementService.getOfferAndProduct(
            droit.getCodeAssureur(), droit.getCode(), periodeDebutDroit, periodeFinDroit);

    List<ExtendedOffreProduits> extendedOffreProduitsList = new ArrayList<>();

    for (ProductElementLight productElementLight : productElementLightList) {
      if (productElementLight != null
          && StringUtils.isNotBlank(productElementLight.getCodeOffer())) {
        extendedOffreProduitsList.add(
            mapExtendedOffreProduit(
                droit.getCode(),
                droit.getLibelle(),
                productElementLight,
                periodeDebutDroit,
                periodeFinDroit));
      }
    }

    return extendedOffreProduitsList;
  }

  @ContinueSpan(log = "getExtendedOffreProduits")
  public List<ExtendedOffreProduits> getExtendedOffreProduits(TriggeredBeneficiary benef) {
    List<ExtendedOffreProduits> extendedOffreProduits = new ArrayList<>();

    if (benef.getNewContract() != null) {
      // retourne la liste des ExtendedOffreProduits en regroupant les produits par
      // code d'offre
      extendedOffreProduits =
          getOffers(benef.getNewContract()).stream()
              .map(WaitingExtendedOffreProduits::getOffersAndProducts)
              .collect(
                  groupingBy(
                      ExtendedOffreProduits::getCode,
                      mapping(
                          ExtendedOffreProduits::getProduits,
                          collectingAndThen(
                              Collectors.toList(),
                              list -> list.stream().flatMap(List::stream).toList()))))
              .entrySet()
              .stream()
              .map(
                  stringListEntry -> {
                    ExtendedOffreProduits e = new ExtendedOffreProduits();
                    e.setCode(stringListEntry.getKey());
                    e.setProduits(stringListEntry.getValue());
                    return e;
                  })
              .toList();
    }

    return extendedOffreProduits;
  }

  @ContinueSpan(log = "getOffers")
  public List<WaitingExtendedOffreProduits> getOffers(ServicePrestationTriggerBenef newContract) {
    List<DroitAssure> droits = newContract.getDroitsGaranties();
    List<WaitingExtendedOffreProduits> offersAndProducts = new ArrayList<>();

    if (!CollectionUtils.isEmpty(droits)) {

      for (DroitAssure droit : droits) {
        List<Periode> periodesBobb =
            getPeriodesBobb(
                newContract.getDateSouscription(),
                newContract.getDateResiliation(),
                newContract.getDateRadiation(),
                droit);
        for (Periode periodeForBobb : periodesBobb) {
          offersAndProducts.addAll(addOfferAndProduct(droit, periodeForBobb));
        }
      }
    }

    return offersAndProducts;
  }

  @ContinueSpan(log = "getOffersAndThrowException")
  public List<WaitingExtendedOffreProduits> getOffersAndThrowException(
      ServicePrestationTriggerBenef newContract)
      throws BeneficiaryToIgnoreException, BobbNotFoundException {
    List<DroitAssure> droits = newContract.getDroitsGaranties();
    List<WaitingExtendedOffreProduits> offersAndProducts = new ArrayList<>();

    if (!CollectionUtils.isEmpty(droits)) {
      boolean atLeastOneIgnored = false;

      for (DroitAssure droit : droits) {
        if (DateUtils.compareDate(droit.getPeriode().getDebut(), droit.getPeriode().getFin()) > 0) {
          atLeastOneIgnored = true;
        } else {
          List<Periode> periodesBobb =
              getPeriodesBobb(
                  newContract.getDateSouscription(),
                  newContract.getDateResiliation(),
                  newContract.getDateRadiation(),
                  droit);
          for (Periode periodeForBobb : periodesBobb) {
            List<WaitingExtendedOffreProduits> offreProduitsList =
                addOfferAndProduct(droit, periodeForBobb);
            atLeastOneIgnored =
                verifyPeriodCovered(droit, periodeForBobb, offreProduitsList, atLeastOneIgnored);
            offersAndProducts.addAll(offreProduitsList);
          }
        }
      }

      if (offersAndProducts.isEmpty() && atLeastOneIgnored) {
        // Si la liste est vide et qu on a pas eu de BOBB_NO_PRODUCT_FOUND alors on peut
        // ne pas traiter le benef
        throw new BeneficiaryToIgnoreException();
      }
    }

    return offersAndProducts;
  }

  private boolean verifyPeriodCovered(
      DroitAssure droit,
      Periode periodeForBobb,
      List<WaitingExtendedOffreProduits> offreProduitsList,
      boolean atLeastOneIgnored)
      throws BobbNotFoundException {
    if (CollectionUtils.isEmpty(offreProduitsList)) {
      if (!checkContractElementIgnored(droit)) {
        TriggeredBeneficiaryAnomaly triggeredBeneficiaryAnomaly =
            TriggeredBeneficiaryAnomaly.create(
                Anomaly.BOBB_NO_PRODUCT_FOUND, droit.getCode(), periodeForBobb.getDebut());
        log.debug(triggeredBeneficiaryAnomaly.getDescription());
        throw new BobbNotFoundException(triggeredBeneficiaryAnomaly);
      } else {
        atLeastOneIgnored = true;
      }
    }
    return atLeastOneIgnored;
  }

  private boolean checkContractElementIgnored(DroitAssure droit) {
    ContractElement elem =
        contractElementService.get(droit.getCode(), droit.getCodeAssureur(), true);
    return elem != null && elem.isIgnored();
  }

  public List<WaitingExtendedOffreProduits> addOfferAndProduct(
      DroitAssure droit, Periode periodeForBobb) {
    // Base right
    List<WaitingExtendedOffreProduits> offreProduitsRights =
        mapRightToOfferAndProduct(droit, periodeForBobb);

    // Replacement rights
    List<CarenceDroit> carences = droit.getCarences();

    if (CollectionUtils.isNotEmpty(carences)) {
      for (CarenceDroit carence : carences) {

        DroitRemplacement droitRemplacement = carence.getDroitRemplacement();

        if (droitRemplacement != null) {

          List<ExtendedOffreProduits> extendedOffreProduitsList =
              mapReplacementRightToOfferAndProduct(
                  droitRemplacement,
                  carence.getPeriode().getDebut(),
                  carence.getPeriode().getFin());

          for (ExtendedOffreProduits extendedOffreProduits : extendedOffreProduitsList) {
            extendedOffreProduits.setCodeCarence(carence.getCode());
            extendedOffreProduits.setCodeOrigine(droit.getCode());
            extendedOffreProduits.setCodeAssureurOrigine(droit.getCodeAssureur());
            WaitingExtendedOffreProduits offreProduitsReplacementRight =
                new WaitingExtendedOffreProduits();

            offreProduitsReplacementRight.setWaitingParameterError(null);
            offreProduitsReplacementRight.setOffersAndProducts(extendedOffreProduits);

            offreProduitsRights.add(offreProduitsReplacementRight);
          }
        }
      }
    }

    return offreProduitsRights;
  }

  private String getMaxDateDebutForBOBB(String dateSouscription, DroitAssure droitAssure) {
    List<String> datesDebut = new ArrayList<>();
    datesDebut.add(dateSouscription);
    if (droitAssure.getPeriode() != null) {
      datesDebut.add(droitAssure.getPeriode().getDebut());
    }
    return DateUtils.getMaxDateOrNull(datesDebut.stream().filter(Objects::nonNull).toList());
  }

  private String getMinDateFinForBOBB(
      String dateResiliation, String dateRadiation, DroitAssure droitAssure) {
    List<String> datesFin = new ArrayList<>();
    if (dateResiliation != null) {
      datesFin.add(dateResiliation);
    }
    if (dateRadiation != null) {
      datesFin.add(dateRadiation);
    }
    if (droitAssure.getPeriode() != null && droitAssure.getPeriode().getFin() != null) {
      datesFin.add(droitAssure.getPeriode().getFin());
    }
    return DateUtils.getMinDateOrNull(datesFin);
  }

  public List<Periode> getPeriodesBobb(
      String dateSouscription,
      String dateResiliation,
      String dateRadiation,
      DroitAssure droitAssure) {
    List<Periode> periodesBobb = new ArrayList<>();
    String debutDroit = getMaxDateDebutForBOBB(dateSouscription, droitAssure);
    String finDroit = getMinDateFinForBOBB(dateResiliation, dateRadiation, droitAssure);

    periodesBobb.add(new Periode(debutDroit, finDroit));

    return periodesBobb;
  }
}
