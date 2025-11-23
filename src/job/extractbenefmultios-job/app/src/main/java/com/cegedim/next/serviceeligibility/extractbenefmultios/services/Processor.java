package com.cegedim.next.serviceeligibility.extractbenefmultios.services;

import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduExtractBenefMultiOS;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.SocieteEmettrice;
import com.cegedim.next.serviceeligibility.core.services.bdd.BeneficiaryService;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.extractbenefmultios.constants.Constants;
import com.cegedim.next.serviceeligibility.extractbenefmultios.model.Extraction;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.tracing.annotation.NewSpan;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Processor {

  private static final String EXPORT_FILE_NAME = "Export_Benef_Multi_OS.json";

  private final BeneficiaryService beneficiaryService;

  private final String workdirPath;

  public Processor(
      @Value("${OUTPUT_PATH:/workdir/serviceeligibility/extractbenefmultios/}") String workdirPath,
      BeneficiaryService beneficiaryService) {
    this.workdirPath = workdirPath;
    this.beneficiaryService = beneficiaryService;
  }

  @NewSpan
  public int calcul(CompteRenduExtractBenefMultiOS compteRenduExtractBenefMultiOS) {
    int processReturnCode = Constants.PROCESSED_WITHOUT_ERRORS;
    List<Extraction> extractions = new ArrayList<>();
    Iterator<BenefAIV5> benefs = beneficiaryService.getBenefMultiOS();
    while (benefs.hasNext()) {
      BenefAIV5 benef = benefs.next();
      List<SocieteEmettrice> societeEmettrices =
          extractSocietesEmettricesChevauche(benef.getSocietesEmettrices());
      if (!societeEmettrices.isEmpty()) {
        Extraction extraction = new Extraction();
        extraction.setKey(benef.getKey());
        extraction.setSocieteEmettrices(societeEmettrices);
        extractions.add(extraction);
      }
    }

    if (!extractions.isEmpty()) {
      try {
        Files.createDirectories(Paths.get(workdirPath));
        String pathToSavedFile = workdirPath + EXPORT_FILE_NAME;
        createFileJson(extractions, pathToSavedFile);
        compteRenduExtractBenefMultiOS.setCheminFichierExtraction(pathToSavedFile);
      } catch (IOException e) {
        log.error(e.getMessage(), e);
        processReturnCode = Constants.PROCESSED_WITH_ERRORS;
      }
    }

    compteRenduExtractBenefMultiOS.setNbBeneficiairesExtraits(extractions.size());
    return processReturnCode;
  }

  private void createFileJson(List<Extraction> extractions, String filePath) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    String json = objectMapper.writeValueAsString(extractions);

    try (FileWriter writer = new FileWriter(filePath)) {
      writer.write(json);
    }
  }

  List<SocieteEmettrice> extractSocietesEmettricesChevauche(
      List<SocieteEmettrice> societeEmettrices) {
    Map<SocieteEmettrice, Set<Periode>> periodesPerSocieteEmettrice = new HashMap<>();

    for (int i = 0; i < societeEmettrices.size(); i++) {
      SocieteEmettrice societeEmettrice1 = societeEmettrices.get(i);

      for (int j = i + 1; j < societeEmettrices.size(); j++) {
        SocieteEmettrice societeEmettrice2 = societeEmettrices.get(j);

        for (Periode periode1 : societeEmettrice1.getPeriodes()) {
          for (Periode periode2 : societeEmettrice2.getPeriodes()) {
            if (DateUtils.isOverlapping(
                periode1.getDebut(), periode1.getFin(), periode2.getDebut(), periode2.getFin())) {
              periodesPerSocieteEmettrice.putIfAbsent(societeEmettrice1, new LinkedHashSet<>());
              periodesPerSocieteEmettrice.putIfAbsent(societeEmettrice2, new LinkedHashSet<>());
              periodesPerSocieteEmettrice.get(societeEmettrice1).add(periode1);
              periodesPerSocieteEmettrice.get(societeEmettrice2).add(periode2);
            }
          }
        }
      }
    }

    List<SocieteEmettrice> result = new ArrayList<>();
    periodesPerSocieteEmettrice.forEach(
        (societeEmettrice, periodes) -> {
          societeEmettrice.setPeriodes(new ArrayList<>(periodes));
          result.add(societeEmettrice);
        });

    return result;
  }
}
