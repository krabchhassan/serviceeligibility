package com.cegedim.next.serviceeligibility.core.bdd.service.mapping;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.FormuleMetierDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl.MapperFormuleMetierImpl;
import com.cegedim.next.serviceeligibility.core.model.domain.FormuleMetier;
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
public class MapperFormuleMetierTest {

  @Autowired private MapperFormuleMetierImpl mapperFormuleMetier;

  private FormuleMetier formuleMetier;

  private FormuleMetierDto formuleMetierDto;

  @BeforeTestClass
  public static void init() {
    TomcatURLStreamHandlerFactory.getInstance();
  }

  @BeforeAll
  public void initTests() {
    prepareData();
  }

  private void prepareData() {
    formuleMetier = new FormuleMetier();
    formuleMetier.setLibelle("Libelle metier");
    formuleMetier.setCode("012345");

    formuleMetierDto = new FormuleMetierDto();
    formuleMetierDto.setLibelle("Libelle metier dto");
    formuleMetierDto.setCode("012346");
  }

  @Test
  void should_create_dto_from_entity() {
    final FormuleMetierDto formuleMetierDto =
        mapperFormuleMetier.entityToDto(formuleMetier, null, false, false, null);

    Assertions.assertNotNull(formuleMetierDto);
    Assertions.assertEquals(formuleMetierDto.getLibelle(), formuleMetier.getLibelle());
    Assertions.assertEquals(formuleMetierDto.getCode(), formuleMetier.getCode());
  }

  @Test
  void should_create_entity_from_dto() {
    final FormuleMetier formuleMetier = mapperFormuleMetier.dtoToEntity(formuleMetierDto);

    Assertions.assertNotNull(formuleMetier);
    Assertions.assertEquals(formuleMetier.getLibelle(), formuleMetierDto.getLibelle());
    Assertions.assertEquals(formuleMetier.getCode(), formuleMetierDto.getCode());
  }

  @Test
  void shouldBeTheSame() {
    Assertions.assertEquals(
        Integer.compare(Integer.parseInt("1"), Integer.parseInt("2")), "1".compareTo("2"));
  }
}
