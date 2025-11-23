package com.cegedim.next.serviceeligibility.core.bdd.service.utils;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.BeneficiaireDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DomaineDroitDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.PrioriteDroitDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.CarteTiersPayantUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CarteTiersPayantUtilsTest {

  private static final String CODE1 = "code1";

  @Test
  void should_set_props_for_contract_1() {

    String nirBeneficiaire = "myBeneficiaire";
    List<DeclarationDto> declarationList = new ArrayList<>();
    DeclarationDto dd = new DeclarationDto();
    declarationList.add(dd);
    DomaineDroitDto domainDto = new DomaineDroitDto();
    List<DomaineDroitDto> list = new ArrayList<>();
    list.add(domainDto);
    dd.setDomaineDroits(list);
    PrioriteDroitDto priority = new PrioriteDroitDto();
    priority.setCode(CODE1);
    priority.setNirPrio1(nirBeneficiaire);
    String contract2 = "prioContratNir1";
    priority.setPrioContratNir1(contract2);
    domainDto.setPrioriteDroit(priority);
    CarteTiersPayantUtils.setPrioriteCarteTiersPayant(declarationList, nirBeneficiaire);
    Assertions.assertEquals(dd.getDomaineDroits().get(0).getPrioriteDroit().getCode(), contract2);
    Assertions.assertEquals(
        dd.getDomaineDroits().get(0).getPrioriteDroit().getLibelle(), contract2);
  }

  @Test
  void should_set_props_for_contract_2() {

    String nirBeneficiaire = "myBeneficiaire";
    List<DeclarationDto> declarationList = new ArrayList<>();
    DeclarationDto dd = new DeclarationDto();
    declarationList.add(dd);
    DomaineDroitDto domainDto = new DomaineDroitDto();
    List<DomaineDroitDto> list = new ArrayList<>();
    list.add(domainDto);
    dd.setDomaineDroits(list);
    PrioriteDroitDto priority = new PrioriteDroitDto();
    priority.setCode(CODE1);
    priority.setNirPrio1("not matching benef");
    priority.setPrioContratNir1("prioContratNir1");

    priority.setNirPrio1(nirBeneficiaire);
    String contract2 = "prioContratNir2";
    priority.setPrioContratNir1(contract2);

    domainDto.setPrioriteDroit(priority);
    CarteTiersPayantUtils.setPrioriteCarteTiersPayant(declarationList, nirBeneficiaire);
    Assertions.assertEquals(dd.getDomaineDroits().get(0).getPrioriteDroit().getCode(), contract2);
    Assertions.assertEquals(
        dd.getDomaineDroits().get(0).getPrioriteDroit().getLibelle(), contract2);
  }

  @Test
  void should_create_map() {
    DeclarationDto dto = new DeclarationDto();
    BeneficiaireDto benefDto = new BeneficiaireDto();
    dto.setBeneficiaire(benefDto);
    benefDto.setNirOd1("part1");
    benefDto.setCleNirOd1("part2");
    benefDto.setDateNaissance("date1");
    benefDto.setRangNaissance("rang");
    List<DeclarationDto> declarations = new ArrayList<>();
    declarations.add(dto);
    Map<String, List<DeclarationDto>> result =
        CarteTiersPayantUtils.getDeclarationByBeneficiaire(declarations);
    Assertions.assertEquals(1, result.size());
    List<DeclarationDto> resultList = result.get("part1*part2*date1*rang");
    Assertions.assertNotNull(resultList);
  }

  @Test
  void should_remove_doubles() {
    List<DeclarationDto> declarationList = new ArrayList<>();

    DeclarationDto dd = new DeclarationDto();
    declarationList.add(dd);
    List<DomaineDroitDto> list = new ArrayList<>();
    dd.setDomaineDroits(list);

    DomaineDroitDto domainDto = new DomaineDroitDto();

    domainDto.setCode(CODE1);
    list.add(domainDto);

    PrioriteDroitDto priority = new PrioriteDroitDto();
    String priorityCode1 = "priorityCode1";
    priority.setCode(priorityCode1);
    domainDto.setPrioriteDroit(priority);

    DomaineDroitDto domainDto2 = new DomaineDroitDto();
    domainDto2.setCode(CODE1);
    list.add(domainDto2);

    PrioriteDroitDto priority2 = new PrioriteDroitDto();
    priority2.setCode(priorityCode1);
    domainDto2.setPrioriteDroit(priority2);
    Assertions.assertEquals(2, declarationList.get(0).getDomaineDroits().size());
    CarteTiersPayantUtils.checkDomaineDroitDoublon(declarationList);
    Assertions.assertEquals(1, declarationList.get(0).getDomaineDroits().size());
  }
}
