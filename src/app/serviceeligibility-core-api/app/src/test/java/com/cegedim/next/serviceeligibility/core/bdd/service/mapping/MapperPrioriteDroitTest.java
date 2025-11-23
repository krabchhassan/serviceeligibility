package com.cegedim.next.serviceeligibility.core.bdd.service.mapping;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.PrioriteDroitDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl.MapperPrioriteDroitImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.PrioriteDroit;
import org.apache.catalina.webresources.TomcatURLStreamHandlerFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestClass;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfig.class})
@TestInstance(Lifecycle.PER_CLASS)
public class MapperPrioriteDroitTest {

  @Autowired private MapperPrioriteDroitImpl mapperPrioriteDroit;

  private PrioriteDroit prioriteDroit;

  private PrioriteDroitDto prioriteDroitDto;

  @BeforeTestClass
  public static void init() {
    TomcatURLStreamHandlerFactory.getInstance();
  }

  @BeforeAll
  public void initTests() {
    prepareData();
  }

  private void prepareData() {
    prioriteDroit = new PrioriteDroit();
    prioriteDroit.setLibelle("Libelle");
    prioriteDroit.setCode("0123456789");
    prioriteDroit.setPrioriteBO("2");
    prioriteDroit.setTypeDroit("type1");
    prioriteDroit.setPrioDroitNir1("1650431555715");
    prioriteDroit.setPrioDroitNir2("1650431555716");
    prioriteDroit.setNirPrio1("1650431555715");
    prioriteDroit.setNirPrio2("1650431555716");
    prioriteDroit.setPrioContratNir1("10");
    prioriteDroit.setPrioContratNir2("10");

    prioriteDroitDto = new PrioriteDroitDto();
    prioriteDroitDto.setLibelle("Libelle dto");
    prioriteDroitDto.setCode("0123456788");
    prioriteDroitDto.setPrioriteBO("1");
    prioriteDroitDto.setTypeDroit("type2");
    prioriteDroitDto.setPrioDroitNir1("1650431555712");
    prioriteDroitDto.setPrioDroitNir2("1650431555714");
    prioriteDroitDto.setNirPrio1("1650431555712");
    prioriteDroitDto.setNirPrio2("1650431555714");
    prioriteDroitDto.setPrioContratNir1("11");
    prioriteDroitDto.setPrioContratNir2("12");
  }

  @Test
  void should_create_dto_from_entity() {
    final PrioriteDroitDto prioriteDroitDto =
        mapperPrioriteDroit.entityToDto(
            prioriteDroit, TypeProfondeurRechercheService.AVEC_FORMULES, false, false, null);

    Assertions.assertNotNull(prioriteDroitDto);
    Assertions.assertEquals(prioriteDroitDto.getLibelle(), prioriteDroit.getLibelle());
    Assertions.assertEquals(prioriteDroitDto.getCode(), prioriteDroit.getCode());
    Assertions.assertEquals(prioriteDroitDto.getPrioriteBO(), prioriteDroit.getPrioriteBO());
    Assertions.assertEquals(prioriteDroitDto.getTypeDroit(), prioriteDroit.getTypeDroit());
    Assertions.assertEquals(prioriteDroitDto.getPrioDroitNir1(), prioriteDroit.getPrioDroitNir1());
    Assertions.assertEquals(prioriteDroitDto.getPrioDroitNir2(), prioriteDroit.getPrioDroitNir2());
    Assertions.assertEquals(prioriteDroitDto.getNirPrio1(), prioriteDroit.getNirPrio1());
    Assertions.assertEquals(prioriteDroitDto.getNirPrio2(), prioriteDroit.getNirPrio2());
    Assertions.assertEquals(
        prioriteDroitDto.getPrioContratNir1(), prioriteDroit.getPrioContratNir1());
    Assertions.assertEquals(
        prioriteDroitDto.getPrioContratNir2(), prioriteDroit.getPrioContratNir2());
  }

  @Test
  void should_create_entity_from_dto() {
    final PrioriteDroit prioriteDroit = mapperPrioriteDroit.dtoToEntity(prioriteDroitDto);

    Assertions.assertNotNull(prioriteDroit);
    Assertions.assertEquals(prioriteDroit.getLibelle(), prioriteDroitDto.getLibelle());
    Assertions.assertEquals(prioriteDroit.getCode(), prioriteDroitDto.getCode());
    Assertions.assertEquals(prioriteDroit.getPrioriteBO(), prioriteDroitDto.getPrioriteBO());
    Assertions.assertEquals(prioriteDroit.getTypeDroit(), prioriteDroitDto.getTypeDroit());
    Assertions.assertEquals(prioriteDroit.getPrioDroitNir1(), prioriteDroitDto.getPrioDroitNir1());
    Assertions.assertEquals(prioriteDroit.getPrioDroitNir2(), prioriteDroitDto.getPrioDroitNir2());
    Assertions.assertEquals(prioriteDroit.getNirPrio1(), prioriteDroitDto.getNirPrio1());
    Assertions.assertEquals(prioriteDroit.getNirPrio2(), prioriteDroitDto.getNirPrio2());
    Assertions.assertEquals(
        prioriteDroit.getPrioContratNir1(), prioriteDroitDto.getPrioContratNir1());
    Assertions.assertEquals(
        prioriteDroit.getPrioContratNir2(), prioriteDroitDto.getPrioContratNir2());
  }

  @Test
  void shouldBeTheSame() {
    Assertions.assertEquals(
        Integer.compare(Integer.parseInt("1"), Integer.parseInt("2")), "1".compareTo("2"));
  }
}
