package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.PeriodeDroit;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.utils.DeclarationConsolideUtils;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class DomaineDroitServiceTest {

  @Test
  void shouldReturnMinDebutPeriodeAndMaxFinPeriode() {
    Declaration declaration = new Declaration();
    DomaineDroit domaineDroit = new DomaineDroit();
    PeriodeDroit periodeDroit = new PeriodeDroit();
    periodeDroit.setPeriodeDebut("2024/01/01");
    periodeDroit.setPeriodeFin("2024/12/31");
    domaineDroit.setCode("RADL");

    DomaineDroit domaineDroit2 = new DomaineDroit();
    PeriodeDroit periodeDroit2 = new PeriodeDroit();
    periodeDroit2.setPeriodeDebut("2023/01/01");
    periodeDroit2.setPeriodeFin("2023/12/31");
    domaineDroit.setPeriodeDroit(periodeDroit);
    domaineDroit2.setPeriodeDroit(periodeDroit2);

    declaration.setDomaineDroits(List.of(domaineDroit, domaineDroit2));

    PeriodeDroit actual =
        DeclarationConsolideUtils.getMinMaxPeriodesDomaineDroit(declaration, true);
    Assertions.assertEquals("2023/01/01", actual.getPeriodeDebut());
    Assertions.assertEquals("2024/12/31", actual.getPeriodeFin());
  }
}
