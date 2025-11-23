package com.cegedim.next.serviceeligibility.core.services.bdd;

import com.cegedim.next.serviceeligibility.core.dao.DeclarationDao;
import com.cegedim.next.serviceeligibility.core.model.entity.ContractProcessResponseV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.TraceStatus;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.exception.IdClientBoException;
import com.cegedim.next.serviceeligibility.core.services.*;
import com.cegedim.next.serviceeligibility.core.services.event.EventInsuredTerminationService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.pojo.ContractValidationBean;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class ConsumerServicePrestationService {

  private final ValidationContratService validationService;
  private final IdClientBOService idClientBOService;
  private final EventService eventService;
  private final ServicePrestationService servicePrestationService;
  private final ReferentielParametrageCarteTPService referentielParametrageCarteTPService;
  private final PersonService pservice;
  private final TraceService traceService;
  private final BeneficiaryService benefService;
  private final BenefInfosService benefInfos;
  private final EventInsuredTerminationService eventInsuredTerminationService;
  private final DeclarationDao declarationDao;

  public ContractProcessResponseV5 processV6(
      ContratAIV6 contratAIV6, String keycloakUser, String traceId, String idDeclarant)
      throws IdClientBoException {
    ContractProcessResponseV5 reponse = new ContractProcessResponseV5();
    contratAIV6.setTraceId(traceId);
    traceService.updateStatus(traceId, TraceStatus.Deserialized, Constants.CONTRACT_TRACE);

    logTestPermission(idDeclarant, contratAIV6.getIdDeclarant(), keycloakUser);
    // Contrôle que l'idClientBo a le droit de créer/modifier des
    // données de l'AMC
    try {
      idClientBOService.controlIdClientBO(idDeclarant, contratAIV6.getIdDeclarant(), keycloakUser);
    } catch (IdClientBoException e) {
      eventService.sendObservabilityEventContractReceptionInvalid(contratAIV6, e.getMessage());
      throw e;
    }

    ContractValidationBean contractValidationBean = new ContractValidationBean();
    validationService.validateContrat(contratAIV6, contractValidationBean);
    // Valorisation du contrat
    validationService.transformeContrat(contratAIV6);

    manageSansEffetAssure(contratAIV6);

    getContractProcessResponse(keycloakUser, traceId, reponse, contratAIV6);

    return reponse;
  }

  public void getContractProcessResponse(
      String keycloakUser,
      String traceId,
      ContractProcessResponseV5 reponse,
      ContratAIV6 contractV6) {
    List<Assure> assureList = contractV6.getAssures();
    servicePrestationService.generateBeyondId(contractV6);
    referentielParametrageCarteTPService.saveReferentielParametrageCarteTP(contractV6);
    reponse.setBenefs(
        pservice.extractBenefFromContratCommun(assureList, contractV6, keycloakUser, traceId));
    reponse.setKey(contractV6.getIdDeclarant() + "-" + contractV6.getNumero());
    reponse.setContract(contractV6);
  }

  public void logTestPermission(
      String idDeclarant, String idDeclarantContrat, String keycloakUser) {
    log.info(
        "Testing permission : idDeclarant URL : {} - idDeclarant Contrat : {} - idClientBo : {}",
        idDeclarant,
        idDeclarantContrat,
        keycloakUser);
  }

  public void manageSansEffetAssure(ContratAIV6 contractV6) {
    ContratAIV6 existingContract =
        servicePrestationService.getContratByUK(
            contractV6.getIdDeclarant(), contractV6.getNumero(), contractV6.getNumeroAdherent());
    if (existingContract != null) {
      List<Assure> existingAssures = existingContract.getAssures();
      List<Assure> assures = contractV6.getAssures();
      List<String> numerosPersonneContract = getNumerosPersonne(assures);
      List<Assure> newAssures =
          existingAssures.stream()
              .filter(
                  assure ->
                      !numerosPersonneContract.contains(assure.getIdentite().getNumeroPersonne()))
              .collect(Collectors.toList());
      if (CollectionUtils.isNotEmpty(newAssures)) {
        for (Assure newAssure : newAssures) {
          checkSansEffetAssure(existingContract, newAssure, contractV6);
        }
      }
    }
  }

  private void checkSansEffetAssure(
      ContratAIV6 existingContract, Assure newAssure, ContratAIV6 newContract) {
    String benefKey =
        benefService.calculateKey(
            existingContract.getIdDeclarant(), newAssure.getIdentite().getNumeroPersonne());
    BenefAIV5 beneficiaire = benefService.getBeneficiaryByKey(benefKey);
    List<ContratAIV6> benefContracts =
        servicePrestationService.getServicePrestationV6(
            beneficiaire.getAmc().getIdDeclarant(), beneficiaire.getIdentite().getNumeroPersonne());
    if (benefContracts.contains(existingContract)) {
      if (benefContracts.size() == 1) {
        removeServiceBenef(beneficiaire, Constants.SERVICE_PRESTATION);
      }
      boolean hasTPrights =
          declarationDao.hasTPrightsForContract(
              existingContract.getIdDeclarant(),
              existingContract.getNumeroAdherent(),
              newAssure.getIdentite().getNumeroPersonne(),
              existingContract.getNumero());
      List<ContratV5> filteredContracts =
          beneficiaire.getContrats().stream()
              .filter(
                  contract ->
                      !(contract.getNumeroContrat().equals(existingContract.getNumero())
                          || StringUtils.isNotBlank(contract.getCodeEtat())))
              .toList();
      beneficiaire.setContrats(
          !beneficiaire.getServices().isEmpty() && (hasTPrights || filteredContracts.isEmpty())
              ? beneficiaire.getContrats()
              : filteredContracts);
      beneficiaire.setSocietesEmettrices(
          !beneficiaire.getServices().isEmpty() && (hasTPrights || filteredContracts.isEmpty())
              ? benefInfos.handlePeriodesSocieteEmettriceForBenef(beneficiaire.getContrats())
              : benefInfos.handlePeriodesSocieteEmettriceForBenef(filteredContracts));

      // Event
      eventInsuredTerminationService.manageEventsInsuredTerminationSansEffet(
          eventService, newAssure, newContract, existingContract.getDateSouscription());

      // Ecrase le benef existant
      benefService.save(beneficiaire);
    }
  }

  private List<String> getNumerosPersonne(List<Assure> assures) {
    List<String> numeroPersonneList = new ArrayList<>();
    for (Assure assure : assures) {
      numeroPersonneList.add(assure.getIdentite().getNumeroPersonne());
    }
    return numeroPersonneList;
  }

  public void removeServiceBenef(BenefAIV5 benef, String serviceToDelete) {
    List<String> services = benef.getServices();
    if (CollectionUtils.isNotEmpty(services)) {
      for (String service : services) {
        if (serviceToDelete.equalsIgnoreCase(service)) {
          services.remove(service);
          break;
        }
      }
    }
  }
}
