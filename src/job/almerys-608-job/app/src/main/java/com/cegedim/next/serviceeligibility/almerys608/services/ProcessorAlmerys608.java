package com.cegedim.next.serviceeligibility.almerys608.services;

import com.cegedim.next.serviceeligibility.almerys608.exceptions.FileNotCreatedException;
import com.cegedim.next.serviceeligibility.almerys608.model.*;
import com.cegedim.next.serviceeligibility.almerys608.model.Contrat;
import com.cegedim.next.serviceeligibility.almerys608.model.InfoEntreprise;
import com.cegedim.next.serviceeligibility.almerys608.utils.EnumTempTable;
import com.cegedim.next.serviceeligibility.core.job.batch.*;
import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduAlmerys608;
import com.cegedim.next.serviceeligibility.core.model.domain.Pilotage;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecution608;
import com.cegedim.next.serviceeligibility.core.model.entity.Transcodage;
import com.cegedim.next.serviceeligibility.core.model.entity.almv3.TmpObject2;
import com.cegedim.next.serviceeligibility.core.model.job.DataForJob608;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarantService;
import com.cegedim.next.serviceeligibility.core.services.common.batch.ARLService;
import com.cegedim.next.serviceeligibility.core.services.common.batch.TalendJob;
import com.cegedim.next.serviceeligibility.core.services.trace.TraceExtractionConsoService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

@Slf4j
@RequiredArgsConstructor
public class ProcessorAlmerys608 extends TalendJob<HistoriqueExecution608, DataForJob608> {

  private final DeclarantService declarantService;
  private final MappersAlmerys mappersAlmerys;
  private final CreationFileService creationFileService;
  private final DomainService domainService;
  private final UtilService utilService;
  private final ProcessorService processorService;
  private final ARLService arlService;
  private final TraceExtractionConsoService traceExtractionConsoService;

  private static final Date DATE_EXEC = new Date();
  private final Map<String, TraceExtractionConso> traceExtractConsoParDeclarationConso =
      new HashMap<>();
  private final List<HistoriqueExecution608> histoPilotages = new ArrayList<>();
  private String fileName;
  private int processResult = -1;

  @Override
  protected String getBatchNumber() {
    return Constants.NUMERO_BATCH_608;
  }

  @Override
  protected String getCollection() {
    return Constants.DECLARATIONS_CONSOLIDEES_ALMERYS;
  }

  @Override
  protected boolean isAmcReprise() {
    return false;
  }

  public int process(DataForJob608 dataForJob608, CompteRenduAlmerys608 compteRendu) {
    newHistoriqueExecutions = new HistoriqueExecution608();
    newHistoriqueExecutions.setDateExecution(DATE_EXEC);
    try {
      processResult = process(dataForJob608);
    } catch (Exception e) {
      log.error("Unexpected error while processing batch");
      log.error(e.getMessage(), e);
    } finally {
      compteRendu.addMetriquePilotage(histoPilotages);
      compteRendu.addTotal(newHistoriqueExecutions);
      log.debug("fin");
    }

    return processResult;
  }

  @Override
  protected void processRecords(DataForJob608 dataForJob) {
    List<Declarant> declarants =
        declarantService.getDeclarantsByCodeService(
            List.of(Constants.ALMV3), dataForJob.getCouloirClient());
    for (Declarant declarant : declarants) {
      buildAlmerysFile(declarant, dataForJob.getCouloirClient());
    }

    for (HistoriqueExecution608 histoPilotage : histoPilotages) {
      newHistoriqueExecutions.incDeclarationsConsolideesAlmerysLues(
          histoPilotage.getNbDeclarationsConsolideesAlmerysLues());
      newHistoriqueExecutions.incContrats(histoPilotage.getNbContrats());
      newHistoriqueExecutions.incMembresContrats(histoPilotage.getNbMembresContrats());
      newHistoriqueExecutions.incNombreContratRejet(histoPilotage.getNbContratsRejetes());
      newHistoriqueExecutions.incNbRejetsNonBloquants(histoPilotage.getNbRejetsNonBloquants());
      newHistoriqueExecutions.incNbRejetsBloquants(histoPilotage.getNbRejetsBloquants());
      newHistoriqueExecutions.incNbFichiersGeneres(histoPilotage.getNbFichiersGeneres());
    }
    newHistoriqueExecutions.log();
    historiqueExecutionsDao.save(newHistoriqueExecutions);
  }

  @Override
  protected void manageAMCReprise(DataForJob608 dataForJob) {
    //
  }

  @Override
  protected void fillLastIdProcessedAndDeclaration(DataForJob608 dataForJob) {
    //
  }

  protected void buildAlmerysFile(Declarant declarant, String couloirClient) {
    List<String> critSecondaireDetailleToExclude = new ArrayList<>();
    for (Pilotage pilotage : declarant.getPilotages()) {
      log.info(
          "debut declarant {}; csd : {}",
          declarant.get_id(),
          pilotage.getCritereRegroupementDetaille());
      HistoriqueExecution608 newHistoPilotage =
          utilService.createHistoriquePilotage(declarant, pilotage, DATE_EXEC, getBatchNumber());
      HistoriqueExecution608 lastPilotageHisto =
          utilService.getLastHistoriquePilotage(declarant, pilotage, getBatchNumber());
      Date dateDerniereExecution = utilService.getDateDernierExecution(pilotage, lastPilotageHisto);
      int nombreDeclarationATraiter =
          utilService.countDeclarationConsolideesAlmerys(
              declarant, pilotage, dateDerniereExecution, critSecondaireDetailleToExclude);

      newHistoPilotage.incDeclarationsConsolideesAlmerysLues(nombreDeclarationATraiter);
      if (nombreDeclarationATraiter > 0) {
        String nameTmpCollection1 = utilService.getNameTmpCollection(couloirClient, 1);
        String nameTmpCollection2 = utilService.getNameTmpCollection(couloirClient, 2);
        // clear tmp1 & 2
        clearTmpTables(nameTmpCollection1, nameTmpCollection2);
        utilService.createTmpCollection1(
            declarant,
            pilotage,
            critSecondaireDetailleToExclude,
            dateDerniereExecution,
            nameTmpCollection1);
        utilService.createTmpCollection2(declarant, nameTmpCollection1, nameTmpCollection2);
        Map<String, String> tempTables = utilService.initTempCollectionName(declarant, pilotage);
        domainService.clearTempTables(tempTables);
        utilService.createIndexOnCollection(tempTables);

        final List<Transcodage> domaineDroitsTranscoList =
            utilService.getTranscodage(pilotage.getCodeService(), "Domaine_Droits");

        final List<Transcodage> lienJuridiqueTranscoList =
            utilService.getTranscodage(pilotage.getCodeService(), "Lien_Juridique");

        final List<Transcodage> typeBeneficiaireTranscoList =
            utilService.getTranscodage(pilotage.getCodeService(), "Type_beneficiaire");

        final List<Transcodage> codeMouvementTranscoList =
            utilService.getTranscodage(pilotage.getCodeService(), "Code_Mouvement");

        final List<Transcodage> modePaiementTranscoList =
            utilService.getTranscodage(pilotage.getCodeService(), "Mode_Paiement");

        List<ProduitsAlmerysExclus> produitsAlmerysExclus =
            utilService.getProduitsAlmerysExclus(
                declarant.get_id(), pilotage.getCritereRegroupementDetaille());

        BulkList<BulkObject> bulkList = new BulkList<>();
        bulkList.setTempTables(tempTables);

        BulkAndList bulkAndList =
            new BulkAndList(
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new HashMap<>(),
                new ArrayList<>(),
                new HashMap<>(),
                new ArrayList<>(),
                bulkList);
        AtomicBoolean isHtp = new AtomicBoolean(false);
        Stream<TmpObject2> tmpDeclarations = utilService.getAllForSouscripteur(nameTmpCollection2);
        AtomicReference<String> currentNumeroContrat = new AtomicReference<>();
        tmpDeclarations.forEach(
            tmpObject2 -> {
              isHtp.set(UtilService.isHTP(tmpObject2));
              // on surcharge le numeroPersonne avec celui calculé pour plus s'embeter ensuite
              tmpObject2
                  .getBeneficiaire()
                  .setNumeroPersonne(
                      Objects.requireNonNullElse(
                          tmpObject2.getBeneficiaire().getRefExternePersonne(),
                          tmpObject2.getBeneficiaire().getNumeroPersonne()));
              currentNumeroContrat.set(
                  getCurrentNumeroContrat(
                      newHistoPilotage,
                      lienJuridiqueTranscoList,
                      bulkAndList,
                      isHtp.get(),
                      currentNumeroContrat.get(),
                      tmpObject2));
              setInfoSouscripteur(
                  newHistoPilotage,
                  dateDerniereExecution,
                  typeBeneficiaireTranscoList,
                  codeMouvementTranscoList,
                  bulkAndList,
                  currentNumeroContrat.get(),
                  tmpObject2);

              // service TP
              mappersAlmerys
                  .mapperAlmerysServiceTP()
                  .mapServiceTP(tmpObject2, lastPilotageHisto, bulkAndList.serviceTPsContrat());

              // Carences
              mappersAlmerys
                  .mapperAlmerysCarence()
                  .mapCarences(tmpObject2, bulkAndList.carencesList(), domaineDroitsTranscoList);

              // Produits
              mappersAlmerys
                  .mapperAlmerysProduit()
                  .mapProduitStep1(
                      tmpObject2,
                      bulkAndList.produitsToCheckList(),
                      produitsAlmerysExclus,
                      bulkAndList.rejetNonBloquantsList(),
                      bulkAndList.rejetsProduitsExclusList());

              // Traces extraction conso
              List<TraceExtractionConso> traces =
                  traceExtractionConsoService.generateExtractionTraceAlmerys(tmpObject2, pilotage);
              traces.forEach(
                  trace ->
                      traceExtractConsoParDeclarationConso.put(
                          trace.getIdDeclarationConsolidee(), trace));
            });
        // fin boucle (apres derniere déclaration)
        stopWatch.start();
        processorService.actionFinBoucle1(
            bulkAndList,
            lienJuridiqueTranscoList,
            currentNumeroContrat.get(),
            true,
            newHistoPilotage,
            isHtp.get());
        registerTime("action fin boucle 1");

        actionBoucle2(
            pilotage,
            nameTmpCollection2,
            bulkAndList,
            modePaiementTranscoList,
            tempTables,
            newHistoPilotage);

        stopWatch.start();
        log.debug(
            "Debut creation fichier declarant emetteur csd {} {} {}",
            declarant.get_id(),
            pilotage.getCaracteristique().getNumEmetteur(),
            pilotage.getCritereRegroupementDetaille());
        try {
          fileName =
              creationFileService.fillFile(declarant.get_id(), pilotage, DATE_EXEC, tempTables);
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          processResult = -1;
        }

        if (fileName == null) {
          throw new FileNotCreatedException(pilotage);
        } else {
          newHistoPilotage.incNbFichiersGeneres(1);
        }
        log.debug(
            "fin creation fichier declarant emetteur csd {} {} {}",
            declarant.get_id(),
            pilotage.getCaracteristique().getNumEmetteur(),
            pilotage.getCritereRegroupementDetaille());

        registerTime("creation du fichier");
        log.debug("debut gestion des traces et ARL");
        // ARL
        List<Rejection> rejections = new ArrayList<>();
        List<Rejet> rejetsBloquants =
            processorService.getAll(
                utilService.getTempCollectionName(
                    EnumTempTable.REJET.getTableName(),
                    declarant.get_id(),
                    pilotage.getCritereRegroupementDetaille()),
                Rejet.class);
        int nbRejetBloquant = rejetsBloquants.size();
        Map<String, Long> counted =
            rejetsBloquants.stream()
                .collect(Collectors.groupingBy(Rejet::getNumContrat, Collectors.counting()));
        newHistoPilotage.setNbContratsRejetes(counted.size());
        newHistoPilotage.incNombreContratRejet(counted.size());
        List<Rejet> rejets = new ArrayList<>(rejetsBloquants);
        rejets.addAll(
            processorService.getAll(
                utilService.getTempCollectionName(
                    EnumTempTable.REJET_NON_BLOQUANT.getTableName(),
                    declarant.get_id(),
                    pilotage.getCritereRegroupementDetaille()),
                Rejet.class));
        int nbRejetNonBloquant = rejets.size() - nbRejetBloquant;

        Set<String> idDeclarationsAvecRejet =
            rejets.stream().map(Rejet::getIdDeclarationConsolidee).collect(Collectors.toSet());

        // Mise à jour des traces
        updateTraces(
            declarant, pilotage, rejections, rejetsBloquants, rejets, idDeclarationsAvecRejet);
        log.debug("fin gestion des traces et ARL");
        newHistoPilotage.setNbRejetsBloquants(nbRejetBloquant);
        newHistoPilotage.setNbRejetsNonBloquants(nbRejetNonBloquant);
        newHistoPilotage.setNbContratsRejetes(
            (int) rejets.stream().map(Rejet::getNumContrat).distinct().count());
        domainService.clearTempTables(tempTables);
        // clear tmp1 & 2
        clearTmpTables(nameTmpCollection1, nameTmpCollection2);
      }
      newHistoPilotage.log();
      historiqueExecutionsDao.save(newHistoPilotage);
      histoPilotages.add(newHistoPilotage);
    }
  }

  private void actionBoucle2(
      Pilotage pilotage,
      String nameTmpCollection2,
      BulkAndList bulkAndList,
      List<Transcodage> modePaiementTranscoList,
      Map<String, String> tempTables,
      HistoriqueExecution608 newHistoPilotage) {
    Stream<TmpObject2> tmpDeclarations = utilService.getAll(nameTmpCollection2);
    Iterator<TmpObject2> iterator = tmpDeclarations.iterator();
    List<String> processedKeys = new ArrayList<>();
    List<BulkObject> membreContratsList;
    List<BulkObject> adresseAdherentList;
    List<BulkObject> infoCentreGestionList = new ArrayList<>();
    String numeroContrat = null;
    while (iterator.hasNext()) {
      TmpObject2 tmpObject2 = iterator.next();
      // on surcharge le numeroPersonne avec celui calculé pour plus s'embeter ensuite
      tmpObject2
          .getBeneficiaire()
          .setNumeroPersonne(
              Objects.requireNonNullElse(
                  tmpObject2.getBeneficiaire().getRefExternePersonne(),
                  tmpObject2.getBeneficiaire().getNumeroPersonne()));
      String currentContrat = UtilService.mapRefNumContrat(tmpObject2);
      log.debug("contrat {}", currentContrat);
      membreContratsList = new ArrayList<>();
      Contrat contrat = mappersAlmerys.mapperAlmerysContrat().mapContrat(tmpObject2, pilotage);
      InfoEntreprise infoEntreprise =
          mappersAlmerys.mapperAlmerysEntreprise().mapEntreprise(tmpObject2, pilotage);
      if (numeroContrat == null || !numeroContrat.equals(currentContrat)) {
        bulkAndList.bulkList().addOne(EnumTempTable.CONTRAT.name(), contrat);
        numeroContrat = currentContrat;
      }
      adresseAdherentList =
          new ArrayList<>(mappersAlmerys.mapperAlmerysAdresse().mapAdressesAdherent(tmpObject2));
      infoCentreGestionList = getInfoCentreGestionList(pilotage, infoCentreGestionList, tmpObject2);

      mappersAlmerys
          .mapperAlmerysMembreContrat()
          .mapMembreContrat(
              tmpObject2,
              processedKeys,
              membreContratsList,
              modePaiementTranscoList,
              domainService.get(
                  Souscripteur.class,
                  tempTables.get(EnumTempTable.SOUSCRIPTEUR.name()),
                  UtilService.mapRefNumContrat(tmpObject2)));
      bulkAndList.bulkList().addMulti(EnumTempTable.MEMBRE_CONTRAT.name(), membreContratsList);

      setInfoEntreprise(bulkAndList, infoEntreprise);
      bulkAndList.bulkList().addMulti(EnumTempTable.ADRESSE_AD.name(), adresseAdherentList);
      bulkAndList.bulkList().addMulti(EnumTempTable.ADRESSE_GE.name(), infoCentreGestionList);
      stopWatch.start();
      domainService.saveBulks(bulkAndList.bulkList(), newHistoPilotage);
      registerTime("sauvegarde du bulk");
    }
    stopWatch.start();
    domainService.saveBulks(bulkAndList.bulkList(), true, newHistoPilotage);
    registerTime("sauvegarde du bulk");
  }

  private static void setInfoEntreprise(BulkAndList bulkAndList, InfoEntreprise infoEntreprise) {
    if (infoEntreprise != null) {
      bulkAndList.bulkList().addOne(EnumTempTable.ENTREPRISE.name(), infoEntreprise);
    }
  }

  private List<BulkObject> getInfoCentreGestionList(
      Pilotage pilotage, List<BulkObject> infoCentreGestionList, TmpObject2 tmpObject2) {
    List<InfoCentreGestion> infoCentreGestions =
        mappersAlmerys.mapperAlmerysAdresse().mapInfoCentreGestions(tmpObject2, pilotage);
    if (!CollectionUtils.isEmpty(infoCentreGestions)) {
      infoCentreGestionList = new ArrayList<>(infoCentreGestions);
    }
    return infoCentreGestionList;
  }

  private String getCurrentNumeroContrat(
      HistoriqueExecution608 newHistoPilotage,
      List<Transcodage> lienJuridiqueTranscoList,
      BulkAndList bulkAndList,
      boolean isHtp,
      String currentNumeroContrat,
      TmpObject2 tmpObject2) {
    if (!UtilService.mapRefNumContrat(tmpObject2).equals(currentNumeroContrat)) {
      if (currentNumeroContrat != null) {
        stopWatch.start();
        processorService.actionFinBoucle1(
            bulkAndList,
            lienJuridiqueTranscoList,
            currentNumeroContrat,
            false,
            newHistoPilotage,
            isHtp);
        registerTime("action fin boucle 1");
      }

      currentNumeroContrat = UtilService.mapRefNumContrat(tmpObject2);
    }
    return currentNumeroContrat;
  }

  private void updateTraces(
      Declarant declarant,
      Pilotage pilotage,
      List<Rejection> rejections,
      List<Rejet> rejetsBloquants,
      List<Rejet> rejets,
      Set<String> idDeclarationsAvecRejet) {
    rejets.forEach(
        rejet -> {
          TraceExtractionConso trace =
              traceExtractConsoParDeclarationConso.get(rejet.getIdDeclarationConsolidee());
          if (trace != null) {
            // Mise à jour directe de la trace concernée
            trace.setCodeRejet(rejet.getCodeRejetTraces());

            // Mise à jour des autres traces du même bénéficiaire, mais uniquement celles qui
            // n'ont pas déjà un rejet directement lié à leur déclaration consolidée
            traceExtractConsoParDeclarationConso.values().stream()
                .filter(t -> rejet.getNoPersonne().equals(t.getNumeroPersonne()))
                .filter(t -> !idDeclarationsAvecRejet.contains(t.getIdDeclarationConsolidee()))
                .forEach(t -> t.setCodeRejet(rejet.getCodeRejetTraces()));
            // TODO Indira à revoir, ça marche pas pour un cas => qd y a pl rejets qui pvt
            // correspondre à une trace qui est relié à aucun rejet directement par
            // l'idDeclConso..
          }
        });
    traceExtractConsoParDeclarationConso.values().stream()
        .filter(
            trace ->
                rejetsBloquants.stream()
                    .noneMatch(
                        rb ->
                            rb.getIdDeclarationConsolidee()
                                .equals(trace.getIdDeclarationConsolidee())))
        .forEach(trace -> trace.setNomFichier(fileName));

    rejets.forEach(
        rejet -> {
          Rejection rejection =
              new Rejection(
                  rejet.getCodeRejetTraces() + ";;" + rejet.getError(),
                  declarationDao.getDeclarationById(rejet.getNoDeclaration()),
                  new Date(),
                  Constants.ALMV3);
          rejections.add(rejection);
        });

    if (!rejections.isEmpty()) {
      arlService.buildARL(
          rejections,
          declarant,
          DATE_EXEC,
          new ArrayList<>(), // pas de tracesConso générées dans le 608
          new ArrayList<>(traceExtractConsoParDeclarationConso.values()),
          pilotage.getCritereRegroupement(),
          pilotage.getCritereRegroupementDetaille());
    } else if (!traceExtractConsoParDeclarationConso.isEmpty()) {
      // TODO changer par bulk si possible (avec upsert obligatoire)
      traceExtractionConsoService.saveTraceList(
          new ArrayList<>(traceExtractConsoParDeclarationConso.values()), null);
    }
  }

  private void setInfoSouscripteur(
      HistoriqueExecution608 newHistoPilotage,
      Date dateDerniereExecution,
      List<Transcodage> typeBeneficiaireTranscoList,
      List<Transcodage> codeMouvementTranscoList,
      BulkAndList bulkAndList,
      String currentNumeroContrat,
      TmpObject2 tmpObject2) {
    if (!bulkAndList
        .numeroPersonneList()
        .contains(tmpObject2.getBeneficiaire().getNumeroPersonne())) {
      List<String> listFinDroit = new ArrayList<>();
      listFinDroit.add(tmpObject2.getDomaineDroit().getPeriodeDroit().getPeriodeFin());
      bulkAndList
          .infosSouscripteurList()
          .add(
              new InfosSouscripteur(
                  tmpObject2.getBeneficiaire().isSouscripteur(),
                  tmpObject2.getBeneficiaire().getAffiliation().getQualite(),
                  tmpObject2.getCodeEtat(),
                  tmpObject2.getBeneficiaire().getDateRadiation(),
                  listFinDroit,
                  tmpObject2.getContrat().getRangAdministratif(),
                  tmpObject2.getBeneficiaire().getNumeroPersonne(),
                  UtilService.mapRefNumContrat(tmpObject2),
                  tmpObject2.getBeneficiaire().getAffiliation().getTypeAssure(),
                  utilService.getRejet(
                      tmpObject2,
                      currentNumeroContrat,
                      null,
                      null) /*prepare rejet if needed later*/));

      mappersAlmerys
          .mapperAlmerysBeneficiaire()
          .mapBeneficiaire(
              tmpObject2,
              typeBeneficiaireTranscoList,
              codeMouvementTranscoList,
              bulkAndList.beneficiariesList(),
              bulkAndList.rejetBloquantsList(),
              dateDerniereExecution,
              newHistoPilotage);

      bulkAndList.numeroPersonneList().add(tmpObject2.getBeneficiaire().getNumeroPersonne());
    } else {
      bulkAndList.infosSouscripteurList().stream()
          .filter(
              benef ->
                  tmpObject2.getBeneficiaire().getNumeroPersonne().equals(benef.numeroPersonne()))
          .findFirst()
          .ifPresent(
              infosSouscripteur ->
                  infosSouscripteur
                      .dateFinDroit()
                      .add(tmpObject2.getDomaineDroit().getPeriodeDroit().getPeriodeFin()));
    }
  }

  private void clearTmpTables(String nameTmpCollection1, String nameTmpCollection2) {
    Map<String, String> tmp608Tables = new HashMap<>();
    tmp608Tables.put("tmp_608_1", nameTmpCollection1);
    tmp608Tables.put("tmp_608_2", nameTmpCollection2);
    domainService.clearTempTables(tmp608Tables);
  }
}
