package com.cegedim.next.serviceeligibility.core.services.trigger;

import static com.cegedim.next.serviceeligibility.core.utils.Constants.NO_EXPORT;
import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.SEND_EVENTS;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.common.base.s3.client.exceptions.S3Exception;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import com.cegedim.next.serviceeligibility.core.services.s3.S3Service;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class TriggerCSVService {
  private final TriggerRecyclageService triggerRecyclageService;
  private final TriggerService triggerService;
  private final S3Service s3Service;

  private final boolean sendEvents;

  public TriggerCSVService(
      TriggerRecyclageService triggerRecyclageService,
      TriggerService triggerService,
      S3Service s3Service,
      BeyondPropertiesService beyondPropertiesService) {
    this.triggerRecyclageService = triggerRecyclageService;
    this.triggerService = triggerService;
    this.s3Service = s3Service;
    sendEvents = beyondPropertiesService.getBooleanProperty(SEND_EVENTS).orElse(Boolean.TRUE);
  }

  public CSVPrinter createCSV(
      Appendable writer, Iterator<TriggeredBeneficiary> results, boolean sendEventsForRenewal)
      throws IOException {
    long time = System.currentTimeMillis();
    log.debug("createCSV send event : {}", (sendEvents && sendEventsForRenewal));
    CSVFormat format = CSVFormat.newFormat(';');
    try (CSVPrinter file = new CSVPrinter(writer, format)) {
      file.print(
          "Numero Contrat;NIR Bénéficiaire;Date Naissance;Garanties;Collectivité;Collège;Critère Secondaire Détaillé;Détail Anomalie\n");
      file.println();
      while (results.hasNext()) {
        TriggeredBeneficiary benef = results.next();
        if (sendEvents && sendEventsForRenewal) {
          triggerRecyclageService.launchFinishedEventsForRenewal(benef);
        }
        String motifAnomalie = "";
        List<TriggeredBeneficiaryStatus> statuts = benef.getHistoriqueStatuts();
        if (!CollectionUtils.isEmpty(statuts)) {
          Date dateEffet = null;
          for (TriggeredBeneficiaryStatus statut : statuts) {
            if (statut.getStatut().equals(TriggeredBeneficiaryStatusEnum.Error)
                && (dateEffet == null || statut.getDateEffet().after(dateEffet))) {
              motifAnomalie = statut.getAnomaly().getDescription();
              dateEffet = statut.getDateEffet();
            }
          }
        }
        file.print(
            StringUtils.replace(
                String.format(
                    "%s;%s;%s;%s;%s;%s;%s;%s%n",
                    benef.getNumeroContrat(),
                    benef.getNir(),
                    benef.getDateNaissance(),
                    mapGTToString(benef.getNewContract().getDroitsGaranties()),
                    benef.getCollectivite(),
                    benef.getCollege(),
                    benef.getCritereSecondaireDetaille(),
                    motifAnomalie),
                "null",
                ""));
        file.println();
        file.flush();
      }
      long time2 = System.currentTimeMillis();
      log.debug("createCSV time elapsed : " + (time2 - time) + "ms");
      return file;
    }
  }

  private String mapGTToString(List<DroitAssure> droitAssures) {
    String gts = "";
    boolean isFirstGt = true;
    if (CollectionUtils.isEmpty(droitAssures)) {
      return gts;
    }
    for (DroitAssure droitAssure : droitAssures) {
      gts =
          isFirstGt
              ? String.format("%s %s", droitAssure.getCode(), droitAssure.getCodeAssureur())
              : String.format(
                  "%s , %s %s", gts, droitAssure.getCode(), droitAssure.getCodeAssureur());
      isFirstGt = false;
    }

    return gts;
  }

  private CSVPrinter createCSVFromS3(Appendable writer, Stream<String> lines) throws IOException {
    CSVFormat format = CSVFormat.newFormat(';').withRecordSeparator('\n');
    try (CSVPrinter file = new CSVPrinter(writer, format)) {
      Iterator<String> iter = lines.iterator();
      while (iter.hasNext()) {
        String line = iter.next();
        file.print(line);
        file.println();
        file.flush();
      }
      return file;
    }
  }

  public String saveTriggerProcessToS3AndSendEvent(String triggerId, boolean sendEventsForRenewal) {
    try {
      File tmpFile = File.createTempFile(triggerId, Constants.CSV);
      Iterator<TriggeredBeneficiary> results =
          triggerService.getTriggeredBeneficiariesStream(triggerId);
      try (FileWriter writer = new FileWriter(tmpFile, StandardCharsets.UTF_8)) {
        createCSV(writer, results, sendEventsForRenewal);
      }
      return s3Service.writeToS3bucketTriggerBenef(tmpFile, triggerId);
    } catch (Exception e) {
      log.error("Impossible de sauvegarder les informations du trigger {} sur s3", triggerId, e);
    }
    return NO_EXPORT;
  }

  public CSVPrinter readTriggerBenefFileFromS3(Appendable writer, String idTrigger)
      throws IOException, S3Exception {
    return createCSVFromS3(writer, s3Service.readTriggerBenefFile(idTrigger));
  }

  public List<String> saveNotExported() {
    List<String> paths = new ArrayList<>();
    Iterator<Trigger> triggers = triggerService.getIDsTriggerRenewNotExported();
    while (triggers.hasNext()) {
      Trigger trigger = triggers.next();
      String path = saveTriggerProcessToS3AndSendEvent(trigger.getId(), true);
      if (NO_EXPORT.equals(path)) {
        paths.add(trigger.getId() + "-" + NO_EXPORT);
      } else {
        paths.add(path);
        triggerService.setExported(trigger.getId(), true);
      }
    }
    return paths;
  }
}
