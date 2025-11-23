package com.cegedim.next.serviceeligibility.core.utility;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.consultation_droit.GetInfoBddRequestDto;
import com.cegedim.next.serviceeligibility.core.services.GlobalValidationService;
import com.cegedim.next.serviceeligibility.core.utils.RequestValidator;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RequestValidationListException;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointRequest;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointRequestV5;
import com.cegedimassurances.norme.amc.TypeAmc;
import com.cegedimassurances.norme.base_de_droit.TypeInfoBdd;
import com.cegedimassurances.norme.base_de_droit.TypeInfoBdd.ListeSegmentRecherche;
import com.cegedimassurances.norme.base_de_droit.TypeInfoBdd.TypeProfondeurRecherche;
import com.cegedimassurances.norme.base_de_droit.TypeInfoBdd.TypeRechercheBeneficiaire;
import com.cegedimassurances.norme.base_de_droit.TypeInfoBdd.TypeRechercheSegment;
import com.cegedimassurances.norme.beneficiaire.TypeBeneficiaireDemandeur;
import jakarta.validation.Validation;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RequestValidatorTest {

  RequestValidator requestValidator;

  @BeforeAll
  public void initTests() {
    requestValidator = new RequestValidator();
    GlobalValidationService validationService = new GlobalValidationService();
    requestValidator.globalValidationService = validationService;
    validationService.validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  GetInfoBddRequestDto getRequest() {
    GetInfoBddRequestDto request = new GetInfoBddRequestDto();
    TypeBeneficiaireDemandeur beneficiaire = new TypeBeneficiaireDemandeur();
    beneficiaire.setNIRCertifie("1234567890123");
    beneficiaire.setCleNIR("12");
    beneficiaire.setDateNaissance("12345678");
    beneficiaire.setRangGemellaire(1);
    TypeAmc amc = new TypeAmc();
    amc.setAdherent("1234567890");
    amc.setNom("name");
    amc.setNumeroAMCPrefectoral("1234567890");
    TypeInfoBdd infoBdd = new TypeInfoBdd();
    infoBdd.setTypeRechercheBenef(TypeRechercheBeneficiaire.BENEFICIAIRE);
    infoBdd.setTypeRechercheSegment(TypeRechercheSegment.LISTE_SEGMENT);
    ListeSegmentRecherche list = new ListeSegmentRecherche();
    list.createNewInstance();
    List<String> segments = list.getSegmentRecherche();
    segments.add("PHAR");
    segments.add("PHOR");
    infoBdd.setListeSegmentRecherche(list);
    infoBdd.setTypeProfondeurRecherche(TypeProfondeurRecherche.SANS_FORMULES);
    request.setBeneficiaire(beneficiaire);
    request.setAmc(amc);
    request.setInfoBdd(infoBdd);
    return request;
  }

  void test(RequestValidationListException e, int count) {
    Assertions.assertEquals(count, e.getExceptions().size());
  }

  @Test
  void testValidation() {
    GetInfoBddRequestDto request = getRequest();
    requestValidator.validateRequestV4(request);
  }

  @Test
  void failOnNir() {
    GetInfoBddRequestDto request = getRequest();
    request.getBeneficiaire().setNIRCertifie(null);
    try {
      requestValidator.validateRequestV4(request);
    } catch (RequestValidationListException e) {
      test(e, 1);
    }
  }

  @Test
  void failOnCleNir() {
    GetInfoBddRequestDto request = getRequest();
    request.getBeneficiaire().setCleNIR(null);
    try {
      requestValidator.validateRequestV4(request);
    } catch (RequestValidationListException e) {
      test(e, 1);
    }
  }

  @Test
  void failOnCleNir2() {
    GetInfoBddRequestDto request = getRequest();
    request.getBeneficiaire().setCleNIR("123");
    try {
      requestValidator.validateRequestV4(request);
    } catch (RequestValidationListException e) {
      test(e, 1);
    }
  }

  @Test
  void failOnDatanaissance() {
    GetInfoBddRequestDto request = getRequest();
    request.getBeneficiaire().setDateNaissance("123");
    try {
      requestValidator.validateRequestV4(request);
    } catch (RequestValidationListException e) {
      test(e, 1);
    }
    request.getBeneficiaire().setDateNaissance(null);
    try {
      requestValidator.validateRequestV4(request);
    } catch (RequestValidationListException e) {
      test(e, 1);
    }
  }

  @Test
  void failOnAmc() {
    GetInfoBddRequestDto request = getRequest();
    request.getAmc().setNumeroAMCPrefectoral("123456789012");
    try {
      requestValidator.validateRequestV4(request);
    } catch (RequestValidationListException e) {
      test(e, 1);
    }
  }

  @Test
  void failOnSegment() {
    GetInfoBddRequestDto request = getRequest();
    ListeSegmentRecherche list = request.getInfoBdd().getListeSegmentRecherche();
    request.getInfoBdd().setListeSegmentRecherche(null);
    try {
      requestValidator.validateRequestV4(request);
    } catch (RequestValidationListException e) {
      test(e, 1);
    }

    list.getSegmentRecherche().add("TOO LONG ELEMENT");

    request.getInfoBdd().setListeSegmentRecherche(list);
    try {
      requestValidator.validateRequestV4(request);
    } catch (RequestValidationListException e) {
      test(e, 1);
    }
  }

  @Test
  void failValidateRequestUAPAllNull() {
    UniqueAccessPointRequest uniqueAccessPointRequest =
        new UniqueAccessPointRequestV5(
            null, null, null, null, null, null, null, null, null, null, null, null, null, false);
    try {
      RequestValidator.validateRequestUAP(uniqueAccessPointRequest);
    } catch (RequestValidationListException e) {
      test(e, 4);
      Assertions.assertEquals("startDate est obligatoire", e.getExceptions().get(0).getMessage());
      Assertions.assertEquals(
          "NEXT-SERVICEELIGIBILITY-CORE-00062", e.getExceptions().get(0).getCustomErrorCode());
      Assertions.assertEquals("insurerId est obligatoire", e.getExceptions().get(1).getMessage());
      Assertions.assertEquals(
          "NEXT-SERVICEELIGIBILITY-CORE-00062", e.getExceptions().get(1).getCustomErrorCode());
      Assertions.assertEquals(
          "Veuillez renseigner les critères de recherche du bénéficiaire",
          e.getExceptions().get(2).getMessage());
      Assertions.assertEquals(
          "NEXT-SERVICEELIGIBILITY-CORE-00165", e.getExceptions().get(2).getCustomErrorCode());
      Assertions.assertEquals("context est obligatoire", e.getExceptions().get(3).getMessage());
      Assertions.assertEquals(
          "NEXT-SERVICEELIGIBILITY-CORE-00062", e.getExceptions().get(3).getCustomErrorCode());
    }
  }

  @Test
  void failValidateRequestUAPIncorrectDateFormatAndWrongContext() {
    UniqueAccessPointRequest uniqueAccessPointRequest =
        new UniqueAccessPointRequestV5(
            null,
            null,
            null,
            "2020-0200",
            "2020-0300",
            null,
            null,
            null,
            "TP_ORLANE",
            null,
            "1",
            null,
            null,
            false);
    try {
      RequestValidator.validateRequestUAP(uniqueAccessPointRequest);
    } catch (RequestValidationListException e) {
      test(e, 4);
      Assertions.assertEquals("insurerId est obligatoire", e.getExceptions().get(0).getMessage());
      Assertions.assertEquals(
          "NEXT-SERVICEELIGIBILITY-CORE-00062", e.getExceptions().get(0).getCustomErrorCode());
      Assertions.assertEquals(
          "'startDate' avec la valeur '2020-0200' ne respecte pas le format 'yyyy-MM-dd' .",
          e.getExceptions().get(1).getMessage());
      Assertions.assertEquals(
          "NEXT-SERVICEELIGIBILITY-CORE-00064", e.getExceptions().get(1).getCustomErrorCode());
      Assertions.assertEquals(
          "'endDate' avec la valeur '2020-0300' ne respecte pas le format 'yyyy-MM-dd' .",
          e.getExceptions().get(2).getMessage());
      Assertions.assertEquals(
          "NEXT-SERVICEELIGIBILITY-CORE-00064", e.getExceptions().get(2).getCustomErrorCode());
      Assertions.assertEquals(
          "Le contexte 'TP_ORLANE' est inconnu. Les contextes permis sont 'HTP', 'TP_ONLINE' et 'TP_OFFLINE'.",
          e.getExceptions().get(3).getMessage());
      Assertions.assertEquals(
          "NEXT-SERVICEELIGIBILITY-CORE-00063", e.getExceptions().get(3).getCustomErrorCode());
    }
  }

  @Test
  void failValidateRequestUAPIncorrectBirthDateAndMultipleContext() {
    UniqueAccessPointRequest uniqueAccessPointRequest =
        new UniqueAccessPointRequestV5(
            "123456789",
            "1980-0605",
            "1",
            "2020-02-01",
            "2020-03-01",
            "BALOO",
            null,
            null,
            "TP_ONLINE, TP_OFFLINE",
            null,
            "1",
            null,
            null,
            false);
    try {
      RequestValidator.validateRequestUAP(uniqueAccessPointRequest);
    } catch (RequestValidationListException e) {
      test(e, 2);
      Assertions.assertEquals(
          "La date de naissance 1980-0605 n'est pas valide", e.getExceptions().get(0).getMessage());
      Assertions.assertEquals(
          "NEXT-SERVICEELIGIBILITY-CORE-00064", e.getExceptions().get(0).getCustomErrorCode());
      Assertions.assertEquals(
          "Le paramètre context doit ne contenir qu'un seul contexte",
          e.getExceptions().get(1).getMessage());
      Assertions.assertEquals(
          "NEXT-SERVICEELIGIBILITY-CORE-00063", e.getExceptions().get(1).getCustomErrorCode());
    }
  }

  @Test
  void failValidateRequestUAPIncorrectBeneficiary() {
    UniqueAccessPointRequest uniqueAccessPointRequest =
        new UniqueAccessPointRequestV5(
            "123456789",
            null,
            "1",
            "2020-02-01",
            "2020-03-01",
            "BALOO",
            null,
            null,
            "TP_ONLINE",
            null,
            null,
            null,
            null,
            false);
    try {
      RequestValidator.validateRequestUAP(uniqueAccessPointRequest);
    } catch (RequestValidationListException e) {
      test(e, 1);
      Assertions.assertEquals(
          "Veuillez renseigner les critères de recherche du bénéficiaire",
          e.getExceptions().getFirst().getMessage());
      Assertions.assertEquals(
          "NEXT-SERVICEELIGIBILITY-CORE-00165", e.getExceptions().getFirst().getCustomErrorCode());
    }
  }

  @Test
  void failValidateRequestUAPIncorrectBeneficiary2AndStartDateGreaterThanEndDate() {
    UniqueAccessPointRequest uniqueAccessPointRequest =
        new UniqueAccessPointRequestV5(
            "123456789",
            "19800605",
            null,
            "2020-04-01",
            "2020-03-01",
            "BALOO",
            null,
            null,
            "TP_ONLINE",
            null,
            null,
            null,
            null,
            false);
    try {
      RequestValidator.validateRequestUAP(uniqueAccessPointRequest);
    } catch (RequestValidationListException e) {
      test(e, 2);
      Assertions.assertEquals(
          "Veuillez renseigner les critères de recherche du bénéficiaire",
          e.getExceptions().get(0).getMessage());
      Assertions.assertEquals(
          "NEXT-SERVICEELIGIBILITY-CORE-00165", e.getExceptions().get(0).getCustomErrorCode());
      Assertions.assertEquals(
          "La date de début d'interrogation est supérieure à la date de fin.",
          e.getExceptions().get(1).getMessage());
      Assertions.assertEquals(
          "NEXT-SERVICEELIGIBILITY-CORE-00065", e.getExceptions().get(1).getCustomErrorCode());
    }
  }

  @Test
  void failValidateRequestUAPIncorrectBeneficiary3() {
    UniqueAccessPointRequest uniqueAccessPointRequest =
        new UniqueAccessPointRequestV5(
            null,
            "19800605",
            "1",
            "2020-02-01",
            "2020-03-01",
            "BALOO",
            null,
            null,
            "TP_ONLINE",
            null,
            null,
            null,
            null,
            false);
    try {
      RequestValidator.validateRequestUAP(uniqueAccessPointRequest);
    } catch (RequestValidationListException e) {
      test(e, 1);
      Assertions.assertEquals(
          "Veuillez renseigner les critères de recherche du bénéficiaire",
          e.getExceptions().getFirst().getMessage());
      Assertions.assertEquals(
          "NEXT-SERVICEELIGIBILITY-CORE-00165", e.getExceptions().getFirst().getCustomErrorCode());
    }
  }

  @Test
  void failValidateRequestUAPV5WithNoBirthDateInContextTP() {
    UniqueAccessPointRequest uniqueAccessPointRequest =
        new UniqueAccessPointRequestV5(
            "123456789",
            null,
            "1",
            "2020-02-01",
            "2020-03-01",
            "BALOO",
            null,
            null,
            "TP_ONLINE",
            null,
            null,
            null,
            null,
            false);
    try {
      RequestValidator.validateRequestUAP(uniqueAccessPointRequest);
    } catch (RequestValidationListException e) {
      test(e, 1);
      Assertions.assertEquals(
          "Veuillez renseigner les critères de recherche du bénéficiaire",
          e.getExceptions().getFirst().getMessage());
      Assertions.assertEquals(
          "NEXT-SERVICEELIGIBILITY-CORE-00165", e.getExceptions().getFirst().getCustomErrorCode());
    }
  }

  @Test
  void notFailValidateRequestUAPV5WithNoBirthDateInContextHTP() {
    UniqueAccessPointRequest uniqueAccessPointRequest =
        new UniqueAccessPointRequestV5(
            "123456789",
            null,
            "1",
            "2020-02-01",
            "2020-03-01",
            "BALOO",
            null,
            null,
            "HTP",
            null,
            null,
            null,
            null,
            false);
    try {
      RequestValidator.validateRequestUAP(uniqueAccessPointRequest);
    } catch (RequestValidationListException e) {
      test(e, 0);
    }
  }

  @Test
  void notFailValidateRequestUAPV5WithNoNirCodeContextHTP() {
    UniqueAccessPointRequest uniqueAccessPointRequest =
        new UniqueAccessPointRequestV5(
            null,
            "19800605",
            "1",
            "2020-02-01",
            "2020-03-01",
            "BALOO",
            "pipo",
            null,
            "HTP",
            null,
            null,
            null,
            null,
            false);
    try {
      RequestValidator.validateRequestUAP(uniqueAccessPointRequest);
    } catch (RequestValidationListException e) {
      test(e, 0);
    }
  }

  @Test
  void failValidateRequestUAPV5WithNoNirCodeAndNoSubrscriberIdContextTP() {
    UniqueAccessPointRequest uniqueAccessPointRequest =
        new UniqueAccessPointRequestV5(
            null,
            "19800605",
            "1",
            "2020-02-01",
            "2020-03-01",
            "BALOO",
            null,
            null,
            "TP_ONLINE",
            null,
            null,
            null,
            null,
            false);
    try {
      RequestValidator.validateRequestUAP(uniqueAccessPointRequest);
    } catch (RequestValidationListException e) {
      test(e, 1);
      Assertions.assertEquals(
          "Veuillez renseigner les critères de recherche du bénéficiaire",
          e.getExceptions().getFirst().getMessage());
      Assertions.assertEquals(
          "NEXT-SERVICEELIGIBILITY-CORE-00165", e.getExceptions().getFirst().getCustomErrorCode());
    }
  }

  @Test
  void noFailValidateRequestUAPV5WithNoNirCodeButSubrscriberIdContextTP() {
    UniqueAccessPointRequest uniqueAccessPointRequest =
        new UniqueAccessPointRequestV5(
            null,
            "19800605",
            "1",
            "2020-02-01",
            "2020-03-01",
            "BALOO",
            "1234556",
            null,
            "TP_ONLINE",
            null,
            null,
            null,
            null,
            false);
    try {
      RequestValidator.validateRequestUAP(uniqueAccessPointRequest);
    } catch (RequestValidationListException e) {
      test(e, 0);
    }
  }

  @Test
  void notFailValidateRequestUAPV5() {
    UniqueAccessPointRequest uniqueAccessPointRequest =
        new UniqueAccessPointRequestV5(
            "123456789",
            "19800605",
            "1",
            "2020-02-01",
            "2020-03-01",
            "BALOO",
            null,
            null,
            "TP_ONLINE",
            null,
            null,
            null,
            null,
            false);
    try {
      RequestValidator.validateRequestUAP(uniqueAccessPointRequest);
    } catch (RequestValidationListException e) {
      test(e, 0);
    }
  }
}
