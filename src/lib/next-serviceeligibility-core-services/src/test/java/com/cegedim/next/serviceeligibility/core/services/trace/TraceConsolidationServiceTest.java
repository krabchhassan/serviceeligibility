package com.cegedim.next.serviceeligibility.core.services.trace;

import static com.cegedim.next.serviceeligibility.core.job.utils.Constants.COLLECTION_CONSOLIDATION_CARTES;
import static com.cegedim.next.serviceeligibility.core.utils.Constants.NUMERO_BATCH_620;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.arldata.ARLDataDto;
import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.config.UtilsForTesting;
import com.cegedim.next.serviceeligibility.core.job.batch.TraceConsolidation;
import com.cegedim.next.serviceeligibility.core.model.entity.DeclarationConsolide;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class TraceConsolidationServiceTest {
  @Autowired private TraceConsolidationService traceConsolidationService;

  private static final String DECLARATION_CONSO_PATH =
      "src/test/resources/620-declarationConsolidees/";

  private static final String ARL_PATH = "src/test/resources/620-arl/";

  @Test
  void generateTracesNoDeclarationTest() {
    List<TraceConsolidation> traces =
        traceConsolidationService.generateValidTraces((DeclarationConsolide) null, "BAL");

    Assertions.assertEquals(0, traces.size());
  }

  @Test
  void generateTracesOneServiceTest() throws IOException {
    String codeClient = "BAL";

    DeclarationConsolide declarationConsolide =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConso-Traces-1-service.json",
            DeclarationConsolide.class);

    // Because of a timezone issue, we force the date in declarationConsolide
    Date date =
        DateUtils.parseDate("2024-01-17T14:34:15.779+0000", DateUtils.YYYY_MM_DD_T_HH_MM_SS_SSS);
    declarationConsolide.setDateConsolidation(date);

    List<TraceConsolidation> traces =
        traceConsolidationService.generateValidTraces(declarationConsolide, codeClient);

    Assertions.assertEquals(1, traces.size());

    TraceConsolidation trace = traces.get(0);
    Assertions.assertEquals("65a7e13b61e31412b3279163", trace.getIdDeclaration());
    Assertions.assertEquals("0097810998", trace.getIdDeclarant());
    Assertions.assertEquals(date, trace.getDateExecution());
    Assertions.assertEquals(Constants.CARTE_DEMATERIALISEE, trace.getCodeService());
    Assertions.assertNull(trace.getCodeRejet());
    Assertions.assertEquals(NUMERO_BATCH_620, trace.getBatch());
    Assertions.assertNull(trace.getNomFichierARL());
    Assertions.assertEquals(codeClient, trace.getCodeClient());
    Assertions.assertEquals(COLLECTION_CONSOLIDATION_CARTES, trace.getCollectionConsolidee());
  }

  @Test
  void generateTracesTwoServicesTest() throws IOException {
    String codeClient = "BAL";
    DeclarationConsolide declarationConsolide =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConso-Traces-2-services.json",
            DeclarationConsolide.class);

    // Because of a timezone issue, we force the date in declarationConsolide
    Date date =
        DateUtils.parseDate("2024-01-17T14:34:15.779+0000", DateUtils.YYYY_MM_DD_T_HH_MM_SS_SSS);
    declarationConsolide.setDateConsolidation(date);

    List<TraceConsolidation> traces =
        traceConsolidationService.generateValidTraces(declarationConsolide, codeClient);

    Assertions.assertEquals(2, traces.size());

    TraceConsolidation trace = traces.get(0);
    Assertions.assertEquals("65a7e13b61e31412b3279163", trace.getIdDeclaration());
    Assertions.assertEquals("0097810998", trace.getIdDeclarant());
    Assertions.assertEquals(date, trace.getDateExecution());
    Assertions.assertEquals(Constants.CARTE_DEMATERIALISEE, trace.getCodeService());
    Assertions.assertNull(trace.getCodeRejet());
    Assertions.assertEquals(NUMERO_BATCH_620, trace.getBatch());
    Assertions.assertNull(trace.getNomFichierARL());
    Assertions.assertEquals(codeClient, trace.getCodeClient());
    Assertions.assertEquals(COLLECTION_CONSOLIDATION_CARTES, trace.getCollectionConsolidee());

    trace = traces.get(1);
    Assertions.assertEquals("65a7e13b61e31412b3279163", trace.getIdDeclaration());
    Assertions.assertEquals("0097810998", trace.getIdDeclarant());
    Assertions.assertEquals(date, trace.getDateExecution());
    Assertions.assertEquals(Constants.CARTE_TP, trace.getCodeService());
    Assertions.assertNull(trace.getCodeRejet());
    Assertions.assertEquals(NUMERO_BATCH_620, trace.getBatch());
    Assertions.assertNull(trace.getNomFichierARL());
    Assertions.assertEquals(codeClient, trace.getCodeClient());
    Assertions.assertEquals(COLLECTION_CONSOLIDATION_CARTES, trace.getCollectionConsolidee());
  }

  @Test
  void generateRejectedTraceTest() throws IOException {
    ARLDataDto arlData =
        UtilsForTesting.createTFromJson(ARL_PATH + "arl-carte-demat.json", ARLDataDto.class);

    Date date = DateUtils.parseDate("2024-01-17 14:34:15", DateUtils.YYYY_MM_DD_HH_MM_SS);

    String nomFichierArl = "fichier_arl.csv";
    TraceConsolidation trace =
        traceConsolidationService.generateRejectedTrace(
            arlData, nomFichierArl, "BAL", NUMERO_BATCH_620);

    Assertions.assertEquals("65a7e13b61e31412b3279163", trace.getIdDeclaration());
    Assertions.assertEquals("0097810998", trace.getIdDeclarant());
    Assertions.assertEquals(date, trace.getDateExecution());
    Assertions.assertEquals(Constants.CARTE_DEMATERIALISEE, trace.getCodeService());
    Assertions.assertEquals("C17", trace.getCodeRejet());
    Assertions.assertEquals(NUMERO_BATCH_620, trace.getBatch());
    Assertions.assertEquals(nomFichierArl, trace.getNomFichierARL());
    Assertions.assertEquals("BAL", trace.getCodeClient());
    Assertions.assertNull(trace.getIdDeclarationConsolidee());
    Assertions.assertNull(trace.getCollectionConsolidee());
  }
}
