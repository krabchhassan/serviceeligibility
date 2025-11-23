package com.cegedim.next.serviceeligibility.core.services.common.batch;

import static com.cegedim.next.serviceeligibility.core.utils.DateUtils.YYYY_MM_DD_T_HH_MM_SS_SSS;

import com.cegedim.common.base.pefb.services.MetaService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.arldata.ARLDataDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.flux.ParametresFluxDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.CircuitService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.FluxService;
import com.cegedim.next.serviceeligibility.core.job.batch.Rejection;
import com.cegedim.next.serviceeligibility.core.job.batch.TraceConsolidation;
import com.cegedim.next.serviceeligibility.core.job.batch.TraceExtractionConso;
import com.cegedim.next.serviceeligibility.core.model.domain.*;
import com.cegedim.next.serviceeligibility.core.model.domain.Beneficiaire;
import com.cegedim.next.serviceeligibility.core.model.entity.*;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ConstantesRejetsConsolidations;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ConstantesRejetsExtractions;
import com.cegedim.next.serviceeligibility.core.services.trace.TraceConsolidationService;
import com.cegedim.next.serviceeligibility.core.services.trace.TraceExtractionConsoService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.FileSystemUtils;

@Slf4j
@RequiredArgsConstructor
public class ARLService {
  public static final String REJET = "REJET_";
  private final FluxService fluxService;
  private final CircuitService circuitService;
  private final TraceConsolidationService traceConsolidationService;
  private final TraceExtractionConsoService traceExtractionConsoService;
  private final String arlFolder;
  private final int tracesBulkSize; // Quantity of rejectedTraces in memory before saving
  private final String batchName;

  public static final String CAN_T_OPEN_OR_WRITE_THE_FILE = "Can't open or write the file {} : {}";
  public static final String META_EXTENSION = ".meta";

  /** méthode seulement utlisé pour le batch 620 sur les tests de service */
  public void deleteDirectoryARL() {
    File directory = new File(arlFolder);
    if (directory.exists()) {
      boolean deleted = FileSystemUtils.deleteRecursively(directory);
      if (deleted) {
        log.info("{} deleted ", arlFolder);
      }
    }
  }

  @ContinueSpan(log = "buildARL")
  public void buildARL(
      List<Rejection> rejections,
      Declarant declarant,
      Date dateExecution,
      List<TraceConsolidation> rejectedTraces,
      List<TraceExtractionConso> rejectedTracesExtraction,
      String critereRegroupement,
      String critereRegroupementDetaille) {
    File directory = new File(arlFolder);
    if (!directory.exists()) {
      log.debug("{} created {}", arlFolder, directory.mkdirs());
    }

    List<ARLDataDto> arlDataList = this.createARL(rejections, declarant);

    String fileNameCSV =
        getArlNameNoExtension(
                batchName,
                declarant.getCodePartenaire(),
                declarant.get_id(),
                declarant.getOperateurPrincipal(),
                critereRegroupement,
                critereRegroupementDetaille,
                DateUtils.formatDate(dateExecution, DateUtils.IDENTIFIANT_BATCH_FORMAT_DATE))
            + ".csv";

    String filePath = directory.getAbsolutePath() + File.separator + fileNameCSV;

    File fichierCSV = new File(filePath);

    updateOrCreateTraceFluxForArl(arlDataList, declarant, fileNameCSV, dateExecution);

    generateARLFile(
        arlDataList,
        fileNameCSV,
        filePath,
        fichierCSV,
        declarant.getCodePartenaire(),
        rejectedTraces,
        rejectedTracesExtraction);
  }

  public static String getArlNameNoExtension(
      String batch,
      String codePartenaire,
      String numAMC,
      String operateurPrincipal,
      String critereRegroupement,
      String critereRegroupementDetaille,
      String timestamp) {
    StringBuilder res = new StringBuilder();
    res.append("ARL_").append(batch);
    if (codePartenaire != null && !codePartenaire.isEmpty()) {
      res.append("_").append(codePartenaire);
    }
    if (numAMC != null && !numAMC.isEmpty()) {
      res.append("_").append(numAMC);
    }
    if (operateurPrincipal != null && !operateurPrincipal.isEmpty()) {
      res.append("_").append(operateurPrincipal);
    }
    if (critereRegroupement != null && !critereRegroupement.isEmpty()) {
      res.append("_").append(critereRegroupement);
    }
    if (critereRegroupementDetaille != null && !critereRegroupementDetaille.isEmpty()) {
      res.append("_").append(critereRegroupementDetaille);
    }
    res.append("_").append(timestamp);
    return res.toString();
  }

  private void generateARLFile(
      List<ARLDataDto> arlDataDtoListbyAMC,
      String fileNameCSV,
      String filePath,
      File fichierCSV,
      String codeClient,
      List<TraceConsolidation> rejectedTraces,
      List<TraceExtractionConso> rejectedTracesExtraction) {
    try (FileWriter outputfileCSV = new FileWriter(fichierCSV)) {
      // create CSVWriter with '|' as separator
      CSVWriter writer =
          new CSVWriter(
              outputfileCSV,
              ';',
              ICSVWriter.NO_QUOTE_CHARACTER,
              ICSVWriter.DEFAULT_ESCAPE_CHARACTER,
              ICSVWriter.DEFAULT_LINE_END);

      // create a List which contains String array
      List<String[]> data = new ArrayList<>();
      data.add(
          new String[] {
            "TRAITEMENT_LE",
            "SERVICE",
            "NO_AMC",
            "CONVENTION",
            "REGROUPEMENT",
            "REGROUPEMENT_DETAILLE",
            "NO_PERSONNE",
            "NIR",
            "DATE_NAISSANCE",
            "RANG_NAISSANCE",
            "NOM",
            "PRENOM",
            "NO_CONTRAT",
            "GESTIONNAIRE_CONTRAT",
            "GROUPE_ASSURES",
            "DROITS",
            "MVT",
            "DATE_DECLARATION",
            "TYPE_REJET",
            "NIVEAU_REJET",
            "REJET",
            "MOTIF_REJET",
            "VALEUR_REJET",
            "NOM_FICHIER_NO_DECLARATION",
            "EMETTEUR_DROITS",
            "CODE_CIRCUIT",
            "LIBELLE_CIRCUIT",
            "DESTINATAIRE_DROITS",
            "BO_EMETTEUR_DROITS"
          });

      for (ARLDataDto arlDataDto : arlDataDtoListbyAMC) {
        data.add(
            new String[] {
              arlDataDto.getDateTraitement(),
              arlDataDto.getCodeService(),
              arlDataDto.getAmcNumber(),
              arlDataDto.getConvention(),
              arlDataDto.getRegroupement(),
              arlDataDto.getRegroupementDetaille(),
              arlDataDto.getNumeroPersonne(),
              arlDataDto.getNir(),
              arlDataDto.getDateNaissance(),
              arlDataDto.getRangNaissance(),
              arlDataDto.getNom(),
              arlDataDto.getPrenom(),
              arlDataDto.getNumeroContrat(),
              arlDataDto.getGestionnaireContrat(),
              arlDataDto.getGroupeAssures(),
              arlDataDto.getDroits(),
              arlDataDto.getMouvement(),
              arlDataDto.getDateDeclaration(),
              arlDataDto.getTypeRejet(),
              arlDataDto.getNiveauRejet(),
              arlDataDto.getRejet(),
              arlDataDto.getMotifRejet(),
              arlDataDto.getValeurRejet(),
              arlDataDto.getNomFichierNumDeclaration(),
              arlDataDto.getEmetteurDroits(),
              arlDataDto.getCodeCircuit(),
              arlDataDto.getLibelleCircuit(),
              arlDataDto.getDestinataireDroits(),
              arlDataDto.getBoEmetteurDroits()
            });

        // Generate trace, keep it in a list, and save them if we reached the threshold
        manageRejectTrace(
            arlDataDto, fileNameCSV, codeClient, rejectedTraces, rejectedTracesExtraction);
      }

      // Save remaining traces
      saveRejectTraces(rejectedTraces, rejectedTracesExtraction);

      writer.writeAll(data);
      // closing writer connection
      writer.close();

      File file = new File(filePath);
      String meta = MetaService.createMeta(file, "UTF-8", LocalDateTime.now(), "batch" + batchName);
      log.debug("meta: {}", meta);
      meta = meta.replace("\"fileName\":\".", "\"fileName\":\"");
      Files.writeString(Paths.get(filePath + META_EXTENSION), meta, StandardOpenOption.CREATE);
    } catch (IOException e) {
      log.error(CAN_T_OPEN_OR_WRITE_THE_FILE, fileNameCSV, e.getMessage());
    }
  }

  private void manageRejectTrace(
      ARLDataDto arlData,
      String nomFichierARL,
      String codeClient,
      List<TraceConsolidation> rejectedTraces,
      List<TraceExtractionConso> rejectedTracesExtraction) {
    if (Constants.NUMERO_BATCH_607.equals(batchName)) {
      rejectedTraces.forEach(rejectedTrace -> rejectedTrace.setNomFichierARL(nomFichierARL));
    } else if (Constants.NUMERO_BATCH_608.equals(batchName)) {
      rejectedTracesExtraction.forEach(
          rejectedTraceExtraction -> rejectedTraceExtraction.setNomFichierARL(nomFichierARL));
    } else {
      TraceConsolidation trace =
          traceConsolidationService.generateRejectedTrace(
              arlData, nomFichierARL, codeClient, batchName);
      rejectedTraces.add(trace);

      List<TraceExtractionConso> tracesExtraction =
          traceExtractionConsoService.completeRejectedTrace(arlData, nomFichierARL);
      if (CollectionUtils.isNotEmpty(tracesExtraction)) {
        rejectedTracesExtraction.addAll(tracesExtraction);
      }
    }

    // If we reached max bulk size is reached, save current traces and clear traces
    // list
    // Prevents eventual OOMs
    if (rejectedTraces.size() >= tracesBulkSize) {
      saveRejectTraces(rejectedTraces, rejectedTracesExtraction);
    }
  }

  private void saveRejectTraces(
      List<TraceConsolidation> rejectedTraces,
      List<TraceExtractionConso> rejectedTracesExtraction) {
    if (CollectionUtils.isNotEmpty(rejectedTraces)) {
      log.debug("Saved {} traces", rejectedTraces.size());
      traceConsolidationService.saveTraceList(rejectedTraces);

      // Empty the list for next bulk
      rejectedTraces.clear();
    }

    if (CollectionUtils.isNotEmpty(rejectedTracesExtraction)) {
      log.debug("Saved {} traces extraction", rejectedTracesExtraction.size());
      traceExtractionConsoService.saveTraceList(rejectedTracesExtraction, null);

      // Empty the list for next bulk
      rejectedTracesExtraction.clear();
    }
  }

  @ContinueSpan(log = "createARL")
  public List<ARLDataDto> createARL(List<Rejection> rejections, Declarant declarant) {
    List<ARLDataDto> arlDataList = new ArrayList<>();
    for (Rejection rejection : rejections) {
      Declaration declaration = rejection.getDeclaration();
      if (declaration != null) {
        log.debug("Rejet on declaration {}", declaration.get_id());

        String[] rejectionValues = rejection.getCodeRejet().split(";");
        String[] rejectionCode = new String[5];

        // rejectionValues[0] is the code
        ConstantesRejetsConsolidations constantesRejetsConsolidations =
            ConstantesRejetsConsolidations.findByCode(REJET + rejectionValues[0]);
        if (constantesRejetsConsolidations != null) {
          rejectionCode = constantesRejetsConsolidations.toString().split(";");
        } else {
          ConstantesRejetsExtractions constantesRejetsExtractions =
              ConstantesRejetsExtractions.findByCode(REJET + rejectionValues[0]);
          if (constantesRejetsExtractions != null) {
            rejectionCode = constantesRejetsExtractions.toString().split(";");
          }
        }
        String codeService = rejection.getCodeService();

        // rejectionValues[2] is the built error message
        arlDataList.add(
            createARLData(
                declaration,
                rejection.getDateExecution(),
                codeService,
                rejectionCode,
                StringUtils.isNotBlank(rejectionValues[2]) ? rejectionValues[2] : rejectionCode[1],
                declarant));
      } else {
        DeclarationConsolide declarationConsolide = rejection.getDeclarationConsolide();
        if (declarationConsolide == null) {
          log.error(
              "Rejet ignoré : pas de déclaration, ni de déclarationConsolidée retrouvées pour ce rejet !");
        } else {
          log.debug("Rejet on declaration {}", declarationConsolide.getIdDeclarations());

          String[] rejectionValues = rejection.getCodeRejet().split(";");

          // rejectionValues[0] is the code
          ConstantesRejetsConsolidations rejectionCode =
              ConstantesRejetsConsolidations.findByCode(REJET + rejectionValues[0]);
          String codeService = rejection.getCodeService();

          // rejectionValues[2] is the built error message
          String valeurRejet =
              rejectionValues.length > 2 && StringUtils.isNotBlank(rejectionValues[2])
                  ? rejectionValues[2]
                  : rejectionCode.getMessage();

          arlDataList.add(
              createARLDataFromDeclarationConsolide(
                  declarationConsolide,
                  rejection.getDateExecution(),
                  codeService,
                  rejectionCode,
                  valeurRejet,
                  declarant));
        }
      }
    }
    return arlDataList;
  }

  private ARLDataDto createARLData(
      Declaration declaration,
      Date dateExecution,
      String codeService,
      String[] rejection,
      String valeurRejet,
      Declarant declarant) {
    ARLDataDto arlData = new ARLDataDto();
    String dateTraitement = DateUtils.formatDate(dateExecution, DateUtils.YYYY_MM_DD_HH_MM_SS);
    arlData.setDateTraitement(dateTraitement);
    arlData.setCodeService(codeService);
    arlData.setAmcNumber(declaration.getIdDeclarant());
    Contrat contrat = declaration.getContrat();
    arlData.setConvention(contrat.getTypeConvention());
    arlData.setRegroupement(contrat.getCritereSecondaire());
    arlData.setRegroupementDetaille(contrat.getCritereSecondaireDetaille());
    BeneficiaireV2 beneficiaire = declaration.getBeneficiaire();
    arlData.setNumeroPersonne(beneficiaire.getNumeroPersonne());
    arlData.setNir(beneficiaire.getNirBeneficiaire());
    arlData.setDateNaissance(beneficiaire.getDateNaissance());
    arlData.setRangNaissance(beneficiaire.getRangNaissance());
    arlData.setNom(beneficiaire.getAffiliation().getNom());
    arlData.setPrenom(beneficiaire.getAffiliation().getPrenom());
    arlData.setNumeroContrat(contrat.getNumero());
    arlData.setGestionnaireContrat(contrat.getGestionnaire());
    arlData.setGroupeAssures(contrat.getGroupeAssures());
    arlData.setDroits(declaration.getCodeEtat());
    List<DomaineDroit> domaineDroits = declaration.getDomaineDroits();
    if (CollectionUtils.isNotEmpty(domaineDroits)) {
      String obtentionModeList =
          domaineDroits.stream()
              .map(domaineDroit -> domaineDroit.getPeriodeDroit().getModeObtention())
              .collect(Collectors.joining(","));
      arlData.setMouvement(obtentionModeList);
    }
    if (declaration.getEffetDebut() != null) {
      arlData.setDateDeclaration(
          DateUtils.formatDate(declaration.getEffetDebut(), YYYY_MM_DD_T_HH_MM_SS_SSS));
    }
    arlData.setRejet(rejection[0]);
    arlData.setMotifRejet(rejection[1]);
    arlData.setTypeRejet(rejection[3]);
    arlData.setNiveauRejet(rejection[4].replace("#", ""));
    arlData.setValeurRejet(valeurRejet);
    arlData.setNomFichierNumDeclaration(declaration.get_id());
    arlData.setEmetteurDroits(declarant.getEmetteurDroits());
    List<Circuit> circuitList = circuitService.findAllCircuits();
    Optional<Circuit> circuit =
        circuitList.stream()
            .filter(circuit1 -> declarant.getCodeCircuit().equals(circuit1.getCodeCircuit()))
            .findFirst();
    arlData.setCodeCircuit(
        circuit.isPresent() ? circuit.get().getCodeCircuit() : declarant.getCodeCircuit());
    arlData.setLibelleCircuit(circuit.isPresent() ? circuit.get().getLibelleCircuit() : "");
    arlData.setDestinataireDroits("BDD");
    arlData.setBoEmetteurDroits(declarant.getEmetteurDroits());
    return arlData;
  }

  private ARLDataDto createARLDataFromDeclarationConsolide(
      DeclarationConsolide declarationConsolide,
      Date dateExecution,
      String codeService,
      ConstantesRejetsConsolidations rejection,
      String valeurRejet,
      Declarant declarant) {
    ARLDataDto arlData = new ARLDataDto();
    String dateTraitement = DateUtils.formatDate(dateExecution, DateUtils.YYYY_MM_DD_HH_MM_SS);
    arlData.setDateTraitement(dateTraitement);
    arlData.setCodeService(codeService);
    arlData.setAmcNumber(declarationConsolide.getIdDeclarant());
    Contrat contrat = declarationConsolide.getContrat();
    arlData.setConvention(contrat.getTypeConvention());
    arlData.setRegroupement(contrat.getCritereSecondaire());
    arlData.setRegroupementDetaille(contrat.getCritereSecondaireDetaille());
    Beneficiaire beneficiaire = declarationConsolide.getBeneficiaire();
    arlData.setNumeroPersonne(beneficiaire.getNumeroPersonne());
    arlData.setNir(beneficiaire.getNirBeneficiaire());
    arlData.setDateNaissance(beneficiaire.getDateNaissance());
    arlData.setRangNaissance(beneficiaire.getRangNaissance());
    arlData.setNom(beneficiaire.getAffiliation().getNom());
    arlData.setPrenom(beneficiaire.getAffiliation().getPrenom());
    arlData.setNumeroContrat(contrat.getNumero());
    arlData.setGestionnaireContrat(contrat.getGestionnaire());
    arlData.setGroupeAssures(contrat.getGroupeAssures());
    arlData.setDroits(Constants.CODE_ETAT_VALIDE);
    List<DomaineDroit> domaineDroits = declarationConsolide.getDomaineDroits();
    if (CollectionUtils.isNotEmpty(domaineDroits)) {
      String obtentionModeList =
          domaineDroits.stream()
              .map(domaineDroit -> domaineDroit.getPeriodeDroit().getModeObtention())
              .collect(Collectors.joining(","));
      arlData.setMouvement(obtentionModeList);
    }
    if (declarationConsolide.getEffetDebut() != null) {
      arlData.setDateDeclaration(
          DateUtils.formatDate(declarationConsolide.getEffetDebut(), YYYY_MM_DD_T_HH_MM_SS_SSS));
    }
    arlData.setTypeRejet(rejection.getTypeErreur());
    arlData.setNiveauRejet(rejection.getNiveau());
    arlData.setRejet(rejection.getCode());
    arlData.setMotifRejet(rejection.getMessage());
    arlData.setValeurRejet(valeurRejet);
    arlData.setNomFichierNumDeclaration(declarationConsolide.get_id());
    arlData.setEmetteurDroits(declarant.getEmetteurDroits());
    List<Circuit> circuitList = circuitService.findAllCircuits();
    Optional<Circuit> circuit =
        circuitList.stream()
            .filter(circuit1 -> declarant.getCodeCircuit().equals(circuit1.getCodeCircuit()))
            .findFirst();
    arlData.setCodeCircuit(
        circuit.isPresent() ? circuit.get().getCodeCircuit() : declarant.getCodeCircuit());
    arlData.setLibelleCircuit(circuit.isPresent() ? circuit.get().getLibelleCircuit() : "");
    arlData.setDestinataireDroits("BDD");
    arlData.setBoEmetteurDroits(declarant.getEmetteurDroits());
    return arlData;
  }

  private void updateOrCreateTraceFluxForArl(
      List<ARLDataDto> arlDataDtoListbyAMC,
      Declarant declarant,
      String fileName,
      Date dateExecution) {
    Map<String, List<ARLDataDto>> arlDataByServices =
        arlDataDtoListbyAMC.stream().collect(Collectors.groupingBy(ARLDataDto::getCodeService));

    arlDataByServices.forEach(
        (codeService, datas) -> {
          int errorNb = datas.size();
          ParametresFluxDto paramSearch = new ParametresFluxDto();
          paramSearch.setProcessus(codeService);
          paramSearch.setNomAMC(declarant.getNom());
          paramSearch.setNomFichier(fileName);
          paramSearch.setFichierEmis(true);
          if (!fluxService.updateMouvementEmisFlux(paramSearch, errorNb)) {
            createOneTraceFlux(fileName, dateExecution, errorNb, declarant, codeService);
          }
        });
  }

  private void createOneTraceFlux(
      String fileName, Date dateExecution, int errorNb, Declarant declarant, String codeService) {
    Flux flux = new Flux();
    flux.setBatch(batchName);
    flux.setProcessus(codeService);
    flux.setDateExecution(dateExecution);
    flux.setIdDeclarant(declarant.get_id());
    InfoAMC infoAMC = new InfoAMC();
    infoAMC.setNomAMC(declarant.getNom());
    infoAMC.setCodeCircuit(declarant.getCodeCircuit());
    infoAMC.setCodePartenaire(declarant.getCodePartenaire());
    infoAMC.setEmetteurDroits(declarant.getEmetteurDroits());
    infoAMC.setOperateurPrincipal(declarant.getOperateurPrincipal());
    flux.setInfoAMC(infoAMC);
    flux.setTypeFichier("ARL");
    flux.setFichierEmis(true);

    InfoFichierEmis infoFichierEmis = new InfoFichierEmis();
    infoFichierEmis.setMouvementEmis(Math.toIntExact(errorNb));
    infoFichierEmis.setNomFichier(fileName);
    flux.setInfoFichierEmis(infoFichierEmis);

    fluxService.create(flux);
  }
}
