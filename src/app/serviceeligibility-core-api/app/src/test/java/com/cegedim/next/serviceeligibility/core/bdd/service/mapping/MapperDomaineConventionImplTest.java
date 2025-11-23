package com.cegedim.next.serviceeligibility.core.bdd.service.mapping;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat.ConventionDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat.DomaineConventionDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl.cartedemat.MapperDomaineConventionImpl;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.Convention;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.DomaineConvention;
import java.util.ArrayList;
import java.util.List;
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
class MapperDomaineConventionImplTest {

  private static final String CONV_DTO2_TYPE = "convDto2Type";

  private static final String CONV_DTO1_TYPE = "convDto1Type";

  private static final String CODE_DOMAINE = "codeDomaine";

  @Autowired private MapperDomaineConventionImpl mapper;

  private DomaineConventionDto dto;
  private DomaineConvention entity;

  @BeforeTestClass
  public static void init() {
    TomcatURLStreamHandlerFactory.getInstance();
  }

  @BeforeAll
  public void initTests() {
    prepareData();
  }

  private void prepareData() {
    // prepare Dto
    dto = new DomaineConventionDto();
    dto.setCodeDomaine(CODE_DOMAINE);
    dto.setRang(1);

    ConventionDto convDto1 = new ConventionDto();
    convDto1.setTypeConventionnement(CONV_DTO1_TYPE);
    convDto1.setPrioriteConventionnement(25);

    ConventionDto convDto2 = new ConventionDto();
    convDto2.setTypeConventionnement(CONV_DTO2_TYPE);
    convDto2.setPrioriteConventionnement(2);
    List<ConventionDto> conventionsDto1 = new ArrayList<>();
    conventionsDto1.add(convDto1);
    conventionsDto1.add(convDto2);
    dto.setConventionDtos(conventionsDto1);

    // prepare entity

    entity = new DomaineConvention();
    entity.setCode(CODE_DOMAINE);
    entity.setRang(1);

    Convention conv1 = new Convention();
    conv1.setCode(CONV_DTO1_TYPE);
    conv1.setPriorite(25);

    Convention conv2 = new Convention();
    conv2.setCode(CONV_DTO2_TYPE);
    conv2.setPriorite(25);

    List<Convention> conventions = new ArrayList<>();
    conventions.add(conv1);
    conventions.add(conv2);
    entity.setConventions(conventions);
  }

  @Test
  void should_create_entity_from_dto() {
    final DomaineConvention resultEntity = mapper.dtoToEntity(dto);
    Assertions.assertEquals(CODE_DOMAINE, resultEntity.getCode());
    Assertions.assertEquals(1, (int) resultEntity.getRang());
    Assertions.assertEquals(2, resultEntity.getConventions().size());
  }

  @Test
  void should_create_dto_from_entity() {
    final DomaineConventionDto resultEntity = mapper.entityToDto(entity, null, false, false, null);
    Assertions.assertEquals(CODE_DOMAINE, resultEntity.getCodeDomaine());
    Assertions.assertEquals(1, (int) resultEntity.getRang());
    Assertions.assertEquals(2, resultEntity.getConventionDtos().size());
  }
}
