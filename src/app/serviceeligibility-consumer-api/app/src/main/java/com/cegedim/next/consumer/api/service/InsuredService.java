package com.cegedim.next.consumer.api.service;

import com.cegedim.next.consumer.api.exception.ContractNotFound;
import com.cegedim.next.consumer.api.repositories.ContratAIRepository;
import com.cegedim.next.serviceeligibility.core.kafka.common.KafkaSendingException;
import com.cegedim.next.serviceeligibility.core.kafkabenef.serviceprestation.ProducerBenef;
import com.cegedim.next.serviceeligibility.core.model.kafka.AdresseAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.Contact;
import com.cegedim.next.serviceeligibility.core.model.kafka.NomAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.insuredv5.InsuredDataV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.exception.IdClientBoException;
import com.cegedim.next.serviceeligibility.core.services.IdClientBOService;
import com.cegedim.next.serviceeligibility.core.services.PersonService;
import com.cegedim.next.serviceeligibility.core.services.RecipientMessageService;
import com.cegedim.next.serviceeligibility.core.services.ValidationInsuredService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.pojo.ContractValidationBean;
import com.cegedim.next.serviceeligibility.core.services.pojo.DataForEventRibModification;
import com.cegedim.next.serviceeligibility.core.utils.*;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ValidationContractException;
import jakarta.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.CollectionUtils;

@Slf4j
@RequiredArgsConstructor
public class InsuredService {

  private static final String DATA = "data";
  private static final String ASSURES = "assures";

  private static final String IDENTITE = "identite";
  private static final String NUMERO_PERSONNE = "assures.identite.numeroPersonne";
  private static final String NUMERO = "numero";
  private static final String ID_DECLARANT = "idDeclarant";

  private final ContratAIRepository contratAIRepository;
  private final RecipientMessageService recipientMessageService;
  private final ValidationInsuredService validationService;
  private final AuthenticationFacade authenticationFacade;
  private final PersonService pservice;
  private final ProducerBenef benefProducer;
  private final IdClientBOService idClientBOService;
  private final MongoTemplate template;
  private final EventService eventService;

  /**
   * Update the insured Data (V5)
   *
   * @param insuredDataV5 Insured data
   * @param idDeclarant Declarant id
   * @param numeroContrat Contract number
   * @param numeroPersonne numeroPersonne
   * @return String response
   */
  public String updateInsured(
      InsuredDataV5 insuredDataV5,
      String idDeclarant,
      String numeroContrat,
      String numeroPersonne,
      String traceId)
      throws ContractNotFound, IdClientBoException, InterruptedException, KafkaSendingException {

    String keycloakUser = authenticationFacade.getAuthenticationUserName();

    String response = "";
    log.info(
        "Processing Insured data  for declarant {} - contract {} - numeroPersonne {} ",
        idDeclarant,
        numeroContrat,
        numeroPersonne);

    // Retrieve insured
    ContratAIV6 contratAI = contratAIRepository.findBy(idDeclarant, numeroContrat, numeroPersonne);

    if (contratAI == null) {
      response =
          String.format(
              "Le contrat %s de l'AMC %s possédant le bénéficiaire ayant pour numéro de personne %s n'a pas été trouvé",
              numeroContrat, idDeclarant, numeroPersonne);
      eventService.sendObservabilityEventInsuredInvalid(
          idDeclarant, numeroContrat, numeroPersonne, response);
      throw new ContractNotFound(response);
    } else {
      log.info(
          "Testing permission : idDeclarant URL : {} - idDeclarant Contrat : {} - idClientBo : {}",
          idDeclarant,
          contratAI.getIdDeclarant(),
          keycloakUser);

      try {
        idClientBOService.controlIdClientBO(idDeclarant, contratAI.getIdDeclarant(), keycloakUser);
      } catch (IdClientBoException e) {
        eventService.sendObservabilityEventInsuredInvalid(
            idDeclarant, numeroContrat, numeroPersonne, e.getMessage());
        throw new IdClientBoException(e.getMessage());
      }

      // Validation
      try {
        ContractValidationBean contractValidationBean = new ContractValidationBean();
        validationService.validateInsuredData(insuredDataV5, contractValidationBean);
      } catch (ValidationContractException e) {
        eventService.sendObservabilityEventInsuredInvalid(
            idDeclarant, numeroContrat, numeroPersonne, e.getMessage());
        throw new ValidationException(e.getMessage());
      }

      // Update
      int indiceAssure = identifyAssureToUpdate(contratAI.getAssures(), numeroPersonne);

      // If we found insured
      if (indiceAssure != -1) {
        completeInsuredDataV5WithAMC(insuredDataV5, idDeclarant);
        Assure assureInContractCollection = contratAI.getAssures().get(indiceAssure);

        sendPaymentBenefitEvents(
            contratAI,
            assureInContractCollection.getData(),
            insuredDataV5,
            numeroPersonne,
            numeroContrat);
        this.mappingExistingAssureV5(assureInContractCollection, insuredDataV5);

        Query query = new Query();
        Criteria criteria =
            Criteria.where(ID_DECLARANT).is(idDeclarant).and(NUMERO).is(numeroContrat);
        criteria.and(NUMERO_PERSONNE).is(numeroPersonne);
        query.addCriteria(criteria);
        Update update = new Update();
        update.set(ASSURES + "." + indiceAssure + "." + DATA, assureInContractCollection.getData());

        // complément BLUE-4759
        update.set(
            ASSURES + "." + indiceAssure + "." + IDENTITE + "." + "rangNaissance",
            insuredDataV5.getRangNaissance());
        update.set(
            ASSURES + "." + indiceAssure + "." + IDENTITE + "." + "dateNaissance",
            insuredDataV5.getDateNaissance());
        if (insuredDataV5.getNir() != null) {
          update.set(
              ASSURES + "." + indiceAssure + "." + IDENTITE + "." + "nir", insuredDataV5.getNir());
        }
        if (!CollectionUtils.isEmpty(insuredDataV5.getAffiliationsRO())) {
          update.set(
              ASSURES + "." + indiceAssure + "." + IDENTITE + "." + "affiliationsRO",
              insuredDataV5.getAffiliationsRO());
        }
        template.updateFirst(query, update, ContratAIV6.class);

        // Sending beneficiary
        List<BenefAIV5> benefs =
            pservice.extractBenefFromContratCommun(
                contratAI, keycloakUser, traceId, numeroPersonne);
        sendBenef(benefs, numeroPersonne);
      }
    }

    eventService.sendObservabilityEventInsured(contratAI.getId());
    return response;
  }

  private void sendPaymentBenefitEvents(
      ContratAIV6 contract,
      DataAssure oldData,
      DataAssure newData,
      String numeroPersonne,
      String numeroContrat) {
    EventChangeCheck eventChangeCheck =
        checkChange(oldData, newData, contract.getIdDeclarant(), numeroPersonne, numeroContrat);

    log.debug("Will generate payment and benefit change message if needed.");

    if (!eventChangeCheck.isPaymentChange() && !eventChangeCheck.isBenefitChange()) {
      log.debug("No change in payment or benefit.");
    } else {
      // If there is a change in destinatairePaiements or
      // destinataireRelevePrestations, send the linked message
      List<DataAssure> dataAssure =
          new ArrayList<>(
              contract.getAssures().stream()
                  .filter(
                      assure -> !assure.getIdentite().getNumeroPersonne().equals(numeroPersonne))
                  .map(Assure::getData)
                  .filter(Objects::nonNull)
                  .toList());
      if (newData != null) {
        dataAssure.add(newData);
      }

      recipientMessageService.prepareAndSendRecipientBenefitsMessage(contract, dataAssure);
    }
  }

  private EventChangeCheck checkChange(
      DataAssure oldData,
      DataAssure newData,
      String idDeclarant,
      String numeroPersonne,
      String numeroContrat) {
    EventChangeCheck eventChangeCheck = new EventChangeCheck();
    List<DataForEventRibModification> dataForEventRibModificationList = new ArrayList<>();

    List<DestinatairePrestations> oldDp = new ArrayList<>();
    List<DestinataireRelevePrestations> oldDrp = null;
    if (oldData != null) {
      oldDp = oldData.getDestinatairesPaiements();
      oldDrp = oldData.getDestinatairesRelevePrestations();
    }

    List<DestinatairePrestations> newDp = new ArrayList<>();
    List<DestinataireRelevePrestations> newDrp = null;
    if (newData != null) {
      newDp = newData.getDestinatairesPaiements();
      newDrp = newData.getDestinatairesRelevePrestations();
    }

    if (!CollectionUtils.isEmpty(oldDp)) {
      eventChangeCheck.setOrPaymentChange(
          CollectionUtils.isEmpty(newDp)
              || !org.apache.commons.collections.CollectionUtils.isEqualCollection(oldDp, newDp));

      List<DestinatairePrestations> newDestPrestWithRibAndVIR = new ArrayList<>();
      DestinatairePrestationsEventUtil.extractAndFilterListDestinataire(
          newDestPrestWithRibAndVIR, newData, true);
      DestinatairePrestationsEventUtil.manageRecipientEventChange(
          newDestPrestWithRibAndVIR,
          oldDp,
          dataForEventRibModificationList,
          idDeclarant,
          numeroPersonne);
    } else {
      eventChangeCheck.setOrPaymentChange(!CollectionUtils.isEmpty(newDp));
    }

    if (!CollectionUtils.isEmpty(oldDrp)) {
      eventChangeCheck.setOrBenefitChange(
          CollectionUtils.isEmpty(newDrp)
              || !org.apache.commons.collections.CollectionUtils.isEqualCollection(oldDrp, newDrp));
    } else {
      eventChangeCheck.setOrBenefitChange(!CollectionUtils.isEmpty(newDrp));
    }
    sendEventsPaymentRecipients(dataForEventRibModificationList, numeroContrat);
    return eventChangeCheck;
  }

  private void sendEventsPaymentRecipients(
      final List<DataForEventRibModification> dataForEventRibModificationList,
      String numeroContrat) {
    for (final DataForEventRibModification data : dataForEventRibModificationList) {
      this.sendEventPaymentRecipients(data, numeroContrat);
    }
  }

  public void sendEventPaymentRecipients(
      final DataForEventRibModification dataForEventRibModification, String numeroContrat) {
    this.eventService.sendObservabilityEventContractRibModification(
        dataForEventRibModification.getIdDeclarant(),
        dataForEventRibModification.getOldDestinataire(),
        dataForEventRibModification.getNewDestinataire(),
        dataForEventRibModification.getNumeroPersonne());
    log.debug(
        "Envoi event de changement de rib pour le contrat numero {} et de la personne numero {}",
        numeroContrat,
        dataForEventRibModification.getNumeroPersonne());
  }

  private void completeInsuredDataV5WithAMC(InsuredDataV5 dataAssure, String idDeclarant) {
    // Génération de l'id Beyond des destinataire
    List<DestinatairePrestations> listDestPaiement = dataAssure.getDestinatairesPaiements();
    if (!CollectionUtils.isEmpty(listDestPaiement)) {
      for (DestinatairePrestations dest : listDestPaiement) {
        dest.setIdBeyondDestinatairePaiements(
            dest.getIdDestinatairePaiements() + "-" + idDeclarant);
      }
    }
    List<DestinataireRelevePrestations> listDestRelevePrestation =
        dataAssure.getDestinatairesRelevePrestations();
    if (!CollectionUtils.isEmpty(listDestRelevePrestation)) {
      for (DestinataireRelevePrestations dest : listDestRelevePrestation) {
        dest.setIdBeyondDestinataireRelevePrestations(
            dest.getIdDestinataireRelevePrestations() + "-" + idDeclarant);
      }
    }
  }

  /**
   * Maps an existing insured from Insured data event (on met à jour l'existingAssure. parce c'est
   * la référence de l'assure que l'on a sur le contrat)
   *
   * @param existingAssure The existing insured to update
   * @param dataAssureFromRequest Insured data
   */
  private void mappingExistingAssure(Assure existingAssure, DataAssure dataAssureFromRequest) {
    DataAssure existingDataAssure = existingAssure.getData();

    if (existingDataAssure == null) {
      existingDataAssure = new DataAssure();
      existingAssure.setData(existingDataAssure);
    }

    if (dataAssureFromRequest != null) {
      mapDataAssure(dataAssureFromRequest, existingDataAssure);
    }
  }

  private void mapDataAssure(DataAssure dataAssureFromRequest, DataAssure existingDataAssure) {
    // Mapping Nom
    if (dataAssureFromRequest.getNom() != null) {
      if (existingDataAssure.getNom() == null) {
        existingDataAssure.setNom(dataAssureFromRequest.getNom());
      } else {
        this.mapNomDataAIToNomAssure(dataAssureFromRequest.getNom(), existingDataAssure.getNom());
      }
    }

    // Mapping Contact
    mapContact(dataAssureFromRequest, existingDataAssure);

    // Mapping Adresse
    mapAdresse(dataAssureFromRequest, existingDataAssure);

    if (!CollectionUtils.isEmpty(dataAssureFromRequest.getDestinatairesPaiements())) {
      existingDataAssure.setDestinatairesPaiements(
          dataAssureFromRequest.getDestinatairesPaiements());
      BusinessSortUtility.triListeDestinatairesPrestationsV4(
          existingDataAssure.getDestinatairesPaiements());
    }

    if (!CollectionUtils.isEmpty(dataAssureFromRequest.getDestinatairesRelevePrestations())) {
      existingDataAssure.setDestinatairesRelevePrestations(
          dataAssureFromRequest.getDestinatairesRelevePrestations());
      BusinessSortUtility.triListeDestinatairesRelevePrestationsV5(
          existingDataAssure.getDestinatairesRelevePrestations());
    }
  }

  private void mapAdresse(DataAssure dataAssureFromRequest, DataAssure existingdataAssure) {
    if (dataAssureFromRequest.getAdresse() != null) {
      if (existingdataAssure.getAdresse() == null) {
        existingdataAssure.setAdresse(dataAssureFromRequest.getAdresse());
      } else {
        this.mapAdresseDataAIToAdresseAssure(
            dataAssureFromRequest.getAdresse(), existingdataAssure.getAdresse());
      }
    }
  }

  private void mapContact(DataAssure dataAssureFromRequest, DataAssure existingdataAssure) {
    if (dataAssureFromRequest.getContact() != null) {
      if (existingdataAssure.getContact() == null) {
        existingdataAssure.setContact(dataAssureFromRequest.getContact());
      } else {
        this.mapContactDataAIToContactAssure(
            dataAssureFromRequest.getContact(), existingdataAssure.getContact());
      }
    }
  }

  /**
   * Maps an existing insured from Insured data event (on met à jour l'existingAssure. parce c'est
   * la référence de l'assure que l'on a sur le contrat)
   *
   * @param existingAssure The existing insured to update
   * @param dataAssureFromRequest Insured data
   */
  private void mappingExistingAssureV5(Assure existingAssure, InsuredDataV5 dataAssureFromRequest) {
    mappingExistingAssure(existingAssure, dataAssureFromRequest);

    // Mise à jour de l'assure
    if (dataAssureFromRequest != null) {
      existingAssure.getIdentite().setDateNaissance(dataAssureFromRequest.getDateNaissance());
      existingAssure.getIdentite().setRangNaissance(dataAssureFromRequest.getRangNaissance());
      existingAssure.getIdentite().setNir(dataAssureFromRequest.getNir());
      existingAssure.getIdentite().setAffiliationsRO(dataAssureFromRequest.getAffiliationsRO());
      BusinessSortUtility.triListeAffiliationRO(existingAssure.getIdentite().getAffiliationsRO());
    }
  }

  /**
   * Maps Insured name from insured data name
   *
   * @param nomDataAI Existing insured name
   * @param existingNomAssure Insured name data
   */
  private void mapNomDataAIToNomAssure(NomAssure nomDataAI, NomAssure existingNomAssure) {
    if (StringUtils.isNotEmpty(nomDataAI.getCivilite())) {
      existingNomAssure.setCivilite(nomDataAI.getCivilite());
    }

    if (StringUtils.isNotEmpty(nomDataAI.getNomFamille())) {
      existingNomAssure.setNomFamille(nomDataAI.getNomFamille());
    }

    if (StringUtils.isNotEmpty(nomDataAI.getNomUsage())) {
      existingNomAssure.setNomUsage(nomDataAI.getNomUsage());
    }

    if (StringUtils.isNotEmpty(nomDataAI.getPrenom())) {
      existingNomAssure.setPrenom(nomDataAI.getPrenom());
    }
  }

  /**
   * Maps Insured contact from insured data contact
   *
   * @param contactDataAI Existing insured contact
   * @param existingContact Insured name contact
   */
  private void mapContactDataAIToContactAssure(Contact contactDataAI, Contact existingContact) {
    if (StringUtils.isNotEmpty(contactDataAI.getEmail())) {
      existingContact.setEmail(contactDataAI.getEmail());
    }

    if (StringUtils.isNotEmpty(contactDataAI.getFixe())) {
      existingContact.setFixe(contactDataAI.getFixe());
    }

    if (StringUtils.isNotEmpty(contactDataAI.getMobile())) {
      existingContact.setMobile(contactDataAI.getMobile());
    }
  }

  /**
   * Maps Insured address from insured data address
   *
   * @param adresseDataAI Existing insured address
   * @param existingAdresseAssure Insured address contact
   */
  private void mapAdresseDataAIToAdresseAssure(
      AdresseAssure adresseDataAI, AdresseAssure existingAdresseAssure) {
    if (StringUtils.isNotEmpty(adresseDataAI.getCodePostal())) {
      existingAdresseAssure.setCodePostal(adresseDataAI.getCodePostal());
    }

    if (StringUtils.isNotEmpty(adresseDataAI.getLigne1())) {
      existingAdresseAssure.setLigne1(adresseDataAI.getLigne1());
    }

    if (StringUtils.isNotEmpty(adresseDataAI.getLigne2())) {
      existingAdresseAssure.setLigne2(adresseDataAI.getLigne2());
    }
    if (StringUtils.isNotEmpty(adresseDataAI.getLigne3())) {
      existingAdresseAssure.setLigne3(adresseDataAI.getLigne3());
    }
    if (StringUtils.isNotEmpty(adresseDataAI.getLigne4())) {
      existingAdresseAssure.setLigne4(adresseDataAI.getLigne4());
    }
    if (StringUtils.isNotEmpty(adresseDataAI.getLigne5())) {
      existingAdresseAssure.setLigne5(adresseDataAI.getLigne5());
    }
    if (StringUtils.isNotEmpty(adresseDataAI.getLigne6())) {
      existingAdresseAssure.setLigne6(adresseDataAI.getLigne6());
    }
    if (StringUtils.isNotEmpty(adresseDataAI.getLigne7())) {
      existingAdresseAssure.setLigne7(adresseDataAI.getLigne7());
    }
  }

  /**
   * Retourne l'assure identifié par sa date de naissance et son rang de naissance
   *
   * @param assures Liste des assurés
   * @param numeroPersonne Numéro de Personne
   * @return l'Assuré identifié
   */
  private int identifyAssureToUpdate(List<Assure> assures, String numeroPersonne) {
    if (!CollectionUtils.isEmpty(assures)) {
      for (int i = 0; i < assures.size(); i++) {
        if (assures.get(i).getIdentite() != null
            && assures.get(i).getIdentite().getNumeroPersonne().equals(numeroPersonne)) {
          return i;
        }
      }
    }
    return -1;
  }

  /**
   * Envoie du benef identifié dans le topic
   *
   * @param benefs Liste des benefs
   * @param numeroPersonne Numéro de personne du benéf
   */
  private void sendBenef(List<BenefAIV5> benefs, String numeroPersonne)
      throws InterruptedException, KafkaSendingException {
    if (!CollectionUtils.isEmpty(benefs)) {
      for (BenefAIV5 benef : benefs) {
        if (benef.getIdentite() != null
            && benef.getIdentite().getNumeroPersonne().equals(numeroPersonne)) {
          benefProducer.send(benef, Constants.ORIGINE_SERVICE_PRESTATION);
          break;
        }
      }
    }
  }
}
