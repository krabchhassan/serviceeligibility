package com.cegedim.next.serviceeligibility.core.bdd.service.mapping;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.FormuleDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.FormuleMetierDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.PrestationDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl.MapperPrestationImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.Formule;
import com.cegedim.next.serviceeligibility.core.model.domain.FormuleMetier;
import com.cegedim.next.serviceeligibility.core.model.domain.Prestation;
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
public class MapperPrestationTest {

  @Autowired private MapperPrestationImpl mapperPrestation;

  private Prestation prestation;

  private PrestationDto prestationDto;

  @BeforeTestClass
  public static void init() {
    TomcatURLStreamHandlerFactory.getInstance();
  }

  @BeforeAll
  public void initTests() {
    prepareData();
  }

  private void prepareData() {
    prestation = new Prestation();
    prestation.setLibelle("Libelle");
    prestation.setCode("0123456789");
    prestation.setFormule(new Formule());
    prestation.setDateEffet("2022/01/01");
    prestation.setCodeRegroupement("789");
    prestation.setFormuleMetier(new FormuleMetier());
    prestation.setIsEditionRisqueCarte(true);

    prestationDto = new PrestationDto();
    prestationDto.setLibelle("Libelle dto");
    prestationDto.setCode("0123456788");
    prestationDto.setFormule(new FormuleDto());
    prestationDto.setDateEffet(DateUtils.parseDate("2021/01/01", DateUtils.FORMATTERSLASHED));
    prestationDto.setCodeRegroupement("790");
    prestationDto.setFormuleMetier(new FormuleMetierDto());
    prestationDto.setIsEditionRisqueCarte(true);
  }

  @Test
  void should_create_dto_from_entity() {
    final PrestationDto prestationDto =
        mapperPrestation.entityToDto(
            prestation, TypeProfondeurRechercheService.AVEC_FORMULES, false, false, null);

    Assertions.assertNotNull(prestationDto);
    Assertions.assertEquals(prestationDto.getLibelle(), prestation.getLibelle());
    Assertions.assertEquals(prestationDto.getCode(), prestation.getCode());
    Assertions.assertEquals(
        prestationDto.getDateEffet(),
        DateUtils.parseDate(prestation.getDateEffet(), DateUtils.FORMATTERSLASHED));
    Assertions.assertEquals(prestationDto.getCodeRegroupement(), prestation.getCodeRegroupement());
    Assertions.assertTrue(prestation.getIsEditionRisqueCarte());
  }

  @Test
  void should_create_entity_from_dto() {
    final Prestation prestation = mapperPrestation.dtoToEntity(prestationDto);

    Assertions.assertNotNull(prestation);
    Assertions.assertEquals(prestation.getLibelle(), prestationDto.getLibelle());
    Assertions.assertEquals(prestation.getCode(), prestationDto.getCode());
    Assertions.assertEquals(
        DateUtils.parseDate(prestation.getDateEffet(), DateUtils.FORMATTERSLASHED),
        prestationDto.getDateEffet());
    Assertions.assertEquals(prestation.getCodeRegroupement(), prestationDto.getCodeRegroupement());
    Assertions.assertTrue(prestationDto.getIsEditionRisqueCarte());
  }

  @Test
  void shouldBeTheSame() {
    Assertions.assertEquals(
        Integer.compare(Integer.parseInt("1"), Integer.parseInt("2")), "1".compareTo("2"));
  }
}
