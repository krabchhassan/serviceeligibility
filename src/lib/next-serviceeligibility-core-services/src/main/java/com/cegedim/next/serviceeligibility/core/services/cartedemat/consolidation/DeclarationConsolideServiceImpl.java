package com.cegedim.next.serviceeligibility.core.services.cartedemat.consolidation;

import com.cegedim.beyond.schemas.Event;
import com.cegedim.next.serviceeligibility.core.dao.DeclarationConsolideDao;
import com.cegedim.next.serviceeligibility.core.job.batch.BulkActions;
import com.cegedim.next.serviceeligibility.core.job.batch.Rejection;
import com.cegedim.next.serviceeligibility.core.job.batch.TraceConsolidation;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.DomaineConvention;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggerEmitter;
import com.cegedim.next.serviceeligibility.core.model.entity.*;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ConstantesRejetsConsolidations;
import com.cegedim.next.serviceeligibility.core.model.job.DataForJob620;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarationService;
import com.cegedim.next.serviceeligibility.core.services.bdd.DomaineService;
import com.cegedim.next.serviceeligibility.core.services.cartedemat.carte.ProcessorCartesService;
import com.cegedim.next.serviceeligibility.core.services.cartedemat.invalidation.InvalidationCarteService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.trace.TraceConsolidationService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DeclarationConsolideUtils;
import com.cegedim.next.serviceeligibility.core.utils.DomaineDroitBufferConsolidationUtils;
import com.mongodb.client.ClientSession;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeclarationConsolideServiceImpl implements DeclarationConsolideService {

  private final DeclarationConsolideDao declarationConsolideDao;
  private final EventService eventService;
  private final TraceConsolidationService traceConsolidationService;
  private final DeclarationService declarationService;
  private final DomaineService domaineService;
  private final InvalidationCarteService invalidationCarteService;
  private final ProcessorCartesService cartesService;

  public DeclarationConsolideServiceImpl(
      @Autowired DeclarationConsolideDao declarationConsolideDao,
      @Autowired(required = false) EventService eventService,
      TraceConsolidationService traceConsolidationService,
      DeclarationService declarationService,
      DomaineService domaineService,
      InvalidationCarteService invalidationCarteService,
      ProcessorCartesService cartesService) {
    this.declarationConsolideDao = declarationConsolideDao;
    this.eventService = eventService;
    this.traceConsolidationService = traceConsolidationService;
    this.declarationService = declarationService;
    this.domaineService = domaineService;
    this.invalidationCarteService = invalidationCarteService;
    this.cartesService = cartesService;
  }

  public Collection<DeclarationConsolide> saveDeclarationConsolides(
      List<DeclarationConsolide> declarationConsolides,
      HistoriqueExecutions620 histo,
      String codeClient) {
    // TODO voir si besoin de transactionnel en cas d'erreurs
    List<TraceConsolidation> traces = new ArrayList<>();
    List<DeclarationConsolide> declarationConsolideToSave = new ArrayList<>();
    for (DeclarationConsolide declarationConsolide : declarationConsolides) {
      log.debug("getAMC_contrat : {} ", declarationConsolide.getAMC_contrat());

      if (declarationConsolide.isDeclarationValide()) {
        histo.incNbConsolidationCree(1);
        declarationConsolideToSave.add(declarationConsolide);
      } else {
        histo.incNbDeclarationErreur(1);
      }
    }
    Collection<DeclarationConsolide> saved =
        declarationConsolideDao.insertAll(declarationConsolideToSave);

    for (DeclarationConsolide declaConsoSaved : saved) {
      traces.addAll(traceConsolidationService.generateValidTraces(declaConsoSaved, codeClient));

      eventService.sendObservabilityEventConsolidationDeclaration(declaConsoSaved);
    }

    traceConsolidationService.insertTraceList(traces);

    return saved;
  }

  public List<DeclarationConsolide>
      removeDeclarationConsolideeAndGetUpdateDeclarationConsolideOnOtherBenefs(
          List<DeclarationConsolide> existingDeclarationConsos,
          Set<String> numeroPersonnes,
          String dateDebutMinimum,
          String dateFinMinimum,
          String identifiant,
          BulkActions bulkActions) {
    List<DeclarationConsolide> declarationConsolidesToChange = new ArrayList<>();
    List<DeclarationConsolide> toDelete = new ArrayList<>();
    // faire la recherche sans numero de personne
    // et mettre à jour les autres déclarations du contrat avec l'identifiant

    for (DeclarationConsolide declarationConsolidee : existingDeclarationConsos) {
      if (declarationConsolidee.getPeriodeFin() != null
          && dateDebutMinimum != null
          && dateFinMinimum != null
          && (declarationConsolidee.getPeriodeFin().compareTo(dateFinMinimum) >= 0
              || declarationConsolidee.getPeriodeFin().compareTo(dateDebutMinimum) > 0)) {
        if (numeroPersonnes.contains(declarationConsolidee.getBeneficiaire().getNumeroPersonne())) {
          toDelete.add(declarationConsolidee);
        } else {
          declarationConsolidee.setIdentifiant(identifiant);
          declarationConsolidesToChange.add(declarationConsolidee);
        }
      }
    }
    bulkActions.delete(toDelete);
    bulkActions.update(declarationConsolidesToChange);
    return declarationConsolidesToChange;
  }

  @Override
  public void deleteAll() {
    declarationConsolideDao.deleteAll();
  }

  /**
   * gère tout ce qu'il doit se passer par contrat : suppression declarationConso mise à jour
   * declarationConso création carte invalidation carte création cartePapier
   */
  @Override
  public void processGroupedDeclarationsByContract(
      DataForJob620 dataForJob620,
      BulkActions bulkActions,
      Declarant declarant,
      List<Declaration> declarations,
      String numContrat,
      List<DeclarationConsolide> existingDeclarationConsos,
      List<CarteDemat> existingCarteDemats) {
    bulkActions.getHistoriqueExecution().incNbDeclarationLue(declarations.size());
    List<Periode> periodeSuspensionList =
        DeclarationConsolideUtils.getPeriodeSuspensionList(declarations);
    String dateDebutMinimum = DeclarationConsolideUtils.getDateDebutMinimum(declarations);
    String dateFinMinimum =
        DeclarationConsolideUtils.getDateFinMinimum(declarations, dataForJob620.getClientType());
    log.debug(
        "Numero contrat : {} - date debut min {} - date fin min {}",
        numContrat,
        dateDebutMinimum,
        dateFinMinimum);

    BulkActions bulkActionsContract = new BulkActions();
    bulkActionsContract.setContexte(getContexte(dataForJob620, declarations));

    Map<String, List<Declaration>> groupByPersonne =
        declarations.stream()
            .collect(Collectors.groupingBy(decl -> decl.getBeneficiaire().getNumeroPersonne()));
    // la liste des anciennes déclarationConsolidés à supprimer / mettre à jour
    List<DeclarationConsolide> declarationConsolidesToCheckInvalidation =
        removeDeclarationConsolideeAndGetUpdateDeclarationConsolideOnOtherBenefs(
            existingDeclarationConsos,
            groupByPersonne.keySet(),
            dateDebutMinimum,
            dateFinMinimum,
            dataForJob620.getIdentifiant(),
            bulkActionsContract);
    String numPersonError = null; // pour gérer le rejet C17
    List<String> servicesContrat = new ArrayList<>(); // Pour les invalidation
    Map<Declaration, List<String>> selectedDeclarationsAndServices = new HashMap<>();
    // boucle par personne du contrat
    for (Map.Entry<String, List<Declaration>> declaByPersonne : groupByPersonne.entrySet()) {
      List<String> servicesDeclaration =
          DeclarationConsolideUtils.consolidateServicesDeclarations(
              declaByPersonne.getValue(), declarant);

      // Skip this consolidation if servicesDeclaration is empty
      if (CollectionUtils.isEmpty(servicesDeclaration)) {
        bulkActionsContract
            .getHistoriqueExecution()
            .incNbDeclarationIgnoree(declaByPersonne.getValue().size());
        log.debug("Services list is empty, skipping to next consolidation");
        continue;
      }

      servicesContrat.addAll(servicesDeclaration);

      List<Declaration> selectedDeclaration =
          declarationService.filterLatestDeclarationsForConso(
              declaByPersonne.getValue(), dataForJob620.getToday(), dataForJob620.getClientType());

      // on met à jour les déclarations ignorés entre celle de la personne et celle
      // selectionnés
      bulkActionsContract
          .getHistoriqueExecution()
          .incNbDeclarationIgnoree(declaByPersonne.getValue().size() - selectedDeclaration.size());
      if (CollectionUtils.isNotEmpty(selectedDeclaration) && numPersonError == null) {
        selectedDeclaration.forEach(
            declaration -> selectedDeclarationsAndServices.put(declaration, servicesDeclaration));
        boolean toBeIgnored = true;
        if (servicesDeclaration.contains(Constants.CARTE_DEMATERIALISEE)) {
          List<CarteDemat> carteDematsToInvalid =
              invalidationCarteService.invalidationCartes(
                  existingCarteDemats,
                  selectedDeclaration,
                  Declaration::getDomaineDroits,
                  dataForJob620.getClientType(),
                  periodeSuspensionList);
          if (CollectionUtils.isNotEmpty(carteDematsToInvalid)) {
            toBeIgnored = false;
          }
          bulkActionsContract.invalid(carteDematsToInvalid);
        }

        if (CollectionUtils.isNotEmpty(declarationConsolidesToCheckInvalidation)
            && servicesDeclaration.contains(Constants.CARTE_DEMATERIALISEE)) {
          List<CarteDemat> carteDematsToInvalid =
              invalidationCarteService.invalidationCartes(
                  existingCarteDemats,
                  declarationConsolidesToCheckInvalidation,
                  DeclarationConsolide::getDomaineDroits,
                  dataForJob620.getClientType(),
                  periodeSuspensionList);
          if (CollectionUtils.isNotEmpty(carteDematsToInvalid)) {
            toBeIgnored = false;
          }
          bulkActionsContract.invalid(carteDematsToInvalid);
        }
        bulkActionsContract.save(
            generateDeclarationConsolides(
                selectedDeclaration,
                dataForJob620,
                servicesDeclaration,
                bulkActionsContract,
                toBeIgnored));
        if (CollectionUtils.isNotEmpty(bulkActionsContract.getRejections())) {
          numPersonError = declaByPersonne.getKey();
        }
      }
    }
    //
    if (numPersonError == null) {
      bulkActions.save(bulkActionsContract.getToSave());
      bulkActions.delete(bulkActionsContract.getToDelete());
      bulkActions.update(bulkActionsContract.getToUpdate());

      // On set le context du dernier contrat en cours pour la creation des cartes
      // demat/papier. A chaque contrat la donnee sera donc ecrasee pour les
      // traitements suivant
      bulkActions.setContexte(bulkActionsContract.getContexte());

      List<DeclarationConsolide> declarationConsolides = new ArrayList<>();
      declarationConsolides.addAll(bulkActionsContract.getToSave());
      declarationConsolides.addAll(bulkActionsContract.getToUpdate());

      cartesService.createCartesDematAndPaper(
          dataForJob620,
          declarant,
          declarationConsolides,
          bulkActions,
          extractLastSuspensionPeriod(
              bulkActionsContract.getToDelete(),
              declarationConsolides,
              dataForJob620.getClientType()));

      if (CollectionUtils.isNotEmpty(bulkActionsContract.getRejections())) {
        bulkActions.getRejections().addAll(bulkActionsContract.getRejections());
      } else if (servicesContrat.contains(Constants.CARTE_DEMATERIALISEE)) {
        // Si il n'y a pas eu de rejet lors de la création des cartes et que les
        // services voulus contiennent carte demat
        bulkActions.invalid(bulkActionsContract.getToInvalid());
      }
    } else {
      // mettre C17 sur les autres
      bulkActions.getRejections().addAll(bulkActionsContract.getRejections());
      for (Map.Entry<Declaration, List<String>> declarationListEntry :
          selectedDeclarationsAndServices.entrySet()) {
        Declaration declaration = declarationListEntry.getKey();
        for (String codeService : declarationListEntry.getValue()) {
          if (!declaration.getBeneficiaire().getNumeroPersonne().equals(numPersonError)) {
            Rejection rejet =
                new Rejection(
                    ConstantesRejetsConsolidations.REJET_C17.toString(),
                    declarationListEntry.getKey(),
                    dataForJob620.getToday(),
                    codeService);
            bulkActions.reject(rejet);
          }
        }
      }
    }

    HistoriqueExecutions620 histoContrat = bulkActions.getHistoriqueExecution();
    HistoriqueExecutions620 histoPerso = bulkActionsContract.getHistoriqueExecution();
    histoContrat.incAllFromOther(histoPerso);
  }

  private Periode extractLastSuspensionPeriod(
      List<DeclarationConsolide> existingDeclarationConsos,
      List<DeclarationConsolide> declarationConsolides,
      String clientType) {
    Periode previousSuspensionPeriod = new Periode();

    if (Constants.CLIENT_TYPE_INSURER.equals(clientType)) {
      Optional<DeclarationConsolide> mostRecentWithSuspension =
          existingDeclarationConsos.stream()
              .filter(
                  declaration ->
                      CollectionUtils.isNotEmpty(declaration.getContrat().getPeriodeSuspensions()))
              .max(Comparator.comparing(DeclarationConsolide::getDateCreation));

      if (mostRecentWithSuspension.isPresent()) {
        boolean hasSuspensionInNewDeclaration =
            declarationConsolides.stream()
                .anyMatch(
                    declaration ->
                        CollectionUtils.isNotEmpty(
                            declaration.getContrat().getPeriodeSuspensions()));

        // Si les déclarations existantes ont des suspensions et les nouvelles
        // déclarations n'ont pas de suspensions
        if (!hasSuspensionInNewDeclaration) {
          previousSuspensionPeriod.setDebut(
              mostRecentWithSuspension
                  .get()
                  .getContrat()
                  .getPeriodeSuspensions()
                  .get(0)
                  .getDebut());
          previousSuspensionPeriod.setFin(
              mostRecentWithSuspension.get().getContrat().getPeriodeSuspensions().get(0).getFin());
        }
      }
    }

    return previousSuspensionPeriod;
  }

  /** Cree un liste de {@link DeclarationConsolide} en fonction de la {@link Declaration} */
  private List<DeclarationConsolide> createDeclarationConso(
      Declaration declaration,
      Map<String, Map<String, DomaineDroitBuffer>> grouped,
      DataForJob620 dataForJob620,
      List<String> servicesWanted,
      BulkActions bulkActions) {
    List<DeclarationConsolide> declarationConsolides = new ArrayList<>();
    ConstantesRejetsConsolidations rejectedDeclarationCode = null;

    for (Map<String, DomaineDroitBuffer> byCodeDomaine : grouped.values()) {
      Collection<DomaineDroitBuffer> domainesBufferList = byCodeDomaine.values();
      List<DomaineDroit> domainesEnrichis = null;
      List<DomaineConvention> listeConv = null;
      DomaineDroit produit = null;
      String periodeDebut = "";
      String periodeFin = "";

      // Calcul domaines
      String errorOnDomains = domaineService.validateDomainesConsolides(domainesBufferList);
      boolean declarationValide = errorOnDomains.isEmpty();

      if (declarationValide) {
        domainesEnrichis = domaineService.updateDomainesConsolides(domainesBufferList);
        if (domainesEnrichis != null) {
          listeConv =
              DomaineDroitBufferConsolidationUtils.getListDomaineConvention(domainesBufferList);
          produit = DomaineDroitBufferConsolidationUtils.getProduitBenef(domainesBufferList);
          periodeDebut =
              domainesBufferList.stream()
                  .map(buffer -> buffer.getProduit().getPeriodeDroit().getPeriodeDebut())
                  .min(String::compareTo)
                  .orElse("");
          periodeFin =
              domainesBufferList.stream()
                  .map(buffer -> buffer.getProduit().getPeriodeDroit().getPeriodeFin())
                  .min(String::compareTo)
                  .orElse("");

        } else {
          log.debug("Rejet C13");
          for (String codeService : servicesWanted) {
            bulkActions.reject(
                new Rejection(
                    ConstantesRejetsConsolidations.REJET_C13.toString(),
                    declaration,
                    dataForJob620.getToday(),
                    codeService));
          }
          rejectedDeclarationCode = ConstantesRejetsConsolidations.REJET_C13;
        }
      } else {
        // erreur de rejet remonté de la méthode validateDomainesConsolides.
        log.debug(errorOnDomains);
        for (String codeService : servicesWanted) {
          bulkActions.reject(
              new Rejection(errorOnDomains, declaration, dataForJob620.getToday(), codeService));
        }
        rejectedDeclarationCode =
            ConstantesRejetsConsolidations.findByCode("REJET_" + errorOnDomains.split(";")[0]);
      }

      if (rejectedDeclarationCode == null) {
        try {
          DeclarationConsolide declarationConso =
              DeclarationConsolideUtils.createDeclarationConsolidee(
                  declaration,
                  dataForJob620.getToday(),
                  domainesEnrichis,
                  servicesWanted,
                  listeConv,
                  periodeDebut,
                  periodeFin,
                  declarationValide,
                  produit);
          declarationConso.setIdentifiant(dataForJob620.getIdentifiant());
          declarationConsolides.add(declarationConso);
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          Event event =
              eventService.prepareObservabilityEventConsolidationFailed(
                  declaration, e.getMessage());
          bulkActions.event(event);
        }
      } else {
        Event event =
            eventService.prepareObservabilityEventConsolidationFailed(
                declaration, rejectedDeclarationCode.getMessage());
        bulkActions.event(event);
      }
    }

    return declarationConsolides;
  }

  /**
   * generation des déclarations consolidés
   *
   * @param declarations : liste des déclarations à traiter
   * @param dataForJob620 : donnée transverse du batch 620
   * @param servicesWanted : service voulu (carte demat et/ou papier)
   * @param bulkActions : actions à faire plus tard
   * @param toBeIgnore : sait si l'on doit mettre à jour le nombre de déclarations ignorés dans
   *     l'histoExec
   * @return la liste des déclarations consolidés à créer
   */
  public List<DeclarationConsolide> generateDeclarationConsolides(
      List<Declaration> declarations,
      DataForJob620 dataForJob620,
      List<String> servicesWanted,
      BulkActions bulkActions,
      boolean toBeIgnore) {
    List<DeclarationConsolide> declarationConsolides = new ArrayList<>();
    boolean isInsurer = Constants.CLIENT_TYPE_INSURER.equals(dataForJob620.getClientType());
    for (Declaration declaration : declarations) {
      List<DomaineDroit> domaineDroits =
          domaineService.splitAndFilterDomaineDroits(
              declaration, dataForJob620.getToday(), isInsurer);

      if (domaineDroits == null) {
        if (toBeIgnore) {
          bulkActions.getHistoriqueExecution().incNbDeclarationIgnoree(1);
        }
        continue;
      }

      Map<String, Map<String, DomaineDroitBuffer>> groupedDomainesDroits =
          domaineService.groupeByDateFinAndCode(domaineDroits);

      List<DeclarationConsolide> declarationConsolidesByDeclaration =
          createDeclarationConso(
              declaration, groupedDomainesDroits, dataForJob620, servicesWanted, bulkActions);

      if (declarationConsolidesByDeclaration.isEmpty()) {
        bulkActions.getHistoriqueExecution().incNbDeclarationErreur(1);
      }
      declarationConsolides.addAll(declarationConsolidesByDeclaration);
    }

    return declarationConsolides;
  }

  public Collection<DeclarationConsolide> bulkSaveUpdateDelete(
      BulkActions bulkActions, String codeClient, ClientSession session) {
    Collection<DeclarationConsolide> saved =
        saveDeclarationConsolides(
            bulkActions.getToSave(), bulkActions.getHistoriqueExecution(), codeClient);
    declarationConsolideDao.bulkDeleteUpdate(
        bulkActions.getToDelete(), bulkActions.getToUpdate(), session);
    return saved;
  }

  public List<DeclarationConsolide> getDeclarationsConsolideesByAmcContrats(String amcContracts) {
    return declarationConsolideDao.getDeclarationsConsolideesByAmcContrats(amcContracts);
  }

  /**
   * Recupere le context A (annuel) ou Q (quotidien) en fonction de la topologie du client et des
   * donnees des declarations.
   */
  private String getContexte(DataForJob620 dataForJob620, List<Declaration> declarations) {
    if (Constants.CLIENT_TYPE_INSURER.equals(dataForJob620.getClientType())) {
      for (Declaration declaration : ListUtils.emptyIfNull(declarations)) {
        if (TriggerEmitter.Renewal.toString().equals(declaration.getUserCreation())) {
          return Constants.ANNUEL;
        }
      }
      return Constants.QUOTIDIEN;
    }

    // OTP
    for (Declaration declaration : ListUtils.emptyIfNull(declarations)) {
      for (DomaineDroit dom : ListUtils.emptyIfNull(declaration.getDomaineDroits())) {
        if (dom.getPeriodeDroit() == null
            || StringUtils.isBlank(dom.getPeriodeDroit().getMotifEvenement())
            || Constants.MOTIF_RENOUVELLEMENT.equals(dom.getPeriodeDroit().getMotifEvenement())) {
          return Constants.ANNUEL;
        }
      }
    }
    return Constants.QUOTIDIEN;
  }
}
