package com.cegedim.next.serviceeligibility.core.mapper;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.DroitsDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration.MapperDroits;
import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.PeriodeDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.PrioriteDroit;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class MapperDroitsTest {

  @Autowired private MapperDroits mapperDroits;

  @Test
  void testExcluCarteDemat() {
    DomaineDroit domaineDroit = new DomaineDroit();
    domaineDroit.setCode("PHAR");

    PeriodeDroit periode = new PeriodeDroit();
    periode.setPeriodeDebut("2025/01/01");
    periode.setPeriodeFin("2025/07/31");
    periode.setPeriodeFermetureDebut("2025/08/01");
    periode.setPeriodeFermetureFin("2025/12/31");
    domaineDroit.setPeriodeDroit(periode);

    PrioriteDroit priorite = new PrioriteDroit();
    priorite.setCode("1");
    domaineDroit.setPrioriteDroit(priorite);

    Declaration declaration = new Declaration();
    declaration.setDomaineDroits(List.of(domaineDroit));

    declaration.setCarteTPaEditerOuDigitale("2");
    DroitsDto droits = mapperDroits.entityToDto(declaration);
    Assertions.assertEquals("31/07/2025", droits.getPeriodeDroitOfflineFin());

    declaration.setCarteTPaEditerOuDigitale("3");
    droits = mapperDroits.entityToDto(declaration);
    Assertions.assertEquals("31/12/2025", droits.getPeriodeDroitOfflineFin());

    declaration.setCarteTPaEditerOuDigitale("4");
    droits = mapperDroits.entityToDto(declaration);
    Assertions.assertEquals("31/12/2025", droits.getPeriodeDroitOfflineFin());
  }
}
