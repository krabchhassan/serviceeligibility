package com.cegedim.next.beneficiary.worker.service;

import com.cegedim.next.beneficiary.worker.utils.BusinessUtils;
import com.cegedim.next.serviceeligibility.core.model.kafka.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.EventType;
import com.cegedim.next.serviceeligibility.core.model.kafka.Source;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DataAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Dematerialisation;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DestinatairePrestations;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DestinataireRelevePrestations;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratV5;
import com.cegedim.next.serviceeligibility.core.services.BenefInfosService;
import com.cegedim.next.serviceeligibility.core.services.bdd.BeneficiaryService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TraceService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.utils.BusinessSortUtility;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.Util;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class BenefV5Service {
  private final Logger logger = LoggerFactory.getLogger(BenefV5Service.class);
  public static final String SERVICE_PRESTATION = "SERVICE_PRESTATION";

  @Autowired private MongoTemplate template;

  @Autowired private TraceService traceService;

  @Autowired private EventService eventService;

  @Autowired private BenefInfosService benefInfos;

  public BenefAIV5 process(BenefAIV5 benefAi, boolean isFirst, Source source) {
    logger.debug("Process BenefAIV5...");

    BenefAIV5 benefToSave = returnUpdatedBeneficiary(benefAi, source);
    traceService.updateStatus(
        benefAi.getTraceId(), TraceStatus.SuccesfullyProcessed, Constants.BENEF_TRACE);
    try {
      logger.debug("Try to save beneficiary");
      return template.save(benefToSave, Constants.BENEFICIAIRE_COLLECTION_NAME);
    } catch (org.springframework.dao.DuplicateKeyException e) {
      logger.debug("Beneficiary already exists");
      if (isFirst) {
        logger.debug("Processing beneficiary once again");
        return process(benefAi, false, source);
      }
    }
    return null;
  }

  public BenefAIV5 returnUpdatedBeneficiary(BenefAIV5 benefAi, Source source) {
    BenefAIV5 existingBenef = benefExists(benefAi);
    if (existingBenef == null) {
      logger.debug(
          "Beneficiary isn't already in database. Sending event '{}' to Observability",
          EventType.BENEFICIARY_EVENT_CREATION);
      eventService.sendObservabilityEventBeneficiaryCreation(benefAi);
      IdentiteContrat identite = benefAi != null ? benefAi.getIdentite() : null;
      BeneficiaryService.updateBirthRankDateAndAffiliation(identite, identite, source);
      return benefAi;
    } else {
      logger.debug(
          "Beneficiary exists in database. Sending event '{}' to Observability",
          EventType.BENEFICIARY_EVENT_MODIFICATION);
      eventService.sendObservabilityEventBeneficiaryModification(benefAi);
      return updateBeneficiaire(benefAi, existingBenef, source);
    }
  }

  /*
   * take the new benef and merge/update the contracts, services and NIRs in the
   * identite -> data
   */
  private BenefAIV5 updateBeneficiaire(BenefAIV5 benefAi, BenefAIV5 existingBenef, Source source) {
    logger.debug("Update beneficiary with id : {}", existingBenef.getId());
    benefAi.setId(existingBenef.getId());
    // services
    List<String> serviceList = existingBenef.getServices();
    List<String> newServiceList = benefAi.getServices();

    if (serviceList == null) {
      serviceList = new ArrayList<>();
    }
    if (newServiceList == null) {
      newServiceList = new ArrayList<>();
    }
    if (!newServiceList.isEmpty()) {
      for (String newService : newServiceList) {
        if (StringUtils.isNotBlank(newService) && !serviceList.contains(newService)) {
          logger.debug(
              "Actual service list size : {}. Adding '{}' to service list",
              serviceList.size(),
              newService);
          serviceList.add(newService);
        }
      }
    }

    benefAi.setServices(serviceList);

    // Si la source est AUTRE on update que les services
    if (Source.AUTRE.equals(source)) {
      return benefAi;
    }

    logger.debug("Updating contract");
    updateContract(benefAi, existingBenef);

    if (SERVICE_PRESTATION.equals(source.name())) {
      benefAi.setSocietesEmettrices(
          benefInfos.handlePeriodesSocieteEmettriceForBenef(benefAi.getContrats()));
    } else {
      benefAi.setSocietesEmettrices(existingBenef.getSocietesEmettrices());
    }

    BusinessSortUtility.updateNirs(benefAi.getIdentite(), existingBenef.getIdentite(), source);

    BeneficiaryService.updateBirthRankDateAndAffiliation(
        benefAi.getIdentite(), existingBenef.getIdentite(), source);

    // audit
    Audit audit = new Audit();
    audit.setDateEmission(DateUtils.generateDate());
    benefAi.setAudit(audit);
    return benefAi;
  }

  private void updateContract(BenefAIV5 benefAi, BenefAIV5 existingBenef) {
    List<ContratV5> newContratList = Util.assignList(benefAi.getContrats(), new ArrayList<>());
    List<ContratV5> oldContratList =
        Util.assignList(existingBenef.getContrats(), new ArrayList<>());

    if (!newContratList.isEmpty()) {
      for (ContratV5 newContract : newContratList) {
        boolean found = false;
        logger.debug("Contract {} found", newContract.getNumeroContrat());

        for (int i = 0; i < oldContratList.size(); i++) {
          ContratV5 existingContract = oldContratList.get(i);

          if (existingContract != null) {
            String cNumber = newContract.getNumeroContrat();
            if (StringUtils.isNotBlank(cNumber)
                && cNumber.equals(existingContract.getNumeroContrat())) {
              logger.debug("Contract already exists, proceed to update");
              updateContractV5(newContract, existingContract);

              oldContratList.set(i, newContract);
              found = true;
              break;
            }
          }
        }
        if (!found) {
          logger.debug("Not found, setting default 'dematerialisation' information");
          setDematInNewContract(newContract);
          oldContratList.add(newContract);
        }
      }
    }
    benefAi.setContrats(oldContratList);
  }

  private void setDematInNewContract(ContratV5 newContract) {
    if (newContract != null) {
      DataAssure dataAssureV5 = newContract.getData();
      if (dataAssureV5 != null) {
        List<DestinataireRelevePrestations> newDrpList =
            dataAssureV5.getDestinatairesRelevePrestations();
        setDefaultDemat(newDrpList);
      }
    }
  }

  private void updateContractV5(ContratV5 newContract, ContratV5 existingContract) {
    // Gestion de la mise à jour des champs - Si on recoit
    // un bénéf avec des infos manquantes par rapport à
    // celui existant on garde les anciennes valeurs
    if (StringUtils.isBlank(newContract.getCodeEtat())) {
      newContract.setCodeEtat(existingContract.getCodeEtat());
    }

    DataAssure newData = newContract.getData();
    DataAssure existingData = existingContract.getData();
    if (newData == null) {
      if (existingData != null) {
        List<DestinataireRelevePrestations> existingDrpList =
            existingData.getDestinatairesRelevePrestations();
        setDefaultDemat(existingDrpList);
      }
      newContract.setData(existingData);
    } else {
      setNewData(newContract, newData, existingData);
    }

    if (StringUtils.isBlank(newContract.getNumeroAdherent())) {
      newContract.setNumeroAdherent(existingContract.getNumeroAdherent());
    }

    if (StringUtils.isBlank(newContract.getSocieteEmettrice())) {
      newContract.setSocieteEmettrice(existingContract.getSocieteEmettrice());
    }

    if (StringUtils.isBlank(newContract.getNumeroAMCEchange())) {
      newContract.setNumeroAMCEchange(existingContract.getNumeroAMCEchange());
    }

    if (CollectionUtils.isEmpty(newContract.getPeriodes())) {
      newContract.setPeriodes(existingContract.getPeriodes());
    }
  }

  private void setNewData(ContratV5 newContract, DataAssure newData, DataAssure existingData) {
    logger.debug("Setting new data for contract n°{}", newContract.getNumeroContrat());

    // Cas particulier de l'adresse on ne contrôle pas
    // champs par champs mais uniquement si on a une
    // adresse dans le nouveau benef
    if (newData.getAdresse() == null && existingData != null) {
      newContract.getData().setAdresse(existingData.getAdresse());
    }

    // Gestion du nom
    NomAssure newNomAssure = newData.getNom();
    NomAssure existingNomAssure = (existingData != null) ? existingData.getNom() : null;
    newNomAssure = BusinessUtils.updateName(newNomAssure, existingNomAssure);
    newData.setNom(newNomAssure);

    // Gestion du contact
    Contact newContact = newData.getContact();
    Contact existingContact = (existingData != null) ? existingData.getContact() : null;
    newContact = BusinessUtils.updateContact(newContact, existingContact);
    newData.setContact(newContact);

    // Gestion des DestinatairePrestation
    List<DestinatairePrestations> newDpList = newData.getDestinatairesPaiements();
    List<DestinatairePrestations> existingDpList =
        (existingData != null) ? existingData.getDestinatairesPaiements() : null;
    if (newDpList == null || newDpList.isEmpty()) {
      newContract.getData().setDestinatairesPaiements(existingDpList);
    } else {
      newContract.getData().setDestinatairesPaiements(newDpList);
    }

    // Gestion des DestinataireRelevePrestations
    List<DestinataireRelevePrestations> newDrpList = newData.getDestinatairesRelevePrestations();
    List<DestinataireRelevePrestations> existingDrpList =
        (existingData != null) ? existingData.getDestinatairesRelevePrestations() : null;
    if (newDrpList == null || newDrpList.isEmpty()) {
      logger.debug("New DRP list is empty, setting newContract's DRP list to existingDRPList");
      setDefaultDemat(existingDrpList);
      newContract.getData().setDestinatairesRelevePrestations(existingDrpList);
    } else {
      setDefaultDemat(newDrpList);
      newContract.getData().setDestinatairesRelevePrestations(newDrpList);
    }
  }

  private void setDefaultDemat(List<DestinataireRelevePrestations> drpList) {
    if (!CollectionUtils.isEmpty(drpList)) {
      for (DestinataireRelevePrestations destinataireRelevePrestations : drpList) {
        Dematerialisation demat = destinataireRelevePrestations.getDematerialisation();
        if (demat == null) {
          demat = new Dematerialisation();
          demat.setIsDematerialise(false);
          destinataireRelevePrestations.setDematerialisation(demat);
        }
      }
    }
  }

  // Find beneficiairy based on criteria : return it
  private BenefAIV5 benefExists(BenefAIV5 benefAi) {
    if (benefAi == null) {
      return null;
    }

    Amc amc = benefAi.getAmc();
    IdentiteContrat identite = benefAi.getIdentite();
    String idDeclarant = "";
    if (amc != null) {
      idDeclarant = amc.getIdDeclarant();
    }
    if (amc == null || identite == null) {
      String traceId = benefAi.getTraceId();
      logger.error(
          "Beneficiary or contract null for Declaration with ID declarant {} and traceId {}",
          idDeclarant,
          traceId);
      return null;
    } else {
      // CRITERIA
      Criteria criteria = new Criteria();
      criteria.andOperator(
          Criteria.where("amc.idDeclarant").is(idDeclarant),
          Criteria.where("identite.numeroPersonne").is(identite.getNumeroPersonne()));

      Query query = new Query(criteria);

      return template.findOne(query, BenefAIV5.class, Constants.BENEFICIAIRE_COLLECTION_NAME);
    }
  }
}
