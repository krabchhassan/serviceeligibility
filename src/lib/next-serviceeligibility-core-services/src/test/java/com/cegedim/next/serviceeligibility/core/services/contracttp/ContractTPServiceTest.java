package com.cegedim.next.serviceeligibility.core.services.contracttp;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.dao.ContractDao;
import com.cegedim.next.serviceeligibility.core.model.domain.Contrat;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.*;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.PeriodeSuspensionDeclaration;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class ContractTPServiceTest {

  @Autowired ObjectMapper objectMapper;

  @Autowired ContractTPService service;

  @MockBean ContractDao contratDao;

  private Object savedObject = null;

  private void processMessageFromJson(String fileName) throws IOException {
    String filePath = "src/test/resources/declarations/";
    String declarationAsString = new String(Files.readAllBytes(Paths.get(filePath + fileName)));
    processMessage(declarationAsString);
  }

  private void processMessage(String json) throws JsonProcessingException {
    Declaration declaration = objectMapper.readValue(json, Declaration.class);
    service.processDeclaration(declaration);
  }

  private void assertPeriodEquals(
      PeriodeDroitContractTP periodeAssert, PeriodeDroitContractTP periodeToTest) {
    Assertions.assertEquals(periodeAssert.getPeriodeDebut(), periodeToTest.getPeriodeDebut());
    Assertions.assertEquals(periodeAssert.getPeriodeFin(), periodeToTest.getPeriodeFin());
    Assertions.assertEquals(periodeAssert.getTypePeriode(), periodeToTest.getTypePeriode());
    Assertions.assertEquals(
        periodeAssert.getPeriodeFinFermeture(), periodeToTest.getPeriodeFinFermeture());
  }

  @BeforeEach
  public void before() {
    Mockito.doAnswer(
            invocation -> {
              savedObject = invocation.getArgument(0);
              return savedObject;
            })
        .when(contratDao)
        .saveContract(Mockito.any());

    Mockito.doAnswer(invocation -> savedObject)
        .when(contratDao)
        .getContract(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

    Mockito.doAnswer(
            invocation -> {
              savedObject = invocation.getArgument(0);
              return savedObject;
            })
        .when(contratDao)
        .saveContract(Mockito.any(), Mockito.any());

    Mockito.doAnswer(invocation -> savedObject)
        .when(contratDao)
        .getContract(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any());

    Mockito.doAnswer(
            invocation -> {
              return savedObject = null;
            })
        .when(contratDao)
        .deleteContract(Mockito.any(), Mockito.any());
  }

  @Test
  void declarationsWithoutContractThenUpdate() throws IOException {
    processMessageFromJson("declarationA1.json");

    ContractTP contrat1 = contratDao.getContract("0000000002", "Contrat1", "15211521");
    Assertions.assertNotNull(contrat1);
    Assertions.assertEquals(false, contrat1.getIsContratCMU());
    Assertions.assertEquals("", contrat1.getNumeroAdherentComplet());
    Assertions.assertEquals("1", contrat1.getBeneficiaires().get(0).getRangAdministratif());
    Assertions.assertEquals("IS", contrat1.getTypeConvention());

    processMessageFromJson("declarationA2.json");

    ContractTP contrat2 = contratDao.getContract("0000000002", "Contrat1", "15211521");
    Assertions.assertNotNull(contrat2);
    Assertions.assertEquals(contrat1.get_id(), contrat2.get_id());

    // even if different, these datas are not updated
    Assertions.assertEquals("", contrat2.getNumeroAdherentComplet());

    // this is updated
    Assertions.assertEquals("SP", contrat2.getTypeConvention());
    Assertions.assertEquals("2", contrat2.getBeneficiaires().get(0).getRangAdministratif());
  }

  @Test
  void updateDomainsAndPeriods() throws IOException {
    processMessageFromJson("declarationB1.json");

    ContractTP contrat1 = contratDao.getContract("0000000002", "Contrat1", "15211521");
    Assertions.assertNotNull(contrat1);

    List<DomaineDroitContractTP> domaineDroits1 =
        contrat1.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(1, domaineDroits1.size());
    Assertions.assertEquals("OPTI", domaineDroits1.get(0).getCode());
    Assertions.assertEquals(3, domaineDroits1.get(0).getGaranties().size());

    PeriodeDroitContractTP contrat1periode1 =
        domaineDroits1
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
            .get(0);
    PeriodeDroitContractTP periodeAssert_contrat1periode1 = new PeriodeDroitContractTP();
    periodeAssert_contrat1periode1.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat1periode1.setPeriodeDebut("2015/01/01");
    periodeAssert_contrat1periode1.setPeriodeFin("2050/12/31");
    assertPeriodEquals(periodeAssert_contrat1periode1, contrat1periode1);

    PeriodeDroitContractTP contrat1periode2 =
        domaineDroits1
            .get(0)
            .getGaranties()
            .get(1)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(0);
    PeriodeDroitContractTP periodeAssert_contrat1periode2 = new PeriodeDroitContractTP();
    periodeAssert_contrat1periode2.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat1periode2.setPeriodeDebut("2015/01/01");
    periodeAssert_contrat1periode2.setPeriodeFin("2050/12/31");
    assertPeriodEquals(periodeAssert_contrat1periode2, contrat1periode2);

    PeriodeDroitContractTP contrat1periode3 =
        domaineDroits1
            .get(0)
            .getGaranties()
            .get(2)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(0);
    PeriodeDroitContractTP periodeAssert_contrat1periode3 = new PeriodeDroitContractTP();
    periodeAssert_contrat1periode3.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat1periode3.setPeriodeDebut("2015/01/01");
    periodeAssert_contrat1periode3.setPeriodeFin("2050/12/31");
    assertPeriodEquals(periodeAssert_contrat1periode3, contrat1periode3);

    processMessageFromJson("declarationB2.json");

    ContractTP contrat2 = contratDao.getContract("0000000002", "Contrat1", "15211521");
    Assertions.assertNotNull(contrat2);
    Assertions.assertEquals(contrat1.get_id(), contrat2.get_id());

    List<DomaineDroitContractTP> domaineDroits2 =
        contrat2.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(2, domaineDroits2.size());
    Assertions.assertEquals("OPTI", domaineDroits2.get(0).getCode());
    Assertions.assertEquals("DENT", domaineDroits2.get(1).getCode());
    Assertions.assertEquals(3, domaineDroits2.get(1).getGaranties().size());

    PeriodeDroitContractTP contrat2periode1 =
        domaineDroits2
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
            .get(0);
    PeriodeDroitContractTP periodeAssert_contrat2periode1 = new PeriodeDroitContractTP();
    periodeAssert_contrat2periode1.setTypePeriode(TypePeriode.ONLINE);
    periodeAssert_contrat2periode1.setPeriodeDebut("2015/01/01");
    periodeAssert_contrat2periode1.setPeriodeFin(null);
    assertPeriodEquals(periodeAssert_contrat2periode1, contrat2periode1);

    PeriodeDroitContractTP contrat2periode2 =
        domaineDroits2
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
            .get(1);
    PeriodeDroitContractTP periodeAssert_contrat2periode2 = new PeriodeDroitContractTP();
    periodeAssert_contrat2periode2.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat2periode2.setPeriodeDebut("2015/01/01");
    periodeAssert_contrat2periode2.setPeriodeFin("2050/12/31");
    assertPeriodEquals(periodeAssert_contrat2periode2, contrat2periode2);

    PeriodeDroitContractTP contrat2periode3 =
        domaineDroits2
            .get(1)
            .getGaranties()
            .get(1)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(0);
    PeriodeDroitContractTP periodeAssert_contrat2periode3 = new PeriodeDroitContractTP();
    periodeAssert_contrat2periode3.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat2periode3.setPeriodeDebut("2015/01/01");
    periodeAssert_contrat2periode3.setPeriodeFin("2050/12/31");
    assertPeriodEquals(periodeAssert_contrat2periode3, contrat2periode3);

    PeriodeDroitContractTP contrat2periode4 =
        domaineDroits2
            .get(1)
            .getGaranties()
            .get(2)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(0);
    PeriodeDroitContractTP periodeAssert_contrat2periode4 = new PeriodeDroitContractTP();
    periodeAssert_contrat2periode4.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat2periode4.setPeriodeDebut("2015/01/01");
    periodeAssert_contrat2periode4.setPeriodeFin("2050/12/31");
    assertPeriodEquals(periodeAssert_contrat2periode4, contrat2periode4);

    processMessageFromJson("declarationB3.json");

    ContractTP contrat3 = contratDao.getContract("0000000002", "Contrat1", "15211521");
    Assertions.assertNotNull(contrat3);
    Assertions.assertEquals(contrat1.get_id(), contrat3.get_id());

    List<DomaineDroitContractTP> domaineDroits3 =
        contrat3.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(2, domaineDroits3.size());
    // when a domain is updated, it's placed at the end of array, so order may
    // change
    Assertions.assertEquals("DENT", domaineDroits3.get(0).getCode());
    Assertions.assertEquals("OPTI", domaineDroits3.get(1).getCode());
    Assertions.assertEquals(3, domaineDroits3.get(1).getGaranties().size());

    PeriodeDroitContractTP contrat3periode1 =
        domaineDroits3
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
            .get(0);
    PeriodeDroitContractTP periodeAssert_contrat3periode1 = new PeriodeDroitContractTP();
    periodeAssert_contrat3periode1.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat3periode1.setPeriodeDebut("2015/01/01");
    periodeAssert_contrat3periode1.setPeriodeFin("2055/12/31");
    assertPeriodEquals(periodeAssert_contrat3periode1, contrat3periode1);

    PeriodeDroitContractTP contrat3periode2 =
        domaineDroits3
            .get(1)
            .getGaranties()
            .get(1)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(0);
    PeriodeDroitContractTP periodeAssert_contrat3periode2 = new PeriodeDroitContractTP();
    periodeAssert_contrat3periode2.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat3periode2.setPeriodeDebut("2015/01/01");
    periodeAssert_contrat3periode2.setPeriodeFin("2055/12/31");
    assertPeriodEquals(periodeAssert_contrat3periode2, contrat3periode2);

    PeriodeDroitContractTP contrat3periode3 =
        domaineDroits3
            .get(1)
            .getGaranties()
            .get(2)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(0);
    PeriodeDroitContractTP periodeAssert_contrat3periode3 = new PeriodeDroitContractTP();
    periodeAssert_contrat3periode3.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat3periode3.setPeriodeDebut("2015/01/01");
    periodeAssert_contrat3periode3.setPeriodeFin("2055/12/31");
    assertPeriodEquals(periodeAssert_contrat3periode3, contrat3periode3);

    PeriodeDroitContractTP contrat3periode4 =
        domaineDroits3
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
            .get(1);
    PeriodeDroitContractTP periodeAssert_contrat3periode4 = new PeriodeDroitContractTP();
    periodeAssert_contrat3periode4.setTypePeriode(TypePeriode.ONLINE);
    periodeAssert_contrat3periode4.setPeriodeDebut("2015/01/01");
    periodeAssert_contrat3periode4.setPeriodeFin("2055/12/31");
    assertPeriodEquals(periodeAssert_contrat3periode4, contrat3periode4);
  }

  @Test
  void updatePeriodsComplex() throws IOException {
    // periode du 2018/01/01 au 2019/12/31 avec fermeture du 2018/09/01 au
    // 2018/12/31
    processMessageFromJson("declarationC1.json");
    processMessageFromJson("declarationC1bis.json");

    ContractTP contrat1 = contratDao.getContract("0089996144", "Contrat1", "15211521");
    Assertions.assertNotNull(contrat1);

    List<DomaineDroitContractTP> domaineDroits1 =
        contrat1.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(1, domaineDroits1.size());
    Assertions.assertEquals("OPTI", domaineDroits1.get(0).getCode());
    Assertions.assertEquals(
        2,
        domaineDroits1
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
            .size());

    PeriodeDroitContractTP contrat1periode1 =
        domaineDroits1
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
            .get(0);
    PeriodeDroitContractTP periodeAssert_contrat1periode1 = new PeriodeDroitContractTP();
    periodeAssert_contrat1periode1.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat1periode1.setPeriodeDebut("2018/01/01");
    periodeAssert_contrat1periode1.setPeriodeFin("2018/08/31");
    periodeAssert_contrat1periode1.setPeriodeFinFermeture("2018/12/31");
    assertPeriodEquals(periodeAssert_contrat1periode1, contrat1periode1);

    PeriodeDroitContractTP contrat1periode2 =
        domaineDroits1
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
            .get(1);
    PeriodeDroitContractTP periodeAssert_contrat1periode2 = new PeriodeDroitContractTP();
    periodeAssert_contrat1periode2.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat1periode2.setPeriodeDebut("2019/01/01");
    periodeAssert_contrat1periode2.setPeriodeFin("2019/12/31");
    assertPeriodEquals(periodeAssert_contrat1periode2, contrat1periode2);

    // periode du 2018/01/01 au 2018/12/31
    processMessageFromJson("declarationC2.json");

    contrat1 = contratDao.getContract("0089996144", "Contrat1", "15211521");
    Assertions.assertNotNull(contrat1);

    List<DomaineDroitContractTP> domaineDroits2 =
        contrat1.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(1, domaineDroits2.size());
    Assertions.assertEquals("OPTI", domaineDroits2.get(0).getCode());
    Assertions.assertEquals(
        1,
        domaineDroits2
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
            .size());

    PeriodeDroitContractTP contrat2periode1 =
        domaineDroits2
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
            .get(0);
    PeriodeDroitContractTP periodeAssert_contrat2periode1 = new PeriodeDroitContractTP();
    periodeAssert_contrat2periode1.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat2periode1.setPeriodeDebut("2018/01/01");
    periodeAssert_contrat2periode1.setPeriodeFin("2019/12/31");
    assertPeriodEquals(periodeAssert_contrat2periode1, contrat2periode1);

    // periode du 2018/01/01 au 2018/06/30 avec fermeture du 2018/07/01 au
    // 2019/12/31
    processMessageFromJson("declarationC3.json");

    ContractTP contrat3 = contratDao.getContract("0089996144", "Contrat1", "15211521");
    Assertions.assertNotNull(contrat3);
    Assertions.assertEquals(contrat1.get_id(), contrat3.get_id());

    List<DomaineDroitContractTP> domaineDroits3 =
        contrat3.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(1, domaineDroits3.size());
    Assertions.assertEquals("OPTI", domaineDroits3.get(0).getCode());
    Assertions.assertEquals(
        1,
        domaineDroits3
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
            .size());

    PeriodeDroitContractTP contrat3periode1 =
        domaineDroits3
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
            .get(0);
    PeriodeDroitContractTP periodeAssert_contrat3periode1 = new PeriodeDroitContractTP();
    periodeAssert_contrat3periode1.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat3periode1.setPeriodeDebut("2018/01/01");
    periodeAssert_contrat3periode1.setPeriodeFin("2018/06/30");
    periodeAssert_contrat3periode1.setPeriodeFinFermeture("2019/12/31");
    assertPeriodEquals(periodeAssert_contrat3periode1, contrat3periode1);
  }

  @Test
  void updatePeriodsComplex2() throws IOException {
    // periode du 2018/01/01 au 2018/12/31
    processMessageFromJson("declarationD1.json");

    ContractTP contrat1 = contratDao.getContract("0079001962", "Contrat1", "15211521");
    Assertions.assertNotNull(contrat1);

    List<DomaineDroitContractTP> domaineDroits1 =
        contrat1.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(1, domaineDroits1.size());
    Assertions.assertEquals("OPTI", domaineDroits1.get(0).getCode());
    Assertions.assertEquals(
        1,
        domaineDroits1
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
            .size());

    PeriodeDroitContractTP contrat1periode1 =
        domaineDroits1
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
            .get(0);
    PeriodeDroitContractTP periodeAssert_contrat1periode1 = new PeriodeDroitContractTP();
    periodeAssert_contrat1periode1.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat1periode1.setPeriodeDebut("2018/01/01");
    periodeAssert_contrat1periode1.setPeriodeFin("2018/12/31");
    assertPeriodEquals(periodeAssert_contrat1periode1, contrat1periode1);

    // periode du 2019/01/01 au 2019/12/31
    processMessageFromJson("declarationD2.json");

    ContractTP contrat2 = contratDao.getContract("0079001962", "Contrat1", "15211521");
    Assertions.assertNotNull(contrat2);
    Assertions.assertEquals(contrat1.get_id(), contrat2.get_id());

    List<DomaineDroitContractTP> domaineDroits2 =
        contrat2.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(1, domaineDroits2.size());
    Assertions.assertEquals("OPTI", domaineDroits2.get(0).getCode());
    Assertions.assertEquals(
        1,
        domaineDroits2
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
            .size());

    PeriodeDroitContractTP contrat2periode1 =
        domaineDroits2
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
            .get(0);
    PeriodeDroitContractTP periodeAssert_contrat2periode1 = new PeriodeDroitContractTP();
    periodeAssert_contrat2periode1.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat2periode1.setPeriodeDebut("2018/01/01");
    periodeAssert_contrat2periode1.setPeriodeFin("2019/12/31");
    assertPeriodEquals(periodeAssert_contrat2periode1, contrat2periode1);

    // periode du 2019/01/01 au 2018/06/30
    processMessageFromJson("declarationD3.json");

    ContractTP contrat3 = contratDao.getContract("0079001962", "Contrat1", "15211521");
    Assertions.assertNotNull(contrat3);
    Assertions.assertEquals(contrat1.get_id(), contrat3.get_id());

    List<DomaineDroitContractTP> domaineDroits3 =
        contrat3.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(1, domaineDroits3.size());
    Assertions.assertEquals("OPTI", domaineDroits3.get(0).getCode());
    Assertions.assertEquals(
        1,
        domaineDroits3
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
            .size());

    PeriodeDroitContractTP contrat3periode1 =
        domaineDroits3
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
            .get(0);
    PeriodeDroitContractTP periodeAssert_contrat3periode1 = new PeriodeDroitContractTP();
    periodeAssert_contrat3periode1.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat3periode1.setPeriodeDebut("2018/01/01");
    periodeAssert_contrat3periode1.setPeriodeFin("2018/06/30");
    periodeAssert_contrat3periode1.setPeriodeFinFermeture("2019/12/31");
    assertPeriodEquals(periodeAssert_contrat3periode1, contrat3periode1);
  }

  @Test
  void updatePeriodsComplexBIG() throws IOException {
    // periode du 2018/01/01 au 2019/12/31 avec fermeture du 2018/09/01 au
    // 2018/12/31
    processMessageFromJson("declarationF1.json");
    processMessageFromJson("declarationF2.json");
    processMessageFromJson("declarationF4.json");

    processMessageFromJson("declarationF3.json");
    processMessageFromJson("declarationF5.json");

    ContractTP contrat1 = contratDao.getContract("0089996144", "Contrat1", "15211521");
    Assertions.assertNotNull(contrat1);

    List<DomaineDroitContractTP> domaineDroits1 =
        contrat1.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(1, domaineDroits1.size());
    Assertions.assertEquals("OPAU", domaineDroits1.get(0).getCode());
    Assertions.assertEquals(
        2,
        domaineDroits1
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
            .size());

    PeriodeDroitContractTP contrat1periode1 =
        domaineDroits1
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
            .get(0);
    PeriodeDroitContractTP periodeAssert_contrat1periode1 = new PeriodeDroitContractTP();
    periodeAssert_contrat1periode1.setTypePeriode(TypePeriode.ONLINE);
    periodeAssert_contrat1periode1.setPeriodeDebut("2018/01/01");
    periodeAssert_contrat1periode1.setPeriodeFin(null);
    assertPeriodEquals(periodeAssert_contrat1periode1, contrat1periode1);

    PeriodeDroitContractTP contrat1periode2 =
        domaineDroits1
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
            .get(1);
    PeriodeDroitContractTP periodeAssert_contrat1periode2 = new PeriodeDroitContractTP();
    periodeAssert_contrat1periode2.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat1periode2.setPeriodeDebut("2018/01/01");
    periodeAssert_contrat1periode2.setPeriodeFin("2020/12/31");
    assertPeriodEquals(periodeAssert_contrat1periode2, contrat1periode2);
  }

  @Test
  void updatePeriodsComplexPortabilite() throws IOException {
    // periode du 2021/01/01 au 2022/12/31 avec fermeture du 2022/09/01 au
    // 2021/12/31
    processMessageFromJson("declarationPortabilite1.json");
    processMessageFromJson("declarationPortabilite2.json");
    processMessageFromJson("declarationPortabilite4.json");

    processMessageFromJson("declarationPortabilite3.json");
    processMessageFromJson("declarationPortabilite5.json");

    ContractTP contrat1 = contratDao.getContract("0089996144", "Contrat1", "15211521");
    Assertions.assertNotNull(contrat1);

    List<DomaineDroitContractTP> domaineDroits1 =
        contrat1.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(1, domaineDroits1.size());
    Assertions.assertEquals("OPAU", domaineDroits1.get(0).getCode());
    Assertions.assertEquals(
        2,
        domaineDroits1
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
            .size());

    PeriodeDroitContractTP contrat1periode1 =
        domaineDroits1
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
            .get(0);
    PeriodeDroitContractTP periodeAssert_contrat1periode1 = new PeriodeDroitContractTP();
    periodeAssert_contrat1periode1.setTypePeriode(TypePeriode.ONLINE);
    periodeAssert_contrat1periode1.setPeriodeDebut("2021/01/01");
    periodeAssert_contrat1periode1.setPeriodeFin("2024/03/20");
    assertPeriodEquals(periodeAssert_contrat1periode1, contrat1periode1);

    PeriodeDroitContractTP contrat1periode2 =
        domaineDroits1
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
            .get(1);
    PeriodeDroitContractTP periodeAssert_contrat1periode2 = new PeriodeDroitContractTP();
    periodeAssert_contrat1periode2.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat1periode2.setPeriodeDebut("2021/01/01");
    periodeAssert_contrat1periode2.setPeriodeFin("2023/12/31");
    assertPeriodEquals(periodeAssert_contrat1periode2, contrat1periode2);
  }

  @Test
  void testPeriodesSuspension() {
    ContractTP contract = new ContractTP();

    Declaration declaration = new Declaration();
    Contrat contrat = new Contrat();
    declaration.setContrat(contrat);
    PeriodeSuspensionDeclaration periodeSuspensionDeclaration = new PeriodeSuspensionDeclaration();
    periodeSuspensionDeclaration.setDebut("2022-03-01");
    periodeSuspensionDeclaration.setFin(null);
    contrat.setPeriodeSuspensions(List.of(periodeSuspensionDeclaration));
    declaration.setEtatSuspension("1");

    ContractTPService.updatePeriodesSuspension(contract, declaration);

    Assertions.assertNotNull(contract.getSuspension());
    Assertions.assertEquals("1", contract.getSuspension().getEtatSuspension());
    Assertions.assertEquals(1, contract.getSuspension().getPeriodesSuspension().size());
    Assertions.assertEquals(
        "2022-03-01",
        contract.getSuspension().getPeriodesSuspension().get(0).getDateDebutSuspension());
    Assertions.assertNull(
        contract.getSuspension().getPeriodesSuspension().get(0).getDateFinSuspension());

    periodeSuspensionDeclaration.setFin("2022-03-15");
    declaration.setEtatSuspension("2");
    ContractTPService.updatePeriodesSuspension(contract, declaration);
    Assertions.assertEquals("2", contract.getSuspension().getEtatSuspension());
    Assertions.assertEquals(1, contract.getSuspension().getPeriodesSuspension().size());
    Assertions.assertEquals(
        "2022-03-15",
        contract.getSuspension().getPeriodesSuspension().get(0).getDateFinSuspension());

    PeriodeSuspensionDeclaration periodeSuspensionDeclaration2 = new PeriodeSuspensionDeclaration();
    periodeSuspensionDeclaration2.setDebut("2022-04-15");
    periodeSuspensionDeclaration2.setFin("2022-04-16");
    declaration
        .getContrat()
        .setPeriodeSuspensions(
            List.of(periodeSuspensionDeclaration, periodeSuspensionDeclaration2));
    ContractTPService.updatePeriodesSuspension(contract, declaration);
    Assertions.assertEquals(2, contract.getSuspension().getPeriodesSuspension().size());
    Assertions.assertEquals(
        "2022-04-15",
        contract.getSuspension().getPeriodesSuspension().get(1).getDateDebutSuspension());
    Assertions.assertEquals(
        "2022-04-16",
        contract.getSuspension().getPeriodesSuspension().get(1).getDateFinSuspension());

    PeriodeSuspensionDeclaration periodeSuspensionDeclaration3 = new PeriodeSuspensionDeclaration();
    periodeSuspensionDeclaration3.setDebut("2021-04-15");
    periodeSuspensionDeclaration3.setFin("2021-04-15");
    declaration
        .getContrat()
        .setPeriodeSuspensions(
            List.of(
                periodeSuspensionDeclaration,
                periodeSuspensionDeclaration2,
                periodeSuspensionDeclaration3));
    ContractTPService.updatePeriodesSuspension(contract, declaration);
    Assertions.assertEquals(3, contract.getSuspension().getPeriodesSuspension().size());
    Assertions.assertEquals(
        "2021-04-15",
        contract.getSuspension().getPeriodesSuspension().get(0).getDateDebutSuspension());
    Assertions.assertEquals(
        "2021-04-15",
        contract.getSuspension().getPeriodesSuspension().get(0).getDateFinSuspension());
    Assertions.assertEquals(
        "2022-04-15",
        contract.getSuspension().getPeriodesSuspension().get(2).getDateDebutSuspension());
    Assertions.assertEquals(
        "2022-04-16",
        contract.getSuspension().getPeriodesSuspension().get(2).getDateFinSuspension());

    PeriodeSuspensionDeclaration periodeSuspensionDeclaration4 = new PeriodeSuspensionDeclaration();
    periodeSuspensionDeclaration4.setDebut("2021-05-15");
    periodeSuspensionDeclaration4.setFin("2021-05-15");
    declaration
        .getContrat()
        .setPeriodeSuspensions(
            List.of(
                periodeSuspensionDeclaration,
                periodeSuspensionDeclaration2,
                periodeSuspensionDeclaration3,
                periodeSuspensionDeclaration4));
    ContractTPService.updatePeriodesSuspension(contract, declaration);
    Assertions.assertEquals(4, contract.getSuspension().getPeriodesSuspension().size());
    Assertions.assertEquals(
        "2021-05-15",
        contract.getSuspension().getPeriodesSuspension().get(1).getDateDebutSuspension());
    Assertions.assertEquals(
        "2021-05-15",
        contract.getSuspension().getPeriodesSuspension().get(1).getDateFinSuspension());
    Assertions.assertEquals(
        "2022-04-15",
        contract.getSuspension().getPeriodesSuspension().get(3).getDateDebutSuspension());
    Assertions.assertEquals(
        "2022-04-16",
        contract.getSuspension().getPeriodesSuspension().get(3).getDateFinSuspension());
  }

  @Test
  void testMultiplesPeriodesSuspension() {
    ContractTP contract = new ContractTP();

    Declaration declaration = new Declaration();
    Contrat contrat = new Contrat();
    declaration.setContrat(contrat);

    PeriodeSuspensionDeclaration periodeSuspensionDeclaration = new PeriodeSuspensionDeclaration();
    periodeSuspensionDeclaration.setDebut("2022-04-15");
    periodeSuspensionDeclaration.setFin("2022-04-16");
    contrat.setPeriodeSuspensions(List.of(periodeSuspensionDeclaration));
    declaration.setEtatSuspension("1");
    ContractTPService.updatePeriodesSuspension(contract, declaration);

    Assertions.assertNotNull(contract.getSuspension());
    Assertions.assertEquals("1", contract.getSuspension().getEtatSuspension());
    Assertions.assertEquals(1, contract.getSuspension().getPeriodesSuspension().size());
    Assertions.assertEquals(
        "2022-04-15",
        contract.getSuspension().getPeriodesSuspension().get(0).getDateDebutSuspension());
    Assertions.assertEquals(
        "2022-04-16",
        contract.getSuspension().getPeriodesSuspension().get(0).getDateFinSuspension());

    PeriodeSuspensionDeclaration periodeSuspensionDeclaration2 = new PeriodeSuspensionDeclaration();
    periodeSuspensionDeclaration2.setDebut("2018-04-15");
    periodeSuspensionDeclaration2.setFin("2018-04-16");
    declaration
        .getContrat()
        .setPeriodeSuspensions(
            List.of(periodeSuspensionDeclaration, periodeSuspensionDeclaration2));
    declaration.setEtatSuspension("1");
    ContractTPService.updatePeriodesSuspension(contract, declaration);

    Assertions.assertNotNull(contract.getSuspension());
    Assertions.assertEquals("1", contract.getSuspension().getEtatSuspension());
    Assertions.assertEquals(2, contract.getSuspension().getPeriodesSuspension().size());
    Assertions.assertEquals(
        "2018-04-15",
        contract.getSuspension().getPeriodesSuspension().get(0).getDateDebutSuspension());
    Assertions.assertEquals(
        "2018-04-16",
        contract.getSuspension().getPeriodesSuspension().get(0).getDateFinSuspension());

    PeriodeSuspensionDeclaration periodeSuspensionDeclaration3 = new PeriodeSuspensionDeclaration();
    periodeSuspensionDeclaration3.setDebut("2017-04-15");
    periodeSuspensionDeclaration3.setFin(null);
    declaration
        .getContrat()
        .setPeriodeSuspensions(
            List.of(
                periodeSuspensionDeclaration,
                periodeSuspensionDeclaration2,
                periodeSuspensionDeclaration3));
    declaration.setEtatSuspension("1");
    ContractTPService.updatePeriodesSuspension(contract, declaration);

    Assertions.assertNotNull(contract.getSuspension());
    Assertions.assertEquals("1", contract.getSuspension().getEtatSuspension());
    Assertions.assertEquals(3, contract.getSuspension().getPeriodesSuspension().size());
    Assertions.assertEquals(
        "2017-04-15",
        contract.getSuspension().getPeriodesSuspension().get(0).getDateDebutSuspension());
    Assertions.assertNull(
        contract.getSuspension().getPeriodesSuspension().get(0).getDateFinSuspension());

    periodeSuspensionDeclaration3.setFin("2017-04-16");
    declaration.setEtatSuspension("1");
    ContractTPService.updatePeriodesSuspension(contract, declaration);

    Assertions.assertNotNull(contract.getSuspension());
    Assertions.assertEquals("1", contract.getSuspension().getEtatSuspension());
    Assertions.assertEquals(3, contract.getSuspension().getPeriodesSuspension().size());
    Assertions.assertEquals(
        "2017-04-15",
        contract.getSuspension().getPeriodesSuspension().get(0).getDateDebutSuspension());
    Assertions.assertEquals(
        "2017-04-16",
        contract.getSuspension().getPeriodesSuspension().get(0).getDateFinSuspension());
  }

  @Test
  void testRetroClosureCreate() throws IOException {
    // periode du 2018/01/01 au 2018/12/31
    processMessageFromJson("declarationE1.json");
    // periode du 2019/01/01 au 2019/12/31
    processMessageFromJson("declarationE2.json");

    ContractTP contrat1 = contratDao.getContract("0079001962", "Contrat1", "15211521");
    Assertions.assertNotNull(contrat1);
    Assertions.assertEquals(1, contrat1.getBeneficiaires().size());

    List<DomaineDroitContractTP> domaineDroits1 =
        contrat1.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(1, domaineDroits1.size());
    Assertions.assertEquals("OPTI", domaineDroits1.get(0).getCode());
    Assertions.assertEquals(
        1,
        domaineDroits1
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
            .size());

    PeriodeDroitContractTP contrat1periode1 =
        domaineDroits1
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
            .get(0);
    PeriodeDroitContractTP periodeAssert_contrat1periode1 = new PeriodeDroitContractTP();
    periodeAssert_contrat1periode1.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat1periode1.setPeriodeDebut("2018/01/01");
    periodeAssert_contrat1periode1.setPeriodeFin("2019/12/31");
    assertPeriodEquals(periodeAssert_contrat1periode1, contrat1periode1);

    // fermeture periode du 2018/07/01 au 2018/12/31
    processMessageFromJson("declarationE3.json");

    ContractTP contrat2 = contratDao.getContract("0079001962", "Contrat1", "15211521");
    Assertions.assertNotNull(contrat2);
    Assertions.assertEquals(1, contrat2.getBeneficiaires().size());

    List<DomaineDroitContractTP> domaineDroits2 =
        contrat2.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(1, domaineDroits2.size());
    Assertions.assertEquals("OPTI", domaineDroits2.get(0).getCode());
    Assertions.assertEquals(
        1,
        domaineDroits2
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
            .size());

    PeriodeDroitContractTP contrat2periode1 =
        domaineDroits2
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
            .get(0);
    PeriodeDroitContractTP periodeAssert_contrat2periode1 = new PeriodeDroitContractTP();
    periodeAssert_contrat2periode1.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat2periode1.setPeriodeDebut("2018/01/01");
    periodeAssert_contrat2periode1.setPeriodeFin("2018/06/30");
    periodeAssert_contrat2periode1.setPeriodeFinFermeture("2019/12/31");
    assertPeriodEquals(periodeAssert_contrat2periode1, contrat2periode1);
  }

  // https://cegedim-insurance.atlassian.net/wiki/spaces/AIN/pages/3948184127/Beyond+-+BDDS+-+Consolidation+Worker+Tests
  @Test
  void testCase123() throws IOException {
    // periode du 2022/01/01 au 2022/12/31
    processMessageFromJson("declaration01.json");
    // fermeture periode du 2022/04/01 au 2022/12/31
    processMessageFromJson("declaration02-1.json");
    // periode du 2022/06/01 au 2022/12/31
    processMessageFromJson("declaration02-2.json");

    ContractTP contrat12 = contratDao.getContract("0097810998", "CONSOW0", "CONSOW0");
    Assertions.assertNotNull(contrat12);
    Assertions.assertEquals(1, contrat12.getBeneficiaires().size());

    List<DomaineDroitContractTP> domaineDroits12 =
        contrat12.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(2, domaineDroits12.size());
    Assertions.assertEquals("AMM", domaineDroits12.get(0).getCode());
    Assertions.assertEquals("LABO", domaineDroits12.get(1).getCode());
    Assertions.assertEquals(
        4,
        domaineDroits12
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
            .size());

    PeriodeDroitContractTP contrat12periode1 =
        domaineDroits12
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
            .get(0);
    PeriodeDroitContractTP periodeAssert_contrat12periode1 = new PeriodeDroitContractTP();
    periodeAssert_contrat12periode1.setTypePeriode(TypePeriode.ONLINE);
    periodeAssert_contrat12periode1.setPeriodeDebut("2022/01/01");
    periodeAssert_contrat12periode1.setPeriodeFin("2022/03/31");
    periodeAssert_contrat12periode1.setPeriodeFinFermeture("2022/12/31");
    assertPeriodEquals(periodeAssert_contrat12periode1, contrat12periode1);

    PeriodeDroitContractTP contrat12periode2 =
        domaineDroits12
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
            .get(1);
    PeriodeDroitContractTP periodeAssert_contrat12periode2 = new PeriodeDroitContractTP();
    periodeAssert_contrat12periode2.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat12periode2.setPeriodeDebut("2022/01/01");
    periodeAssert_contrat12periode2.setPeriodeFin("2022/03/31");
    periodeAssert_contrat12periode2.setPeriodeFinFermeture("2022/12/31");
    assertPeriodEquals(periodeAssert_contrat12periode2, contrat12periode2);

    PeriodeDroitContractTP contrat12periode3 =
        domaineDroits12
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
            .get(2);
    PeriodeDroitContractTP periodeAssert_contrat12periode3 = new PeriodeDroitContractTP();
    periodeAssert_contrat12periode3.setTypePeriode(TypePeriode.ONLINE);
    periodeAssert_contrat12periode3.setPeriodeDebut("2022/06/01");
    periodeAssert_contrat12periode3.setPeriodeFin(null);
    assertPeriodEquals(periodeAssert_contrat12periode3, contrat12periode3);

    PeriodeDroitContractTP contrat12periode4 =
        domaineDroits12
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
            .get(3);
    PeriodeDroitContractTP periodeAssert_contrat12periode4 = new PeriodeDroitContractTP();
    periodeAssert_contrat12periode4.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat12periode4.setPeriodeDebut("2022/06/01");
    periodeAssert_contrat12periode4.setPeriodeFin("2022/12/31");
    assertPeriodEquals(periodeAssert_contrat12periode4, contrat12periode4);

    // resiliation periode 2022/01/01 au 2022/03/31
    processMessageFromJson("declaration03-1.json");

    ContractTP contrat123a = contratDao.getContract("0097810998", "CONSOW0", "CONSOW0");
    Assertions.assertNotNull(contrat123a);
    Assertions.assertEquals(1, contrat123a.getBeneficiaires().size());

    List<DomaineDroitContractTP> domaineDroits123a =
        contrat123a.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(2, domaineDroits123a.size());
    Assertions.assertEquals("AMM", domaineDroits123a.get(0).getCode());
    Assertions.assertEquals("LABO", domaineDroits123a.get(1).getCode());
    Assertions.assertEquals(
        3,
        domaineDroits123a
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
            .size());

    PeriodeDroitContractTP contrat123aperiode1 =
        domaineDroits123a
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
            .get(0);
    PeriodeDroitContractTP periodeAssert_contrat123aperiode1 = new PeriodeDroitContractTP();
    periodeAssert_contrat123aperiode1.setPeriodeDebut("2022/01/01");
    periodeAssert_contrat123aperiode1.setPeriodeFin("2021/12/31");
    periodeAssert_contrat123aperiode1.setPeriodeFinFermeture("2022/03/31");
    periodeAssert_contrat123aperiode1.setTypePeriode(TypePeriode.OFFLINE);
    assertPeriodEquals(periodeAssert_contrat123aperiode1, contrat123aperiode1);

    PeriodeDroitContractTP contrat123aperiode2 =
        domaineDroits123a
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
            .get(1);
    PeriodeDroitContractTP periodeAssert_contrat123aperiode2 = new PeriodeDroitContractTP();
    periodeAssert_contrat123aperiode2.setTypePeriode(TypePeriode.ONLINE);
    periodeAssert_contrat123aperiode2.setPeriodeDebut("2022/06/01");
    periodeAssert_contrat123aperiode2.setPeriodeFin(null);
    assertPeriodEquals(periodeAssert_contrat123aperiode2, contrat123aperiode2);

    PeriodeDroitContractTP contrat123aperiode3 =
        domaineDroits123a
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
            .get(2);

    PeriodeDroitContractTP periodeAssert_contrat123aperiode3 = new PeriodeDroitContractTP();
    periodeAssert_contrat123aperiode3.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat123aperiode3.setPeriodeDebut("2022/06/01");
    periodeAssert_contrat123aperiode3.setPeriodeFin("2022/12/31");
    assertPeriodEquals(periodeAssert_contrat123aperiode3, contrat123aperiode3);

    // resiliation periode 2022/06/01 au 2022/12/31
    processMessageFromJson("declaration03-2.json");

    contrat123a = contratDao.getContract("0097810998", "CONSOW0", "CONSOW0");
    // les droits offlines sont toujours ouverts
    Assertions.assertNotNull(contrat123a);

    contrat123aperiode1 =
        domaineDroits123a
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
            .get(0);
    periodeAssert_contrat123aperiode1 = new PeriodeDroitContractTP();
    periodeAssert_contrat123aperiode1.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat123aperiode1.setPeriodeDebut("2022/01/01");
    periodeAssert_contrat123aperiode1.setPeriodeFin("2021/12/31");
    periodeAssert_contrat123aperiode1.setPeriodeFinFermeture("2022/03/31");
    assertPeriodEquals(periodeAssert_contrat123aperiode1, contrat123aperiode1);

    contrat123aperiode2 =
        domaineDroits123a
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
            .get(1);
    periodeAssert_contrat123aperiode2 = new PeriodeDroitContractTP();
    periodeAssert_contrat123aperiode2.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat123aperiode2.setPeriodeDebut("2022/06/01");
    periodeAssert_contrat123aperiode2.setPeriodeFin("2022/05/31");
    periodeAssert_contrat123aperiode2.setPeriodeFinFermeture("2022/12/31");
    assertPeriodEquals(periodeAssert_contrat123aperiode2, contrat123aperiode2);
  }

  @Test
  void testCase123B() throws IOException {
    // periode du 2022/01/01 au 2022/12/31
    processMessageFromJson("declaration01.json");
    // fermeture periode du 2022/04/01 au 2022/12/31
    processMessageFromJson("declaration02-1.json");
    // periode du 2022/06/01 au 2022/12/31
    processMessageFromJson("declaration02-2.json");

    // Les assertions a ce stade sont déjà dans le test testCase123()

    // resiliation periode 2022/01/01 au 2022/11/30 (via periodeFinFermeture)
    processMessageFromJson("declaration03-3.json");

    ContractTP contrat123a = contratDao.getContract("0097810998", "CONSOW0", "CONSOW0");
    Assertions.assertNotNull(contrat123a);
    Assertions.assertEquals(1, contrat123a.getBeneficiaires().size());

    List<DomaineDroitContractTP> domaineDroits123a =
        contrat123a.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(2, domaineDroits123a.size());
    Assertions.assertEquals("AMM", domaineDroits123a.get(0).getCode());
    Assertions.assertEquals(
        2,
        domaineDroits123a
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
            .size());

    PeriodeDroitContractTP contrat123aperiode1 =
        domaineDroits123a
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
            .get(0);
    PeriodeDroitContractTP periodeAssert_contrat123aperiode1 = new PeriodeDroitContractTP();
    periodeAssert_contrat123aperiode1.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat123aperiode1.setPeriodeDebut("2022/01/01");
    periodeAssert_contrat123aperiode1.setPeriodeFin("2021/12/31");
    periodeAssert_contrat123aperiode1.setPeriodeFinFermeture("2022/11/30");
    assertPeriodEquals(periodeAssert_contrat123aperiode1, contrat123aperiode1);

    PeriodeDroitContractTP contrat123aperiode2 =
        domaineDroits123a
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
            .get(1);
    PeriodeDroitContractTP periodeAssert_contrat123aperiode2 = new PeriodeDroitContractTP();
    periodeAssert_contrat123aperiode2.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat123aperiode2.setPeriodeDebut("2022/12/01");
    periodeAssert_contrat123aperiode2.setPeriodeFin("2022/12/31");
    assertPeriodEquals(periodeAssert_contrat123aperiode2, contrat123aperiode2);
  }

  @Test
  void testCase14() throws IOException {
    // periode du 2022/01/01 au 2022/12/31
    processMessageFromJson("declaration01.json");
    // fermeture periode du 2022/01/01 au 2022/12/31 pour AMM
    processMessageFromJson("declaration04.json");

    ContractTP contrat14 = contratDao.getContract("0097810998", "CONSOW0", "CONSOW0");
    Assertions.assertNotNull(contrat14);
    Assertions.assertEquals(1, contrat14.getBeneficiaires().size());

    List<DomaineDroitContractTP> domaineDroits14 =
        contrat14.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(2, domaineDroits14.size());
    Assertions.assertEquals("AMM", domaineDroits14.get(0).getCode());
    Assertions.assertEquals(
        1,
        domaineDroits14
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
            .size());

    PeriodeDroitContractTP contrat14periode1 =
        domaineDroits14
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
            .get(0);
    PeriodeDroitContractTP periodeAssert_contrat14periode1 = new PeriodeDroitContractTP();
    periodeAssert_contrat14periode1.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat14periode1.setPeriodeDebut("2022/01/01");
    periodeAssert_contrat14periode1.setPeriodeFin("2021/12/31");
    periodeAssert_contrat14periode1.setPeriodeFinFermeture("2022/12/31");
    assertPeriodEquals(periodeAssert_contrat14periode1, contrat14periode1);
  }

  @Test
  void testCase5() throws IOException {
    // periode du 2022/01/01 au 2022/12/31
    processMessageFromJson("declaration05-1.json");
    // fermeture periode du 2022/01/01 au 2022/12/31 pour AMM
    processMessageFromJson("declaration05-2.json");

    ContractTP contrat5 = contratDao.getContract("0097810998", "CONSOW0", "CONSOW0");
    Assertions.assertNotNull(contrat5);
    Assertions.assertEquals(1, contrat5.getBeneficiaires().size());

    List<DomaineDroitContractTP> domaineDroits5 =
        contrat5.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(1, domaineDroits5.size());
    Assertions.assertEquals("AMM", domaineDroits5.get(0).getCode());
    Assertions.assertEquals(
        2,
        domaineDroits5
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
            .size());

    PeriodeDroitContractTP contrat5periode1 =
        domaineDroits5
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
            .get(0);
    PeriodeDroitContractTP periodeAssert_contrat5periode1 = new PeriodeDroitContractTP();
    periodeAssert_contrat5periode1.setTypePeriode(TypePeriode.ONLINE);
    periodeAssert_contrat5periode1.setPeriodeDebut("2022/01/01");
    periodeAssert_contrat5periode1.setPeriodeFin("2022/10/31");
    assertPeriodEquals(periodeAssert_contrat5periode1, contrat5periode1);

    PeriodeDroitContractTP contrat5periode2 =
        domaineDroits5
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
            .get(1);
    PeriodeDroitContractTP periodeAssert_contrat5periode2 = new PeriodeDroitContractTP();
    periodeAssert_contrat5periode2.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat5periode2.setPeriodeDebut("2022/01/01");
    periodeAssert_contrat5periode2.setPeriodeFin("2022/10/31");
    periodeAssert_contrat5periode2.setPeriodeFinFermeture("2022/12/31");
    assertPeriodEquals(periodeAssert_contrat5periode2, contrat5periode2);
  }

  @Test
  void testCase6() throws IOException {
    // periode du 2022/01/01 au 2022/12/31 avec 2 garanties
    processMessageFromJson("declaration06.json");

    ContractTP contrat6 = contratDao.getContract("0097810998", "CONSOW0", "CONSOW0");
    Assertions.assertNotNull(contrat6);
    Assertions.assertEquals(1, contrat6.getBeneficiaires().size());

    List<DomaineDroitContractTP> domaineDroits6 =
        contrat6.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(1, domaineDroits6.size());
    Assertions.assertEquals("AMM", domaineDroits6.get(0).getCode());
    Assertions.assertEquals(2, domaineDroits6.get(0).getGaranties().size());

    PeriodeDroitContractTP contrat6periode1 =
        domaineDroits6
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
            .get(0);
    PeriodeDroitContractTP periodeAssert_contrat6periode1 = new PeriodeDroitContractTP();
    periodeAssert_contrat6periode1.setTypePeriode(TypePeriode.ONLINE);
    periodeAssert_contrat6periode1.setPeriodeDebut("2022/01/01");
    periodeAssert_contrat6periode1.setPeriodeFin(null);
    assertPeriodEquals(periodeAssert_contrat6periode1, contrat6periode1);

    PeriodeDroitContractTP contrat6periode2 =
        domaineDroits6
            .get(0)
            .getGaranties()
            .get(1)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(0);
    PeriodeDroitContractTP periodeAssert_contrat6periode2 = new PeriodeDroitContractTP();
    periodeAssert_contrat6periode2.setTypePeriode(TypePeriode.ONLINE);
    periodeAssert_contrat6periode2.setPeriodeDebut("2022/01/01");
    periodeAssert_contrat6periode2.setPeriodeFin(null);
    assertPeriodEquals(periodeAssert_contrat6periode2, contrat6periode2);

    PeriodeDroitContractTP contrat6periode3 =
        domaineDroits6
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
            .get(1);
    PeriodeDroitContractTP periodeAssert_contrat6periode3 = new PeriodeDroitContractTP();
    periodeAssert_contrat6periode3.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat6periode3.setPeriodeDebut("2022/01/01");
    periodeAssert_contrat6periode3.setPeriodeFin("2022/12/31");
    assertPeriodEquals(periodeAssert_contrat6periode3, contrat6periode3);

    PeriodeDroitContractTP contrat6periode4 =
        domaineDroits6
            .get(0)
            .getGaranties()
            .get(1)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(1);
    PeriodeDroitContractTP periodeAssert_contrat6periode4 = new PeriodeDroitContractTP();
    periodeAssert_contrat6periode4.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat6periode4.setPeriodeDebut("2022/01/01");
    periodeAssert_contrat6periode4.setPeriodeFin("2022/12/31");
    assertPeriodEquals(periodeAssert_contrat6periode4, contrat6periode4);
  }

  @Test
  void testCase7() throws IOException {
    // periode du 2022/01/01 au 2022/12/31
    processMessageFromJson("declaration07-1.json");

    // suspension du 2022/09/01 au 2022/12/31
    processMessageFromJson("declaration07-2.json");

    // fin suspension 2022/09/30
    processMessageFromJson("declaration07-3.json");

    ContractTP contrat7 = contratDao.getContract("0097810998", "CONSOW0", "CONSOW0");
    Assertions.assertNotNull(contrat7);
    Assertions.assertEquals(1, contrat7.getBeneficiaires().size());

    List<DomaineDroitContractTP> domaineDroits7 =
        contrat7.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(1, domaineDroits7.size());
    Assertions.assertEquals("AMM", domaineDroits7.get(0).getCode());
    Assertions.assertEquals(
        2,
        domaineDroits7
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
            .size());

    PeriodeDroitContractTP contrat7periode1 =
        domaineDroits7
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
            .get(0);
    PeriodeDroitContractTP periodeAssert_contrat7periode1 = new PeriodeDroitContractTP();
    periodeAssert_contrat7periode1.setTypePeriode(TypePeriode.ONLINE);
    periodeAssert_contrat7periode1.setPeriodeDebut("2022/01/01");
    periodeAssert_contrat7periode1.setPeriodeFin(null);
    assertPeriodEquals(periodeAssert_contrat7periode1, contrat7periode1);

    PeriodeDroitContractTP contrat7periode2 =
        domaineDroits7
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
            .get(1);
    PeriodeDroitContractTP periodeAssert_contrat7periode2 = new PeriodeDroitContractTP();
    periodeAssert_contrat7periode2.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat7periode2.setPeriodeDebut("2022/01/01");
    periodeAssert_contrat7periode2.setPeriodeFin("2022/12/31");
    assertPeriodEquals(periodeAssert_contrat7periode2, contrat7periode2);

    SuspensionContract suspension = contrat7.getSuspension();
    Assertions.assertNotNull(suspension);
    Assertions.assertNotNull(suspension.getPeriodesSuspension());
    Assertions.assertEquals(1, suspension.getPeriodesSuspension().size());
    PeriodeSuspensionContract periodeSuspensionContract = suspension.getPeriodesSuspension().get(0);
    Assertions.assertEquals("2022-09-01", periodeSuspensionContract.getDateDebutSuspension());
    Assertions.assertEquals("2022-09-30", periodeSuspensionContract.getDateFinSuspension());
  }

  @Test
  void testCase8() throws IOException {
    // periode du 2022/01/01 au 2022/12/31
    processMessageFromJson("declaration08-1.json");

    // periode du 2023/01/01 au 2023/12/31
    processMessageFromJson("declaration08-2.json");

    // fermeture 2022/11/01 au 2022/12/31
    processMessageFromJson("declaration08-3.json");

    ContractTP contrat8 = contratDao.getContract("0097810998", "CONSOW0", "CONSOW0");
    Assertions.assertNotNull(contrat8);
    Assertions.assertEquals(1, contrat8.getBeneficiaires().size());

    List<DomaineDroitContractTP> domaineDroits8 =
        contrat8.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(1, domaineDroits8.size());
    Assertions.assertEquals("AMM", domaineDroits8.get(0).getCode());
    Assertions.assertEquals(
        3,
        domaineDroits8
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
            .size());

    PeriodeDroitContractTP contrat8periode1 =
        domaineDroits8
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
            .get(0);
    PeriodeDroitContractTP periodeAssert_contrat8periode1 = new PeriodeDroitContractTP();
    periodeAssert_contrat8periode1.setTypePeriode(TypePeriode.ONLINE);
    periodeAssert_contrat8periode1.setPeriodeDebut("2022/01/01");
    periodeAssert_contrat8periode1.setPeriodeFin("2022/10/31");
    periodeAssert_contrat8periode1.setPeriodeFinFermeture("2022/12/31");
    assertPeriodEquals(periodeAssert_contrat8periode1, contrat8periode1);

    PeriodeDroitContractTP contrat8periode2 =
        domaineDroits8
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
            .get(1);
    PeriodeDroitContractTP periodeAssert_contrat8periode2 = new PeriodeDroitContractTP();
    periodeAssert_contrat8periode2.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat8periode2.setPeriodeDebut("2022/01/01");
    periodeAssert_contrat8periode2.setPeriodeFin("2022/10/31");
    periodeAssert_contrat8periode2.setPeriodeFinFermeture("2022/12/31");
    assertPeriodEquals(periodeAssert_contrat8periode2, contrat8periode2);

    PeriodeDroitContractTP contrat8periode3 =
        domaineDroits8
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
            .get(2);
    PeriodeDroitContractTP periodeAssert_contrat8periode3 = new PeriodeDroitContractTP();
    periodeAssert_contrat8periode3.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat8periode3.setPeriodeDebut("2023/01/01");
    periodeAssert_contrat8periode3.setPeriodeFin("2023/12/31");
    assertPeriodEquals(periodeAssert_contrat8periode3, contrat8periode3);
  }

  @Test
  void testCase9() throws IOException {
    // issu du cas 9 des carences
    processMessageFromJson("declarationCase9.json");

    ContractTP contrat9 = contratDao.getContract("0000401166", "MBA0003", "MBA0003");
    Assertions.assertNotNull(contrat9);
    Assertions.assertEquals(1, contrat9.getBeneficiaires().size());

    List<DomaineDroitContractTP> domaineDroits9 =
        contrat9.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(4, domaineDroits9.size());
    Assertions.assertEquals("OPTI", domaineDroits9.get(0).getCode());
    Assertions.assertEquals("HOSP", domaineDroits9.get(1).getCode());
    Assertions.assertEquals("DENT", domaineDroits9.get(2).getCode());
    Assertions.assertEquals("PHAR", domaineDroits9.get(3).getCode());

    PeriodeDroitContractTP contrat9dentPeriode1 =
        domaineDroits9
            .get(2)
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(0);
    PeriodeDroitContractTP periodeAssert_contrat9dentPeriode1 = new PeriodeDroitContractTP();
    periodeAssert_contrat9dentPeriode1.setTypePeriode(TypePeriode.ONLINE);
    periodeAssert_contrat9dentPeriode1.setPeriodeDebut("2023/01/01");
    periodeAssert_contrat9dentPeriode1.setPeriodeFin("2023/01/31");
    assertPeriodEquals(periodeAssert_contrat9dentPeriode1, contrat9dentPeriode1);

    PeriodeDroitContractTP contrat9dentPeriode2 =
        domaineDroits9
            .get(2)
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(1);
    PeriodeDroitContractTP periodeAssert_contrat9dentPeriode2 = new PeriodeDroitContractTP();
    periodeAssert_contrat9dentPeriode2.setTypePeriode(TypePeriode.ONLINE);
    periodeAssert_contrat9dentPeriode2.setPeriodeDebut("2023/04/01");
    periodeAssert_contrat9dentPeriode2.setPeriodeFin(null);
    assertPeriodEquals(periodeAssert_contrat9dentPeriode2, contrat9dentPeriode2);

    PeriodeDroitContractTP contrat9dentPeriode3 =
        domaineDroits9
            .get(2)
            .getGaranties()
            .get(1)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(0);
    PeriodeDroitContractTP periodeAssert_contrat9dentPeriode3 = new PeriodeDroitContractTP();
    periodeAssert_contrat9dentPeriode3.setTypePeriode(TypePeriode.ONLINE);
    periodeAssert_contrat9dentPeriode3.setPeriodeDebut("2023/02/01");
    periodeAssert_contrat9dentPeriode3.setPeriodeFin("2023/03/31");
    assertPeriodEquals(periodeAssert_contrat9dentPeriode3, contrat9dentPeriode3);

    PeriodeDroitContractTP contrat9dentPeriode4 =
        domaineDroits9
            .get(2)
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(2);
    PeriodeDroitContractTP periodeAssert_contrat9dentPeriode4 = new PeriodeDroitContractTP();
    periodeAssert_contrat9dentPeriode4.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat9dentPeriode4.setPeriodeDebut("2023/01/01");
    periodeAssert_contrat9dentPeriode4.setPeriodeFin("2023/01/31");
    assertPeriodEquals(periodeAssert_contrat9dentPeriode4, contrat9dentPeriode4);

    PeriodeDroitContractTP contrat9dentPeriode5 =
        domaineDroits9
            .get(2)
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(3);
    PeriodeDroitContractTP periodeAssert_contrat9dentPeriode5 = new PeriodeDroitContractTP();
    periodeAssert_contrat9dentPeriode5.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat9dentPeriode5.setPeriodeDebut("2023/04/01");
    periodeAssert_contrat9dentPeriode5.setPeriodeFin("2023/12/31");
    assertPeriodEquals(periodeAssert_contrat9dentPeriode5, contrat9dentPeriode5);

    PeriodeDroitContractTP contrat9dentPeriode6 =
        domaineDroits9
            .get(2)
            .getGaranties()
            .get(1)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(1);
    PeriodeDroitContractTP periodeAssert_contrat9dentPeriode6 = new PeriodeDroitContractTP();
    periodeAssert_contrat9dentPeriode6.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat9dentPeriode6.setPeriodeDebut("2023/02/01");
    periodeAssert_contrat9dentPeriode6.setPeriodeFin("2023/03/31");
    assertPeriodEquals(periodeAssert_contrat9dentPeriode6, contrat9dentPeriode6);
  }

  @Test
  void consolidateNaturePrestationOnline() throws IOException {
    // Declaration d'ouverture avec des droits :
    // - Offline sur tout 2023, nature DENTAIRE
    // - Pas de Online, aucune nature
    processMessageFromJson("declarationNatPrestOnline01.json");

    // Declaration d'ouverture avec des droits :
    // - Offline sur tout 2023, nature DENTAIRE
    // - Online commançant au 2023/05/01 et sans date de fin, nature DENTAIRE_ONLINE
    processMessageFromJson("declarationNatPrestOnline02.json");

    ContractTP contratNatPrestOnline = contratDao.getContract("0000401166", "NRO01", "NRO01");
    Assertions.assertNotNull(contratNatPrestOnline);
    Assertions.assertEquals(1, contratNatPrestOnline.getBeneficiaires().size());

    List<DomaineDroitContractTP> domaineDroits =
        contratNatPrestOnline.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(1, domaineDroits.size());
    Assertions.assertEquals("DENT", domaineDroits.get(0).getCode());
    DomaineDroitContractTP contratDent = domaineDroits.get(0);
    Assertions.assertEquals(
        2,
        contratDent
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .size());

    PeriodeDroitContractTP contratDentPeriode1 =
        contratDent
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(0);
    PeriodeDroitContractTP periodeAssert_contratDentPeriode1 = new PeriodeDroitContractTP();
    periodeAssert_contratDentPeriode1.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contratDentPeriode1.setPeriodeDebut("2023/01/01");
    periodeAssert_contratDentPeriode1.setPeriodeFin("2023/12/31");
    assertPeriodEquals(periodeAssert_contratDentPeriode1, contratDentPeriode1);

    PeriodeDroitContractTP contratDentPeriode2 =
        contratDent
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(1)
            .getPeriodesDroit()
            .get(0);
    PeriodeDroitContractTP periodeAssert_contratDentPeriode2 = new PeriodeDroitContractTP();
    periodeAssert_contratDentPeriode2.setTypePeriode(TypePeriode.ONLINE);
    periodeAssert_contratDentPeriode2.setPeriodeDebut("2023/05/01");
    periodeAssert_contratDentPeriode2.setPeriodeFin(null);
    assertPeriodEquals(periodeAssert_contratDentPeriode2, contratDentPeriode2);
  }

  @Test
  void consolidateNaturePrestationOnlineFermeture() throws IOException {
    // Declaration d'ouverture avec des droits :
    // - Offline sur tout 2023, nature DENTAIRE
    // - Pas de Online, aucune nature
    processMessageFromJson("declarationNatPrestOnline01.json");

    // Declaration d'ouverture avec des droits :
    // - Offline sur tout 2023, nature DENTAIRE
    // - Online commançant au 2023/05/01 et sans date de fin, nature DENTAIRE_ONLINE
    processMessageFromJson("declarationNatPrestOnline02.json");

    // Declaration de fermeture avec des droits :
    // - Offline du 2023/01/01 au 2023/05/31, nature DENTAIRE
    // - Online du 2023/05/01 au 2023/05/31, nature DENTAIRE_ONLINE
    processMessageFromJson("declarationNatPrestOnline03.json");

    ContractTP contratNatPrestOnline = contratDao.getContract("0000401166", "NRO01", "NRO01");
    Assertions.assertNotNull(contratNatPrestOnline);

    List<DomaineDroitContractTP> domaineDroits =
        contratNatPrestOnline.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(1, domaineDroits.size());
    Assertions.assertEquals("DENT", domaineDroits.get(0).getCode());
    DomaineDroitContractTP contratDent = domaineDroits.get(0);
    Assertions.assertEquals(
        2,
        contratDent
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .size());

    PeriodeDroitContractTP contratDentPeriode1 =
        contratDent
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(0);
    PeriodeDroitContractTP periodeAssert_contratDentPeriode1 = new PeriodeDroitContractTP();
    periodeAssert_contratDentPeriode1.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contratDentPeriode1.setPeriodeDebut("2023/01/01");
    periodeAssert_contratDentPeriode1.setPeriodeFin("2023/05/31");
    periodeAssert_contratDentPeriode1.setPeriodeFinFermeture("2023/12/31");
    assertPeriodEquals(periodeAssert_contratDentPeriode1, contratDentPeriode1);

    PeriodeDroitContractTP contratDentPeriode2 =
        contratDent
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(1)
            .getPeriodesDroit()
            .get(0);
    PeriodeDroitContractTP periodeAssert_contratDentPeriode2 = new PeriodeDroitContractTP();
    periodeAssert_contratDentPeriode2.setTypePeriode(TypePeriode.ONLINE);
    periodeAssert_contratDentPeriode2.setPeriodeDebut("2023/05/01");
    periodeAssert_contratDentPeriode2.setPeriodeFin("2023/05/31");
    assertPeriodEquals(periodeAssert_contratDentPeriode2, contratDentPeriode2);
  }

  @Test
  void consolidateDidier1() throws IOException {
    processMessageFromJson("declarationDidier1.json"); // V 2021
    // processMessageFromJson("declarationDidier2.json"); // BEN2 - V 2021

    processMessageFromJson("declarationDidier3.json"); // V 2022
    // processMessageFromJson("declarationDidier4.json"); // BEN2 - V 2022

    processMessageFromJson("declarationDidier5.json"); // F 2021
    processMessageFromJson("declarationDidier6.json"); // F 2022
    processMessageFromJson("declarationDidier7.json"); // V 2023 V1.50
    // processMessageFromJson("declarationDidier8.json"); // BEN2 - F 2021
    // processMessageFromJson("declarationDidier9.json"); // BEN2 - F 2022
    // processMessageFromJson("declarationDidier10.json"); // BEN2 - V 2023 V1.50

    // processMessageFromJson("declarationDidier11.json"); // BEN2 - F 2021
    // processMessageFromJson("declarationDidier12.json"); // BEN2 - F 2022
    // processMessageFromJson("declarationDidier13.json"); // BEN2 - F 2023 01/01
    // 07/21 V1.50
    // processMessageFromJson("declarationDidier14.json"); // BEN2 - V 2023 07/21
    // 12/31 V1.61
    processMessageFromJson("declarationDidier15.json"); // F 2021
    processMessageFromJson("declarationDidier16.json"); // F 2022
    processMessageFromJson("declarationDidier17.json"); // F 2023 01/01 07/21 V1.50
    processMessageFromJson("declarationDidier18.json"); // V 2023 07/21 12/31 V1.61

    // processMessageFromJson("declarationDidier19.json"); // BEN2 - F 2021
    // processMessageFromJson("declarationDidier20.json"); // BEN2 - F 2022
    // processMessageFromJson("declarationDidier21.json"); // BEN2 - F 2023 01/01
    // 07/21 V1.50
    // processMessageFromJson("declarationDidier22.json"); // BEN2 - F 2023 07/21
    // 07/25 V1.61
    // processMessageFromJson("declarationDidier23.json"); // BEN2 - V 2023 07/25
    // 12/31 V1.61
    processMessageFromJson("declarationDidier24.json"); // F 2021
    processMessageFromJson("declarationDidier25.json"); // F 2022
    processMessageFromJson("declarationDidier26.json"); // F 2023 01/01 07/21 V1.50
    processMessageFromJson("declarationDidier27.json"); // F 2023 07/21 07/25 V1.61
    processMessageFromJson("declarationDidier28.json"); // V 2023 07/25 12/31 V1.61

    ContractTP contratNatPrestOnline = contratDao.getContract("0000401166", "02247565", "02247565");
    Assertions.assertNotNull(contratNatPrestOnline);

    List<DomaineDroitContractTP> domaineDroits =
        contratNatPrestOnline.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(11, domaineDroits.size());
    Assertions.assertEquals("OPAU", domaineDroits.get(0).getCode());
    DomaineDroitContractTP contratOpau = domaineDroits.get(0);
    Assertions.assertEquals(
        2,
        contratOpau
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .size());

    // Il doit y avoir
    // 1 période ON du 01/01/2021 au null
    // 1 période OFF du 01/01/2021 au 31/12/2023

    PeriodeDroitContractTP contratOpauPeriode1 =
        contratOpau
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(0);
    PeriodeDroitContractTP periodeAssert_contratOpauPeriode1 = new PeriodeDroitContractTP();
    periodeAssert_contratOpauPeriode1.setTypePeriode(TypePeriode.ONLINE);
    periodeAssert_contratOpauPeriode1.setPeriodeDebut("2021/01/01");
    periodeAssert_contratOpauPeriode1.setPeriodeFin(null);
    periodeAssert_contratOpauPeriode1.setPeriodeFinFermeture(null);
    assertPeriodEquals(periodeAssert_contratOpauPeriode1, contratOpauPeriode1);

    PeriodeDroitContractTP contratOpauPeriode2 =
        contratOpau
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(1);
    PeriodeDroitContractTP periodeAssert_contratOpauPeriode2 = new PeriodeDroitContractTP();
    periodeAssert_contratOpauPeriode2.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contratOpauPeriode2.setPeriodeDebut("2021/01/01");
    periodeAssert_contratOpauPeriode2.setPeriodeFin("2023/12/31");
    assertPeriodEquals(periodeAssert_contratOpauPeriode2, contratOpauPeriode2);
  }

  @Test
  void consolidateDidier2() throws IOException {
    processMessageFromJson("declarationDidier2.json"); // BEN2 - V 2021

    processMessageFromJson("declarationDidier4.json"); // BEN2 - V 2022

    processMessageFromJson("declarationDidier8.json"); // BEN2 - F 2021
    processMessageFromJson("declarationDidier9.json"); // BEN2 - F 2022
    processMessageFromJson("declarationDidier10.json"); // BEN2 - V 2023 V1.50

    processMessageFromJson("declarationDidier11.json"); // BEN2 - F 2021
    processMessageFromJson("declarationDidier12.json"); // BEN2 - F 2022
    processMessageFromJson("declarationDidier13.json"); // BEN2 - F 2023 01/01 07/21 V1.50
    processMessageFromJson("declarationDidier14.json"); // BEN2 - V 2023 07/21 12/31 V1.61

    processMessageFromJson("declarationDidier19.json"); // BEN2 - F 2021
    processMessageFromJson("declarationDidier20.json"); // BEN2 - F 2022
    processMessageFromJson("declarationDidier21.json"); // BEN2 - F 2023 01/01 07/21 V1.50
    processMessageFromJson("declarationDidier22.json"); // BEN2 - F 2023 07/21 07/25 V1.61
    processMessageFromJson("declarationDidier23.json"); // BEN2 - V 2023 07/25 12/31 V1.61

    ContractTP contratNatPrestOnline = contratDao.getContract("0000401166", "02247565", "02247565");
    Assertions.assertNotNull(contratNatPrestOnline);

    List<DomaineDroitContractTP> domaineDroits =
        contratNatPrestOnline.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(11, domaineDroits.size());
    Assertions.assertEquals("OPAU", domaineDroits.get(0).getCode());
    DomaineDroitContractTP contratOpau = domaineDroits.get(0);
    Assertions.assertEquals(
        2,
        contratOpau
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .size());

    // Il doit y avoir
    // 2 périodes (OFF+ON) du 01/01/2021 au 31/12/2022
    // 2 périodes (OFF+ON) du 01/01/2023 au 21/07/2023
    // 1 périodes OFF du 21/07/2023 au 31/12/2023
    // 1 périodes ON du 21/07/2023 au null
    // 1 période ON du 01/01/2024 au null

    PeriodeDroitContractTP contratOpauPeriode1 =
        contratOpau
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(0);
    PeriodeDroitContractTP periodeAssert_contratOpauPeriode1 = new PeriodeDroitContractTP();
    periodeAssert_contratOpauPeriode1.setTypePeriode(TypePeriode.ONLINE);
    periodeAssert_contratOpauPeriode1.setPeriodeDebut("2021/01/01");
    periodeAssert_contratOpauPeriode1.setPeriodeFin(null);
    periodeAssert_contratOpauPeriode1.setPeriodeFinFermeture(null);
    assertPeriodEquals(periodeAssert_contratOpauPeriode1, contratOpauPeriode1);

    PeriodeDroitContractTP contratOpauPeriode2 =
        contratOpau
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(1);
    PeriodeDroitContractTP periodeAssert_contratOpauPeriode2 = new PeriodeDroitContractTP();
    periodeAssert_contratOpauPeriode2.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contratOpauPeriode2.setPeriodeDebut("2021/01/01");
    periodeAssert_contratOpauPeriode2.setPeriodeFin("2023/12/31");
    assertPeriodEquals(periodeAssert_contratOpauPeriode2, contratOpauPeriode2);
  }

  @Test
  void consolidatePeriodeDecoupeAdjonction() throws IOException {
    processMessageFromJson("declaration5516-1.json"); // BEN1 - 2023
    processMessageFromJson("declaration5516-2.json"); // BEN1 - 2023
    processMessageFromJson("declaration5516-3.json"); // BEN1 - 2023 découpé
    processMessageFromJson("declaration5516-4.json"); // BEN2 - 2023 à partir de l'adjonction

    ContractTP contratNatPrestOnline = contratDao.getContract("0000401166", "93000808", "93000808");
    Assertions.assertNotNull(contratNatPrestOnline);

    Assertions.assertEquals(2, contratNatPrestOnline.getBeneficiaires().size());
    List<DomaineDroitContractTP> domaineDroits =
        contratNatPrestOnline.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(1, domaineDroits.size());
    Assertions.assertEquals("OPAU", domaineDroits.get(0).getCode());
    DomaineDroitContractTP domaineDroitContract = domaineDroits.get(0);
    Assertions.assertEquals(
        2,
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .size());

    PeriodeDroitContractTP contratOpauPeriode1 =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(0);
    PeriodeDroitContractTP periodeAssert_contratOpauPeriode1 = new PeriodeDroitContractTP();
    periodeAssert_contratOpauPeriode1.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contratOpauPeriode1.setPeriodeDebut("2023/01/01");
    periodeAssert_contratOpauPeriode1.setPeriodeFin("2023/12/31");
    periodeAssert_contratOpauPeriode1.setPeriodeFinFermeture(null);
    assertPeriodEquals(periodeAssert_contratOpauPeriode1, contratOpauPeriode1);

    PeriodeDroitContractTP contratOpauPeriode2 =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(1);
    PeriodeDroitContractTP periodeAssert_contratOpauPeriode2 = new PeriodeDroitContractTP();
    periodeAssert_contratOpauPeriode2.setTypePeriode(TypePeriode.ONLINE);
    periodeAssert_contratOpauPeriode2.setPeriodeDebut("2023/01/01");
    periodeAssert_contratOpauPeriode2.setPeriodeFin(null);
    periodeAssert_contratOpauPeriode2.setPeriodeFinFermeture(null);
    assertPeriodEquals(periodeAssert_contratOpauPeriode2, contratOpauPeriode2);

    contratOpauPeriode1 =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(1);
    periodeAssert_contratOpauPeriode1 = new PeriodeDroitContractTP();
    periodeAssert_contratOpauPeriode1.setTypePeriode(TypePeriode.ONLINE);
    periodeAssert_contratOpauPeriode1.setPeriodeDebut("2023/01/01");
    periodeAssert_contratOpauPeriode1.setPeriodeFinFermeture(null);
    assertPeriodEquals(periodeAssert_contratOpauPeriode1, contratOpauPeriode1);

    // Benef 2
    domaineDroits = contratNatPrestOnline.getBeneficiaires().get(1).getDomaineDroits();
    Assertions.assertEquals(1, domaineDroits.size());
    Assertions.assertEquals("OPAU", domaineDroits.get(0).getCode());
    domaineDroitContract = domaineDroits.get(0);
    Assertions.assertEquals(
        2,
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .size());

    contratOpauPeriode1 =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(0);
    periodeAssert_contratOpauPeriode1 = new PeriodeDroitContractTP();
    periodeAssert_contratOpauPeriode1.setTypePeriode(TypePeriode.ONLINE);
    periodeAssert_contratOpauPeriode1.setPeriodeDebut("2023/09/01");
    periodeAssert_contratOpauPeriode1.setPeriodeFinFermeture(null);
    assertPeriodEquals(periodeAssert_contratOpauPeriode1, contratOpauPeriode1);

    contratOpauPeriode2 =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(1);
    periodeAssert_contratOpauPeriode2 = new PeriodeDroitContractTP();
    periodeAssert_contratOpauPeriode2.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contratOpauPeriode2.setPeriodeDebut("2023/09/01");
    periodeAssert_contratOpauPeriode2.setPeriodeFin("2023/12/31");
    periodeAssert_contratOpauPeriode2.setPeriodeFinFermeture(null);
    assertPeriodEquals(periodeAssert_contratOpauPeriode2, contratOpauPeriode2);
  }

  @Test
  void consolidePeriodeDecoupeAdjonctionAvecResiliationAvant() throws IOException {
    processMessageFromJson("declaration5516-resil-1.json"); // BEN1 - 2023 ouverture
    processMessageFromJson("declaration5516-resil-2.json"); // BEN1 - 2023 ouverture decoupé
    processMessageFromJson("declaration5516-resil-4.json"); // BEN2 - 2023 à partir de l'adjonction
    processMessageFromJson("declaration5516-resil-3.json"); // BEN1 - 2023 fermeture découpé
    processMessageFromJson("declaration5516-resil-5.json"); // BEN2 - fermeture

    ContractTP contratNatPrestOnline = contratDao.getContract("0000401166", "93000808", "93000808");
    Assertions.assertNotNull(contratNatPrestOnline);

    Assertions.assertEquals(2, contratNatPrestOnline.getBeneficiaires().size());
    List<DomaineDroitContractTP> domaineDroits =
        contratNatPrestOnline.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(1, domaineDroits.size());
    Assertions.assertEquals("OPAU", domaineDroits.get(0).getCode());
    DomaineDroitContractTP domaineDroitContract = domaineDroits.get(0);
    Assertions.assertEquals(
        2,
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .size());

    PeriodeDroitContractTP periodeAssert_contratOpauPeriode1;
    PeriodeDroitContractTP contratOpauPeriode1 =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(0);
    periodeAssert_contratOpauPeriode1 = new PeriodeDroitContractTP();
    periodeAssert_contratOpauPeriode1.setTypePeriode(TypePeriode.ONLINE);
    periodeAssert_contratOpauPeriode1.setPeriodeDebut("2023/01/01");
    periodeAssert_contratOpauPeriode1.setPeriodeFin("2023/06/01");
    periodeAssert_contratOpauPeriode1.setPeriodeFinFermeture(null);
    assertPeriodEquals(periodeAssert_contratOpauPeriode1, contratOpauPeriode1);

    PeriodeDroitContractTP periodeAssert_contratOpauPeriode2;
    PeriodeDroitContractTP contratOpauPeriode2 =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(1);
    periodeAssert_contratOpauPeriode2 = new PeriodeDroitContractTP();
    periodeAssert_contratOpauPeriode2.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contratOpauPeriode2.setPeriodeDebut("2023/01/01");
    periodeAssert_contratOpauPeriode2.setPeriodeFin("2023/06/01");
    periodeAssert_contratOpauPeriode2.setPeriodeFinFermeture("2023/12/31");
    assertPeriodEquals(periodeAssert_contratOpauPeriode2, contratOpauPeriode2);
    // Benef 2
    domaineDroits = contratNatPrestOnline.getBeneficiaires().get(1).getDomaineDroits();
    Assertions.assertEquals(1, domaineDroits.size());
    Assertions.assertEquals("OPAU", domaineDroits.get(0).getCode());
    domaineDroitContract = domaineDroits.get(0);
    Assertions.assertEquals(
        1,
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .size());

    contratOpauPeriode2 =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(0);
    periodeAssert_contratOpauPeriode2 = new PeriodeDroitContractTP();
    periodeAssert_contratOpauPeriode2.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contratOpauPeriode2.setPeriodeDebut("2023/09/01");
    periodeAssert_contratOpauPeriode2.setPeriodeFin("2023/06/01");
    periodeAssert_contratOpauPeriode2.setPeriodeFinFermeture("2023/12/31");
    assertPeriodEquals(periodeAssert_contratOpauPeriode2, contratOpauPeriode2);
  }

  @Test
  void consolidePeriodeDecoupeAdjonctionAvecResiliationPendant() throws IOException {
    processMessageFromJson("declaration5516-resil-1.json"); // BEN1 - 2023
    ContractTP contratNatPrestOnline = contratDao.getContract("0000401166", "93000808", "93000808");
    processMessageFromJson("declaration5516-resilapres-2.json"); // BEN1 - 2023 ouverture decoupé
    contratNatPrestOnline = contratDao.getContract("0000401166", "93000808", "93000808");
    processMessageFromJson("declaration5516-resilapres-3.json"); // BEN1 - 2023 fermeture découpé
    contratNatPrestOnline = contratDao.getContract("0000401166", "93000808", "93000808");
    processMessageFromJson(
        "declaration5516-resilapres-4.json"); // BEN2 - 2023 à partir de l'adjonction
    contratNatPrestOnline = contratDao.getContract("0000401166", "93000808", "93000808");
    processMessageFromJson("declaration5516-resilapres-5.json"); // BEN2 - fermeture
    contratNatPrestOnline = contratDao.getContract("0000401166", "93000808", "93000808");

    Assertions.assertNotNull(contratNatPrestOnline);

    Assertions.assertEquals(2, contratNatPrestOnline.getBeneficiaires().size());
    List<DomaineDroitContractTP> domaineDroits =
        contratNatPrestOnline.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(1, domaineDroits.size());
    Assertions.assertEquals("OPAU", domaineDroits.get(0).getCode());
    DomaineDroitContractTP domaineDroitContract = domaineDroits.get(0);
    Assertions.assertEquals(
        2,
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .size());

    PeriodeDroitContractTP contratOpauPeriode1 =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(0);
    PeriodeDroitContractTP periodeAssert_contratOpauPeriode1 = new PeriodeDroitContractTP();
    periodeAssert_contratOpauPeriode1.setTypePeriode(TypePeriode.ONLINE);
    periodeAssert_contratOpauPeriode1.setPeriodeDebut("2023/01/01");
    periodeAssert_contratOpauPeriode1.setPeriodeFin("2023/10/01");
    periodeAssert_contratOpauPeriode1.setPeriodeFinFermeture(null);
    assertPeriodEquals(periodeAssert_contratOpauPeriode1, contratOpauPeriode1);

    PeriodeDroitContractTP contratOpauPeriode2 =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(1);
    PeriodeDroitContractTP periodeAssert_contratOpauPeriode2 = new PeriodeDroitContractTP();
    periodeAssert_contratOpauPeriode2.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contratOpauPeriode2.setPeriodeDebut("2023/01/01");
    periodeAssert_contratOpauPeriode2.setPeriodeFin("2023/10/01");
    periodeAssert_contratOpauPeriode2.setPeriodeFinFermeture("2023/12/31");
    assertPeriodEquals(periodeAssert_contratOpauPeriode2, contratOpauPeriode2);
    // Benef 2
    domaineDroits = contratNatPrestOnline.getBeneficiaires().get(1).getDomaineDroits();
    Assertions.assertEquals(1, domaineDroits.size());
    Assertions.assertEquals("OPAU", domaineDroits.get(0).getCode());
    domaineDroitContract = domaineDroits.get(0);
    Assertions.assertEquals(
        1,
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .size());

    contratOpauPeriode1 =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(0);
    periodeAssert_contratOpauPeriode1 = new PeriodeDroitContractTP();
    periodeAssert_contratOpauPeriode1.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contratOpauPeriode1.setPeriodeDebut("2023/09/01");
    periodeAssert_contratOpauPeriode1.setPeriodeFin("2023/06/01");
    periodeAssert_contratOpauPeriode1.setPeriodeFinFermeture("2023/12/31");
    assertPeriodEquals(periodeAssert_contratOpauPeriode1, contratOpauPeriode1);
  }

  @Test
  void consolide5894() throws IOException {
    processMessageFromJson("declaration5894-1.json");
    processMessageFromJson("declaration5894-2.json");
    processMessageFromJson("declaration5894-3.json");
    processMessageFromJson("declaration5894-4.json");
    processMessageFromJson("declaration5894-5.json");
    processMessageFromJson("declaration5894-6.json");

    ContractTP contratNatPrestOnline = contratDao.getContract("0000401166", "93000808", "93000808");
    Assertions.assertNotNull(contratNatPrestOnline);

    processMessageFromJson("declaration5894-7.json");

    contratNatPrestOnline = contratDao.getContract("0000401166", "93000808", "93000808");
    Assertions.assertNotNull(contratNatPrestOnline);

    Assertions.assertEquals(1, contratNatPrestOnline.getBeneficiaires().size());
    List<DomaineDroitContractTP> domaineDroits =
        contratNatPrestOnline.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(2, domaineDroits.size());
    Assertions.assertEquals("OPAU", domaineDroits.get(0).getCode());
    Assertions.assertEquals("HOSP", domaineDroits.get(1).getCode());
    DomaineDroitContractTP domaineDroitContract = domaineDroits.get(0);
    Assertions.assertEquals(
        2,
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .size());

    PeriodeDroitContractTP contratOpauPeriode1 =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(0);
    PeriodeDroitContractTP periodeAssert_contratOpauPeriode1 = new PeriodeDroitContractTP();
    periodeAssert_contratOpauPeriode1.setTypePeriode(TypePeriode.ONLINE);
    periodeAssert_contratOpauPeriode1.setPeriodeDebut("2024/01/01");
    periodeAssert_contratOpauPeriode1.setPeriodeFin("2025/05/05");
    periodeAssert_contratOpauPeriode1.setPeriodeFinFermeture(null);
    assertPeriodEquals(periodeAssert_contratOpauPeriode1, contratOpauPeriode1);

    PeriodeDroitContractTP periodeAssert_contratOpauPeriode2 = new PeriodeDroitContractTP();
    PeriodeDroitContractTP contratOpauPeriode2 =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(1);
    periodeAssert_contratOpauPeriode2.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contratOpauPeriode2.setPeriodeDebut("2024/01/01");
    periodeAssert_contratOpauPeriode2.setPeriodeFin("2025/05/05");
    periodeAssert_contratOpauPeriode2.setPeriodeFinFermeture("2025/12/31");
    assertPeriodEquals(periodeAssert_contratOpauPeriode2, contratOpauPeriode2);
  }

  @Test
  void testCaseConsolidationPeriodeOffline6476() throws IOException {
    // ouverture periode du 2024/01/01 au 2024/12/31 GT_BASE2
    processMessageFromJson("declaration6476-1V.json");
    // fermeture periode du 2024/01/01 au 2024/12/31 GT_BASE2
    processMessageFromJson("declaration6476-R.json");
    // réouverture GT_BASE2 periode du 2024/04/01 au 2024/12/31 + GT_REMP2 periode
    // du 2024/01/01 au 2024/03/31
    processMessageFromJson("declaration6476-2V.json");

    ContractTP contrat6476 = contratDao.getContract("0000401166", "MBA0003", "MBA0003");
    Assertions.assertNotNull(contrat6476);
    Assertions.assertEquals(1, contrat6476.getBeneficiaires().size());

    List<DomaineDroitContractTP> domaineDroits =
        contrat6476.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(1, domaineDroits.size());
    DomaineDroitContractTP domaineDroitContract = domaineDroits.get(0);
    Assertions.assertEquals(
        2,
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .size());
    Assertions.assertEquals(
        2,
        domaineDroitContract
            .getGaranties()
            .get(1)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .size());
    PeriodeDroitContractTP contratPeriodeBase =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(0);

    PeriodeDroitContractTP periodeAssert_contrat6476 = new PeriodeDroitContractTP();
    periodeAssert_contrat6476.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contrat6476.setPeriodeDebut("2024/01/01");
    periodeAssert_contrat6476.setPeriodeFin("2024/12/31");
    periodeAssert_contrat6476.setPeriodeFinFermeture(null);
    assertPeriodEquals(periodeAssert_contrat6476, contratPeriodeBase);

    PeriodeDroitContractTP contratPeriodeRemp =
        domaineDroitContract
            .getGaranties()
            .get(1)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(0);

    periodeAssert_contrat6476 = new PeriodeDroitContractTP();
    periodeAssert_contrat6476.setTypePeriode(TypePeriode.ONLINE);
    periodeAssert_contrat6476.setPeriodeDebut("2024/01/01");
    periodeAssert_contrat6476.setPeriodeFin("2024/03/31");
    periodeAssert_contrat6476.setPeriodeFinFermeture(null);
    assertPeriodEquals(periodeAssert_contrat6476, contratPeriodeRemp);
  }

  @Test
  void testPeriodeFermetureResilFutur() throws IOException {
    processMessageFromJson("declarationResilFutur1.json");
    processMessageFromJson("declarationResilFutur2.json");
    ContractTP contrat = contratDao.getContract("0000401166", "MBA1476", "MBA1476");

    List<DomaineDroitContractTP> domaineDroits =
        contrat.getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(2, domaineDroits.size());
    Assertions.assertEquals("OPAU", domaineDroits.get(0).getCode());
    Assertions.assertEquals("HOSP", domaineDroits.get(1).getCode());
    DomaineDroitContractTP domaineDroitContract = domaineDroits.get(0);

    PeriodeDroitContractTP contratOpauPeriode1 =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(0);
    PeriodeDroitContractTP periodeAssert_contratOpauPeriode1 = new PeriodeDroitContractTP();
    periodeAssert_contratOpauPeriode1.setTypePeriode(TypePeriode.ONLINE);
    periodeAssert_contratOpauPeriode1.setPeriodeDebut("2024/01/01");
    periodeAssert_contratOpauPeriode1.setPeriodeFin("2025/11/25");
    assertPeriodEquals(periodeAssert_contratOpauPeriode1, contratOpauPeriode1);

    PeriodeDroitContractTP contratOpauPeriode2 =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit()
            .get(1);
    PeriodeDroitContractTP periodeAssert_contratOpauPeriode2 = new PeriodeDroitContractTP();
    periodeAssert_contratOpauPeriode2.setTypePeriode(TypePeriode.OFFLINE);
    periodeAssert_contratOpauPeriode2.setPeriodeDebut("2024/01/01");
    periodeAssert_contratOpauPeriode2.setPeriodeFin("2024/12/31");
    assertPeriodEquals(periodeAssert_contratOpauPeriode2, contratOpauPeriode2);
  }

  @Test
  void test6757AncienneFermetureSansDateFermeture() throws IOException {
    processMessageFromJson("ancienneDecl6757-01.json");
    processMessageFromJson("ancienneDecl6757-02.json");

    ContractTP contrat = contratDao.getContract("0002420008", "00000239671550010005", "02396715");
    Assertions.assertNotNull(contrat);
    List<PeriodeDroitContractTP> periodes =
        contrat
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
            .getPeriodesDroit();
    Assertions.assertEquals(1, periodes.size());
    PeriodeDroitContractTP periode = periodes.get(0);
    Assertions.assertEquals("2019/12/02", periode.getPeriodeDebut());
    Assertions.assertEquals("2020/03/31", periode.getPeriodeFin());
    Assertions.assertEquals("2035/12/31", periode.getPeriodeFinFermeture());
  }

  @Test
  void test6756DoublePeriodeFermetureUnDroit() throws IOException {
    processMessageFromJson("decl6756-1.json");
    processMessageFromJson("decl6756-2.json");
    processMessageFromJson("decl6756-3.json");
    processMessageFromJson("decl6756-4.json");
    processMessageFromJson("decl6756-5.json");

    ContractTP contrat = contratDao.getContract("0000401166", "0171054", "0171054");

    Assertions.assertNotNull(contrat);
    List<PeriodeDroitContractTP> periodes =
        contrat
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
            .getPeriodesDroit();
    Assertions.assertEquals(2, periodes.size());

    PeriodeDroitContractTP online = periodes.get(0);
    Assertions.assertEquals(TypePeriode.ONLINE, online.getTypePeriode());
    Assertions.assertEquals("2022/01/01", online.getPeriodeDebut());
    Assertions.assertNull(online.getPeriodeFin());

    PeriodeDroitContractTP offline = periodes.get(1);
    Assertions.assertEquals(TypePeriode.OFFLINE, offline.getTypePeriode());
    Assertions.assertEquals("2022/01/01", offline.getPeriodeDebut());
    Assertions.assertEquals("2024/12/31", offline.getPeriodeFin());

    // On ferme le droit au 2024/01/01 avec deux periodeDroitDeclaration
    // allant de 2024/01/01->2024/05/31 et 2024/06/01->2024/12/31
    processMessageFromJson("decl6756-6.json");

    contrat = contratDao.getContract("0000401166", "0171054", "0171054");

    Assertions.assertNotNull(contrat);
    periodes =
        contrat
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
            .getPeriodesDroit();

    Assertions.assertEquals(2, periodes.size());

    online = periodes.get(0);
    Assertions.assertEquals(TypePeriode.ONLINE, online.getTypePeriode());
    Assertions.assertEquals("2022/01/01", online.getPeriodeDebut());
    Assertions.assertEquals("2023/12/31", online.getPeriodeFin());

    offline = periodes.get(1);
    Assertions.assertEquals(TypePeriode.OFFLINE, offline.getTypePeriode());
    Assertions.assertEquals("2022/01/01", offline.getPeriodeDebut());
    Assertions.assertEquals("2023/12/31", offline.getPeriodeFin());
    Assertions.assertEquals("2024/12/31", offline.getPeriodeFinFermeture());
  }

  @Test
  void testSansEffetPasTouteAnnee6757() throws IOException {
    processMessageFromJson("decl6757-1.json");
    processMessageFromJson("decl6757-2.json");
    // Sans effet sur l annee 2024
    processMessageFromJson("decl6757-3.json");

    ContractTP contrat = contratDao.getContract("0080996135", "S2348323", "S2348323");

    Assertions.assertNotNull(contrat);
    List<PeriodeDroitContractTP> periodes =
        contrat
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
            .getPeriodesDroit();

    Assertions.assertEquals(2, periodes.size());

    PeriodeDroitContractTP online = periodes.get(0);
    Assertions.assertEquals(TypePeriode.ONLINE, online.getTypePeriode());
    Assertions.assertEquals("2023/08/01", online.getPeriodeDebut());
    Assertions.assertEquals("2023/12/31", online.getPeriodeFin());

    PeriodeDroitContractTP offline = periodes.get(1);
    Assertions.assertEquals(TypePeriode.OFFLINE, offline.getTypePeriode());
    Assertions.assertEquals("2023/08/01", offline.getPeriodeDebut());
    Assertions.assertEquals("2023/12/31", offline.getPeriodeFin());
    Assertions.assertEquals("2023/12/31", offline.getPeriodeFinFermeture());
  }

  @Test
  void consolidateFermetureSansPeriodeFermeture() throws IOException {
    processMessageFromJson("6874/declaration2019.json"); // V 2019
    processMessageFromJson("6874/declaration2019Fermeture.json"); // F 2019
    processMessageFromJson("6874/declaration2020.json"); // V 2020
    ContractTP contrat = contratDao.getContract("0000920101", "00695745", "00695745");
    Assertions.assertNotNull(contrat);
    List<PeriodeDroitContractTP> periodes =
        contrat
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
            .getPeriodesDroit();

    Assertions.assertEquals(2, periodes.size());

    Assertions.assertEquals(TypePeriode.OFFLINE, periodes.get(0).getTypePeriode());
    Assertions.assertEquals("2019/07/01", periodes.get(0).getPeriodeDebut());
    Assertions.assertEquals("2019/07/19", periodes.get(0).getPeriodeFin());
    Assertions.assertEquals("2019/12/31", periodes.get(0).getPeriodeFinFermeture());

    Assertions.assertEquals(TypePeriode.OFFLINE, periodes.get(1).getTypePeriode());
    Assertions.assertEquals("2020/01/01", periodes.get(1).getPeriodeDebut());
    Assertions.assertEquals("2020/12/31", periodes.get(1).getPeriodeFin());
    Assertions.assertNull(periodes.get(1).getPeriodeFinFermeture());

    processMessageFromJson("6874/declaration2020SansEffet.json"); // F 2020 Sans effet

    contrat = contratDao.getContract("0000920101", "00695745", "00695745");
    periodes =
        contrat
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
            .getPeriodesDroit();

    Assertions.assertEquals(2, periodes.size());

    Assertions.assertEquals(TypePeriode.OFFLINE, periodes.get(0).getTypePeriode());
    Assertions.assertEquals("2019/07/01", periodes.get(0).getPeriodeDebut());
    Assertions.assertEquals("2019/07/19", periodes.get(0).getPeriodeFin());
    Assertions.assertEquals("2019/12/31", periodes.get(0).getPeriodeFinFermeture());

    Assertions.assertEquals(TypePeriode.OFFLINE, periodes.get(1).getTypePeriode());
    Assertions.assertEquals("2020/01/01", periodes.get(1).getPeriodeDebut());
    Assertions.assertEquals("2019/12/31", periodes.get(1).getPeriodeFin());
    Assertions.assertEquals("2020/12/31", periodes.get(1).getPeriodeFinFermeture());
  }

  @Test
  void consolidateFermetureSansPeriodeFermetureAonDoubleFermeture() throws IOException {
    processMessageFromJson("6874/declarationAonOuverture.json"); // V 2020
    processMessageFromJson("6874/declarationAonF1.json"); // 1ere fermeture
    processMessageFromJson(
        "6874/declarationAonF2.json"); // 2eme fermeture (pourquoi ça chevauche pas)
    ContractTP contrat = contratDao.getContract("0000920101", "00695745", "00695745");
    Assertions.assertNotNull(contrat);

    List<PeriodeDroitContractTP> periodes =
        contrat
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
            .getPeriodesDroit();

    Assertions.assertEquals(TypePeriode.OFFLINE, periodes.get(0).getTypePeriode());
    Assertions.assertEquals("2020/01/01", periodes.get(0).getPeriodeDebut());
    Assertions.assertEquals("2020/03/19", periodes.get(0).getPeriodeFin());
    Assertions.assertEquals("2020/12/31", periodes.get(0).getPeriodeFinFermeture());
  }

  @Test
  void test6921() throws IOException {
    processMessageFromJson("6921/decl1.json");
    processMessageFromJson("6921/decl2.json");
    ContractTP contract = contratDao.getContract("0000402925", "10037227", "10037227");
    Assertions.assertNotNull(contract);
    processMessageFromJson("6921/decl3.json");
    contract = contratDao.getContract("0000402925", "10037227", "10037227");
    Assertions.assertNull(contract);
  }
}
