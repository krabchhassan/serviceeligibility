package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.kafka.common.KafkaSendingException;
import com.cegedim.next.serviceeligibility.core.kafka.serviceprestation.ExtractContractProducer;
import com.cegedim.next.serviceeligibility.core.mapper.MapperContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.ServicePrestationTriggerBenef;
import com.cegedim.next.serviceeligibility.core.model.entity.Retention;
import com.cegedim.next.serviceeligibility.core.model.kafka.NirRattachementRO;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.benef.BeneficiaireId;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodeSuspension;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.bdd.RetentionService;
import com.cegedim.next.serviceeligibility.core.services.event.EventInsuredTerminationService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.pojo.DataForEventRibModification;
import com.cegedim.next.serviceeligibility.core.utils.*;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.NumeroAssureException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContratAivService {

  private final MongoTemplate template;

  private final EventService eventService;

  private final RecipientMessageService recipientMessageService;

  private final SuspensionService suspensionService;

  private final ExtractContractProducer extractContractProducer;

  private final EventInsuredTerminationService eventInsuredTerminationService;
  private final RetentionService retentionService;

  private final ObjectMapper objectMapper;

  private final Logger logger = LoggerFactory.getLogger(ContratAivService.class);

  /**
   * @param contract contract to process
   * @return New and old contract if it existed
   */
  @ContinueSpan(log = "process V6")
  public List<ContratAIV6> process(final ContratAIV6 contract) throws KafkaSendingException {
    final ContratAIV6 existingContract = this.contractExists(contract);
    final ContratAIV6 newContract;
    ContratAIV6 originalContract = null;

    // If the contract doesn't exist yet...
    if (existingContract == null) {
      for (final Assure assure : contract.getAssures()) {
        assure.setDateCreation(LocalDateTime.now(ZoneOffset.UTC));
      }

      removeEmptyEndDateFromPeriods(contract);
      newContract = this.template.save(contract);

      retentionService.manageRetention(contract, originalContract);

      // If there is at least one destinatairePaiements or
      // destinataireRelevePrestations, send the linked message
      if (checkDestinatairesPaiements(newContract)
          || checkDestinatairesRelevePrestations(newContract)) {
        recipientMessageService.prepareAndSendRecipientBenefitsMessage(newContract);
      }

      eventService.sendObservabilityEventContractCreation(newContract);
      logger.debug(
          "Contract creation event sent to Observability for contract {}", newContract.getId());
    }
    // If the contract already exists...
    else {
      final EventChangeCheck eventChangeCheck = this.checkNewData(existingContract, contract);
      originalContract = cloneContract(existingContract);

      // Le nouveau contrat annule et remplace l'existant
      this.updateContract(existingContract, contract);
      removeEmptyEndDateFromPeriods(existingContract);
      newContract = this.template.save(existingContract);
      eventInsuredTerminationService.manageEventsInsuredTermination(
          eventService, contract, originalContract);
      retentionService.manageRetention(contract, originalContract);

      this.manageRemovedBenefs(contract, originalContract);
      manageRemovedBenefsForRetention(contract, originalContract);

      // Send the contract modification events
      this.eventService.sendObservabilityEventContractModification(newContract);

      // If there is a change in destinatairePaiements or
      // destinataireRelevePrestations, send the linked message
      if (eventChangeCheck.isPaymentChange() || eventChangeCheck.isBenefitChange()) {
        recipientMessageService.prepareAndSendRecipientBenefitsMessage(newContract);
      }
    }
    this.handleSuspension(originalContract, newContract);
    if (originalContract != null) {
      this.handlePaymentRecipients(originalContract, newContract);
    }

    return Arrays.asList(newContract, originalContract);
  }

  public String getMinDateDebutAssure(ContratAIV6 contract, String numeroPersonne) {
    List<com.cegedim.next.serviceeligibility.core.model.kafka.Periode> periodesDroitsAssures =
        new ArrayList<>();
    for (Assure assure : contract.getAssures()) {
      if (numeroPersonne.equals(assure.getIdentite().getNumeroPersonne())) {
        periodesDroitsAssures.addAll(assure.getPeriodes());
      }
      periodesDroitsAssures.addAll(assure.getPeriodes());
    }

    periodesDroitsAssures =
        DateUtils.getPeriodesFusionnees(
            periodesDroitsAssures.stream().filter(Objects::nonNull).toList());

    return DateUtils.getMinDateOrNull(
        periodesDroitsAssures.stream()
            .map(com.cegedim.next.serviceeligibility.core.model.kafka.Periode::getDebut)
            .toList());
  }

  public void manageRemovedBenefsForRetention(ContratAIV6 contract, ContratAIV6 originalContract) {
    // gestion retention sans effet assure
    List<Assure> oldAssures = originalContract.getAssures();
    List<Assure> newAssures = contract.getAssures();

    String today = LocalDate.now().toString();
    String dateSouscription = contract.getDateSouscription();

    // on selectionne les assures en sans effet avec une periode assurées <= today (+
    // dateSouscription contrat)
    final List<Assure> removedBenefs =
        oldAssures.stream()
            .filter(
                old -> {
                  String dateRef =
                      DateUtils.getMinDate(
                          dateSouscription,
                          getMinDateDebutAssure(
                              originalContract, old.getIdentite().getNumeroPersonne()),
                          DateUtils.FORMATTER);
                  return newAssures.stream()
                              .noneMatch(
                                  newA ->
                                      newA.getIdentite()
                                          .getNumeroPersonne()
                                          .equals(old.getIdentite().getNumeroPersonne()))
                          && dateRef == null
                      || !DateUtils.after(dateRef, today, DateUtils.FORMATTER);
                })
            .toList();

    List<Retention> retentionList = new ArrayList<>();
    removedBenefs.forEach(
        oldBenef ->
            retentionService.manageRetentionAssure(
                contract, originalContract, oldBenef, retentionList));

    if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(retentionList)) {
      for (Retention retention : retentionList) {
        retentionService.updateOrCreateRetention(retention);
      }
    }
  }

  public ContratAIV6 cloneContract(ContratAIV6 orig) {
    try {
      return objectMapper.readValue(objectMapper.writeValueAsString(orig), ContratAIV6.class);
    } catch (JsonProcessingException e) {
      return null;
    }
  }

  public void removeEmptyEndDateFromPeriods(ContratAIV6 contract) {
    if (contract.getPeriodesContratResponsableOuvert() != null) {
      handlePeriodeEndDateBlank(contract.getPeriodesContratResponsableOuvert());
    }
    if (contract.getPeriodesContratCMUOuvert() != null) {
      contract
          .getPeriodesContratCMUOuvert()
          .forEach(
              periodeContratCMUOuvert -> {
                Periode periode = periodeContratCMUOuvert.getPeriode();
                if (Boolean.TRUE.equals(isPeriodeEndDateBlank(periode, periode.getFin()))) {
                  periodeContratCMUOuvert.setPeriode(handlePeriodeEndDateBlank(periode));
                }
              });
    }
    if (contract.getPeriodesSuspension() != null) {
      contract
          .getPeriodesSuspension()
          .forEach(
              periodeSuspension -> {
                Periode periode = periodeSuspension.getPeriode();
                if (Boolean.TRUE.equals(isPeriodeEndDateBlank(periode, periode.getFin()))) {
                  periodeSuspension.setPeriode(handlePeriodeEndDateBlank(periode));
                }
              });
    }

    for (Assure assure : contract.getAssures()) {
      handleAssurePeriodeEndDateBlank(assure);
    }
  }

  private void handleAssurePeriodeEndDateBlank(Assure assure) {
    handlePeriodeEndDateBlank(assure.getPeriodes());
    if (assure.getPeriodesMedecinTraitantOuvert() != null) {
      handlePeriodeEndDateBlank(assure.getPeriodesMedecinTraitantOuvert());
    }
    if (CollectionUtils.isNotEmpty(assure.getRegimesParticuliers())) {
      assure
          .getRegimesParticuliers()
          .forEach(
              regimeParticulier -> {
                Periode periode = regimeParticulier.getPeriode();
                if (Boolean.TRUE.equals(isPeriodeEndDateBlank(periode, periode.getFin()))) {
                  regimeParticulier.setPeriode(handlePeriodeEndDateBlank(periode));
                }
              });
    }
    if (CollectionUtils.isNotEmpty(assure.getSituationsParticulieres())) {
      assure
          .getSituationsParticulieres()
          .forEach(
              situationParticuliere -> {
                Periode periode = situationParticuliere.getPeriode();
                if (Boolean.TRUE.equals(isPeriodeEndDateBlank(periode, periode.getFin()))) {
                  situationParticuliere.setPeriode(handlePeriodeEndDateBlank(periode));
                }
              });
    }
    if (assure.getIdentite() != null
        && CollectionUtils.isNotEmpty(assure.getIdentite().getAffiliationsRO())) {
      assure
          .getIdentite()
          .getAffiliationsRO()
          .forEach(
              affiliationRO -> {
                Periode periode = affiliationRO.getPeriode();
                if (Boolean.TRUE.equals(isPeriodeEndDateBlank(periode, periode.getFin()))) {
                  affiliationRO.setPeriode(handlePeriodeEndDateBlank(periode));
                }
              });
    }
    if (assure.getDigitRelation() != null
        && CollectionUtils.isNotEmpty(assure.getDigitRelation().getTeletransmissions())) {
      assure
          .getDigitRelation()
          .getTeletransmissions()
          .forEach(
              teletransmission -> {
                Periode periode = teletransmission.getPeriode();
                if (Boolean.TRUE.equals(isPeriodeEndDateBlank(periode, periode.getFin()))) {
                  teletransmission.setPeriode(handlePeriodeEndDateBlank(periode));
                }
              });
    }
    handleAssureDroitPeriodeEndDateBlank(assure);
    handleAssureDataPeriodeEndDateBlank(assure);
  }

  private void handleAssureDroitPeriodeEndDateBlank(Assure assure) {
    assure
        .getDroits()
        .forEach(
            droits -> {
              Periode periode = droits.getPeriode();
              if (Boolean.TRUE.equals(isPeriodeEndDateBlank(periode, periode.getFin()))) {
                droits.setPeriode(handlePeriodeEndDateBlank(periode));
              }
            });
  }

  private void handleAssureDataPeriodeEndDateBlank(Assure assure) {
    if (assure.getData().getDestinatairesPaiements() != null) {
      assure
          .getData()
          .getDestinatairesPaiements()
          .forEach(
              destinatairePrestations -> {
                PeriodeDestinataire periode = destinatairePrestations.getPeriode();
                if (Boolean.TRUE.equals(isPeriodeEndDateBlank(periode, periode.getFin()))) {
                  destinatairePrestations.setPeriode(handlePeriodeEndDateBlank(periode));
                }
              });
    }
    if (assure.getData().getDestinatairesRelevePrestations() != null) {
      assure
          .getData()
          .getDestinatairesRelevePrestations()
          .forEach(
              destinataireRelevePrestations -> {
                PeriodeDestinataire periode = destinataireRelevePrestations.getPeriode();
                if (Boolean.TRUE.equals(isPeriodeEndDateBlank(periode, periode.getFin()))) {
                  destinataireRelevePrestations.setPeriode(handlePeriodeEndDateBlank(periode));
                }
              });
    }
  }

  private Boolean isPeriodeEndDateBlank(Periode periode, String endDare) {
    return periode != null && "".equals(endDare);
  }

  private Boolean isPeriodeEndDateBlank(PeriodeDestinataire periode, String endDate) {
    return periode != null && "".equals(endDate);
  }

  private PeriodeDestinataire handlePeriodeEndDateBlank(PeriodeDestinataire periode) {
    PeriodeDestinataire newPeriode = new PeriodeDestinataire();
    newPeriode.setDebut(periode.getDebut());
    return newPeriode;
  }

  private Periode handlePeriodeEndDateBlank(Periode periode) {
    Periode newPeriode = new Periode();
    newPeriode.setDebut(periode.getDebut());
    return newPeriode;
  }

  private void handlePeriodeEndDateBlank(List<Periode> periodes) {
    for (int i = periodes.size() - 1; i >= 0; i--) {
      Periode currentPeriode = periodes.get(i);
      if (Boolean.TRUE.equals(isPeriodeEndDateBlank(currentPeriode, currentPeriode.getFin()))) {
        periodes.remove(i);
        periodes.add(i, handlePeriodeEndDateBlank(currentPeriode));
      }
    }
  }

  private void manageRemovedBenefs(final ContratAIV6 contract, final ContratAIV6 existingContract)
      throws KafkaSendingException {
    final List<Assure> oldBenefs = existingContract.getAssures();
    final List<Assure> newBenefs = contract.getAssures();

    // extract removed
    final var removedBenefs =
        oldBenefs.stream()
            .filter(old -> !this.existInNewList(old, newBenefs))
            .filter(assure -> assure.getIdentite().getNir() != null)
            .collect(
                Collectors.toMap(
                    oldBenef -> oldBenef.getIdentite().getNumeroPersonne(),
                    ContratAivService::extractId));
    final var removedAffiliation =
        oldBenefs.stream()
            .filter(old -> !this.existInNewList(old, newBenefs))
            .filter(assure -> assure.getIdentite().getAffiliationsRO() != null)
            .flatMap(
                assure ->
                    assure.getIdentite().getAffiliationsRO().stream()
                        .map(affiliation -> extractId(assure, affiliation)));

    // Send kafka
    Stream.concat(
            removedBenefs.values().stream(),
            removedAffiliation) // merge removed benef + affiliationRO
        .distinct()
        .forEach(
            c -> {
              logger.info("manageRemovedBenefs - Send Extract Contrat for key {}", c.getKey());
              this.extractContractProducer.send(c, c.getKey());
            });
  }

  private static BeneficiaireId extractId(final Assure assure) {
    return BeneficiaireId.builder()
        .nir(assure.getIdentite().getNir().getCode())
        .dateNaissance(assure.getIdentite().getDateNaissance())
        .rangNaissance(assure.getIdentite().getRangNaissance())
        .trackingId(null)
        .build();
  }

  private static BeneficiaireId extractId(
      final Assure assure, final NirRattachementRO affiliation) {
    return BeneficiaireId.builder()
        .nir(affiliation.getNir().getCode())
        .dateNaissance(assure.getIdentite().getDateNaissance())
        .rangNaissance(assure.getIdentite().getRangNaissance())
        .trackingId(null)
        .build();
  }

  private boolean existInNewList(final Assure old, final List<Assure> newBenefs) {
    return newBenefs.stream()
        .anyMatch(
            b ->
                StringUtils.equals(
                    b.getIdentite().getNumeroPersonne(), old.getIdentite().getNumeroPersonne()));
  }

  private boolean checkDestinatairesPaiements(final ContratAIV6 contract) {
    final List<Assure> assures = contract.getAssures();

    // For each assure...
    for (final Assure assure : assures) {
      if (!CollectionUtils.isEmpty(assure.getData().getDestinatairesPaiements())) {
        return true;
      }
    }

    return false;
  }

  // BLUE-3958 - suspension
  private void handleSuspension(final ContratAIV6 existing, final ContratAIV6 newContract) {
    if (!this.periodesSuspensionsContratsEgales(existing, newContract)) {
      final String result = this.suspensionCalculation(existing, newContract);
      if (!result.equals(Constants.ERREUR_SUSPENSION)) {
        if (result.equals(Constants.SUSPENSION)) {
          this.eventService.sendObservabilityEventContractSuspension(existing, newContract);
        } else {
          this.eventService.sendObservabilityEventContractSuspensionLifted(existing, newContract);
        }

        logger.debug(
            "Envoi event {} de suspension pour le contrat {}",
            result.equals(Constants.SUSPENSION) ? "" : "levee ",
            newContract.getId());
      }
    }
  }

  @ContinueSpan(log = "suspensionCalculation Contrat")
  public String suspensionCalculation(final ContratAIV6 existing, final ContratAIV6 newContract) {
    final ServicePrestationTriggerBenef mappedExisting = new ServicePrestationTriggerBenef();
    final ServicePrestationTriggerBenef mappedNewContract = new ServicePrestationTriggerBenef();

    if (existing != null) {
      mappedExisting.setPeriodesSuspension(existing.getPeriodesSuspension());
    }

    if (newContract != null) {
      mappedNewContract.setPeriodesSuspension(newContract.getPeriodesSuspension());
    }

    return suspensionService.suspensionCalculation(mappedExisting, mappedNewContract);
  }

  private boolean checkDestinatairesRelevePrestations(final ContratAIV6 contract) {
    final List<Assure> assures = contract.getAssures();

    // For each assure...
    for (final Assure assure : assures) {
      if (!CollectionUtils.isEmpty(assure.getData().getDestinatairesRelevePrestations())) {
        return true;
      }
    }

    return false;
  }

  public EventChangeCheck checkNewData(
      final ContratAIV6 existingContract, final ContratAIV6 contract) {
    final EventChangeCheck eventChangeCheck = new EventChangeCheck();
    final List<Assure> existingAssureList =
        Objects.requireNonNullElse(existingContract.getAssures(), Collections.emptyList());
    final List<Assure> newAssureList =
        Objects.requireNonNullElse(contract.getAssures(), Collections.emptyList());

    for (final Assure existingAssure : existingAssureList) {
      if (this.checkExistingAssure(eventChangeCheck, newAssureList, existingAssure)) break;
    }

    for (final Assure newAssure : newAssureList) {
      if (this.exitFor(eventChangeCheck)) break;
      if (CollectionUtils.isEmpty(existingAssureList)
          || (this.assureExist(newAssure, existingAssureList) == null
              && newAssure.getData() != null)) {
        eventChangeCheck.setOrPaymentChange(
            !CollectionUtils.isEmpty(newAssure.getData().getDestinatairesPaiements()));
        eventChangeCheck.setOrBenefitChange(
            !CollectionUtils.isEmpty(newAssure.getData().getDestinatairesRelevePrestations()));
      }
    }
    return eventChangeCheck;
  }

  @ContinueSpan(log = "periodesSuspensionsContratsEgales")
  public boolean periodesSuspensionsContratsEgales(
      final ContratAIV6 oldContract, final ContratAIV6 newContract) {
    final List<PeriodeSuspension> oldPeriodes =
        oldContract == null ? null : oldContract.getPeriodesSuspension();
    final List<PeriodeSuspension> newPeriodes =
        newContract == null ? null : newContract.getPeriodesSuspension();

    return this.periodesSuspensionsEgales(oldPeriodes, newPeriodes);
  }

  @ContinueSpan(log = "periodesSuspensionsEgales")
  public boolean periodesSuspensionsEgales(
      final List<PeriodeSuspension> oldPeriodes, final List<PeriodeSuspension> newPeriodes) {
    if (CollectionUtils.isEmpty(oldPeriodes) || CollectionUtils.isEmpty(newPeriodes)) {
      return false;
    }

    final Periode oldPeriode = oldPeriodes.getFirst().getPeriode();
    final Periode newPeriode = newPeriodes.getFirst().getPeriode();

    if (oldPeriode == null || newPeriode == null) {
      return false;
    }

    return oldPeriode.getDebut() != null
        && oldPeriode.getDebut().equals(newPeriode.getDebut())
        && ((oldPeriode.getFin() != null && oldPeriode.getFin().equals(newPeriode.getFin()))
            || (oldPeriode.getFin() == null && newPeriode.getFin() == null));
  }

  private boolean checkExistingAssure(
      final EventChangeCheck eventChangeCheck,
      final List<Assure> newAssureList,
      final Assure existingAssure) {
    if (this.exitFor(eventChangeCheck)) return true;
    final Assure newAssure = this.assureExist(existingAssure, newAssureList);
    if (newAssure == null && existingAssure.getData() != null) {
      eventChangeCheck.setOrPaymentChange(
          !CollectionUtils.isEmpty(existingAssure.getData().getDestinatairesPaiements()));
      eventChangeCheck.setOrBenefitChange(
          !CollectionUtils.isEmpty(existingAssure.getData().getDestinatairesRelevePrestations()));
    } else {
      List<DestinatairePrestations> existingDestPaiementList = null;
      List<DestinataireRelevePrestations> existingDestinataireRelevePrestationsList = null;
      if (existingAssure.getData() != null) {
        existingDestPaiementList = existingAssure.getData().getDestinatairesPaiements();
        existingDestinataireRelevePrestationsList =
            existingAssure.getData().getDestinatairesRelevePrestations();
      }
      List<DestinatairePrestations> newDestPaiementList = null;
      List<DestinataireRelevePrestations> newDestinataireRelevePrestationsList = null;
      if (newAssure != null && newAssure.getData() != null) {
        newDestPaiementList = newAssure.getData().getDestinatairesPaiements();
        newDestinataireRelevePrestationsList =
            newAssure.getData().getDestinatairesRelevePrestations();
      }
      eventChangeCheck.setOrPaymentChange(
          existingDestPaiementList != null
              && !existingDestPaiementList.equals(newDestPaiementList));
      eventChangeCheck.setOrBenefitChange(
          (existingDestinataireRelevePrestationsList != null
              && !existingDestinataireRelevePrestationsList.equals(
                  newDestinataireRelevePrestationsList)));
    }
    return false;
  }

  private boolean exitFor(final EventChangeCheck eventChangeCheck) {
    return eventChangeCheck.isPaymentChange() && eventChangeCheck.isBenefitChange();
  }

  private Assure assureExist(final Assure assure, final List<Assure> assureV5List) {
    for (final Assure assureFromList : assureV5List) {
      if (this.sameAssure(assureFromList, assure)) {
        return assureFromList;
      }
    }
    return null;
  }

  private boolean sameAssure(final Assure assure1, final Assure assure2) {
    final IdentiteContrat identite1 = assure1.getIdentite();
    final IdentiteContrat identite2 = assure2.getIdentite();

    if (identite1.getNumeroPersonne() == null) {
      throw new NumeroAssureException("Aucun numéro assuré");
    }

    return identite1.getNumeroPersonne().equals(identite2.getNumeroPersonne());
  }

  // Find contract based on criteria : return it
  private ContratAIV6 contractExists(final ContratAIV6 contract) {
    // CRITERIA
    final Criteria criteria =
        Criteria.where(Constants.ID_DECLARANT)
            .is(contract.getIdDeclarant())
            .and(Constants.NUMERO)
            .is(contract.getNumero())
            .and(Constants.NUMERO_ADHERENT)
            .is(contract.getNumeroAdherent());

    final Query query = new Query(criteria);

    return this.template.findOne(query, ContratAIV6.class);
  }

  private void updateContract(final ContratAIV6 existingContract, final ContratAIV6 contract) {
    MapperContrat.mapCommonToV6(existingContract, contract);
    existingContract.setContexteTiersPayant(contract.getContexteTiersPayant());
    existingContract.setPeriodesSuspension(contract.getPeriodesSuspension());
    existingContract.setPeriodesContratCMUOuvert(contract.getPeriodesContratCMUOuvert());
    existingContract.setTraceId(contract.getTraceId());
    existingContract.setNomFichierOrigine(contract.getNomFichierOrigine());
    existingContract.setContratCollectif(contract.getContratCollectif());
    for (final Assure assure : contract.getAssures()) {
      assure.setDateModification(LocalDateTime.now(ZoneOffset.UTC));
    }
    existingContract.setAssures(contract.getAssures());
  }

  void handlePaymentRecipients(final ContratAIV6 existing, final ContratAIV6 newContract) {
    final List<DataForEventRibModification> dataForEventRibModificationList = new ArrayList<>();

    final List<DestinatairePrestations> oldListDestinataire = new ArrayList<>();
    final List<DestinatairePrestations> newListDestinataire = new ArrayList<>();

    for (final Assure newAssure : newContract.getAssures()) {
      final Assure oldAssure =
          existing.getAssures().stream()
              .filter(
                  assureV5 ->
                      assureV5
                          .getIdentite()
                          .getNumeroPersonne()
                          .equals(newAssure.getIdentite().getNumeroPersonne()))
              .findFirst()
              .orElse(null);
      if (oldAssure != null) {
        DestinatairePrestationsEventUtil.extractAndFilterListDestinataire(
            newListDestinataire, newAssure.getData(), true);
        DestinatairePrestationsEventUtil.extractAndFilterListDestinataire(
            oldListDestinataire, oldAssure.getData(), false);
        final String numeroPersonne = newAssure.getIdentite().getNumeroPersonne();
        if (!CollectionUtils.isEmpty(oldListDestinataire)) {
          // Changement des périodes de début et/ou de fin de 2 destinataires existants et
          // avec des IBAN différents
          // Changement de la valeur de l’IBAN d'un destinataire ou changement du mode de
          // paiement différent de VIR vers VIR
          DestinatairePrestationsEventUtil.manageRecipientEventChange(
              newListDestinataire,
              oldListDestinataire,
              dataForEventRibModificationList,
              newContract.getIdDeclarant(),
              numeroPersonne);
        } else {
          // nouveau(x) destinataire(s)
          newListDestinataire.forEach(
              destinatairePrestationsV4 ->
                  this.newRecipientWithDifferentIban(
                      newContract,
                      dataForEventRibModificationList,
                      numeroPersonne,
                      oldListDestinataire,
                      destinatairePrestationsV4));
        }
      }
    }
    this.sendEventsPaymentRecipients(dataForEventRibModificationList, newContract.getId());
  }

  void newRecipientWithDifferentIban(
      final ContratAIV6 newContract,
      final List<DataForEventRibModification> dataForEventRibModificationList,
      final String numeroPersonne,
      final List<DestinatairePrestations> oldDestinatairePrestationsList,
      final DestinatairePrestations newdestinatairePrestations) {

    final List<DestinatairePrestations> existingIban =
        oldDestinatairePrestationsList.stream()
            .filter(
                destinatairePrestationsV4 ->
                    destinatairePrestationsV4
                        .getRib()
                        .getIban()
                        .equals(newdestinatairePrestations.getRib().getIban()))
            .toList();
    if (CollectionUtils.isEmpty(existingIban)) {
      // aucun iban identique
      for (final DestinatairePrestations oldDestinatairePrestationsV4 :
          oldDestinatairePrestationsList) {
        DestinatairePrestationsEventUtil.addNewEvent(
            newContract.getIdDeclarant(),
            dataForEventRibModificationList,
            numeroPersonne,
            newdestinatairePrestations,
            oldDestinatairePrestationsV4);
      }
    }
  }

  private void sendEventsPaymentRecipients(
      final List<DataForEventRibModification> dataForEventRibModificationList, String idContract) {
    for (final DataForEventRibModification data : dataForEventRibModificationList) {
      this.sendEventPaymentRecipients(data, idContract);
    }
  }

  private void sendEventPaymentRecipients(
      final DataForEventRibModification dataForEventRibModification, String idContract) {
    logger.debug("Envoi event de changement de rib pour le contrat {}", idContract);
    this.eventService.sendObservabilityEventContractRibModification(
        dataForEventRibModification.getIdDeclarant(),
        dataForEventRibModification.getOldDestinataire(),
        dataForEventRibModification.getNewDestinataire(),
        dataForEventRibModification.getNumeroPersonne());
  }

  @ContinueSpan(log = "getId ContratAICommun")
  public String getId(final ContratAICommun contratAICommun) {
    if (contratAICommun != null) {
      return contratAICommun.getId();
    } else {
      return "UNKNOWN";
    }
  }
}
