package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.arldata.ARLDataDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.CircuitService;
import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.job.batch.Rejection;
import com.cegedim.next.serviceeligibility.core.model.entity.Circuit;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ConstantesRejetsConsolidations;
import com.cegedim.next.serviceeligibility.core.services.common.batch.ARLService;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
public class ARLServiceTest {

  public static final String ID_DECLARANT = "0000401166";
  public static final String NOM_AMC = "BALOO";
  public static final String CODE_CIRCUIT = "01";
  public static final String CODE_PARTENAIRE = "TNR";
  public static final String EMETTEUR_DROITS = "AMC";
  public static final String OPERATEUR_PRINCIPAL = "IS";
  public static final String CARTE_TP = "CARTE_TP";
  public static final String CARTE_DEMAT = "CARTE_DEMAT";
  public static final String LIBELLE_CIRCUIT = "Droits hébergés BDD : AMC ==> BDD ==> TPG";
  @Autowired ARLService arlService;

  @Autowired ObjectMapper objectMapper;

  @MockBean CircuitService circuitService;

  private Date dateExecution;
  private String dateTraitement;

  @BeforeEach
  public void before() {
    dateExecution = new Date();
    dateTraitement = DateUtils.formatDate(dateExecution, DateUtils.YYYY_MM_DD_HH_MM_SS);

    List<Circuit> circuitList = new ArrayList<>();
    Circuit circuit = new Circuit();
    circuit.setCodeCircuit(CODE_CIRCUIT);
    circuit.setLibelleCircuit(LIBELLE_CIRCUIT);
    circuitList.add(circuit);
    Mockito.when(circuitService.findAllCircuits()).thenReturn(circuitList);
  }

  @Test
  void createARLTest() throws IOException {
    List<Rejection> rejectionList = new ArrayList<>();
    Declarant declarant = new Declarant();
    declarant.set_id(ID_DECLARANT);
    declarant.setNom(NOM_AMC);
    declarant.setCodeCircuit(CODE_CIRCUIT);
    declarant.setCodePartenaire(CODE_PARTENAIRE);
    declarant.setEmetteurDroits(EMETTEUR_DROITS);
    declarant.setOperateurPrincipal(OPERATEUR_PRINCIPAL);
    String codeDomaine = "PHAR";
    List<String> listPriorites = new ArrayList<>();
    listPriorites.add("1");
    listPriorites.add("2");
    Rejection rejection =
        new Rejection(
            ConstantesRejetsConsolidations.REJET_C12.toString(codeDomaine + " : " + listPriorites),
            createDeclarationFromJson("declarationARL-1.json"),
            dateExecution,
            CARTE_TP);
    rejectionList.add(rejection);
    Rejection rejection2 =
        new Rejection(
            ConstantesRejetsConsolidations.REJET_C16.toString(),
            createDeclarationFromJson("declarationARL-2.json"),
            dateExecution,
            CARTE_DEMAT);
    rejectionList.add(rejection2);
    List<ARLDataDto> ARLDataDtos = arlService.createARL(rejectionList, declarant);
    Assertions.assertTrue(CollectionUtils.isNotEmpty(ARLDataDtos));
    Assertions.assertEquals(2, ARLDataDtos.size());
    checkCommonData(ARLDataDtos);
    ARLDataDto ARLDataDto = ARLDataDtos.get(0);
    Assertions.assertEquals("0987654321098765432", ARLDataDto.getNomFichierNumDeclaration());
    Assertions.assertEquals("M", ARLDataDto.getMouvement());
    Assertions.assertEquals(
        ConstantesRejetsConsolidations.REJET_C12.getCode(), ARLDataDto.getRejet());
    Assertions.assertEquals(
        ConstantesRejetsConsolidations.REJET_C12.getTypeErreur(), ARLDataDto.getTypeRejet());
    Assertions.assertEquals("PHAR : [1, 2]", ARLDataDto.getValeurRejet());
    Assertions.assertEquals(
        ConstantesRejetsConsolidations.REJET_C12.getNiveau(), ARLDataDto.getNiveauRejet());
    Assertions.assertEquals(CARTE_TP, ARLDataDto.getCodeService());
    Assertions.assertEquals(
        ConstantesRejetsConsolidations.REJET_C12.getMessage(), ARLDataDto.getMotifRejet());
    ARLDataDto ARLDataDto1 = ARLDataDtos.get(1);
    Assertions.assertEquals("0987654321098765433", ARLDataDto1.getNomFichierNumDeclaration());
    Assertions.assertEquals("M", ARLDataDto1.getMouvement());
    Assertions.assertEquals(
        ConstantesRejetsConsolidations.REJET_C16.getCode(), ARLDataDto1.getRejet());
    Assertions.assertEquals(
        ConstantesRejetsConsolidations.REJET_C16.getTypeErreur(), ARLDataDto1.getTypeRejet());
    Assertions.assertEquals("TAUX DE COUVERTURE NON NUMERIQUE", ARLDataDto1.getValeurRejet());
    Assertions.assertEquals(
        ConstantesRejetsConsolidations.REJET_C16.getNiveau(), ARLDataDto1.getNiveauRejet());
    Assertions.assertEquals(CARTE_DEMAT, ARLDataDto1.getCodeService());
    Assertions.assertEquals(
        ConstantesRejetsConsolidations.REJET_C16.getMessage(), ARLDataDto1.getMotifRejet());
  }

  private void checkCommonData(List<ARLDataDto> ARLDataDtos) {
    for (ARLDataDto dto : ARLDataDtos) {
      Assertions.assertEquals(ID_DECLARANT, dto.getAmcNumber());
      Assertions.assertEquals(CODE_CIRCUIT, dto.getCodeCircuit());
      Assertions.assertEquals("IS", dto.getConvention());
      Assertions.assertEquals(EMETTEUR_DROITS, dto.getEmetteurDroits());
      Assertions.assertEquals("V", dto.getDroits());
      Assertions.assertEquals(EMETTEUR_DROITS, dto.getBoEmetteurDroits());
      Assertions.assertEquals("19791006", dto.getDateNaissance());
      Assertions.assertEquals(dateTraitement, dto.getDateTraitement());
      Assertions.assertEquals("BDD", dto.getDestinataireDroits());
      Assertions.assertEquals("MGEN", dto.getGroupeAssures());
      Assertions.assertEquals("IGestion", dto.getGestionnaireContrat());
      Assertions.assertEquals(LIBELLE_CIRCUIT, dto.getLibelleCircuit());
      Assertions.assertEquals("1701062498046", dto.getNir());
      Assertions.assertEquals("CONSOW0", dto.getNumeroContrat());
      Assertions.assertEquals("MBA-000", dto.getNumeroPersonne());

      Assertions.assertEquals("POPI", dto.getNom());
      Assertions.assertEquals("JEAN PIERRE", dto.getPrenom());
      Assertions.assertEquals("Employes", dto.getRegroupement());
      Assertions.assertEquals("GEO-FRA-OCC", dto.getRegroupementDetaille());
      Assertions.assertEquals("1", dto.getRangNaissance());
    }
  }

  private Declaration createDeclarationFromJson(String fileName) throws IOException {
    String filePath = "src/test/resources/620-declarations/";
    String declarationAsString = new String(Files.readAllBytes(Paths.get(filePath + fileName)));
    return createDeclaration(declarationAsString);
  }

  private Declaration createDeclaration(String json) throws JsonProcessingException {
    return objectMapper.readValue(json, Declaration.class);
  }
}
