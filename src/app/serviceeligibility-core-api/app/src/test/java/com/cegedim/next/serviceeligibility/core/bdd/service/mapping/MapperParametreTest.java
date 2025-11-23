package com.cegedim.next.serviceeligibility.core.bdd.service.mapping;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ParametreDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl.MapperParametreImpl;
import com.cegedim.next.serviceeligibility.core.model.domain.Parametre;
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
public class MapperParametreTest {

  @Autowired private MapperParametreImpl mapperParametre;

  private Parametre parametre;

  private ParametreDto parametreDto;

  @BeforeTestClass
  public static void init() {
    TomcatURLStreamHandlerFactory.getInstance();
  }

  @BeforeAll
  public void initTests() {
    prepareData();
  }

  private void prepareData() {
    parametre = new Parametre();
    parametre.setLibelle("Libelle");
    parametre.setNumero("0123456789");
    parametre.setValeur("val789");

    parametreDto = new ParametreDto();
    parametreDto.setLibelle("Libelle dto");
    parametreDto.setNumero("0123456788");
    parametreDto.setValeur("val788");
  }

  @Test
  void should_create_dto_from_entity() {
    final ParametreDto parametreDto =
        mapperParametre.entityToDto(parametre, null, false, false, null);

    Assertions.assertNotNull(parametreDto);
    Assertions.assertEquals(parametreDto.getLibelle(), parametre.getLibelle());
    Assertions.assertEquals(parametreDto.getNumero(), parametre.getNumero());
    Assertions.assertEquals(parametreDto.getValeur(), parametre.getValeur());
  }

  @Test
  void should_create_entity_from_dto() {
    final Parametre Parametre = mapperParametre.dtoToEntity(parametreDto);

    Assertions.assertNotNull(Parametre);
    Assertions.assertEquals(Parametre.getLibelle(), parametreDto.getLibelle());
    Assertions.assertEquals(Parametre.getNumero(), parametreDto.getNumero());
    Assertions.assertEquals(Parametre.getValeur(), parametreDto.getValeur());
  }

  @Test
  void shouldBeTheSame() {
    Assertions.assertEquals(
        Integer.compare(Integer.parseInt("1"), Integer.parseInt("2")), "1".compareTo("2"));
  }
}
