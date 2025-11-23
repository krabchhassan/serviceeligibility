package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.beyond.serviceeligibility.common.organisation.OrganisationServiceWrapper;
import com.cegedim.beyond.spring.configuration.properties.organisation.Organisation;
import com.cegedim.next.serviceeligibility.core.bobb.ContractElement;
import com.cegedim.next.serviceeligibility.core.bobb.ProductElementLight;
import com.cegedim.next.serviceeligibility.core.bobb.services.ContractElementService;
import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.mapper.MapperContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.CarenceDroit;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContexteTPV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratCollectifV6;
import com.cegedim.next.serviceeligibility.core.referential.Referential;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.pojo.ContractValidationBean;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.TestingDataForValidationService;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ValidationContractException;
import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class ValidationServiceContratTest {
  private final Logger logger = LoggerFactory.getLogger(ValidationServiceContratTest.class);

  @Autowired ValidationContratService service;

  @Autowired MongoTemplate template;

  @SpyBean ContractElementService contractElementService;

  @Autowired EventService eventService;

  @SpyBean ReferentialService referentialService;

  @BeforeEach
  void init() {
    // ProductElementLight
    ProductElementLight product = new ProductElementLight();
    product.setCodeOffer("OFFER1");
    product.setCodeProduct("PRODUCT1");
    AggregationResults<ProductElementLight> res =
        new AggregationResults<>(List.of(product), new Document());
    Mockito.when(
            template.aggregate(
                Mockito.any(Aggregation.class),
                Mockito.anyString(),
                Mockito.eq(ProductElementLight.class)))
        .thenReturn(res);

    ReflectionTestUtils.setField(
        service, "organisationServiceWrapper", getMockedOrganisationService());
    service.setControleCorrespondanceBobb(false);
    service.setUseReferentialValidation(false);
    service.setControleMetierHtp(false);
  }

  private OrganisationServiceWrapper getMockedOrganisationService() {
    OrganisationServiceWrapper organisationServiceMock =
        Mockito.mock(OrganisationServiceWrapper.class);
    Organisation org = new Organisation();
    org.setMain(true);
    org.setCode(null);
    org.setFullName("s3OrgaFullName");
    org.setCommercialName("s3OrgaCommercialName");
    try {
      Mockito.doAnswer(
              invocation -> {
                org.setCode(invocation.getArgument(0));
                return org;
              })
          .when(organisationServiceMock)
          .getOrganizationByAmcNumber(Mockito.any());
      Mockito.when(organisationServiceMock.isOrgaAttached(Mockito.anyString(), Mockito.anyString()))
          .thenReturn(true);
    } catch (Exception ignore) {
    }
    return organisationServiceMock;
  }

  @Test
  void should_validate_nir() {
    boolean res =
        com.cegedim.common.base.core.services.ValidationService.isValidNir("1840197416357", "")
            .isValid();
    Assertions.assertFalse(res);

    res = com.cegedim.common.base.core.services.ValidationService.isValidNir("", "").isValid();
    Assertions.assertTrue(res);

    res =
        com.cegedim.common.base.core.services.ValidationService.isValidNir("1840197416357", "84")
            .isValid();
    Assertions.assertFalse(res);

    res =
        com.cegedim.common.base.core.services.ValidationService.isValidNir("1840197416357", "85")
            .isValid();
    Assertions.assertTrue(res);

    // NIR Corse
    res =
        com.cegedim.common.base.core.services.ValidationService.isValidNir("179102A103259", "40")
            .isValid();
    Assertions.assertTrue(res);
    // NIR Corse
    res =
        com.cegedim.common.base.core.services.ValidationService.isValidNir("179102B033988", "80")
            .isValid();
    Assertions.assertTrue(res);
  }

  @Test
  void should_validate_contratV5() {
    ContratAIV5 contrat = getContratV5(true);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      logger.info("ve: {}", e.getMessage());
      Assertions.fail();
    }
    Assertions.assertTrue(true);
  }

  @Test
  void should_validate_contratV6() {
    ContratAIV6 contrat = getContratV6(true);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      logger.info("ve: {}", e.getMessage());
      Assertions.fail();
    }
    Assertions.assertTrue(true);
  }

  @Test
  void should_NotValidate_contratV5WithBobb() {
    ContratAIV5 contrat = getContratV5(true);
    service.setControleCorrespondanceBobb(true);
    ContractValidationBean contractValidationBean = new ContractValidationBean();
    try {
      service.validateContrat(contrat, contractValidationBean);
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : Le produit BO code REM et assureur RAS n'a pas de correspondance dans Beyond",
          contractValidationBean.getErrorValidationBeans().getFirst().getError());
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : Le produit BO code c et assureur ca n'a pas de correspondance dans Beyond",
          contractValidationBean.getErrorValidationBeans().get(1).getError());
    }
    service.setControleCorrespondanceBobb(false);
  }

  @Test
  void should_Validate_contratV5WithBobb() {
    ContratAIV5 contrat = getContratV5(true);
    ContractElement contractElement = new ContractElement();
    contractElement.setCodeInsurer("A");
    contractElement.setCodeContractElement("B");

    service.setControleCorrespondanceBobb(true);
    Mockito.when(
            contractElementService.get(
                Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean()))
        .thenReturn(contractElement);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      logger.info("ve: {}", e.getMessage());
      Assertions.fail();
    }
    Assertions.assertTrue(true);
    service.setControleCorrespondanceBobb(false);
  }

  @Test
  void should_validate_contratV5_without_rib() {
    ContratAIV5 contrat = getContratV5(true);
    DestinatairePrestations destinatairePrestations = new DestinatairePrestations();
    destinatairePrestations.setIdDestinatairePaiements("7788552244");

    NomDestinataire nomDestPrest = new NomDestinataire();
    nomDestPrest.setRaisonSociale("Destinataire groupe");
    destinatairePrestations.setNom(nomDestPrest);

    ModePaiement modePaiement = new ModePaiement();
    modePaiement.setCode("VIR");
    modePaiement.setLibelle("Virement");
    modePaiement.setCodeMonnaie("EUR");
    destinatairePrestations.setModePaiementPrestations(modePaiement);

    PeriodeDestinataire periode = new PeriodeDestinataire();
    periode.setDebut("2020-07-01");
    periode.setFin("2020-12-31");
    destinatairePrestations.setPeriode(periode);

    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .add(destinatairePrestations);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      logger.info("ve: {}", e.getMessage());
      Assertions.fail();
    }
    Assertions.assertTrue(true);
  }

  @Test
  void should_validate_contratV6_without_rib() {
    ContratAIV6 contrat = getContratV6(true);
    DestinatairePrestations destinatairePrestations = new DestinatairePrestations();
    destinatairePrestations.setIdDestinatairePaiements("7788552244");

    NomDestinataire nomDestPrest = new NomDestinataire();
    nomDestPrest.setRaisonSociale("Destinataire groupe");
    destinatairePrestations.setNom(nomDestPrest);

    ModePaiement modePaiement = new ModePaiement();
    modePaiement.setCode("VIR");
    modePaiement.setLibelle("Virement");
    modePaiement.setCodeMonnaie("EUR");
    destinatairePrestations.setModePaiementPrestations(modePaiement);

    PeriodeDestinataire periode = new PeriodeDestinataire();
    periode.setDebut("2020-07-01");
    periode.setFin("2020-12-31");
    destinatairePrestations.setPeriode(periode);

    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .add(destinatairePrestations);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      logger.info("ve: {}", e.getMessage());
      Assertions.fail();
    }
    Assertions.assertTrue(true);
  }

  @Test
  void should_validate_contratV5_without_periode_in_periode_suspension() {
    ContratAIV5 contrat = getContratV5(true);
    List<PeriodeSuspension> periodeSuspensions = new ArrayList<>();
    PeriodeSuspension periodeSuspension = new PeriodeSuspension();
    periodeSuspension.setTypeSuspension("Provisoire");
    periodeSuspension.setMotifSuspension("Non Paiement des cotis de février");
    periodeSuspension.setMotifLeveeSuspension("");
    periodeSuspensions.add(periodeSuspension);
    contrat.setPeriodesSuspension(periodeSuspensions);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : l'information periodesSuspension.periode est obligatoire",
          e.getLocalizedMessage());
    }
  }

  @Test
  void should_validate_contratV5_without_periode_suspension_start() {
    ContratAIV5 contrat = getContratV5(true);
    List<PeriodeSuspension> periodeSuspensions = new ArrayList<>();
    PeriodeSuspension periodeSuspension = new PeriodeSuspension();
    Periode periode = new Periode();
    periodeSuspension.setPeriode(periode);
    periodeSuspension.setTypeSuspension("Provisoire");
    periodeSuspension.setMotifSuspension("Non Paiement des cotis de février");
    periodeSuspension.setMotifLeveeSuspension("");
    periodeSuspensions.add(periodeSuspension);
    contrat.setPeriodesSuspension(periodeSuspensions);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : l'information periodesSuspension.periode.debut est obligatoire",
          e.getLocalizedMessage());
    }
  }

  @Test
  void should_validate_contratV5_without_GoodDemat() {
    ContratAIV5 contrat = getContratV5(true);
    DataAssure data = contrat.getAssures().getFirst().getData();
    Dematerialisation dematerialisation = new Dematerialisation();
    dematerialisation.setIsDematerialise(true);
    data.getDestinatairesRelevePrestations().getFirst().setDematerialisation(dematerialisation);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information data.destinatairesRelevePrestations.dematerialisation.email ou data.destinatairesRelevePrestations.dematerialisation.mobile est obligatoire",
          e.getLocalizedMessage());
    }
  }

  @Test
  void should_validate_contratV5_with_wrong_date_format() {
    ContratAIV5 contrat = getContratV5(true);
    List<PeriodeSuspension> periodeSuspensions = new ArrayList<>();
    PeriodeSuspension periodeSuspension = new PeriodeSuspension();
    Periode periode = new Periode();
    periode.setDebut("2021-12-1s0");
    periodeSuspension.setPeriode(periode);
    periodeSuspension.setTypeSuspension("Provisoire");
    periodeSuspension.setMotifSuspension("Non Paiement des cotis de février");
    periodeSuspension.setMotifLeveeSuspension("");
    periodeSuspensions.add(periodeSuspension);
    contrat.setPeriodesSuspension(periodeSuspensions);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : L'information periodesSuspension.periode.debut(2021-12-1s0) n'est pas une date au format yyyy-MM-dd",
          e.getLocalizedMessage());
    }
  }

  @Test
  void should_validate_contratV5_with_wrong_type_suspension() {
    ContratAIV5 contrat = getContratV5(true);
    List<PeriodeSuspension> periodeSuspensions = new ArrayList<>();
    PeriodeSuspension periodeSuspension = new PeriodeSuspension();
    Periode periode = new Periode();
    periode.setDebut("2021-12-10");
    periodeSuspension.setPeriode(periode);
    periodeSuspension.setTypeSuspension("Test");
    periodeSuspension.setMotifSuspension("Non Paiement des cotis de février");
    periodeSuspension.setMotifLeveeSuspension("");
    periodeSuspensions.add(periodeSuspension);
    contrat.setPeriodesSuspension(periodeSuspensions);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : L'information periodesSuspension.typeSuspension ne contient pas une valeur autorisée",
          e.getLocalizedMessage());
    }
  }

  @Test
  void should_validate_contratV5_without_type_suspension() {
    ContratAIV5 contrat = getContratV5(true);
    List<PeriodeSuspension> periodeSuspensions = new ArrayList<>();
    PeriodeSuspension periodeSuspension = new PeriodeSuspension();
    Periode periode = new Periode();
    periode.setDebut("2021-12-10");
    periodeSuspension.setPeriode(periode);
    periodeSuspension.setMotifSuspension("Non Paiement des cotis de février");
    periodeSuspension.setMotifLeveeSuspension("");
    periodeSuspensions.add(periodeSuspension);
    contrat.setPeriodesSuspension(periodeSuspensions);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : L'information periodesSuspension.typeSuspension est obligatoire",
          e.getLocalizedMessage());
    }
  }

  @Test
  void should_validate_contratV6_without_periode_in_periode_suspension() {
    ContratAIV6 contrat = getContratV6(true);
    List<PeriodeSuspension> periodeSuspensions = new ArrayList<>();
    PeriodeSuspension periodeSuspension = new PeriodeSuspension();
    periodeSuspension.setTypeSuspension("Provisoire");
    periodeSuspension.setMotifSuspension("Non Paiement des cotis de février");
    periodeSuspension.setMotifLeveeSuspension("");
    periodeSuspensions.add(periodeSuspension);
    contrat.setPeriodesSuspension(periodeSuspensions);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : l'information periodesSuspension.periode est obligatoire",
          e.getLocalizedMessage());
    }
  }

  @Test
  void should_validate_contratV6_without_periode_suspension_start() {
    ContratAIV6 contrat = getContratV6(true);
    List<PeriodeSuspension> periodeSuspensions = new ArrayList<>();
    PeriodeSuspension periodeSuspension = new PeriodeSuspension();
    Periode periode = new Periode();
    periodeSuspension.setPeriode(periode);
    periodeSuspension.setTypeSuspension("Provisoire");
    periodeSuspension.setMotifSuspension("Non Paiement des cotis de février");
    periodeSuspension.setMotifLeveeSuspension("");
    periodeSuspensions.add(periodeSuspension);
    contrat.setPeriodesSuspension(periodeSuspensions);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : l'information periodesSuspension.periode.debut est obligatoire",
          e.getLocalizedMessage());
    }
  }

  @Test
  void should_validate_contratV6_without_GoodDemat() {
    ContratAIV6 contrat = getContratV6(true);
    DataAssure data = contrat.getAssures().getFirst().getData();
    Dematerialisation dematerialisation = new Dematerialisation();
    dematerialisation.setIsDematerialise(true);
    data.getDestinatairesRelevePrestations().getFirst().setDematerialisation(dematerialisation);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information data.destinatairesRelevePrestations.dematerialisation.email ou data.destinatairesRelevePrestations.dematerialisation.mobile est obligatoire",
          e.getLocalizedMessage());
    }
  }

  @Test
  void should_validate_contratV6_with_wrong_date_format() {
    ContratAIV6 contrat = getContratV6(true);
    List<PeriodeSuspension> periodeSuspensions = new ArrayList<>();
    PeriodeSuspension periodeSuspension = new PeriodeSuspension();
    Periode periode = new Periode();
    periode.setDebut("2021-12-1s0");
    periodeSuspension.setPeriode(periode);
    periodeSuspension.setTypeSuspension("Provisoire");
    periodeSuspension.setMotifSuspension("Non Paiement des cotis de février");
    periodeSuspension.setMotifLeveeSuspension("");
    periodeSuspensions.add(periodeSuspension);
    contrat.setPeriodesSuspension(periodeSuspensions);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : L'information periodesSuspension.periode.debut(2021-12-1s0) n'est pas une date au format yyyy-MM-dd",
          e.getLocalizedMessage());
    }
  }

  @Test
  void should_validate_contratV6_with_wrong_type_suspension() {
    ContratAIV6 contrat = getContratV6(true);
    List<PeriodeSuspension> periodeSuspensions = new ArrayList<>();
    PeriodeSuspension periodeSuspension = new PeriodeSuspension();
    Periode periode = new Periode();
    periode.setDebut("2021-12-10");
    periodeSuspension.setPeriode(periode);
    periodeSuspension.setTypeSuspension("Test");
    periodeSuspension.setMotifSuspension("Non Paiement des cotis de février");
    periodeSuspension.setMotifLeveeSuspension("");
    periodeSuspensions.add(periodeSuspension);
    contrat.setPeriodesSuspension(periodeSuspensions);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : L'information periodesSuspension.typeSuspension ne contient pas une valeur autorisée",
          e.getLocalizedMessage());
    }
  }

  @Test
  void should_validate_contratV6_without_type_suspension() {
    ContratAIV6 contrat = getContratV6(true);
    List<PeriodeSuspension> periodeSuspensions = new ArrayList<>();
    PeriodeSuspension periodeSuspension = new PeriodeSuspension();
    Periode periode = new Periode();
    periode.setDebut("2021-12-10");
    periodeSuspension.setPeriode(periode);
    periodeSuspension.setMotifSuspension("Non Paiement des cotis de février");
    periodeSuspension.setMotifLeveeSuspension("");
    periodeSuspensions.add(periodeSuspension);
    contrat.setPeriodesSuspension(periodeSuspensions);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : L'information periodesSuspension.typeSuspension est obligatoire",
          e.getLocalizedMessage());
    }
  }

  @Test
  void should_not_validate_contratV5() {
    // Date de naissance invalide
    ContratAIV5 contrat = getContratV5(true);
    contrat.getAssures().getFirst().getIdentite().setDateNaissance("1987124");
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information identite.dateNaissance(1987124) n'est pas une date au format yyyyMMdd",
          e.getLocalizedMessage());
    }

    // Date d'une periode invalide
    contrat = getContratV5(true);
    contrat.getPeriodesContratResponsableOuvert().getFirst().setDebut("2020/01/01");
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : L'information periodesContratResponsableOuvert.periode.debut(2020/01/01) n'est pas une date au format yyyy-MM-dd",
          e.getLocalizedMessage());
    }

    // clé de nir invalide - hors limite
    contrat = getContratV5(true);
    contrat.getAssures().getFirst().getIdentite().getNir().setCle("104");
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : le NIR (1840197416357 / 104) de identite.nir n'est pas valide.",
          e.getLocalizedMessage());
    }

    // clé de nir invalide - nom Numérique
    contrat = getContratV5(true);
    contrat.getAssures().getFirst().getIdentite().getNir().setCle("UN");
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : le NIR (1840197416357 / UN) de identite.nir n'est pas valide.",
          e.getLocalizedMessage());
    }

    // Longueur d'IBAN invalide
    contrat = getContratV5(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getRib()
        .setIban("FR123515");
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : la longueur de l'IBAN (FR123515) doit être comprise entre 14 et 34",
          e.getLocalizedMessage());
    }

    // Longueur d'IBAN invalide
    contrat = getContratV5(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getRib()
        .setIban("FR12332103210321032103106506540654561606515");
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : la longueur de l'IBAN (FR12332103210321032103106506540654561606515) doit être comprise entre 14 et 34",
          e.getLocalizedMessage());
    }

    // IBAN invalide
    contrat = getContratV5(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getRib()
        .setIban("FR8130003000708267412316T44");
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'IBAN (FR8130003000708267412316T44) n'est pas valide.",
          e.getLocalizedMessage());
    }

    // BIC invalide
    contrat = getContratV5(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getRib()
        .setBic("XXX1");
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : le BIC (XXX1) n'est pas conforme.",
          e.getLocalizedMessage());
    }

    // NIR invalide
    contrat = getContratV5(true);
    contrat.getAssures().getFirst().getIdentite().getNir().setCode("1791062518142");
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : le NIR (1791062518142 / 85) de identite.nir n'est pas valide.",
          e.getLocalizedMessage());
    }

    // NIR invalide
    contrat = getContratV5(true);
    contrat
        .getAssures()
        .getFirst()
        .getIdentite()
        .getAffiliationsRO()
        .getFirst()
        .getNir()
        .setCode("1791062518142");
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : le NIR (1791062518142 / 58) de identite.affiliationsRO.nir n'est pas valide.",
          e.getLocalizedMessage());
    }
  }

  @Test
  void should_not_validate_contratV6_invalid_model() {
    List<Periode> periodesNull = new ArrayList<>();
    ReflectionTestUtils.setField(
        service, "organisationServiceWrapper", getMockedOrganisationService());
    // Contrat obligatoire
    ContratAIV6 contratNull = null;
    try {
      service.validateContrat(contratNull, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals("Le contrat est obligatoire", e.getLocalizedMessage());
    }

    validateContratFieldsTest();
    ContratAIV6 contrat;

    // periodesContratResponsableOuvert.debut obligatoire
    contrat = getContratV6(true);
    contrat.getPeriodesContratResponsableOuvert().getFirst().setDebutToNull();
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : L'information periodesContratResponsableOuvert.debut est obligatoire",
          e.getLocalizedMessage());
    }

    // periodesContratCMUOuvert.debut obligatoire
    contrat = getContratV6(true);
    contrat.getPeriodesContratCMUOuvert().getFirst().getPeriode().setDebutToNull();
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : L'information periodesContratCMUOuvert.periode.debut est obligatoire",
          e.getLocalizedMessage());
    }

    validateContratCollectifTest();

    validateAssuresTest(periodesNull);

    validateIdentiteTest();

    validateDataTest();

    validateDestinatairePrestationsTest();

    validateDestinatairesRelevePrestationsTest();

    validateDroitsTest();

    validateRegimesParticuliersTest();
  }

  private void validateContratFieldsTest() {
    // idDeclarant obligatoire
    ContratAIV6 contrat = getContratV6(true);
    contrat.setIdDeclarant(null);
    ContractValidationBean contractValidationBean = new ContractValidationBean();
    try {
      service.validateContrat(contrat, contractValidationBean);
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC non renseignée : L'information idDeclarant est obligatoire",
          contractValidationBean.getErrorValidationBeans().getFirst().getError());
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC non renseignée : Impossible de déterminer une organisation secondaire pour l’organisation null et ayant le n° d’AMC 0000000ABC",
          contractValidationBean.getErrorValidationBeans().get(1).getError());
    }

    // societeEmettrice obligatoire
    contrat = getContratV6(true);
    contrat.setSocieteEmettrice(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : L'information societeEmettrice est obligatoire",
          e.getLocalizedMessage());
    }

    // numero obligatoire
    contrat = getContratV6(true);
    contrat.setNumero(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n° non renseigné de l'adhérent n°42 lié à l'AMC 0000000001 : L'information numero est obligatoire",
          e.getLocalizedMessage());
    }

    // numeroAdherent obligatoire
    contrat = getContratV6(true);
    contrat.setNumeroAdherent(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n° non renseigné lié à l'AMC 0000000001 : L'information numeroAdherent est obligatoire",
          e.getLocalizedMessage());
    }

    // dateSouscription obligatoire
    contrat = getContratV6(true);
    contrat.setDateSouscription(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : L'information dateSouscription est obligatoire",
          e.getLocalizedMessage());
    }

    // isContratIndividuel obligatoire
    contrat = getContratV6(true);
    contrat.setIsContratIndividuel(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : L'information isContratIndividuel est obligatoire",
          e.getLocalizedMessage());
    }

    // gestionnaire obligatoire
    contrat = getContratV6(true);
    contrat.setGestionnaire(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : L'information gestionnaire est obligatoire",
          e.getLocalizedMessage());
    }

    // qualification obligatoire
    contrat = getContratV6(true);
    contrat.setQualification(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : L'information qualification est obligatoire",
          e.getLocalizedMessage());
    }

    // ordrePriorisation obligatoire
    contrat = getContratV6(true);
    contrat.setOrdrePriorisation(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : L'information ordrePriorisation est obligatoire",
          e.getLocalizedMessage());
    }
  }

  private void validateRegimesParticuliersTest() {
    ContratAIV6 contrat;
    // Code d'un code/periode non renseigné
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().getRegimesParticuliers().getFirst().setCodeToNull();
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information regimesParticuliers.code est obligatoire",
          e.getLocalizedMessage());
    }

    // Periode d'un code/periode non renseigné
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().getRegimesParticuliers().getFirst().setPeriodeToNull();
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information regimesParticuliers.periode est obligatoire",
          e.getLocalizedMessage());
    }

    // Periode de début d'un code/periode non renseigné
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getRegimesParticuliers()
        .getFirst()
        .getPeriode()
        .setDebutToNull();
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information regimesParticuliers.periode.debut est obligatoire",
          e.getLocalizedMessage());
    }
  }

  private void validateAssuresTest(List<Periode> periodesNull) {
    ContratAIV6 contrat;
    // assures obligatoires
    contrat = getContratV6(true);
    List<Assure> assures = new ArrayList<>();
    contrat.setAssures(assures);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : L'information assures est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.isSouscripteur obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().setIsSouscripteur(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information isSouscripteur est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.rangAdministratif obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().setRangAdministratif(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information rangAdministratif est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.dateAdhesionMutuelle obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().setDateAdhesionMutuelle(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information dateAdhesionMutuelle est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.digitRelation obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().setDigitRelation(null);
    ContratAIV6 finalContrat = contrat;
    Assertions.assertThrows(
        ValidationContractException.class,
        () -> service.validateContrat(finalContrat, new ContractValidationBean()),
        "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information digitRelation est obligatoire");

    // assures.digitRelation.isTeletransmission obligatoire
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getDigitRelation()
        .getTeletransmissions()
        .getFirst()
        .setIsTeletransmission(null);
    ContratAIV6 finalContrat1 = contrat;
    Assertions.assertThrows(
        ValidationContractException.class,
        () -> service.validateContrat(finalContrat1, new ContractValidationBean()),
        "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information digitRelation.teletransmissions.isTeletransmission est obligatoire");

    // assures.digitRelation.periodeDebut obligatoire
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getDigitRelation()
        .getTeletransmissions()
        .getFirst()
        .setPeriode(new Periode());
    ContratAIV6 finalContrat3 = contrat;
    Assertions.assertThrows(
        ValidationContractException.class,
        () -> service.validateContrat(finalContrat3, new ContractValidationBean()),
        "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information digitRelation.teletransmissions.periode.debut est obligatoire");

    // assures.periodes obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().setPeriodes(periodesNull);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information periodes est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.periodes obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().getPeriodes().getFirst().setDebut(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information periodes.debut est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.periodesMedecinTraitantOuvert.debut obligatoire
    contrat = getContratV6(true);
    List<Periode> periodesMedecin = new ArrayList<>();
    periodesMedecin.add(new Periode(null, "2020-12-31"));
    contrat.getAssures().getFirst().setPeriodesMedecinTraitantOuvert(periodesMedecin);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information periodesMedecinTraitantOuvert.debut est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.qualite obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().setQualite(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information qualite est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.qualite.code obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().getQualite().setCode(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information qualite.code est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.qualite.libelle obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().getQualite().setLibelle(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information qualite.libelle est obligatoire",
          e.getLocalizedMessage());
    }
  }

  private void validateDataTest() {
    ContratAIV6 contrat;
    // assures.data obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().setData(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information data est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.data.nom obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().getData().setNom(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information data.nom est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.data.nom.nomFamille obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().getData().getNom().setNomFamille(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information data.nom.nomFamille est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.data.nom.prenom obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().getData().getNom().setPrenom(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information data.nom.prenom est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.data.nom.civilite obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().getData().getNom().setCivilite(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information data.nom.civilite est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.data.adresse obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().getData().setAdresse(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information data.adresse est obligatoire",
          e.getLocalizedMessage());
    }
  }

  private void validateIdentiteTest() {
    ContratAIV6 contrat;
    // assures.identite obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().setIdentite(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n° non renseigné : l'information identite est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.identite.dateNaissance obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().getIdentite().setDateNaissance(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information identite.dateNaissance est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.identite.rangNaissance obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().getIdentite().setRangNaissance(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information identite.rangNaissance est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.identite.numeroPersonne obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().getIdentite().setNumeroPersonne(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information identite.numeroPersonne est obligatoire",
          e.getLocalizedMessage());
    }

    // assures au moins un nir doit être fournit
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().getIdentite().setNir(null);
    contrat.getAssures().getFirst().getIdentite().setAffiliationsRO(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : doit posséder au moins un nir",
          e.getLocalizedMessage());
    }

    // assures.identite.nir.code obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().getIdentite().getNir().setCode(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information identite.nir.code est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.identite.nir.cle obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().getIdentite().getNir().setCle(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information identite.nir.cle est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.identite.affiliationsRO.nir obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().getIdentite().getAffiliationsRO().getFirst().setNir(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information identite.affiliationsRO.nir est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.identite.affiliationsRO.nir.code obligatoire
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getIdentite()
        .getAffiliationsRO()
        .getFirst()
        .getNir()
        .setCode(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information identite.affiliationsRO.nir.code est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.identite.affiliationsRO.nir.cle obligatoire
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getIdentite()
        .getAffiliationsRO()
        .getFirst()
        .getNir()
        .setCle(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information identite.affiliationsRO.nir.cle est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.identite.affiliationsRO.nir.cle incorrecte
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getIdentite()
        .getAffiliationsRO()
        .getFirst()
        .getNir()
        .setCle("124");
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : le NIR (1840197124511 / 124) de identite.affiliationsRO.nir n'est pas valide.",
          e.getLocalizedMessage());
    }

    // assures.identite.affiliationsRO.nir.cle non numérique
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getIdentite()
        .getAffiliationsRO()
        .getFirst()
        .getNir()
        .setCle("AB");
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : le NIR (1840197124511 / AB) de identite.affiliationsRO.nir n'est pas valide.",
          e.getLocalizedMessage());
    }

    // assures.identite.affiliationsRO.periode obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().getIdentite().getAffiliationsRO().getFirst().setPeriode(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information identite.affiliationsRO.periode est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.identite.affiliationsRO.periode.debut obligatoire
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getIdentite()
        .getAffiliationsRO()
        .getFirst()
        .getPeriode()
        .setDebut(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information identite.affiliationsRO.periode.debut est obligatoire",
          e.getLocalizedMessage());
    }
  }

  private void validateDroitsTest() {
    ContratAIV6 contrat;
    // assures.droits obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().setDroits(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information droits est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.droits.code obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().getDroits().getFirst().setCodeToNull();
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information droits.code est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.droits.codeAssureur obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().getDroits().getFirst().setCodeAssureurToNull();
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information droits.codeAssureur est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.droits.libelle obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().getDroits().getFirst().setLibelleToNull();
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information droits.libelle est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.droits.ordrePriorisation obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().getDroits().getFirst().setOrdrePriorisationToNull();
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information droits.ordrePriorisation est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.droits.type obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().getDroits().getFirst().setTypeToNull();
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information droits.type est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.droits.periode obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().getDroits().getFirst().setPeriodeToNull();
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information droits.periode est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.droits.periode.debut obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().getDroits().getFirst().getPeriode().setDebutToNull();
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information droits.periode.debut est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.droits.dateAncienneteGarantie obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().getDroits().getFirst().setDateAncienneteGarantieToNull();
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information droits.dateAncienneteGarantie est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.droits.carences.code obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().getDroits().getFirst().getCarences().getFirst().setCodeToNull();
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information droits.carences.code est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.droits.carence.periode obligatoire
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getDroits()
        .getFirst()
        .getCarences()
        .getFirst()
        .setPeriodeToNull();
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information droits.carences.periode est obligatoire",
          e.getLocalizedMessage());
    }
  }

  private void validateDestinatairesRelevePrestationsTest() {
    ContratAIV6 contrat;
    // assures.data.destinatairesRelevePrestations.idDestinatairePrestations
    // obligatoire
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .setIdDestinataireRelevePrestations(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information data.destinatairesRelevePrestations.idDestinataireRelevePrestations est obligatoire",
          e.getLocalizedMessage());
    }
    validateDestinatairesPrestationNomTest();

    // assures.data.destinatairesRelevePrestations.periode obligatoire
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .setPeriode(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information data.destinatairesRelevePrestations.periode est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.data.destinatairesRelevePrestations.periode.debut obligatoire
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getPeriode()
        .setDebut(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information data.destinatairesRelevePrestations.periode.debut est obligatoire",
          e.getLocalizedMessage());
    }
  }

  private void validateDestinatairePrestationsTest() {
    ContratAIV6 contrat;
    // assures.data.destinatairesPrestations.nom obligatoire
    contrat = getContratV6(true);
    contrat.getAssures().getFirst().getData().getDestinatairesPaiements().getFirst().setNom(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information data.destinatairesPaiements.nom est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.data.destinatairesPrestations.idDestinatairePrestations obligatoire
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .setIdDestinatairePaiements(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information data.destinatairesPaiements.idDestinatairePaiements est obligatoire",
          e.getLocalizedMessage());
    }

    validateDestinatairesPrestationsNom();

    // assures.data.destinatairesPrestations.rib.iban obligatoire
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getRib()
        .setIban(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information data.destinatairesPaiements.rib.iban est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.data.destinatairesPrestations.rib.bic obligatoire
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getRib()
        .setBic(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information data.destinatairesPaiements.rib.bic est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.data.destinatairesPrestations.modePaiementPrestations
    // obligatoire
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .setModePaiementPrestations(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information data.destinatairesPaiements.modePaiementPrestations est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.data.destinatairesPrestations.modePaiementPrestations.code
    // obligatoire
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getModePaiementPrestations()
        .setCode(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information data.destinatairesPaiements.modePaiementPrestations.code est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.data.destinatairesPrestations.modePaiementPrestations.libelle
    // obligatoire
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getModePaiementPrestations()
        .setLibelle(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information data.destinatairesPaiements.modePaiementPrestations.libelle est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.data.destinatairesPrestations.modePaiementPrestations.codeMonnaie
    // obligatoire
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getModePaiementPrestations()
        .setCodeMonnaie(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information data.destinatairesPaiements.modePaiementPrestations.codeMonnaie est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.data.destinatairesPrestations.periode obligatoire
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .setPeriode(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information data.destinatairesPaiements.periode est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.data.destinatairesPrestations.periode.debut obligatoire
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getPeriode()
        .setDebut(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information data.destinatairesPaiements.periode.debut est obligatoire",
          e.getLocalizedMessage());
    }
  }

  private void validateDestinatairesPrestationNomTest() {
    ContratAIV6 contrat;
    // assures.data.destinatairesRelevePrestations.nom obligatoire
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .setNom(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : l'information data.destinatairesRelevePrestations.nom est obligatoire",
          e.getLocalizedMessage());
    }

    // assures.data.destinatairesRelevePrestations.nom : Personne morale ou
    // physique
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setNomFamille(null);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setNomUsage(null);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setPrenom(null);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setCivilite(null);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setRaisonSociale(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      assertEqualWithoutSpecificChar(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : Si le destinataire de prestation est une personne physique les données nomFamille, prenom et civilite doivent être renseignées. Si le destinataire de prestation est une personne morale, la raison sociale doit être renseignée.",
          e.getLocalizedMessage());
    }
    // assures.data.destinatairesRelevePrestations.nom : Personne physique
    // incomplete - NomFamille
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setNomFamilleToNull();
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setPrenom("Test");
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setCivilite("Test");
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setRaisonSocialeToNull();
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      assertEqualWithoutSpecificChar(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : Si le destinataire de prestation est une personne physique les données nomFamille, prenom et civilite doivent être renseignées.",
          e.getLocalizedMessage());
    }

    // assures.data.destinatairesRelevePrestations.nom : Personne physique
    // incomplete - Prenom
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setNomFamille("Test");
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setPrenomToNull();
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setCivilite("Test");
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setRaisonSocialeToNull();
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      assertEqualWithoutSpecificChar(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : Si le destinataire de prestation est une personne physique les données nomFamille, prenom et civilite doivent être renseignées.",
          e.getLocalizedMessage());
    }

    // assures.data.destinatairesRelevePrestations.nom : Personne physique
    // incomplete - Civilite
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setNomFamille("Test");
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setPrenom("Test");
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setCiviliteToNull();
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setRaisonSocialeToNull();
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      assertEqualWithoutSpecificChar(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : Si le destinataire de prestation est une personne physique les données nomFamille, prenom et civilite doivent être renseignées.",
          e.getLocalizedMessage());
    }

    // assures.data.destinatairesRelevePrestations.nom : Personne morale
    // avec info personne physique
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setNomFamille("Test");
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setNomUsageToNull();
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setPrenomToNull();
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setCiviliteToNull();
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setRaisonSociale("Test");
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      assertEqualWithoutSpecificChar(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : La raison sociale ne peut pas être renseignée si l'un des champs suivant l'est aussi : nomFamille, nomUsage, prenom, civilite",
          e.getLocalizedMessage());
    }

    // assures.data.destinatairesRelevePrestations.nom : Personne morale
    // avec info personne physique
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setRaisonSociale("Test");
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setNomFamilleToNull();
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setNomUsage("Test");
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setPrenomToNull();
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setCiviliteToNull();
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setRaisonSociale("Test");
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      assertEqualWithoutSpecificChar(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : La raison sociale ne peut pas être renseignée si l'un des champs suivant l'est aussi : nomFamille, nomUsage, prenom, civilite",
          e.getLocalizedMessage());
    }

    // assures.data.destinatairesRelevePrestations.nom : Personne morale
    // avec info personne physique
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setNomFamilleToNull();
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setNomUsageToNull();
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setPrenom("Test");
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setCiviliteToNull();
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setRaisonSociale("Test");

    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      assertEqualWithoutSpecificChar(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : La raison sociale ne peut pas être renseignée si l'un des champs suivant l'est aussi : nomFamille, nomUsage, prenom, civilite",
          e.getLocalizedMessage());
    }

    // assures.data.destinatairesRelevePrestations.nom : Personne morale
    // avec info personne physique
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setNomFamilleToNull();
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setNomUsageToNull();
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setPrenomToNull();
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setCivilite("MR");
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesRelevePrestations()
        .getFirst()
        .getNom()
        .setRaisonSociale("Test");
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      assertEqualWithoutSpecificChar(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : La raison sociale ne peut pas être renseignée si l'un des champs suivant l'est aussi : nomFamille, nomUsage, prenom, civilite",
          e.getLocalizedMessage());
    }
  }

  private void validateDestinatairesPrestationsNom() {
    ContratAIV6 contrat;
    // assures.data.destinatairesPrestations.nom : Personne morale ou
    // physique
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setNomFamille(null);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setNomUsage(null);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setPrenom(null);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setCivilite(null);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setRaisonSociale(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      assertEqualWithoutSpecificChar(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : Si le destinataire de prestation est une personne physique les données nomFamille, prenom et civilite doivent être renseignées. Si le destinataire de prestation est une personne morale, la raison sociale doit être renseignée.",
          e.getLocalizedMessage());
    }
    // assures.data.destinatairesPrestations.nom : Personne physique
    // incomplete - NomFamille
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setNomFamille(null);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setPrenom("Test");
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setCivilite("Test");
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setRaisonSocialeToNull();
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      assertEqualWithoutSpecificChar(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : Si le destinataire de prestation est une personne physique les données nomFamille, prenom et civilite doivent être renseignées.",
          e.getLocalizedMessage());
    }

    // assures.data.destinatairesPrestations.nom : Personne physique
    // incomplete - Prenom
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setNomFamille("Test");
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setCivilite("Test");
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setPrenomToNull();
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setRaisonSocialeToNull();
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      assertEqualWithoutSpecificChar(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : Si le destinataire de prestation est une personne physique les données nomFamille, prenom et civilite doivent être renseignées.",
          e.getLocalizedMessage());
    }

    // assures.data.destinatairesPrestations.nom : Personne physique
    // incomplete - Civilite
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setNomFamille("Test");
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setPrenom("Test");
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setCiviliteToNull();
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setRaisonSocialeToNull();
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      assertEqualWithoutSpecificChar(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : Si le destinataire de prestation est une personne physique les données nomFamille, prenom et civilite doivent être renseignées.",
          e.getLocalizedMessage());
    }

    // assures.data.destinatairesPrestations.nom : Personne morale avec info
    // personne physique
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setRaisonSociale("Test");
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setNomFamille("Test");
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setNomUsage(null);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setPrenom(null);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setCivilite(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      assertEqualWithoutSpecificChar(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : La raison sociale ne peut pas être renseignée si l'un des champs suivant l'est aussi : nomFamille, nomUsage, prenom, civilite",
          e.getLocalizedMessage());
    }

    // assures.data.destinatairesPrestations.nom : Personne morale avec info
    // personne physique
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setRaisonSociale("Test");
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setNomFamille(null);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setNomUsage("Test");
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setPrenom(null);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setCivilite(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      assertEqualWithoutSpecificChar(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : La raison sociale ne peut pas être renseignée si l'un des champs suivant l'est aussi : nomFamille, nomUsage, prenom, civilite",
          e.getLocalizedMessage());
    }

    // assures.data.destinatairesPrestations.nom : Personne morale avec info
    // personne physique
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setRaisonSociale("Test");
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setNomFamille(null);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setNomUsage(null);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setPrenom("Test");
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setCivilite(null);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      assertEqualWithoutSpecificChar(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : La raison sociale ne peut pas être renseignée si l'un des champs suivant l'est aussi : nomFamille, nomUsage, prenom, civilite",
          e.getLocalizedMessage());
    }

    // assures.data.destinatairesPrestations.nom : Personne morale avec info
    // personne physique
    contrat = getContratV6(true);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setRaisonSociale("Test");
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setNomFamille(null);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setNomUsage(null);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setPrenom(null);
    contrat
        .getAssures()
        .getFirst()
        .getData()
        .getDestinatairesPaiements()
        .getFirst()
        .getNom()
        .setCivilite("MR");
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      assertEqualWithoutSpecificChar(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 et l'assuré n°36 : La raison sociale ne peut pas être renseignée si l'un des champs suivant l'est aussi : nomFamille, nomUsage, prenom, civilite",
          e.getLocalizedMessage());
    }
  }

  private void validateContratCollectifTest() {
    ContratAIV6 contrat;
    // contratCollectif.numero obligatoire
    contrat = getContratV6(true);
    ContratCollectifV6 contratColl = new ContratCollectifV6();
    contrat.setContratCollectif(contratColl);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : L'information contratCollectif.numero est obligatoire\n"
              + "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : L'information contratCollectif.identifiantCollectivite est obligatoire\n"
              + "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : L'information contratCollectif.raisonSociale est obligatoire\n"
              + "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : L'information contratCollectif.groupePopulation est obligatoire",
          e.getLocalizedMessage());
    }

    contratColl.setNumero("111111111");
    contrat.setContratCollectif(contratColl);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : L'information contratCollectif.identifiantCollectivite est obligatoire\n"
              + "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : L'information contratCollectif.raisonSociale est obligatoire\n"
              + "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : L'information contratCollectif.groupePopulation est obligatoire",
          e.getLocalizedMessage());
    }

    contratColl.setIdentifiantCollectivite("111111111");
    contrat.setContratCollectif(contratColl);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : L'information contratCollectif.raisonSociale est obligatoire\n"
              + "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : L'information contratCollectif.groupePopulation est obligatoire",
          e.getLocalizedMessage());
    }

    contratColl.setRaisonSociale("111111111");
    contrat.setContratCollectif(contratColl);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : L'information contratCollectif.groupePopulation est obligatoire",
          e.getLocalizedMessage());
    }

    contratColl.setGroupePopulation("111111111");
    contrat.setContratCollectif(contratColl);
  }

  private Assure getAssureV5forContractV5(Boolean isTemoin) {
    Assure assure = new Assure();

    assure.setIsSouscripteur(true);
    assure.setRangAdministratif("A");

    IdentiteContrat id = new IdentiteContrat();
    id.setDateNaissance("19871224");

    id.setRangNaissance("1");
    id.setNumeroPersonne("36");

    Nir nir = new Nir();
    nir.setCode("1840197416357");
    nir.setCle("85");
    id.setNir(nir);

    List<NirRattachementRO> affiliationsRO = new ArrayList<>();
    NirRattachementRO nirRattRO = new NirRattachementRO();
    Nir nirRO = new Nir();
    nirRO.setCode("1840197124511");
    nirRO.setCle("58");
    Periode pNirRO = new Periode();
    pNirRO.setDebut("2020-01-01");
    pNirRO.setFin("2020-06-30");
    nirRattRO.setNir(nirRO);
    nirRattRO.setPeriode(pNirRO);
    affiliationsRO.add(nirRattRO);
    id.setAffiliationsRO(affiliationsRO);

    assure.setIdentite(id);

    assure.setDateAdhesionMutuelle("2020-04-04");

    DigitRelation digit = new DigitRelation();
    Teletransmission teletransmission = new Teletransmission();
    Periode pTeletrans = new Periode();
    pTeletrans.setDebut("2020-01-01");
    teletransmission.setPeriode(pTeletrans);
    teletransmission.setIsTeletransmission(true);
    List<Teletransmission> teletrans = new ArrayList<>();
    teletrans.add(teletransmission);
    digit.setTeletransmissions(teletrans);
    assure.setDigitRelation(digit);

    List<Periode> lp = new ArrayList<>();
    Periode p1 = new Periode();
    p1.setDebut("2020-01-01");
    p1.setFin("2020-06-30");
    Periode p2 = new Periode();
    p2.setDebut("2020-07-01");
    p2.setFin("2020-12-31");

    if (isTemoin) {
      lp.add(p2);
      lp.add(p1);
    } else {
      lp.add(p1);
      lp.add(p2);
    }

    assure.setPeriodes(lp);

    List<CodePeriode> lc = new ArrayList<>();
    CodePeriode c1 = new CodePeriode();
    c1.setCode("code1");
    p1 = new Periode();
    p1.setDebut("2020-01-01");
    p1.setFin("2020-06-30");
    c1.setPeriode(p1);
    CodePeriode c2 = new CodePeriode();
    c2.setCode("code2");
    p2 = new Periode();
    p2.setDebut("2020-07-01");
    p2.setFin("2020-12-31");
    c2.setPeriode(p2);
    lc.add(c2);
    lc.add(c1);
    assure.setRegimesParticuliers(lc);

    QualiteAssure qual = new QualiteAssure();
    qual.setCode("code");
    qual.setLibelle("libelle");
    assure.setQualite(qual);

    DroitAssure droit = new DroitAssure();
    droit.setCode("c");
    droit.setCodeAssureur("ca");
    droit.setDateAncienneteGarantie("2019-12-12");
    droit.setLibelle("l");
    droit.setOrdrePriorisation("1");

    Periode p = new Periode();
    p.setDebut("2020-02-02");
    p.setFin("2020-03-03");
    droit.setPeriode(p);
    droit.setType("t");

    List<CarenceDroit> carences = new ArrayList<>();
    CarenceDroit carence = new CarenceDroit();
    carence.setCode("TRM");
    PeriodeCarence periodeCarence = new PeriodeCarence();
    periodeCarence.setDebut("2020-02-02");
    periodeCarence.setFin("2020-03-03");
    carence.setPeriode(periodeCarence);
    DroitRemplacement droitRemplacement = new DroitRemplacement();
    droitRemplacement.setCode("REM");
    droitRemplacement.setCodeAssureur("RAS");
    droitRemplacement.setLibelle("Droit remplacé");
    carence.setDroitRemplacement(droitRemplacement);

    carences.add(carence);
    droit.setCarences(carences);

    List<DroitAssure> ld = new ArrayList<>();
    ld.add(droit);
    assure.setDroits(ld);

    return assure;
  }

  private Assure getAssureV5FroContractV6(Boolean isTemoin) {
    Assure assure = new Assure();

    assure.setIsSouscripteur(true);
    assure.setRangAdministratif("A");

    IdentiteContrat id = new IdentiteContrat();
    id.setDateNaissance("19871224");

    id.setRangNaissance("1");
    id.setNumeroPersonne("36");

    Nir nir = new Nir();
    nir.setCode("1840197416357");
    nir.setCle("85");
    id.setNir(nir);

    List<NirRattachementRO> affiliationsRO = new ArrayList<>();
    NirRattachementRO nirRattRO = new NirRattachementRO();
    Nir nirRO = new Nir();
    nirRO.setCode("1840197124511");
    nirRO.setCle("58");
    Periode pNirRO = new Periode();
    pNirRO.setDebut("2020-01-01");
    pNirRO.setFin("2020-06-30");
    nirRattRO.setNir(nirRO);
    nirRattRO.setPeriode(pNirRO);
    affiliationsRO.add(nirRattRO);
    id.setAffiliationsRO(affiliationsRO);

    assure.setIdentite(id);

    assure.setDateAdhesionMutuelle("2020-04-04");

    DigitRelation digit = new DigitRelation();
    Teletransmission teletransmission = new Teletransmission();
    Periode pTeletrans = new Periode();
    pTeletrans.setDebut("2020-01-01");
    teletransmission.setPeriode(pTeletrans);
    teletransmission.setIsTeletransmission(true);
    List<Teletransmission> teletrans = new ArrayList<>();
    teletrans.add(teletransmission);
    digit.setTeletransmissions(teletrans);
    assure.setDigitRelation(digit);

    List<Periode> lp = new ArrayList<>();
    Periode p1 = new Periode();
    p1.setDebut("2020-01-01");
    p1.setFin("2020-06-30");
    Periode p2 = new Periode();
    p2.setDebut("2020-07-01");
    p2.setFin("2020-12-31");

    if (isTemoin) {
      lp.add(p2);
      lp.add(p1);
    } else {
      lp.add(p1);
      lp.add(p2);
    }

    assure.setPeriodes(lp);

    List<CodePeriode> lc = new ArrayList<>();
    CodePeriode c1 = new CodePeriode();
    c1.setCode("code1");
    p1 = new Periode();
    p1.setDebut("2020-01-01");
    p1.setFin("2020-06-30");
    c1.setPeriode(p1);
    CodePeriode c2 = new CodePeriode();
    c2.setCode("code2");
    p2 = new Periode();
    p2.setDebut("2020-07-01");
    p2.setFin("2020-12-31");
    c2.setPeriode(p2);
    lc.add(c2);
    lc.add(c1);
    assure.setRegimesParticuliers(lc);

    QualiteAssure qual = new QualiteAssure();
    qual.setCode("code");
    qual.setLibelle("libelle");
    assure.setQualite(qual);

    DroitAssure droit = new DroitAssure();
    droit.setCode("c");
    droit.setCodeAssureur("ca");
    droit.setDateAncienneteGarantie("2019-12-12");
    droit.setLibelle("l");
    droit.setOrdrePriorisation("1");

    Periode p = new Periode();
    p.setDebut("2020-02-02");
    p.setFin("2020-03-03");
    droit.setPeriode(p);
    droit.setType("t");

    List<CarenceDroit> carences = new ArrayList<>();
    CarenceDroit carence = new CarenceDroit();
    carence.setCode("TRM");
    PeriodeCarence periodeCarence = new PeriodeCarence();
    periodeCarence.setDebut("2020-02-02");
    periodeCarence.setFin("2020-03-03");
    carence.setPeriode(periodeCarence);
    DroitRemplacement droitRemplacement = new DroitRemplacement();
    droitRemplacement.setCode("REM");
    droitRemplacement.setCodeAssureur("RAS");
    droitRemplacement.setLibelle("Droit remplacé");
    carence.setDroitRemplacement(droitRemplacement);

    carences.add(carence);
    droit.setCarences(carences);

    List<DroitAssure> ld = new ArrayList<>();
    ld.add(droit);
    assure.setDroits(ld);

    return assure;
  }

  private ContratAIV6 getContratV6ForTestV5Map(Boolean isTemoin) {
    Periode pTestErreur = new Periode();
    pTestErreur.setDebut("2020-02-02");
    pTestErreur.setFin("2020-03-03");

    ContratAIV6 contrat = new ContratAIV6();
    contrat.setIdDeclarant("0000000001");
    contrat.setSocieteEmettrice("ABC");
    contrat.setNumero("12");
    contrat.setNumeroAdherent("42");
    contrat.setIsContratIndividuel(true);
    contrat.setGestionnaire("MileSafe");
    contrat.setQualification("A");
    contrat.setOrdrePriorisation("1");

    Assure assure = getAssureV5FroContractV6(isTemoin);
    assure.getDigitRelation().getTeletransmissions().getFirst().getPeriode().setDebut("2020-01-01");
    assure.getDigitRelation().getTeletransmissions().getFirst().getPeriode().setFin(null);
    DataAssure data = TestingDataForValidationService.getDataAssureV5();
    assure.setData(data);

    List<Assure> la = new ArrayList<>();
    la.add(assure);
    contrat.setAssures(la);

    contrat.setDateSouscription("2020-01-15");
    contrat.setDateResiliation("2020-11-15");

    List<PeriodeContratCMUOuvert> lp = new ArrayList<>();
    PeriodeContratCMUOuvert pc = new PeriodeContratCMUOuvert();
    Periode p = new Periode();
    p.setDebut("2020-02-02");
    p.setFin("2020-03-03");
    pc.setPeriode(p);
    pc.setCode("CMU");
    lp.add(pc);

    List<Periode> lpTestErreur = new ArrayList<>();
    lpTestErreur.add(pTestErreur);
    contrat.setPeriodesContratResponsableOuvert(lpTestErreur);
    contrat.setPeriodesContratCMUOuvert(lp);

    ContratCollectifV6 contratCollectifV6 = new ContratCollectifV6();
    contratCollectifV6.setNumero("numContrat1");
    contratCollectifV6.setSiret(null);
    contratCollectifV6.setRaisonSociale(Constants.N_A);
    contratCollectifV6.setIdentifiantCollectivite(Constants.N_A);
    contratCollectifV6.setGroupePopulation(Constants.N_A);
    contrat.setContratCollectif(contratCollectifV6);

    return contrat;
  }

  private ContratAIV5 getContratV5(Boolean isTemoin) {
    Periode pTestErreur = new Periode();
    pTestErreur.setDebut("2020-02-02");
    pTestErreur.setFin("2020-03-03");

    ContratAIV5 contrat = new ContratAIV5();
    contrat.setIdDeclarant("0000000001");
    contrat.setSocieteEmettrice("ABC");
    contrat.setNumero("12");
    contrat.setNumeroAdherent("42");
    contrat.setIsContratIndividuel(true);
    contrat.setGestionnaire("MileSafe");
    contrat.setQualification("A");
    contrat.setOrdrePriorisation("1");

    Assure assure = getAssureV5forContractV5(isTemoin);
    DataAssure data = TestingDataForValidationService.getDataAssureV5ForContractV5();
    assure.setData(data);

    List<Assure> la = new ArrayList<>();
    la.add(assure);
    contrat.setAssures(la);

    contrat.setDateSouscription("2020-01-15");
    contrat.setDateResiliation("2020-11-15");

    List<PeriodeContratCMUOuvert> lp = new ArrayList<>();
    Periode p = new Periode();
    p.setDebut("2020-02-02");
    p.setFin("2020-03-03");
    PeriodeContratCMUOuvert periodeContratCMUOuvert = new PeriodeContratCMUOuvert();
    periodeContratCMUOuvert.setCode("CMU");
    periodeContratCMUOuvert.setPeriode(p);
    lp.add(periodeContratCMUOuvert);
    List<Periode> lpTestErreur = new ArrayList<>();
    lpTestErreur.add(pTestErreur);
    contrat.setPeriodesContratResponsableOuvert(lpTestErreur);
    contrat.setPeriodesContratCMUOuvert(lp);

    ContratCollectif contratCollectif = new ContratCollectif();
    contratCollectif.setNumero("numContrat1");
    contrat.setContratCollectif(contratCollectif);

    return contrat;
  }

  private ContratAIV6 getContratV6(Boolean isTemoin) {
    Periode pTestErreur = new Periode();
    pTestErreur.setDebut("2020-02-02");
    pTestErreur.setFin("2020-03-03");

    ContratAIV6 contrat = new ContratAIV6();
    contrat.setIdDeclarant("0000000001");
    contrat.setSocieteEmettrice("ABC");
    contrat.setNumero("12");
    contrat.setNumeroAdherent("42");
    contrat.setIsContratIndividuel(true);
    contrat.setGestionnaire("MileSafe");
    contrat.setQualification("A");
    contrat.setOrdrePriorisation("1");

    Assure assure = getAssureV5FroContractV6(isTemoin);
    DataAssure data = TestingDataForValidationService.getDataAssureV5();
    assure.setData(data);

    List<Assure> la = new ArrayList<>();
    la.add(assure);
    contrat.setAssures(la);

    contrat.setDateSouscription("2020-01-15");
    contrat.setDateResiliation("2020-11-15");

    List<PeriodeContratCMUOuvert> lp = new ArrayList<>();
    PeriodeContratCMUOuvert pc = new PeriodeContratCMUOuvert();
    Periode p = new Periode();
    p.setDebut("2020-02-02");
    p.setFin("2020-03-03");
    pc.setPeriode(p);
    pc.setCode("CMU");
    lp.add(pc);

    List<Periode> lpTestErreur = new ArrayList<>();
    lpTestErreur.add(pTestErreur);
    contrat.setPeriodesContratResponsableOuvert(lpTestErreur);
    contrat.setPeriodesContratCMUOuvert(lp);

    ContexteTPV6 cont = new ContexteTPV6();
    PeriodesDroitsCarte per2 = new PeriodesDroitsCarte();
    per2.setDebut("2020-02-02");
    per2.setFin("2020-03-03");
    cont.setPeriodesDroitsCarte(per2);
    contrat.setContexteTiersPayant(cont);

    return contrat;
  }

  private void assertEqualWithoutSpecificChar(String expected, String actual) {
    String expected2 = expected.replaceAll("[^A-Za-z]+", "");
    String actual2 = actual.replaceAll("[^A-Za-z]+", "");
    Assertions.assertEquals(expected2, actual2);
  }

  @Test
  void should_map_contratV5_to_V6() {
    ContratAIV5 contratV5 = getContratV5(true);
    ContratAIV6 contratV6 = getContratV6ForTestV5Map(true);
    ContratAIV6 contratV6Map = MapperContrat.mapV5toV6(contratV5);

    Assertions.assertEquals(contratV6, contratV6Map);
  }

  @Test
  void should_have_onlyOnePrincipalInsured() {
    List<AssureCommun> assureCommunList = new ArrayList<>();

    Assure assure1 = new Assure();
    QualiteAssure qualiteAssure = new QualiteAssure();
    qualiteAssure.setCode("A");
    QualiteAssure qualiteAssure2 = new QualiteAssure();
    qualiteAssure2.setCode("B");
    QualiteAssure qualiteAssure3 = new QualiteAssure();
    qualiteAssure3.setCode("C");
    Assure assure2 = new Assure();
    Assure assure3 = new Assure();
    assure1.setQualite(qualiteAssure);
    assure2.setQualite(qualiteAssure2);
    assure3.setQualite(qualiteAssure3);
    assureCommunList.add(assure1);
    assureCommunList.add(assure2);
    assureCommunList.add(assure3);
    ContractValidationBean contractValidationBean = new ContractValidationBean();
    service.onlyOnePrincipalInsured(assureCommunList, contractValidationBean);
    Assertions.assertTrue(
        CollectionUtils.isEmpty(contractValidationBean.getErrorValidationBeans()));

    List<AssureCommun> assureCommunList2 = new ArrayList<>();

    Assure assure4 = new Assure();
    Assure assure5 = new Assure();
    Assure assure6 = new Assure();
    QualiteAssure qualiteAssure4 = new QualiteAssure();
    qualiteAssure4.setCode("A");
    QualiteAssure qualiteAssure5 = new QualiteAssure();
    qualiteAssure5.setCode("B");
    QualiteAssure qualiteAssure6 = new QualiteAssure();
    qualiteAssure6.setCode("A");
    assure4.setQualite(qualiteAssure4);
    assure5.setQualite(qualiteAssure5);
    assure6.setQualite(qualiteAssure6);

    assureCommunList2.add(assure4);
    assureCommunList2.add(assure5);
    assureCommunList2.add(assure6);
    contractValidationBean = new ContractValidationBean();
    service.onlyOnePrincipalInsured(assureCommunList2, contractValidationBean);
    String actualError = contractValidationBean.getErrorValidationBeans().getFirst().getError();
    String expectedError =
        "Pour le contrat n° non renseigné de l'adhérent n° non renseigné lié à l'AMC non renseignée : Il ne peut y avoir qu'un seul assuré principal sur le contrat.";
    Assertions.assertEquals(expectedError, actualError);
  }

  @Test
  void should_have_onlyOneSubscriber() {
    List<AssureCommun> assureCommunList = new ArrayList<>();

    Assure assure1 = new Assure();
    assure1.setIsSouscripteur(true);
    Assure assure2 = new Assure();
    assure2.setIsSouscripteur(false);

    assureCommunList.add(assure1);
    assureCommunList.add(assure2);
    ContractValidationBean contractValidationBean = new ContractValidationBean();
    service.onlyOneSubscriber(assureCommunList, contractValidationBean);
    Assertions.assertTrue(
        CollectionUtils.isEmpty(contractValidationBean.getErrorValidationBeans()));

    List<AssureCommun> assureCommunList2 = new ArrayList<>();

    Assure assure3 = new Assure();
    assure3.setIsSouscripteur(true);
    Assure assure4 = new Assure();
    assure4.setIsSouscripteur(true);

    assureCommunList2.add(assure3);
    assureCommunList2.add(assure4);
    contractValidationBean = new ContractValidationBean();
    service.onlyOneSubscriber(assureCommunList2, contractValidationBean);
    String actualError = contractValidationBean.getErrorValidationBeans().getFirst().getError();
    String expectedError =
        "Pour le contrat n° non renseigné de l'adhérent n° non renseigné lié à l'AMC non renseignée : Il ne peut y avoir qu'un seul souscripteur sur le contrat.";
    Assertions.assertEquals(expectedError, actualError);
  }

  @Test
  void administrativeRank_should_be_unique() {
    List<AssureCommun> assureCommunList = new ArrayList<>();

    Assure assure1 = new Assure();
    assure1.setRangAdministratif("1");
    Assure assure2 = new Assure();
    assure2.setRangAdministratif("2");

    assureCommunList.add(assure1);
    assureCommunList.add(assure2);
    ContractValidationBean contractValidationBean = new ContractValidationBean();
    service.administrativeRankUnique(assureCommunList, contractValidationBean);
    Assertions.assertTrue(
        CollectionUtils.isEmpty(contractValidationBean.getErrorValidationBeans()));

    List<AssureCommun> assureCommunList2 = new ArrayList<>();

    Assure assure3 = new Assure();
    assure3.setRangAdministratif("1");
    Assure assure4 = new Assure();
    assure4.setRangAdministratif("1");

    assureCommunList2.add(assure3);
    assureCommunList2.add(assure4);
    contractValidationBean = new ContractValidationBean();
    service.administrativeRankUnique(assureCommunList2, contractValidationBean);
    String actualError = contractValidationBean.getErrorValidationBeans().getFirst().getError();
    String expectedError =
        "Pour le contrat n° non renseigné de l'adhérent n° non renseigné lié à l'AMC non renseignée : Le rang administratif doit être unique pour chaque assuré.";
    Assertions.assertEquals(expectedError, actualError);
  }

  @Test
  void guarantee_periods_for_same_guarantee_and_insured() {

    List<AssureCommun> assureCommunList = new ArrayList<>();
    Assure assure = new Assure();
    IdentiteContrat identite = new IdentiteContrat();
    identite.setDateNaissance("19791006");
    identite.setNumeroPersonne("123");
    identite.setRangNaissance("1");
    assure.setIdentite(identite);
    List<DroitAssure> droitAssureList = new ArrayList<>();
    DroitAssure droit = new DroitAssure();
    Periode periode = new Periode();
    periode.setDebut("2021-07-12");
    periode.setFin("2021-08-30");
    droit.setPeriode(periode);
    droit.setCode("CODE");
    droit.setCodeAssureur("CODEASSUREUR");
    droitAssureList.add(droit);
    DroitAssure droit1 = new DroitAssure();
    Periode periode1 = new Periode();
    periode1.setDebut("2021-08-30");
    periode1.setFin("2021-09-30");
    droit1.setPeriode(periode1);
    droit1.setCode("DIFFERENTCODE");
    droit1.setCodeAssureur("DIFFERENTCODEASSUREUR");
    droitAssureList.add(droit1);
    assure.setDroits(droitAssureList);
    assureCommunList.add(assure);
    ContractValidationBean contractValidationBean = new ContractValidationBean();
    service.noOverlappingGuaranteePeriod(assureCommunList, contractValidationBean);
    Assertions.assertTrue(
        CollectionUtils.isEmpty(contractValidationBean.getErrorValidationBeans()));

    DroitAssure droit4 = new DroitAssure();
    Periode periode4 = new Periode();
    periode4.setDebut("2021-07-10");
    periode4.setFin("2021-08-30");
    droit4.setPeriode(periode4);
    droit4.setCode("CODE");
    droit4.setCodeAssureur("CODEASSUREUR");
    droitAssureList.add(droit4);
    contractValidationBean = new ContractValidationBean();
    service.noOverlappingGuaranteePeriod(assureCommunList, contractValidationBean);
    String actualError = contractValidationBean.getErrorValidationBeans().getFirst().getError();
    String expectedError =
        "Pour le contrat n° non renseigné de l'adhérent n° non renseigné lié à l'AMC non renseignée et l'assuré n°123 : Les périodes de garantie pour une même garantie et un même assuré ne doivent pas se chevaucher.";
    Assertions.assertEquals(expectedError, actualError);

    List<AssureCommun> assureCommunList2 = new ArrayList<>();
    Assure assure2 = new Assure();
    List<DroitAssure> droitAssureList2 = new ArrayList<>();
    DroitAssure droit2 = new DroitAssure();
    Periode periode2 = new Periode();
    periode2.setDebut("2021-08-30");
    droit2.setPeriode(periode2);
    droit2.setCode("CODE");
    droit2.setCodeAssureur("CODEASSUREUR");
    droitAssureList2.add(droit2);
    droitAssureList2.add(droit2);
    assure2.setDroits(droitAssureList2);
    assureCommunList2.add(assure2);
    service.noOverlappingGuaranteePeriod(assureCommunList2, new ContractValidationBean());
    Assertions.assertEquals(expectedError, actualError);
  }

  @Test
  void overlapping_guarantees_with_invalid_period_should_not_fail() {

    List<AssureCommun> assureCommunList = new ArrayList<>();
    Assure assure = new Assure();
    List<DroitAssure> droitAssureList = new ArrayList<>();
    DroitAssure droit = new DroitAssure();
    Periode periode = new Periode();
    periode.setDebut("2023-01-01");
    periode.setFin("2022-12-31");
    droit.setPeriode(periode);
    droit.setCode("CODE");
    droit.setCodeAssureur("CODEASSUREUR");
    droitAssureList.add(droit);
    DroitAssure droit1 = new DroitAssure();
    Periode periode1 = new Periode();
    periode1.setDebut("2023-01-01");
    droit1.setPeriode(periode1);
    droit1.setCode("CODE");
    droit1.setCodeAssureur("CODEASSUREUR");
    droitAssureList.add(droit1);
    assure.setDroits(droitAssureList);
    assureCommunList.add(assure);
    ContractValidationBean contractValidationBean = new ContractValidationBean();
    service.noOverlappingGuaranteePeriod(assureCommunList, contractValidationBean);
    Assertions.assertTrue(
        CollectionUtils.isEmpty(contractValidationBean.getErrorValidationBeans()));
  }

  @Test
  void overlapping_guarantees_for_same_person_number_should_fail() {
    List<AssureCommun> assureCommunList = new ArrayList<>();
    Assure assure = new Assure();
    Assure assure2 = new Assure();
    IdentiteContrat identite = new IdentiteContrat();
    List<DroitAssure> droitAssureList = new ArrayList<>();
    DroitAssure droit = new DroitAssure();
    Periode periode = new Periode();
    periode.setDebut("2025-01-01");
    droit.setPeriode(periode);
    droit.setCode("CODE");
    droit.setCodeAssureur("CODEASSUREUR");
    droitAssureList.add(droit);
    identite.setNumeroPersonne("123");
    assure.setDroits(droitAssureList);
    assure.setIdentite(identite);
    assure2.setDroits(droitAssureList);
    assure2.setIdentite(identite);
    assureCommunList.add(assure);
    assureCommunList.add(assure2);
    ContractValidationBean contractValidationBean = new ContractValidationBean();
    service.noOverlappingGuaranteePeriodForSamePersonNumber(
        assureCommunList, contractValidationBean);
    String actualError = contractValidationBean.getErrorValidationBeans().getFirst().getError();
    String expectedError =
        "Pour le contrat n° non renseigné de l'adhérent n° non renseigné lié à l'AMC non renseignée et l'assuré n°123 : Les périodes de garantie pour une même garantie et un même assuré ne doivent pas se chevaucher.";
    Assertions.assertEquals(expectedError, actualError);
  }

  @Test
  void not_overlapping_guarantees_for_same_person_number_should_not_fail() {
    List<AssureCommun> assureCommunList = new ArrayList<>();
    Assure assure = new Assure();
    Assure assure2 = new Assure();
    IdentiteContrat identite = new IdentiteContrat();
    List<DroitAssure> droitAssureList = new ArrayList<>();
    DroitAssure droit = new DroitAssure();
    Periode periode = new Periode();
    periode.setDebut("2025-01-01");
    periode.setFin("2025-06-03");
    droit.setPeriode(periode);
    droit.setCode("CODE");
    droit.setCodeAssureur("CODEASSUREUR");
    droitAssureList.add(droit);
    identite.setNumeroPersonne("123");
    assure.setDroits(droitAssureList);
    assure.setIdentite(identite);
    DroitAssure droit1 = new DroitAssure();
    Periode periode1 = new Periode();
    periode1.setDebut("2025-06-04");
    droit1.setPeriode(periode1);
    droit1.setCode("CODE");
    droit1.setCodeAssureur("CODEASSUREUR");
    assure2.setDroits(List.of(droit1));
    assure2.setIdentite(identite);
    assureCommunList.add(assure);
    assureCommunList.add(assure2);
    ContractValidationBean contractValidationBean = new ContractValidationBean();
    service.noOverlappingGuaranteePeriodForSamePersonNumber(
        assureCommunList, contractValidationBean);
    Assertions.assertTrue(
        CollectionUtils.isEmpty(contractValidationBean.getErrorValidationBeans()));
  }

  @Test
  void should_validate_contratV5_with_guarantee_and_carence_with_invalid_periods() {
    ContratAIV5 contrat = getContratV5(true);
    service.setControleCorrespondanceBobb(true);
    List<DroitAssure> droitAssureList = new ArrayList<>();
    DroitAssure droit = new DroitAssure();
    Periode periode = new Periode();
    periode.setDebut("2023-01-01");
    periode.setFin("2022-12-31");
    droit.setPeriode(periode);
    droit.setCode("CODE");
    droit.setCodeAssureur("CODEASSUREUR");
    droit.setOrdrePriorisation("1");
    droit.setType("B");
    droit.setDateAncienneteGarantie("2023-01-01");
    droit.setLibelle("LIBELLE");

    List<CarenceDroit> carences = new ArrayList<>();
    CarenceDroit carence = new CarenceDroit();
    carence.setCode("TRM");
    PeriodeCarence periodeCarence = new PeriodeCarence();
    periodeCarence.setDebut("2023-03-01");
    periodeCarence.setFin("2023-01-01");
    carence.setPeriode(periodeCarence);
    DroitRemplacement droitRemplacement = new DroitRemplacement();
    droitRemplacement.setCode("REM");
    droitRemplacement.setCodeAssureur("RAS");
    droitRemplacement.setLibelle("Droit remplacé");
    carence.setDroitRemplacement(droitRemplacement);
    carences.add(carence);
    droit.setCarences(carences);
    droitAssureList.add(droit);
    contrat.getAssures().getFirst().setDroits(droitAssureList);
    droitAssureList.add(droit);
    contrat.getAssures().getFirst().setDroits(droitAssureList);
    try {
      service.validateContrat(contrat, new ContractValidationBean());
    } catch (ValidationContractException e) {
      logger.info("ve: {}", e.getMessage());
      Assertions.fail();
    }
    Assertions.assertTrue(true);
    service.setControleCorrespondanceBobb(false);
  }

  @SneakyThrows
  @Test
  void should_validate_call_to_orga() {
    ContratAIV6 contrat = getContratV6(true);
    ContractValidationBean contractValidationBean = new ContractValidationBean();
    service.callOrga(contrat, contractValidationBean);
    Assertions.assertTrue(
        CollectionUtils.isEmpty(contractValidationBean.getErrorValidationBeans()));
  }

  @SneakyThrows
  @Test
  void should_fail_call_to_orga() {
    ContratAIV6 contrat = getContratV6(true);

    OrganisationServiceWrapper organisationServiceMock =
        Mockito.mock(OrganisationServiceWrapper.class);
    Mockito.when(organisationServiceMock.getOrganizationByAmcNumber(Mockito.anyString()))
        .thenReturn(null);
    ReflectionTestUtils.setField(service, "organisationServiceWrapper", organisationServiceMock);
    ContractValidationBean contractValidationBean = new ContractValidationBean();
    service.callOrga(contrat, contractValidationBean);
    Assertions.assertEquals(
        "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : Impossible de déterminer l'organisation liée à l'AMC n°"
            + contrat.getIdDeclarant(),
        contractValidationBean.getErrorValidationBeans().getFirst().getError());
  }

  @SneakyThrows
  @Test
  void should_validate_call_to_orga_secondary() {
    ContratAIV6 contrat = getContratV6(true);

    OrganisationServiceWrapper organisationServiceMock =
        Mockito.mock(OrganisationServiceWrapper.class);
    Organisation org = new Organisation();
    org.setMain(true);
    org.setCode(contrat.getIdDeclarant());
    org.setFullName("s3OrgaFullName");
    org.setCommercialName("s3OrgaCommercialName");
    Mockito.when(organisationServiceMock.getOrganizationByAmcNumber(Mockito.anyString()))
        .thenReturn(org);
    Mockito.when(organisationServiceMock.isOrgaAttached(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(false);
    ReflectionTestUtils.setField(service, "organisationServiceWrapper", organisationServiceMock);
    ContractValidationBean contractValidationBean = new ContractValidationBean();
    service.callOrga(contrat, contractValidationBean);
    Assertions.assertEquals(
        "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : Impossible de déterminer une organisation secondaire pour l’organisation "
            + contrat.getIdDeclarant()
            + " et ayant le n° d’AMC "
            + contrat.getSocieteEmettrice(),
        contractValidationBean.getErrorValidationBeans().getFirst().getError());
  }

  @SneakyThrows
  @Test
  void should_not_validate_call_to_orga() {
    ContratAIV6 contrat = getContratV6(true);

    OrganisationServiceWrapper organisationServiceMock =
        Mockito.mock(OrganisationServiceWrapper.class);
    Organisation org = new Organisation();
    org.setMain(false);
    org.setCode(contrat.getSocieteEmettrice());
    org.setFullName("s3OrgaFullName");
    org.setCommercialName("s3OrgaCommercialName");
    Mockito.when(organisationServiceMock.getOrganizationByAmcNumber(Mockito.anyString()))
        .thenReturn(org);
    ReflectionTestUtils.setField(service, "organisationServiceWrapper", organisationServiceMock);
    ContractValidationBean contractValidationBean = new ContractValidationBean();
    service.callOrga(contrat, contractValidationBean);
    Assertions.assertEquals(
        "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : Incohérence entre l’idDeclarant "
            + contrat.getIdDeclarant()
            + " et la societeEmettrice "
            + contrat.getSocieteEmettrice(),
        contractValidationBean.getErrorValidationBeans().getFirst().getError());
  }

  @Test
  void should_validate_suspension_non_paiement_v6() {
    ContratAIV6 contratAIV6 = getContratV6(false);

    String motif = Constants.NON_PAIEMENT_COTISATIONS;
    String type = TypeSuspension.Provisoire.name();
    PeriodeSuspension periodeSuspension = createListPeriodeSuspensions(motif, type);
    contratAIV6.setPeriodesSuspension(List.of(periodeSuspension));
    service.setControleCorrespondanceBobb(false);
    service.validateContrat(contratAIV6, new ContractValidationBean());

    Assertions.assertEquals(motif, periodeSuspension.getMotifSuspension());
  }

  @Test
  void should_validate_suspension_portabilite_non_justifiee_v6() {
    ContratAIV6 contratAIV6 = getContratV6(false);
    String motif = Constants.PORTABILITE_NON_JUSTIFIEE;
    String type = TypeSuspension.Provisoire.name();
    PeriodeSuspension periodeSuspension = createListPeriodeSuspensions(motif, type);
    contratAIV6.setPeriodesSuspension(List.of(periodeSuspension));

    service.validateContrat(contratAIV6, new ContractValidationBean());

    Assertions.assertEquals(motif, periodeSuspension.getMotifSuspension());
  }

  @Test
  void should_throw_portabilite_non_justifie_definitif_v6() {
    ContratAIV6 contratAIV6 = getContratV6(false);

    String motif = Constants.PORTABILITE_NON_JUSTIFIEE;
    String type = TypeSuspension.Definitif.name();
    PeriodeSuspension periodeSuspension = createListPeriodeSuspensions(motif, type);
    contratAIV6.setPeriodesSuspension(List.of(periodeSuspension));

    Assertions.assertThrows(
        ValidationContractException.class,
        () -> service.validateContrat(contratAIV6, new ContractValidationBean()));
  }

  @Test
  void should_replace_by_non_paiement_v6() {
    ContratAIV6 contratAIV6 = getContratV6(false);

    String motif = "NIMP";
    String type = TypeSuspension.Definitif.name();
    PeriodeSuspension periodeSuspension = createListPeriodeSuspensions(motif, type);
    contratAIV6.setPeriodesSuspension(List.of(periodeSuspension));

    service.validateContrat(contratAIV6, new ContractValidationBean());

    Assertions.assertEquals(
        Constants.NON_PAIEMENT_COTISATIONS, periodeSuspension.getMotifSuspension());
  }

  @Test
  void should_replace_by_non_paiement_v5() {
    ContratAIV5 contratAIV5 = getContratV5(false);

    String motif = "NIMP";
    String type = TypeSuspension.Definitif.name();
    PeriodeSuspension periodeSuspension = createListPeriodeSuspensions(motif, type);
    contratAIV5.setPeriodesSuspension(List.of(periodeSuspension));

    service.validateContrat(contratAIV5, new ContractValidationBean());

    Assertions.assertEquals(
        Constants.NON_PAIEMENT_COTISATIONS, periodeSuspension.getMotifSuspension());
  }

  @Test
  void should_replace_by_non_paiement_portabilite_definitif_v5() {
    ContratAIV5 contratAIV5 = getContratV5(false);

    String motif = Constants.PORTABILITE_NON_JUSTIFIEE;
    String type = TypeSuspension.Definitif.name();
    PeriodeSuspension periodeSuspension = createListPeriodeSuspensions(motif, type);
    contratAIV5.setPeriodesSuspension(List.of(periodeSuspension));

    service.validateContrat(contratAIV5, new ContractValidationBean());

    Assertions.assertEquals(
        Constants.NON_PAIEMENT_COTISATIONS, periodeSuspension.getMotifSuspension());
  }

  @Test
  void should_validate_suspension_non_paiement_v5() {
    ContratAIV5 contratAIV5 = getContratV5(false);

    String motif = Constants.NON_PAIEMENT_COTISATIONS;
    String type = TypeSuspension.Provisoire.name();
    PeriodeSuspension periodeSuspension = createListPeriodeSuspensions(motif, type);
    contratAIV5.setPeriodesSuspension(List.of(periodeSuspension));
    service.setControleCorrespondanceBobb(false);
    service.validateContrat(contratAIV5, new ContractValidationBean());

    Assertions.assertEquals(motif, periodeSuspension.getMotifSuspension());
  }

  @Test
  void should_validate_suspension_portabilite_non_justifiee_v5() {
    ContratAIV5 contratAIV5 = getContratV5(false);
    String motif = Constants.PORTABILITE_NON_JUSTIFIEE;
    String type = TypeSuspension.Provisoire.name();
    PeriodeSuspension periodeSuspension = createListPeriodeSuspensions(motif, type);
    contratAIV5.setPeriodesSuspension(List.of(periodeSuspension));

    service.validateContrat(contratAIV5, new ContractValidationBean());

    Assertions.assertEquals(motif, periodeSuspension.getMotifSuspension());
  }

  @Test
  void should_validate_suspension_not_in_referential_v5() {
    ContratAIV5 contratAIV5 = getContratV5(false);
    String motif = "Blabla";
    String type = TypeSuspension.Provisoire.name();
    PeriodeSuspension periodeSuspension = createListPeriodeSuspensions(motif, type);
    contratAIV5.setPeriodesSuspension(List.of(periodeSuspension));

    Referential referential = new Referential();
    referential.setCode("PeriodesSuspension.MotifSuspension");
    referential.setAuthorizedValues(
        List.of(Constants.PORTABILITE_NON_JUSTIFIEE, Constants.NON_PAIEMENT_COTISATIONS));
    Mockito.doReturn(List.of(referential))
        .when(referentialService)
        .getReferential(Mockito.anyString());
    service.validateContrat(contratAIV5, new ContractValidationBean());

    Assertions.assertEquals(
        Constants.NON_PAIEMENT_COTISATIONS, periodeSuspension.getMotifSuspension());
  }

  @Test
  void should_throw_suspension_not_in_referential_v6() {
    ContratAIV6 contratAIV6 = getContratV6(false);
    String motif = "Blabla";
    String type = TypeSuspension.Provisoire.name();
    PeriodeSuspension periodeSuspension = createListPeriodeSuspensions(motif, type);
    contratAIV6.setPeriodesSuspension(List.of(periodeSuspension));
    service.setUseReferentialValidation(true);
    Referential referential = new Referential();
    referential.setCode("PeriodesSuspension.MotifSuspension");
    referential.setAuthorizedValues(
        List.of(Constants.PORTABILITE_NON_JUSTIFIEE, Constants.NON_PAIEMENT_COTISATIONS));
    ContractValidationBean contractValidationBean = new ContractValidationBean();
    Mockito.doReturn(List.of(referential))
        .when(referentialService)
        .getReferential(Mockito.anyString());
    Assertions.assertThrows(
        ValidationContractException.class,
        () -> service.validateContrat(contratAIV6, contractValidationBean));
    Assertions.assertEquals(
        "Pour le contrat n°12 de l'adhérent n°42 lié à l'AMC 0000000001 : L'information PeriodesSuspension.MotifSuspension n'a pas une valeur comprise dans la liste : [PORTABILITE_NON_JUSTIFIEE, NON_PAIEMENT_COTISATIONS]",
        contractValidationBean.getErrorValidationBeans().getFirst().getError());
    service.setUseReferentialValidation(false);
  }

  private PeriodeSuspension createListPeriodeSuspensions(String motif, String type) {
    PeriodeSuspension periodeSuspension = new PeriodeSuspension();
    periodeSuspension.setMotifSuspension(motif);
    periodeSuspension.setPeriode(new Periode("2000-01-01", "2000-12-31"));
    periodeSuspension.setTypeSuspension(type);

    return periodeSuspension;
  }
}
