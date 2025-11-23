package com.cegedim.next.serviceeligibility.core.services;

import static org.mockito.Mockito.when;

import com.cegedim.next.serviceeligibility.core.bobb.ContractElement;
import com.cegedim.next.serviceeligibility.core.bobb.ProductElement;
import com.cegedim.next.serviceeligibility.core.bobb.services.ContractElementService;
import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.config.UtilsForTesting;
import com.cegedim.next.serviceeligibility.core.model.domain.carence.ParametrageCarence;
import com.cegedim.next.serviceeligibility.core.model.domain.pw.DroitsTPOfflinePW;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.Anomaly;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.PeriodeCarence;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.CarenceDroit;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitRemplacement;
import com.cegedim.next.serviceeligibility.core.services.pojo.*;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.RestConnector;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.*;
import jakarta.annotation.Nullable;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = TestConfiguration.class)
class CalculDroitsTPServiceTest {
  @Autowired private CalculDroitsTPGenerationService calculDroitsTPGenerationService;
  @Autowired private CalculDroitsTPPAUService calculDroitsTPPAUService;

  @Autowired private MongoTemplate mongoTemplate;

  @Autowired private PwService pwService;

  @Autowired private ContractElementService contractElementService;

  private static final LocalDate CURRENTDATE = LocalDate.now(ZoneOffset.UTC);

  @Autowired private RestConnector restConnector;

  void mockContractElement(String dateFin) {
    LocalDateTime to = null;
    if (dateFin != null) {
      to = DateUtils.parseLocalDateTime(dateFin, DateUtils.YYYY_MM_DD);
    }
    ProductElement productElement =
        createProductElement(
            LocalDateTime.of(2020, 1, 1, 0, 0, 0),
            to,
            Constants.NATURE_PRESTATION_VIDE_BOBB,
            "Offre1");
    ProductElement productElement1 =
        createProductElement(
            LocalDateTime.of(2020, 1, 1, 0, 0, 0),
            to,
            Constants.NATURE_PRESTATION_VIDE_BOBB,
            "OffreReplace");
    mockContractElements(List.of(productElement), List.of(productElement1));
  }

  void mockContractElements(
      List<ProductElement> productElements, List<ProductElement> productElement2) {
    ContractElement contractElement = new ContractElement();
    contractElement.setCodeContractElement("GT_BASE");
    contractElement.setCodeInsurer("BALOO");
    contractElement.setProductElements(productElements);
    if (productElement2 != null) {
      ContractElement contractElement2 = new ContractElement();
      contractElement2.setCodeContractElement("GT_REMP");
      contractElement2.setCodeInsurer("BALOO");
      contractElement2.setProductElements(productElement2);
      Mockito.when(
              contractElementService.get(
                  Mockito.eq("CC1"), Mockito.eq("CCASS1"), Mockito.anyBoolean()))
          .thenReturn(contractElement2);
    }
    Mockito.when(
            contractElementService.get(
                Mockito.eq("DRT"), Mockito.eq("DRTASS"), Mockito.anyBoolean()))
        .thenReturn(contractElement);
  }

  ProductElement createProductElement(
      LocalDateTime from, @Nullable LocalDateTime to, String nature, String offre) {
    ProductElement productElement = new ProductElement();
    productElement.setCodeProduct("PDT_BASE1");
    productElement.setCodeOffer(offre);
    productElement.setCodeAmc("OC");
    productElement.setCodeBenefitNature(nature);
    productElement.setFrom(from);
    if (to != null) {
      productElement.setTo(to);
    }
    return productElement;
  }

  @Test
  void carencingTestBase() {
    List<DroitsTPExtended> listeDroits = getListeDroitsBase();
    List<ParametrageCarence> parametrageCarenceList = getListeCarencesBase();
    String dateFinCarence = "2022-03-31";

    calculDroitsTPGenerationService.carencing(listeDroits, parametrageCarenceList, dateFinCarence);

    Assertions.assertEquals(1, listeDroits.size());
    Assertions.assertEquals("2022-04-01", listeDroits.get(0).getDateDebut());

    dateFinCarence = "2022-12-31";

    calculDroitsTPGenerationService.carencing(listeDroits, parametrageCarenceList, dateFinCarence);

    Assertions.assertEquals(0, listeDroits.size());
  }

  @Test
  void carencingTestCas9() {
    List<DroitsTPExtended> listeDroits = getListeDroitsBase();
    List<ParametrageCarence> parametrageCarenceList = getListeCarencesCas9();
    String dateFinCarence = "2022-03-31";

    calculDroitsTPGenerationService.carencing(listeDroits, parametrageCarenceList, dateFinCarence);

    Assertions.assertEquals(2, listeDroits.size());

    Assertions.assertEquals("2022-04-01", listeDroits.get(0).getDateDebut());
    Assertions.assertEquals("2022-12-31", listeDroits.get(0).getDateFin());

    Assertions.assertEquals("2022-01-01", listeDroits.get(1).getDateDebut());
    Assertions.assertEquals("2022-01-31", listeDroits.get(1).getDateFin());

    dateFinCarence = "2022-12-31";

    calculDroitsTPGenerationService.carencing(listeDroits, parametrageCarenceList, dateFinCarence);

    Assertions.assertEquals(1, listeDroits.size());
  }

  @Test
  void carencingTestCas10() {
    List<DroitsTPExtended> listeDroits = getListeDroitsBase();
    List<ParametrageCarence> parametrageCarenceList = getListeCarencesCas10();
    String dateFinCarence = "2022-02-28";

    calculDroitsTPGenerationService.carencing(listeDroits, parametrageCarenceList, dateFinCarence);

    Assertions.assertEquals(1, listeDroits.size());
    Assertions.assertEquals("2022-02-01", listeDroits.get(0).getDateDebut());
    Assertions.assertEquals("2022-12-31", listeDroits.get(0).getDateFin());
  }

  @Test
  void carencingTestDouble() {
    List<DroitsTPExtended> listeDroits = getListeDroitsBase();
    List<ParametrageCarence> parametrageCarenceList = getListeCarencesDouble();
    String dateFinCarence = "2022-03-31";

    calculDroitsTPGenerationService.carencing(listeDroits, parametrageCarenceList, dateFinCarence);

    Assertions.assertEquals(1, listeDroits.size());
    Assertions.assertEquals("2022-04-01", listeDroits.get(0).getDateDebut());
  }

  @Test
  void carencingTestHoles() {
    List<DroitsTPExtended> listeDroits = getListeDroitsBase();
    List<ParametrageCarence> parametrageCarenceList = getListeCarencesHoles();
    String dateFinCarence = "2022-04-30";

    calculDroitsTPGenerationService.carencing(listeDroits, parametrageCarenceList, dateFinCarence);

    Assertions.assertEquals(3, listeDroits.size());
    Assertions.assertEquals("2022-05-01", listeDroits.get(0).getDateDebut());
    Assertions.assertEquals("2022-12-31", listeDroits.get(0).getDateFin());
    Assertions.assertEquals("2022-03-01", listeDroits.get(1).getDateDebut());
    Assertions.assertEquals("2022-03-31", listeDroits.get(1).getDateFin());
    Assertions.assertEquals("2022-01-15", listeDroits.get(2).getDateDebut());
    Assertions.assertEquals("2022-01-31", listeDroits.get(2).getDateFin());
  }

  private List<DroitsTPExtended> getListeDroitsBase() {
    List<DroitsTPExtended> listeDroits = new ArrayList<>();
    DroitsTPExtended droitsTPExtended = new DroitsTPExtended();
    droitsTPExtended.setCodeGarantie("PHAR");
    PAPNatureTags natureTags = new PAPNatureTags();
    natureTags.setNature("Pharmacie");
    droitsTPExtended.setPapNatureTags(natureTags);
    droitsTPExtended.setDateDebut("2022-01-01");
    droitsTPExtended.setDateFin("2022-12-31");
    listeDroits.add(droitsTPExtended);
    return listeDroits;
  }

  private List<ParametrageCarence> getListeCarencesBase() {
    List<ParametrageCarence> listeCarence = new ArrayList<>();
    ParametrageCarence carence = new ParametrageCarence();
    carence.setNaturePrestation("Pharmacie");
    carence.setDateDebutParametrage("2022-01-01");
    carence.setDateFinParametrage(null);
    listeCarence.add(carence);
    return listeCarence;
  }

  private List<ParametrageCarence> getListeCarencesCas9() {
    List<ParametrageCarence> listeCarence = new ArrayList<>();
    ParametrageCarence carence = new ParametrageCarence();
    carence.setNaturePrestation("Pharmacie");
    carence.setDateDebutParametrage("2022-02-01");
    carence.setDateFinParametrage(null);
    listeCarence.add(carence);
    return listeCarence;
  }

  private List<ParametrageCarence> getListeCarencesCas10() {
    List<ParametrageCarence> listeCarence = new ArrayList<>();
    ParametrageCarence carence = new ParametrageCarence();
    carence.setNaturePrestation("Pharmacie");
    carence.setDateDebutParametrage("2000-01-01");
    carence.setDateFinParametrage("2022-01-31");
    listeCarence.add(carence);
    return listeCarence;
  }

  private List<ParametrageCarence> getListeCarencesDouble() {
    List<ParametrageCarence> listeCarence = new ArrayList<>();
    ParametrageCarence carence1 = new ParametrageCarence();
    carence1.setNaturePrestation("Pharmacie");
    carence1.setDateDebutParametrage("2022-02-01");
    carence1.setDateFinParametrage(null);
    listeCarence.add(carence1);

    ParametrageCarence carence2 = new ParametrageCarence();
    carence2.setNaturePrestation("Pharmacie");
    carence2.setDateDebutParametrage("2022-01-01");
    carence2.setDateFinParametrage("2022-01-31");
    listeCarence.add(carence2);
    return listeCarence;
  }

  private List<ParametrageCarence> getListeCarencesHoles() {
    List<ParametrageCarence> listeCarence = new ArrayList<>();
    ParametrageCarence carence1 = new ParametrageCarence();
    carence1.setNaturePrestation("Pharmacie");
    carence1.setDateDebutParametrage("2022-04-01");
    carence1.setDateFinParametrage("2022-08-31");
    listeCarence.add(carence1);

    ParametrageCarence carence2 = new ParametrageCarence();
    carence2.setNaturePrestation("Dentaire");
    carence2.setDateDebutParametrage("2022-01-01");
    carence2.setDateFinParametrage("2022-03-31");
    listeCarence.add(carence2);

    ParametrageCarence carence3 = new ParametrageCarence();
    carence3.setNaturePrestation("Pharmacie");
    carence3.setDateDebutParametrage("2022-02-01");
    carence3.setDateFinParametrage("2022-02-28");
    listeCarence.add(carence3);

    ParametrageCarence carence4 = new ParametrageCarence();
    carence4.setNaturePrestation("Pharmacie");
    carence4.setDateDebutParametrage("2021-12-01");
    carence4.setDateFinParametrage("2022-01-14");
    listeCarence.add(carence4);
    return listeCarence;
  }

  @Test
  void shouldReturnParametrageProduit() throws PwException {
    ParametrageBobbProductElement parametrageBobbProductElement =
        getParametrageBobb().getParametrageBobbProductElements().get(0);

    mockPWOffersStructure();
    List<ParametrageAtelierProduit> parametrageAtelierProduits =
        calculDroitsTPPAUService.callPW(parametrageBobbProductElement);

    Assertions.assertEquals(1, parametrageAtelierProduits.size());
    ParametrageAtelierProduit parametrageAtelierProduit = parametrageAtelierProduits.get(0);
    Assertions.assertEquals("PDT_BASE1", parametrageAtelierProduit.getCodeProduit());
    Assertions.assertEquals("OC", parametrageAtelierProduit.getCodeOc());
    Assertions.assertEquals("OFFRE1", parametrageAtelierProduit.getCodeOffre());
    Assertions.assertEquals(
        "DENTAIRE", parametrageAtelierProduit.getNaturesTags().get(0).getNature());
    Assertions.assertEquals(
        "HOSPITALISATION", parametrageAtelierProduit.getNaturesTags().get(1).getNature());
    Assertions.assertEquals(
        "PHARMACIE", parametrageAtelierProduit.getNaturesTags().get(2).getNature());
    Assertions.assertEquals(
        "OPTIQUE", parametrageAtelierProduit.getNaturesTags().get(3).getNature());
    Assertions.assertEquals(
        "APPAREILLAGEDENTAIRE", parametrageAtelierProduit.getNaturesTags().get(4).getNature());
  }

  private static ParametrageBobb getParametrageBobb() {
    ParametrageBobb parametrageBobb = new ParametrageBobb();
    parametrageBobb.setCodeAssureur("BALOO");
    parametrageBobb.setCodeGarantie("GT");
    ParametrageBobbProductElement parametrageBobbProductElement =
        new ParametrageBobbProductElement();
    parametrageBobbProductElement.setCodeOc("OC");
    parametrageBobbProductElement.setCodeProduit("PDT_BASE1");
    parametrageBobbProductElement.setCodeOffre("OFFRE1");
    ParametrageBobbNaturePrestation parametrageBobbNaturePrestation =
        new ParametrageBobbNaturePrestation();
    parametrageBobbNaturePrestation.setNaturePrestation(Constants.NATURE_PRESTATION_VIDE_BOBB);
    parametrageBobbNaturePrestation.setDateDebut("2020-01-01");
    parametrageBobbNaturePrestation.setDateFin("2050-01-01");
    parametrageBobbProductElement.setNaturePrestation(List.of(parametrageBobbNaturePrestation));
    parametrageBobb.setParametrageBobbProductElements(List.of(parametrageBobbProductElement));
    return parametrageBobb;
  }

  @Test
  void shouldNotReturnParametrageProduit() throws PwException {
    ParametrageBobbProductElement parametrageBobbProductElement =
        new ParametrageBobbProductElement();
    parametrageBobbProductElement.setCodeOc("OC");
    parametrageBobbProductElement.setCodeProduit("PDT_BASE1");
    parametrageBobbProductElement.setCodeOffre("OFFRE1");
    ParametrageBobbNaturePrestation parametrageBobbNaturePrestation =
        new ParametrageBobbNaturePrestation();
    parametrageBobbNaturePrestation.setNaturePrestation(Constants.NATURE_PRESTATION_VIDE_BOBB);
    parametrageBobbNaturePrestation.setDateDebut("2020-01-01");
    parametrageBobbNaturePrestation.setDateFin("2050-01-01");
    parametrageBobbProductElement.setNaturePrestation(List.of(parametrageBobbNaturePrestation));
    TriggerDataForTesting.mockPWTpOfflineRightsReturnNull(restConnector);
    List<ParametrageAtelierProduit> parametrageAtelierProduits =
        calculDroitsTPPAUService.callPW(parametrageBobbProductElement);

    Assertions.assertEquals(0, parametrageAtelierProduits.size());
  }

  private void mockPWCarence(String file) {
    JSONObject productWorkshopResponse2 = null;
    try {
      productWorkshopResponse2 = UtilsForTesting.parseJSONFile("src/test/resources/" + file);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    LocalDate currentdate = LocalDate.now(ZoneOffset.UTC);
    JSONArray jsonArray = productWorkshopResponse2.getJSONArray("carences");
    for (int i = 0; i < jsonArray.length(); i++) {
      JSONObject itemArr = (JSONObject) jsonArray.get(i);
      String date =
          itemArr
              .get("startEffectDate")
              .toString()
              .replaceAll("XXXX", String.valueOf(currentdate.getYear()));
      itemArr.put("startEffectDate", date);
    }
    when(restConnector.fetchArray(Mockito.anyString(), Mockito.any())).thenReturn(jsonArray);
  }

  private void mockPWOffersStructure() {
    JSONObject productWorkshopResponse2;
    try {
      productWorkshopResponse2 =
          UtilsForTesting.parseJSONFile("src/test/resources/offersStructure.json");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    JSONArray retourOffers = productWorkshopResponse2.getJSONArray("offers");
    updateValidityDateInOffersStructure(retourOffers);
    when(restConnector.fetchArray(Mockito.anyString(), Mockito.any())).thenReturn(retourOffers);
  }

  private void mockPWAndSettings(String fileCarence, String fileOffer) {
    JSONObject productWorkshopResponse2 = null;
    JSONObject ocResponse = null;
    try {
      productWorkshopResponse2 = UtilsForTesting.parseJSONFile("src/test/resources/" + fileCarence);
      ocResponse = UtilsForTesting.parseJSONFile("src/test/resources/" + fileOffer);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    JSONArray retourCarence = productWorkshopResponse2.getJSONArray("carences");
    JSONArray retourOffers = ocResponse.getJSONArray("offers");
    updateValidityDateInOffersStructure(retourOffers);
    when(restConnector.fetchArray(Mockito.anyString(), Mockito.any()))
        .thenReturn(retourOffers, retourCarence);
  }

  private static void updateValidityDateInOffersStructure(JSONArray retourOffers) {
    LocalDate currentdate = LocalDate.now(ZoneOffset.UTC);
    for (int i = 0; i < retourOffers.length(); i++) {
      JSONObject itemArr = (JSONObject) retourOffers.get(i);
      String date =
          itemArr
              .get(Constants.PW_VALIDITY_DATE)
              .toString()
              .replaceAll("XXXX", String.valueOf(currentdate.getYear()));
      itemArr.put(Constants.PW_VALIDITY_DATE, date);
      if (itemArr.has(Constants.PW_END_VALIDITY_DATE)) {
        date =
            itemArr
                .get(Constants.PW_END_VALIDITY_DATE)
                .toString()
                .replaceAll("XXXX", String.valueOf(currentdate.getYear()));
        itemArr.put(Constants.PW_END_VALIDITY_DATE, date);
      }
    }
  }

  private void mockPWAndSettingsWithReplacements(String fileCarence, String fileOffer) {
    JSONObject productWorkshopResponse2 = null;
    JSONObject ocResponse = null;
    try {
      productWorkshopResponse2 = UtilsForTesting.parseJSONFile("src/test/resources/" + fileCarence);
      ocResponse = UtilsForTesting.parseJSONFile("src/test/resources/" + fileOffer);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    JSONArray retourCarence = productWorkshopResponse2.getJSONArray("carences");
    JSONArray retourOffers = ocResponse.getJSONArray("offers");
    updateValidityDateInOffersStructure(retourOffers);
    when(restConnector.fetchArray(Mockito.anyString(), Mockito.any()))
        .thenReturn(retourOffers, retourCarence, null);
  }

  @Test
  void shouldReturnParametrageCarence() throws CarenceException {
    ParametrageBobb parametrageBobb = getParametrageBobb();
    mockPWCarence("carences.json");
    CarenceDroit carence = new CarenceDroit();
    carence.setCode("CAR001");
    PeriodeCarence periodeCarence = new PeriodeCarence();
    periodeCarence.setDebut("2000-01-01");
    periodeCarence.setFin("2020-01-01");
    carence.setPeriode(periodeCarence);
    List<ParametrageCarence> parametrageCarenceList =
        calculDroitsTPGenerationService.callCarence(
            parametrageBobb.getCodeAssureur(),
            parametrageBobb.getParametrageBobbProductElements().get(0).getCodeOffre(),
            parametrageBobb.getParametrageBobbProductElements().get(0).getCodeProduit(),
            carence.getCode(),
            false);

    Assertions.assertEquals(4, parametrageCarenceList.size());
    ParametrageCarence parametrageCarence = parametrageCarenceList.get(0);
    Assertions.assertEquals("CAR001", parametrageCarence.getCodeCarence());
    Assertions.assertEquals("PDT_BASE1", parametrageCarence.getProduit());
    Assertions.assertEquals("OFFRE1", parametrageCarence.getOffre());
    Assertions.assertEquals("DENTAIRE", parametrageCarence.getNaturePrestation());
  }

  @Test
  void shouldNotReturnParametrageCarence() throws CarenceException {
    ParametrageBobb parametrageBobb = getParametrageBobb();
    mockPWCarence("carences.json");
    CarenceDroit carence = new CarenceDroit();
    carence.setCode("PAS");
    PeriodeCarence periodeCarence = new PeriodeCarence();
    periodeCarence.setDebut("2000-01-01");
    periodeCarence.setFin("2020-01-01");
    carence.setPeriode(periodeCarence);
    List<ParametrageCarence> parametrageCarenceList =
        calculDroitsTPGenerationService.callCarence(
            parametrageBobb.getCodeAssureur(),
            parametrageBobb.getParametrageBobbProductElements().get(0).getCodeOffre(),
            parametrageBobb.getParametrageBobbProductElements().get(0).getCodeProduit(),
            carence.getCode(),
            false);

    Assertions.assertEquals(0, parametrageCarenceList.size());
  }

  public static DroitAssure getDroitAssureV3(LocalDate currentdate) {
    String firstDayOfYear = currentdate.getYear() + "-01-01";
    String endOfMarchOfYear = currentdate.getYear() + "-03-31";

    Periode periodeCurrentYear = new Periode(firstDayOfYear, null);
    List<Periode> periodesCurrentYear = new ArrayList<>();
    periodesCurrentYear.add(periodeCurrentYear);
    DroitAssure droit = new DroitAssure();
    droit.setCode("DRT");
    droit.setCodeAssureur("DRTASS");
    droit.setLibelle("Droit 2");
    droit.setOrdrePriorisation("1");
    droit.setType("DRT");
    droit.setPeriode(periodeCurrentYear);
    droit.setDateAncienneteGarantie(firstDayOfYear);
    List<CarenceDroit> carences = new ArrayList<>();
    CarenceDroit carence = new CarenceDroit();
    carence.setCode("CAR001");
    carence.setPeriode(new PeriodeCarence(firstDayOfYear, endOfMarchOfYear));
    DroitRemplacement droitRemplacement = new DroitRemplacement();
    droitRemplacement.setCode("CC1");
    droitRemplacement.setCodeAssureur("CCASS1");
    droitRemplacement.setLibelle("Carence1");
    carence.setDroitRemplacement(droitRemplacement);
    carences.add(carence);
    droit.setCarences(carences);
    return droit;
  }

  public static DroitAssure getDroitAssureV3CasPWV4(LocalDate currentdate) {
    String firstDayOfYear = currentdate.getYear() + "-01-01";
    String endOfMarchOfYear = currentdate.getYear() + "-03-31";

    Periode periodeCurrentYear = new Periode(firstDayOfYear, null);
    List<Periode> periodesCurrentYear = new ArrayList<>();
    periodesCurrentYear.add(periodeCurrentYear);
    DroitAssure droit = new DroitAssure();
    droit.setCode("DRT");
    droit.setCodeAssureur("DRTASS");
    droit.setLibelle("Droit 2");
    droit.setOrdrePriorisation("1");
    droit.setType("DRT");
    droit.setPeriode(periodeCurrentYear);
    droit.setDateAncienneteGarantie(firstDayOfYear);
    List<CarenceDroit> carences = new ArrayList<>();
    CarenceDroit carence = new CarenceDroit();
    carence.setCode("CAR001");
    carence.setPeriode(new PeriodeCarence(firstDayOfYear, endOfMarchOfYear));
    carences.add(carence);
    droit.setCarences(carences);
    return droit;
  }

  public static DroitAssure getDroitAssureV3cas7(LocalDate currentdate) {
    String firstDayOfYear = currentdate.getYear() + "-10-01";
    String endOfMarchOfYear = currentdate.getYear() + "-12-31";

    Periode periodeCurrentYear = new Periode(firstDayOfYear, null);
    List<Periode> periodesCurrentYear = new ArrayList<>();
    periodesCurrentYear.add(periodeCurrentYear);
    DroitAssure droit = new DroitAssure();
    droit.setCode("DRT");
    droit.setCodeAssureur("DRTASS");
    droit.setLibelle("Droit 2");
    droit.setOrdrePriorisation("1");
    droit.setType("DRT");
    droit.setPeriode(periodeCurrentYear);
    droit.setDateAncienneteGarantie(firstDayOfYear);
    List<CarenceDroit> carences = new ArrayList<>();
    CarenceDroit carence = new CarenceDroit();
    carence.setCode("CAR001");
    carence.setPeriode(new PeriodeCarence(firstDayOfYear, endOfMarchOfYear));
    carences.add(carence);
    droit.setCarences(carences);
    return droit;
  }

  @Test
  void shouldNotReturnDroitsTPExtended() {
    LocalDate currentdate = LocalDate.now(ZoneOffset.UTC);
    DroitAssure droitAssure = getDroitAssureV3(currentdate);
    String dateDebut = currentdate.getYear() + "-01-01";
    String dateFin = currentdate.getYear() + "-12-31";
    mockContractElement(dateFin);
    PwException pwException =
        Assertions.assertThrows(
            PwException.class,
            () ->
                calculDroitsTPGenerationService.calculDroitsTP(
                    droitAssure, dateDebut, dateFin, null));
    Assertions.assertEquals(
        Anomaly.PRODUCT_WORKSHOP_ERROR, pwException.getTriggeredBeneficiaryAnomaly().getAnomaly());
  }

  @Test
  void shouldReturnDroitsTPExtended() throws Exception {
    mockContractElement(null);
    mockPWCarence("carences.json");
    TriggerDataForTesting.mockPWTpOfflineRightsWithReplacement(
        restConnector, "pw_response_Offre1.json", "pw_response_OffreReplace.json");

    LocalDate currentdate = LocalDate.now(ZoneOffset.UTC);
    DroitAssure droitAssure = getDroitAssureV3(currentdate);
    List<DroitsTPExtended> droitsTPExtendeds =
        calculDroitsTPGenerationService.calculDroitsTP(
            droitAssure, currentdate.getYear() + "-01-01", currentdate.getYear() + "-12-31", null);
    Assertions.assertEquals(6, droitsTPExtendeds.size());
    Assertions.assertEquals("DENTAIRE", droitsTPExtendeds.get(0).getDetailsOffline().getNature());
    Assertions.assertEquals(
        currentdate.getYear() + "-04-01", droitsTPExtendeds.get(0).getDateDebut());
    Assertions.assertEquals("RADIOLOGIE", droitsTPExtendeds.get(2).getDetailsOffline().getNature());
    Assertions.assertEquals(
        currentdate.getYear() + "-01-01", droitsTPExtendeds.get(2).getDateDebut());
    Assertions.assertEquals("DENTAIRE", droitsTPExtendeds.get(4).getDetailsOffline().getNature());
    Assertions.assertEquals(
        currentdate.getYear() + "-01-01", droitsTPExtendeds.get(4).getDateDebut());
  }

  @Test
  void shouldReturnDroitsTPExtendedWithOneBenefitType() throws Exception {
    mockContractElement(null);
    mockPWCarence("carences.json");
    TriggerDataForTesting.mockPWTpOfflineRightsWithReplacement(
        restConnector, "pw_response_OffreSansTpOffline.json", "pw_response_OffreReplace.json");

    LocalDate currentdate = LocalDate.now(ZoneOffset.UTC);
    DroitAssure droitAssureV3 = getDroitAssureV3(currentdate);
    List<DroitsTPExtended> droitsTPExtendeds =
        calculDroitsTPGenerationService.calculDroitsTP(
            droitAssureV3,
            currentdate.getYear() + "-01-01",
            currentdate.getYear() + "-12-31",
            null);
    Assertions.assertEquals(2, droitsTPExtendeds.size());
    Assertions.assertEquals("Offre1", droitsTPExtendeds.get(0).getCodeOffre());
    Assertions.assertEquals("DENTAIRE", droitsTPExtendeds.get(0).getDetailsOffline().getNature());
    Assertions.assertEquals(
        currentdate.getYear() + "-04-01", droitsTPExtendeds.get(0).getDateDebut());
    Assertions.assertEquals("OffreReplace", droitsTPExtendeds.get(1).getCodeOffre());
    Assertions.assertEquals("DENTAIRE", droitsTPExtendeds.get(1).getDetailsOffline().getNature());
    Assertions.assertEquals(
        currentdate.getYear() + "-01-01", droitsTPExtendeds.get(1).getDateDebut());
  }

  @Test
  void shouldNotReturnDroitsTPExtended_NoTpOffline() throws Exception {
    mockContractElement(null);
    mockPWCarence("carences.json");
    TriggerDataForTesting.mockPWTpOfflineRightsWithReplacement(
        restConnector,
        "pw_response_OffreEmptyTpOffline.json",
        "pw_response_OffreReplaceEmptyTpOffline.json");

    LocalDate currentdate = LocalDate.now(ZoneOffset.UTC);
    DroitAssure droitAssureV3 = getDroitAssureV3(currentdate);
    List<DroitsTPExtended> droitsTPExtendeds =
        calculDroitsTPGenerationService.calculDroitsTP(
            droitAssureV3,
            currentdate.getYear() + "-01-01",
            currentdate.getYear() + "-12-31",
            null);
    Assertions.assertEquals(0, droitsTPExtendeds.size());
  }

  @Test
  void shouldReturnDroitsTPExtendedWithPCchange() throws Exception {
    mockContractElement(null);
    mockPWCarence("carences.json");
    TriggerDataForTesting.mockPWV4TpRights("pw_double_offre.json", restConnector);

    LocalDate currentdate = LocalDate.now(ZoneOffset.UTC);
    DroitAssure droitAssure = getDroitAssureV3CasPWV4(currentdate);
    List<DroitsTPExtended> droitsTPExtendeds =
        calculDroitsTPGenerationService.calculDroitsTP(
            droitAssure,
            currentdate.getYear() + "-01-01",
            currentdate.getYear() + "-12-31",
            currentdate.getYear() + "-06-30");
    Assertions.assertEquals(6, droitsTPExtendeds.size());
    Assertions.assertEquals("DENTAIRE", droitsTPExtendeds.get(2).getDetailsOffline().getNature());
    Assertions.assertEquals(
        currentdate.getYear() + "-04-01", droitsTPExtendeds.get(2).getDateDebut());
    Assertions.assertEquals(
        currentdate.getYear() + "-06-30", droitsTPExtendeds.get(2).getDateFinOnline());
    Assertions.assertEquals("RADIOLOGIE", droitsTPExtendeds.get(1).getDetailsOffline().getNature());
    Assertions.assertEquals(
        currentdate.getYear() + "-01-01", droitsTPExtendeds.get(1).getDateDebut());
    Assertions.assertEquals(
        currentdate.getYear() + "-06-30", droitsTPExtendeds.get(1).getDateFinOnline());
    Assertions.assertEquals("DENTAIRE", droitsTPExtendeds.get(4).getDetailsOffline().getNature());
    Assertions.assertEquals(
        currentdate.getYear() + "-07-01", droitsTPExtendeds.get(4).getDateDebut());
    Assertions.assertEquals(
        currentdate.getYear() + "-12-31", droitsTPExtendeds.get(4).getDateFin());
  }

  @Test
  void shouldReturnDroitsTPExtendedWithNoBenefitNature() throws Exception {
    mockContractElement(null);
    mockPWAndSettings("carencesinconnus.json", "offersStructureWithoutEndValidityDate.json");

    LocalDate currentdate = LocalDate.now(ZoneOffset.UTC);
    DroitAssure droitAssure = getDroitAssureV3(currentdate);
    Periode periode =
        new Periode(currentdate.getYear() + "-01-01", currentdate.getYear() + "-12-31");
    List<DroitsTPExtended> droitsTPExtendeds =
        calculDroitsTPPAUService.calculDroitsTP(droitAssure, null, periode, null);
    Assertions.assertEquals(5, droitsTPExtendeds.size());
    Assertions.assertNull(droitsTPExtendeds.get(0).getPapNatureTags());
    Assertions.assertEquals(
        currentdate.getYear() + "-01-01", droitsTPExtendeds.get(0).getDateDebut());
    Assertions.assertEquals(
        currentdate.getYear() + "-12-31", droitsTPExtendeds.get(0).getDateFin());
    Assertions.assertNull(droitsTPExtendeds.get(0).getDateFinOnline());
  }

  @Test
  void shouldReturnDroitsTPExtendedWithNoBenefitNature2() throws Exception {
    LocalDate currentdate = LocalDate.now(ZoneOffset.UTC);
    mockContractElement(currentdate.getYear() + "-06-01");
    mockPWAndSettings("carencesinconnus2.json", "offersStructureWithoutEndValidityDate.json");
    DroitAssure droitAssure = getDroitAssureV3(currentdate);
    Periode periode =
        new Periode(currentdate.getYear() + "-01-01", currentdate.getYear() + "-12-31");
    List<DroitsTPExtended> droitsTPExtendeds =
        calculDroitsTPPAUService.calculDroitsTP(
            droitAssure, currentdate.getYear() + "-12-31", periode, null);
    Assertions.assertEquals(5, droitsTPExtendeds.size());
    Assertions.assertNull(droitsTPExtendeds.get(0).getPapNatureTags());
    Assertions.assertEquals(
        currentdate.getYear() + "-01-01", droitsTPExtendeds.get(0).getDateDebut());
    Assertions.assertEquals(
        currentdate.getYear() + "-06-01", droitsTPExtendeds.get(0).getDateFinOnline());
  }

  @Test
  void shouldReturnDroitsTPExtendedWithMultipleBenefitType() throws Exception {
    LocalDate currentdate = LocalDate.now(ZoneOffset.UTC);
    mockContractElement(currentdate.getYear() + "-05-01");
    mockPWAndSettings("carences.json", "offersStructureWithoutEndValidityDate.json");

    DroitAssure droitAssure = getDroitAssureV3(currentdate);
    droitAssure.setCarencesToNull();
    Periode periode =
        new Periode(currentdate.getYear() + "-01-01", currentdate.getYear() + "-12-31");
    List<DroitsTPExtended> droitsTPExtendeds =
        calculDroitsTPPAUService.calculDroitsTP(
            droitAssure, currentdate.getYear() + "-12-31", periode, null);
    Assertions.assertEquals(5, droitsTPExtendeds.size());
    Assertions.assertEquals("DENTAIRE", droitsTPExtendeds.get(0).getPapNatureTags().getNature());
    Assertions.assertEquals(
        "HOSPITALISATION", droitsTPExtendeds.get(1).getPapNatureTags().getNature());
    Assertions.assertEquals("PHARMACIE", droitsTPExtendeds.get(2).getPapNatureTags().getNature());
    Assertions.assertEquals("OPTIQUE", droitsTPExtendeds.get(3).getPapNatureTags().getNature());
    Assertions.assertEquals(
        "APPAREILLAGEDENTAIRE", droitsTPExtendeds.get(4).getPapNatureTags().getNature());
    Assertions.assertEquals(
        currentdate.getYear() + "-01-01", droitsTPExtendeds.get(0).getDateDebut());
    Assertions.assertEquals(
        currentdate.getYear() + "-05-01", droitsTPExtendeds.get(0).getDateFin());
    Assertions.assertEquals(
        currentdate.getYear() + "-05-01", droitsTPExtendeds.get(0).getDateFinOnline());
  }

  @Test
  void shouldReturnDroitsTPExtendedCas9() throws Exception {
    mockContractElement(null);
    mockPWCarence("carencesdebutfevrier.json");
    TriggerDataForTesting.mockPWTpOfflineRightsWithReplacementDentaire(restConnector);

    LocalDate currentdate = LocalDate.now(ZoneOffset.UTC);
    DroitAssure droitAssure = getDroitAssureV3(currentdate);
    List<DroitsTPExtended> droitsTPExtendeds =
        calculDroitsTPGenerationService.calculDroitsTP(
            droitAssure, currentdate.getYear() + "-01-01", currentdate.getYear() + "-12-31", null);
    Assertions.assertEquals(3, droitsTPExtendeds.size());
    Assertions.assertEquals("DENTAIRE", droitsTPExtendeds.get(0).getDetailsOffline().getNature());
    Assertions.assertEquals("DENTAIRE", droitsTPExtendeds.get(1).getDetailsOffline().getNature());
    Assertions.assertEquals("DENTAIRE", droitsTPExtendeds.get(2).getDetailsOffline().getNature());
    Assertions.assertEquals("Offre1", droitsTPExtendeds.get(0).getCodeOffre());
    Assertions.assertEquals("Offre1", droitsTPExtendeds.get(1).getCodeOffre());
    Assertions.assertEquals("OffreReplace", droitsTPExtendeds.get(2).getCodeOffre());
    Assertions.assertEquals(
        currentdate.getYear() + "-04-01", droitsTPExtendeds.get(0).getDateDebut());
    Assertions.assertEquals(
        currentdate.getYear() + "-12-31", droitsTPExtendeds.get(0).getDateFin());

    Assertions.assertEquals(
        currentdate.getYear() + "-01-01", droitsTPExtendeds.get(1).getDateDebut());
    Assertions.assertEquals(
        currentdate.getYear() + "-01-31", droitsTPExtendeds.get(1).getDateFin());

    Assertions.assertEquals(
        currentdate.getYear() + "-02-01", droitsTPExtendeds.get(2).getDateDebut());
    Assertions.assertEquals(
        currentdate.getYear() + "-03-31", droitsTPExtendeds.get(2).getDateFin());
  }

  @Test
  void shouldReturnDroitsTPExtendedWithReplacementRightEmpty() throws Exception {
    ContractElement contractElement = new ContractElement();
    contractElement.setCodeContractElement("GT_BASE");
    contractElement.setCodeInsurer("BALOO");
    List<ProductElement> productElementList = new ArrayList<>();
    ProductElement productElement = new ProductElement();
    productElement.setCodeProduct("PDT_BASE1");
    productElement.setCodeOffer("OFFRE");
    productElement.setCodeAmc("OC");
    productElement.setCodeBenefitNature(Constants.NATURE_PRESTATION_VIDE_BOBB);
    productElement.setFrom(LocalDateTime.of(2020, 01, 01, 0, 0, 0));
    productElementList.add(productElement);
    contractElement.setProductElements(productElementList);

    Mockito.when(contractElementService.get("DRT", "DRTASS", false)).thenReturn(contractElement);
    Mockito.when(contractElementService.get("CC1", "CCASS1", false)).thenReturn(null);
    mockPWAndSettings("carences.json", "offersStructureWithoutEndValidityDate.json");

    LocalDate currentdate = LocalDate.now(ZoneOffset.UTC);
    DroitAssure droitAssure = getDroitAssureV3(currentdate);
    Periode periode =
        new Periode(currentdate.getYear() + "-01-01", currentdate.getYear() + "-12-31");
    List<DroitsTPExtended> droitsTPExtendeds =
        calculDroitsTPPAUService.calculDroitsTP(
            droitAssure, currentdate.getYear() + "-08-05", periode, null);
    Assertions.assertEquals(6, droitsTPExtendeds.size());
    Assertions.assertEquals("DENTAIRE", droitsTPExtendeds.get(0).getPapNatureTags().getNature());
    Assertions.assertEquals(
        "HOSPITALISATION", droitsTPExtendeds.get(1).getPapNatureTags().getNature());
    Assertions.assertEquals("PHARMACIE", droitsTPExtendeds.get(2).getPapNatureTags().getNature());
    Assertions.assertEquals("OPTIQUE", droitsTPExtendeds.get(3).getPapNatureTags().getNature());
    Assertions.assertEquals(
        "APPAREILLAGEDENTAIRE", droitsTPExtendeds.get(4).getPapNatureTags().getNature());
    Assertions.assertNull(
        droitsTPExtendeds.get(5).getPapNatureTags()); // GT de remplacement non trouvé
    Assertions.assertEquals("DRT", droitsTPExtendeds.get(5).getOriginCode());
    Assertions.assertEquals("DRTASS", droitsTPExtendeds.get(5).getOriginInsurerCode());
    Assertions.assertEquals("CC1", droitsTPExtendeds.get(5).getCodeGarantie());
    Assertions.assertEquals("CCASS1", droitsTPExtendeds.get(5).getInsurerCode());
    Assertions.assertEquals(
        currentdate.getYear() + "-08-05", droitsTPExtendeds.get(0).getDateFinOnline());
    Assertions.assertNull(droitsTPExtendeds.get(5).getCodeProduit());
  }

  @Test
  void shouldReturnDroitsTPExtendedWithReplacementRightWithNoProducts() throws Exception {

    ContractElement contractElement = new ContractElement();
    contractElement.setCodeContractElement("GT_BASE");
    contractElement.setCodeInsurer("BALOO");
    List<ProductElement> productElementList = new ArrayList<>();
    ProductElement productElement = new ProductElement();
    productElement.setCodeProduct("PDT_BASE1");
    productElement.setCodeOffer("OFFRE");
    productElement.setCodeAmc("OC");
    productElement.setCodeBenefitNature(Constants.NATURE_PRESTATION_VIDE_BOBB);
    productElement.setFrom(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
    productElementList.add(productElement);
    contractElement.setProductElements(productElementList);

    Mockito.when(contractElementService.get("DRT", "DRTASS", false)).thenReturn(contractElement);
    Mockito.when(contractElementService.get("CC1", "CCASS1", false)).thenReturn(contractElement);
    mockPWAndSettingsWithReplacements(
        "carences.json", "offersStructureWithoutEndValidityDate.json");

    LocalDate currentdate = LocalDate.now(ZoneOffset.UTC);
    DroitAssure droitAssure = getDroitAssureV3(currentdate);
    Periode periode =
        new Periode(currentdate.getYear() + "-01-01", currentdate.getYear() + "-12-31");
    List<DroitsTPExtended> droitsTPExtendeds =
        calculDroitsTPPAUService.calculDroitsTP(
            droitAssure, currentdate.getYear() + "-12-31", periode, null);
    Assertions.assertEquals(6, droitsTPExtendeds.size());
    Assertions.assertEquals("DENTAIRE", droitsTPExtendeds.get(0).getPapNatureTags().getNature());
    Assertions.assertEquals(
        "HOSPITALISATION", droitsTPExtendeds.get(1).getPapNatureTags().getNature());
    Assertions.assertEquals("PHARMACIE", droitsTPExtendeds.get(2).getPapNatureTags().getNature());
    Assertions.assertEquals("OPTIQUE", droitsTPExtendeds.get(3).getPapNatureTags().getNature());
    Assertions.assertEquals(
        "APPAREILLAGEDENTAIRE", droitsTPExtendeds.get(4).getPapNatureTags().getNature());
    // GT de remplacement trouvé mais pas de produit
    Assertions.assertNull(droitsTPExtendeds.get(5).getPapNatureTags());
    Assertions.assertEquals("DRT", droitsTPExtendeds.get(5).getOriginCode());
    Assertions.assertEquals("DRTASS", droitsTPExtendeds.get(5).getOriginInsurerCode());
    Assertions.assertEquals("CC1", droitsTPExtendeds.get(5).getCodeGarantie());
    Assertions.assertEquals("CCASS1", droitsTPExtendeds.get(5).getInsurerCode());
    Assertions.assertEquals("PDT_BASE1", droitsTPExtendeds.get(5).getCodeProduit());
    Assertions.assertEquals("OFFRE", droitsTPExtendeds.get(5).getCodeOffre());
  }

  @Test
  void shouldReturnDroitsTPExtendedCas7() {
    mockContractElement(null);
    mockPWCarence("carencescas7.json");
    TriggerDataForTesting.mockPWTpOfflineRightsWithReplacementDentaire(restConnector);

    LocalDate currentdate = LocalDate.now(ZoneOffset.UTC);
    DroitAssure droitAssure = getDroitAssureV3cas7(currentdate);
    TriggerWarningException triggerWarningException =
        Assertions.assertThrows(
            TriggerWarningException.class,
            () ->
                calculDroitsTPGenerationService.calculDroitsTP(
                    droitAssure,
                    currentdate.getYear() + "-01-01",
                    currentdate.getYear() + "-12-31",
                    null));
    Assertions.assertEquals(
        Anomaly.NO_TP_RIGHTS_CAUSED_BY_WAITINGS_PERIODS,
        triggerWarningException.getTriggeredBeneficiaryAnomaly().getAnomaly());
  }

  @Test
  void shouldReturnDroitsTPExtendedWithoutProducts() throws PwException, CarenceException {
    Mockito.when(contractElementService.get("DRT", "DRTASS", false)).thenReturn(null);
    when(mongoTemplate.findOne(Mockito.any(Query.class), Mockito.eq(ContractElement.class)))
        .thenReturn(null);

    LocalDate currentdate = LocalDate.now(ZoneOffset.UTC);
    DroitAssure droitAssure = getDroitAssureV3cas7(currentdate);
    Periode periode =
        new Periode(currentdate.getYear() + "-01-01", currentdate.getYear() + "-12-31");
    List<DroitsTPExtended> liste =
        calculDroitsTPPAUService.calculDroitsTP(
            droitAssure, currentdate.getYear() + "-12-31", periode, null);
    Assertions.assertNotNull(liste);
    Assertions.assertEquals(1, liste.size());
  }

  @Test
  void shouldReturnTwoDroitsTPExtendedWithoutProducts() throws PwException, CarenceException {
    Mockito.when(contractElementService.get("DRT", "DRTASS", false)).thenReturn(null);
    Mockito.when(contractElementService.get("CC1", "CCASS1", false)).thenReturn(null);

    LocalDate currentdate = LocalDate.now(ZoneOffset.UTC);
    DroitAssure droitAssure = getDroitAssureV3cas7(currentdate);
    DroitRemplacement droitRemplacement = new DroitRemplacement();
    droitRemplacement.setCodeAssureur("DRTASS");
    droitRemplacement.setCode("AAA");
    droitAssure.getCarences().get(0).setDroitRemplacement(droitRemplacement);
    Periode periode =
        new Periode(currentdate.getYear() + "-01-01", currentdate.getYear() + "-12-31");
    List<DroitsTPExtended> liste =
        calculDroitsTPPAUService.calculDroitsTP(
            droitAssure, currentdate.getYear() + "-12-31", periode, null);
    Assertions.assertNotNull(liste);
    Assertions.assertEquals(2, liste.size());
  }

  @Test
  void shouldNotBeInError0() {
    List<ParametrageCarence> parametrageCarenceList = new ArrayList<>();
    ParametrageCarence parametrageCarence = new ParametrageCarence();
    parametrageCarence.setDateDebutParametrage("2020-01-01");
    parametrageCarenceList.add(parametrageCarence);
    Assertions.assertFalse(
        calculDroitsTPGenerationService.errorParametrageDatesCarence(
            "2023-01-01", "2023-03-31", parametrageCarenceList));
  }

  @Test
  void shouldNotBeInError1() {
    List<ParametrageCarence> parametrageCarenceList = new ArrayList<>();
    ParametrageCarence parametrageCarence = new ParametrageCarence();
    parametrageCarence.setDateDebutParametrage("2020-01-01");
    parametrageCarence.setDateFinParametrage("2023-01-31");
    parametrageCarenceList.add(parametrageCarence);
    ParametrageCarence parametrageCarence2 = new ParametrageCarence();
    parametrageCarence2.setDateDebutParametrage("2023-02-01");
    parametrageCarenceList.add(parametrageCarence);
    parametrageCarenceList.add(parametrageCarence2);
    Assertions.assertFalse(
        calculDroitsTPGenerationService.errorParametrageDatesCarence(
            "2023-01-01", "2023-03-31", parametrageCarenceList));
  }

  @Test
  void shouldNotBeInError2() {
    List<ParametrageCarence> parametrageCarenceList = new ArrayList<>();
    ParametrageCarence parametrageCarence = new ParametrageCarence();
    parametrageCarence.setDateDebutParametrage("2020-01-01");
    parametrageCarence.setDateFinParametrage("2023-01-31");
    parametrageCarenceList.add(parametrageCarence);
    ParametrageCarence parametrageCarence2 = new ParametrageCarence();
    parametrageCarence2.setDateDebutParametrage("2023-02-01");
    ParametrageCarence parametrageCarence3 = new ParametrageCarence();
    parametrageCarence3.setDateDebutParametrage("2023-02-01");
    parametrageCarenceList.add(parametrageCarence);
    parametrageCarenceList.add(parametrageCarence2);
    parametrageCarenceList.add(parametrageCarence3);
    Assertions.assertFalse(
        calculDroitsTPGenerationService.errorParametrageDatesCarence(
            "2023-01-01", "2023-03-31", parametrageCarenceList));
  }

  @Test
  void shouldBeInError() {
    List<ParametrageCarence> parametrageCarenceList = new ArrayList<>();
    ParametrageCarence parametrageCarence = new ParametrageCarence();
    parametrageCarence.setDateDebutParametrage("2020-01-01");
    parametrageCarence.setDateFinParametrage("2023-01-31");
    parametrageCarenceList.add(parametrageCarence);
    ParametrageCarence parametrageCarence2 = new ParametrageCarence();
    parametrageCarence2.setDateDebutParametrage("2023-03-01");
    parametrageCarenceList.add(parametrageCarence);
    parametrageCarenceList.add(parametrageCarence2);
    Assertions.assertFalse(
        calculDroitsTPGenerationService.errorParametrageDatesCarence(
            "2023-01-01", "2023-03-31", parametrageCarenceList));
  }

  // BLUE-4753
  // @Test
  void shouldReturnRightsFromBothProducts() throws PwException {
    DroitsTPOfflinePW droitPw1 = new DroitsTPOfflinePW();
    DroitsTPOfflinePW droitPw2 = new DroitsTPOfflinePW();

    String debut = "2023-01-01";
    String fin = "2023-12-31";

    when(pwService.getDroitsProductsWorkshop(Mockito.anyList(), "PDT1", "OC", debut, fin))
        .thenReturn(List.of(droitPw1));
    when(pwService.getDroitsProductsWorkshop(Mockito.anyList(), "PDT2", "OC", debut, fin))
        .thenReturn(List.of(droitPw2));
  }

  @Test
  void shouldReturnDroitsTPExtendedBobLimitedDate() throws Exception {
    ProductElement productElement =
        createProductElement(
            LocalDateTime.of(CURRENTDATE.getYear(), 1, 1, 0, 0, 0),
            LocalDateTime.of(CURRENTDATE.getYear(), 12, 31, 0, 0, 0),
            "",
            "Offre1");

    ProductElement productElement2 =
        createProductElement(
            LocalDateTime.of(CURRENTDATE.getYear(), 1, 1, 0, 0, 0),
            LocalDateTime.of(CURRENTDATE.getYear(), 12, 31, 0, 0, 0),
            "",
            "OffreReplace");
    mockContractElements(List.of(productElement), List.of(productElement2));

    List<DroitsTPExtended> droitsTPExtendeds = launchCalculDroitsTP();

    Assertions.assertEquals(6, droitsTPExtendeds.size());

    Assertions.assertEquals(
        "DENTAIRE", droitsTPExtendeds.getFirst().getDetailsOffline().getNature());
    Assertions.assertEquals(
        CURRENTDATE.getYear() + "-04-01", droitsTPExtendeds.getFirst().getDateDebut());
    Assertions.assertEquals(
        CURRENTDATE.getYear() + "-12-31", droitsTPExtendeds.getFirst().getDateFinOnline());
    Assertions.assertEquals(
        CURRENTDATE.getYear() + "-12-31", droitsTPExtendeds.getFirst().getDateFin());
    Assertions.assertEquals(
        CURRENTDATE.getYear() + "-01-01", droitsTPExtendeds.get(2).getDateDebut());
    Assertions.assertEquals(
        CURRENTDATE.getYear() + "-12-31", droitsTPExtendeds.get(2).getDateFinOnline());
    Assertions.assertEquals(
        CURRENTDATE.getYear() + "-12-31", droitsTPExtendeds.get(2).getDateFin());
    Assertions.assertEquals(
        CURRENTDATE.getYear() + "-01-01", droitsTPExtendeds.get(4).getDateDebut());
    Assertions.assertEquals(
        CURRENTDATE.getYear() + "-03-31", droitsTPExtendeds.get(4).getDateFinOnline());
    Assertions.assertEquals(
        CURRENTDATE.getYear() + "-03-31", droitsTPExtendeds.get(4).getDateFin());
  }

  @Test
  void shouldReturnDroitsTPExtendedBobLimitedNature() throws Exception {
    ProductElement productElement =
        createProductElement(
            LocalDateTime.of(CURRENTDATE.getYear(), 1, 1, 0, 0, 0),
            LocalDateTime.of(CURRENTDATE.getYear(), 12, 31, 0, 0, 0),
            "DENTAIRE",
            "Offre1");
    ProductElement productElement2 =
        createProductElement(
            LocalDateTime.of(CURRENTDATE.getYear(), 1, 1, 0, 0, 0),
            LocalDateTime.of(CURRENTDATE.getYear(), 12, 31, 0, 0, 0),
            "DENTAIRE",
            "OffreReplace");
    mockContractElements(List.of(productElement), List.of(productElement2));

    List<DroitsTPExtended> droitsTPExtendeds = launchCalculDroitsTP();

    Assertions.assertEquals(2, droitsTPExtendeds.size());

    Assertions.assertEquals("DENTAIRE", droitsTPExtendeds.get(0).getDetailsOffline().getNature());
    Assertions.assertEquals(
        CURRENTDATE.getYear() + "-04-01", droitsTPExtendeds.get(0).getDateDebut());
    Assertions.assertEquals(
        CURRENTDATE.getYear() + "-12-31", droitsTPExtendeds.get(0).getDateFinOnline());
    Assertions.assertEquals(
        CURRENTDATE.getYear() + "-12-31", droitsTPExtendeds.get(0).getDateFin());

    Assertions.assertEquals("DENTAIRE", droitsTPExtendeds.get(1).getDetailsOffline().getNature());
    Assertions.assertEquals(
        CURRENTDATE.getYear() + "-01-01", droitsTPExtendeds.get(1).getDateDebut());
    Assertions.assertEquals(
        CURRENTDATE.getYear() + "-03-31", droitsTPExtendeds.get(1).getDateFinOnline());
    Assertions.assertEquals(
        CURRENTDATE.getYear() + "-03-31", droitsTPExtendeds.get(1).getDateFin());
  }

  @Test
  void shouldReturnDroitsTPExtendedBobLimitedSameNatureDatesDifferences() throws Exception {
    ProductElement productElement1 =
        createProductElement(
            LocalDateTime.of(CURRENTDATE.getYear(), 1, 1, 0, 0, 0),
            LocalDateTime.of(CURRENTDATE.getYear(), 6, 30, 0, 0, 0),
            "DENTAIRE",
            "Offre1");
    ProductElement productElement2 =
        createProductElement(
            LocalDateTime.of(CURRENTDATE.getYear(), 7, 1, 0, 0, 0), null, "DENTAIRE", "Offre1");
    ProductElement productElement3 =
        createProductElement(
            LocalDateTime.of(CURRENTDATE.getYear(), 1, 1, 0, 0, 0),
            LocalDateTime.of(CURRENTDATE.getYear(), 12, 31, 0, 0, 0),
            "DENTAIRE",
            "OffreReplace");
    ProductElement productElement4 =
        createProductElement(
            LocalDateTime.of(CURRENTDATE.getYear(), 7, 1, 0, 0, 0),
            null,
            "DENTAIRE",
            "OffreReplace");
    mockContractElements(
        List.of(productElement1, productElement2), List.of(productElement3, productElement4));

    List<DroitsTPExtended> droitsTPExtendeds = launchCalculDroitsTPPW3Calls();

    Assertions.assertEquals(3, droitsTPExtendeds.size());

    Assertions.assertEquals("DENTAIRE", droitsTPExtendeds.get(0).getDetailsOffline().getNature());
    Assertions.assertEquals(
        CURRENTDATE.getYear() + "-04-01", droitsTPExtendeds.get(0).getDateDebut());
    Assertions.assertEquals(
        CURRENTDATE.getYear() + "-06-30", droitsTPExtendeds.get(0).getDateFinOnline());
    Assertions.assertEquals(
        CURRENTDATE.getYear() + "-06-30", droitsTPExtendeds.get(0).getDateFin());

    Assertions.assertEquals("DENTAIRE", droitsTPExtendeds.get(1).getDetailsOffline().getNature());
    Assertions.assertEquals(
        CURRENTDATE.getYear() + "-07-01", droitsTPExtendeds.get(1).getDateDebut());
    Assertions.assertNull(droitsTPExtendeds.get(1).getDateFinOnline());
    Assertions.assertEquals(
        CURRENTDATE.getYear() + "-12-31", droitsTPExtendeds.get(1).getDateFin());

    Assertions.assertEquals("DENTAIRE", droitsTPExtendeds.get(2).getDetailsOffline().getNature());
    Assertions.assertEquals(
        CURRENTDATE.getYear() + "-01-01", droitsTPExtendeds.get(2).getDateDebut());
    Assertions.assertEquals(
        CURRENTDATE.getYear() + "-03-31", droitsTPExtendeds.get(2).getDateFin());
    Assertions.assertEquals(
        CURRENTDATE.getYear() + "-03-31", droitsTPExtendeds.get(2).getDateFinOnline());
  }

  private List<DroitsTPExtended> launchCalculDroitsTP()
      throws BobbNotFoundException,
          PwException,
          TriggerWarningException,
          CarenceException,
          BeneficiaryToIgnoreException {
    mockPWCarence("carences.json");
    TriggerDataForTesting.mockPWTpOfflineRightsWithReplacement(
        restConnector, "pw_response_Offre1.json", "pw_response_OffreReplace.json");
    DroitAssure droitAssureV3 = getDroitAssureV3(CURRENTDATE);
    return calculDroitsTPGenerationService.calculDroitsTP(
        droitAssureV3, CURRENTDATE.getYear() + "-01-01", CURRENTDATE.getYear() + "-12-31", null);
  }

  private List<DroitsTPExtended> launchCalculDroitsTPPW3Calls()
      throws BobbNotFoundException,
          PwException,
          TriggerWarningException,
          CarenceException,
          BeneficiaryToIgnoreException {
    mockPWCarence("carences.json");
    TriggerDataForTesting.mockPWTpOfflineRightsWithReplacement(
        restConnector,
        "pw_response_Offre1.json",
        "pw_response_Offre1.json",
        "pw_response_OffreReplace.json");
    DroitAssure droitAssureV3 = getDroitAssureV3(CURRENTDATE);
    return calculDroitsTPGenerationService.calculDroitsTP(
        droitAssureV3, CURRENTDATE.getYear() + "-01-01", CURRENTDATE.getYear() + "-12-31", null);
  }

  @Test
  void shouldNotReturnDroitsTPExtendedIncorrectOffer() {
    mockContractElement(null);
    mockPWCarence("carencescas7.json");
    TriggerDataForTesting.mockPWTpOfflineRightsWithReplacementDentaire(
        restConnector, "offrepwpasbonne");

    LocalDate currentdate = LocalDate.now(ZoneOffset.UTC);
    DroitAssure droitAssureV3 = getDroitAssureV3cas7(currentdate);
    PwException pwException =
        Assertions.assertThrows(
            PwException.class,
            () ->
                calculDroitsTPGenerationService.calculDroitsTP(
                    droitAssureV3,
                    currentdate.getYear() + "-01-01",
                    currentdate.getYear() + "-12-31",
                    null));
    Assertions.assertEquals(
        Anomaly.PRODUCT_WORKSHOP_SETTINGS_NOT_FOUND,
        pwException.getTriggeredBeneficiaryAnomaly().getAnomaly());
  }

  @Test
  void requestPeriodNotIncludedInperiodPw() {
    List<ParametrageAtelierProduit> parametrageAtelierProduits = new ArrayList<>();
    ParametrageAtelierProduit parametrageAtelierProduit = new ParametrageAtelierProduit();
    parametrageAtelierProduit.setPwValidityDate("2023-06-01");

    parametrageAtelierProduits.add(parametrageAtelierProduit);
    Assertions.assertTrue(
        calculDroitsTPGenerationService.requestPeriodNotIncludedInPeriodPw(
            parametrageAtelierProduits, LocalDate.of(2023, 1, 1), null));
  }

  @Test
  void requestPeriodNotIncludedInperiodPw2() {
    List<ParametrageAtelierProduit> parametrageAtelierProduits = new ArrayList<>();
    ParametrageAtelierProduit parametrageAtelierProduit = new ParametrageAtelierProduit();
    parametrageAtelierProduit.setPwValidityDate("2023-06-01");
    parametrageAtelierProduit.setPwEndValidityDate("2023-09-01");

    parametrageAtelierProduits.add(parametrageAtelierProduit);
    Assertions.assertTrue(
        calculDroitsTPGenerationService.requestPeriodNotIncludedInPeriodPw(
            parametrageAtelierProduits, LocalDate.of(2023, 1, 1), null));
  }

  @Test
  void requestPeriodNotIncludedInperiodPw3() {
    List<ParametrageAtelierProduit> parametrageAtelierProduits = new ArrayList<>();
    ParametrageAtelierProduit parametrageAtelierProduit = new ParametrageAtelierProduit();
    parametrageAtelierProduit.setPwValidityDate("2022-06-01");
    parametrageAtelierProduit.setPwEndValidityDate("2023-09-01");

    ParametrageAtelierProduit parametrageAtelierProduit2 = new ParametrageAtelierProduit();
    parametrageAtelierProduit2.setPwValidityDate("2023-09-02");

    parametrageAtelierProduits.add(parametrageAtelierProduit);
    parametrageAtelierProduits.add(parametrageAtelierProduit2);
    Assertions.assertFalse(
        calculDroitsTPGenerationService.requestPeriodNotIncludedInPeriodPw(
            parametrageAtelierProduits, LocalDate.of(2023, 1, 1), null));
  }

  @Test
  void requestPeriodNotIncludedInperiodPw4() {
    List<ParametrageAtelierProduit> parametrageAtelierProduits = new ArrayList<>();
    ParametrageAtelierProduit parametrageAtelierProduit = new ParametrageAtelierProduit();
    parametrageAtelierProduit.setPwValidityDate("2023-01-01");
    parametrageAtelierProduit.setPwEndValidityDate("2023-09-01");

    ParametrageAtelierProduit parametrageAtelierProduit2 = new ParametrageAtelierProduit();
    parametrageAtelierProduit2.setPwValidityDate("2023-09-02");

    parametrageAtelierProduits.add(parametrageAtelierProduit);
    parametrageAtelierProduits.add(parametrageAtelierProduit2);
    Assertions.assertFalse(
        calculDroitsTPGenerationService.requestPeriodNotIncludedInPeriodPw(
            parametrageAtelierProduits, LocalDate.of(2023, 1, 1), LocalDate.of(2023, 8, 1)));
  }

  @Test
  void shouldNotReturnDroitsTPExtendedIncorrectParameter() {
    mockContractElement(null);
    TriggerDataForTesting.mockPWV4TpRights("pw_wrong_offre.json", restConnector);

    LocalDate currentdate = LocalDate.now(ZoneOffset.UTC);
    DroitAssure droitAssureV3 = getDroitAssureV3cas7(currentdate);
    Assertions.assertThrows(
        PwException.class,
        () ->
            calculDroitsTPGenerationService.calculDroitsTP(
                droitAssureV3,
                currentdate.getYear() + "-01-01",
                currentdate.getYear() + "-12-31",
                null));
  }

  @Test
  void testCallBobb() throws BeneficiaryToIgnoreException {
    ContractElement contractElement = new ContractElement();
    contractElement.setCodeAMC("AMC");
    contractElement.setCodeInsurer("CI");
    ProductElement productElement = new ProductElement();
    productElement.setCodeAmc("AMC");
    productElement.setCodeProduct("CP");
    productElement.setCodeOffer("CO");
    productElement.setFrom(LocalDateTime.now().withMonth(10).withDayOfMonth(15));
    productElement.setTo(LocalDateTime.now().withMonth(12).withDayOfMonth(31));
    contractElement.setProductElements(List.of(productElement));
    when(contractElementService.get(Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean()))
        .thenReturn(contractElement);
    String dateDebut = LocalDate.now().getYear() + "-10-01";
    String dateFin = LocalDate.now().getYear() + "-12-31";

    Periode periodeCurrentYear = new Periode(dateDebut, null);
    List<Periode> periodesCurrentYear = new ArrayList<>();
    periodesCurrentYear.add(periodeCurrentYear);
    DroitAssure droit = new DroitAssure();
    droit.setCode("DRT");
    droit.setCodeAssureur("DRTASS");
    droit.setLibelle("Droit 2");
    droit.setOrdrePriorisation("1");
    droit.setType("DRT");
    droit.setPeriode(periodeCurrentYear);
    droit.setDateAncienneteGarantie(dateDebut);
    List<CarenceDroit> carences = new ArrayList<>();
    CarenceDroit carence = new CarenceDroit();
    carence.setCode("CAR001");
    carence.setPeriode(new PeriodeCarence(dateDebut, dateFin));
    carences.add(carence);
    droit.setCarences(carences);

    var res = calculDroitsTPGenerationService.callBobbIncludingIgnored(droit, periodeCurrentYear);
    Assertions.assertNotNull(res);
  }
}
