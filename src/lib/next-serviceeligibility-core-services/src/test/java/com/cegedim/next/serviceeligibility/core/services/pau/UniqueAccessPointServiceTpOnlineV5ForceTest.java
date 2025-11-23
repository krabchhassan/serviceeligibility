package com.cegedim.next.serviceeligibility.core.services.pau;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.domain.TypeConventionnement;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.services.GenerateContract;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.ContextConstants;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointRequestV5;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointResponse;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class UniqueAccessPointServiceTpOnlineV5ForceTest extends UniqueAccessPointUtilsTesting {

  @Autowired private UniqueAccessPointServiceV5TPOnlineTPImpl uapV5Service;

  @Autowired private GenerateContract contract;

  @Test
  void testPAUForceShouldReturnContractValidAfterRequest() {
    final String startDate = "2022-07-15";
    final String endDate = "2022-07-15";

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
            "HOSP",
            "123456",
            null,
            null,
            true);

    final ContractTP contrat = this.contract.getContrat();
    contrat.setOrigineDeclaration(Constants.ORIGINE_DECLARATIONEVT);
    final ContractTP contrat2 = new ContractTP(contrat);
    final ContractTP contrat3 = new ContractTP(contrat);

    contrat.setNumeroContrat("1");
    contrat2.setNumeroContrat("2");
    contrat3.setNumeroContrat("3");
    contrat
        .getBeneficiaires()
        .get(0)
        .setDomaineDroits(List.of(mapDomaine("2022/01/01", "2022/06/30", null)));
    contrat2
        .getBeneficiaires()
        .get(0)
        .setDomaineDroits(List.of(mapDomaine("2022/09/01", null, null)));
    contrat3
        .getBeneficiaires()
        .get(0)
        .setDomaineDroits(List.of(mapDomaine("2023/01/01", "2023/04/30", null)));

    final AggregationResults<ContractTP> res =
        new AggregationResults<>(List.of(contrat, contrat2, contrat3), new Document());

    this.mockListBenefWithContract(res);

    this.mockOc();

    final UniqueAccessPointResponse response = this.uapV5Service.execute(requete);
    // Doit retourner le contrat 2 (contrat ayant la date de début de validité
    // postérieure la plus faible)
    Assertions.assertEquals(1, response.getContracts().size());
    Assertions.assertEquals("2", response.getContracts().get(0).getNumber());
    Assertions.assertTrue(response.getContracts().get(0).getIsForced());
    Assertions.assertNull(
        response
            .getContracts()
            .get(0)
            .getInsured()
            .getRights()
            .get(0)
            .getProducts()
            .get(0)
            .getPeriod()
            .getEnd());
  }

  @Test
  void testPAUForceShouldReturnContractsValidBeforeRequest() {
    final String startDate = "2023-07-15";
    final String endDate = "2023-07-15";

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
            "HOSP",
            "123456",
            null,
            null,
            true);

    final ContractTP contrat = this.contract.getContrat();
    contrat.setOrigineDeclaration(Constants.ORIGINE_DECLARATIONEVT);
    final ContractTP contrat2 = new ContractTP(contrat);
    final ContractTP contrat3 = new ContractTP(contrat);
    final ContractTP contrat4 = new ContractTP(contrat);
    final ContractTP contrat5 = new ContractTP(contrat);

    contrat.setNumeroContrat("1");
    contrat2.setNumeroContrat("2");
    contrat3.setNumeroContrat("3");
    contrat4.setNumeroContrat("4");
    contrat5.setNumeroContrat("5");
    contrat
        .getBeneficiaires()
        .get(0)
        .setDomaineDroits(List.of(mapDomaine("2022/01/01", "2022/06/30", null)));
    contrat2
        .getBeneficiaires()
        .get(0)
        .setDomaineDroits(List.of(mapDomaine("2022/09/01", "2022/09/30", null)));
    contrat3
        .getBeneficiaires()
        .get(0)
        .setDomaineDroits(List.of(mapDomaine("2023/01/01", "2023/04/30", null)));
    contrat4
        .getBeneficiaires()
        .get(0)
        .setDomaineDroits(List.of(mapDomaine("2023/01/01", "2023/06/30", "2023/06/30")));
    contrat5
        .getBeneficiaires()
        .get(0)
        .setDomaineDroits(List.of(mapDomaine("2023/01/01", "2023/06/30", "2023/06/30")));

    final AggregationResults<ContractTP> res =
        new AggregationResults<>(
            List.of(contrat, contrat2, contrat3, contrat4, contrat5), new Document());

    this.mockListBenefWithContract(res);

    this.mockOc();

    final UniqueAccessPointResponse response = this.uapV5Service.execute(requete);
    // Doit retourner les contrats 4 et 5 (pas de contrat ayant une date de début de
    // validité postérieure => sélection des contrats ayant la date de fin de
    // validité la plus grande)
    Assertions.assertEquals(2, response.getContracts().size());
    Assertions.assertEquals("4", response.getContracts().get(0).getNumber());
    Assertions.assertEquals("5", response.getContracts().get(1).getNumber());
    Assertions.assertTrue(response.getContracts().get(0).getIsForced());
    Assertions.assertTrue(response.getContracts().get(1).getIsForced());
  }

  @Test
  void testPAUSansFinPasDeForce() {
    final String startDate = "2022-07-15";
    final String endDate = null;

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
            "HOSP",
            "123456",
            null,
            null,
            true);

    final ContractTP contrat = this.contract.getContrat();
    contrat.setOrigineDeclaration(Constants.ORIGINE_DECLARATIONEVT);
    final ContractTP contrat2 = new ContractTP(contrat);
    final ContractTP contrat3 = new ContractTP(contrat);
    final ContractTP contrat4 = new ContractTP(contrat);

    contrat.setNumeroContrat("1");
    contrat2.setNumeroContrat("2");
    contrat3.setNumeroContrat("3");
    contrat4.setNumeroContrat("4");
    contrat
        .getBeneficiaires()
        .get(0)
        .setDomaineDroits(List.of(mapDomaine("2022/01/01", "2022/06/30", null)));
    contrat2
        .getBeneficiaires()
        .get(0)
        .setDomaineDroits(List.of(mapDomaine("2022/07/01", "2022/07/10", null)));
    contrat3
        .getBeneficiaires()
        .get(0)
        .setDomaineDroits(List.of(mapDomaine("2022/09/01", "2022/09/30", null)));
    contrat4
        .getBeneficiaires()
        .get(0)
        .setDomaineDroits(List.of(mapDomaine("2023/01/01", "2023/04/30", null)));

    final AggregationResults<ContractTP> res =
        new AggregationResults<>(List.of(contrat, contrat2, contrat3, contrat4), new Document());

    this.mockListBenefWithContract(res);

    this.mockOc();

    final UniqueAccessPointResponse response = this.uapV5Service.execute(requete);
    // La requête n'a pas de date de fin => 2 contrats sont ouverts après la date de
    // début demandée => on ne passe pas par les étapes de forçage
    Assertions.assertEquals(2, response.getContracts().size());
    Assertions.assertEquals("3", response.getContracts().get(0).getNumber());
    Assertions.assertEquals("4", response.getContracts().get(1).getNumber());
    Assertions.assertFalse(response.getContracts().get(0).getIsForced());
    Assertions.assertFalse(response.getContracts().get(1).getIsForced());
  }

  @Test
  void testPAUSansFinAvecForce() {
    final String startDate = "2023-05-01";
    final String endDate = null;

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
            "HOSP",
            "123456",
            null,
            null,
            true);

    final ContractTP contrat = this.contract.getContrat();
    contrat.setOrigineDeclaration(Constants.ORIGINE_DECLARATIONEVT);
    final ContractTP contrat2 = new ContractTP(contrat);
    final ContractTP contrat3 = new ContractTP(contrat);
    final ContractTP contrat4 = new ContractTP(contrat);

    contrat.setNumeroContrat("1");
    contrat2.setNumeroContrat("2");
    contrat3.setNumeroContrat("3");
    contrat4.setNumeroContrat("4");
    contrat
        .getBeneficiaires()
        .get(0)
        .setDomaineDroits(List.of(mapDomaine("2022/01/01", "2022/06/30", null)));
    contrat2
        .getBeneficiaires()
        .get(0)
        .setDomaineDroits(List.of(mapDomaine("2022/07/01", "2022/07/10", null)));
    contrat3
        .getBeneficiaires()
        .get(0)
        .setDomaineDroits(List.of(mapDomaine("2022/09/01", "2022/09/30", null)));
    contrat4
        .getBeneficiaires()
        .get(0)
        .setDomaineDroits(List.of(mapDomaine("2023/01/01", "2023/04/30", null)));

    final AggregationResults<ContractTP> res =
        new AggregationResults<>(List.of(contrat, contrat2, contrat3, contrat4), new Document());

    this.mockListBenefWithContract(res);

    this.mockOc();

    final UniqueAccessPointResponse response = this.uapV5Service.execute(requete);
    // La requête n'a pas de date de fin => Aucun contrat n'est ouvert après la date
    // de début demandée => on passe par les étapes de forçage
    // Directement par la step 2 : sélection des contrats ayant une date de fin
    // de validité inférieure à la date de soins puis sélection du contrat ayant la
    // date de fin de validité la plus grande => ici le contrat n°4
    Assertions.assertEquals(1, response.getContracts().size());
    Assertions.assertEquals("4", response.getContracts().get(0).getNumber());
    Assertions.assertTrue(response.getContracts().get(0).getIsForced());
  }

  @Test
  void testPAUSansFinAvecForce2() {
    final String startDate = "2023-07-01";
    final String endDate = null;

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
            "HOSP",
            "123456",
            null,
            null,
            true);

    final ContractTP contrat = this.contract.getContrat();
    contrat.setOrigineDeclaration(Constants.ORIGINE_DECLARATIONEVT);
    final ContractTP contrat2 = new ContractTP(contrat);
    final ContractTP contrat3 = new ContractTP(contrat);
    final ContractTP contrat4 = new ContractTP(contrat);
    final ContractTP contrat5 = new ContractTP(contrat);

    contrat.setNumeroContrat("1");
    contrat2.setNumeroContrat("2");
    contrat3.setNumeroContrat("3");
    contrat4.setNumeroContrat("4");
    contrat5.setNumeroContrat("5");
    contrat
        .getBeneficiaires()
        .get(0)
        .setDomaineDroits(List.of(mapDomaine("2022/01/01", "2022/06/30", null)));
    contrat2
        .getBeneficiaires()
        .get(0)
        .setDomaineDroits(List.of(mapDomaine("2022/07/01", "2022/07/10", null)));
    contrat3
        .getBeneficiaires()
        .get(0)
        .setDomaineDroits(List.of(mapDomaine("2022/09/01", "2022/09/30", null)));
    contrat4
        .getBeneficiaires()
        .get(0)
        .setDomaineDroits(List.of(mapDomaine("2023/01/01", "2023/06/30", "2023/06/30")));
    contrat5
        .getBeneficiaires()
        .get(0)
        .setDomaineDroits(List.of(mapDomaine("2023/01/01", "2023/06/30", "2023/06/30")));

    final AggregationResults<ContractTP> res =
        new AggregationResults<>(
            List.of(contrat, contrat2, contrat3, contrat4, contrat5), new Document());

    this.mockListBenefWithContract(res);

    this.mockOc();

    final UniqueAccessPointResponse response = this.uapV5Service.execute(requete);
    // La requête n'a pas de date de fin => Aucun contrat n'est ouvert après la date
    // de début demandée => on passe par les étapes de forçage
    // Directement par la step 2 : sélection des contrats ayant une date de fin
    // de validité inférieure à la date de soins puis sélection du contrat ayant la
    // date de fin de validité la plus grande => ici les contrats n°4 et n°5
    Assertions.assertEquals(2, response.getContracts().size());
    Assertions.assertEquals("4", response.getContracts().get(0).getNumber());
    Assertions.assertEquals("5", response.getContracts().get(1).getNumber());
    Assertions.assertTrue(response.getContracts().get(0).getIsForced());
    Assertions.assertTrue(response.getContracts().get(1).getIsForced());
  }

  @Test
  void testPAUForceShouldReturnContractSameContext() {
    final String startDate = "2024-07-15";
    final String endDate = "2024-07-15";

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
            "HOSP",
            "123456",
            null,
            null,
            true);

    final ContractTP contrat = this.contract.getContrat();
    contrat.setOrigineDeclaration(Constants.ORIGINE_DECLARATIONEVT);
    final ContractTP contrat2 = new ContractTP(contrat);

    contrat.setNumeroContrat("1");
    contrat2.setNumeroContrat("2");
    contrat
        .getBeneficiaires()
        .getFirst()
        .setDomaineDroits(
            List.of(
                mapDomaine("2024/01/01", "2023/12/31", "2024/12/31", false),
                mapDomaine("2025/01/01", "2024/12/31", "2025/12/31", false)));
    contrat2
        .getBeneficiaires()
        .getFirst()
        .setDomaineDroits(List.of(mapDomaine("2025/01/01", null, null)));

    final AggregationResults<ContractTP> res =
        new AggregationResults<>(List.of(contrat, contrat2), new Document());

    this.mockListBenefWithContract(res);

    this.mockOc();

    final UniqueAccessPointResponse response = this.uapV5Service.execute(requete);
    // Doit retourner le contrat 2 (contrat avec du ONLINE)
    Assertions.assertEquals(1, response.getContracts().size());
    Assertions.assertEquals("2", response.getContracts().getFirst().getNumber());
    Assertions.assertTrue(response.getContracts().getFirst().getIsForced());
    Assertions.assertNull(
        response
            .getContracts()
            .getFirst()
            .getInsured()
            .getRights()
            .getFirst()
            .getProducts()
            .getFirst()
            .getPeriod()
            .getEnd());
  }

  private DomaineDroitContractTP mapDomaine(
      String periodeDebut, String periodeFin, String periodeFinFermeture) {
    return mapDomaine(periodeDebut, periodeFin, periodeFinFermeture, true);
  }

  private DomaineDroitContractTP mapDomaine(
      String periodeDebut, String periodeFin, String periodeFinFermeture, boolean withOnline) {
    final DomaineDroitContractTP ddc = new DomaineDroitContractTP();
    ddc.setCode("HOSP");

    final List<PeriodeDroitContractTP> periodeDroitContractTPS = new ArrayList<>();
    final PeriodeDroitContractTP pdc1 = new PeriodeDroitContractTP();
    pdc1.setTypePeriode(TypePeriode.OFFLINE);
    pdc1.setPeriodeDebut(periodeDebut);
    pdc1.setPeriodeFin(periodeFin);
    pdc1.setPeriodeFinFermeture(periodeFinFermeture);
    periodeDroitContractTPS.add(pdc1);

    final PrioriteDroitContrat prioriteDroit = new PrioriteDroitContrat();
    prioriteDroit.setCode("01");
    prioriteDroit.setLibelle("01");
    prioriteDroit.setTypeDroit("01");
    prioriteDroit.setPrioriteBO("01");
    Periode periode = new Periode();
    periode.setDebut(periodeDebut);
    periode.setFin(periodeFin);
    prioriteDroit.setPeriodes(List.of(periode));

    if (withOnline) {
      final PeriodeDroitContractTP pdc1Online = new PeriodeDroitContractTP(pdc1);
      pdc1Online.setTypePeriode(TypePeriode.ONLINE);
      periodeDroitContractTPS.add(pdc1Online);
    }

    List<ConventionnementContrat> conventionnements = new ArrayList<>();
    ConventionnementContrat conventionnement1 = new ConventionnementContrat();
    ConventionnementContrat conventionnement2 = new ConventionnementContrat();
    TypeConventionnement typeConventionnement1 = new TypeConventionnement();
    TypeConventionnement typeConventionnement2 = new TypeConventionnement();
    typeConventionnement1.setCode("conventionCode1");
    typeConventionnement2.setCode("conventionCode2");
    conventionnement1.setPriorite(1);
    conventionnement1.setTypeConventionnement(typeConventionnement1);
    conventionnement2.setPriorite(2);
    conventionnement2.setTypeConventionnement(typeConventionnement2);
    conventionnements.add(conventionnement1);
    conventionnements.add(conventionnement2);

    Garantie garantie = new Garantie();
    garantie.setCodeAssureurGarantie("KLESIA_CARCEPT");
    garantie.setDateAdhesionCouverture("2019/01/01");
    garantie.setCodeGarantie("KC_PlatineComp");
    Produit produit = new Produit();
    produit.setCodeProduit("PlatineComplémentaire");
    produit.setCodeOffre("OFFER1");
    ReferenceCouverture referenceCouverture = new ReferenceCouverture();
    NaturePrestation naturePrestation = new NaturePrestation();
    naturePrestation.setNaturePrestation("HOSPITALISATION");
    naturePrestation.setPeriodesDroit(periodeDroitContractTPS);
    naturePrestation.setConventionnements(conventionnements);
    naturePrestation.setPrioritesDroit(List.of(prioriteDroit));
    referenceCouverture.setNaturesPrestation(List.of(naturePrestation));
    produit.setReferencesCouverture(List.of(referenceCouverture));
    garantie.setProduits(List.of(produit));
    ddc.setGaranties(List.of(garantie));
    return ddc;
  }
}
