package com.cegedim.next.serviceeligibility.extractpopulation.services;

import com.cegedim.common.base.pefb.services.MetaService;
import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduExtractionPopulation;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.bdd.ServicePrestationService;
import com.cegedim.next.serviceeligibility.extractpopulation.constants.ConstantsExtractPop;
import com.cegedim.next.serviceeligibility.extractpopulation.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class JsonFileService {
  private final ServicePrestationService servicePrestationService;

  public int fillJsonFiles(
      int maxContractsPerFile,
      String stringDateExec,
      String outputDirectory,
      CompteRenduExtractionPopulation compteRenduExtractionPopulation) {
    if (maxContractsPerFile > ConstantsExtractPop.MAX_CONTRACTS_PER_FILE_DEFAULT) {
      log.warn(
          "Attention ! Vous avez paramétré une limite de nombre de contrat par flux JSON supérieure à la limite maximale autorisée ({} > {}). La limite maximale autorisée va donc être appliquée.",
          maxContractsPerFile,
          ConstantsExtractPop.MAX_CONTRACTS_PER_FILE_DEFAULT);
      maxContractsPerFile = ConstantsExtractPop.MAX_CONTRACTS_PER_FILE_DEFAULT;
    }

    int indexFile = 1;
    String jsonFileExtractPopName =
        ConstantsExtractPop.EXTRACTION_CONTRATS
            + stringDateExec
            + "_"
            + indexFile
            + ConstantsExtractPop.JSON_EXTENSION;
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    FileWriter jsonExtractPopWriter = null;

    try {
      jsonExtractPopWriter =
          new FileWriter(outputDirectory + jsonFileExtractPopName, StandardCharsets.UTF_8, true);
      Stream<ContratAIV6> stream = servicePrestationService.getAllContrats();

      // Début du fichier
      jsonExtractPopWriter.write("[\n");

      int nbWrittenContracts = 0;
      Iterator<ContratAIV6> iterator = stream.iterator();
      while (iterator.hasNext()) {
        ContratAIV6 contract = iterator.next();
        jsonExtractPopWriter.append(
            objectMapper.writeValueAsString(getFluxJsonExtractPop(contract)));
        nbWrittenContracts++;

        // S'il y a encore des contrats à extraire et que l'on n'a pas atteint la limite
        // de contrats par fichier => on ajoute une virgule
        if (iterator.hasNext() && nbWrittenContracts != maxContractsPerFile) {
          jsonExtractPopWriter.append(",\n");
        }

        jsonExtractPopWriter.flush();

        // S'il y a encore des contrats à extraire et que l'on a atteint la limite par fichier =>
        // on ferme le writer existant pour en créer un nouveau permettant d'écrire le prochain
        // fichier
        if (iterator.hasNext() && nbWrittenContracts == maxContractsPerFile) {
          jsonExtractPopWriter.write("\n]");
          jsonExtractPopWriter.close();
          MetaService.generateMetaData(
              jsonFileExtractPopName, outputDirectory, ConstantsExtractPop.ISSUER);
          compteRenduExtractionPopulation.addNomFichiers(jsonFileExtractPopName);

          indexFile++;
          jsonFileExtractPopName =
              ConstantsExtractPop.EXTRACTION_CONTRATS
                  + stringDateExec
                  + "_"
                  + indexFile
                  + ConstantsExtractPop.JSON_EXTENSION;
          jsonExtractPopWriter =
              new FileWriter(
                  outputDirectory + jsonFileExtractPopName, StandardCharsets.UTF_8, true);

          // Début du fichier
          jsonExtractPopWriter.write("[\n");
          nbWrittenContracts = 0;
        }
      }
      jsonExtractPopWriter.write("\n]");

      MetaService.generateMetaData(
          jsonFileExtractPopName, outputDirectory, ConstantsExtractPop.ISSUER);
      compteRenduExtractionPopulation.addNomFichiers(jsonFileExtractPopName);

    } catch (IOException e) {
      log.error(e.getMessage(), e);
      return -1;
    } finally {
      if (jsonExtractPopWriter != null) {
        try {
          jsonExtractPopWriter.close();
        } catch (IOException e) {
          log.error("Erreur lors de la fermeture du fileWriter : " + e.getMessage());
        }
      }
    }
    return 0;
  }

  private FluxJsonExtractPop getFluxJsonExtractPop(ContratAIV6 contract) {
    List<com.cegedim.next.serviceeligibility.extractpopulation.model.Assure> insureds =
        contract.getAssures().stream()
            .map(
                assure ->
                    new com.cegedim.next.serviceeligibility.extractpopulation.model.Assure(
                        assure.getIdentite().getNumeroPersonne(),
                        assure.getIsSouscripteur(),
                        assure.getIdentite().getNir() != null
                            ? assure.getIdentite().getNir().getCode()
                            : null,
                        getAffiliationROList(assure),
                        assure.getIdentite().getDateNaissance(),
                        assure.getIdentite().getRangNaissance(),
                        assure.getQualite() != null ? assure.getQualite().getCode() : null,
                        assure.getRangAdministratif(),
                        assure.getData().getNom().getNomFamille(),
                        assure.getData().getNom().getNomUsage(),
                        assure.getData().getNom().getPrenom(),
                        assure.getDateDebutAdhesionIndividuelle(),
                        assure.getDateRadiation(),
                        getGuarantees(assure)))
            .toList();
    return new FluxJsonExtractPop(
        contract.getIdDeclarant(),
        contract.getNumeroAdherent(),
        contract.getNumero(),
        contract.getDateSouscription(),
        contract.getDateResiliation(),
        contract.getSocieteEmettrice(),
        insureds);
  }

  private List<AffiliationRO> getAffiliationROList(Assure assure) {
    if (assure.getIdentite() == null || assure.getIdentite().getAffiliationsRO() == null) {
      return Collections.emptyList();
    }

    return assure.getIdentite().getAffiliationsRO().stream()
        .map(
            nirRattachementRO -> {
              String codeCaisse = null;
              String codeCentre = null;
              String codeRegime = null;
              String debut = null;
              String fin = null;
              if (nirRattachementRO.getRattachementRO() != null) {
                codeCentre = nirRattachementRO.getRattachementRO().getCodeCentre();
                codeCaisse = nirRattachementRO.getRattachementRO().getCodeCaisse();
                codeRegime = nirRattachementRO.getRattachementRO().getCodeRegime();
              }
              if (nirRattachementRO.getPeriode() != null) {
                debut = nirRattachementRO.getPeriode().getDebut();
                fin = nirRattachementRO.getPeriode().getFin();
              }
              return new AffiliationRO(
                  nirRattachementRO.getNir() != null ? nirRattachementRO.getNir().getCode() : null,
                  codeRegime,
                  codeCaisse,
                  codeCentre,
                  debut,
                  fin);
            })
        .toList();
  }

  private List<Garantie> getGuarantees(Assure assure) {
    return assure.getDroits().stream()
        .map(
            droitAssure ->
                new Garantie(
                    droitAssure.getCodeAssureur(),
                    droitAssure.getCode(),
                    droitAssure.getPeriode().getDebut(),
                    droitAssure.getPeriode().getFin()))
        .toList();
  }
}
