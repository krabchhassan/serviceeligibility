package com.cegedim.next.serviceeligibility.core.bdd.service.mapping;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarantDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl.MapperDeclarantImpl;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
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
public class MapperDeclarantTest {

  @Autowired private MapperDeclarantImpl mapperDeclarant;

  private Declarant declarant;

  private DeclarantDto declarantDto;

  @BeforeTestClass
  public static void init() {
    TomcatURLStreamHandlerFactory.getInstance();
  }

  @BeforeAll
  public void initTests() {
    prepareData();
  }

  private void prepareData() {
    declarant = new Declarant();
    declarant.setIdClientBO("bob@gmail.com");
    declarant.setUserModification("bob@gmail.com");
    declarant.setUserCreation("bob@gmail.com");
    declarant.set_id("0032165199");
    declarant.setLibelle("TEST BLUE");
    declarant.setNom("TEST BLUE");
    declarant.setSiret("123456789 00012");
    declarant.setCodePartenaire("TNR");
    declarant.setDateCreation(DateUtils.parseDate("2021/01/01", DateUtils.FORMATTERSLASHED));
    declarant.setDateModification(DateUtils.parseDate("2021/03/01", DateUtils.FORMATTERSLASHED));

    declarantDto = new DeclarantDto();
    declarantDto.setLibelle("TEST BLUES");
    declarantDto.setNom("TEST BLUs");
    declarantDto.setNumeroPrefectoral("0032165198");
    declarantDto.setSiret("123456789 00014");
    declarantDto.setCodePartenaire("TNT");
    declarantDto.setDateCreation(DateUtils.parseDate("2021/01/01", DateUtils.FORMATTERSLASHED));
    declarantDto.setUserCreation("bobby@gmail.com");
    declarantDto.setDateModification(DateUtils.parseDate("2021/03/01", DateUtils.FORMATTERSLASHED));
    declarantDto.setUserModification("bobby@gmail.com");
  }

  @Test
  void should_create_dto_from_entity() {
    final DeclarantDto declarantDto =
        mapperDeclarant.entityToDto(declarant, null, false, false, null);

    Assertions.assertNotNull(declarantDto);
    Assertions.assertEquals(declarantDto.getNumeroRNM(), declarant.get_id());
    Assertions.assertEquals(declarantDto.getLibelle(), declarant.getLibelle());
    Assertions.assertEquals(declarantDto.getNom(), declarant.getNom());
    Assertions.assertEquals(declarantDto.getNumeroPrefectoral(), declarant.get_id());
    Assertions.assertEquals(declarantDto.getSiret(), declarant.getSiret());
    Assertions.assertEquals(declarantDto.getCodePartenaire(), declarant.getCodePartenaire());
    Assertions.assertEquals(declarantDto.getDateCreation(), declarant.getDateCreation());
    Assertions.assertEquals(declarantDto.getUserCreation(), declarant.getUserCreation());
    Assertions.assertEquals(declarantDto.getDateModification(), declarant.getDateModification());
    Assertions.assertEquals(declarantDto.getUserModification(), declarant.getUserModification());
  }

  @Test
  void should_create_entity_from_dto() {
    final Declarant declarant = mapperDeclarant.dtoToEntity(declarantDto);

    Assertions.assertNotNull(declarant);
    Assertions.assertEquals(declarant.getLibelle(), declarantDto.getLibelle());
    Assertions.assertEquals(declarant.getNom(), declarantDto.getNom());
    Assertions.assertEquals(declarant.getNumeroPrefectoral(), declarantDto.getNumeroPrefectoral());
    Assertions.assertEquals(declarant.getSiret(), declarantDto.getSiret());
    Assertions.assertEquals(declarant.getCodePartenaire(), declarantDto.getCodePartenaire());
    Assertions.assertEquals(declarant.getDateCreation(), declarantDto.getDateCreation());
    Assertions.assertEquals(declarant.getUserCreation(), declarantDto.getUserCreation());
    Assertions.assertEquals(declarant.getDateModification(), declarantDto.getDateModification());
    Assertions.assertEquals(declarant.getUserModification(), declarantDto.getUserModification());
  }

  @Test
  void shouldBeTheSame() {
    Assertions.assertEquals(
        Integer.compare(Integer.parseInt("1"), Integer.parseInt("2")), "1".compareTo("2"));
  }
}
