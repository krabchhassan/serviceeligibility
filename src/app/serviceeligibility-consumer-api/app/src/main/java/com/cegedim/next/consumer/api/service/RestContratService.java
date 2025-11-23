package com.cegedim.next.consumer.api.service;

import com.cegedim.next.serviceeligibility.core.dao.DeclarationDao;
import com.cegedim.next.serviceeligibility.core.dao.ServicePrestationDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.Retention;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.ContratAivService;
import com.cegedim.next.serviceeligibility.core.services.ExtractContractsService;
import com.cegedim.next.serviceeligibility.core.services.bdd.*;
import com.cegedim.next.serviceeligibility.core.services.bdd.ConsumerServicePrestationService;
import com.cegedim.next.serviceeligibility.core.services.event.EventInsuredTerminationService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerCreationService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ParametrageCarteTPNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestContratService {

  private final Logger logger = LoggerFactory.getLogger(RestContratService.class);

  // TODO To remove after rewrite method in Dao
  private final MongoTemplate template;

  private final ServicePrestationDao servicePrestationDao;

  private final TriggerCreationService triggerCreationService;

  private final EventService eventService;

  private final ExtractContractsService extractContractsService;

  private final EventInsuredTerminationService eventInsuredTerminationService;

  private final ConsumerServicePrestationService consumerServicePrestationService;

  private final RetentionService retentionService;

  private final ContratAivService contratAivService;

  private final DeclarationDao declarationDao;

  public ContratAIV6 deleteContrat(String idDeclarant, String numeroContrat, String subscriberId) {
    ContratAIV6 contrat =
        servicePrestationDao.getContratByUK(idDeclarant, numeroContrat, subscriberId);
    if (contrat != null) {
      // Avant de supprimer le contrat il faut envoyer une demande de
      // fermeture de déclaration
      boolean deleteServiceTP = false;
      try {
        String triggerId =
            triggerCreationService.generateTriggerFromContracts(null, contrat, true, null, true);
        if (triggerId != null && logger.isDebugEnabled()) {
          logger.debug(
              String.format(
                  "Trigger généré et envoyé sur kafka pour le contrat %s de l'AMC %s, id : %s",
                  contrat.getNumero(), contrat.getIdDeclarant(), triggerId));
        }
      } catch (ParametrageCarteTPNotFoundException e) {
        logger.error("Getting ref data error : {}", e.getMessage());
      } catch (Exception e) {
        logger.error("Error during declaration generation : {}", e.getMessage(), e);
      }

      // Suppression du contrat
      logger.info("Suppression du contrat {}/{}/{}", idDeclarant, numeroContrat, subscriberId);
      servicePrestationDao.remove(contrat);
      // Boucle sur les assures afin de les supprimer de la collection
      // beneficiaires s'ils n'existent plus dans un autre contrat
      List<Assure> assures = contrat.getAssures();
      if (!CollectionUtils.isEmpty(assures)) {
        deleteContratAssures(idDeclarant, numeroContrat, assures, contrat, deleteServiceTP);
      }

      eventService.sendObservabilityEventContractDelete(contrat);
      this.extractContractsService.sendExtractContractByBeneficiaryIdMessage(contrat);
    }
    return contrat;
  }

  private void deleteContratAssures(
      String idDeclarant,
      String numeroContrat,
      List<Assure> assures,
      ContratAIV6 contrat,
      boolean deleteServiceTP) {
    List<Retention> retentionList = new ArrayList<>();
    for (Assure assure : assures) {
      // Event
      eventInsuredTerminationService.manageEventsInsuredTerminationSansEffet(
          eventService, assure, contrat, contrat.getDateSouscription());

      Query qryBenef = new Query();
      qryBenef.addCriteria(
          Criteria.where(Constants.ID_DECLARANT_BENEF)
              .is(idDeclarant)
              .andOperator(
                  Criteria.where(Constants.NUMERO_PERSONNE_BENEF)
                      .is(assure.getIdentite().getNumeroPersonne()),
                  Criteria.where(Constants.NUMERO_ADHERENT).is(contrat.getNumeroAdherent())));
      BenefAIV5 benef =
          template.findOne(qryBenef, BenefAIV5.class, Constants.BENEFICIAIRE_COLLECTION_NAME);
      removeContractFromBenefV5(
          benef,
          numeroContrat,
          idDeclarant,
          deleteServiceTP,
          contrat.getNumeroAdherent()); // gestion sans effet assure pour delete contrat
      String today = LocalDate.now().toString();
      String dateRef =
          DateUtils.getMinDate(
              contrat.getDateSouscription(),
              contratAivService.getMinDateDebutAssure(
                  contrat, assure.getIdentite().getNumeroPersonne()),
              DateUtils.FORMATTER);
      if (dateRef == null || !DateUtils.after(dateRef, today, DateUtils.FORMATTER)) {
        retentionService.manageRetentionAssure(null, contrat, assure, retentionList);
      }
      if (CollectionUtils.isNotEmpty(retentionList)) {
        for (Retention retention : retentionList) {
          retentionService.updateOrCreateRetention(retention);
        }
      }
    }
  }

  private void removeContractFromBenefV5(
      BenefAIV5 benef,
      String numeroContrat,
      String idDeclarant,
      boolean deleteServiceTP,
      String numeroAdherent) {
    if (benef != null) {
      List<ContratV5> contratsBenef = benef.getContrats();
      if (!CollectionUtils.isEmpty(contratsBenef)) {
        for (ContratV5 contratBenef : contratsBenef) {
          if (contratBenef.getNumeroContrat().equals(numeroContrat)) {
            // S'il n'existe pas de droits TP pour ce contrat, on retire le contrat du bénef
            if (!declarationDao.hasTPrightsForContract(
                idDeclarant,
                numeroAdherent,
                benef.getIdentite().getNumeroPersonne(),
                numeroContrat)) {
              contratsBenef.remove(contratBenef);
            }
            break;
          }
        }
        checkServiceRemovalV5(
            benef,
            idDeclarant,
            contratsBenef,
            deleteServiceTP,
            numeroAdherent,
            benef.getIdentite().getNumeroPersonne(),
            numeroContrat);
        template.save(benef, Constants.BENEFICIAIRE_COLLECTION_NAME);
      }
    }
  }

  private void checkServiceRemovalV5(
      BenefAIV5 benef,
      String idDeclarant,
      List<ContratV5> contracts,
      boolean deleteServiceTP,
      String numeroAdherent,
      String numeroPersonne,
      String numeroContrat) {
    if (contracts == null || contracts.isEmpty()) {
      consumerServicePrestationService.removeServiceBenef(benef, Constants.SERVICE_PRESTATION);
    } else {
      List<String> numbers = new ArrayList<>();
      for (ContratV5 item : contracts) {
        numbers.add(item.getNumeroContrat());
      }
      Query qryContrat = new Query();
      qryContrat.addCriteria(
          Criteria.where(Constants.ID_DECLARANT)
              .is(idDeclarant)
              .andOperator(Criteria.where(Constants.NUMERO).in(numbers)));
      ContratAIV6 contrat = template.findOne(qryContrat, ContratAIV6.class);
      if (contrat == null) {
        consumerServicePrestationService.removeServiceBenef(benef, Constants.SERVICE_PRESTATION);
      }
    }

    if (deleteServiceTP) {
      Query qryDeclaration = new Query();
      qryDeclaration.addCriteria(
          Criteria.where(Constants.ID_DECLARANT)
              .is(idDeclarant)
              .and(Constants.CONTRAT_NUMERO_ADHERENT)
              .is(numeroAdherent)
              .and(Constants.CONTRAT_NUMERO)
              .ne(numeroContrat)
              .and(Constants.BENEFICIAIRE_NUMERO_PERSONNE)
              .is(numeroPersonne));
      Declaration dec = template.findOne(qryDeclaration, Declaration.class);
      if (dec == null) {
        consumerServicePrestationService.removeServiceBenef(benef, Constants.SERVICE_TP);
      }
    }
  }
}
