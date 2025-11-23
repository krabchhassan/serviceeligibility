package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceDaoImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class TraceDaoTest {
  @Autowired private TraceDaoImpl dao;

  @Test
  void stage1_should_request_with_id() {
    try {
      dao.findOneBy(null, "collectionTest");
    } catch (IllegalArgumentException e) {
      Assertions.assertEquals("This method required an id", e.getLocalizedMessage());
    }
  }
}
