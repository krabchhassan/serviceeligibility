package com.cegedim.next.serviceeligibility.core.business.consultationdroits.idb;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.features.consultationdroits.MapperContratToContractDto;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.services.GenerateContract;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfig.class)
public class IDBStepsTest {

  @Autowired private GenerateContract contract;

  @Autowired private IDBService idbService;

  @Autowired private MapperContratToContractDto mapperContractDto;

  private void updateContract1Exemple1(ContractTP contrat) {
    for (DomaineDroitContractTP domaineDroitContract :
        contrat.getBeneficiaires().get(0).getDomaineDroits()) {
      updateDomaineContrat(domaineDroitContract);
    }
  }

  private void updateContract1Exemple2(ContractTP contrat) {
    for (DomaineDroitContractTP domaineDroitContract :
        contrat.getBeneficiaires().get(0).getDomaineDroits()) {
      updateDomaineContratExemple2(domaineDroitContract);
    }
  }

  private void updateContract2(ContractTP contrat) {
    contrat.setNumeroContrat("contrat2");
    contrat.setNumeroAdherent("contrat2");
    updateDomaineContrat2(
        contrat.getBeneficiaires().get(0).getDomaineDroits().get(0), "2022/10/01", "2023/12/31");
    updateDomaineContrat2(
        contrat.getBeneficiaires().get(0).getDomaineDroits().get(1), "2022/10/01", "2023/12/31");
  }

  private void updateContract3(ContractTP contrat) {
    contrat.setNumeroContrat("contrat3");
    contrat.setNumeroAdherent("contrat3");
    updateDomaineContrat3(contrat.getBeneficiaires().get(0).getDomaineDroits().get(0));
    updateDomaineContrat3(contrat.getBeneficiaires().get(0).getDomaineDroits().get(1));
  }

  private void updateDomaineContrat(DomaineDroitContractTP domaine) {
    Garantie garantie1 = new Garantie();
    Garantie garantie11 = new Garantie();
    Produit produit1 = new Produit();
    Produit produit11 = new Produit();
    ReferenceCouverture referenceCouvertureNOSEL = new ReferenceCouverture();
    ReferenceCouverture referenceCouverture1 = new ReferenceCouverture();
    ReferenceCouverture referenceCouverture11 = new ReferenceCouverture();
    NaturePrestation naturePrestation1 = new NaturePrestation();
    NaturePrestation naturePrestation2 = new NaturePrestation();
    NaturePrestation naturePrestation3 = new NaturePrestation();
    PeriodeDroitContractTP periode1 = new PeriodeDroitContractTP();
    PeriodeDroitContractTP periode2 = new PeriodeDroitContractTP();
    PeriodeDroitContractTP periode3 = new PeriodeDroitContractTP();
    naturePrestation1.setPrioritesDroit(mapPrioriteDroit());
    periode1.setPeriodeDebut("2022/01/01");
    periode1.setPeriodeFin("2022/03/31");
    naturePrestation2.setPrioritesDroit(mapPrioriteDroit());
    periode2.setPeriodeDebut("2022/04/01");
    periode2.setPeriodeFin("2022/06/30");
    naturePrestation3.setPrioritesDroit(mapPrioriteDroit());
    periode3.setPeriodeDebut("2022/07/01");
    periode3.setPeriodeFin("2022/09/30");
    naturePrestation1.setPeriodesDroit(List.of(periode1));
    naturePrestation2.setPeriodesDroit(List.of(periode2));
    naturePrestation3.setPeriodesDroit(List.of(periode3));
    referenceCouvertureNOSEL.setReferenceCouverture("NOSEL");
    referenceCouvertureNOSEL.setNaturesPrestation(List.of(naturePrestation1));
    referenceCouverture1.setReferenceCouverture("1");
    referenceCouverture1.setNaturesPrestation(List.of(naturePrestation2));
    referenceCouverture11.setReferenceCouverture("11");
    referenceCouverture11.setNaturesPrestation(List.of(naturePrestation3));
    produit1.setReferencesCouverture(List.of(referenceCouvertureNOSEL, referenceCouverture1));
    produit11.setReferencesCouverture(List.of(referenceCouverture11));
    garantie1.setCodeGarantie("garantie 1");
    garantie1.setProduits(List.of(produit1));
    garantie11.setCodeGarantie("garantie 11");
    garantie11.setProduits(List.of(produit11));
    domaine.setGaranties(List.of(garantie1, garantie11));
  }

  private void updateDomaineContratExemple2(DomaineDroitContractTP domaine) {
    Garantie garantie1 = new Garantie();
    Produit produit1 = new Produit();
    ReferenceCouverture referenceCouvertureNOSEL = new ReferenceCouverture();
    ReferenceCouverture referenceCouverture1 = new ReferenceCouverture();
    ReferenceCouverture referenceCouverture11 = new ReferenceCouverture();
    NaturePrestation naturePrestation1 = new NaturePrestation();
    NaturePrestation naturePrestation2 = new NaturePrestation();
    NaturePrestation naturePrestation3 = new NaturePrestation();
    PeriodeDroitContractTP periode1 = new PeriodeDroitContractTP();
    PeriodeDroitContractTP periode2 = new PeriodeDroitContractTP();
    PeriodeDroitContractTP periode3 = new PeriodeDroitContractTP();
    PeriodeDroitContractTP periode4 = new PeriodeDroitContractTP();

    naturePrestation1.setPrioritesDroit(mapPrioriteDroit());
    periode1.setPeriodeDebut("2022/01/01");
    periode1.setPeriodeFin("2022/03/31");
    naturePrestation2.setPrioritesDroit(mapPrioriteDroit());
    periode2.setPeriodeDebut("2022/04/01");
    periode2.setPeriodeFin("2022/05/31");
    periode3.setPeriodeDebut("2022/06/02");
    periode3.setPeriodeFin("2022/06/30");
    naturePrestation3.setPrioritesDroit(mapPrioriteDroit());
    periode4.setPeriodeDebut("2022/07/01");
    periode4.setPeriodeFin("2022/09/30");
    naturePrestation1.setPeriodesDroit(List.of(periode1));
    naturePrestation2.setPeriodesDroit(List.of(periode2, periode3));
    naturePrestation3.setPeriodesDroit(List.of(periode4));

    referenceCouvertureNOSEL.setReferenceCouverture("NOSEL");
    referenceCouvertureNOSEL.setNaturesPrestation(List.of(naturePrestation1));
    referenceCouverture1.setReferenceCouverture("1");
    referenceCouverture1.setNaturesPrestation(List.of(naturePrestation2));
    referenceCouverture11.setReferenceCouverture("11");
    referenceCouverture11.setNaturesPrestation(List.of(naturePrestation3));

    produit1.setReferencesCouverture(
        List.of(referenceCouvertureNOSEL, referenceCouverture1, referenceCouverture11));
    garantie1.setCodeGarantie("garantie 1");
    garantie1.setProduits(List.of(produit1));
    domaine.setGaranties(List.of(garantie1));
  }

  public static List<PrioriteDroitContrat> mapPrioriteDroit() {
    final PrioriteDroitContrat pD = new PrioriteDroitContrat();
    pD.setCode("01");
    pD.setLibelle("01");
    pD.setTypeDroit("01");
    pD.setPrioriteBO("01");
    return List.of(pD);
  }

  private void updateDomaineContrat2(
      DomaineDroitContractTP domaine, String periodeDebut, String periodeFin) {
    Garantie garantie1 = new Garantie();
    Produit produit1 = new Produit();
    ReferenceCouverture referenceCouverture1 = new ReferenceCouverture();
    NaturePrestation naturePrestation1 = new NaturePrestation();
    PeriodeDroitContractTP periode1 = new PeriodeDroitContractTP();

    naturePrestation1.setPrioritesDroit(mapPrioriteDroit());
    periode1.setPeriodeDebut(periodeDebut);
    periode1.setPeriodeFin(periodeFin);
    naturePrestation1.setPeriodesDroit(List.of(periode1));
    referenceCouverture1.setReferenceCouverture("2");
    referenceCouverture1.setNaturesPrestation(List.of(naturePrestation1));
    produit1.setReferencesCouverture(List.of(referenceCouverture1));
    garantie1.setCodeGarantie("garantie 2");
    garantie1.setProduits(List.of(produit1));
    domaine.setGaranties(List.of(garantie1));
  }

  private void updateDomaineContrat3(DomaineDroitContractTP domaine) {
    Garantie garantie1 = new Garantie();
    Produit produit1 = new Produit();
    ReferenceCouverture referenceCouverture1 = new ReferenceCouverture();
    NaturePrestation naturePrestation1 = new NaturePrestation();
    PeriodeDroitContractTP periode1 = new PeriodeDroitContractTP();

    naturePrestation1.setPrioritesDroit(mapPrioriteDroit());
    periode1.setPeriodeDebut("2022/10/02");
    periode1.setPeriodeFin("2024/12/31");
    naturePrestation1.setPeriodesDroit(List.of(periode1));
    referenceCouverture1.setReferenceCouverture("3");
    referenceCouverture1.setNaturesPrestation(List.of(naturePrestation1));
    produit1.setReferencesCouverture(List.of(referenceCouverture1));
    garantie1.setCodeGarantie("garantie 3");
    garantie1.setProduits(List.of(produit1));
    domaine.setGaranties(List.of(garantie1));
  }

  @Test
  void exemple1_periodesContigues() {
    ContractTP contrat = this.contract.getContrat();
    updateContract1Exemple1(contrat);
    ContractTP contrat2 = this.contract.getContrat();
    updateContract2(contrat2);
    // Interrogation n°1 sur la période 05/04/2022 - 20/07/2022
    Pair<String, Periode> res =
        idbService.getPeriodesAndContractNumForIDB(
            mapperContractDto.entityListToDtoList(List.of(contrat, contrat2), "2022-04-05"),
            DateUtils.parseDate("2022-04-05", DateUtils.YYYY_MM_DD),
            DateUtils.parseDate("2022-07-20", DateUtils.YYYY_MM_DD));
    Assertions.assertEquals("1213", res.getFirst());
    Assertions.assertEquals("2022/04/05", res.getSecond().getDebut());
    Assertions.assertEquals("2022/07/20", res.getSecond().getFin());

    // Interrogation n°2 sur la période 15/12/2021 - 20/11/2022
    res =
        idbService.getPeriodesAndContractNumForIDB(
            mapperContractDto.entityListToDtoList(List.of(contrat, contrat2), "2021-12-15"),
            DateUtils.parseDate("2021-12-15", DateUtils.YYYY_MM_DD),
            DateUtils.parseDate("2022-11-20", DateUtils.YYYY_MM_DD));
    Assertions.assertEquals("1213", res.getFirst());
    Assertions.assertEquals("2022/01/01", res.getSecond().getDebut());
    Assertions.assertEquals("2022/09/30", res.getSecond().getFin());
  }

  @Test
  void exemple2_periodeAvecTrou() {
    ContractTP contrat = this.contract.getContrat();
    updateContract1Exemple2(contrat);
    ContractTP contrat2 = this.contract.getContrat();
    updateContract2(contrat2);
    // Appel IDB sur la période 05/04/2022 - 20/07/2022
    Pair<String, Periode> res =
        idbService.getPeriodesAndContractNumForIDB(
            mapperContractDto.entityListToDtoList(List.of(contrat, contrat2), "2022-04-05"),
            DateUtils.parseDate("2022-04-05", DateUtils.YYYY_MM_DD),
            DateUtils.parseDate("2022-07-20", DateUtils.YYYY_MM_DD));
    Assertions.assertEquals("1213", res.getFirst());
    Assertions.assertEquals("2022/04/05", res.getSecond().getDebut());
    Assertions.assertEquals("2022/05/31", res.getSecond().getFin());
  }

  @Test
  void exemple3_interrogation_periodeFin_nonCouverte() {
    ContractTP contrat = this.contract.getContrat();
    updateContract1Exemple2(contrat);
    // Appel IDB sur la période 15/08/2022 - 15/10/2022
    Pair<String, Periode> res =
        idbService.getPeriodesAndContractNumForIDB(
            mapperContractDto.entityListToDtoList(List.of(contrat), "2022-08-15"),
            DateUtils.parseDate("2022-08-15", DateUtils.YYYY_MM_DD),
            DateUtils.parseDate("2022-10-15", DateUtils.YYYY_MM_DD));
    Assertions.assertEquals("1213", res.getFirst());
    Assertions.assertEquals("2022/08/15", res.getSecond().getDebut());
    Assertions.assertEquals("2022/09/30", res.getSecond().getFin());
  }

  @Test
  void interrogation_plusieurs_contrats() {
    ContractTP contrat = this.contract.getContrat();
    updateContract1Exemple2(contrat);
    ContractTP contract1 = this.contract.getContrat();
    updateContract1Exemple1(contract1);
    contract1.setNumeroContrat("contrat2");
    contract1.getBeneficiaires().get(0).getDomaineDroits().get(0).setCode("AUDI");
    contract1.getBeneficiaires().get(0).getDomaineDroits().get(1).setCode("DENT");
    // Appel IDB sur la période 05/04/2022 - 20/07/2022 -> deux contrats couvrent
    // cette période
    // les deux contrats couvrent la date de début demandée donc on sélectionne le
    // contrat ayant la plus grande date de fin
    Pair<String, Periode> res =
        idbService.getPeriodesAndContractNumForIDB(
            mapperContractDto.entityListToDtoList(List.of(contrat, contract1), "2022-04-05"),
            DateUtils.parseDate("2022-04-05", DateUtils.YYYY_MM_DD),
            DateUtils.parseDate("2022-07-20", DateUtils.YYYY_MM_DD));
    Assertions.assertEquals("contrat2", res.getFirst());
    Assertions.assertEquals("2022/04/05", res.getSecond().getDebut());
    Assertions.assertEquals("2022/07/20", res.getSecond().getFin());
  }

  @Test
  void interrogation_plusieurs_contrats2() {
    ContractTP contrat = this.contract.getContrat();
    // Contrat qui couvre du 2022/10/02 au 2024/12/31
    updateContract3(contrat);
    ContractTP contrat2 = this.contract.getContrat();
    // Contrat qui couvre du 2022/10/01 au 2023/12/31
    updateContract2(contrat2);
    // Les deux contrats couvrent une partie de la période demandée => on prend le
    // contrat qui couvre au plus tôt, le début de la période demandée
    Pair<String, Periode> res =
        idbService.getPeriodesAndContractNumForIDB(
            mapperContractDto.entityListToDtoList(List.of(contrat, contrat2), "2022-09-01"),
            DateUtils.parseDate("2022-09-01", DateUtils.YYYY_MM_DD),
            DateUtils.parseDate("2023-01-01", DateUtils.YYYY_MM_DD));
    Assertions.assertEquals("contrat2", res.getFirst());
    Assertions.assertEquals("2022/10/01", res.getSecond().getDebut());
    Assertions.assertEquals("2023/01/01", res.getSecond().getFin());
  }

  @Test
  void interrogation_plusieurs_contrats_selection_plus_petit_numeroContrat() {
    ContractTP contrat = this.contract.getContrat();
    // Contrat qui couvre du 2022/10/01 au 2023/12/31
    updateContract2(contrat);
    contrat.setNumeroContrat("contrat1");
    contrat.setNumeroAdherent("contrat1");
    ContractTP contrat2 = this.contract.getContrat();
    // Contrat qui couvre du 2022/10/01 au 2023/12/31
    updateContract2(contrat2);
    // Les deux contrats couvrent une partie de la période demandée
    // => ont la même date de début, la même date de fin : sélection du contrat
    // ayant le plus petit numéro
    Pair<String, Periode> res =
        idbService.getPeriodesAndContractNumForIDB(
            mapperContractDto.entityListToDtoList(List.of(contrat, contrat2), "2022-09-01"),
            DateUtils.parseDate("2022-09-01", DateUtils.YYYY_MM_DD),
            DateUtils.parseDate("2023-01-01", DateUtils.YYYY_MM_DD));
    Assertions.assertEquals("contrat1", res.getFirst());
    Assertions.assertEquals("2022/10/01", res.getSecond().getDebut());
    Assertions.assertEquals("2023/01/01", res.getSecond().getFin());
  }

  @Test
  void interrogation_plusieurs_contrats_avec_date_fin_nulle() {
    ContractTP contrat = this.contract.getContrat();
    // Contrat qui couvre du 2022/10/01 à l'infini
    contrat.setNumeroContrat("contrat1");
    contrat.setNumeroAdherent("contrat1");
    updateDomaineContrat2(
        contrat.getBeneficiaires().get(0).getDomaineDroits().get(0), "2022/10/01", null);
    updateDomaineContrat2(
        contrat.getBeneficiaires().get(0).getDomaineDroits().get(1), "2022/10/01", null);
    ContractTP contrat2 = this.contract.getContrat();
    // Contrat qui couvre du 2022/10/01 à l'infini
    contrat2.setNumeroContrat("contrat3");
    contrat2.setNumeroAdherent("contrat3");
    updateDomaineContrat2(
        contrat2.getBeneficiaires().get(0).getDomaineDroits().get(0), "2022/10/01", null);
    updateDomaineContrat2(
        contrat2.getBeneficiaires().get(0).getDomaineDroits().get(1), "2022/10/01", null);
    ContractTP contrat3 = this.contract.getContrat();
    // Contrat qui couvre du 2022/10/01 au 2023/12/31
    updateContract2(contrat3);
    // Les deux contrats couvrent une partie de la période demandée
    // => ont la même date de début, la même date de fin : sélection du contrat
    // ayant le plus petit numéro
    Pair<String, Periode> res =
        idbService.getPeriodesAndContractNumForIDB(
            mapperContractDto.entityListToDtoList(
                List.of(contrat, contrat2, contrat3), "2022-09-01"),
            DateUtils.parseDate("2022-09-01", DateUtils.YYYY_MM_DD),
            DateUtils.parseDate("2023-01-01", DateUtils.YYYY_MM_DD));
    Assertions.assertEquals("contrat1", res.getFirst());
    Assertions.assertEquals("2022/10/01", res.getSecond().getDebut());
    Assertions.assertEquals("2023/01/01", res.getSecond().getFin());
  }

  @Test
  void interrogation_plusieurs_contrats_requete_et_contrat_periode_fin_nulle() {
    ContractTP contrat = this.contract.getContrat();
    // Contrat qui couvre du 2022/10/01 à l'infini
    contrat.setNumeroContrat("contrat1");
    contrat.setNumeroAdherent("contrat1");
    updateDomaineContrat2(
        contrat.getBeneficiaires().get(0).getDomaineDroits().get(0), "2022/10/01", null);
    updateDomaineContrat2(
        contrat.getBeneficiaires().get(0).getDomaineDroits().get(1), "2022/10/01", null);
    ContractTP contrat2 = this.contract.getContrat();
    // Contrat qui couvre du 2022/10/01 à l'infini
    contrat2.setNumeroContrat("contrat3");
    contrat2.setNumeroAdherent("contrat3");
    updateDomaineContrat2(
        contrat2.getBeneficiaires().get(0).getDomaineDroits().get(0), "2022/10/01", null);
    updateDomaineContrat2(
        contrat2.getBeneficiaires().get(0).getDomaineDroits().get(1), "2022/10/01", null);
    ContractTP contrat3 = this.contract.getContrat();
    // Contrat qui couvre du 2022/10/01 au 2023/12/31
    updateContract2(contrat3);
    // Les deux contrats couvrent une partie de la période demandée
    // => ont la même date de début, la même date de fin : sélection du contrat
    // ayant le plus petit numéro
    Pair<String, Periode> res =
        idbService.getPeriodesAndContractNumForIDB(
            mapperContractDto.entityListToDtoList(
                List.of(contrat, contrat2, contrat3), "2022-09-01"),
            DateUtils.parseDate("2022-09-01", DateUtils.YYYY_MM_DD),
            null);
    Assertions.assertEquals("contrat1", res.getFirst());
    Assertions.assertEquals("2022/10/01", res.getSecond().getDebut());
    Assertions.assertNull(res.getSecond().getFin());
  }

  @Test
  void interrogation_plusieurs_contrats_requete_periode_fin_nulle() {
    ContractTP contrat = this.contract.getContrat();
    // Contrat qui couvre du 2022/10/01 au 2023/12/31
    contrat.setNumeroContrat("contrat1");
    contrat.setNumeroAdherent("contrat1");
    updateDomaineContrat2(
        contrat.getBeneficiaires().get(0).getDomaineDroits().get(0), "2022/10/01", "2023/12/31");
    updateDomaineContrat2(
        contrat.getBeneficiaires().get(0).getDomaineDroits().get(1), "2022/10/01", "2023/12/31");
    ContractTP contrat2 = this.contract.getContrat();
    // Contrat qui couvre du 2022/10/01 au 2023/11/30
    contrat2.setNumeroContrat("contrat3");
    contrat2.setNumeroAdherent("contrat3");
    updateDomaineContrat2(
        contrat2.getBeneficiaires().get(0).getDomaineDroits().get(0), "2022/10/01", "2023/11/30");
    updateDomaineContrat2(
        contrat2.getBeneficiaires().get(0).getDomaineDroits().get(1), "2022/10/01", "2023/11/30");
    ContractTP contrat3 = this.contract.getContrat();
    // Contrat qui couvre du 2022/10/01 au 2023/12/31
    updateContract2(contrat3);
    // Les deux contrats couvrent une partie de la période demandée
    // => ont la même date de début, la même date de fin : sélection du contrat
    // ayant le plus petit numéro
    Pair<String, Periode> res =
        idbService.getPeriodesAndContractNumForIDB(
            mapperContractDto.entityListToDtoList(
                List.of(contrat, contrat2, contrat3), "2022-09-01"),
            DateUtils.parseDate("2022-09-01", DateUtils.YYYY_MM_DD),
            null);
    Assertions.assertEquals("contrat1", res.getFirst());
    Assertions.assertEquals("2022/10/01", res.getSecond().getDebut());
    Assertions.assertEquals("2023/12/31", res.getSecond().getFin());
  }
}
