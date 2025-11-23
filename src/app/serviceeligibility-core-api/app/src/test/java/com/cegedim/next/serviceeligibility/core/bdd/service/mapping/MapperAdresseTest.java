package com.cegedim.next.serviceeligibility.core.bdd.service.mapping;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.AdresseDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.TypeAdresseDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl.MapperAdresseImpl;
import com.cegedim.next.serviceeligibility.core.model.domain.Adresse;
import com.cegedim.next.serviceeligibility.core.model.domain.TypeAdresse;
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
public class MapperAdresseTest {

  @Autowired private MapperAdresseImpl mapperAdresse;

  private Adresse a1;
  private TypeAdresse ta;

  private AdresseDto aDto;
  private TypeAdresseDto atDto;

  @BeforeTestClass
  public static void init() {
    TomcatURLStreamHandlerFactory.getInstance();
  }

  @BeforeAll
  public void initTests() {
    prepareData();
  }

  private void prepareData() {
    a1 = new Adresse();
    a1.setCodePostal("123");
    a1.setEmail("bob@gmail.com");
    a1.setTelephone("0666666666");
    a1.setLigne1("line 1");
    a1.setLigne2("line 2");
    a1.setLigne3("line 3");
    a1.setLigne4("line 4");
    a1.setLigne5("line 5");
    a1.setLigne6("line 6");
    a1.setLigne7("line 7");
    a1.setPays("France");
    ta = new TypeAdresse();
    ta.setLibelle("type label");
    ta.setType("new type");
    a1.setTypeAdresse(ta);

    aDto = new AdresseDto();
    aDto.setCodePostal("1232");
    aDto.setEmail("bob@gmail2.com");
    aDto.setTelephone("0666666666");
    aDto.setLigne1("line 12");
    aDto.setLigne2("line 2");
    aDto.setLigne3("line 3");
    aDto.setLigne4("line 4");
    aDto.setLigne5("line 5");
    aDto.setLigne6("line 6");
    aDto.setLigne7("line 7");
    aDto.setPays("France");
    atDto = new TypeAdresseDto();
    atDto.setLibelle("type label2");
    atDto.setType("new type2");
    aDto.setTypeAdresseDto(atDto);
  }

  @Test
  void should_create_dto_from_entity() {
    final AdresseDto adresseDto = mapperAdresse.entityToDto(a1, null, false, false, null);

    Assertions.assertNotNull(adresseDto);
    Assertions.assertEquals(adresseDto.getEmail(), a1.getEmail());
    Assertions.assertEquals(adresseDto.getCodePostal(), a1.getCodePostal());
    Assertions.assertEquals(adresseDto.getLigne1(), a1.getLigne1());
    Assertions.assertEquals(adresseDto.getLigne2(), a1.getLigne2());
    Assertions.assertEquals(adresseDto.getLigne3(), a1.getLigne3());
    Assertions.assertEquals(adresseDto.getLigne4(), a1.getLigne4());
    Assertions.assertEquals(adresseDto.getLigne5(), a1.getLigne5());
    Assertions.assertEquals(adresseDto.getLigne6(), a1.getLigne6());
    Assertions.assertEquals(adresseDto.getLigne7(), a1.getLigne7());
    Assertions.assertEquals(adresseDto.getTelephone(), a1.getTelephone());
    Assertions.assertEquals(adresseDto.getPays(), a1.getPays());
    Assertions.assertEquals(adresseDto.getTypeAdresseDto().getLibelle(), ta.getLibelle());
  }

  @Test
  void should_create_entity_from_dto() {
    final Adresse adresse = mapperAdresse.dtoToEntity(aDto);

    Assertions.assertNotNull(adresse);
    Assertions.assertEquals(adresse.getEmail(), aDto.getEmail());
    Assertions.assertEquals(adresse.getCodePostal(), aDto.getCodePostal());
    Assertions.assertEquals(adresse.getLigne1(), aDto.getLigne1());
    Assertions.assertEquals(adresse.getLigne2(), aDto.getLigne2());
    Assertions.assertEquals(adresse.getLigne3(), aDto.getLigne3());
    Assertions.assertEquals(adresse.getLigne4(), aDto.getLigne4());
    Assertions.assertEquals(adresse.getLigne5(), aDto.getLigne5());
    Assertions.assertEquals(adresse.getLigne6(), aDto.getLigne6());
    Assertions.assertEquals(adresse.getLigne7(), aDto.getLigne7());
    Assertions.assertEquals(adresse.getTelephone(), aDto.getTelephone());
    Assertions.assertEquals(adresse.getPays(), aDto.getPays());
    Assertions.assertEquals(adresse.getTypeAdresse().getLibelle(), atDto.getLibelle());
  }

  @Test
  void shouldBeTheSame() {
    Assertions.assertEquals(
        Integer.compare(Integer.parseInt("1"), Integer.parseInt("2")), "1".compareTo("2"));
  }
}
