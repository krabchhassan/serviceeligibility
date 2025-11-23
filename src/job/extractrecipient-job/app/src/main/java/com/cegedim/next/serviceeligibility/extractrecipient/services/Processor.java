package com.cegedim.next.serviceeligibility.extractrecipient.services;

import static com.cegedim.next.serviceeligibility.core.job.utils.Constants.CODE_RETOUR_OK;
import static com.cegedim.next.serviceeligibility.core.job.utils.Constants.CODE_RETOUR_UNEXPECTED_EXCEPTION;

import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduExtractRecipient;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.AssureV5Recipient;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratAIV5Recipient;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.DataAssureV5Recipient;
import com.cegedim.next.serviceeligibility.core.services.bdd.ServicePrestationService;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Processor implements ApplicationRunner {

  private final String workdirPath;

  private final long batchSize;

  private final CrexProducer crexProducer;

  private final ServicePrestationService servicePrestationService;

  private final Tracer tracer;

  private final String spanName;

  public Processor(
      CrexProducer crexProducer,
      ServicePrestationService servicePrestationService,
      Tracer tracer,
      @Value("${JOB_SPAN_NAME:default_span}") String spanName,
      @Value("${BATCH_SIZE:10000}") long batchSize,
      @Value("${WORKDIR_PATH}") String workdirPath) {
    this.crexProducer = crexProducer;
    this.servicePrestationService = servicePrestationService;
    this.tracer = tracer;
    this.spanName = spanName;
    this.batchSize = batchSize;
    this.workdirPath = workdirPath;
  }

  public int runProcess() {
    CompteRenduExtractRecipient compteRendu = new CompteRenduExtractRecipient();
    int retour;
    try {
      retour = processAllServicePrestation(compteRendu);
    } catch (Exception e) {
      log.error(e.getLocalizedMessage(), e);
      retour = CODE_RETOUR_UNEXPECTED_EXCEPTION;
    }
    // In any case, produce a CREX
    finally {
      displayResult(compteRendu);
      crexProducer.generateCrex(compteRendu);
    }
    return retour;
  }

  private int processAllServicePrestation(CompteRenduExtractRecipient compteRendu) {
    // Process HTP contracts by batch of size BATCH_SIZE
    int iterator = 0;
    List<ContratAIV5Recipient> servicePrestations =
        servicePrestationService.getContractsRecipientsPaginated(batchSize, iterator);

    // If we don't have any HTP contract, we don't need to do anything
    if (CollectionUtils.isEmpty(servicePrestations)) {
      log.info("No HTP contract in database. Aborting...");
      return CODE_RETOUR_OK;
    }

    // Open file stream
    String filePath = workdirPath + getFileName();
    compteRendu.setNomFichier(filePath);

    // Try to create directory
    try {
      Files.createDirectories(Paths.get(workdirPath));
    } catch (IOException e) {
      log.error("Failed to create directory {}", workdirPath, e);
      return CODE_RETOUR_UNEXPECTED_EXCEPTION;
    }

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      // Init file service
      FileService fileService = new FileService(writer);

      fileService.writeFileStart();

      // Loop through HTP contracts
      while (CollectionUtils.isNotEmpty(servicePrestations)) {
        log.info(
            "Reading HTP contracts {} to {}", batchSize * iterator, batchSize * (iterator + 1));
        compteRendu.addContratsExtraits(servicePrestations.size());

        // We add the missing comma if it's not the first time we looped
        if (iterator > 0) {
          fileService.writeComma();
        }

        // For each HTP contract, add it to workdir file
        for (int i = 0; i < servicePrestations.size(); i++) {
          ContratAIV5Recipient servicePrestation = servicePrestations.get(i);
          fileService.addContractToFile(servicePrestation);

          // Update CREX
          addRecipientToCrex(compteRendu, servicePrestation);

          // We add a comma except for the last element
          if (i < (servicePrestations.size() - 1)) {
            fileService.writeComma();
          }

          // Write buffer in file
          fileService.writeInFile();
        }

        // Next page for next batch
        iterator++;
        servicePrestations =
            servicePrestationService.getContractsRecipientsPaginated(batchSize, iterator);
      }

      // Close the bracket in file
      fileService.writeFileEnd();
      fileService.writeInFile();

      return CODE_RETOUR_OK;
    } catch (IOException e) {
      log.error("Error while trying to access file {}", filePath, e);
      return CODE_RETOUR_UNEXPECTED_EXCEPTION;
    } catch (Exception e) {
      log.error("Unexpected exception", e);
      return CODE_RETOUR_UNEXPECTED_EXCEPTION;
    }
  }

  private void addRecipientToCrex(
      CompteRenduExtractRecipient compteRendu, ContratAIV5Recipient servicePrestation) {
    for (AssureV5Recipient assure : servicePrestation.getAssures()) {
      DataAssureV5Recipient data = assure.getData();

      if (CollectionUtils.isNotEmpty(data.getDestinatairesPaiements())) {
        compteRendu.addDestinatairesPaiement(data.getDestinatairesPaiements().size());
      }
      if (CollectionUtils.isNotEmpty(data.getDestinatairesRelevePrestations())) {
        compteRendu.addDestinatairesRelevePrestation(
            data.getDestinatairesRelevePrestations().size());
      }
    }
  }

  private String getFileName() {
    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_hhmmss");
    String formattedDateNow = format.format(new Date());
    return ("BDDS_recipient_extract_" + formattedDateNow + ".json");
  }

  private void displayResult(CompteRenduExtractRecipient compteRendu) {
    log.info("=== Resultat du traitement d'extraction des destinataires ===");

    log.info("Nombre de contrats extraits : {}", compteRendu.getContratsExtraits());
    log.info(
        "Nombre de destinataires de relevé de prestation extraits : {}",
        compteRendu.getDestinatairesRelevePrestation());
    log.info(
        "Nombre de destinataires de paiement extraits : {}",
        compteRendu.getDestinatairesPaiement());
    log.info("Nom du fichier créé : {}", compteRendu.getNomFichier());
  }

  @Override
  public void run(ApplicationArguments args) {
    Span newSpan = tracer.nextSpan().name(spanName).start();
    try (Tracer.SpanInScope spanInScope = tracer.withSpan(newSpan.start())) {
      this.runProcess();
    } finally {
      newSpan.end();
    }
  }
}
