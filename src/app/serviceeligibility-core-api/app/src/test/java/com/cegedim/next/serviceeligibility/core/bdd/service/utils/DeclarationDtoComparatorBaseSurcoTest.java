package com.cegedim.next.serviceeligibility.core.bdd.service.utils;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ContratDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ConventionnementDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DomaineDroitDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.PrioriteDroitDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.DeclarationDtoComparatorBaseSurco;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DeclarationDtoComparatorBaseSurcoTest {

  @Test
  void should_compare_declaration_on_conventionnement() {
    String priorityCode = "code1";
    String contratNumber = "1";
    DeclarationDto o1Dto = new DeclarationDto();
    DeclarationDto o2Dto = new DeclarationDto();

    ContratDto c1 = new ContratDto();
    o1Dto.setContrat(c1);
    ContratDto c2 = new ContratDto();
    o2Dto.setContrat(c2);
    c1.setNumero(contratNumber);
    c2.setNumero(contratNumber);

    PrioriteDroitDto pd1 = new PrioriteDroitDto();
    PrioriteDroitDto pd2 = new PrioriteDroitDto();
    pd1.setCode(priorityCode);
    pd2.setCode(priorityCode);

    DomaineDroitDto dd1 = new DomaineDroitDto();
    List<DomaineDroitDto> benefs1 = new ArrayList<>();
    dd1.setPrioriteDroit(pd1);
    benefs1.add(dd1);
    o1Dto.setDomaineDroits(benefs1);

    DomaineDroitDto dd2 = new DomaineDroitDto();
    dd2.setPrioriteDroit(pd2);
    List<DomaineDroitDto> benefs2 = new ArrayList<>();
    benefs2.add(dd2);
    o2Dto.setDomaineDroits(benefs2);

    ConventionnementDto conv1 = new ConventionnementDto();
    conv1.setPriorite(1);
    List<ConventionnementDto> conv1List = new ArrayList<>();
    conv1List.add(conv1);
    dd1.setConventionnements(conv1List);

    ConventionnementDto conv2 = new ConventionnementDto();
    conv2.setPriorite(1);
    List<ConventionnementDto> conv2List = new ArrayList<>();
    conv2List.add(conv2);
    dd2.setConventionnements(conv2List);

    o1Dto.setEffetDebut(new Date());
    Date tomorrow = new Date();
    Calendar c = Calendar.getInstance();
    c.setTime(tomorrow);
    c.add(Calendar.DATE, 1);
    tomorrow = c.getTime();
    o2Dto.setEffetDebut(tomorrow);

    DeclarationDtoComparatorBaseSurco comparator = new DeclarationDtoComparatorBaseSurco(null);
    int result = comparator.compare(o1Dto, o2Dto);
    Assertions.assertEquals(-1, result);

    int result2 = comparator.compare(o2Dto, o1Dto);
    Assertions.assertEquals(1, result2);
  }
}
