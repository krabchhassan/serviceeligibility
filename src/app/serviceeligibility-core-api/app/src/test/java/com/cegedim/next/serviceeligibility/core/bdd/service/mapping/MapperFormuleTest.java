package com.cegedim.next.serviceeligibility.core.bdd.service.mapping;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.FormuleDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl.MapperFormuleImpl;
import com.cegedim.next.serviceeligibility.core.model.domain.Formule;
import java.util.ArrayList;
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
public class MapperFormuleTest {

  @Autowired private MapperFormuleImpl mapperFormule;

  private Formule formule;

  private FormuleDto formuleDto;

  @BeforeTestClass
  public static void init() {
    TomcatURLStreamHandlerFactory.getInstance();
  }

  @BeforeAll
  public void initTests() {
    prepareData();
  }

  private void prepareData() {
    formule = new Formule();
    formule.setLibelle("Libelle");
    formule.setNumero("0123456789");
    formule.setParametres(new ArrayList<>());

    formuleDto = new FormuleDto();
    formuleDto.setLibelle("Libelle dto");
    formuleDto.setNumero("0123456788");
    formuleDto.setParametres(new ArrayList<>());
  }

  @Test
  void should_create_dto_from_entity() {
    final FormuleDto formuleDto = mapperFormule.entityToDto(formule, null, false, false, null);

    Assertions.assertNotNull(formuleDto);
    Assertions.assertEquals(formuleDto.getLibelle(), formule.getLibelle());
    Assertions.assertEquals(formuleDto.getNumero(), formule.getNumero());
  }

  @Test
  void should_create_entity_from_dto() {
    final Formule formule = mapperFormule.dtoToEntity(formuleDto);

    Assertions.assertNotNull(formule);
    Assertions.assertEquals(formule.getLibelle(), formuleDto.getLibelle());
    Assertions.assertEquals(formule.getNumero(), formuleDto.getNumero());
  }

  @Test
  void shouldBeTheSame() {
    Assertions.assertEquals(
        Integer.compare(Integer.parseInt("1"), Integer.parseInt("2")), "1".compareTo("2"));
  }
}
