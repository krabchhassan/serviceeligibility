package com.cegedim.next.serviceeligibility.core.services.bdd;

import static org.mockito.ArgumentMatchers.any;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails.*;
import com.cegedim.next.serviceeligibility.core.elast.contract.ContratElastic;
import com.cegedim.next.serviceeligibility.core.elast.contract.ElasticHistorisationContractService;
import com.cegedim.next.serviceeligibility.core.mapper.MapperDomaineDroitContractDto;
import com.cegedim.next.serviceeligibility.core.model.domain.TypeConventionnement;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ConventionnementContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PeriodeDroitContractTP;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.TypePeriode;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.kafka.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratV5;
import com.cegedim.next.serviceeligibility.core.services.contracttp.ContractService;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RequestValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfig.class)
class BeneficiaryDetailsServiceTest {
  @Autowired ObjectMapper objectMapper;

  @Autowired private BeneficiaryDetailsService beneficiaryDetailsService;

  @Autowired private MongoTemplate mongoTemplate;

  @Autowired private ElasticsearchOperations elasticSearch;

  @MockBean private ElasticHistorisationContractService elasticHistorisationContractService;

  @MockBean private BeneficiaryService beneficiaryService;
  @MockBean private ContractService contractService;

  private Declaration declaration001;
  private Declaration declaration002;
  private Declaration declaration003;

  private PeriodeDroitContractTP createPeriodeDroitContractTP(
      TypePeriode typePeriode,
      String debut,
      String fin,
      String libelle,
      String modeObtention,
      String motif) {
    PeriodeDroitContractTP periodeDroitContractTP = new PeriodeDroitContractTP();
    periodeDroitContractTP.setTypePeriode(typePeriode);
    periodeDroitContractTP.setPeriodeDebut(debut);
    periodeDroitContractTP.setPeriodeFin(fin);
    periodeDroitContractTP.setLibelleEvenement(libelle);
    periodeDroitContractTP.setModeObtention(modeObtention);
    periodeDroitContractTP.setMotifEvenement(motif);
    return periodeDroitContractTP;
  }

  private TypeConventionnement createTypeConventionnement(String code, String libelle) {
    TypeConventionnement typeConventionnement = new TypeConventionnement();
    typeConventionnement.setCode(code);
    typeConventionnement.setLibelle(libelle);
    return typeConventionnement;
  }

  private ConventionnementContrat createConventionnementContrat(
      int priorite, TypeConventionnement typeConventionnement, List<Periode> periodes) {
    ConventionnementContrat conventionnementContrat = new ConventionnementContrat();
    conventionnementContrat.setPriorite(priorite);
    conventionnementContrat.setTypeConventionnement(typeConventionnement);
    conventionnementContrat.setPeriodes(periodes);
    return conventionnementContrat;
  }

  @PostConstruct
  public void initDeclarations() throws IOException {
    this.declaration001 = getDeclaration("declarationDetailsService001.json");
    this.declaration002 = getDeclaration("declarationDetailsService002.json");
    this.declaration003 = getDeclaration("declarationDetailsService003.json");
  }

  public void initMocks() throws IOException {
    // contrat_001
    // => declaration 001
    // contrat_002
    // => declaration 002
    // => declaration 003
    Mockito.when(mongoTemplate.findById("001", Declaration.class)).thenReturn(this.declaration001);
    Mockito.when(mongoTemplate.findById("002", Declaration.class)).thenReturn(this.declaration002);
    Mockito.when(mongoTemplate.findById("003", Declaration.class)).thenReturn(this.declaration003);

    // 0000401166 - baloo
    Declarant baloo = readObjectFromFile("src/test/resources/declarantBaloo.json", Declarant.class);
    Mockito.when(mongoTemplate.findOne(any(Query.class), Mockito.eq(Declarant.class)))
        .thenReturn(baloo);
  }

  private BenefAIV5 getBenef() {
    BenefAIV5 benef = new BenefAIV5();
    Periode periode = new Periode("2020-01-01", null);
    String traceId = "5ecf664a4b56700001c54fda";
    benef.setTraceId(traceId);
    benef.setIdClientBO("test@cegedim.com");
    benef.setNumeroAdherent("11223344");

    Amc amc = new Amc();
    amc.setIdDeclarant("0000401166");
    amc.setLibelle("Amc de test");
    benef.setAmc(amc);

    IdentiteContrat identite = new IdentiteContrat();
    Nir nir = new Nir();
    nir.setCode("1791062498047");
    nir.setCle("45");
    identite.setNir(nir);
    List<NirRattachementRO> affiliationsRO = new ArrayList<>();
    NirRattachementRO rattRo = new NirRattachementRO();
    rattRo.setNir(nir);
    rattRo.setPeriode(periode);
    rattRo.setRattachementRO(new RattachementRO("01", "624", "1236"));
    affiliationsRO.add(rattRo);
    identite.setAffiliationsRO(affiliationsRO);
    identite.setDateNaissance("19791024");
    identite.setRangNaissance("1");
    identite.setNumeroPersonne("123456789");
    identite.setRefExternePersonne("13032066654465");
    benef.setIdentite(identite);

    List<ContratV5> contrats = new ArrayList<>();
    ContratV5 contrat = new ContratV5();
    contrat.setCodeEtat("V");
    contrat.setNumeroContrat("32103206664");
    contrats.add(contrat);
    benef.setContrats(contrats);
    List<String> services = new ArrayList<>();
    services.add("ServicePrestation");
    benef.setServices(services);
    benef.setKey(benef.getAmc().getIdDeclarant() + "-" + benef.getIdentite().getNumeroPersonne());

    return benef;
  }

  @Test
  void shouldValidateRequestTP() {
    String personNumber = "123456789";
    String insurerId = "0000401166";

    Assertions.assertDoesNotThrow(
        () -> beneficiaryDetailsService.validateRequestTP(personNumber, insurerId, null, null));

    String declarationId = "0654845454545e424234b32";
    String contractNumber = "test-001";

    Assertions.assertDoesNotThrow(
        () ->
            beneficiaryDetailsService.validateRequestTP(
                personNumber, insurerId, List.of(declarationId), contractNumber));
  }

  @Test
  void shouldNotValidateRequestTP() {
    String personNumber = "123456789";
    String insurerId = "0000401166";
    String declarationId = "0654845454545e424234b32";
    String contractNumber = "test-001";
    Assertions.assertThrows(
        RequestValidationException.class,
        () ->
            beneficiaryDetailsService.validateRequestTP(
                personNumber, insurerId, List.of(declarationId), null));

    Assertions.assertThrows(
        RequestValidationException.class,
        () ->
            beneficiaryDetailsService.validateRequestTP(
                personNumber, insurerId, null, contractNumber));
  }

  @Test
  void getDeclarationDetailsTest() throws IOException {
    initMocks();
    // contrat_001
    testDeclarationDetails(List.of("002", "003"), "contrat_001", 0);
    testDeclarationDetails(List.of("001", "002", "003"), "contrat_001", 1);

    // contrat_002
    testDeclarationDetails(List.of("001"), "contrat_002", 0);
    testDeclarationDetails(List.of("003"), "contrat_002", 1);
    testDeclarationDetails(List.of("002", "003"), "contrat_002", 2);
    testDeclarationDetails(List.of("001", "002", "003"), "contrat_002", 2);
  }

  @Test
  void getPartialResponseTest() throws IOException {
    initMocks();
    String personNumber = "123456789";
    String subscriberId = "123456789";
    String insurerId = "0000401166";
    List<ContratElastic> contratElasticList = new ArrayList<>();
    BenefAIV5 benef = getBenef();
    ContractTP contractTP = new ContractTP();

    Mockito.when(beneficiaryService.getBeneficiaryByKey(Mockito.anyString())).thenReturn(benef);
    Mockito.when(
            contractService.getContract(
                Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
        .thenReturn(contractTP);
    Mockito.when(elasticHistorisationContractService.findByNumeroContrat(Mockito.anyString()))
        .thenReturn(contratElasticList);

    // contrat_001
    testPartialResponse("001", "contrat_001", personNumber, insurerId, subscriberId, 1);
    testPartialResponse("002", "contrat_001", personNumber, insurerId, subscriberId, 0);
    testPartialResponse("003", "contrat_001", personNumber, insurerId, subscriberId, 0);

    // contrat_002
    testPartialResponse("001", "contrat_002", personNumber, insurerId, subscriberId, 0);
    testPartialResponse("002", "contrat_002", personNumber, insurerId, subscriberId, 1);
    testPartialResponse("003", "contrat_002", personNumber, insurerId, subscriberId, 1);
  }

  @Test
  void fullResponseShouldFailIfElasticDown() throws IOException {
    initMocks();
    String personNumber = "123456789";
    String insurerId = "0000401166";
    Mockito.when(
            elasticSearch.get(
                Mockito.anyString(), Mockito.eq(BenefAIV5.class), any(IndexCoordinates.class)))
        .thenReturn(null);
    Mockito.when(
            mongoTemplate.aggregate(
                any(Aggregation.class), Mockito.eq("declarations"), Mockito.eq(Declaration.class)))
        .thenReturn(new AggregationResults<>(List.of(this.declaration001), new Document()));
    Mockito.when(mongoTemplate.find(any(Query.class), Mockito.eq(Declaration.class)))
        .thenReturn(List.of(this.declaration001));

    Assertions.assertThrows(
        RequestValidationException.class,
        () -> beneficiaryDetailsService.getFullResponse(personNumber, insurerId));
  }

  @Test
  void fullResponseShouldFailIfDeclarationNotFound() throws IOException {
    initMocks();
    String personNumber = "123456789";
    String insurerId = "0000401166";
    Mockito.when(
            elasticSearch.get(
                Mockito.anyString(), Mockito.eq(BenefAIV5.class), any(IndexCoordinates.class)))
        .thenReturn(new BenefAIV5());
    Mockito.when(
            mongoTemplate.aggregate(
                any(Aggregation.class), Mockito.eq("declarations"), Mockito.eq(Declaration.class)))
        .thenReturn(new AggregationResults<>(List.of(), new Document()));
    Mockito.when(mongoTemplate.find(any(Query.class), Mockito.eq(Declaration.class)))
        .thenReturn(List.of());

    Assertions.assertThrows(
        RequestValidationException.class,
        () -> beneficiaryDetailsService.getFullResponse(personNumber, insurerId));
  }

  @Test
  void fullResponseShouldFailIfHistoryNotFound() {
    String personNumber = "123456789";
    String insurerId = "0000401166";
    Mockito.when(
            elasticSearch.get(
                Mockito.anyString(), Mockito.eq(BenefAIV5.class), any(IndexCoordinates.class)))
        .thenReturn(new BenefAIV5());
    Mockito.when(
            mongoTemplate.aggregate(
                any(Aggregation.class), Mockito.eq("declarations"), Mockito.eq(Declaration.class)))
        .thenReturn(new AggregationResults<>(List.of(this.declaration001), new Document()));
    Mockito.when(mongoTemplate.find(any(Query.class), Mockito.eq(Declaration.class)))
        .thenReturn(List.of());

    Assertions.assertThrows(
        RequestValidationException.class,
        () -> beneficiaryDetailsService.getFullResponse(personNumber, insurerId));
  }

  /*
   * @Test void getFullResponseTest() { String personNumber = "123456789"; String
   * insurerId = "0000401166"; List<ContratElastic> contratElasticList = new
   * ArrayList<>(); BenefAIV5 benef = new BenefAIV5();
   * benef.setKey("0000401166-123456789"); Amc amc = new Amc();
   * amc.setIdDeclarant("0000401166"); benef.setAmc(amc);
   * benef.setNumeroAdherent("123456789"); ContratV5 contratV5 = new ContratV5();
   * contratV5.setNumeroContrat("123456789");
   * benef.setContrats(List.of(contratV5)); Mockito .when( elasticSearch .get(
   * Mockito.anyString(), Mockito.eq(BenefAIV5.class),
   * Mockito.any(IndexCoordinates.class))) .thenReturn(new BenefAIV5()); Mockito
   * .when( mongoTemplate .aggregate( Mockito.any(Aggregation.class),
   * Mockito.eq("declarations"), Mockito.eq(Declaration.class))) .thenReturn(new
   * AggregationResults<>(List.of(this.declaration001), new Document())); Mockito
   * .when(mongoTemplate.find(Mockito.any(Query.class),
   * Mockito.eq(Declaration.class))) .thenReturn(List.of(this.declaration001));
   *
   * Mockito .when(mongoTemplate.findOne(Mockito.any(Query.class),
   * Mockito.eq(BenefAIV5.class), Mockito.eq("beneficiaires")))
   * .thenReturn(benef);
   *
   * Mockito
   * .when(elasticHistorisationContractService.findByNumeroContrat(Mockito.
   * anyString())) .thenReturn(contratElasticList);
   *
   * BeneficiaryDetailsDto response =
   * beneficiaryDetailsService.getFullResponse(personNumber, insurerId);
   * Assertions.assertNotNull(response.getBenefDetails());
   * Assertions.assertNotNull(response.getDeclarationToOpen());
   * Assertions.assertNotNull(response.getDeclarationDetails()); // No further
   * tests because everything relies on Mockito }
   */

  private void testDeclarationDetails(
      List<String> declarationIds, String contractNumber, int expectedSize) {
    DeclarationDetailsDto declarationDetails;

    if (expectedSize == 0) {
      Assertions.assertThrows(
          RequestValidationException.class,
          () ->
              beneficiaryDetailsService.getDeclarationDetails(
                  Pair.of(false, declarationIds), contractNumber, Collections.emptyList()));
    } else { // expectedSize != 0
      declarationDetails =
          beneficiaryDetailsService.getDeclarationDetails(
              Pair.of(false, declarationIds), contractNumber, Collections.emptyList());
      Assertions.assertEquals(expectedSize, declarationDetails.getInfosAssureDtos().size());
    }
  }

  private void testPartialResponse(
      String declarationId,
      String contractNumber,
      String personNumber,
      String insurerId,
      String subscriberId,
      int expectedSize) {
    BeneficiaryDetailsDto response;

    if (expectedSize == 0) {
      Assertions.assertThrows(
          RequestValidationException.class,
          () ->
              beneficiaryDetailsService.getPartialResponse(
                  List.of(declarationId), contractNumber, subscriberId, personNumber, insurerId));
    } else { // expectedSize != 0
      response =
          beneficiaryDetailsService.getPartialResponse(
              List.of(declarationId), contractNumber, subscriberId, personNumber, insurerId);

      Assertions.assertNull(response.getBenefDetails());
      Assertions.assertNull(response.getDeclarationToOpen());
      Assertions.assertEquals(
          expectedSize, response.getDeclarationDetails().getInfosAssureDtos().size());
    }
  }

  @Test
  void getHistoConsoTest() throws IOException {
    initMocks();
    List<ContratElastic> contratElasticList = new ArrayList<>();
    ContratElastic contratElastic1 = new ContratElastic();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD_HH_MM_SS);
    ContractTP contrat1 = new ContractTP();
    contrat1.setDateConsolidation(LocalDateTime.parse("2024-06-30 10:00:00", formatter));
    contratElastic1.setContrat(contrat1);
    contratElastic1.setDateSauvegarde(LocalDateTime.parse("2024-06-30 10:00:00", formatter));
    contratElasticList.add(contratElastic1);
    ContratElastic contratElastic2 = new ContratElastic();
    ContractTP contrat2 = new ContractTP();
    contrat2.setDateConsolidation(LocalDateTime.parse("2024-06-30 23:59:55", formatter));
    contratElastic2.setContrat(contrat2);
    contratElastic2.setDateSauvegarde(LocalDateTime.parse("2024-06-30 23:59:55", formatter));
    contratElasticList.add(contratElastic2);
    ContratElastic contratElastic3 = new ContratElastic();
    ContractTP contrat3 = new ContractTP();
    contrat3.setDateConsolidation(LocalDateTime.parse("2024-06-29 11:00:00", formatter));
    contratElastic3.setContrat(contrat3);
    contratElastic3.setDateSauvegarde(LocalDateTime.parse("2024-06-29 11:00:00", formatter));
    contratElasticList.add(contratElastic3);

    Mockito.when(
            elasticHistorisationContractService.findByIdDeclarantContratAdherentAndNumeroPersonne(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any(Pageable.class)))
        .thenReturn(contratElasticList);

    ConsolidatedContractHistory consolidatedContractHistory =
        beneficiaryDetailsService.getHistoriqueConso(
            "idDeclarant", "numeroContrat", "numeroAdherent", "numeroPersonne", 0);
    List<ConsolidatedContractDto> consolidatedContractDtoList =
        consolidatedContractHistory.getConsolidatedContracts();
    Assertions.assertEquals(
        "30/06/2024 23:59", consolidatedContractDtoList.get(0).getDateConsolidation());
  }

  private Declaration getDeclaration(String file) throws IOException {
    final String filePath = "src/test/resources/declarations/";
    return readObjectFromFile(filePath + file, Declaration.class);
  }

  private <T> T readObjectFromFile(String filePath, Class<T> clazz) throws IOException {
    String declarationAsString = new String(Files.readAllBytes(Paths.get(filePath)));
    return objectMapper.readValue(declarationAsString, clazz);
  }

  @Test
  void mapConventionnementsCasSimpleTest() {
    PeriodeDroitContractTP periodeDroitContractTP =
        createPeriodeDroitContractTP(
            TypePeriode.OFFLINE,
            "2023/01/01",
            "2023/12/31",
            "Initialisation des droits",
            "O",
            "IN");

    // KA - 0
    // 01/07 => 31/07
    // IS - 1
    // 01/05 => 31/07
    // CB - 0
    // 01/05 => 30/06
    List<ConventionnementContrat> conventionnementContrats = new ArrayList<>();
    conventionnementContrats.add(
        createConventionnementContrat(
            0,
            createTypeConventionnement("KA", "KA"),
            new ArrayList<>(Collections.singletonList(new Periode("2023/07/01", "2023/07/31")))));
    conventionnementContrats.add(
        createConventionnementContrat(
            1,
            createTypeConventionnement("IS", "IS"),
            new ArrayList<>(Collections.singletonList(new Periode("2023/05/01", "2023/07/31")))));
    conventionnementContrats.add(
        createConventionnementContrat(
            0,
            createTypeConventionnement("CB", "CB"),
            new ArrayList<>(Collections.singletonList(new Periode("2023/05/01", "2023/06/30")))));

    List<ConventionnementContract> resul =
        MapperDomaineDroitContractDto.mapConventionnements(
            conventionnementContrats, periodeDroitContractTP);

    PeriodeContractDto expectedPeriodeKAIS = new PeriodeContractDto();
    expectedPeriodeKAIS.setDebut("2023/07/01");
    expectedPeriodeKAIS.setFin("2023/07/31");
    PeriodeContractDto expectedPeriodeCBIS = new PeriodeContractDto();
    expectedPeriodeCBIS.setDebut("2023/05/01");
    expectedPeriodeCBIS.setFin("2023/06/30");
    Assertions.assertEquals(2, resul.size());

    // 01/07 => 31/07
    // KA - 0
    // IS - 1
    Assertions.assertEquals(expectedPeriodeKAIS, resul.get(0).getPeriode());
    Assertions.assertEquals(
        "KA", resul.get(0).getTypeConventionnements().get(0).getTypeConventionnement().getCode());
    Assertions.assertEquals(0, resul.get(0).getTypeConventionnements().get(0).getPriorite());
    Assertions.assertEquals(
        "IS", resul.get(0).getTypeConventionnements().get(1).getTypeConventionnement().getCode());
    Assertions.assertEquals(1, resul.get(0).getTypeConventionnements().get(1).getPriorite());

    // 01/05 => 30/06
    // CB - 0
    // IS - 1
    Assertions.assertEquals(expectedPeriodeCBIS, resul.get(1).getPeriode());
    Assertions.assertEquals(
        "CB", resul.get(1).getTypeConventionnements().get(0).getTypeConventionnement().getCode());
    Assertions.assertEquals(0, resul.get(1).getTypeConventionnements().get(0).getPriorite());
    Assertions.assertEquals(
        "IS", resul.get(1).getTypeConventionnements().get(1).getTypeConventionnement().getCode());
    Assertions.assertEquals(1, resul.get(1).getTypeConventionnements().get(1).getPriorite());
  }

  @Test
  void mapConventionnementsCasComplexeTest() {
    PeriodeDroitContractTP periodeDroitContractTP =
        createPeriodeDroitContractTP(
            TypePeriode.OFFLINE,
            "2023/01/01",
            "2023/12/31",
            "Initialisation des droits",
            "O",
            "IN");

    // KA - 0
    // 01/07 => 31/12
    // IS - 1
    // 01/05 => 31/07
    // CB - 0
    // 01/05 => 30/06
    List<ConventionnementContrat> conventionnementContrats = new ArrayList<>();
    conventionnementContrats.add(
        createConventionnementContrat(
            0,
            createTypeConventionnement("KA", "KA"),
            new ArrayList<>(Collections.singletonList(new Periode("2023/07/01", "2023/12/31")))));
    conventionnementContrats.add(
        createConventionnementContrat(
            1,
            createTypeConventionnement("IS", "IS"),
            new ArrayList<>(Collections.singletonList(new Periode("2023/05/01", "2023/07/31")))));
    conventionnementContrats.add(
        createConventionnementContrat(
            0,
            createTypeConventionnement("CB", "CB"),
            new ArrayList<>(Collections.singletonList(new Periode("2023/05/01", "2023/06/30")))));

    List<ConventionnementContract> resul =
        MapperDomaineDroitContractDto.mapConventionnements(
            conventionnementContrats, periodeDroitContractTP);

    PeriodeContractDto expectedPeriodeKAIS = new PeriodeContractDto();
    expectedPeriodeKAIS.setDebut("2023/07/01");
    expectedPeriodeKAIS.setFin("2023/07/31");
    PeriodeContractDto expectedPeriodeCBIS = new PeriodeContractDto();
    expectedPeriodeCBIS.setDebut("2023/05/01");
    expectedPeriodeCBIS.setFin("2023/06/30");
    PeriodeContractDto expectedPeriodeKA = new PeriodeContractDto();
    expectedPeriodeKA.setDebut("2023/08/01");
    expectedPeriodeKA.setFin("2023/12/31");
    Assertions.assertEquals(3, resul.size());

    // 01/08 => 31/12
    // KA - 0
    Assertions.assertEquals(expectedPeriodeKA, resul.get(0).getPeriode());
    Assertions.assertEquals(
        "KA", resul.get(0).getTypeConventionnements().get(0).getTypeConventionnement().getCode());
    Assertions.assertEquals(0, resul.get(0).getTypeConventionnements().get(0).getPriorite());

    // 01/07 => 31/07
    // KA - 0
    // IS - 1
    Assertions.assertEquals(expectedPeriodeKAIS, resul.get(1).getPeriode());
    Assertions.assertEquals(
        "KA", resul.get(1).getTypeConventionnements().get(0).getTypeConventionnement().getCode());
    Assertions.assertEquals(0, resul.get(1).getTypeConventionnements().get(0).getPriorite());
    Assertions.assertEquals(
        "IS", resul.get(1).getTypeConventionnements().get(1).getTypeConventionnement().getCode());
    Assertions.assertEquals(1, resul.get(1).getTypeConventionnements().get(1).getPriorite());

    // 01/05 => 30/06
    // CB - 0
    // IS - 1
    Assertions.assertEquals(expectedPeriodeCBIS, resul.get(2).getPeriode());
    Assertions.assertEquals(
        "CB", resul.get(2).getTypeConventionnements().get(0).getTypeConventionnement().getCode());
    Assertions.assertEquals(0, resul.get(2).getTypeConventionnements().get(0).getPriorite());
    Assertions.assertEquals(
        "IS", resul.get(2).getTypeConventionnements().get(1).getTypeConventionnement().getCode());
    Assertions.assertEquals(1, resul.get(2).getTypeConventionnements().get(1).getPriorite());
  }

  @Test
  void mapConventionnementsCasComplexe2Test() {
    PeriodeDroitContractTP periodeDroitContractTP =
        createPeriodeDroitContractTP(
            TypePeriode.OFFLINE,
            "2023/01/01",
            "2023/12/31",
            "Initialisation des droits",
            "O",
            "IN");

    // KA - 0
    // 01/07 => 31/12
    // IS - 1
    // 01/05 => 31/12
    // CB - 0
    // 01/05 => 30/06
    List<ConventionnementContrat> conventionnementContrats = new ArrayList<>();
    conventionnementContrats.add(
        createConventionnementContrat(
            0,
            createTypeConventionnement("KA", "KA"),
            new ArrayList<>(Collections.singletonList(new Periode("2023/07/01", "2023/12/31")))));
    conventionnementContrats.add(
        createConventionnementContrat(
            1,
            createTypeConventionnement("IS", "IS"),
            new ArrayList<>(Collections.singletonList(new Periode("2023/05/01", "2023/12/31")))));
    conventionnementContrats.add(
        createConventionnementContrat(
            0,
            createTypeConventionnement("CB", "CB"),
            new ArrayList<>(Collections.singletonList(new Periode("2023/05/01", "2023/06/30")))));

    List<ConventionnementContract> resul =
        MapperDomaineDroitContractDto.mapConventionnements(
            conventionnementContrats, periodeDroitContractTP);

    PeriodeContractDto expectedPeriodeKAIS = new PeriodeContractDto();
    expectedPeriodeKAIS.setDebut("2023/07/01");
    expectedPeriodeKAIS.setFin("2023/12/31");
    PeriodeContractDto expectedPeriodeCBIS = new PeriodeContractDto();
    expectedPeriodeCBIS.setDebut("2023/05/01");
    expectedPeriodeCBIS.setFin("2023/06/30");
    Assertions.assertEquals(2, resul.size());

    // 01/07 => 31/12
    // KA - 0
    // IS - 1
    Assertions.assertEquals(expectedPeriodeKAIS, resul.get(0).getPeriode());
    Assertions.assertEquals(
        "KA", resul.get(0).getTypeConventionnements().get(0).getTypeConventionnement().getCode());
    Assertions.assertEquals(0, resul.get(0).getTypeConventionnements().get(0).getPriorite());
    Assertions.assertEquals(
        "IS", resul.get(0).getTypeConventionnements().get(1).getTypeConventionnement().getCode());
    Assertions.assertEquals(1, resul.get(0).getTypeConventionnements().get(1).getPriorite());

    // 01/05 => 30/06
    // CB - 0
    // IS - 1
    Assertions.assertEquals(expectedPeriodeCBIS, resul.get(1).getPeriode());
    Assertions.assertEquals(
        "CB", resul.get(1).getTypeConventionnements().get(0).getTypeConventionnement().getCode());
    Assertions.assertEquals(0, resul.get(1).getTypeConventionnements().get(0).getPriorite());
    Assertions.assertEquals(
        "IS", resul.get(1).getTypeConventionnements().get(1).getTypeConventionnement().getCode());
    Assertions.assertEquals(1, resul.get(1).getTypeConventionnements().get(1).getPriorite());
  }

  @Test
  void mapConventionnementsCasCompletTest() {
    PeriodeDroitContractTP periodeDroitContractTP =
        createPeriodeDroitContractTP(
            TypePeriode.OFFLINE,
            "2023/01/01",
            "2023/12/31",
            "Initialisation des droits",
            "O",
            "IN");

    // KA - 0
    // 01/01 => 28/02
    // 01/07 => 31/07
    // IS - 1
    // 01/01 => 28/02
    // 01/05 => 31/08
    // VM - 0
    // 01/03 => 30/04
    // KA - 1
    // 01/03 => 30/04
    // CB - 0
    // 01/05 => 30/06
    // IS - 0
    // 01/08 => 31/12
    List<ConventionnementContrat> conventionnementContrats = new ArrayList<>();
    conventionnementContrats.add(
        createConventionnementContrat(
            0,
            createTypeConventionnement("KA", "KA"),
            Arrays.asList(
                new Periode("2023/01/01", "2023/02/28"), new Periode("2023/07/01", "2023/07/31"))));
    conventionnementContrats.add(
        createConventionnementContrat(
            1,
            createTypeConventionnement("IS", "IS"),
            Arrays.asList(
                new Periode("2023/01/01", "2023/02/28"), new Periode("2023/05/01", "2023/08/31"))));
    conventionnementContrats.add(
        createConventionnementContrat(
            0,
            createTypeConventionnement("VM", "VM"),
            new ArrayList<>(Collections.singletonList(new Periode("2023/03/01", "2023/04/30")))));
    conventionnementContrats.add(
        createConventionnementContrat(
            1,
            createTypeConventionnement("KA", "KA"),
            new ArrayList<>(Collections.singletonList(new Periode("2023/03/01", "2023/04/30")))));
    conventionnementContrats.add(
        createConventionnementContrat(
            0,
            createTypeConventionnement("CB", "CB"),
            new ArrayList<>(Collections.singletonList(new Periode("2023/05/01", "2023/06/30")))));
    conventionnementContrats.add(
        createConventionnementContrat(
            0,
            createTypeConventionnement("IS", "IS"),
            new ArrayList<>(Collections.singletonList(new Periode("2023/08/01", "2023/12/31")))));

    List<ConventionnementContract> resul =
        MapperDomaineDroitContractDto.mapConventionnements(
            conventionnementContrats, periodeDroitContractTP);

    PeriodeContractDto expectedPeriodeIS = new PeriodeContractDto();
    expectedPeriodeIS.setDebut("2023/09/01");
    expectedPeriodeIS.setFin("2023/12/31");
    PeriodeContractDto expectedPeriodeISIS = new PeriodeContractDto();
    expectedPeriodeISIS.setDebut("2023/08/01");
    expectedPeriodeISIS.setFin("2023/08/31");
    PeriodeContractDto expectedPeriodeKAIS = new PeriodeContractDto();
    expectedPeriodeKAIS.setDebut("2023/07/01");
    expectedPeriodeKAIS.setFin("2023/07/31");
    PeriodeContractDto expectedPeriodeCBIS = new PeriodeContractDto();
    expectedPeriodeCBIS.setDebut("2023/05/01");
    expectedPeriodeCBIS.setFin("2023/06/30");
    PeriodeContractDto expectedPeriodeVMKA = new PeriodeContractDto();
    expectedPeriodeVMKA.setDebut("2023/03/01");
    expectedPeriodeVMKA.setFin("2023/04/30");
    PeriodeContractDto expectedPeriodeKAIS2 = new PeriodeContractDto();
    expectedPeriodeKAIS2.setDebut("2023/01/01");
    expectedPeriodeKAIS2.setFin("2023/02/28");
    Assertions.assertEquals(6, resul.size());

    // 01/09 => 31/12
    // IS - 0
    Assertions.assertEquals(expectedPeriodeIS, resul.get(0).getPeriode());
    Assertions.assertEquals(
        "IS", resul.get(0).getTypeConventionnements().get(0).getTypeConventionnement().getCode());
    Assertions.assertEquals(0, resul.get(0).getTypeConventionnements().get(0).getPriorite());

    // 01/08 => 31/08
    // IS - 0
    // IS - 1
    Assertions.assertEquals(expectedPeriodeISIS, resul.get(1).getPeriode());
    Assertions.assertEquals(
        "IS", resul.get(1).getTypeConventionnements().get(0).getTypeConventionnement().getCode());
    Assertions.assertEquals(0, resul.get(1).getTypeConventionnements().get(0).getPriorite());
    Assertions.assertEquals(
        "IS", resul.get(1).getTypeConventionnements().get(1).getTypeConventionnement().getCode());
    Assertions.assertEquals(1, resul.get(1).getTypeConventionnements().get(1).getPriorite());

    // 01/07 => 31/07
    // KA - 0
    // IS - 1
    Assertions.assertEquals(expectedPeriodeKAIS, resul.get(2).getPeriode());
    Assertions.assertEquals(
        "KA", resul.get(2).getTypeConventionnements().get(0).getTypeConventionnement().getCode());
    Assertions.assertEquals(0, resul.get(2).getTypeConventionnements().get(0).getPriorite());
    Assertions.assertEquals(
        "IS", resul.get(2).getTypeConventionnements().get(1).getTypeConventionnement().getCode());
    Assertions.assertEquals(1, resul.get(2).getTypeConventionnements().get(1).getPriorite());

    // 01/05 => 30/06
    // CB - 0
    // IS - 1
    Assertions.assertEquals(expectedPeriodeCBIS, resul.get(3).getPeriode());
    Assertions.assertEquals(
        "CB", resul.get(3).getTypeConventionnements().get(0).getTypeConventionnement().getCode());
    Assertions.assertEquals(0, resul.get(3).getTypeConventionnements().get(0).getPriorite());
    Assertions.assertEquals(
        "IS", resul.get(3).getTypeConventionnements().get(1).getTypeConventionnement().getCode());
    Assertions.assertEquals(1, resul.get(3).getTypeConventionnements().get(1).getPriorite());

    // 01/03 => 30/04
    // VM - 0
    // KA - 1
    Assertions.assertEquals(expectedPeriodeVMKA, resul.get(4).getPeriode());
    Assertions.assertEquals(
        "VM", resul.get(4).getTypeConventionnements().get(0).getTypeConventionnement().getCode());
    Assertions.assertEquals(0, resul.get(4).getTypeConventionnements().get(0).getPriorite());
    Assertions.assertEquals(
        "KA", resul.get(4).getTypeConventionnements().get(1).getTypeConventionnement().getCode());
    Assertions.assertEquals(1, resul.get(4).getTypeConventionnements().get(1).getPriorite());

    // 01/01 => 28/02
    // KA - 0
    // IS - 1
    Assertions.assertEquals(expectedPeriodeKAIS2, resul.get(5).getPeriode());
    Assertions.assertEquals(
        "KA", resul.get(5).getTypeConventionnements().get(0).getTypeConventionnement().getCode());
    Assertions.assertEquals(0, resul.get(5).getTypeConventionnements().get(0).getPriorite());
    Assertions.assertEquals(
        "IS", resul.get(5).getTypeConventionnements().get(1).getTypeConventionnement().getCode());
    Assertions.assertEquals(1, resul.get(5).getTypeConventionnements().get(1).getPriorite());
  }
}
