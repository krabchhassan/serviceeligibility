package com.cegedim.next.serviceeligibility.core.services.bdd;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.BenefInfos;
import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.SasContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.TriggerBenefs;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiary;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class SasContractServiceTestTP {

  @Autowired MongoTemplate mongoTemplate;

  @Autowired private SasContratService sasContratService;

  @Test
  void searchSas() {
    mongoTemplate.findAllAndRemove(new Query(), SasContrat.class, Constants.SAS_CONTRAT_COLLECTION);
    SasContrat firstSas = new SasContrat();
    firstSas.setIdDeclarant("2523263210");
    firstSas.setNumeroContrat("JUNIT-001");
    firstSas.setNumeroAdherent("1212343455656");
    firstSas.setAnomalies(List.of("Une anomalie JUNIT"));
    firstSas.setServicePrestationId("SP1");
    List<TriggerBenefs> triggersBenefs = new ArrayList<>();
    TriggerBenefs triggerBenefs = new TriggerBenefs();
    triggerBenefs.setTriggerId("111122223333");
    BenefInfos benefInfos1 = new BenefInfos();
    benefInfos1.setBenefId("22224444");
    BenefInfos benefInfos2 = new BenefInfos();
    benefInfos2.setBenefId("33334444");
    triggerBenefs.setBenefsInfos(List.of(benefInfos1, benefInfos2));
    triggersBenefs.add(triggerBenefs);
    firstSas.setTriggersBenefs(triggersBenefs);
    sasContratService.save(firstSas);

    SasContrat secondSas = new SasContrat();
    secondSas.setIdDeclarant("2523263210");
    secondSas.setNumeroContrat("JUNIT-002");
    secondSas.setNumeroAdherent("1212343455656");
    secondSas.setServicePrestationId("SP2");
    triggersBenefs = new ArrayList<>();
    triggerBenefs = new TriggerBenefs();
    triggerBenefs.setTriggerId("111122223333");
    BenefInfos benefInfos3 = new BenefInfos();
    benefInfos3.setBenefId("44445555");
    BenefInfos benefInfos4 = new BenefInfos();
    benefInfos4.setBenefId("55556666");
    triggerBenefs.setBenefsInfos(List.of(benefInfos3, benefInfos4));
    triggersBenefs.add(triggerBenefs);
    secondSas.setTriggersBenefs(triggersBenefs);
    sasContratService.save(secondSas);

    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(Query.class), Mockito.eq(SasContrat.class), Mockito.anyString()))
        .thenReturn(firstSas);
    SasContrat sas =
        sasContratService.getByFunctionalKey("2523263210", "JUNIT-001", "1212343455656");
    Assertions.assertNotNull(sas);
    Assertions.assertEquals("Une anomalie JUNIT", sas.getAnomalies().get(0));

    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(Query.class), Mockito.eq(SasContrat.class), Mockito.anyString()))
        .thenReturn(null);
    sas = sasContratService.getByFunctionalKey("3333333333", "JUNIT-002", "1212343455656");
    Assertions.assertNull(sas);

    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(Query.class), Mockito.eq(SasContrat.class), Mockito.anyString()))
        .thenReturn(firstSas);
    sas = sasContratService.getByServicePrestationId("SP1");
    Assertions.assertNotNull(sas);
    Assertions.assertEquals("Une anomalie JUNIT", sas.getAnomalies().get(0));

    Mockito.when(
            mongoTemplate.find(
                Mockito.any(Query.class), Mockito.eq(SasContrat.class), Mockito.anyString()))
        .thenReturn(List.of(firstSas, secondSas));
    List<SasContrat> sasContrats = sasContratService.getByIdTrigger("111122223333");
    Assertions.assertNotNull(sasContrats);
    sas = sasContrats.get(0);
    Assertions.assertEquals("2523263210", sas.getIdDeclarant());
    Assertions.assertEquals("JUNIT-001", sas.getNumeroContrat());
    sas = sasContrats.get(1);
    Assertions.assertEquals("2523263210", sas.getIdDeclarant());
    Assertions.assertEquals("JUNIT-002", sas.getNumeroContrat());

    sasContratService.delete(sas.getId());
    Mockito.when(
            mongoTemplate.find(
                Mockito.any(Query.class), Mockito.eq(SasContrat.class), Mockito.anyString()))
        .thenReturn(List.of(firstSas));
    sasContrats = sasContratService.getByIdTrigger("111122223333");
    Assertions.assertNotNull(sasContrats);
    Assertions.assertEquals(1, sasContrats.size());
  }

  @Test
  void manageSas() {
    TriggeredBeneficiary benef = new TriggeredBeneficiary();
    benef.setId("BENEF1");
    benef.setAmc("AMC1");
    benef.setNumeroContrat("CTT1");
    benef.setServicePrestationId("SP1");
    benef.setIdTrigger("TRG1");
    SasContrat sasContrat = sasContratService.manageSasContrat(null, benef, "ANO JUNIT");
    Assertions.assertNotNull(sasContrat);
    Assertions.assertEquals(1, sasContrat.getTriggersBenefs().size());
    Assertions.assertEquals(1, sasContrat.getTriggersBenefs().get(0).getBenefsInfos().size());
    Assertions.assertEquals(
        "BENEF1", sasContrat.getTriggersBenefs().get(0).getBenefsInfos().get(0).getBenefId());

    benef = new TriggeredBeneficiary();
    benef.setId("BENEF2");
    benef.setAmc("AMC1");
    benef.setNumeroContrat("CTT1");
    benef.setServicePrestationId("SP1");
    benef.setIdTrigger("TRG1");
    sasContrat = sasContratService.manageSasContrat(sasContrat, benef, "ANO JUNIT");
    Assertions.assertNotNull(sasContrat);
    Assertions.assertEquals(1, sasContrat.getTriggersBenefs().size());
    Assertions.assertEquals(2, sasContrat.getTriggersBenefs().get(0).getBenefsInfos().size());
    Assertions.assertEquals(
        "BENEF1", sasContrat.getTriggersBenefs().get(0).getBenefsInfos().get(0).getBenefId());
    Assertions.assertEquals(
        "BENEF2", sasContrat.getTriggersBenefs().get(0).getBenefsInfos().get(1).getBenefId());

    benef = new TriggeredBeneficiary();
    benef.setId("BENEF3");
    benef.setAmc("AMC1");
    benef.setNumeroContrat("CTT1");
    benef.setServicePrestationId("SP1");
    benef.setIdTrigger("TRG2");
    sasContrat = sasContratService.manageSasContrat(sasContrat, benef, "ANO JUNIT");
    Assertions.assertNotNull(sasContrat);
    Assertions.assertEquals(2, sasContrat.getTriggersBenefs().size());
    Assertions.assertEquals(1, sasContrat.getTriggersBenefs().get(1).getBenefsInfos().size());
    Assertions.assertEquals(
        "BENEF3", sasContrat.getTriggersBenefs().get(1).getBenefsInfos().get(0).getBenefId());
  }
}
