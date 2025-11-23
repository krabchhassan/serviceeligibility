package com.cegedim.next.serviceeligibility.core.mapper;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
public class MapperContratTest {

  public static final String DEBUT = "2020-01-01";
  public static final String FIN = "2020-12-31";

  @Test
  void mapCommonToV6Test_existing_contract_null() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    ContratAIV5 contractFromFile =
        objectMapper.readValue(
            new File("src/test/resources/contractV5/contract1.json"), ContratAIV5.class);

    ContratAIV6 contratAIV6 = MapperContrat.mapCommonToV6(contractFromFile);
    Assertions.assertEquals("2", contratAIV6.getId());
    Assertions.assertEquals("03", contratAIV6.getTraceId());
    Assertions.assertEquals("0097810998", contratAIV6.getIdDeclarant());
    Assertions.assertEquals("8343484392", contratAIV6.getNumero());
    Assertions.assertEquals("6", contratAIV6.getVersion());

    Assertions.assertEquals("SLOK4983989", contratAIV6.getNumeroExterne());
    Assertions.assertEquals("83747438", contratAIV6.getNumeroAdherent());
    Assertions.assertEquals("TBT83747438", contratAIV6.getNumeroAdherentComplet());
    Assertions.assertEquals("2020-01-22", contratAIV6.getDateSouscription());
    Assertions.assertNull(contratAIV6.getDateResiliation());
    Assertions.assertEquals("Courtier & Co", contratAIV6.getApporteurAffaire());
    Assertions.assertEquals(
        DEBUT, contratAIV6.getPeriodesContratResponsableOuvert().get(0).getDebut());
    Assertions.assertEquals(FIN, contratAIV6.getPeriodesContratResponsableOuvert().get(0).getFin());
    Assertions.assertEquals("Cadres", contratAIV6.getCritereSecondaire());
    Assertions.assertEquals("CAD", contratAIV6.getCritereSecondaireDetaille());
    Assertions.assertTrue(contratAIV6.getIsContratIndividuel());
    Assertions.assertEquals("IGestion", contratAIV6.getGestionnaire());
    Assertions.assertEquals("Base", contratAIV6.getQualification());
    Assertions.assertEquals("1", contratAIV6.getOrdrePriorisation());
    Assertions.assertEquals("KLESIA CARCEPT", contratAIV6.getSocieteEmettrice());
  }

  @Test
  void mapCommonToV6Test() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    ContratAIV6 existingContract =
        objectMapper.readValue(
            new File("src/test/resources/contractV5/contract1.json"), ContratAIV6.class);
    ContratAIV5 contractFromFile =
        objectMapper.readValue(
            new File("src/test/resources/contractV5/contract2.json"), ContratAIV5.class);

    ContratAIV6 contratAIV6 = MapperContrat.mapCommonToV6(existingContract, contractFromFile);
    Assertions.assertEquals("2", contratAIV6.getId());
    Assertions.assertEquals("03", contratAIV6.getTraceId());
    Assertions.assertEquals("0097810998", contratAIV6.getIdDeclarant());
    Assertions.assertEquals("8343484392", contratAIV6.getNumero());
    Assertions.assertEquals("SLOK4983990", contratAIV6.getNumeroExterne());

    Assertions.assertEquals("6", contratAIV6.getVersion());

    checkNumeroAdherent(contratAIV6);
    Assertions.assertEquals("2020-01-23", contratAIV6.getDateSouscription());
    Assertions.assertEquals(FIN, contratAIV6.getDateResiliation());
    Assertions.assertEquals("Courtier & Co", contratAIV6.getApporteurAffaire());
    checkPeriodesContratResponsableOuvert(contratAIV6);
    Assertions.assertEquals("Cadres", contratAIV6.getCritereSecondaire());
    Assertions.assertEquals("CAD", contratAIV6.getCritereSecondaireDetaille());
    Assertions.assertTrue(contratAIV6.getIsContratIndividuel());
    Assertions.assertEquals("IGestion", contratAIV6.getGestionnaire());
    Assertions.assertEquals("Base", contratAIV6.getQualification());
    Assertions.assertEquals("1", contratAIV6.getOrdrePriorisation());
    Assertions.assertEquals("KLESIA CARCEPT", contratAIV6.getSocieteEmettrice());

    Assertions.assertNull(contratAIV6.getContexteTiersPayant());
    Assertions.assertEquals(1, contratAIV6.getAssures().size());
    Assertions.assertNull(contratAIV6.getCodeOc());
    Assertions.assertNull(contratAIV6.getPeriodesSuspension());
    checkPeriodesContratCMU(contratAIV6);
    Assertions.assertNull(contratAIV6.getNomFichierOrigine());
  }

  private static void checkNumeroAdherent(ContratAIV6 contratAIV6) {
    Assertions.assertEquals("83747440", contratAIV6.getNumeroAdherent());
    Assertions.assertEquals("TBT83747440", contratAIV6.getNumeroAdherentComplet());
  }

  private static void checkPeriodesContratResponsableOuvert(ContratAIV6 contratAIV6) {
    Assertions.assertEquals(
        DEBUT, contratAIV6.getPeriodesContratResponsableOuvert().get(0).getDebut());
    Assertions.assertEquals(FIN, contratAIV6.getPeriodesContratResponsableOuvert().get(0).getFin());
  }

  private static void checkPeriodesContratCMU(ContratAIV6 contratAIV6) {
    Assertions.assertEquals(
        DEBUT, contratAIV6.getPeriodesContratCMUOuvert().get(0).getPeriode().getDebut());
    Assertions.assertEquals(
        FIN, contratAIV6.getPeriodesContratCMUOuvert().get(0).getPeriode().getFin());
    Assertions.assertEquals("CMU", contratAIV6.getPeriodesContratCMUOuvert().get(0).getCode());
  }
}
