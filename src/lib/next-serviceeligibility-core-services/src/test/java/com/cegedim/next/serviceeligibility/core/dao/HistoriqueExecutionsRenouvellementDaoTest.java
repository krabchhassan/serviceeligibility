package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecutionsRenouvellement;
import java.util.Date;
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
class HistoriqueExecutionsRenouvellementDaoTest {
  @Autowired private HistoriqueExecutionsRenouvellementDaoImpl dao;

  @Autowired MongoTemplate mongoTemplate;

  @Test
  void shouldCreateAndGetHistorique() {
    HistoriqueExecutionsRenouvellement histoGet = dao.getLastExecution();
    Assertions.assertNull(histoGet);

    HistoriqueExecutionsRenouvellement histo = new HistoriqueExecutionsRenouvellement();
    histo.setDateCreation(new Date());
    histo.setDateExecution(new Date());
    histo.setDateTraitement("2020-01-01");
    histo.setNombreTriggeredBeneficiariesCreesAnniversaire(10);
    histo.setNombreTriggeredBeneficiariesCreesRenouvellement(20);
    histo.setNombreTriggersCreesAnniversaire(2);
    histo.setNombreTriggersCreesRenouvellement(3);
    dao.create(histo);

    Mockito.when(
            mongoTemplate.find(
                Mockito.any(Query.class), Mockito.eq(HistoriqueExecutionsRenouvellement.class)))
        .thenReturn(List.of(histo));
    histoGet = dao.getLastExecution();
    Assertions.assertEquals("2020-01-01", histoGet.getDateTraitement());
    Assertions.assertEquals(10, histoGet.getNombreTriggeredBeneficiariesCreesAnniversaire());
    Assertions.assertEquals(20, histoGet.getNombreTriggeredBeneficiariesCreesRenouvellement());
    Assertions.assertEquals(2, histoGet.getNombreTriggersCreesAnniversaire());
    Assertions.assertEquals(3, histoGet.getNombreTriggersCreesRenouvellement());

    histo = new HistoriqueExecutionsRenouvellement();
    histo.setDateCreation(new Date());
    histo.setDateExecution(new Date());
    histo.setDateTraitement("2020-03-01");
    histo.setNombreTriggeredBeneficiariesCreesAnniversaire(40);
    histo.setNombreTriggeredBeneficiariesCreesRenouvellement(150);
    histo.setNombreTriggersCreesAnniversaire(20);
    histo.setNombreTriggersCreesRenouvellement(5);
    dao.create(histo);

    Mockito.when(
            mongoTemplate.find(
                Mockito.any(Query.class), Mockito.eq(HistoriqueExecutionsRenouvellement.class)))
        .thenReturn(List.of(histo));
    histoGet = dao.getLastExecution();
    Assertions.assertEquals("2020-03-01", histoGet.getDateTraitement());
    Assertions.assertEquals(40, histoGet.getNombreTriggeredBeneficiariesCreesAnniversaire());
    Assertions.assertEquals(150, histoGet.getNombreTriggeredBeneficiariesCreesRenouvellement());
    Assertions.assertEquals(20, histoGet.getNombreTriggersCreesAnniversaire());
    Assertions.assertEquals(5, histoGet.getNombreTriggersCreesRenouvellement());
  }
}
