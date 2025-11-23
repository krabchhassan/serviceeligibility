package com.cegedim.next.serviceeligibility.core.services.pau;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.*;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.services.GenerateContract;
import com.cegedim.next.serviceeligibility.core.services.pojo.ContractAdherentPeriode;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.ContextConstants;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.UAPFunctionalException;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.tuple.Triple;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class UniqueAccessPointServiceTpOnlineV5Test extends UniqueAccessPointUtilsTesting {

  @Autowired private UniqueAccessPointServiceV5TPOnlineTPImpl uapV5Service;

  @Autowired private IssuingCompanyCodeService issuingCompanyCodeService;

  @Autowired private GenerateContract contract;

  @SpyBean private BeyondPropertiesService beyondPropertiesService;

  @Test
  void testExecuteTPOnlineTDB() throws IOException { // NOSONAR
    final String startDate = "2022-01-01";
    final String endDate = "2022-08-01";
    final UniqueAccessPointRequestV5 requete =
        new UniqueAccessPointRequestV5(
            "1791059632524",
            "19800605",
            "1",
            startDate,
            endDate,
            "123456",
            null,
            null,
            ContextConstants.TP_ONLINE,
            null,
            null,
            null,
            null,
            false);

    final ContractTP contractTP1 = this.contract.getContrat(startDate, endDate);
    contractTP1
        .getBeneficiaires()
        .forEach(
            beneficiaireContract ->
                beneficiaireContract.getDomaineDroits().stream()
                    .flatMap(domaineDroitContract -> domaineDroitContract.getGaranties().stream())
                    .flatMap(garantie -> garantie.getProduits().stream())
                    .forEach(produit -> produit.setCodeOffre(null)));
    final AggregationResults<ContractTP> res =
        new AggregationResults<>(List.of(contractTP1), new Document());

    this.mockListBenefWithContract(res);

    this.mockOc();

    mockOfferStructurePW(
        "src/test/resources/offerStructure.json",
        requete.getStartDate(),
        requete.getEndDate(),
        ContextConstants.TP_ONLINE,
        "KC_PlatineComp");
    mockOfferStructurePW(
        "src/test/resources/offerStructure_KC_PlatineBase.json",
        requete.getStartDate(),
        requete.getEndDate(),
        ContextConstants.TP_ONLINE,
        "KC_PlatineBase");

    Mockito.doReturn("http://next-engine-core-api:8080")
        .when(beyondPropertiesService)
        .getPropertyOrThrowError(PW_API_URL);
    Mockito.doReturn("/v4/offerStructure")
        .when(beyondPropertiesService)
        .getPropertyOrThrowError(PW_ENDPOINT_OFFERSTRUCTURE);
    Mockito.doReturn("INSURER").when(beyondPropertiesService).getPropertyOrThrowError(CLIENT_TYPE);

    final UniqueAccessPointResponse response = this.uapV5Service.execute(requete);
    Assertions.assertEquals(1, response.getContracts().size());
    final GenericRightDto contratRetour = response.getContracts().get(0);
    Assertions.assertEquals(ContextConstants.TP_ONLINE, contratRetour.getContext());
    Assertions.assertEquals("0000003664", contratRetour.getInsurerId());
    Assertions.assertEquals("codeOc", contratRetour.getOcCode());
    Assertions.assertEquals("1213", contratRetour.getNumber());
    Assertions.assertEquals("1213_EXT", contratRetour.getExternalNumber());
    Assertions.assertEquals("ADH_1213", contratRetour.getSubscriberId());
    Assertions.assertEquals("ADH_1213COMPLET", contratRetour.getSubscriberFullId());
    Assertions.assertEquals("2022-01-01", contratRetour.getSubscriptionDate());
    Assertions.assertNull(contratRetour.getTerminationDate());
    Assertions.assertNull(contratRetour.getBusinessContributor());

    final List<Period> periodResponsable = contratRetour.getResponsibleContractOpenPeriods();
    Assertions.assertEquals(startDate, periodResponsable.get(0).getStart());
    Assertions.assertEquals(endDate, periodResponsable.get(0).getEnd());

    final List<CmuContractOpenPeriod> periodCMU = contratRetour.getCmuContractOpenPeriods();
    Assertions.assertEquals(startDate, periodCMU.get(0).getPeriod().getStart());
    Assertions.assertEquals(endDate, periodCMU.get(0).getPeriod().getEnd());
    Assertions.assertEquals("CMU", periodCMU.get(0).getCode());

    Assertions.assertEquals("CRITSECONDDETAIL", contratRetour.getDetailedSecondaryCriterion());
    Assertions.assertEquals("CRITSECOND", contratRetour.getSecondaryCriterion());
    Assertions.assertEquals(true, contratRetour.getIsIndividualContract());
    Assertions.assertEquals("GEST_BLUE", contratRetour.getOperator());

    final CollectiveContract collectiveContract = contratRetour.getCollectiveContract();
    Assertions.assertEquals("COLL", collectiveContract.getNumber());
    Assertions.assertEquals("EXTCOLL", collectiveContract.getExternalNumber());

    Assertions.assertEquals("QUAL_BLUE", contratRetour.getQualification());
    Assertions.assertEquals("1", contratRetour.getPrioritizationOrder());

    final Insured insured = contratRetour.getInsured();
    Assertions.assertEquals(Boolean.FALSE, insured.getIsSubscriber());
    Assertions.assertEquals("RA", insured.getAdministrativeRank());

    final Identity identity = insured.getIdentity();

    Assertions.assertEquals("19800605", identity.getBirthDate());
    Assertions.assertEquals("1", identity.getBirthRank());
    Assertions.assertEquals("1791062498047", identity.getNir().getCode());
    Assertions.assertEquals("45", identity.getNir().getKey());
    Assertions.assertEquals("0000003664-1213", identity.getPersonNumber());
    Assertions.assertEquals("7209738ADF", identity.getPersonExternalRef());
    final List<AffiliationRO> affiliationRO = identity.getAffiliationsRO();

    Assertions.assertEquals("1791062498047", affiliationRO.get(0).getNir().getCode());
    Assertions.assertEquals("45", affiliationRO.get(0).getNir().getKey());
    Assertions.assertEquals("2022-01-01", affiliationRO.get(0).getPeriod().getStart());
    Assertions.assertEquals("2023-01-01", affiliationRO.get(0).getPeriod().getEnd());
    Assertions.assertEquals("6200", affiliationRO.get(0).getAttachementRO().getCenterCode());
    Assertions.assertEquals("03", affiliationRO.get(0).getAttachementRO().getRegimeCode());
    Assertions.assertEquals(
        "054", affiliationRO.get(0).getAttachementRO().getHealthInsuranceCompanyCode());

    Assertions.assertEquals("2791062498047", affiliationRO.get(1).getNir().getCode());
    Assertions.assertEquals("44", affiliationRO.get(1).getNir().getKey());
    Assertions.assertEquals("2022-01-01", affiliationRO.get(1).getPeriod().getStart());
    Assertions.assertEquals("2023-01-01", affiliationRO.get(1).getPeriod().getEnd());
    Assertions.assertEquals("4200", affiliationRO.get(1).getAttachementRO().getCenterCode());
    Assertions.assertEquals("04", affiliationRO.get(1).getAttachementRO().getRegimeCode());
    Assertions.assertEquals(
        "044", affiliationRO.get(1).getAttachementRO().getHealthInsuranceCompanyCode());

    final InsuredData data = insured.getData();
    Assertions.assertEquals("ligne1", data.getAddress().getLine1());
    Assertions.assertEquals("ligne2", data.getAddress().getLine2());
    Assertions.assertEquals("ligne3", data.getAddress().getLine3());
    Assertions.assertEquals("ligne4", data.getAddress().getLine4());
    Assertions.assertEquals("ligne5", data.getAddress().getLine5());
    Assertions.assertEquals("ligne6", data.getAddress().getLine6());
    Assertions.assertEquals("ligne7", data.getAddress().getLine7());
    Assertions.assertEquals("codepostal", data.getAddress().getPostcode());

    Assertions.assertEquals("nomPatro", data.getName().getCommonName());
    Assertions.assertEquals("prenom", data.getName().getFirstName());
    Assertions.assertEquals("nom", data.getName().getLastName());
    Assertions.assertEquals("M", data.getName().getCivility());
    Assertions.assertEquals("email", data.getContact().getEmail());
    Assertions.assertEquals("000", data.getContact().getLandline());
    Assertions.assertEquals("000", data.getContact().getMobile());

    Assertions.assertEquals("2020-01-01", insured.getHealthMutualSubscriptionDate());
    Assertions.assertEquals("2020-02-01", insured.getIndividualSubscriptionStartDate());
    Assertions.assertEquals("CONTRATIND", insured.getIndividualSubscriptionNumber());
    Assertions.assertEquals("9999-12-31", insured.getCancellationDate());

    final DigitRelation digitRelation = insured.getDigitRelation();
    Assertions.assertNull(digitRelation.getDematerialization());
    final List<RemoteTransmission> remoteTransmissions = digitRelation.getRemoteTransmissions();
    Assertions.assertEquals(false, remoteTransmissions.get(0).getIsRemotelyTransmitted());
    Assertions.assertEquals(startDate, remoteTransmissions.get(0).getPeriod().getStart());
    Assertions.assertEquals(endDate, remoteTransmissions.get(0).getPeriod().getEnd());
    Assertions.assertNull(insured.getAttendingPhysicianOpenedPeriods());
    Assertions.assertEquals("Special", insured.getSpecialPlans().get(0).getCode());
    Assertions.assertEquals(startDate, insured.getSpecialPlans().get(0).getPeriod().getStart());
    Assertions.assertEquals(endDate, insured.getSpecialPlans().get(0).getPeriod().getEnd());

    Assertions.assertEquals("C", insured.getQuality().getCode());
    Assertions.assertEquals("Conjoint", insured.getQuality().getLabel());
    final Right rightComp = contratRetour.getInsured().getRights().get(1);
    final Right rightBase = contratRetour.getInsured().getRights().get(0);
    Assertions.assertEquals("KC_PlatineComp", rightComp.getCode());
    Assertions.assertEquals("KC_PlatineBase", rightBase.getCode());
    Assertions.assertEquals("KLESIA_CARCEPT", rightComp.getInsurerCode());
    Assertions.assertEquals("01", rightComp.getPrioritizationOrder());
    Assertions.assertEquals("2019-01-01", rightComp.getGuaranteeAgeDate());

    Assertions.assertEquals("KC_PlatineComp", rightComp.getProducts().get(0).getProductCode());
    Assertions.assertEquals("123", rightComp.getProducts().get(0).getOfferCode());
    Assertions.assertEquals("OFFRE_KC_PLATINE_BASE", rightBase.getProducts().get(0).getOfferCode());

    Assertions.assertEquals("KC_PlatineBase", rightBase.getProducts().get(0).getProductCode());
    final BenefitType benefitType =
        rightBase.getProducts().get(0).getBenefitsType().iterator().next();
    Assertions.assertEquals("2022-01-01", benefitType.getPeriod().getStart());

    Assertions.assertEquals("2022-08-01", benefitType.getPeriod().getEnd());

    Assertions.assertEquals(4, rightBase.getProducts().get(0).getBenefitsType().size());

    final BenefitType benefitTypeComp =
        rightComp.getProducts().get(0).getBenefitsType().iterator().next();
    Assertions.assertEquals("2022-01-01", benefitTypeComp.getPeriod().getStart());
    Assertions.assertEquals("2022-08-01", benefitTypeComp.getPeriod().getEnd());

    Assertions.assertEquals(7, rightComp.getProducts().get(0).getBenefitsType().size());
  }

  @Test
  void testExecuteTPOnlineTDBWithDomains() throws IOException {
    final String startDate = "2021-02-15";
    final String endDate = "2022-11-01";

    final UniqueAccessPointRequestV5 requete =
        new UniqueAccessPointRequestV5(
            "1791059632524",
            "19800605",
            "1",
            startDate,
            endDate,
            "123456",
            null,
            null,
            ContextConstants.TP_ONLINE,
            "OPTI",
            null,
            null,
            null,
            false);

    final ContractTP contractTP1 = this.contract.getContrat();
    contractTP1
        .getBeneficiaires()
        .forEach(
            beneficiaireContract ->
                beneficiaireContract.getDomaineDroits().stream()
                    .flatMap(domaineDroitContract -> domaineDroitContract.getGaranties().stream())
                    .flatMap(garantie -> garantie.getProduits().stream())
                    .forEach(produit -> produit.setCodeOffre(null)));
    final AggregationResults<ContractTP> res =
        new AggregationResults<>(List.of(contractTP1), new Document());

    this.mockListBenefWithContract(res);

    this.mockOc();

    mockOfferStructurePW(
        "src/test/resources/offerStructure.json",
        requete.getStartDate(),
        requete.getEndDate(),
        ContextConstants.TP_ONLINE,
        "KC_PlatineComp");
    mockOfferStructurePW(
        "src/test/resources/offerStructure_KC_PlatineBase.json",
        requete.getStartDate(),
        requete.getEndDate(),
        ContextConstants.TP_ONLINE,
        "KC_PlatineBase");

    Mockito.doReturn("http://next-engine-core-api:8080")
        .when(beyondPropertiesService)
        .getPropertyOrThrowError(PW_API_URL);
    Mockito.doReturn("/v4/offerStructure")
        .when(beyondPropertiesService)
        .getPropertyOrThrowError(PW_ENDPOINT_OFFERSTRUCTURE);
    Mockito.doReturn("INSURER").when(beyondPropertiesService).getPropertyOrThrowError(CLIENT_TYPE);

    final UniqueAccessPointResponse response = this.uapV5Service.execute(requete);
    Assertions.assertEquals(1, response.getContracts().size());
    final GenericRightDto contratRetour = response.getContracts().get(0);
    Assertions.assertEquals(2, contratRetour.getInsured().getRights().size());
    final Right rightBase = contratRetour.getInsured().getRights().get(0);

    Assertions.assertEquals("OFFRE_KC_PLATINE_BASE", rightBase.getProducts().get(0).getOfferCode());

    Assertions.assertEquals("KC_PlatineBase", rightBase.getProducts().get(0).getProductCode());

    Set<BenefitType> benefitTypeSet =
        contratRetour.getInsured().getRights().get(0).getProducts().get(0).getBenefitsType();
    Assertions.assertEquals(4, benefitTypeSet.size());
    Assertions.assertEquals("OPTIQUE", benefitTypeSet.stream().findFirst().get().getBenefitType());
  }

  @Test
  void testExecuteTPOnlineWithSubscriberShouldReturnOneContract() {
    final String startDate = "2022-07-17";
    final String endDate = "2022-12-17";

    final UniqueAccessPointRequestV5 requete =
        new UniqueAccessPointRequestV5(
            "1791059632524",
            "19800605",
            "1",
            startDate,
            endDate,
            "123456",
            "ADH_1213",
            null,
            ContextConstants.TP_ONLINE,
            null,
            null,
            null,
            null,
            false);

    final ContractTP contrat = this.contract.getContrat();
    contrat.setOrigineDeclaration(Constants.ORIGINE_DECLARATIONEVT);
    final AggregationResults<ContractTP> res =
        new AggregationResults<>(List.of(contrat), new Document());
    this.mockListBenefWithContract(res);

    this.mockOc();

    final UniqueAccessPointResponse response = this.uapV5Service.execute(requete);
    final GenericRightDto returnContract = response.getContracts().get(0);
    Assertions.assertNotNull(returnContract);
  }

  @Test
  void testExecuteTPOnlineWithSubscriberAndDomainShouldReturnOneContract() {
    final String startDate = "2022-07-17";
    final String endDate = "2022-12-17";
    final UniqueAccessPointRequestV5 requete =
        new UniqueAccessPointRequestV5(
            "1791059632524",
            "19800605",
            "1",
            startDate,
            endDate,
            "123456",
            "ADH_1213",
            null,
            ContextConstants.TP_ONLINE,
            "HOSP",
            null,
            null,
            null,
            false);

    final ContractTP contrat = this.contract.getContrat();
    contrat.setOrigineDeclaration(Constants.ORIGINE_DECLARATIONEVT);
    final AggregationResults<ContractTP> res =
        new AggregationResults<>(List.of(contrat), new Document());

    this.mockListBenefWithContract(res);

    this.mockOc();

    final UniqueAccessPointResponse response = this.uapV5Service.execute(requete);
    final GenericRightDto returnContract = response.getContracts().get(0);
    Assertions.assertNotNull(returnContract);
  }

  @Test
  void testExecuteTPOnlineWithSubscriberAndDomainShouldReturnExceptionTypeDeDepenseNonOuvert() {
    final String startDate = "2022-07-17";
    final String endDate = "2022-12-17";
    final UniqueAccessPointRequestV5 requete =
        new UniqueAccessPointRequestV5(
            "1791059632524",
            "19800605",
            "1",
            startDate,
            endDate,
            "123456",
            "ADH_123",
            null,
            ContextConstants.TP_ONLINE,
            "OPSI",
            null,
            null,
            null,
            false);

    final ContractTP contrat = this.contract.getContrat();
    contrat.setOrigineDeclaration(Constants.ORIGINE_DECLARATIONEVT);
    final AggregationResults<ContractTP> res =
        new AggregationResults<>(List.of(contrat), new Document());

    this.mockListBenefWithContract(res);

    this.mockOc();

    try {
      this.uapV5Service.execute(requete);
    } catch (final UAPFunctionalException e) {
      Assertions.assertEquals("Type de dépense non ouvert", e.getMessage());
      return;
    }
    Assertions.fail();
  }

  @Test
  void testExecuteTPOnlineWithSubscriberAndDomainShouldReturnExceptionTypeDepenseNonOuvert() {
    final String startDate = "2022-07-17";
    final String endDate = "2022-12-17";
    final UniqueAccessPointRequestV5 requete =
        new UniqueAccessPointRequestV5(
            "1791059632524",
            "19800605",
            "1",
            startDate,
            endDate,
            "123456",
            "ADH_1213",
            null,
            ContextConstants.TP_ONLINE,
            "OPSI",
            null,
            null,
            null,
            false);

    final ContractTP contrat = this.contract.getContrat();
    contrat.setOrigineDeclaration(Constants.ORIGINE_DECLARATIONEVT);
    final AggregationResults<ContractTP> res =
        new AggregationResults<>(List.of(contrat), new Document());

    this.mockListBenefWithContract(res);

    this.mockOc();

    try {
      this.uapV5Service.execute(requete);
    } catch (final UAPFunctionalException e) {
      Assertions.assertEquals("Type de dépense non ouvert", e.getMessage());
      return;
    }
    Assertions.fail();
  }

  @Test
  void testExecuteTPOnlineWithSubscriberAndDomainShouldReturnOneContract2() {
    final String startDate = "2022-07-17";
    final String endDate = "2022-10-17";
    final UniqueAccessPointRequestV5 requete =
        new UniqueAccessPointRequestV5(
            "1791059632524",
            "19800605",
            "1",
            startDate,
            endDate,
            "123456",
            null,
            null,
            ContextConstants.TP_ONLINE,
            "OPTI",
            null,
            null,
            null,
            false);

    final ContractTP contrat = this.contract.getContrat();
    contrat.setOrigineDeclaration(Constants.ORIGINE_DECLARATIONEVT);
    final AggregationResults<ContractTP> res =
        new AggregationResults<>(List.of(contrat), new Document());

    this.mockListBenefWithContract(res);

    this.mockOc();

    final UniqueAccessPointResponse response = this.uapV5Service.execute(requete);
    Assertions.assertEquals(1, response.getContracts().size());
  }

  @Test
  void testExecuteTPOnlineWithSubscriberAndDomainShouldReturnExceptionDroitsNonOuverts() {
    final String startDate = "2020-01-22";
    final String endDate = "2020-02-22";
    final UniqueAccessPointRequestV5 requete =
        new UniqueAccessPointRequestV5(
            "1791059632524",
            "19800605",
            "1",
            startDate,
            endDate,
            "123456",
            "ADH_1213",
            null,
            ContextConstants.TP_ONLINE,
            "OPTI",
            null,
            null,
            null,
            false);

    final ContractTP contrat = this.contract.getContrat();
    contrat.setOrigineDeclaration(Constants.ORIGINE_DECLARATIONEVT);
    final AggregationResults<ContractTP> res =
        new AggregationResults<>(List.of(contrat), new Document());

    this.mockListBenefWithContract(res);

    this.mockOc();

    try {
      this.uapV5Service.execute(requete);
    } catch (final UAPFunctionalException e) {
      Assertions.assertEquals("Droits non ouverts", e.getMessage());
      return;
    }
    Assertions.fail();
  }

  @Test
  void testExecuteTPOnlineWithSubscriberAndDomainShouldReturnExceptionDroitsNonOuverts2() {
    final String startDate = "2024-03-01";
    final String endDate = "2024-12-17";
    final UniqueAccessPointRequestV5 requete =
        new UniqueAccessPointRequestV5(
            "1791059632524",
            "19800605",
            "1",
            startDate,
            endDate,
            "123456",
            "ADH_123",
            null,
            ContextConstants.TP_ONLINE,
            "OPTI",
            null,
            null,
            null,
            false);

    final ContractTP contrat = this.contract.getContrat();
    contrat.setOrigineDeclaration(Constants.ORIGINE_DECLARATIONEVT);
    final AggregationResults<ContractTP> res =
        new AggregationResults<>(List.of(contrat), new Document());

    this.mockListBenefWithContract(res);

    this.mockOc();

    try {
      this.uapV5Service.execute(requete);
    } catch (final UAPFunctionalException e) {
      Assertions.assertEquals("Droits non ouverts", e.getMessage());
      return;
    }
    Assertions.fail();
  }

  @Test
  void testGetAdherentPeriodList1() {
    final ContractTP contrat = this.contract.getContrat();

    final UniqueAccessPointRequestV5 request =
        new UniqueAccessPointRequestV5(
            "7781021111340",
            "19631021",
            "1",
            "2022-02-07",
            "2022-02-07",
            null,
            null,
            null,
            ContextConstants.TP_ONLINE,
            null,
            null,
            null,
            null,
            false);

    final List<ContractAdherentPeriode> list =
        UniqueAccessPointServiceTPImpl.getAdherentPeriodList(List.of(contrat), request);

    Assertions.assertEquals(6, list.size());
    Assertions.assertEquals("ADH_1213", list.get(0).getNumeroAdherent());
  }

  @Test
  void testGetAdherentPeriodList2() {
    final ContractTP contrat = this.contract.getContrat();

    final UniqueAccessPointRequestV5 request =
        new UniqueAccessPointRequestV5(
            "7781021111340",
            "19631021",
            "1",
            "2020-02-07",
            "2020-02-07",
            null,
            null,
            null,
            ContextConstants.TP_ONLINE,
            null,
            null,
            null,
            null,
            false);

    final List<ContractAdherentPeriode> list =
        UniqueAccessPointServiceTPImpl.getAdherentPeriodList(List.of(contrat), request);

    Assertions.assertEquals(0, list.size());
  }

  @Test
  void testGetNumeroAdherent() {
    final ContractTP contrat = this.contract.getContrat();
    contrat.setOrigineDeclaration(Constants.ORIGINE_DECLARATIONEVT);
    final UniqueAccessPointTPRequestV5 uniqueAccessPointTPRequestV5 =
        getUniqueAccessPointV5TPOnlineRequest();
    Assertions.assertEquals(
        "ADH_1213",
        this.uapV5Service.getNumeroAdherent(uniqueAccessPointTPRequestV5, List.of(contrat)));
  }

  private static UniqueAccessPointTPRequestV5 getUniqueAccessPointV5TPOnlineRequest() {
    final UniqueAccessPointTPRequestV5 uniqueAccessPointTPRequestV5 =
        new UniqueAccessPointTPRequestV5();
    final UniqueAccessPointRequestV5 request =
        new UniqueAccessPointRequestV5(
            "7781021111340",
            "19631021",
            "1",
            "2023-02-07",
            "2023-02-07",
            null,
            null,
            null,
            ContextConstants.TP_ONLINE,
            null,
            null,
            null,
            null,
            false);

    uniqueAccessPointTPRequestV5.setRequest(request);
    return uniqueAccessPointTPRequestV5;
  }

  @Test
  void testGetNumeroAdherentWhenFirstSearchIsEmptyReturnNull() {
    final ContractTP contrat1 = this.contract.getContrat();
    contrat1.setNumeroContrat("123456");
    contrat1.setNumeroAdherent("ADH_1215");
    final ContractTP contrat2 = this.contract.getContrat();
    contrat2.setNumeroContrat("123456");
    contrat2.setNumeroAdherent("ADH_1215");
    final UniqueAccessPointTPRequestV5 uniqueAccessPointTPRequestV5 =
        getUniqueAccessPointV5TPOnlineRequest();
    uniqueAccessPointTPRequestV5.getRequest().setSubscriberId("ADH_1214");
    Assertions.assertNull(
        this.uapV5Service.getNumeroAdherentWhenFirstSearchIsEmpty(
            uniqueAccessPointTPRequestV5, List.of(contrat1, contrat2), null));
  }

  @Test
  void
      testGetNumeroAdherentWhenFirstSearchIsEmptyReturnAnotherAdherentWhenAdherentInRequestIsNull() {
    final ContractTP contrat1 = this.contract.getContrat();
    contrat1.setNumeroContrat("123456");
    contrat1.setNumeroAdherent("ADH_1215");
    final ContractTP contrat2 = this.contract.getContrat();
    contrat2.setNumeroContrat("123456");
    contrat2.setNumeroAdherent("ADH_1215");
    final UniqueAccessPointTPRequestV5 uniqueAccessPointTPRequestV5 =
        getUniqueAccessPointV5TPOnlineRequest();
    uniqueAccessPointTPRequestV5.getRequest().setStartDate("2022-01-01");
    uniqueAccessPointTPRequestV5.getRequest().setEndDate("2022-12-31");
    Assertions.assertEquals(
        "ADH_1215",
        this.uapV5Service.getNumeroAdherentWhenFirstSearchIsEmpty(
            uniqueAccessPointTPRequestV5, List.of(contrat1, contrat2), null));
  }

  @Test
  void
      testGetNumeroAdherentWhenFirstSearchIsEmptyReturnAnotherAdherentWhenAdherentInRequestIsNotNull() {
    final ContractTP contrat1 = this.contract.getContrat();
    contrat1.setNumeroContrat("123456");
    contrat1.setIndividuelOuCollectif("2");
    contrat1.setNumeroAdherent("ADH_1217");
    final ContractTP contrat2 = this.contract.getContrat();
    contrat2.setNumeroContrat("123456");
    contrat2.setNumeroAdherent("ADH_1215");
    final UniqueAccessPointTPRequestV5 uniqueAccessPointTPRequestV5 =
        getUniqueAccessPointV5TPOnlineRequest();
    uniqueAccessPointTPRequestV5.getRequest().setStartDate("2022-01-01");
    uniqueAccessPointTPRequestV5.getRequest().setEndDate("2022-12-31");
    uniqueAccessPointTPRequestV5.getRequest().setSubscriberId("ADH_1214");
    Assertions.assertEquals(
        "ADH_1217",
        this.uapV5Service.getNumeroAdherentWhenFirstSearchIsEmpty(
            uniqueAccessPointTPRequestV5, List.of(contrat1, contrat2), null));
  }

  @Test
  void testGetNumeroAdherentWhenMultiContratSameDate() {
    final ContractTP contrat1 = this.contract.getContrat();
    contrat1.setNumeroContrat("123456");
    contrat1.setNumeroAdherent("ADH_1215");
    contrat1
        .getBeneficiaires()
        .get(0)
        .getDomaineDroits()
        .get(0)
        .getGaranties()
        .get(0)
        .getProduits()
        .get(0)
        .getReferencesCouverture()
        .get(0)
        .getNaturesPrestation()
        .get(0)
        .getPeriodesDroit()
        .get(0)
        .setPeriodeDebut("2021/01/01");
    contrat1
        .getBeneficiaires()
        .get(0)
        .getDomaineDroits()
        .get(0)
        .getGaranties()
        .get(0)
        .getProduits()
        .get(0)
        .getReferencesCouverture()
        .get(0)
        .getNaturesPrestation()
        .get(0)
        .getPeriodesDroit()
        .get(1)
        .setPeriodeDebut("2021/01/01");
    contrat1
        .getBeneficiaires()
        .get(0)
        .getDomaineDroits()
        .get(1)
        .getGaranties()
        .get(0)
        .getProduits()
        .get(0)
        .getReferencesCouverture()
        .get(0)
        .getNaturesPrestation()
        .get(0)
        .getPeriodesDroit()
        .get(0)
        .setPeriodeDebut("2021/01/01");
    final ContractTP contrat2 = this.contract.getContrat();
    contrat2.setNumeroContrat("123456");
    contrat2.setNumeroAdherent("ADH_1216");
    contrat2
        .getBeneficiaires()
        .get(0)
        .getDomaineDroits()
        .get(0)
        .getGaranties()
        .get(0)
        .getProduits()
        .get(0)
        .getReferencesCouverture()
        .get(0)
        .getNaturesPrestation()
        .get(0)
        .getPeriodesDroit()
        .get(0)
        .setPeriodeDebut("2021/01/01");
    contrat2
        .getBeneficiaires()
        .get(0)
        .getDomaineDroits()
        .get(0)
        .getGaranties()
        .get(0)
        .getProduits()
        .get(0)
        .getReferencesCouverture()
        .get(0)
        .getNaturesPrestation()
        .get(0)
        .getPeriodesDroit()
        .get(1)
        .setPeriodeDebut("2021/01/01");
    contrat2
        .getBeneficiaires()
        .get(0)
        .getDomaineDroits()
        .get(1)
        .getGaranties()
        .get(0)
        .getProduits()
        .get(0)
        .getReferencesCouverture()
        .get(0)
        .getNaturesPrestation()
        .get(0)
        .getPeriodesDroit()
        .get(0)
        .setPeriodeDebut("2021/01/01");
    final UniqueAccessPointTPRequestV5 uniqueAccessPointTPRequestV5 =
        getUniqueAccessPointV5TPOnlineRequest();
    uniqueAccessPointTPRequestV5.getRequest().setStartDate("2022-01-01");
    uniqueAccessPointTPRequestV5.getRequest().setEndDate("2022-12-31");
    Assertions.assertEquals(
        "ADH_1215",
        this.uapV5Service.getNumeroAdherentWhenFirstSearchIsEmpty(
            uniqueAccessPointTPRequestV5, List.of(contrat1, contrat2), null));
  }

  @Test
  void shouldThrowContratResilie() {
    final UniqueAccessPointTPRequestV5 uniqueAccessPointTPRequestV5 =
        getUniqueAccessPointV5TPOnlineRequest();

    try {
      this.uapV5Service.getNumeroAdherent(uniqueAccessPointTPRequestV5, new ArrayList<>());
    } catch (final UAPFunctionalException e) {
      Assertions.assertEquals("Contrat résilié", e.getMessage());
      return;
    }
    Assertions.fail();
  }

  @Test
  void testBenefCas1ExecuteTPOfflineWithSubscriberAndDomainShouldReturnOneContract() {
    final String startDate = "2022-07-17";
    final String endDate = "2022-10-17";
    final UniqueAccessPointRequestV5 requete =
        new UniqueAccessPointRequestV5(
            "1791059632524",
            "19800605",
            "1",
            startDate,
            endDate,
            "123456",
            "ADH_1213",
            null,
            ContextConstants.TP_ONLINE,
            "OPTI",
            null,
            null,
            null,
            false);

    final ContractTP contrat = this.contract.getContrat();
    contrat.setOrigineDeclaration(Constants.ORIGINE_DECLARATIONEVT);
    final AggregationResults<ContractTP> res =
        new AggregationResults<>(List.of(contrat), new Document());

    this.mockListBenefWithContract(res);

    this.mockOc();

    final UniqueAccessPointResponse response = this.uapV5Service.execute(requete);
    Assertions.assertEquals(1, response.getContracts().size());
  }

  @Test
  void testBenefCas2ExecuteTPOnlineWithSubscriberAndDomainShouldReturnOneContract() {
    final String startDate = "2022-07-17";
    final String endDate = "2022-10-17";
    final UniqueAccessPointRequestV5 requete =
        new UniqueAccessPointRequestV5(
            "1791059632524",
            "19800605",
            "1",
            startDate,
            endDate,
            "123456",
            "ADH_1215",
            null,
            ContextConstants.TP_ONLINE,
            "OPTI",
            null,
            null,
            null,
            false);

    final ContractTP contrat = this.contract.getContrat();
    contrat.setOrigineDeclaration(Constants.ORIGINE_DECLARATIONEVT);
    final AggregationResults<ContractTP> res =
        new AggregationResults<>(List.of(contrat), new Document());

    this.mockListBenefWithContract(res);

    this.mockOc();

    final UniqueAccessPointResponse response = this.uapV5Service.execute(requete);
    Assertions.assertEquals(1, response.getContracts().size());
  }

  @Test
  void testBenefWithBeneficiaryExecuteTPOnlineWithSubscriberAndDomainShouldReturnOneContract() {
    final String startDate = "2022-07-17";
    final String endDate = "2022-10-17";

    final UniqueAccessPointRequestV5 requete =
        new UniqueAccessPointRequestV5(
            "1791059632524",
            "19800605",
            "1",
            startDate,
            endDate,
            "123456",
            null,
            null,
            ContextConstants.TP_ONLINE,
            "OPTI",
            "123456",
            null,
            null,
            false);

    final ContractTP contrat = this.contract.getContrat();
    contrat.setOrigineDeclaration(Constants.ORIGINE_DECLARATIONEVT);
    final AggregationResults<ContractTP> res =
        new AggregationResults<>(List.of(contrat), new Document());

    this.mockListBenefWithContract(res);

    this.mockOc();

    final UniqueAccessPointResponse response = this.uapV5Service.execute(requete);
    Assertions.assertEquals(1, response.getContracts().size());
  }

  @Test
  void
      testBenefWithNumeroAmcEchangeExecuteTPOnlineWithSubscriberAndDomainShouldReturnOneContract() {
    final String startDate = "2022-07-17";
    final String endDate = "2022-10-17";
    final UniqueAccessPointRequestV5 requete =
        new UniqueAccessPointRequestV5(
            "1791059632524",
            "19800605",
            "1",
            startDate,
            endDate,
            "123456",
            "ADH_1215",
            null,
            ContextConstants.TP_ONLINE,
            "OPTI",
            null,
            null,
            null,
            false);

    final ContractTP contrat = this.contract.getContrat();
    contrat.setOrigineDeclaration(Constants.ORIGINE_DECLARATIONEVT);
    final AggregationResults<ContractTP> res =
        new AggregationResults<>(List.of(contrat), new Document());

    this.mockListBenefWithContract(res, "152362");

    this.mockOc();

    final UniqueAccessPointResponse response = this.uapV5Service.execute(requete);
    Assertions.assertEquals(1, response.getContracts().size());
  }

  @Test
  void testPriorityTpContract_returnOC() {
    boolean isTpOnline = true;
    final UniqueAccessPointRequestV5 requete =
        new UniqueAccessPointRequestV5(
            "1791062498047",
            "19800605",
            "1",
            "",
            null,
            null,
            "ADH_1213",
            null,
            ContextConstants.TP_ONLINE,
            null,
            null,
            null,
            null,
            false);

    final UniqueAccessPointTPRequest uniqueAccessPointTPRequest =
        new UniqueAccessPointTPRequestV5();
    uniqueAccessPointTPRequest.setRequest(requete);
    uniqueAccessPointTPRequest.setNumeroPersonnes(List.of("1213"));

    // C1 sur OS1 valide du 01/01/2023 => 31/03/2023
    // C2 sur OS2 valide du 01/04/2023 => 31/03/2024
    // C3 sur OS3 valide du 01/04/2023 => 31/05/2024
    final ContractTP contractTP1 =
        this.contract.getContrat_testPriorityTpContract(
            "C1", "OS1", "2023/01/21", "2023/03/31", isTpOnline);
    final ContractTP contractTP2 =
        this.contract.getContrat_testPriorityTpContract(
            "C2", "OS2", "2023/04/01", "2024/03/31", isTpOnline);
    final ContractTP contractTP3 =
        this.contract.getContrat_testPriorityTpContract(
            "C3", "OS3", "2023/04/01", "2024/05/31", isTpOnline);

    List<ContractTP> contractTPS = new ArrayList<>();
    contractTPS.add(contractTP1);
    contractTPS.add(contractTP2);
    contractTPS.add(contractTP3);

    // Interrogation en date du 01/01/2023 => retourne OS1
    final Triple<String, String, String> result1 =
        this.issuingCompanyCodeService.getFuturContractInfo(
            uniqueAccessPointTPRequest.getNumeroPersonnes(), contractTPS, isTpOnline);
    Assertions.assertEquals("OS1", result1.getLeft());

    // Interrogation en date du 01/01/2000 => retourne OS1
    requete.setStartDate("2000-01-01");
    final Triple<String, String, String> result2 =
        this.issuingCompanyCodeService.getFuturContractInfo(
            uniqueAccessPointTPRequest.getNumeroPersonnes(), contractTPS, isTpOnline);
    Assertions.assertEquals("OS1", result2.getLeft());

    // Interrogation en date du 01/06/2024 => retourne OS3
    requete.setStartDate("2024-06-01");
    final Triple<String, String, String> result3 =
        this.issuingCompanyCodeService.getPastContractInfo(
            uniqueAccessPointTPRequest.getNumeroPersonnes(), contractTPS, isTpOnline);
    Assertions.assertEquals("OS3", result3.getLeft());

    // C1 sur OS1 valide du 01/01/2023 => 31/03/2023
    // C2 sur OS2 valide du 01/04/2023 => 31/03/2023
    // C3 sur OS3 valide du 01/05/2023 => 31/03/2024
    // C4 sur OS4 valide du 01/05/2023 => 31/03/2024
    final ContractTP contractTP4 =
        this.contract.getContrat_testPriorityTpContract(
            "C1", "OS1", "2023/01/01", "2023/03/31", isTpOnline);
    final ContractTP contractTP5 =
        this.contract.getContrat_testPriorityTpContract(
            "C2", "OS2", "2023/04/01", "2023/03/31", isTpOnline);
    final ContractTP contractTP6 =
        this.contract.getContrat_testPriorityTpContract(
            "C3", "OS3", "2023/05/01", "2024/03/31", isTpOnline);
    final ContractTP contractTP7 =
        this.contract.getContrat_testPriorityTpContract(
            "C4", "OS4", "2023/05/01", "2024/03/31", isTpOnline);

    List<ContractTP> contracts2 = new ArrayList<>();
    contracts2.add(contractTP4);
    contracts2.add(contractTP5);
    contracts2.add(contractTP6);
    contracts2.add(contractTP7);

    // Interrogation en date du 01/04/2024 => retourne OS3
    requete.setStartDate("2024-04-01");
    final Triple<String, String, String> result4 =
        this.issuingCompanyCodeService.getPastContractInfo(
            uniqueAccessPointTPRequest.getNumeroPersonnes(), contracts2, isTpOnline);
    Assertions.assertEquals("OS3", result4.getLeft());

    // Interrogation en date du 01/01/2023 => retourne OS1
    requete.setStartDate("2023-01-01");
    final Triple<String, String, String> result5 =
        this.issuingCompanyCodeService.getFuturContractInfo(
            uniqueAccessPointTPRequest.getNumeroPersonnes(), contracts2, isTpOnline);
    Assertions.assertEquals("OS1", result5.getLeft());

    // Interrogation en date du 01/05/2024 => retourne OS3
    requete.setStartDate("2024-05-01");
    final Triple<String, String, String> result6 =
        this.issuingCompanyCodeService.getPastContractInfo(
            uniqueAccessPointTPRequest.getNumeroPersonnes(), contracts2, isTpOnline);
    Assertions.assertEquals("OS3", result6.getLeft());

    // Interrogation en date du 01/01/2000 => retourne OS1
    requete.setStartDate("2000-01-01");
    final Triple<String, String, String> result7 =
        this.issuingCompanyCodeService.getFuturContractInfo(
            uniqueAccessPointTPRequest.getNumeroPersonnes(), contracts2, isTpOnline);
    Assertions.assertEquals("OS1", result7.getLeft());
  }
}
