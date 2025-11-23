package com.cegedim.next.serviceeligibility.extractbenefmultios.services;

import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.SocieteEmettrice;
import com.cegedim.next.serviceeligibility.extractbenefmultios.configs.config.TestConfiguration;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfiguration.class})
class ProcessorTest {

  @Autowired private Processor processor;

  @Test
  void noChevauchement() {
    List<SocieteEmettrice> societeEmettrices = new ArrayList<>();
    SocieteEmettrice societeEmettrice1 = new SocieteEmettrice();
    societeEmettrice1.setCode("1");
    Periode periode1 = new Periode();
    periode1.setDebut("2021-01-01");
    periode1.setFin("2021-12-31");
    societeEmettrice1.setPeriodes(List.of(periode1));
    societeEmettrices.add(societeEmettrice1);

    SocieteEmettrice societeEmettrice2 = new SocieteEmettrice();
    societeEmettrice2.setCode("2");
    Periode periode2 = new Periode();
    periode2.setDebut("2022-01-01");
    periode2.setFin("2022-12-31");
    societeEmettrice2.setPeriodes(List.of(periode2));
    societeEmettrices.add(societeEmettrice2);

    List<SocieteEmettrice> result = processor.extractSocietesEmettricesChevauche(societeEmettrices);
    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  void shouldChevauchement() {
    List<SocieteEmettrice> societeEmettrices = new ArrayList<>();
    SocieteEmettrice societeEmettrice1 = new SocieteEmettrice();
    societeEmettrice1.setCode("1");
    Periode periode1 = new Periode();
    periode1.setDebut("2021-01-01");
    periode1.setFin("9999-12-31");
    societeEmettrice1.setPeriodes(List.of(periode1));
    societeEmettrices.add(societeEmettrice1);

    SocieteEmettrice societeEmettrice2 = new SocieteEmettrice();
    societeEmettrice2.setCode("2");
    Periode periode2 = new Periode();
    periode2.setDebut("2022-01-01");
    periode2.setFin("2022-12-31");
    societeEmettrice2.setPeriodes(List.of(periode2));
    societeEmettrices.add(societeEmettrice2);

    List<SocieteEmettrice> result = processor.extractSocietesEmettricesChevauche(societeEmettrices);
    Assertions.assertFalse(result.isEmpty());
    Assertions.assertEquals(2, result.size());
  }

  @Test
  void bothNoAndShouldChevauchement() {
    List<SocieteEmettrice> societeEmettrices = new ArrayList<>();
    SocieteEmettrice societeEmettrice1 = new SocieteEmettrice();
    societeEmettrice1.setCode("1");
    Periode periode1 = new Periode();
    periode1.setDebut("2021-01-01");
    periode1.setFin("2021-12-31");
    societeEmettrice1.setPeriodes(List.of(periode1));
    societeEmettrices.add(societeEmettrice1);

    SocieteEmettrice societeEmettrice2 = new SocieteEmettrice();
    societeEmettrice2.setCode("2");
    Periode periode2 = new Periode();
    periode2.setDebut("2022-01-01");
    periode2.setFin("9999-12-31");
    societeEmettrice2.setPeriodes(List.of(periode2));
    societeEmettrices.add(societeEmettrice2);

    SocieteEmettrice societeEmettrice3 = new SocieteEmettrice();
    societeEmettrice3.setCode("3");
    Periode periode3 = new Periode();
    periode3.setDebut("2023-01-01");
    periode3.setFin("2023-12-31");
    societeEmettrice3.setPeriodes(List.of(periode3));
    societeEmettrices.add(societeEmettrice3);

    List<SocieteEmettrice> result = processor.extractSocietesEmettricesChevauche(societeEmettrices);
    Assertions.assertFalse(result.isEmpty());
    Assertions.assertEquals(2, result.size());
  }
}
