package com.cegedim.next.serviceeligibility.core.bdd.service.mapping;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ConventionnementDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.TypeConventionnementDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl.MapperConventionnementImpl;
import com.cegedim.next.serviceeligibility.core.model.domain.Conventionnement;
import com.cegedim.next.serviceeligibility.core.model.domain.TypeConventionnement;
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
public class MapperConventionnementTest {

  @Autowired private MapperConventionnementImpl mapperConventionnement;

  private Conventionnement conventionnement;

  private ConventionnementDto conventionnementDto;

  @BeforeTestClass
  public static void init() {
    TomcatURLStreamHandlerFactory.getInstance();
  }

  @BeforeAll
  public void initTests() {
    prepareData();
  }

  private void prepareData() {
    conventionnement = new Conventionnement();
    TypeConventionnement typeConventionnement = new TypeConventionnement();
    typeConventionnement.setCode("codeType");
    typeConventionnement.setLibelle("libelleType");
    conventionnement.setTypeConventionnement(typeConventionnement);
    conventionnement.setPriorite(1);

    conventionnementDto = new ConventionnementDto();
    TypeConventionnementDto typeConventionnementDto = new TypeConventionnementDto();
    typeConventionnementDto.setCode("codeType");
    typeConventionnementDto.setLibelle("libelleType");
    conventionnementDto.setTypeConventionnement(typeConventionnementDto);
    conventionnementDto.setPriorite(2);
  }

  @Test
  void should_create_dto_from_entity() {
    final ConventionnementDto conventionnementDto =
        mapperConventionnement.entityToDto(conventionnement, null, false, false, null);

    Assertions.assertNotNull(conventionnementDto);
    Assertions.assertEquals(
        conventionnementDto.getTypeConventionnement().getCode(),
        conventionnement.getTypeConventionnement().getCode());
    Assertions.assertEquals(
        conventionnementDto.getTypeConventionnement().getLibelle(),
        conventionnement.getTypeConventionnement().getLibelle());
    Assertions.assertEquals(conventionnementDto.getPriorite(), conventionnement.getPriorite());
  }

  @Test
  void should_create_entity_from_dto() {
    final Conventionnement conventionnement =
        mapperConventionnement.dtoToEntity(conventionnementDto);

    Assertions.assertNotNull(conventionnement);
    Assertions.assertEquals(
        conventionnement.getTypeConventionnement().getCode(),
        conventionnementDto.getTypeConventionnement().getCode());
    Assertions.assertEquals(
        conventionnement.getTypeConventionnement().getLibelle(),
        conventionnementDto.getTypeConventionnement().getLibelle());
    Assertions.assertEquals(conventionnement.getPriorite(), conventionnementDto.getPriorite());
  }

  @Test
  void shouldBeTheSame() {
    Assertions.assertEquals(
        Integer.compare(Integer.parseInt("1"), Integer.parseInt("2")), "1".compareTo("2"));
  }
}
