package com.cegedim.next.serviceeligibility.core.bdd.service;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarationDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.data.DemandeInfoBeneficiaire;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeRechercheSegmentService;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.VisiodroitUtils;
import com.cegedim.next.serviceeligibility.core.business.declarant.service.DeclarantService;
import com.cegedim.next.serviceeligibility.core.business.declaration.service.DeclarationServiceImpl;
import com.cegedim.next.serviceeligibility.core.model.domain.Pilotage;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.*;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.soap.consultation.mapper.MapperContractToDeclarationDto;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeclarationServiceTest {

  @Autowired private DeclarationServiceImpl declarationServiceImpl;

  @Autowired private MapperContractToDeclarationDto mapperContractToDeclarationDto;

  @BeforeEach
  public void initTests() {
    prepareData();
  }

  private void prepareData() {
    Declarant declarant = new Declarant();
    declarant.setNumeroPrefectoral("123");

    Pilotage p = new Pilotage();

    declarant.setPilotages(List.of(p));

    DeclarantService declarantService = Mockito.mock(DeclarantService.class);
    Mockito.when(declarantService.findById(Mockito.any())).thenReturn(declarant);
    ReflectionTestUtils.setField(
        mapperContractToDeclarationDto, "declarantService", declarantService);
    ReflectionTestUtils.setField(
        declarationServiceImpl, "contratMapper", mapperContractToDeclarationDto);
  }

  private ContractTP getContratTP() {
    ContractTP contractTP = new ContractTP();
    contractTP.setIdDeclarant("123");
    contractTP.setNumeroAdherent("NA42");
    BeneficiaireContractTP beneficiaireContractTP = new BeneficiaireContractTP();
    beneficiaireContractTP.setDateNaissance("19840101");
    beneficiaireContractTP.setRangNaissance("1");
    beneficiaireContractTP.setNirBeneficiaire("NIR1");
    beneficiaireContractTP.setCleNirBeneficiaire("12");
    beneficiaireContractTP.setDateModification(LocalDateTime.now(ZoneOffset.UTC));

    DomaineDroitContractTP ddc = new DomaineDroitContractTP();
    ddc.setCode("HOSP");
    List<PeriodeDroitContractTP> pdcs = new ArrayList<>();
    PeriodeDroitContractTP pdc1 = new PeriodeDroitContractTP();
    pdc1.setPeriodeDebut("2021/01/01");
    pdc1.setPeriodeFin("2021/12/31");

    PeriodeDroitContractTP pdc2 = new PeriodeDroitContractTP();
    pdc2.setPeriodeDebut("2021/01/01");
    pdc2.setPeriodeFin("2042/12/31");

    pdcs.add(pdc1);
    pdcs.add(pdc2);
    Garantie garantie = new Garantie();
    garantie.setCodeGarantie("GAR");
    Produit produit = new Produit();
    produit.setCodeProduit("PROD");
    ReferenceCouverture referenceCouverture = new ReferenceCouverture();
    referenceCouverture.setReferenceCouverture("toto");
    NaturePrestation naturePrestation = new NaturePrestation();
    naturePrestation.setPeriodesDroit(pdcs);

    PrioriteDroitContrat prioriteDroitContrat = new PrioriteDroitContrat();
    Periode periode = new Periode();
    periode.setDebut("2021/01/01");
    periode.setFin("2021/12/31");
    prioriteDroitContrat.setPeriodes(List.of(periode));
    prioriteDroitContrat.setCode("01");
    naturePrestation.setPrioritesDroit(List.of(prioriteDroitContrat));

    RemboursementContrat remboursementContrat = new RemboursementContrat();
    remboursementContrat.setPeriodes(List.of(periode));
    remboursementContrat.setTauxRemboursement("01");
    naturePrestation.setRemboursements(List.of(remboursementContrat));

    referenceCouverture.setNaturesPrestation(List.of(naturePrestation));
    produit.setReferencesCouverture(List.of(referenceCouverture));
    garantie.setProduits(List.of(produit));
    ddc.setGaranties(List.of(garantie));
    beneficiaireContractTP.setDomaineDroits(List.of(ddc));
    contractTP.setBeneficiaires(List.of(beneficiaireContractTP));

    return contractTP;
  }

  @Test
  void should_checkValidite() {
    DemandeInfoBeneficiaire infoBenef = new DemandeInfoBeneficiaire();
    infoBenef.setTypeRechercheSegment(TypeRechercheSegmentService.LISTE_SEGMENT);
    infoBenef.setListeSegmentRecherche(List.of("HOSP"));
    infoBenef.setNumeroPrefectoral("123");
    infoBenef.setDateReference(
        DateUtils.parseDate(
            "2022-11-11", com.cegedim.next.serviceeligibility.core.utils.DateUtils.YYYY_MM_DD));
    infoBenef.setDateNaissance("19840101");
    infoBenef.setRangNaissance("1");
    infoBenef.setNirBeneficiaire("NIR1");
    infoBenef.setCleNirBneficiare("12");
    infoBenef.setNumeroAdherent("NA42");
    infoBenef.setProfondeurRecherche(TypeProfondeurRechercheService.AVEC_FORMULES);

    List<ContractTP> contratsEclates = new ArrayList<>();
    VisiodroitUtils.eclaterBeneficiairesParDomaine(
        List.of(getContratTP()), contratsEclates, false, infoBenef);

    List<DeclarationDto> declarationList =
        new ArrayList<>(
            declarationServiceImpl.mapperListeContratToDeclaration(
                infoBenef.getProfondeurRecherche(),
                contratsEclates,
                false,
                infoBenef.getNumeroPrefectoral(),
                false,
                infoBenef.getListeSegmentRecherche(),
                infoBenef.getSegmentRecherche(),
                infoBenef.getDateFin(),
                DateUtils.formatDate(infoBenef.getDateReference())));

    declarationServiceImpl.checkDomaineDroit(declarationList, Set.of("HOSP"));

    Assertions.assertTrue(
        declarationServiceImpl.checkValiditePeriodeDroit(
            declarationList, true, infoBenef.getDateReference(), infoBenef.getDateFin()));
  }
}
