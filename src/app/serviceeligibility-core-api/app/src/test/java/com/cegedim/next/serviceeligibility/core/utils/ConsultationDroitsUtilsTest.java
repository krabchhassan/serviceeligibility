package com.cegedim.next.serviceeligibility.core.utils;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ContractDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.contract.NaturePrestationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.contract.ProduitDto;
import com.cegedim.next.serviceeligibility.core.features.consultationdroits.MapperContratToContractDto;
import com.cegedim.next.serviceeligibility.core.features.utils.ConsultationDroitsUtils;
import com.cegedim.next.serviceeligibility.core.model.domain.TypeConventionnement;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.services.GenerateContract;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfig.class)
class ConsultationDroitsUtilsTest {

  @Autowired private GenerateContract contract;

  @Autowired private MapperContratToContractDto mapperContractDto;

  private void updateDomaineContrat(DomaineDroitContractTP domaine, String year) {
    Garantie garantie1 = new Garantie();
    Garantie garantie11 = new Garantie();
    Garantie garantie111 = new Garantie();
    Produit produit1 = new Produit();
    Produit produit11 = new Produit();
    Produit produit111 = new Produit();
    ReferenceCouverture referenceCouvertureNOSEL = new ReferenceCouverture();
    ReferenceCouverture referenceCouverture1 = new ReferenceCouverture();
    ReferenceCouverture referenceCouverture11 = new ReferenceCouverture();
    NaturePrestation naturePrestation1 = new NaturePrestation();
    NaturePrestation naturePrestation2 = new NaturePrestation();
    NaturePrestation naturePrestation3 = new NaturePrestation();
    PeriodeDroitContractTP periode1 = new PeriodeDroitContractTP();
    PeriodeDroitContractTP periode2 = new PeriodeDroitContractTP();
    PeriodeDroitContractTP periode3 = new PeriodeDroitContractTP();
    String dateDebut = year + "/01/01";
    String dateFin = year + "/03/31";
    naturePrestation1.setPrioritesDroit(
        mapPrioriteDroit(new ArrayList<>(List.of(new Periode(dateDebut, dateFin)))));
    periode1.setPeriodeDebut(dateDebut);
    periode1.setPeriodeFin(dateFin);
    dateDebut = year + "/04/01";
    dateFin = year + "/06/30";
    List<Periode> subPeriodes =
        List.of(new Periode("2020/01/01", "2020/12/31"), new Periode(dateDebut, dateFin));
    naturePrestation2.setPrioritesDroit(mapPrioriteDroit(new ArrayList<>(subPeriodes)));
    naturePrestation2.setConventionnements(mapConventionnements(new ArrayList<>(subPeriodes)));
    naturePrestation2.setPrestations(mapPrestations(new ArrayList<>(subPeriodes)));
    naturePrestation2.setRemboursements(mapRemboursements(new ArrayList<>(subPeriodes)));
    periode2.setPeriodeDebut(dateDebut);
    periode2.setPeriodeFin(dateFin);
    List<PrioriteDroitContrat> prioriteDroitContrats =
        mapPrioriteDroit(
            new ArrayList<>(
                List.of(
                    new Periode(dateDebut, dateFin),
                    new Periode(year + "/10/01", year + "/12/31"))));
    prioriteDroitContrats.addAll(
        mapPrioriteDroit(new ArrayList<>(List.of(new Periode(year + "/10/01", year + "/12/31")))));
    naturePrestation3.setPrioritesDroit(prioriteDroitContrats);
    dateDebut = year + "/07/01";
    dateFin = year + "/09/30";
    periode3.setPeriodeDebut(dateDebut);
    periode3.setPeriodeFin(dateFin);
    naturePrestation1.setPeriodesDroit(new ArrayList<>(List.of(periode1)));
    naturePrestation2.setPeriodesDroit(new ArrayList<>(List.of(periode2)));
    naturePrestation3.setPeriodesDroit(new ArrayList<>(List.of(periode3)));
    referenceCouvertureNOSEL.setReferenceCouverture("NOSEL");
    referenceCouvertureNOSEL.setNaturesPrestation(new ArrayList<>(List.of(naturePrestation1)));
    referenceCouverture1.setReferenceCouverture("1");
    referenceCouverture1.setNaturesPrestation(new ArrayList<>(List.of(naturePrestation2)));
    referenceCouverture11.setReferenceCouverture("11");
    referenceCouverture11.setNaturesPrestation(new ArrayList<>(List.of(naturePrestation3)));
    produit1.setReferencesCouverture(
        new ArrayList<>(List.of(referenceCouvertureNOSEL, referenceCouverture1)));
    produit11.setReferencesCouverture(new ArrayList<>(List.of(referenceCouverture11)));
    garantie1.setCodeGarantie("garantie 1");
    garantie1.setProduits(new ArrayList<>(List.of(produit1)));
    garantie11.setCodeGarantie("garantie 11");
    garantie11.setProduits(new ArrayList<>(List.of(produit11)));
    garantie111.setProduits(new ArrayList<>(List.of(produit111)));
    domaine.setGaranties(
        new ArrayList<>(Arrays.asList(garantie1, garantie11, new Garantie(), garantie111)));
  }

  private static List<PrioriteDroitContrat> mapPrioriteDroit(List<Periode> periodes) {
    final PrioriteDroitContrat pD = new PrioriteDroitContrat();
    pD.setCode("01");
    pD.setLibelle("01");
    pD.setTypeDroit("01");
    pD.setPrioriteBO("01");
    pD.setPeriodes(periodes);
    return new ArrayList<>(List.of(pD));
  }

  private static List<ConventionnementContrat> mapConventionnements(List<Periode> periodes) {
    final ConventionnementContrat conventionnement = new ConventionnementContrat();
    final TypeConventionnement typeConventionnement = new TypeConventionnement();
    typeConventionnement.setCode("IS");
    typeConventionnement.setLibelle("IS");
    conventionnement.setTypeConventionnement(typeConventionnement);
    conventionnement.setPeriodes(periodes);
    return new ArrayList<>(List.of(conventionnement));
  }

  private static List<RemboursementContrat> mapRemboursements(List<Periode> periodes) {
    final RemboursementContrat remboursementContrat = new RemboursementContrat();
    remboursementContrat.setTauxRemboursement("100");
    remboursementContrat.setUniteTauxRemboursement("TA");
    remboursementContrat.setPeriodes(periodes);
    return new ArrayList<>(List.of(remboursementContrat));
  }

  private static List<PrestationContrat> mapPrestations(List<Periode> periodes) {
    final PrestationContrat prestationContrat = new PrestationContrat();
    prestationContrat.setCode("DEF");
    prestationContrat.setPeriodes(periodes);
    return new ArrayList<>(List.of(prestationContrat));
  }

  @Test
  void testFilterContractsPeriodsNotOverlappingRequest() {
    ContractTP contrat = this.contract.getContrat();
    ContractTP contrat2 = this.contract.getContrat();

    updateDomaineContrat(contrat.getBeneficiaires().get(0).getDomaineDroits().get(0), "2022");
    updateDomaineContrat(contrat2.getBeneficiaires().get(0).getDomaineDroits().get(0), "2023");
    updateDomaineContrat(contrat.getBeneficiaires().get(0).getDomaineDroits().get(1), "2022");
    updateDomaineContrat(contrat2.getBeneficiaires().get(0).getDomaineDroits().get(1), "2023");

    List<ContractDto> contractDtoList =
        mapperContractDto.entityListToDtoList(
            new ArrayList<>(Arrays.asList(contrat, contrat2)), "2022-04-05");
    ConsultationDroitsUtils.filterContractsPeriodsNotOverlappingRequest(
        contractDtoList, "2022-04-05", "2022-07-20");

    // 1 contrat supprimé
    Assertions.assertEquals(1, contractDtoList.size());
    Assertions.assertEquals(
        2, contractDtoList.get(0).getDomaineDroits().get(0).getGaranties().size());
    List<ProduitDto> produitDtos =
        contractDtoList.get(0).getDomaineDroits().get(0).getGaranties().get(0).getProduits();
    Assertions.assertEquals(1, produitDtos.size());

    // La referenceCouverture NOSEL est supprimée
    Assertions.assertEquals(1, produitDtos.get(0).getReferencesCouverture().size());
    Assertions.assertEquals(
        "1", produitDtos.get(0).getReferencesCouverture().get(0).getReferenceCouverture());
    Assertions.assertEquals(
        1, produitDtos.get(0).getReferencesCouverture().get(0).getNaturesPrestation().size());

    List<NaturePrestationDto> naturePrestationDtos =
        produitDtos.get(0).getReferencesCouverture().get(0).getNaturesPrestation();

    // Les différentes sous-périodes ne couvrant pas la période demandée sont
    // supprimées
    Assertions.assertEquals(1, naturePrestationDtos.get(0).getPrioritesDroit().size());
    Assertions.assertEquals(
        1, naturePrestationDtos.get(0).getPrioritesDroit().get(0).getPeriodes().size());
    Assertions.assertEquals(
        "2022/04/01",
        naturePrestationDtos.get(0).getPrioritesDroit().get(0).getPeriodes().get(0).getDebut());
    Assertions.assertEquals(
        "2022/06/30",
        naturePrestationDtos.get(0).getPrioritesDroit().get(0).getPeriodes().get(0).getFin());
    Assertions.assertEquals(1, naturePrestationDtos.get(0).getConventionnements().size());
    Assertions.assertEquals(
        1, naturePrestationDtos.get(0).getConventionnements().get(0).getPeriodes().size());
    Assertions.assertEquals(
        "2022/04/01",
        naturePrestationDtos.get(0).getConventionnements().get(0).getPeriodes().get(0).getDebut());
    Assertions.assertEquals(
        "2022/06/30",
        naturePrestationDtos.get(0).getConventionnements().get(0).getPeriodes().get(0).getFin());
    Assertions.assertEquals(1, naturePrestationDtos.get(0).getRemboursements().size());
    Assertions.assertEquals(
        1, naturePrestationDtos.get(0).getRemboursements().get(0).getPeriodes().size());
    Assertions.assertEquals(
        "2022/04/01",
        naturePrestationDtos.get(0).getRemboursements().get(0).getPeriodes().get(0).getDebut());
    Assertions.assertEquals(
        "2022/06/30",
        naturePrestationDtos.get(0).getRemboursements().get(0).getPeriodes().get(0).getFin());
    Assertions.assertEquals(1, naturePrestationDtos.get(0).getPrestations().size());
    Assertions.assertEquals(
        1, naturePrestationDtos.get(0).getPrestations().get(0).getPeriodes().size());
    Assertions.assertEquals(
        "2022/04/01",
        naturePrestationDtos.get(0).getPrestations().get(0).getPeriodes().get(0).getDebut());
    Assertions.assertEquals(
        "2022/06/30",
        naturePrestationDtos.get(0).getPrestations().get(0).getPeriodes().get(0).getFin());
    Assertions.assertEquals(1, naturePrestationDtos.get(0).getPeriodesDroit().size());
    Assertions.assertEquals(
        "2022/04/01", naturePrestationDtos.get(0).getPeriodesDroit().get(0).getPeriodeDebut());
    Assertions.assertEquals(
        "2022/06/30", naturePrestationDtos.get(0).getPeriodesDroit().get(0).getPeriodeFin());

    naturePrestationDtos =
        contractDtoList
            .get(0)
            .getDomaineDroits()
            .get(0)
            .getGaranties()
            .get(1)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation();
    Assertions.assertEquals(1, naturePrestationDtos.get(0).getPrioritesDroit().size());
    Assertions.assertEquals(
        1, naturePrestationDtos.get(0).getPrioritesDroit().get(0).getPeriodes().size());
    Assertions.assertEquals(
        "2022/04/01",
        naturePrestationDtos.get(0).getPrioritesDroit().get(0).getPeriodes().get(0).getDebut());
    Assertions.assertEquals(
        "2022/06/30",
        naturePrestationDtos.get(0).getPrioritesDroit().get(0).getPeriodes().get(0).getFin());
  }
}
