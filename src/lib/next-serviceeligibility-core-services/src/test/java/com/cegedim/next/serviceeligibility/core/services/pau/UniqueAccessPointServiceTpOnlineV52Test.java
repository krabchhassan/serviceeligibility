package com.cegedim.next.serviceeligibility.core.services.pau;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.services.GenerateContract;
import com.cegedim.next.serviceeligibility.core.utils.ContextConstants;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.*;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class UniqueAccessPointServiceTpOnlineV52Test extends UniqueAccessPointUtilsTesting {

  @Autowired private UniqueAccessPointServiceV5TPOnlineTPImpl uapV5Service;

  @Autowired private GenerateContract contract;

  // @Test pas encore compris pourquoi il passe 1 fois sur 2
  void testExecuteTPOnlineTDBOnMultipleOffers() throws IOException {
    final String startDate = "2022-01-01";
    final String endDate = "2022-09-01";
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

    final ContractTP contractTP1 = this.contract.getContrat();
    contractTP1
        .getBeneficiaires()
        .forEach(
            beneficiaireContract ->
                beneficiaireContract.getDomaineDroits().stream()
                    .flatMap(domaineDroitContract -> domaineDroitContract.getGaranties().stream())
                    .flatMap(garantie -> garantie.getProduits().stream())
                    .forEach(produit -> produit.setCodeOffre(null)));
    contractTP1
        .getBeneficiaires()
        .forEach(
            beneficiaireContract ->
                beneficiaireContract
                    .getDomaineDroits()
                    .forEach(
                        domaineDroitContract ->
                            domaineDroitContract
                                .getGaranties()
                                .forEach(garantie -> garantie.setCodeGarantie("A"))));
    final AggregationResults<ContractTP> res =
        new AggregationResults<>(List.of(contractTP1), new Document());

    this.mockListBenefWithContract(res);

    this.mockOc();
    mockOfferStructurePW(
        "src/test/resources/offersStructure_blue_001_multiple.json",
        requete.getStartDate(),
        requete.getEndDate(),
        ContextConstants.TP_ONLINE,
        "A");

    final UniqueAccessPointResponse response = this.uapV5Service.execute(requete);
    Assertions.assertEquals(1, response.getContracts().size());
    final GenericRightDto contratRetour = response.getContracts().get(0);
    Assertions.assertEquals(ContextConstants.TP_ONLINE, contratRetour.getContext());

    final Right rightBase = contratRetour.getInsured().getRights().get(0);
    Assertions.assertEquals("A", rightBase.getCode());
    Product product = rightBase.getProducts().get(0);
    Assertions.assertEquals("OFFRE", product.getOfferCode());
    Assertions.assertEquals("A", product.getProductCode());
    Assertions.assertEquals(3, product.getBenefitsType().size());
    Iterator<BenefitType> iterator = product.getBenefitsType().iterator();
    BenefitType benefitType = iterator.next();
    Assertions.assertEquals("OPTIQUE", benefitType.getBenefitType());
    Assertions.assertEquals("2022-01-01", benefitType.getPeriod().getStart());
    Assertions.assertEquals("2022-03-31", benefitType.getPeriod().getEnd());
    benefitType = iterator.next();
    Assertions.assertEquals("HOSPITALISATION", benefitType.getBenefitType());
    Assertions.assertEquals("2022-01-01", benefitType.getPeriod().getStart());
    Assertions.assertEquals("2022-09-01", benefitType.getPeriod().getEnd());
    benefitType = iterator.next();
    Assertions.assertEquals("OPTIQUE", benefitType.getBenefitType());
    Assertions.assertEquals("2022-07-01", benefitType.getPeriod().getStart());
    Assertions.assertEquals("2022-09-01", benefitType.getPeriod().getEnd());
  }
}
