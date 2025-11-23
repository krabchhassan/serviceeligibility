package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.dao.TriggerDaoImpl;
import com.cegedim.next.serviceeligibility.core.kafka.dao.TriggerKafkaDao;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggerBatchUnitaire;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiary;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiaryStatusEnum;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.stream.Streams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class TriggerDaoTest {
  @Autowired private TriggerDaoImpl dao;

  @SpyBean private TriggerKafkaDao kafkaDao;

  @Autowired private MongoTemplate template;

  @Test
  void convert_date_test() {
    LocalDateTime dateTime = dao.getDateFromString("2021-06-15");
    String strDateTime = "2021-06-15T00:00:00";
    LocalDateTime expectedDateTime = LocalDateTime.parse(strDateTime);
    Assertions.assertEquals(expectedDateTime, dateTime);
  }

  @Test
  void getTriggerUnitaireForBatch() {
    Mockito.when(
            template.stream(
                Mockito.any(Query.class),
                Mockito.eq(TriggeredBeneficiary.class),
                Mockito.anyString()))
        .thenReturn(Streams.of(Collections.emptyIterator()));

    List<TriggerBatchUnitaire> triggersBatchUnitaire = new ArrayList<>();
    kafkaDao.getTriggersBatchUnitaireAndSend("12", 0L, triggersBatchUnitaire);
    Assertions.assertTrue(CollectionUtils.isEmpty(triggersBatchUnitaire));
    TriggeredBeneficiary tb1 = new TriggeredBeneficiary();
    tb1.setIdTrigger("trigger1");
    tb1.setServicePrestationId("sp1");
    tb1.setStatut(TriggeredBeneficiaryStatusEnum.ToProcess);
    template.save(tb1, Constants.TRIGGERED_BENEFICIARY);
    TriggeredBeneficiary tb2 = new TriggeredBeneficiary();
    tb2.setIdTrigger("trigger1");
    tb2.setServicePrestationId("sp1");
    tb2.setStatut(TriggeredBeneficiaryStatusEnum.ToProcess);
    template.save(tb2, Constants.TRIGGERED_BENEFICIARY);
    TriggeredBeneficiary tb3 = new TriggeredBeneficiary();
    tb3.setIdTrigger("trigger1");
    tb3.setServicePrestationId("sp2");
    tb3.setStatut(TriggeredBeneficiaryStatusEnum.ToProcess);
    template.save(tb3, Constants.TRIGGERED_BENEFICIARY);
    TriggeredBeneficiary tb4 = new TriggeredBeneficiary();
    tb4.setIdTrigger("trigger1");
    tb4.setServicePrestationId("sp3");
    tb4.setStatut(TriggeredBeneficiaryStatusEnum.ToProcess);
    template.save(tb4, Constants.TRIGGERED_BENEFICIARY);

    Mockito.when(
            template.stream(
                Mockito.any(Query.class),
                Mockito.eq(TriggeredBeneficiary.class),
                Mockito.anyString()))
        .thenReturn(Streams.of(List.of(tb1, tb2, tb3, tb4)));
    triggersBatchUnitaire = new ArrayList<>();
    kafkaDao.getTriggersBatchUnitaireAndSend("trigger1", 0L, triggersBatchUnitaire);
    Assertions.assertFalse(CollectionUtils.isEmpty(triggersBatchUnitaire));
    Assertions.assertEquals("sp1", triggersBatchUnitaire.get(0).getServicePrestationId());
  }
}
