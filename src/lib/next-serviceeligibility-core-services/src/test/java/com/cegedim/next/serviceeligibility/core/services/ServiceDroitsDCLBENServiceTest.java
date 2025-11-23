package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.entity.ServiceDroitsDCLBEN;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
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
class ServiceDroitsDCLBENServiceTest {

  @Autowired private ServiceDroitsDCLBENService service;

  @Autowired private MongoTemplate template;

  @Test
  void shouldGetServiceDroit() {
    ServiceDroitsDCLBEN serv = service.getServiceDroitsDCLBEN();
    Assertions.assertNull(serv);

    ServiceDroitsDCLBEN newServ = new ServiceDroitsDCLBEN();
    newServ.setCode(Constants.AIGUILLAGE);

    template.save(newServ, Constants.SERVICEDROITS);
    Mockito.when(
            template.findOne(
                Mockito.any(Query.class),
                Mockito.eq(ServiceDroitsDCLBEN.class),
                Mockito.anyString()))
        .thenReturn(newServ);
    serv = service.getServiceDroitsDCLBEN();
    Assertions.assertNotNull(serv);
    Assertions.assertEquals(Constants.AIGUILLAGE, serv.getCode());
  }
}
