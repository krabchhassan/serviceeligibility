package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.entity.Retention;
import com.cegedim.next.serviceeligibility.core.model.entity.RetentionStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class RetentionDaoImplTest {
  @Autowired RetentionDaoImpl retentionDao;

  @Test
  void getIds() {
    Retention r1 = new Retention();
    r1.setStatus(RetentionStatus.TO_PROCESS);
    r1.setReceptionDate(LocalDateTime.now());
    r1.setInsurerId("1");
    retentionDao.createRetention(r1);

    Retention r2 = new Retention();
    r2.setStatus(RetentionStatus.TO_PROCESS);
    r2.setReceptionDate(LocalDateTime.now());
    r2.setInsurerId("1");
    retentionDao.createRetention(r2);

    List<Retention> retentions = retentionDao.findAllByDelai("1", 0);
  }
}
