package com.cegedim.next.serviceeligibility.core.bdd.service.mapping;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.AffiliationDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl.MapperAffiliationImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperAffiliation;
import com.cegedim.next.serviceeligibility.core.model.domain.Affiliation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class MapperAffiliationImplTest {

  private MapperAffiliation mapper;

  private AffiliationDto dto;

  /** Methode permettant d'initialiser les tests. */
  @BeforeAll
  public void initTests() {
    mapper = new MapperAffiliationImpl();

    prepareData();
  }

  private void prepareData() {
    dto = new AffiliationDto();
    dto.setCaisseOD1("caisseOD1");
    dto.setCaisseOD2("caisseOD2");
    dto.setRegimeOD1("regimeOD1");
  }

  @Test
  void should_create_entity_from_dto() {
    final Affiliation entity = mapper.dtoToEntity(dto);

    Assertions.assertNotNull(entity);
    Assertions.assertEquals("caisseOD1", entity.getCaisseOD1());
    Assertions.assertEquals("caisseOD2", entity.getCaisseOD2());
    Assertions.assertEquals("regimeOD1", entity.getRegimeOD1());
  }
}
