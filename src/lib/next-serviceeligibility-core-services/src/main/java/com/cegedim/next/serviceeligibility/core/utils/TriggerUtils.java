package com.cegedim.next.serviceeligibility.core.utils;

import com.cegedim.next.serviceeligibility.core.bobb.GarantieTechnique;
import com.cegedim.next.serviceeligibility.core.bobb.Lot;
import com.cegedim.next.serviceeligibility.core.model.domain.carence.ParametrageCarence;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageDroitsCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.*;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.enumeration.DateRenouvellementCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ModeDeclenchementCarteTP;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodeSuspension;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodesDroitsCarte;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContexteTPV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratCollectifV6;
import com.cegedim.next.serviceeligibility.core.services.pojo.ParametrageTrigger;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.TriggerException;
import jakarta.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class TriggerUtils {

  private TriggerUtils() {
    // Utils
  }

  public static String getLog(final ParametrageCarteTP param) {
    if (param == null) {
      return "";
    }

    final List<String[]> logArrayList = new ArrayList<>();
    logArrayList.add(new String[] {"AMC %s", param.getAmcNom()});
    if (StringUtils.isNotBlank(param.getGroupePopulation())) {
      logArrayList.add(new String[] {"Groupe de population %s", param.getGroupePopulation()});
    }
    if (StringUtils.isNotBlank(param.getIdentifiantCollectivite())) {
      logArrayList.add(
          new String[] {"Identifiant de collectivité %s", param.getIdentifiantCollectivite()});
    }
    if (StringUtils.isNotBlank(param.getCritereSecondaireDetaille())) {
      logArrayList.add(
          new String[] {"Critère secondaire détaillé %s", param.getCritereSecondaireDetaille()});
    }

    final List<GarantieTechnique> gts = param.getGarantieTechniques();
    final String gtsString;
    if (CollectionUtils.isNotEmpty(gts)) {
      gtsString =
          gts.stream()
              .map(
                  garantieTechnique ->
                      garantieTechnique.getCodeAssureur()
                          + "."
                          + garantieTechnique.getCodeGarantie())
              .collect(Collectors.joining("-"));
      logArrayList.add(new String[] {"Garanties technique %s", gtsString});
    }

    final List<String> idLots = param.getIdLots();
    final String idLotsString;
    if (CollectionUtils.isNotEmpty(idLots)) {
      idLotsString = String.join("-", idLots);
      logArrayList.add(new String[] {"Id lots de GT %s", idLotsString});
    }

    final List<String[]> filteredLogArrayList =
        logArrayList.stream().filter(Objects::nonNull).toList();

    final List<String> logList = new ArrayList<>();
    for (final String[] logArray : filteredLogArrayList) {
      final String log = String.format(logArray[0], logArray[1]);
      logList.add(log);
    }

    return String.join(",", logList);
  }

  public static boolean isGarantieInsideLot(
      Lot lot, GarantieTechnique garantieTechniqueFromContract) {
    if (lot == null) {
      return false;
    }
    return lot.getGarantieTechniques().stream()
        .filter(
            garantieTechnique -> StringUtils.isBlank(garantieTechnique.getDateSuppressionLogique()))
        .anyMatch(garantieTechnique -> garantieTechnique.equals(garantieTechniqueFromContract));
  }

  public static List<TriggeredBeneficiary> extractBenefsFromContracts(
      final List<ContratAIV6> contrats,
      final boolean isEndpointDelete,
      final boolean updateNewContract) {
    final List<TriggeredBeneficiary> benefs = new ArrayList<>();
    for (final ContratAIV6 contrat : contrats) {
      extractBenefsFromContract(benefs, contrat, isEndpointDelete, updateNewContract);
    }
    return benefs;
  }

  // méthode pour rajouter les anciens benefs qui ne sont pas dans le nouveau
  // contrat.
  public static List<TriggeredBeneficiary> extractBenefsUniquesFromContracts(
      final List<TriggeredBeneficiary> newBenefList,
      final List<TriggeredBeneficiary> oldBenefList) {
    final List<TriggeredBeneficiary> benefs = new ArrayList<>(newBenefList);

    for (final TriggeredBeneficiary oldBenef : oldBenefList) {
      boolean found = false;
      for (final TriggeredBeneficiary newBenef : newBenefList) {
        if (newBenef.getNumeroPersonne().equals(oldBenef.getNumeroPersonne())
            && newBenef.getNumeroContrat().equals(oldBenef.getNumeroContrat())
            && newBenef.getNumeroAdherent().equals(oldBenef.getNumeroAdherent())
            && newBenef.getRangAdministratif().equals(oldBenef.getRangAdministratif())) {
          found = true;
          newBenef.setOldContract(oldBenef.getOldContract());
          break;
        }
      }
      if (!found) {
        benefs.add(oldBenef);
      }
    }
    return benefs;
  }

  public static void extractBenefsFromContract(
      final List<TriggeredBeneficiary> benefs,
      final ContratAIV6 contrat,
      final boolean isEndpointDelete,
      final boolean updateNewContract) {
    // Recherche du porteur du contrat
    // Les contrats ont tous au moins un assuré
    Assure assureSouscripteur = getAssureSouscripteur(contrat);
    for (final Assure assure : contrat.getAssures()) {
      final TriggeredBeneficiary trgBenef = new TriggeredBeneficiary();
      if (isEndpointDelete) {
        trgBenef.setParametreAction(Constants.DELETE_ENDPOINT);
      }
      trgBenef.setServicePrestationId(contrat.getId());

      extractInfoAssure(contrat, assure, assureSouscripteur, trgBenef);
      addStatus(trgBenef, TriggeredBeneficiaryStatusEnum.ToProcess, null, true);

      final ServicePrestationTriggerBenef contractBenef = mapContrat(assure, contrat);

      if (updateNewContract) {
        trgBenef.setNewContract(contractBenef);
      } else {
        trgBenef.setOldContract(contractBenef);
      }

      benefs.add(trgBenef);
    }
  }

  public static Assure getAssureSouscripteur(ContratAIV6 contrat) {
    Assure assureSouscripteur = null;
    if (CollectionUtils.isNotEmpty(contrat.getAssures())) {
      for (final Assure assure : contrat.getAssures()) {
        if (Boolean.TRUE.equals(assure.getIsSouscripteur())) {
          assureSouscripteur = assure;
          break;
        }
      }
      // Si on n'a pas trouvé de souscripteur, on recherche par rang administratif
      if (assureSouscripteur == null) {
        final List<Assure> assureContratsList = contrat.getAssures();
        assureSouscripteur =
            assureContratsList.stream()
                .max(Comparator.comparing(Assure::getRangAdministratif))
                .orElse(null);
      }
      return assureSouscripteur;
    }
    return null;
  }

  public static ServicePrestationTriggerBenef mapContrat(
      final Assure assure, final ContratAIV6 contrat) {
    final ServicePrestationTriggerBenef contratTriggerBenef = new ServicePrestationTriggerBenef();

    contratTriggerBenef.setDateAdhesionMutuelle(assure.getDateAdhesionMutuelle());
    contratTriggerBenef.setDateDebutAdhesionIndividuelle(assure.getDateDebutAdhesionIndividuelle());
    contratTriggerBenef.setNumeroAdhesionIndividuelle(assure.getNumeroAdhesionIndividuelle());

    if (contrat.getContexteTiersPayant() != null) {
      contratTriggerBenef.setPeriodesDroitsCarte(
          contrat.getContexteTiersPayant().getPeriodesDroitsCarte());
      contratTriggerBenef.setDateRestitution(
          contrat.getContexteTiersPayant().getDateRestitutionCarte());
    }

    contratTriggerBenef.setPeriodesContratResponsable(
        contrat.getPeriodesContratResponsableOuvert());
    contratTriggerBenef.setPeriodesContratCMU(contrat.getPeriodesContratCMUOuvert());
    contratTriggerBenef.setDateSouscription(contrat.getDateSouscription());
    contratTriggerBenef.setDateResiliation(contrat.getDateResiliation());
    contratTriggerBenef.setDateRadiation(assure.getDateRadiation());
    contratTriggerBenef.setDroitsGaranties(assure.getDroits());

    // Mapping et tri des périodes de suspension
    final List<PeriodeSuspension> periodesSuspension = contrat.getPeriodesSuspension();
    if (CollectionUtils.isNotEmpty(periodesSuspension)) {
      periodesSuspension.sort(
          Comparator.comparing(periodeSuspension -> periodeSuspension.getPeriode().getDebut()));
      contratTriggerBenef.setPeriodesSuspension(periodesSuspension);
    }

    return contratTriggerBenef;
  }

  public static void extractInfoAssure(
      final ContratAIV6 contrat,
      final Assure assure,
      final Assure assureSouscripteur,
      final TriggeredBeneficiary trgBenef) {
    IdentiteContrat identite = assure.getIdentite();
    if (identite.getNir() != null) {
      trgBenef.setNir(identite.getNir().getCode());
      trgBenef.setCleNir(identite.getNir().getCle());
    }
    trgBenef.setAffiliationsRO(identite.getAffiliationsRO());

    if (assureSouscripteur != null) {
      if (assureSouscripteur.getData().getNom().getNomUsage() != null) {
        trgBenef.setNomPorteur(assureSouscripteur.getData().getNom().getNomUsage());
      } else {
        trgBenef.setNomPorteur(assureSouscripteur.getData().getNom().getNomFamille());
      }
      trgBenef.setPrenomPorteur(assureSouscripteur.getData().getNom().getPrenom());
      trgBenef.setCivilitePorteur(assureSouscripteur.getData().getNom().getCivilite());
    }
    if (Boolean.TRUE.equals(assure.getIsSouscripteur())) {
      trgBenef.setSouscripteurAlmv3(true);
    }
    trgBenef.setQualification(contrat.getQualification());
    trgBenef.setCritereSecondaire(contrat.getCritereSecondaire());
    trgBenef.setNumeroExterneContratIndividuel(contrat.getNumeroExterne());
    trgBenef.setGestionnaire(contrat.getSocieteEmettrice());

    trgBenef.setNumeroContrat(contrat.getNumero());
    trgBenef.setIsContratIndividuel(contrat.getIsContratIndividuel());
    trgBenef.setNumeroAdherent(contrat.getNumeroAdherent());
    trgBenef.setNumeroAdherentComplet(contrat.getNumeroAdherentComplet());
    trgBenef.setDateNaissance(identite.getDateNaissance());
    trgBenef.setRangNaissance(identite.getRangNaissance());

    trgBenef.setAmc(contrat.getIdDeclarant());

    extractContextTiersPayant(contrat, trgBenef);
    extractContratCollectif(contrat, trgBenef);
    trgBenef.setCritereSecondaireDetaille(contrat.getCritereSecondaireDetaille());
    trgBenef.setNumeroPersonne(identite.getNumeroPersonne());
    trgBenef.setRefExternePersonne(identite.getRefExternePersonne());
    // Nom de jeune fille = nom de famille
    final String nomFamille = assure.getData().getNom().getNomFamille();
    final String nomUsage =
        Objects.requireNonNullElse(assure.getData().getNom().getNomUsage(), nomFamille);
    trgBenef.setNomPatronymique(nomFamille);
    trgBenef.setNom(nomUsage);
    trgBenef.setNomMarital(nomUsage);
    trgBenef.setPrenom(assure.getData().getNom().getPrenom());
    trgBenef.setCivilite(assure.getData().getNom().getCivilite());
    trgBenef.setDestinatairesRelevePrestation(assure.getData().getDestinatairesRelevePrestations());
    trgBenef.setPeriodeDebutAffiliation(assure.getDateAdhesionMutuelle());

    if (StringUtils.isNotBlank(contrat.getDateResiliation())
        && StringUtils.isNotBlank(assure.getDateRadiation())) {
      final LocalDate dateResiliation = LocalDate.parse(contrat.getDateResiliation());
      final LocalDate dateRadiation = LocalDate.parse(assure.getDateRadiation());

      final Optional<LocalDate> minDateOptional =
          Stream.of(dateResiliation, dateRadiation).min(LocalDate::compareTo);

      minDateOptional.ifPresent(
          minDate ->
              trgBenef.setPeriodeFinAffiliation(
                  minDate.format(DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD))));
    } else if (StringUtils.isNotBlank(contrat.getDateResiliation())) {
      trgBenef.setPeriodeFinAffiliation(contrat.getDateResiliation());
    } else if (StringUtils.isNotBlank(assure.getDateRadiation())) {
      trgBenef.setPeriodeFinAffiliation(assure.getDateRadiation());
    }

    if (assure.getQualite() != null) {
      trgBenef.setQualite(assure.getQualite().getCode());
      trgBenef.setTypeAssure(assure.getQualite().getLibelle());
    }

    final String dateDuJour = new SimpleDateFormat(DateUtils.YYYY_MM_DD).format(new Date());
    extractMedecinTraitantBenef(assure, trgBenef, dateDuJour);
    final List<CodePeriode> regimesParticuliers = assure.getRegimesParticuliers();
    if (!CollectionUtils.isEmpty(regimesParticuliers)) {
      trgBenef.setCodeRegimeParticulier(regimesParticuliers.get(0).getCode());
    }
    extractBeneficiaireACSBenef(assure, trgBenef, dateDuJour);

    extractIsTeletransmissionBenef(assure, trgBenef, dateDuJour);

    trgBenef.setRangAdministratif(assure.getRangAdministratif());
    extractContactAndCodeModePaiementBenef(assure, trgBenef, dateDuJour);
    trgBenef.setOrdrePriorisation(contrat.getOrdrePriorisation());

    if (assure.getDigitRelation() != null) {
      trgBenef.setTeletransmissions(assure.getDigitRelation().getTeletransmissions());
    }
    trgBenef.setPeriodesMedecinTraitant(assure.getPeriodesMedecinTraitantOuvert());
    trgBenef.setRegimesParticuliers(assure.getRegimesParticuliers());
    trgBenef.setSituationsParticulieres(assure.getSituationsParticulieres());
  }

  private static void extractContextTiersPayant(
      ContratAIV6 contrat, TriggeredBeneficiary trgBenef) {
    final ContexteTPV6 contexteTP = contrat.getContexteTiersPayant();
    if (contexteTP != null) {
      trgBenef.setIsCartePapierAEditer(contexteTP.getIsCartePapierAEditer());
      trgBenef.setIsCarteDematerialisee(contexteTP.getIsCarteDematerialisee());
      trgBenef.setIsCartePapier(contexteTP.getIsCartePapier());
    }
  }

  private static void extractContratCollectif(ContratAIV6 contrat, TriggeredBeneficiary trgBenef) {
    ContratCollectifV6 contratCollectifV6 = contrat.getContratCollectif();
    if (contratCollectifV6 != null) {
      trgBenef.setNumeroContratCollectif(contrat.getContratCollectif().getNumero());
      trgBenef.setNumeroExterneContratCollectif(contrat.getContratCollectif().getNumeroExterne());

      trgBenef.setCollege(
          contratCollectifV6.getGroupePopulation() != null
              ? contratCollectifV6.getGroupePopulation()
              : Constants.N_A);
      trgBenef.setCollectivite(
          contratCollectifV6.getIdentifiantCollectivite() != null
              ? contratCollectifV6.getIdentifiantCollectivite()
              : Constants.N_A);
      trgBenef.setSiret(contratCollectifV6.getSiret());
      trgBenef.setRaisonSociale(contratCollectifV6.getRaisonSociale());
      trgBenef.setGroupePopulation(contratCollectifV6.getGroupePopulation());
      trgBenef.setIdentifiantCollectivite(contratCollectifV6.getIdentifiantCollectivite());
    }
  }

  private static void extractContactAndCodeModePaiementBenef(
      final Assure assure, final TriggeredBeneficiary trgBenef, final String dateDuJour) {
    if (assure.getData() != null) {
      final DataAssure data = assure.getData();
      trgBenef.setContact(data.getContact());

      final List<DestinatairePrestations> destinatairesPaiements = data.getDestinatairesPaiements();
      if (!CollectionUtils.isEmpty(destinatairesPaiements)) {
        for (final DestinatairePrestations destPaiement : destinatairesPaiements) {
          final PeriodeDestinataire periode = destPaiement.getPeriode();
          if (StringUtils.compare(periode.getDebut(), dateDuJour) <= 0
              && (periode.getFin() == null
                  || StringUtils.compare(periode.getFin(), dateDuJour) >= 0)
              && destPaiement.getModePaiementPrestations() != null) {
            trgBenef.setModePaiementPrestations(
                destPaiement.getModePaiementPrestations().getCode());
            break;
          }
        }
      }
    }
  }

  private static void extractIsTeletransmissionBenef(
      final Assure assure, final TriggeredBeneficiary trgBenef, final String dateDuJour) {
    boolean isTeleTransmission = false;
    final DigitRelation digitRelation = assure.getDigitRelation();
    if (digitRelation != null && !CollectionUtils.isEmpty(digitRelation.getTeletransmissions())) {
      for (final Teletransmission teletransmission : digitRelation.getTeletransmissions()) {
        final Periode periode = teletransmission.getPeriode();
        if (StringUtils.compare(periode.getDebut(), dateDuJour) <= 0
            && (periode.getFin() == null
                || StringUtils.compare(periode.getFin(), dateDuJour) >= 0)) {
          isTeleTransmission = teletransmission.getIsTeletransmission();
          break;
        }
      }
    }
    trgBenef.setIsTeleTransmission(isTeleTransmission);
  }

  private static void extractBeneficiaireACSBenef(
      final Assure assure, final TriggeredBeneficiary trgBenef, final String dateDuJour) {
    boolean isBeneficiaireACS = false;
    final List<CodePeriode> situationsParticulieres = assure.getSituationsParticulieres();
    if (!CollectionUtils.isEmpty(situationsParticulieres)) {
      for (final CodePeriode situationParticuliere : situationsParticulieres) {
        final Periode periode = situationParticuliere.getPeriode();
        if (StringUtils.equalsIgnoreCase(situationParticuliere.getCode(), "CMUP")
            && StringUtils.compare(periode.getDebut(), dateDuJour) <= 0
            && (periode.getFin() == null
                || StringUtils.compare(periode.getFin(), dateDuJour) >= 0)) {
          isBeneficiaireACS = true;
          break;
        }
      }
    }
    trgBenef.setIsBeneficiaireACS(isBeneficiaireACS);
  }

  private static void extractMedecinTraitantBenef(
      final Assure assure, final TriggeredBeneficiary trgBenef, final String dateDuJour) {
    boolean hasMedecinTraitant = false;
    final List<Periode> periodesMedecin = assure.getPeriodesMedecinTraitantOuvert();
    if (!CollectionUtils.isEmpty(periodesMedecin)) {
      for (final Periode periode : periodesMedecin) {
        if (StringUtils.compare(periode.getDebut(), dateDuJour) <= 0
            && (periode.getFin() == null
                || StringUtils.compare(periode.getFin(), dateDuJour) >= 0)) {
          hasMedecinTraitant = true;
          break;
        }
      }
    }
    trgBenef.setHasMedecinTraitant(hasMedecinTraitant);
  }

  public static void addStatus(
      TriggeredBeneficiary tb,
      TriggeredBeneficiaryStatusEnum statusToAdd,
      @Nullable TriggeredBeneficiaryAnomaly triggeredBeneficiaryAnomaly,
      boolean overrideLastMotif) {
    List<TriggeredBeneficiaryStatus> statuts = tb.getHistoriqueStatuts();
    TriggeredBeneficiaryStatus status = new TriggeredBeneficiaryStatus();
    status.setStatut(statusToAdd);
    status.setDateEffet(new Date());
    status.setAnomaly(triggeredBeneficiaryAnomaly);

    if (statuts != null) {
      statuts.add(status);
    } else {
      final List<TriggeredBeneficiaryStatus> tbsList = new ArrayList<>();
      tbsList.add(status);
      tb.setHistoriqueStatuts(tbsList);
    }
    if (overrideLastMotif) {
      tb.setDerniereAnomalie(triggeredBeneficiaryAnomaly);
    }
    tb.setStatut(statusToAdd);
  }

  public static String calculateDigital(
      final TriggeredBeneficiary tb, final ParametrageDroitsCarteTP pdc) {
    final Boolean papierAEditer = tb.getIsCartePapierAEditer();
    final Boolean isDemat = tb.getIsCarteDematerialisee();
    final Boolean isPapier = tb.getIsCartePapier();
    if (isDemat != null
        && papierAEditer != null) { // sans ces données là, ce n'est pas cohérent, une façon de
      // valider les informations envoyés
      if (Boolean.FALSE.equals(papierAEditer) && Boolean.FALSE.equals(isDemat)) {
        return Constants.AUCUNE_EDITION_CARTETP;
      } else if (Boolean.TRUE.equals(papierAEditer) && Boolean.FALSE.equals(isDemat)) {
        return Constants.EDITION_CARTETP_A_FAIRE;
      } else if (Boolean.FALSE.equals(isPapier) && Boolean.FALSE.equals(papierAEditer)) {
        return Constants.ATTESTATION_DIGITALE_UNIQUEMENT_A_DELIVRER;
      } else if (Boolean.TRUE.equals(papierAEditer)) {
        return Constants.EDITION_CARTETP_AFAIRE_ET_ATTESTATION_DIGITALE_A_DELIVRER;
      } else {
        return Constants.ATTESTATION_DIGITALE_A_DELIVRER_ET_AUCUNE_EDITION_CARTETP;
      }
    }
    // le contexte TP n'est pas obligatoire
    return calculateDigitalParametrage(pdc);
  }

  private static String calculateDigitalParametrage(final ParametrageDroitsCarteTP pdc) {
    final Boolean papier = pdc.getIsCarteEditablePapier();
    final Boolean isDemat2 = pdc.getIsCarteDematerialisee();
    if (papier != null && isDemat2 != null) {
      if (Boolean.TRUE.equals(papier) && Boolean.TRUE.equals(isDemat2)) {
        return Constants.EDITION_CARTETP_AFAIRE_ET_ATTESTATION_DIGITALE_A_DELIVRER;
      } else if (Boolean.TRUE.equals(papier)) {
        return Constants.EDITION_CARTETP_A_FAIRE;
      } else if (Boolean.TRUE.equals(isDemat2)) {
        return Constants.ATTESTATION_DIGITALE_UNIQUEMENT_A_DELIVRER;
      } else {
        return Constants.AUCUNE_EDITION_CARTETP;
      }
    }

    return null;
  }

  public static void controlePilotageBO(
      TriggeredBeneficiary triggeredBenef, List<TriggeredBeneficiaryAnomaly> anomalyList) {
    if (triggeredBenef.getIsCarteDematerialisee() == null) {
      anomalyList.add(TriggeredBeneficiaryAnomaly.create(Anomaly.IS_DEMATERIALISED_CARD_NOT_FOUND));
    }
    if (triggeredBenef.getIsCartePapier() == null) {
      anomalyList.add(TriggeredBeneficiaryAnomaly.create(Anomaly.IS_PAPER_CARD_NOT_FOUND));
    }
    if (triggeredBenef.getNewContract().getPeriodesDroitsCarte() == null) {
      anomalyList.add(TriggeredBeneficiaryAnomaly.create(Anomaly.NO_CARD_RIGHT_PERIODS));
    } else {
      if (StringUtils.isBlank(
          triggeredBenef.getNewContract().getPeriodesDroitsCarte().getDebut())) {
        anomalyList.add(
            TriggeredBeneficiaryAnomaly.create(Anomaly.NO_START_DATE_ON_CARD_RIGHT_PERIODS));
      }
      if (StringUtils.isBlank(triggeredBenef.getNewContract().getPeriodesDroitsCarte().getFin())) {
        anomalyList.add(
            TriggeredBeneficiaryAnomaly.create(Anomaly.NO_END_DATE_ON_CARD_RIGHT_PERIODS));
      }
    }
  }

  public static LocalDate calculDateDebut(
      final LocalDate now, final LocalDate dateDebutPotentielle, final int nombreMois) {

    if (!dateDebutPotentielle.isAfter(now)) {
      return dateDebutPotentielle;
    }
    for (int i = 1; i <= 12 / nombreMois; i++) {
      final LocalDate d = dateDebutPotentielle.minusMonths((long) i * nombreMois);
      if (!now.isBefore(d)) {
        return d;
      }
    }

    // ne devrait jamais arriver
    return null;
  }

  private static LocalDate calculateSettingsDates(
      ParametrageCarteTP parametrageCarteTP,
      TriggerEmitter origine,
      String dateEffet,
      LocalDate now) {
    if (StringUtils.isNotBlank(dateEffet)) {
      now = LocalDate.parse(dateEffet);
      if (TriggerEmitter.Renewal.equals(origine)) {
        if (DateUtils.isLeapYear(now.getYear()) && now.getMonthValue() < 2) {
          now =
              now.plusDays(
                  parametrageCarteTP.getParametrageRenouvellement().getDelaiDeclenchementCarteTP()
                      + 1L);
        } else {
          now =
              now.plusDays(
                  parametrageCarteTP.getParametrageRenouvellement().getDelaiDeclenchementCarteTP());
        }
      }
    }
    return now;
  }

  private static LocalDate calculateDatesForContractRenewal(
      TriggerEmitter parametrageTrigger,
      String dateSouscription,
      LocalDate dateDebutParametrageToSet,
      String year,
      LocalDate now) {
    final boolean isBatch = TriggerEmitter.Renewal.equals(parametrageTrigger);
    if (!isBatch && StringUtils.isNotBlank(dateSouscription)) {
      dateDebutParametrageToSet =
          LocalDate.parse(
              year + dateSouscription.substring(4, 10),
              DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD));
      if (now.isBefore(dateDebutParametrageToSet)) {
        dateDebutParametrageToSet = dateDebutParametrageToSet.minusYears(1);
      }
    }
    return dateDebutParametrageToSet;
  }

  private static int getDureeValiditeDroits(final ParametrageCarteTP parametrage) {
    int nombreMois = 0;

    if (parametrage.getParametrageRenouvellement() != null) {
      switch (parametrage.getParametrageRenouvellement().getDureeValiditeDroitsCarteTP()) {
        case Trimestriel:
          nombreMois = 3;
          break;
        case Semestriel:
          nombreMois = 6;
          break;
        case Annuel:
          nombreMois = 12;
          break;
        default:
          break;
      }
    }

    return nombreMois;
  }

  // TODO : A garder ? A virer ?
  // C'était utilisé avant la création des triggeredBenefs pour comparer avec les
  // dates radiation/résiliation et si cette date était avant on ne générait pas
  // le trigger
  // A garder éventuellement pour placer le triggeredBenef en warning ?
  // A virer pour directement faire le calcul sur les droits ?
  public static String getDateDebutParametrageCarte(
      final ParametrageCarteTP parametrageCarte,
      final TriggerEmitter triggerEmitter,
      final TriggeredBeneficiary triggeredBenef) {
    if (parametrageCarte.getParametrageRenouvellement() == null) {
      return "";
    }

    final LocalDate now = LocalDate.now(ZoneOffset.UTC);
    final int nombreMois = getDureeValiditeDroits(parametrageCarte);
    final int currentYear = now.getYear();
    final String year = String.valueOf(currentYear);
    LocalDate dateDebutParametrageToSet = now;
    final String dateSouscription =
        Objects.requireNonNullElse(triggeredBenef.getNewContract().getDateSouscription(), "");

    switch (parametrageCarte.getParametrageRenouvellement().getModeDeclenchement()) {
      case Automatique:
        if (DateRenouvellementCarteTP.AnniversaireContrat.equals(
            (parametrageCarte.getParametrageRenouvellement().getDateRenouvellementCarteTP()))) {
          dateDebutParametrageToSet =
              calculateDatesForContractRenewal(
                  triggerEmitter, dateSouscription, dateDebutParametrageToSet, year, now);
        } else {
          final String stringDateDebutParametrageToSet =
              parametrageCarte.getParametrageRenouvellement().getDebutEcheance() + "/" + year;
          dateDebutParametrageToSet =
              LocalDate.parse(
                  stringDateDebutParametrageToSet, DateTimeFormatter.ofPattern(DateUtils.DD_MM_YY));

          dateDebutParametrageToSet = calculDateDebut(now, dateDebutParametrageToSet, nombreMois);
        }
        return (dateDebutParametrageToSet != null ? dateDebutParametrageToSet.toString() : "");

      case Manuel:
        final String dateDebutDroitTP =
            Objects.requireNonNullElse(
                parametrageCarte.getParametrageRenouvellement().getDateDebutDroitTP(), "");
        if (TriggerEmitter.Renewal.equals(triggerEmitter) && !dateDebutDroitTP.isEmpty()) {
          dateDebutParametrageToSet =
              LocalDate.parse(
                  StringUtils.substring(dateDebutDroitTP, 0, 10),
                  DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD));

          dateDebutParametrageToSet =
              dateDebutParametrageToSet.plusDays(
                  parametrageCarte.getParametrageRenouvellement().getDelaiDeclenchementCarteTP());

          return dateDebutParametrageToSet.toString();
        } else {
          return now.toString();
        }

      default:
        // Cas du déclenchement Pilotage Back Office Contrat
        // Pas de gestion date spécifique
        return "";
    }
  }

  public static List<String> calculDates(
      final ParametrageTrigger parametrageTrigger,
      final DroitAssure droit,
      final boolean includeFinDroit) {
    final String dateSouscription =
        Objects.requireNonNullElse(parametrageTrigger.getDateSouscription(), "");

    String dateDebutDroitGarantieAssure = "";
    String dateFinDroitGarantieAssure = "";
    if (droit != null) {
      dateDebutDroitGarantieAssure = Objects.requireNonNullElse(droit.getPeriode().getDebut(), "");
      if (includeFinDroit) {
        dateFinDroitGarantieAssure = Objects.requireNonNullElse(droit.getPeriode().getFin(), "");
      }
    }
    final String dateAdhesion =
        Objects.requireNonNullElse(
            parametrageTrigger.getTriggeredBeneficiary().getPeriodeDebutAffiliation(), "");

    List<String> dateDebutList =
        new ArrayList<>(List.of(dateDebutDroitGarantieAssure, dateSouscription, dateAdhesion));
    List<String> dateFinList = new ArrayList<>(List.of(dateFinDroitGarantieAssure));

    Periode periode = getPeriodeFromParametrage(parametrageTrigger);
    if (periode != null) {
      dateDebutList.add(periode.getDebut());
      dateFinList.add(periode.getFin());
    }

    dateDebutList = dateDebutList.stream().filter(StringUtils::isNotBlank).toList();
    dateFinList = dateFinList.stream().filter(StringUtils::isNotBlank).toList();

    final List<String> dateDebutFin = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(dateDebutList)) {
      dateDebutFin.add(Collections.max(dateDebutList));
    }

    if (CollectionUtils.isNotEmpty(dateFinList)) {
      dateDebutFin.add(Collections.min(dateFinList));
    }

    return dateDebutFin;
  }

  public static Periode getPeriodeFromParametrage(ParametrageTrigger parametrageTrigger) {
    final int nombreMois = getDureeValiditeDroits(parametrageTrigger.getParametrageCarteTP());
    LocalDate now = LocalDate.now(ZoneOffset.UTC);
    PeriodesDroitsCarte periodesDroitsCarte = null;
    if (parametrageTrigger.getTriggeredBeneficiary() != null) {
      periodesDroitsCarte =
          parametrageTrigger.getTriggeredBeneficiary().getNewContract().getPeriodesDroitsCarte();
    }
    if (ModeDeclenchementCarteTP.PilotageBO.equals(
            parametrageTrigger
                .getParametrageCarteTP()
                .getParametrageRenouvellement()
                .getModeDeclenchement())
        && TriggerEmitter.Event.equals(parametrageTrigger.getOrigine())
        && periodesDroitsCarte != null) {
      return new Periode(periodesDroitsCarte.getDebut(), periodesDroitsCarte.getFin());
    }

    // calcul parametrage dates
    now =
        calculateSettingsDates(
            parametrageTrigger.getParametrageCarteTP(),
            parametrageTrigger.getOrigine(),
            parametrageTrigger.getDateEffet(),
            now);
    final int currentYear = now.getYear();
    final String year = String.valueOf(currentYear);

    String dateDebutParametrage = null;
    String dateFinParametrage = null;
    LocalDate dateDebutParametrageToSet = now;

    // Déclenchement automatique
    if (ModeDeclenchementCarteTP.Automatique.equals(
        parametrageTrigger
            .getParametrageCarteTP()
            .getParametrageRenouvellement()
            .getModeDeclenchement())) {
      if (DateRenouvellementCarteTP.AnniversaireContrat.equals(
          (parametrageTrigger
              .getParametrageCarteTP()
              .getParametrageRenouvellement()
              .getDateRenouvellementCarteTP()))) {
        dateDebutParametrageToSet =
            calculateDatesForContractRenewal(
                parametrageTrigger.getOrigine(),
                parametrageTrigger.getDateSouscription(),
                dateDebutParametrageToSet,
                year,
                now);
      } else {
        final String stringDateDebutParametrageToSet =
            parametrageTrigger
                    .getParametrageCarteTP()
                    .getParametrageRenouvellement()
                    .getDebutEcheance()
                + "/"
                + year;
        dateDebutParametrageToSet =
            LocalDate.parse(
                stringDateDebutParametrageToSet, DateTimeFormatter.ofPattern(DateUtils.DD_MM_YY));

        dateDebutParametrageToSet = calculDateDebut(now, dateDebutParametrageToSet, nombreMois);
      }
      if (dateDebutParametrageToSet != null) {
        dateDebutParametrage = dateDebutParametrageToSet.toString();
        dateFinParametrage =
            dateDebutParametrageToSet.plusMonths(nombreMois).minusDays(1).toString();
      }
      return new Periode(dateDebutParametrage, dateFinParametrage);
    }
    // Déclenchement manuel
    if (ModeDeclenchementCarteTP.Manuel.equals(
        parametrageTrigger
            .getParametrageCarteTP()
            .getParametrageRenouvellement()
            .getModeDeclenchement())) {
      if (TriggerEmitter.Renewal.equals(parametrageTrigger.getOrigine())
          && DateRenouvellementCarteTP.AnniversaireContrat.equals(
              parametrageTrigger
                  .getParametrageCarteTP()
                  .getParametrageRenouvellement()
                  .getDateRenouvellementCarteTP())) {
        return getPeriodeForManuelAnniversaireContrat(
            parametrageTrigger.getParametrageCarteTP(),
            nombreMois,
            now,
            dateDebutParametrageToSet,
            parametrageTrigger.isRdo(),
            parametrageTrigger.getDateSouscription());
      } else {
        return getPeriodeForManuelDebutEcheance(
            parametrageTrigger.getParametrageCarteTP(),
            parametrageTrigger.getOrigine(),
            nombreMois,
            now,
            dateDebutParametrageToSet);
      }
    }
    // else Déclenchement Pilotage Back Office Contrat
    // pas de gestion date spécifique
    return null;
  }

  private static Periode getPeriodeForManuelDebutEcheance(
      ParametrageCarteTP parametrageCarteTP,
      TriggerEmitter origine,
      int nombreMois,
      LocalDate now,
      LocalDate dateDebutParametrageToSet) {
    final String dateFinParametrage;
    final String dateDebutParametrage;
    final String dateDebutDroitTP =
        Objects.requireNonNullElse(
            parametrageCarteTP.getParametrageRenouvellement().getDateDebutDroitTP(), "");
    if (TriggerEmitter.Renewal.equals(origine) && !dateDebutDroitTP.isEmpty()) {
      dateDebutParametrageToSet =
          LocalDate.parse(
              StringUtils.substring(dateDebutDroitTP, 0, 10),
              DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD));

      dateDebutParametrageToSet =
          dateDebutParametrageToSet.plusDays(
              parametrageCarteTP.getParametrageRenouvellement().getDelaiDeclenchementCarteTP());

      dateDebutParametrage = dateDebutParametrageToSet.toString();
    } else {
      dateDebutParametrage = now.toString();
    }
    dateFinParametrage = dateDebutParametrageToSet.plusMonths(nombreMois).minusDays(1).toString();
    return new Periode(dateDebutParametrage, dateFinParametrage);
  }

  private static Periode getPeriodeForManuelAnniversaireContrat(
      ParametrageCarteTP parametrageCarteTP,
      int nombreMois,
      LocalDate now,
      LocalDate dateDebutParametrageToSet,
      boolean isRdo,
      String dateSouscription) {
    final String dateFinParametrage;
    final String dateDebutParametrage;

    LocalDate dateSouscriptionContrat =
        LocalDate.parse(
            StringUtils.substring(dateSouscription, 0, 10),
            DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD));

    final String dateDebutDroitTP =
        Objects.requireNonNullElse(
            parametrageCarteTP.getParametrageRenouvellement().getDateDebutDroitTP(), "");
    if (StringUtils.isNotBlank(dateDebutDroitTP)) {
      dateDebutParametrageToSet =
          LocalDate.parse(
              StringUtils.substring(dateDebutDroitTP, 0, 10),
              DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD));
      dateDebutParametrage = dateDebutParametrageToSet.toString();
    } else {
      dateDebutParametrage = now.toString();
    }

    boolean hasToRenew = checkIfHasToRenew(parametrageCarteTP, dateDebutDroitTP, dateSouscription);
    // RDO
    if (isRdo) {
      if (hasToRenew) {
        LocalDate dateDebutDroitsToSet =
            LocalDate.of(
                dateDebutParametrageToSet.getYear(),
                dateSouscriptionContrat.getMonth(),
                dateSouscriptionContrat.getDayOfMonth());
        dateFinParametrage = dateDebutDroitsToSet.plusMonths(nombreMois).minusDays(1).toString();
        return new Periode(dateDebutDroitsToSet.toString(), dateFinParametrage);
      } else {
        return null;
      }
    } else {
      // Lors d'un changement atelier produit
      if (hasToRenew) {
        LocalDate dateDebutDroitsToSet =
            LocalDate.of(
                dateDebutParametrageToSet.getYear(),
                dateSouscriptionContrat.getMonth(),
                dateSouscriptionContrat.getDayOfMonth());
        dateFinParametrage = dateDebutDroitsToSet.plusMonths(nombreMois).minusDays(1).toString();
        if (dateDebutParametrageToSet.isBefore(dateSouscriptionContrat)) {
          // Cas du renouvellement lors d'un changement atelier produit avant la
          // dateContratSouscription
          return new Periode(dateDebutDroitsToSet.toString(), dateFinParametrage);
        }
        return new Periode(dateDebutParametrage, dateFinParametrage);
      } else if (dateDebutParametrageToSet.isAfter(dateSouscriptionContrat)) {
        // Cas du renouvellement lors d'un changement atelier produit apres la
        // dateContratSouscription
        LocalDate dateDebutDroitsToSet =
            LocalDate.of(
                dateDebutParametrageToSet.getYear(),
                dateSouscriptionContrat.getMonth(),
                dateSouscriptionContrat.getDayOfMonth());
        dateFinParametrage = dateDebutDroitsToSet.minusDays(1).toString();
        return new Periode(dateDebutParametrage, dateFinParametrage);
      } else {
        // Cas du renouvellement n+x
        return null;
      }
    }
  }

  public static boolean checkIfHasToRenew(
      ParametrageCarteTP parametrageCarteTP, String dateDebutDroitTP, String dateSouscription) {
    LocalDate dateExecutionBatch = getDateExecutionBatch(parametrageCarteTP);
    LocalDate dateDebutParametrageToSet =
        LocalDate.parse(
            StringUtils.substring(dateDebutDroitTP, 0, 10),
            DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD));

    LocalDate dateSouscriptionContrat =
        LocalDate.parse(
            StringUtils.substring(dateSouscription, 0, 10),
            DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD));

    LocalDate dateAnniversaireAutomatique =
        LocalDate.of(
            dateDebutParametrageToSet.getYear(),
            dateSouscriptionContrat.getMonth(),
            dateSouscriptionContrat.getDayOfMonth());
    dateAnniversaireAutomatique =
        dateAnniversaireAutomatique.minusDays(
            parametrageCarteTP.getParametrageRenouvellement().getDelaiRenouvellement());

    return !dateSouscriptionContrat.isAfter(
            dateDebutParametrageToSet.with(TemporalAdjusters.lastDayOfYear()))
        && !dateExecutionBatch.isBefore(dateAnniversaireAutomatique);
  }

  public static boolean checkIfRenewalForProductChange(
      String dateSouscription, String dateDebutDroitTP, boolean isRdo) {
    LocalDate dateSouscriptionContrat =
        LocalDate.parse(
            StringUtils.substring(dateSouscription, 0, 10),
            DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD));
    LocalDate dateDebutParametrageToSet =
        LocalDate.parse(
            StringUtils.substring(dateDebutDroitTP, 0, 10),
            DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD));
    return !isRdo && dateDebutParametrageToSet.isAfter(dateSouscriptionContrat);
  }

  public static LocalDate getDateExecutionBatch(ParametrageCarteTP parametrageCarteTP) {
    return LocalDate.parse(
        StringUtils.substring(
            parametrageCarteTP.getParametrageRenouvellement().getDateExecutionBatch(), 0, 10),
        DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD));
  }

  public static void manageBenefError(
      boolean createTriggeredBenefWhenError,
      TriggeredBeneficiary benef,
      TriggeredBeneficiaryAnomaly triggeredBeneficiaryAnomaly,
      boolean overrideLastMotif) {
    if (createTriggeredBenefWhenError) {
      // Le benef doit être créé en erreur
      TriggerUtils.addStatus(
          benef,
          TriggeredBeneficiaryStatusEnum.Error,
          triggeredBeneficiaryAnomaly,
          overrideLastMotif);
    } else {
      // Le benef ne doit pas être créé
      throw new TriggerException(triggeredBeneficiaryAnomaly.getDescription());
    }
  }

  public static void manageBenefWarning(
      TriggeredBeneficiary benef,
      TriggeredBeneficiaryAnomaly triggeredBeneficiaryAnomaly,
      boolean overrideLastMotif) {
    // Le benef doit être créé en info
    TriggerUtils.addStatus(
        benef,
        TriggeredBeneficiaryStatusEnum.Warning,
        triggeredBeneficiaryAnomaly,
        overrideLastMotif);
  }

  public static String getCodeCarenceForPrestationsNatureInCarenceSetting(
      final List<ParametrageCarence> carenceList, final String prestationNature) {
    final ParametrageCarence parametrageCarence =
        carenceList.stream()
            .filter(parametrage -> prestationNature.equals(parametrage.getNaturePrestation()))
            .findFirst()
            .orElse(null);
    if (parametrageCarence != null) {
      return parametrageCarence.getCodeCarence();
    }
    return null;
  }

  public static String getDateFinOnline(final String dateFermeture, final String dateFinDroit) {
    String dateFinOnline = dateFinDroit;
    if (dateFermeture != null) {
      if (dateFinDroit != null) {
        dateFinOnline = Collections.min(List.of(dateFermeture.replace("/", "-"), dateFinOnline));
      } else {
        dateFinOnline = dateFermeture.replace("/", "-");
      }
    }
    return dateFinOnline;
  }

  public static String getDateFinOnlineSameFormat(
      final String dateFermeture, final String dateFinDroit) {
    String dateFinOnline = dateFinDroit;
    if (dateFermeture != null) {
      if (dateFinDroit != null) {
        dateFinOnline = Collections.min(List.of(dateFermeture, dateFinOnline));
      } else {
        dateFinOnline = dateFermeture;
      }
    }
    return dateFinOnline;
  }

  public static String getOrigineDeclaration(Declaration declaration) {
    if (Constants.ORIGINE_DECLARATIONEVT.equals(declaration.getUserCreation())
        || "WORKER".equals(declaration.getUserCreation())
        || "BatchRenouv".equals(declaration.getUserCreation())
        || "Request".equals(declaration.getUserCreation())
        || "Renewal".equals(declaration.getUserCreation())) {
      return Constants.ORIGINE_DECLARATIONEVT;
    } else {
      return Constants.ORIGINE_DECLARATIONTDB;
    }
  }

  public static Assure getAssureForRenouvellement(ContratAIV6 contrat, Periode periode) {
    Assure assureSouscripteur = null;
    for (final Assure assure : contrat.getAssures()) {
      if (Boolean.TRUE.equals(assure.getIsSouscripteur())) {
        assureSouscripteur = assure;
        break;
      }
    }
    if (assureSouscripteur != null) {
      for (DroitAssure droitAssure : assureSouscripteur.getDroits()) {
        if (DateUtils.isOverlapping(
            droitAssure.getPeriode().getDebut(),
            droitAssure.getPeriode().getFin(),
            periode.getDebut(),
            periode.getFin())) {
          return assureSouscripteur;
        }
      }
    }
    // Si on n'a pas trouvé de souscripteur, on recherche par rang administratif
    final List<Assure> assureContratsList = contrat.getAssures();
    assureSouscripteur =
        assureContratsList.stream()
            .filter(assure -> assure.getRangAdministratif() != null)
            .max(Comparator.comparing(Assure::getRangAdministratif))
            .orElse(assureContratsList.get(0));
    if (assureSouscripteur != null) {
      for (DroitAssure droitAssure : assureSouscripteur.getDroits()) {
        if (DateUtils.isOverlapping(
            droitAssure.getPeriode().getDebut(),
            droitAssure.getPeriode().getFin(),
            periode.getDebut(),
            periode.getFin())) {
          return assureSouscripteur;
        }
      }
    }
    return null;
  }

  public static Periode calculPeriodeReprise(
      ParametrageCarteTP parametrageCarteTP, String dateSouscription, String minDateFin) {
    LocalDate today = LocalDate.now();
    int year = today.getYear();
    String debutEcheance = parametrageCarteTP.getParametrageRenouvellement().getDebutEcheance();
    if (DateRenouvellementCarteTP.AnniversaireContrat.equals(
        parametrageCarteTP.getParametrageRenouvellement().getDateRenouvellementCarteTP())) {
      debutEcheance = dateSouscription.substring(5, 7) + "/" + dateSouscription.substring(8, 10);

      LocalDate startRenewal =
          LocalDate.of(
              year,
              Integer.parseInt(dateSouscription.substring(5, 7)),
              Integer.parseInt(dateSouscription.substring(8, 10)));
      long nbDaysBirthday = ChronoUnit.DAYS.between(today, startRenewal);
      if (nbDaysBirthday < 0) {
        year = today.getYear() + 1;
      }
      String dateDebut =
          year + "-" + debutEcheance.substring(0, 2) + "-" + debutEcheance.substring(3, 5);
      String dateFinString = getDateFinFromDebut(dateDebut, minDateFin);
      return new Periode(dateDebut, dateFinString);
    } else {
      // cas annuel
      year = today.getYear() + 1;
      String dateDebut =
          year + "-" + debutEcheance.substring(0, 2) + "-" + debutEcheance.substring(3, 5);
      String dateFinString = getDateFinFromDebut(dateDebut, minDateFin);
      return new Periode(dateDebut, dateFinString);
    }
  }

  private static String getDateFinFromDebut(String dateDebut, String minDateFin) {
    LocalDate localDateDebut = LocalDate.parse(dateDebut, DateUtils.FORMATTER);
    LocalDate minLocalDateFin = null;
    if (minDateFin != null) {
      minLocalDateFin = LocalDate.parse(minDateFin, DateUtils.FORMATTER);
      // on ajoute une année pour la reprise de l'année suivante
      minLocalDateFin = minLocalDateFin.plusYears(1);
    }
    LocalDate localDateFin = localDateDebut.plusYears(1).minusDays(1);
    if (minLocalDateFin != null
        && DateUtils.betweenLocalDate(minLocalDateFin, localDateDebut, localDateFin)) {
      localDateFin = minLocalDateFin;
    } else if (minLocalDateFin != null && minLocalDateFin.isBefore(localDateDebut)) {
      localDateFin = localDateDebut.minusDays(1);
    }
    return DateUtils.formatDate(localDateFin, DateUtils.FORMATTER);
  }
}
