package com.cegedim.next.serviceeligibility.rdoserviceprestationpurge.services;

import static com.cegedim.next.serviceeligibility.rdoserviceprestationpurge.constants.Constants.SEARCH_BY_AMC_NUMBER;
import static com.cegedim.next.serviceeligibility.rdoserviceprestationpurge.constants.Constants.SEARCH_BY_FILE_NAME;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.FluxDao;
import com.cegedim.next.serviceeligibility.core.dao.*;
import com.cegedim.next.serviceeligibility.core.elast.BenefElasticService;
import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduPurgeRdo;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiary;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6Light;
import com.cegedim.next.serviceeligibility.core.services.BenefInfosService;
import com.cegedim.next.serviceeligibility.core.services.ParametrageCarteTPService;
import com.cegedim.next.serviceeligibility.core.services.RDOServicePrestationService;
import com.cegedim.next.serviceeligibility.core.services.ReferentielParametrageCarteTPService;
import com.cegedim.next.serviceeligibility.core.services.bdd.*;
import com.cegedim.next.serviceeligibility.core.services.bdd.RestBeneficiaireService;
import com.cegedim.next.serviceeligibility.core.services.contracttp.ContractService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {
  @Value("${BATCH_SIZE:10000}")
  private long batchSize;

  private final BeneficiaryService benefService;

  private final TraceService traceService;

  private final MongoTemplate template;

  private final ServicePrestationService servicePrestationService;

  private final TriggerService triggerService;

  private final ContractService contractService;

  private final DeclarationService declarationService;

  private final CrexProducer crexProducer;

  private final BenefElasticService benefElasticService;

  private final SasContratService sasContratService;

  private final RestBeneficiaireService restBeneficiaireService;

  private final ParametrageCarteTPService parametrageCarteTPService;

  private final ReferentielParametrageCarteTPService referentielParametrageCarteTPService;

  private final BenefInfosService benefInfos;

  private final DeclarationConsolideDao declarationConsolideDao;

  private final CarteDematDao carteDematDao;

  private final CartePapierDao cartePapierDao;

  private final FluxDao fluxDao;

  private final HistoriqueExecutionsDao historiqueExecutionsDao;

  private final HistoriqueExecutionsRenouvellementDao historiqueExecutionsRenouvellementDao;

  private final RDOServicePrestationService rdoServicePrestationService;

  private final Logger logger = LoggerFactory.getLogger(FileService.class);

  private void showLogs(String message) {
    logger.debug(message);
  }

  public boolean processFile(int searchType, String input) {
    boolean foundTechnicalIssue = false;
    CompteRenduPurgeRdo compteRendu = new CompteRenduPurgeRdo();

    try {
      if (searchType == SEARCH_BY_AMC_NUMBER) {
        foundTechnicalIssue = processByAmc(input, compteRendu);
      } else if (searchType == SEARCH_BY_FILE_NAME) {
        foundTechnicalIssue = processHTPByFileName(input, compteRendu);
        foundTechnicalIssue = foundTechnicalIssue || processTPByFileName(input, compteRendu);
        deleteHistory();
      }
    } catch (Exception e) {
      log.error("Erreur lors du traitement de purge : {}", e.getMessage(), e);
      foundTechnicalIssue = true;
    } finally {
      displayResult(compteRendu);
      crexProducer.generateCrex(compteRendu);
    }

    return foundTechnicalIssue;
  }

  private void displayResult(CompteRenduPurgeRdo compteRendu) {
    log.info("=== Resultat du traitement de purge ===");

    // Benef
    log.info("Beneficiaires :");
    log.info("- Nombre de traces assuré purges : {}", compteRendu.getTracesInsuredPurges());
    log.info("- Nombre de traces beneficiaire purges : {}", compteRendu.getTracesBenefPurges());
    log.info("- Nombre de beneficiaires supprimes : {}", compteRendu.getBenefsPurges());

    // HTP
    log.info("Contrats HTP :");
    log.info(
        "- Nombre de traces service prestation purges : {}",
        compteRendu.getTracesServicePrestationPurges());
    log.info(
        "- Nombre de service prestation purges : {}", compteRendu.getServicePrestationPurges());
    log.info(
        "- Nombre de beneficiaires ayant vu leurs droits HTP modifies : {}",
        compteRendu.getBenefsModifiesHTP());

    // TP
    log.info("Contrats TP :");
    log.info("- Nombre de triggers purges : {}", compteRendu.getTriggersPurges());
    log.info(
        "- Nombre de triggered beneficiaries purges : {}",
        compteRendu.getTriggeredBeneficiariesPurges());
    log.info("- Nombre de contrats modifies : {}", compteRendu.getContratsModifies());
    log.info("- Nombre de contrats purges : {}", compteRendu.getContratsPurges());
    log.info(
        "- Nombre de traces declaration purges : {}", compteRendu.getTracesDeclarationPurges());
    log.info("- Nombre de declarations purges : {}", compteRendu.getDeclarationsPurges());
    log.info(
        "- Nombre de beneficiaires ayant vu leurs droits TP modifies : {}",
        compteRendu.getBenefsModifiesTP());
    log.info(
        "- Nombre de parametrages de carte TP purges : {}",
        compteRendu.getParametrageCarteTPPurges());
    log.info(
        "- Nombre de referentiels de parametrages de carte TP  purges : {}",
        compteRendu.getReferentielParametrageCarteTPPurges());

    // Conso
    logger.info("Consolidation :");
    logger.info(
        "- Nombre de déclaration consolidée carte démat purges : {}",
        compteRendu.getDeclarationConsolidePurges());
    logger.info("- Nombre de trace purges : {}", compteRendu.getTracesPurges());
    logger.info("- Nombre de carte dématérialisée purges : {}", compteRendu.getCarteDematPurges());
    logger.info("- Nombre de carte papier purges : {}", compteRendu.getCartePapierPurges());
    logger.info("- Nombre de traceFlux purges : {}", compteRendu.getTraceFluxPurges());
    logger.info(
        "- Nombre d'historique execution purges : {}", compteRendu.getHistoriqueExecutionPurges());
    logger.info(
        "- Nombre d'historique execution renouvellement purges : {}",
        compteRendu.getHistoriqueExecutionRenouvellementPurges());
  }

  private boolean processTPByFileName(String input, CompteRenduPurgeRdo compteRendu) {
    boolean foundTechnicalIssue = false;
    int iterator = 0;
    List<Declaration> declarations = declarationService.findDeclarationsByNomFichierOrigine(input);

    while (!CollectionUtils.isEmpty(declarations)) {
      log.info(
          "Lecture de declarations en base pour le nom de fichier {} - {} traites",
          input,
          batchSize * iterator);
      for (Declaration declaration : declarations) {
        foundTechnicalIssue =
            processDeclaration(input, compteRendu, declaration, foundTechnicalIssue);
        clearConsoByDeclaration(compteRendu, declaration.get_id());
      }

      declarations = declarationService.findDeclarationsByNomFichierOrigine(input);
      iterator++;
    }

    clearConsoByFile(compteRendu, input);

    return foundTechnicalIssue;
  }

  @NewSpan
  public boolean processDeclaration(
      String input,
      CompteRenduPurgeRdo compteRendu,
      Declaration declaration,
      boolean foundTechnicalIssue) {
    String numeroPersonne = declaration.getBeneficiaire().getNumeroPersonne();
    String benefKey = benefService.calculateKey(declaration.getIdDeclarant(), numeroPersonne);

    log.debug("Lecture du beneficiaire en base par la clé " + benefKey);
    BenefAIV5 beneficiaire = benefService.getBeneficiaryByKey(benefKey);

    // Si le bénéficiaire a été trouvé en base...
    if (beneficiaire != null) {
      // Mise à jour des contrats TP du bénéficiaire
      foundTechnicalIssue =
          manageContratsTPDelete(
              compteRendu, beneficiaire, declaration.getContrat().getNumero(), input);
    }
    // On supprime quand même la déclaration et sa trace si le bénéficiaire associé
    // n'a pas été trouvé
    else {
      deleteDeclaration(compteRendu, declaration.get_id());
    }
    return foundTechnicalIssue;
  }

  private boolean processHTPByFileName(String input, CompteRenduPurgeRdo compteRendu) {
    boolean foundTechnicalIssue = false;
    int iterator = 0;
    List<ContratAIV6Light> servicePrestations =
        servicePrestationService.getContratsLightByFileName(input);

    while (!CollectionUtils.isEmpty(servicePrestations)) {
      log.info(
          "Lecture de service prestations en base pour le nom de fichier {} - {} traites",
          input,
          batchSize * iterator);
      for (ContratAIV6Light servicePrestation : servicePrestations) {
        foundTechnicalIssue =
            processBenefsServicePrestation(servicePrestation, compteRendu, input)
                || foundTechnicalIssue;
      }

      servicePrestations = servicePrestationService.getContratsLightByFileName(input);
      iterator++;
    }

    if (!foundTechnicalIssue) {
      log.debug("Suppression des traces service prestations");
      compteRendu.addTracesServicePrestationPurges(
          traceService.deleteServicePrestationTracesByFileName(input));
    }

    return foundTechnicalIssue;
  }

  private boolean processByAmc(String input, CompteRenduPurgeRdo compteRendu) {
    log.info("Purge de l'amc {}", input);

    log.debug("Suppression des beneficiaires dans ElasticSearch");
    if (testDeleteBenefES(input)) return true;

    // Benef
    log.debug("Suppression des traces assuré");
    compteRendu.addTracesInsuredPurges(traceService.deleteInsuredDataTracesByAmc(input));

    log.debug("Suppression des traces beneficiaire");
    compteRendu.addTracesBenefPurges(traceService.deleteBeneficiaryTracesByAmc(input));

    log.debug("Suppression des beneficiaires");
    compteRendu.addBenefsPurges(benefService.deleteBeneficiariesByAmc(input));

    // HTP
    log.debug("Suppression des traces service prestation");
    compteRendu.addTracesServicePrestationPurges(
        traceService.deleteServicePrestationTracesByAmc(input));

    log.debug("Suppression des service prestations");
    compteRendu.addServicePrestationPurges(servicePrestationService.deleteContratsByAmc(input));

    // TP
    log.debug("Suppression des triggers");
    compteRendu.addTriggersPurges(triggerService.deleteTriggerByAmc(input));

    log.debug("Suppression des triggered beneficiaries");
    compteRendu.addTriggeredBeneficiariesPurges(
        triggerService.deleteTriggeredBeneficiaryByAmc(input));

    log.debug("Suppression des contrats");
    compteRendu.addContratsPurges(contractService.deleteContractByAmc(input));

    log.debug("Suppression des traces declaration");
    compteRendu.addTracesDeclarationPurges(traceService.deleteDeclarationTracesByAmc(input));

    log.debug("Suppression des declarations");
    compteRendu.addDeclarationsPurges(declarationService.deleteDeclarationByAmc(input));

    log.debug("Suppression des sasContrat");
    compteRendu.addSasContratsPurges(sasContratService.deleteByAmc(input));

    log.debug("Suppression des données des parametrages de carte TP");
    compteRendu.addParametrageCarteTPPurges(parametrageCarteTPService.deleteByAmc(input));

    log.debug("Suppression des données des référentiels de parametrages de carte TP");
    compteRendu.addReferentielParametrageCarteTPPurges(
        referentielParametrageCarteTPService.deleteByAmc(input));

    log.debug("Suppression des données des rdoServicePrestation");
    compteRendu.addRDOServicePrestationPurges(rdoServicePrestationService.deleteByAMC(input));

    // Historique
    deleteHistory();

    clearConsoByAMC(compteRendu, input);

    return false;
  }

  private boolean testDeleteBenefES(String input) {
    try {
      log.debug("Delete benef elastic");
      benefElasticService.deleteBulk(input);
    } catch (Exception e) {
      try {
        log.debug("Retry delete benef elastic");
        benefElasticService.deleteBulk(input);
      } catch (Exception ex) {
        log.error(
            String.format("Error while calling ElasticSearch : code %s", ex.getMessage()), ex);
        log.error(String.format("Exception type : %s", ex.getClass().toString()));
        return true;
      }
    }
    return false;
  }

  // Returns a boolean stating if an error occurred while processing beneficiary
  @NewSpan
  public boolean processBenefsServicePrestation(
      ContratAIV6Light servicePrestation, CompteRenduPurgeRdo compteRendu, String fileName) {
    log.debug("Traitement des beneficiaires");
    boolean errorFound = false;
    List<String> listNumeroPersonne = servicePrestation.getListNumeroPersonne();
    String idServicePrestation = servicePrestation.getId();

    // Pour chaque bénéficiaire...
    for (String numeroPersonne : listNumeroPersonne) {
      String benefKey =
          benefService.calculateKey(servicePrestation.getIdDeclarant(), numeroPersonne);

      log.debug("Lecture du beneficiaire en base par la clé " + benefKey);
      BenefAIV5 beneficiaire = benefService.getBeneficiaryByKey(benefKey);

      // Si le bénéficiaire a été trouvé en base...
      if (beneficiaire != null) {
        // Mise a jour des contrats HTP du bénéficiaire
        List<ContratV5> contratsBenefFiltres =
            beneficiaire.getContrats().stream()
                .filter(
                    contrat -> !contrat.getNumeroContrat().equals(servicePrestation.getNumero()))
                .toList();
        beneficiaire.setContrats(contratsBenefFiltres);
        beneficiaire.setSocietesEmettrices(
            benefInfos.handlePeriodesSocieteEmettriceForBenef(contratsBenefFiltres));

        // Si le bénéficiaire n'a plus de contrats, on le supprime de la base et d'ES
        if (CollectionUtils.isEmpty(beneficiaire.getContrats())) {
          errorFound = purgeBenef(compteRendu, beneficiaire);
        }
        // Si il reste au moins un contrat au benef, on le met a jour
        else {
          updateBenefHTP(compteRendu, fileName, beneficiaire);
        }
      }
    }

    if (!errorFound) {
      manageTriggersDelete(compteRendu, idServicePrestation);

      log.debug(String.format("Suppression du service prestation %s", idServicePrestation));
      compteRendu.addServicePrestationPurges(
          servicePrestationService.deleteContratById(idServicePrestation));

      log.debug(String.format("Suppression du sas contrat %s", idServicePrestation));
      compteRendu.addSasContratsPurges(
          sasContratService.deleteByServicePrestationId(idServicePrestation));
    }

    return errorFound;
  }

  private void updateBenefHTP(
      CompteRenduPurgeRdo compteRendu, String fileName, BenefAIV5 beneficiaire) {
    log.debug("Modification du beneficiaire");

    IdentiteContrat identiteBenef = beneficiaire.getIdentite();

    // Si le bénéficiaire n'a pas d'autre contrat HTP...
    if (!servicePrestationService.anotherContractExists(
        beneficiaire.getAmc().getIdDeclarant(), identiteBenef.getNumeroPersonne(), fileName)) {
      List<String> servicesBenefFiltres =
          beneficiaire.getServices().stream()
              .filter(service -> !service.equals("ServicePrestation"))
              .toList();
      beneficiaire.setServices(servicesBenefFiltres);
    }

    template.save(beneficiaire, Constants.BENEFICIAIRE_COLLECTION_NAME);
    compteRendu.addBenefsModifiesHTP(1);
  }

  private void updateBenefTP(CompteRenduPurgeRdo compteRendu, BenefAIV5 beneficiaire) {
    log.debug("Modification du beneficiaire");

    IdentiteContrat identiteBenef = beneficiaire.getIdentite();

    // Si le bénéficiaire n'a pas d'autre contrat TP...
    if (!declarationService.anotherContractExists(
        beneficiaire.getAmc().getIdDeclarant(),
        identiteBenef.getNumeroPersonne(),
        identiteBenef.getDateNaissance(),
        identiteBenef.getRangNaissance())) {
      List<String> servicesBenefFiltres =
          beneficiaire.getServices().stream()
              .filter(service -> !service.equals(Constants.SERVICE_TP))
              .toList();
      beneficiaire.setServices(servicesBenefFiltres);
    }

    template.save(beneficiaire, Constants.BENEFICIAIRE_COLLECTION_NAME);
    compteRendu.addBenefsModifiesTP(1);
  }

  private boolean purgeBenef(CompteRenduPurgeRdo compteRendu, BenefAIV5 beneficiaire) {
    log.debug("Suppression du beneficiaire");
    try {
      benefElasticService.delete(beneficiaire.getKey());
    } catch (Exception e) {
      log.error(String.format("Error while calling ElasticSearch : code %s", e.getMessage()), e);
      return true;
    }

    log.debug("Suppression des traces du beneficiaire");
    compteRendu.addTracesBenefPurges(
        traceService.deleteBeneficiaryTracesByOriginId(beneficiaire.getId()));

    log.debug("Suppression du bénéficiaire");
    compteRendu.addBenefsPurges(benefService.deleteBeneficiaryById(beneficiaire.getId()));
    return false;
  }

  private boolean manageContratsTPDelete(
      CompteRenduPurgeRdo compteRendu,
      BenefAIV5 beneficiaire,
      String numeroContrat,
      String nomFichier) {
    boolean errorFound = false;

    // Récupérer les déclarations par la clé unique benef
    log.debug("Lecture des Declarations en base");
    IdentiteContrat identite = beneficiaire.getIdentite();
    List<Declaration> declarations =
        declarationService.findDeclarationsOfBenef(
            beneficiaire.getAmc().getIdDeclarant(),
            numeroContrat,
            identite.getNumeroPersonne(),
            identite.getDateNaissance(),
            identite.getRangNaissance(),
            null,
            null);

    if (!CollectionUtils.isEmpty(declarations)) {
      List<String> declarationsSuppr = new ArrayList<>();

      for (Declaration declaration : declarations) {
        // Si la declaration porte le nom de fichier, on la supprime
        if (nomFichier.equals(declaration.getNomFichierOrigine())) {
          String declarationId = declaration.get_id();

          deleteDeclaration(compteRendu, declarationId);

          declarationsSuppr.add(declaration.getContrat().getNumero());
        }
      }

      // Mise à jour de la liste des contrats du bénéficiaire
      List<ContratV5> contratsBenef = beneficiaire.getContrats();
      List<ContratV5> contratsBenefFiltres =
          contratsBenef.stream()
              .filter(contrat -> !declarationsSuppr.contains(contrat.getNumeroContrat()))
              .toList();
      beneficiaire.setContrats(contratsBenefFiltres);

      // Si le bénéficiaire n'a plus de contrats, on le supprime de la base et d'ES
      if (CollectionUtils.isEmpty(beneficiaire.getContrats())) {
        errorFound = purgeBenef(compteRendu, beneficiaire);
      }
      // Si il reste au moins un contrat au benef, on le met a jour
      else {
        updateBenefTP(compteRendu, beneficiaire);
      }

      // Mise à jour des contrats consolidés
      manageContratsUpdate(
          compteRendu,
          beneficiaire.getAmc().getIdDeclarant(),
          numeroContrat,
          beneficiaire.getNumeroAdherent(),
          identite.getNumeroPersonne());

      log.debug("Re-consolidation des contrats TP");
      IdentiteContrat identiteBenef = beneficiaire.getIdentite();
      long replayedDeclarations =
          declarationService.replayDeclarations(
              beneficiaire.getAmc().getIdDeclarant(),
              identiteBenef.getNumeroPersonne(),
              identiteBenef.getDateNaissance(),
              identiteBenef.getRangNaissance());
      log.debug(
          String.format(
              "%s declarations rejouees pour le benef %s",
              replayedDeclarations, beneficiaire.getKey()));
    }

    return errorFound;
  }

  private void deleteDeclaration(CompteRenduPurgeRdo compteRendu, String declarationId) {
    // Supprimer les declarationTraces par originId
    log.debug(String.format("Suppression des traces de la declaration %s", declarationId));
    compteRendu.addTracesDeclarationPurges(
        traceService.deleteDeclarationTracesByOriginId(declarationId));

    // Supprimer la declaration
    log.debug(String.format("Suppression de la declaration %s", declarationId));
    compteRendu.addDeclarationsPurges(declarationService.deleteDeclarationById(declarationId));
  }

  private void manageContratsUpdate(
      CompteRenduPurgeRdo compteRendu,
      String idDeclarant,
      String numeroContrat,
      String numeroAdherent,
      String numeroPersonne) {
    log.debug("Suppression du beneficiaire du contrat consolidé");
    int modifContrat =
        contractService.deleteBenefFromContract(
            idDeclarant, numeroContrat, numeroAdherent, numeroPersonne);

    // Si une modification a été effectuée sur un contrat...
    if (modifContrat == 1) {
      compteRendu.addContratsModifies(1);
    }
    // Si le contrat a été supprimé...
    else if (modifContrat == 0) {
      compteRendu.addContratsPurges(1);
    }
    // Si aucun contrat n'a été trouvé en base...
    // Aucun traitement
  }

  private void manageTriggersDelete(CompteRenduPurgeRdo compteRendu, String idServicePrestation) {
    // Récupérer les triggeredBenefs par l'idServicePrestation
    log.debug("Lecture des TriggeredBeneficiaries en base");
    List<TriggeredBeneficiary> triggeredBenefs =
        triggerService.getTriggeredBeneficiariesByServicePrestation(idServicePrestation);

    if (!CollectionUtils.isEmpty(triggeredBenefs)) {
      // Pour chaque triggeredBenefs, constituer un set d'idTriggers
      Set<String> idTriggers = new HashSet<>();

      for (TriggeredBeneficiary triggeredBenef : triggeredBenefs) {
        idTriggers.add(triggeredBenef.getIdTrigger());
      }

      // Delete les Triggers en bouclant
      for (String idTrigger : idTriggers) {
        log.debug(String.format("Suppression du Trigger %s", idTrigger));
        compteRendu.addTriggersPurges(triggerService.deleteTriggerById(idTrigger));
      }

      // Delete les TriggeredBenefs en bouclant
      for (TriggeredBeneficiary triggeredBenef : triggeredBenefs) {
        log.debug(String.format("Suppression du TriggeredBenef %s", triggeredBenef.getId()));
        compteRendu.addTriggeredBeneficiariesPurges(
            triggerService.deleteTriggeredBeneficiaryById(triggeredBenef.getId()));
      }
    }
  }

  private void deleteHistory() {
    log.debug("Suppression de l'historique de consultation des bénéficiaires");
    restBeneficiaireService.deleteHistory();

    log.debug("Suppression de l'historique de consultation des déclarations");
    declarationService.deleteHistory();
  }

  private void clearConsoByAMC(CompteRenduPurgeRdo compteRendu, String amc) {
    showLogs("Suppression des déclarations consolidées carte démat");
    compteRendu.addDeclarationConsolidePurges(declarationConsolideDao.deleteByAMC(amc));

    showLogs("Suppression des traces");
    compteRendu.addTracesPurges(traceService.deleteTracesByAMC(amc));

    showLogs("Suppression des cartes dématérialisées");
    compteRendu.addCarteDematPurges(carteDematDao.deleteByAMC(amc));

    showLogs("Suppression des cartes papiers");
    compteRendu.addCartePapierPurges(cartePapierDao.deleteByAMC(amc));

    showLogs("Suppression des tracesFlux");
    compteRendu.addTraceFluxPurges(fluxDao.deleteByAMC(amc));

    showLogs("Suppression des historiques executions");
    compteRendu.addHistoriqueExecutionPurges(historiqueExecutionsDao.deleteByAMC(amc));

    showLogs("Suppression des historiques executions renouvellement");
    compteRendu.addHistoriqueExecutionRenouvellementPurges(
        historiqueExecutionsRenouvellementDao.deleteAll());
  }

  private void clearConsoByFile(CompteRenduPurgeRdo compteRendu, String file) {
    showLogs("Suppression des traces");
    compteRendu.addTracesPurges(traceService.deleteTracesByFileName(file));

    showLogs("Suppression des tracesFlux");
    compteRendu.addTraceFluxPurges(fluxDao.deleteByFileName(file));

    showLogs("Suppression des historiques executions renouvellement");
    compteRendu.addHistoriqueExecutionRenouvellementPurges(
        historiqueExecutionsRenouvellementDao.deleteAll());
  }

  private void clearConsoByDeclaration(CompteRenduPurgeRdo compteRendu, String declaration) {
    showLogs("Suppression des déclarations consolidées carte démat");
    compteRendu.addDeclarationConsolidePurges(
        declarationConsolideDao.deleteByDeclaration(declaration));

    showLogs("Suppression des cartes dématérialisées");
    compteRendu.addCarteDematPurges(carteDematDao.deleteByDeclaration(declaration));

    showLogs("Suppression des cartes papiers");
    compteRendu.addCartePapierPurges(cartePapierDao.deleteByDeclaration(declaration));
  }
}
