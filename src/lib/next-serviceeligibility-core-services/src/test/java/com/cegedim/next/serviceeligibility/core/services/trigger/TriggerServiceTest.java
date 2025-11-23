package com.cegedim.next.serviceeligibility.core.services.trigger;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.*;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.ManageBenefsContract;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.Trigger;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggerStatus;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class TriggerServiceTest {

  @Autowired TriggerService triggerService;

  @Autowired MongoTemplate mongoTemplate;

  private static final String idTrigger = "id";

  @Test
  void updateStatusProcessed() {
    Trigger trigger = new Trigger();
    ManageBenefsContract manageBenefsContract = new ManageBenefsContract();
    manageBenefsContract.setNbBenefKO(0);
    manageBenefsContract.setNbBenefWarning(0);
    Mockito.when(mongoTemplate.findById(idTrigger, Trigger.class)).thenReturn(trigger);
    Mockito.when(mongoTemplate.save(trigger, Constants.TRIGGER)).thenReturn(trigger);
    trigger = triggerService.updateStatutTrigger(manageBenefsContract, trigger, false, 0L);
    Assertions.assertNotNull(trigger);
    Assertions.assertEquals(TriggerStatus.Processed, trigger.getStatus());
  }

  @Test
  void updateStatusProcessedWithError() {
    Trigger trigger = new Trigger();
    ManageBenefsContract manageBenefsContract = new ManageBenefsContract();
    manageBenefsContract.setNbBenefKO(1);
    Mockito.when(mongoTemplate.findById(idTrigger, Trigger.class)).thenReturn(trigger);
    Mockito.when(mongoTemplate.save(trigger, Constants.TRIGGER)).thenReturn(trigger);
    trigger = triggerService.updateStatutTrigger(manageBenefsContract, trigger, false, 0L);
    Assertions.assertNotNull(trigger);
    Assertions.assertEquals(TriggerStatus.ProcessedWithErrors, trigger.getStatus());
  }

  @Test
  void updateStatusProcessedWithError2() {
    Trigger trigger = new Trigger();
    ManageBenefsContract manageBenefsContract = new ManageBenefsContract();
    Mockito.when(mongoTemplate.findById(idTrigger, Trigger.class)).thenReturn(trigger);
    Mockito.when(mongoTemplate.save(trigger, Constants.TRIGGER)).thenReturn(trigger);
    trigger = triggerService.updateStatutTrigger(manageBenefsContract, trigger, true, 0L);
    Assertions.assertNotNull(trigger);
    Assertions.assertEquals(TriggerStatus.ProcessedWithErrors, trigger.getStatus());
  }

  @Test
  void updateStatusProcessedWithWarning() {
    Trigger trigger = new Trigger();
    ManageBenefsContract manageBenefsContract = new ManageBenefsContract();
    manageBenefsContract.setNbBenefKO(0);
    manageBenefsContract.setWarningBenef(true);
    Mockito.when(mongoTemplate.findById(idTrigger, Trigger.class)).thenReturn(trigger);
    Mockito.when(mongoTemplate.save(trigger, Constants.TRIGGER)).thenReturn(trigger);
    trigger = triggerService.updateStatutTrigger(manageBenefsContract, trigger, false, 0L);
    Assertions.assertNotNull(trigger);
    Assertions.assertEquals(TriggerStatus.ProcessedWithWarnings, trigger.getStatus());
  }
}
